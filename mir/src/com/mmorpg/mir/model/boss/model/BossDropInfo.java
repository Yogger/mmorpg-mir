package com.mmorpg.mir.model.boss.model;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.reward.model.Reward;

public class BossDropInfo implements Comparable<BossDropInfo> {
	private long playerId;
	private String name;
	private String server;
	private long time;
	private Reward reward;
	private String spawnId;
	private int countryId;

	@Transient
	private transient BossScheme bossScheme;

	public static BossDropInfo valueOf(Player player, Reward reward, String spawnId) {
		BossDropInfo drop = new BossDropInfo();
		drop.playerId = player.getObjectId();
		drop.name = player.getName();
		drop.time = System.currentTimeMillis();
		drop.reward = reward;
		drop.spawnId = spawnId;
		drop.setCountryId(player.getCountryValue());
		return drop;
	}

	@JsonIgnore
	public void delete() {
		bossScheme.getBossHistory().getDropInfos().remove(this);
		bossScheme.getBossHistory().update();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	@Override
	public int compareTo(BossDropInfo o) {
		return (int) (o.getTime() - time);
	}

	@JsonIgnore
	public BossScheme getBossScheme() {
		return bossScheme;
	}

	@JsonIgnore
	public void setBossScheme(BossScheme bossScheme) {
		this.bossScheme = bossScheme;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getSpawnId() {
		return spawnId;
	}

	public void setSpawnId(String spawnId) {
		this.spawnId = spawnId;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

}
