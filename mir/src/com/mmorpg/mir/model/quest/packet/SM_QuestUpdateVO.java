package com.mmorpg.mir.model.quest.packet;

import java.util.ArrayList;
import java.util.HashMap;

public class SM_QuestUpdateVO {
	private int code;
	private ArrayList<QuestVO> vos;
	private short[] shortRemove;
	private short[] shortClientAccepts;
	private HashMap<Short, Integer> shortCompletionHistory;
	private HashMap<Short, Integer> shortTodayCompletionHistory;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public ArrayList<QuestVO> getVos() {
		return vos;
	}

	public void setVos(ArrayList<QuestVO> vos) {
		this.vos = vos;
	}

	public short[] getShortRemove() {
		return shortRemove;
	}

	public void setShortRemove(short[] shortRemove) {
		this.shortRemove = shortRemove;
	}

	public short[] getShortClientAccepts() {
		return shortClientAccepts;
	}

	public void setShortClientAccepts(short[] shortClientAccepts) {
		this.shortClientAccepts = shortClientAccepts;
	}

	public HashMap<Short, Integer> getShortCompletionHistory() {
		return shortCompletionHistory;
	}

	public void setShortCompletionHistory(HashMap<Short, Integer> shortCompletionHistory) {
		this.shortCompletionHistory = shortCompletionHistory;
	}

	public HashMap<Short, Integer> getShortTodayCompletionHistory() {
		return shortTodayCompletionHistory;
	}

	public void setShortTodayCompletionHistory(HashMap<Short, Integer> shortTodayCompletionHistory) {
		this.shortTodayCompletionHistory = shortTodayCompletionHistory;
	}

}
