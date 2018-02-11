package com.mmorpg.mir.model.collect.reward;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.collect.packet.SM_FamedGeneral_Update;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.New;

@Component
public class EquipActiveRewardProvider extends RewardProvider{
	
	public static final String OWNERKEY = "OWNERKEY";

	@Override
	public RewardType getType() {
		return RewardType.ACTIVE_EQUIPSOUL;
	}

	@Override
	public void withdraw(Player player, RewardItem rewardItem, ModuleInfo module) {
		String equipCreateSoulKey = rewardItem.getCode();
		String ownerKey = rewardItem.getParms().get(OWNERKEY);
		if (player.getCollect().getFamedGeneral().collect(player, ownerKey, equipCreateSoulKey)) {
			Map<String, ArrayList<String>> map = New.hashMap();
			ArrayList<String> addNew = New.arrayList();
			addNew.add(equipCreateSoulKey);
			map.put(ownerKey, addNew);
			PacketSendUtility.sendPacket(player, SM_FamedGeneral_Update.valueOf(map));
		} else {
			throw new ManagedException(ManagedErrorCode.EQUIP_SOUL_ALREADY_ENOUGH);
		}
	}

}
