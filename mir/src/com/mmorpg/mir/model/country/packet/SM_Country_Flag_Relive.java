package com.mmorpg.mir.model.country.packet;

import java.util.ArrayList;

import org.h2.util.New;

import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.vo.CountryFlagInfoVO;

public class SM_Country_Flag_Relive {

	private ArrayList<CountryFlagInfoVO> countrysInfo;

	public static SM_Country_Flag_Relive valueOf() {
		SM_Country_Flag_Relive sm = new SM_Country_Flag_Relive();
		ArrayList<CountryFlagInfoVO> countrysInfo = New.arrayList();
		for (Country country : CountryManager.getInstance().getCountries().values()) {
			CountryFlagInfoVO vo = CountryFlagInfoVO.valueOf(country);
			countrysInfo.add(vo);
		}
		sm.setCountrysInfo(countrysInfo);
		return sm;
	}    

	public ArrayList<CountryFlagInfoVO> getCountrysInfo() {
		return countrysInfo;
	}

	public void setCountrysInfo(ArrayList<CountryFlagInfoVO> countrysInfo) {
		this.countrysInfo = countrysInfo;
	}

}
