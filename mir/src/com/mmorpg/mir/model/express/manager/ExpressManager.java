package com.mmorpg.mir.model.express.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActionType;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.action.CurrencyAction;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.country.model.Official;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.express.model.LorryNavigatorVO;
import com.mmorpg.mir.model.express.packet.SM_Express_Reset;
import com.mmorpg.mir.model.express.packet.SM_Express_Reward;
import com.mmorpg.mir.model.express.packet.SM_Express_Start;
import com.mmorpg.mir.model.express.packet.SM_Get_Current_Express;
import com.mmorpg.mir.model.express.packet.SM_Lorry_AttackTime;
import com.mmorpg.mir.model.express.packet.SM_Lorry_MapInfo;
import com.mmorpg.mir.model.express.resource.ExpressResource;
import com.mmorpg.mir.model.express.resource.ExpressResource.LorryColor;
import com.mmorpg.mir.model.gameobjects.Lorry;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.model.QuestPhase;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.system.packet.SM_System_Sign;
import com.mmorpg.mir.model.transport.manager.PlayerChatTransportManager;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.welfare.event.ExpressEvent;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldPosition;
import com.mmorpg.mir.model.world.resource.MapCountry;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.collection.ConcurrentHashSet;

@Component
public class ExpressManager implements IExpressManager {

	private static Logger logger = Logger.getLogger(ExpressManager.class);
	@Static("EXPRESS:EXPRESS_FIAL_TIME")
	private ConfigValue<Integer> EXPRESS_FAIL_TIME;

	@Static("EXPRESS:EXPRESS_FARAWAY_FIAL_TIME")
	public ConfigValue<Integer> EXPRESS_FARAWAY_FIAL_TIME;

	@Static("EXPRESS:EXPRESS_CONDITIONS")
	private ConfigValue<String[]> expressConditions;

	@Static("EXPRESS:EXPRESS_NORMAL_RESET_ACTIONS")
	private ConfigValue<String> resetLorryNormalActionIds;

	@Static("EXPRESS:EXPRESS_GOLD_RESET_ACTIONS")
	private ConfigValue<String[]> resetLorryGoldActionIds;

	@Static("EXPRESS:EXPRESS_OUT_OF_ONATTACK")
	private ConfigValue<Integer> EXPRESS_OUT_OF_ONATTACK;

	@Static("EXPRESS:EXPRESS_RESET_SELECT_CONDITIONS")
	private ConfigValue<String[]> EXPRESS_RESET_SELECT_CONDITIONS;

	@Static("EXPRESS:EXPRESS_REFRESH_CHOOSERGROUP_GOLD")
	private ConfigValue<String> EXPRESS_REFRESH_CHOOSERGROUP_GOLD;

	@Static("EXPRESS:EXPRESS_REFRESH_CHOOSERGROUP")
	private ConfigValue<String> EXPRESS_REFRESH_CHOOSERGROUP;

	@Static("EXPRESS:NOT_SEE_RANGE")
	private ConfigValue<Integer> NOT_SEE_RANGE;

	@Static("EXPRESS:ROB_REWARD_COUNT_LIMIT")
	private ConfigValue<Integer> ROB_REWARD_COUNT_LIMIT;

	@Static("EXPRESS:ROB_REWARD_PERCENT")
	private ConfigValue<Double> ROB_REWARD_PERCENT;

	@Static("PUBLIC:REWARD_SHARE_TYPE")
	public ConfigValue<RewardType[]> ROB_SHARE_TYPE;

	@Static("EXPRESS:COE_CHOOSER")
	public ConfigValue<String> COE_CHOOSER;

	@Static("EXPRESS:LORRY_COLOR_NAME")
	public ConfigValue<String[]> LORRY_COLOR_NAME;

	@Static("EXPRESS:COLOR_FACTOR")
	private ConfigValue<Integer[]> COLOR_FACTOR;

	@Static("EXPRESS:NAVIGATOR_MAX_SIZE_PER_LINE")
	public ConfigValue<Integer> NAVIGATOR_MAX_SIZE_PER_LINE;

	@Static("EXPRESS:STRATEGYESCOR")
	public ConfigValue<Map<String, Map<String, Integer>>> STRATEGYESCOR;

	@Static("EXPRESS:ROB_FLY_ACT")
	public ConfigValue<String[]> ROB_FLY_ACT;

	@Static("EXPRESS:ROB_FLY_COND")
	public ConfigValue<String[]> ROB_FLY_COND;

	@Static("EXPRESS:SELECT_NEXT_CHOOSER")
	public ConfigValue<String[]> SELECT_NEXT_CHOOSER;

	@Static("EXPRESS:BRANCH_EXPRESS_MISSION_EFFECT")
	public ConfigValue<String[]> BRANCH_EXPRESS_MISSION_EFFECT;

	/** 劫镖者奖励邮件标题i18n */
	@Static("EXPRESS:ROB_LORRY_REWARD_MAIL_TITLE")
	public ConfigValue<String> ROB_LORRY_REWARD_MAIL_TITLE;

	/** 劫镖者奖励邮件内容i18n */
	@Static("EXPRESS:ROB_LORRY_REWARD_MAIL_CONTENT")
	public ConfigValue<String> ROB_LORRY_REWARD_MAIL_CONTENT;

	@Static("EXPRESS:ROB_LORRY_REWARD_MAIL_TITLE_TECH")
	public ConfigValue<String> ROB_LORRY_REWARD_MAIL_TITLE_TECH;

	@Static("EXPRESS:ROB_LORRY_REWARD_MAIL_CONTENT_TECH")
	public ConfigValue<String> ROB_LORRY_REWARD_MAIL_CONTENT_TECH;

	/** 弱国被劫镖车补偿条件 */
	@Static("EXPRESS:ROB_LORRY_COMPENSATE_CONDITION_ID")
	private ConfigValue<String[]> ROB_LORRY_COMPENSATE_CONDITION_ID;

	/** 弱国被劫镖车奖励邮件标题 */
	@Static("EXPRESS:COMPENSATE_REWARD_MAIL_TITLE_I18N")
	private ConfigValue<String> COMPENSATE_REWARD_MAIL_TITLE_I18N;

	/** 弱国被劫镖车奖励邮件内容 */
	@Static("EXPRESS:COMPENSATE_REWARD_MAIL_CONTENT_I18N")
	private ConfigValue<String> COMPENSATE_REWARD_MAIL_CONTENT_I18N;

	/** 劫镖补偿邮件默认署名 */
	@Static("EXPRESS:COMPENSATE_MAIL_DEFAULT_SIGN")
	private ConfigValue<String> COMPENSATE_REWARD_MAIL_DEFAULT_SIGN;

	@Static("EXPRESS:NORMAL_GOLD_ACTIONIDS")
	private ConfigValue<String[]> NORMAL_GOLD_ACTIONIDS;

	@Static("EXPRESS:ROB_LORRY_REWARD_CHOOSERGROUP")
	private ConfigValue<String> ROB_LORRY_REWARD_CHOOSERGROUP;

	@Static("EXPRESS:ROB_TECH_EXTRA_COND")
	public ConfigValue<String[]> ROB_TECH_EXTRA_COND;

	@Static("EXPRESS:ROB_TECH_EXTRA_VALUE")
	public ConfigValue<Double> ROB_TECH_EXTRA_VALUE;

	@Static("EXPRESS:ROB_TECH_EXTRA_DAMAGE_RATE")
	public ConfigValue<Double> ROB_TECH_EXTRA_DAMAGE_RATE;

	private CoreConditions robExtraRewardTech;

	public CoreConditions getRobTechCondition() {
		if (robExtraRewardTech == null) {
			robExtraRewardTech = CoreConditionManager.getInstance()
					.getCoreConditions(1, ROB_TECH_EXTRA_COND.getValue());
		}
		return robExtraRewardTech;
	}

	public double getExtraDamage() {
		return ROB_TECH_EXTRA_DAMAGE_RATE.getValue();
	}

	/** <color, lorry> */
	private Map<Integer, Set<Lorry>> currentLorryCollection;

	/** <countryValue, lorry> */
	private Map<Integer, Map<Long, LorryNavigatorVO>> showResultCollection;

	@Autowired
	private CoreConditionManager conditionManager;
	@Autowired
	private CoreActionManager actionManager;
	@Autowired
	private PlayerChatTransportManager playerChatTransportManager;

	@Static
	private Storage<String, ExpressResource> expressResources;

	@Autowired
	private RewardManager rewardManager;

	private static ExpressManager instance;

	public static ExpressManager getInstance() {
		return instance;
	}

	@PostConstruct
	void init() {
		instance = this;
		currentLorryCollection = new ConcurrentHashMap<Integer, Set<Lorry>>();
		for (LorryColor color : LorryColor.values()) {
			currentLorryCollection.put(color.getValue(), new ConcurrentHashSet<Lorry>());
		}

		showResultCollection = new ConcurrentHashMap<Integer, Map<Long, LorryNavigatorVO>>();
		for (CountryId id : CountryId.values()) {
			showResultCollection.put(id.getValue(), new ConcurrentHashMap<Long, LorryNavigatorVO>());
		}

		ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new Runnable() {
			@Override
			public void run() {

				Map<Integer, Map<Integer, Integer>> translate = new HashMap<Integer, Map<Integer, Integer>>();
				for (CountryId id : CountryId.values()) {
					showResultCollection.get(id.getValue()).clear();
					Map<String, Integer> distributeMapIds = STRATEGYESCOR.getValue().get(id.getValue() + "");
					Map<Integer, Integer> map = new HashMap<Integer, Integer>();
					for (Entry<String, Integer> entry : distributeMapIds.entrySet()) {
						map.put(Integer.valueOf(entry.getKey()), entry.getValue());
					}
					translate.put(id.getValue(), map);
				}

				for (int colorIndex = LorryColor.ORANGE.getValue() - 1; colorIndex >= LorryColor.WHITE.getValue(); colorIndex--) {
					Set<Lorry> set = currentLorryCollection.get(colorIndex);
					for (Lorry lorry : set) {
						int countryValue = lorry.getCountryValue();

						int left = translate.get(countryValue).get(lorry.getMapId());
						if (left > 0) {
							showResultCollection.get(countryValue).put(lorry.getOwner().getObjectId(),
									LorryNavigatorVO.valueOf(lorry));
							translate.get(countryValue).put(lorry.getMapId(), left - 1);
						}
					}
				}
			}
		}, 500, 1000);
	}

	public void registerLorry(Lorry lorry) {
		currentLorryCollection.get(lorry.getColor()).add(lorry);
	}

	public void unRegisterLorry(Lorry lorry) {
		currentLorryCollection.get(lorry.getColor()).remove(lorry);
	}

	public Double getRobRewardPercent() {
		return ROB_REWARD_PERCENT.getValue();
	}

	public void resetSelect(Player player, boolean gold, boolean autoBuy) {
		if (player.getExpress().hadLorry()) {
			throw new ManagedException(ManagedErrorCode.HAD_EXPRESS);
		}
		if (player.getExpress().hasGodLorry()) {
			throw new ManagedException(ManagedErrorCode.HAS_GOD_LORRY);
		}

		if (player.getExpress().hasGodLorry()) {
			throw new ManagedException(ManagedErrorCode.HAS_GOD_LORRY);
		}

		if (gold) {
			if (!player.getVip().getResource().isGodExpress()) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.VIP_CONDITION_NOT_SATISFY);
				return;
			}
		}

		CoreConditions conditions = conditionManager.getCoreConditions(1, EXPRESS_RESET_SELECT_CONDITIONS.getValue());
		if (!conditions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		// 送一个
		if (player.getExpress().getSelectLorrys() != null) {
			CoreActions actions = null;
			if (gold) {
				actions = actionManager.getCoreActions(1, resetLorryGoldActionIds.getValue());
			} else if (autoBuy) {
				actions = actionManager.getCoreActions(1, resetLorryNormalActionIds.getValue());
				if (!actions.verify(player, false)) {
					actions = actionManager.getCoreActions(1, NORMAL_GOLD_ACTIONIDS.getValue());
				}
			} else {
				actions = actionManager.getCoreActions(1, resetLorryNormalActionIds.getValue());
			}
			if (!actions.verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}
			actions.act(player, ModuleInfo.valueOf(ModuleType.EXPRESS, SubModuleType.REFRESH_EXPRESS_ACT));
		}

		List<String> result = selectLorry(player, gold, player.getExpress().getSelectLorrys() == null);
		player.getExpress().setSelectLorrys(result.get(0));
		PacketSendUtility.sendPacket(player, SM_Express_Reset.valueOf(player.getExpress().getSelectLorrys()));

		String id = result.get(0);
		if (expressResources.get(id, true).getColor() == ExpressResource.LorryColor.PURPLE.getValue()) {
			I18nUtils utils = I18nUtils.valueOf("30103");
			utils.addParm("name", I18nPack.valueOf(player.getName()));
			utils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			ChatManager.getInstance().sendSystem(11001, utils, null);
		}
	}

	public List<String> selectLorry(Player player, boolean gold, boolean initial) {
		if (gold) {
			return ChooserManager.getInstance().chooseValueByRequire(player,
					EXPRESS_REFRESH_CHOOSERGROUP_GOLD.getValue());
		} else if (initial) {
			return ChooserManager.getInstance().chooseValueByRequire(player, EXPRESS_REFRESH_CHOOSERGROUP.getValue());
		} else {
			List<String> result = ChooserManager.getInstance().chooseValueByRequire(player,
					EXPRESS_REFRESH_CHOOSERGROUP.getValue());
			String resourceId = result.get(0);
			int currentColorIndex = player.getExpress().getNextSelectIndex();
			String newResourceId = null;
			if (isGuideRefresh(player)) {
				newResourceId = getExpressResource(resourceId).getCurrentIndex(currentColorIndex + 1);
			} else {
				List<String> nextResult = ChooserManager.getInstance().chooser(
						SELECT_NEXT_CHOOSER.getValue()[currentColorIndex - 1]);
				Integer colorIndex = Integer.parseInt(nextResult.get(0));
				if (colorIndex.intValue() == LorryColor.WHITE.getValue()) {
					player.getExpress().restCurrentSelectIndex();
				}
				newResourceId = getExpressResource(resourceId).getCurrentIndex(colorIndex);
			}
			result.clear();
			result.add(newResourceId);
			return result;
		}
	}

	private boolean isGuideRefresh(Player player) {
		for (String guildMissionId : BRANCH_EXPRESS_MISSION_EFFECT.getValue()) {
			Quest quest = player.getQuestPool().getQuests().get(guildMissionId);
			if (quest != null && quest.getPhase() == QuestPhase.INCOMPLETE) {
				return true;
			}
		}
		return false;
	}

	/** 发送弱国补偿邮件 */
	private void sendCompensateMail(Player player, Reward robReward) {
		CoreConditions condition = CoreConditionManager.getInstance().getCoreConditions(1,
				ROB_LORRY_COMPENSATE_CONDITION_ID.getValue());
		if (condition.verify(player)) {
			// 满足弱国条件
			I18nUtils titel18n = I18nUtils.valueOf(COMPENSATE_REWARD_MAIL_TITLE_I18N.getValue());
			I18nUtils contextl18n = I18nUtils.valueOf(COMPENSATE_REWARD_MAIL_CONTENT_I18N.getValue());
			contextl18n.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
			contextl18n.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));

			Official kingOffical = player.getCountry().getCourt().getKing();
			String sign = COMPENSATE_REWARD_MAIL_DEFAULT_SIGN.getValue();
			if (kingOffical != null) {
				long kingId = kingOffical.getPlayerId();
				Player king = PlayerManager.getInstance().getPlayer(kingId);
				sign = king.getName();
			}
			contextl18n.addParm("sign", I18nPack.valueOf(sign));
			Mail mail = Mail.valueOf(titel18n, contextl18n, null, robReward);
			MailManager.getInstance().sendMail(mail, player.getObjectId());
		}
	}

	public void express(final Player player, final String id, int sign) {
		CoreConditions conditions = conditionManager.getCoreConditions(1, expressConditions.getValue());
		if (player.getExpress().hadLorry()) {
			throw new ManagedException(ManagedErrorCode.HAD_EXPRESS);
		}
		if (player.getExpress().getSelectLorrys() == null || !player.getExpress().getSelectLorrys().contains(id)) {
			throw new ManagedException(ManagedErrorCode.NOT_FOUND_LORRY);
		}
		if (!conditions.verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		final ExpressResource resource = getExpressResources().get(id, true);
		if (!resource.getExpressCondition().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		CurrencyAction action = CoreActionType.createCurrencyCondition(
				CurrencyType.valueOf(resource.getGuaranteeType()), resource.getGuaranteeMoney());
		action.verify(player);
		action.act(player, ModuleInfo.valueOf(ModuleType.EXPRESS, SubModuleType.EXPRESS_ACT));

		// 生成镖车
		final Lorry lorry = (Lorry) SpawnManager.getInstance().spawnObject(resource.getLorrySpawnId(),
				player.getInstanceId(), player, resource.getColor());
		lorry.setExpressId(id);
		lorry.setCountry(MapCountry.valueOf(player.getCountryValue()));
		lorry.getObserveController().addObserver(new ActionObserver(ObserverType.ROUTEOVER) {
			@Override
			public void routeOver() {
				try {
					// 发奖
					Reward reward = lorry.getOwner().getExpress().getReward();
					reward = createExpressReward(lorry, resource.getChooserId());
					if (lorry.isRob()) {
						Reward robReward = reward.divideIntoTwoPieces(getRobRewardPercent(), ROB_SHARE_TYPE.getValue());
						sendCompensateMail(player, robReward);
					}
					reward.addCurrency(CurrencyType.valueOf(resource.getGuaranteeType()), resource.getGuaranteeMoney());
					lorry.getOwner().getExpress().setReward(reward);
					PacketSendUtility.sendPacket(lorry.getOwner(), SM_Express_Reward.valueOf(reward, lorry.isRob()));
					lorry.stop();
					player.getExpress().addLorryCompleteHistoryCount();
					player.getExpress().addLorryColorCompleteCount(lorry.getColor());
					if (resource.isAddLorryCount()) {
						// 剧情副本的镖车不算次数
						player.getExpress().addLorryCount();
					}
					EventBusManager.getInstance().submit(ExpressEvent.valueOf(player, id));
					unRegisterLorry(lorry);
					player.getExpress().setBeenNotifyFail(true);

					LogManager.express(player.getObjectId(), player.getPlayerEnt().getServer(), player.getPlayerEnt()
							.getAccountName(), player.getName(), System.currentTimeMillis(), lorry.getExpressId(),
							player.getExpress().getLorryCount(), player.getExpress().getLorryCompleteHistoryCount(), 0);
				} catch (Exception e) {
					logger.error("镖车发奖报错！", e);
				}

			}
		});
		if (lorry.isGod()) {
			I18nUtils i18n = I18nUtils.valueOf("30101")
					.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()))
					.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
			ChatManager.getInstance().sendSystem(11001, i18n, null);
			// I18nUtils utils = I18nUtils.valueOf("303005", i18n);
			// ChatManager.getInstance().sendSystem(0, utils, null);
		} else if (lorry.getColor() == ExpressResource.LorryColor.PURPLE.getValue()) {
			I18nUtils i18n = I18nUtils.valueOf("30104")
					.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()))
					.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
			ChatManager.getInstance().sendSystem(11001, i18n, null);
			// I18nUtils utils = I18nUtils.valueOf("303006", i18n);
			// ChatManager.getInstance().sendSystem(0, utils, null);
		}

		// 推送镖车地图信息
		lorry.setMapInfoFuture(ThreadPoolManager.getInstance().scheduleAtFixedRate((new Runnable() {
			@Override
			public void run() {
				if (player.getExpress().getCurrentLorry() != null
						&& SessionManager.getInstance().isOnline(player.getObjectId())) {
					if (!MathUtil.isInRange(player.getX(), player.getY(), lorry.getX(), lorry.getY(),
							NOT_SEE_RANGE.getValue(), NOT_SEE_RANGE.getValue())) {
						lorry.faraway(false);
					}
					PacketSendUtility.sendPacket(player,
							SM_Lorry_MapInfo.valueOf(player.getExpress().getCurrentLorry()));
				}
			}
		}), 100, 5000L));

		// 超时
		Future<?> failTimeFuture = ThreadPoolManager.getInstance().schedule(new Runnable() {
			public void run() {
				player.getExpress().setSelectLorrys(null);
				lorry.fail();
				player.getExpress().setReward(null);
			}
		}, EXPRESS_FAIL_TIME.getValue());
		lorry.getFutures().add(failTimeFuture);
		player.getExpress().setCurrentLorry(lorry);
		player.getExpress().restCurrentSelectIndex();
		PacketSendUtility.sendPacket(player, SM_Express_Start.valueOf(sign, player.getExpress().createLorryVO()));
		// navigator the lorry position
		registerLorry(lorry);

	}

	public void expressCopy(final Player player, final String id) {
		final ExpressResource resource = getExpressResources().get(id, true);
		// 生成镖车
		final Lorry lorry = (Lorry) SpawnManager.getInstance().spawnObject(resource.getLorrySpawnId(),
				player.getInstanceId(), player, resource.getColor());
		lorry.setExpressId(id);
		lorry.getObserveController().addObserver(new ActionObserver(ObserverType.ROUTEOVER) {
			@Override
			public void routeOver() {
				try {
					// 发奖
					Reward reward = lorry.getOwner().getExpress().getReward();
					reward = createExpressReward(lorry, resource.getChooserId());
					if (lorry.isRob()) {
						Reward robReward = reward.divideIntoTwoPieces(getRobRewardPercent(), ROB_SHARE_TYPE.getValue());
						sendCompensateMail(player, robReward);
					}
					reward.addCurrency(CurrencyType.valueOf(resource.getGuaranteeType()), resource.getGuaranteeMoney());
					lorry.getOwner().getExpress().setReward(reward);
					PacketSendUtility.sendPacket(lorry.getOwner(), SM_Express_Reward.valueOf(reward, lorry.isRob()));
					lorry.stop();
					EventBusManager.getInstance().submit(ExpressEvent.valueOf(player, id));
					player.getExpress().addLorryColorCompleteCount(lorry.getColor());
				} catch (Exception e) {
					logger.error("镖车发奖报错！", e);
				}
			}
		});

		// 推送镖车地图信息
		lorry.setMapInfoFuture(ThreadPoolManager.getInstance().scheduleAtFixedRate((new Runnable() {
			@Override
			public void run() {
				if (player.getExpress().getCurrentLorry() != null
						&& SessionManager.getInstance().isOnline(player.getObjectId())) {
					if (!MathUtil.isInRange(player.getX(), player.getY(), lorry.getX(), lorry.getY(),
							NOT_SEE_RANGE.getValue(), NOT_SEE_RANGE.getValue())) {
						lorry.faraway(false);
					}
					PacketSendUtility.sendPacket(player,
							SM_Lorry_MapInfo.valueOf(player.getExpress().getCurrentLorry()));
				}
			}
		}), 100, 5000L));

		// 超时
		Future<?> failTimeFuture = ThreadPoolManager.getInstance().schedule(new Runnable() {
			public void run() {
				player.getExpress().setSelectLorrys(null);
				lorry.fail();
				player.getExpress().setReward(null);
			}
		}, EXPRESS_FAIL_TIME.getValue());
		lorry.getFutures().add(failTimeFuture);
		player.getExpress().setCurrentLorry(lorry);
		PacketSendUtility.sendPacket(player, SM_Express_Start.valueOf(0, player.getExpress().createLorryVO()));
	}

	public void reward(Player player, int sign) {
		Reward reward = player.getExpress().getReward();
		if (reward == null) {
			throw new ManagedException(ManagedErrorCode.NOT_FOUND_EXPRESS_REWARD);
		}

		rewardManager.grantReward(player, reward, ModuleInfo.valueOf(ModuleType.EXPRESS, SubModuleType.EXPRESS_REWARD));
		player.getExpress().setBeenRob(false);
		player.getExpress().setReward(null);
	}

	public void getRecentOnAttackTime(Player player) {
		if (player.getExpress().getCurrentLorry() != null) {
			PacketSendUtility.sendPacket(player,
					SM_Lorry_AttackTime.valueOf(player.getExpress().getCurrentLorry().getLastOnAttackTime()));
		}
	}

	public void flyToLorry(Player player, int sign) {
		Lorry lorry = player.getExpress().getCurrentLorry();
		if (player.getLifeStats().isAlreadyDead()) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}
		if (lorry == null) {
			throw new ManagedException(ManagedErrorCode.NOT_FOUND_LORRY);
		}
		World.getInstance().canEnterMap(player, lorry.getMapId());
		if (World.getInstance().getWorldMap(lorry.getMapId()).isBlock(lorry.getX(), lorry.getY())) {
			throw new ManagedException(ManagedErrorCode.LORRY_IS_BUSY_NOW);
		}
		player.getMoveController().stopMoving();
		if (lorry.getMapId() == player.getMapId()) {
			World.getInstance().updatePosition(player, lorry.getX(), lorry.getY(), lorry.getHeading());
		} else {
			World.getInstance().setPosition(player, lorry.getMapId(), World.INIT_INSTANCE, lorry.getX(), lorry.getY(),
					player.getHeading());
		}
		player.sendUpdatePosition();
		PacketSendUtility.sendSignMessage(player, sign);
	}

	public Storage<String, ExpressResource> getExpressResources() {
		return expressResources;
	}

	public ExpressResource getExpressResource(String key) {
		return expressResources.get(key, true);
	}

	public void setExpressResources(Storage<String, ExpressResource> expressResources) {
		this.expressResources = expressResources;
	}

	public int getOutOfRangeRadius() {
		return NOT_SEE_RANGE.getValue();
	}

	public int getOutOfOnAttackTime() {
		return EXPRESS_OUT_OF_ONATTACK.getValue();
	}

	public int getRobRewardCountLimit() {
		return ROB_REWARD_COUNT_LIMIT.getValue();
	}

	public Reward createExpressReward(Lorry lorry, String chooserGroupId) {
		List<String> core = ChooserManager.getInstance().chooseValueByRequire(lorry.getOwner(), COE_CHOOSER.getValue());
		Map<String, Object> param = New.hashMap();
		param.put("LEVEL", lorry.getOwner().getLevel());
		param.put("EXPRESSCOE", core.get(0));
		param.put("COLOR", COLOR_FACTOR.getValue()[lorry.getColor() - 1]);
		param.put("WORLD_CLASS_BONUS", WorldRankManager.getInstance().getPlayerWorldLevel(lorry.getOwner()));
		param.put("HONORINCREASE", lorry.getOwner().getGameStats().getCurrentStat(StatEnum.HONOR_PLUS) * 1.0 / 10000);
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(lorry.getOwner(), chooserGroupId);
		Reward reward = RewardManager.getInstance().creatReward(lorry.getOwner(), rewardIds, param);
		return reward;
	}

	public Reward createRobExpressReward(Player attacker, Lorry lorry) {
		List<String> core = ChooserManager.getInstance().chooseValueByRequire(lorry.getOwner(), COE_CHOOSER.getValue());
		Map<String, Object> param = New.hashMap();
		param.put("EXPRESSCOE_PLAY_LEVEL", lorry.getOwner().getLevel());
		param.put("EXPRESSCOE", core.get(0));
		param.put("COLOR", COLOR_FACTOR.getValue()[lorry.getColor() - 1]);
		param.put("TECH_EXTRA", getRobTechCondition().verify(attacker) ? ROB_TECH_EXTRA_VALUE.getValue() : 0);
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(lorry.getOwner(),
				ROB_LORRY_REWARD_CHOOSERGROUP.getValue());
		Reward reward = RewardManager.getInstance().creatReward(lorry.getOwner(), rewardIds, param);
		return reward;
	}

	public void checkLorryInVisualRange(Player player, boolean canSeeLorry) {

		final Lorry lorry = player.getExpress().getCurrentLorry();
		if (lorry == null) {
			throw new ManagedException(ManagedErrorCode.NOT_FOUND_LORRY);
		}
		if (canSeeLorry
				&& MathUtil.isInRange(player.getX(), player.getY(), lorry.getX(), lorry.getY(),
						NOT_SEE_RANGE.getValue(), NOT_SEE_RANGE.getValue())) {
			lorry.faraway(canSeeLorry);
		} else {
			lorry.faraway(false);
		}

	}

	public void askForHelp(Player player) {
		final Lorry lorry = player.getExpress().getCurrentLorry();
		if (lorry == null) {
			throw new ManagedException(ManagedErrorCode.NOT_FOUND_LORRY);
		}
		if (player.getGang() == null) {
			throw new ManagedException(ManagedErrorCode.GANG_NOT_JOIN);
		}

		I18nUtils utils = I18nUtils
				.valueOf("301007")
				.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()))
				.addParm(
						I18NparamKey.FLYID,
						I18nPack.valueOf(playerChatTransportManager.addTransport(lorry.getMapId(), lorry.getX(),
								lorry.getY(), lorry.getInstanceId())));
		ChatManager.getInstance().sendSystem(2, utils, null, player.getGang());
	}

	public void getExpressNavigator(Player player) {
		PacketSendUtility.sendPacket(player, SM_Get_Current_Express.valueOf(showResultCollection));
	}

	public void robSpecifiedLorry(Player player, long targetId, int sign) {
		Player target = PlayerManager.getInstance().getPlayer(targetId);
		if (target == null || target.getExpress().getCurrentLorry() == null) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.TARGET_LORRY_NOT_EXIST);
			return;
		}

		CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1, ROB_FLY_COND.getValue());
		if (!conditions.verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}

		Lorry targetLorry = target.getExpress().getCurrentLorry();
		World.getInstance().canEnterMap(player, targetLorry.getMapId());
		WorldPosition destinationPoint = targetLorry.getPosition();
		RelivePosition dPoint = RelivePosition.valueOf(destinationPoint.getX(), destinationPoint.getY(),
				destinationPoint.getMapId());

		CoreActions actions = CoreActionManager.getInstance().getCoreActions(1, ROB_FLY_ACT.getValue());
		actions.verify(player, true);
		actions.act(player, ModuleInfo.valueOf(ModuleType.EXPRESS, SubModuleType.ROB_LORRY_FLY));
		int destinationCountry = World.getInstance().getMapResource(targetLorry.getMapId()).getCountry();
		if (destinationCountry != MapCountry.NEUTRAL.getValue() && destinationCountry != player.getCountryValue()) {
			dPoint = ConfigValueManager.getInstance().EXPRESS_FLY_DESINATION.getValue()[targetLorry.getCountryValue() - 1];
		}

		player.getMoveController().stopMoving();
		if (player.getMapId() == dPoint.getMapId() && player.getInstanceId() == destinationPoint.getInstanceId()) {
			World.getInstance().updatePosition(player, dPoint.getX(), dPoint.getY(), player.getHeading());
		} else {
			World.getInstance().setPosition(player, dPoint.getMapId(), destinationPoint.getInstanceId(), dPoint.getX(),
					dPoint.getY(), player.getHeading());
		}
		player.sendUpdatePosition();
		PacketSendUtility.sendPacket(player, SM_System_Sign.valueOf(sign));
	}
}
