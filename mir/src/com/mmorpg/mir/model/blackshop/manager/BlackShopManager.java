package com.mmorpg.mir.model.blackshop.manager;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.blackshop.BlackShopConfig;
import com.mmorpg.mir.model.blackshop.model.BlackShop;
import com.mmorpg.mir.model.blackshop.model.BlackShopServer;
import com.mmorpg.mir.model.blackshop.packet.SM_BlackShop_Query_Info;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActionType;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.action.ItemAction;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.utils.PacketSendUtility;

@Component
public class BlackShopManager {

	@Autowired
	private BlackShopConfig config;

	private static BlackShopManager instance;

	public static BlackShopManager getInstance() {
		return instance;
	}

	@PostConstruct
	void init() {
		instance = this;
	}

	public void queryBlackShop(Player player) {
		BlackShopServer blackShopServer = ServerState.getInstance().getBlackShopServer();
		if (!blackShopServer.isActivityOpen()) {
			throw new ManagedException(ManagedErrorCode.BLACKSHOP_ACTIVITY_NOT_OPEN);
		}
		if (!config.getOpenConditions().verify(player)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		BlackShop blackShop = player.getBlackShop();
		if (blackShop.checkCanRefresh() || blackShopServer.getVersion() != blackShop.getVersion()) {
			blackShop.refresh(true, false);
		}
		PacketSendUtility.sendPacket(player, SM_BlackShop_Query_Info.valueOf(player));
	}

	public void customRefresh(Player player) {
		BlackShopServer blackShopServer = ServerState.getInstance().getBlackShopServer();
		if (!blackShopServer.isActivityOpen()) {
			throw new ManagedException(ManagedErrorCode.BLACKSHOP_ACTIVITY_NOT_OPEN);
		}
		if (!config.getOpenConditions().verify(player)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		BlackShop blackShop = player.getBlackShop();
		String itemCode = config.SHOP_REFRESH_PRIORITY_ITEM_ACTION.getValue();
		int itemActCount = config.SHOP_REFRESH_PRIORITY_ITEM_ACTION_COUNT.getValue();
		if (player.getPack().getItemSizeByKey(itemCode) >= itemActCount) {
			// 优先扣除道具
			ItemAction itemAction = CoreActionType.createItemCondition(itemCode, itemActCount);
			itemAction.verify(player);
			itemAction.act(player, ModuleInfo.valueOf(ModuleType.BLACKSHOP, SubModuleType.BLACKSHOP_REFRESH_ACT));
		} else { // 扣除元宝 CoerActions
			CoreActions actions = config.getRefreshActions();
			if (!actions.verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}
			actions.act(player, ModuleInfo.valueOf(ModuleType.BLACKSHOP, SubModuleType.BLACKSHOP_REFRESH_ACT));
		}

		blackShop.refresh(false, true);
	}

	public void systemRefresh(Player player) {
		BlackShopServer blackShopServer = ServerState.getInstance().getBlackShopServer();
		if (!blackShopServer.isActivityOpen()) {
			throw new ManagedException(ManagedErrorCode.BLACKSHOP_ACTIVITY_NOT_OPEN);
		}
		if (!config.getOpenConditions().verify(player)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		BlackShop blackShop = player.getBlackShop();
		if (!blackShop.checkCanRefresh()) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		blackShop.refresh(true, true);
	}

	public void buy(Player player, int gridIndex) {
		BlackShopServer blackShopServer = ServerState.getInstance().getBlackShopServer();
		if (!blackShopServer.isActivityOpen()) {
			throw new ManagedException(ManagedErrorCode.BLACKSHOP_ACTIVITY_NOT_OPEN);
		}
		if (!config.getOpenConditions().verify(player)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		int gridCountMax = config.SHOP_GRID_COUNT.getValue();
		if (gridIndex < 0 || gridIndex >= gridCountMax) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		BlackShop blackShop = player.getBlackShop();
		blackShop.buy(gridIndex);
	}
}
