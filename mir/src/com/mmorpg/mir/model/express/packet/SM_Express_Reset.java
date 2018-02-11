package com.mmorpg.mir.model.express.packet;

import java.util.ArrayList;
import java.util.Arrays;

public class SM_Express_Reset {

	public static SM_Express_Reset valueOf(String... selectLorry) {
		SM_Express_Reset sm = new SM_Express_Reset();
		sm.selectLorry = new ArrayList<String>(Arrays.asList(selectLorry));
		return sm;
	}

	private ArrayList<String> selectLorry;

	public ArrayList<String> getSelectLorry() {
		return selectLorry;
	}

	public void setSelectLorry(ArrayList<String> selectLorry) {
		this.selectLorry = selectLorry;
	}

}
