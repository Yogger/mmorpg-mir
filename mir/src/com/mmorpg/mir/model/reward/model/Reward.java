package com.mmorpg.mir.model.reward.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.combatspirit.model.CombatSpiritStorage.CombatSpiritType;
import com.mmorpg.mir.model.purse.model.CurrencyType;

/**
 * 奖励
 * 
 * @author Kuang Hao
 * @since v1.0 2012-12-20
 * 
 */
public class Reward {
	/** 奖励集合 */
	private ArrayList<RewardItem> items;

	public static Reward valueOf() {
		Reward reward = new Reward();
		reward.items = new ArrayList<RewardItem>();
		return reward;
	}

	@JsonIgnore
	public boolean isToPack() {
		for (RewardItem item : items) {
			if (item.getParms() != null && item.getParms().containsKey("TO_TREASURE_PACK")) {
				return false;
			}
		}
		return true;
	}

	@JsonIgnore
	public int getTargetItemCount(String itemId) {
		int count = 0;
		for (RewardItem item : items) {
			if (item.getRewardType() == RewardType.ITEM && item.getCode().equals(itemId)) {
				count += item.getAmount();
			}
		}
		return count;
	}

	@JsonIgnore
	public void mutipleRewards(int multiValue) {
		for (RewardItem item : items) {
			item.setAmount(item.getAmount() * multiValue);
		}
	}

	@JsonIgnore
	public List<RewardItem> getItemsByType(RewardType type) {
		List<RewardItem> ris = New.arrayList();
		for (RewardItem item : items) {
			if (item.getRewardType() == type) {
				ris.add(item);
			}
		}
		return ris;
	}

	@JsonIgnore
	public int getTotalAmountByType(RewardType type) {
		int sum = 0;
		for (RewardItem item : items) {
			if (item.getRewardType() == type) {
				sum += item.getAmount();
			}
		}
		return sum;
	}

	/**
	 * 合并两个奖励
	 * 
	 * @param reward
	 * @return
	 */
	public Reward addReward(Reward reward) {
		for (RewardItem item : reward.getItems()) {
			boolean merge = false;
			for (RewardItem i : items) {
				if (i.equals(item)) {
					long result = i.getAmount() + (long) item.getAmount();
					if (result >= Integer.MAX_VALUE) {
						continue;
					}
					i.setAmount(i.getAmount() + item.getAmount());
					merge = true;
					break;
				}
			}
			if (!merge) {
				this.addRewardItem(item.clone());
			}
		}
		return this;
	}

	public Reward addRewardNotMerge(Reward reward) {
		for (RewardItem item : reward.getItems()) {
			this.addRewardItem(item.clone());
		}
		return this;
	}

	/**
	 * 添加奖励子项
	 * 
	 * @param item
	 */
	public Reward addRewardItem(RewardItem item) {
		this.items.add(item);
		return this;
	}

	/**
	 * 添加奖励子项
	 * 
	 * @param rewardType
	 * @param code
	 * @param amount
	 */
	public Reward addItem(RewardType rewardType, String code, int amount) {
		this.addRewardItem(RewardItem.valueOf(rewardType, code, amount));
		return this;
	}

	public Reward addItem(RewardType rewardType, String code, int amount, Map<String, String> params) {
		this.addRewardItem(RewardItem.valueOf(rewardType, code, amount, params));
		return this;
	}

	/**
	 * 添加称号
	 * 
	 * @param nameId
	 * @return
	 */
	public Reward addNickname(Integer nameId) {
		this.addRewardItem(RewardItem.valueOf(RewardType.NICKNAME, null, nameId));
		return this;
	}

	/**
	 * 添加经验
	 * 
	 * @param amount
	 * @return
	 */
	public Reward addExp(int amount) {
		this.addRewardItem(RewardItem.valueOf(RewardType.EXP, null, amount));
		return this;
	}

	/**
	 * 添加道具
	 * 
	 * @param itemSample
	 * @param amount
	 * @return
	 */
	public Reward addItem(String itemSample, int amount) {
		this.addRewardItem(RewardItem.valueOf(RewardType.ITEM, itemSample, amount));
		return this;
	}

	public Reward addLifeGridItem(String itemSamle, int amout,Map<String,String> parms) {
		this.addRewardItem(RewardItem.valueOf(RewardType.LIFEGRID, itemSamle, amout,parms));
		return this;
	}

	/**
	 * 添加装备
	 * 
	 * @param itemSample
	 * @param amount
	 * @param parms
	 * @return
	 */
	public Reward addEquipment(String itemSample, int amount, Map<String, String> parms) {
		this.addRewardItem(RewardItem.valueOf(RewardType.ITEM, itemSample, amount, parms));
		return this;
	}

	/**
	 * 添加坐骑
	 * 
	 * @param horseId
	 * @param level
	 * @return
	 */
	public Reward addHorse(String horseId, int level) {
		this.addRewardItem(RewardItem.valueOf(RewardType.HORSE, horseId, level));
		return this;
	}

	/**
	 * 添加神兵
	 * 
	 * @param horseId
	 * @param level
	 * @return
	 */
	public Reward addArtifact(int level) {
		this.addRewardItem(RewardItem.valueOf(RewardType.ARTIFACT, null, level));
		return this;
	}

	/**
	 * 添加英魂
	 * 
	 * @param horseId
	 * @param level
	 * @return
	 */
	public Reward addSoul(int level) {
		this.addRewardItem(RewardItem.valueOf(RewardType.SOUL, null, level));
		return this;
	}

	/**
	 * 添加货币
	 * 
	 * @param type
	 * @param amount
	 * @return
	 */
	public Reward addCurrency(CurrencyType type, int amount) {
		this.addRewardItem(RewardItem.valueOf(RewardType.CURRENCY, type.getValue() + "", amount));
		return this;
	}

	public Reward addRP(int addValue) {
		this.addRewardItem(RewardItem.valueOf(RewardType.RP, null, addValue));
		return this;
	}

	public Reward addCombatSpiritGrow(CombatSpiritType type, int amount) {
		this.addRewardItem(RewardItem.valueOf(RewardType.COMBAT_SPIRIT, type.name(), amount));
		return this;
	}

	public Reward addEquipmentSmeltValue(int addValue) {
		this.addRewardItem(RewardItem.valueOf(RewardType.SMELT, null, addValue));
		return this;
	}

	/**
	 * @param percent
	 *            方法返回的的比率
	 * @param couldSharedRewardType
	 *            需要分割的类型
	 * @return percent的原来的奖励
	 */
	public Reward divideIntoTwoPieces(double percent, RewardType[] couldSharedRewardType) {
		if (couldSharedRewardType == null || couldSharedRewardType.length < 1) {
			return null;
		}
		Reward dividePart = Reward.valueOf();
		for (RewardItem rewardItem : items) {
			for (RewardType rewardType : couldSharedRewardType) {
				if (rewardType.name().equals(rewardItem.getType())) {
					RewardItem robItem = RewardItem.valueOf(rewardItem.getRewardType(), rewardItem.getCode(),
							(int) (rewardItem.getAmount() * percent));
					dividePart.addRewardItem(robItem);
					rewardItem.setAmount((int) (rewardItem.getAmount() * (1.0 - percent)));
				}
			}
		}

		return dividePart;
	}

	public static void main(String[] args) {
		// test spilteItemReward
		Reward r = Reward.valueOf();
		r.items.add(RewardItem.valueOf(RewardType.ITEM, "333", 1));
		r.items.add(RewardItem.valueOf(RewardType.ITEM, "334", 1));
		r.items.add(RewardItem.valueOf(RewardType.ITEM, "335", 1));
		r.items.add(RewardItem.valueOf(RewardType.ITEM, "336", 1));
		r.items.add(RewardItem.valueOf(RewardType.ITEM, "337", 1));
		System.out.println(r.spilteItemReward(5).size() == 1);
		r.items.add(RewardItem.valueOf(RewardType.ITEM, "3333", 1));
		System.out.println(r.spilteItemReward(5).size() == 2);
		System.out.println(Reward.valueOf().spilteItemReward(5).isEmpty());
	}

	/**
	 * @param percent
	 *            方法返回的的比率
	 * @param couldSharedRewardType
	 *            需要部分的类型
	 * @return
	 */
	@JsonIgnore
	public Reward getPartOfReward(double percent, RewardType[] couldSharedRewardType) {
		if (couldSharedRewardType == null || couldSharedRewardType.length < 1) {
			return null;
		}
		Reward dividePart = Reward.valueOf();
		for (RewardItem rewardItem : items) {
			for (RewardType rewardType : couldSharedRewardType) {
				if (rewardType.name().equals(rewardItem.getType())) {
					RewardItem robItem = RewardItem.valueOf(rewardItem.getRewardType(), rewardItem.getCode(),
							(int) (rewardItem.getAmount() * percent));
					dividePart.addRewardItem(robItem);
				}
			}
		}

		return dividePart;
	}

	public ArrayList<RewardItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<RewardItem> items) {
		this.items = items;
	}

	/**
	 * 克隆自己,产生一个新的reward
	 * 
	 * @return
	 */
	public Reward copy() {
		Reward reward = Reward.valueOf();
		for (RewardItem rewardItem : items) {
			reward.addRewardItem(RewardItem.valueOf(rewardItem.getRewardType(), rewardItem.getCode(),
					rewardItem.getAmount(), rewardItem.getParms()));
		}
		return reward;
	}

	/**
	 * 将有多个道具奖励的，分成多份
	 * 
	 * @param itemReward
	 * @param maxItemsPerReward
	 * @return
	 */
	public ArrayList<Reward> spilteItemReward(int maxItemsPerReward) {
		ArrayList<Reward> rewards = New.arrayList();
		Reward reward = Reward.valueOf();
		int count = 0;
		for (RewardItem item : items) {
			reward.addRewardItem(item);
			count++;

			if (count >= maxItemsPerReward) {
				rewards.add(reward);
				reward = Reward.valueOf();
				count = 0;
			}
		}

		if (count > 0) {
			rewards.add(reward);
		}
		return rewards;
	}

	@JsonIgnore
	public boolean isEmpty() {
		return items == null || items.isEmpty();
	}

	public Reward init() {
		// 从网络传输过来的rewardItem中间并没有rewardType这个属性，这里需要生成一下
		for (RewardItem rewardItem : items) {
			rewardItem.setRewardType(RewardType.valueOf(rewardItem.getType()));
		}
		return this;
	}

}
