package com.mmorpg.mir.model.welfare.service;

import java.util.ArrayList;
import java.util.Map;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.welfare.event.ClawbackEvent;
import com.mmorpg.mir.model.welfare.manager.ClawbackManager;
import com.mmorpg.mir.model.welfare.manager.PublicWelfareManager;
import com.mmorpg.mir.model.welfare.model.ActionCurrencyType;
import com.mmorpg.mir.model.welfare.model.ClawbackEnum;
import com.mmorpg.mir.model.welfare.model.ClawbackType;
import com.mmorpg.mir.model.welfare.packet.SM_Active_Num;
import com.mmorpg.mir.model.welfare.packet.SM_Tag_Light;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Clawback_Open;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Clawback_Push_Back;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Clawback_Reward;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Clawback_Reward_Auto;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Clawback_Reward_Superme;
import com.mmorpg.mir.model.welfare.packet.SM_Welfare_Push_Light_Reward;
import com.mmorpg.mir.model.welfare.resource.ClawbackResource;
import com.windforce.common.event.core.EventBusManager;

@Component
public class ClawbackServiceImpl implements ClawbackService {

	@Autowired
	private ClawbackManager clawbackManager;

	/** 查看追回收益 */
	public void openClawback(Player player) {
		refreshClawbackData(player);
		// 返回旧数据
		PacketSendUtility.sendPacket(player, SM_Welfare_Clawback_Open.value(player));
	}
	
	public void refreshClawbackData(Player player) {
		clawbackManager.checkCondition(player);
		for (ClawbackEnum clawbackEnum : ClawbackEnum.values()) {
			int eventId = clawbackEnum.getEventId();
			clawbackManager.checkTimeOut(player, eventId);
		}
	}
	
	/** 追回收益 */
	public void clawback(Player player, int eventId, int currencyType) {
		refreshClawbackData(player);
		ClawbackResource resource = clawbackManager.getClawbackResource(eventId);
		if (!clawbackManager.canClawback(player, resource)) {
			throw new ManagedException(ManagedErrorCode.WELFARE_CLAWBACK_BACK_IS_NULL);
		}
		int clawbackType = resource.getType();
		// 验证追回方式是否正确
		if (clawbackType == ClawbackType.CLAWBACK_TYPE_NUM.getType()
				&& currencyType == ActionCurrencyType.ACTION_GOLD.getType()) {
			throw new ManagedException(ManagedErrorCode.WELFARE_CLAWBACK_CAN_NOT_USE_GOLD);
		}
		// 消耗
		clawbackManager.reduceCurrency(player, currencyType, resource, true);
		// 奖励
		clawbackManager.reward(player, clawbackType, currencyType, resource);
		PacketSendUtility.sendPacket(player, SM_Welfare_Clawback_Reward.valueOf(eventId));
		PacketSendUtility.sendPacket(player,
				SM_Welfare_Push_Light_Reward.valueOf(PublicWelfareManager.getInstance().countLightNum(player)));
		PacketSendUtility.sendPacket(player, SM_Active_Num.valueOf(PublicWelfareManager.getInstance().countActiveCount(player)));
		PacketSendUtility.sendPacket(player, SM_Welfare_Clawback_Push_Back.valueOf(player));
		EventBusManager.getInstance().submit(ClawbackEvent.valueOf(player, ClawbackEnum.valueOf(eventId)));
	}

	/** 一键追回 */
	public void autoClawback(Player player, int clawbackType) {
		refreshClawbackData(player);
		ArrayList<Integer> eventIds = New.arrayList();
		for (ClawbackEnum clawbackEnum : ClawbackEnum.values()) {
			int eventId = clawbackEnum.getEventId();
			ClawbackResource resource = clawbackManager.getClawbackResource(eventId);
			if (clawbackType != resource.getType() || !clawbackManager.canClawback(player, resource)) {
				continue;
			}
			if (clawbackManager.reduceCurrency(player, ActionCurrencyType.ACTION_COPPER.getType(), resource, false))
				eventIds.add(eventId);
		}
		clawbackManager.rewardBeatch(player, clawbackType, ActionCurrencyType.ACTION_COPPER.getType(), eventIds);
		PacketSendUtility.sendPacket(player, SM_Welfare_Clawback_Reward_Auto.valueOf(eventIds));
		PacketSendUtility.sendPacket(player,
				SM_Welfare_Push_Light_Reward.valueOf(PublicWelfareManager.getInstance().countLightNum(player)));
		PacketSendUtility.sendPacket(player, SM_Active_Num.valueOf(PublicWelfareManager.getInstance().countActiveCount(player)));
		PacketSendUtility.sendPacket(player, SM_Welfare_Clawback_Push_Back.valueOf(player));
		
		for (Integer eventId : eventIds) {
			EventBusManager.getInstance().submit(ClawbackEvent.valueOf(player, ClawbackEnum.valueOf(eventId)));
		}
	}

	/** 至尊追回 */
	public void supermeClawback(Player player, int clawbackType) {
		refreshClawbackData(player);
		if (clawbackType == ClawbackType.CLAWBACK_TYPE_NUM.getType()) {
			throw new ManagedException(ManagedErrorCode.WELFARE_CLAWBACK_CAN_NOT_CLAWBACK_NUM);
		}
		ArrayList<Integer> eventIds = New.arrayList();
		for (ClawbackEnum clawbackEnum : ClawbackEnum.values()) {
			int eventId = clawbackEnum.getEventId();
			ClawbackResource resource = clawbackManager.getClawbackResource(eventId);
			if (resource.getType() != clawbackType || !clawbackManager.canClawback(player, resource)) {
				continue;
			}
			if (clawbackManager.reduceCurrency(player, ActionCurrencyType.ACTION_GOLD.getType(), resource, false))
				eventIds.add(eventId);
		}
		clawbackManager.rewardBeatch(player, clawbackType, ActionCurrencyType.ACTION_GOLD.getType(), eventIds);
		PacketSendUtility.sendPacket(player, SM_Welfare_Clawback_Reward_Superme.valueOf(eventIds));
		PacketSendUtility.sendPacket(player,
				SM_Welfare_Push_Light_Reward.valueOf(PublicWelfareManager.getInstance().countLightNum(player)));
		PacketSendUtility.sendPacket(player, SM_Active_Num.valueOf(PublicWelfareManager.getInstance().countActiveCount(player)));
		PacketSendUtility.sendPacket(player, SM_Welfare_Clawback_Push_Back.valueOf(player));
		
		for (Integer eventId : eventIds) {
			EventBusManager.getInstance().submit(ClawbackEvent.valueOf(player, ClawbackEnum.valueOf(eventId)));
		}
	}

	public void getTagLight(Player player, int code) {
		Map<Integer, Boolean> ret = null;
		if (code != 0) {
			ret = New.hashMap();
			ret.put(code, PublicWelfareManager.getInstance().tagLight(player, code));
			PacketSendUtility.sendPacket(player, SM_Tag_Light.valueOf(ret));
		} else {
			PacketSendUtility.sendPacket(player,
					SM_Tag_Light.valueOf(PublicWelfareManager.getInstance().getTagStatus(player)));
		}
	}

}
