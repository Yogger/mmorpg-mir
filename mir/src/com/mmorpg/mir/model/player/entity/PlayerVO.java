package com.mmorpg.mir.model.player.entity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import com.mmorpg.mir.model.addication.packet.AddicationVO;
import com.mmorpg.mir.model.agateinvest.model.vo.InvestAgatePoolVo;
import com.mmorpg.mir.model.artifact.model.ArtifactVO;
import com.mmorpg.mir.model.beauty.packet.vo.BeautyGirlPoolVo;
import com.mmorpg.mir.model.blackshop.packet.vo.BlackShopActivityVo;
import com.mmorpg.mir.model.boss.model.PlayerBossData;
import com.mmorpg.mir.model.collect.model.Collect;
import com.mmorpg.mir.model.combatspirit.model.CombatSpiritStorage;
import com.mmorpg.mir.model.commonactivity.model.vo.CommonActivityPoolVo;
import com.mmorpg.mir.model.contact.model.SocialNetData;
import com.mmorpg.mir.model.controllers.stats.PlayerDeadStatsVO;
import com.mmorpg.mir.model.copy.model.CopyHistoryVO;
import com.mmorpg.mir.model.country.model.vo.CountryFlagQuestVO;
import com.mmorpg.mir.model.country.model.vo.OfficialVO;
import com.mmorpg.mir.model.country.packet.SM_Country_QuestStatus;
import com.mmorpg.mir.model.country.packet.SM_Get_Country_Unitiy_Buff_Floor;
import com.mmorpg.mir.model.country.packet.vo.CountryTechnologyVo;
import com.mmorpg.mir.model.countrycopy.packet.vo.CountryCopyVO;
import com.mmorpg.mir.model.express.model.ExpressVO;
import com.mmorpg.mir.model.fashion.packet.vo.FashionPoolVo;
import com.mmorpg.mir.model.footprint.model.FootprintPool;
import com.mmorpg.mir.model.gameobjects.stats.PlayerGameStatsVO;
import com.mmorpg.mir.model.gang.model.PlayerGangVO;
import com.mmorpg.mir.model.gascopy.model.vo.PlayerGasCopyVO;
import com.mmorpg.mir.model.horse.packet.SM_HorseUpdate;
import com.mmorpg.mir.model.invest.model.vo.InvestPoolVo;
import com.mmorpg.mir.model.investigate.packet.vo.InvestigateVO;
import com.mmorpg.mir.model.item.storage.EquipmentStorage;
import com.mmorpg.mir.model.item.storage.ItemStorage;
import com.mmorpg.mir.model.item.storage.ItemUseVO;
import com.mmorpg.mir.model.item.storage.PlayerItemStorage;
import com.mmorpg.mir.model.item.storage.TreasureItemStorage;
import com.mmorpg.mir.model.lifegrid.packet.vo.LifeGridPoolVo;
import com.mmorpg.mir.model.mail.model.MailBox;
import com.mmorpg.mir.model.mergeactive.model.vo.MergeActiveVo;
import com.mmorpg.mir.model.military.model.MilitaryVO;
import com.mmorpg.mir.model.moduleopen.packet.vo.ModuleOpenVO;
import com.mmorpg.mir.model.nickname.model.vo.NickNamePoolVO;
import com.mmorpg.mir.model.openactive.model.OldSoulActive;
import com.mmorpg.mir.model.openactive.model.vo.CelebrateActivityVo;
import com.mmorpg.mir.model.openactive.model.vo.OldSevenCompeteVO;
import com.mmorpg.mir.model.openactive.model.vo.OpenActiveVo;
import com.mmorpg.mir.model.openactive.model.vo.PublicTestVO;
import com.mmorpg.mir.model.openactive.model.vo.SevenCompeteVO;
import com.mmorpg.mir.model.operator.model.OperatorPool;
import com.mmorpg.mir.model.operator.model.QiHu360PrivilegeVO;
import com.mmorpg.mir.model.operator.model.QiHu360SpeedPrivilegeVO;
import com.mmorpg.mir.model.operator.model.SuperVipVO;
import com.mmorpg.mir.model.promote.model.PromotionVO;
import com.mmorpg.mir.model.purse.model.PurseVO;
import com.mmorpg.mir.model.quest.packet.SM_QuestUpdateVO;
import com.mmorpg.mir.model.rescue.model.Rescue;
import com.mmorpg.mir.model.seal.model.Seal;
import com.mmorpg.mir.model.shop.packet.vo.ShoppingHistoryVO;
import com.mmorpg.mir.model.skill.model.AbnormalVO;
import com.mmorpg.mir.model.skill.model.SkillListVO;
import com.mmorpg.mir.model.soul.model.SoulVO;
import com.mmorpg.mir.model.suicide.packet.vo.SuicideVo;
import com.mmorpg.mir.model.temple.model.TempleVO;
import com.mmorpg.mir.model.vip.model.Vip;
import com.mmorpg.mir.model.warbook.packet.vo.WarbookVo;
import com.mmorpg.mir.model.welfare.model.BossGift;
import com.mmorpg.mir.model.welfare.packet.SM_GiftCollect_Open;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_FirstPay;
import com.mmorpg.mir.model.welfare.packet.vo.PushNumListVO;
import com.mmorpg.mir.model.welfare.packet.vo.SevenDayRewardVo;
import com.mmorpg.mir.transfer.model.TransferInfo;

public class PlayerVO {
	private long id;
	private int x;
	private int y;
	private int mapId;
	private int instanceId;
	private byte heading;
	private String name;
	private String server;
	private long createTime;
	/** 背包 **/
	private PlayerItemStorage pack;
	/** 仓库 **/
	private ItemStorage wareHouse;
	/** 回购背包 **/
	private ItemStorage buyBackPack;
	/** 探宝仓库 */
	private TreasureItemStorage treasureWareHouse;
	/** 装备栏 **/
	private EquipmentStorage equipmentStorage;

	private EquipmentStorage horseEquipStorage;
	/**  **/
	private CombatSpiritStorage combatSpiritStorage;
	/** 邮件背包 **/
	private MailBox mailBox;

	/** 显示活跃度的可以领奖的数量 */
	private int activeLightNum;
	/** 活跃度是否全部领奖完 */
	private boolean activeAllRecieved;
	/** 显示福利大厅可领奖的数量 */
	private int welfareLightNum;
	/** 福利大厅追回后当前事件执行次数显示 */
	private PushNumListVO pushNumListVO;

	private long currentHp;
	private long currentMp;
	private long currentDp;
	private long currentBarrier;

	private int role;

	private PlayerGameStatsVO playerGameStatsVO;

	private int level;

	private long exp;

	private PurseVO purseVO;

	private String keys;

	private SM_QuestUpdateVO questVO;

	private long lastCompletedRandomQuestTime;

	private SM_HorseUpdate horseVO;

	private Map<Integer, Integer> horseSkills;

	private byte ride;

	private PlayerGangVO playerGangVO;

	private SkillListVO skillListVO;

	private int[] autoBattle;

	private int rp;

	private SoulVO soulVO;

	private ArtifactVO artifactVO;

	private CopyHistoryVO copyHistoryVO;

	private PlayerDeadStatsVO playerDeadStatsVO;

	private MilitaryVO militaryVO;

	private int country;

	private long forbidEndTime;

	private long systemTime;

	private byte calltogetherToken;

	private int tankId;

	private ExpressVO expressVO;

	private TempleVO templeVO;

	private InvestigateVO investigateVO;

	private OfficialVO officalVO;

	private AbnormalVO abnormalVO;

	private LinkedList<SocialNetData> lastRecentlyEnemies;

	private String clientSettings;

	private long gangOfWarStartTime;

	private long kingOfWarStartTime;

	private long nextKingOfWarStartTime;

	private long nextGangOfWarStartTime;

	private boolean duringGangOfWar;

	private boolean duringKingOfWar;

	private ModuleOpenVO moduleOpenVO;

	private boolean yesterdayReward;

	private int worldLevel;

	private int worldMilitaryRank;

	private int countryLevel;

	private SM_Country_QuestStatus countryQuestStatus;

	private String flagId;

	private Vip vip;

	private ArrayList<Boolean> oneOffGift;

	private boolean isFate;

	private byte kingOfking;

	private Map<Integer, ArrayList<Integer>> countryWarStatus;

	private long loginoutTimeCount;

	private byte[] complexState;

	private long countryFlagDeadTime;

	private Map<Integer, Integer> hiddenMissionInfo;

	private AddicationVO addicationVO;

	private ArrayList<BossGift> bossGiftList;

	private int soulOfGeneral;

	private long openDate;
	/** 是否可以领取国民福利, true 可以领 */
	private boolean canRecieveCivilSalary;
	/** 是否可以领取官员福利, true 可以领 */
	private boolean canRecieveOfficialSalary;

	private PromotionVO promotionVO;

	private int exerciseAvailable;

	private Rescue rescue;

	private FootprintPool footprintPool;

	private Map<String, Long> huntBossHistory;

	/** 七天登录奖励记录 */
	private SevenDayRewardVo sevenDayRewardVO;

	/** 首充 */
	private SM_Welfare_FirstPay vipFirstPay;

	private String versionMd5;

	private boolean isInWeakCountry;

	private ItemUseVO itemUseVO;

	private Map<Integer, Long> skillContainer;

	private OperatorPool operatorPool;
	private NickNamePoolVO nickNameVos;
	/** 是否需要初始化超级VIP */
	private boolean needInitSuperVip;

	private SuperVipVO superVipVO;

	private InvestPoolVo investPoolVo;

	private InvestAgatePoolVo investAgatePoolVo;

	private OpenActiveVo openActiveVo;

	private SevenCompeteVO sevenCompeteVo;

	private OldSevenCompeteVO oldSevenCompeteVo;

	private OldSoulActive oldSoulActive;

	private SM_GiftCollect_Open giftCollectOpen;

	private boolean attendRiot;

	private long lastRiotEndTime;

	private SM_Get_Country_Unitiy_Buff_Floor unityBuffFloor;

	private Collect collect;

	// private AchievementVO achievementVo;

	private CountryFlagQuestVO flagQuestVO;

	private CountryCopyVO countryCopyVO;

	private boolean reserveKing;

	private long nextDiplomacyStartTime;

	private Map<String, Boolean> bossFHRewardHistory;

	private PublicTestVO publicTestVO;

	private CountryTechnologyVo countryTechnologyVo;

	/** 最后一次领取360大厅礼包的时间 */
	private long lastReward360giftTime;
	/** 总共领取360大厅礼包的时间 */
	private int count360gift;

	private ShoppingHistoryVO shopHistoryVO;

	private boolean techCopying;
	/** 运营信息客户端自存取 */
	private String operatorClintInfo;

	private QiHu360PrivilegeVO qiHuPrivilegeVO;

	private QiHu360SpeedPrivilegeVO qiHuSpeedPrivilegeVO;
	/** 战国庆典 */
	private CelebrateActivityVo celebrateActivityVo;
	/** 时装 */
	private FashionPoolVo fashionPoolVo;

	private int captureInfoCount;

	private BlackShopActivityVo blackShopActivityVo;

	private MergeActiveVo mergeActiveVo;

	private CommonActivityPoolVo commonActivityPoolVo;

	private PlayerGasCopyVO playerGasCopyVO;

	private PlayerBossData bossData;
	/** 美人 */
	private BeautyGirlPoolVo beautyGirlPoolVo;
	// 暴击活动开启次数
	private int weekCriOpenCount;

	private int recollectAllCount;

	private int accLoginDays;

	private int totalSignCount;

	private String serverNamePrefix;

	private WarbookVo warbookVo;

	private LifeGridPoolVo lifeGridPoolVo;

	private TransferInfo transferInfo;

	private boolean isInAssassin;
	
	private boolean isInMinister;

	private SuicideVo suicideVo;

	private Seal seal;
	
	public long getLastReward360giftTime() {
		return lastReward360giftTime;
	}

	public void setLastReward360giftTime(long lastReward360giftTime) {
		this.lastReward360giftTime = lastReward360giftTime;
	}

	public int getCount360gift() {
		return count360gift;
	}

	public void setCount360gift(int count360gift) {
		this.count360gift = count360gift;
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

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public byte getHeading() {
		return heading;
	}

	public void setHeading(byte heading) {
		this.heading = heading;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public PushNumListVO getPushNumListVO() {
		return pushNumListVO;
	}

	public void setPushNumListVO(PushNumListVO pushNumListVO) {
		this.pushNumListVO = pushNumListVO;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(long currentHp) {
		this.currentHp = currentHp;
	}

	public long getCurrentMp() {
		return currentMp;
	}

	public void setCurrentMp(long currentMp) {
		this.currentMp = currentMp;
	}

	public PlayerItemStorage getPack() {
		return pack;
	}

	public void setPack(PlayerItemStorage pack) {
		this.pack = pack;
	}

	public ItemStorage getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(ItemStorage wareHouse) {
		this.wareHouse = wareHouse;
	}

	public EquipmentStorage getEquipmentStorage() {
		return equipmentStorage;
	}

	public SM_Welfare_FirstPay getVipFirstPay() {
		return vipFirstPay;
	}

	public void setVipFirstPay(SM_Welfare_FirstPay vipFirstPay) {
		this.vipFirstPay = vipFirstPay;
	}

	public void setEquipmentStorage(EquipmentStorage equipmentStorage) {
		this.equipmentStorage = equipmentStorage;
	}

	public PlayerGameStatsVO getPlayerGameStatsVO() {
		return playerGameStatsVO;
	}

	public void setPlayerGameStatsVO(PlayerGameStatsVO playerGameStatsVO) {
		this.playerGameStatsVO = playerGameStatsVO;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public PurseVO getPurseVO() {
		return purseVO;
	}

	public void setPurseVO(PurseVO purseVO) {
		this.purseVO = purseVO;
	}

	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public SM_QuestUpdateVO getQuestVO() {
		return questVO;
	}

	public void setQuestVO(SM_QuestUpdateVO questVO) {
		this.questVO = questVO;
	}

	public SM_HorseUpdate getHorseVO() {
		return horseVO;
	}

	public void setHorseVO(SM_HorseUpdate horseVO) {
		this.horseVO = horseVO;
	}

	public byte getRide() {
		return ride;
	}

	public void setRide(byte ride) {
		this.ride = ride;
	}

	public SevenDayRewardVo getSevenDayRewardVO() {
		return sevenDayRewardVO;
	}

	public void setSevenDayRewardVO(SevenDayRewardVo sevenDayRewardVO) {
		this.sevenDayRewardVO = sevenDayRewardVO;
	}

	public PlayerGangVO getPlayerGangVO() {
		return playerGangVO;
	}

	public void setPlayerGangVO(PlayerGangVO playerGangVO) {
		this.playerGangVO = playerGangVO;
	}

	public ItemStorage getBuyBackPack() {
		return buyBackPack;
	}

	public void setBuyBackPack(ItemStorage buyBackPack) {
		this.buyBackPack = buyBackPack;
	}

	public SkillListVO getSkillListVO() {
		return skillListVO;
	}

	public void setSkillListVO(SkillListVO skillListVO) {
		this.skillListVO = skillListVO;
	}

	public MailBox getMailBox() {
		return mailBox;
	}

	public void setMailBox(MailBox mailBox) {
		this.mailBox = mailBox;
	}

	public long getCurrentDp() {
		return currentDp;
	}

	public void setCurrentDp(long currentDp) {
		this.currentDp = currentDp;
	}

	public int[] getAutoBattle() {
		return autoBattle;
	}

	public void setAutoBattle(int[] autoBattle) {
		this.autoBattle = autoBattle;
	}

	public int getRp() {
		return rp;
	}

	public void setRp(int rp) {
		this.rp = rp;
	}

	public SoulVO getSoulVO() {
		return soulVO;
	}

	public void setSoul(SoulVO soulVO) {
		this.soulVO = soulVO;
	}

	public ArtifactVO getArtifactVO() {
		return artifactVO;
	}

	public void setArtifactVO(ArtifactVO artifactVO) {
		this.artifactVO = artifactVO;
	}

	public void setSoulVO(SoulVO soulVO) {
		this.soulVO = soulVO;
	}

	public CopyHistoryVO getCopyHistoryVO() {
		return copyHistoryVO;
	}

	public void setCopyHistoryVO(CopyHistoryVO copyHistoryVO) {
		this.copyHistoryVO = copyHistoryVO;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public long getForbidEndTime() {
		return forbidEndTime;
	}

	public void setForbidEndTime(long forbidEndTime) {
		this.forbidEndTime = forbidEndTime;
	}

	public long getSystemTime() {
		return systemTime;
	}

	public void setSystemTime(long systemTime) {
		this.systemTime = systemTime;
	}

	public byte getCalltogetherToken() {
		return calltogetherToken;
	}

	public void setCalltogetherToken(byte calltogetherToken) {
		this.calltogetherToken = calltogetherToken;
	}

	public int getTankId() {
		return tankId;
	}

	public void setTankId(int tankId) {
		this.tankId = tankId;
	}

	public int getWelfareLightNum() {
		return welfareLightNum;
	}

	public void setWelfareLightNum(int welfareLightNum) {
		this.welfareLightNum = welfareLightNum;
	}

	public TempleVO getTempleVO() {
		return templeVO;
	}

	public void setTempleVO(TempleVO templeVO) {
		this.templeVO = templeVO;
	}

	public InvestigateVO getInvestigateVO() {
		return investigateVO;
	}

	public void setInvestigateVO(InvestigateVO investigateVO) {
		this.investigateVO = investigateVO;
	}

	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

	public OfficialVO getOfficalVO() {
		return officalVO;
	}

	public void setOfficalVO(OfficialVO officalVO) {
		this.officalVO = officalVO;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public LinkedList<SocialNetData> getLastRecentlyEnemies() {
		return lastRecentlyEnemies;
	}

	public void setLastRecentlyEnemies(LinkedList<SocialNetData> lastRecentlyEnemies) {
		this.lastRecentlyEnemies = lastRecentlyEnemies;
	}

	public String getClientSettings() {
		return clientSettings;
	}

	public void setClientSettings(String clientSettings) {
		this.clientSettings = clientSettings;
	}

	public long getGangOfWarStartTime() {
		return gangOfWarStartTime;
	}

	public void setGangOfWarStartTime(long gangOfWarStartTime) {
		this.gangOfWarStartTime = gangOfWarStartTime;
	}

	public long getKingOfWarStartTime() {
		return kingOfWarStartTime;
	}

	public void setKingOfWarStartTime(long kingOfWarStartTime) {
		this.kingOfWarStartTime = kingOfWarStartTime;
	}

	public int getWorldLevel() {
		return worldLevel;
	}

	public void setWorldLevel(int worldLevel) {
		this.worldLevel = worldLevel;
	}

	public SM_Country_QuestStatus getCountryQuestStatus() {
		return countryQuestStatus;
	}

	public void setCountryQuestStatus(SM_Country_QuestStatus countryQuestStatus) {
		this.countryQuestStatus = countryQuestStatus;
	}

	public String getFlagId() {
		return flagId;
	}

	public void setFlagId(String flagId) {
		this.flagId = flagId;
	}

	public ArrayList<Boolean> getOneOffGift() {
		return oneOffGift;
	}

	public void setOneOffGift(ArrayList<Boolean> oneOffGift) {
		this.oneOffGift = oneOffGift;
	}

	public Vip getVip() {
		return vip;
	}

	public void setVip(Vip vip) {
		this.vip = vip;
	}

	public final CombatSpiritStorage getCombatSpiritStorage() {
		return combatSpiritStorage;
	}

	public final void setCombatSpiritStorage(CombatSpiritStorage combatSpiritStorage) {
		this.combatSpiritStorage = combatSpiritStorage;
	}

	public boolean isFate() {
		return isFate;
	}

	public void setFate(boolean isFate) {
		this.isFate = isFate;
	}

	public byte getKingOfking() {
		return kingOfking;
	}

	public void setKingOfking(byte kingOfking) {
		this.kingOfking = kingOfking;
	}

	public Map<Integer, ArrayList<Integer>> getCountryWarStatus() {
		return countryWarStatus;
	}

	public void setCountryWarStatus(Map<Integer, ArrayList<Integer>> countryWarStatus) {
		this.countryWarStatus = countryWarStatus;
	}

	public long getLoginoutTimeCount() {
		return loginoutTimeCount;
	}

	public void setLoginoutTimeCount(long loginoutTimeCount) {
		this.loginoutTimeCount = loginoutTimeCount;
	}

	public byte[] getComplexState() {
		return complexState;
	}

	public void setComplexState(byte[] complexState) {
		this.complexState = complexState;
	}

	public long getCountryFlagDeadTime() {
		return countryFlagDeadTime;
	}

	public void setCountryFlagDeadTime(long countryFlagDeadTime) {
		this.countryFlagDeadTime = countryFlagDeadTime;
	}

	public Map<Integer, Integer> getHiddenMissionInfo() {
		return hiddenMissionInfo;
	}

	public void setHiddenMissionInfo(Map<Integer, Integer> hiddenMissionInfo) {
		this.hiddenMissionInfo = hiddenMissionInfo;
	}

	public long getNextKingOfWarStartTime() {
		return nextKingOfWarStartTime;
	}

	public void setNextKingOfWarStartTime(long nextKingOfWarStartTime) {
		this.nextKingOfWarStartTime = nextKingOfWarStartTime;
	}

	public long getNextGangOfWarStartTime() {
		return nextGangOfWarStartTime;
	}

	public void setNextGangOfWarStartTime(long nextGangOfWarStartTime) {
		this.nextGangOfWarStartTime = nextGangOfWarStartTime;
	}

	public PlayerDeadStatsVO getPlayerDeadStatsVO() {
		return playerDeadStatsVO;
	}

	public void setPlayerDeadStatsVO(PlayerDeadStatsVO playerDeadStatsVO) {
		this.playerDeadStatsVO = playerDeadStatsVO;
	}

	public AddicationVO getAddicationVO() {
		return addicationVO;
	}

	public void setAddicationVO(AddicationVO addicationVO) {
		this.addicationVO = addicationVO;
	}

	public boolean isDuringGangOfWar() {
		return duringGangOfWar;
	}

	public void setDuringGangOfWar(boolean duringGangOfWar) {
		this.duringGangOfWar = duringGangOfWar;
	}

	public boolean isDuringKingOfWar() {
		return duringKingOfWar;
	}

	public void setDuringKingOfWar(boolean duringKingOfWar) {
		this.duringKingOfWar = duringKingOfWar;
	}

	public ArrayList<BossGift> getBossGiftList() {
		return bossGiftList;
	}

	public void setBossGiftList(ArrayList<BossGift> bossGiftList) {
		this.bossGiftList = bossGiftList;
	}

	public int getSoulOfGeneral() {
		return soulOfGeneral;
	}

	public void setSoulOfGeneral(int soulOfGeneral) {
		this.soulOfGeneral = soulOfGeneral;
	}

	public long getOpenDate() {
		return openDate;
	}

	public void setOpenDate(long openDate) {
		this.openDate = openDate;
	}

	public boolean isCanRecieveCivilSalary() {
		return canRecieveCivilSalary;
	}

	public void setCanRecieveCivilSalary(boolean canRecieveCivilSalary) {
		this.canRecieveCivilSalary = canRecieveCivilSalary;
	}

	public boolean isCanRecieveOfficialSalary() {
		return canRecieveOfficialSalary;
	}

	public void setCanRecieveOfficialSalary(boolean canRecieveOfficialSalary) {
		this.canRecieveOfficialSalary = canRecieveOfficialSalary;
	}

	public PromotionVO getPromotionVO() {
		return promotionVO;
	}

	public void setPromotionVO(PromotionVO promotionVO) {
		this.promotionVO = promotionVO;
	}

	public int getExerciseAvailable() {
		return exerciseAvailable;
	}

	public void setExerciseAvailable(int exerciseAvailable) {
		this.exerciseAvailable = exerciseAvailable;
	}

	public Rescue getRescue() {
		return rescue;
	}

	public void setRescue(Rescue rescue) {
		this.rescue = rescue;
	}

	public FootprintPool getFootprintPool() {
		return footprintPool;
	}

	public void setFootprintPool(FootprintPool footprintPool) {
		this.footprintPool = footprintPool;
	}

	public Map<String, Long> getHuntBossHistory() {
		return huntBossHistory;
	}

	public void setHuntBossHistory(Map<String, Long> huntBossHistory) {
		this.huntBossHistory = huntBossHistory;
	}

	public String getVersionMd5() {
		return versionMd5;
	}

	public void setVersionMd5(String versionMd5) {
		this.versionMd5 = versionMd5;
	}

	public int getActiveLightNum() {
		return activeLightNum;
	}

	public void setActiveLightNum(int activeLightNum) {
		this.activeLightNum = activeLightNum;
	}

	public boolean isYesterdayReward() {
		return yesterdayReward;
	}

	public void setYesterdayReward(boolean yesterdayReward) {
		this.yesterdayReward = yesterdayReward;
	}

	public AbnormalVO getAbnormalVO() {
		return abnormalVO;
	}

	public void setAbnormalVO(AbnormalVO abnormalVO) {
		this.abnormalVO = abnormalVO;
	}

	public ExpressVO getExpressVO() {
		return expressVO;
	}

	public void setExpressVO(ExpressVO expressVO) {
		this.expressVO = expressVO;
	}

	public MilitaryVO getMilitaryVO() {
		return militaryVO;
	}

	public void setMilitaryVO(MilitaryVO militaryVO) {
		this.militaryVO = militaryVO;
	}

	public boolean isInWeakCountry() {
		return isInWeakCountry;
	}

	public void setInWeakCountry(boolean isInWeakCountry) {
		this.isInWeakCountry = isInWeakCountry;
	}

	public ItemUseVO getItemUseVO() {
		return itemUseVO;
	}

	public void setItemUseVO(ItemUseVO itemUseVO) {
		this.itemUseVO = itemUseVO;
	}

	public long getLastCompletedRandomQuestTime() {
		return lastCompletedRandomQuestTime;
	}

	public void setLastCompletedRandomQuestTime(long lastCompletedRandomQuestTime) {
		this.lastCompletedRandomQuestTime = lastCompletedRandomQuestTime;
	}

	public Map<Integer, Long> getSkillContainer() {
		return skillContainer;
	}

	public void setSkillContainer(Map<Integer, Long> skillContainer) {
		this.skillContainer = skillContainer;
	}

	public OperatorPool getOperatorPool() {
		return operatorPool;
	}

	public void setOperatorPool(OperatorPool operatorPool) {
		this.operatorPool = operatorPool;
	}

	public boolean isNeedInitSuperVip() {
		return needInitSuperVip;
	}

	public void setNeedInitSuperVip(boolean needInitSuperVip) {
		this.needInitSuperVip = needInitSuperVip;
	}

	public SuperVipVO getSuperVipVO() {
		return superVipVO;
	}

	public void setSuperVipVO(SuperVipVO superVipVO) {
		this.superVipVO = superVipVO;
	}

	public NickNamePoolVO getNickNameVos() {
		return nickNameVos;
	}

	public void setNickNameVos(NickNamePoolVO nickNameVos) {
		this.nickNameVos = nickNameVos;
	}

	public OpenActiveVo getOpenActiveVo() {
		return openActiveVo;
	}

	public void setOpenActiveVo(OpenActiveVo openActiveVo) {
		this.openActiveVo = openActiveVo;
	}

	public InvestPoolVo getInvestPoolVo() {
		return investPoolVo;
	}

	public void setInvestPoolVo(InvestPoolVo investPoolVo) {
		this.investPoolVo = investPoolVo;
	}

	public boolean isActiveAllRecieved() {
		return activeAllRecieved;
	}

	public void setActiveAllRecieved(boolean activeAllRecieved) {
		this.activeAllRecieved = activeAllRecieved;
	}

	public final int getWorldMilitaryRank() {
		return worldMilitaryRank;
	}

	public final void setWorldMilitaryRank(int worldMilitaryRank) {
		this.worldMilitaryRank = worldMilitaryRank;
	}

	public final int getCountryLevel() {
		return countryLevel;
	}

	public final void setCountryLevel(int countryLevel) {
		this.countryLevel = countryLevel;
	}

	public SevenCompeteVO getSevenCompeteVo() {
		return sevenCompeteVo;
	}

	public void setSevenCompeteVo(SevenCompeteVO sevenCompeteVo) {
		this.sevenCompeteVo = sevenCompeteVo;
	}

	public SM_GiftCollect_Open getGiftCollectOpen() {
		return giftCollectOpen;
	}

	public void setGiftCollectOpen(SM_GiftCollect_Open giftCollectOpen) {
		this.giftCollectOpen = giftCollectOpen;
	}

	public boolean isAttendRiot() {
		return attendRiot;
	}

	public void setAttendRiot(boolean attendRiot) {
		this.attendRiot = attendRiot;
	}

	public long getLastRiotEndTime() {
		return lastRiotEndTime;
	}

	public void setLastRiotEndTime(long lastRiotEndTime) {
		this.lastRiotEndTime = lastRiotEndTime;
	}

	public SM_Get_Country_Unitiy_Buff_Floor getUnityBuffFloor() {
		return unityBuffFloor;
	}

	public void setUnityBuffFloor(SM_Get_Country_Unitiy_Buff_Floor unityBuffFloor) {
		this.unityBuffFloor = unityBuffFloor;
	}

	public Collect getCollect() {
		return collect;
	}

	public void setCollect(Collect collect) {
		this.collect = collect;
	}

	public CountryFlagQuestVO getFlagQuestVO() {
		return flagQuestVO;
	}

	public void setFlagQuestVO(CountryFlagQuestVO flagQuestVO) {
		this.flagQuestVO = flagQuestVO;
	}

	public TreasureItemStorage getTreasureWareHouse() {
		return treasureWareHouse;
	}

	public void setTreasureWareHouse(TreasureItemStorage treasureWareHouse) {
		this.treasureWareHouse = treasureWareHouse;
	}

	public CountryCopyVO getCountryCopyVO() {
		return countryCopyVO;
	}

	public void setCountryCopyVO(CountryCopyVO countryCopyVO) {
		this.countryCopyVO = countryCopyVO;
	}

	public boolean isReserveKing() {
		return reserveKing;
	}

	public void setReserveKing(boolean reserveKing) {
		this.reserveKing = reserveKing;
	}

	public long getNextDiplomacyStartTime() {
		return nextDiplomacyStartTime;
	}

	public void setNextDiplomacyStartTime(long nextDiplomacyStartTime) {
		this.nextDiplomacyStartTime = nextDiplomacyStartTime;
	}

	public Map<String, Boolean> getBossFHRewardHistory() {
		return bossFHRewardHistory;
	}

	public void setBossFHRewardHistory(Map<String, Boolean> bossFHRewardHistory) {
		this.bossFHRewardHistory = bossFHRewardHistory;
	}

	public PublicTestVO getPublicTestVO() {
		return publicTestVO;
	}

	public void setPublicTestVO(PublicTestVO publicTestVO) {
		this.publicTestVO = publicTestVO;
	}

	public CountryTechnologyVo getCountryTechnologyVo() {
		return countryTechnologyVo;
	}

	public void setCountryTechnologyVo(CountryTechnologyVo countryTechnologyVo) {
		this.countryTechnologyVo = countryTechnologyVo;
	}

	public ShoppingHistoryVO getShopHistoryVO() {
		return shopHistoryVO;
	}

	public void setShopHistoryVO(ShoppingHistoryVO shopHistoryVO) {
		this.shopHistoryVO = shopHistoryVO;
	}

	public boolean isTechCopying() {
		return techCopying;
	}

	public void setTechCopying(boolean techCopying) {
		this.techCopying = techCopying;
	}

	public String getOperatorClintInfo() {
		return operatorClintInfo;
	}

	public void setOperatorClintInfo(String operatorClintInfo) {
		this.operatorClintInfo = operatorClintInfo;
	}

	public ModuleOpenVO getModuleOpenVO() {
		return moduleOpenVO;
	}

	public void setModuleOpenVO(ModuleOpenVO moduleOpenVO) {
		this.moduleOpenVO = moduleOpenVO;
	}

	public OldSevenCompeteVO getOldSevenCompeteVo() {
		return oldSevenCompeteVo;
	}

	public void setOldSevenCompeteVo(OldSevenCompeteVO oldSevenCompeteVo) {
		this.oldSevenCompeteVo = oldSevenCompeteVo;
	}

	public OldSoulActive getOldSoulActive() {
		return oldSoulActive;
	}

	public void setOldSoulActive(OldSoulActive oldSoulActive) {
		this.oldSoulActive = oldSoulActive;
	}

	public QiHu360PrivilegeVO getQiHuPrivilegeVO() {
		return qiHuPrivilegeVO;
	}

	public void setQiHuPrivilegeVO(QiHu360PrivilegeVO qiHuPrivilegeVO) {
		this.qiHuPrivilegeVO = qiHuPrivilegeVO;
	}

	public CelebrateActivityVo getCelebrateActivityVo() {
		return celebrateActivityVo;
	}

	public void setCelebrateActivityVo(CelebrateActivityVo celebrateActivityVo) {
		this.celebrateActivityVo = celebrateActivityVo;
	}

	public FashionPoolVo getFashionPoolVo() {
		return fashionPoolVo;
	}

	public void setFashionPoolVo(FashionPoolVo fashionPoolVo) {
		this.fashionPoolVo = fashionPoolVo;
	}

	public QiHu360SpeedPrivilegeVO getQiHuSpeedPrivilegeVO() {
		return qiHuSpeedPrivilegeVO;
	}

	public void setQiHuSpeedPrivilegeVO(QiHu360SpeedPrivilegeVO qiHuSpeedPrivilegeVO) {
		this.qiHuSpeedPrivilegeVO = qiHuSpeedPrivilegeVO;
	}

	public Map<Integer, Integer> getHorseSkills() {
		return horseSkills;
	}

	public void setHorseSkills(Map<Integer, Integer> horseSkills) {
		this.horseSkills = horseSkills;
	}

	public int getCaptureInfoCount() {
		return captureInfoCount;
	}

	public void setCaptureInfoCount(int captureInfoCount) {
		this.captureInfoCount = captureInfoCount;
	}

	public BlackShopActivityVo getBlackShopActivityVo() {
		return blackShopActivityVo;
	}

	public void setBlackShopActivityVo(BlackShopActivityVo blackShopActivityVo) {
		this.blackShopActivityVo = blackShopActivityVo;
	}

	public MergeActiveVo getMergeActiveVo() {
		return mergeActiveVo;
	}

	public void setMergeActiveVo(MergeActiveVo mergeActiveVo) {
		this.mergeActiveVo = mergeActiveVo;
	}

	public PlayerGasCopyVO getPlayerGasCopyVO() {
		return playerGasCopyVO;
	}

	public void setPlayerGasCopyVO(PlayerGasCopyVO playerGasCopyVO) {
		this.playerGasCopyVO = playerGasCopyVO;
	}

	public CommonActivityPoolVo getCommonActivityPoolVo() {
		return commonActivityPoolVo;
	}

	public void setCommonActivityPoolVo(CommonActivityPoolVo commonActivityPoolVo) {
		this.commonActivityPoolVo = commonActivityPoolVo;
	}

	public int getWeekCriOpenCount() {
		return weekCriOpenCount;
	}

	public void setWeekCriOpenCount(int weekCriOpenCount) {
		this.weekCriOpenCount = weekCriOpenCount;
	}

	public long getCurrentBarrier() {
		return currentBarrier;
	}

	public void setCurrentBarrier(long currentBarrier) {
		this.currentBarrier = currentBarrier;
	}

	public PlayerBossData getBossData() {
		return bossData;
	}

	public void setBossData(PlayerBossData bossData) {
		this.bossData = bossData;
	}

	public InvestAgatePoolVo getInvestAgatePoolVo() {
		return investAgatePoolVo;
	}

	public void setInvestAgatePoolVo(InvestAgatePoolVo investAgatePoolVo) {
		this.investAgatePoolVo = investAgatePoolVo;
	}

	public int getRecollectAllCount() {
		return recollectAllCount;
	}

	public void setRecollectAllCount(int recollectAllCount) {
		this.recollectAllCount = recollectAllCount;
	}

	public BeautyGirlPoolVo getBeautyGirlPoolVo() {
		return beautyGirlPoolVo;
	}

	public void setBeautyGirlPoolVo(BeautyGirlPoolVo beautyGirlPoolVo) {
		this.beautyGirlPoolVo = beautyGirlPoolVo;
	}

	public int getAccLoginDays() {
		return accLoginDays;
	}

	public void setAccLoginDays(int accLoginDays) {
		this.accLoginDays = accLoginDays;
	}

	public int getTotalSignCount() {
		return totalSignCount;
	}

	public void setTotalSignCount(int totalSignCount) {
		this.totalSignCount = totalSignCount;
	}

	public String getServerNamePrefix() {
		return serverNamePrefix;
	}

	public void setServerNamePrefix(String serverNamePrefix) {
		this.serverNamePrefix = serverNamePrefix;
	}

	public WarbookVo getWarbookVo() {
		return warbookVo;
	}

	public void setWarbookVo(WarbookVo warbookVo) {
		this.warbookVo = warbookVo;
	}

	public EquipmentStorage getHorseEquipStorage() {
		return horseEquipStorage;
	}

	public void setHorseEquipStorage(EquipmentStorage horseEquipStorage) {
		this.horseEquipStorage = horseEquipStorage;
	}

	public LifeGridPoolVo getLifeGridPoolVo() {
		return lifeGridPoolVo;
	}

	public void setLifeGridPoolVo(LifeGridPoolVo lifeGridPoolVo) {
		this.lifeGridPoolVo = lifeGridPoolVo;
	}

	public TransferInfo getTransferInfo() {
		return transferInfo;
	}

	public void setTransferInfo(TransferInfo transferInfo) {
		this.transferInfo = transferInfo;
	}

	public boolean isInAssassin() {
		return isInAssassin;
	}

	public void setInAssassin(boolean isInAssassin) {
		this.isInAssassin = isInAssassin;
	}

	public SuicideVo getSuicideVo() {
		return suicideVo;
	}

	public void setSuicideVo(SuicideVo suicideVo) {
		this.suicideVo = suicideVo;
	}

	public boolean isInMinister() {
		return isInMinister;
	}

	public void setInMinister(boolean isInMinister) {
		this.isInMinister = isInMinister;
	}

	public Seal getSeal() {
		return seal;
	}

	public void setSeal(Seal seal) {
		this.seal = seal;
	}

}
