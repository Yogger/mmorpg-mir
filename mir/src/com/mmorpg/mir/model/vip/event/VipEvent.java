package com.mmorpg.mir.model.vip.event;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.vip.resource.VipResource;
import com.windforce.common.event.event.IEvent;

/**
 * Vip升级
 * 
 * @author Kuang Hao
 * @since v1.0 2014-12-22
 * 
 */
public class VipEvent implements IEvent {

	private long playerId;
	private VipResource vipResource;
	private int beforeUpgradeVip;

	public static IEvent valueOf(Player player, VipResource vipResource, int level) {
		VipEvent event = new VipEvent();
		event.setPlayerId(player.getObjectId());
		event.setVipResource(vipResource);
		event.beforeUpgradeVip = level;
		return event;
	}

	@Override
	public long getOwner() {
		return playerId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public VipResource getVipResource() {
		return vipResource;
	}

	public void setVipResource(VipResource vipResource) {
		this.vipResource = vipResource;
	}

	public int getBeforeUpgradeVip() {
		return beforeUpgradeVip;
	}

	public void setBeforeUpgradeVip(int beforeUpgradeVip) {
		this.beforeUpgradeVip = beforeUpgradeVip;
	}

}
