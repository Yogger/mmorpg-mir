package com.mmorpg.mir.model.copy.controller;

import javax.annotation.PostConstruct;

import com.mmorpg.mir.model.copy.manager.CopyManager;
import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.world.WorldMapInstance;

public abstract class AbstractCopyController {
	/** 玩家 */
	protected Player owner;
	/** 副本地图 */
	protected WorldMapInstance worldMapInstance;
	protected CopyResource copyResource;
	/** 通过控制器离开副本 */
	protected boolean controlleaveCopy;

	public AbstractCopyController() {
	}

	@PostConstruct
	public void init() {
		CopyManager.copyControllers.put(getCopyId(), this);
	}

	public AbstractCopyController(Player owner, WorldMapInstance worldMapInstance, CopyResource resource) {
		this.owner = owner;
		this.worldMapInstance = worldMapInstance;
		this.copyResource = resource;
	}

	public abstract String getCopyId();

	public abstract void startCopy();

	public void leaveCopy(Player player) {
	};

	public void leaveCopyBefore(Player player) {
	};

	public abstract void enterCopy(Player player, WorldMapInstance worldMapInstance, CopyResource resource);

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public WorldMapInstance getWorldMapInstance() {
		return worldMapInstance;
	}

	public void setWorldMapInstance(WorldMapInstance worldMapInstance) {
		this.worldMapInstance = worldMapInstance;
	}

	public CopyResource getCopyResource() {
		return copyResource;
	}

	public void setCopyResource(CopyResource copyResource) {
		this.copyResource = copyResource;
	}

	public boolean isControlleaveCopy() {
		return controlleaveCopy;
	}

	public void setControlleaveCopy(boolean controlleaveCopy) {
		this.controlleaveCopy = controlleaveCopy;
	}

}
