package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.country.packet.vo.SalaryStatusVO;

public class SM_Country_ReceivedOfficialSalary {
	private SalaryStatusVO salaryStatusVO;

	public static SM_Country_ReceivedOfficialSalary valueOf(SalaryStatusVO salaryStatusVO) {
		SM_Country_ReceivedOfficialSalary sm = new SM_Country_ReceivedOfficialSalary();
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
