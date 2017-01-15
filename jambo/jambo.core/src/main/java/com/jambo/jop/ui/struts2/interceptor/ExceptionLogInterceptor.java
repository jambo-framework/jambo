package com.jambo.jop.ui.struts2.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//extends AbstractInterceptor
public class ExceptionLogInterceptor  {

	private static final long serialVersionUID = -3411300998892475560L;
	
	protected static final Logger log = LoggerFactory.getLogger(ExceptionLogInterceptor.class);

                                   /*
    public String intercept(ActionInvocation invocation) throws Exception {
        String result;

        try {
            result = invocation.invoke();
        } catch (Exception e) {
        	if(e instanceof ServletException )
        		e.printStackTrace();
        	
        	doLog(log, e);        	
            List exceptionMappings = invocation.getProxy().getConfig().getExceptionMappings();
            String mappedResult = this.findResultFromExceptions(exceptionMappings, e);
            if (mappedResult != null) {
                result = mappedResult;
                publishException(invocation, new ExceptionHolder(e));
            } else {
                throw e;
            }
        }
        return result;
    }

    protected void doLog(Logger logger, Exception e) {
    	if(logger.isErrorEnabled())
    		logger.error(e.getMessage(), e);
    }

    private String findResultFromExceptions(List exceptionMappings, Throwable t) {
        String result = null;

        // Check for specific exception mappings.
        if (exceptionMappings != null) {
            int deepest = Integer.MAX_VALUE;
            for (Iterator iter = exceptionMappings.iterator(); iter.hasNext();) {
                ExceptionMappingConfig exceptionMappingConfig = (ExceptionMappingConfig) iter.next();
                int depth = getDepth(exceptionMappingConfig.getExceptionClassName(), t);
                if (depth >= 0 && depth < deepest) {
                    deepest = depth;
                    result = exceptionMappingConfig.getResult();
                }
            }
        }

        return result;
    }
              */
    /**
     * Return the depth to the superclass matching. 0 means ex matches exactly. Returns -1 if there's no match.
     * Otherwise, returns depth. Lowest depth wins.
     *
     * @param exceptionMapping  the mapping classname
     * @param t  the cause
     * @return the depth, if not found -1 is returned.
     */
    public int getDepth(String exceptionMapping, Throwable t) {
        return getDepth(exceptionMapping, t.getClass(), 0);
    }

    private int getDepth(String exceptionMapping, Class exceptionClass, int depth) {
        if (exceptionClass.getName().indexOf(exceptionMapping) != -1) {
            // Found it!
            return depth;
        }
        // If we've gone as far as we can go and haven't found it...
        if (exceptionClass.equals(Throwable.class)) {
            return -1;
        }
        return getDepth(exceptionMapping, exceptionClass.getSuperclass(), depth + 1);
    }

    /**
     * Default implementation to handle ExceptionHolder publishing. Pushes given ExceptionHolder on the stack.
     * Subclasses may override this to customize publishing.
     *
     * @param invocation The invocation to publish Exception for.
     * @param exceptionHolder The exceptionHolder wrapping the Exception to publish.
     */

//    protected void publishException(ActionInvocation invocation, ExceptionHolder exceptionHolder) {
//        invocation.getStack().push(exceptionHolder);
//    }
}
