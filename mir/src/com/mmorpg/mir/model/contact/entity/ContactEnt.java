package com.mmorpg.mir.model.contact.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.mmorpg.mir.model.contact.model.ContactRelationData;
import com.mmorpg.mir.model.contact.model.SocialNetData;
import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.Persister;
import com.windforce.common.utility.JsonUtils;

@Entity
@Cached(size = "double", persister = @Persister("30s"))
public class ContactEnt implements IEntity<Long>{

	@Id
	@Column(columnDefinition = "bigint default 0", nullable = false)
	private Long playerId;
	
	@Lob
	private String myInfoJson;
	@Lob
	private String relationJson;
	@Column(columnDefinition = "bit default 0", nullable = false)
	private boolean displayOnline = true;
	@Column(columnDefinition = "bit default 0", nullable = false)
	private boolean showHead = true;
	@Column(columnDefinition = "bit default 0", nullable = false)
	private boolean openMapInfo;
	@Column(columnDefinition = "bit default 0", nullable = false)
	private boolean disbandAddFriends;
	
	@Transient
	private SocialNetData mySocialData;
	@Transient
	private ContactRelationData myRelationData;
	
	public static ContactEnt valueOf(Long id) {
		ContactEnt contactEnt = new ContactEnt();
		contactEnt.playerId = id;
		contactEnt.myInfoJson = JsonUtils.object2String(SocialNetData.valueOf(id));
		contactEnt.relationJson = JsonUtils.object2String(ContactRelationData.valueOf());
		return contactEnt;
	}
	
	@Override
	public Long getId() {
		return playerId;
	}

	@Override
	public boolean serialize() {
		if (mySocialData != null) {
			myInfoJson = JsonUtils.object2String(mySocialData);
		}
		if (myRelationData != null) {
			relationJson = JsonUtils.object2String(myRelationData);
		}
		return true;
	}
	
	public SocialNetData getMySocialData() {
		if (mySocialData == null) {
			mySocialData = JsonUtils.string2Object(myInfoJson, SocialNetData.class);
		}
		return mySocialData;
	}

	public void setMySocialData(SocialNetData mySocialData) {
		this.mySocialData = mySocialData;
	}

	public ContactRelationData getMyRelationData() {
		if (myRelationData == null) {
			myRelationData = JsonUtils.string2Object(relationJson, ContactRelationData.class);
		}
		return myRelationData;
	}

	public void setMyRelationData(ContactRelationData myRelationData) {
		this.myRelationData = myRelationData;
	}

	public String getMyInfoJson() {
		return myInfoJson;
	}

	public void setMyInfoJson(String myInfoJson) {
		this.myInfoJson = myInfoJson;
	}

	public String getRelationJson() {
		return relationJson;
	}

	public void setRelationJson(String relationJson) {
		this.relationJson = relationJson;
	}

	public boolean isDisplayOnline() {
		return displayOnline;
	}

	public void setDisplayOnline(boolean displayOnline) {
		this.displayOnline = displayOnline;
	}

	public boolean isShowHead() {
		return showHead;
	}

	public void setShowHead(boolean showHead) {
		this.showHead = showHead;
	}

	public boolean isOpenMapInfo() {
		return openMapInfo;
	}

	public void setOpenMapInfo(boolean openMapInfo) {
		this.openMapInfo = openMapInfo;
	}

	public boolean isDisbandAddFriends() {
    	return disbandAddFriends;
    }

	public void setDisbandAddFriends(boolean disbandAddFriends) {
    	this.disbandAddFriends = disbandAddFriends;
    }
	
}
