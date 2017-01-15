package com.jambo.jop.infrastructure.component.impl;

import com.jambo.jop.business.base.dictitem.IDictitemVO;
import com.jambo.jop.business.system.config.code2name.control.Code2NameBO;
import com.jambo.jop.common.utils.lang.InterfaceUtils;
import com.jambo.jop.common.utils.lang.PublicUtils;
import com.jambo.jop.infrastructure.component.Code2Name;
import com.jambo.jop.infrastructure.config.CoreConfigInfo;
import com.jambo.jop.infrastructure.control.BOFactory;
import com.jambo.jop.infrastructure.db.DBAccessUser;
import com.jambo.jop.infrastructure.db.DBQueryParam;
import com.jambo.jop.infrastructure.db.DataPackage;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * 需要先读取code2name 的配置， 具体需要区分数据字典，动态VO，和固定参数配置三种情况
 * 三者需要在 echcache-config.xml 配置三个缓存管理器，它们的时间控制策略是不通的
 * 数据字典 有效时间6小时
 * 动态VO    采用线程级缓存
 * 固定参数   永久缓存
*/
public class DefaultCode2Name implements Code2Name {
	
	private static Logger log = LoggerFactory.getLogger("com.jambo.jop.code2name");
	
	public DefaultCode2Name() {	 	
	}
	
	public DBAccessUser getUser(String dbFlag) throws Exception{
		DBAccessUser user = new DBAccessUser();
		user.setDbFlag(dbFlag);
		return user;
	}
	
	public String checkDBFlag(String dbFlag) {
		if (dbFlag == null){
			return CoreConfigInfo.COMMON_DB_NAME;
		} else { 
			return dbFlag;
		}
	}
	
	/**
	 * 编码转化为名称
	 * @param definition  code2name.xml 中项目的名称
	 * @param codeName
	 * @param nameName
	 * @param codeValue
	 * @return 名称
	 */
	public String code2Name(String def, String codeValue,String dbFlag) {
		dbFlag = checkDBFlag(dbFlag);
		
		if(codeValue == null || "".equals(codeValue.trim())) return "";
		String definition = formateDefinition(def);	
		
		if(codeValue.indexOf("CODE:") != -1 || codeValue.indexOf("code:") != -1){
			codeValue = codeValue.substring(5);
		}
		try{
			if (def.startsWith("#")) {  
				//动态数据，根据VO查询 //Code2NameConfiger.sysMap.containsKey(definition)
				// 如果是数据字典参数， 需要在 code2name.xml 中配置字典VO
				Node node = (Node) Code2NameConfiger.sysMap.get(definition);
				
				if(node == null)
					throw new NullPointerException("Can't find code2name definition " + def);
				Code2NameBO code2NameControl = (Code2NameBO) BOFactory.build(Code2NameBO.class,getUser(dbFlag)) ;
				//2011-10-15 jinbo 修改为toString(),避免转换为其它类型时,出现异常
	            String value = code2NameControl.doTranslateCode(node.getValueObject(), node
	    					.getCode(), node.getName(), codeValue, dbFlag).toString();
	           
	            //2011-10-20 jinbo 避免value是null时,直接显示null在界面上
	            value = checkNameValue(codeValue, value);
	            if(log.isDebugEnabled())
	            	log.debug("[code2name dictitem]#code:" + codeValue +",value:" + value + ",dbFlag:" + dbFlag);
	           return value;
	           
			}else if(def.startsWith("$") ) {
				
				Node node = (Node) Code2NameConfiger.sysMap.get("$");
				if(node == null)
					throw new NullPointerException("Can't find code2name definition " + def);
				
				Code2NameBO code2NameControl = (Code2NameBO) BOFactory.build(Code2NameBO.class,getUser(dbFlag)) ;		
				
	            String value = (String)code2NameControl.doTranslateCode(node.getValueObject(),definition, node
	    					.getCode(), node.getName(), codeValue, dbFlag);
	            
	            //2011-10-20 jinbo 避免value是null时,直接显示null在界面上
	            value = checkNameValue(codeValue, value);
	            if(log.isDebugEnabled())
	            	log.debug("[code2name table]$code:" + codeValue +",value:" + value + ",dbFlag:" + dbFlag);
	            return value;
				
			}else if(def.startsWith("@") ) {
				Class<PersonalizeInterface> implClass = (Class<PersonalizeInterface>)Class.forName(definition);
				PersonalizeInterface pi = implClass.newInstance();
				
	            //2011-10-20 jinbo 避免value是null时,直接显示null在界面上
				String value = pi.code2name(codeValue,dbFlag);
	            value = checkNameValue(codeValue, value);
				return value;

			}else if( Code2NameConfiger.localMap.containsKey(definition) ){  //配置的固定参数		
				LocalNote localcode = (LocalNote) Code2NameConfiger.localMap.get(definition);
				
				if(localcode == null)
					throw new NullPointerException("Can't find code2name definition " + def);
				
				String value = localcode.getValue(codeValue.toString());
				if(log.isDebugEnabled())
	            	log.debug("[code2name configed]$code:" + codeValue +",value:" + value + ",dbFlag:" + dbFlag);
				
				return value;
			}else{
				if(log.isErrorEnabled())
					log.error("未知的 code2name definition " + definition);
				return codeValue;
			}
		}catch (Exception e) {
			if(log.isErrorEnabled()) 
				log.error("编码转化时发生错误! " + e.getMessage(),e);
			return codeValue;
		}
	}
	
	/**
	 * 获取某个字典项或数据的所有可用编码名称
	 * @param definition
	 * @param dbFlag
	 */
	public Map valueList(String definition, String dbFlag) {
		return valueList(definition,null, dbFlag);
	}
	
	public Map valueList(String def, String condition, String dbFlag) {	
		return valueList(def, condition, null,dbFlag);
	}
	
	public Map valueList(String def, String condition,DBQueryParam param, String dbFlag) {
		dbFlag = checkDBFlag(dbFlag);

		String definition = formateDefinition(def);	
		Map values = null;
		try{
			if (def.startsWith("#") || def.startsWith("$")) {   
				//动态数据，根据VO查询 //Code2NameConfiger.sysMap.containsKey(definition)
				// 如果是数据字典参数， 需要在 code2name.xml 中配置字典VO
				//throw new IllegalArgumentException("单表型编码列表查询,需要使用 pickerDialog组件，请使用 valueListPackage 方法：" + def);
				DataPackage dp = valueListPackage(def, condition, param, dbFlag);

				Map maplist = new LinkedHashMap(dp.getDatas().size());
//				
//				//将数据转换为 map
				for(int i = 0 ; i < dp.getDatas().size() ; i++ ) {
					Node node = (Node) dp.getDatas().get(i);
					
		            //2011-10-20 jinbo 避免value是null时,直接显示null在界面上
					node.setName(checkNameValue(node.getCode(), node.getName()));

					maplist.put(node.getCode(), node.getName());
				}
				
				return maplist;
				
			}else	if( Code2NameConfiger.localMap.containsKey(definition) ){  //配置的固定参数		
				LocalNote localcode = (LocalNote) Code2NameConfiger.localMap.get(definition);
				values = localcode.getItems();
				//添加固定参数的过滤,只支持用CODE:?,?,?这种形式
				if(!StringUtils.isEmpty(condition)){
					Map conditionMap = new LinkedMap();
					String code = condition;
					if(condition.indexOf(";") != -1){
						code = condition.substring(condition.indexOf("CODE"), condition.indexOf(";"));
					}
					String[] codeArray = StringUtils.split(code, ":");
					String[] codeValues = StringUtils.split(codeArray[1],",");
					for(int i=0;i<codeValues.length;i++){
						if(!StringUtils.isEmpty(values.get(codeValues[i]).toString())){
							conditionMap.put(codeValues[i], values.get(codeValues[i]));
						}
					}
					values = conditionMap;
				}
				
			}else{
				if(log.isErrorEnabled())
					log.error("未知的 code2name definition " + definition);				
			}
		}catch (Exception e) {
			if(log.isErrorEnabled()) 
				log.error("编码转化时发生错误! " + e.getMessage(),e);
			
		}
		return values;
	}
	
	/**
	 * picker 需要DataPackage 型的数据，下拉框需要Map型的数据
	 */
	public DataPackage valueListPackage(String def, String condition,DBQueryParam param, String dbFlag ){
		dbFlag = checkDBFlag(dbFlag);

		if (!(def.startsWith("#") || def.startsWith("$")) ) {   
			throw new IllegalArgumentException("非单表型编码或数据字典查询，请使用 valueList 方法：" + def);
		}
		DataPackage dp = null;
		String definition = formateDefinition(def);
		try{
			//动态数据，根据VO查询 //Code2NameConfiger.sysMap.containsKey(definition)
			// 如果是数据字典参数， 需要在 code2name.xml 中配置字典VO
			Node node = null;
			if( def.startsWith("$")) 
				definition = "$"; //数据字典
			
			node = (Node) Code2NameConfiger.sysMap.get(definition);
			if(node == null) 
				throw new IllegalArgumentException("Definition " + def + " not found in code2name.xml! Notice: if it's fixed param, use $" + definition +"!");
		
			String voClassName = node.getValueObject();
			String codeProperty = node.getCode();
			String nameProperty = node.getName();

			Class voClass = InterfaceUtils.getInstance().getImplClass(IDictitemVO.class);
			if (voClass == null)
				voClass = Class.forName(voClassName);

			if (param != null) param.getQueryConditions().clear();
			if(log.isInfoEnabled()) log.info("options filter:" + condition); 
			DBQueryParam listVO = convert2Param(voClass, condition, definition); //将condition中的条件添加到查询参数上
			
			//将初始设置在查询参数上的条件，如页码，pagesize等复制过去。
			if(param == null)
				param = listVO;
			else
				param.getQueryConditions().putAll(listVO.getQueryConditions());
			 
//            if(StringUtils.isBlank(param.get_orderby()) )
            param.set_orderby(getCodeProperty(definition));	//如果没有设置排序列，则按编码列排序
//            if(StringUtils.isBlank(param.get_desc()) )
            param.set_desc("0");						//如果没有设置排序方式，则按升序排序
            
            
            Code2NameBO code2NameControl = (Code2NameBO) BOFactory.build(Code2NameBO.class, getUser(dbFlag));
            
            if(StringUtils.isBlank(dbFlag)) {
            	dbFlag = DBAccessUser.getInnerUser().getDbFlag(); //如果没有设置dbFlag，则取user中的dbFlag
            }
			dp = null;
			if( def.startsWith("#")) 
				dp = code2NameControl.doValueListPackage(voClass, codeProperty, nameProperty, param, dbFlag);
			else //数据字典
				dp = code2NameControl.doValueListPackage(formateDefinition(def) , codeProperty, nameProperty, param, dbFlag);
			
//			dp = commonControl.query(param); //查询
			if( dp.getDatas().size() > 0 ){
				Iterator iter = dp.getDatas().iterator();
				List list = new ArrayList(dp.getDatas().size());
				while( iter.hasNext() ){
					Object item = iter.next();
					Node thisnode = new Node();
					thisnode.setCode( String.valueOf( BeanUtils.getProperty(item, codeProperty)) );
					thisnode.setName( String.valueOf( BeanUtils.getProperty(item,nameProperty)) );
					list.add( thisnode );
				}
                dp.getDatas().clear();
                dp.setDatas( list );
			}
			
		}catch (Exception e) {
			if(log.isErrorEnabled()) 
				log.error("valueListPackage 执行时发生错误! " + e.getMessage(),e);
		}
		
		if(dp == null) {
			dp = new DataPackage();
			dp.setDatas(new ArrayList(0));
			dp.setRowCount(0);
		}
		return dp;
	}
	
	private String formateDefinition(String definition ) {
		if(definition.startsWith("$") || definition.startsWith("#") || definition.startsWith("@")) {
			return definition.substring(1);
		}
		return definition;
	}
	
	private DBQueryParam convert2Param(Class voClass,String condition,String definition) {
		DBQueryParam param = new DBQueryParam();
		if(StringUtils.isNotBlank(condition)) {
			try {
				String filters[] = condition.split(";");
				for (int i = 0; i < filters.length; i++) {
					String filterPairString = filters[i];
					String[] filterPairArray = StringUtils.split(filterPairString, ":");
					
					String conditionName = filterPairArray[0];
					String conValue = null;
					if(filterPairArray.length >1 )
						conValue = filterPairArray[1];
					
					if("CODE".equals(conditionName)) {	//CODE要转换为 vo类对应的编码属性名
						conditionName = getCodeProperty(definition);
					}
					
					if("NAME".equals(conditionName)) {	//NAME要转换为 vo类对应的编码属性名
						conditionName = getNameProperty(definition);
					}
					
					String conditionString = getConditionString(voClass, conditionName,conValue);
					Object conditionValue = castConditionValue(voClass,conditionName, conValue);
					
					param.getQueryConditions().put(conditionString, conditionValue);
				}
				
			} catch (Exception e) {
				if(log.isErrorEnabled()) log.error("Invalid filter param. ", e);
			}
		}
		return param;
	}
	/**
	 * 根据字段类型,对字符型字段值进行转换
	 * @param voClass
	 * @param field
	 * @param conValue
	 * @return
	 * @throws Exception
	 */
	public static Object castConditionValue(Class voClass, String field, String conValue) throws Exception {

		try {
			// by hekun, 2008-12-25, 修改按条件字段查询的bug。
			if (field.startsWith("_")) {
				return conValue;
			}

			Object value = null;
			Object obj = voClass.newInstance();
			PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(obj, field);
			Class pType = descriptor.getPropertyType();

			// add List type by Canigar
			if (conValue.indexOf(",") != -1) {
				String[] values = StringUtils.split(conValue, ",");
				int regFlag = 0;
				for (int i = 0; i < values.length; i++) {
					if (values[i].startsWith("!")) {
						regFlag++;
					}
				}
				if (regFlag == 0 || regFlag == values.length) {
					conValue = conValue.replaceAll("!", "");
					String[] conValues = StringUtils.split(conValue, ",");
					value = Arrays.asList(conValues);
				}
			} else if (pType == String.class) {
				value = conValue == null ? "" : conValue;
			} else if (Number.class.isAssignableFrom(pType)) {
				value = conValue == null ? new Long(0) : new Long(conValue);
			} else if (pType.isPrimitive() && pType.getName().equals("long") || pType.getName().equals("integer") || pType.getName().equals("byte")
					|| pType.getName().equals("short")) {
				value = conValue == null ? new Integer(0) : new Integer(conValue);
			} else {
				throw new IllegalArgumentException("Unsupport type of condition field " + field + ".Require String, Long, Integer type.");
			}

			return value;
		} catch (Exception e) {
			throw e;
		}
	}

	private static String getConditionString(Class voClass, String field, String vlalue) throws Exception {
		try {
			// by hekun, 2008-12-25, 修改按条件字段查询的bug。
			if (field.startsWith("_")) {
				return field;
			}

			Object obj = voClass.newInstance();
			PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(obj, field);
			Class pType = descriptor.getPropertyType();
			String prefix = "";

			if (pType == String.class) {
				prefix = vlalue == null ? "_sn_" : "_sk_";
			} else if (Number.class.isAssignableFrom(pType)) {
				prefix = vlalue == null ? "_nn_" : "_ne_";
			} else if (pType.isPrimitive() && pType.getName().equals("long") || pType.getName().equals("integer") || pType.getName().equals("byte")
					|| pType.getName().equals("short")) {
				prefix = vlalue == null ? "_nn_" : "_ne_";
			} else {
				throw new IllegalArgumentException("Unsupport type of condition field " + field + ".Require String, Long, Integer type.");
			}

			// add List type by Canigar
			if (vlalue.indexOf(",") != -1) {
				String[] values = StringUtils.split(vlalue, ",");
				int regFlag = 0;
				for (int i = 0; i < values.length; i++) {
					if (values[i].startsWith("!")) {
						regFlag++;
					}
				}
				if (regFlag == 0) {
					prefix = prefix.substring(0, 2);
					prefix = prefix + "in_";
				} else if (regFlag == values.length) {
					prefix = prefix.substring(0, 2);
					prefix = prefix + "nin_";
				}
			}
			
			return prefix + field;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static String getCodeProperty( String definition ) throws Exception {
		Node node = (Node)Code2NameConfiger.sysMap.get(definition);
		return node.getCode();
	}
	
	public static String getNameProperty( String definition ) throws Exception {
		Node node = (Node) Code2NameConfiger.sysMap.get(definition);
		return node.getName();
	}

	/**
	 * 2011-10-20 jinbo 
	 * 检查name值,如果是null,则返回code值,避免部分code转换后没内容则直接显示null 
	 */
	public String checkNameValue(String codeValue, String nameValue){
		if (nameValue == null || nameValue.equals("null")) return codeValue;
		else return nameValue;
	}

	/**
	 * 按字典组的定义，把code,name拼装为一个条件字符串,并追加到condition字符串中
	 * 格式：dictid:sn-01;dictname:测试
	 */
	public String castConditionValue(String definition, String condition, String code, String name) {
		StringBuffer sb = new StringBuffer(condition);

		String def;
		if( definition.startsWith("$")) {
			def = "$"; //数据字典
		}else {
			def = formateDefinition(definition);
		}
		Node node = (Node) Code2NameConfiger.sysMap.get(def);
		if (node != null){
			if (!PublicUtils.isBlankString(code)){
				if (sb.length() > 0) sb.append(";");
				//添加前缀，避免与表的字段冲突
				sb.append("__").append(node.getCode()).append(":").append(code);
			}
			if (!PublicUtils.isBlankString(name)){
				if (sb.length() > 0) sb.append(";");
				//添加前缀，避免与表的字段冲突
				sb.append("__").append(node.getName()).append(":").append(name);
			}
		}
		return sb.toString();
	}
}
