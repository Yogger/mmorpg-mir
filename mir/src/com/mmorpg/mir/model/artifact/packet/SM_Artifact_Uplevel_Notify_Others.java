package com.mmorpg.mir.model.artifact.packet;

import com.mmorpg.mir.model.gameobjects.Player;

/**
 * 通知其他玩家
 * 
 * @author 37wan
 * 
 */
public class SM_Artifact_Uplevel_Notify_Others {

	private int code; // 错误码
	private long playerId;// 英魂等级发生改变的玩家
	private int level; // 当前玩家最新的英魂等级
	private int appLevel;

	public static SM_Artifact_Uplevel_Notify_Others valueOf(Player player) {
		SM_Artifact_Uplevel_Notify_Others response = new SM_Artifact_Uplevel_Notify_Others();
		response.playerId = player.getObjectId();
		response.level = player.getArtifact().getLevel();
		response.appLevel = player.getArtifact().getAppLevel();
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

	public int getAppLevel() {
		return appLevel;
	}

	public void setAppLevel(int appLevel) {
		this.appLevel = appLevel;
	}

}
