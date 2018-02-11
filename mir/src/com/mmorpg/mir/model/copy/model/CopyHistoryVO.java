package com.mmorpg.mir.model.copy.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.h2.util.New;

import com.mmorpg.mir.model.world.WorldMapInstance;

public class CopyHistoryVO {
	private Map<String, Integer> enterHistory = New.hashMap();
	private Map<String, Integer> todayEnterHistory = New.hashMap();
	private Map<String, Integer> completeHistory = New.hashMap();
	private Map<String, Integer> todayCompleteHistory = New.hashMap();
	private HashMap<Integer, Integer> ladderCurrenctResetCount = New.hashMap();

	private Map<String, Long> lastEnterTime = New.hashMap();

	private Map<String, Integer> buyCounts = New.hashMap();

	private ArrayList<String> ladderRewarded;

	private int ladderResetCount;

	private int ladderCompleteIndex;

	private int ladderHisCompleteIndex;

	private ArrayList<String> bossRewarded;

	private Map<String, Long> copyFirstDoneTime;

	private CopyInfo copyInfo;

	private HashMap<String, Integer> horseEquipMaxQuestHis;

	public static CopyHistoryVO valueOf(CopyHistory ch) {
		CopyHistoryVO vo = new CopyHistoryVO();
		vo.enterHistory = ch.getEnterHistory();
		vo.todayEnterHistory = ch.getTodayEnterHistory();
		vo.completeHistory = ch.getCompleteHistory();
		vo.todayCompleteHistory = ch.getTodayCompleteHistory();
		vo.lastEnterTime = ch.getLastEnterTime();
		vo.buyCounts = ch.getBuyCounts();
		vo.ladderRewarded = (ArrayList<String>) ch.getLadderRewarded();
		vo.ladderHisCompleteIndex = ch.getLadderHisCompleteIndex();
		vo.ladderResetCount = ch.getLadderResetCount();
		vo.ladderCompleteIndex = ch.getLadderCompleteIndex();
		vo.bossRewarded = (ArrayList<String>) ch.getBossRewarded();
		vo.copyFirstDoneTime = ch.getCopyFirstDoneTime();
		if (ch.getCurrentCopyResource() != null && ch.getCurrentCopyResource().getType() == CopyType.EXP) {
			WorldMapInstance worldMapInstance = ch.getCurrentMapInstance();
			if (worldMapInstance != null) {
				vo.setCopyInfo(worldMapInstance.getCopyInfo());
			}
		}
		vo.ladderCurrenctResetCount = new HashMap<Integer, Integer>(ch.getLadderCurrenctResetCount());
		vo.horseEquipMaxQuestHis = new HashMap<String, Integer>(ch.getHorseEquipMaxQuestHis());
		return vo;
	}

	public Map<String, Integer> getEnterHistory() {
		return enterHistory;
	}

	public void setEnterHistory(Map<String, Integer> enterHistory) {
		this.enterHistory = enterHistory;
	}

	public Map<String, Integer> getTodayEnterHistory() {
		return todayEnterHistory;
	}

	public void setTodayEnterHistory(Map<String, Integer> todayEnterHistory) {
		this.todayEnterHistory = todayEnterHistory;
	}

	public Map<String, Long> getLastEnterTime() {
		return lastEnterTime;
	}

	public void setLastEnterTime(Map<String, Long> lastEnterTime) {
		this.lastEnterTime = lastEnterTime;
	}

	public Map<String, Integer> getBuyCounts() {
		return buyCounts;
	}

	public void setBuyCounts(Map<String, Integer> buyCounts) {
		this.buyCounts = buyCounts;
	}

	public Map<String, Integer> getCompleteHistory() {
		return completeHistory;
	}

	public void setCompleteHistory(Map<String, Integer> completeHistory) {
		this.completeHistory = completeHistory;
	}

	public Map<String, Integer> getTodayCompleteHistory() {
		return todayCompleteHistory;
	}

	public void setTodayCompleteHistory(Map<String, Integer> todayCompleteHistory) {
		this.todayCompleteHistory = todayCompleteHistory;
	}

	public ArrayList<String> getLadderRewarded() {
		return ladderRewarded;
	}

	public void setLadderRewarded(ArrayList<String> ladderRewarded) {
		this.ladderRewarded = ladderRewarded;
	}

	public int getLadderResetCount() {
		return ladderResetCount;
	}

	public void setLadderResetCount(int ladderResetCount) {
		this.ladderResetCount = ladderResetCount;
	}

	public int getLadderCompleteIndex() {
		return ladderCompleteIndex;
	}

	public void setLadderCompleteIndex(int ladderCompleteIndex) {
		this.ladderCompleteIndex = ladderCompleteIndex;
	}

	public int getLadderHisCompleteIndex() {
		return ladderHisCompleteIndex;
	}

	public void setLadderHisCompleteIndex(int ladderHisCompleteIndex) {
		this.ladderHisCompleteIndex = ladderHisCompleteIndex;
	}

	public ArrayList<String> getBossRewarded() {
		return bossRewarded;
	}

	public void setBossRewarded(ArrayList<String> bossRewarded) {
		this.bossRewarded = bossRewarded;
	}

	public Map<String, Long> getCopyFirstDoneTime() {
		return copyFirstDoneTime;
	}

	public void setCopyFirstDoneTime(Map<String, Long> copyFirstDoneTime) {
		this.copyFirstDoneTime = copyFirstDoneTime;
	}

	public CopyInfo getCopyInfo() {
		return copyInfo;
	}

	public void setCopyInfo(CopyInfo copyInfo) {
		this.copyInfo = copyInfo;
	}

	public HashMap<Integer, Integer> getLadderCurrenctResetCount() {
		return ladderCurrenctResetCount;
	}

	public void setLadderCurrenctResetCount(HashMap<Integer, Integer> ladderCurrenctResetCount) {
		this.ladderCurrenctResetCount = ladderCurrenctResetCount;
	}

	public HashMap<String, Integer> getHorseEquipMaxQuestHis() {
		return horseEquipMaxQuestHis;
	}

	public void setHorseEquipMaxQuestHis(HashMap<String, Integer> horseEquipMaxQuestHis) {
		this.horseEquipMaxQuestHis = horseEquipMaxQuestHis;
	}
}
