package com.mmorpg.mir.model.country.packet;

public class SM_Get_Country_Unitiy_Buff_Floor {
	private int unityBuffFloor;

	public static SM_Get_Country_Unitiy_Buff_Floor valueOf(int unityBuffFloor) {
		SM_Get_Country_Unitiy_Buff_Floor result = new SM_Get_Country_Unitiy_Buff_Floor();
		result.unityBuffFloor = unityBuffFloor;
		return result;
	}

	public int getUnityBuffFloor() {
		return unityBuffFloor;
	}

	public void setUnityBuffFloor(int unityBuffFloor) {
		this.unityBuffFloor = unityBuffFloor;
	}

}
