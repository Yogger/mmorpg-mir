package com.mmorpg.mir.model.country.packet;

import java.util.ArrayList;

import org.h2.util.New;

import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.vo.DiplomacyInfoVO;

public class SM_Country_Diplomacy_Reilve {

	private ArrayList<DiplomacyInfoVO> countrysInfo;

	public static SM_Country_Diplomacy_Reilve valueOf() {
		SM_Country_Diplomacy_Reilve sm = new SM_Country_Diplomacy_Reilve();
		ArrayList<DiplomacyInfoVO> countrysInfo = New.arrayList();
		for (Country country : CountryManager.getInstance().getCountries().values()) {
			DiplomacyInfoVO vo = DiplomacyInfoVO.valueOf(country);
			countrysInfo.add(vo);
		}
		sm.setCountrysInfo(countrysInfo);
		return sm;   
	}

	public ArrayList<DiplomacyInfoVO> getCountrysInfo() {
		return countrysInfo;
	}

	public void setCountrysInfo(ArrayList<DiplomacyInfoVO> countrysInfo) {
		this.countrysInfo = countrysInfo;
	}

}
