package com.mmorpg.mir.model.beauty.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.beauty.BeautyGirlConfig;
import com.mmorpg.mir.model.beauty.model.BeautyGirl;
import com.mmorpg.mir.model.beauty.model.BeautyGirlPool;
import com.mmorpg.mir.model.beauty.resource.BeautyGirlResource;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.shop.manager.ShopManager;
import com.mmorpg.mir.model.shop.model.BuyResult;
import com.mmorpg.mir.model.shop.packet.vo.ShoppingHistoryVO;
import com.mmorpg.mir.model.shop.resouce.ShopResource;
import com.mmorpg.mir.model.shop.service.ShopService;
import com.mmorpg.mir.model.utils.PacketSendUtility;

@Component
public class BeautyManager {

	@Autowired
	private ModuleOpenManager moduleOpenManager;

	@Autowired
	private CoreActionManager coreActionManager;

	@Autowired
	private ShopService shopService;

	@Autowired
	private BeautyGirlConfig config;

	/**
	 * 激活
	 * 
	 * @param player
	 * @param id
	 */
	public void active(Player player, String id) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.BEAUTY)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		if (player.getLifeStats().isAlreadyDead()) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}

		BeautyGirlPool pool = player.getBeautyGirlPool();

		if (pool.isActive(id)) {
			throw new ManagedException(ManagedErrorCode.BEAUTY_GIRL_ACTIVE);
		}

		BeautyGirlResource resource = config.beautyGirlStorage.get(id, true);
		boolean verify = resource.isActiveVerify() ? resource.getActiveConditions().verify(player) : resource
				.getActiveConditions().verifyOr(player);
		if (!verify) {
			throw new ManagedException(ManagedErrorCode.BEAUTY_ACTIVE_CONDITION_NOT_VERYFY);
		}
		if (!resource.getActiveActions().isEmpty()) {
			CoreActions activeActions = resource.getActiveActions();
			if (!activeActions.verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.SYS_ERROR);
			}
			activeActions.act(player,
					ModuleInfo.valueOf(ModuleType.BEAUTY, SubModuleType.BEAUTY_ACTIVE_ACT, resource.getId()));
		}
		BeautyGirl girl = BeautyGirl.valueOf(resource);
		pool.active(girl);
	}

	/**
	 * 出战
	 * 
	 * @param player
	 * @param group
	 */
	public void fight(Player player, String id) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.BEAUTY)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		if (player.getLifeStats().isAlreadyDead()) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}

		BeautyGirlPool pool = player.getBeautyGirlPool();

		pool.fight(id);
	}

	public void unFight(Player player) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.BEAUTY)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		if (player.getLifeStats().isAlreadyDead()) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}

		BeautyGirlPool pool = player.getBeautyGirlPool();
		pool.unFight();
	}

	public void linger(Player player, String id, boolean autoBuy) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.BEAUTY)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		if (player.getLifeStats().isAlreadyDead()) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}

		BeautyGirlPool pool = player.getBeautyGirlPool();
		if (!pool.isActive(id)) {
			throw new ManagedException(ManagedErrorCode.BEAUTY_TARGET_NOT_ACTIVE);
		}
		BeautyGirl girl = pool.getBeautyGirls().get(id);
		if (girl.isMaxLevel()) {
			throw new ManagedException(ManagedErrorCode.BEAUTY_MAX_LEVEL);
		}

		BeautyGirlResource resource = config.beautyGirlStorage.get(id, true);

		String itemId = coreActionManager.getCoreActions(1, resource.getLingerMaterialAct()).getFirstItemKey();
		int currentItemCount = (int) player.getPack().getItemSizeByKey(itemId);
		int needItemCount = resource.getLingerActCountByLevel(girl.getLevel());

		int senseAdd = 0;
		CoreActions lingerActions = new CoreActions();
		if ((autoBuy && currentItemCount >= needItemCount) || !autoBuy) {
			senseAdd = resource.getSenseByLevel(girl.getLevel());
			lingerActions.addActions(coreActionManager.getCoreActions(
					resource.getLingerActCountByLevel(girl.getLevel()), resource.getLingerMaterialAct()));
		} else if (autoBuy) {
			int currentCount = currentItemCount;
			String autoBuyShopId = resource.getAutoBuyShopId();
			int buyCount = needItemCount - currentCount;
			BuyResult result = shopService.autoBuy(player, autoBuyShopId, buyCount);
			if (result.getCode() != null) {
				throw new ManagedException(result.getCode());
			}
			player.getShoppingHistory().refresh();
			PacketSendUtility.sendPacket(player, ShoppingHistoryVO.valueOf(player));
			currentCount += result.getReward().getTargetItemCount(itemId);
			// 缠绵次数
			int lingerCount = currentCount / resource.getLingerActCountByLevel(girl.getLevel());

			// 多余的返还
			int rewardItemCount = currentCount - currentItemCount - lingerCount
					* resource.getLingerActCountByLevel(girl.getLevel());
			if (rewardItemCount > 0) {
				Reward r = Reward.valueOf().addItem(RewardType.ITEM, itemId, rewardItemCount);
				r.addItem(RewardType.ITEM, itemId, rewardItemCount);
				ShopResource shopResource = ShopManager.getInstance().getShopResource(autoBuyShopId);
				RewardManager.getInstance().grantReward(player, r,
						ModuleInfo.valueOf(ModuleType.SHOP, SubModuleType.SHOP_BUY, shopResource.getI18name()));
			}
			if (lingerCount == 0) {
				return;
			}
			lingerActions
					.addActions(coreActionManager.getCoreActions(currentItemCount, resource.getLingerMaterialAct()));
			senseAdd = lingerCount * resource.getSenseByLevel(girl.getLevel());

		}

		if (!lingerActions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.SYS_ERROR);
		}
		lingerActions.act(player, ModuleInfo.valueOf(ModuleType.BEAUTY, SubModuleType.BEAUTY_LINGER_ACT, id));

		pool.addSense(id, senseAdd);
	}
}
