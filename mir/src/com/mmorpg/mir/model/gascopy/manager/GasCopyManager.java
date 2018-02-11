package com.mmorpg.mir.model.gascopy.manager;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActionType;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.action.ItemAction;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.countrycopy.manager.CountryCopyManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gascopy.config.GasCopyMapConfig;
import com.mmorpg.mir.model.gascopy.packet.SM_Enter_GasCopy;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.object.ObjectManager;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.shop.manager.ShopManager;
import com.mmorpg.mir.model.shop.resouce.ShopResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.windforce.common.scheduler.ScheduledTask;
import com.windforce.common.scheduler.impl.SimpleScheduler;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;

@Component
public class GasCopyManager {
	
	private static GasCopyManager INSTANCE;
	
	@Autowired
	private GasCopyMapConfig config;
	
	@Autowired
	private SimpleScheduler simpleScheduler;
	
	@PostConstruct
	void init() {
		INSTANCE = this;
	}
	
	public static GasCopyManager getInstance() {
		return INSTANCE;
	}

	public void initialGasCopyMapTask() {
		// map timer to run sth
		final WorldMap map = World.getInstance().getWorldMap(config.MAPID.getValue());
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				for (WorldMapInstance instance : map.getInstances().values()) {
					Iterator<Player> iter = instance.playerIterator();
					while (iter.hasNext()) {
						Player player = iter.next();
						if (!player.getLifeStats().isAlreadyDead()) {
							Map<String, Object> params = New.hashMap();
							params.put("LEVEL", player.getLevel());
							params.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player));
							RewardManager.getInstance().grantReward(player, config.IN_MAP_REWARDID.getValue(), 
									ModuleInfo.valueOf(ModuleType.GAS_COPY, SubModuleType.GAS_COPY_INMAP_REWARD), params);
						}
					}
				}
			}
		}, config.IN_MAP_REWARD_PERIOD.getValue() * DateUtils.MILLIS_PER_SECOND
		 , config.IN_MAP_REWARD_PERIOD.getValue() * DateUtils.MILLIS_PER_SECOND);
		
		simpleScheduler.schedule(new ScheduledTask() {
			
			@Override
			public void run() {
				for (WorldMapInstance instance : map.getInstances().values()) {
					Iterator<Player> iter = instance.playerIterator();
					while (iter.hasNext()) {
						Player player = iter.next();
						player.getGasCopy().calcGasValue();
					}
				}
			}
			
			@Override
			public String getName() {
				return "西周王陵加瘴气";
			}
			
		}, "0 * * * * *");
	}

	public void enterGasCopy(final Player player, boolean gold) {
		if (!config.getEnterConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.GAS_COPY_CANNOT_ENTER);
		}
		if (player.getGasCopy().getDailyEnterCount() >= player.getVip().getResource().getGasCopyDailyEnterLimit()) {
			throw new ManagedException(ManagedErrorCode.GAS_COPY_COUNT_NOT_ENOUGH);
		}
		Integer amount = config.getEnterActItemNum(player);
		
		CoreActions originalActions = new CoreActions();
		ItemAction itemActions = CoreActionType.createItemCondition(config.ENTER_ACT_ITEMKEY.getValue(), amount);
		originalActions.addActions(itemActions);
		
		if (!originalActions.verify(player, false)) {
			if (!gold) {
				throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_ITEM);
			}
			CoreActions actions = new CoreActions();
			int have = (int) player.getPack().getItemSizeByKey(config.ENTER_ACT_ITEMKEY.getValue());
			if (have > 0) {
				ItemAction goldItemAct = CoreActionType.createItemCondition(config.ENTER_ACT_ITEMKEY.getValue(), have);
				actions.addAction(goldItemAct);
			}
			ShopResource shopResource = ShopManager.getInstance().getShopResource(config.ENTER_ITEM_SHOP_ID.getValue());
			actions.addActions(CoreActionManager.getInstance().getCoreActions(amount - have, shopResource.getActions()));
			
			if (!actions.verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_GOLD);
			}
			actions.act(player, ModuleInfo.valueOf(ModuleType.SHOP, SubModuleType.SHOP_BUY, shopResource.getI18name()));
		} else {
			originalActions.act(player, ModuleInfo.valueOf(ModuleType.GAS_COPY, SubModuleType.GAS_COPY_INMAP_ACTIONS));
		}
		
		player.getMoveController().stopMoving();
		CountryCopyManager.getInstance().unEnrolleBySystem(player);
		player.getGasCopy().enter();
		PacketSendUtility.sendPacket(player, new SM_Enter_GasCopy());
		// I18nUtils utils = I18nUtils.valueOf("306133");
		// utils.addParm("name", I18nPack.valueOf(player.getName()));
		// utils.addParm("country",
		// I18nPack.valueOf(player.getCountry().getName()));
		// for (Country country :
		// CountryManager.getInstance().getCountries().values()) {
		// ChatManager.getInstance().sendSystem(6, utils, null, country);
		// }
	}

	public void leaveGasCopy(Player player) {
		if (!config.isInGasCopyMap(player.getMapId()) || !SessionManager.getInstance().isOnline(player.getObjectId())) {
			return;
		}
		player.getMoveController().stopMoving();
		List<String> result = ChooserManager.getInstance().chooseValueByRequire(player.getCountryId().getValue(),
				GasCopyMapConfig.getInstance().BACKHOME_POINT.getValue());
		RelivePosition p = JsonUtils.string2Object(result.get(0), RelivePosition.class);
		if (player.getPosition().getMapId() == p.getMapId()) {
			World.getInstance().updatePosition(player, p.getX(), p.getY(), player.getHeading());
		} else {
			World.getInstance().setPosition(player, p.getMapId(), p.getX(), p.getY(), player.getHeading());
		}
		player.sendUpdatePosition();
		player.getGasCopy().leaveGasCopy();
	}

	public void killMonster(Player player, String key) {
		int addValue = ObjectManager.getInstance().getObjectResource(key).getAddGasValue();
		player.getGasCopy().addGasValue(addValue);
		PlayerManager.getInstance().updateIfOffline(player);
	}
	
}
