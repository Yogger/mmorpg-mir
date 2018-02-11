package com.mmorpg.mir.model.investigate.manager;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.ClearAndMigrate;
import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.investigate.packet.SM_Investigate_Accept;
import com.mmorpg.mir.model.investigate.packet.SM_Investigate_Complete;
import com.mmorpg.mir.model.investigate.packet.SM_Investigate_TakeInfo;
import com.mmorpg.mir.model.investigate.packet.SM_Query_Investigate_Status;
import com.mmorpg.mir.model.investigate.resource.InvestigateNpcResource;
import com.mmorpg.mir.model.investigate.resource.InvestigateResource;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.player.packet.SM_CountryPlayerInfo;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.temple.manager.TempleManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.welfare.event.InvestigateEvent;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class InvestigateManager implements IInvestigateManager {
	@Static
	private Storage<String, InvestigateResource> investigateResources;
	@Static
	private Storage<String, InvestigateNpcResource> investigateNpcResources;

	@Autowired
	private ChooserManager chooserManager;

	@Autowired
	private RewardManager rewardManager;

	@Static("INVESTIGATE:COE_CHOOSER")
	private ConfigValue<String> COE_CHOOSER;

	@Static("INVESTIGATE:ACCEPT_CONDITIONIDS")
	private ConfigValue<String[]> ACCEPT_CONDITIONIDS;

	@Static("INVESTIGATE:COLOR_FACTOR")
	private ConfigValue<Integer[]> COLOR_FACTOR;

	@Static("INVESTIGATE:BEST_INFO")
	public ConfigValue<Map<String, String>> BEST_INFO;

	@Static("INVESTIGATE:PURPLE_INFO")
	public ConfigValue<Map<String, String>> PURPLE_INFO;

	@Static("INVESTIGATE:INVESTIGATE_ITEM_ACTIONS")
	public ConfigValue<Map<String, String>> INVESTIGATE_ITEM_ACTIONS;

	@Static("INVESTIGATE:INVESTIGATE_ITEM_COLOR")
	public ConfigValue<Map<String, Integer>> INVESTIGATE_ITEM_COLOR;

	@Static("INVESTIGATE:CHANGE_INFO_GOLD_ACT")
	public ConfigValue<String[]> CHANGE_INFO_GOLD_ACT;

	@Static("INVESTIGATE:SELECT_INVESTIGATENPC")
	public ConfigValue<String[]> SELECT_INVESTIGATENPC;

	private Map<Integer, Integer> investigateStatus;

	@Autowired
	private ConfigValueManager configValueManager;

	@Autowired
	private TempleManager templeManager;

	private static InvestigateManager instance;

	@PostConstruct
	public void init() {
		instance = this;
		if (ClearAndMigrate.clear) {
			return;
		}
		investigateStatus = new ConcurrentHashMap<Integer, Integer>();

		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Runnable() {

			@Override
			public void run() {
				for (Integer mapId : ConfigValueManager.getInstance().COUNTRY_ACT_DISPLAY_MAPID.getValue()) {
					int count = 0;
					for (WorldMapInstance instance : World.getInstance().getWorldMap(mapId).getInstances().values()) {
						Iterator<Player> iterator = instance.playerIterator();
						while (iterator.hasNext()) {
							Player next = iterator.next();
							if (next.getInvestigate().getCurrentInvestigate() != null) {
								count++;
							}
						}
					}
					investigateStatus.put(mapId, count);
				}
			}
		}, 5000, 5000);
	}

	public static InvestigateManager getInstance() {
		return instance;
	}

	public void accept(Player player) {
		player.getInvestigate().refresh();
		CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1,
				ACCEPT_CONDITIONIDS.getValue());
		if (!conditions.verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		if (player.getInvestigate().getCount() >= configValueManager.INVESTIGATE_MAX_COUNT.getValue()
				+ player.getVip().getResource().getExInvestigateCount()) {
			throw new ManagedException(ManagedErrorCode.INVESTIGE_MAX_COUNT);
		}
		if (player.getInvestigate().getCurrentNpc() != null || player.getInvestigate().getCurrentInvestigate() != null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		String npcId = SELECT_INVESTIGATENPC.getValue()[templeManager.selectCountry(player.getCountryValue()) - 1];
		player.getInvestigate().setCurrentNpc(npcId);
		PacketSendUtility.sendPacket(player, SM_Investigate_Accept.valueOf(player.getInvestigate().getCurrentNpc(),
				player.getInvestigate().getCount()));

	}

	/**
	 * 使用刺探令
	 * 
	 * @param player
	 */
	public void useInvestigateItem(Player player, String itemId) {
		if (player.getLifeStats().isAlreadyDead() || (!player.isSpawned())) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}

		player.getInvestigate().refresh();
		String currentNpc = player.getInvestigate().getCurrentNpc();
		if (currentNpc == null) {
			throw new ManagedException(ManagedErrorCode.INVESTIGE_NOT_NPC);
		}

		// if (player.getInvestigate().getCurrentInvestigate() == null) {
		// throw new ManagedException(ManagedErrorCode.INVESTIGATE_NOT_START);
		// }
		int color = INVESTIGATE_ITEM_COLOR.getValue().get(itemId);
		if (player.getInvestigate().getCurrentInvestigate() != null) {
			InvestigateResource r = investigateResources.get(player.getInvestigate().getCurrentInvestigate(), true);
			if (r.getColor() >= color) {
				throw new ManagedException(ManagedErrorCode.INVESTIGATE_CURRENT_COLOR_GREATER);
			}
		}

		InvestigateNpcResource npcResource = investigateNpcResources.get(currentNpc, true);
		if (!npcResource.getItemAccepConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		String actionIds = INVESTIGATE_ITEM_ACTIONS.getValue().get(itemId);
		CoreActions actions = CoreActionManager.getInstance().getCoreActions(1, actionIds);
		if (!actions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		actions.act(player, ModuleInfo.valueOf(ModuleType.INVESTIGATE, SubModuleType.INVESTIGATE_ITEM_ACT));
		List<String> investigateIds = null;
		if (color == 4) {
			investigateIds = Arrays.asList(PURPLE_INFO.getValue().get(String.valueOf(player.getCountryValue())));
		} else {
			investigateIds = Arrays.asList(BEST_INFO.getValue().get(String.valueOf(player.getCountryValue())));
		}
		if (investigateIds.size() > 1 || investigateIds.isEmpty()) {
			throw new ManagedException(ManagedErrorCode.INVESTIGE_SELECT_NPC_MORE);
		}
		InvestigateResource resource = investigateResources.get(investigateIds.get(0), true);

		player.getInvestigate().changeId(resource.getId());

		PacketSendUtility.broadcastPacket(player, SM_CountryPlayerInfo.valueOf(player));
		PacketSendUtility.sendPacket(player,
				SM_Investigate_TakeInfo.valueOf(resource.getId(), player.getInvestigate().getLastChangeTime(), true));
		publicNotice(resource, npcResource.getBelongCountry(), player);

	}

	public void change(Player player, boolean useGold) {
		if (player.getLifeStats().isAlreadyDead() || (!player.isSpawned())) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}
		player.getInvestigate().refresh();
		String currentNpc = player.getInvestigate().getCurrentNpc();
		if (currentNpc == null) {
			throw new ManagedException(ManagedErrorCode.INVESTIGE_NOT_NPC);
		}

		if (player.getInvestigate().getCurrentInvestigate() == null) {
			return;
			// throw new ManagedException(ManagedErrorCode.INVESTIGEING);
		}
		InvestigateNpcResource npcResource = investigateNpcResources.get(currentNpc, true);
		if (!npcResource.getAcceptConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		List<String> investigateIds = null;
		if (useGold) {
			if (!player.getVip().getResource().isInvestigateOrange()) {
				throw new ManagedException(ManagedErrorCode.VIP_CONDITION_NOT_SATISFY);
			}
			CoreActions actions = CoreActionManager.getInstance().getCoreActions(1, CHANGE_INFO_GOLD_ACT.getValue());
			actions.verify(player, true);
			actions.act(player, ModuleInfo.valueOf(ModuleType.INVESTIGATE, SubModuleType.INVESTIGATE_REFRESH_ACT));
			investigateIds = Arrays.asList(BEST_INFO.getValue().get(String.valueOf(player.getCountryValue())));
		} else {
			if (player.getInvestigate().changeTimeCD(configValueManager.INVESTIGATE_SELECT_CHANGE_CD.getValue())) {
				throw new ManagedException(ManagedErrorCode.INVESTIGE_CHANGE_CD);
			}
			investigateIds = chooserManager.chooseValueByRequire(player, npcResource.getChooserGroupId());
		}

		if (investigateIds.size() > 1 || investigateIds.isEmpty()) {
			throw new ManagedException(ManagedErrorCode.INVESTIGE_SELECT_NPC_MORE);
		}
		InvestigateResource resource = investigateResources.get(investigateIds.get(0), true);

		player.getInvestigate().changeId(resource.getId());

		PacketSendUtility.broadcastPacket(player, SM_CountryPlayerInfo.valueOf(player));
		PacketSendUtility
				.sendPacket(player, SM_Investigate_TakeInfo.valueOf(resource.getId(), player.getInvestigate()
						.getLastChangeTime(), useGold));
		publicNotice(resource, npcResource.getBelongCountry(), player);
	}

	public void takeInfo(Player player) {
		if (player.getLifeStats().isAlreadyDead() || (!player.isSpawned())) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}
		player.getInvestigate().refresh();
		String currentNpc = player.getInvestigate().getCurrentNpc();
		if (currentNpc == null) {
			throw new ManagedException(ManagedErrorCode.INVESTIGE_NOT_NPC);
		}
		if (player.getInvestigate().getCurrentInvestigate() != null) {
			throw new ManagedException(ManagedErrorCode.INVESTIGEING);
		}

		InvestigateNpcResource npcResource = investigateNpcResources.get(currentNpc, true);
		if (!npcResource.getAcceptConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		List<String> investigateIds = chooserManager.chooseValueByRequire(player, npcResource.getChooserGroupId());
		if (investigateIds.size() > 1 || investigateIds.isEmpty()) {
			throw new ManagedException(ManagedErrorCode.INVESTIGE_SELECT_NPC_MORE);
		}

		InvestigateResource resource = investigateResources.get(investigateIds.get(0), true);

		player.getInvestigate().setCurrentInvestigate(resource.getId());

		PacketSendUtility.broadcastPacket(player, SM_CountryPlayerInfo.valueOf(player));
		PacketSendUtility.sendPacket(player,
				SM_Investigate_TakeInfo.valueOf(resource.getId(), player.getInvestigate().getLastChangeTime(), false));

		publicNotice(resource, npcResource.getBelongCountry(), player);
	}

	public void complete(Player player) {
		player.getInvestigate().refresh();
		if (player.getInvestigate().getCurrentInvestigate() == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		InvestigateResource resource = investigateResources.get(player.getInvestigate().getCurrentInvestigate(), true);
		if (!resource.getCompleteConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		player.getInvestigate().addCount(1);
		Reward reward = createInvestReward(player, resource);
		PacketSendUtility.broadcastPacket(player, SM_CountryPlayerInfo.valueOf(player));
		rewardManager.grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.INVESTIGATE, SubModuleType.INVESTIGATE_REWARD));

		player.getInvestigate().setCurrentInvestigate(null);
		player.getInvestigate().setCurrentNpc(null);
		player.getInvestigate().addHistoryColorCount(resource.getColor());

		PacketSendUtility.sendPacket(player, SM_Investigate_Complete.valueOf(resource.getId(), reward));
		EventBusManager.getInstance().submit(InvestigateEvent.valueOf(player));
		LogManager.investigate(player.getObjectId(), player.getPlayerEnt().getServer(), player.getPlayerEnt()
				.getAccountName(), player.getName(), System.currentTimeMillis(), player.getInvestigate().getCount(),
				player.getInvestigate().getCountAll());

		if (Integer.valueOf(resource.getColor()) >= 4) {
			// 紫色通报
			I18nUtils i18nUtils = I18nUtils.valueOf("10410");
			i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
			i18nUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			i18nUtils.addParm("messagecolor", I18nPack.valueOf(resource.getColorI18n()));
			i18nUtils.addParm(
					"color",
					I18nPack.valueOf(TempleManager.getInstance().COUNTRY_TEMPLE_SPY_COLOR.getValue().get(
							resource.getColor() + "")));
			ChatManager.getInstance().sendSystem(11001, i18nUtils, null);
		}
	}

	public void queryInvestigateStatus(Player player) {
		PacketSendUtility.sendPacket(player, SM_Query_Investigate_Status.valueOf(investigateStatus));
	}

	private Reward createInvestReward(Player player, InvestigateResource resource) {
		List<String> core = ChooserManager.getInstance().chooseValueByRequire(player, COE_CHOOSER.getValue());
		Map<String, Object> param = New.hashMap();
		param.put("LEVEL", player.getLevel());
		param.put("EXPSPYCOE", core.get(0));
		param.put("COLOR", COLOR_FACTOR.getValue()[resource.getColor() - 1]);
		param.put("WORLD_CLASS_BONUS", WorldRankManager.getInstance().getPlayerWorldLevel(player));
		param.put("HONORINCREASE", player.getGameStats().getCurrentStat(StatEnum.HONOR_PLUS) * 1.0 / 10000);
		List<String> rewardIds = ChooserManager.getInstance()
				.chooseValueByRequire(player, resource.getChooserGroupId());
		Reward reward = RewardManager.getInstance().creatReward(player, rewardIds, param);
		return reward;
	}

	private void publicNotice(InvestigateResource resource, int targetCountry, Player player) {
		if (Integer.valueOf(resource.getColor()) < 4) {
			return;
		}
		// 紫色通报
		I18nUtils i18nUtils = I18nUtils.valueOf("10412");
		i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
		i18nUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
		i18nUtils.addParm("messagecolor", I18nPack.valueOf(resource.getColorI18n()));
		i18nUtils.addParm(
				"color",
				I18nPack.valueOf(TempleManager.getInstance().COUNTRY_TEMPLE_SPY_COLOR.getValue().get(
						resource.getColor() + "")));
		CountryId id = CountryId.valueOf(targetCountry);
		i18nUtils.addParm("targetCountry",
				I18nPack.valueOf(CountryManager.getInstance().getCountries().get(id).getName()));
		ChatManager.getInstance().sendSystem(11001, i18nUtils, null);
	}
}
