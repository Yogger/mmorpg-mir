package com.mmorpg.mir.model.controllers.stats;

import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.controllers.packet.SM_STATUPDATE_DP;
import com.mmorpg.mir.model.controllers.packet.SM_STATUPDATE_HP;
import com.mmorpg.mir.model.controllers.packet.SM_STATUPDATE_MP;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.relive.event.PlayerReliveEvent;
import com.mmorpg.mir.model.relive.manager.PlayerReliveManager;
import com.mmorpg.mir.model.task.tasks.PacketBroadcaster.BroadcastMode;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;

public class PlayerLifeStats extends CreatureLifeStats<Player> {

	private long currentDp;

	private ReentrantLock dpLock = new ReentrantLock();

	private long lastDeadTime;

	private long buyLiveTime;

	private int buyCounts;

	private String lastAttackerName;

	private long lastAttackerId;

	private Future<?> dpRestoreTask;

	public PlayerLifeStats(Creature owner, long currentHp, long currentMp, boolean die) {
		super(owner, currentHp, currentMp);
		this.currentDp = getMaxDp();
		if (currentDp == 0) {
			currentDp = ConfigValueManager.getInstance().PLAYER_INIT_DP.getValue();
		}
		this.alreadyDead = die;
	}

	public PlayerLifeStats(Creature owner, LifeStatDB lifeStatDB) {
		this(owner, lifeStatDB.getHp(), lifeStatDB.getMp(), lifeStatDB.isDie());
		this.lastDeadTime = lifeStatDB.getLastDeadTime();
		this.buyLiveTime = lifeStatDB.getBuyLiveTime();
		this.buyCounts = lifeStatDB.getBuyCounts();
		this.currentBarrier = lifeStatDB.getBarrier();
	}

	@Override
	public Player getOwner() {
		return (Player) super.getOwner();
	}

	public LifeStatDB createLifeStatDB() {
		hpLock.lock();
		try {
			return new LifeStatDB(currentHp, currentMp, alreadyDead, currentDp, lastDeadTime, buyLiveTime, buyCounts,
					lastAttackerName, lastAttackerId, currentBarrier);
		} finally {
			hpLock.unlock();
		}
	}

	public long getCurrentDp() {
		return currentDp;
	}

	public long reduceDp(long value) {
		dpLock.lock();
		try {
			long newDp = this.currentDp - value;

			if (newDp < 0)
				newDp = 0;

			this.currentDp = newDp;
		} finally {
			dpLock.unlock();
		}

		onReduceDp();

		return currentDp;
	}

	public long increaseDp(long value) {
		dpLock.lock();
		try {
			if (isAlreadyDead()) {
				return 0;
			}
			long newDp = this.currentDp + value;
			if (newDp > getMaxDp()) {
				newDp = getMaxDp();
			}
			if (currentDp != newDp) {
				this.currentDp = newDp;
				onIncreaseDp(value);
			}
		} finally {
			dpLock.unlock();
		}

		return currentDp;
	}

	@Override
	public void sendHpDamage(Creature attacker, int skillId) {
		owner.addPacketBroadcastMask(BroadcastMode.BORADCAST_DAMAGE_STAT);
	}

	protected void onIncreaseDp(long value) {
		sendDpPacketUpdate();
	}

	protected void onReduceDp() {
		sendDpPacketUpdate();
		if (dpRestoreTask == null || dpRestoreTask.isCancelled()) {
			dpRestoreTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new DpRestoreTask(this), 1000,
					DateUtils.MILLIS_PER_SECOND * 10);
		}
	}

	public void restoreDp() {
		if (currentDp < getMaxDp()) {
			if (dpRestoreTask == null || dpRestoreTask.isCancelled()) {
				dpRestoreTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new DpRestoreTask(this), 1000,
						DateUtils.MILLIS_PER_SECOND * 10);
			}
		}
	}

	@Override
	protected void onIncreaseMp(long value) {
		sendMpPacketUpdate();
	}

	@Override
	protected void onReduceMp() {
		sendMpPacketUpdate();
		triggerRestoreTask();
	}

	@Override
	protected void onIncreaseHp(long value) {
		sendHpPacketUpdate();
	}

	@Override
	protected void onReduceHp() {
		sendHpPacketUpdate();
		triggerRestoreTask();
	}

	public void sendHpPacketUpdate() {
		owner.addPacketBroadcastMask(BroadcastMode.UPDATE_PLAYER_HP_STAT);
	}

	public void fullStoreHpAndMp() {
		boolean dead = alreadyDead;
		super.fullStoreHpAndMp(alreadyDead);
		sendHpPacketUpdate();
		sendMpPacketUpdate();
		if (dead) {
			EventBusManager.getInstance().syncSubmit(PlayerReliveEvent.valueOf(getOwner().getObjectId()));
		}
	}

	public void sendHpPacketUpdateImpl() {
		if (owner == null)
			return;
		// 这里推送血量的信息
		PacketSendUtility.sendPacket((Player) owner, SM_STATUPDATE_HP.valueOf(owner));
	}

	public void sendMpPacketUpdate() {
		owner.addPacketBroadcastMask(BroadcastMode.UPDATE_PLAYER_MP_STAT);
	}

	public void sendDpPacketUpdate() {
		owner.addPacketBroadcastMask(BroadcastMode.UPDATE_PLAYER_DP_STAT);
	}

	public void sendMpPacketUpdateImpl() {
		if (owner == null)
			return;
		// 这里推送mp的变化量
		PacketSendUtility.sendPacket((Player) owner, SM_STATUPDATE_MP.valueOf(owner));
	}

	public void sendDpPacketUpdateImpl() {
		if (owner == null)
			return;
		// 这里推送dp的变化量
		PacketSendUtility.sendPacket((Player) owner, SM_STATUPDATE_DP.valueOf((Player) owner));
	}

	public void postBuyLife() {
		long now = System.currentTimeMillis();

		if (now - buyLiveTime < PlayerReliveManager.getInstance().getClearDeadCountTime()) {
			buyCounts++;
		} else {
			buyCounts = 1;
		}

		buyLiveTime = now;
	}

	@JsonIgnore
	public int getOriginalBuyCount() {
		if (System.currentTimeMillis() - buyLiveTime < PlayerReliveManager.getInstance().getClearDeadCountTime()) {
			return buyCounts + 1;
		}
		return 1;
	}

	public boolean autoReliveCd() {
		return System.currentTimeMillis() - lastDeadTime < PlayerReliveManager.getInstance().getAutoReliveCD();
	}

	public long getDp() {
		return currentDp;
	}

	public void setDp(long dp) {
		this.currentDp = dp;
	}

	public long getLastDeadTime() {
		return lastDeadTime;
	}

	public void setLastDeadTime(long lastDeadTime) {
		this.lastDeadTime = lastDeadTime;
	}

	public long getBuyLiveTime() {
		return buyLiveTime;
	}

	public void setBuyLiveTime(long buyLiveTime) {
		this.buyLiveTime = buyLiveTime;
	}

	public int getBuyCounts() {
		if (System.currentTimeMillis() - buyLiveTime >= PlayerReliveManager.getInstance().getClearDeadCountTime()) {
			return 0;
		} else {
			return buyCounts;
		}
	}

	public void setBuyCounts(int buyCounts) {
		this.buyCounts = buyCounts;
	}

	public String getLastAttackerName() {
		return lastAttackerName;
	}

	public void setLastAttackerName(String lastAttackerName) {
		this.lastAttackerName = lastAttackerName;
	}

	public long getLastAttackerId() {
		return lastAttackerId;
	}

	public void setLastAttackerId(long lastAttackerId) {
		this.lastAttackerId = lastAttackerId;
	}

	public void cancelDpRestoreTask() {
		if (dpRestoreTask != null && !dpRestoreTask.isCancelled()) {
			dpRestoreTask.cancel(false);
			this.dpRestoreTask = null;
		}
	}

	private static class DpRestoreTask implements Runnable {
		private PlayerLifeStats lifeStats;

		private DpRestoreTask(PlayerLifeStats lifeStats) {
			this.lifeStats = lifeStats;
		}

		@Override
		public void run() {
			if (lifeStats.isAlreadyDead()) {
				return;
			}
			if (lifeStats.getDp() >= lifeStats.getMaxDp() || lifeStats.getOwner().getPosition() == null
					|| !lifeStats.getOwner().isSpawned()) {
				lifeStats.cancelDpRestoreTask();
			} else {
				// 每10秒回复
				lifeStats.increaseDp(PlayerManager.getInstance().DP_INC_10SECOND.getValue()
						+ lifeStats.getOwner().getGameStats().getCurrentStat(StatEnum.DP_INCREMENT));
			}
		}
	}

	public long getMaxDp() {
		long maxDp = owner.getGameStats().getCurrentStat(StatEnum.MAXDP);
		return maxDp;
	}

	public boolean isFullyRestoredDp() {
		return getMaxDp() <= currentDp;
	}

}
