package com.mmorpg.mir.model.invest.manager;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.invest.InvestConfig;
import com.mmorpg.mir.model.invest.entity.InvestHistoryEntity;
import com.mmorpg.mir.model.invest.model.Invest;
import com.mmorpg.mir.model.invest.model.InvestInfo;
import com.mmorpg.mir.model.invest.model.InvestType;
import com.mmorpg.mir.model.invest.packet.SM_HistoryInfo;
import com.mmorpg.mir.model.invest.packet.SM_Invest_Draw_Reward;
import com.mmorpg.mir.model.invest.resource.InvestResource;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.service.EntityBuilder;
import com.windforce.common.ramcache.service.EntityCacheService;

@Component
public class InvestManager implements IInvestManager {

	private static InvestManager INSTANCE;

	@Autowired
	private InvestConfig config;

	@Autowired
	private RewardManager rewardManager;

	@Autowired
	private CoreActionManager coreActionManager;

	@Inject
	private EntityCacheService<Long, InvestHistoryEntity> cachService;

	private InvestHistoryEntity investHistoryEntity;

	@PostConstruct
	void init() {
		investHistoryEntity = cachService.loadOrCreate(1L, new EntityBuilder<Long, InvestHistoryEntity>() {

			@Override
			public InvestHistoryEntity newInstance(Long id) {
				return InvestHistoryEntity.valueOf(id);
			}
		});
		investHistoryEntity.setEntityCachService(cachService);
		investHistoryEntity.getInvestHistory().setInvestHistoryEnity(investHistoryEntity);
		INSTANCE = this;
	}

	public static InvestManager getInstance() {
		return INSTANCE;
	}

	@Override
	public void buy(Player player, InvestType type) {
		if (player.getInvestPool().getInvests().containsKey(type.getType())) {
			// 已经购买
			throw new ManagedException(type.getErrorCode());
		}

		CoreConditions conditions = InvestConfig.getInstance().getBuyConditions(type);
		if (!conditions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		CoreActions actions = coreActionManager.getCoreActions(1, config.getBuyCostByType(type));
		actions.verify(player, true);
		actions.act(player, ModuleInfo.valueOf(ModuleType.INVEST, type.getActSubmodule()));
		player.getInvestPool().buy(player, type);
		investHistoryEntity.getInvestHistory().addRecord(
				InvestInfo.valueOf(player.getName(), player.getCountryValue(), type.getType()));
	}

	@Override
	public void drawReward(Player player, InvestType type, String resourceId) {
		if (!player.getInvestPool().getInvests().containsKey(type.getType())) {
			// 该类型投资没有购买
			throw new ManagedException(ManagedErrorCode.INVEST_TYPE_NOT_BUY);
		}

		Invest invest = player.getInvestPool().getInvests().get(type.getType());
		if (invest.getRewarded().contains(resourceId)) {
			// 奖励已经领取
			throw new ManagedException(ManagedErrorCode.INVEST_REWARD_DRAW_FAIL);
		}

		InvestResource resource = config.investStorage.get(resourceId, true);
		if (!resource.getConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		rewardManager.grantReward(player, resource.getRewardId(),
				ModuleInfo.valueOf(ModuleType.INVEST, type.getRewardSubmodule(), resourceId));
		invest.getRewarded().add(resourceId);
		PacketSendUtility.sendPacket(player, SM_Invest_Draw_Reward.valueOf(resourceId));
		investHistoryEntity.getInvestHistory().addRecord(
				InvestInfo.valueOf(player.getName(), player.getCountryValue(), type.getType(), resourceId));
	}

	@Override
	public SM_HistoryInfo getHistoryRecord() {
		return investHistoryEntity.getInvestHistory().getHistoryRecords();
	}
}
