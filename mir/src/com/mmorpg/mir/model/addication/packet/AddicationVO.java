package com.mmorpg.mir.model.addication.packet;

import com.mmorpg.mir.model.addication.AntiAddictionManager;
import com.mmorpg.mir.model.addication.model.Addication;

/**
 * 防沉迷VO对象
 * 
 * @author Kuang Hao
 * @since v1.0 2012-5-29
 * 
 */
public class AddicationVO {

	/** 构造方法 */
	public static AddicationVO valueOf(Addication addication) {
		AddicationVO vo = new AddicationVO();
		vo.unlineTime = addication.getUnlineTime();
		vo.onlineTime = addication.getOnlineTime();
		vo.rate = addication.getRate();
		vo.open = AntiAddictionManager.openAnti;
		vo.adult = addication.getOwner().getPlayerEnt().getAdult() > 0;
		return vo;
	}

	/** 在线时间 */
	private long onlineTime;
	/** 下线时间 */
	private long unlineTime;
	/** 收益百分比 */
	private double rate;
	/** 是否开启 */
	private boolean open;
	/** 是否成年 */
	private boolean adult;

	public long getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(long onlineTime) {
		this.onlineTime = onlineTime;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isAdult() {
		return adult;
	}

	public void setAdult(boolean adult) {
		this.adult = adult;
	}

	public long getUnlineTime() {
		return unlineTime;
	}

	public void setUnlineTime(long unlineTime) {
		this.unlineTime = unlineTime;
	}

}
