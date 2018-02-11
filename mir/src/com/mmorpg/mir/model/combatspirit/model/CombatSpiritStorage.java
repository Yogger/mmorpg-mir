package com.mmorpg.mir.model.combatspirit.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.combatspirit.CombatSpirit;
import com.mmorpg.mir.model.combatspirit.manager.CombatSpiritManager;
import com.mmorpg.mir.model.combatspirit.resource.CombatSpiritResource;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.windforce.common.utility.DateUtils;

public class CombatSpiritStorage {

	private Map<Integer, CombatSpirit> combatSpiritCollection;
	
	/** 今天护符初始的资源ID map **/
	private String todayInitialResoruceId;
	
	/** 记录历史有效杀人数目 */
	private int killedHistory;
	
	/** <playerId, <lastKillTime, count>> 防刷 */
	private transient Map<Long, Long> killLastTime;

	private transient Map<Long, Integer> killCount;
	
	private transient Map<Long, Integer> dailyLimit;
	
	private transient Map<String, Integer> itemDailyLimit;
	
	private transient long lastRefreshTime;

	public static CombatSpiritStorage valueOf() {
		CombatSpiritStorage storage = new CombatSpiritStorage();
		storage.combatSpiritCollection = New.hashMap();
		for (CombatSpiritType type: CombatSpiritType.values()) {
			storage.combatSpiritCollection.put(type.getValue(), CombatSpirit.valueOf(type)); 
		}
		storage.killCount = new HashMap<Long, Integer>();
		storage.dailyLimit = new HashMap<Long, Integer>();
		storage.killLastTime = new HashMap<Long, Long>();
		storage.itemDailyLimit = new HashMap<String, Integer>();
		return storage;
	}
	
	public void initCombatSpiritStorage(Player player) {
		Map<Integer, CombatSpirit> map = player.getCombatSpiritStorage().getCombatSpiritCollection();
		for (Entry<Integer, CombatSpirit> entry: map.entrySet()) {
			CombatSpiritType type = CombatSpiritType.valueOf(entry.getKey());
			String openId = CombatSpiritManager.getInstance().COMBAT_SPIRIT_OPID.getValue().get(type.name());
			CombatSpiritResource resource = CombatSpiritManager.getInstance().getCombatSpiritResource(entry.getValue().getCombatResourceId(), true);
			if (ModuleOpenManager.getInstance().isOpenByKey(player, openId)) {
				String combatTypeId = CombatSpiritType.valueOf(entry.getKey().intValue()).name();
				player.getGameStats().addModifiers(StatEffectId.valueOf(combatTypeId, StatEffectType.COMBAT_SPIRIT),
						CombatSpiritManager.getInstance().getCombatSpiritStats(resource.getId()), false);
			}
		}
		refresh();
	}
	
	@JsonIgnore
	public void refresh() {
		long now = System.currentTimeMillis();
		ArrayList<Long> removeIds = New.arrayList();
		for (Entry<Long, Long> entry : killLastTime.entrySet()) {
			if (now - entry.getValue() >= DateUtils.MILLIS_PER_HOUR) {
				removeIds.add(entry.getKey());
			}
		}
		for (Long rId : removeIds) {
			killLastTime.remove(rId);
			killCount.remove(rId);
		}
		ArrayList<Long> dailyRemoveIds = New.arrayList();
		for (Entry<Long, Integer> entry: dailyLimit.entrySet()) {
			if (!DateUtils.isToday(new Date(entry.getKey()))) {
				removeIds.add(entry.getKey());
			}
		}
		for (Long k: dailyRemoveIds) {
			dailyLimit.remove(k);
		}
		refreshDailyItem();
	}
	
	@JsonIgnore
	private void refreshDailyItem() {
		if (!DateUtils.isToday(new Date(lastRefreshTime))) {
			itemDailyLimit.clear();
			CombatSpirit spirit = combatSpiritCollection.get(CombatSpiritType.PROTECTRUNE.getValue());
			todayInitialResoruceId = spirit.getCombatResourceId();
			spirit.refresh();
		}
		lastRefreshTime = System.currentTimeMillis();
	}
	
	@JsonIgnore
	public void logBenifitItems(List<RewardItem> rewardItems) {
		if (rewardItems == null) {
			return;
		}
		for (RewardItem rewardItem: rewardItems) {
			Integer count = itemDailyLimit.get(rewardItem.getCode());
			if (count == null) {
				count = 0;
			}
			itemDailyLimit.put(rewardItem.getCode(), count + rewardItem.getAmount());
		}
	}
	
	public final Map<Integer, CombatSpirit> getCombatSpiritCollection() {
		return combatSpiritCollection;
	}

	public final void setCombatSpiritCollection(Map<Integer, CombatSpirit> combatSpiritCollection) {
		this.combatSpiritCollection = combatSpiritCollection;
	}
	
	public Map<Long, Long> getKillLastTime() {
		return killLastTime;
	}

	public void setKillLastTime(Map<Long, Long> killLastTime) {
		this.killLastTime = killLastTime;
	}

	public Map<Long, Integer> getKillCount() {
		return killCount;
	}

	public void setKillCount(Map<Long, Integer> killCount) {
		this.killCount = killCount;
	}
	
	public int getKilledHistory() {
		return killedHistory;
	}

	public void setKilledHistory(int killedHistory) {
		this.killedHistory = killedHistory;
	}
	
	@JsonIgnore
	public void addKilledHistory(int count) {
		this.killedHistory += count;
	}
	
	public Map<Long, Integer> getDailyLimit() {
		return dailyLimit;
	}

	public void setDailyLimit(Map<Long, Integer> dailyLimit) {
		this.dailyLimit = dailyLimit;
	}
	
	public Map<String, Integer> getItemDailyLimit() {
		return itemDailyLimit;
	}

	public void setItemDailyLimit(Map<String, Integer> itemDailyLimit) {
		this.itemDailyLimit = itemDailyLimit;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	public String getTodayInitialResoruceId() {
		return todayInitialResoruceId;
	}

	public void setTodayInitialResoruceId(String todayInitialResoruceId) {
		this.todayInitialResoruceId = todayInitialResoruceId;
	}

	public static enum CombatSpiritType {
		/** 护符 **/
		PROTECTRUNE(1, ManagedErrorCode.PROTECTURE_GROWVALUE_NOT_ENOUGH),
		/** 宝物 **/
		TREASURE(2, ManagedErrorCode.TREASURE_GROWVALUE_NOT_ENOUGH),
		/** 勋章 **/
		MEDAL(3, ManagedErrorCode.MEDAL_GROWVALUE_NOT_ENOUGH);

		private final int value;
		private final int errCode;

		public static CombatSpiritType valueOf(int value) {
			for (CombatSpiritType type: CombatSpiritType.values()) {
				if (type.getValue() == value) {
					return type;
				}
			}
			throw new RuntimeException(" no match type of CombatSpiritType[" + value + "]");
		}
		
		private CombatSpiritType(int c, int errCode) {
			this.value = c;
			this.errCode = errCode;
		}

		public int getValue() {
			return value;
		}
		
		public int getErrCode() {
			return errCode;
		}
	}

	/**
	 * 对于不删档的玩家 新增的数据 做初始化  
	 */
	@JsonIgnore
	public void initNewFeature() {
		CombatSpirit spirit = combatSpiritCollection.get(CombatSpiritType.PROTECTRUNE.getValue());
		if (spirit.getHistoryValue() == 0) {
			String iterId = CombatSpiritManager.getInstance().COMBAT_SPIRIT_ID_INIT.getValue().get(CombatSpiritType.PROTECTRUNE.name());
			int total = spirit.getGrowUpValue();
			while (iterId != null && !iterId.equals(spirit.getCombatResourceId())) {
				CombatSpiritResource res = CombatSpiritManager.getInstance().getCombatSpiritResource(iterId, false);
				if (res == null) {
					break;
				}
				total += res.getUpgradeNeed();
				iterId = res.getNextId();
			}
			spirit.setHistoryValue(total);
		}
	}

}
