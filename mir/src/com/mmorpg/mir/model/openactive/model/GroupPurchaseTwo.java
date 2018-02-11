package com.mmorpg.mir.model.openactive.model;

import java.util.HashSet;
import java.util.Map;

import org.cliffc.high_scale_lib.NonBlockingHashSet;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.OpenActiveConfig;
import com.mmorpg.mir.model.openactive.packet.SM_GroupPurchase_Two_Attend_Satisfy;
import com.mmorpg.mir.model.openactive.packet.SM_GroupPurchase_Two_Gold_Change;
import com.mmorpg.mir.model.openactive.resource.GroupPurchaseTwoResource;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class GroupPurchaseTwo {
	/** 已经领取充值数 */
	private HashSet<String> rewarded;

	/** 活动期间充值数 */
	private long goldAmount;

	public static GroupPurchaseTwo valueOf() {
		GroupPurchaseTwo result = new GroupPurchaseTwo();
		result.rewarded = New.hashSet();
		return result;
	}

	@JsonIgnore
	public void addGold(Player player, long amount) {
		this.goldAmount += amount;
		Map<String, NonBlockingHashSet<Long>> groupPurchasePlayers = ServerState.getInstance()
				.getGroupPurchasePlayers2();
		if (groupPurchasePlayers == null) {
			return;
		}
		for (GroupPurchaseTwoResource resource : OpenActiveConfig.getInstance().groupPurchaseTwoStorage.getAll()) {
			boolean attendGroupPurchase = groupPurchasePlayers.get(resource.getId()).contains(player.getObjectId());
			if (!attendGroupPurchase && resource.getAttendConditions().verify(player.getObjectId())) {
				groupPurchasePlayers.get(resource.getId()).add(player.getObjectId());
				boolean attendAmountVerify = resource.getSendConditions().verify(null);
				if (attendAmountVerify) {
					sendAttendPlayerPacket(
							resource.getId(),
							SM_GroupPurchase_Two_Attend_Satisfy.valueOf(resource.getId(),
									groupPurchasePlayers.get(resource.getId()).size()));
				}
			}
		}
		PacketSendUtility.sendPacket(player, SM_GroupPurchase_Two_Gold_Change.valueOf(this.goldAmount));
	}

	@JsonIgnore
	private void sendAttendPlayerPacket(String resoureId, Object packet) {
		for (Long attendPlayerId : ServerState.getInstance().getGroupPurchasePlayers2().get(resoureId)) {
			GroupPurchaseTwo groupPurchase = ServerState.getInstance().getPlayerGroupPurchases2().get(attendPlayerId);
			if (!groupPurchase.isRewarded(resoureId)) {
				Player attendPlayer = PlayerManager.getInstance().getPlayer(attendPlayerId);
				PacketSendUtility.sendPacket(attendPlayer, packet);
			}
		}
	}

	@JsonIgnore
	public boolean isRewarded(String resourceId) {
		return rewarded.contains(resourceId);
	}

	@JsonIgnore
	public void addRewarded(String id) {
		this.rewarded.add(id);
	}

	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}

	public long getGoldAmount() {
		return goldAmount;
	}

	public void setGoldAmount(long goldAmount) {
		this.goldAmount = goldAmount;
	}

}
