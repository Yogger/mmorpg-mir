package com.mmorpg.mir.model.welfare.manager;

import java.util.Map;

import org.h2.util.New;

import com.mmorpg.mir.model.country.model.countryact.HiddenMissionType;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.model.ClawbackEnum;
import com.mmorpg.mir.model.welfare.model.TagLightEnum;
import com.mmorpg.mir.model.welfare.resource.ClawbackResource;
import com.mmorpg.mir.model.welfare.resource.OnlineResource;
import com.mmorpg.mir.model.welfare.resource.SignResource;
import com.windforce.common.utility.DateUtils;

public class PublicWelfareManager implements IPublicWelfareManager {

	private PublicWelfareManager() {

	}

	private static class A {
		static PublicWelfareManager self = new PublicWelfareManager();
	}

	public static PublicWelfareManager getInstance() {
		return A.self;
	}

	/**
	 * 福利大厅 - 计算前端图标显示可领奖的数量
	 * 
	 * @param player
	 * @return
	 */
	public int countLightNum(Player player) {
		int count = 0;
		// 签到
		if (player.getWelfare().getSign().canSign(System.currentTimeMillis())) {
			count++;
		}
		// 签到领奖
		for (SignResource signRes : SignManager.getInstance().getAllSignResources()) {
			if (player.getWelfare().getSign().getTotalSignNum() < signRes.getDays()) {
				break;
			}
			boolean hasReward = signRes.getDefaultRewadChooserGroup() != null
					&& signRes.getDefaultRewadChooserGroup().length() != 0;
			if (hasReward && !player.getWelfare().getSign().getRewardedList().contains(signRes.getDays())) {
				count++;
				break;
			}
		}

		// 在线奖励
		for (OnlineResource res : OnlineManager.getInstance().onlineStorage.getAll()) {
			long timesCount = player.getWelfare().getOnlineReward().refreshTime(player);
			if (timesCount >= res.getOnlineTimeMinutes() * DateUtils.MILLIS_PER_MINUTE) {
				if (!player.getWelfare().getOnlineReward().isRewarded(res.getId())) {
					count++;
				}
			}
		}
		// 离线奖励
		if (player.getWelfare().getOfflineExp().getOfflineCountSeconds() > 0) {
			count++;
		}

		// 收益追回
		for (ClawbackResource resource : ClawbackManager.getInstance().clawbackStorage.getAll()) {
			if (ClawbackManager.getInstance().canClawback(player, resource)) {
				count += 1;
			}
		}

		return count;
	}

	public int countActiveCount(Player player) {
		int count = 0;
		// 活跃值
		Integer[] values = WelfareConfigValueManager.getInstance().WELFARE_VITALITY_AWARD_NUM.getValue();
		for (int v : values) {
			if (player.getWelfare().getActiveValue().getActiveValue() >= v
					&& !player.getWelfare().getActiveValue().isRewarded(v)) {
				count++;
			}
		}
		return count;
	}

	public boolean activeRewardAllRecieved(Player player) {
		Integer[] values = WelfareConfigValueManager.getInstance().WELFARE_VITALITY_AWARD_NUM.getValue();
		for (int v : values) {
			if (!player.getWelfare().getActiveValue().isRewarded(v)) {
				return false;
			}
		}
		return true;
	}

	public boolean tagLight(Player player, int tagId) {
		TagLightEnum type = TagLightEnum.valueOf(tagId);
		return type.getStatus(player);
	}

	public Map<Integer, Boolean> getTagStatus(Player player) {
		Map<Integer, Boolean> result = New.hashMap();
		for (TagLightEnum type : TagLightEnum.values()) {
			result.put(type.getValue(), type.getStatus(player));
		}
		return result;
	}

	/**
	 * 玩家当前的事件执行次数扣除福利大厅追回的次数
	 * 
	 * @param player
	 * @param eventId
	 *            事件Id @see {@link ClawbackEnum}
	 * @param num
	 *            追回的次数
	 */
	public void reduceClawbackNum(Player player, int eventId, int num) {
		if (eventId == ClawbackEnum.CLAWBACK_EVENT_INVESTIGATE.getEventId()) {
			player.getInvestigate().setCount(player.getInvestigate().getCount() - num);
		} else if (eventId == ClawbackEnum.CLAWBACK_EVENT_EXPRESS.getEventId()) {
			player.getExpress().setLorryCount(player.getExpress().getLorryCount() - num);
		} else if (eventId == ClawbackEnum.CLAWBACK_EVENT_TEMPLE.getEventId()) {
			player.getTempleHistory().setCount(player.getTempleHistory().getCount() - num);
		} else if (eventId == ClawbackEnum.CLAWBACK_EVENT_RESCUE.getEventId()) {
			// 营救是任务,需要任务模块配合
		} else if (eventId == ClawbackEnum.CLAWBACK_EVENT_KILL_DISPLOMACY.getEventId()) {
			player.getPlayerCountryHistory().clawbackCountryMission(HiddenMissionType.CUT_DIPOMACY_DOWN, num);
		} else if (eventId == ClawbackEnum.CLAWBACK_EVENT_KILL_FLAG.getEventId()) {
			player.getPlayerCountryHistory().clawbackCountryMission(HiddenMissionType.DEFEND_FLAG, num);
		}
	}

}
