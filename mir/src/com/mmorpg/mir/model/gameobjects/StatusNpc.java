package com.mmorpg.mir.model.gameobjects;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.h2.util.New;

import com.mmorpg.mir.model.controllers.StatusNpcController;
import com.mmorpg.mir.model.gameobjects.packet.SM_StatusNpc_Change;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.WorldPosition;

public class StatusNpc extends Npc {

	/** 当前的状态 */
	private int status;
	/** 持续时间 */
	private Map<Long, Long> durations = New.hashMap();

	public StatusNpc(long objId, StatusNpcController controller, WorldPosition position) {
		super(objId, controller, position);
		controller.setOwner(this);
	}

	synchronized public void addDuration(long playerId, long endTime) {
		// 删除过期的
		List<Long> removes = New.arrayList();
		for (Entry<Long, Long> entry : getDurations().entrySet()) {
			if ((entry.getValue() + 5000) < System.currentTimeMillis()) {
				removes.add(entry.getKey());
			}
		}
		for (long rid : removes) {
			getDurations().remove(rid);
		}
		getDurations().put(playerId, endTime);
	}

	synchronized public boolean removeDuration(long playerId) {
		if (getDurations().containsKey(playerId)) {
			getDurations().remove(playerId);
			return true;
		}
		return false;
	}

	public int getStatus() {
		return status;
	}

	synchronized public void setStatus(int status) {
		this.status = status;
		// 通知客服端
		PacketSendUtility.broadcastPacket(this, SM_StatusNpc_Change.valueOf(this));
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.STATUS_NPC;
	}

	public Map<Long, Long> getDurations() {
		return durations;
	}

	public void setDurations(Map<Long, Long> durations) {
		this.durations = durations;
	}

	@Override
	public StatusNpcController getController() {
		return (StatusNpcController) super.getController();
	}

	@Override
	public int getCountryValue() {
		return getCountry().getValue();
	}
	
}
