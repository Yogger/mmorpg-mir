package com.mmorpg.mir.model.player.entity;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.artifact.packet.vo.ArtifactSimpleVo;
import com.mmorpg.mir.model.combatspirit.manager.CombatSpiritManager;
import com.mmorpg.mir.model.combatspirit.model.CombatSpiritStorage;
import com.mmorpg.mir.model.combatspirit.model.CombatSpiritStorage.CombatSpiritType;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.PlayerGameStatsVO;
import com.mmorpg.mir.model.horse.packet.vo.HorseSimpleVo;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.PetItem;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.soul.packet.vo.SoulSimpleVo;
import com.mmorpg.mir.model.warbook.packet.vo.WarbookSimpleVo;

/**
 * @author 37wan
 */
public class PlayerDetailVO {
	/** 玩家ID **/
	private long id;
	/** 玩家名字 **/
	private String name;
	/** 玩家所在服 **/
	private String server;
	/** 玩家家族名字 **/
	private String gangName;
	/** 装备栏 **/
	private Equipment[] equipments;
	/** 坐骑装备栏 */
	private Equipment[] horseEquipments;
	/** 身上的宠物 **/
	private PetItem petItem;
	/** 玩家角色 **/
	private int role;
	/** 玩家各种属性 **/
	private PlayerGameStatsVO playerGameStatsVO;
	/** 玩家当前血量 **/
	private long hp;
	/** 玩家当前蓝量 **/
	private long mp;
	/** 玩家当前防护值 **/
	private long currentBarrier;
	/** 玩家当前经验 **/
	private long exp;
	/** 玩家总战斗力 **/
	private int battleScore;
	/** 玩家等级 **/
	private int level;
	/** 国家 **/
	private int country;
	/** 英魂 */
	private SoulSimpleVo soulSimpleVo;
	/** 神兵 */
	private ArtifactSimpleVo artifactSimpleVo;
	/** 坐骑 */
	private HorseSimpleVo horseSimpleVo;
	/** 兵书 */
	private WarbookSimpleVo warbookSimpleVo;
	/** 军衔 **/
	private int rank;
	/** 荣誉 **/
	private long honor;
	/** 组队模块开启 **/
	private boolean groupModuleOpen;
	/** 家族模块开启 **/
	private boolean gangModuleOpen;
	/** 宝石模块开启 **/
	private boolean gemModuleOpen;
	/** 佩戴的称号ID **/
	private ArrayList<Integer> equipIds;
	/** 战魂 **/
	private CombatSpiritStorage combatSpiritStorage;
	/** 战魂模块开启 **/
	private boolean[] combatSpiritOpenStatus;
	/** 官员信息 **/
	private byte official;
	/** 皇帝 **/
	private byte kingOfKing;

	private int promotionId;
	/** 时装 */
	private byte fashionId;

	private byte horseMinEnhanceLevel;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public Equipment[] getEquipments() {
		return equipments;
	}

	public void setEquipments(Equipment[] equipments) {
		this.equipments = equipments;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public PlayerGameStatsVO getPlayerGameStatsVO() {
		return playerGameStatsVO;
	}

	public void setPlayerGameStatsVO(PlayerGameStatsVO playerGameStatsVO) {
		this.playerGameStatsVO = playerGameStatsVO;
	}

	public long getHp() {
		return hp;
	}

	public void setHp(long hp) {
		this.hp = hp;
	}

	public long getMp() {
		return mp;
	}

	public void setMp(long mp) {
		this.mp = mp;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public int getBattleScore() {
		return battleScore;
	}

	public void setBattleScore(int battleScore) {
		this.battleScore = battleScore;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public SoulSimpleVo getSoulSimpleVo() {
		return soulSimpleVo;
	}

	public void setSoulSimpleVo(SoulSimpleVo soulSimpleVo) {
		this.soulSimpleVo = soulSimpleVo;
	}

	public ArtifactSimpleVo getArtifactSimpleVo() {
		return artifactSimpleVo;
	}

	public void setArtifactSimpleVo(ArtifactSimpleVo artifactSimpleVo) {
		this.artifactSimpleVo = artifactSimpleVo;
	}

	public HorseSimpleVo getHorseSimpleVo() {
		return horseSimpleVo;
	}

	public void setHorseSimpleVo(HorseSimpleVo horseSimpleVo) {
		this.horseSimpleVo = horseSimpleVo;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public long getHonor() {
		return honor;
	}

	public void setHonor(long honor) {
		this.honor = honor;
	}

	public String getGangName() {
		return gangName;
	}

	public void setGangName(String gangName) {
		this.gangName = gangName;
	}

	public boolean isGroupModuleOpen() {
		return groupModuleOpen;
	}

	public void setGroupModuleOpen(boolean groupModuleOpen) {
		this.groupModuleOpen = groupModuleOpen;
	}

	public boolean isGangModuleOpen() {
		return gangModuleOpen;
	}

	public void setGangModuleOpen(boolean gangModuleOpen) {
		this.gangModuleOpen = gangModuleOpen;
	}

	public CombatSpiritStorage getCombatSpiritStorage() {
		return combatSpiritStorage;
	}

	public void setCombatSpiritStorage(CombatSpiritStorage combatSpiritStorage) {
		this.combatSpiritStorage = combatSpiritStorage;
	}

	public byte getOfficial() {
		return official;
	}

	public void setOfficial(byte official) {
		this.official = official;
	}

	public byte getKingOfKing() {
		return kingOfKing;
	}

	public void setKingOfKing(byte kingOfKing) {
		this.kingOfKing = kingOfKing;
	}

	public int getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}

	public boolean[] getCombatSpiritOpenStatus() {
		return combatSpiritOpenStatus;
	}

	public void setCombatSpiritOpenStatus(boolean[] combatSpiritOpenStatus) {
		this.combatSpiritOpenStatus = combatSpiritOpenStatus;
	}

	@JsonIgnore
	public void setCombatSpiritOpen(Player player, CombatSpiritStorage storage) {
		boolean[] opens = new boolean[CombatSpiritType.values().length];
		for (CombatSpiritType type : CombatSpiritType.values()) {
			String key = CombatSpiritManager.getInstance().COMBAT_SPIRIT_OPID.getValue().get(type.name());
			opens[type.ordinal()] = ModuleOpenManager.getInstance().isOpenByKey(player, key);
		}
		combatSpiritOpenStatus = opens;
	}

	public boolean isGemModuleOpen() {
		return gemModuleOpen;
	}

	public void setGemModuleOpen(boolean gemModuleOpen) {
		this.gemModuleOpen = gemModuleOpen;
	}

	public ArrayList<Integer> getEquipIds() {
		return equipIds;
	}

	public void setEquipIds(ArrayList<Integer> equipIds) {
		this.equipIds = equipIds;
	}

	public byte getFashionId() {
		return fashionId;
	}

	public void setFashionId(byte fashionId) {
		this.fashionId = fashionId;
	}

	public PetItem getPetItem() {
		return petItem;
	}

	public void setPetItem(PetItem petItem) {
		this.petItem = petItem;
	}

	public long getCurrentBarrier() {
		return currentBarrier;
	}

	public void setCurrentBarrier(long currentBarrier) {
		this.currentBarrier = currentBarrier;
	}

	public Equipment[] getHorseEquipments() {
		return horseEquipments;
	}

	public void setHorseEquipments(Equipment[] horseEquipments) {
		this.horseEquipments = horseEquipments;
	}

	public WarbookSimpleVo getWarbookSimpleVo() {
		return warbookSimpleVo;
	}

	public void setWarbookSimpleVo(WarbookSimpleVo warbookSimpleVo) {
		this.warbookSimpleVo = warbookSimpleVo;
	}

	public byte getHorseMinEnhanceLevel() {
		return horseMinEnhanceLevel;
	}

	public void setHorseMinEnhanceLevel(byte horseMinEnhanceLevel) {
		this.horseMinEnhanceLevel = horseMinEnhanceLevel;
	}

}
