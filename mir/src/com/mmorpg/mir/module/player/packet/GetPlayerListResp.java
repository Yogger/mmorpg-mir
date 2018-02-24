package com.mmorpg.mir.module.player.packet;

import java.util.ArrayList;
import java.util.List;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mir.common.PacketId;
import com.mmorpg.mir.module.player.packet.vo.PlayerShortInfo;
import com.windforce.annotation.SocketPacket;

/**
 * 返回所有该账号所有的角色
 * 
 * @author Kuang Hao
 * @since v1.0 2018年2月23日
 *
 */
@SocketPacket(packetId = PacketId.GET_PLAYERLIST_RESP)
public class GetPlayerListResp {

	@Protobuf
	private List<PlayerShortInfo> playerItems = new ArrayList<>();

	public List<PlayerShortInfo> getPlayerItems() {
		return playerItems;
	}

	public void setPlayerItems(List<PlayerShortInfo> playerItems) {
		this.playerItems = playerItems;
	}

}
