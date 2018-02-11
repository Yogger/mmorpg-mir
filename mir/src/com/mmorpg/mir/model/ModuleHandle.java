package com.mmorpg.mir.model;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.mmorpg.mir.model.player.manager.PlayerManager;

public abstract class ModuleHandle {

	@Autowired
	private PlayerManager playerManager;

	@PostConstruct
	public void init() {
		playerManager.registerHandle(this);
	}

	/** 模块号 @see ModuleKey */
	public abstract ModuleKey getModule();

	/**
	 * 反序列化对象对象
	 * 
	 * @param ent
	 */
	public abstract void deserialize(PlayerEnt ent);

	/**
	 * 序列化对象
	 * 
	 * @param ent
	 */
	public abstract void serialize(PlayerEnt ent);

}
