package com.mmorpg.mir.model.controllers.service;

import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.controllers.stats.CreatureLifeStats;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.utils.ThreadPoolManager;

@Component
public class LifeStatsRestoreService {
	private static final int DEFAULT_DELAY = 6000;

	private static LifeStatsRestoreService self;

	public static LifeStatsRestoreService getInstance() {
		return self;
	}

	@PostConstruct
	protected final void init() {
		self = this;
	}

	/**
	 * HP and MP restoring task
	 * 
	 * @param creature
	 * @return Future<?>
	 */
	public Future<?> scheduleRestoreTask(CreatureLifeStats<? extends Creature> lifeStats) {
		return ThreadPoolManager.getInstance().scheduleAtFixedRate(new HpMpRestoreTask(lifeStats), 1700, DEFAULT_DELAY);
	}

	/**
	 * HP restoring task
	 * 
	 * @param lifeStats
	 * @return
	 */
	public Future<?> scheduleHpRestoreTask(CreatureLifeStats<? extends Creature> lifeStats) {
		return ThreadPoolManager.getInstance().scheduleAtFixedRate(new HpRestoreTask(lifeStats), 1700, DEFAULT_DELAY);
	}

	private static class HpRestoreTask implements Runnable {
		private CreatureLifeStats<?> lifeStats;

		private HpRestoreTask(CreatureLifeStats<?> lifeStats) {
			this.lifeStats = lifeStats;
		}

		@Override
		public void run() {
			if (lifeStats.isAlreadyDead() || lifeStats.isFullyRestoredHp()) {
				lifeStats.cancelRestoreTask();
			} else {
				lifeStats.restoreHp();
			}
		}
	}

	private static class HpMpRestoreTask implements Runnable {
		private CreatureLifeStats<?> lifeStats;

		private HpMpRestoreTask(CreatureLifeStats<?> lifeStats) {
			this.lifeStats = lifeStats;
		}

		@Override
		public void run() {
			if (lifeStats.isAlreadyDead() || lifeStats.isFullyRestoredHpMp()) {
				lifeStats.cancelRestoreTask();
			} else {
				lifeStats.restoreHp();
				lifeStats.restoreMp();
			}
		}
	}
}
