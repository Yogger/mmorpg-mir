package com.mmorpg.mir.model.vip.manager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.purse.reward.CurrencyRewardsProvider;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.vip.entity.RechargeRebate;
import com.mmorpg.mir.model.vip.packet.SM_Vip_WeekReward;
import com.mmorpg.mir.model.vip.resource.VipResource;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.orm.Querier;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.DateUtils;

@Component
public class VipManager implements IVipManager {
	@Static
	private Storage<Integer, VipResource> vipResource;

	private static VipManager instance;

	@Autowired
	private RewardManager rewardManager;

	@Inject
	private EntityCacheService<String, RechargeRebate> rechargeRebateDbService;

	private ConcurrentHashMap<String, RechargeRebate> rechargeRebates = new ConcurrentHashMap<String, RechargeRebate>();

	@Autowired
	private PlayerManager playerManager;

	@Autowired
	private Querier querier;

	private static final int RECHARGE_REBATE = 10;

	private static final int RECHARGE_NOVIP_REBATE = 2;

	@PostConstruct
	public void init() {
		instance = this;
		List<RechargeRebate> rechargeRebateEnts = querier.all(RechargeRebate.class);
		for (RechargeRebate ent : rechargeRebateEnts) {
			rechargeRebates.put(ent.getId(), ent);
		}
	}

	public void rebate(Player player) {
		String account = player.getPlayerEnt().getAccountName();
		if (rechargeRebates.containsKey(account)) {
			RechargeRebate rr = rechargeRebates.get(account);
			if (rr.getRebate() == 0) {
				Reward reward = Reward.valueOf();
				int gold = rr.getRecharge() * RECHARGE_REBATE;
				reward.addCurrency(CurrencyType.GOLD, gold);
				for (RewardItem item : reward.getItems()) {
					item.putParms(CurrencyRewardsProvider.VIP_REWARD, "true");
				}
				int noVipGold = rr.getRecharge() * RECHARGE_NOVIP_REBATE;
				reward.addCurrency(CurrencyType.GOLD, noVipGold);

				rewardManager.grantReward(player, reward,
						ModuleInfo.valueOf(ModuleType.REBATE, SubModuleType.RECHARGE_REBATE));
				playerManager.updatePlayer(player);
				rr.setRebate(1);
				rechargeRebateDbService.writeBack(account, rr);
			}
		}
	}

	public void rewardWeekReward(Player player, int sign) {
		if (player.getVip().getResource().getWeekRewardChoosergroup() == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (DateUtils.isSameWeek(new Date(player.getVip().getLastRecivedWeekTime()), Calendar.MONDAY)) {
			throw new ManagedException(ManagedErrorCode.VIP_WEEKREWARD_RECEIVED);
		}
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
				player.getVip().getResource().getWeekRewardChoosergroup());
		Reward reward = rewardManager.creatReward(player, rewardIds, null);
		int needEmpty = rewardManager.calcRewardNeedPackSize(reward);
		if (needEmpty != 0 && player.getPack().getEmptySize() < needEmpty) {
			throw new ManagedException(ManagedErrorCode.PACK_NOT_ENOUGH);
		}
		rewardManager.grantReward(player, reward, ModuleInfo.valueOf(ModuleType.VIP, SubModuleType.VIP_WEEK_REWARD));
		player.getVip().setLastRecivedWeekTime(System.currentTimeMillis());
		player.getVip().setNextRecivedWeekTime(
				DateUtils.getNextTime("0 0 0 * * MON", new Date(player.getVip().getLastRecivedWeekTime())).getTime());
		PacketSendUtility.sendPacket(player, SM_Vip_WeekReward.valueOf(player.getVip().getNextRecivedWeekTime()));
		PacketSendUtility.sendSignMessage(player, sign);
	}

	public void rewardVipLevelReward(Player player, int level, int sign) {
		if (player.getVip().getResource().getVipRewardChoosergroup() == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (player.getVip().getLevel() < level) {
			throw new ManagedException(ManagedErrorCode.VIP_LEVEL_CANNOT_BATCHLADDER);
		}
		if (player.getVip().getReceived().contains(level)) {
			throw new ManagedException(ManagedErrorCode.VIP_LEVEL_REWARDED);
		}

		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
				VipManager.getInstace().getVipResource(level).getVipRewardChoosergroup());
		Reward reward = rewardManager.creatReward(player, rewardIds, null);
		int needEmpty = rewardManager.calcRewardNeedPackSize(reward);
		if (needEmpty != 0 && player.getPack().getEmptySize() < needEmpty) {
			throw new ManagedException(ManagedErrorCode.PACK_NOT_ENOUGH);
		}
		rewardManager.grantReward(player, reward, ModuleInfo.valueOf(ModuleType.VIP, SubModuleType.VIP_LEVEL_REWARD));
		player.getVip().getReceived().add(level);
		PacketSendUtility.sendSignMessage(player, sign);
	}

	public void receiveTempVip(Player player, int sign) {
		player.getVip().addTempVip();
		PacketSendUtility.sendSignMessage(player, sign);
	}

	public static VipManager getInstace() {
		return instance;
	}

	public Storage<Integer, VipResource> getVipResource() {
		return vipResource;
	}

	public void setVipResource(Storage<Integer, VipResource> vipResource) {
		this.vipResource = vipResource;
	}

	public VipResource getVipResource(int level) {
		return vipResource.get(level, true);
	}

}
