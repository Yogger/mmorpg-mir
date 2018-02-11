package com.mmorpg.mir.model.welfare.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.welfare.packet.vo.BossGiftVO;

public class BossGift implements Comparable<BossGift>{

	private long owner;
	private String ownerName;
	
	private long deadTime;
	private String spawnKey;
	private Reward reward;
	
	public static BossGift valueOf(Player player, long time, String key) {
		BossGift gift = new BossGift();
		gift.owner = player.getObjectId();
		gift.ownerName = player.getName();
		gift.deadTime = time;
		gift.spawnKey = key;
		return gift;
	}

	public static BossGift valueOf(Player player, long time, String key, Reward reward) {
		BossGift gift = valueOf(player, time, key);
		gift.reward = reward;
		return gift;
	}

	public long getDeadTime() {
		return deadTime;
	}

	public void setDeadTime(long deadTime) {
		this.deadTime = deadTime;
	}

	public String getSpawnKey() {
		return spawnKey;
	}

	public void setSpawnKey(String spawnKey) {
		this.spawnKey = spawnKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (deadTime ^ (deadTime >>> 32));
		result = prime * result + ((spawnKey == null) ? 0 : spawnKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BossGift other = (BossGift) obj;
		if (deadTime != other.deadTime)
			return false;
		if (spawnKey == null) {
			if (other.spawnKey != null)
				return false;
		} else if (!spawnKey.equals(other.spawnKey))
			return false;
		return true;
	}

	@Override
	public int compareTo(BossGift o) {
		return (int) (o.getReward().getTotalAmountByType(RewardType.CURRENCY) - reward.getTotalAmountByType(RewardType.CURRENCY));
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	public long getOwner() {
		return owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	@JsonIgnore
	public BossGiftVO createVO() {
		return BossGiftVO.valueOf(owner, ownerName, reward.getTotalAmountByType(RewardType.CURRENCY));
	}
}
