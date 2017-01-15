package com.jambo.jop.ui.init;

import com.jambo.jop.ui.init.convert.SqlDateConverter;
import com.jambo.jop.ui.init.convert.SqlTimestampConverter;
import com.jambo.jop.ui.init.convert.UtilDateConverter;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


/**
 * <p>
 * Title: GDIBOSS
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * <p>
 * Company: jambo-framework Tech Ltd.
 * </p>
 * 
 * @author sunil
 * @version 1.0
 * @version 1.1 HuangBaiming
 */
public class InitServlet extends HttpServlet {

	private static final Logger log = LogManager.getLogger(InitServlet.class);

	public void init() throws ServletException {
		if (log.isInfoEnabled()) { 
			log.info("Entering InitServlet.init().");
		}
//		SpringContextManager.init();
		// init commons BeanUtils
		ConvertUtils.register(new SqlTimestampConverter(null), java.sql.Timestamp.class);
		ConvertUtils.register(new UtilDateConverter(null), java.util.Date.class);
		ConvertUtils.register(new SqlDateConverter(null), java.sql.Date.class);

		// hekun: don't convert null number to 0.
		ConvertUtils.register(new ByteConverter(null), Byte.class);
		ConvertUtils.register(new ShortConverter(null), Short.class);
		ConvertUtils.register(new IntegerConverter(null), Integer.class);
		ConvertUtils.register(new LongConverter(null), Long.class);
		ConvertUtils.register(new FloatConverter(null), Float.class);
		ConvertUtils.register(new DoubleConverter(null), Double.class);

		log.info("Exiting InitServlet.init()");
	}

	public void destroy() {
		if (log.isInfoEnabled()) {
			log.info("Calling InitServlet.destroy()");
		}
	}

}
