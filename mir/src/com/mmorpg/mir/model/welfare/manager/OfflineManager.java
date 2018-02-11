package com.mmorpg.mir.model.welfare.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.h2.util.New;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.FormulaParmsUtil;
import com.mmorpg.mir.model.core.action.CoreActionType;
import com.mmorpg.mir.model.core.action.CurrencyAction;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.rescue.config.RescueConfig;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Offline_Reward;
import com.windforce.common.resource.anno.Static;

@Component
public class OfflineManager implements IOfflineManager {

	private static OfflineManager instance;

	@Static("WELFARE:OFFLINE_EXP_BASIC_UNIT")
	public ConfigValue<Integer> OFFLINE_EXP_BASIC_UNIT;

	@PostConstruct
	public void init() {
		instance = this;
	}

	public static OfflineManager getInstance() {
		return instance;
	}

	/** 第一档经验奖励 */
	public void reward1(Player player) {
		// 第一档,一倍经验
		String keyExp = "WELFARE:OFFLINE_INCOME_EXP_COUNT_1";
		Map<String, Object> map = New.hashMap();
		map.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player));
		map.put("LEVEL", player.getLevel());
		map.put("S", player.getWelfare().getOfflineExp().getOfflineCountSeconds());
		Reward rewardExp = RewardManager.getInstance().creatReward(player, keyExp, map);
		rewardExp = worldLevelExpAddition(player.getLevel(), rewardExp);
		RewardManager.getInstance().grantReward(player, rewardExp, ModuleInfo.valueOf(ModuleType.OFFLINE, SubModuleType.OFFLINE_EXP_REWARD));
		player.getWelfare().getOfflineExp().reward(1);
		PacketSendUtility.sendPacket(player, SM_Welfare_Offline_Reward.valueOf(new int[] { 1, 1 }));
	}

	/** 第2档经验奖励 - 消耗铜币 */
	public void reward2(Player player) {
		// 铜币消耗
		FormulaParmsUtil cunsume = FormulaParmsUtil
				.valueOf(WelfareConfigValueManager.getInstance().OFFLINE_CONSUME_EXP_COUNT_1);
		String CURRENCYRESCUE = RescueConfig.getInstance().CURRENCYRESCUE.getValue();
		List<String> coreCopper = ChooserManager.getInstance().chooseValueByRequire(player, CURRENCYRESCUE);
		cunsume.addParm("CURRENCYRESCUE", coreCopper.get(0));
		cunsume.addParm("LEVEL", player.getLevel());
		cunsume.addParm("S", player.getWelfare().getOfflineExp().getOfflineCountSeconds());
		Integer value = (Integer) cunsume.getValue();
		CurrencyAction action = CoreActionType.createCurrencyCondition(CurrencyType.COPPER, value);
		if (!action.verify(player)) {
			PacketSendUtility.sendPacket(player, SM_Welfare_Offline_Reward.valueOf(new int[] { 2, 0 }));
			return;
		}

		// 扣除铜币
		action.act(player, ModuleInfo.valueOf(ModuleType.OFFLINE, SubModuleType.OFFLINE_EXP_ACT));

		// 奖励双倍经验
		String keyExp = "WELFARE:OFFLINE_INCOME_EXP_COUNT_2";
		Map<String, Object> map = New.hashMap();
		map.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player));
		map.put("LEVEL", player.getLevel());
		map.put("S", player.getWelfare().getOfflineExp().getOfflineCountSeconds());
		Reward rewardExp = RewardManager.getInstance().creatReward(player, keyExp, map);
		rewardExp = worldLevelExpAddition(player.getLevel(), rewardExp);
		RewardManager.getInstance().grantReward(player, rewardExp, ModuleInfo.valueOf(ModuleType.OFFLINE, SubModuleType.OFFLINE_EXP_ACT));
		player.getWelfare().getOfflineExp().reward(2);
		PacketSendUtility.sendPacket(player, SM_Welfare_Offline_Reward.valueOf(new int[] { 2, 1 }));
	}

	/** 第3档经验奖励 - 消耗元宝 */
	public void reward3(Player player) {
		// 元宝消耗
		FormulaParmsUtil cunsume = FormulaParmsUtil
				.valueOf(WelfareConfigValueManager.getInstance().OFFLINE_CONSUME_EXP_COUNT_2);
		cunsume.addParm("S", player.getWelfare().getOfflineExp().getOfflineCountSeconds());
		Integer value = (Integer) cunsume.getValue();
		CurrencyAction action = CoreActionType.createCurrencyCondition(CurrencyType.GOLD, value);
		if (!action.verify(player)) {
			PacketSendUtility.sendPacket(player, SM_Welfare_Offline_Reward.valueOf(new int[] { 3, 0 }));
			return;
		}

		// 扣除元宝
		action.act(player, ModuleInfo.valueOf(ModuleType.OFFLINE, SubModuleType.OFFLINE_EXP_ACT));

		// 奖励3倍经验
		String keyExp = "WELFARE:OFFLINE_INCOME_EXP_COUNT_3";
		Map<String, Object> map = New.hashMap();
		map.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player));
		map.put("LEVEL", player.getLevel());
		map.put("S", player.getWelfare().getOfflineExp().getOfflineCountSeconds());
		Reward rewardExp = RewardManager.getInstance().creatReward(player, keyExp, map);
		rewardExp = worldLevelExpAddition(player.getLevel(), rewardExp);
		RewardManager.getInstance().grantReward(player, rewardExp, ModuleInfo.valueOf(ModuleType.OFFLINE, SubModuleType.OFFLINE_EXP_ACT));
		player.getWelfare().getOfflineExp().reward(3);
		PacketSendUtility.sendPacket(player, SM_Welfare_Offline_Reward.valueOf(new int[] { 3, 1 }));
	}

	private Reward worldLevelExpAddition(int level, Reward originalExp) {
		// 世界等级的加成
		double expAddRate = 0.0;
		if (level >= 90) {
			int dif = WorldRankManager.getInstance().getWorldLevel() - level;
			if (dif > 0) {
				expAddRate += (dif * WorldRankManager.getInstance().WORLD_LEVEL_CONSTANT.getValue() / 100.0);
			}
		}
		if (expAddRate != 0.0) {
			List<RewardItem> source = originalExp.getItemsByType(RewardType.EXP);
			for (RewardItem r : source) {
				r.setAmount((int) (r.getAmount() * (1.0 + expAddRate)));
			}
		}
		return originalExp;
	}

}
