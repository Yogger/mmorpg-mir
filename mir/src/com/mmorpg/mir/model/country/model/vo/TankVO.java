package com.mmorpg.mir.model.country.model.vo;

import com.mmorpg.mir.model.country.model.Tank;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;

public class TankVO {
	private int id;
	private String resourceId;
	private int index;
	private PlayerSimpleInfo playerSimpleInfo;

	public static TankVO valueOf(Tank tank) {
		TankVO vo = new TankVO();
		vo.id = tank.getId();
		vo.resourceId = tank.getResourceId();
		vo.index = tank.getIndex();
		if (tank.getOwner() != 0) {
			vo.playerSimpleInfo = PlayerManager.getInstance().getPlayer(tank.getOwner()).createSimple();
		}
		return vo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public PlayerSimpleInfo getPlayerSimpleInfo() {
		return playerSimpleInfo;
	}

	public void setPlayerSimpleInfo(PlayerSimpleInfo playerSimpleInfo) {
		this.playerSimpleInfo = playerSimpleInfo;
	}

}
