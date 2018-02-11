package com.mmorpg.mir.admin.packet;

public class SGM_SendMail {
	private int code;
	private long mailId;

	public long getMailId() {
		return mailId;
	}

	public void setMailId(long mailId) {
		this.mailId = mailId;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
