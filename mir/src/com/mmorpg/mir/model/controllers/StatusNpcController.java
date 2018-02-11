package com.mmorpg.mir.model.controllers;

import java.util.List;
import java.util.Map;

import org.h2.util.New;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Status;
import com.mmorpg.mir.model.gameobjects.StatusNpc;
import com.mmorpg.mir.model.gameobjects.packet.SM_StatusNpc_Change;
import com.mmorpg.mir.model.trigger.manager.TriggerManager;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class StatusNpcController extends NpcController {

	@Override
	public StatusNpc getOwner() {
		return (StatusNpc) super.getOwner();
	}

	synchronized public void playerChangeStatus(Player player) {
		if (getOwner().getSpawn().getStatusDuration() != 0) {
			if (!getOwner().getDurations().containsKey(player.getObjectId())
					|| getOwner().getDurations().get(player.getObjectId()) > System.currentTimeMillis()) {
				throw new ManagedException(ManagedErrorCode.STATUS_NPC_NO_DURATION);
			}
		}

		List<Status> status = New.arrayList();
		for (Status s : getOwner().getSpawn().getStatus()) {
			if (s.getF() == getOwner().getStatus()) {
				status.add(s);
			}
		}
		for (Status s : status) {
			if (s.getConditions().verify(player)) {
				getOwner().setStatus(s.getT());
				// 通知客服端
				PacketSendUtility.broadcastPacket(getOwner(), SM_StatusNpc_Change.valueOf(getOwner()));
				// 触发器
				if (getOwner().getSpawn().getStatusTriggers() != null
						&& getOwner().getSpawn().getStatusTriggers().containsKey(s.getT())) {
					Map<String, Object> contexts = New.hashMap();
					contexts.put(TriggerContextKey.PLAYER, player);
					contexts.put(TriggerContextKey.STATUS_NPC, this);
					TriggerManager.getInstance().trigger(contexts,
							getOwner().getSpawn().getStatusTriggers().get(s.getT()));
				}
				getOwner().getDurations().clear();
				return;
			}
		}

		throw new ManagedException(ManagedErrorCode.STATUS_NPC_CHANGE);
	}
}
