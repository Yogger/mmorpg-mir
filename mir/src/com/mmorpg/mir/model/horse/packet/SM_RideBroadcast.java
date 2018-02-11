package com.mmorpg.mir.model.horse.packet;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;

public class SM_RideBroadcast {
	private long objId;
	private int grade;
	private int appearanceId;

	public static SM_RideBroadcast valueOf(Player player) {
		SM_RideBroadcast resp = new SM_RideBroadcast();
		resp.objId = player.getObjectId();
		resp.grade = player.getHorse().getGrade();
		resp.appearanceId = player.getHorse().getAppearance().getCurrentAppearance();
		return resp;
	}

	public static SM_RideBroadcast valueOf(Summon summon) {
		SM_RideBroadcast resp = new SM_RideBroadcast();
		resp.objId = summon.getObjectId();
		resp.grade = 6;
		resp.appearanceId = 6;
		return resp;
	}

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getAppearanceId() {
		return appearanceId;
	}

	public void setAppearanceId(int appearanceId) {
		this.appearanceId = appearanceId;
	}
}
