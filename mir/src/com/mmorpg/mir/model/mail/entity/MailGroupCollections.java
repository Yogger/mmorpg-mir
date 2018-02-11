package com.mmorpg.mir.model.mail.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.mmorpg.mir.model.mail.model.MailGroup;
import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.Persister;
import com.windforce.common.utility.JsonUtils;

@Entity
@Cached(size = "ten", persister = @Persister("30s"))
public class MailGroupCollections implements IEntity<Long> {
	@Id
	@Column(columnDefinition = "bigint default 0", nullable = false)
	private Long id;
	@Lob
	private String mailGroupJson;

	private long maxGroupId;

	@Transient
	private List<MailGroup> mailGroups;

	public static MailGroupCollections valueOf() {
		MailGroupCollections mc = new MailGroupCollections();
		mc.mailGroupJson = JsonUtils.object2String(new ArrayList<MailGroup>());
		mc.id = 1L;
		return mc;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMailGroupJson() {
		return mailGroupJson;
	}

	public void setMailGroupJson(String mailGroupJson) {
		this.mailGroupJson = mailGroupJson;
	}

	@SuppressWarnings("unchecked")
	public List<MailGroup> getMailGroups() {
		if (mailGroups == null) {
			mailGroups = JsonUtils.string2Collection(mailGroupJson, List.class, MailGroup.class);
		}
		return mailGroups;
	}

	public void setMailGroups(List<MailGroup> mailGroups) {
		this.mailGroups = mailGroups;
	}

	public long getMaxGroupId() {
		return maxGroupId;
	}

	public void setMaxGroupId(long maxGroupId) {
		this.maxGroupId = maxGroupId;
	}

	public void addMailGroup(MailGroup mailGroup) {
		List<MailGroup> mailGroups = getMailGroups();
		mailGroups.add(mailGroup);
		maxGroupId = mailGroup.getId();
	}

	@Override
	public boolean serialize() {
		if (mailGroups != null) {
			mailGroupJson = JsonUtils.object2String(mailGroups);
		}
		return true;
	}
}
