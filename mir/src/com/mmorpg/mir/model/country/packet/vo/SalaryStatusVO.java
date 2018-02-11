package com.mmorpg.mir.model.country.packet.vo;

public class SalaryStatusVO {
	
	private int position;//0.国家福利1.官员福利 
	private boolean isCivilSalary;//是否已经发放
	private boolean isReceived;//是否已经领取

	public static SalaryStatusVO valueOf(int position,boolean isCivilSalary,boolean isReceived){
		SalaryStatusVO vo = new SalaryStatusVO();
		vo.setPosition(position);
		vo.setCivilSalary(isCivilSalary);
		vo.setReceived(isReceived);
		return vo;
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isCivilSalary() {
		return isCivilSalary;
	}

	public void setCivilSalary(boolean isCivilSalary) {
		this.isCivilSalary = isCivilSalary;
	}

	public boolean isReceived() {
		return isReceived;
	}

	public void setReceived(boolean isReceived) {
		this.isReceived = isReceived;
	}

}
