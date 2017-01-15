/**
 * 
 */
package com.jambo.jop.common.businesslog;

import com.jambo.jop.common.commoncontrol.CommonBO;
import com.jambo.jop.common.spring.AbstractAdvisor;
import com.jambo.jop.common.utils.lang.PublicUtils;
import com.jambo.jop.infrastructure.control.AbstractBO;
import com.jambo.jop.infrastructure.control.BOFactory;
import com.jambo.jop.infrastructure.db.BaseVO;
import com.jambo.jop.infrastructure.db.DBAccessUser;
import com.jambo.jop.infrastructure.db.hibernate3.Hibernate3RouterMap;
import com.jambo.jop.infrastructure.sysadmin.BusinessLog;
import com.jambo.jop.infrastructure.sysadmin.BusinessLogException;
import com.jambo.jop.infrastructure.sysadmin.BusinessRepointLog;
import com.jambo.jop.infrastructure.sysadmin.ManageLog;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * 业务日志拦截器
 * 2013-6-18 jinbo 使用commondbtable对VO进行公共库识别，是公共库则使用commonuser
 * @author hbm,jinbo
 * 
 */
public class BusinessLogAdvisor extends AbstractAdvisor {

	private static Logger log = LoggerFactory.getLogger(BusinessLogAdvisor.class);

	private static StringBuffer methods;
	private static final String notBlank = "\\S*"; //非空字符,等同doCreate*的*
	private static final String or = "|";
	
	private static final String METHOD_CREATE = "doCreate";
	private static final String METHOD_UPDATE = "doUpdate";
	private static final String METHOD_REMOVE = "doRemove";
	private static final String METHOD_REMOVEBYVO = "doRemoveByVO";
	private static final String METHOD_REMOVEBYPK = "doRemoveByPK";
	
	private static Pattern pattern;
	
	static {
		methods = new StringBuffer("");
		//正则表达式最后一个不能加|,如果需要方法验证则加在最前面
		methods.append("(").append(METHOD_CREATE).append(notBlank).append(")").append(or);
		methods.append("(").append(METHOD_UPDATE).append(notBlank).append(")").append(or);
		methods.append("(").append(METHOD_REMOVE).append(notBlank).append(")").append(or);
		methods.append("(").append(METHOD_REMOVEBYVO).append(notBlank).append(")").append(or);
		methods.append("(").append(METHOD_REMOVEBYPK).append(notBlank).append(")");
		pattern = Pattern.compile(methods.toString());
	}

	private boolean checkArgs(Method method, Object[] args) {
		boolean requireLog = false;

		if (pattern.matcher(method.getName()).find())
			requireLog = true;

		if (args == null || args.length == 0) {
			throw new BusinessLogException("参数丢失！ " + method.getName());
		}

		if (args[0] == null)
			throw new BusinessLogException("第一个参数VO 丢失！ " + method.getName());

		if (METHOD_REMOVEBYPK.equals(method.getName())) {
			if (!(args[0] instanceof Serializable)) {
				throw new BusinessLogException("第一个参数VO 必须实现 Serializable 接口！ "
						+ method.getName());
			}

			String voClassName = StringUtils.replace(method.getDeclaringClass()
					.getName(), ".control.", ".business.");
			voClassName = StringUtils.replace(voClassName, "BO", "VO");

			try {
				Class cls = Class.forName(voClassName);
				Object obj = cls.newInstance();
				if (obj instanceof BusinessLog)
					requireLog = true;
				else
					requireLog = false;
			} catch (Exception e) {
				throw new BusinessLogException(e.getMessage(), e);
			}

		} else {
			if (!(args[0] instanceof BaseVO))
				throw new BusinessLogException("第一个参数VO 必须是BaseVO的子类！ "
						+ method.getName());

			if (!((args[0] instanceof ManageLog) || (args[0] instanceof BusinessLog)))
				requireLog = false;
		}

		return requireLog;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object result = null;

		Object object = invocation.getThis();
		Method method = invocation.getMethod();
		Object[] args = invocation.getArguments();
		Object target = invocation.getStaticPart();

		boolean requireLog = false; // 检查是否需要记录日志
		try {
			requireLog = checkArgs(method, args);
		} catch (BusinessLogException e) {
			if (log.isErrorEnabled())
				log.error("记录业务日志的条件不符合规范,但业务操作仍会继续，日志将不会记录:" + e.getMessage());
		}

		if (requireLog == false) { // 不需要或无法记录日志的情况，业务操作仍会继续
			try {
				result = invocation.proceed();
				return result;
			} catch (Exception e) {
				throw e;
			}
		}
		// 符合条件的情况，需要记录业务日志
		boolean sucess = false;
		try {
			if ((method.getName().equals(METHOD_REMOVEBYPK))
					|| (method.getName().equals(METHOD_REMOVEBYVO))) {

				AbstractBO control = (AbstractBO) object;
				DBAccessUser user = control.getUser();

				int objectCount = countObjectToRemove(object, method, args,
						user);
				if (objectCount > 0) { // 有要删除的数据， 继续
					try {
						doLog(object, method, args, target);
					} finally {
						result = invocation.proceed();
						sucess = true;
					}

				} else {
					if (log.isDebugEnabled())
						log.debug("没有要删除的数据, 不记录业务日志:" + object + ",方法:"
								+ method.getName());
					return null; // 否则，直接返回
				}

			} else {
				result = invocation.proceed();
				sucess = true;
				doLog(object, method, args, target);
			}

			if (log.isDebugEnabled())
				log.debug("记录成功业务日志:" + object + ",方法:" + method.getName());
			// 记录成功
		} catch (Exception e) {
			// 记录失败

			if (!(e instanceof BusinessLogException)) {
				String failcause = null;
				if (e.getMessage() != null) {
					int len = e.getMessage().length();
					failcause = len > 256 ? e.getMessage().substring(len - 256)
							: e.getMessage();
				}

				try {
					doLog(object, method, args, target);
					if (log.isDebugEnabled())
						log.debug("记录失败业务日志:" + object + ",方法:"
								+ method.getName() + ",原因:" + e.getMessage());

				} catch (BusinessLogException e2) {
					if (log.isWarnEnabled()) {
						log.warn("业务操作失败，并且无法记录业务日志:" + e2.getMessage()
								+ object + ",方法:" + method.getName());
					}
				}
				throw e;
			} else {
				if (log.isWarnEnabled()) {
					if (sucess)
						log.warn("业务操作成功，但无法记录业务日志:" + e.getMessage() + ",对象:"
								+ object + ",方法:" + method.getName(), e);
					else
						log.warn("业务操作失败，并且无法记录业务日志:" + e.getMessage() + ",对象:"
								+ object + ",方法:" + method.getName(), e);
				}

			}
		}
		return result;
	}

	private int countObjectToRemove(Object object, Method method,
			Object[] args, DBAccessUser user) {
		try {
			Serializable pk = (Serializable) args[0];
			String voClassName = object.getClass().getName();
			voClassName = StringUtils.replace(voClassName, ".control.",
					".business."); // 替换包名
			voClassName = StringUtils.replace(voClassName, "BO", "VO"); // 替换为VO名

			CommonBO commonControl = (CommonBO) BOFactory.build(
					CommonBO.class, Hibernate3RouterMap.checkVOForUser(voClassName, user));
			commonControl.setVoClass(Class.forName(voClassName));
			BaseVO baseVO = (BaseVO) commonControl.findByPk(pk); // 为提高效率，以后改为
			// count方法
			if (baseVO != null)
				return 1;
			else
				return 0;

		} catch (Throwable t) {
			throw new BusinessLogException(t.getMessage(), t);
		}

	}

	private void doLog(Object object, Method method, Object[] args,
			Object target) throws BusinessLogException {
		try {
			AbstractBO control = (AbstractBO) object;
			DBAccessUser user = control.getUser();
			Serializable logObject = (Serializable) args[0];
			if (logObject instanceof ManageLog) { // 记录管理日志
				manageLog(method, logObject, user);
			} else { // 记录业务日志
				businessLog(method, logObject, user);
			}
		} catch (Throwable t) {
			throw new BusinessLogException(t.getMessage(), t);
		}

	}

	private void manageLog(Method method, Serializable logObject,
			DBAccessUser user) throws Exception {
		// Managelog control = (Managelog) BOFactory.build(ManagelogBO.class,
		// user);
		// ManagelogVO logvo = new ManagelogVO();
		//				
		// logvo.setOprtime(new Date());
		// logvo.setOprcode(user.getOprcode());
		// logvo.setIp(user.getIp());
		// logvo.setOprstate( oprstate );
		// logvo.setOpraction( getOprAction(method.getName()));
		// //失败原因
		// if(oprstate.byteValue() == BusinessLog.STATE_FAIL.byteValue() &&
		// failcause!=null)
		// logvo.setFailcause(failcause);
		//				
		// if( logObject instanceof BaseVO) { //记录对象为VO
		// BaseVO baseVO = (BaseVO)logObject;
		// String shortName =
		// ClassUtils.getShortClassName(logObject.getClass().getName());
		// shortName = StringUtils.remove(shortName, "VO");
		// logvo.setOprtype( StringUtils.upperCase(shortName) );
		// ToStringStyle style =new ManageLogToStringStyle();
		// logvo.setOprdata( ReflectionToStringBuilder.toString(baseVO, style));
		//					
		// control.doRecordLog(logvo); //
		// 注意：必须使用ManageLogControl.doRecordLog来记录业务日志, 使用
		// ManageLogControl.doCreate 方法，会被重复拦截记录管理日志
		//					
		// }else if(METHOD_REMOVEBYPK.equals(method.getName())){ //
		// 记录对象为Serializable, 例如：doRemoveByPK 方法的参数PK
		//					
		// String voClassName = StringUtils.replace(
		// method.getDeclaringClass().getName(), ".control.", ".persistent.");
		// //替换包名
		// voClassName = StringUtils.replace(voClassName, "ControlBean", "VO");
		// //替换为VO名
		// log.info("voClassName " + voClassName);
		// voClassName = StringUtils.replace(voClassName, "Control", "VO");
		// //替换为VO名
		// String shortName = ClassUtils.getShortClassName(voClassName);
		// shortName = StringUtils.remove(shortName, "VO");
		// logvo.setOprtype(shortName);
		// logvo.setOprdata("PK:" + logObject);
		//					
		// control.doRecordLog(logvo); //
		// 注意：必须使用ManageLogControl.doRecordLog来记录业务日志, 使用
		// ManageLogControl.doCreate 方法，会被重复拦截记录管理日志
		// }
		throw new BusinessLogException("ManageLog接口暂未实现   " + method.getName());
	}

	protected void businessLog(Method method, Serializable baseVO,
			DBAccessUser user) {
		try {
			if (baseVO instanceof BusinessLog) {
				BusinessLog businessLog = (BusinessLog) baseVO;
				Class logVOClass = businessLog.logVOClass();
				Object logVO = logVOClass.newInstance();
				String methodname = method.getName();
				
				String[] properties = BusinessRepointLog.logProperties;
				if(businessLog instanceof BusinessRepointLog){
					properties = ((BusinessRepointLog)businessLog).repointLogProperites();
					if(properties == null || properties.length != 5){
						throw new BusinessLogException("VO " + baseVO
								+ " 未设定 repointLogProperites 值或者指定属性个数不等于5个，不能记录业务日志 ");
					}
				}
				logVO = setLogProperties(logVO, methodname, user, properties);
//				BeanUtils.copyProperties(logVO, baseVO);
                BeanCopier beanCopier = BeanCopier.create(BaseVO.class, logVOClass, false);
                beanCopier.copy(baseVO, logVO, null);

                BusinessLogControl control = (BusinessLogControl) BOFactory
						.build(BusinessLogBO.class, Hibernate3RouterMap.checkVOForUser(logVOClass.getName(), user));
				String voClassName = logVOClass.getName();
				control.setVoClass(Class.forName(voClassName));
				control.doLogCreate(logVO);
			} else if (METHOD_REMOVEBYPK.equals(method.getName())) {//doRemoveByPK传入的是主键值
				String voClassName = StringUtils.replace(method
                        .getDeclaringClass().getName(), ".control.",
                        ".business.");
				voClassName = StringUtils.replace(voClassName, "BO", "VO");
				CommonBO commonControl = (CommonBO) BOFactory.build(
						CommonBO.class, Hibernate3RouterMap.checkVOForUser(voClassName, user));
				commonControl.setVoClass(Class.forName(voClassName));
				Object baseVO2 = commonControl.findByPk(baseVO);
				BusinessLog businessLog = (BusinessLog) baseVO2;
				if (businessLog.logVOClass() == null) {
					throw new BusinessLogException("VO " + baseVO2
							+ " 未设定 logVOClass 值，不能记录业务日志 ");
				}
				Class logVOClass = businessLog.logVOClass();
				Object logVO = logVOClass.newInstance();
				String methodname = method.getName();
				
				String[] properties = BusinessRepointLog.logProperties;
				if(businessLog instanceof BusinessRepointLog){
					properties = ((BusinessRepointLog)businessLog).repointLogProperites();
					if(properties == null || properties.length != 5){
						throw new BusinessLogException("VO " + baseVO2
								+ " 未设定 repointLogProperites 值或者指定属性个数必须等于5个,如果表里面没有该属性则赋值为null,不能记录业务日志 ");
					}
				}
				Object logVO2 = setLogProperties(logVO, methodname, user, properties);
//				BeanUtils.copyProperties(logVO2, baseVO2);
                BeanCopier beanCopier = BeanCopier.create(BaseVO.class, logVOClass, false);
                beanCopier.copy(baseVO2, logVO2, null);

				BusinessLogControl control = (BusinessLogControl) BOFactory
						.build(BusinessLogBO.class, Hibernate3RouterMap.checkVOForUser(logVOClass.getName(), user));
				String logvoClassName = logVOClass.getName();
				control.setVoClass(Class.forName(logvoClassName));
				control.doLogCreate(logVO2);
			}
		} catch (Throwable t) {
			throw new BusinessLogException(t.getMessage(), t);
		}
	}

	/**
	 * 对日志的公共属性进行设置
	 * @param properties 
	 */
	protected Object setLogProperties(Object obj, String methodName,
			DBAccessUser user, String[] properties) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Serializable s = (Serializable) obj;
		PropertyDescriptor optimePty = PropertyUtils.getPropertyDescriptor(s,
				properties[BusinessRepointLog.optime]);
		java.util.Date optimeValue = new java.util.Date();
		String optimeString = PublicUtils.formatUtilDate(optimeValue,
				"yyyy-MM-dd HH:mm:ss");
		if (optimePty != null) {
			Class optimeType = optimePty.getPropertyType();
			if (java.sql.Date.class.isAssignableFrom(optimeType)) {
				optimeValue = new java.sql.Date(System.currentTimeMillis());
				optimeString = PublicUtils.formatUtilDate(optimeValue,
						"yyyy-MM-dd");
			} else if (java.sql.Timestamp.class.isAssignableFrom(optimeType)) {
				optimeValue = new java.sql.Timestamp(System.currentTimeMillis());
				optimeString = PublicUtils.formatUtilDate(optimeValue,
						"yyyy-MM-dd HH:mm:ss");
			}
		}
		String oprtype = getOprtype(methodName);
		if(StringUtils.isNotEmpty(properties[BusinessRepointLog.optime]))

			BeanUtils.setProperty(s, properties[BusinessRepointLog.optime], optimeString);
		if(StringUtils.isNotEmpty(properties[BusinessRepointLog.oprcode]))
			BeanUtils.setProperty(s, properties[BusinessRepointLog.oprcode], user.getOprcode());
		if(StringUtils.isNotEmpty(properties[BusinessRepointLog.oprtype]))
			BeanUtils.setProperty(s, properties[BusinessRepointLog.oprtype], oprtype);
		if(StringUtils.isNotEmpty(properties[BusinessRepointLog.success]))
			BeanUtils.setProperty(s, properties[BusinessRepointLog.success], new String("success"));
		
		return s;
	}

	private String getOprtype(String methodName) {
		if (methodName.startsWith(METHOD_CREATE)) {
			return new String("create");
		} else if (methodName.startsWith(METHOD_UPDATE)) {
			return new String("update");
		} else if (methodName.startsWith(METHOD_REMOVE)) { // doRemove*
			return new String("remove");
		} else
			return new String("unknown");
	}

}
