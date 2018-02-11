package com.mmorpg.mir.model.boss.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.boss.model.BossDropInfo;

public class SM_Boss_DropInfo {
	private ArrayList<BossDropInfo> drops;

	public static SM_Boss_DropInfo valueOf(ArrayList<BossDropInfo> drops) {
		SM_Boss_DropInfo sm = new SM_Boss_DropInfo();
		sm.drops = drops;
		return sm;
	}

	public ArrayList<BossDropInfo> getDrops() {
		return drops;
	}

	public void setDrops(ArrayList<BossDropInfo> drops) {
		this.drops = drops;
	}

}
