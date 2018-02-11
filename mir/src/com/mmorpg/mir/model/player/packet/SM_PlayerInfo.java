package com.mmorpg.mir.model.player.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.artifact.core.ArtifactManager;
import com.mmorpg.mir.model.country.model.CountryOfficial;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.gang.model.GangInfo;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.PetItem;
import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.skill.model.AbnormalVO;
import com.mmorpg.mir.model.soul.core.SoulManager;

public class SM_PlayerInfo {
	private long playerId;
	private short x;
	private short y;
	private byte heading;
	private String name;
	// TODO byte percent
	private long maxHp;
	private long currentHp;
	private long maxBarrier;
	private long currentBarrier;
	private short speed;
	private byte die;

	private short level;
	private byte role;

	private byte rideLevel;
	private int currentHorseAppearanceId;
	private byte soulLevel;
	private byte artifactLevel;
	private short weaponTemplateId;
	private short clothesTemplateId;
	private short petTemplateId;
	/** 是否是皇帝 1-是，0-不是 */
	private byte kingOfking;
	/** 官职的值 @see CountryOfficial */
	private byte officials;

	private byte country;
	private AbnormalVO abnormalVO;

	private String createdRoleServer;// 角色创建服务
	private byte military;// 军衔
	private GangInfo gangInfo; // 帮派信息

	private byte[] complexState; // 个人各种设置信息
	/** 当前刺探 */
	private String currentInvestigate;
	/** 是否有营救任务 */
	private byte rescue;
	/** 砖块的Id */
	private String brickId;
	/** 转职的状态 */
	private byte promoteStage;
	/** 足迹 */
	private byte footprintId;
	/** 装备了的称号 */
	private ArrayList<Integer> equipNickNames;
	/** 全身强化等级 */
	private byte enhanceLevel;
	/** 时装 */
	private byte fashionId;
	/** 兵书阶数 */
	private int warbookGrade;

	public static SM_PlayerInfo valueOf(Player player) {
		SM_PlayerInfo req = new SM_PlayerInfo();
		req.playerId = player.getObjectId();
		if (player.getPosition() != null) {
			req.x = (short) player.getX();
			req.y = (short) player.getY();
			req.heading = player.getHeading();
		}
		req.name = player.getName();
		req.maxHp = player.getGameStats().getCurrentStat(StatEnum.MAXHP);
		req.speed = (short) player.getGameStats().getCurrentStat(StatEnum.SPEED);
		req.setCurrentHp(player.getLifeStats().getCurrentHp());
		req.maxBarrier = player.getGameStats().getCurrentStat(StatEnum.BARRIER);
		req.currentBarrier = player.getLifeStats().getCurrentBarrier();
		req.die = (byte) (player.getLifeStats().isAlreadyDead() ? 1 : 0);
		Equipment weapon = player.getEquipmentStorage().getEquip(EquipmentType.WEAPON);
		req.weaponTemplateId = (weapon == null ? 0 : weapon.getResource().getTemplateId());
		Equipment clothes = player.getEquipmentStorage().getEquip(EquipmentType.CLOTHES);
		req.clothesTemplateId = (clothes == null ? 0 : clothes.getResource().getTemplateId());
		PetItem petItem = player.getEquipmentStorage().getPetItem();
		req.petTemplateId = (petItem == null ? 0 : petItem.getResource().getTemplateId());
		req.kingOfking = player.isKingOfking() ? (byte) 1 : 0;
		req.level = (short) player.getPlayerEnt().getLevel();
		req.role = (byte) player.getPlayerEnt().getRole();
		if (player.isRide()) {
			req.rideLevel = (byte) player.getHorse().getGrade();
			req.currentHorseAppearanceId = player.getHorse().getAppearance().getCurrentAppearance();
		} else {
			req.rideLevel = (byte) 0;
			req.currentHorseAppearanceId = 0;
		}
		req.country = (byte) player.getPlayerEnt().getCountry();
		req.abnormalVO = new AbnormalVO(player.getEffectController());
		req.createdRoleServer = player.getPlayerEnt().getServer();
		req.military = (byte) player.getMilitary().getRank();
		CountryOfficial official = player.getCountry().getCourt().getPlayerOfficial(player);
		if (official != null) {
			req.setOfficials((byte) official.getValue());
		} else {
			req.setOfficials((byte) CountryOfficial.CITIZEN.getValue());
		}
		req.gangInfo = GangInfo.valueOf(player.getGang());
		req.soulLevel = SoulManager.getInstance().isOpen(player) ? (byte) player.getSoul().getLevel() : 0;
		if (ArtifactManager.getInstance().isOpen(player)) {
			req.artifactLevel = (byte) player.getArtifact().getLevel();
		} else {
			req.artifactLevel = (player.getArtifact().getAppLevel() == 1 ? (byte) 1 : (byte) 0);
		}
		req.complexState = player.getComplexState().getStates();
		req.currentInvestigate = player.getInvestigate().getCurrentInvestigate();
		req.rescue = player.getRescue().isStart() ? (byte) 1 : 0;
		req.brickId = player.getTempleHistory().getCurrentBrick() == null ? null : player.getTempleHistory()
				.getCurrentBrick().getId();
		req.promoteStage = (byte) player.getPromotion().getStage();
		req.footprintId = (byte) player.getFootprintPool().getOpenFootprint();
		req.equipNickNames = new ArrayList<Integer>(player.getNicknamePool().getEquipIds());
		req.enhanceLevel = (byte) player.getEquipmentStorage().getSuitStarLevel();
		req.fashionId = (byte) (player.getFashionPool().isHided() ? -1 : player.getFashionPool().getCurrentFashionId());

		if (ModuleOpenManager.getInstance().isOpenByModuleKey(player, ModuleKey.WARBOOK)) {
			req.warbookGrade = player.getWarBook().isHided() ? -1 : player.getWarBook().getGrade();
		} else {
			req.warbookGrade = -1;
		}

		return req;
	}

	public String getCreatedRoleServer() {
		return createdRoleServer;
	}

	public void setCreatedRoleServer(String createdRoleServer) {
		this.createdRoleServer = createdRoleServer;
	}

	public AbnormalVO getAbnormalVO() {
		return abnormalVO;
	}

	public void setAbnormalVO(AbnormalVO abnormalVO) {
		this.abnormalVO = abnormalVO;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public byte getHeading() {
		return heading;
	}

	public void setHeading(byte heading) {
		this.heading = heading;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(long maxHp) {
		this.maxHp = maxHp;
	}

	public long getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(long currentHp) {
		this.currentHp = currentHp;
	}

	public short getX() {
		return x;
	}

	public void setX(short x) {
		this.x = x;
	}

	public short getY() {
		return y;
	}

	public void setY(short y) {
		this.y = y;
	}

	public short getSpeed() {
		return speed;
	}

	public void setSpeed(short speed) {
		this.speed = speed;
	}

	public byte getDie() {
		return die;
	}

	public void setDie(byte die) {
		this.die = die;
	}

	public short getLevel() {
		return level;
	}

	public void setLevel(short level) {
		this.level = level;
	}

	public byte getRole() {
		return role;
	}

	public void setRole(byte role) {
		this.role = role;
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
	}

	public byte getMilitary() {
		return military;
	}

	public void setMilitary(byte military) {
		this.military = military;
	}

	public GangInfo getGangInfo() {
		return gangInfo;
	}

	public void setGangInfo(GangInfo gangInfo) {
		this.gangInfo = gangInfo;
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

	public void setWeaponTemplateId(byte weaponTemplateId) {
		this.weaponTemplateId = weaponTemplateId;
	}

	public void setClothesTemplateId(byte clothesTemplateId) {
		this.clothesTemplateId = clothesTemplateId;
	}

	public byte getKingOfking() {
		return kingOfking;
	}

	public void setKingOfking(byte kingOfking) {
		this.kingOfking = kingOfking;
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

	public byte getOfficials() {
		return officials;
	}

	public void setOfficials(byte officials) {
		this.officials = officials;
	}

	public byte[] getComplexState() {
		return complexState;
	}

	public void setComplexState(byte[] complexState) {
		this.complexState = complexState;
	}

	public String getCurrentInvestigate() {
		return currentInvestigate;
	}

	public void setCurrentInvestigate(String currentInvestigate) {
		this.currentInvestigate = currentInvestigate;
	}

	public byte getRescue() {
		return rescue;
	}

	public void setRescue(byte rescue) {
		this.rescue = rescue;
	}

	public String getBrickId() {
		return brickId;
	}

	public void setBrickId(String brickId) {
		this.brickId = brickId;
	}

	public byte getPromoteStage() {
		return promoteStage;
	}

	public void setPromoteStage(byte promoteStage) {
		this.promoteStage = promoteStage;
	}

	public byte getFootprintId() {
		return footprintId;
	}

	public void setFootprintId(byte footprintId) {
		this.footprintId = footprintId;
	}

	public int getCurrentHorseAppearanceId() {
		return currentHorseAppearanceId;
	}

	public void setCurrentHorseAppearanceId(int currentHorseAppearanceId) {
		this.currentHorseAppearanceId = currentHorseAppearanceId;
	}

	public ArrayList<Integer> getEquipNickNames() {
		return equipNickNames;
	}

	public void setEquipNickNames(ArrayList<Integer> equipNickNames) {
		this.equipNickNames = equipNickNames;
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

	public short getPetTemplateId() {
		return petTemplateId;
	}

	public void setPetTemplateId(short petTemplateId) {
		this.petTemplateId = petTemplateId;
	}

	public long getCurrentBarrier() {
		return currentBarrier;
	}

	public void setCurrentBarrier(long currentBarrier) {
		this.currentBarrier = currentBarrier;
	}

	public long getMaxBarrier() {
		return maxBarrier;
	}

	public void setMaxBarrier(long maxBarrier) {
		this.maxBarrier = maxBarrier;
	}

	public int getWarbookGrade() {
		return warbookGrade;
	}

	public void setWarbookGrade(int warbookGrade) {
		this.warbookGrade = warbookGrade;
	}

}
