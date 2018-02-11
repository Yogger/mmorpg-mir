package com.mmorpg.mir.model.welfare.service;

import java.util.ArrayList;
import java.util.List;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.welfare.event.OnlineRewardEvent;
import com.mmorpg.mir.model.welfare.manager.OnlineManager;
import com.mmorpg.mir.model.welfare.manager.PublicWelfareManager;
import com.mmorpg.mir.model.welfare.packet.SM_Active_Num;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Online_Open;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Online_Reward;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Push_Light_Reward;
import com.mmorpg.mir.model.welfare.packet.vo.OnlineRewardVO;
import com.mmorpg.mir.model.welfare.resource.OnlineResource;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;

@Component
public class OnlineServiceImpl implements OnlineService {

	@Autowired
	private OnlineManager onlineManager;
	@Autowired
	private ChooserManager chooserManager;

	/** 打开在线奖励 */
	public void openOnline(Player player) {
		PacketSendUtility.sendPacket(player, SM_Welfare_Online_Open.valueOf(player, player.getWelfare().getOnlineReward().refreshTime(player)));
		PacketSendUtility.sendPacket(player,
				SM_Welfare_Push_Light_Reward.valueOf(PublicWelfareManager.getInstance().countLightNum(player)));
	}

	/** 领取在线奖励 */
	public void onlineReward(Player player, int[] index) {
		player.getWelfare().getOnlineReward().refreshTime(player);
		ArrayList<OnlineRewardVO> vos = New.arrayList();
		List<String> rewards = New.arrayList();
		for (int i = 0; i < index.length; i++) {
			int k = index[i];
			if (player.getWelfare().getOnlineReward().isRewarded(k)) {
				continue;
			}
			OnlineResource res = onlineManager.getOnlineResource(k);
			if (player.getWelfare().getOnlineReward().lackOnlinetime(res.getOnlineTimeMinutes() * DateUtils.MILLIS_PER_MINUTE)) {
				continue;
			}
			List<String> rewardIds = chooserManager.chooseValueByRequire(player, res.getGroupId());
			player.getWelfare().getOnlineReward().rewarded(k, new ArrayList<String>(rewardIds));
			rewards.addAll(rewardIds);
			vos.add(OnlineRewardVO.valueOf(k, rewardIds.get(0)));
		}
		if (vos.isEmpty()) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.NO_ONLINE_REWARD_AVALIABLE);
			return;
		}
		RewardManager.getInstance().grantReward(player, rewards, ModuleInfo.valueOf(ModuleType.ONLINEREWARD, SubModuleType.ONLINE_REWARD));
		EventBusManager.getInstance().submit(OnlineRewardEvent.valueOf(player.getObjectId()));
		PacketSendUtility.sendPacket(player, SM_Welfare_Online_Reward.valueOf(vos));
		PacketSendUtility.sendPacket(player,
				SM_Welfare_Push_Light_Reward.valueOf(PublicWelfareManager.getInstance().countLightNum(player)));
		PacketSendUtility.sendPacket(player, SM_Active_Num.valueOf(PublicWelfareManager.getInstance().countActiveCount(player)));
	}

}
