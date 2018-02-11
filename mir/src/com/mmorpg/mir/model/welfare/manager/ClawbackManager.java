package com.mmorpg.mir.model.welfare.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.welfare.event.RescueClawbackEvent;
import com.mmorpg.mir.model.welfare.model.ActionCurrencyType;
import com.mmorpg.mir.model.welfare.model.ClawbackEnum;
import com.mmorpg.mir.model.welfare.model.ClawbackType;
import com.mmorpg.mir.model.welfare.model.ClawbackValue;
import com.mmorpg.mir.model.welfare.resource.ClawbackResource;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.DateUtils;

@Component
public class ClawbackManager implements IClawbackManager {

	private static ClawbackManager instance;
	@Autowired
	private ChooserManager chooserManager;

	@PostConstruct
	public void init() {
		instance = this;
	}

	public static ClawbackManager getInstance() {
		return instance;
	}

	@Static
	public Storage<Integer, ClawbackResource> clawbackStorage;

	public ClawbackResource getClawbackResource(int eventId) {
		return clawbackStorage.get(eventId, true);
	}

	public int getCorrectExeNums(ClawbackResource resource, int level) {
		Map<Integer, Integer> map = resource.getVipExtraCount();
		int count = resource.getExeNum();
		if (map != null && map.containsKey(level)) {
			count += map.get(level);
		}
		return count;
	}

	public int getCorrectExeRuns(ClawbackResource resource, int level) {
		Map<Integer, Integer> map = resource.getVipExtraCount();
		int count = resource.getRuns();
		if (map != null && map.containsKey(level)) {
			count += map.get(level);
		}
		return count;
	}

	/**
	 * 扣钱
	 * 
	 * @param player
	 * @param currencyType
	 * @param resource
	 * @param throwException
	 * @return
	 */
	public boolean reduceCurrency(Player player, int currencyType, ClawbackResource resource, boolean throwException) {
		String groupId = "";
		if (currencyType == ActionCurrencyType.ACTION_COPPER.getType()) {
			groupId = resource.getCopperActionGroupId();
		} else if (currencyType == ActionCurrencyType.ACTION_GOLD.getType()) {
			groupId = resource.getGoldActionGroupId();
		}
		List<String> result = chooserManager.chooseValueByRequire(player, groupId);
		String[] results = new String[result.size()];
		for (int i = 0; i < result.size(); i++) {
			results[i] = result.get(i);
		}
		int clawbackType = resource.getType();
		int size = 0;
		if (clawbackType == ClawbackType.CLAWBACK_TYPE_VALUE.getType()) {// 资源
			int maxRuns = ClawbackManager.getInstance()
					.getCorrectExeRuns(resource, player.getVip().getYesterdayLevel());
			size = maxRuns - player.getWelfare().getOldlawback().getRunNum(resource.getEventId());
			/*if (resource.getEventId() == ClawbackEnum.CLAWBACK_EVENT_LADDER.getEventId()) {
				Long key = DayKey.valueOf().getLunchTime() - DateUtils.MILLIS_PER_DAY;
				boolean yesterdayRest = player.getWelfare().getWelfareHistory().getLastRestTime().containsKey(key);
				if (!yesterdayRest) {
					size = 1;
				} else {
					return false;
				}
			}*/
		} else if (clawbackType == ClawbackType.CLAWBACK_TYPE_NUM.getType()) {
			int maxExeNum = ClawbackManager.getInstance().getCorrectExeNums(resource, player.getVip().getYesterdayLevel());
			size = maxExeNum - player.getWelfare().getOldlawback().getExeNum(resource.getEventId());
		}
		CoreActions actions = CoreActionManager.getInstance().getCoreActions(size, results);
		boolean succ = actions.verify(player, throwException);
		if (succ) {
			actions.act(player, ModuleInfo.valueOf(ModuleType.CLAWBACK, SubModuleType.CLAWBACK_ACT));
		}
		return succ;
	}

	public void rewardBeatch(Player player, int clawbackType, int currencyType, List<Integer> eventIds) {
		for (int eventId : eventIds) {
			ClawbackResource resource = getClawbackResource(eventId);
			reward(player, clawbackType, currencyType, resource);
		}
	}

	public void reward(Player player, int clawbackType, int currencyType, ClawbackResource resource) {
		if (clawbackType == ClawbackType.CLAWBACK_TYPE_NUM.getType()) {
			rewardNum(player, resource);
		} else if (clawbackType == ClawbackType.CLAWBACK_TYPE_VALUE.getType()) {
			rewardValue(currencyType, player, resource);
		}
	}

	/**
	 * 资源追回
	 * 
	 * @param player
	 * @param resource
	 */
	private void rewardValue(int currencyType, Player player, ClawbackResource resource) {
		int eventId = resource.getEventId();
		// 奖励
		String rewardGroupId = resource.getRewardGroupId();
		List<String> rewardResult = chooserManager.chooseValueByRequire(player, rewardGroupId);
		// 相差的环数
		int count = 0;
		// 爬塔的环数就是之前自己做过的最高的环数
		int maxRuns = ClawbackManager.getInstance().getCorrectExeRuns(resource, player.getVip().getYesterdayLevel());
		count = maxRuns - player.getWelfare().getOldlawback().getRunNum(eventId);
		/*if (eventId == ClawbackEnum.CLAWBACK_EVENT_LADDER.getEventId()) {
			Long key = DayKey.valueOf().getLunchTime() - DateUtils.MILLIS_PER_DAY;

			if (!player.getWelfare().getWelfareHistory().getLastRestTime().containsKey(key)) {
				// count = player.getCopyHistory().getLadderCompleteIndex();
				count = 1;
			}
		}*/
		String standardExp = PlayerManager.getInstance().getStandardExp(player);
		String standardCoins = PlayerManager.getInstance().getStandardCoins(player);
		Map<String, Object> params = New.hashMap();
		params.put("LEVEL", player.getLevel());
		params.put("STANDARD_EXP", standardExp);
		params.put("STANDARD_COINS", standardCoins);
		Reward onePiece = RewardManager.getInstance().creatReward(player, rewardResult, params);
		Reward reward = Reward.valueOf();
		for (int i = 0; i < count; i++) {
			reward.addReward(onePiece.copy());
		}
		if (currencyType == ActionCurrencyType.ACTION_COPPER.getType()) {
			reward.divideIntoTwoPieces(0.30d, new RewardType[] { RewardType.EXP, RewardType.CURRENCY, RewardType.ITEM });
		}
		RewardManager.getInstance().grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.CLAWBACK, SubModuleType.CLAW_BACK));
		// 记录追回
		player.getWelfare().getWelfareHistory().clawback(ClawbackEnum.valueOf(eventId), count);
	}

	/**
	 * 次数追回
	 * 
	 * @param player
	 * @param resource
	 */
	private void rewardNum(Player player, ClawbackResource resource) {
		int maxExeNum = getCorrectExeNums(resource, player.getVip().getYesterdayLevel());
		int count = maxExeNum - player.getWelfare().getOldlawback().getExeNum(resource.getEventId());
		PublicWelfareManager.getInstance().reduceClawbackNum(player, resource.getEventId(), count);
		// 记录追回
		player.getWelfare().getWelfareHistory().clawback(ClawbackEnum.valueOf(resource.getEventId()), count);
		if (resource.getEventId() == ClawbackEnum.CLAWBACK_EVENT_RESCUE.getEventId()) {
			EventBusManager.getInstance().submit(RescueClawbackEvent.valueOf(player.getObjectId(), count));
		}
		
	/*	先找回再做和 先做再找回不一样， 如果要一样，把这段代码打开即可  （基佬炫的需求)
	    ClawbackValue todayValue = player.getWelfare().getNewClawback().getClawbackValue(resource.getEventId());
		int todayExe = todayValue.getCurrentNum();
		if (todayExe > 0 && todayExe > count) {
			todayValue.logUseClawbackNum(count);
			todayValue.setCurrentNum(todayExe - count);
		} else if (todayExe > 0){
			todayValue.logUseClawbackNum(Math.abs(todayExe - count));
			todayValue.setCurrentNum(0);
		}*/
		
	}

	public void checkCondition(Player player) {
		for (ClawbackEnum claw : ClawbackEnum.values()) {
			ClawbackResource resource = ClawbackManager.getInstance().getClawbackResource(claw.getEventId());

			if (ModuleOpenManager.getInstance().isOpenByKey(player, resource.getOpenModuleId())) {
				if (!player.getWelfare().getOldlawback().isOpen(claw.getEventId())) {
					player.getWelfare().getOldlawback().open(claw.getEventId());
				}
				if (!player.getWelfare().getNewClawback().isOpen(claw.getEventId())) {
					player.getWelfare().getNewClawback().open(claw.getEventId());
				}
			}
		}
	}

	// 开启
	public boolean isOpen(Player player, int eventId) {
		return player.getWelfare().getOldlawback().isOpen(eventId)
				|| player.getWelfare().getNewClawback().isOpen(eventId);
	}

	// 已追回
	public boolean isClawbacked(Player player, int eventId) {
		return player.getWelfare().getWelfareHistory().getClawbackNum(ClawbackEnum.valueOf(eventId)) > 0;
	}

	// 验证
	public boolean pass(Player player, ClawbackResource resource) {
		int eventId = resource.getEventId();
		// 未到第二天
		Date openDate = new Date(player.getWelfare().getNewClawback().getOpenTime(resource.getEventId()));
		if (DateUtils.calcIntervalDays(openDate, new Date()) < 1) {
			return false;
		}
		/*// 爬塔 - 名将试炼 ,没做过不可追回
		if (eventId == ClawbackEnum.CLAWBACK_EVENT_LADDER.getEventId()) {
			return player.getWelfare().getWelfareHistory().isPass()
					&& player.getCopyHistory().getLadderHisCompleteIndex() > 0;
		}*/
		// 进度对比
		int type = resource.getType();
		if (type == ClawbackType.CLAWBACK_TYPE_VALUE.getType()) {
			int runs = player.getWelfare().getOldlawback().getRunNum(eventId);
			int maxRuns = ClawbackManager.getInstance()
					.getCorrectExeRuns(resource, player.getVip().getYesterdayLevel());
			return runs < maxRuns;
		} else if (type == ClawbackType.CLAWBACK_TYPE_NUM.getType()) {
			int exeNum = player.getWelfare().getOldlawback().getExeNum(eventId);
			int maxNum = ClawbackManager.getInstance().getCorrectExeNums(resource, player.getVip().getYesterdayLevel());
			return exeNum < maxNum;
		} else {
			throw new RuntimeException("canClawbackReward error [" + player.getObjectId() + " , " + player.getName()
					+ " , " + eventId + "]");
		}
	}

	/**
	 * 是否能追回收益
	 * 
	 * @param player
	 * @param resource
	 * @return
	 */
	public boolean canClawback(Player player, ClawbackResource resource) {
		int eventId = resource.getEventId();
		return isOpen(player, eventId) && !isClawbacked(player, eventId) && pass(player, resource);
	}

	/**
	 * 如果换了一天,把今天数据 放到昨天数据
	 * 
	 * @param player
	 * @param eventId
	 */
	public void checkTimeOut(Player player, int eventId) {
		long newClawbackLastTime = player.getWelfare().getNewClawback().getLastExeTime(eventId);
		int calcIntervalDaysNew = DateUtils.calcIntervalDays(new Date(newClawbackLastTime), new Date());
		if (calcIntervalDaysNew > 0) {
			ClawbackValue newValue = player.getWelfare().getNewClawback().getClawbackValue(eventId);
			player.getWelfare().getOldlawback().set(eventId, newValue);
			newValue.clear();
		}
		long oldClawbackLastTime = player.getWelfare().getOldlawback().getLastExeTime(eventId);
		int calcIntervalDaysOld = DateUtils.calcIntervalDays(new Date(oldClawbackLastTime), new Date());
		if (calcIntervalDaysOld > 1) {
			ClawbackValue oldValue = player.getWelfare().getOldlawback().getClawbackValue(eventId);
			oldValue.clear();
		}
	}

	/**
	 * 执行进度
	 * 
	 * @param player
	 * @param claw
	 *            see {@link ClawbackEnum}
	 */
	public void exec(Player player, ClawbackEnum claw, int count) {
		int eventId = claw.getEventId();
		ClawbackResource resource = getClawbackResource(eventId);
		checkTimeOut(player, eventId);
		int type = resource.getType();
		if (type == ClawbackType.CLAWBACK_TYPE_VALUE.getType()) {
			player.getWelfare().getNewClawback().addRun(player, resource, eventId, count);
		} else if (type == ClawbackType.CLAWBACK_TYPE_NUM.getType()) {
			player.getWelfare().getNewClawback().addNum(player, resource, eventId, count);
		} else {
			throw new RuntimeException("clawback exe error [" + player.getObjectId() + " , " + player.getName() + " ,"
					+ claw + "]");
		}
	}

	public void exec(Player player, ClawbackEnum claw) {
		exec(player, claw, 1);
	}
}
