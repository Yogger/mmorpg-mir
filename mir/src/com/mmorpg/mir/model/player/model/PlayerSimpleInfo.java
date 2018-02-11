package com.mmorpg.mir.model.player.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.i18n.model.I18nPackItem;
import com.mmorpg.mir.model.i18n.model.I18nPackType;

public class PlayerSimpleInfo implements I18nPackItem {
	private long playerId;
	private String name;
	private String server;

	private short level;
	private byte role;
	private byte rideLevel;
	private byte soulLevel;
	private byte artifactLevel;
	private short weaponTemplateId;
	private short clothesTemplateId;
	/** 是否是皇帝 1-是，0-不是 */
	private byte kingOfking;
	/** 官职的值 @see CountryOfficial */
	private byte officials;
	private byte country;
	private int promotionId;
	private byte enhanceLevel;
	private byte fashionId;

	private int warbookGrade;
	private int warbookLevel;

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public byte getRole() {
		return role;
	}

	public void setRole(byte role) {
		this.role = role;
	}

	public byte getRideLevel() {
		return rideLevel;
	}

	public void setRideLevel(byte rideLevel) {
		this.rideLevel = rideLevel;
	}

	public byte getSoulLevel() {
		return soulLevel;
	}

	public void setSoulLevel(byte soulLevel) {
		this.soulLevel = soulLevel;
	}

	public byte getArtifactLevel() {
		return artifactLevel;
	}

	public void setArtifactLevel(byte artifactLevel) {
		this.artifactLevel = artifactLevel;
	}

	public short getWeaponTemplateId() {
		return weaponTemplateId;
	}

	public void setWeaponTemplateId(short weaponTemplateId) {
		this.weaponTemplateId = weaponTemplateId;
	}

	public short getClothesTemplateId() {
		return clothesTemplateId;
	}

	public void setClothesTemplateId(short clothesTemplateId) {
		this.clothesTemplateId = clothesTemplateId;
	}

	public byte getKingOfking() {
		return kingOfking;
	}

	public void setKingOfking(byte kingOfking) {
		this.kingOfking = kingOfking;
	}

	public byte getOfficials() {
		return officials;
	}

	public void setOfficials(byte officials) {
		this.officials = officials;
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
	}

	public void setLevel(short level) {
		this.level = level;
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

	public int getLevel() {
		return level;
	}

	@Override
	@JsonIgnore
	public byte getMessageType() {
		return I18nPackType.PLAYERSIMPLE.getValue();
	}

	public int getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}

	public byte getEnhanceLevel() {
		return enhanceLevel;
	}

	public void setEnhanceLevel(byte enhanceLevel) {
		this.enhanceLevel = enhanceLevel;
	}

	public byte getFashionId() {
		return fashionId;
	}

	public void setFashionId(byte fashionId) {
		this.fashionId = fashionId;
	}

	public int getWarbookGrade() {
		return warbookGrade;
	}

	public void setWarbookGrade(int warbookGrade) {
		this.warbookGrade = warbookGrade;
	}

	public int getWarbookLevel() {
		return warbookLevel;
	}

	public void setWarbookLevel(int warbookLevel) {
		this.warbookLevel = warbookLevel;
	}

}
