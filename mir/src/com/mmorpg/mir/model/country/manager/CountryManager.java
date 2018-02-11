package com.mmorpg.mir.model.country.manager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.ClearAndMigrate;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.boss.config.BossConfig;
import com.mmorpg.mir.model.capturetown.config.TownConfig;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActionType;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.action.CurrencyAction;
import com.mmorpg.mir.model.core.action.ItemAction;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.core.condition.country.CountryCalltogeterCountCondition;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.country.controllers.CountryArmyFlagController;
import com.mmorpg.mir.model.country.entity.CountryEnt;
import com.mmorpg.mir.model.country.model.AuthorityID;
import com.mmorpg.mir.model.country.model.CoppersType;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.CountryFlag;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.country.model.CountryOfficial;
import com.mmorpg.mir.model.country.model.Diplomacy;
import com.mmorpg.mir.model.country.model.ForbidChat;
import com.mmorpg.mir.model.country.model.NewTechnology;
import com.mmorpg.mir.model.country.model.Official;
import com.mmorpg.mir.model.country.model.ReserveKing;
import com.mmorpg.mir.model.country.model.ReserveTaskEnum;
import com.mmorpg.mir.model.country.model.Tank;
import com.mmorpg.mir.model.country.model.TraitorPlayerFix;
import com.mmorpg.mir.model.country.model.countryact.CountryFlagQuestType;
import com.mmorpg.mir.model.country.model.countryact.HiddenMissionType;
import com.mmorpg.mir.model.country.model.log.CountryLogEnum;
import com.mmorpg.mir.model.country.model.log.CountryLogger;
import com.mmorpg.mir.model.country.model.vo.CountryLoggerVO;
import com.mmorpg.mir.model.country.model.vo.KingGuradVO;
import com.mmorpg.mir.model.country.model.vo.TraitorVO;
import com.mmorpg.mir.model.country.packet.SM_CountryFlag_Damage_Rank;
import com.mmorpg.mir.model.country.packet.SM_Country_Action_See_All_Log;
import com.mmorpg.mir.model.country.packet.SM_Country_Appoint;
import com.mmorpg.mir.model.country.packet.SM_Country_CallTogether;
import com.mmorpg.mir.model.country.packet.SM_Country_CallTogether_Guard;
import com.mmorpg.mir.model.country.packet.SM_Country_CallbackTank;
import com.mmorpg.mir.model.country.packet.SM_Country_Check_Salary_Status;
import com.mmorpg.mir.model.country.packet.SM_Country_Contribute_Shop;
import com.mmorpg.mir.model.country.packet.SM_Country_Contribution;
import com.mmorpg.mir.model.country.packet.SM_Country_Coppers;
import com.mmorpg.mir.model.country.packet.SM_Country_Depose;
import com.mmorpg.mir.model.country.packet.SM_Country_DistributeTank;
import com.mmorpg.mir.model.country.packet.SM_Country_Door;
import com.mmorpg.mir.model.country.packet.SM_Country_Factory;
import com.mmorpg.mir.model.country.packet.SM_Country_Fete;
import com.mmorpg.mir.model.country.packet.SM_Country_Fete_Log;
import com.mmorpg.mir.model.country.packet.SM_Country_Flag;
import com.mmorpg.mir.model.country.packet.SM_Country_ForbidChat;
import com.mmorpg.mir.model.country.packet.SM_Country_Offical;
import com.mmorpg.mir.model.country.packet.SM_Country_Open_Diplomacy;
import com.mmorpg.mir.model.country.packet.SM_Country_Open_Flag;
import com.mmorpg.mir.model.country.packet.SM_Country_ReceivedCivilSalary;
import com.mmorpg.mir.model.country.packet.SM_Country_ReceivedOfficialSalary;
import com.mmorpg.mir.model.country.packet.SM_Country_SetNotice;
import com.mmorpg.mir.model.country.packet.SM_Country_Shop;
import com.mmorpg.mir.model.country.packet.SM_Country_Storage;
import com.mmorpg.mir.model.country.packet.SM_Country_Store;
import com.mmorpg.mir.model.country.packet.SM_Country_Technology_Flag_Change;
import com.mmorpg.mir.model.country.packet.SM_Country_Technology_PlaceFlag;
import com.mmorpg.mir.model.country.packet.SM_Country_TogetherToken;
import com.mmorpg.mir.model.country.packet.SM_Country_Traitor_Num;
import com.mmorpg.mir.model.country.packet.SM_Country_Traitor_Rank;
import com.mmorpg.mir.model.country.packet.SM_Country_UpgradeShop;
import com.mmorpg.mir.model.country.packet.SM_Diplomacy_Damage_Rank;
import com.mmorpg.mir.model.country.packet.SM_Flag_Quest_Info;
import com.mmorpg.mir.model.country.packet.SM_Hidden_Mission_Info;
import com.mmorpg.mir.model.country.packet.SM_Official_Mobilization;
import com.mmorpg.mir.model.country.packet.SM_Player_Official_Change;
import com.mmorpg.mir.model.country.packet.SM_Query_Mobilization;
import com.mmorpg.mir.model.country.packet.SM_ReserveKingVO;
import com.mmorpg.mir.model.country.packet.SM_ReserveKing_CallTogether;
import com.mmorpg.mir.model.country.packet.vo.SalaryStatusVO;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.country.resource.CountryAuthorityResource;
import com.mmorpg.mir.model.country.resource.CountryBuildValueFixResource;
import com.mmorpg.mir.model.country.resource.CountryDoorResource;
import com.mmorpg.mir.model.country.resource.CountryFactoryResource;
import com.mmorpg.mir.model.country.resource.CountryFlagResource;
import com.mmorpg.mir.model.country.resource.CountryShopResource;
import com.mmorpg.mir.model.country.resource.CountryTankResource;
import com.mmorpg.mir.model.dirtywords.model.WordsType;
import com.mmorpg.mir.model.dirtywords.service.DirtyWordsManager;
import com.mmorpg.mir.model.gameobjects.CountryNpc;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.RequestHandlerType;
import com.mmorpg.mir.model.gameobjects.RequestResponseHandler;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.item.AbstractItem;
import com.mmorpg.mir.model.item.storage.ItemStorage;
import com.mmorpg.mir.model.kingofwar.config.KingOfWarConfig;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.serverstate.FlagSpecifiedSatatus;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.effecttemplate.ForbidChatEffect;
import com.mmorpg.mir.model.skill.effecttemplate.TraitorEffect;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.system.packet.SM_System_Sign;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.welfare.event.CountryBuyEvent;
import com.mmorpg.mir.model.welfare.event.CountrySacrificeEvent;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.resource.MapResource;
import com.mmorpg.mir.utils.CharCheckUtil;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.orm.Querier;
import com.windforce.common.ramcache.service.EntityBuilder;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.scheduler.ScheduledTask;
import com.windforce.common.scheduler.impl.SimpleScheduler;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;
import com.windforce.common.utility.RandomUtils;

@Component
public class CountryManager implements ICountryManager {

	private static CountryManager instance;
	@Static
	private Storage<String, CountryAuthorityResource> authorityResource;
	private Map<CountryId, Country> countries = new HashMap<CountryId, Country>();
	@Inject
	private EntityCacheService<String, CountryEnt> countryEntDbService;
	@Autowired
	private CoreActionManager actionManager;
	@Autowired
	private RewardManager rewardManager;
	@Autowired
	private ChooserManager chooserManager;
	@Autowired
	private ConfigValueManager configValueManager;
	@Static
	private Storage<String, CountryShopResource> countryShopResources;
	@Static
	private Storage<String, CountryDoorResource> countryDoorResources;
	@Static
	private Storage<String, CountryFlagResource> countryFlagResources;
	@Static
	private Storage<String, CountryFactoryResource> countryFactoryResources;
	@Static
	private Storage<String, CountryTankResource> countryTankResources;
	@Autowired
	private CoreConditionManager conditionManager;
	@Autowired
	private SpawnManager spawnManager;
	@Autowired
	private Querier querier;
	@Autowired
	private SimpleScheduler simpleScheduler;
	@Autowired
	private ServerState serverState;

	/** 抛出禁言次数不足错误码,如果code不是禁言,则抛出权限使用次数不足错误码 */
	public boolean throwManagedException(String code) {
		String forbidErrorCode = configValueManager.COUNTRY_FIRBIDCHAT_ERRORCODE.getValue().trim();
		if (forbidErrorCode.equals(code)) {
			throw new ManagedException(ManagedErrorCode.COUNTRY_FORBID_NUM_NOT_ENGOUTH);
		}
		throw new ManagedException(ManagedErrorCode.COUNTRY_TODAY_NOT_AUTHORITY);
	}

	@PostConstruct
	public void init() {
		setInstance(this);
	}

	// @PostConstruct
	public void initAll() {
		List<CountryEnt> countryEnts = querier.all(CountryEnt.class);
		if (countryEnts.isEmpty()) {
			// 新服
			for (CountryId id : CountryId.values()) {
				final Country country = new Country();
				country.setId(id);
				country.init();
				CountryEnt ent = countryEntDbService.loadOrCreate(id.name(), new EntityBuilder<String, CountryEnt>() {
					@Override
					public CountryEnt newInstance(String id) {
						CountryEnt ent = new CountryEnt();
						ent.setId(id);
						ent.setCountry(country);
						ent.setCountryJson(JsonUtils.object2String(country));
						return ent;
					}
				});
				country.setEnt(ent);
				getCountries().put(id, country);
			}
		} else {
			for (CountryEnt ent : countryEnts) {
				Country country = ent.createCountry();
				country.setEnt(ent);
				if (country.getNewTechnology() == null) {
					country.setNewTechnology(NewTechnology.valueOf(ConfigValueManager.getInstance().COUNTRY_TECHNOLOGY_FLAG_INIT_COUNT
							.getValue()));
				}
				getCountries().put(country.getId(), country);
			}
		}
		for (Country country : getCountries().values()) {
			country.getCourt().setCountry(country);
			country.getTechnology().setCountry(country);
			country.getTemple().setCountry(country);
			if (!ClearAndMigrate.clear) {
				// 砍大臣和国旗初始化
				country.getTemple().initStatusNpc(spawnManager, configValueManager);
				country.getCountryFlag().initFlag(country);
				country.getDiplomacy().initCountryBattleNpc(country);
			}
			country.getCountryQuest().setCountry(country);
			country.getNewTechnology().setCountry(country);
		}

		if (ClearAndMigrate.clear) {
			return;
		}

		// 刷新军旗数量
		refreshTechnologyFlagCount(false);

		// 刷新商店
		refreshCountryShop();
		// 检查俸禄发放
		refreshSalary();
		//
		refreshCountryQuest();

		// 开服了，但是还没到X小时 生成墓碑 并起一个生成的任务
		startDiplomacy();
		startCountryFlag();
		countryBuildValueFix();
		String[] dipRefreshTimes = ConfigValueManager.getInstance().COUNTRY_DIPLOMACY_REFRESH_TIME_CRONS.getValue();
		for (String dipRefreshTime : dipRefreshTimes) {
			simpleScheduler.schedule(new ScheduledTask() {

				@Override
				public void run() {
					if (!serverState.isOpenServer()) {
						return;
					}
					if (System.currentTimeMillis() >= getOpenServerDiplomacySpawnTime(serverState.getOpenServerDate())) {
						for (Country country : countries.values()) {
							country.getDiplomacy().spawnDiplomacyNpc();
						}
					}

				}

				@Override
				public String getName() {
					return "大臣刷新";
				}
			}, dipRefreshTime);
		}

		for (String flagRefreshTimes : configValueManager.COUNTRYFLAG_SPAWN_CRON.getValue()) {
			simpleScheduler.schedule(new ScheduledTask() {

				@Override
				public void run() {
					if (!serverState.isOpenServer()) {
						return;
					}
					if (System.currentTimeMillis() >= getOpenServerFlagSpawnTime(serverState.getOpenServerDate())) {
						initCountryFlagQuest();
					}
				}

				@Override
				public String getName() {
					return "国旗刷新,国旗任务重置";
				}
			}, flagRefreshTimes);
		}

		for (String flagNoticeTimes : configValueManager.COUNTRYFLAG_BEFORE_SPAWN_CRON.getValue()) {
			simpleScheduler.schedule(new ScheduledTask() {

				@Override
				public void run() {
					if (!serverState.isOpenServer()) {
						return;
					}
					if (serverState.getOpenServerDate() != null
							&& (System.currentTimeMillis() - 5 * DateUtils.MILLIS_PER_MINUTE) >= getOpenServerFlagSpawnTime(serverState
									.getOpenServerDate())) {
						I18nUtils utils = I18nUtils.valueOf("601007");
						ChatManager.getInstance().sendSystem(6100190, utils, null);
					}
				}

				@Override
				public String getName() {
					return "国旗刷新提前5分钟的公告提示";
				}
			}, flagNoticeTimes);
		}

		// 修复201-12-23，9211数据库崩溃玩家丢失遭成的数据不完整
		for (Country country : countries.values()) {
			country.getCourt().fix();
		}

	}

	public void update(Country country) {
		countryEntDbService.writeBack(country.getId().name(), country.getEnt());
	}

	public static CountryAuthorityResource getAuthorityResource(String id) {
		return CountryManager.getInstance().authorityResource.get(id, true);
	}

	/** 任命 */
	public void appoint(Player player, Player target, String offical, int index) {
		try {
			player.getCountry().lockLock();

			if (!player.sameCountry(target)) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_NOT_HAVE_PEOPLE);
			}
			if (player.getCountry().isOffical(target)) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_OFFICAL_HAVED);
			}
			// 已经是卫队
			if (player.getCountry().getCourt().isGurad(target.getObjectId())) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_APPOINT_IS_GUARD);
			}

			CountryOfficial countryOfficial = CountryOfficial.typeOf(offical);
			for (Official former : player.getCountry().getOfficalType(countryOfficial)) {
				if (former.getIndex() == index) {
					String deposeAuthority = (AuthorityID.DEPOSE_BASE + countryOfficial.getValue()) + "";
					player.getCountry().authority(player, deposeAuthority);
					Player formerPlayer = PlayerManager.getInstance().getPlayer(former.getPlayerId());
					player.getCountry().getCourt().depose(formerPlayer);
					break;
				}
			}
			String authorityId = (AuthorityID.APPOINT_BASE + countryOfficial.getValue()) + "";
			player.getCountry().authority(player, authorityId);
			player.getCountry().appoint(target, CountryOfficial.typeOf(offical), index);
			player.addUseAuthorityHistory(authorityId);

			I18nUtils utils = I18nUtils.valueOf("10502");
			utils.addParm("name", I18nPack.valueOf(target.getName()));
			utils.addParm(
					"officialname",
					I18nPack.valueOf(ConfigValueManager.getInstance().OFFICIAL_NAME.getValue().get(
							countryOfficial.name())));
			ChatManager.getInstance().sendSystem(11003, utils, null, player.getCountry());

			I18nUtils chatUtils = I18nUtils.valueOf("302004", utils);
			ChatManager.getInstance().sendSystem(6, chatUtils, null, player.getCountry());

			// inform target and those who appear in his screen
			if (SessionManager.getInstance().isOnline(target.getObjectId())) {
				PacketSendUtility.broadcastPacket(target,
						SM_Player_Official_Change.valueOf(target.getObjectId(), countryOfficial.name()));
			}
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void forbidChat(Player player, Player target, int sign) {
		try {
			player.getCountry().lockLock();

			if (player.getObjectId() == target.getObjectId()) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_CANTNOT_FORBID_SELF);
			}
			if (!player.sameCountry(target)) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_NOT_HAVE_PEOPLE);
			}

			CountryOfficial myOfficial = player.getCountry().getCourt().getPlayerOfficial(player);
			CountryOfficial targetOfficial = player.getCountry().getCourt().getPlayerOfficial(target);

			if (myOfficial == null) {
				myOfficial = CountryOfficial.CITIZEN;
			}
			if (targetOfficial == null) {
				targetOfficial = CountryOfficial.CITIZEN;
			}

			if (configValueManager.getOfficialPower(myOfficial.name()) < configValueManager
					.getOfficialPower(targetOfficial.name())) {
				throw new ManagedException(ManagedErrorCode.OFFICIAL_CANNOT_FORBIT_HIGHER_OFFICIAL);
			}

			// TODO 御史大夫不能禁言,前端ui显示错误
			player.getCountry().authority(player, AuthorityID.FORBIDCHAT);
			if (player.getCountry().isForbidchat(target.getObjectId())) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COUNTRY_FORBIDCHATED);
				return;
			}

			Official official = player.getCountry().getCourt().getOfficials().get(player.getObjectId());
			int dailyAlreadyUse = player.getCountry().getCourt().getForbidChatCount(player.getObjectId());
			int dailyCountLimit = ConfigValueManager.getInstance().OFFICIAL_FORBID_DAILY_COUNT.getValue().get(
					official.getOfficial().name());
			if (dailyAlreadyUse >= dailyCountLimit) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COUNTRY_FORBID_COUNT_LIMIT);
				return;
			}

			player.getCountry().forbidChat(target.getObjectId(), configValueManager.COUNTRY_FORBIDCHAT_TIME.getValue());
			if (SessionManager.getInstance().isOnline(target.getObjectId())) {
				ForbidChat chat = player.getCountry().getForbidChats().get(target.getObjectId());
				PacketSendUtility.sendPacket(target, SM_Country_ForbidChat.valueOf(chat.getEndTime()));
			}
			PacketSendUtility.sendSignMessage(player, sign);
			player.addUseAuthorityHistory(AuthorityID.FORBIDCHAT);

			CountryOfficial countryOfficial = player.getCountry().getCourt().getOfficials().get(player.getObjectId())
					.getOfficial();
			I18nUtils utils = I18nUtils.valueOf("10504");
			utils.addParm("targetName", I18nPack.valueOf(target.getName()));
			utils.addParm(
					"officialname",
					I18nPack.valueOf(ConfigValueManager.getInstance().OFFICIAL_NAME.getValue().get(
							countryOfficial.name())));
			utils.addParm("name", I18nPack.valueOf(player.getName()));
			ChatManager.getInstance().sendSystem(11003, utils, null, player.getCountry());
			if (!target.getEffectController().contains(ForbidChatEffect.FORBIDCHAT)) {
				Skill skill = SkillEngine.getInstance().getSkill(null, 20069, target.getObjectId(), 0, 0, target, null);
				skill.noEffectorUseSkill();
			}
			player.getCountry().getCourt().addForbidChatCount(player.getObjectId());
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void startExpress(final Player player, int sign) {
		try {
			player.getCountry().lockLock();
			player.getCountry().authority(player, AuthorityID.START_COUNTRY_EXPESS);
			if (player.getCountry().getCountryQuest().getTodayExpressLeftCount() <= 0) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_QUEST_STARTED);
			}
			if (!conditionManager.getCoreConditions(1, configValueManager.QUEST_EXPRESS_TIME_CONDITIONS.getValue())
					.verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}
			long lastStartExpressTime = player.getCountry().getCountryQuest().getExpressEndTime()
					- configValueManager.QUEST_EXPRESS_TIME.getValue() * DateUtils.MILLIS_PER_SECOND;
			if (System.currentTimeMillis() - lastStartExpressTime < configValueManager.COUNTRY_TECH_ACT_CD.getValue()
					* DateUtils.MILLIS_PER_SECOND) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}

			player.getCountry().getCountryQuest().startExpress();
			// 通知
			// I18nUtils i18nUtils = I18nUtils.valueOf("405001");
			// i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
			// i18nUtils.addParm("country",
			// I18nPack.valueOf(player.getCountry().getName()));
			// ChatManager.getInstance().sendSystem(71001, i18nUtils, null);

			// 通知
			// I18nUtils i18nUtils1 = I18nUtils.valueOf("303001");
			// i18nUtils1.addParm("name", I18nPack.valueOf(player.getName()));
			// i18nUtils1.addParm("country",
			// I18nPack.valueOf(player.getCountry().getName()));
			// i18nUtils1.addParm(I18NparamKey.MAPID,
			// I18nPack.valueOf(ConfigValueManager.getInstance().KING_CITY_MAPID
			// .getValue()[player.getCountryValue() - 1]));
			// ChatManager.getInstance().sendSystem(6, i18nUtils1, null,
			// player.getCountry());

			PacketSendUtility.sendSignMessage(player, sign);
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void startTemple(final Player player, int sign) {
		try {
			player.getCountry().lockLock();
			player.getCountry().authority(player, AuthorityID.START_COUNTRY_TEMPLE);
			if (player.getCountry().getCountryQuest().getTodayTempleLeftCount() <= 0) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_QUEST_STARTED);
			}
			if (!conditionManager.getCoreConditions(1, configValueManager.QUEST_TEMPLE_TIME_CONDITIONS.getValue())
					.verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}
			long lastStartTempleTime = player.getCountry().getCountryQuest().getTempleEndTime()
					- configValueManager.QUEST_TEMPLE_TIME.getValue() * DateUtils.MILLIS_PER_SECOND;
			if (System.currentTimeMillis() - lastStartTempleTime < configValueManager.COUNTRY_TECH_ACT_CD.getValue()
					* DateUtils.MILLIS_PER_SECOND) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}
			player.getCountry().getCountryQuest().startTemple();
			// 通知
			// I18nUtils i18nUtils = I18nUtils.valueOf("405002");
			// i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
			// i18nUtils.addParm("country",
			// I18nPack.valueOf(player.getCountry().getName()));
			// ChatManager.getInstance().sendSystem(71001, i18nUtils, null);

			// 通知
			// I18nUtils i18nUtils1 = I18nUtils.valueOf("303002");
			// i18nUtils1.addParm("name", I18nPack.valueOf(player.getName()));
			// i18nUtils1.addParm("country",
			// I18nPack.valueOf(player.getCountry().getName()));
			// i18nUtils1.addParm(I18NparamKey.MAPID,
			// I18nPack.valueOf(ConfigValueManager.getInstance().KING_CITY_MAPID
			// .getValue()[player.getCountryValue() - 1]));
			// ChatManager.getInstance().sendSystem(6, i18nUtils1, null,
			// player.getCountry());

			PacketSendUtility.sendSignMessage(player, sign);
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void distributeTank(Player player, Player target, int tankId, int sign) {
		try {
			player.getCountry().lockLock();
			if (!player.sameCountry(target)) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_NOT_HAVE_PEOPLE);
			}
			player.getCountry().authority(player, AuthorityID.DISTRIBUTE_TANK);
			Tank tank = player.getCountry().getTechnology().getArmsFactory()
					.distributeTank(tankId, target.getObjectId());
			PacketSendUtility.sendPacket(target, SM_Country_DistributeTank.valueOf(tank));
			PacketSendUtility.sendPacket(player, SM_Country_DistributeTank.valueOf(tank));
			PacketSendUtility.sendSignMessage(player, sign);
			player.addUseAuthorityHistory(AuthorityID.DISTRIBUTE_TANK);
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void callbackTank(Player player, int tankId, int sign) {
		try {
			player.getCountry().lockLock();
			player.getCountry().authority(player, AuthorityID.CALLBACK_TANK);
			long ownerId = player.getCountry().getTechnology().getArmsFactory().callbackTank(tankId);
			if (SessionManager.getInstance().isOnline(ownerId)) {
				Player owner = PlayerManager.getInstance().getPlayer(ownerId);
				PacketSendUtility.sendPacket(owner, SM_Country_CallbackTank.valueOf(tankId));
				PacketSendUtility.sendPacket(player, SM_Country_CallbackTank.valueOf(tankId));
				if (owner.getEffectController().contains("TANK")) {
					owner.getEffectController().clearEffect(owner.getEffectController().getAnormalEffect("TANK"));
				}
			}
			PacketSendUtility.sendSignMessage(player, sign);
			player.addUseAuthorityHistory(AuthorityID.CALLBACK_TANK);
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void openCivilSalary(Player player, int sign) {
		try {
			player.getCountry().lockLock();
			player.getCountry().authority(player, AuthorityID.OPEN_CIVILSALARY);

			if (player.getCountry().getCourt().isCivilSalary()) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COUNTRY_SALARYED);
				return;
			}

			CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1,
					ConfigValueManager.getInstance().COUNTRY_OPEN_CIVIL_SALARY_CONDITION.getValue());
			if (!conditions.verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}

			player.getCountry().getCourt().openCivilSalary();
			PacketSendUtility.sendSignMessage(player, sign);

			CoreConditions filterCond = CoreConditionManager.getInstance().getCoreConditions(1,
					ConfigValueManager.getInstance().COUNTRY_RECEIVE_SALARY.getValue());
			// 公告
			I18nUtils openUtils = I18nUtils.valueOf("301017");
			String x = ConfigValueManager.getInstance().CIVIL_NPC_LOACTION.getValue()
					.get(player.getCountryValue() + "").get("x");
			String y = ConfigValueManager.getInstance().CIVIL_NPC_LOACTION.getValue()
					.get(player.getCountryValue() + "").get("y");
			openUtils
					.addParm(
							I18NparamKey.MAPID,
							I18nPack.valueOf(ConfigValueManager.getInstance().KING_CITY_MAPID.getValue()[player
									.getCountryValue() - 1])).addParm("x", I18nPack.valueOf(x))
					.addParm("y", I18nPack.valueOf(y));
			ChatManager.getInstance().sendSystem(Country.channalId, openUtils, null, player.getCountry(), filterCond);
			// tv
			String officialName = ConfigValueManager.getInstance().OFFICIAL_NAME.getValue().get(
					player.getCountry().getPlayerOffical(player.getObjectId()).getOfficial().name());
			I18nUtils utils = I18nUtils.valueOf("10305")
					.addParm(I18NparamKey.OFFICENAME, I18nPack.valueOf(officialName))
					.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
			ChatManager.getInstance().sendSystem(11003, utils, null, player.getCountry(), filterCond);
			// 邮件
			/*
			 * I18nUtils titel18n =
			 * I18nUtils.valueOf("country_national_salary_title"); I18nUtils
			 * contextl18n = I18nUtils.valueOf("country_national_salary");
			 * contextl18n.addParm(I18NparamKey.MAPID,
			 * I18nPack.valueOf(ConfigValueManager.getInstance().KING_CITY_MAPID
			 * .getValue()[player.getCountryValue() - 1]));
			 * contextl18n.addParm("x", I18nPack.valueOf(x));
			 * contextl18n.addParm("y", I18nPack.valueOf(y));
			 * contextl18n.addParm("country",
			 * I18nPack.valueOf(player.getCountry().getName()));
			 * CoreConditionResource[] conditionResources =
			 * CoreConditionManager.getInstance().getCoreConditionResources(
			 * ConfigValueManager
			 * .getInstance().COUNTRY_RECEIVE_SALARY.getValue());
			 * conditionResources = (CoreConditionResource[]) ArrayUtils.add(
			 * conditionResources,
			 * CoreConditionResource.createCondition(CoreConditionType
			 * .COUNTRY_COND, null, player.getCountryValue())); MailGroup
			 * mailGroup = MailGroup.valueOf(titel18n, contextl18n, null,
			 * conditionResources, null);
			 * MailManager.getInstance().addMailGroup(mailGroup);
			 */
			player.addUseAuthorityHistory(AuthorityID.OPEN_CIVILSALARY);
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void checkSalaryStatus(Player player) {
		PacketSendUtility.sendPacket(player, SM_Country_Check_Salary_Status.valueOf(player));
	}

	public void recevieCivilSalary(Player player, int sign) {
		try {
			player.getCountry().lockLock();
			CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1,
					ConfigValueManager.getInstance().COUNTRY_RECEIVE_SALARY.getValue());
			if (!conditions.verify(player, true)) {
				PacketSendUtility.sendErrorMessage(player);
				return;
			}
			if (!player.getCountry().getCourt().isCivilSalary()) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COUNTRY_NOT_SALARYED);
				return;
			}
			if (player.getCountry().getCourt().getLastOpenCivilSalary() != 0L
					&& (!DateUtils.isToday(new Date(player.getCountry().getCourt().getLastOpenCivilSalary())))) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COUNTRY_NOT_SALARYED);
				return;
			}
			if (player.getCountry().getCourt().getCivilReceived().contains(player.getObjectId())) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COUNTRY_RECEVIED);
				return;
			}
			List<String> rewardIds = chooserManager.chooseValueByRequire(player,
					configValueManager.COUNTRY_CIVIL_SALARY_CHOOSERGROUP.getValue());
			rewardManager.grantReward(player, rewardIds,
					ModuleInfo.valueOf(ModuleType.COUNTRY, SubModuleType.COUNTRY_SALARY));
			player.getCountry().getCourt().receiveCivilSalary(player.getObjectId());

			PacketSendUtility.sendSignMessage(player, sign);
			PacketSendUtility.sendPacket(
					player,
					SM_Country_ReceivedCivilSalary.valueOf(SalaryStatusVO.valueOf(0, player.getCountry().getCourt()
							.isCivilSalary(),
							player.getCountry().getCourt().getCivilReceived().contains(player.getObjectId()))));
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void recevieOfficialSalary(Player player, int sign) {
		try {
			player.getCountry().lockLock();
			CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1,
					ConfigValueManager.getInstance().COUNTRY_RECEIVE_OFFICIAL_SALARY.getValue());
			if (!conditions.verify(player, true)) {// 已经跑出异常
				PacketSendUtility.sendErrorMessage(player);
				return;
			}
			if (!player.getCountry().getCourt().isOfficialSalary()) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COUNTRY_GET_OFFICIAL_ERROR);
				return;
			}
			if (player.getCountry().getCourt().getOfficialReceived().contains(player.getObjectId())) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COUNTRY_RECEVIED);
				return;
			}
			if (!player.getCountry().isOffical(player) && !player.getCountry().getCourt().isGurad(player.getObjectId())) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_AUTHORITY_ERROR);
			}
			if (!player.getCountry().getCourt().getOfficialSalaryRecord().contains(player.getObjectId())) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.NO_SALARY_RECORD);
				return;
			}
			List<String> rewardIds = chooserManager.chooseValueByRequire(player,
					configValueManager.COUNTRY_OFFICAL_SALARY_CHOOSERGROUP.getValue());

			rewardManager.grantReward(player, rewardIds,
					ModuleInfo.valueOf(ModuleType.COUNTRY, SubModuleType.COUNTRY_OFFICIAL_SALARY));
			player.getCountry().getCourt().receiveOfficialSalary(player.getObjectId());

			PacketSendUtility.sendSignMessage(player, sign);
			PacketSendUtility.sendPacket(
					player,
					SM_Country_ReceivedOfficialSalary.valueOf(SalaryStatusVO.valueOf(1, player.getCountry().getCourt()
							.isOfficialSalary(),
							player.getCountry().getCourt().getOfficialReceived().contains(player.getObjectId()))));
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void openOfficialSalary(Player player, int sign) {
		try {
			player.getCountry().lockLock();
			player.getCountry().authority(player, AuthorityID.OPEN_OFFICIALSALARY);

			if (player.getCountry().getCourt().isOfficialSalary()) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COUNTRY_SALARYED);
				return;
			}

			CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1,
					ConfigValueManager.getInstance().COUNTRY_OPEN_OFFICIAL_SALARY_CONDITION.getValue());
			if (!conditions.verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}

			player.getCountry().getCourt().openOfficialSalary();

			PacketSendUtility.sendSignMessage(player, sign);
			player.addUseAuthorityHistory(AuthorityID.OPEN_OFFICIALSALARY);

			CoreConditions filterCond = CoreConditionManager.getInstance().getCoreConditions(1,
					ConfigValueManager.getInstance().COUNTRY_RECEIVE_OFFICIAL_SALARY.getValue());
			CoreConditionResource ccr = CoreConditionResource.createCondition(CoreConditionType.PLAYER_ID,
					JsonUtils.object2String(player.getCountry().getCourt().getOfficialSalaryRecord()), 0);
			CoreConditions ccrs = CoreConditionManager.getInstance().getCoreConditions(1, ccr);
			for (AbstractCoreCondition cond : ccrs.getConditionList()) {
				filterCond.addCondition(cond);
			}

			// 公告
			I18nUtils openUtils = I18nUtils.valueOf("301018");
			String x = ConfigValueManager.getInstance().OFFICIAL_NPC_LOACTION.getValue()
					.get(player.getCountryValue() + "").get("x");
			String y = ConfigValueManager.getInstance().OFFICIAL_NPC_LOACTION.getValue()
					.get(player.getCountryValue() + "").get("y");
			openUtils.addParm(I18NparamKey.MAPID, I18nPack.valueOf(ConfigValueManager.getInstance().KING_CITY_MAPID
					.getValue()[player.getCountryValue() - 1]));
			openUtils.addParm("x", I18nPack.valueOf(x));
			openUtils.addParm("y", I18nPack.valueOf(y));
			ChatManager.getInstance().sendSystem(Country.channalId, openUtils, null, player.getCountry(), filterCond);
			// tv
			String officialName = ConfigValueManager.getInstance().OFFICIAL_NAME.getValue().get(
					player.getCountry().getPlayerOffical(player.getObjectId()).getOfficial().name());
			I18nUtils utils = I18nUtils.valueOf("10306")
					.addParm(I18NparamKey.OFFICENAME, I18nPack.valueOf(officialName))
					.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
			ChatManager.getInstance().sendSystem(11003, utils, null, player.getCountry(), filterCond);
			// 邮件
			/*
			 * I18nUtils titel18n =
			 * I18nUtils.valueOf("country_officer_salary_title"); I18nUtils
			 * contextl18n = I18nUtils.valueOf("country_officer_salary");
			 * contextl18n.addParm(I18NparamKey.MAPID,
			 * I18nPack.valueOf(ConfigValueManager.getInstance().KING_CITY_MAPID
			 * .getValue()[player.getCountryValue() - 1]));
			 * contextl18n.addParm("x", I18nPack.valueOf(x));
			 * contextl18n.addParm("y", I18nPack.valueOf(y));
			 * contextl18n.addParm("country",
			 * I18nPack.valueOf(player.getCountry().getName())); MailGroup
			 * mailGroup = MailGroup.valueOf(titel18n, contextl18n, null, new
			 * CoreConditionResource[] { ccr }, null);
			 * mailGroup.setEndTime(System.currentTimeMillis() +
			 * DateUtils.MILLIS_PER_DAY * 7);
			 * MailManager.getInstance().addMailGroup(mailGroup);
			 */
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void appointGurad(Player player, Player target, int index) {
		if (index < 0 || index > 4) { // 和前端约定的，都是写在代码里，卫队的位置是0-4
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		try {
			player.getCountry().lockLock();
			if (!player.sameCountry(target)) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_NOT_HAVE_PEOPLE);
			}
			if (player.getCountry().getCourt().isGurad(target.getObjectId())) {
				throw new ManagedException(ManagedErrorCode.PLAYER_GUARDED);
			}
			if (player.getCountry().getCourt().isOfficial(target.getObjectId())) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_APPOINT_IS_OFFICIAL);
			}
			player.getCountry().authority(player, AuthorityID.APPOINT_GUARD);

			CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1,
					ConfigValueManager.getInstance().COUNTRY_APPOINT_GUARD.getValue());

			if (!conditions.verify(target, true)) {
				PacketSendUtility.sendErrorMessage(player);
				return;
			}

			if (player.getCountry().getCourt().containsIndexGuard(index)) {
				long pid = player.getCountry().getCourt().getIndexGuardsPlayerId(index);
				Player former = PlayerManager.getInstance().getPlayer(pid);
				player.getCountry().authority(player, AuthorityID.DESPOSE_GUARD);
				player.getCountry().getCourt().deposeGurads(former.getObjectId());
				PacketSendUtility.sendPacket(player,
						SM_Country_Depose.valueOf(former, KingGuradVO.GUARD_OFFICIAL, index));
				player.addUseAuthorityHistory(AuthorityID.DESPOSE_GUARD);
				player.getCountry().getCourt().depose(former);
				player.getCountry().getCourt().deposeNotice(former, CountryOfficial.GUARD);
			}

			player.getCountry().getCourt().setGurads(target.getObjectId(), index);
			player.getCountry().getCourt().appoint(target, CountryOfficial.GUARD, index);
			PacketSendUtility.sendPacket(player, SM_Country_Appoint.valueOf(target, KingGuradVO.GUARD_OFFICIAL, index));
			player.addUseAuthorityHistory(AuthorityID.APPOINT_GUARD);

			CountryOfficial countryOfficial = CountryOfficial.GUARD;
			I18nUtils utils = I18nUtils.valueOf("10502");
			utils.addParm("name", I18nPack.valueOf(target.getName()));
			utils.addParm(
					"officialname",
					I18nPack.valueOf(ConfigValueManager.getInstance().OFFICIAL_NAME.getValue().get(
							countryOfficial.name())));
			ChatManager.getInstance().sendSystem(11003, utils, null, player.getCountry());

			I18nUtils chatUtils = I18nUtils.valueOf("302004", utils);
			ChatManager.getInstance().sendSystem(6, chatUtils, null, player.getCountry());
			// inform target and those who appear in his screen
			if (SessionManager.getInstance().isOnline(target.getObjectId())) {
				PacketSendUtility.broadcastPacket(target,
						SM_Player_Official_Change.valueOf(target.getObjectId(), countryOfficial.name()));
			}
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void deposeGurad(Player player, Player target) {
		try {
			player.getCountry().lockLock();
			if (!player.sameCountry(target)) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_NOT_HAVE_PEOPLE);
			}
			if (!player.getCountry().getCourt().isGurad(target.getObjectId())) {
				PacketSendUtility.sendPacket(player, ManagedErrorCode.PLAYER_GUARDED);
				return;
			}
			player.getCountry().authority(player, AuthorityID.DESPOSE_GUARD);

			int index = player.getCountry().getCourt().deposeGurads(target.getObjectId());
			PacketSendUtility.sendPacket(player, SM_Country_Depose.valueOf(target, KingGuradVO.GUARD_OFFICIAL, index));
			player.addUseAuthorityHistory(AuthorityID.DESPOSE_GUARD);
			player.getCountry().getCourt().depose(target);
			player.getCountry().getCourt().deposeNotice(target, CountryOfficial.GUARD);
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void depose(Player player, Player target) {
		try {
			player.getCountry().lockLock();
			if (!player.sameCountry(target)) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_NOT_HAVE_PEOPLE);
			}
			if (!player.getCountry().isOffical(target) || player == target) {
				PacketSendUtility.sendErrorMessage(player);
				return;
			}
			CountryOfficial countryOfficial = target.getCountry().getPlayerOffical(target.getObjectId()).getOfficial();
			String authorityId = (AuthorityID.DEPOSE_BASE + countryOfficial.getValue()) + "";
			player.getCountry().authority(player, authorityId);
			player.getCountry().getCourt().depose(target);
			player.addUseAuthorityHistory(authorityId);
			player.getCountry().getCourt().deposeNotice(target, countryOfficial);
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void setTraitor(Player player, Player target, int sign) {
		try {
			player.getCountry().lockLock();
			if (player.getObjectId().equals(target.getObjectId())) {
				throw new ManagedException(ManagedErrorCode.CANNOT_SET_SELF_TRAITOR);
			}

			if (player.getCountry().getTraitorMapFixs().size() >= ConfigValueManager.getInstance().TRAITOR_MAX_LIMIT
					.getValue()) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_TRAITOR_TOO_MUCH);
			}

			if (!player.sameCountry(target)) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_NOT_HAVE_PEOPLE);
			}

			if (target.isKingOfking()) {
				throw new ManagedException(ManagedErrorCode.OFFCIAL_CANNOT_SETTRAITOR_HIGER_OFFICIAL);
			}

			CountryOfficial countryOfficial = player.getCountry().getCourt().getPlayerOfficial(player);
			CountryOfficial targetOfficial = player.getCountry().getCourt().getPlayerOfficial(target);

			if (countryOfficial == null) {
				countryOfficial = CountryOfficial.CITIZEN;
			}
			if (targetOfficial == null) {
				targetOfficial = CountryOfficial.CITIZEN;
			}

			if (configValueManager.getOfficialPower(countryOfficial.name()) < configValueManager
					.getOfficialPower(targetOfficial.name())) {
				throw new ManagedException(ManagedErrorCode.OFFCIAL_CANNOT_SETTRAITOR_HIGER_OFFICIAL);
			}

			player.getCountry().authority(player, AuthorityID.SET_TRAITOR);
			if (target.getEffectController().contains(TraitorEffect.TRAITOR)) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COUNTRY_TRAITORED);
				return;
			}
			player.getCountry().addTraitor(target);
			Skill skill = SkillEngine.getInstance().getSkill(null,
					configValueManager.COUNTRY_TRAITOR_SKILLID.getValue(), target.getObjectId(), 0, 0, target, null);
			skill.noEffectorUseSkill();
			PacketSendUtility.sendSignMessage(player, sign);
			player.addUseAuthorityHistory(AuthorityID.SET_TRAITOR);

			I18nUtils utils = I18nUtils.valueOf("10503");
			utils.addParm(
					"officialname",
					I18nPack.valueOf(ConfigValueManager.getInstance().OFFICIAL_NAME.getValue().get(
							countryOfficial.name())));
			utils.addParm("name", I18nPack.valueOf(target.getName()));
			ChatManager.getInstance().sendSystem(11003, utils, null, player.getCountry());

			I18nUtils chatUtils = I18nUtils.valueOf("302005", utils);
			ChatManager.getInstance().sendSystem(6, chatUtils, null, player.getCountry());

			if (!SessionManager.getInstance().isOnline(target.getObjectId())) { // 不在线要手动保存
				PlayerManager.getInstance().updatePlayer(target);
			}

		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void relieveTraitor(Player player, Player target, int sign) {
		try {
			player.getCountry().lockLock();

			if (!player.sameCountry(target)) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_NOT_HAVE_PEOPLE);
			}
			if (!target.getEffectController().contains(TraitorEffect.TRAITOR)
					&& !player.getCountry().getTraitorMapFixs().containsKey(target.getObjectId())) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.TARGET_NOT_TRAITOR);
				return;
			}

			player.getCountry().authority(player, AuthorityID.RELIEVE_TRAITOR);
			target.getEffectController().removeEffect(TraitorEffect.TRAITOR);
			player.getCountry().getTraitorMapFixs().remove(target.getObjectId());
			player.addUseAuthorityHistory(AuthorityID.RELIEVE_TRAITOR);
			I18nUtils utils = I18nUtils.valueOf("10506");
			utils.addParm("name", I18nPack.valueOf(target.getName()));
			ChatManager.getInstance().sendSystem(11003, utils, null, player.getCountry());

			I18nUtils chatUtils = I18nUtils.valueOf("302006", utils);
			ChatManager.getInstance().sendSystem(6, chatUtils, null, player.getCountry());
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void upgradeCountryShop(Player player) {
		try {
			player.getCountry().lockLock();
			player.getCountry().authority(player, AuthorityID.UPGRADE_COUNTRYSHOP);
			int level = player.getCountry().getCountryShop().getLevel();
			String actionId = ConfigValueManager.getInstance().COUNTRY_SHOP_UPGRADE_ACTION.getValue().get(
					String.valueOf(level + 1));
			if (actionId == null) {
				throw new ManagedException(ManagedErrorCode.COUNTRYSHOP_MAX_LEVEL);
			}

			CoreActions actions = CoreActionManager.getInstance().getCoreActions(1, actionId);
			actions.verify(player, true);
			actions.act(player, ModuleInfo.valueOf(ModuleType.COUNTRY, SubModuleType.COUNTRY_SHOP_UPGRADE));

			player.getCountry().getCountryShop().upgradeCountryShop();
			int newLevel = player.getCountry().getCountryShop().getLevel();
			long exp = player.getCountry().getCoppers().getValue(CoppersType.SHOP_EXP);
			PacketSendUtility.sendPacket(player, SM_Country_UpgradeShop.valueOf(newLevel, exp));
			player.addUseAuthorityHistory(AuthorityID.UPGRADE_COUNTRYSHOP);

			I18nUtils utils = I18nUtils.valueOf("301024");
			utils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			String kingsName = "";
			Player king = player.getCountry().getKing();
			if (king != null) {
				kingsName = king.getName();
			}
			utils.addParm("name", I18nPack.valueOf(kingsName));
			utils.addParm("N", I18nPack.valueOf(newLevel));
			ChatManager.getInstance().sendSystem(6, utils, null, player.getCountry());

			I18nUtils utils2 = I18nUtils.valueOf("40115", utils);
			ChatManager.getInstance().sendSystem(11001, utils2, null);

		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void upgradeDoor(Player player) {
		try {
			player.getCountry().lockLock();
			player.getCountry().authority(player, AuthorityID.UPGRADE_DOOR);
			CountryDoorResource resource = countryDoorResources.get(player.getCountry().getTechnology().getDoorId(),
					true);
			if (resource.getNextLevelId() == null) {
				throw new ManagedException(ManagedErrorCode.DOOR_MAX_LEVEL);
			}
			resource.getCoreActions().verify(player, true);
			resource.getCoreActions().act(player,
					ModuleInfo.valueOf(ModuleType.COUNTRY, SubModuleType.COUNTRY_DOOR_UPGRADE));

			player.getCountry().getTechnology().upgradeDoor(resource.getNextLevelId());
			player.addUseAuthorityHistory(AuthorityID.UPGRADE_DOOR);
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void distributeTogetherToken(Player player, Player target, int sign) {
		try {
			player.getCountry().lockLock();
			if (!player.sameCountry(target)) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_NOT_HAVE_PEOPLE);
			}
			if (player.getCountry().getDiplomacy().hasTogetherToken(target.getObjectId()) != 0) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.HAD_TOKEN);
				return;
			}
			// TODO 虎符只能两个人拥有,判断
			CoreConditions conditions = conditionManager.getCoreConditions(1,
					configValueManager.COUNTRY_TOGETHERTOKENS_SIZE.getValue());
			if (!conditions.verify(player, true)) {
				PacketSendUtility.sendErrorMessage(player);
				return;
			}
			player.getCountry().authority(player, AuthorityID.DISTRIBUTE_TOGETHER_TOKEN);
			byte token = player.getCountry().getDiplomacy().addCallTogetherToken(target.getObjectId());
			PacketSendUtility.sendPacket(target, SM_Country_TogetherToken.valueOf(token, player.getName()));
			PacketSendUtility.sendSignMessage(player, sign);
			player.addUseAuthorityHistory(AuthorityID.DISTRIBUTE_TOGETHER_TOKEN);
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void callTogether(Player player, int sign) {
		try {
			player.getCountry().lockLock();

			final int x = player.getX();
			final int y = player.getY();
			final int mapId = player.getMapId();
			final int instanceId = player.getInstanceId();
			final MapResource mapResource = World.getInstance().getMapResource(mapId);
			if (player.isInCopy() && player.getMapId() != KingOfWarConfig.getInstance().MAPID.getValue()) {
				throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
			}
			if (BossConfig.getInstance().isInBossHomeMap(player.getMapId())) {
				throw new ManagedException(ManagedErrorCode.BOSS_HOME_CANNOT_CALLTOGETHER);
			}

			Official offical = player.getCountry().getPlayerOffical(player.getObjectId());
			if (offical.calltogetherCd()) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.CALLTOGETHER_CD);
				return;
			}
			player.getCountry().authority(player, AuthorityID.CALLTOGETHER);
			CoreConditions filterConditions = conditionManager.getCoreConditions(1,
					configValueManager.COUNTRY_CALL_TOGETHER.getValue());
			player.getCountry().getCourt().getCallCount().set(0);
			for (final Player target : player.getCountry().getAllCountryPlayer(player)) {
				// 如果双方都在地图中就不算召集次数
				/*
				 * boolean inKingOfWarMap = (player.getMapId() ==
				 * KingOfWarConfig.getInstance().MAPID.getValue() && target
				 * .getMapId() ==
				 * KingOfWarConfig.getInstance().MAPID.getValue());
				 * CountryCalltogeterCountCondition
				 * countryCalltogeterCountCondition = filterConditions
				 * .findConditionType(CountryCalltogeterCountCondition.class);
				 * countryCalltogeterCountCondition.setOpen(!inKingOfWarMap);
				 * int limit = countryCalltogeterCountCondition.getValue(); if
				 * (player.getCountry().isWeakCountry()) { limit +=
				 * ConfigValueManager
				 * .getInstance().WEAK_COUNTRY_CALL_COUNT.getValue(); }
				 */

				if (filterConditions.verify(target) && target != player) {
					boolean result = target.getRequester().putRequest(RequestHandlerType.COUNTRY_CALLTOGETHER,
							new RequestResponseHandler(player) {
								@Override
								public boolean deprecated() {
									if (mapId == KingOfWarConfig.getInstance().MAPID.getValue()
											&& !KingOfWarManager.getInstance().isWarring()) {
										PacketSendUtility.sendErrorMessage(target,
												ManagedErrorCode.KINGOFWAR_NOT_FIGHTING);
										return true;
									}
									return false;
								}

								@Override
								public void denyRequest(Creature requester, Player responder) {
								}

								@Override
								public void acceptRequest(Creature requester, Player responder) {
									if (responder.getCountry().getCourt().getCallCount().incrementAndGet() > ConfigValueManager
											.getInstance().CALL_TOGETHER_COUNT_LIMIT.getValue()) {
										PacketSendUtility.sendErrorMessage(responder,
												ManagedErrorCode.CALL_TOGETHER_LIMIT);
										return;
									}
									if (responder.isInCopy()
											&& responder.getMapId() != KingOfWarConfig.getInstance().MAPID.getValue()) {
										PacketSendUtility.sendErrorMessage(responder, ManagedErrorCode.PLAYER_IN_COPY);
										return;
									}
									World.getInstance().canEnterMap(responder, mapId);
									Official king = target.getCountry().getCourt().getKing();
									if (king != null) {
										if (requester.getMapId() != KingOfWarConfig.getInstance().MAPID.getValue()
												|| responder.getMapId() != KingOfWarConfig.getInstance().MAPID
														.getValue()) {
											king.addCallCount(responder.getObjectId());
										}
									}

									responder.getMoveController().stopMoving();
									if (responder.getPosition().getMapId() == mapId
											&& responder.getPosition().getInstanceId() == instanceId) {
										World.getInstance().updatePosition(responder, x, y, responder.getHeading());
									} else {
										World.getInstance().setPosition(responder, mapId, instanceId, x, y,
												responder.getHeading());
									}
									responder.sendUpdatePosition();

									// 国王召集国民,添加免费复活BUFF
									Skill skill = SkillEngine.getInstance().getSkill(null, 20040,
											responder.getObjectId(), 0, 0, responder, null);
									skill.noEffectorUseSkill();
									if (!mapResource.isCopy()
											|| mapId == KingOfWarConfig.getInstance().MAPID.getValue()) {
										Skill godSKill = SkillEngine.getInstance().getSkill(null,
												configValueManager.BEEN_CALLED_BUFF.getValue(),
												responder.getObjectId(), 0, 0, responder, null);
										godSKill.noEffectorUseSkill();
									}
								}
							});
					if (result) {
						PacketSendUtility.sendPacket(target, SM_Country_CallTogether.valueOf((byte) 0,
								player.getName(), mapId, x, y, 0 - offical.getCallCount(target.getObjectId()),
								(byte) offical.getOfficial().getValue()));
					}
				}
			}
			List<Official> shareOfficials = player.getCountry().getCourt()
					.getOfficialByOfficialName(configValueManager.COUNTRY_CALL_PEOPLE.getValue());
			for (Official shareOfficial : shareOfficials) {
				shareOfficial.calltogether(configValueManager.COUNTRY_CALL_TOGETHER_CD.getValue());
			}
			PacketSendUtility.sendSignMessage(player, sign);
			player.addUseAuthorityHistory(AuthorityID.CALLTOGETHER);
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void callTogetherToken(Player player, int sign) {
		try {
			player.getCountry().lockLock();
			if (player.getCountry().getDiplomacy().hasTogetherToken(player.getObjectId()) == 0) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.NO_TOGETHER_TOKEN);
				return;
			}
			if (player.isInCopy() && player.getMapId() != KingOfWarConfig.getInstance().MAPID.getValue()) {
				throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
			}
			final int x = player.getX();
			final int y = player.getY();
			final int mapId = player.getMapId();
			final int instanceId = player.getInstanceId();
			CoreConditions useConditions = conditionManager.getCoreConditions(1,
					configValueManager.COUNTRY_USECALLTOKEN_CONDITIONS.getValue());
			if (!useConditions.verify(player, true)) {
				PacketSendUtility.sendErrorMessage(player);
				return;
			}
			CoreConditions filterConditions = conditionManager.getCoreConditions(1,
					configValueManager.COUNTRY_CALL_TOGETHER.getValue());
			player.getCountry().getCourt().getCallTokenCount().set(0);
			for (Player target : player.getCountry().getAllCountryPlayer(player)) {
				// 如果双方都在地图中就不算召集次数
				boolean inKingOfWarMap = (player.getMapId() == KingOfWarConfig.getInstance().MAPID.getValue() && target
						.getMapId() == KingOfWarConfig.getInstance().MAPID.getValue());
				CountryCalltogeterCountCondition countryCalltogeterCountCondition = filterConditions
						.findConditionType(CountryCalltogeterCountCondition.class);
				countryCalltogeterCountCondition.setOpen(!inKingOfWarMap);
				int limit = countryCalltogeterCountCondition.getValue();
				if (player.getCountry().isWeakCountry()) {
					limit += ConfigValueManager.getInstance().WEAK_COUNTRY_CALL_COUNT.getValue();
				}
				if (filterConditions.verify(target) && target != player) {
					boolean result = target.getRequester().putRequest(RequestHandlerType.COUNTRY_CALLTOGETHER,
							new RequestResponseHandler(target) {
								@Override
								public boolean deprecated() {
									return false;
								}

								@Override
								public void denyRequest(Creature requester, Player responder) {
								}

								@Override
								public void acceptRequest(Creature requester, Player responder) {
									if (responder.getCountry().getCourt().getCallTokenCount().incrementAndGet() > ConfigValueManager
											.getInstance().CALL_TOGETHER_COUNT_LIMIT.getValue()) {
										PacketSendUtility.sendErrorMessage(responder,
												ManagedErrorCode.CALL_TOGETHER_LIMIT);
										return;
									}
									if (responder.isInCopy()
											&& responder.getMapId() != KingOfWarConfig.getInstance().MAPID.getValue()) {
										PacketSendUtility.sendErrorMessage(responder, ManagedErrorCode.PLAYER_IN_COPY);
										return;
									}
									World.getInstance().canEnterMap(responder, mapId);
									responder.getMoveController().stopMoving();
									if (responder.getPosition().getMapId() == mapId
											&& responder.getPosition().getInstanceId() == instanceId) {
										World.getInstance().updatePosition(responder, x, y, responder.getHeading());
									} else {
										World.getInstance().setPosition(responder, mapId, instanceId, x, y,
												responder.getHeading());
									}
									responder.sendUpdatePosition();
									Official king = responder.getCountry().getCourt().getKing();
									if (king != null) {
										king.addCallCount(responder.getObjectId());
									}
								}
							});
					if (result) {
						PacketSendUtility.sendPacket(target, SM_Country_CallTogether.valueOf(player.getCountry()
								.getDiplomacy().hasTogetherToken(player.getObjectId()), player.getName(), mapId, x, y,
								limit - player.getCountry().getCourt().getKing().getCallCount(target.getObjectId()),
								(byte) CountryOfficial.CITIZEN.getValue()));
					}
				}
			}
			player.getCountry().getDiplomacy().useTogetherToken(player.getObjectId());
			PacketSendUtility.sendSignMessage(player, sign);
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void callTogetherGuard(Player player, int sign) {
		try {
			player.getCountry().lockLock();
			player.getCountry().authority(player, AuthorityID.CALLTOGETHER_GUARD);
			final int x = player.getX();
			final int y = player.getY();
			final int mapId = player.getMapId();
			final int instanceId = player.getInstanceId();
			if (player.getMapId() == KingOfWarConfig.getInstance().MAPID.getValue()) {
				throw new ManagedException(ManagedErrorCode.KINGOFWAR_CANNOT_CALLGUARD);
			}

			if (player.isInCopy()) {
				throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
			}
			if (BossConfig.getInstance().isInBossHomeMap(player.getMapId())) {
				throw new ManagedException(ManagedErrorCode.BOSS_HOME_CANNOT_CALLTOGETHER);
			}

			int limitCount = ConfigValueManager.getInstance().COUNTRY_CALL_GUARD_COUNT.getValue()
					+ player.getCountry().getCourt().getAuthorityExtraCount(AuthorityID.CALLTOGETHER_GUARD);
			Official official = player.getCountry().getCourt().getOfficials().get(player.getObjectId());
			Integer useCount = official.getUseAuthorityHistory(AuthorityID.CALLTOGETHER_GUARD);
			int available = limitCount - (useCount == null ? 0 : useCount);
			for (Player target : player.getCountry().getAllGuardPlayer(player)) {
				if (!ConfigValueManager.getInstance().getGuardCalledCondtions().verify(target, false)) {
					continue;
				}
				boolean result = target.getRequester().putRequest(RequestHandlerType.COUNTRY_GUARDTOGETHER,
						new RequestResponseHandler(target) {
							@Override
							public boolean deprecated() {
								return false;
							}

							@Override
							public void denyRequest(Creature requester, Player responder) {
							}

							@Override
							public void acceptRequest(Creature requester, Player responder) {
								if (responder.isInCopy()) {
									PacketSendUtility.sendErrorMessage(responder, ManagedErrorCode.PLAYER_IN_COPY);
									return;
								}
								World.getInstance().canEnterMap(responder, mapId);
								responder.getMoveController().stopMoving();
								if (responder.getPosition().getMapId() == mapId
										&& responder.getPosition().getInstanceId() == instanceId) {
									World.getInstance().updatePosition(responder, x, y, responder.getHeading());
								} else {
									World.getInstance().setPosition(responder, mapId, instanceId, x, y,
											responder.getHeading());
								}
								responder.sendUpdatePosition();
							}
						});
				if (result) {
					PacketSendUtility.sendPacket(target,
							SM_Country_CallTogether_Guard.valueOf(player.getName(), mapId, x, y, available));
				}
			}
			PacketSendUtility.sendSignMessage(player, sign);
			player.addUseAuthorityHistory(AuthorityID.CALLTOGETHER_GUARD);
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void upgradeFlag(Player player) {
		try {
			player.getCountry().lockLock();
			player.getCountry().authority(player, AuthorityID.UPGRADE_FLAG);
			CountryFlagResource resource = getCountryFlagResources().get(
					player.getCountry().getCountryFlag().getFlagId(), true);
			if (resource.getNextLevelId() == null) {
				throw new ManagedException(ManagedErrorCode.FLAG_MAX_LEVEL);
			}
			resource.getCoreActions().verify(player, true);
			resource.getCoreActions().act(player,
					ModuleInfo.valueOf(ModuleType.COUNTRY, SubModuleType.COUNTRY_FLAG_UPGRADE));

			player.getCountry().getCountryFlag().upgradeFlag(resource.getNextLevelId());
			player.addUseAuthorityHistory(AuthorityID.UPGRADE_FLAG);
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void upgradeFactory(Player player) {
		try {
			player.getCountry().lockLock();
			player.getCountry().authority(player, AuthorityID.UPGRADE_FACATORY);
			CountryFactoryResource resource = getCountryFactoryResources().get(
					player.getCountry().getTechnology().getArmsFactory().getFactoryId(), true);
			if (resource.getNextLevelId() == null) {
				throw new ManagedException(ManagedErrorCode.FACTORY_MAX_LEVEL);
			}
			resource.getCoreActions().verify(player, true);
			resource.getCoreActions().act(player,
					ModuleInfo.valueOf(ModuleType.COUNTRY, SubModuleType.COUNTRY_FACTORY_UPGRADE));

			player.getCountry().getTechnology().getArmsFactory().upgradeFactory(resource.getNextLevelId());
			player.addUseAuthorityHistory(AuthorityID.UPGRADE_FACATORY);
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void setNotice(Player player, String notice) throws UnsupportedEncodingException {
		String fileterNotice = CharCheckUtil.filterOffUtf8Mb4(notice);
		player.getCountry().authority(player, AuthorityID.SET_NOTICE);
		player.getCountry().getCourt().setNotice(fileterNotice);
		PacketSendUtility.sendPacket(player, SM_Country_SetNotice.valueOf(fileterNotice));
		player.addUseAuthorityHistory(AuthorityID.SET_NOTICE);
	}

	public void contribution(Player player, String type, int count, int sign) {
		try {
			player.getCountry().lockLock();
			CoppersType coppersType = CoppersType.typeOf(type);
			String actionId = ConfigValueManager.getInstance().COUNTRY_CONTRIBUTION.getValue().get(coppersType.name());
			CoreActions actions = actionManager.getCoreActions(count, actionId);
			actions.verify(player, true);
			ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.COUNTRY, SubModuleType.COUNTRY_CONTRIBUTE);
			actions.act(player, moduleInfo);
			String rewardId = ConfigValueManager.getInstance().COUNTRY_CONTRIBUTION_REWARD.getValue().get(
					coppersType.name());
			String rewardId1 = ConfigValueManager.getInstance().COUNTRY_CONTRIBUTION_REWARD1.getValue().get(
					coppersType.name());
			List<String> rewardIds = new ArrayList<String>();
			for (int i = 0; i < count; i++) {
				rewardIds.add(rewardId);
				rewardIds.add(rewardId1);
			}
			Reward reward = rewardManager.creatReward(player, rewardIds, null);
			int myCountribute = 0;
			List<RewardItem> contributeAdd = reward.getItemsByType(RewardType.CURRENCY);
			for (RewardItem item : contributeAdd) {
				if (item.getType().equals(RewardType.CURRENCY.name())
						&& Integer.valueOf(item.getCode()).intValue() == CurrencyType.CONTRIBUTION.getValue()) {
					myCountribute += item.getAmount();
				}
			}
			rewardManager.grantReward(player, rewardIds, moduleInfo);
			PacketSendUtility.sendPacket(player,
					SM_Country_Contribution.valueOf(player.getCountry().getCoppers(), sign, myCountribute));
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void contributionShop(Player player, String itemId, int count) {
		try {
			player.getCountry().lockLock();
			ItemAction actions = CoreActionType.createItemCondition(itemId, count);
			actions.verify(player);
			ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.COUNTRY, SubModuleType.CONTRIBUTE_KILLITEM);
			actions.act(player, moduleInfo);
			String rewardId = ConfigValueManager.getInstance().COUNTRY_CONTRIBUTION_REWARD.getValue().get(itemId);
			String rewardId1 = ConfigValueManager.getInstance().COUNTRY_CONTRIBUTION_REWARD1.getValue().get(itemId);
			List<String> rewardIds = new ArrayList<String>();
			for (int i = 0; i < count; i++) {
				rewardIds.add(rewardId);
				rewardIds.add(rewardId1);
			}
			Reward reward = rewardManager.creatReward(player, rewardIds, null);
			int myCountribute = 0;
			int shopAddExp = 0;
			for (RewardItem item : reward.getItems()) {
				if (item.getType().equals(RewardType.COUNTRY_CURRENCY.name())
						&& Integer.valueOf(item.getCode()).intValue() == CoppersType.SHOP_EXP.getValue()) {
					shopAddExp += item.getAmount();
				}
				if (item.getType().equals(RewardType.CURRENCY.name())
						&& Integer.valueOf(item.getCode()).intValue() == CurrencyType.CONTRIBUTION.getValue()) {
					myCountribute += item.getAmount();
				}
			}
			rewardManager.grantReward(player, rewardIds, moduleInfo);
			PacketSendUtility.sendPacket(player, SM_Country_Contribute_Shop.valueOf(player.getCountry().getCoppers()
					.getValue(CoppersType.SHOP_EXP), shopAddExp, myCountribute));
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void buy(Player player, String id, int count) {
		try {
			player.getCountry().lockLock();
			CountryShopResource resource = countryShopResources.get(id, true);

			CoreConditions conditions = conditionManager.getCoreConditions(count, resource.getBuyCondtions());
			if (!conditions.verify(player, true)) {
				PacketSendUtility.sendErrorMessage(player);
				return;
			}

			CoreActions actions = actionManager.getCoreActions(count, resource.getActions());
			actions.verify(player, true);
			ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.COUNTRY, SubModuleType.COUNTRYSHOP_BUY);

			List<String> rewardIds = new ArrayList<String>();
			for (int i = 0; i < count; i++) {
				List<String> chooserRewardIds = chooserManager.chooseValueByRequire(player,
						resource.getChooserGroupId());
				rewardIds.addAll(chooserRewardIds);
			}

			Reward reward = rewardManager.creatReward(player, rewardIds, null);
			if (!rewardManager.playerPackCanholdAll(player, reward)) {
				throw new ManagedException(ManagedErrorCode.PACK_GRID_NOT_ENOUGH);
			}

			actions.act(player, moduleInfo);
			rewardManager.grantReward(player, reward, moduleInfo);
			player.getCountry().getCountryShop().addCount(player, id, count);
			EventBusManager.getInstance().submit(CountryBuyEvent.valueOf(player));
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void store(Player player, Set<Integer> indexs, boolean inPack) {
		try {
			player.getCountry().lockLock();
			if (player.isTrading()) {
				throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
			}
			player.getCountry().authority(player, AuthorityID.STORE);

			ItemStorage pack = (inPack ? player.getPack() : player.getWareHouse());
			ItemStorage countryStore = player.getCountry().getCountryStorge().getStorge();
			// 绑定物品验证
			for (int index : indexs) {
				AbstractItem item = pack.getItemByIndex(index);
				if (item == null) {
					throw new ManagedException(ManagedErrorCode.NOT_FOUND_ITEM);
				}
				if (item.isBind()) {
					throw new ManagedException(ManagedErrorCode.ITEM_BIND);
				}
			}

			int size = indexs.size();
			if (countryStore.getEmptySize() < size) {
				throw new ManagedException(ManagedErrorCode.WARE_NOT_ENOUGH);
			}
			player.getCountry().getCountryStorge().recordLog(CountryLogEnum.PUT, player, indexs, false);

			exchange(pack, countryStore, indexs);

			PacketSendUtility.sendPacket(player,
					SM_Country_Store.valueOf(pack.collectUpdate(), countryStore.collectUpdate()));

			player.addUseAuthorityHistory(AuthorityID.STORE);
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void countryMovePack(Player player, byte type, int fromIndex, int toIndex, int sign, boolean inPack) {
		try {
			player.getCountry().lockLock();
			if (player.isTrading()) {
				throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
			}
			if (type == 0) {
				player.getCountry().authority(player, AuthorityID.STORE);
			} else {
				player.getCountry().authority(player, AuthorityID.TAKE);
			}

			ItemStorage pack = (inPack ? player.getPack() : player.getWareHouse());
			ItemStorage countryStore = player.getCountry().getCountryStorge().getStorge();

			if (type == 0) {
				Set<Integer> indexs = new HashSet<Integer>();
				indexs.add(new Integer(fromIndex));
				player.getCountry().getCountryStorge().recordLog(CountryLogEnum.PUT, player, indexs, false);
				AbstractItem fItem = pack.removeItemByIndex(fromIndex);
				if (fItem.isBind()) {
					throw new ManagedException(ManagedErrorCode.ITEM_BIND);
				}
				AbstractItem tItem = countryStore.removeItemByIndex(toIndex);
				pack.addItemByIndex(fromIndex, tItem);
				countryStore.addItemByIndex(toIndex, fItem);
			} else {
				Set<Integer> indexs = new HashSet<Integer>();
				indexs.add(new Integer(fromIndex));
				player.getCountry().getCountryStorge().recordLog(CountryLogEnum.TAKE, player, indexs, false);
				AbstractItem fItem = countryStore.removeItemByIndex(fromIndex);
				AbstractItem tItem = pack.removeItemByIndex(toIndex);
				countryStore.addItemByIndex(fromIndex, tItem);
				pack.addItemByIndex(toIndex, fItem);
			}

			SM_Country_Store result = new SM_Country_Store();
			result.setPackUpdate(pack.collectUpdate());
			result.setCountryStorageUpdate((countryStore.collectUpdate()));

			PacketSendUtility.sendPacket(player, result);
			PacketSendUtility.sendSignMessage(player, sign);
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void take(Player player, Set<Integer> indexs, boolean inPack) {
		try {
			player.getCountry().lockLock();
			if (player.isTrading()) {
				throw new ManagedException(ManagedErrorCode.IN_TRADING_ERROR);
			}
			player.getCountry().authority(player, AuthorityID.TAKE);

			ItemStorage pack = (inPack ? player.getPack() : player.getWareHouse());
			ItemStorage countryStore = player.getCountry().getCountryStorge().getStorge();

			int size = indexs.size();
			if (pack.getEmptySize() < size) {
				throw new ManagedException(ManagedErrorCode.PACK_NOT_ENOUGH);
			}
			player.getCountry().getCountryStorge().checkIndexs(indexs);

			player.getCountry().getCountryStorge().recordLog(CountryLogEnum.TAKE, player, indexs, false);

			exchange(countryStore, pack, indexs);

			PacketSendUtility.sendPacket(player,
					SM_Country_Store.valueOf(pack.collectUpdate(), countryStore.collectUpdate()));
			player.addUseAuthorityHistory(AuthorityID.TAKE);
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void createTank(Player player, String id, int index, int sign) {
		try {
			player.getCountry().lockLock();
			player.getCountry().authority(player, AuthorityID.CREATE_TANK);
			if (player.getCountry().getTechnology().getArmsFactory().isFull()) {
				throw new ManagedException(ManagedErrorCode.FACTORY_FULL);
			}
			CountryTankResource resource = countryTankResources.get(id, true);
			if (!resource.getCreateConditions().verify(player)) {
				PacketSendUtility.sendErrorMessage(player);
				return;
			}

			resource.getCreateActions().verify(player, true);
			resource.getCreateActions().act(player,
					ModuleInfo.valueOf(ModuleType.COUNTRY, SubModuleType.COUNTRY_TANK_CREATE));

			player.getCountry().getTechnology().getArmsFactory().createTank(id, player, index);
			player.addUseAuthorityHistory(AuthorityID.CREATE_TANK);
			PacketSendUtility.sendSignMessage(player, sign);
		} finally {
			player.getCountry().unlockLock();
		}

	}

	public void getFactory(Player player) {
		PacketSendUtility.sendPacket(player,
				SM_Country_Factory.valueOf(player.getCountry().getTechnology().getArmsFactory().createVO()));
	}

	public void getFlag(Player player) {
		PacketSendUtility.sendPacket(player, SM_Country_Flag.valueOf(player.getCountry().getCountryFlag()));
	}

	public void getOffers(Player player) {
		PacketSendUtility.sendPacket(player, SM_Country_Coppers.valueOf(player.getCountry().getCoppers(), 0));
	}

	public void getStorge(Player player) {
		PacketSendUtility
				.sendPacket(player, SM_Country_Storage.valueOf(player, player.getCountry().getCountryStorge()));
	}

	public void getShop(Player player) {
		refreshCountryShop();
		PacketSendUtility.sendPacket(player, SM_Country_Shop.valueOf(player, player.getCountry().getCountryShop()));
	}

	public void getOfficalList(Player player) {
		PacketSendUtility.sendPacket(player, SM_Country_Offical.valueOf(player.getCountry()));
	}

	public void getDoor(Player player) {
		PacketSendUtility.sendPacket(player, SM_Country_Door.valueOf(player.getCountry().getTechnology()));
	}

	public void upgradeTank(Player player, int id, int sign) {
		try {
			player.getCountry().lockLock();
			player.getCountry().authority(player, AuthorityID.UPGRADE_TANK);

			Tank tank = player.getCountry().getTechnology().getArmsFactory().getTank(id);
			if (tank == null) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.NOT_FOUND_TANK);
				return;
			}
			CountryTankResource resource = countryTankResources.get(tank.getResourceId(), true);
			if (resource.getNextLevelId() == null) {
				PacketSendUtility.sendErrorMessage(player);
				return;
			}
			if (!resource.getUpgradeConditions().verify(player)) {
				PacketSendUtility.sendErrorMessage(player);
				return;
			}

			resource.getUpgradeActions().verify(player, true);
			resource.getUpgradeActions().act(player,
					ModuleInfo.valueOf(ModuleType.COUNTRY, SubModuleType.COUNTRY_TANK_UPGRADE));

			player.getCountry().getTechnology().getArmsFactory().upgradeTank(id, resource.getNextLevelId(), player);
			player.addUseAuthorityHistory(AuthorityID.UPGRADE_TANK);
			PacketSendUtility.sendSignMessage(player, sign);
		} finally {
			player.getCountry().unlockLock();
		}

	}

	private void exchange(ItemStorage from, ItemStorage to, Set<Integer> indexs) {
		for (Integer index : indexs) {
			if (from.getItemByIndex(index) == null) {
				throw new ManagedException(ManagedErrorCode.NOT_FOUND_ITEM);
			}
		}
		for (int index : indexs) {
			AbstractItem item = from.removeItemByIndex(index);
			if (item != null) {
				to.addItems(false, item);
			}
		}
	}

	public Country getCountry(Player player) {
		return getCountries().get(CountryId.valueOf(player.getPlayerEnt().getCountry()));
	}

	public static CountryManager getInstance() {
		return instance;
	}

	public static void setInstance(CountryManager instance) {
		CountryManager.instance = instance;
	}

	/** 商店筛选 2015.4.1 现在不随机了, 所有商品全部要显示， 这里前端不愿意改，就还是推一次.. */
	// / private static final String countryShopChooser =
	// "country_shop_chooser";

	public void refreshCountryShop() {
		if (getCountries() == null || getCountries().isEmpty()) {
			return;
		}
		// 3个国家的商店道具分开存储产生冗余。是便于以后不同国家刷新不同道具扩展用。
		Country country = getCountries().get(CountryId.C1);
		long now = System.currentTimeMillis();
		if (!DateUtils.isToday(new Date(country.getCountryShop().getLastRefreshTime()))) {
			List<String> items = new ArrayList<String>();
			for (CountryShopResource res : countryShopResources.getAll()) {
				items.add(res.getId());
			}
			for (Country c : getCountries().values()) {
				try {
					c.lockLock();
					c.getCountryShop().reset(now, items);
				} finally {
					c.unlockLock();
				}
				// I18nUtils utils = I18nUtils.valueOf("301025");
				// ChatManager.getInstance().sendSystem(6, utils, null, c);
			}
		}
	}

	public void refreshConctrol() {
		for (Country c : getCountries().values()) {
			try {
				c.lockLock();
				c.getCourt().refresh();
			} finally {
				c.unlockLock();
			}
		}
	}

	public void refreshSalary() {
		for (Country c : getCountries().values()) {
			try {
				c.lockLock();
				c.getCourt().resetSalary(configValueManager.COUNTRY_CIVIL_SALARY_RESETTIME.getValue(),
						configValueManager.COUNTRY_OFFICIAL_SALARY_RESETTIME.getValue());
			} finally {
				c.unlockLock();
			}
		}
	}

	public void refreshForbidChat() {
		for (Country c : getCountries().values()) {
			try {
				c.lockLock();
				List<Long> removes = New.arrayList();
				for (ForbidChat fc : c.getForbidChats().values()) {
					if (fc.end()) {
						removes.add(fc.getPlayerId());
					}
				}
				for (long playerId : removes) {
					c.getForbidChats().remove(playerId);
				}
			} finally {
				c.unlockLock();
			}
		}
	}

	public void refreshTraitorPlayerFix() {
		for (Country c : getCountries().values()) {
			try {
				c.lockLock();
				List<Long> removes = New.arrayList();
				for (TraitorPlayerFix tp : c.getTraitorMapFixs().values()) {
					Player player = c.getCivils().get(tp.getPlayerId());
					if (player != null) {
						if (tp.reduceTimeAndIsRemove(player)) {
							removes.add(player.getObjectId());
						}
					}
				}
				for (long playerId : removes) {
					c.getTraitorMapFixs().remove(playerId);
				}
			} finally {
				c.unlockLock();
			}
		}
	}

	public void loginTraitorPlayrFix(Player player) {
		player.getEffectController().removeEffect(TraitorEffect.TRAITOR);
		TraitorPlayerFix tp = player.getCountry().getTraitorMapFixs().get(player.getObjectId());
		if (tp != null) {
			tp.login(player);
		}
	}

	public void logoutTraitorPlayrFix(Player player) {
		TraitorPlayerFix tp = player.getCountry().getTraitorMapFixs().get(player.getObjectId());
		if (tp != null) {
			tp.reduceTimeAndIsRemove(player);
		}
	}

	public SM_Country_Open_Diplomacy openDiplomacyPanel(Player player) {
		SM_Country_Open_Diplomacy sm = new SM_Country_Open_Diplomacy();
		Map<Integer, Boolean> map = New.hashMap();
		Map<Integer, Integer> hiterCountry = New.hashMap();
		Map<Integer, Long> deadTime = New.hashMap();
		for (Country country : CountryManager.getInstance().getCountries().values()) {
			map.put(country.getId().getValue(), country.getDiplomacy().isNotAttacked());
			if (country.getDiplomacy().getCountryNpc() != null
					&& country.getDiplomacy().getCountryNpc().getLifeStats().isAlreadyDead()) {
				hiterCountry.put(country.getId().getValue(), country.getDiplomacy().getHiterCountry());
			}
			deadTime.put(country.getId().getValue(), country.getDiplomacy().getNextReliveTime());
		}
		sm.setCountryNpcHpMap(map);
		sm.setHiterCountry(hiterCountry);
		sm.setDeadTime(deadTime);
		return sm;
	}

	public SM_Country_Open_Flag openFlagPanel(Player player) {
		SM_Country_Open_Flag sm = new SM_Country_Open_Flag();
		Map<Integer, Boolean> map = New.hashMap();
		Map<Integer, Integer> hiterCountry = New.hashMap();
		Map<Integer, Long> deadTime = New.hashMap();
		for (Country country : CountryManager.getInstance().getCountries().values()) {
			map.put(country.getId().getValue(), country.getCountryFlag().isNotAttacked());
			if (country.getCountryFlag().getCountryNpc() != null
					&& country.getCountryFlag().getCountryNpc().getLifeStats().isAlreadyDead()) {
				hiterCountry.put(country.getId().getValue(), country.getCountryFlag().getHiterCountry());
			}
			deadTime.put(country.getId().getValue(), country.getCountryFlag().getNextReliveTime());
		}
		sm.setCountryNpcHpMap(map);
		sm.setHiterCountry(hiterCountry);
		sm.setDeadTime(deadTime);
		return sm;
	}

	public Storage<String, CountryFactoryResource> getCountryFactoryResources() {
		return countryFactoryResources;
	}

	public void registerPlayer(Player player) {
		player.getCountry().registerCivil(player);
	}

	public void unRegisterPlayer(Player player) {
		player.getCountry().unRegisterCivil(player);
	}

	public Map<CountryId, Country> getCountries() {
		return countries;
	}

	public Country getCountryByValue(int countryValue) {
		CountryId countryId = CountryId.valueOf(countryValue);
		return countries.get(countryId);
	}

	public Country getCountryById(CountryId id) {
		return countries.get(id);
	}

	public Storage<String, CountryFlagResource> getCountryFlagResources() {
		return countryFlagResources;
	}

	public void setCountryFlagResources(Storage<String, CountryFlagResource> countryFlagResources) {
		this.countryFlagResources = countryFlagResources;
	}

	public SimpleScheduler getSimpleScheduler() {
		return simpleScheduler;
	}

	public void setSimpleScheduler(SimpleScheduler simpleScheduler) {
		this.simpleScheduler = simpleScheduler;
	}

	public void seeAllLog(Player player) {
		Map<Integer, CountryLoggerVO> map = New.hashMap();
		for (Map.Entry<Integer, CountryLogger> entry : player.getCountry().getCountryStorge().getLogMap().entrySet()) {
			int action = entry.getKey();
			CountryLogger logger = entry.getValue();
			map.put(action, CountryLoggerVO.valueOf(logger));
		}
		SM_Country_Action_See_All_Log sm = SM_Country_Action_See_All_Log.valueOf(map);
		PacketSendUtility.sendPacket(player, sm);
	}

	public void seeFeteLog(Player player, boolean all) {
		PacketSendUtility.sendPacket(player,
				SM_Country_Fete_Log.valueOf(player.getCountry().getFeteLog().getLogValues(all)));
	}

	public void sacrifice(Player player, int type, int sign) {
		if (!ModuleOpenManager.getInstance().isOpenByKey(player, "opmk53")) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.MODULE_NOT_OPEN);
			return;
		}
		try {
			player.getCountry().lockLock();

			if (DateUtils.isToday(new Date(player.getWelfare().getLastFeteTime()))) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.TODAY_ALREADY_FETE);
				return;
			}

			List<String> rewardIds = null;
			if (type == 0) { // 铜币祭祀
				List<String> actionIds = ChooserManager.getInstance().chooseValueByRequire(player,
						ConfigValueManager.getInstance().FETE_COPPER_ACTION.getValue());
				String[] actionString = new String[actionIds.size()];
				CoreActions actions = CoreActionManager.getInstance()
						.getCoreActions(1, actionIds.toArray(actionString));
				if (!actions.verify(player, true)) {
					PacketSendUtility.sendErrorMessage(player);
					return;
				}
				actions.act(player, ModuleInfo.valueOf(ModuleType.FETE, SubModuleType.SACRIFICE_ACT));
				rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
						ConfigValueManager.getInstance().FETE_COPPER_REWARD.getValue());
			} else if (type == 1) { // 元宝祭祀
				if (!player.getVip().getResource().isCanSacrifice()) {
					PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.VIP_CONDITION_NOT_SATISFY);
					return;
				}
				List<String> actionIds = ChooserManager.getInstance().chooseValueByRequire(player,
						ConfigValueManager.getInstance().FETE_COINS_ACTION.getValue());
				String[] actionString = new String[actionIds.size()];
				CoreActions actions = CoreActionManager.getInstance()
						.getCoreActions(1, actionIds.toArray(actionString));
				if (!actions.verify(player, true)) {
					PacketSendUtility.sendErrorMessage(player);
					return;
				}
				actions.act(player, ModuleInfo.valueOf(ModuleType.FETE, SubModuleType.SACRIFICE_GOLD_ACT));
				rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
						ConfigValueManager.getInstance().FETE_COINS_REWARD.getValue());

				// 使用元宝祭祀公告
				I18nUtils tvUtils = I18nUtils.valueOf("20301")
						.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()))
						.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
				ChatManager.getInstance().sendSystem(11001, tvUtils, null);

				I18nUtils chatUtils = I18nUtils.valueOf("301009")
						.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()))
						.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
				ChatManager.getInstance().sendSystem(0, chatUtils, null);
			}
			Reward reward = RewardManager.getInstance().creatReward(player, rewardIds, null);
			List<RewardItem> items = reward.getItemsByType(RewardType.CURRENCY);
			int honor = 0;
			for (RewardItem i : items) {
				if (Integer.valueOf(i.getCode()).intValue() == CurrencyType.HONOR.getValue()) {
					honor = i.getAmount();
				}
			}
			player.getWelfare().addFeteHistoryCount(1);
			RewardManager.getInstance().grantReward(player, rewardIds,
					ModuleInfo.valueOf(ModuleType.FETE, SubModuleType.SACRIFICE_REWARD));
			player.getCountry()
					.getFeteLog()
					.record(player.getPlayerEnt().getServer(), player.getObjectId(), type, player.getName(), honor,
							System.currentTimeMillis(), player.getCountryValue());
			PacketSendUtility.sendPacket(player,
					SM_Country_Fete_Log.valueOf(player.getCountry().getFeteLog().getLogValues(false)));
			PacketSendUtility.sendPacket(player, SM_Country_Fete.valueOf(type));
			EventBusManager.getInstance().submit(CountrySacrificeEvent.valueOf(player.getObjectId()));
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void mobilization(Player player, String phrases, int sign) {
		try {
			player.getCountry().lockLock();
			player.getCountry().authority(player, AuthorityID.OFFICIAL_MOBILIZATION);
			Official official = player.getCountry().getCourt().getOfficials().get(player.getObjectId());
			int dailyAlreadyUse = player.getCountry().getCourt().getMobilizationCount(player.getObjectId());
			int dailyCountLimit = ConfigValueManager.getInstance().OFFICIAL_MOBILIZATION_DAILY_COUNT.getValue().get(
					official.getOfficial().name());
			if (dailyAlreadyUse >= dailyCountLimit) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COUNTRY_MOBILIZATION_COUNT_LIMIT);
				return;
			}

			Map<String, Map<String, String>> npcLocation = ConfigValueManager.getInstance().OFFICIAL_MOBILIZATION_NPC
					.getValue();
			npcLocation.get(String.valueOf(player.getCountryValue()));

			String countryKey = String.valueOf(player.getCountryValue());
			final int x = Integer.parseInt(npcLocation.get(countryKey).get("x"));
			final int y = Integer.parseInt(npcLocation.get(countryKey).get("y"));
			final int mapId = Integer.parseInt(npcLocation.get(countryKey).get("mapId"));
			final int instanceId = player.getInstanceId();
			if (player.isInCopy()) {
				throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
			}
			if (player.getLifeStats().isAlreadyDead()) {
				throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
			}
			if (player.getMapId() != mapId || (!MathUtil.isInRange(x, y, player, 10, 10))) {
				throw new ManagedException(ManagedErrorCode.NOT_RIGHT_POSITION);
			}
			if (phrases != null && phrases.length() > 140) {
				PacketSendUtility.sendPacket(player, ManagedErrorCode.PHRASE_TOO_LARGE);
				return;
			}

			String[] acts = ConfigValueManager.getInstance().OFFICIAL_MOBILIZATION_ACTS.getValue();
			CoreActions actions = CoreActionManager.getInstance().getCoreActions(1, acts);
			if (!actions.verify(player)) {
				PacketSendUtility.sendErrorMessage(player);
				return;
			}

			actions.act(player, ModuleInfo.valueOf(ModuleType.COUNTRY, SubModuleType.MOBILIZATION));

			phrases = DirtyWordsManager.getInstance().filter(phrases, WordsType.CHATWORDS);

			String[] conditions = ConfigValueManager.getInstance().OFFICIAL_MOBILIZATION_ACCEPT_CONDITIONID.getValue();
			CoreConditions filterConditions = CoreConditionManager.getInstance().getCoreConditions(1, conditions);
			player.getCountry().getCourt().getMobilizationCount().set(0);
			for (Player target : player.getCountry().getAllCountryPlayer(player)) {
				if (filterConditions.verify(target) && target != player) {
					boolean result = target.getRequester().putRequest(RequestHandlerType.OFFICIAL_MOBILIZATION,
							new RequestResponseHandler(target) {
								@Override
								public boolean deprecated() {
									return false;
								}

								@Override
								public void denyRequest(Creature requester, Player responder) {
								}

								@Override
								public void acceptRequest(Creature requester, Player responder) {
									if (responder.getCountry().getCourt().getMobilizationCount().incrementAndGet() > ConfigValueManager
											.getInstance().CALL_TOGETHER_COUNT_LIMIT.getValue()) {
										PacketSendUtility.sendErrorMessage(responder,
												ManagedErrorCode.CALL_TOGETHER_LIMIT);
										return;
									}
									if (responder.isInCopy()) {
										PacketSendUtility.sendErrorMessage(responder, ManagedErrorCode.PLAYER_IN_COPY);
										return;
									}
									if (responder.getLifeStats().isAlreadyDead()) {
										PacketSendUtility.sendErrorMessage(responder, ManagedErrorCode.DEAD_ERROR);
										return;
									}
									try {
										World.getInstance().canEnterMap(responder, mapId);
										responder.getMoveController().stopMoving();
									} catch (ManagedException e) {
										PacketSendUtility.sendErrorMessage(responder, e.getCode());
										return;
									}
									if (responder.getPosition().getMapId() == mapId
											&& responder.getPosition().getInstanceId() == instanceId) {
										World.getInstance().updatePosition(responder, x, y, responder.getHeading());
									} else {
										World.getInstance().setPosition(responder, mapId, instanceId, x, y,
												responder.getHeading());
									}
									responder.sendUpdatePosition();
								}
							});
					if (result) {
						PacketSendUtility.sendPacket(target, SM_Official_Mobilization.valueOf(player.getName(),
								phrases, official.getOfficial().name()));
					}
				}
			}

			PacketSendUtility.sendSignMessage(player, sign);
			player.addUseAuthorityHistory(AuthorityID.OFFICIAL_MOBILIZATION);
			player.getCountry().getCourt().addMobilizationCount(player.getObjectId());
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void getHiddenMissionInfo(Player player, int type) {
		PacketSendUtility.sendPacket(player, SM_Hidden_Mission_Info.valueOf(getHiddenMissionLeftCount(player, type)));
	}

	public int getHiddenMissionLeftCount(Player player, int type) {
		HiddenMissionType missionType = HiddenMissionType.valueOf(type);
		int leftCount = player.getPlayerCountryHistory().getHiddenMissionDailyCount().get(missionType.getValue());
		if (type == HiddenMissionType.RESCUE.getValue()) {
			leftCount -= player.getRescue().getTodayCompleteCount();
		}
		int count = ConfigValueManager.getInstance().HIDDEN_MISSION_PER_COUNT.getValue().get(missionType.name());
		return (int) Math.ceil(leftCount * 1.0 / count);
	}

	public void queryMobilization(Player player) {
		Official official = player.getCountry().getCourt().getOfficials().get(player.getObjectId());
		if (official == null) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COUNTRY_NOT_OFFICAL);
			return;
		}

		Integer alreadyUseCount = player.getCountry().getCourt().getMobilizationHistory().get(player.getObjectId());
		int dailyAlreadyUse = ((alreadyUseCount == null) ? 0 : alreadyUseCount.intValue());
		int dailyCountLimit = ConfigValueManager.getInstance().OFFICIAL_MOBILIZATION_DAILY_COUNT.getValue().get(
				official.getOfficial().name());
		PacketSendUtility.sendPacket(player, SM_Query_Mobilization.valueOf(dailyCountLimit - dailyAlreadyUse));
	}

	public void getDiplomacyDamageRank(Player player, int country) {
		Diplomacy diplomacy = CountryManager.getInstance().getCountries().get(CountryId.valueOf(country))
				.getDiplomacy();
		if (!diplomacy.getCountryNpc().getLifeStats().isAlreadyDead()) {
			throw new ManagedException(ManagedErrorCode.DIPLOMACY_IS_ALIVE);
		}
		PacketSendUtility.sendPacket(player,
				SM_Diplomacy_Damage_Rank.valueOf(diplomacy.getDamagedRank(), diplomacy.getDeadTime(), country));
	}

	public void getCountryFlagDamageRank(Player player, int country) {
		CountryFlag countryFlag = CountryManager.getInstance().getCountries().get(CountryId.valueOf(country))
				.getCountryFlag();
		boolean dead = countryFlag.getCountryNpc().getLifeStats().isAlreadyDead();
		boolean spawned = countryFlag.getCountryNpc().isSpawned();
		if ((dead && spawned) || (!dead && spawned)) {
			throw new ManagedException(ManagedErrorCode.COUNTRYFLAG_IS_ALIVE);
		}
		PacketSendUtility.sendPacket(player,
				SM_CountryFlag_Damage_Rank.valueOf(countryFlag.getDamagedRank(), countryFlag.getDeadTime(), country));
	}

	public boolean canRecieveCivilSalary(Player player) {
		CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1,
				ConfigValueManager.getInstance().COUNTRY_RECEIVE_SALARY.getValue());
		boolean condtion = conditions.verify(player, false);
		boolean civilSalary = player.getCountry().getCourt().isCivilSalary();
		boolean notReceive = !player.getCountry().getCourt().getCivilReceived().contains(player.getObjectId());
		return condtion && civilSalary && notReceive;
	}

	public boolean canRecieveOfficialSalary(Player player) {
		CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1,
				ConfigValueManager.getInstance().COUNTRY_RECEIVE_OFFICIAL_SALARY.getValue());
		boolean condition = conditions.verify(player, false);
		boolean opened = player.getCountry().getCourt().isOfficialSalary();
		boolean notRecieved = !player.getCountry().getCourt().getOfficialReceived().contains(player.getObjectId());
		boolean contains = player.getCountry().getCourt().getOfficialSalaryRecord().contains(player.getObjectId());
		return condition && opened && notRecieved && contains;
	}

	public void getCountryTraitorNum(Player player) {
		PacketSendUtility.sendPacket(player,
				SM_Country_Traitor_Num.valueOf(player.getCountry().getTraitorMapFixs().size()));
	}

	public void getCountryTraitorRank(Player player) {
		ArrayList<TraitorVO> results = new ArrayList<TraitorVO>();
		for (TraitorPlayerFix p : player.getCountry().getTraitorMapFixs().values()) {
			results.add(TraitorVO.valueOf(p));
		}
		Collections.sort(results);
		PacketSendUtility.sendPacket(player, SM_Country_Traitor_Rank.valueOf(results));
	}

	/**
	 * 成为储君
	 * 
	 * @param player
	 */
	public void becomeReserveKing(Player player) {
		player.getCountry().lockLock();
		try {
			if (player.getOperatorPool().getGmPrivilege().isGm()) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.OPERATOR_GM);
				return;
			}
			if (ServerState.getInstance().getLastGangOfWarDate() != null) {
				// 已经开启过家族战
				return;
			}
			ReserveKing reserveKing = player.getCountry().getReserveKing();
			if (reserveKing.isExistReserveKing()) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.RESERVEKING_EXIST);
				return;
			}
			if (reserveKing.getBlackList().contains(player.getObjectId())) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.IN_RESERVEKING_BLACKLIST);
				return;
			}

			CoreConditions conditions = ConfigValueManager.getInstance().getReserveKingConditions();
			if (!conditions.verify(player, true)) {
				PacketSendUtility.sendErrorMessage(player);
				return;
			}

			CurrencyAction actions = ConfigValueManager.getInstance().getReserveKingBecomeActs();
			actions.verify(player);
			actions.act(player, ModuleInfo.valueOf(ModuleType.RESERVEKING, SubModuleType.RESERVEKING_BECOME_ACT));

			reserveKing.becomeReserveKing(player);
		} finally {
			player.getCountry().unlockLock();
		}

	}

	/**
	 * 检查并退位国君
	 */
	public void checkAndAdbicateReserveKing() {
		for (Country country : countries.values()) {
			ReserveKing reserveKing = country.getReserveKing();
			boolean existReserveKing = reserveKing.isExistReserveKing();
			boolean online = reserveKing.getPlayerId() != 0L ? SessionManager.getInstance().isOnline(
					reserveKing.getPlayerId()) : false;
			boolean deprect = reserveKing.isDeprected();
			boolean needchange = reserveKing.isUnlineTimeOverLimit();
			if (!deprect && existReserveKing && !online && needchange) {
				// 储君过期下位
				reserveKing.passiveAdbicate();
			}
		}
	}

	/**
	 * 储君主动退位
	 */
	public void initiativeAdbicate(Player player, int sign) {
		if (ServerState.getInstance().getLastGangOfWarDate() != null) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		player.getCountry().lockLock();
		try {
			ReserveKing reserveKing = player.getCountry().getReserveKing();
			if (!reserveKing.isReserveKing(player.getObjectId())) {
				PacketSendUtility.sendErrorMessage(player);
				return;
			}
			reserveKing.initiativeAdbicate();
			PacketSendUtility.sendPacket(player, SM_System_Sign.valueOf(sign));
		} finally {
			player.getCountry().unlockLock();
		}

	}

	/**
	 * 储君系统失效 在家族战开启调用
	 */
	public void reserveKingEnd(CountryId countryId) {
		ReserveKing reserveKing = countries.get(countryId).getReserveKing();
		reserveKing.deprect();
	}

	/**
	 * 领取储君完成奖励
	 */
	public void rewardReserveKingTask(Player player, ReserveTaskEnum taskType) {
		ReserveKing reserveKing = player.getCountry().getReserveKing();
		if (!reserveKing.isReserveKing(player.getObjectId())) {
			throw new ManagedException(ManagedErrorCode.NOT_RESERVEKING);
		}
		reserveKing.rewardTask(taskType);
	}

	/**
	 * 获取该国家储君信息
	 * 
	 * @param player
	 * @param countryId
	 */
	public SM_ReserveKingVO getReserveKingInfo(Player player, CountryId countryId) {
		HashMap<Integer, PlayerSimpleInfo> reserkingInfos = New.hashMap();
		for (Map.Entry<CountryId, Country> entry : countries.entrySet()) {
			Player reserveKingPlayer = entry.getValue().getReserveKing().getReserveKingPlayer();
			if (entry.getValue().getReserveKing().isExistReserveKing()) {
				reserkingInfos.put(entry.getKey().getValue(), reserveKingPlayer.createSimple());
			}
		}
		long nextCallTime = player.getCountry().getReserveKing().getNextCallTime();
		return SM_ReserveKingVO.valueOf(nextCallTime, countries.get(countryId).getReserveKing(), reserkingInfos);
	}

	public void updateCountry() {
		for (Country country : getCountries().values()) {
			try {
				country.lockLock();
				country.upate();
			} finally {
				country.unlockLock();
			}
		}
	}

	/**
	 * 储君召集
	 * 
	 * @param player
	 */
	public void reserveKingCallTogether(Player player) {
		player.getCountry().lockLock();
		try {
			ReserveKing reserveKing = player.getCountry().getReserveKing();
			if (!reserveKing.isReserveKing(player.getObjectId())) {
				PacketSendUtility.sendErrorMessage(player);
				return;
			}
			if (System.currentTimeMillis() < reserveKing.getNextCallTime()) {
				// 在cd时间中
				throw new ManagedException(ManagedErrorCode.RESERVEKING_CALLTOGETHER_CD);
			}

			if (BossConfig.getInstance().isInBossHomeMap(player.getMapId())) {
				throw new ManagedException(ManagedErrorCode.BOSS_HOME_CANNOT_CALLTOGETHER);
			}

			final int x = player.getX();
			final int y = player.getY();
			final int mapId = player.getMapId();
			final int instanceId = player.getInstanceId();
			final MapResource mapResource = World.getInstance().getMapResource(mapId);
			if (player.isInCopy() && player.getMapId() != KingOfWarConfig.getInstance().MAPID.getValue()) {
				throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
			}
			CoreConditions filterConditions = conditionManager.getCoreConditions(1,
					configValueManager.COUNTRY_CALL_TOGETHER.getValue());
			player.getCountry().getReserveKing().getCallCount().set(0);
			for (final Player target : player.getCountry().getAllCountryPlayer(player)) {
				// // 如果双方都在地图中就不算召集次数
				// boolean inKingOfWarMap = (player.getMapId() ==
				// KingOfWarConfig.getInstance().MAPID.getValue() && target
				// .getMapId() ==
				// KingOfWarConfig.getInstance().MAPID.getValue());
				// ReserveKingCalltogetherCountCondition
				// reserveCalltogeterCountCondition = filterConditions
				// .findConditionType(ReserveKingCalltogetherCountCondition.class);
				// reserveCalltogeterCountCondition.setOpen(!inKingOfWarMap);
				// int limit = reserveCalltogeterCountCondition.getValue();
				// if (player.getCountry().isWeakCountry()) {
				// limit +=
				// ConfigValueManager.getInstance().WEAK_COUNTRY_CALL_COUNT.getValue();
				// }

				if (filterConditions.verify(target) && target != player) {
					boolean result = target.getRequester().putRequest(RequestHandlerType.RESERVEKING_CALLTOGETHER,
							new RequestResponseHandler(player) {
								@Override
								public boolean deprecated() {
									if (mapId == KingOfWarConfig.getInstance().MAPID.getValue()
											&& !KingOfWarManager.getInstance().isWarring()) {
										PacketSendUtility.sendErrorMessage(target,
												ManagedErrorCode.KINGOFWAR_NOT_FIGHTING);
										return true;
									}
									return false;
								}

								@Override
								public void denyRequest(Creature requester, Player responder) {
								}

								@Override
								public void acceptRequest(Creature requester, Player responder) {
									if (responder.getCountry().getReserveKing().getCallCount().incrementAndGet() > ConfigValueManager
											.getInstance().CALL_TOGETHER_COUNT_LIMIT.getValue()) {
										PacketSendUtility.sendErrorMessage(responder,
												ManagedErrorCode.CALL_TOGETHER_LIMIT);
										return;
									}
									if (responder.isInCopy()
											&& responder.getMapId() != KingOfWarConfig.getInstance().MAPID.getValue()) {
										PacketSendUtility.sendErrorMessage(responder, ManagedErrorCode.PLAYER_IN_COPY);
										return;
									}
									World.getInstance().canEnterMap(responder, mapId);

									ReserveKing reserveKing = target.getCountry().getReserveKing();
									if (reserveKing.isExistReserveKing()) {
										if (requester.getMapId() != KingOfWarConfig.getInstance().MAPID.getValue()
												|| responder.getMapId() != KingOfWarConfig.getInstance().MAPID
														.getValue()) {

											reserveKing.addCallCount(target.getObjectId());
										}
									}

									responder.getMoveController().stopMoving();
									if (responder.getPosition().getMapId() == mapId
											&& responder.getPosition().getInstanceId() == instanceId) {
										World.getInstance().updatePosition(responder, x, y, responder.getHeading());
									} else {
										World.getInstance().setPosition(responder, mapId, instanceId, x, y,
												responder.getHeading());
									}
									responder.sendUpdatePosition();

									// 储君召集国民,添加免费复活BUFF
									if (responder instanceof Player) {
										Player player = (Player) responder;
										Skill skill = SkillEngine.getInstance().getSkill(null, 20040,
												player.getObjectId(), 0, 0, player, null);
										skill.noEffectorUseSkill();
									}
									if (!mapResource.isCopy()) {
										Skill godSKill = SkillEngine.getInstance().getSkill(null,
												configValueManager.BEEN_CALLED_BUFF.getValue(),
												responder.getObjectId(), 0, 0, responder, null);
										godSKill.noEffectorUseSkill();
									}
								}
							});
					if (result) {
						PacketSendUtility.sendPacket(
								target,
								SM_ReserveKing_CallTogether.valueOf(player.getName(), mapId, x, y,
										0 - reserveKing.getCallCount(target.getObjectId())));
					}
				}
			}
			reserveKing.useCallTogether(configValueManager.RESERVEKING_CALLTOGETHER_CD.getValue());
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void initCountryFlagQuest() {
		String chooserGroupId = configValueManager.COUNTRYFLAG_QUEST_CHOOSERGROUP.getValue();
		if (serverState.getFlagSpecifiedStatus() == FlagSpecifiedSatatus.CALCULATE.getValue()) {
			chooserGroupId = configValueManager.COUNTRYFLAG_QUEST_FIRST_CHOOSERGROUP.getValue();
			serverState.firstFlagQuestAfterCountryPowerCalc();
		}
		List<String> randomResult = chooserManager.chooseValueByRequire(null, chooserGroupId);
		String result = randomResult.get(0).trim();
		// 123,23-1
		if (result.contains("-")) { // 2v1模式
			CountryId allianceFirstCountry = CountryId.valueOf(Integer.valueOf(result.substring(0, 1)));
			CountryId allianceSecondCountry = CountryId.valueOf(Integer.valueOf(result.substring(1, 2)));
			CountryId defend = CountryId.valueOf(Integer.valueOf(result.substring(3)));

			getCountries().get(allianceFirstCountry).initCountryFlagQuest(CountryFlagQuestType.ATTACK_WITH_ALLIANCE,
					defend.getValue(), allianceSecondCountry.getValue(), Collections.<Integer> emptyList());
			getCountries().get(allianceSecondCountry).initCountryFlagQuest(CountryFlagQuestType.ATTACK_WITH_ALLIANCE,
					defend.getValue(), allianceFirstCountry.getValue(), Collections.<Integer> emptyList());
			getCountries().get(defend).initCountryFlagQuest(CountryFlagQuestType.DEFENCE, 0, 0,
					Arrays.asList(allianceFirstCountry.getValue(), allianceSecondCountry.getValue()));
		} else { // 1v1v1 模式
			CountryId firstCountry = CountryId.valueOf(Integer.valueOf(result.substring(0, 1)));
			CountryId secondCountry = CountryId.valueOf(Integer.valueOf(result.substring(1, 2)));
			CountryId thirdCountry = CountryId.valueOf(Integer.valueOf(result.substring(2)));

			getCountries().get(firstCountry).initCountryFlagQuest(CountryFlagQuestType.ATTACK,
					secondCountry.getValue(), 0, Arrays.asList(thirdCountry.getValue()));
			getCountries().get(secondCountry).initCountryFlagQuest(CountryFlagQuestType.ATTACK,
					thirdCountry.getValue(), 0, Arrays.asList(firstCountry.getValue()));
			getCountries().get(thirdCountry).initCountryFlagQuest(CountryFlagQuestType.ATTACK, firstCountry.getValue(),
					0, Arrays.asList(secondCountry.getValue()));
		}
	}

	public void getFlagQuestInfo(Player player) {
		MapResource resource = World.getInstance().getMapResource(player.getMapId());
		if (resource.getCountry() == 0) {
			return;
		}
		CountryFlag flag = CountryManager.getInstance().getCountryByValue(resource.getCountry()).getCountryFlag();
		CountryNpc flagNpc = flag.getCountryNpc();
		if (flagNpc == null) {
			return;
		}
		Map<Integer, Integer> map = New.hashMap();
		int c1Count = 0, c2Count = 0, c3Count = 0;
		int width = ConfigValueManager.getInstance().ATTEND_REWARD_SCOPE_WIDTH.getValue();
		int height = ConfigValueManager.getInstance().ATTEND_REWARD_SCOPE_HEIGHT.getValue();

		Iterator<Player> ite = World.getInstance().getWorldMap(flagNpc.getMapId()).getInstances()
				.get(flagNpc.getInstanceId()).playerIterator();
		while (ite.hasNext()) {
			Player p = ite.next();
			if (MathUtil.isInRange(flagNpc, p, width, height)) {
				int countryValue = p.getCountryValue();
				if (countryValue == 1) {
					c1Count++;
				} else if (countryValue == 2) {
					c2Count++;
				} else {
					c3Count++;
				}
			}
		}
		map.put(1, c1Count);
		map.put(2, c2Count);
		map.put(3, c3Count);
		PacketSendUtility.sendPacket(
				player,
				SM_Flag_Quest_Info.valueOf(map, flag.getCountryNpc().getLifeStats().getCurrentHp(), flag
						.getCountryNpc().getLifeStats().getMaxHp()));
	}

	public void startCountryFlag() {
		if (!serverState.isOpenServer()) {
			return;
		}
		long flagSpawnTime = getOpenServerFlagSpawnTime(serverState.getOpenServerDate());
		long delay = flagSpawnTime - System.currentTimeMillis();
		for (Country country : countries.values()) {
			country.getCountryFlag().setReliveTime(flagSpawnTime);
			if (delay < 0) {
				country.getCountryFlag().spawnBombstone(ConfigValueManager.getInstance().getNextCountryFlagStartTime());
			} else {
				country.getCountryFlag().spawnBombstone(flagSpawnTime);
			}
		}
	}

	public void startDiplomacy() {
		if (!serverState.isOpenServer()) {
			return;
		}
		long diplomacySpawnTime = getOpenServerDiplomacySpawnTime(serverState.getOpenServerDate());
		long delay = diplomacySpawnTime - System.currentTimeMillis();
		for (Country country : countries.values()) {
			country.getDiplomacy().setReliveTime(diplomacySpawnTime);
			if (delay < 0) {
				country.getDiplomacy().spawnBombstone(
						ConfigValueManager.getInstance().getNextCountryDiplomacyStartTime());
			} else {
				country.getDiplomacy().spawnBombstone(diplomacySpawnTime);
			}
		}
	}

	public long getOpenServerFlagSpawnTime(Date openDate) {
		Date startSpawnFlagTime = new Date(openDate.getTime()
				+ configValueManager.OPENSERVER_COUNTRYFLAG_SPAWN.getValue() * DateUtils.MILLIS_PER_HOUR);
		return configValueManager.getNextCountryFlagStartTime(startSpawnFlagTime);
	}

	public long getOpenServerDiplomacySpawnTime(Date openDate) {
		Date startSpawnDiplomacyTime = new Date(openDate.getTime()
				+ configValueManager.OPENSERVER_DIPLOMACY_SPAWN.getValue() * DateUtils.MILLIS_PER_HOUR);
		return configValueManager.getNextCountryDiplomacyStartTime(startSpawnDiplomacyTime);
	}

	public void refreshCountryQuest() {
		for (Country country : countries.values()) {
			country.getCountryQuest().refresh();
		}
	}

	public int getMaxBuildValueAmongCountries() {
		int buildValueMax = 0;
		for (Country country : countries.values()) {
			if (country.getNewTechnology().getBuildValue() > buildValueMax) {
				buildValueMax = country.getNewTechnology().getBuildValue();
			}
		}
		return buildValueMax;
	}

	public int getMinBuildValueAmongCountries() {
		int buildValueMin = 0;
		for (Country country : countries.values()) {
			if (country.getNewTechnology().getBuildValue() < buildValueMin) {
				buildValueMin = country.getNewTechnology().getBuildValue();
			}
		}
		return buildValueMin;
	}

	/**
	 * 捐献建设令
	 * 
	 * @param player
	 */
	public void contributeBuildItem(Player player) {
		if (!ModuleOpenManager.getInstance().isOpenByModuleKey(player, ModuleKey.COUNTRYTECHNOLOGY)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		String itemId = ConfigValueManager.getInstance().COUNTRY_TECHNOLOGY_BUILD_ITEM_ID.getValue();
		long count = player.getPack().getItemSizeByKey(itemId);
		ItemAction itemAction = CoreActionType.createItemCondition(itemId, (int) count);
		if (!itemAction.verify(player)) {
			throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_ITEM);
		}

		itemAction.act(
				player,
				ModuleInfo.valueOf(ModuleType.COUNTRY, SubModuleType.COUNTRY_TECHNOLOGY_DONATE_ACT, itemId + ":"
						+ count));

		String rewardId = ConfigValueManager.getInstance().COUNTRY_TECHNOLOGY_DONATE_REWARDID.getValue();
		Reward reward = Reward.valueOf();
		for (int i = 0; i < count; i++) {
			int min = ConfigValueManager.getInstance().COUNTRY_TACHNOLOGY_BASIC_RANDOM.getValue()[0];
			int max = ConfigValueManager.getInstance().COUNTRY_TACHNOLOGY_BASIC_RANDOM.getValue()[1];
			int basic = RandomUtils.nextInt(max - min + 1) + min;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("BASIC_VALUE", basic);
			params.put("HIGEST_COUNTRY_BUILDVALUE", getMaxBuildValueAmongCountries());
			params.put("CURRENT_COUNTRY_BUILDVALUE", player.getCountry().getNewTechnology().getBuildValue());
			Reward aReward = RewardManager.getInstance().creatReward(player, rewardId, params);
			reward.addReward(aReward);
		}
		RewardManager.getInstance().grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.COUNTRY, SubModuleType.COUNTRY_TECHNOLOGY_DONATE_REWARD));
	}

	public void increaseBuildValue(Player player, int value) {
		player.getCountry().lockLock();
		try {
			player.getCountry().getNewTechnology().addBuildValue(player, value);
		} finally {
			player.getCountry().unlockLock();
		}
	}

	/**
	 * 放置国旗
	 * 
	 * @param player
	 */
	public void spawnCountryTechnologyFlag(Player player) {
		player.getCountry().lockLock();
		try {
			if (TownConfig.getInstance().isInTownsCopyMap(player.getMapId())) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}

			if (!ConfigValueManager.getInstance().getTechnologyFlagPlaceConds().verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}

			if (player.getCountry().getNewTechnology().getFlagCount() <= 0) {
				throw new ManagedException(ManagedErrorCode.COUNTRY_TECHNOLOGY_NOT_ENOUGH_FLAG_COUNT);
			}

			player.getCountry().authority(player, AuthorityID.PLACE_ARMY_FLAG);
			// 刷新数据
			refreshFlagCount(player.getCountry());

			String chooserId = ConfigValueManager.getInstance().COUNTRY_TECHNOLOGY_CHOOSER_GROUPID.getValue();
			String spawnId = ChooserManager.getInstance().chooseValueByRequire(player, chooserId).get(0);

			CountryNpc flagObject = (CountryNpc) SpawnManager.getInstance().creatObject(spawnId, 1);
			flagObject.getPosition().setMapId(player.getMapId());
			flagObject.getPosition().setXY(player.getPosition());
			CountryArmyFlagController controller = new CountryArmyFlagController();
			controller.setOwner(flagObject);
			flagObject.setController(controller);

			player.getCountry().getNewTechnology().placeFlag(player.getObjectId());
			spawnManager.bringIntoWorld(flagObject, 1);

			player.getCountry().sendPacketAll(AuthorityID.PLACE_ARMY_FLAG,
					SM_Country_Technology_Flag_Change.valueOf(player.getCountry().getNewTechnology()));
			PacketSendUtility.sendPacket(player, new SM_Country_Technology_PlaceFlag());
			Skill skill = SkillEngine.getInstance().getSkill(null,
					ConfigValueManager.getInstance().COUNTRY_TECHNOLODY_FLAG_SKILLID.getValue(),
					flagObject.getObjectId(), 0, 0, flagObject, null);
			skill.noEffectorUseSkill();
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public boolean refreshFlagCount(Country country) {
		country.lockLock();
		try {
			return country.getNewTechnology().refreshFlagCount();
		} finally {
			country.unlockLock();
		}
	}

	/**
	 * 官员请求
	 * 
	 * @param player
	 */
	public void refreshTechnologyFlagCount(Player player) {
		player.getCountry().lockLock();
		try {
			player.getCountry().authority(player, AuthorityID.PLACE_ARMY_FLAG);
			NewTechnology technology = player.getCountry().getNewTechnology();
			refreshFlagCount(player.getCountry());
			PacketSendUtility.sendPacket(player, SM_Country_Technology_Flag_Change.valueOf(technology));
		} finally {
			player.getCountry().unlockLock();
		}
	}

	public void refreshTechnologyFlagCount(boolean sended) {
		for (Country country : countries.values()) {
			boolean refreshed = refreshFlagCount(country);
			if (refreshed && sended) {
				if (!ClearAndMigrate.clear) {
					country.sendPacketAll(AuthorityID.PLACE_ARMY_FLAG,
							SM_Country_Technology_Flag_Change.valueOf(country.getNewTechnology()));
				}
			}
		}

	}

	public void countryBuildValueFix() {
		for (Country country : countries.values()) {
			country.lockLock();
			try {
				Date openDate = ServerState.getInstance().getOpenServerDate();
				if (openDate == null) {
					return;
				}
				Date openTimeFirstTime = DateUtils.getFirstTime(openDate);

				int days = ConfigValueManager.getInstance().COUNTRY_TECH_BUILDVALUE_CHECK_DAYS.getValue();
				Date targetDate = DateUtils.addDays(openTimeFirstTime, days - 1);
				targetDate = DateUtils.addHours(targetDate, 1);
				boolean buildValueFixVerify = System.currentTimeMillis() < targetDate.getTime();
				if (buildValueFixVerify) {
					continue;
				}
				int intervalHours = (int) ((System.currentTimeMillis() - targetDate.getTime()) / DateUtils.MILLIS_PER_HOUR);
				intervalHours++;
				CountryBuildValueFixResource resource = ConfigValueManager.getInstance().countryBuildValueStorage.get(
						intervalHours, false);
				int standValue;
				if (resource == null) {
					standValue = configValueManager.COUNTRY_TECH_MAX_STAND_BUILDVALUE.getValue();
				} else {
					standValue = resource.getStandValue();
				}
				if (country.getNewTechnology().getBuildValue() < standValue) {
					country.getNewTechnology().fixBuildValue(standValue);
				}
			} finally {
				country.unlockLock();
			}
		}
	}
}