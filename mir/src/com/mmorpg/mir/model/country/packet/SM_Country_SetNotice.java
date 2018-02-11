package com.mmorpg.mir.model.country.packet;

public class SM_Country_SetNotice {

	private String notice;

	public static SM_Country_SetNotice valueOf(String notice) {
		SM_Country_SetNotice sm = new SM_Country_SetNotice();
		sm.notice = notice;
		return sm;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

}
