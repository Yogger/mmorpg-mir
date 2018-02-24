package com.mmorpg.mir.module.account.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mir.common.PacketId;
import com.windforce.annotation.SocketPacket;

/**
 * 登陆验证
 * 
 * @author Kuang Hao
 * @since v1.0 2018年2月23日
 *
 */
@SocketPacket(packetId = PacketId.LOGIN_AUTN_REQ)
public class LoginAuthReq {
	@Protobuf
	private String account;
	@Protobuf
	private int serverId;
	@Protobuf
	private String md5check;

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

	public String getMd5check() {
		return md5check;
	}

	public void setMd5check(String md5check) {
		this.md5check = md5check;
	}

}
