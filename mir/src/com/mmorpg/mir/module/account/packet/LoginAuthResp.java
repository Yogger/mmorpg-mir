package com.mmorpg.mir.module.account.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mir.common.PacketId;
import com.windforce.annotation.SocketPacket;

/**
 * 登陆验证结果
 * 
 * @author Kuang Hao
 * @since v1.0 2018年2月23日
 *
 */
@SocketPacket(packetId = PacketId.LOGIN_AUTN_RESP)
public class LoginAuthResp {

	@Protobuf
	private int result;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}
}
