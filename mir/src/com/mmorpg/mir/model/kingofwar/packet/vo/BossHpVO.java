package com.mmorpg.mir.model.kingofwar.packet.vo;

import java.util.Map;

import org.h2.util.New;

public class BossHpVO {
	private Map<String, Long> bossHps;

	public static BossHpVO valueOf() {
		BossHpVO bc = new BossHpVO();
		bc.bossHps = New.hashMap();
		return bc;
	}

	public void add(String spawId, long hp) {
		bossHps.put(spawId, hp);
	}

	public Map<String, Long> getBossHps() {
		return bossHps;
	}

	public void setBossHps(Map<String, Long> bossHps) {
		this.bossHps = bossHps;
	}

}
