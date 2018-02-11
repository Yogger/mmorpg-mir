package com.mmorpg.mir.model.kingofwar.controller;

import java.util.Arrays;
import java.util.concurrent.Future;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.controllers.BossController;
import com.mmorpg.mir.model.gameobjects.Boss;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.StatusNpc;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.kingofwar.config.KingOfWarConfig;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.kingofwar.model.PlayerWarInfo;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.skill.model.DamageResult;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.windforce.common.utility.DateUtils;

/**
 * 禁卫军控制器
 * 
 * @author Kuang Hao
 * @since v1.0 2014-11-6
 * 
 */
public class KingOfWarGuardController extends BossController {

	// 大将军
	private Boss bigBoss;
	// 复活的状态NPC
	private StatusNpc reliveStatusNpc;

	/** 积分光环 */
	private Future<?> haloPointsFuture;
	/** 最后被攻击时间，进入战斗时间 */
	private long lastAttackedTime;

	public KingOfWarGuardController(Boss bigBoss, StatusNpc reliveStatusNpc) {
		super();
		this.bigBoss = bigBoss;
		this.reliveStatusNpc = reliveStatusNpc;
	}

	private Object haloPointsFutureLock = new Object();

	@Override
	public void onAttack(Creature creature, int skillId, long damage, DamageResult damageResult) {
		if (lastAttackedTime == 0) {
			lastAttackedTime = System.currentTimeMillis();
		}
		synchronized (haloPointsFutureLock) {
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
								Integer[] points = KingOfWarConfig.getInstance().GUARDBOSS_HALOPOINTS_VALUE.getValue();
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
		super.onAttack(creature, skillId, damage, damageResult);
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

	@Override
	public void onDie(Creature lastAttacker, int skillId) {
		super.onDie(lastAttacker, skillId);
		Player player = null;
		if (lastAttacker instanceof Summon) {
			player = ((Summon) lastAttacker).getMaster();
		} else {
			player = (Player) lastAttacker;
		}
		KingOfWarBigBossController bigBossController = (KingOfWarBigBossController) bigBoss.getController();

		// 复活点出现
		SpawnManager.getInstance().bringIntoWorld(reliveStatusNpc, 1);

		// 击杀禁卫军积分奖励
		KingOfWarManager.getInstance().getPlayerWarInfos().get(player.getObjectId())
				.increasePoints(KingOfWarConfig.getInstance().KILL_GUARD_POINTS.getValue());

		for (PlayerWarInfo info : KingOfWarManager.getInstance().getPlayerWarInfos().values()) {
			if (info.getPlayer().getController() instanceof KingOfWarPlayerController) {
				if (SessionManager.getInstance().isOnline(info.getPlayer().getObjectId())) {
					if (info.getPlayer().getCountry() == player.getCountry() && info.getPlayer() != player) {
						info.increasePoints(KingOfWarConfig.getInstance().KILL_GUARD_COUNTRY_POINTS.getValue());
					}
				}
			}
		}

		// 通报
		I18nUtils i18nUtils = I18nUtils.valueOf("202002");
		i18nUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
		i18nUtils.addParm("guard", I18nPack.valueOf(getOwner().getObjectResource().getName()));
		ChatManager.getInstance().sendSystem(41004, i18nUtils, null,
				Integer.valueOf(KingOfWarConfig.getInstance().MAPID.getValue()));

		// 通报
		I18nUtils i18nUtils1 = I18nUtils.valueOf("10108");
		i18nUtils1.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
		i18nUtils1.addParm("guard", I18nPack.valueOf(getOwner().getObjectResource().getName()));
		ChatManager.getInstance().sendSystem(11001, i18nUtils1, null);

		bigBossController.guardDie();
	}

}
