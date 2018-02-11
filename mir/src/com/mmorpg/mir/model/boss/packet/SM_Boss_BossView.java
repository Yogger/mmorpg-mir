package com.mmorpg.mir.model.boss.packet;

import java.util.Map;

import com.mmorpg.mir.model.boss.model.BossView;

public class SM_Boss_BossView {
	private Map<String, BossView> bossViews;
	private int group;

	public static SM_Boss_BossView valueOf(Map<String, BossView> bossViews, int group) {
		SM_Boss_BossView sm = new SM_Boss_BossView();
		sm.bossViews = bossViews;
		sm.group = group;
		return sm;
	}

	public Map<String, BossView> getBossViews() {
		return bossViews;
	}

	public void setBossViews(Map<String, BossView> bossViews) {
		this.bossViews = bossViews;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

}
