package com.mmorpg.mir.model.soul.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.soul.core.SoulManager;
import com.mmorpg.mir.model.soul.packet.SM_Soul_EnhanceCount_Change;
import com.mmorpg.mir.model.soul.packet.SM_Soul_Grow_Item_Change;
import com.mmorpg.mir.model.soul.resource.SoulGrowItemResource;
import com.mmorpg.mir.model.soul.resource.SoulResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;

/**
 * 英魂
 * 
 * @author 37wan
 * 
 */
public class Soul {

	public static final StatEffectId SOUL_STAT_ID = StatEffectId.valueOf("soul-pf", StatEffectType.SOUL);
	public static final StatEffectId SOUL_ENHANCE = StatEffectId.valueOf("soul_enhance", StatEffectType.SOUL);
	public static final StatEffectId SOUL_GROW = StatEffectId.valueOf("soul_grow", StatEffectType.SOUL);
	public static final StatEffectId SOUL_BLESS = StatEffectId.valueOf("soul_bless", StatEffectType.SOUL);
	
	// 阶数
	private int level;
	// 等级
	private int rank;
	private long clearTime; // 清除祝福值的时间
	@Deprecated
	private int nowBlessValue; // 玩家当前的祝福值
	private int upSum; // 进阶的次数

	/** 强化道具使用次数 */
	private Map<String, Integer> enhanceItemCount;
	/** 成长丹 */
	private Map<String, Integer> growItemCount;

	@Transient
	private transient Player owner;

	public static Soul valueOf(Player owner) {
		Soul self = new Soul();
		self.owner = owner;
		self.level = 1;
		self.rank = 0;
		return self;
	}

	@JsonIgnore
	public void addGrowItemCount(String itemId) {
		if (!this.growItemCount.containsKey(itemId)) {
			this.growItemCount.put(itemId, 0);
		}
		this.growItemCount.put(itemId, this.growItemCount.get(itemId) + 1);
		owner.getGameStats().replaceModifiers(SOUL_GROW, getAllGrowItemStats(), true);

		PacketSendUtility.sendPacket(owner, SM_Soul_Grow_Item_Change.valueOf(this));
	}

	@JsonIgnore
	public Stat[] getTempBlessStats() {
		SoulResource resource = getResource();
		if (upSum > 0 && !resource.isMaxGrade()) {
			Stat[] currentStats = resource.getStats()[this.rank];
			Stat[] nextLevelStats = null;
			if (isMaxLevel()) {
				nextLevelStats = SoulManager.getInstance().getSoulResource(level + 1).getStats()[0];
			} else {
				nextLevelStats = resource.getStats()[this.rank + 1];
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
			
			double factor = upSum * 1.0 / resource.getNeedCount()[this.rank];
			for (Stat stat : tempStats) {
				stat.multipMerge(factor);
			}
			return tempStats;
		}
		return null;
	}
	
	@JsonIgnore
	public Stat[] getAllGrowItemStats() {
		List<Stat> itemStats = new ArrayList<Stat>();
		for (Map.Entry<String, Integer> itemCountEntry : this.growItemCount.entrySet()) {
			String itemId = itemCountEntry.getKey();
			int count = itemCountEntry.getValue();
			SoulGrowItemResource resource = SoulManager.getInstance().soulGrowItemStorage.get(itemId, true);
			for (int i = 0; i < count; i++) {
				itemStats.addAll(Arrays.asList(resource.getStat()));
			}
		}
		return itemStats.toArray(new Stat[0]);
	}

	@JsonIgnore
	public void upgrade() {
		if (isMaxLevel()) {
			this.level++;
			this.rank = 0;
		} else {
			this.rank++;
		}
		LogManager.soulUp(owner.getPlayerEnt().getServer(), owner.getPlayerEnt().getAccountName(), owner.getName(),
				owner.getObjectId(), owner.getSoul().getLevel(), owner.getSoul().getRank(), owner.getSoul().getUpSum(),
				System.currentTimeMillis());
		clear();
	}

	@JsonIgnore
	public void clear() {
		this.clearTime = 0L;
		this.upSum = 0;
	}

	@JsonIgnore
	public void refreshBlessing(Player player) {
		if (getResource().isCountReset() && clearTime <= System.currentTimeMillis()) {
			upSum = 0;
			// PacketSendUtility.sendPacket(player,
			// this.createArtifactVO(player));
		}
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

	public void setUpSum(int upSum) {
		this.upSum = upSum;
	}

	@JsonIgnore
	public void addUpSum(int add) {
		upSum += add;
		long now = System.currentTimeMillis();
		if (getResource().isCountReset() && clearTime < now)
			intervalClearTime(now);
	}

	public int getUpSum() {
		return upSum;
	}

	@JsonIgnore
	public boolean isTimeOut() {
		long nowTime = System.currentTimeMillis();
		long surplusTimes = SoulManager.getInstance().getIntervalTime() - (clearTime - nowTime);
		return surplusTimes <= 0;

	}

	public long getClearTime() {
		return clearTime;
	}

	public void setClearTime(long clearTime) {
		this.clearTime = clearTime;
	}

	public void intervalClearTime(long clearTime) {
		this.clearTime = clearTime + SoulManager.getInstance().getIntervalTime();
	}

	public int getLevel() {
		if (level < 1) {
			level = 1;
		}
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNowBlessValue() {
		return nowBlessValue;
	}

	public void setNowBlessValue(int nowBlessValue) {
		this.nowBlessValue = nowBlessValue;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	@JsonIgnore
	public SoulResource getResource() {
		return SoulManager.getInstance().getSoulResource(level);
	}

	@JsonIgnore
	public boolean isMaxGrade() {
		return getResource().getCount() == 0 ? true : false;
	}

	@JsonIgnore
	public boolean isMaxLevel() {
		int[] needCount = getResource().getNeedCount();
		return this.rank == needCount.length - 1;
	}

	@JsonIgnore
	public List<Stat> getSoulStat() {
		List<Stat> stats = New.arrayList();
		SoulResource hr = SoulManager.getInstance().getSoulResource(level);
		stats.addAll(Arrays.asList(hr.getStats()[this.rank]));
		return stats;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public Map<String, Integer> getEnhanceItemCount() {
		return enhanceItemCount;
	}

	public void setEnhanceItemCount(Map<String, Integer> enhanceItemCount) {
		this.enhanceItemCount = enhanceItemCount;
	}

	@JsonIgnore
	public void addEhanceStat(String itemCode) {
		if (!enhanceItemCount.containsKey(itemCode)) {
			enhanceItemCount.put(itemCode, 0);
		}
		enhanceItemCount.put(itemCode, enhanceItemCount.get(itemCode) + 1);
		owner.getGameStats().replaceModifiers(SOUL_ENHANCE, getEnhanceStats(), true);
		PacketSendUtility.sendPacket(owner, SM_Soul_EnhanceCount_Change.valueOf(owner));
	}

	public Map<String, Integer> getGrowItemCount() {
		return growItemCount;
	}

	public void setGrowItemCount(Map<String, Integer> growItemCount) {
		this.growItemCount = growItemCount;
	}

}
