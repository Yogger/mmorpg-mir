package com.mmorpg.mir.model.gangofwar.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.controllers.BossController;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gang.manager.GangManager;
import com.mmorpg.mir.model.gang.model.Gang;
import com.mmorpg.mir.model.gangofwar.config.GangOfWarConfig;
import com.mmorpg.mir.model.gangofwar.manager.GangOfWar;
import com.mmorpg.mir.model.gangofwar.manager.GangOfWarManager;
import com.mmorpg.mir.model.gangofwar.packet.SM_GangOfWar_SealBossDamageRank;
import com.mmorpg.mir.model.gangofwar.packet.vo.GangOfWarBossDamageVO;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.skill.model.DamageResult;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.New;

public class GangOfWarSealBossController extends BossController implements GangOfWarCamp {
	private Camps camps = Camps.DEFEND;
	private GangOfWar gangOfWar;
	/** 需要广播的血量 */
	private int[] hpBroads = new int[] { 80, 50, 20 };
	/** 已经广播的血量 */
	private List<Integer> hpBroadeds = New.arrayList();

	private Map<Long, Long> gangDamage = new ConcurrentHashMap<Long, Long>();

	public GangOfWarSealBossController(GangOfWar gangOfWar) {
		this.gangOfWar = gangOfWar;
	}

	@Override
	public void onAttack(Creature creature, int skillId, long damage, DamageResult damageResult) {
		Player player = null;
		if (creature instanceof Player) {
			player = (Player) creature;
		} else if (creature instanceof Summon) {
			player = ((Summon) creature).getMaster();
		}
		if (player != null && player.getGang() != null) {
			if (!gangDamage.containsKey(player.getGang().getId())) {
				gangDamage.put(player.getGang().getId(), Long.valueOf(damage));
			} else {
				gangDamage.put(player.getGang().getId(), gangDamage.get(player.getGang().getId()) + damage);
			}
		}
		super.onAttack(creature, skillId, damage, damageResult);
		float remainHp = getOwner().getLifeStats().getHpPercentage();
		for (Integer hpBroad : hpBroads) {
			if (remainHp <= hpBroad && !hpBroadeds.contains(hpBroad)) {
				hpBroadeds.add(hpBroad);
				// 通报
				I18nUtils i18nUtils = I18nUtils.valueOf("201002");
				i18nUtils.addParm("n", I18nPack.valueOf(hpBroad + ""));
				ChatManager.getInstance().sendSystem(41008, i18nUtils, null,
						Integer.valueOf(GangOfWarConfig.getInstance().MAPID.getValue()),
						getOwner().getPosition().getInstanceId());
			}
		}

	}

	@Override
	synchronized protected void broadDamage() {
		if (damageBroadFuture == null || damageBroadFuture.isCancelled()) {
			damageBroadFuture = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					if (gangDamage.isEmpty() || getOwner().getLifeStats().isAlreadyDead()
							|| !getOwner().getPosition().isSpawned()) {
						damageBroadFuture.cancel(false);
						return;
					}

					List<GangOfWarBossDamageVO> gangDamageList = New.arrayList();
					for (Long gangId : gangDamage.keySet()) {
						gangDamageList.add(GangOfWarBossDamageVO.valueOf(GangManager.getInstance().get(gangId),
								gangDamage.get(gangId)));
					}
					Collections.sort(gangDamageList);
					Map<Integer, GangOfWarBossDamageVO> gangRanks = New.hashMap();
					int i = 1;
					for (GangOfWarBossDamageVO gangOfWarBossDamageVO : gangDamageList) {
						if (i > GangOfWarConfig.getInstance().GANG_BOSS_DAMAGE_SHOW_MAXSIZE.getValue()) {
							break;
						}
						gangRanks.put(i, gangOfWarBossDamageVO);
						i++;
					}

					// 发送自己家族伤害
					for (VisibleObject vi : getOwner().getKnownList()) {
						if (vi instanceof Player) {
							Player player = (Player) vi;
							GangOfWarBossDamageVO playerGangVO = GangOfWarBossDamageVO.valueOf(player.getGang(), 0);
							if (gangRanks.containsValue(playerGangVO)) {
								PacketSendUtility.sendPacket((Player) vi,
										SM_GangOfWar_SealBossDamageRank.valueOf(gangRanks));
							} else {
								Map<Integer, GangOfWarBossDamageVO> gangRankTemp = new HashMap<Integer, GangOfWarBossDamageVO>(
										gangRanks);
								if (gangDamage.containsKey(player.getGang().getId())) {
									gangRankTemp.put(
											gangDamageList.indexOf(playerGangVO) + 1,
											GangOfWarBossDamageVO.valueOf(player.getGang(),
													gangDamage.get(player.getGang().getId())));
								}
								PacketSendUtility.sendPacket((Player) vi,
										SM_GangOfWar_SealBossDamageRank.valueOf(gangRankTemp));
							}
						}
					}

				}
			}, 2000, 5000);
		}
	}

	@Override
	public void onDie(Creature lastAttacker, int skillId) {
		super.onDie(lastAttacker, skillId);
		Player player = null;
		if (lastAttacker instanceof Player) {
			player = (Player) lastAttacker;
		} else if (lastAttacker instanceof Summon) {
			player = ((Summon) lastAttacker).getMaster();
		}
		// 需要延迟
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				gangOfWar.nextPhase();
			}
		}, GangOfWarConfig.getInstance().DELAY_CHANGE_DEFEND_TIME.getValue() * DateUtils.MILLIS_PER_SECOND);

		// 通报
		// I18nUtils i18nUtils = I18nUtils.valueOf("403003");
		// i18nUtils.addParm("family",
		// I18nPack.valueOf(getFirstDamageGang().getName()));
		// ChatManager.getInstance().sendSystem(71008, i18nUtils, null,
		// Integer.valueOf(GangOfWarConfig.getInstance().MAPID.getValue()),
		// getOwner().getPosition().getInstanceId());

		GangOfWarManager.getInstance().getGangOfwars().get(player.getCountryId()).rank();
		GangOfWarManager.getInstance().getGangOfwars().get(player.getCountryId()).clearWarObject();
	}

	public Gang getFirstDamageGang() {
		Entry<Long, Long> gangEntry = null;
		for (Entry<Long, Long> entry : gangDamage.entrySet()) {
			if (gangEntry == null || gangEntry.getValue() < entry.getValue()) {
				gangEntry = entry;
			}
		}
		return GangManager.getInstance().get(gangEntry.getKey());
	}

	@Override
	public boolean inActivity() {
		return true;
	}

	@Override
	public boolean isActivityEnemy(Creature other) {
		if (other instanceof Summon) {
			other = ((Summon) other).getMaster();
		}
		if (other.getController() instanceof GangOfWarCamp) {
			GangOfWarCamp gop = (GangOfWarCamp) other.getController();
			return getCamps() != gop.getCamps() ? true : false;
		}
		return false;
	}

	public Camps getCamps() {
		return camps;
	}

	public void setCamps(Camps camps) {
		this.camps = camps;
	}

}
