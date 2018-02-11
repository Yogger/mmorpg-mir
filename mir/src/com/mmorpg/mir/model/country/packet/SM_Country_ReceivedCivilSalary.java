package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.country.packet.vo.SalaryStatusVO;

public class SM_Country_ReceivedCivilSalary {

	private SalaryStatusVO salaryStatusVO;

	public static SM_Country_ReceivedCivilSalary valueOf(SalaryStatusVO salaryStatusVO) {
		SM_Country_ReceivedCivilSalary sm = new SM_Country_ReceivedCivilSalary();
		sm.setSalaryStatusVO(salaryStatusVO);
		return sm;
	}

	public SalaryStatusVO getSalaryStatusVO() {
		return salaryStatusVO;
	}

	public void setSalaryStatusVO(SalaryStatusVO salaryStatusVO) {
		this.salaryStatusVO = salaryStatusVO;
	}

}
