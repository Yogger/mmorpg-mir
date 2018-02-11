package com.mmorpg.mir.model.contact.packet;

import java.util.Map;

import com.mmorpg.mir.model.contact.model.EnemyVO;
import com.mmorpg.mir.model.contact.model.SocialNetData;

public class SM_GET_CONTACTINFO {

	private Map<Long, SocialNetData> friends;
	private Map<Long, SocialNetData> enemies;
	private Map<Long, EnemyVO> enmityMap;
	private Map<Long, SocialNetData> blackList;

	public static SM_GET_CONTACTINFO valueOf(Map<Long, SocialNetData> friends, Map<Long, SocialNetData> enemies, Map<Long, EnemyVO> enmity, Map<Long, SocialNetData> blackList) {
		SM_GET_CONTACTINFO sm = new SM_GET_CONTACTINFO();
		sm.friends = friends;
		sm.enemies = enemies;
		sm.blackList = blackList;
		sm.enmityMap = enmity;
		return sm;
	}
	
	public Map<Long, EnemyVO> getEnmityMap() {
		return enmityMap;
	}

	public void setEnmityMap(Map<Long, EnemyVO> enmityMap) {
		this.enmityMap = enmityMap;
	}

	public Map<Long, SocialNetData> getFriends() {
		return friends;
	}

	public void setFriends(Map<Long, SocialNetData> friends) {
		this.friends = friends;
	}

	public Map<Long, SocialNetData> getEnemies() {
		return enemies;
	}

	public void setEnemies(Map<Long, SocialNetData> enemies) {
		this.enemies = enemies;
	}

	public Map<Long, SocialNetData> getBlackList() {
		return blackList;
	}

	public void setBlackList(Map<Long, SocialNetData> blackList) {
		this.blackList = blackList;
	}

}
