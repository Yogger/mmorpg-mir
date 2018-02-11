package com.mmorpg.mir.model.gameobjects.stats;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.BattleScoreRefreshEvent;
import com.mmorpg.mir.model.gameobjects.packet.SM_BattleScore_Update;
import com.mmorpg.mir.model.gameobjects.packet.SM_PlayerGameStats;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.player.resource.Role;
import com.mmorpg.mir.model.task.tasks.PacketBroadcaster.BroadcastMode;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;

public class PlayerGameStats extends CreatureGameStats<Player> {

	private boolean battleScoreMark;

	private Map<StatEnum, Stat> battleStats;

	private int battleScore;

	private int equipmentEnhanceBattleScore;

	private int equipmentHorseEnhanceBattleScore;

	/**
	 * ROUNDUP((PHYSICAL_ATTACK+MAGICAL_ATTACK+PHYSICAL_DEFENSE+MAGICAL_DEFENSE)
	 * *12+MAXHP*1,0)+(IGNORE+IGNORE_RESIST)/10000*(标准攻击-标准防御)*2*12+
	 * (CRITICAL+CRITICAL_RESIST)/10000*(标准攻击-标准防御)*6,0)
	 * 
	 * @return
	 */
	public int calcEquipmentEnhanceBattleSorce() {
		if (battleScoreMark) {
			double total = 0.0;
			for (Equipment equip : owner.getEquipmentStorage().getEquipments()) {
				if (equip == null || equip.getEnhanceLevel() == 0)
					continue;
				double[] factors = getCalcFactors(equip.getEnhanceExtraStat());
				double result = 0.0;
				double v1 = (factors[0] + factors[1] + factors[2] + factors[3]) * 12 + factors[4] * 1.0;
				double v2 = (factors[5] + factors[6]) / 10000 * getStandard() * 2 * 12;
				double v3 = (factors[7] + factors[8]) / 10000 * getStandard() * 6.0;
				result = Math.ceil(v1 + v2 + v3);
				total += result;
			}
			equipmentEnhanceBattleScore = (int) Math.ceil(total);
		}
		return equipmentEnhanceBattleScore;
	}

	public int calcEquipmentHorseEnhanceBattleScore() {
		if (battleScoreMark) {
			double total = 0.0;
			for (Equipment equip : owner.getHorseEquipmentStorage().getEquipments()) {
				if (equip == null || equip.getEnhanceLevel() == 0)
					continue;
				double[] factors = getCalcHorseFactors(equip.getEnhanceExtraStat());
				double result = 0.0;
				double v1 = ((factors[0] + factors[1]) / 2 + factors[2] + factors[3]) * 12;
				double v2 = factors[4] * 1 + factors[5] / 10000 * getStandard() * 6;
				result = Math.ceil(v1 + v2);
				total += result;
			}
			equipmentHorseEnhanceBattleScore = (int) Math.ceil(total);
		}
		return equipmentHorseEnhanceBattleScore;
	}

	public double[] getCalcFactors(List<Stat> stats) {
		double[] factors = new double[9];
		for (Stat stat : stats) {
			long value = stat.getValue();
			if (stat.getType() == StatEnum.PHYSICAL_ATTACK) {
				factors[0] += value;
			} else if (stat.getType() == StatEnum.MAGICAL_ATTACK) {
				factors[1] += value;
			} else if (stat.getType() == StatEnum.PHYSICAL_DEFENSE) {
				factors[2] += value;
			} else if (stat.getType() == StatEnum.MAGICAL_DEFENSE) {
				factors[3] += value;
			} else if (stat.getType() == StatEnum.MAXHP) {
				factors[4] += value;
			} else if (stat.getType() == StatEnum.IGNORE) {
				factors[5] += value;
			} else if (stat.getType() == StatEnum.IGNORE_RESIST) {
				factors[6] += value;
			} else if (stat.getType() == StatEnum.CRITICAL) {
				factors[7] += value;
			} else if (stat.getType() == StatEnum.CRITICAL_RESIST) {
				factors[8] += value;
			}
		}
		return factors;
	}

	public double[] getCalcHorseFactors(List<Stat> stats) {
		double[] factors = new double[6];
		for (Stat stat : stats) {
			long value = stat.getValue();
			if (stat.getType() == StatEnum.PHYSICAL_ATTACK) {
				factors[0] += value;
			} else if (stat.getType() == StatEnum.MAGICAL_ATTACK) {
				factors[1] += value;
			} else if (stat.getType() == StatEnum.PHYSICAL_DEFENSE) {
				factors[2] += value;
			} else if (stat.getType() == StatEnum.MAGICAL_DEFENSE) {
				factors[3] += value;
			} else if (stat.getType() == StatEnum.MAXHP) {
				factors[4] += value;
			} else if (stat.getType() == StatEnum.CRITICAL_RESIST) {
				factors[5] += value;
			}
		}

		return factors;
	}

	public int calcBattleScore() {
		int oldBattleScore = battleScore;
		if (battleScoreMark) {
			battleStats = doRecomputeStats(false);
			calcEquipmentEnhanceBattleSorce();
			calcEquipmentHorseEnhanceBattleScore();
			battleScoreMark = false;
			int type = owner.getRole();// 职业
			// 战士/弓手战力计算
			if (type == Role.WARRIOR.value() || type == Role.ARCHER.value()) {
				battleScore = getBattleScoreByWA();
			} else
			// 谋士/方士战力计算
			if (type == Role.STRATEGIST.value() || type == Role.SORCERER.value()) {
				battleScore = getBattleScoreBySS();
			}
		}
		boolean becomeMorePower = battleScore > oldBattleScore;
		boolean notSame = battleScore != oldBattleScore;
		if (notSame) {
			if (owner.getGang() != null) {
				owner.getGang().updateMemberBattlePoints(owner.getObjectId(), battleScore, owner);
			}
			owner.getPlayerEnt().setBattleScore(battleScore);
			EventBusManager.getInstance()
					.submit(BattleScoreRefreshEvent.valueOf(owner.getObjectId(), owner.getLevel(), battleScore,
							becomeMorePower));
		}
		return battleScore;
	}

	private double d = 10000.00d;

	/** 战士/弓手战力计算 */
	private int getBattleScoreByWA() {
		double fightValue = 0;
		double v1 = getValue(StatEnum.PHYSICAL_ATTACK) + getValue(StatEnum.PHYSICAL_DEFENSE)
				+ getValue(StatEnum.MAGICAL_DEFENSE);
		fightValue += v1 * 12;
		fightValue += getValue(StatEnum.MAXHP) * 1;
		fightValue += getValue(StatEnum.BARRIER) * 1;
		double v2 = ((getValue(StatEnum.IGNORE) + getValue(StatEnum.IGNORE_RESIST)) / d);
		int v3 = (getStandard());
		fightValue += v2 * v3 * 2 * 12;
		double v4 = ((getValue(StatEnum.CRITICAL) + getValue(StatEnum.CRITICAL_RESIST)
				+ getValue(StatEnum.MAGICAL_RESIST) + getValue(StatEnum.PHYSICAL_RESIST)) / d);
		double v5 = getStandard();
		fightValue += v4 * v5 * 6;
		fightValue += getValue(StatEnum.TRUE_DAMAGE) * 12;
		double v6 = (getValue(StatEnum.IGNORE_DEFENSE) + getValue(StatEnum.INCREASE_DEFENSE)) / d
				* PlayerManager.getInstance().STANDARD_DEFENSE.getValue() * 12;
		fightValue += v6;
		double v7 = (getValue(StatEnum.DAMAGE_INCREASE) + getValue(StatEnum.DAMAGE_REDUCE)
				+ getValue(StatEnum.LIFE_STEAL) + getValue(StatEnum.RETURN_DAMAGE) + getValue(StatEnum.BARRIER_PERCENT))
				/ d * getStandard() * 12;
		fightValue += v7;
		double v8 = (getValue(StatEnum.CRITICAL_DAMAGE_INCREASE) + getValue(StatEnum.CRITICAL_DAMAGE_REDUCE)) / d
				* (getStandHurt() * getStandCritical()) * 12;
		fightValue += v8;

		double v9 = (getValue(StatEnum.DAMAGE_INCREASE1) + getValue(StatEnum.DAMAGE_REDUCE1)) / d * getStandHurt() * 12;
		fightValue += v9;
		return (int) Math.ceil(fightValue);
	}

	/** 谋士/方士战力计算 */
	private int getBattleScoreBySS() {
		double fightValue = 0;
		double v1 = getValue(StatEnum.MAGICAL_ATTACK) + getValue(StatEnum.PHYSICAL_DEFENSE)
				+ getValue(StatEnum.MAGICAL_DEFENSE);
		fightValue += v1 * 12;
		fightValue += getValue(StatEnum.MAXHP) * 1;
		fightValue += getValue(StatEnum.BARRIER) * 1;
		double v2 = ((getValue(StatEnum.IGNORE) + getValue(StatEnum.IGNORE_RESIST)) / d);
		double v3 = (getStandard());
		fightValue += v2 * v3 * 2 * 12;
		double v4 = ((getValue(StatEnum.CRITICAL) + getValue(StatEnum.CRITICAL_RESIST)
				+ getValue(StatEnum.MAGICAL_RESIST) + getValue(StatEnum.PHYSICAL_RESIST)) / d);
		double v5 = getStandard();
		fightValue += v4 * v5 * 6;
		fightValue += getValue(StatEnum.TRUE_DAMAGE) * 12;
		double v6 = (getValue(StatEnum.IGNORE_DEFENSE) + getValue(StatEnum.INCREASE_DEFENSE)) / d
				* PlayerManager.getInstance().STANDARD_DEFENSE.getValue() * 12;
		fightValue += v6;
		double v7 = (getValue(StatEnum.DAMAGE_INCREASE) + getValue(StatEnum.DAMAGE_REDUCE)
				+ getValue(StatEnum.LIFE_STEAL) + getValue(StatEnum.RETURN_DAMAGE) + getValue(StatEnum.BARRIER_PERCENT))
				/ d * getStandard() * 12;
		fightValue += v7;
		double v8 = (getValue(StatEnum.CRITICAL_DAMAGE_INCREASE) + getValue(StatEnum.CRITICAL_DAMAGE_REDUCE)) / d
				* (getStandHurt() * getStandCritical()) * 12;
		fightValue += v8;

		double v9 = (getValue(StatEnum.DAMAGE_INCREASE1) + getValue(StatEnum.DAMAGE_REDUCE1)) / d * getStandHurt() * 12;
		fightValue += v9;
		return (int) Math.ceil(fightValue);
	}

	/**
	 * 只是用来测试用的
	 */
	public void listAllStatEffectBattleScore() {
		int type = owner.getRole();// 职业
		Set<StatEffectId> addValueCList = New.hashSet();
		for (Entry<StatEffectId, List<Stat>> entry : statsModifiers.entrySet()) {
			List<Stat> stats = entry.getValue();
			for (Stat stat : stats) {
				if (stat.getValue() == 0 && stat.getValueC() != 0) {
					addValueCList.add(entry.getKey());
				}
			}
			System.out.println(entry.getKey());
			battleStats = justForTestRecomputeStats(entry.getKey());
			if (type == Role.WARRIOR.value() || type == Role.ARCHER.value()) {
				battleScore = getBattleScoreByWA();
			} else if (type == Role.STRATEGIST.value() || type == Role.SORCERER.value()) {
				battleScore = getBattleScoreBySS();
			}
			String s = "owner: " + owner.getName() + " " + battleScore;
			if (addValueCList.contains(entry.getKey())) {
				s = s + "  contains valuec ";
			}
			System.err.println(s);
		}

		calcBattleScore(); // reset
	}

	private long getValue(StatEnum type) {
		return battleStats.containsKey(type) ? battleStats.get(type).getValue() : 0;
	}

	private int getStandard() {
		int c = PlayerManager.getInstance().STANDARD_ATTACK.getValue()
				- PlayerManager.getInstance().STANDARD_DEFENSE.getValue();
		if (c < 0) {
			throw new RuntimeException("标准攻击-标准防御 < 0");
		}
		return c;
	}

	private double getStandCritical() {
		double c = PlayerManager.getInstance().STANDARD_CRITICAL.getValue();
		if (c < 0) {
			throw new RuntimeException("标准暴击<0");
		}
		return c;
	}

	private double getStandHurt() {
		double c = PlayerManager.getInstance().STANDARD_HURT.getValue();
		if (c < 0) {
			throw new RuntimeException("标准伤害");
		}
		return c;
	}

	public PlayerGameStats(Player owner) {
		super(owner);
	}

	public void sendBattleScoreImp() {
		int score = calcBattleScore();
		int enhanceScore = calcEquipmentEnhanceBattleSorce();
		int horseEnhanceScore = calcEquipmentHorseEnhanceBattleScore();
		PacketSendUtility.sendPacket(owner, SM_BattleScore_Update.valueOf(score, enhanceScore, horseEnhanceScore));
	}

	public void sendBattleScore() {
		owner.addPacketBroadcastMask(BroadcastMode.SEND_BATTLE_SCORE);
	}

	@Override
	protected void onAddModifiers(StatEffectId id) {
		super.onAddModifiers(id);
		if (!id.isNoBattleScore()) {
			battleScoreMark = true;
		}
	}

	@Override
	protected void onEndModifiers(StatEffectId id) {
		super.onEndModifiers(id);
		if (!id.isNoBattleScore()) {
			battleScoreMark = true;
		}
	}

	@Override
	protected void onRecomputeStats() {
		PacketSendUtility.sendPacket(this.owner, SM_PlayerGameStats.valueOf(stats));
		sendBattleScore();
	}

	public PlayerGameStatsVO createPlayerGameStatsVO() {
		PlayerGameStatsVO vo = new PlayerGameStatsVO();
		for (Stat stat : stats.values()) {
			vo.getStats().put(stat.getType().getName(), stat.getValue());
		}
		return vo;
	}

	public void doLevelUpgrade(Stat[] stats) {
		this.endModifiers(StatEffectId.valueOf("level_base", StatEffectType.LEVEL_BASE), false);
		this.addModifiers(StatEffectId.valueOf("level_base", StatEffectType.LEVEL_BASE), Arrays.asList(stats));
	}

	public boolean isBattleScoreMark() {
		return battleScoreMark;
	}

	public void setBattleScoreMark(boolean battleScoreMark) {
		this.battleScoreMark = battleScoreMark;
	}
}
