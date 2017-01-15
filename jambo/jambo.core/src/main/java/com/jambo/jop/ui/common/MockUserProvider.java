package com.jambo.jop.ui.common;

import com.jambo.jop.infrastructure.db.DBAccessUser;

public class MockUserProvider implements UserProvider {

	public DBAccessUser getUser() {
		return DBAccessUser.getInnerUser();
	}

}
