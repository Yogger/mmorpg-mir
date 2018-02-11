package com.mmorpg.mir.model.express.model;

import java.util.ArrayList;

import com.mmorpg.mir.model.reward.model.Reward;
import com.windforce.common.utility.New;

public class ExpressVO {
	
	private int lorryCount;
	private long lastRefreshTime;
	/** 劫镖次数 */
	private int robCount;
	/** 赛选出来的镖车 */
	private ArrayList<String> selectLorrys;
	/** 未领取的奖励 */
	private Reward reward;
	/** 是否是被抢 */
	private boolean beenRob;
	/** 当前镖车 */
	private LorryVO currentLorry;
	/** 是否通知了失败  */
	private boolean beenNotifyFail;
	
	public static ExpressVO valueOf(Express express, LorryVO lorryVO) {
		ExpressVO vo = new ExpressVO();
		vo.lorryCount = express.getLorryCount();
		vo.lastRefreshTime = express.getLastRefreshTime();
		vo.robCount = express.getRobCount().get();
		vo.selectLorrys = New.arrayList();
		if (express.getSelectLorrys() != null) {
			vo.selectLorrys.add(express.getSelectLorrys());
		}
		vo.reward = express.getReward();
		vo.currentLorry = lorryVO;
		vo.beenRob = express.isBeenRob();
		vo.beenNotifyFail = express.isBeenNotifyFail();
		return vo;
	}

	public int getLorryCount() {
		return lorryCount;
	}

	public void setLorryCount(int lorryCount) {
		this.lorryCount = lorryCount;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	public int getRobCount() {
		return robCount;
	}

	public void setRobCount(int robCount) {
		this.robCount = robCount;
	}

	public ArrayList<String> getSelectLorrys() {
		return selectLorrys;
	}

	public void setSelectLorrys(ArrayList<String> selectLorrys) {
		this.selectLorrys = selectLorrys;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	public LorryVO getCurrentLorry() {
		return currentLorry;
	}

	public void setCurrentLorry(LorryVO currentLorry) {
		this.currentLorry = currentLorry;
	}

	public boolean isBeenRob() {
		return beenRob;
	}

	public void setBeenRob(boolean beenRob) {
		this.beenRob = beenRob;
	}

	public boolean isBeenNotifyFail() {
    	return beenNotifyFail;
    }

	public void setBeenNotifyFail(boolean beenNotifyFail) {
    	this.beenNotifyFail = beenNotifyFail;
    }
	
}
