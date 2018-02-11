package com.mmorpg.mir.model.mail.packet;

import java.util.Map;

import com.mmorpg.mir.model.mail.model.MailBox;

public class SM_MailUpdate {
	public Map<Integer, Object> mailUpdate;

	public Map<Integer, Object> getMailUpdate() {
		return mailUpdate;
	}

	public void setMailUpdate(Map<Integer, Object> mailUpdate) {
		this.mailUpdate = mailUpdate;
	}

	public static SM_MailUpdate valueOf(MailBox mailBox) {
		SM_MailUpdate req = new SM_MailUpdate();
		req.mailUpdate = mailBox.collectUpdate();
		return req;
	}
}
