package com.mmorpg.mir.model.controllers.stats;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.mmorpg.mir.model.controllers.packet.SM_ATTACK_STATUS;
import com.mmorpg.mir.model.controllers.packet.SM_STATUPDATE_BARRIER;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.task.tasks.PacketBroadcaster.BroadcastMode;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.windforce.common.utility.DateUtils;

public abstract class CreatureLifeStats<T extends Creature> {
	protected static final Logger log = Logger.getLogger(CreatureLifeStats.class);

	protected long currentHp;
	protected long currentMp;

	volatile protected boolean alreadyDead = Boolean.FALSE;

	private AtomicBoolean isDead = new AtomicBoolean(false);

	private ReentrantLock barrierLock = new ReentrantLock();

	protected long currentBarrier;

	protected Creature owner;

	protected final ReentrantLock hpLock = new ReentrantLock();
	private final ReentrantLock mpLock = new ReentrantLock();

	protected Future<?> lifeRestoreTask;

	private Future<?> barrierRestoreTask;

	public CreatureLifeStats(Creature owner, long currentHp, long currentMp) {
		super();
		this.owner = owner;
		this.currentHp = currentHp;
		this.currentMp = currentMp;
		this.currentBarrier = getMaxBarrier();
	}

	/**
	 * @return the owner
	 */
	public Creature getOwner() {
		return owner;
	}

	/**
	 * @return the currentHp
	 */
	public long getCurrentHp() {
		return currentHp;
	}

	/**
	 * @return the currentMp
	 */
	public long getCurrentMp() {
		return currentMp;
	}

	/**
	 * @return maxHp of creature according to stats
	 */
	public long getMaxHp() {
		long maxHp = owner.getGameStats().getCurrentStat(StatEnum.MAXHP);
		if (maxHp == 0) {
			maxHp = 1;
			log.warn("CHECKPOINT: maxhp is 0 :" + this.getOwner());
		}
		return maxHp;
	}

	/**
	 * @return maxMp of creature according to stats
	 */
	public long getMaxMp() {
		long maxMp = owner.getGameStats().getCurrentStat(StatEnum.MAXMP);
		if (maxMp == 0) {
			maxMp = 1;
			log.warn("CHECKPOINT: maxMp is 0 :" + this.getOwner());
		}
		return maxMp;
	}

	/**
	 * @return the alreadyDead There is no setter method cause life stats should
	 *         be completely renewed on revive
	 */
	public boolean isAlreadyDead() {
		return alreadyDead;
	}

	public long reduceBarrier(long value) {
		barrierLock.lock();
		try {
			long newBarrier = this.currentBarrier - value;

			if (newBarrier < 0)
				newBarrier = 0;

			this.currentBarrier = newBarrier;
		} finally {
			barrierLock.unlock();
		}

		onReduceBarrier();
		return currentBarrier;
	}

	protected void onIncreaseBarrier(long value) {
		sendBarrierPacketUpdate();
	}

	public void cancelBarrierRestoreTask() {
		barrierLock.lock();
		try {
			if (barrierRestoreTask != null && !barrierRestoreTask.isCancelled()) {
				barrierRestoreTask.cancel(false);
				this.barrierRestoreTask = null;
			}
		} finally {
			barrierLock.unlock();
		}
	}

	private void onReduceBarrier() {
		sendBarrierPacketUpdate();
		barrierLock.lock();
		try {
			if (barrierRestoreTask == null || barrierRestoreTask.isCancelled()) {
				barrierRestoreTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new BarrierRestoreTask(this),
						1000, DateUtils.MILLIS_PER_SECOND * 5);
			}
		} finally {
			barrierLock.unlock();
		}
	}

	public void restoreBarrier() {
		barrierLock.lock();
		try {
			if (currentBarrier < getMaxBarrier()) {
				if (barrierRestoreTask == null || barrierRestoreTask.isCancelled()) {
					barrierRestoreTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(
							new BarrierRestoreTask(this), 1000, DateUtils.MILLIS_PER_SECOND * 5);
				}
			}
		} finally {
			barrierLock.unlock();
		}
	}

	public long increaseBarrier(long value) {
		barrierLock.lock();
		try {
			if (isAlreadyDead()) {
				return 0;
			}
			long newBarrier = this.currentBarrier + value;
			if (newBarrier > getMaxBarrier()) {
				newBarrier = getMaxBarrier();
			}
			if (currentBarrier != newBarrier) {
				this.currentBarrier = newBarrier;
				onIncreaseBarrier(value);
			}
		} finally {
			barrierLock.unlock();
		}

		return currentBarrier;
	}

	public void setCurrentBarrierPercent(long barrierPercent) {
		barrierLock.lock();
		try {
			this.currentBarrier = getMaxBarrier() * barrierPercent / 100;
		} finally {
			barrierLock.unlock();
		}
	}

	public boolean isFullyRestoredBarrier() {
		return getMaxBarrier() <= currentBarrier;
	}

	private static class BarrierRestoreTask implements Runnable {
		private CreatureLifeStats<?> lifeStats;

		private BarrierRestoreTask(CreatureLifeStats<?> lifeStats) {
			this.lifeStats = lifeStats;
		}

		@Override
		public void run() {
			if (lifeStats.isAlreadyDead()) {
				return;
			}
			if (lifeStats.getCurrentBarrier() >= lifeStats.getMaxBarrier()
					|| lifeStats.getOwner().getPosition() == null || !lifeStats.getOwner().isSpawned()) {
				lifeStats.cancelBarrierRestoreTask();
			} else {
				// 每5秒回复
				long restoreBarrier = Math.round((lifeStats.getOwner().getGameStats()
						.getCurrentStat(StatEnum.BARRIER_RESTORE) * 1.0 / 10000)
						* lifeStats.getMaxBarrier());
				lifeStats.increaseBarrier(restoreBarrier);
			}
		}
	}

	public long getMaxBarrier() {
		long barrier = 0;
		try {
			if (owner.getGameStats() == null) {
				return 0;
			}
			barrier = owner.getGameStats().getCurrentStat(StatEnum.BARRIER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return barrier;
	}

	public long getCurrentBarrier() {
		return currentBarrier;
	}

	public void setCurrentBarrier(long currentBarrier) {
		this.currentBarrier = currentBarrier;
	}

	/**
	 * This method is called whenever caller wants to absorb creatures's HP
	 * 
	 * @param value
	 * @param attacker
	 * @return currentHp
	 */
	public long reduceHp(long value, Creature attacker, int skillId) {
		hpLock.lock();
		try {
			if (alreadyDead)
				return 0;
			if (attacker != null && owner.getEffectController().isAbnoramlSet(EffectId.DAMAGESUCK)) {
				increaseHp(value);
				// SM_PeriodicAction sm =
				// SM_PeriodicAction.valueOf(attacker.getObjectId(), skillId,
				// -value, getOwner()
				// .getObjectId(), null);
				// PacketSendUtility.sendPacket(getOwner(), sm);
			} else {
				long newHp = this.currentHp - value;

				if (newHp <= 0) {
					if (owner.getEffectController().isAbnoramlSet(EffectId.UNDEAD)) {
						newHp = 1;
					} else {
						newHp = 0;
						alreadyDead = Boolean.TRUE;
					}
				}
				this.currentHp = newHp;
				onReduceHp();
			}
		} finally {
			hpLock.unlock();
		}

		sendHpDamage(attacker, skillId);
		if (alreadyDead) {
			if (isDead.compareAndSet(Boolean.FALSE, Boolean.TRUE)) {
				getOwner().getController().onDie(attacker, skillId);
			}
		}
		return currentHp;
	}

	/**
	 * This method is called whenever caller wants to absorb creatures's HP
	 * 
	 * @param value
	 * @param attacker
	 * @return currentHp
	 */
	public long reduceHpIgnoreEffect(long value, Creature attacker, int skillId) {
		hpLock.lock();
		try {
			if (alreadyDead)
				return 0;
			long newHp = this.currentHp - value;
			if (newHp < 0) {
				newHp = 0;
			}
			alreadyDead = Boolean.TRUE;
			this.currentHp = newHp;
			onReduceHp();
		} finally {
			hpLock.unlock();
		}

		sendHpDamage(attacker, skillId);
		if (alreadyDead) {
			if (isDead.compareAndSet(Boolean.FALSE, Boolean.TRUE)) {
				getOwner().getController().onDie(attacker, skillId);
			}
		}
		return currentHp;
	}

	public void sendHpDamage(Creature attacker, int skillId) {
		SM_ATTACK_STATUS msg = SM_ATTACK_STATUS.valueOf(getOwner());
		Creature owner = getOwner();

		if (owner.isObjectType(ObjectType.SUMMON)) {
			PacketSendUtility.sendPacket(((Summon) owner).getMaster(), msg);
		}

		if (attacker != null) {
			// 发送消息给玩家，自己的血被扣除了
			if (attacker instanceof Player) {
				PacketSendUtility.sendPacket((Player) attacker, msg);
			} else if (attacker instanceof Summon) {
				PacketSendUtility.sendPacket(((Summon) attacker).getMaster(), msg);
			}
		}
	}

	/**
	 * This method is called whenever caller wants to absorb creatures's HP
	 * 
	 * @param value
	 * @return currentMp
	 */
	public long reduceMp(long value) {
		mpLock.lock();
		try {
			long newMp = this.currentMp - value;

			if (newMp < 0)
				newMp = 0;

			this.currentMp = newMp;
		} finally {
			mpLock.unlock();
		}

		onReduceMp();

		return currentMp;
	}

	/**
	 * This method is called whenever caller wants to restore creatures's HP
	 * 
	 * @param value
	 * @return currentHp
	 */
	public long increaseHp(long value) {
		hpLock.lock();
		try {
			if (isAlreadyDead()) {
				return 0;
			}
			if (value < 0) {
				throw new RuntimeException("加血出现负值!value[" + value + "]");
			}
			long newHp = this.currentHp + value;
			if (newHp > getMaxHp()) {
				newHp = getMaxHp();
			}
			if (currentHp != newHp) {
				this.currentHp = newHp;
				sendHpDamage(null, 0);
				onIncreaseHp(value);
			}
		} finally {
			hpLock.unlock();
		}
		return currentHp;
	}

	/**
	 * This method is called whenever caller wants to restore creatures's MP
	 * 
	 * @param value
	 * @return currentMp
	 */
	public long increaseMp(long value) {
		mpLock.lock();

		try {
			if (isAlreadyDead()) {
				return 0;
			}
			long newMp = this.currentMp + value;
			if (newMp > getMaxMp()) {
				newMp = getMaxMp();
			}
			if (currentMp != newMp) {
				this.currentMp = newMp;
				onIncreaseMp(value);
			}
		} finally {
			mpLock.unlock();
		}

		return currentMp;
	}

	/**
	 * Restores HP with value set as HP_RESTORE_TICK
	 */
	public void restoreHp() {
		// TODO 测试使用
		long value = 50 + getOwner().getGameStats().getCurrentStat(StatEnum.REGEN_HP);
		increaseHp(value);
	}

	/**
	 * Restores HP with value set as MP_RESTORE_TICK
	 */
	public void restoreMp() {
		// TODO 测试使用
		long value = 1 + getOwner().getGameStats().getCurrentStat(StatEnum.REGEN_MP);
		increaseMp(value);
	}

	/**
	 * Will trigger restore task if not already
	 */
	protected void triggerRestoreTask() {
		// 暂时没有自动恢复功能
		// if (lifeRestoreTask == null && !alreadyDead) {
		// // 这里提交一个任务
		// lifeRestoreTask =
		// LifeStatsRestoreService.getInstance().scheduleRestoreTask(this);
		// }
	}

	/**
	 * Cancel currently running restore task
	 */
	public void cancelRestoreTask() {
		if (lifeRestoreTask != null && !lifeRestoreTask.isCancelled()) {
			lifeRestoreTask.cancel(false);
			this.lifeRestoreTask = null;
		}
	}

	/**
	 * 
	 * @return true or false
	 */
	public boolean isFullyRestoredHpMp() {
		return getMaxHp() == currentHp && getMaxMp() == currentMp;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isFullyRestoredHp() {
		return getMaxHp() <= currentHp;
	}

	public boolean isFullyRestoredMp() {
		return getMaxMp() <= currentMp;
	}

	/**
	 * The purpose of this method is synchronize current HP and MP with updated
	 * MAXHP and MAXMP stats This method should be called only on creature load
	 * to game or player level up
	 */
	public void synchronizeWithMaxStats() {
		long maxHp = getMaxHp();
		if (currentHp != maxHp)
			currentHp = maxHp;
		long maxMp = getMaxMp();
		if (currentMp != maxMp)
			currentMp = maxMp;
	}

	public void balanceHpMp() {
		long maxHp = getMaxHp();
		if (currentHp > maxHp) {
			currentHp = maxHp;
		}
		long maxMp = getMaxMp();
		if (currentMp > maxMp)
			currentMp = maxMp;
	}

	/**
	 * The purpose of this method is synchronize current HP and MP with MAXHP
	 * and MAXMP when max stats were decreased below current level
	 * 
	 * 
	 */
	public void updateCurrentStats() {
		long maxHp = getMaxHp();
		if (maxHp < currentHp)
			currentHp = maxHp;

		long maxMp = getMaxMp();
		if (maxMp < currentMp)
			currentMp = maxMp;

		if (!isFullyRestoredHpMp())
			triggerRestoreTask();
	}

	protected abstract void onIncreaseMp(long value);

	protected abstract void onReduceMp();

	protected abstract void onIncreaseHp(long value);

	protected abstract void onReduceHp();

	/**
	 * 
	 * @param value
	 * @return
	 */
	public int increaseFp(int value) {
		return 0;
	}

	/**
	 * Cancel all tasks when player logout
	 */
	public void cancelAllTasks() {
		cancelRestoreTask();
	}

	/**
	 * This method can be used for Npc's to fully restore its HP and remove dead
	 * state of lifestats
	 * 
	 * @param hpPercent
	 */
	public void setCurrentHpPercent(long hpPercent) {
		hpLock.lock();
		try {
			long maxHp = getMaxHp();
			this.currentHp = (long) ((long) maxHp * hpPercent / 100);

			if (this.currentHp > 0) {
				this.alreadyDead = Boolean.FALSE;
				this.isDead.set(Boolean.FALSE);
				owner.getController().onRelive();
			}
		} finally {
			hpLock.unlock();
		}
	}

	/**
	 * @param hp
	 */
	public void setCurrentHp(int hp) {
		hpLock.lock();
		try {
			this.currentHp = hp;

			if (this.currentHp > 0) {
				this.alreadyDead = Boolean.FALSE;
				this.isDead.set(Boolean.FALSE);
				owner.getController().onRelive();
			}

			if (this.currentHp < getMaxHp())
				onReduceHp();
		} finally {
			hpLock.unlock();
		}
	}

	public long setCurrentMp(long value) {
		mpLock.lock();
		try {
			long newMp = value;

			if (newMp < 0)
				newMp = 0;

			this.currentMp = newMp;
		} finally {
			mpLock.unlock();
		}

		onReduceMp();

		return currentMp;
	}

	/**
	 * This method can be used for Npc's to fully restore its MP
	 * 
	 * @param mpPercent
	 */
	public void setCurrentMpPercent(long mpPercent) {
		mpLock.lock();
		try {
			long maxMp = getMaxMp();
			this.currentMp = maxMp * mpPercent / 100;

		} finally {
			mpLock.unlock();
		}
	}

	/**
	 * 满血满蓝
	 */
	protected void fullStoreHpAndMp(boolean isRelive) {
		this.setCurrentMpPercent(100);
		if (isRelive) {
			this.setCurrentHpPercent(100);
		} else {
			this.increaseHp(getMaxHp() - getCurrentHp());
		}
		setCurrentBarrierPercent(100L);
		sendBarrierPacketUpdate();
	}

	public void sendBarrierPacketUpdate() {
		owner.addPacketBroadcastMask(BroadcastMode.UPDATE_PLAYER_BARRIER_STAT);
	}

	public void sendBarrierPacketUpdateImpl() {
		if (owner == null)
			return;
		// 这里推送BARRIER的变化量
		PacketSendUtility.broadcastPacketAndReceiver(owner, SM_STATUPDATE_BARRIER.valueOf(owner));
	}

	public float getHpPercentage() {
		return (float) (100.0F * currentHp / getMaxHp());
	}

	public boolean isHpBlowPercentage(int percent) {
		if (getHpPercentage() <= percent) {
			return true;
		}
		return false;
	}

	/**
	 * This method should be called after creature's revival For creatures -
	 * trigger hp regeneration For players - trigger hp/mp/fp regeneration (in
	 * overriding method)
	 */
	public void triggerRestoreOnRevive() {
		this.triggerRestoreTask();
	}

}
