package com.mmorpg.mir.model.country.packet;

import java.util.ArrayList;

import org.h2.util.New;

import com.mmorpg.mir.model.country.packet.vo.SalaryStatusVO;
import com.mmorpg.mir.model.gameobjects.Player;

/**
 * 领取俸禄的状态
 * 
 * @author 37wan
 * 
 */
public class SM_Country_Check_Salary_Status {

	private ArrayList<SalaryStatusVO> arrayList;

	public static SM_Country_Check_Salary_Status valueOf(Player player) {
		SM_Country_Check_Salary_Status sm = new SM_Country_Check_Salary_Status();
		ArrayList<SalaryStatusVO> arrayList = New.arrayList();
		// 国家福利
		SalaryStatusVO vo = SalaryStatusVO.valueOf(0, player.getCountry().getCourt().isCivilSalary(), player
				.getCountry().getCourt().getCivilReceived().contains(player.getObjectId()));
		arrayList.add(vo);
		// 官员福利
		SalaryStatusVO vo2 = SalaryStatusVO.valueOf(1, player.getCountry().getCourt().isOfficialSalary(), player
				.getCountry().getCourt().getOfficialReceived().contains(player.getObjectId()));
		arrayList.add(vo2);
		sm.setArrayList(arrayList);
		return sm;
	}

	public ArrayList<SalaryStatusVO> getArrayList() {
		return arrayList;
	}

	public void setArrayList(ArrayList<SalaryStatusVO> arrayList) {
		this.arrayList = arrayList;
	}

}
