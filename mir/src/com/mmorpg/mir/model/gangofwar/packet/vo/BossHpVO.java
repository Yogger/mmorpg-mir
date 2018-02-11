package com.mmorpg.mir.model.gangofwar.packet.vo;

import java.util.Map;

import com.windforce.common.utility.New;

public class BossHpVO {
	private Map<String, BossStatus> bossHps;

	public static BossHpVO valueOf() {
		BossHpVO bc = new BossHpVO();
		bc.bossHps = New.hashMap();
		return bc;
	}

	public boolean add(String spawnId, BossStatus bossStatus) {
		if (!bossHps.containsKey(spawnId)) {
			bossHps.put(spawnId, bossStatus);
			return true;
		} else {
			if (!bossHps.get(spawnId).equals(bossStatus)) {
				bossHps.put(spawnId, bossStatus);
				return true;
			}
		}
		return false;
	}

	public Map<String, BossStatus> getBossHps() {
		return bossHps;
	}

	public void setBossHps(Map<String, BossStatus> bossHps) {
		this.bossHps = bossHps;
	}

}