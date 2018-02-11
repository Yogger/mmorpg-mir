package com.mmorpg.mir.model.assassin.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.countrycopy.model.vo.TechCopyRankVO;

public class SM_Assassination_Square {
	/** 自己的活动数据 */
	private TechCopyRankVO selfVO;
	/** 前3名的活动数据 */
	private ArrayList<TechCopyRankVO> ranks;
	/** 最后一击的人的活动数据 */
	private TechCopyRankVO lastAttacker;
	/** 活动结束的时间 */
	private long endTime;

	public static SM_Assassination_Square valueOf(TechCopyRankVO selfVO, TechCopyRankVO lastAttack, ArrayList<TechCopyRankVO> ranks, long t) {
		SM_Assassination_Square sm = new SM_Assassination_Square();
		sm.endTime = t;
		sm.selfVO = selfVO;
		sm.ranks = ranks;
		sm.lastAttacker = lastAttack;
		return sm;
	}

	public TechCopyRankVO getSelfVO() {
		return selfVO;
	}

	public void setSelfVO(TechCopyRankVO selfVO) {
		this.selfVO = selfVO;
	}

	public ArrayList<TechCopyRankVO> getRanks() {
		return ranks;
	}

	public void setRanks(ArrayList<TechCopyRankVO> ranks) {
		this.ranks = ranks;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public TechCopyRankVO getLastAttacker() {
		return lastAttacker;
	}

	public void setLastAttacker(TechCopyRankVO lastAttacker) {
		this.lastAttacker = lastAttacker;
	}
	
}
