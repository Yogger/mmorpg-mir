package com.mmorpg.mir.model.copy.packet;

import java.util.HashMap;

import com.mmorpg.mir.model.gameobjects.Player;

public class SM_Ladder_ResetCount_Change {

	private HashMap<Integer, Integer> ladderCurrenctResetCount;

	public static SM_Ladder_ResetCount_Change valueOf(Player player) {
		SM_Ladder_ResetCount_Change result = new SM_Ladder_ResetCount_Change();
		result.ladderCurrenctResetCount = new HashMap<Integer, Integer>(player.getCopyHistory()
				.getLadderCurrenctResetCount());
		return result;
	}

	public HashMap<Integer, Integer> getLadderCurrenctResetCount() {
		return ladderCurrenctResetCount;
	}

	public void setLadderCurrenctResetCount(HashMap<Integer, Integer> ladderCurrenctResetCount) {
		this.ladderCurrenctResetCount = ladderCurrenctResetCount;
	}

}
