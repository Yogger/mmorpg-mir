package com.mmorpg.mir.module.player.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.Persister;

@Entity
@Cached(size = "maxOnline", persister = @Persister("30s"))
@NamedQueries({ @NamedQuery(name = "PlayerEnt.name", query = "from PlayerEnt where name = ?") })
public class PlayerEnt implements IEntity<Long> {

	@Id
	private long playerId;
	private String account;
	private String serverId;
	private String name;
	private int role;

	@Override
	public Long getId() {
		return playerId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	@Override
	public boolean serialize() {
		return true;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

}
