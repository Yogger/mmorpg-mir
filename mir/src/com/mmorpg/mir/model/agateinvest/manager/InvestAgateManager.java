package com.mmorpg.mir.model.agateinvest.manager;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.agateinvest.InvestAgateConfig;
import com.mmorpg.mir.model.agateinvest.entity.InvestAgateHistoryEntity;
import com.mmorpg.mir.model.agateinvest.model.InvestAgate;
import com.mmorpg.mir.model.agateinvest.model.InvestAgateInfo;
import com.mmorpg.mir.model.agateinvest.model.InvestAgateType;
import com.mmorpg.mir.model.agateinvest.packet.SM_Agate_HistoryInfo;
import com.mmorpg.mir.model.agateinvest.packet.SM_Agate_Invest_Draw_Reward;
import com.mmorpg.mir.model.agateinvest.resource.InvestAgateResource;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.service.EntityBuilder;
import com.windforce.common.ramcache.service.EntityCacheService;

@Component
public class InvestAgateManager implements IInvestAgateManager {

	private static InvestAgateManager INSTANCE;

	@Autowired
	private InvestAgateConfig config;

	@Autowired
	private RewardManager rewardManager;

	@Autowired
	private CoreActionManager coreActionManager;

	@Inject
	private EntityCacheService<Long, InvestAgateHistoryEntity> cachService;

	private InvestAgateHistoryEntity investHistoryEntity;

	@PostConstruct
	void init() {
		investHistoryEntity = cachService.loadOrCreate(1L, new EntityBuilder<Long, InvestAgateHistoryEntity>() {

			@Override
			public InvestAgateHistoryEntity newInstance(Long id) {
				return InvestAgateHistoryEntity.valueOf(id);
			}
		});
		investHistoryEntity.setEntityCachService(cachService);
		investHistoryEntity.getInvestHistory().setInvestHistoryEnity(investHistoryEntity);
		INSTANCE = this;
	}

	public static InvestAgateManager getInstance() {
		return INSTANCE;
	}

	@Override
	public void buy(Player player, InvestAgateType type) {
		if (player.getInvestAgatePool().getInvests().containsKey(type.getType())) {
			// 已经购买
			throw new ManagedException(type.getErrorCode());
		}

		CoreConditions conditions = InvestAgateConfig.getInstance().getBuyConditions(type);
		if (!conditions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		CoreActions actions = coreActionManager.getCoreActions(1, config.getBuyCostByType(type));
		actions.verify(player, true);
		actions.act(player, ModuleInfo.valueOf(ModuleType.INVESTAGATE, type.getActSubmodule()));
		player.getInvestAgatePool().buy(player, type);
		investHistoryEntity.getInvestHistory().addRecord(
				InvestAgateInfo.valueOf(player.getName(), player.getCountryValue(), type.getType()));
	}

	@Override
	public void drawReward(Player player, InvestAgateType type, String resourceId) {
		if (!player.getInvestAgatePool().getInvests().containsKey(type.getType())) {
			// 该类型投资没有购买
			throw new ManagedException(ManagedErrorCode.INVEST_TYPE_NOT_BUY);
		}

		InvestAgate invest = player.getInvestAgatePool().getInvests().get(type.getType());
		if (invest.getRewarded().contains(resourceId)) {
			// 奖励已经领取
			throw new ManagedException(ManagedErrorCode.INVEST_REWARD_DRAW_FAIL);
		}

		InvestAgateResource resource = config.investStorage.get(resourceId, true);
		if (!resource.getConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		rewardManager.grantReward(player, resource.getRewardId(),
				ModuleInfo.valueOf(ModuleType.INVESTAGATE, type.getRewardSubmodule(), resourceId));
		invest.getRewarded().add(resourceId);
		PacketSendUtility.sendPacket(player, SM_Agate_Invest_Draw_Reward.valueOf(resourceId));
		investHistoryEntity.getInvestHistory().addRecord(
				InvestAgateInfo.valueOf(player.getName(), player.getCountryValue(), type.getType(), resourceId));
	}

	@Override
	public SM_Agate_HistoryInfo getHistoryRecord() {
		return investHistoryEntity.getInvestHistory().getHistoryRecords();
	}
}
