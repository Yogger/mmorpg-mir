package com.mmorpg.mir.model.gascopy.model;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Future;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gascopy.config.GasCopyMapConfig;
import com.mmorpg.mir.model.gascopy.manager.GasCopyManager;
import com.mmorpg.mir.model.gascopy.packet.SM_GasValue_Change;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.World;
import com.windforce.common.utility.DateUtils;

public class PlayerGasCopy {
	/** 今天进入的次数 **/
	private int dailyEnterCount;
	/** 次数的刷新时间 **/
	private long lastRefreshTime;
	/** 瘴气值 **/
	private int gasValue;
	/** 上一次加瘴气值的时间 **/
	private long lastAddGasTime;
	/** 上一次地图加瘴气值的时间 **/
	private transient long lastMapAddGasTime;
	
	@Transient
	private Future<?> leaveTask;
	
	@Transient
	private Future<?> thunderDeadTask;
	
	@Transient
	private Player owner;
	
	@JsonIgnore
	public void setOwner(Player player) {
		this.owner = player;
	}
	
	@JsonIgnore
	public void refresh() {
		if (!DateUtils.isToday(new Date(lastRefreshTime))) {
			dailyEnterCount = 0;
			lastRefreshTime = System.currentTimeMillis();
		}
	}
	
	public static PlayerGasCopy valueOf() {
		return new PlayerGasCopy();
	}

	public int getDailyEnterCount() {
		return dailyEnterCount;
	}

	public void setDailyEnterCount(int dailyEnterCount) {
		this.dailyEnterCount = dailyEnterCount;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	public int getGasValue() {
		return gasValue;
	}

	public void setGasValue(int gasValue) {
		this.gasValue = gasValue;
	}

	public long getLastAddGasTime() {
		return lastAddGasTime;
	}

	public void setLastAddGasTime(long lastAddGasTime) {
		this.lastAddGasTime = lastAddGasTime;
	}
	
	@JsonIgnore
	public void enter() {
		dailyEnterCount++;
		gasValue = 0;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.SECOND, 0);
		lastMapAddGasTime = cal.getTimeInMillis();
		World.getInstance().setPosition(owner, 
				GasCopyMapConfig.getInstance().MAPID.getValue(), 1, 
				GasCopyMapConfig.getInstance().BORN_POSITION.getValue()[0], 
				GasCopyMapConfig.getInstance().BORN_POSITION.getValue()[1],
				owner.getHeading());
		owner.sendUpdatePosition();
	}

	@JsonIgnore
	public void offlineLogin() {
		refresh();
		
		
			
		owner.getObserveController().addObserver(new ActionObserver(ObserverType.SPAWN) {
			@Override
			public void spawn(int mapId, int instanceId) {
				if (GasCopyMapConfig.getInstance().isInGasCopyMap(mapId)) {
					boolean afterDeadTime = System.currentTimeMillis() - lastAddGasTime 
							> GasCopyMapConfig.getInstance().GAS_FULL_LEAVE_TIME.getValue() * DateUtils.MILLIS_PER_SECOND;
							
					if (lastAddGasTime != 0L && afterDeadTime && isGasFull()) {
						if (!owner.getLifeStats().isAlreadyDead()) {
							owner.getLifeStats().reduceHpIgnoreEffect(Long.MAX_VALUE, owner, 0);
						}
						triggerLeaveCopy();
					}
				}
			}
		});
		
		if (GasCopyMapConfig.getInstance().isInGasCopyMap(owner.getMapId()) && lastMapAddGasTime != 0L) {
			int count = (int) ((System.currentTimeMillis() - lastMapAddGasTime) / DateUtils.MILLIS_PER_MINUTE);
			if (count > 0) {
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.SECOND, 0);
				lastMapAddGasTime = cal.getTimeInMillis();
				addGasValue(count * GasCopyMapConfig.getInstance().IN_MAP_GAS_VALUE.getValue());
			}
		}
		
		owner.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				if (GasCopyMapConfig.getInstance().isInGasCopyMap(owner.getMapId())) {
					addGasValue(GasCopyMapConfig.getInstance().PLAYER_DEAD_GAS_VALUE.getValue());
				}
			}
		});
	}

	@JsonIgnore
	public void calcGasValue() {
		long now = (System.currentTimeMillis() / 1000L) * 1000L;
		long last = (lastMapAddGasTime / 1000L) * 1000L;
		if (now - last >= DateUtils.MILLIS_PER_MINUTE) {
			lastMapAddGasTime = now;
			addGasValue(GasCopyMapConfig.getInstance().IN_MAP_GAS_VALUE.getValue());
		}
	}
	
	@JsonIgnore
	public void addGasValue(int value) {
		int max = GasCopyMapConfig.getInstance().PLAYER_GAS_VALUE_MAX.getValue();
		if (gasValue >= max) {
			return;
		}
		synchronized (this) {
			int newValue = gasValue + value;
			gasValue = (newValue > max ? max: newValue);
		}
		lastAddGasTime = System.currentTimeMillis();
		PacketSendUtility.sendPacket(owner, SM_GasValue_Change.valueOf(owner));
		if (gasValue >= max) {
			tunderPlayerTask();
		}
	}
	
	@JsonIgnore
	public void leaveGasCopy() {
		gasValue = 0;
		lastAddGasTime = 0L;
		lastMapAddGasTime = 0L;
		if (thunderDeadTask != null) {
			thunderDeadTask.cancel(false);
		}
		if (leaveTask != null) {
			leaveTask.cancel(false);
		}
		owner.getLifeStats().fullStoreHpAndMp();
	}
	
	@JsonIgnore
	public void tunderPlayerTask() {
		if (thunderDeadTask == null || thunderDeadTask.isCancelled() || thunderDeadTask.isDone()) {
			thunderDeadTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					owner.getLifeStats().reduceHpIgnoreEffect(Long.MAX_VALUE, owner, 0);
					triggerLeaveCopy();
				}
			}, GasCopyMapConfig.getInstance().GAS_FULL_LEAVE_TIME.getValue() * DateUtils.MILLIS_PER_SECOND);
		}
	}
	
	@JsonIgnore
	public void triggerLeaveCopy() {
		if (leaveTask == null || leaveTask.isCancelled() || leaveTask.isDone()) {
			leaveTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					GasCopyManager.getInstance().leaveGasCopy(owner);
				}
			}, GasCopyMapConfig.getInstance().TRIGGER_LEAVE_TIME.getValue() * DateUtils.MILLIS_PER_SECOND);
		}
	}
	
	@JsonIgnore
	public void cancelLeaveTask() {
		if (leaveTask != null) {
			leaveTask.cancel(false);
		}
	}

	public long getLastMapAddGasTime() {
		return lastMapAddGasTime;
	}

	public void setLastMapAddGasTime(long lastMapAddGasTime) {
		this.lastMapAddGasTime = lastMapAddGasTime;
	}
	
	@JsonIgnore
	public boolean cannotRelive() {
		return GasCopyMapConfig.getInstance().isInGasCopyMap(owner.getMapId()) && isGasFull();
	}
	
	@JsonIgnore
	public boolean isGasFull() {
		return gasValue >= GasCopyMapConfig.getInstance().PLAYER_GAS_VALUE_MAX.getValue();
	}
	
}
