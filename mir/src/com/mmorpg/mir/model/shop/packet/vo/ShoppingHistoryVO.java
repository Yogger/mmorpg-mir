package com.mmorpg.mir.model.shop.packet.vo;

import java.util.HashMap;
import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;

public class ShoppingHistoryVO {
	
	private Map<String, Integer> history;

	private Map<String, Integer> totalHistory;

	public static ShoppingHistoryVO valueOf(Player player) {
		ShoppingHistoryVO vo = new ShoppingHistoryVO();
		vo.history = new HashMap<String, Integer>(player.getShoppingHistory().getHistory());
		vo.totalHistory = new HashMap<String, Integer>(player.getShoppingHistory().getTotalHistory());
		return vo;
	}

	public Map<String, Integer> getHistory() {
		return history;
	}

	public void setHistory(Map<String, Integer> history) {
		this.history = history;
	}

	public Map<String, Integer> getTotalHistory() {
		return totalHistory;
	}

	public void setTotalHistory(Map<String, Integer> totalHistory) {
		this.totalHistory = totalHistory;
	}

}
