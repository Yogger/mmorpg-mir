package com.mmorpg.mir.model.suicide.packet.vo;

import java.util.HashMap;

import com.mmorpg.mir.model.suicide.model.Suicide;

public class SuicideVo {
	private int turn;
	private HashMap<Integer, Integer> elements;

	public static SuicideVo valueOf(Suicide suicide) {
		SuicideVo result = new SuicideVo();
		result.turn = suicide.getTurn();
		result.elements = suicide.getElements();
		return result;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public HashMap<Integer, Integer> getElements() {
		return elements;
	}

	public void setElements(HashMap<Integer, Integer> elements) {
		this.elements = elements;
	}

}
