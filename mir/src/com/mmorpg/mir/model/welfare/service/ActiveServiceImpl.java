package com.mmorpg.mir.model.welfare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.welfare.manager.ActiveManager;
import com.mmorpg.mir.model.welfare.manager.GiftCollectManage;
import com.mmorpg.mir.model.welfare.manager.PublicWelfareManager;
import com.mmorpg.mir.model.welfare.packet.SM_Active_Num;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Active_Open_Panel;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Active_Reward;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Push_Light_Reward;
import com.mmorpg.mir.model.welfare.resource.ActiveRewardResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class ActiveServiceImpl implements ActiveService {

	@Autowired
	private ActiveManager activeManager;
	@Autowired
	private ChooserManager chooserManager;
	@Autowired
	private ChatManager chatManager;
	@Autowired
	private GiftCollectManage giftCollectManager;

	@Static
	public Storage<Integer, ActiveRewardResource> activeRewardStorage;

	public void openActiveValue(Player player) {
		activeManager.check(player);
		PacketSendUtility.sendPacket(player, SM_Welfare_Active_Open_Panel.valueOf(player));
	}

	public void activeValueReward(Player player, int value) {
		int v = player.getWelfare().getActiveValue().getActiveValue();
		if (v < value) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.WELFARE_ACTIVE_VALUE_NOT_ENGOUTH);
			return;
		}
		if (player.getWelfare().getActiveValue().isRewarded(value)) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.WELFARE_ACTIVE_REWARDED);
			return;
		}
		ActiveRewardResource resource = activeManager.getActiveRewardResource(value);
		String groupId = resource.getGroupId();
		List<String> result = chooserManager.chooseValueByRequire(player, groupId);
		RewardManager.getInstance().grantReward(player, result,
				ModuleInfo.valueOf(ModuleType.ACTIVEVALUE, SubModuleType.ACTIVE_VALUE_REWARD));
		player.getWelfare().getActiveValue().reward(value);
		PacketSendUtility.sendPacket(player, SM_Welfare_Active_Reward.valueOf(value));
		PacketSendUtility.sendPacket(player,
				SM_Welfare_Push_Light_Reward.valueOf(PublicWelfareManager.getInstance().countLightNum(player)));
		PacketSendUtility.sendPacket(player,
				SM_Active_Num.valueOf(PublicWelfareManager.getInstance().countActiveCount(player)));
		player.getWelfare().getGiftCollect().addAmount(resource.getGiftCount());
		PacketSendUtility.sendPacket(player, giftCollectManager.getGiftCollect_Open(player));
		if (player.getWelfare().getActiveValue().isFinishDrawAllReward()) {
			I18nUtils i18nUtils = I18nUtils.valueOf("80101");
			i18nUtils.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
			i18nUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			chatManager.sendSystem(11001, i18nUtils, null);
			
			I18nUtils i18nUtils2 = I18nUtils.valueOf("310101",i18nUtils);
			chatManager.sendSystem(0, i18nUtils2, null);
		}
	}

}
