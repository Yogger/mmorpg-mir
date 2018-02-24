package com.mmorpg.mir.common.session;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.common.PacketId;
import com.windforce.core.WrequestPacket;
import com.windforce.core.Wsession;
import com.windforce.filter.firewall.FirewallManager;

@Component
public class FirewallManagerImpl implements FirewallManager {

	@Override
	public boolean packetFilter(Wsession session, WrequestPacket packet) {
		if (packet.getPacketId() == PacketId.LOGIN_AUTN_REQ) {
			// 第一个验证消息允许通过
			return true;
		}
		if (SessionUtils.isLoginAuth(session)) {
			return true;
		}
		// 未通过验证的消息,不允许
		return false;
	}

}
