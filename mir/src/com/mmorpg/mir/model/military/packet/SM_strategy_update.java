package com.mmorpg.mir.model.military.packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SM_strategy_update {

	private int code;
	private Map<Integer, Integer> strategy;
	private boolean isSelect;
	private ArrayList<Integer> breaks;
	
	public static SM_strategy_update valueOf(int code, Map<Integer, Integer> strategy, boolean isSelect, ArrayList<Integer> breaks) {
		SM_strategy_update update = new SM_strategy_update();
		update.code = code;
		update.strategy = new HashMap<Integer, Integer>(strategy);
		update.isSelect = isSelect;
		update.breaks = breaks;
		return update;
	}

	public boolean isSelect() {
		return isSelect;
	}


	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}


	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Map<Integer, Integer> getStrategy() {
		return strategy;
	}

	public void setStrategy(Map<Integer, Integer> strategy) {
		this.strategy = strategy;
	}

	public ArrayList<Integer> getBreaks() {
		return breaks;
	}

	public void setBreaks(ArrayList<Integer> breaks) {
		this.breaks = breaks;
	}
}
