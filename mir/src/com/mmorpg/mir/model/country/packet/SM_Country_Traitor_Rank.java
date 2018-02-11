package com.mmorpg.mir.model.country.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.country.model.vo.TraitorVO;

public class SM_Country_Traitor_Rank {

	private ArrayList<TraitorVO> traitors;
	
	public static SM_Country_Traitor_Rank valueOf(ArrayList<TraitorVO> ranks) {
		SM_Country_Traitor_Rank sm = new SM_Country_Traitor_Rank();
		sm.traitors = ranks;
		return sm;
	}

	public ArrayList<TraitorVO> getTraitors() {
		return traitors;
	}

	public void setTraitors(ArrayList<TraitorVO> traitors) {
		this.traitors = traitors;
	}
	
}
