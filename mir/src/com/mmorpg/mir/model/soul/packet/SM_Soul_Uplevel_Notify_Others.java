package com.mmorpg.mir.model.soul.packet;

import com.mmorpg.mir.model.gameobjects.Player;

/**
 * 英魂进阶成功后通知周围的玩家
 * 
 * @author 37wan
 * 
 */
public class SM_Soul_Uplevel_Notify_Others {
	
	private int code; // 错误码
	private long playerId;// 英魂等级发生改变的玩家
	private int level; // 当前玩家最新的英魂等级

	public static SM_Soul_Uplevel_Notify_Others valueOf(Player player) {
		SM_Soul_Uplevel_Notify_Others response = new SM_Soul_Uplevel_Notify_Others();
		response.playerId = player.getObjectId();
		response.level = player.getSoul().getLevel();
		return response;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
