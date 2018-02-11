package com.mmorpg.mir.model.gameobjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.ServerConfigValue;
import com.mmorpg.mir.model.achievement.model.Achievement;
import com.mmorpg.mir.model.addication.model.Addication;
import com.mmorpg.mir.model.addication.packet.AddicationVO;
import com.mmorpg.mir.model.agateinvest.model.InvestAgatePool;
import com.mmorpg.mir.model.agateinvest.model.vo.InvestAgatePoolVo;
import com.mmorpg.mir.model.artifact.core.ArtifactManager;
import com.mmorpg.mir.model.artifact.model.Artifact;
import com.mmorpg.mir.model.artifact.model.ArtifactVO;
import com.mmorpg.mir.model.artifact.packet.vo.ArtifactSimpleVo;
import com.mmorpg.mir.model.assassin.manager.AssassinManager;
import com.mmorpg.mir.model.beauty.model.BeautyGirlPool;
import com.mmorpg.mir.model.beauty.packet.vo.BeautyGirlPoolVo;
import com.mmorpg.mir.model.blackshop.model.BlackShop;
import com.mmorpg.mir.model.blackshop.packet.vo.BlackShopActivityVo;
import com.mmorpg.mir.model.boss.model.PlayerBossData;
import com.mmorpg.mir.model.capturetown.config.TownConfig;
import com.mmorpg.mir.model.chat.model.ChatCoolTime;
import com.mmorpg.mir.model.collect.model.Collect;
import com.mmorpg.mir.model.combatspirit.model.CombatSpiritStorage;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.commonactivity.CommonActivityConfig;
import com.mmorpg.mir.model.commonactivity.model.CommonActivityPool;
import com.mmorpg.mir.model.commonactivity.model.vo.CommonActivityPoolVo;
import com.mmorpg.mir.model.commonactivity.resource.CommonSPServerResource;
import com.mmorpg.mir.model.complexstate.ComplexState;
import com.mmorpg.mir.model.complexstate.ComplexStateType;
import com.mmorpg.mir.model.contact.manager.ContactManager;
import com.mmorpg.mir.model.controllers.PlayerController;
import com.mmorpg.mir.model.controllers.stats.PlayerDeadStatsVO;
import com.mmorpg.mir.model.controllers.stats.PlayerLifeStats;
import com.mmorpg.mir.model.copy.model.CopyHistory;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.CountryFlag;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.country.model.CountryOfficial;
import com.mmorpg.mir.model.country.model.Official;
import com.mmorpg.mir.model.country.model.PlayerCountryHistory;
import com.mmorpg.mir.model.country.model.Tank;
import com.mmorpg.mir.model.country.packet.SM_Country_QuestStatus;
import com.mmorpg.mir.model.country.packet.SM_Get_Country_Unitiy_Buff_Floor;
import com.mmorpg.mir.model.country.packet.vo.CountryTechnologyVo;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.countrycopy.manager.CountryCopyManager;
import com.mmorpg.mir.model.countrycopy.model.CountryCopyInfo;
import com.mmorpg.mir.model.drop.model.DropHistory;
import com.mmorpg.mir.model.exercise.ExerciseManager;
import com.mmorpg.mir.model.express.model.Express;
import com.mmorpg.mir.model.fashion.model.FashionPool;
import com.mmorpg.mir.model.fashion.packet.vo.FashionPoolVo;
import com.mmorpg.mir.model.footprint.model.FootprintPool;
import com.mmorpg.mir.model.gameobjects.stats.PlayerGameStats;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gang.manager.GangManager;
import com.mmorpg.mir.model.gang.model.Gang;
import com.mmorpg.mir.model.gang.model.PlayerGang;
import com.mmorpg.mir.model.gangofwar.config.GangOfWarConfig;
import com.mmorpg.mir.model.gangofwar.manager.GangOfWarManager;
import com.mmorpg.mir.model.gascopy.model.PlayerGasCopy;
import com.mmorpg.mir.model.gascopy.model.vo.PlayerGasCopyVO;
import com.mmorpg.mir.model.group.model.PlayerGroup;
import com.mmorpg.mir.model.horse.model.Horse;
import com.mmorpg.mir.model.horse.packet.SM_RideBroadcast;
import com.mmorpg.mir.model.horse.packet.SM_UnRideBroadcast;
import com.mmorpg.mir.model.horse.packet.vo.HorseSimpleVo;
import com.mmorpg.mir.model.invest.model.InvestPool;
import com.mmorpg.mir.model.invest.model.vo.InvestPoolVo;
import com.mmorpg.mir.model.investigate.model.Investigate;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.model.EquipmentStorageType;
import com.mmorpg.mir.model.item.storage.EquipmentStorage;
import com.mmorpg.mir.model.item.storage.ItemStorage;
import com.mmorpg.mir.model.item.storage.ItemUseVO;
import com.mmorpg.mir.model.item.storage.PlayerItemStorage;
import com.mmorpg.mir.model.item.storage.TreasureItemStorage;
import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;
import com.mmorpg.mir.model.kingofwar.config.KingOfWarConfig;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.lifegrid.model.LifeGridPool;
import com.mmorpg.mir.model.lifegrid.packet.vo.LifeGridPoolVo;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mergeactive.model.MergeActive;
import com.mmorpg.mir.model.mergeactive.model.vo.MergeActiveVo;
import com.mmorpg.mir.model.military.model.Military;
import com.mmorpg.mir.model.ministerfete.manager.MinisterFeteManager;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.moduleopen.model.ModuleOpen;
import com.mmorpg.mir.model.nickname.model.NicknamePool;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.openactive.model.OpenActive;
import com.mmorpg.mir.model.openactive.model.vo.CelebrateActivityVo;
import com.mmorpg.mir.model.openactive.model.vo.OldSevenCompeteVO;
import com.mmorpg.mir.model.openactive.model.vo.OpenActiveVo;
import com.mmorpg.mir.model.openactive.model.vo.PublicTestVO;
import com.mmorpg.mir.model.openactive.model.vo.SevenCompeteVO;
import com.mmorpg.mir.model.operator.model.OperatorPool;
import com.mmorpg.mir.model.operator.model.QiHu360PrivilegeVO;
import com.mmorpg.mir.model.operator.model.QiHu360SpeedPrivilegeVO;
import com.mmorpg.mir.model.player.entity.PlayerDetailVO;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.mmorpg.mir.model.player.entity.PlayerStat;
import com.mmorpg.mir.model.player.entity.PlayerVO;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;
import com.mmorpg.mir.model.player.model.RP;
import com.mmorpg.mir.model.promote.model.Promotion;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.purse.model.Purse;
import com.mmorpg.mir.model.quest.model.QuestPool;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.rank.model.RankInfo;
import com.mmorpg.mir.model.rescue.model.Rescue;
import com.mmorpg.mir.model.seal.model.Seal;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.shop.model.ShoppingHistory;
import com.mmorpg.mir.model.shop.packet.vo.ShoppingHistoryVO;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.effecttemplate.TraitorEffect;
import com.mmorpg.mir.model.skill.model.AbnormalVO;
import com.mmorpg.mir.model.skill.model.SkillList;
import com.mmorpg.mir.model.soul.core.SoulManager;
import com.mmorpg.mir.model.soul.model.Soul;
import com.mmorpg.mir.model.soul.model.SoulVO;
import com.mmorpg.mir.model.soul.packet.vo.SoulSimpleVo;
import com.mmorpg.mir.model.suicide.model.Suicide;
import com.mmorpg.mir.model.suicide.packet.vo.SuicideVo;
import com.mmorpg.mir.model.temple.model.TempleHistory;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.vip.model.Vip;
import com.mmorpg.mir.model.warbook.model.Warbook;
import com.mmorpg.mir.model.warbook.packet.vo.WarbookSimpleVo;
import com.mmorpg.mir.model.warbook.packet.vo.WarbookVo;
import com.mmorpg.mir.model.warship.model.Warship;
import com.mmorpg.mir.model.welfare.manager.GiftCollectManage;
import com.mmorpg.mir.model.welfare.manager.PublicWelfareManager;
import com.mmorpg.mir.model.welfare.manager.WelfareConfigValueManager;
import com.mmorpg.mir.model.welfare.model.Welfare;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_FirstPay;
import com.mmorpg.mir.model.welfare.packet.vo.PushNumListVO;
import com.mmorpg.mir.model.welfare.packet.vo.SevenDayRewardVo;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.WorldPosition;
import com.mmorpg.mir.model.world.packet.SM_Move;
import com.mmorpg.mir.model.world.packet.SM_Update_Position;
import com.mmorpg.mir.model.world.resource.MapCountry;
import com.mmorpg.mir.transfer.model.TransferInfo;
import com.windforce.common.utility.DateUtils;
import com.xiaosan.socket.core.TSession;

public class Player extends Creature {

	private PlayerEnt playerEnt;
	private AtomicReference<TSession> sessionReference = new AtomicReference<TSession>();
	/** 背包 **/
	private PlayerItemStorage pack;
	/** 仓库 **/
	private ItemStorage wareHouse;
	/** 回购背包 **/
	private ItemStorage buyBackPack;
	/** 探宝仓库 **/
	private TreasureItemStorage treasureWareHouse;
	/** 装备栏 **/
	private EquipmentStorage equipmentStorage;
	/** 坐骑装备栏 */
	private EquipmentStorage horseEquipmentStorage;
	/** 收集道具 **/
	private Collect collect;
	/** 钱包 */
	private Purse purse;
	/** 商店购买记录 */
	private ShoppingHistory shoppingHistory;
	/** 任务 */
	private QuestPool questPool;
	/** 各种请求。etc:交易，组队请求 */
	private ResponseRequester requester;
	/** 组队 */
	private PlayerGroup playerGroup;
	/** 坐骑 */
	private Horse horse;
	/** 交易 */
	private boolean trading;
	/** 帮会信息 */
	private PlayerGang playerGang;
	/** 技能列表 */
	private SkillList skillList;
	/** 聊天CD */
	private ChatCoolTime chatCoolTime = new ChatCoolTime();
	/** 人品 */
	private RP rp;
	/** 英魂 */
	private Soul soul;
	/** 神兵 */
	private Artifact artifact;
	/** 福利 */
	private Welfare welfare;
	// /** 宝宝 */
	// private Summon summon;
	/** 大b哥 **/
	private BigBrother bigBrother;
	/** 营救 */
	private Rescue rescue;
	/** 下一次的本技能施法事件 */
	private long nextSkillTime;
	/** 军衔 */
	private Military military;
	/** 副本 */
	private CopyHistory copyHistory;
	/** VIP */
	private Vip vip;
	/** 运镖 */
	private Express express;
	/** 太庙板砖 */
	private TempleHistory templeHistory;
	/** 刺探 */
	private Investigate investigate;
	/** 最后被玩家攻击时间 */
	private long lastPlayerAttackedTime;
	/** 玩家国家信息 */
	private PlayerCountryHistory playerCountryHistory;
	/** 模块开启信息 */
	private ModuleOpen moduleOpen;
	/** 排行榜的信息 */
	private RankInfo rankInfo;
	/** 防沉迷 */
	private Addication addication;
	/** 战魂 */
	private CombatSpiritStorage combatSpiritStorage;
	/** 怪物击杀记录 */
	private DropHistory dropHistory;
	/** 符合状态管理器,如果该生物为Player状态会持久化 @see {@link ComplexStateType} */
	private ComplexState complexState;

	private int heartBeatTime;

	private long lastHeartBeatTime = System.currentTimeMillis();
	/** 地图切线默认线路 */
	private int defaultMapChannel;

	private Promotion promotion;

	private FootprintPool footprintPool;

	private Warship warship;
	/** 称号 */
	private NicknamePool nicknamePool;
	/** 运营商接口数据 */
	private OperatorPool operatorPool;
	/** 投资理财 */
	private InvestPool investPool;
	/** 聚宝盆投资 */
	private InvestAgatePool investAgatePool;
	/** 开服活动 */
	private OpenActive openActive;
	/** 升级日志 */
	private LevelLog levelLog;
	/** 成就 */
	private Achievement achievement;
	/** 国旗同盟的标记 */
	private int flagStatus;
	/** 国家副本信息 */
	private CountryCopyInfo countryCopyInfo;
	/** 时装 */
	private FashionPool fashionPool;
	/** 最后拾取的时间 */
	private long lastPickUpTime;
	/** 心跳违规次数 */
	private AtomicInteger heartBeatWarnTime = new AtomicInteger(0);
	/** 和服活动 */
	private MergeActive mergeActive;
	/** 所有的公共活动 */
	public CommonActivityPool commonActivityPool;
	/** 黑市 */
	private BlackShop blackShop;

	private PlayerGasCopy gasCopy;

	private PlayerBossData bossData;

	private BeautyGirlPool beautyGirlPool;

	private Warbook warBook;

	private Map<String, Summon> summons = new NonBlockingHashMap<String, Summon>();

	private TransferInfo transferInfo;

	private LifeGridPool lifeGridPool;

	private Suicide suicide;
	
	private Seal seal;

	public PlayerSimpleInfo createSimple() {
		PlayerSimpleInfo info = new PlayerSimpleInfo();
		info.setLevel((short) this.getLevel());
		info.setName(this.getName());
		info.setPlayerId(this.getObjectId());
		info.setRole((byte) this.getRole());
		Equipment weapon = this.getEquipmentStorage().getEquip(EquipmentType.WEAPON);
		info.setWeaponTemplateId((weapon == null ? 0 : weapon.getResource().getTemplateId()));
		Equipment clothes = this.getEquipmentStorage().getEquip(EquipmentType.CLOTHES);
		info.setClothesTemplateId((clothes == null ? 0 : clothes.getResource().getTemplateId()));
		info.setServer(this.getPlayerEnt().getServer());
		info.setCountry((byte) this.getCountryValue());
		info.setSoulLevel(SoulManager.getInstance().isOpen(this) ? (byte) this.getSoul().getLevel() : 0);
		info.setArtifactLevel(ArtifactManager.getInstance().isOpen(this) ? (byte) this.getArtifact().getLevel() : 0);
		info.setKingOfking(this.isKingOfking() ? (byte) 1 : 0);
		Official official = this.getCountry().getPlayerOffical(getObjectId());
		info.setOfficials(official != null ? (byte) official.getOfficial().getValue() : (byte) -1);
		info.setPromotionId(getPromotion().getStage());
		if (SessionManager.getInstance().isOnline(getObjectId())) {
			info.setEnhanceLevel((byte) getEquipmentStorage().getSuitStarLevel());
		} else {
			info.setEnhanceLevel((byte) ItemManager.getInstance().getEnhanceMinLevel(this, EquipmentStorageType.PLAYER));
		}
		info.setFashionId((byte) (getFashionPool().isHided() ? -1 : getFashionPool().getCurrentFashionId()));
		info.setWarbookGrade(getWarBook().getGrade());
		info.setWarbookLevel(getWarBook().getLevel());
		return info;
	}

	public Player(long objId, PlayerController controller, WorldPosition position) {
		super(objId, controller, position);
		controller.setOwner(this);
	}

	public int getRole() {
		return this.getPlayerEnt().getRole();
	}

	public static Player valueOf(PlayerEnt playerEnt, long objId, PlayerController controller, WorldPosition position) {
		Player player = new Player(objId, controller, position);
		player.playerEnt = playerEnt;
		// 这个方法可以修改参数，使用这个方法来创建真正的Player对象，参数修改主要是用来传输一些模版信息，如果有必要的话
		player.setGameStats(new PlayerGameStats(player));
		player.requester = new ResponseRequester(player);
		return player;
	}

	public void sendUpdatePosition() {
		PacketSendUtility.sendPacket(this, SM_Update_Position.valueOf(getMapId(), getX(), getY(), getInstanceId()));
	}

	public void broadCastPosition() {
		PacketSendUtility.broadcastPacket(this, SM_Move.valueOf(this, getX(), getY(), null, (byte) 0));
	}

	@JsonIgnore
	public boolean isInPlayerPk() {
		return (System.currentTimeMillis() - lastPlayerAttackedTime) > PlayerManager.getInstance()
				.getPK_DURATION_TIME().getValue() ? false : true;
	}

	@Override
	public PlayerController getController() {
		return (PlayerController) super.getController();
	}

	@Override
	public PlayerGameStats getGameStats() {
		return (PlayerGameStats) super.getGameStats();
	}

	@Override
	public PlayerLifeStats getLifeStats() {
		return (PlayerLifeStats) super.getLifeStats();
	}

	@JsonIgnore
	public void addUseAuthorityHistory(String authorityId) {
		getCountry().getCourt().addUseAuthorityHistory(this, authorityId);
	}

	@JsonIgnore
	public void addUnityBuff(boolean recomputeStat) {
		int unityBuffFloor = getCountry().getBuffFloor();
		if (unityBuffFloor != 0) {
			Stat[][] stats = ConfigValueManager.getInstance().getCountryUnityBuff();
			if (unityBuffFloor > stats.length) {
				return;
			}
			Stat[] unityStats = stats[unityBuffFloor - 1];
			getGameStats().endModifiers(Country.UNITY_BUFF_ID, false);
			getGameStats().addModifiers(Country.UNITY_BUFF_ID, unityStats, recomputeStat);
		}
	}

	@JsonIgnore
	public void clearUnityBuff() {
		getGameStats().endModifiers(Country.UNITY_BUFF_ID);
	}

	public PlayerEnt getPlayerEnt() {
		return playerEnt;
	}

	public Soul getSoul() {
		return soul;
	}

	public void setSoul(Soul soul) {
		this.soul = soul;
	}

	public Artifact getArtifact() {
		return artifact;
	}

	public void setArtifact(Artifact artifact) {
		this.artifact = artifact;
	}

	public Welfare getWelfare() {
		return welfare;
	}

	public void setWelfare(Welfare welfare) {
		this.welfare = welfare;
	}

	public PlayerCountryHistory getPlayerCountryHistory() {
		return playerCountryHistory;
	}

	public void setPlayerCountryHistory(PlayerCountryHistory playerCountryHistory) {
		this.playerCountryHistory = playerCountryHistory;
	}

	public Country getCountry() {
		return CountryManager.getInstance().getCountry(this);
	}

	public CountryId getCountryId() {
		return CountryId.valueOf(getPlayerEnt().getCountry());
	}

	public boolean sameCountry(Player target) {
		return getCountryId() == target.getCountryId() ? true : false;
	}

	public void setPlayerEnt(PlayerEnt playerEnt) {
		this.playerEnt = playerEnt;
	}

	public TSession getSession() {
		return sessionReference.get();
	}

	public void setSession(TSession session) {
		sessionReference.set(session);
	}

	public boolean compareAndSet(TSession oldS, TSession newS) {
		return sessionReference.compareAndSet(oldS, newS);
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

	public void setEquipmentStorage(EquipmentStorage equipmentStorage) {
		this.equipmentStorage = equipmentStorage;
	}

	@Override
	public boolean isMonsterEnemy(Monster monster) {
		return true;
	}

	@Override
	protected boolean isTownPlayerNpcEnemy(TownPlayerNpc townPlayerNpc) {
		return true;
	}

	@Override
	public boolean isRobotEnemy(Robot creature) {
		return true;
	}

	@Override
	public boolean isBossEnemy(Boss boss) {
		// 特殊活动的判断
		if (getController().inActivity()) {
			return getController().isActivityEnemy(boss);
		}
		return true;
	}

	public boolean isTraitor() {
		return getEffectController().contains(TraitorEffect.TRAITOR);
	}

	/**
	 * @return
	 */
	public PlayerVO createPlayerVO() {
		PlayerVO vo = new PlayerVO();
		vo.setId(playerEnt.getId());
		vo.setName(this.getName());
		vo.setCreateTime(playerEnt.getStat().getCreatedOn().getTime());
		vo.setX(getX());
		vo.setY(getY());
		vo.setMapId(getMapId());
		vo.setInstanceId(getPosition().getInstanceId());
		vo.setHeading(playerEnt.getHeading());
		vo.setMailBox(MailManager.getInstance().getMailEnt(playerEnt.getId()).getMailBox());
		vo.setPack(pack);
		vo.setBuyBackPack(buyBackPack);
		vo.setEquipmentStorage(equipmentStorage);
		vo.setWareHouse(wareHouse);
		vo.setActiveLightNum(PublicWelfareManager.getInstance().countActiveCount(this));
		vo.setActiveAllRecieved(PublicWelfareManager.getInstance().activeRewardAllRecieved(this));
		vo.setWelfareLightNum(PublicWelfareManager.getInstance().countLightNum(this));
		vo.setPushNumListVO(PushNumListVO.valueOf(getPlayerEnt().getPlayer()));
		vo.setServer(playerEnt.getServer());
		vo.setCurrentHp(this.getLifeStats().getCurrentHp());
		vo.setCurrentMp(this.getLifeStats().getCurrentMp());
		vo.setCurrentDp(this.getLifeStats().getCurrentDp());
		vo.setCurrentBarrier(this.getLifeStats().getCurrentBarrier());
		vo.setPlayerGameStatsVO(((PlayerGameStats) this.getGameStats()).createPlayerGameStatsVO());
		vo.setLevel(this.getPlayerEnt().getLevel());
		vo.setExp(this.getPlayerEnt().getExp());
		vo.setPurseVO(this.getPurse().creatPurseVO());
		vo.setKeys(this.getPlayerEnt().getKeyboards());
		vo.setRole(this.getPlayerEnt().getRole());
		vo.setQuestVO(this.getQuestPool().createVO());
		vo.setLastCompletedRandomQuestTime(this.getQuestPool().getLastCompletedRandomQuestTime());
		vo.setHorseVO(this.horse.createVO());
		vo.setSoulVO(SoulVO.valueOf(this));
		vo.setArtifactVO(ArtifactVO.valueOf(this));
		vo.setRide(this.isRide() ? (byte) 1 : (byte) 0);
		vo.setPlayerGangVO(this.getPlayerGang().createVO(getGang()));
		vo.setSkillListVO(skillList.createVO());
		vo.setAutoBattle(getPlayerEnt().getAutoBattle());
		vo.setRp(getRp().getRp());
		vo.setCopyHistoryVO(getCopyHistory().createVO());
		vo.setMilitaryVO(getMilitary().createVO());
		vo.setCountry(getPlayerEnt().getCountry());
		vo.setExpressVO(getExpress().createVO());
		vo.setAbnormalVO(new AbnormalVO(getEffectController()));
		if (getCountry().isForbidchat(objectId)) {
			vo.setForbidEndTime(getCountry().getForbidChats().get(objectId).getEndTime());
		}
		vo.setSystemTime(System.currentTimeMillis());
		vo.setCalltogetherToken(getCountry().getDiplomacy().hasTogetherToken(getObjectId()));
		if (getCountry().getTechnology().getArmsFactory().hasTank(getObjectId())) {
			Tank tank = getCountry().getTechnology().getArmsFactory().getPlayerTank(getObjectId());
			vo.setTankId(tank.getId());
		}
		vo.setTempleVO(getTempleHistory().creatVO());
		vo.setInvestigateVO(getInvestigate().createVO());
		vo.setLastRecentlyEnemies(ContactManager.getInstance().getMyContactRelationData(objectId).getLastEnemiesVO());
		vo.setClientSettings(getPlayerEnt().getClientSettings());
		if (this.getCountry().isOffical(this)) {
			vo.setOfficalVO(getCountry().getPlayerOffical(getObjectId()).creatVO());
		}
		Date gangStart = ServerState.getInstance().getLastGangOfWarDate();
		Date kingStart = ServerState.getInstance().getLastKingOfWarDate();
		vo.setKingOfWarStartTime(kingStart == null ? 0 : kingStart.getTime());
		vo.setGangOfWarStartTime(gangStart == null ? 0 : gangStart.getTime());
		vo.setDuringKingOfWar(KingOfWarManager.getInstance().isWarring());
		vo.setDuringGangOfWar(GangOfWarManager.getInstance().getGangOfWars(this).isWarring());
		vo.setNextKingOfWarStartTime(KingOfWarManager.getInstance().getNextKingOfWarTime());
		vo.setNextGangOfWarStartTime(GangOfWarManager.getInstance().getGangOfWars(this).getNextStartTime(getCountry()));
		vo.setModuleOpenVO(ModuleOpenManager.getInstance().loginRefreshVO(this));
		vo.setYesterdayReward(WorldRankManager.getInstance().yesterdayHeroReward(this));
		vo.setWorldLevel(World.getInstance().getWorldLevel());
		vo.setCountryQuestStatus(SM_Country_QuestStatus.valueOf(getCountry().getCountryQuest(),
				getCountry().getCourt(), !getCountry().getTraitorMapFixs().isEmpty()));
		vo.setFlagId(getCountry().getCountryFlag().getFlagId());
		vo.setOneOffGift(WelfareConfigValueManager.getInstance().oneOffGiftStatus(this));
		vo.setVip(vip);
		vo.setCombatSpiritStorage(getCombatSpiritStorage());
		vo.setFate(DateUtils.isToday(new Date(getWelfare().getLastFeteTime())));
		vo.setKingOfking(isKingOfking() ? (byte) 1 : 0);
		vo.setCountryWarStatus(getCountry().getCountryWarEventStatus(this));
		vo.setLoginoutTimeCount(getWelfare().getOfflineExp().getOfflineCountSeconds());
		vo.setComplexState(getComplexState().getStates());
		vo.setCountryFlagDeadTime(getCountry().getCountryFlag().getDeadTime());
		vo.setHiddenMissionInfo(getPlayerCountryHistory().getHiddenMissionInfo());
		vo.setPlayerDeadStatsVO(PlayerDeadStatsVO.createVO(getLifeStats()));
		vo.setAddicationVO(AddicationVO.valueOf(addication));
		vo.setBossGiftList(getWelfare().getSortedGiftList());
		vo.setSoulOfGeneral(getPlayerEnt().getSoulOfGeneral());
		vo.setOpenDate(ServerState.getInstance().isOpenServer() ? ServerState.getInstance().getOpenServerDate()
				.getTime() : 0L);
		vo.setCanRecieveCivilSalary(CountryManager.getInstance().canRecieveCivilSalary(this));
		vo.setCanRecieveOfficialSalary(CountryManager.getInstance().canRecieveOfficialSalary(this));
		vo.setPromotionVO(getPromotion().createVO());
		vo.setExerciseAvailable(ExerciseManager.getInstance().getExerciseAvailable(this));
		vo.setRescue(getRescue());
		vo.setFootprintPool(getFootprintPool());
		vo.setHuntBossHistory(new HashMap<String, Long>(getDropHistory().getHuntBossHistory()));
		// 7天登陆奖励
		vo.setSevenDayRewardVO(SevenDayRewardVo.valueOf(getWelfare().getSevenDayReward()));
		vo.setVipFirstPay(SM_Welfare_FirstPay.valueOf(getWelfare().isFinishFirstPay(), getWelfare()
				.isDrawVipfirstPayReward()));
		vo.setVersionMd5(ServerConfigValue.versionMd5);
		vo.setInWeakCountry(getCountry().isWeakCountry());
		vo.setItemUseVO(ItemUseVO.valueOf(getPack().getItemDailyLimit()));
		vo.setOperatorPool(getOperatorPool());
		vo.setNickNameVos(getNicknamePool().createVO());
		vo.setInvestPoolVo(InvestPoolVo.valueOf(getInvestPool()));
		vo.setInvestAgatePoolVo(InvestAgatePoolVo.valueOf(getInvestAgatePool()));
		vo.setOpenActiveVo(OpenActiveVo.valueOf(this));
		vo.setWorldMilitaryRank(WorldRankManager.getInstance().getWorldMilitaryRank());
		vo.setCountryLevel(getCountry().getCountryLevel());
		vo.setSevenCompeteVo(SevenCompeteVO.valueOf(this));
		vo.setOldSevenCompeteVo(OldSevenCompeteVO.valueOf(this));
		vo.setOldSoulActive(getOpenActive().getOldSoulActive());
		// vo.setAchievementVo(getAchievement().createVo());
		vo.setGiftCollectOpen(GiftCollectManage.getInstance().getGiftCollect_Open(this));
		vo.setAttendRiot(ServerState.getInstance().getAttendRiotLog().contains(getObjectId()));
		Date riotEnd = ServerState.getInstance().getLastMonsterRiotEndTime(getCountryValue());
		vo.setLastRiotEndTime(riotEnd == null ? 0L : riotEnd.getTime());
		vo.setUnityBuffFloor(SM_Get_Country_Unitiy_Buff_Floor.valueOf(getCountry().getBuffFloor()));
		vo.setCollect(getCollect());
		vo.setFlagQuestVO(getCountry().createFlagQuestVO(this));
		vo.setTreasureWareHouse(getTreasureWareHouse());
		vo.setCountryCopyVO(CountryCopyManager.getInstance().createPlayerCountryCopyVO(this));
		if (ServerState.getInstance().getLastGangOfWarDate() == null) {
			vo.setReserveKing(getCountry().getReserveKing().isReserveKing(this.objectId));
		} else {
			vo.setReserveKing(false);
		}
		vo.setNextDiplomacyStartTime(ServerState.getInstance().getNeverStartDiplomacy() ? 0 : getCountry()
				.getDiplomacy().getNextReliveTime());
		vo.setBossFHRewardHistory(getDropHistory().getBossFHRewardHistory());
		vo.setLastReward360giftTime(getOperatorPool().getOperatorVip().getLastReward360giftTime());
		vo.setCount360gift(getOperatorPool().getOperatorVip().getCount360gift());
		vo.setPublicTestVO(PublicTestVO.valueOf(this));
		vo.setShopHistoryVO(ShoppingHistoryVO.valueOf(this));
		vo.setCountryTechnologyVo(CountryTechnologyVo.valueOf(getCountry().getNewTechnology()));
		vo.setTechCopying(CountryCopyManager.getInstance().isTechCopyWarring(this));
		vo.setOperatorClintInfo(operatorPool.getOperatorClientInfo());
		vo.setQiHuPrivilegeVO(QiHu360PrivilegeVO.valueOf(ServerState.getInstance().getQiHu360PrivilegeServer(), this));
		vo.setFashionPoolVo(FashionPoolVo.valueOf(getFashionPool()));
		vo.setCelebrateActivityVo(CelebrateActivityVo.valueOf(ServerState.getInstance().getCelebrateRecharge()
				.get(getObjectId())));
		vo.setQiHuSpeedPrivilegeVO(QiHu360SpeedPrivilegeVO.valueOf(ServerState.getInstance()
				.getQiHu360SpeedPrivilegeServer(), this));
		vo.setHorseSkills(getHorse().getLearnedSkills());
		vo.setCaptureInfoCount(TownConfig.getInstance().PLAYER_ENTER_DAILY_LIMIT.getValue()
				- getPlayerCountryHistory().getCaptureTownInfo().getDailyCount());
		vo.setBlackShopActivityVo(BlackShopActivityVo.valueOf());
		vo.setMergeActiveVo(MergeActiveVo.valueOf(this));
		vo.setPlayerGasCopyVO(PlayerGasCopyVO.valueOf(this));
		vo.setCommonActivityPoolVo(CommonActivityPoolVo.valueOf(this));
		vo.setWeekCriOpenCount(ServerState.getInstance().getServerEnt().getWeekCriOpenCount());
		vo.setBossData(getBossData());
		if (CommonActivityConfig.getInstance().getRecollectRecievedConditions().verify(this, false)) {
			vo.setRecollectAllCount(getCommonActivityPool().getCurrentRecollectActive().getAllCanClawBackCount(this));
		}
		vo.setBeautyGirlPoolVo(BeautyGirlPoolVo.valueOf(beautyGirlPool));
		vo.setAccLoginDays(getWelfare().getAccLoginDays());
		vo.setTotalSignCount(getWelfare().getSign().getTotalSignCount());
		CommonSPServerResource resource = CommonActivityConfig.getInstance().getResourceContainName(
				ServerState.getInstance().getServerName());
		if (resource == null) {
			vo.setServerNamePrefix("");
		} else {
			vo.setServerNamePrefix(resource.getId());
		}

		vo.setWarbookVo(WarbookVo.valueOf(this.warBook));
		vo.setHorseEquipStorage(getHorseEquipmentStorage());
		vo.setLifeGridPoolVo(LifeGridPoolVo.valueOf(this.lifeGridPool));
		vo.setTransferInfo(getTransferInfo());
		vo.setInAssassin(AssassinManager.getInstance().isInAssassin());
		vo.setSuicideVo(SuicideVo.valueOf(this.suicide));
		vo.setInMinister(MinisterFeteManager.getInstance().isInMinister());
		vo.setSeal(getSeal());
		return vo;
	}

	public PlayerDetailVO createPlayerDetailVO() {
		PlayerDetailVO vo = new PlayerDetailVO();
		vo.setId(playerEnt.getId());
		vo.setName(playerEnt.getName());
		vo.setEquipments(getEquipmentStorage().getEquipments());
		if (ModuleOpenManager.getInstance().isOpenByModuleKey(this, ModuleKey.HORSE_EQUIPSTORE)) {
			vo.setHorseEquipments(getHorseEquipmentStorage().getEquipments());
		}
		vo.setServer(playerEnt.getServer());
		if (this.getGameStats().statSize() == 0) {
			PlayerManager.getInstance().resetPlayerGameStats(this);
			getGameStats().addModifiers(CountryFlag.COUNTRY_FLAG,
					getCountry().getCountryFlag().getResource().getPlayerStats());
		}
		vo.setPlayerGameStatsVO(((PlayerGameStats) this.getGameStats()).createPlayerGameStatsVO());
		vo.setBattleScore(this.getGameStats().calcBattleScore());
		vo.setHp(this.getLifeStats().getCurrentHp());
		vo.setMp(this.getLifeStats().getCurrentMp());
		vo.setCurrentBarrier(this.getLifeStats().getCurrentBarrier());
		vo.setExp(this.getPlayerEnt().getExp());
		vo.setLevel(this.getPlayerEnt().getLevel());
		vo.setRole(this.getPlayerEnt().getRole());
		vo.setCountry(getPlayerEnt().getCountry());
		vo.setRank(military.getRank());
		Long honor = purse.getCurrencies().get(CurrencyType.HONOR);
		vo.setHonor(honor == null ? 0 : honor.longValue());
		if (ModuleOpenManager.getInstance().isOpenByModuleKey(this, ModuleKey.ARTIFACT)) {
			vo.setArtifactSimpleVo(ArtifactSimpleVo.valueOf(this));
		}
		if (ModuleOpenManager.getInstance().isOpenByModuleKey(this, ModuleKey.SOUL_PF)) {
			vo.setSoulSimpleVo(SoulSimpleVo.valueOf(this));
		}
		if (ModuleOpenManager.getInstance().isOpenByModuleKey(this, ModuleKey.HORSE)) {
			vo.setHorseSimpleVo(HorseSimpleVo.valueOf(this));
		}
		if (ModuleOpenManager.getInstance().isOpenByModuleKey(this, ModuleKey.WARBOOK)) {
			vo.setWarbookSimpleVo(WarbookSimpleVo.valueOf(getWarBook()));
		}
		vo.setGangName(getGang() != null ? getGang().getName() : null);
		String groupModuleKey = PlayerManager.getInstance().PLAYER_GROUP_MODULE_KEY.getValue();
		vo.setGroupModuleOpen(ModuleOpenManager.getInstance().isOpenByKey(this, groupModuleKey));
		vo.setGangModuleOpen(ModuleOpenManager.getInstance().isOpenByModuleKey(this, ModuleKey.GANG));
		vo.setGemModuleOpen(ModuleOpenManager.getInstance().isOpenByKey(this, "opmk84"));
		vo.setEquipIds(new ArrayList<Integer>(getNicknamePool().getEquipIds()));
		vo.setCombatSpiritStorage(combatSpiritStorage);
		vo.setCombatSpiritOpen(this, combatSpiritStorage);
		vo.setKingOfKing(isKingOfking() ? (byte) 1 : 0);
		CountryOfficial official = getCountry().getCourt().getPlayerOfficial(this);
		if (official != null) {
			vo.setOfficial((byte) official.getValue());
		} else {
			vo.setOfficial((byte) CountryOfficial.CITIZEN.getValue());
		}
		vo.setPromotionId(getPromotion().getStage());
		vo.setFashionId((byte) (getFashionPool().isHided() ? -1 : getFashionPool().getCurrentFashionId()));
		vo.setPetItem(getEquipmentStorage().getPetItem());
		if (SessionManager.getInstance().isOnline(getObjectId())) {
			vo.setHorseMinEnhanceLevel((byte) getHorseEquipmentStorage().getSuitStarLevel());
		} else {
			vo.setHorseMinEnhanceLevel((byte) ItemManager.getInstance().getEnhanceMinLevel(this,
					EquipmentStorageType.HORSE));
		}
		return vo;
	}

	public Purse getPurse() {
		return purse;
	}

	public void setPurse(Purse purse) {
		this.purse = purse;
	}

	public ShoppingHistory getShoppingHistory() {
		return shoppingHistory;
	}

	public void setShoppingHistory(ShoppingHistory shoppingHistory) {
		this.shoppingHistory = shoppingHistory;
	}

	public QuestPool getQuestPool() {
		return questPool;
	}

	public void setQuestPool(QuestPool questPool) {
		this.questPool = questPool;
	}

	public ResponseRequester getRequester() {
		return requester;
	}

	public void setRequester(ResponseRequester requester) {
		this.requester = requester;
	}

	public PlayerGroup getPlayerGroup() {
		return playerGroup;
	}

	public void setPlayerGroup(PlayerGroup playerGroup) {
		this.playerGroup = playerGroup;
	}

	public Horse getHorse() {
		return horse;
	}

	public void setHorse(Horse horse) {
		this.horse = horse;
	}

	public boolean isTrading() {
		return trading;
	}

	public void setTrading(boolean trading) {
		this.trading = trading;
	}

	public boolean isRide() {
		return this.getComplexState().isState(ComplexStateType.RIDE);
	}

	public int getLevel() {
		return this.getPlayerEnt().getLevel();
	}

	public void ride() {
		if (isRide()) {
			return;
		}
		if (isInPlayerPk()) {
			PacketSendUtility.sendErrorMessage(this, ManagedErrorCode.PLAYER_IN_PK);
			return;
		}
		this.getComplexState().setState(ComplexStateType.RIDE);
		this.getGameStats().addModifiers(Horse.GAME_STATE_SPEEDID, horse.getSpeedStat());
		PacketSendUtility.broadcastPacketAndReceiver(this, SM_RideBroadcast.valueOf(this));
	}

	public void unRide() {
		if (!isRide()) {
			return;
		}
		this.getComplexState().removeState(ComplexStateType.RIDE);
		this.getGameStats().endModifiers(Horse.GAME_STATE_SPEEDID, true);
		PacketSendUtility.broadcastPacketAndReceiver(this, SM_UnRideBroadcast.valueOf(this));
	}

	public Map<Integer, String> getEquipmentIds() {
		Map<Integer, String> equipments = New.hashMap();
		for (int i = 0; i < EquipmentStorage.DEFAULT_SIZE; i++) {
			Equipment equipment = this.getEquipmentStorage().getEquipments()[i];
			if (this.getEquipmentStorage().getEquipments()[i] != null) {
				equipments.put(equipment.getEquipmentType().ordinal(), equipment.getKey());
			}
		}
		return equipments;
	}

	public PlayerGang getPlayerGang() {
		return playerGang;
	}

	public void setPlayerGang(PlayerGang playerGang) {
		this.playerGang = playerGang;
	}

	public boolean isInGroup() {
		return playerGroup != null;
	}

	public boolean isInGang() {
		return playerGang.getGangId() > 0;
	}

	@JsonIgnore
	public Gang getGang() {
		if (!isInGang()) {
			return null;
		}
		Gang gang = GangManager.getInstance().get(playerGang.getGangId());
		if (gang == null) {
			// fix
			// logger.error(String.format("playerId[s%] name[%s] gangId[%s]帮会丢失gangId修正!",
			// getObjectId(), getName(),
			// playerGang.getGangId()));
			playerGang.setGangAndUpdate(0);
		}
		return gang;
	}

	public ItemStorage getBuyBackPack() {
		return buyBackPack;
	}

	public void setBuyBackPack(ItemStorage buyBackPack) {
		this.buyBackPack = buyBackPack;
	}

	@Override
	public String getName() {
		return playerEnt.getName();
	}

	public String getServerAndName() {
		return "s" + playerEnt.getServer() + "." + playerEnt.getName();
	}

	public String getRealName() {
		return playerEnt.getName();
	}

	public SkillList getSkillList() {
		return skillList;
	}

	public void setSkillList(SkillList skillList) {
		this.skillList = skillList;
	}

	public ChatCoolTime getChatCoolTime() {
		return chatCoolTime;
	}

	public void setChatCoolTime(ChatCoolTime chatCoolTime) {
		this.chatCoolTime = chatCoolTime;
	}

	public RP getRp() {
		return rp;
	}

	public void setRp(RP rp) {
		this.rp = rp;
	}

	public CopyHistory getCopyHistory() {
		return copyHistory;
	}

	public void setCopyHistory(CopyHistory copyHistory) {
		this.copyHistory = copyHistory;
	}

	/*
	 * public Summon getSummon() { return summon; }
	 * 
	 * public void setSummon(Summon summon) { this.summon = summon; }
	 */

	@JsonIgnore
	public boolean isInCopy() {
		if (getPosition() == null) {
			return true;
		}
		if (!World.getInstance().getWorldMap(getMapId()).isCopy()) {
			return false;
		}
		return true;
	}

	@JsonIgnore
	public boolean isInCountryActivityMap() {
		if (getPosition() == null || !getPosition().isSpawned()) {
			return false;
		}
		if (getMapId() == GangOfWarConfig.getInstance().MAPID.getValue().intValue()
				|| getMapId() == KingOfWarConfig.getInstance().MAPID.getValue().intValue()) {
			return true;
		}
		return false;
	}

	public long getNextSkillTime() {
		return nextSkillTime;
	}

	public void setNextSkillTime(long nextSkillTime) {
		this.nextSkillTime = nextSkillTime;
	}

	public Military getMilitary() {
		return military;
	}

	public void setMilitary(Military military) {
		this.military = military;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.PLAYER;
	}

	@Override
	public boolean canSee(VisibleObject visibleObject) {
		if (visibleObject.getObjectType() == ObjectType.BIGBROTHER) {
			BigBrother bigBrother = (BigBrother) visibleObject;
			return bigBrother == getBigBrother();
		}
		return true;
	}

	@Override
	public boolean isPlayerEnemy(Player other) {
		// 特殊活动的判断
		if (getController().inActivity()) {
			return getController().isActivityEnemy(other);
		}
		boolean isGmHide = this.getEffectController().isAbnoramlSet(EffectId.GM_HIDE);
		boolean isNewer = other.getLevel() < PlayerManager.getInstance().FRESHMAN_PROTECT.getValue()
				|| getLevel() < PlayerManager.getInstance().FRESHMAN_PROTECT.getValue();
		boolean enemy = getCountryValue() != other.getCountryValue() || other.isTraitor(); // 内奸或者非本国玩家,都是敌人
		boolean result = !isGmHide && (!isNewer) && enemy && enemyNotInSafeArea(other);
		result &= (other.getCountryValue() != getFlagStatus());
		return result;
	}

	@JsonIgnore
	public boolean isMaxLevel() {
		return PlayerManager.getInstance().getPlayerLevelResource(getLevel()).getExp() == 0 ? true : false;
	}

	@Override
	protected boolean isLorryEnemy(Lorry lorry) {
		return !lorry.isGod() && lorry.getCountryValue() != getCountryValue();
	}

	@Override
	protected boolean isCountryNpcEnemy(CountryNpc countryNpc) {
		return countryNpc.getCountry().getValue() != getCountryId().getValue();
	}

	@Override
	protected boolean isCountryObjectEnemy(CountryObject countryObject) {
		return countryObject.getCountry().getValue() != getCountryId().getValue();
	}

	/** 对方是否是在费攻击者安全区内(被攻击的人在本国的保护区或者在中立的保护区都是被保护的) */
	private boolean enemyNotInSafeArea(final Player other) {
		WorldMap currentMap = World.getInstance().getWorldMap(other.getMapId());
		WorldMapInstance instance = currentMap.getWorldMapInstanceById(other.getInstanceId());
		// 保护本国的玩家，中立则都保护
		boolean protectArea = other.getCountryValue() == currentMap.getCountry().getValue()
				|| currentMap.getCountry() == MapCountry.NEUTRAL;
		if (instance.isOpenSafeArea() && protectArea && currentMap.isSafe(other.getX(), other.getY()))
			return false;
		return true;
	}

	public boolean isKingOfking() {
		return KingOfWarManager.getInstance().isKingOfKing(getObjectId());
	}

	public void breakGather() {
		if (getEffectController().isAbnoramlSet(EffectId.GATHER)) {
			getEffectController().unsetAbnormal(EffectId.GATHER, true);
			removeGatherCoolDown();
		}
	}

	public boolean isKing() {
		if (getCountry().getKing() != this) {
			return false;
		}
		return true;
	}

	public Vip getVip() {
		return vip;
	}

	public void setVip(Vip vip) {
		this.vip = vip;
	}

	@Override
	public int getCountryValue() {
		return getPlayerEnt().getCountry();
	}

	public Express getExpress() {
		return express;
	}

	public void setExpress(Express express) {
		this.express = express;
	}

	public TempleHistory getTempleHistory() {
		return templeHistory;
	}

	public void setTempleHistory(TempleHistory templeHistory) {
		this.templeHistory = templeHistory;
	}

	public Investigate getInvestigate() {
		return investigate;
	}

	public void setInvestigate(Investigate investigate) {
		this.investigate = investigate;
	}

	public long getLastPlayerAttackedTime() {
		return lastPlayerAttackedTime;
	}

	public void setLastPlayerAttackedTime(long lastPlayerAttackedTime) {
		this.lastPlayerAttackedTime = lastPlayerAttackedTime;
	}

	public ModuleOpen getModuleOpen() {
		return moduleOpen;
	}

	public void setModuleOpen(ModuleOpen moduleOpen) {
		this.moduleOpen = moduleOpen;
	}

	public RankInfo getRankInfo() {
		return rankInfo;
	}

	public void setRankInfo(RankInfo rankInfo) {
		this.rankInfo = rankInfo;
	}

	public final CombatSpiritStorage getCombatSpiritStorage() {
		return combatSpiritStorage;
	}

	public final void setCombatSpiritStorage(CombatSpiritStorage combatSpiritStorage) {
		this.combatSpiritStorage = combatSpiritStorage;
	}

	public DropHistory getDropHistory() {
		return dropHistory;
	}

	public void setDropHistory(DropHistory dropHistory) {
		this.dropHistory = dropHistory;
	}

	public ComplexState getComplexState() {
		return complexState;
	}

	public void setComplexState(ComplexState complexState) {
		this.complexState = complexState;
	}

	public int getHeartBeatTime() {
		return heartBeatTime;
	}

	public void setHeartBeatTime(int heartBeatTime) {
		this.heartBeatTime = heartBeatTime;
	}

	public long getLastHeartBeatTime() {
		return lastHeartBeatTime;
	}

	public void setLastHeartBeatTime(long lastHeartBeatTime) {
		this.lastHeartBeatTime = lastHeartBeatTime;
	}

	public PlayerStat getPlayerStat() {
		return getPlayerEnt().getStat();
	}

	public Addication getAddication() {
		return addication;
	}

	public void setAddication(Addication addication) {
		this.addication = addication;
	}

	public Rescue getRescue() {
		return rescue;
	}

	public void setRescue(Rescue rescue) {
		this.rescue = rescue;
	}

	public int getDefaultMapChannel() {
		return defaultMapChannel;
	}

	public void setDefaultMapChannel(int defaultMapChannel) {
		this.defaultMapChannel = defaultMapChannel;
	}

	public Promotion getPromotion() {
		return promotion;
	}

	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}

	public FootprintPool getFootprintPool() {
		return footprintPool;
	}

	public void setFootprintPool(FootprintPool footprintPool) {
		this.footprintPool = footprintPool;
	}

	public Warship getWarship() {
		return warship;
	}

	public void setWarship(Warship warship) {
		this.warship = warship;
	}

	public NicknamePool getNicknamePool() {
		return nicknamePool;
	}

	public void setNicknamePool(NicknamePool nicknamePool) {
		this.nicknamePool = nicknamePool;
	}

	public OperatorPool getOperatorPool() {
		return operatorPool;
	}

	public void setOperatorPool(OperatorPool operatorPool) {
		this.operatorPool = operatorPool;
	}

	public OpenActive getOpenActive() {
		return openActive;
	}

	public void setOpenActive(OpenActive openActive) {
		this.openActive = openActive;
	}

	public InvestPool getInvestPool() {
		return investPool;
	}

	public void setInvestPool(InvestPool investPool) {
		this.investPool = investPool;
	}

	public LevelLog getLevelLog() {
		return levelLog;
	}

	public void setLevelLog(LevelLog levelLog) {
		this.levelLog = levelLog;
	}

	public Achievement getAchievement() {
		return achievement;
	}

	public void setAchievement(Achievement achievement) {
		this.achievement = achievement;
	}

	public BigBrother getBigBrother() {
		return bigBrother;
	}

	public void setBigBrother(BigBrother bigBrother) {
		this.bigBrother = bigBrother;
	}

	public TreasureItemStorage getTreasureWareHouse() {
		return treasureWareHouse;
	}

	public void setTreasureWareHouse(TreasureItemStorage treasureWareHouse) {
		this.treasureWareHouse = treasureWareHouse;
	}

	public Collect getCollect() {
		return collect;
	}

	public void setCollect(Collect collect) {
		this.collect = collect;
	}

	public int getFlagStatus() {
		return flagStatus;
	}

	public void setFlagStatus(int flagStatus) {
		this.flagStatus = flagStatus;
	}

	public void clearFlagAllianceState() {
		this.flagStatus = 0;
		getEffectController().unsetAbnormal(EffectId.ALLIANCE_QI, false);
		getEffectController().unsetAbnormal(EffectId.ALLIANCE_CHU, false);
		getEffectController().unsetAbnormal(EffectId.ALLIANCE_ZHAO, true);
	}

	public CountryCopyInfo getCountryCopyInfo() {
		return countryCopyInfo;
	}

	public void setCountryCopyInfo(CountryCopyInfo countryCopyInfo) {
		this.countryCopyInfo = countryCopyInfo;
	}

	public long getLastPickUpTime() {
		return lastPickUpTime;
	}

	public void setLastPickUpTime(long lastPickUpTime) {
		this.lastPickUpTime = lastPickUpTime;
	}

	public long getTotalItemSizeByKey(String key) {
		return this.pack.getItemSizeByKey(key) + this.wareHouse.getItemSizeByKey(key)
				+ this.treasureWareHouse.getItemSizeByKey(key);
	}

	public boolean increaseWarn() {
		boolean block = heartBeatWarnTime.incrementAndGet() > 3;
		if (block) {
			heartBeatWarnTime.set(0);
		}
		return block;
	}

	public FashionPool getFashionPool() {
		return fashionPool;
	}

	public void setFashionPool(FashionPool fashionPool) {
		this.fashionPool = fashionPool;
	}

	public MergeActive getMergeActive() {
		return mergeActive;
	}

	public void setMergeActive(MergeActive mergeActive) {
		this.mergeActive = mergeActive;
	}

	public BlackShop getBlackShop() {
		return blackShop;
	}

	public void setBlackShop(BlackShop blackShop) {
		this.blackShop = blackShop;
	}

	public CommonActivityPool getCommonActivityPool() {
		return commonActivityPool;
	}

	public void setCommonActivityPool(CommonActivityPool commonActivityPool) {
		this.commonActivityPool = commonActivityPool;
	}

	public PlayerGasCopy getGasCopy() {
		return gasCopy;
	}

	public void setGasCopy(PlayerGasCopy gasCopy) {
		this.gasCopy = gasCopy;
	}

	public PlayerBossData getBossData() {
		return bossData;
	}

	public void setBossData(PlayerBossData bossData) {
		this.bossData = bossData;
	}

	public InvestAgatePool getInvestAgatePool() {
		return investAgatePool;
	}

	public void setInvestAgatePool(InvestAgatePool investAgatePool) {
		this.investAgatePool = investAgatePool;
	}

	public void changeSummon(String summonType, Summon summon) {
		synchronized (getSummons()) {
			Summon old = getSummons().get(summonType);
			if (old != null) {
				old.getController().delete();
			}
			getSummons().put(summonType, summon);
		}
	}

	public void clearSummon(String summonType) {
		synchronized (getSummons()) {
			Summon old = getSummons().get(summonType);
			if (old != null) {
				// old.getAi().handleEvent(Event.DELETE);
				old.getController().delete();
			}
			getSummons().remove(summonType);
		}
	}

	public boolean isHavingTargetSummon(Summon summon) {
		synchronized (getSummons()) {
			return getSummons().containsValue(summon);
		}
	}

	// public void clearAllSummon() {
	// synchronized (getSummons()) {
	// for (Summon summon : summons.values()) {
	// summon.getAi().handleEvent(Event.DELETE);
	// if (summon instanceof Servant) {
	// getBeautyGirlPool().die();
	// }
	// }
	// summons.clear();
	// }
	// }

	public Map<String, Summon> getSummons() {
		return summons;
	}

	public void setSummons(Map<String, Summon> summons) {
		this.summons = summons;
	}

	public BeautyGirlPool getBeautyGirlPool() {
		return beautyGirlPool;
	}

	public void setBeautyGirlPool(BeautyGirlPool beautyGirlPool) {
		this.beautyGirlPool = beautyGirlPool;
	}

	public Warbook getWarBook() {
		return warBook;
	}

	public void setWarBook(Warbook warBook) {
		this.warBook = warBook;
	}

	public EquipmentStorage getHorseEquipmentStorage() {
		return horseEquipmentStorage;
	}

	public void setHorseEquipmentStorage(EquipmentStorage horseEquipmentStorage) {
		this.horseEquipmentStorage = horseEquipmentStorage;
	}

	public TransferInfo getTransferInfo() {
		return transferInfo;
	}

	public void setTransferInfo(TransferInfo transferInfo) {
		this.transferInfo = transferInfo;
	}

	public LifeGridPool getLifeGridPool() {
		return lifeGridPool;
	}

	public void setLifeGridPool(LifeGridPool lifeGridPool) {
		this.lifeGridPool = lifeGridPool;
	}

	public Suicide getSuicide() {
		return suicide;
	}

	public void setSuicide(Suicide suicide) {
		this.suicide = suicide;
	}

	public Seal getSeal() {
		return seal;
	}

	public void setSeal(Seal seal) {
		this.seal = seal;
	}

}
