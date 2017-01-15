/**
 * 
 */
package com.jambo.jop.ui.filter;

import com.jambo.jop.infrastructure.db.DBAccessUser;

/**
 * @author He Kun
 * 
 */
public interface PermissionChecker {

	boolean checkURIPermission(String oprcode, String currentURI, DBAccessUser user) throws Exception;

	/**
	 * 判断是否是受保护资源
	 */
	boolean checkPermission(String oprcode, String permissionId, DBAccessUser user) throws Exception;

}
