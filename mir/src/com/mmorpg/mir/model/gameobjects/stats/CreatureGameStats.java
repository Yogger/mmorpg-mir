package com.mmorpg.mir.model.gameobjects.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.controllers.stats.PlayerLifeStats;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.packet.SM_CreatureSpeedChange;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.JsonUtils;

public class CreatureGameStats<T extends Creature> {
	protected static final Logger logger = LoggerFactory.getLogger(CreatureGameStats.class);
	protected Map<StatEnum, Stat> stats;
	protected Map<StatEffectId, List<Stat>> statsModifiers;

	protected T owner = null;
	protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * 
	 * @param owner
	 */
	protected CreatureGameStats(T owner) {
		this.owner = owner;
		this.stats = new HashMap<StatEnum, Stat>();
		this.statsModifiers = new ConcurrentHashMap<StatEffectId, List<Stat>>();
	}

	public List<Stat> getAllStat(double rate) {
		List<Stat> allStats = new ArrayList<Stat>();
		for (Entry<StatEffectId, List<Stat>> entry : statsModifiers.entrySet()) {
			if (!entry.getKey().isNoBattleScore()) {
				for (Stat stat : entry.getValue()) {
					Stat newStat = stat.getNewProperty();
					newStat.setValueA((int) (newStat.getValueA() * rate));
					allStats.add(newStat);
				}
			}
		}
		return allStats;
	}

	public void clear() {
		try {
			lock.writeLock().lock();
			stats.clear();
			statsModifiers.clear();
		} finally {
			lock.writeLock().unlock();
		}
	}

	public double getDamage(StatEnum stat, long damage, long ignoreDefense) {
		double value = 0;

		lock.readLock().lock();
		try {
			long resultDefense = ignoreDefense;
			long increaseDefense = getCurrentStat(StatEnum.INCREASE_DEFENSE);
			resultDefense -= increaseDefense;
			if (stat == StatEnum.PHYSICAL_ATTACK) {
				value = (damage
						- (getCurrentStat(StatEnum.PHYSICAL_DEFENSE) * ((10000 - (resultDefense)) * 1.0 / 10000)));
			}
			if (stat == StatEnum.MAGICAL_ATTACK) {
				value = (damage - (getCurrentStat(StatEnum.MAGICAL_DEFENSE) * ((10000 - resultDefense) * 1.0 / 10000)));
			}
			if (value <= 0) {
				value = 1;
			}
		} finally {
			lock.readLock().unlock();
		}

		return value;
	}

	/**
	 * 
	 * @param stat
	 * @return
	 */
	public long getCurrentStat(StatEnum stat) {
		long value = 0;

		lock.readLock().lock();
		try {
			if (stats.containsKey(stat)) {
				value = stats.get(stat).getValue();
				// if (value < 0) {
				// value = 0;
				// }
			}
		} finally {
			lock.readLock().unlock();
		}

		return value;
	}

	protected void onAddModifiers(StatEffectId id) {

	}

	public void addModifiers(StatEffectId id, List<Stat> modifiers, boolean recomputeStats) {
		if (modifiers == null)
			return;

		statsModifiers.put(id, modifiers);
		if (recomputeStats) {
			recomputeStats();
		}
		onAddModifiers(id);
	}

	/**
	 * 
	 * @param id
	 * @param modifiers
	 */
	public void addModifiers(StatEffectId id, List<Stat> modifiers) {
		this.addModifiers(id, modifiers, true);
	}

	public void addModifiers(StatEffectId id, Stat[] modifiers) {
		this.addModifiers(id, Arrays.asList(modifiers), true);
	}

	public void addModifiers(StatEffectId id, Stat[] modifiers, boolean recomputeStats) {
		if (modifiers == null) {
			return;
		}
		this.addModifiers(id, Arrays.asList(modifiers), recomputeStats);
	}

	public void replaceModifiers(StatEffectId id, Stat[] modifiers, boolean recomputeStats) {
		statsModifiers.remove(id);
		if (modifiers == null)
			return;
		this.addModifiers(id, Arrays.asList(modifiers), recomputeStats);
	}

	public void replaceModifiers(StatEffectId id, List<Stat> stats, boolean recomputeStats) {
		statsModifiers.remove(id);
		if (stats == null || stats.isEmpty())
			return;
		this.addModifiers(id, stats, recomputeStats);
	}

	/**
	 * @return True if the StatEffectId is already added
	 */
	public boolean effectAlreadyAdded(StatEffectId id) {
		return statsModifiers.containsKey(id);
	}

	protected void onRecomputeStats() {
	}

	public Map<StatEnum, Stat> doRecomputeStats(boolean useNoBattleScoreStats) {
		Map<Integer, List<Stat>> stats = new ConcurrentHashMap<>();
		for (Entry<StatEffectId, List<Stat>> entry : statsModifiers.entrySet()) {
			if (!entry.getKey().isNoBattleScore() || useNoBattleScoreStats) {
				for (Stat stat : entry.getValue()) {
					if (!stats.containsKey(stat.getModuleKey())) {
						stats.put(stat.getModuleKey(), new ArrayList<Stat>());
					}
					stats.get(stat.getModuleKey()).add(stat);
				}
			}
		}
		Map<StatEnum, Stat> newStats = new ConcurrentHashMap<>();
		for (Integer key : stats.keySet()) {
			if (key == ModuleKey.ALL.value()) {
				// ALL总值属性
				for (Stat stat : stats.get(key)) {
					if (!newStats.containsKey(stat.getType())) {
						newStats.put(stat.getType(), new Stat(stat.getType(), 0, 0, 0));
					}
					newStats.get(stat.getType()).merge(stat);
				}
			} else {
				// 计算模块属性
				Map<StatEnum, Stat> newModuleStats = new ConcurrentHashMap<>();
				for (Stat stat : stats.get(key)) {
					if (!newModuleStats.containsKey(stat.getType())) {
						newModuleStats.put(stat.getType(), new Stat(stat.getType(), 0, 0, 0));
					}
					newModuleStats.get(stat.getType()).merge(stat);
				}
				for (Stat stat : newModuleStats.values()) {
					if (!newStats.containsKey(stat.getType())) {
						newStats.put(stat.getType(), new Stat(stat.getType(), 0, 0, 0));
					}
					newStats.get(stat.getType()).increaseValueA(stat.getValue());
				}
			}
		}
		return newStats;
	}

	/**
	 * 只是用来测试
	 * 
	 * @return
	 */
	public Map<StatEnum, Stat> justForTestRecomputeStats(StatEffectId id) {
		Map<Integer, List<Stat>> stats = new ConcurrentHashMap<>();
		Map<StatEnum, Stat> newStats = new ConcurrentHashMap<>();
		List<Stat> getSpecifieds = statsModifiers.get(id);
		if (getSpecifieds == null || id.isNoBattleScore()) {
			return newStats;
		}
		for (Stat stat : getSpecifieds) {
			if (!stats.containsKey(stat.getModuleKey())) {
				stats.put(stat.getModuleKey(), new ArrayList<Stat>());
			}
			stats.get(stat.getModuleKey()).add(stat);
		}
		for (Integer key : stats.keySet()) {
			if (key == ModuleKey.ALL.value()) {
				// ALL总值属性
				for (Stat stat : stats.get(key)) {
					if (!newStats.containsKey(stat.getType())) {
						newStats.put(stat.getType(), new Stat(stat.getType(), 0, 0, 0));
					}
					newStats.get(stat.getType()).merge(stat);
				}
			} else {
				// 计算模块属性
				Map<StatEnum, Stat> newModuleStats = new ConcurrentHashMap<>();
				for (Stat stat : stats.get(key)) {
					if (!newModuleStats.containsKey(stat.getType())) {
						newModuleStats.put(stat.getType(), new Stat(stat.getType(), 0, 0, 0));
					}
					newModuleStats.get(stat.getType()).merge(stat);
				}
				for (Stat stat : newModuleStats.values()) {
					if (!newStats.containsKey(stat.getType())) {
						newStats.put(stat.getType(), new Stat(stat.getType(), 0, 0, 0));
					}
					newStats.get(stat.getType()).increaseValueA(stat.getValue());
				}
			}
		}
		return newStats;
	}

	/**
	 * Recomputation of all stats
	 */
	public void recomputeStats() {
		// need check this lock, may be remove
		long oldSpeed = 0;
		long oldMaxHp = 0;
		lock.writeLock().lock();
		try {
			Map<StatEnum, Stat> newStats = doRecomputeStats(true);
			oldSpeed = this.stats.get(StatEnum.SPEED) != null ? this.stats.get(StatEnum.SPEED).getValue() : 0;
			oldMaxHp = this.stats.get(StatEnum.MAXHP) != null ? this.stats.get(StatEnum.MAXHP).getValue() : 0;
			stats = newStats;
		} finally {
			lock.writeLock().unlock();
		}
		Stat speedStat = stats.get(StatEnum.SPEED);
		Stat maxHpStat = stats.get(StatEnum.MAXHP);
		long newSpeed = speedStat != null ? speedStat.getValue() : 0;
		long newMaxHp = maxHpStat != null ? maxHpStat.getValue() : 0;
		if (newSpeed != oldSpeed) {
			PacketSendUtility.broadcastPacket(owner, SM_CreatureSpeedChange.valueOf(newSpeed, owner.getObjectId()));
		}
		if (newMaxHp != oldMaxHp) {
			if (owner.getLifeStats() instanceof PlayerLifeStats) {
				if (owner.getLifeStats().getCurrentHp() > newMaxHp) {
					long dif = owner.getLifeStats().getCurrentHp() - newMaxHp;
					owner.getLifeStats().reduceHp(dif, null, 0);
				}
				((PlayerLifeStats) owner.getLifeStats()).sendHpPacketUpdate();
			}
		}
		onRecomputeStats();
	}

	/**
	 * 
	 * @param id
	 */
	public void endModifiers(StatEffectId id) {
		this.endModifiers(id, true);
	}

	protected void onEndModifiers(StatEffectId id) {

	}

	public void endModifiers(StatEffectId id, boolean recomputeStats) {
		if (statsModifiers.remove(id) == null) {
			return;
		}
		if (recomputeStats) {
			recomputeStats();
		}
		onEndModifiers(id);
	}

	public int statSize() {
		int count = 0;
		lock.readLock().lock();
		try {
			for (List<Stat> stats : this.statsModifiers.values()) {
				count += stats.size();
			}
		} finally {
			lock.readLock().unlock();
		}
		return count;
	}

	@Override
	public String toString() {
		TreeMap<StatEffectId, List<Stat>> treeMap = new TreeMap<StatEffectId, List<Stat>>();
		treeMap.putAll(this.statsModifiers);
		StringBuilder sb = new StringBuilder();
		for (Entry<StatEffectId, List<Stat>> entry : treeMap.entrySet()) {
			sb.append(JsonUtils.object2String(entry.getKey()) + "  " + JsonUtils.object2String(entry.getValue()));
			sb.append("\r\n");
		}
		return sb.toString();
	}
}