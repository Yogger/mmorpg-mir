package com.mmorpg.mir.model.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.h2.util.New;

import com.mmorpg.mir.model.boss.config.BossConfig;
import com.mmorpg.mir.model.boss.packet.SM_Boss_DamageRank;
import com.mmorpg.mir.model.boss.vo.BossDamageVO;
import com.mmorpg.mir.model.gameobjects.Boss;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.skill.model.DamageResult;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;

public class BossController extends MonsterController {

	private boolean respwan = false;

	private boolean damageBroad = true;

	@Override
	public Boss getOwner() {
		return (Boss) super.getOwner();
	}

	@Override
	public void onAttack(Creature creature, int skillId, long damage, DamageResult damageResult) {
		// 启动伤害排名通报
		broadDamage();
		super.onAttack(creature, skillId, damage, damageResult);
	}

	/**
	 * 广播伤害
	 */
	synchronized protected void broadDamage() {
		if (getOwner().getBossScheme() != null && getOwner().getBossScheme().getBossResource().isElite()) {
			// 精英怪不通报伤害
			return;
		}
		if (!damageBroad) {
			return;
		}
		if (damageBroadFuture == null || damageBroadFuture.isCancelled()) {
			damageBroadFuture = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					Map<Player, Long> playerDamages = getOwner().getDamages();
					if (playerDamages.isEmpty() || getOwner().getLifeStats().isAlreadyDead()
							|| !getOwner().getPosition().isSpawned()) {
						damageBroadFuture.cancel(false);
						return;
					}
					Map<Integer, Player> rankDamages = getOwner().getAggroList().getPlayerDamageRank(playerDamages);

					Map<Integer, BossDamageVO> damageVOs = New.hashMap();
					// 发送前10名的伤害
					for (int i = 1; i <= BossConfig.getInstance().SHOW_DAMAGERANK_SIZE.getValue(); i++) {
						if (!rankDamages.containsKey(i)) {
							break;
						}
						Player player = rankDamages.get(i);
						BossDamageVO vo = BossDamageVO.valueOf(player, player.getName(), player.getPlayerEnt()
								.getServer(), playerDamages.get(player), i);
						damageVOs.put(i, vo);
					}
					// 发送自己的伤害
					for (VisibleObject vi : getOwner().getKnownList()) {
						if (vi instanceof Player) {
							Player player = (Player) vi;
							Map<Integer, BossDamageVO> tempDamageVOs = new HashMap<Integer, BossDamageVO>(damageVOs);
							if (!damageVOs.containsValue(BossDamageVO.valueOf(player, player.getName(), player
									.getPlayerEnt().getServer(), 0, 0))) {
								long damage = 0;
								if (playerDamages.containsKey(vi)) {
									damage = playerDamages.get(vi);
									int rank = 0;
									for (Entry<Integer, Player> entry : rankDamages.entrySet()) {
										if (entry.getValue() == player) {
											rank = entry.getKey();
										}
									}
									BossDamageVO mineVO = BossDamageVO.valueOf(player, player.getName(), player
											.getPlayerEnt().getServer(), damage, rank);
									tempDamageVOs.put(rank, mineVO);
								}
							}
							long maxHp = getOwner().getGameStats().getCurrentStat(StatEnum.MAXHP);
							PacketSendUtility.sendPacket((Player) vi, SM_Boss_DamageRank.valueOf(tempDamageVOs, maxHp));
						}
					}

				}
			}, 2000, 2000);
		}
	}

	@Override
	public void onDie(Creature lastAttacker, int skillId) {
		super.onDie(lastAttacker, skillId);
		Boss owner = getOwner();
		if (lastAttacker instanceof Player) {
			if (owner.getBossScheme() != null) {
				owner.getBossScheme().getBossHistory().addKiller((Player) lastAttacker);
				owner.getBossScheme().getBossHistory().update();
			}
		} else if (lastAttacker instanceof Summon) {
			if (owner.getBossScheme() != null) {
				owner.getBossScheme().getBossHistory().addKiller(((Summon) lastAttacker).getMaster());
				owner.getBossScheme().getBossHistory().update();
			}
		}

		if (damageBroadFuture != null && !damageBroadFuture.isCancelled()) {
			damageBroadFuture.cancel(false);
		}
	}

	@Override
	public void onDespawn() {
		super.onDespawn();
		if (damageBroadFuture != null && !damageBroadFuture.isCancelled()) {
			damageBroadFuture.cancel(false);
		}
	}

	public boolean isRespwan() {
		return respwan;
	}

	public void setRespwan(boolean respwan) {
		this.respwan = respwan;
	}

	public boolean isBroad() {
		return damageBroad;
	}

	public void setBroad(boolean broad) {
		this.damageBroad = broad;
	}

}
