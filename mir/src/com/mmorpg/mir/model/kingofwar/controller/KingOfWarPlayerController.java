package com.mmorpg.mir.model.kingofwar.controller;

import java.util.Map;
import java.util.Map.Entry;

import org.h2.util.New;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.controllers.PlayerController;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.kingofwar.config.KingOfWarConfig;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.kingofwar.model.PlayerWarInfo;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.model.DamageResult;

public class KingOfWarPlayerController extends PlayerController {

	/** 伤害记录 */
	private Map<PlayerWarInfo, Long> damageHistory = New.hashMap();

	@Override
	public void onAttack(Creature creature, int skillId, long damage, DamageResult damageResult) {
		if (getOwner().getEffectController().isAbnoramlSet(EffectId.GOD)) {
			if (!KingOfWarManager.getInstance().isProtect()) {
				getOwner().getEffectController().unsetAbnormal(EffectId.GOD.getEffectId());
			}
		}
		Player player = null;
		if (creature instanceof Player) {
			player = (Player) creature;
		} else if (creature instanceof Summon) {
			player = ((Summon) creature).getMaster();
		}
		if (player != null) {
			PlayerWarInfo playerWarInfo = KingOfWarManager.getInstance().getPlayerWarInfos().get(player.getObjectId());
			if (playerWarInfo != null) {
				damageHistory.put(playerWarInfo, System.currentTimeMillis());
			}
		}
		super.onAttack(creature, skillId, damage, damageResult);

	}

	@Override
	public void onRelive() {
		super.onRelive();
		PlayerWarInfo warInfo = KingOfWarManager.getInstance().getPlayerWarInfos().get(getOwner().getObjectId());
		if (warInfo != null) {
			warInfo.refreshBombBuff();
		}
	}

	@Override
	public boolean canUserSkill(Integer skillId) {
		if (getOwner().getLifeStats().isAlreadyDead()) {
			return false;
		}
		if (skillId == KingOfWarConfig.getInstance().BOMB_SKILL.getValue()
				&& getOwner().getMapId() == KingOfWarConfig.getInstance().MAPID.getValue()) {
			PlayerWarInfo warInfo = KingOfWarManager.getInstance().getPlayerWarInfos().get(getOwner().getObjectId());
			if (warInfo != null) {
				if (warInfo.isMaxBombValue()) {
					warInfo.clearBombValue();
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 助攻
	 * 
	 * @param killer
	 */
	private void addAssisPoints(Creature killer) {
		long now = System.currentTimeMillis();
		for (Entry<PlayerWarInfo, Long> entry : damageHistory.entrySet()) {
			if (entry.getKey().getPlayer() != killer
					&& (now - entry.getValue()) < KingOfWarConfig.getInstance().ASSIST_PLAYER_TIME.getValue()) {
				entry.getKey().increasePoints(KingOfWarConfig.getInstance().ASSIST_PLAYER_POINTS.getValue());
			}
		}
		damageHistory.clear();
	}

	@Override
	public void onDie(Creature lastAttacker, int skillId) {
		super.onDie(lastAttacker, skillId);
		if (lastAttacker instanceof Summon) {
			lastAttacker = ((Summon) lastAttacker).getMaster();
		}
		if (lastAttacker instanceof Player) {
			Player attacker = (Player) lastAttacker;
			PlayerWarInfo warInfo = KingOfWarManager.getInstance().getPlayerWarInfos().get(attacker.getObjectId());
			PlayerWarInfo owernWarInfo = KingOfWarManager.getInstance().getPlayerWarInfos()
					.get(getOwner().getObjectId());
			if (warInfo != null && owernWarInfo != null) {
				warInfo.increaseKillCount(getOwner().getObjectId());
				if (warInfo.getKillCounts().get(getOwner().getObjectId()) < KingOfWarConfig.getInstance().KILLED_PLAYER_NOPOINTS_COUNT
						.getValue()) {
					// 连杀同一个玩家没有积分
					warInfo.increasePoints(KingOfWarConfig.getInstance().KILLOR_PLAYER_POINTS.getValue());
					addAssisPoints(lastAttacker);
					owernWarInfo.increasePoints(KingOfWarConfig.getInstance().KILLED_PLAYER_POINTS.getValue());
				}
				owernWarInfo.setContinueKill(0);
				owernWarInfo.addBombValueByKilled();
			}

			synchronized (KingOfWarManager.getInstance().firstKills) {
				if (!KingOfWarManager.getInstance().firstKills.containsKey(attacker.getCountryId())) {
					KingOfWarManager.getInstance().firstKills.put(attacker.getCountryId(), true);
					I18nUtils i18nUtils = I18nUtils.valueOf("10102");
					i18nUtils.addParm("name", I18nPack.valueOf(attacker.getName()));
					i18nUtils.addParm("country", I18nPack.valueOf(attacker.getCountry().getName()));
					ChatManager.getInstance().sendSystem(11001, i18nUtils, null);
				}
			}
		}
	}

}
