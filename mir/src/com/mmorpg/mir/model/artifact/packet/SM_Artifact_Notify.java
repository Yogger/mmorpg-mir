package com.mmorpg.mir.model.artifact.packet;

import com.mmorpg.mir.model.artifact.core.ArtifactManager;
import com.mmorpg.mir.model.artifact.model.Artifact;
import com.mmorpg.mir.model.gameobjects.Player;

/**
 * 通知神兵开启
 * 
 * @author 37wan
 * 
 */
public class SM_Artifact_Notify {

	private boolean open; // 是否开启
	private int level; // 记录玩家当前已经达到的神兵等级,最少是1
	private long clearTime; // 清除祝福值的时间
	private int nowBlessValue; // 玩家当前的祝福值

	public static SM_Artifact_Notify valueOf(Player player, Artifact artifact) {
		SM_Artifact_Notify response = new SM_Artifact_Notify();
		response.setOpen(ArtifactManager.getInstance().isOpen(player));
		response.setClearTime(artifact.getClearTime());
		response.setLevel(artifact.getLevel());
		response.setNowBlessValue(artifact.getNowBlessValue());
		return response;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public long getClearTime() {
		return clearTime;
	}

	public void setClearTime(long clearTime) {
		this.clearTime = clearTime;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNowBlessValue() {
		return nowBlessValue;
	}

	public void setNowBlessValue(int nowBlessValue) {
		this.nowBlessValue = nowBlessValue;
	}

}
