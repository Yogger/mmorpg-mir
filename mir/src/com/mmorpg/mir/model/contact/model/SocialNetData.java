package com.mmorpg.mir.model.contact.model;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;

public class SocialNetData {
	/** 玩家ID*/
	private long playerId;
	/** 名字*/
	private String name;
	/** 所在的MAPID*/
	private int mapId;
	/** 等级*/
	private int level;
	/** 说说*/
	private String moodPhrase;
	/** 国家*/
	private int countryValue;
	/** 角色*/
	private int roletype;
	
	private int promotionId;
	
	/** 在线状态*/
	@Transient
	private boolean onlineStatus;
	@Transient
	private int mapInstanceId;
	
	@JsonIgnore
	public int getMapInstanceId() {
		return mapInstanceId;
	}
	
	@JsonIgnore
	public void setMapInstanceId(int mapInstanceId) {
		this.mapInstanceId = mapInstanceId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public boolean isOnlineStatus() {
		return onlineStatus;
	}

	@JsonIgnore
	public void setOnlineStatus(boolean onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getMoodPhrase() {
		return moodPhrase;
	}

	public void setMoodPhrase(String moodPhrase) {
		this.moodPhrase = moodPhrase;
	}

	public int getCountryValue() {
		return countryValue;
	}

	public void setCountryValue(int countryValue) {
		this.countryValue = countryValue;
	}

	public int getRoletype() {
		return roletype;
	}

	public void setRoletype(int roletype) {
		this.roletype = roletype;
	}

	public static SocialNetData valueOf(Long id) {
		SocialNetData data = new SocialNetData();
		data.onlineStatus = true;
		data.playerId = id;
		data.name = "";
		data.moodPhrase = "";
		return data;
	}
	
	@JsonIgnore
	public void initData(Player player) {
		playerId = player.getObjectId();
		name = player.getName();
		countryValue = player.getCountryValue();
		roletype = player.getRole();
		promotionId = player.getPromotion().getStage();
	}

	public int getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}

}
