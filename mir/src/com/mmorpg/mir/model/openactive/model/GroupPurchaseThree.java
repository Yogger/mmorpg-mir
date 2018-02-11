package com.mmorpg.mir.model.openactive.model;

import java.util.HashSet;
import java.util.Map;

import org.cliffc.high_scale_lib.NonBlockingHashSet;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.OpenActiveConfig;
import com.mmorpg.mir.model.openactive.packet.SM_GroupPurchase_Three_Attend_Satisfy;
import com.mmorpg.mir.model.openactive.packet.SM_GroupPurchase_Three_Gold_Change;
import com.mmorpg.mir.model.openactive.resource.GroupPurchaseThreeResource;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class GroupPurchaseThree {
	/** 已经领取充值数 */
	private HashSet<String> rewarded;

	/** 活动期间充值数 */
	private long goldAmount;

	public static GroupPurchaseThree valueOf() {
		GroupPurchaseThree result = new GroupPurchaseThree();
		result.rewarded = New.hashSet();
		return result;
	}

	@JsonIgnore
	public void addGold(Player player, long amount) {
		this.goldAmount += amount;
		Map<String, NonBlockingHashSet<Long>> groupPurchasePlayers = ServerState.getInstance()
				.getGroupPurchasePlayers3();
		if (groupPurchasePlayers == null) {
			return;
		}
		for (GroupPurchaseThreeResource resource : OpenActiveConfig.getInstance().groupPurchaseThreeStorage.getAll()) {
			boolean attendGroupPurchase = groupPurchasePlayers.get(resource.getId()).contains(player.getObjectId());
			if (!attendGroupPurchase && resource.getAttendConditions().verify(player.getObjectId())) {
				groupPurchasePlayers.get(resource.getId()).add(player.getObjectId());
				boolean attendAmountVerify = resource.getSendConditions().verify(null);
				if (attendAmountVerify) {
					sendAttendPlayerPacket(
							resource.getId(),
							SM_GroupPurchase_Three_Attend_Satisfy.valueOf(resource.getId(),
									groupPurchasePlayers.get(resource.getId()).size()));
				}
			}
		}
		PacketSendUtility.sendPacket(player, SM_GroupPurchase_Three_Gold_Change.valueOf(this.goldAmount));
	}

	@JsonIgnore
	private void sendAttendPlayerPacket(String resoureId, Object packet) {
		for (Long attendPlayerId : ServerState.getInstance().getGroupPurchasePlayers3().get(resoureId)) {
			GroupPurchaseThree groupPurchase = ServerState.getInstance().getPlayerGroupPurchases3().get(attendPlayerId);
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
