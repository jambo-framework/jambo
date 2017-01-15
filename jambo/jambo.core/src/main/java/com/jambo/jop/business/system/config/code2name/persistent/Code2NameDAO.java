package com.jambo.jop.business.system.config.code2name.persistent;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;

import com.jambo.jop.infrastructure.db.AbstractDAO;
import com.jambo.jop.infrastructure.db.BaseDAO;
import com.jambo.jop.infrastructure.db.DAOFactory;
import com.jambo.jop.infrastructure.db.DBQueryParam;
import com.jambo.jop.infrastructure.db.DataPackage;

public class Code2NameDAO extends AbstractDAO {
	
	public Code2NameDAO() {		
	}
	
	public Code2NameDAO(String dbFlag){
		setDbFlag(dbFlag);
	}

    public Object translateCode(String voName, String codeName,
			String nameName, Object codeValue) throws Exception {
    	String daoClassName = StringUtils.replace(voName, "VO", "DAO");
		Class daoClass = Class.forName(daoClassName);
		BaseDAO dao = DAOFactory.build(daoClass, getDbFlag());
		
		DBQueryParam param = new DBQueryParam();
		List fields = new ArrayList(2);
		fields.add(codeName);
		fields.add(nameName);
		param.setSelectFields(fields);
		
//		param.getQueryConditions().put(arg0, arg1); // codeValue作为条件	
		Object returnValue = codeValue;
		
		Class voClass = Class.forName(voName);
		//保证传入的主键类型正确。
		Object typerightCodeValue = convertValueAsPropertyType(voClass,codeName,codeValue);
		
		Object object =  dao.findByPk((Serializable)typerightCodeValue);
		if(object !=null){
			Object nameValue = PropertyUtils.getProperty(object, nameName);
			if(nameValue!=null)
				returnValue = nameValue;
		}
		return returnValue;
    }
//    
    /**
     * 该方法在BO里。
     */
//	public Object translateCode(String voName,  String groupid,  String codeName,
//			String nameName, Object codeValue) throws Exception {		
////		
////		if(groupid!=null) { //数据字典
////			DictItemDAO dao = (DictItemDAO) DAOFactory.build(DictItemDAO.class, getDbFlag());
////			
////			DictItemVO vo = new DictItemVO();
////			vo.setDictCode((Long)codeValue);
////			vo.setGroupCode(groupid);
////			DictItemVO vo2 = (DictItemVO) dao.findByPk(vo);
////			
////			if(vo2.getDictName()!=null)
////				return vo2.getDictName();
////		
////		}
//		return codeValue;
//		
//////		Session session = (Session)getSessionManager().currentSession();		
////		try {
////			// 查询语句： select VO.name from voName as VO where VO.code=:code
////			StringBuffer hql = new StringBuffer("select VO.").append(nameName)
////					.append(" from ").append(voName).append(" as VO where VO.")
////					.append(codeName).append(" = :code");
////			
////			if(groupid!=null)
////				hql.append(" and VO.groupid ").append(" = :groupid");
////			
////			Query query = session.createQuery(hql.toString());
////			query.setParameter("code", codeValue);
////			if(groupid!=null)
////				query.setParameter("groupid", groupid);
////						
////			Iterator iter = query.iterate();
////			if (iter != null && iter.hasNext()) {
////				return iter.next();
////			} else {
////				return codeValue;
////			}
////
////		} catch (HibernateException ex) {
////			if (ex.getCause() != null) {
////				throw new Exception(ex.getCause());
////			} else {
////				throw ex;
////			}
////		}
//	}
	
	private Object convertValueAsPropertyType(Class voClass,String field, Object codeValue) throws Exception {
		Object obj = voClass.newInstance();
		PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(obj, field);
		Class pType = descriptor.getPropertyType();
		
		if(pType == String.class)
			return codeValue;
		else if( Long.class.isAssignableFrom(pType)) {
			return Long.valueOf(codeValue.toString());
			
		}else if( Integer.class.isAssignableFrom(pType)) {
			return Integer.valueOf(codeValue.toString());
			
		}else if( Short.class.isAssignableFrom(pType)) {
			return Short.valueOf(codeValue.toString());
			
		}else if( Byte.class.isAssignableFrom(pType)) {
			return Byte.valueOf(codeValue.toString());
		}
		
		throw new IllegalArgumentException("Unsupported type of condition field " + field + ".Require String, Long, Integer,Short,Byte type."  );
		
		//暂时不考虑primitive类型 pType.getName().equals("long") || pType.getName().equals("integer") || pType.getName().equals("byte") || pType.getName().equals("short"))
	}

//	public DataPackage valueListPackage(String voName, String codeName,
//			String nameName, DBQueryParam param ) throws Exception {
//		
//	}
	
	/**
	 * 获取单表的值列表，只获取编码，名称两个字段
	 */
//	public Map valueList(String voName, String codeName,String nameName ) throws Exception {		
//		return valueList(voName, codeName, null, nameName);
//	}
	


//	public Map valueList(String voName, String codeName, DBQueryParam param, String nameName) throws Exception {
////		Session session = (Session)getSessionManager().currentSession();
//		try {
////			String daoClassName = StringUtils.replace(voName, "VO", "DAO");
////			Class daoClass = Class.forName(daoClassName);
////			BaseDAO dao = DAOFactory.build(daoClass, getDbFlag());
////			
////			List fields = new ArrayList(2);
////			fields.add(codeName);
////			fields.add(nameName);
////			param.setSelectFields(fields);
////			param.setQueryAll(true); //查询所有
////			
////			//Class voClass = Class.forName(voName);
////			//保证传入的主键类型正确。
////			//Object typerightCodeValue = convertValueAsPropertyType(voClass,codeName,codeValue);
//			
//			DataPackage dp = valueListPackage(voName, codeName, nameName, param);
//			
//			Map maplist = new LinkedHashMap(dp.getDatas().size());
//			
//			//将数据转换为 map
//			for(int i = 0 ; i < dp.getDatas().size() ; i++ ) {
//				Object vo = dp.getDatas().get(i);
//				
//			}
////			Object object =  dao.findByPk((Serializable)typerightCodeValue);
////			if(object !=null){
////				Object nameValue = PropertyUtils.getProperty(object, nameName);
////				if(nameValue!=null)
////					returnValue = nameValue;
////			}
////			return returnValue;
//			
//			// 查询语句： select VO.name,VO.code from voName as VO 
////			StringBuffer hql = new StringBuffer("select VO.").append(nameName)
////					.append(",VO.").append(codeName).append(" from ").append(voName).append(" as VO ");
////			Query query = session.createQuery(hql.toString());			
//			
////			Iterator iter = query.iterate();
////			while (iter != null && iter.hasNext()) {
////				Object[] value = (Object[])iter.next();
////				if( value[1] != null && value[0] != null ){
////					maplist.put(value[1].toString(),value[0].toString());
////				}
////			}
//			return maplist;//maplist;
//		} catch (HibernateException ex) {
//			ex.printStackTrace();
//			if (ex.getCause() != null) {
//				throw new Exception(ex.getCause());
//			} else {
//				throw ex;
//			}
//		}
//	}
//	
	/**
	 * 根据VOClass 获取所有编码，名称列表
	 * @param voName
	 * @param codeName
	 * @param nameName
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public DataPackage valueListPackage(Class voClass, String codeName, String nameName, DBQueryParam param) throws Exception {		
		try {			
			BaseDAO dao = DAOFactory.buildCommonDAO(voClass, getDbFlag());		

			//所有的code2name不用应该采用field这种形式
			//这是因为采用field选择列hibernate的反射机制会认为缺少构造器,除非每个VO都有相应的code2name专门构造器,影响如下
			//1.所有VO必须有专门的构造器
			//2.遇到双主键而且主键跟code2name的类型为都为同类型时,构造方法变得无谓而且复杂
			//  例如: 双主键 dictid:String groupid:String   code2name dictid:String dictname:String 
			//	则无法写这两个构造器,只能给原有的构造器添加多一个无谓的属性,还要写多一个code2name的构造器
			//所以屏蔽掉以下代码,这样做最多就是给内存施加多一点压力而已,如果在dictitem表不是庞大,字段不是很多,硬件足够支持的情况下,应为比较好的方案,
			
//			List fields = new ArrayList(2);
//			fields.add(codeName);
//			fields.add(nameName);
//			param.setSelectFields(fields);
//			param.setQueryAll(false); //查询所有
			
			DataPackage dp = dao.query(param);
			
			return dp;
		} catch (HibernateException ex) {
			ex.printStackTrace();
			if (ex.getCause() != null) {
				throw new Exception(ex.getCause());
			} else {
				throw ex;
			}
		}
	}
}