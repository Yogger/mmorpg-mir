package com.mmorpg.mir.model.soul.packet;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.soul.core.SoulManager;
import com.mmorpg.mir.model.soul.model.Soul;

/**
 * 英魂开启
 * 
 * @author 37wan
 * 
 */
public class SM_Soul_Notify {

	private boolean open; // 是否开启
	private int level; // 记录玩家当前已经达到的英魂等级,最少是1
	private long clearTime; // 清除祝福值的时间
	private int nowBlessValue; // 玩家当前的祝福值

	public static SM_Soul_Notify valueOf(Player player, Soul soul) {
		SM_Soul_Notify response = new SM_Soul_Notify();
		response.setOpen(SoulManager.getInstance().isOpen(player));
		response.setLevel(soul.getLevel());
		response.setClearTime(soul.getClearTime());
		response.setNowBlessValue(soul.getNowBlessValue());
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
