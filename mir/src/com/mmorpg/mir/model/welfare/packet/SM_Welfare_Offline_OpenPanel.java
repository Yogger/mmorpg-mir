package com.mmorpg.mir.model.welfare.packet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Welfare_Offline_OpenPanel {

	private int loginoutTimeCount;// 累计离线时间

	public static SM_Welfare_Offline_OpenPanel valueOf(Player player) {
		SM_Welfare_Offline_OpenPanel sm = new SM_Welfare_Offline_OpenPanel();
		sm.setLoginoutTimeCount(player.getWelfare().getOfflineExp().getOfflineCountSeconds());
		return sm;
	}

	public int getLoginoutTimeCount() {
		return loginoutTimeCount;
	}

	public void setLoginoutTimeCount(int loginoutTimeCount) {
		this.loginoutTimeCount = loginoutTimeCount;
	}

}
