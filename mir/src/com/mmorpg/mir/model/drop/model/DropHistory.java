package com.mmorpg.mir.model.drop.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.persistence.Transient;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.core.condition.Operator;
import com.mmorpg.mir.model.object.ObjectManager;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.windforce.common.utility.DateUtils;

public class DropHistory {
	/** 怪物击杀记录,每天清理 */
	private NonBlockingHashMap<String, NonBlockingHashMap<String, Integer>> huntHistory = new NonBlockingHashMap<String, NonBlockingHashMap<String, Integer>>();
	private long lastRefreshTime;
	/** BOSS首次击杀的时间记录 */
	private Map<String, Long> huntBossHistory = New.hashMap();
	/** BOSS首杀奖励记录 */
	private Map<String, Boolean> bossFHRewardHistory = New.hashMap();
	/** 掉落道具数量 */
	private Map<String, Integer> dropItems = New.hashMap();
	/** 击杀BOSS的数量的记录 */
	private Map<String, Integer> huntBossCount = New.hashMap();
	/** 击杀怪物的数量(包括精英) */
	private int huntMonsterCount;
	/** 用于中转的临时怪物击杀量 */
	@Transient
	private MonsterKilledHistory monsterKilledHistory;

	public void addPlayerDropHis(String itemKey, int num) {
		if (!dropItems.containsKey(itemKey)) {
			dropItems.put(itemKey, num);
		} else {
			dropItems.put(itemKey, dropItems.get(itemKey) + num);
		}
	}

	public void refresh() {
		if (!DateUtils.isToday(new Date(lastRefreshTime))) {
			dropItems.clear();
			lastRefreshTime = System.currentTimeMillis();

			for (String key : huntHistory.keySet()) {
				ObjectResource objResource = ObjectManager.getInstance().getObjectResource(key);
				if (objResource.getObjectType() == ObjectType.MONSTER) {
					huntHistory.get(key).clear();
				}
			}
		}
	}

	public boolean verifyPlayerDailyDrop(String itemKey, Operator op, int value) {
		Integer dropCount = dropItems.get(itemKey);
		int count = (dropCount == null ? 0 : dropCount);
		if (op == Operator.GREATER) {
			return count > value;
		} else if (op == Operator.GREATER_EQUAL) {
			return count >= value;
		} else if (op == Operator.EQUAL) {
			return count == value;
		} else if (op == Operator.LESS) {
			return count < value;
		} else if (op == Operator.LESS_EQUAL) {
			return count <= value;
		}
		throw new RuntimeException(String.format("玩家每日掉落配置表操作符非法 操作符"));
	}

	public boolean verify(String objectKey, Operator op, String itemKey, int value) {
		Map<String, Integer> map = huntHistory.get(objectKey);
		int count = 0;
		if (map != null && map.containsKey(itemKey)) {
			count = map.get(itemKey);
		}
		if (op == Operator.GREATER) {
			return count > value;
		} else if (op == Operator.GREATER_EQUAL) {
			return count >= value;
		} else if (op == Operator.EQUAL) {
			return count == value;
		} else if (op == Operator.LESS) {
			return count < value;
		} else if (op == Operator.LESS_EQUAL) {
			return count <= value;
		}
		throw new RuntimeException(String.format("掉落配置表操作符非法 操作符"));
	}

	public boolean verifyBossHunt(String objectKey, Operator op, int value) {
		Integer killCount = huntBossCount.get(objectKey);
		int count = 0;
		if (killCount != null) {
			count = killCount;
		}
		if (op == Operator.GREATER) {
			return count > value;
		} else if (op == Operator.GREATER_EQUAL) {
			return count >= value;
		} else if (op == Operator.EQUAL) {
			return count == value;
		} else if (op == Operator.LESS) {
			return count < value;
		} else if (op == Operator.LESS_EQUAL) {
			return count <= value;
		}
		throw new RuntimeException(String.format("击杀BOSS配置表操作符非法 操作符"));
	}

	public void add(String id) {
		ObjectResource resource = ObjectManager.getInstance().getObjectResource(id);
		if (resource.getDropItemKeys() == null && huntHistory.containsKey(id)) {
			huntHistory.get(id).clear();
		} else if (resource.getDropItemKeys() != null) {
			checkDeprecate(id, resource.getDropItemKeys());
			for (String itemKey : resource.getDropItemKeys()) {
				Integer count = huntHistory.get(id).get(itemKey);
				huntHistory.get(id).put(itemKey, (count == null ? 1 : count + 1));
			}
		}
	}

	@JsonIgnore
	public void addHuntBossCount(String objectKey) {
		if (!huntBossCount.containsKey(objectKey)) {
			huntBossCount.put(objectKey, 1);
		} else {
			huntBossCount.put(objectKey, huntBossCount.get(objectKey) + 1);
		}
	}

	@JsonIgnore
	public void addHuntMonsterCount() {
		huntMonsterCount++;
	}

	/**
	 * 删除配置表没有的记录，防止配置表更新，老的数据还在
	 * 
	 * @param objectId
	 * @param itemKeys
	 */
	private void checkDeprecate(String objectId, String[] itemKeys) {
		if (!huntHistory.containsKey(objectId)) {
			huntHistory.put(objectId, new NonBlockingHashMap<String, Integer>());
		} else {
			ArrayList<String> removeIds = New.arrayList();
			for (Map.Entry<String, Integer> entry : huntHistory.get(objectId).entrySet()) {
				boolean notFound = true;
				for (String key : itemKeys) {
					if (entry.getKey().equals(key)) {
						notFound = false;
						break;
					}
				}
				if (notFound) {
					removeIds.add(entry.getKey());
				}
			}
			for (String removeId : removeIds) {
				huntHistory.get(objectId).remove(removeId);
			}
		}
	}

	public NonBlockingHashMap<String, NonBlockingHashMap<String, Integer>> getHuntHistory() {
		return huntHistory;
	}

	public void setHuntHistory(NonBlockingHashMap<String, NonBlockingHashMap<String, Integer>> huntHistory) {
		this.huntHistory = huntHistory;
	}

	@JsonIgnore
	public void resetSpecifiedItem(String itemkey, String objectId) {
		Map<String, Integer> map = huntHistory.get(objectId);
		if (map != null) {
			map.remove(itemkey);
		}
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	@JsonIgnore
	public MonsterKilledHistory getMonsterKilledHistory() {
		return monsterKilledHistory;
	}

	@JsonIgnore
	public void setMonsterKilledHistory(MonsterKilledHistory monsterKilledHistory) {
		this.monsterKilledHistory = monsterKilledHistory;
	}

	public Map<String, Long> getHuntBossHistory() {
		return huntBossHistory;
	}

	public void setHuntBossHistory(Map<String, Long> huntBossHistory) {
		this.huntBossHistory = huntBossHistory;
	}

	public Map<String, Integer> getDropItems() {
		return dropItems;
	}

	public void setDropItems(Map<String, Integer> dropItems) {
		this.dropItems = dropItems;
	}

	public Map<String, Integer> getHuntBossCount() {
		return huntBossCount;
	}

	public void setHuntBossCount(Map<String, Integer> huntBossCount) {
		this.huntBossCount = huntBossCount;
	}

	public int getHuntMonsterCount() {
		return huntMonsterCount;
	}

	public void setHuntMonsterCount(int huntMonsterCount) {
		this.huntMonsterCount = huntMonsterCount;
	}

	public Map<String, Boolean> getBossFHRewardHistory() {
		return bossFHRewardHistory;
	}

	public void setBossFHRewardHistory(Map<String, Boolean> bossFHRewardHistory) {
		this.bossFHRewardHistory = bossFHRewardHistory;
	}

}
