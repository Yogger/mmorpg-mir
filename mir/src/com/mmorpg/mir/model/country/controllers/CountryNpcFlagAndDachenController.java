package com.mmorpg.mir.model.country.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.cliffc.high_scale_lib.NonBlockingHashMap;

import com.mmorpg.mir.model.boss.vo.BossDamageVO;
import com.mmorpg.mir.model.controllers.CountryNpcController;
import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.CountryFlag;
import com.mmorpg.mir.model.country.model.countryact.CountryFlagQuestType;
import com.mmorpg.mir.model.country.packet.SM_CountryNpc_Fight_Rank;
import com.mmorpg.mir.model.country.packet.vo.CountryNpcDamageVO;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.model.DamageResult;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;

public class CountryNpcFlagAndDachenController extends CountryNpcController {

	// 记录每个国家的伤害
	private Map<Integer, Long> countryDamage = new NonBlockingHashMap<Integer, Long>();

	private boolean isCountryFlag;

	@Override
	public void onAttack(Creature creature, int skillId, long damage, DamageResult damageResult) {
		super.onAttack(creature, skillId, damage, damageResult);
		if (creature instanceof Player) {
			Player player = (Player) creature;
			Long value = countryDamage.get(player.getCountryValue());
			if (value == null) {
				countryDamage.put(player.getCountryValue(), 0L);
				value = 0L;
			}
			countryDamage.put(player.getCountryValue(), value + damage);
		} else if (creature instanceof Summon) {
			Player player = ((Summon) creature).getMaster();
			Long value = countryDamage.get(player.getCountryValue());
			if (value == null) {
				countryDamage.put(player.getCountryValue(), 0L);
				value = 0L;
			}
			countryDamage.put(player.getCountryValue(), value + damage);
		}

		broadDamage();
	}

	@Override
	public void onDespawn() {
		super.onDespawn();
	}

	@Override
	public void onFightOff() {
		super.onFightOff();
		countryDamage.clear();
		String start = isCountryFlag ? ConfigValueManager.getInstance().COUNTRY_SAFE_START_CRON_FLAG.getValue()
				: ConfigValueManager.getInstance().COUNTRY_SAFE_START_CRON.getValue();
		String end = isCountryFlag ? ConfigValueManager.getInstance().COUNTRY_SAFE_END_CRON_FLAG.getValue()
				: ConfigValueManager.getInstance().COUNTRY_SAFE_END_CRON.getValue();
		CoreConditions conditions = new CoreConditions();
		conditions.addCondition(CoreConditionType.createBetweenCronTimeCondition(start, end));
		if (conditions.verify(new Object(), false) && damageBroadFuture != null) {
			damageBroadFuture.cancel(false);
		}
	}

	@Override
	public void onDie(Creature lastAttacker, int skillId) {
		super.onDie(lastAttacker, skillId);
		countryDamage.clear();
	}

	@Override
	protected synchronized void broadDamage() {
		if (damageBroadFuture == null || damageBroadFuture.isCancelled()) {
			damageBroadFuture = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					Map<Player, Long> playerDamages = getOwner().getAggroList().getPlayerDamage();
					if (playerDamages.isEmpty() || getOwner().getLifeStats().isAlreadyDead()
							|| !getOwner().getPosition().isSpawned()) {
						damageBroadFuture.cancel(false);
						return;
					}
					Map<Integer, Player> rankDamages = getOwner().getAggroList().getPlayerDamageRank(playerDamages);

					Map<Integer, CountryNpcDamageVO> damageVOs = new HashMap<Integer, CountryNpcDamageVO>();

					for (int i = 1; i <= ConfigValueManager.getInstance().RANK_SIZE.getValue(); i++) {
						if (!rankDamages.containsKey(i)) {
							break;
						}
						Player player = rankDamages.get(i);
						CountryNpcDamageVO vo = CountryNpcDamageVO.valueOf(i, player, null, playerDamages.get(player));
						damageVOs.put(i, vo);
					}
					// 发送自己的伤害
					for (VisibleObject vi : getOwner().getKnownList()) {
						if (vi instanceof Player) {
							Player player = (Player) vi;
							if (player.getCountryValue() == getOwner().getCountryValue()) {
								continue;
							}
							Map<Integer, CountryNpcDamageVO> tempDamageVOs = new HashMap<Integer, CountryNpcDamageVO>(
									damageVOs);
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
									CountryNpcDamageVO mineVO = CountryNpcDamageVO.valueOf(rank, player, damage);
									tempDamageVOs.put(rank, mineVO);
								}
							}
							long maxHp = getOwner().getGameStats().getCurrentStat(StatEnum.MAXHP);
							PacketSendUtility.sendPacket((Player) vi, SM_CountryNpc_Fight_Rank.valueOf(tempDamageVOs,
									getOwner().getAggroList().getCountryDamage(), maxHp, isCountryFlag ? 0 : 1,
									getOwner().getCountryValue()));
						}
					}
				}
			}, 2000, 2000);
		}
	}

	@Override
	public void see(VisibleObject object) {
		super.see(object);
		if (isCountryFlag && object.getObjectType() == ObjectType.PLAYER) {
			Player player = (Player) object;
			CountryFlag flag = player.getCountry().getCountryFlag();
			player.setFlagStatus(flag.getAlliance());

			if (player.getCountry().getCountryFlag().getFlagQuestType() == CountryFlagQuestType.ATTACK_WITH_ALLIANCE) {
				EffectId id = getAllianceEffectId(player);
				if (id != null) {
					player.getEffectController().setAbnormal(getAllianceEffectId(player), true);
				}
			}
		}
	}

	private EffectId getAllianceEffectId(Player player) {
		int alliance = player.getCountry().getCountryFlag().getAlliance();
		if (alliance == 1) {
			return EffectId.ALLIANCE_QI;
		} else if (alliance == 2) {
			return EffectId.ALLIANCE_CHU;
		} else if (alliance == 3) {
			return EffectId.ALLIANCE_ZHAO;
		}
		return null;
	}

	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange) {
		super.notSee(object, isOutOfRange);
		if (isCountryFlag && object.getObjectType() == ObjectType.PLAYER) {
			Player player = (Player) object;

			CountryFlag flag = CountryManager.getInstance().getCountryByValue(getOwner().getCountryValue())
					.getCountryFlag();
			boolean flagSee = flag.getCountryNpc() != null && flag.getCountryNpc().getKnownList().knowns(player);
			boolean specialNpcSee = false;
			for (Creature creature : flag.getSpecifiedNpcs()) {
				if (creature.getKnownList().knowns(player)) {
					specialNpcSee = true;
					break;
				}
			}
			if (specialNpcSee || flagSee) {
				return;
			}

			player.clearFlagAllianceState();
		}
	}

	public void setCountryFlag() {
		isCountryFlag = true;
	}
}
