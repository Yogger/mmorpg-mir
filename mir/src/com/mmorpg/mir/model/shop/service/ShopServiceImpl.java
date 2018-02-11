package com.mmorpg.mir.model.shop.service;

import java.util.List;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.shop.event.ShopBuyEvent;
import com.mmorpg.mir.model.shop.manager.ShopManager;
import com.mmorpg.mir.model.shop.model.BuyResult;
import com.mmorpg.mir.model.shop.resouce.ShopResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;

@Component
public class ShopServiceImpl implements ShopService {

	@Autowired
	private ShopManager shopManager;

	@Autowired
	private CoreConditionManager coreConditionManager;
	@Autowired
	private CoreActionManager coreActionManager;
	@Autowired
	private RewardManager rewardManager;
	@Autowired
	private ChooserManager chooserManager;

	/**
	 * 购买商品
	 */
	public void buy(Player player, String id, int count) {
		ShopResource resource = shopManager.getShopResource(id);
		if (!coreConditionManager.getCoreConditions(1, resource.getConditions()).verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		if (resource.getLevelBuyCountChooserGroup() != null) {
			String dailyCount = chooserManager.chooseValueByRequire(player, resource.getLevelBuyCountChooserGroup())
					.get(0);
			if (player.getShoppingHistory().getCount(id) + count > (Integer.valueOf(dailyCount))) {
				throw new ManagedException(ManagedErrorCode.SHOP_BUY_LIMIT);
			}
		}
		if (resource.getLevelTotalCountChooserGroup() != null) {
			String totalCount = chooserManager.chooseValueByRequire(player, resource.getLevelTotalCountChooserGroup())
					.get(0);
			if (player.getShoppingHistory().getTotalCount(id) + count > (Integer.valueOf(totalCount))) {
				throw new ManagedException(ManagedErrorCode.SHOP_BUY_LIMIT);
			}
		}
		CoreActions actions = coreActionManager.getCoreActions(count, resource.getActions());
		actions.verify(player, true);
		ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.SHOP, SubModuleType.SHOP_BUY, resource.getI18name());
		actions.act(player, moduleInfo);
		List<String> rewardIds = chooserManager.chooseValueByRequire(player, resource.getChooserGrounpId());
		List<String> allRewardIds = New.arrayList();
		for (int i = 0; i < count; i++) {
			allRewardIds.addAll(rewardIds);
		}
		rewardManager.grantReward(player, allRewardIds, moduleInfo);
		player.getShoppingHistory().addHistory(id, count);
		EventBusManager.getInstance().syncSubmit(ShopBuyEvent.valueOf(player, resource.getId()));
	}

	public BuyResult autoBuy(Player player, String shopId, int buyCount) {
		ShopResource shopResource = ShopManager.getInstance().getShopResource(shopId);
		if (!CoreConditionManager.getInstance().getCoreConditions(1, shopResource.getConditions()).verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return BuyResult.valueOf(ManagedErrorCode.ERROR_MSG);
		}
		if (shopResource.getLevelBuyCountChooserGroup() != null) {
			String dailyCount = ChooserManager.getInstance()
					.chooseValueByRequire(player, shopResource.getLevelBuyCountChooserGroup()).get(0);
			Integer daiyCountInt = Integer.parseInt(dailyCount);
			if (daiyCountInt - player.getShoppingHistory().getCount(shopId) < buyCount) {
				buyCount = (daiyCountInt - player.getShoppingHistory().getCount(shopId));
			}
			if (buyCount <= 0) {
				return BuyResult.valueOf(ManagedErrorCode.SHOP_BUY_LIMIT);
			}
		}

		if (shopResource.getLevelTotalCountChooserGroup() != null) {
			String totalCount = ChooserManager.getInstance()
					.chooseValueByRequire(player, shopResource.getLevelTotalCountChooserGroup()).get(0);
			int totalCountInt = Integer.parseInt(totalCount);
			if (totalCountInt - player.getShoppingHistory().getTotalCount(shopId) < buyCount) {
				buyCount = totalCountInt - player.getShoppingHistory().getTotalCount(shopId);
			}
			if (buyCount <= 0) {
				return BuyResult.valueOf(ManagedErrorCode.SHOP_BUY_LIMIT);
			}
		}
		CoreActions actions = coreActionManager.getCoreActions(buyCount, shopResource.getActions());
		if (!actions.verify(player, true)) {
			return BuyResult.valueOf(ManagedErrorCode.ERROR_MSG);
		}
		ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.SHOP, SubModuleType.SHOP_BUY, shopResource.getI18name());
		actions.verify(player, true);
		actions.act(player, moduleInfo);
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
				shopResource.getChooserGrounpId());
		List<String> allRewardIds = New.arrayList();
		for (int i = 0; i < buyCount; i++) {
			allRewardIds.addAll(rewardIds);
		}
		Reward r = RewardManager.getInstance().creatReward(player, allRewardIds, null);
		player.getShoppingHistory().addHistory(shopId, buyCount);
		return BuyResult.valueOf(r, actions);
	}
}
