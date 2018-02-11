package com.mmorpg.mir.model.nickname.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.SpringComponentStation;
import com.mmorpg.mir.model.nickname.resource.NicknameResource;

public class Nickname {
	private Integer id;

	private boolean actived;

	private long deprecatedTime;

	@JsonIgnore
	public NicknameResource getResource() {
		return SpringComponentStation.getNicknameManager().getNicknameResources().get(id, true);
	}

	@JsonIgnore
	public boolean isDeprecat() {
		if (deprecatedTime != 0 && deprecatedTime <= System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public void addDeprecatedTime(long addTime) {
		deprecatedTime += addTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public long getDeprecatedTime() {
		return deprecatedTime;
	}

	public void setDeprecatedTime(long deprecatedTime) {
		this.deprecatedTime = deprecatedTime;
	}

	public boolean isActived() {
		return actived;
	}

	public void setActived(boolean actived) {
		this.actived = actived;
	}

}
