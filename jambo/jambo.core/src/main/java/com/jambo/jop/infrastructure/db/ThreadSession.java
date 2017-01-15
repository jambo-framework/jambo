package com.jambo.jop.infrastructure.db;

import java.util.HashMap;

import org.hibernate.Session;


/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description: ThreadSession类是开发时用到的类（正式运行时不用），所以有不完善的地方。
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: jambo-framework
 * </p>
 * 
 * @author HuangBaiming
 * 
 * @version 1.0
 */
public class ThreadSession {

	/*
	 * sessionMap的结构 
	 * key: dbFlag       数据库标识，区分不同的数据库
	 * value: Session    数据库会话
	 * 
	 * */
	private HashMap sessionMap;

	public int count;

	public ThreadSession() {
		count = 0;
		sessionMap = new HashMap();
	}


	public Session getSession(String dbFlag) {
		Object session = sessionMap.get(dbFlag); 
		if(session!=null){
			return (Session)session;
		}else{
			return null;
		}		
	}

	public void setSession(String dbFlag,Session session) {
		sessionMap.put(dbFlag, session);
	}

	public HashMap getSessionMap(){
		return sessionMap;
	}
	
}
