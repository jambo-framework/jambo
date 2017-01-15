package com.jambo.jop.exception.system;

import com.jambo.jop.exception.JOPException;

/**
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: jambo-framework Tech Ltd.</p>
 *
 * @author JinBo
 *
 * @version 1.0
 */
public class DAOException extends JOPException {
	private static String errorCode = "dao.common";
	
    public DAOException() {
        super(errorCode);
    }

    public DAOException(String message) {
        super(message);
    }

}
