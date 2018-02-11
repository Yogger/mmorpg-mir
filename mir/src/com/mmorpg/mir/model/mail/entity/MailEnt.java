package com.mmorpg.mir.model.mail.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.mmorpg.mir.model.mail.model.MailBox;
import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.Persister;
import com.windforce.common.utility.JsonUtils;

@Entity
@Cached(size = "double", persister = @Persister("30s"))
public class MailEnt implements IEntity<Long> {
	@Id
	@Column(columnDefinition = "bigint default 0", nullable = false)
	private Long id;
	@Lob
	private String mailBoxJson;

	@Transient
	private MailBox mailBox;

	public static MailEnt valueOf(Long id) {
		MailEnt mc = new MailEnt();
		mc.mailBoxJson = JsonUtils.object2String(MailBox.valueOf());
		mc.id = id;
		return mc;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMailBoxJson() {
		return mailBoxJson;
	}

	public void setMailBoxJson(String mailBoxJson) {
		this.mailBoxJson = mailBoxJson;
	}

	public MailBox getMailBox() {
		if (mailBox == null) {
			mailBox = JsonUtils.string2Object(mailBoxJson, MailBox.class);
		}
		return mailBox;
	}

	public void setMailBox(MailBox mailBox) {
		this.mailBox = mailBox;
	}

	@Override
	public boolean serialize() {
		if (mailBox != null) {
			mailBoxJson = JsonUtils.object2String(mailBox);
		}
		return true;
	}
}
