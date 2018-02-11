package com.mmorpg.mir.model.gang.packet;

public class SM_GangInfo {
	private String code;
	private String infor;

	public static SM_GangInfo valueOf(String infor) {
		SM_GangInfo info = new SM_GangInfo();
		info.setInfor(infor);
		return info;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getInfor() {
		return infor;
	}

	public void setInfor(String infor) {
		this.infor = infor;
	}

}
