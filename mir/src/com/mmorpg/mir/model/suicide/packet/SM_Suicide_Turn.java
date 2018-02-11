package com.mmorpg.mir.model.suicide.packet;

public class SM_Suicide_Turn {
	private int turn;

	public static SM_Suicide_Turn valueOf(int turn) {
		SM_Suicide_Turn result = new SM_Suicide_Turn();
		result.turn = turn;
		return result;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

}
