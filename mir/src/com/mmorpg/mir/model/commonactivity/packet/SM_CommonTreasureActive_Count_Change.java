package com.mmorpg.mir.model.commonactivity.packet;

import java.util.HashMap;

import com.mmorpg.mir.model.commonactivity.model.vo.CommonTreasureActiveVo;

public class SM_CommonTreasureActive_Count_Change {

	private HashMap<String, CommonTreasureActiveVo> treasureActiveVos;

	public static SM_CommonTreasureActive_Count_Change valueOf(HashMap<String, CommonTreasureActiveVo> treasureActiveVos) {
		SM_CommonTreasureActive_Count_Change result = new SM_CommonTreasureActive_Count_Change();
		result.treasureActiveVos = treasureActiveVos;
		return result;
	}

	public HashMap<String, CommonTreasureActiveVo> getTreasureActiveVos() {
		return treasureActiveVos;
	}

	public void setTreasureActiveVos(HashMap<String, CommonTreasureActiveVo> treasureActiveVos) {
		this.treasureActiveVos = treasureActiveVos;
	}

}
