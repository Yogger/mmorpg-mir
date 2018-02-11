package com.mmorpg.mir.model.copy.packet;

public class SM_Lader_Target_Lay_Reset_Count_Change {

	private int copyIndex;
	private int layResetCount;

	public static SM_Lader_Target_Lay_Reset_Count_Change valueOf(int copyIndex, int layResetCount) {
		SM_Lader_Target_Lay_Reset_Count_Change result = new SM_Lader_Target_Lay_Reset_Count_Change();
		result.copyIndex = copyIndex;
		result.layResetCount = layResetCount;
		return result;
	}

	public int getCopyIndex() {
		return copyIndex;
	}

	public void setCopyIndex(int copyIndex) {
		this.copyIndex = copyIndex;
	}

	public int getLayResetCount() {
		return layResetCount;
	}

	public void setLayResetCount(int layResetCount) {
		this.layResetCount = layResetCount;
	}

}
