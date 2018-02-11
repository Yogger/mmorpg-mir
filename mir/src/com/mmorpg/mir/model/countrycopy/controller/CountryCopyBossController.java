package com.mmorpg.mir.model.countrycopy.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.h2.util.New;

import com.mmorpg.mir.model.boss.config.BossConfig;
import com.mmorpg.mir.model.boss.packet.SM_Boss_DamageRank;
import com.mmorpg.mir.model.boss.vo.BossDamageVO;
import com.mmorpg.mir.model.controllers.BossController;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.skill.model.DamageResult;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMapInstance;

public class CountryCopyBossController extends BossController {

	private Map<Player, Long> damages = new NonBlockingHashMap<Player, Long>();

	@Override
	public void onAttack(Creature creature, int skillId, long damage, DamageResult damageResult) {
		addDamage(creature, damage);
		super.onAttack(creature, skillId, damage, damageResult);
	}

	@Override
	protected synchronized void broadDamage() {
		if (damageBroadFuture == null || damageBroadFuture.isCancelled()) {
			damageBroadFuture = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					Map<Player, Long> playerDamages = getDamages();
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
					WorldMapInstance instance = World.getInstance().getWorldMap(getOwner().getMapId())
							.getWorldMapInstanceById(getOwner().getInstanceId());
					Iterator<Player> playerIterator = instance.playerIterator();
					while (playerIterator.hasNext()) {
						Player player = playerIterator.next();
						Map<Integer, BossDamageVO> tempDamageVOs = new HashMap<Integer, BossDamageVO>(damageVOs);
						if (!damageVOs.containsValue(BossDamageVO.valueOf(player, player.getName(), player
								.getPlayerEnt().getServer(), 0, 0))) {
							long damage = 0;
							if (playerDamages.containsKey(player)) {
								damage = playerDamages.get(player);
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
						PacketSendUtility.sendPacket(player, SM_Boss_DamageRank.valueOf(tempDamageVOs, maxHp));
					}
				}
			}, 2000, 2000);
		}
	}

	public Map<Player, Long> getDamages() {
		return damages;
	}

	public void addDamage(Creature creature, long value) {
		Player player = null;
		if (creature instanceof Summon) {
			player = ((Summon) creature).getMaster();
			if (creature instanceof Player) {
				player = (Player) creature;
			}
		}
		if (creature instanceof Player) {
			player = (Player) creature;
		}

		if (player != null) {
			if (getDamages().containsKey(player)) {
				Long oldValue = getDamages().get(player);
				if (oldValue == null) {
					return;
				}
				getDamages().put(player, oldValue + value);
			} else {
				getDamages().put(player, Long.valueOf(value));
			}
		}
	}

	public Map<Integer, Player> getDamageRank() {
		Map<Integer, Player> ranks = New.hashMap();
		if (getDamages() != null && (!getDamages().isEmpty())) {
			List<Entry<Player, Long>> entrys = New.arrayList();
			for (Entry<Player, Long> entry : getDamages().entrySet()) {
				entrys.add(entry);
			}
			Collections.sort(entrys, new Comparator<Entry<Player, Long>>() {
				@Override
				public int compare(Entry<Player, Long> o1, Entry<Player, Long> o2) {
					if (o2.getValue() > o1.getValue()) {
						return 1;
					} else if (o2.getValue() < o1.getValue()) {
						return -1;
					} else {
						return 0;
					}
				}
			});
			int i = 1;
			for (Entry<Player, Long> entry : entrys) {
				ranks.put(i, entry.getKey());
				i++;
			}
		}
		return ranks;
	}
}
