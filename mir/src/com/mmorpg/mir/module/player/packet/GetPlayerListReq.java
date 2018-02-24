package com.mmorpg.mir.module.player.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mir.common.PacketId;
import com.windforce.annotation.SocketPacket;

/**
 * 获取该账号所有的玩家,后缀接REQ,方便安装业务排序
 * 
 * @author Kuang Hao
 * @since v1.0 2018年2月23日
 *
 */
@SocketPacket(packetId = PacketId.GET_PLAYERLIST_REQ)
public class GetPlayerListReq {
	@Protobuf
	private String account;
	@Protobuf
	private int serverId;
	@Protobuf
	private String op;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

}
