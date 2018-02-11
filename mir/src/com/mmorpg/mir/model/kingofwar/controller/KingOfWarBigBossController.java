package com.mmorpg.mir.model.kingofwar.controller;

import java.util.Arrays;
import java.util.concurrent.Future;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.controllers.BossController;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.kingofwar.config.KingOfWarConfig;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.kingofwar.model.PlayerWarInfo;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.model.DamageResult;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.windforce.common.utility.DateUtils;

/**
 * 大将军控制器
 * 
 * @author Kuang Hao
 * @since v1.0 2014-11-6
 * 
 */
public class KingOfWarBigBossController extends BossController {
	// 3个禁卫守卫
	private int guardSurvivalCount = 3;

	private KingOfWarManager kingOfWarManager;

	/** 积分光环 */
	private Future<?> haloPointsFuture;
	/** 最后被攻击时间，进入战斗时间 */
	private long lastAttackedTime;

	//
	// private Integer[] needNoticeHp = new Integer[] { 10, 5, 2 };
	// /** 已经广播的血量 */
	// private Set<Integer> noticed = new HashSet<Integer>();

	public KingOfWarBigBossController(KingOfWarManager kingOfWarManager) {
		super();
		this.kingOfWarManager = kingOfWarManager;
	}

	private Object haloPointsFutureLock = new Object();

	@Override
	public void onAttack(Creature creature, int skillId, long damage, DamageResult damageResult) {
		if (lastAttackedTime == 0) {
			lastAttackedTime = System.currentTimeMillis();
		}
		Player player = null;
		if (creature instanceof Player) {
			player = (Player) creature;
		}
		if (creature instanceof Summon) {
			player = ((Summon) creature).getMaster();
		}
		if (player != null) {
			if (kingOfWarManager.getPlayerWarInfos().get(creature.getObjectId()) != null) {
				kingOfWarManager.getPlayerWarInfos().get(creature.getObjectId()).increaseBossDamage(damage);
			}
		}
		super.onAttack(creature, skillId, damage, damageResult);

		synchronized (haloPointsFutureLock) {
			// for (int noticeHp : needNoticeHp) {
			// if (!noticed.contains(noticeHp) &&
			// getOwner().getLifeStats().isHpBlowPercentage(noticeHp)) {
			// if (noticeHp == 10) {
			// I18nUtils i18nUtils1 = I18nUtils.valueOf("402006");
			// ChatManager.getInstance().sendSystem(71001, i18nUtils1, null);
			// } else if (noticeHp == 5) {
			// I18nUtils i18nUtils1 = I18nUtils.valueOf("402007");
			// ChatManager.getInstance().sendSystem(71001, i18nUtils1, null);
			// } else if (noticeHp == 2) {
			// I18nUtils i18nUtils1 = I18nUtils.valueOf("402008");
			// ChatManager.getInstance().sendSystem(71001, i18nUtils1, null);
			// }
			// noticed.add(noticeHp);
			// }
			// }
			if (haloPointsFuture == null || haloPointsFuture.isCancelled()) {
				haloPointsFuture = ThreadPoolManager.getInstance().scheduleAtFixedRate(
						new Runnable() {
							@Override
							public void run() {
								if (getOwner().getLifeStats().isAlreadyDead()) {
									haloPointsFuture.cancel(false);
								}
								int battleTime = (int) (System.currentTimeMillis() - lastAttackedTime);
								Integer[] times = KingOfWarConfig.getInstance().BOSS_HALOPOINTS_TIME.getValue();
								int index = 0;
								for (int time : times) {
									if (time * DateUtils.MILLIS_PER_SECOND <= battleTime) {
										index++;
									}
								}
								Integer[] points = KingOfWarConfig.getInstance().BIGBOSS_HALOPOINTS_VALUE.getValue();
								Arrays.sort(points);

								for (VisibleObject vo : getOwner().getKnownList()) {
									if (vo instanceof Player) {
										PlayerWarInfo playerWarInfo = KingOfWarManager.getInstance()
												.getPlayerWarInfos().get(vo.getObjectId());
										if (playerWarInfo != null) {
											playerWarInfo.increasePoints(points[times.length - index]);
										}
									}
								}

							}
						}, KingOfWarConfig.getInstance().HALOPOINTS_PERIOD.getValue(),
						KingOfWarConfig.getInstance().HALOPOINTS_PERIOD.getValue());
			}
		}
	}

	@Override
	protected void broadDamage() {
		if (!getOwner().getEffectController().isAbnoramlSet(EffectId.GOD)) {
			super.broadDamage();
		}
	}

	@Override
	public void onAtSpawnLocation() {
		synchronized (haloPointsFutureLock) {
			if (haloPointsFuture != null && !haloPointsFuture.isCancelled()) {
				lastAttackedTime = 0;
				haloPointsFuture.cancel(false);
			}
		}

	}

	@Override
	public void onDespawn() {
		super.onDespawn();
		synchronized (haloPointsFutureLock) {
			if (haloPointsFuture != null && !haloPointsFuture.isCancelled()) {
				lastAttackedTime = 0;
				haloPointsFuture.cancel(false);
			}
		}
	}

	/**
	 * 禁卫军死亡
	 */
	synchronized public void guardDie() {
		guardSurvivalCount--;
		if (guardSurvivalCount == 0) {
			getOwner().getEffectController().unsetAbnormal(EffectId.GOD.getEffectId(), true);
			// // 通报
			// I18nUtils i18nUtils = I18nUtils.valueOf("202004");
			// ChatManager.getInstance().sendSystem(41004, i18nUtils, null,
			// Integer.valueOf(KingOfWarConfig.getInstance().MAPID.getValue()));

			// 通报
			// I18nUtils i18nUtils = I18nUtils.valueOf("402005");
			// ChatManager.getInstance().sendSystem(71001, i18nUtils, null);
			// 通报
			I18nUtils i18nUtils1 = I18nUtils.valueOf("10110");
			ChatManager.getInstance().sendSystem(11001, i18nUtils1, null);
		}
	}

	@Override
	public void onDie(Creature lastAttacker, int skillId) {
		super.onDie(lastAttacker, skillId);
		Player player = null;
		if (lastAttacker instanceof Summon) {
			player = ((Summon) lastAttacker).getMaster();
		} else {
			player = (Player) lastAttacker;
		}
		// 击杀大将军积分奖励
		kingOfWarManager.getPlayerWarInfos().get(player.getObjectId())
				.increasePoints(KingOfWarConfig.getInstance().KILL_BIGBOSS_POINTS.getValue());
		// 咸阳战结束
		kingOfWarManager.end(player);
	}
}
