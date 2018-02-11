package com.mmorpg.mir.model.commonactivity.model.vo;

import java.util.LinkedList;
import java.util.List;

import com.mmorpg.mir.model.commonactivity.model.CommonFirstPay;

public class CommonFirstPayVo {
	private long payCount;

	private LinkedList<String> rewardIds;
	
	public static CommonFirstPayVo valueOf(CommonFirstPay pay){
		CommonFirstPayVo vo = new CommonFirstPayVo();
		vo.payCount = pay.getPayCount();
		vo.rewardIds = pay.getRewardIds();
		return vo;
	}

	public long getPayCount() {
		return payCount;
	}

	public void setPayCount(long payCount) {
		this.payCount = payCount;
	}

	public List<String> getRewardIds() {
		return rewardIds;
	}

	public void setRewardIds(LinkedList<String> rewardIds) {
		this.rewardIds = rewardIds;
	}
}
