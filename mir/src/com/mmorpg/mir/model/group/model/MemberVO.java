package com.mmorpg.mir.model.group.model;

import java.util.Map;

import com.mmorpg.mir.model.artifact.model.ArtifactVO;
import com.mmorpg.mir.model.country.model.CountryOfficial;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.soul.model.SoulVO;

public class MemberVO {
	private long playerId;
	private int level;
	private String name;
	private int role;
	private byte online;
	private Map<Integer, String> equipments;
	private int mapId;
	private int x;
	private int y;
	private int instance;
	private long enterTime;
	private byte kingOfKing;
	private byte officials;
	private SoulVO soulVO;
	private ArtifactVO artifactVO;
	private byte country;
	private int promotionId;
	private byte enhanceLevel;
	private byte fashionId;

	public static MemberVO valueOf(PlayerGroup group, Player player, boolean online) {
		MemberVO sm = new MemberVO();
		sm.playerId = player.getObjectId();
		sm.level = player.getLevel();
		sm.name = player.getName();
		sm.role = player.getPlayerEnt().getRole();
		sm.online = (online ? (byte) 1 : 0);
		sm.setEquipments(player.getEquipmentIds());
		sm.mapId = player.getPosition().getMapId();
		sm.setX(player.getX());
		sm.setY(player.getY());
		sm.setInstance(player.getInstanceId());
		sm.enterTime = group.getEnterTimes().get(player.getObjectId());
		sm.kingOfKing = player.isKingOfking() ? (byte) 1 : 0;
		CountryOfficial official = player.getCountry().getCourt().getPlayerOfficial(player);
		if (official != null) {
			sm.setOfficials((byte) official.getValue());
		} else {
			sm.setOfficials((byte) CountryOfficial.CITIZEN.getValue());
		}
		sm.artifactVO = ArtifactVO.valueOf(player);
		sm.soulVO = SoulVO.valueOf(player);
		sm.country = (byte) player.getCountryValue();
		sm.promotionId = player.getPromotion().getStage();
		sm.enhanceLevel = (byte) player.getEquipmentStorage().getSuitStarLevel();
		sm.fashionId = (byte) (player.getFashionPool().isHided() ? -1 : player.getFashionPool().getCurrentFashionId());
		return sm;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public byte getOnline() {
		return online;
	}

	public void setOnline(byte online) {
		this.online = online;
	}

	public Map<Integer, String> getEquipments() {
		return equipments;
	}

	public void setEquipments(Map<Integer, String> equipments) {
		this.equipments = equipments;
	}

	public int getInstance() {
		return instance;
	}

	public void setInstance(int instance) {
		this.instance = instance;
	}

	public long getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(long enterTime) {
		this.enterTime = enterTime;
	}

	public byte getKingOfKing() {
		return kingOfKing;
	}

	public void setKingOfKing(byte kingOfKing) {
		this.kingOfKing = kingOfKing;
	}

	public byte getOfficials() {
		return officials;
	}

	public void setOfficials(byte officials) {
		this.officials = officials;
	}

	public SoulVO getSoulVO() {
		return soulVO;
	}

	public void setSoulVO(SoulVO soulVO) {
		this.soulVO = soulVO;
	}

	public ArtifactVO getArtifactVO() {
		return artifactVO;
	}

	public void setArtifactVO(ArtifactVO artifactVO) {
		this.artifactVO = artifactVO;
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
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

}
