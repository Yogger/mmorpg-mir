package com.mmorpg.mir.model.addication.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.addication.AntiAddictionManager;
import com.mmorpg.mir.model.addication.packet.AddicationVO;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;

/**
 * 沉迷，在线时间结构体
 * 
 * @author Kuang Hao
 * @since v1.0 2012-3-22
 * 
 */
public class Addication {

	@Transient
	private Player owner;

	/** 在线时间 */
	private long onlineTime;
	/** 不在线时间 */
	private long unlineTime;

	private long lastAddTime;

	@Transient
	private List<Future<?>> futures = new ArrayList<Future<?>>();

	/**
	 * 关闭任务
	 */
	@JsonIgnore
	public void stopAddicationsAntiFuture() {
		for (Future<?> future : futures) {
			future.cancel(true);
		}
		futures.clear();
	}

	/**
	 * 启动任务
	 */
	@JsonIgnore
	public void startAddicationAntiFuture() {
		if (AntiAddictionManager.openAnti && owner.getPlayerEnt().getAdult() < 1) {
			if (onlineTime < AddicationRate.FATIGUE_TIME) {
				Future<?> future = ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						addOnlineTime();
						// 推送给前端沉默
						PacketSendUtility.sendPacket(getOwner(), AddicationVO.valueOf(getOwner().getAddication()));
					}
				}, AddicationRate.FATIGUE_TIME - onlineTime);
				futures.add(future);
			}

			if (onlineTime < AddicationRate.UNHEALTHY_TIME) {
				Future<?> future = ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						addOnlineTime();
						// 推送给前端无经验
						PacketSendUtility.sendPacket(getOwner(), AddicationVO.valueOf(getOwner().getAddication()));
					}
				}, AddicationRate.UNHEALTHY_TIME - onlineTime);
				futures.add(future);
			}

			Future<?> future = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					addOnlineTime();
					// 推送给前端无经验
					PacketSendUtility.sendPacket(getOwner(), AddicationVO.valueOf(getOwner().getAddication()));
				}
			}, 10000, 10000);
			futures.add(future);
		}
	}

	@JsonIgnore
	public void addOnlineTime() {
		onlineTime += (System.currentTimeMillis() - lastAddTime);
		lastAddTime = System.currentTimeMillis();
	}

	@JsonIgnore
	public void addUnlineTime(long addUnTime) {
		unlineTime += addUnTime;
		if (unlineTime >= AddicationRate.UNLINE_CLEAR_TIME) {
			unlineTime = 0;
			onlineTime = 0;
		}
		lastAddTime = System.currentTimeMillis();
	}

	@JsonIgnore
	public double getRate() {
		if (owner.getPlayerEnt().getAdult() > 0 || !AntiAddictionManager.openAnti) {
			// 已经成年
			return AddicationRate.NORMAL;
		}
		if (!AntiAddictionManager.openAnti) {
			// 未开防沉迷
			return AddicationRate.NORMAL;
		}
		return AddicationRate.getRate(onlineTime);
	}

	public long getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(long onlineTime) {
		this.onlineTime = onlineTime;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public long getLastAddTime() {
		return lastAddTime;
	}

	public void setLastAddTime(long lastAddTime) {
		this.lastAddTime = lastAddTime;
	}

	public long getUnlineTime() {
		return unlineTime;
	}

	public void setUnlineTime(long unlineTime) {
		this.unlineTime = unlineTime;
	}

}
