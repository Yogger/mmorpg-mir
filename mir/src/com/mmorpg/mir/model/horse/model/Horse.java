package com.mmorpg.mir.model.horse.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.horse.manager.HorseManager;
import com.mmorpg.mir.model.horse.packet.SM_HorseUpdate;
import com.mmorpg.mir.model.horse.packet.SM_Horse_EnhanceCount_Change;
import com.mmorpg.mir.model.horse.packet.SM_Horse_Grow_Item_Change;
import com.mmorpg.mir.model.horse.resource.HorseAttachResource;
import com.mmorpg.mir.model.horse.resource.HorseGrowItemResource;
import com.mmorpg.mir.model.horse.resource.HorseResource;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.seal.SealConfig;
import com.mmorpg.mir.model.seal.resource.SealResource;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.DateUtils;

public class Horse {

	public static final StatEffectId PLAYER_LEVEL_STATEID = StatEffectId.valueOf("horse_level", StatEffectType.HORSE);
	public static final StatEffectId GAME_STATE_ID = StatEffectId.valueOf("horse", StatEffectType.HORSE);
	public static final StatEffectId GAME_STATE_SPEEDID = StatEffectId.valueOf("horse_speed", StatEffectType.HORSE);
	public static final StatEffectId HORSE_ENHANCE = StatEffectId.valueOf("horse_enhance", StatEffectType.HORSE);
	public static final StatEffectId HORSE_GROWITEM = StatEffectId.valueOf("horse_growitem", StatEffectType.HORSE);
	public static final StatEffectId HORSE_BLESS_STATEID = StatEffectId.valueOf("horse_bless", StatEffectType.HORSE);

	private transient Player owner;
	/** 阶数 **/
	private int grade;
	/** 等级 */
	private int level;
	/** 当前外观 **/
	private HorseAppearance appearance;
	/** 清除祝福值得时间 */
	private long clearTime;
	/** 当前祝福值 */
	@Deprecated
	private int blessValue;
	/** 进阶的次数 */
	private int upSum;
	/** 坐骑强化道具使用次数 */
	private Map<String, Integer> enhanceItemCount;
	/** 坐骑学的技能 */
	private Map<Integer, Integer> learnedSkills;
	/** 成长丹 */
	private Map<String, Integer> growItemCount;

	@JsonIgnore
	public void addObserver() {
		ActionObserver attackedOb = new ActionObserver(ObserverType.ATTACKED) {
			@Override
			public void attacked(Creature creature) {
				if (creature instanceof Player) {
					owner.unRide();
					owner.setLastPlayerAttackedTime(System.currentTimeMillis());
					((Player) creature).unRide();
					((Player) creature).setLastPlayerAttackedTime(System.currentTimeMillis());
				} else if (creature instanceof Npc) {
					if (((Npc) creature).getObjectResource().isHitUnRide()) {
						owner.unRide();
						owner.setLastPlayerAttackedTime(System.currentTimeMillis());
					}
				}
			}

			@Override
			public void skilluse(Skill skill) {
				owner.unRide();
			}

		};
		owner.getObserveController().addObserver(attackedOb);
	}

	@JsonIgnore
	public boolean isMaxHorseLevel() {
		HorseResource resource = HorseManager.getInstance().getHorseResource(this.grade);
		int[] needCount = resource.getNeedCount();
		return this.level == needCount.length - 1;
	}

	@JsonIgnore
	public void addLevel() {
		this.level++;
		LogManager.horseUpgrade(owner.getPlayerEnt().getServer(), owner.getPlayerEnt().getAccountName(),
				owner.getName(), owner.getObjectId(), grade, level, upSum, System.currentTimeMillis());
	}

	@JsonIgnore
	public void addEhanceStat(String itemCode) {
		if (!enhanceItemCount.containsKey(itemCode)) {
			enhanceItemCount.put(itemCode, 0);
		}
		enhanceItemCount.put(itemCode, enhanceItemCount.get(itemCode) + 1);
		owner.getGameStats().replaceModifiers(Horse.HORSE_ENHANCE, getEnhanceStats(), true);
		PacketSendUtility.sendPacket(owner, SM_Horse_EnhanceCount_Change.valueOf(this));
	}

	@JsonIgnore
	public List<Stat> getEnhanceStats() {
		List<Stat> enhanceStat = new ArrayList<Stat>();
		for (Map.Entry<String, Integer> entry : enhanceItemCount.entrySet()) {
			ItemResource itemResource = ItemManager.getInstance().getItemResources().get(entry.getKey(), true);
			for (int i = 0; i < entry.getValue(); i++) {
				enhanceStat.addAll(Arrays.asList(itemResource.getStats()));
			}
		}
		return enhanceStat;
	}

	@JsonIgnore
	public void clear() {
		blessValue = 0;
		clearTime = 0;
		upSum = 0;
	}

	@JsonIgnore
	public void addBlessValue(int add) {
		blessValue += add;
		long now = System.currentTimeMillis();
		if (getResource().isCountReset() && clearTime < now)
			clearTime = now + HorseManager.getInstance().getIntervalTime();
	}

	@JsonIgnore
	public void addGrade() {
		grade++;
		LogManager.horseUpgrade(owner.getPlayerEnt().getServer(), owner.getPlayerEnt().getAccountName(),
				owner.getName(), owner.getObjectId(), grade, level, upSum, System.currentTimeMillis());
	}

	@JsonIgnore
	public void refreshBlessing() {
		if (!getResource().isCountReset()) {
			return;
		}
		if (clearTime != 0L && clearTime <= System.currentTimeMillis()) {
			clear();
			PacketSendUtility.sendPacket(owner, this.createVO());
		}
	}

	@JsonIgnore
	public HorseResource getResource() {
		return HorseManager.getInstance().getHorseResource(grade);
	}

	@JsonIgnore
	public int getCurrentLevel() {
		return isMaxLevel() ? getResource().getMaxLv() : owner.getPlayerEnt().getLevel();
	}

	@JsonIgnore
	public boolean isMaxLevel() {
		return owner.getPlayerEnt().getLevel() > getResource().getMaxLv() ? true : false;
	}

	@JsonIgnore
	public List<Stat> getStat() {
		List<Stat> stats = New.arrayList();
		HorseResource hr = HorseManager.getInstance().getHorseResource(grade);
		if (hr.getStats() != null) {
			stats.addAll(Arrays.asList(hr.getStats()[this.level]));
		}
		if (appearance.getCurrentAppearance() > 0) {
			HorseAttachResource har = HorseManager.getInstance().getHorseAttachResource(
					appearance.getCurrentAppearance());
			if (har.getStats() != null) {
				stats.addAll(Arrays.asList(har.getStats()));
			}
		}
		return stats;
	}
	
	

	@JsonIgnore
	public List<Stat> getSpeedStat() {
		List<Stat> stats = New.arrayList();
		HorseResource hr = HorseManager.getInstance().getHorseResource(grade);
		stats.addAll(Arrays.asList(hr.getSpeedStat()[this.level]));
		return stats;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void addUpSum(int add) {
		upSum += add;
		long now = System.currentTimeMillis();
		if (getResource().isCountReset() && clearTime < now)
			clearTime = now + HorseManager.getInstance().getIntervalTime();

	}

	@JsonIgnore
	public void addGrowItemCount(String itemId) {
		if (!this.growItemCount.containsKey(itemId)) {
			this.growItemCount.put(itemId, 0);
		}
		this.growItemCount.put(itemId, this.growItemCount.get(itemId) + 1);
		owner.getGameStats().replaceModifiers(HORSE_GROWITEM, getAllGrowItemStats(), true);

		PacketSendUtility.sendPacket(owner, SM_Horse_Grow_Item_Change.valueOf(this));
	}

	@JsonIgnore
	public Stat[] getAllGrowItemStats() {
		List<Stat> itemStats = new ArrayList<Stat>();
		for (Map.Entry<String, Integer> itemCountEntry : this.growItemCount.entrySet()) {
			String itemId = itemCountEntry.getKey();
			int count = itemCountEntry.getValue();
			HorseGrowItemResource resource = HorseManager.getInstance().horseGrowItemStorage.get(itemId, true);
			for (int i = 0; i < count; i++) {
				itemStats.addAll(Arrays.asList(resource.getStat()));
			}
		}
		return itemStats.toArray(new Stat[0]);
	}

	/**
	 * 奖励使用
	 * 
	 * @param horseKingResourceId
	 * @param type
	 * @param overTime
	 * @param foreverActive
	 */
	@JsonIgnore
	public void active(Player player, long overTime, boolean foreverActive) {
		this.appearance.active(player, foreverActive, overTime);
	}

	@JsonIgnore
	public SM_HorseUpdate createVO() {
		SM_HorseUpdate update = new SM_HorseUpdate();
		update.setClearTime(clearTime);
		update.setGrade(grade);
		update.setLevel(this.level);
		update.setTotalCount(this.upSum);
		update.setAppearance(appearance);
		update.setEnhanceItemCount(enhanceItemCount);
		update.setGrowItemCount(growItemCount);
		return update;
	}

	public static Horse valueOf() {
		Horse horse = new Horse();
		horse.grade = 1;
		horse.appearance = HorseAppearance.valueOf(0);
		horse.enhanceItemCount = new HashMap<String, Integer>();
		horse.growItemCount = new HashMap<String, Integer>();
		return horse;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public int getUpSum() {
		return upSum;
	}

	public HorseAppearance getAppearance() {
		return appearance;
	}

	public void setAppearance(HorseAppearance appearance) {
		this.appearance = appearance;
	}

	public void setUpSum(int upSum) {
		this.upSum = upSum;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getGrade() {
		return grade;
	}

	public long getClearTime() {
		return clearTime;
	}

	public void setClearTime(long clearTime) {
		this.clearTime = clearTime;
	}

	public void intervalClearTime(long clearTime, long intervalTime) {
		this.clearTime = clearTime + intervalTime * DateUtils.MILLIS_PER_HOUR;
	}

	public int getBlessValue() {
		return blessValue;
	}

	public void setBlessValue(int blessValue) {
		this.blessValue = blessValue;
	}

	public Map<String, Integer> getEnhanceItemCount() {
		return enhanceItemCount;
	}

	public void setEnhanceItemCount(Map<String, Integer> enhanceItemCount) {
		this.enhanceItemCount = enhanceItemCount;
	}

	public Map<Integer, Integer> getLearnedSkills() {
		return learnedSkills;
	}

	public void setLearnedSkills(Map<Integer, Integer> learnedSkills) {
		this.learnedSkills = learnedSkills;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Map<String, Integer> getGrowItemCount() {
		return growItemCount;
	}

	public void setGrowItemCount(Map<String, Integer> growItemCount) {
		this.growItemCount = growItemCount;
	}

	@JsonIgnore
	public Stat[] getTempBlessStats() {
		HorseResource resource = getResource();
		if (upSum > 0 && !resource.isMaxGrade()) {
			Stat[] currentStats = resource.getStats()[this.level];
			Stat[] nextLevelStats = null;
			if (isMaxLevel()) {
				nextLevelStats = HorseManager.getInstance().getHorseResource(grade + 1).getStats()[0];
			} else {
				nextLevelStats = resource.getStats()[this.level + 1];
			}
			Stat[] tempStats = new Stat[nextLevelStats.length];
			for (int i = 0; i < nextLevelStats.length; i++) {
				Stat temp = nextLevelStats[i].copyOf();
				for (int j = 0; j < currentStats.length; j++) {
					Stat target = currentStats[j];
					if (temp.getType() == target.getType()) {
						long difA = temp.getValueA() - target.getValueA();
						long difB = temp.getValueB() - target.getValueB();
						long difC = temp.getValueC() - target.getValueC();
						if (difA >= 0) {
							temp.setValueA(difA);
						}
						if (difB >= 0) {
							temp.setValueB(difB);
						}
						if (difC >= 0) {
							temp.setValueC(difC);
						}
					}
				}
				tempStats[i] = temp;
			}
			
			double factor = upSum * 1.0 / resource.getNeedCount()[this.level];
			for (Stat stat : tempStats) {
				stat.multipMerge(factor);
			}
			return tempStats;
		}
		return null;
	}
	
}
