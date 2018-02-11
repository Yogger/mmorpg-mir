package com.mmorpg.mir.model.addication.model;

import com.windforce.common.utility.DateUtils;

/**
 * 沉迷过后的收益
 * 
 * @author Kuang Hao
 * @since v1.0 2012-3-22
 * 
 */
public class AddicationRate {

	/** 正常 */
	public static final double NORMAL = 1;
	/** 沉迷 */
	public static final double FATIGUE = 0.5;
	/** 不健康 */
	public static final double UNHEALTHY = 0;

	/** 状态的时间 */
	public static final long FATIGUE_TIME = DateUtils.MILLIS_PER_HOUR * 3;
	public static final long UNHEALTHY_TIME = DateUtils.MILLIS_PER_HOUR * 5;
	/** 下线5小时清空在线时间 */
	public static final long UNLINE_CLEAR_TIME = DateUtils.MILLIS_PER_HOUR * 5;

	/**
	 * 收益计算
	 * 
	 * @param onlineTime
	 * @return
	 */
	public static double getRate(long onlineTime) {
		if (onlineTime < FATIGUE_TIME) {
			return NORMAL;
		}
		if (onlineTime < UNHEALTHY_TIME) {
			return FATIGUE;
		}
		return UNHEALTHY;
	}
}
