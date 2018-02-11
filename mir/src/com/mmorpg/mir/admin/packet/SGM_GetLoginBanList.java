package com.mmorpg.mir.admin.packet;

import com.mmorpg.mir.model.operator.model.LoginBanList;

public class SGM_GetLoginBanList {
	private LoginBanList loginBanList;

	public LoginBanList getLoginBanList() {
		return loginBanList;
	}

	public void setLoginBanList(LoginBanList loginBanList) {
		this.loginBanList = loginBanList;
	}

}
