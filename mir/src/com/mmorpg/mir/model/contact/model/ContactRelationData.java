package com.mmorpg.mir.model.contact.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.cliffc.high_scale_lib.NonBlockingHashSet;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.contact.manager.ContactManager;
import com.mmorpg.mir.model.contact.packet.SM_DELETE_CONTACT;
import com.mmorpg.mir.model.contact.service.ContactServiceImpl;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class ContactRelationData {
	/** 关注的玩家ID集合 */
	private Map<Long, Integer> attentionGuys;
	/** 粉丝ID集合 */
	private NonBlockingHashSet<Long> fans;
	/** 屏蔽了我的玩家ID集合 */
	private NonBlockingHashSet<Long> shield;
	/** 仇恨了我的玩家ID集合 */
	private NonBlockingHashSet<Long> hater;

	/** 仇人ID集合 */
	private Map<Long, Integer> enemies;
	/** 我杀了他多少次 */
	private NonBlockingHashMap<Long, Integer> slaughtered;
	/** 仇人最近活跃的时间 */
	private NonBlockingHashMap<Long, Long> enemiesLogTime;
	/** 最近被击杀的仇人 */
	private LinkedList<Long> lastRecentlyEnemies;

	/** 黑名单ID集合 */
	private Map<Long, Integer> blackList;

	@JsonIgnore
	public void clear() {
		shield.clear();
		hater.clear();
		enemies.clear();
		slaughtered.clear();
		enemiesLogTime.clear();
		lastRecentlyEnemies.clear();
		blackList.clear();
	}

	public ContactRelationData() {
		attentionGuys = new HashMap<Long, Integer>();
		fans = new NonBlockingHashSet<Long>();
		shield = new NonBlockingHashSet<Long>();
		hater = new NonBlockingHashSet<Long>();
		slaughtered = new NonBlockingHashMap<Long, Integer>();
		enemies = new HashMap<Long, Integer>();
		enemiesLogTime = new NonBlockingHashMap<Long, Long>();
		lastRecentlyEnemies = new LinkedList<Long>();
		blackList = new HashMap<Long, Integer>();
	}

	public NonBlockingHashSet<Long> getHater() {
		return hater;
	}

	public void setHater(NonBlockingHashSet<Long> hater) {
		this.hater = hater;
	}

	public NonBlockingHashSet<Long> getFans() {
		return fans;
	}

	public void setFans(NonBlockingHashSet<Long> fans) {
		this.fans = fans;
	}

	public NonBlockingHashSet<Long> getShield() {
		return shield;
	}

	public void setShield(NonBlockingHashSet<Long> shield) {
		this.shield = shield;
	}

	/**
	 * @param enemyId
	 * @return true 表示新加的仇人
	 */
	@JsonIgnore
	public boolean addOrUpdateEnemy(Player owner, Long enemyId) {
		enemiesLogTime.put(enemyId, System.currentTimeMillis());
		addToLastRecentlyEnemies(owner, enemyId);
		if (enemies.containsKey(enemyId)) {
			Integer updated = Math.min(ContactManager.getInstance().ENEMITY_LIMIT.getValue(),
					(enemies.get(enemyId) + 1));
			enemies.put(enemyId, updated);
			return false;
		}
		enemies.put(enemyId, 1);
		return true;
	}

	@JsonIgnore
	public void deleteEnemy(Player owner, Long enemyId) {
		enemies.remove(enemyId);
		enemiesLogTime.remove(enemyId);
		slaughtered.remove(enemyId);
		if (lastRecentlyEnemies.remove(enemyId)) {
			PacketSendUtility.sendPacket(owner,
					SM_DELETE_CONTACT.valueOf(enemyId, ContactServiceImpl.ENEMY_SOCIAL_DATA));
		}
	}

	@JsonIgnore
	public Integer slaughtCount(Long slaughteredId) {
		Integer count = slaughtered.putIfAbsent(slaughteredId, 1);
		if (count != null) {
			Integer updated = Math.min(ContactManager.getInstance().ENEMITY_LIMIT.getValue(), (count + 1));
			slaughtered.put(slaughteredId, updated);
			return updated;
		}
		return 1;
	}

	@JsonIgnore
	private void addToLastRecentlyEnemies(Player owner, Long enemyId) {
		if (!lastRecentlyEnemies.contains(enemyId)) {
			if (lastRecentlyEnemies.size() >= ContactManager.getInstance().getEnemySizeLimit()) {
				Long removeId = lastRecentlyEnemies.removeFirst();
				PacketSendUtility.sendPacket(owner,
						SM_DELETE_CONTACT.valueOf(removeId, ContactServiceImpl.ENEMY_SOCIAL_DATA));
			}
			lastRecentlyEnemies.add(enemyId);
		}
	}

	public LinkedList<Long> getLastRecentlyEnemies() {
		return lastRecentlyEnemies;
	}

	public void setLastRecentlyEnemies(LinkedList<Long> lastRecentlyEnemies) {
		this.lastRecentlyEnemies = lastRecentlyEnemies;
	}

	@JsonIgnore
	public LinkedList<SocialNetData> getLastEnemiesVO() {
		LinkedList<SocialNetData> vos = new LinkedList<SocialNetData>();
		for (Long id : lastRecentlyEnemies) {
			vos.add(ContactManager.getInstance().getContactEnt(id).getMySocialData());
		}
		return vos;
	}

	public Map<Long, Integer> getAttentionGuys() {
		return attentionGuys;
	}

	public void setAttentionGuys(Map<Long, Integer> attentionGuys) {
		this.attentionGuys = attentionGuys;
	}

	public Map<Long, Integer> getBlackList() {
		return blackList;
	}

	public void setBlackList(Map<Long, Integer> blackList) {
		this.blackList = blackList;
	}

	public static ContactRelationData valueOf() {
		ContactRelationData data = new ContactRelationData();
		return data;
	}

	public NonBlockingHashMap<Long, Long> getEnemiesLogTime() {
		return enemiesLogTime;
	}

	public void setEnemiesLogTime(NonBlockingHashMap<Long, Long> enemiesLogTime) {
		this.enemiesLogTime = enemiesLogTime;
	}

	public Map<Long, Integer> getEnemies() {
		return enemies;
	}

	public void setEnemies(Map<Long, Integer> enemies) {
		this.enemies = enemies;
	}

	public NonBlockingHashMap<Long, Integer> getSlaughtered() {
		return slaughtered;
	}

	public void setSlaughtered(NonBlockingHashMap<Long, Integer> slaughtered) {
		this.slaughtered = slaughtered;
	}

}
