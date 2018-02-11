package com.mmorpg.mir.model.investigate.packet.vo;

import com.mmorpg.mir.model.investigate.model.Investigate;

public class InvestigateVO {
	/** 已经板砖次数 */
	private int count;
	/** 当前的刺探任务 */
	private String currentInvestigate;
	/** 当前NPC */
	private String currentNpc;
	/** 最后变化的时间 */
	private long lastChangeTime;

	public static InvestigateVO valueOf(Investigate investigate) {
		InvestigateVO vo = new InvestigateVO();
		vo.count = investigate.getCount();
		vo.setCurrentInvestigate(investigate.getCurrentInvestigate());
		vo.setCurrentNpc(investigate.getCurrentNpc());
		vo.setLastChangeTime(investigate.getLastChangeTime());
		return vo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getCurrentInvestigate() {
		return currentInvestigate;
	}

	public void setCurrentInvestigate(String currentInvestigate) {
		this.currentInvestigate = currentInvestigate;
	}

	public String getCurrentNpc() {
		return currentNpc;
	}

	public void setCurrentNpc(String currentNpc) {
		this.currentNpc = currentNpc;
	}

	public long getLastChangeTime() {
		return lastChangeTime;
	}

	public void setLastChangeTime(long lastChangeTime) {
		this.lastChangeTime = lastChangeTime;
	}

}
