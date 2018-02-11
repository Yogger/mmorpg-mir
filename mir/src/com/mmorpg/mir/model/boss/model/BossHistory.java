package com.mmorpg.mir.model.boss.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Transient;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.boss.entity.BossEntity;
import com.mmorpg.mir.model.boss.manager.BossManager;
import com.mmorpg.mir.model.gameobjects.Boss;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.windforce.common.utility.DateUtils;

public class BossHistory {
	/** 最大记录人数 */
	public static final int MAX_KILLER_SIZE = 5;
	/** 上次BOSS被击杀者的信息 */
	private Map<Long, PlayerSimpleInfo> lastByKillPlayers;
	/** 掉落信息 */
	private List<BossDropInfo> dropInfos;

	private NonBlockingHashMap<Long, BossGiftInfo> giftInfoMap;

	// TODO 记录BOSS的等级和经验信息

	@Transient
	private BossEntity bossEntity;

	public static BossHistory valueOf() {
		BossHistory bh = new BossHistory();
		bh.lastByKillPlayers = New.hashMap();
		bh.dropInfos = New.arrayList();
		bh.giftInfoMap = new NonBlockingHashMap<Long, BossGiftInfo>();
		return bh;
	}

	@JsonIgnore
	public void refresh() {
		Date now = new Date();
		ArrayList<Long> removeIds = New.arrayList();
		for (Long key : giftInfoMap.keySet()) {
			if (DateUtils.calcIntervalDays(new Date(key), now) != 0) {
				removeIds.add(key);
			}
		}
		for (Long removeId : removeIds) {
			giftInfoMap.remove(removeId);
		}
	}

	/**
	 * 添加掉落信息
	 * 
	 * @param killer
	 * @param reward
	 */
	@JsonIgnore
	public void addDropInfo(Player killer, Reward reward, Boss boss) {
		if (dropInfos == null) {
			dropInfos = New.arrayList();
		}

		Reward copyThat = Reward.valueOf();
		for (RewardItem rewardItem : reward.getItems()) {
			if (rewardItem.getRewardType() == RewardType.ITEM) {
				if (ItemManager.getInstance().getResource(rewardItem.getCode()).getQuality() >= ItemResource.PURPLE) {
					copyThat.addRewardItem(RewardItem.valueOf(rewardItem.getRewardType(), rewardItem.getCode(),
							rewardItem.getAmount(), rewardItem.getParms()));
				}
			}
		}

		if (!copyThat.getItems().isEmpty()) {
			dropInfos.add(BossDropInfo.valueOf(killer, copyThat, boss.getSpawnKey()));
		}
	}

	@JsonIgnore
	public void addKiller(Player killer) {
		if (lastByKillPlayers == null) {
			lastByKillPlayers = New.hashMap();
		}
		if (lastByKillPlayers.size() >= MAX_KILLER_SIZE) {
			long oldestTime = Long.MAX_VALUE;
			for (Entry<Long, PlayerSimpleInfo> entry : lastByKillPlayers.entrySet()) {
				if (oldestTime > entry.getKey()) {
					oldestTime = entry.getKey();
				}
			}
			lastByKillPlayers.remove(oldestTime);
		}
		lastByKillPlayers.put(System.currentTimeMillis(), killer.createSimple());
	}

	public Map<Long, PlayerSimpleInfo> getLastByKillPlayers() {
		return lastByKillPlayers;
	}

	@JsonIgnore
	public PlayerSimpleInfo getLastAttacker() {
		long max = 0L;
		PlayerSimpleInfo playerSimpleInfo = null;
		for (Entry<Long, PlayerSimpleInfo> entry : lastByKillPlayers.entrySet()) {
			if (entry.getKey() > max) {
				max = entry.getKey();
				playerSimpleInfo = entry.getValue();
			}
		}
		return playerSimpleInfo;
	}

	public void setLastByKillPlayers(Map<Long, PlayerSimpleInfo> lastByKillPlayers) {
		this.lastByKillPlayers = lastByKillPlayers;
	}

	@JsonIgnore
	public BossEntity getBossEntity() {
		return bossEntity;
	}

	public void update() {
		BossManager.getInstance().updateBossHistory(this);
	}

	public void setBossEntity(BossEntity bossEntity) {
		this.bossEntity = bossEntity;
	}

	public List<BossDropInfo> getDropInfos() {
		return dropInfos;
	}

	public void setDropInfos(List<BossDropInfo> dropInfos) {
		this.dropInfos = dropInfos;
	}

	public NonBlockingHashMap<Long, BossGiftInfo> getGiftInfoMap() {
		return giftInfoMap;
	}

	public void setGiftInfoMap(NonBlockingHashMap<Long, BossGiftInfo> giftInfoMap) {
		this.giftInfoMap = giftInfoMap;
	}

}
