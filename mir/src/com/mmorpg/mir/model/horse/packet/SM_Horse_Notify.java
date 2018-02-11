package com.mmorpg.mir.model.horse.packet;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.horse.manager.HorseManager;

/**
 * 坐骑开启
 * 
 * @author 37wan
 * 
 */
public class SM_Horse_Notify {

	private boolean open;
	private int grade;
	private int level;
	private int totalCount;
	private long clearTime;

	public static SM_Horse_Notify valueOf(Player player) {
		SM_Horse_Notify response = new SM_Horse_Notify();
		response.open = HorseManager.getInstance().isOpen(player);
		response.grade = player.getHorse().getGrade();
		response.level = player.getHorse().getLevel();
		response.totalCount = player.getHorse().getUpSum();
		response.clearTime = player.getHorse().getClearTime();
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

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
