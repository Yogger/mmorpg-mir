package com.mmorpg.mir.model.operator.model;

import java.util.HashMap;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;

public class ForbidChatList {
	private HashMap<Long, OpForbidChat> forbids;

	public static ForbidChatList valueOf() {
		ForbidChatList fcl = new ForbidChatList();
		fcl.forbids = new HashMap<Long, OpForbidChat>();
		return fcl;
	}

	@JsonIgnore
	synchronized public OpForbidChat addForbidChat(Player player, long endTime) {
		OpForbidChat of = OpForbidChat.valueOf(player, endTime);
		forbids.put(player.getObjectId(), of);
		return of;
	}

	@JsonIgnore
	synchronized public void unForbidChat(long playerId) {
		forbids.remove(playerId);
	}

	@JsonIgnore
	synchronized public boolean isForbidChat(long playerId) {
		OpForbidChat forbidChat = forbids.get(playerId);
		if (forbidChat == null) {
			return false;
		}

		if (forbidChat.end()) {
			forbids.remove(playerId);
			return false;
		}
		return true;
	}

	public HashMap<Long, OpForbidChat> getForbids() {
		return forbids;
	}

	public void setForbids(HashMap<Long, OpForbidChat> forbids) {
		this.forbids = forbids;
	}

}
