package com.mmorpg.mir.model.welfare.resource;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class SignResource {

	/** 累计签到的天数 */
	@Id
	private int days;
	/** 签到普通奖励ID */
	private String defaultRewadChooserGroup;
	/** 签到VIP奖励ID */
	private String[] vipRewards;

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public String getDefaultRewadChooserGroup() {
		return defaultRewadChooserGroup;
	}

	public void setDefaultRewadChooserGroup(String defaultRewadChooserGroup) {
		this.defaultRewadChooserGroup = defaultRewadChooserGroup;
	}

	public String[] getVipRewards() {
		return vipRewards;
	}

	public void setVipRewards(String[] vipRewards) {
		this.vipRewards = vipRewards;
	}

}
