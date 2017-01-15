package com.jambo.jop.infrastructure.control;

import com.jambo.jop.common.spring.SpringContextManager;
import com.jambo.jop.infrastructure.db.DBAccessUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * <p>
 * Title: Control工厂
 * </p>
 * 
 * <p>
 * Description: 根据配置生成一个AbstractBO对象
 * </p>
 * Copyright: Copyright (c) 2005 </p>
 * <p>
 * Company: Sunrise
 * </p>
 * 
 * @author HuangBaiming && DuHuazheng
 * @author He Kun
 * 
 * @version 1.0
 * @version 2.0 引入Spring,使用 beanFactory 获取所请求的业务Bean实例，Spring为其自动封装事务支持。
 * @version 2.1 by hekun:改判断TESTFLAG 为yes 时创建EJB 为判断 EJBFLAG标识。
 */
public class BOFactory {
	/**
		PROPAGATION_REQUIRED 	如果存在一个事务，则支持当前事务。如果没有事务则开启一个新的事务。 使用 spring 声明式事务， spring 使用 AOP 来支持声明式事务，会根据事务属性，自动在方法调用之前决定是否开启一个事务，并在方法执行之后决定事务提交或回滚事务。 单独调用 methodB 方法 相当于 Spring 保证在 methodB 方法中所有的调用都获得到一个相同的连接。在调用 methodB 时，没有一个存在的事务，所以获得一个新的连接，开启了一个新的事务。 单独调用 MethodA 时，在 MethodA 内又会调用 MethodB. 执行效果相当于 调用 MethodA 时，环境中没有事务，所以开启一个新的事务 . 当在 MethodA 中调用 MethodB 时，环境中已经有了一个事务，所以 methodB 就加入当前事务。 

		PROPAGATION_SUPPORTS 如果存在一个事务，支持当前事务。如果没有事务，则非事务的执行。但是对于事务同步的事务管理器， PROPAGATION_SUPPORTS 与不使用事务有少许不同。 单纯的调用 methodB 时， methodB 方法是非事务的执行的。 当调用 methdA 时 ,methodB 则加入了 methodA 的事务中 , 事务地执行。 

		PROPAGATION_MANDATORY 如果已经存在一个事务，支持当前事务。如果没有一个活动的事务，则抛出异常。 当单独调用 methodB 时，因为当前没有一个活动的事务，则会抛出异常 throw new IllegalTransactionStateException("Transaction propagation ''mandatory'' but no existing transaction found"); 当调用 methodA 时， methodB 则加入到 methodA 的事务中，事务地执行。 

		PROPAGATION_REQUIRES_NEW 总是开启一个新的事务。如果一个事务已经存在，则将这个存在的事务挂起。 当单独调用 methodB 时，相当于把 methodb 声明为 REQUIRED 。开启一个新的事务，事务地执行。 当调用 methodA 时 情况有些大不一样 . 相当于下面的效果。 在这里，我把 ts1 称为外层事务， ts2 称为内层事务。从上面的代码可以看出， ts2 与 ts1 是两个独立的事务，互不相干。 Ts2 是否成功并不依赖于 ts1 。如果 methodA 方法在调用 methodB 方法后的 doSomeThingB 方法失败了，而 methodB 方法所做的结果依然被提交。而除了 methodB 之外的其它代码导致的结果却被回滚了。 使用 PROPAGATION_REQUIRES_NEW, 需要使用 JtaTransactionManager 作为事务管理器。 

		PROPAGATION_NOT_SUPPORTED 总是非事务地执行，并挂起任何存在的事务。 当单独调用 methodB 时，不启用任何事务机制，非事务地执行。 当调用 methodA 时，相当于下面的效果 使用 PROPAGATION_NOT_SUPPORTED, 也需要使用 JtaTransactionManager 作为事务管理器。 

		PROPAGATION_NEVER 总是非事务地执行，如果存在一个活动事务，则抛出异常 单独调用 methodB ，则非事务的执行。 调用 methodA 则会抛出异常 

		PROPAGATION_NESTED 如果一个活动的事务存在，则运行在一个嵌套的事务中 . 如果没有活动事务 , 则按 TransactionDefinition.PROPAGATION_REQUIRED 属性执行 这是一个嵌套事务 , 使用 JDBC 3.0 驱动时 , 仅仅支持 DataSourceTransactionManager 作为事务管理器。需要 JDBC 驱动的 java.sql.Savepoint 类。有一些 JTA 的事务管理器实现可能也提供了同样的功能。 使用 PROPAGATION_NESTED ，还需要把 PlatformTransactionManager 的 nestedTransactionAllowed 属性设为 true; 而 nestedTransactionAllowed 属性值默认为 false; 如果单独调用 methodB 方法，则按 REQUIRED 属性执行。 如果调用 methodA 方法，相当于下面的效果 当 methodB 方法调用之前，调用 setSavepoint 方法，保存当前的状态到 savepoint 。如果 methodB 方法调用失败，则恢复到之前保存的状态。但是需要注意的是，这时的事务并没有进行提交，如果后续的代码 (doSomeThingB() 方法 ) 调用失败，则回滚包括 methodB 方法的所有操作。 嵌套事务一个非常重要的概念就是内层事务依赖于外层事务。外层事务失败时，会回滚内层事务所做的动作。而内层事务操作失败并不会引起外层事务的回滚。
	 */
	
	public static final String PROPAGATION_REQUIRED ="PROPAGATION_REQUIRED";
	public static final String PROPAGATION_SUPPORTS ="PROPAGATION_SUPPORTS";
	public static final String PROPAGATION_MANDATORY ="PROPAGATION_MANDATORY";
	public static final String PROPAGATION_REQUIRES_NEW ="PROPAGATION_REQUIRES_NEW";
	public static final String PROPAGATION_NOT_SUPPORTED ="PROPAGATION_NOT_SUPPORTED";
	public static final String PROPAGATION_NEVER ="PROPAGATION_NEVER";
	public static final String PROPAGATION_NESTED ="PROPAGATION_NESTED";
	
	private static final Logger log = LoggerFactory.getLogger(BOFactory.class);

	private BOFactory() {
	}

	/**
	 * 工厂核心,构造一个control。在无需鉴权的应用中使用，例如：编码转换等 一般情况下使用 build(Class
	 * controlBeanClass,DBAccessUser user)，
	 * 
	 * @param controlBeanClass
	 *            ControlBean类
	 * @deprecated build(Class controlBeanClass,DBAccessUser user)
	 * 
	 */
	public static AbstractBO build(Class controlBeanClass) throws Exception {
		return build(controlBeanClass, null);
	}


	/**
	 * 工厂核心,构造一个control
	 * 
	 * @param controlBeanClass
	 *            ControlBean类
	 * @param user
	 */
	public static AbstractBO build(Class controlBeanClass, DBAccessUser user) throws Exception {
		return build(controlBeanClass, user, PROPAGATION_REQUIRED);
	}

	public static AbstractBO build(Class controlBeanClass, DBAccessUser user, String transactionAttribute) throws Exception {
		return useBean(controlBeanClass, user, transactionAttribute);
	}
	
	/**
	 * 从Spring context中获取已经注册的业务Bean对象。Spring容器将为其增加事务支持。 *
	 * 
	 * @param controlBeanClass
	 *            业务Bean类
	 * @param user
	 * @see #com
	 * 
	 */
	private static AbstractBO useBean(Class controlBeanClass, DBAccessUser user, String transactionAttribute) throws Exception {
		/**
		 * ---------------------------- 与Spring集成方式2， 动态创建并注册业务bean
		 */
		AbstractBO control;
		if(PROPAGATION_REQUIRED.equals(transactionAttribute)){
			SpringContextManager.registerBean(controlBeanClass.getName(), controlBeanClass);
			control = (AbstractBO) SpringContextManager.getBean(controlBeanClass.getName());
		}else{
			SpringContextManager.registerBean(controlBeanClass.getName() + transactionAttribute, controlBeanClass);
			control = (AbstractBO) SpringContextManager.getBean(controlBeanClass.getName() + transactionAttribute);
		}
		control.setUser(user); // 设置user， 业务bean需要
		return (AbstractBO) control;

	}

	public static Object invokeCreate(Object obj) throws Exception {
		Class[] types = {};
		Method m = obj.getClass().getMethod("create", types);
		Object[] args = {};
		return m.invoke(obj, args);
	}
}
