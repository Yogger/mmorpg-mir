package com.mmorpg.mir.model.suicide.packet;

import java.util.HashMap;

import com.mmorpg.mir.model.suicide.model.Suicide;

public class SM_Suicide_Quick_Charge {
	private HashMap<Integer, Integer> elements;

	public static SM_Suicide_Quick_Charge valueOf(Suicide suicide) {
		SM_Suicide_Quick_Charge result = new SM_Suicide_Quick_Charge();
		result.elements = suicide.getElements();
		return result;
	}

	public HashMap<Integer, Integer> getElements() {
		return elements;
	}

	public void setElements(HashMap<Integer, Integer> elements) {
		this.elements = elements;
	}

}
