package com.mmorpg.mir.model.welfare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.welfare.manager.OfflineManager;
import com.mmorpg.mir.model.welfare.manager.PublicWelfareManager;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Offline_OpenPanel;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Push_Light_Reward;

@Component
public class OfflineExpServiceImpl implements OfflineExpService {

	@Autowired
	private OfflineManager offlineManager;

	/** 打开离线面板 */
	public void openOfflineExp(Player player) {
		PacketSendUtility.sendPacket(player, SM_Welfare_Offline_OpenPanel.valueOf(player));
	}

	/** 领取离线奖励 */
	public void offlineExpReward(Player player, int type) {
		if (player.getWelfare().getOfflineExp().getLoginoutTimeCount() <= 0) {
			throw new ManagedException(ManagedErrorCode.WELFARE_OFFLINE_ZERO);
		}
		if (player.getWelfare().getOfflineExp().rewarded(type)) {
			throw new ManagedException(ManagedErrorCode.WELFARE_OFFLINE_REWARDED);
		}
		
		switch (type) {
		case 1:
			offlineManager.reward1(player);
			break;
		case 2:
			if (!player.getVip().getResource().isDoubleOfflineExp()) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.VIP_CONDITION_NOT_SATISFY);
				return;
			}
			offlineManager.reward2(player);
			break;
		case 3:
			if (!player.getVip().getResource().isUltraOfflineExp()) {
				PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.VIP_CONDITION_NOT_SATISFY);
				return;
			}
			offlineManager.reward3(player);
			break;
		}
		player.getWelfare().getOfflineExp().clear();
		PacketSendUtility.sendPacket(player, SM_Welfare_Push_Light_Reward.valueOf(PublicWelfareManager.getInstance().countLightNum(player)));
	}

}
