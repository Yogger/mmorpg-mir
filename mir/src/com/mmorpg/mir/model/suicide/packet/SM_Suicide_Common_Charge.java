package com.mmorpg.mir.model.suicide.packet;

public class SM_Suicide_Common_Charge {
	private int elementType;
	private int count;

	public static SM_Suicide_Common_Charge valueOf(int elementType, int count) {
		SM_Suicide_Common_Charge result = new SM_Suicide_Common_Charge();
		result.elementType = elementType;
		result.count = count;
		return result;
	}

	public int getElementType() {
		return elementType;
	}

	public void setElementType(int elementType) {
		this.elementType = elementType;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
