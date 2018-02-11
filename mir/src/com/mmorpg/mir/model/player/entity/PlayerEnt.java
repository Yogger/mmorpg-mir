package com.mmorpg.mir.model.player.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.ClearAndMigrate;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.controllers.PlayerController;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;
import com.mmorpg.mir.model.world.WorldPosition;
import com.windforce.common.ramcache.IEntity;
import com.windforce.common.ramcache.anno.Cached;
import com.windforce.common.ramcache.anno.Persister;
import com.windforce.common.ramcache.anno.Unique;
import com.windforce.common.utility.JsonUtils;

@Entity
@Cached(size = "thousand", persister = @Persister("30s"))
@NamedQueries({ @NamedQuery(name = "PlayerEnt.name", query = "from PlayerEnt where name = ?"),
		@NamedQuery(name = "PlayerEnt.accountIndex", query = "from PlayerEnt where accountIndex = ?"),
		@NamedQuery(name = "PlayerEnt.allId", query = "SELECT guid from PlayerEnt"),
		@NamedQuery(name = "PlayerEnt.listIndex", query = "SELECT accountIndex from PlayerEnt"),
		@NamedQuery(name = "PlayerEnt.listName", query = "SELECT name from PlayerEnt"),
		@NamedQuery(name = "PlayerEnt.countryCount", query = "SELECT count(*) from PlayerEnt where country = ?"),
		@NamedQuery(name = "PlayerEnt.roleCount", query = "SELECT count(*) from PlayerEnt where role = ?") })
public class PlayerEnt implements IEntity<Long> {

	/** uid */
	@Id
	@Column(columnDefinition = "bigint default 0", nullable = false)
	private long guid;
	/** 玩家姓名 */
	@Unique(query = "PlayerEnt.name")
	@Column(unique = true, columnDefinition = "varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL")
	private String name;
	/** 等级 */
	@Column(columnDefinition = "int default 0", nullable = false)
	private volatile int level;
	@Column(columnDefinition = "bigint default 0", nullable = false)
	private long exp;
	/** 玩家所属阵营 */
	@Column(columnDefinition = "int default 0", nullable = false)
	private int campId;
	/** 账号 */
	@Column(columnDefinition = "varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL")
	private String accountName;
	/** 账号,account_op_server */
	@Unique(query = "PlayerEnt.accountIndex")
	@Column(unique = true, columnDefinition = "varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL")
	private String accountIndex;
	/** 账号状态 */
	@Column(columnDefinition = "int default 0", nullable = false)
	private int status;
	/** 是否通过防沉迷，1-成年，0-未成年 */
	@Column(columnDefinition = "int default 0", nullable = false)
	private int adult;
	/** 服编号 */
	private String server;
	/** 平台 */
	private String op;
	/** 是否内部账号 */
	@Column(columnDefinition = "bit default 0", nullable = false)
	private boolean gm;
	@Column(columnDefinition = "int default 0", nullable = false)
	/** 地图id*/
	private int mapId;
	@Column(columnDefinition = "int default 0", nullable = false)
	/** 地图x坐标*/
	private int x;
	/** 地图y坐标 */
	@Column(columnDefinition = "int default 0", nullable = false)
	private int y;
	/** 人物朝向 */
	@Column(columnDefinition = "int default 0", nullable = false)
	private byte heading;
	/** 战斗力(只是给管理后台读取用的缓存数据) */
	@Column(columnDefinition = "int default 0", nullable = false)
	private int battleScore;
	/** 装备栏序列化信息 **/
	@Lob
	private String equipStoreJson;
	/** 背包序列化信息 **/
	@Lob
	private String packJson;
	@Lob
	private String wareHouseJson;
	/** 玩家状态 */
	@Lob
	private String complexStateJson;
	/** 生命状态 */
	@Lob
	private String lifeJson;
	@Lob
	private String purseJson;
	@Lob
	private String keyboards;
	@Lob
	private String clientSettings;
	@Lob
	private String coolDownJson;
	@Lob
	private String shoppingHistoryJson;
	@Lob
	private String effectControllerDBJson;
	@Lob
	private String questPoolJson;
	@Lob
	private String horseJson;
	@Lob
	private String operatorJson;
	@Lob
	private String playerGangJson;
	@Lob
	private String skillJson;
	@Lob
	// 人品
	private String rpJson;
	@Lob
	// 英魂
	private String soulJson;
	@Lob
	// 神兵
	private String artifactJson;
	@Lob
	// 福利大厅
	private String welfareJson;
	@Lob
	private String footprintPoolJson;
	// 国家相关信息
	@Lob
	private String countryInfoJson;

	@Lob
	private String dropHistoryJson;
	@Lob
	private String rescueJson;

	private int[] autoBattle;
	@Lob
	private String copyJson;
	@Lob
	private String militaryJson;
	@Lob
	private String vipJson;
	@Lob
	private String expressJson;
	@Lob
	private String templeHistoryJson;
	@Lob
	private String investigateJson;
	@Lob
	private String moduleOpenJson;
	@Lob
	private String rankInfoJson;
	@Lob
	private String addicationJson;
	@Lob
	private String combatSpiritJson;
	@Lob
	private String promotionJson;
	@Lob
	private String warshipJson;
	@Lob
	private String nickNameJson;
	@Lob
	private String openActiveJson;
	@Lob
	private String investJson;
	@Lob
	private String investAgateJson;
	@Lob
	private String levelLogJson;
	@Lob
	private String collectJson;
	@Lob
	private String treasureStorageJson;
	@Lob
	private String countryCopyInfoJson;
	@Lob
	private String blackShopJson;
	@Lob
	private String fashionPoolJson;
	@Lob
	private String mergeActiveJson;
	@Lob
	private String transferInfoJson;
	@Lob
	private String activityJson;
	@Lob
	private String gasCopyJson;
	@Lob
	private String bossJson;
	@Lob
	private String beautyGirlJson;
	@Lob
	private String warBookJson;
	@Lob
	private String horseEquipStoreJson;
	@Lob
	private String lifeGridJson;
	@Lob
	private String suicideJson;
	@Lob
	private String sealJson;
	/** 渠道来源 */
	private String source;
	/** 是否有框 */
	@Column(columnDefinition = "int default 0", nullable = false)
	private int noframe;

	/** 这个player是游戏中间真正做业务的player，所有的业务入口也在这里 **/
	@Transient
	private transient Player player;
	/** 这个对象是用来进行数据统计的对象 */
	@Transient
	private transient PlayerStat stat;
	/** 登陆类型 */
	@Transient
	private int loginType;

	/** 职业 */
	@Column(columnDefinition = "int default 0", nullable = false)
	private int role;
	/** 国家 */
	@Column(columnDefinition = "int default 0", nullable = false)
	private int country;

	@Column(columnDefinition = "int default 0", nullable = false)
	private int soulOfGeneral;
	/** 最后登录时间 */
	private transient Date lastLogin;
	/** 渠道 */
	private String userRefer;

	/**
	 * 唯一的构造方法
	 * 
	 * @param guid
	 * @param name
	 * @param account
	 * @param op
	 * @param server
	 * @return
	 */
	public static PlayerEnt valueOf(long guid, String name, int role, String account, String op, String server,
			CountryId countryId, String source, int noframe, String userRefer) {
		PlayerEnt ent = new PlayerEnt();
		ent.guid = guid;
		ent.name = name;
		ent.level = 1;
		ent.accountName = account;
		ent.accountIndex = account + "_" + op + "_" + server;
		ent.status = 0;
		ent.op = op;
		ent.source = source;
		ent.server = server;
		ent.country = countryId.getValue();
		// 这里先假装设置一个位置
		List<String> result = ChooserManager.getInstance().chooseValueByRequire(countryId.getValue(),
				ConfigValueManager.getInstance().BIRTH_POINT.getValue());
		RelivePosition p = JsonUtils.string2Object(result.get(0), RelivePosition.class);
		ent.mapId = p.getMapId();
		ent.x = p.getX();
		ent.y = p.getY();
		ent.heading = 3;
		ent.role = role;
		ent.userRefer = userRefer;
		ent.creatRealPlayer();

		return ent;

	}

	public void creatRealPlayer() {
		if (ClearAndMigrate.clear) {
			throw new RuntimeException("启动合服模式时有反序列化生成Player");
		}
		// 这里必须要生成一个player对象
		PlayerEnt ent = this;
		PlayerManager.loginCopyPosition(ent.mapId, this);
		WorldMap worldMap = World.getInstance().getWorldMap(mapId);
		WorldPosition position = null;
		if (!worldMap.isCopy()) {
			position = World.getInstance().createPosition(null, ent.mapId, ent.x, ent.y, ent.heading);
		}
		Player player = Player.valueOf(ent, guid, new PlayerController(), position);
		ent.setPlayer(player);
	}

	public long getGuid() {
		return guid;
	}

	public void setGuid(long guid) {
		this.guid = guid;
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

	public void setLevel(int level) {
		this.level = level;
	}

	public String getCountryInfoJson() {
		return countryInfoJson;
	}

	public void setCountryInfoJson(String countryInfoJson) {
		this.countryInfoJson = countryInfoJson;
	}

	public String getWelfareJson() {
		return welfareJson;
	}

	public void setWelfareJson(String welfareJson) {
		this.welfareJson = welfareJson;
	}

	@Override
	public Long getId() {
		return this.guid;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountIndex() {
		return accountIndex;
	}

	public void setAccountIndex(String accountIndex) {
		this.accountIndex = accountIndex;
	}

	public int getCampId() {
		return campId;
	}

	public void setCampId(int campId) {
		this.campId = campId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getAdult() {
		return adult;
	}

	public void setAdult(int adult) {
		this.adult = adult;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public boolean isGm() {
		return gm;
	}

	public void setGm(boolean gm) {
		this.gm = gm;
	}

	@JsonIgnore
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@JsonIgnore
	public PlayerStat getStat() {
		return stat;
	}

	public void setStat(PlayerStat stat) {
		this.stat = stat;
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

	public byte getHeading() {
		return heading;
	}

	public void setHeading(byte heading) {
		this.heading = heading;
	}

	public int getLoginType() {
		return loginType;
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}

	public String getEquipStoreJson() {
		return equipStoreJson;
	}

	public void setEquipStoreJson(String equipStoreJson) {
		this.equipStoreJson = equipStoreJson;
	}

	public String getPackJson() {
		return packJson;
	}

	public void setPackJson(String packJson) {
		this.packJson = packJson;
	}

	public String getWareHouseJson() {
		return wareHouseJson;
	}

	public void setWareHouseJson(String wareHouseJson) {
		this.wareHouseJson = wareHouseJson;
	}

	public String getSoulJson() {
		return soulJson;
	}

	public void setSoulJson(String soulJson) {
		this.soulJson = soulJson;
	}

	public String getArtifactJson() {
		return artifactJson;
	}

	public void setArtifactJson(String artifactJson) {
		this.artifactJson = artifactJson;
	}

	@Override
	public boolean serialize() {
		if (ClearAndMigrate.clear) {
			return true;
		}
		return PlayerManager.getInstance().serialize(this);
	}

	public String getComplexStateJson() {
		return complexStateJson;
	}

	public void setComplexStateJson(String complexStateJson) {
		this.complexStateJson = complexStateJson;
	}

	public String getLifeJson() {
		return lifeJson;
	}

	public void setLifeJson(String lifeJson) {
		this.lifeJson = lifeJson;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getPurseJson() {
		return purseJson;
	}

	public void setPurseJson(String purseJson) {
		this.purseJson = purseJson;
	}

	public String getKeyboards() {
		return keyboards;
	}

	public void setKeyboards(String keyboards) {
		this.keyboards = keyboards;
	}

	public String getCoolDownJson() {
		return coolDownJson;
	}

	public void setCoolDownJson(String coolDownJson) {
		this.coolDownJson = coolDownJson;
	}

	public String getShoppingHistoryJson() {
		return shoppingHistoryJson;
	}

	public void setShoppingHistoryJson(String shoppingHistoryJson) {
		this.shoppingHistoryJson = shoppingHistoryJson;
	}

	public String getEffectControllerDBJson() {
		return effectControllerDBJson;
	}

	public void setEffectControllerDBJson(String effectControllerDBJson) {
		this.effectControllerDBJson = effectControllerDBJson;
	}

	public String getQuestPoolJson() {
		return questPoolJson;
	}

	public void setQuestPoolJson(String questPoolJson) {
		this.questPoolJson = questPoolJson;
	}

	public String getHorseJson() {
		return horseJson;
	}

	public void setHorseJson(String horseJson) {
		this.horseJson = horseJson;
	}

	public String getPlayerGangJson() {
		return playerGangJson;
	}

	public void setPlayerGangJson(String playerGangJson) {
		this.playerGangJson = playerGangJson;
	}

	public String getSkillJson() {
		return skillJson;
	}

	public void setSkillJson(String skillJson) {
		this.skillJson = skillJson;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public int[] getAutoBattle() {
		return autoBattle;
	}

	public void setAutoBattle(int[] autoBattle) {
		this.autoBattle = autoBattle;
	}

	public String getRpJson() {
		return rpJson;
	}

	public void setRpJson(String rpJson) {
		this.rpJson = rpJson;
	}

	public String getCopyJson() {
		return copyJson;
	}

	public void setCopyJson(String copyJson) {
		this.copyJson = copyJson;
	}

	public String getMilitaryJson() {
		return militaryJson;
	}

	public void setMilitaryJson(String militaryJson) {
		this.militaryJson = militaryJson;
	}

	public String getVipJson() {
		return vipJson;
	}

	public void setVipJson(String vipJson) {
		this.vipJson = vipJson;
	}

	public String getExpressJson() {
		return expressJson;
	}

	public void setExpressJson(String expressJson) {
		this.expressJson = expressJson;
	}

	public String getTempleHistoryJson() {
		return templeHistoryJson;
	}

	public void setTempleHistoryJson(String templeHistoryJson) {
		this.templeHistoryJson = templeHistoryJson;
	}

	public String getInvestigateJson() {
		return investigateJson;
	}

	public void setInvestigateJson(String investigateJson) {
		this.investigateJson = investigateJson;
	}

	public String getModuleOpenJson() {
		return moduleOpenJson;
	}

	public void setModuleOpenJson(String moduleOpenJson) {
		this.moduleOpenJson = moduleOpenJson;
	}

	public String getClientSettings() {
		return clientSettings;
	}

	public void setClientSettings(String clientSettings) {
		this.clientSettings = clientSettings;
	}

	public String getRankInfoJson() {
		return rankInfoJson;
	}

	public void setRankInfoJson(String rankInfoJson) {
		this.rankInfoJson = rankInfoJson;
	}

	public final String getCombatSpiritJson() {
		return combatSpiritJson;
	}

	public final void setCombatSpiritJson(String combatSpiritJson) {
		this.combatSpiritJson = combatSpiritJson;
	}

	public String getDropHistoryJson() {
		return dropHistoryJson;
	}

	public void setDropHistoryJson(String dropHistoryJson) {
		this.dropHistoryJson = dropHistoryJson;
	}

	public String getAddicationJson() {
		return addicationJson;
	}

	public void setAddicationJson(String addicationJson) {
		this.addicationJson = addicationJson;
	}

	public int getSoulOfGeneral() {
		return soulOfGeneral;
	}

	public void setSoulOfGeneral(int soulOfGeneral) {
		this.soulOfGeneral = soulOfGeneral;
	}

	public String getPromotionJson() {
		return promotionJson;
	}

	public void setPromotionJson(String promotionJson) {
		this.promotionJson = promotionJson;
	}

	public String getRescueJson() {
		return rescueJson;
	}

	public void setRescueJson(String rescueJson) {
		this.rescueJson = rescueJson;
	}

	public String getFootprintPoolJson() {
		return footprintPoolJson;
	}

	public void setFootprintPoolJson(String footprintPoolJson) {
		this.footprintPoolJson = footprintPoolJson;
	}

	public String getWarshipJson() {
		return warshipJson;
	}

	public void setWarshipJson(String warshipJson) {
		this.warshipJson = warshipJson;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getNoframe() {
		return noframe;
	}

	public void setNoframe(int noframe) {
		this.noframe = noframe;
	}

	public String getOperatorJson() {
		return operatorJson;
	}

	public void setOperatorJson(String operatorJson) {
		this.operatorJson = operatorJson;
	}

	public String getNickNameJson() {
		return nickNameJson;
	}

	public void setNickNameJson(String nickNameJson) {
		this.nickNameJson = nickNameJson;
	}

	public String getOpenActiveJson() {
		return openActiveJson;
	}

	public void setOpenActiveJson(String openActiveJson) {
		this.openActiveJson = openActiveJson;
	}

	public String getInvestJson() {
		return investJson;
	}

	public void setInvestJson(String investJson) {
		this.investJson = investJson;
	}

	public String getLevelLogJson() {
		return levelLogJson;
	}

	public void setLevelLogJson(String levelLogJson) {
		this.levelLogJson = levelLogJson;
	}

	public String getCollectJson() {
		return collectJson;
	}

	public void setCollectJson(String collectJson) {
		this.collectJson = collectJson;
	}

	public String getTreasureStorageJson() {
		return treasureStorageJson;
	}

	public void setTreasureStorageJson(String treasureStorageJson) {
		this.treasureStorageJson = treasureStorageJson;
	}

	public String getCountryCopyInfoJson() {
		return countryCopyInfoJson;
	}

	public void setCountryCopyInfoJson(String countryCopyInfoJson) {
		this.countryCopyInfoJson = countryCopyInfoJson;
	}

	public String getBlackShopJson() {
		return blackShopJson;
	}

	public void setBlackShopJson(String blackShopJson) {
		this.blackShopJson = blackShopJson;
	}

	public String getFashionPoolJson() {
		return fashionPoolJson;
	}

	public void setFashionPoolJson(String fashionPoolJson) {
		this.fashionPoolJson = fashionPoolJson;
	}

	public int getBattleScore() {
		return battleScore;
	}

	public void setBattleScore(int battleScore) {
		this.battleScore = battleScore;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getMergeActiveJson() {
		return mergeActiveJson;
	}

	public void setMergeActiveJson(String mergeActiveJson) {
		this.mergeActiveJson = mergeActiveJson;
	}

	public String getUserRefer() {
		return userRefer;
	}

	public void setUserRefer(String userRefer) {
		this.userRefer = userRefer;
	}

	public String getActivityJson() {
		return activityJson;
	}

	public void setActivityJson(String activityJson) {
		this.activityJson = activityJson;
	}

	public String getGasCopyJson() {
		return gasCopyJson;
	}

	public void setGasCopyJson(String gasCopyJson) {
		this.gasCopyJson = gasCopyJson;
	}

	public String getBossJson() {
		return bossJson;
	}

	public void setBossJson(String bossJson) {
		this.bossJson = bossJson;
	}

	public String getInvestAgateJson() {
		return investAgateJson;
	}

	public void setInvestAgateJson(String investAgateJson) {
		this.investAgateJson = investAgateJson;
	}

	public String getBeautyGirlJson() {
		return beautyGirlJson;
	}

	public void setBeautyGirlJson(String beautyGirlJson) {
		this.beautyGirlJson = beautyGirlJson;
	}

	public String getWarBookJson() {
		return warBookJson;
	}

	public void setWarBookJson(String warBookJson) {
		this.warBookJson = warBookJson;
	}

	public String getHorseEquipStoreJson() {
		return horseEquipStoreJson;
	}

	public void setHorseEquipStoreJson(String horseEquipStoreJson) {
		this.horseEquipStoreJson = horseEquipStoreJson;
	}

	public String getTransferInfoJson() {
		return transferInfoJson;
	}

	public void setTransferInfoJson(String transferInfoJson) {
		this.transferInfoJson = transferInfoJson;
	}

	public String getLifeGridJson() {
		return lifeGridJson;
	}

	public void setLifeGridJson(String lifeGridJson) {
		this.lifeGridJson = lifeGridJson;
	}

	public String getSuicideJson() {
		return suicideJson;
	}

	public void setSuicideJson(String suicideJson) {
		this.suicideJson = suicideJson;
	}

	public String getSealJson() {
		return sealJson;
	}

	public void setSealJson(String sealJson) {
		this.sealJson = sealJson;
	}

}
