package com.jambo.jop.ui.common;

import com.jambo.jop.infrastructure.db.*;

/**
 * 用户提供者接口。获取用户实例。用于表示层或底层根据具体情况获取当前会话的用户
 * @author He Kun
 *
 */
public interface UserProvider {
	
	/**
	 * 获取用户实例。
	 * @return
	 */
	DBAccessUser getUser();
}
