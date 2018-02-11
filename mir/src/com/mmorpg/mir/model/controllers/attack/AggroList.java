package com.mmorpg.mir.model.controllers.attack;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.h2.util.New;

import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Servant;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.group.model.PlayerGroup;
import com.mmorpg.mir.model.object.ObjectType;

public final class AggroList {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(AggroList.class);

	private Creature owner;

	private NonBlockingHashMap<Creature, AggroInfo> aggroList = new NonBlockingHashMap<Creature, AggroInfo>();

	private Map<PlayerGroup, Long> groupDamage = New.hashMap();

	private PlayerGroup mostDamageGroup;

	public AggroList(Creature owner) {
		this.owner = owner;
	}

	public boolean isEmpty() {
		return aggroList.isEmpty();
	}

	public void addDamage(Creature creature, long damage) {
		if (creature == null) {
			return;
		}
		if (creature.getObjectType() == ObjectType.SERVANT) {
			if (!owner.isEnemy(((Servant) creature).getMaster())) {
				return;
			}
		} else {
			if (!owner.isEnemy(creature)) {
				return;
			}
		}

		if (creature instanceof Player) {
			addPlayerDamage((Player) creature, damage);
		} else if (creature instanceof Summon) {
			addSummonDamage((Summon) creature, damage);
		} else {
			addCommonDamage(creature, damage);
		}

		owner.getController().onAddDamage();
	}

	private void addPlayerDamage(Player player, long damage) {
		if (player.isInGroup()) {
			long gd = damage;
			synchronized (groupDamage) {
				if (!groupDamage.containsKey(player.getPlayerGroup())) {
					groupDamage.put(player.getPlayerGroup(), Long.valueOf(damage));
				} else {
					gd = groupDamage.get(player.getPlayerGroup()) + damage;
					groupDamage.put(player.getPlayerGroup(), gd);
				}
				if (mostDamageGroup == null
						|| (mostDamageGroup != player.getPlayerGroup() && groupDamage.get(mostDamageGroup) < gd)) {
					mostDamageGroup = player.getPlayerGroup();
				}
			}
		}
		AggroInfo ai = getAggroInfo(player);
		ai.addDamage(damage);
		ai.addHate(damage);
	}

	private void addSummonDamage(Summon creature, long damage) {
		Player player = creature.getMaster();
		if (player.isInGroup()) {
			long gd = damage;
			synchronized (groupDamage) {
				if (!groupDamage.containsKey(player.getPlayerGroup())) {
					groupDamage.put(player.getPlayerGroup(), Long.valueOf(damage));
				} else {
					gd = groupDamage.get(player.getPlayerGroup()) + damage;
					groupDamage.put(player.getPlayerGroup(), gd);
				}
				if (mostDamageGroup == null
						|| (mostDamageGroup != player.getPlayerGroup() && groupDamage.get(mostDamageGroup) < gd)) {
					mostDamageGroup = player.getPlayerGroup();
				}
			}
		}
		AggroInfo playerAI = getAggroInfo(player);
		playerAI.addDamage(damage);

		AggroInfo ai = getAggroInfo(creature);
		ai.addHate(damage);
	}

	private void addCommonDamage(Creature creature, long damage) {
		AggroInfo ai = getAggroInfo(creature);
		ai.addDamage(damage);
		ai.addHate(damage);
	}

	@SuppressWarnings("unchecked")
	public Collection<Player> getMostDamagePlayers() {
		synchronized (groupDamage) {
			long groupMostDamage = 0;
			if (mostDamageGroup != null) {
				groupMostDamage = groupDamage.get(mostDamageGroup);
			}
			Player mp = getMostPlayerDamage();
			if (mp == null) {
				return Collections.EMPTY_LIST;
			}
			long playerMostDamage = getAggroInfo(mp).getDamage();
			if (groupMostDamage >= playerMostDamage) {
				return Collections.unmodifiableCollection((mostDamageGroup.getMembers()));
			} else if (mp.isInGroup()) {
				return Collections.unmodifiableCollection(mp.getPlayerGroup().getMembers());
			} else {
				return Collections.unmodifiableCollection(Arrays.asList(mp));
			}
		}
	}

	public void addHate(Creature creature, long hate) {
		if (creature == null || creature == owner || !owner.isEnemy(creature))
			return;

		AggroInfo ai = getAggroInfo(creature);
		ai.addHate(hate);

		owner.getController().onAddHate();
	}

	public Creature getMostDamage() {
		Creature mostDamage = null;
		long maxDamage = 0;

		for (Entry<Creature, AggroInfo> entry : aggroList.entrySet()) {
			AggroInfo info = entry.getValue();
			if (info.getAttacker() == null)
				continue;

			if (info.getDamage() > maxDamage) {
				mostDamage = info.getAttacker();
				maxDamage = info.getDamage();
			}
		}

		return mostDamage;
	}

	/**
	 * @return player with most damage
	 */
	public Player getMostPlayerDamage() {
		if (aggroList.isEmpty())
			return null;

		Player mostDamage = null;
		long maxDamage = 0;

		for (Entry<Creature, AggroInfo> entry : aggroList.entrySet()) {
			AggroInfo info = entry.getValue();
			Creature creature = info.getAttacker();
			if (creature instanceof Player) {
				if (info.getDamage() > maxDamage) {
					mostDamage = (Player) creature;
					maxDamage = info.getDamage();
				}
			}
		}

		return mostDamage;
	}

	private long totalDamage;

	public double getDamagePercent(long damage) {
		return damage * 1.0 / totalDamage;
	}

	/**
	 * @return player with damage
	 */
	public Map<Player, Long> getPlayerDamage() {
		Map<Player, Long> players = New.hashMap();
		if (aggroList.isEmpty())
			return players;
		totalDamage = 0;
		for (Entry<Creature, AggroInfo> entry : aggroList.entrySet()) {
			AggroInfo info = entry.getValue();
			if (info.getDamage() == 0) {
				continue;
			}
			Creature creature = info.getAttacker();
			totalDamage += info.getDamage();
			if (creature instanceof Player) {
				players.put((Player) creature, Long.valueOf(info.getDamage()));
			}
		}

		return players;
	}

	public Map<Integer, Long> getCountryDamage() {
		Map<Integer, Long> countryDamage = New.hashMap();
		for (Entry<Player, Long> entry : getPlayerDamage().entrySet()) {
			Integer key = entry.getKey().getCountryValue();
			if (!countryDamage.containsKey(key)) {
				countryDamage.put(key, entry.getValue());
			} else {
				countryDamage.put(key, countryDamage.get(key) + entry.getValue());
			}
		}

		return countryDamage;
	}

	public Map<Player, Long> getMostDamageCountryPlayers() {
		Map<Player, Long> playerDamage = getPlayerDamage();
		Map<Integer, Long> countryDamage = New.hashMap();
		for (CountryId id : CountryId.values()) {
			countryDamage.put(id.getValue(), 0L);
		}
		for (Entry<Player, Long> entry : playerDamage.entrySet()) {
			int countryValue = entry.getKey().getCountryValue();
			long total = countryDamage.get(countryValue) + entry.getValue();
			countryDamage.put(countryValue, total);
		}
		int mostDamageCountry = 0;
		long mostDamage = 0;
		for (Entry<Integer, Long> entry : countryDamage.entrySet()) {
			if (entry.getValue().longValue() > mostDamage) {
				mostDamage = entry.getValue().longValue();
				mostDamageCountry = entry.getKey().intValue();
			}
		}
		Map<Player, Long> result = New.hashMap();
		for (Entry<Player, Long> entry : playerDamage.entrySet()) {
			int countryValue = entry.getKey().getCountryValue();
			if (countryValue == mostDamageCountry) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}

	public Map<Player, Long> getSpecifiedCountryDamagePlayers(List<Integer> attackCountries) {
		Map<Player, Long> playerDamage = getPlayerDamage();
		Map<Player, Long> result = New.hashMap();
		for (Entry<Player, Long> entry : playerDamage.entrySet()) {
			int countryValue = entry.getKey().getCountryValue();
			if (attackCountries.contains(countryValue)) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}

	/**
	 * 玩家伤害排名
	 * 
	 * @return
	 */
	public Map<Integer, Player> getPlayerDamageRank() {
		return getPlayerDamageRank(getPlayerDamage());
	}

	/**
	 * 玩家伤害排名
	 * 
	 * @return
	 */
	public Map<Integer, Player> getPlayerDamageRank(Map<Player, Long> players) {
		Map<Integer, Player> rankMap = New.hashMap();
		if (players.isEmpty()) {
			return rankMap;
		}
		LinkedList<Entry<Player, Long>> ranks = new LinkedList<Entry<Player, Long>>();
		for (Entry<Player, Long> entry : players.entrySet()) {
			ranks.add(entry);
		}

		Collections.sort(ranks, new Comparator<Entry<Player, Long>>() {
			@Override
			public int compare(Entry<Player, Long> o1, Entry<Player, Long> o2) {
				return (o2.getValue() - o1.getValue()) < 0 ? -1 : 1;
			}
		});
		int i = 1;
		for (Entry<Player, Long> rank : ranks) {
			rankMap.put(i, rank.getKey());
			i++;
		}
		return rankMap;
	}

	public static void main(String[] args) {

		Map<String, Long> players = new HashMap<String, Long>();
		players.put("f", 6l);
		players.put("a", 1l);
		players.put("c", 3l);
		players.put("e", 5l);
		players.put("d", 4l);
		players.put("b", 2l);
		Map<Integer, String> rankMap = New.hashMap();

		LinkedList<Entry<String, Long>> ranks = new LinkedList<Entry<String, Long>>();
		for (Entry<String, Long> entry : players.entrySet()) {
			ranks.add(entry);
		}

		Collections.sort(ranks, new Comparator<Entry<String, Long>>() {
			@Override
			public int compare(Entry<String, Long> o1, Entry<String, Long> o2) {
				return (int) (o1.getValue() - o2.getValue());
			}
		});
		int i = 1;
		for (Entry<String, Long> rank : ranks) {
			rankMap.put(i, rank.getKey());
			System.out.println(i + ":" + rank.getKey());
			i++;
		}

	}

	/**
	 * 
	 * @return most hated creature
	 */
	public Creature getMostHated() {
		if (aggroList.isEmpty())
			return null;

		Creature mostHated = null;
		long maxHate = 0;

		for (AggroInfo ai : aggroList.values()) {
			if (ai == null)
				continue;

			// aggroList will never contain anything but creatures
			Creature attacker = (Creature) ai.getAttacker();

			if (attacker.getLifeStats().isAlreadyDead() || !owner.getKnownList().knowns(ai.getAttacker()))
				ai.setHate(0);

			if (ai.getHate() > maxHate) {
				mostHated = attacker;
				maxHate = ai.getHate();
			}
		}

		return mostHated;
	}

	/**
	 * 
	 * @param creature
	 * @return
	 */
	public boolean isMostHated(Creature creature) {
		if (creature == null || creature.getLifeStats().isAlreadyDead())
			return false;

		Creature mostHated = getMostHated();
		if (mostHated == null)
			return false;

		return mostHated.equals(creature);
	}

	/**
	 * @param creature
	 * @param value
	 */
	public void notifyHate(Creature creature, int value) {
		if (isHating(creature))
			addHate(creature, value);
	}

	/**
	 * 
	 * @param creature
	 */
	public void stopHating(Creature creature) {
		AggroInfo aggroInfo = aggroList.get(creature);
		if (aggroInfo != null)
			aggroInfo.setHate(0);
	}

	public void discountHating(Creature creature) {
		AggroInfo aggroInfo = aggroList.get(creature);
		if (aggroInfo != null) {
			if (aggroInfo.getHate() > 1) {
				aggroInfo.setHate(aggroInfo.getHate() / 2);
			}
		}
	}

	/**
	 * Remove completely creature from aggro list
	 * 
	 * @param creature
	 */
	public void remove(Creature creature) {
		aggroList.remove(creature);
	}

	/**
	 * Clear aggroList
	 */
	public void clear() {
		aggroList.clear();
		groupDamage.clear();
		mostDamageGroup = null;
	}

	/**
	 * 
	 * @param creature
	 * @return aggroInfo
	 */
	private AggroInfo getAggroInfo(Creature creature) {
		AggroInfo ai = aggroList.get(creature);
		if (ai == null) {
			ai = new AggroInfo(creature);
			AggroInfo add = aggroList.putIfAbsent(creature, ai);
			if (add != null)
				ai = add;
		}
		return ai;
	}

	/**
	 * 
	 * @param creature
	 * @return boolean
	 */
	private boolean isHating(Creature creature) {
		return aggroList.containsKey(creature);
	}

	/**
	 * @return aggro list
	 */
	public Collection<AggroInfo> getList() {
		return aggroList.values();
	}

	/**
	 * @return total damage
	 */
	public int getTotalDamage() {
		int totalDamage = 0;
		for (AggroInfo ai : this.aggroList.values()) {
			totalDamage += ai.getDamage();
		}
		return totalDamage;
	}

}
