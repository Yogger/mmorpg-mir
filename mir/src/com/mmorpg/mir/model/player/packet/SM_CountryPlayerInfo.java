package com.mmorpg.mir.model.player.packet;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_CountryPlayerInfo {

	private long id;
	/** 当前刺探 */
	private String currentInvestigate;
	/** 是否有营救任务 */
	private byte rescue;
	/** 砖块的Id */
	private String brickId;

	public static SM_CountryPlayerInfo valueOf(Player player) {
		SM_CountryPlayerInfo req = new SM_CountryPlayerInfo();
		req.id = player.getObjectId();
		req.currentInvestigate = player.getInvestigate().getCurrentInvestigate();
		req.rescue = player.getRescue().isStart()? (byte) 1 : 0;
		req.brickId = player.getTempleHistory().getCurrentBrick() == null ? null : player.getTempleHistory()
				.getCurrentBrick().getId();
		return req;
	}

	public String getCurrentInvestigate() {
		return currentInvestigate;
	}

	public void setCurrentInvestigate(String currentInvestigate) {
		this.currentInvestigate = currentInvestigate;
	}

	public byte getRescue() {
		return rescue;
	}

	public void setRescue(byte rescue) {
		this.rescue = rescue;
	}

	public String getBrickId() {
		return brickId;
	}

	public void setBrickId(String brickId) {
		this.brickId = brickId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
