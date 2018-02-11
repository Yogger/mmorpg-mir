package com.mmorpg.mir.model.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.model.EquipmentStat;
import com.mmorpg.mir.model.item.model.EquipmentStatType;
import com.mmorpg.mir.model.item.model.EquipmentStorageType;
import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;

public class Equipment extends AbstractItem {

	public static final StatEffectId STAR_SUIT = StatEffectId.valueOf("STAR_SUIT", StatEffectType.EQUIPMENT);
	public static final StatEffectId SMELT_STAT = StatEffectId.valueOf("SMELT_STAT", StatEffectType.EQUIPMENT);
	public static final StatEffectId ATTACK_SOUL_SUIT = StatEffectId.valueOf("ATTACK_SUIT", StatEffectType.EQUIPMENT);
	public static final StatEffectId DEFENSE_SOUL_SUIT = StatEffectId.valueOf("DEFENSE_SUIT", StatEffectType.EQUIPMENT);
	/** 强化等级 */
	private int enhanceLevel;
	/** 额外属性 */
	private Map<Integer, EquipmentStat> extraStats = new HashMap<Integer, EquipmentStat>();
	/** 五行@see EquimentElementType */
	private int element;

	private int grade;

	// 装备转生次数
	private int turn;

	@JsonIgnore
	private transient int soulType;

	@JsonIgnore
	public AbstractItem copy() {
		Equipment equip = new Equipment();
		equip.element = this.element;
		equip.enhanceLevel = this.enhanceLevel;
		equip.objectId = this.objectId;
		equip.deprecatedTime = this.deprecatedTime;
		equip.key = this.key;
		equip.size = this.size;
		equip.state = this.state;
		for (EquipmentStat stat : extraStats.values()) {
			equip.extraStats.put(stat.getType(), stat.copy());
		}
		return equip;
	}

	@JsonIgnore
	public int getSoulType() {
		return soulType;
	}

	@JsonIgnore
	public void setSoulType(int soulType) {
		this.soulType = soulType;
	}

	@JsonIgnore
	public EquipmentType getEquipmentType() {
		return getResource().getEquipmentType();
	}

	public int getEnhanceLevel() {
		return enhanceLevel;
	}

	public void setEnhanceLevel(int enhanceLevel) {
		this.enhanceLevel = enhanceLevel;
	}

	@JsonIgnore
	public CoreConditions getEquipCoreConditions() {
		return getResource().getEquipCoreConditions();
	}

	@JsonIgnore
	public List<Stat> getModifiers() {
		final Stat[] stats = getResource().getStats();
		List<Stat> newStats = New.arrayList();
		for (Stat s : stats) {
			Stat newStat = new Stat(s.getType(), s.getValueA(), s.getValueB(), s.getValueC(), s.getModuleKey());
			if (getResource().getEquipStorageType() == EquipmentStorageType.PLAYER.getWhere()) {
				newStat.multipMerge(ItemManager.getInstance().getEnhanceRatio(enhanceLevel));
			} else {
				newStat.multipMerge(ItemManager.getInstance().getHorseEnhanceRatio(enhanceLevel));
			}
			newStats.add(newStat);
		}
		if (enhanceLevel != 0) {
			for (Stat s : getResource().getEnhanceStats()) {
				Stat newStat = new Stat(s.getType(), s.getValueA() * enhanceLevel, s.getValueB(), s.getValueC(),
						s.getModuleKey());
				newStats.add(newStat);
			}
		}
		return newStats; // ItemManager.getInstance().getEnhanceStats(getResource().getEquipmentType(),
							// stats, enhanceLevel);
	}

	@JsonIgnore
	public List<Stat> getEnhanceExtraStat() {
		List<Stat> enhancedStats = New.arrayList();
		for (Stat s : getResource().getStats()) {
			Stat newStat = new Stat(s.getType(), s.getValueA(), s.getValueB(), s.getValueC(), s.getModuleKey());
			if (getResource().getEquipStorageType() == EquipmentStorageType.PLAYER.getWhere()) {
				newStat.multipMerge(ItemManager.getInstance().getEnhanceRatio(enhanceLevel));
			} else {
				newStat.multipMerge(ItemManager.getInstance().getHorseEnhanceRatio(enhanceLevel));
			}
			newStat.setValueA(newStat.getValueA() - s.getValueA());
			newStat.setValueB(newStat.getValueB() - s.getValueB());
			newStat.setValueC(newStat.getValueC() - s.getValueC());
			enhancedStats.add(newStat);
		}

		if (enhanceLevel != 0) {
			for (Stat s : getResource().getEnhanceStats()) {
				Stat newStat = new Stat(s.getType(), s.getValueA() * enhanceLevel, s.getValueB(), s.getValueC(),
						s.getModuleKey());
				enhancedStats.add(newStat);
			}
		}
		return enhancedStats;
	}

	@JsonIgnore
	public boolean isEquipBind() {
		return getResource().getEquipBind() > 0;
	}

	public Map<Integer, EquipmentStat> getExtraStats() {
		return extraStats;
	}

	public void setExtraStats(Map<Integer, EquipmentStat> extraStats) {
		this.extraStats = extraStats;
	}

	public void addExtraStats(int value, String... statResourceIds) {
		extraStats.put(value, EquipmentStat.valueOf(value, statResourceIds));
	}

	@JsonIgnore
	public List<String> getExtraIDs(EquipmentStatType type) {
		EquipmentStat stat = extraStats.get(type.getValue());
		return stat == null ? null : stat.getContext();
	}

	public int getElement() {
		return element;
	}

	public void setElement(int element) {
		this.element = element;
	}

	@JsonIgnore
	public boolean hasElement() {
		return this.element != 0;
	}

	@JsonIgnore
	public boolean hasSpecifiedTypeStat(EquipmentStatType statType) {
		return extraStats.get(statType.getValue()) != null;
	}

	@JsonIgnore
	public void addEquipmentExtraStats(Player player, boolean recompute) {
		for (Map.Entry<Integer, EquipmentStat> entry : extraStats.entrySet()) {
			List<Stat> stats = ItemManager.getInstance().calculateStat(player, this, entry.getValue());
			player.getGameStats().addModifiers(getExtraStatEffectId(entry.getKey()), stats, recompute);
		}
	}

	@JsonIgnore
	public void endEquipmentExtraStats(Player player, boolean recompute) {
		for (Map.Entry<Integer, EquipmentStat> entry : extraStats.entrySet()) {
			player.getGameStats().endModifiers(getExtraStatEffectId(entry.getKey()), recompute);
		}
	}

	@JsonIgnore
	public StatEffectId getExtraStatEffectId(int type) {
		return StatEffectId.valueOf(EquipmentStatType.valueOf(type).name() + getEquipmentType().name(),
				StatEffectType.EQUIPMENT);
	}

	@JsonIgnore
	public StatEffectId getExtraStatEffectId(String statTypeName) {
		return StatEffectId.valueOf(statTypeName + getEquipmentType().name(), StatEffectType.EQUIPMENT);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<Integer, EquipmentStat> entry : extraStats.entrySet()) {
			sb.append("|");
			sb.append(entry.getKey());
			EquipmentStat stat = entry.getValue();
			for (String key : stat.getContext()) {
				sb.append("$");
				sb.append(key);
			}
		}
		return state + "|" + deprecatedTime + "|" + enhanceLevel + "|" + element + sb.toString();
	}

	@JsonIgnore
	public void changeStats(EquipmentStatType statType, String targetSoulKey, int index) {
		if (!extraStats.containsKey(statType.getValue())) {
			return;
		}
		int size = extraStats.get(statType.getValue()).getContext().size();
		if (index >= size || index < 0) {
			return;
		}
		extraStats.get(statType.getValue()).getContext().set(index, targetSoulKey);
	}

	@JsonIgnore
	public void upgradeEnhanceLevel() {
		setEnhanceLevel(getEnhanceLevel() + 1);
	}

	@JsonIgnore
	public void addTurn(Player player, String resourceId) {
		this.turn++;
		this.addExtraStats(EquipmentStatType.SUICIDE_TURN.getValue(), resourceId);
		addEquipmentExtraStats(player, true);
		player.getEquipmentStorage().markByEquipmentType(getEquipmentType());
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

}
