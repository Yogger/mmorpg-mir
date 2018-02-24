package com.mmorpg.mir.module.player.facade;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.module.player.packet.GetPlayerListReq;
import com.mmorpg.mir.module.player.packet.GetPlayerListResp;
import com.windforce.annotation.SocketClass;
import com.windforce.annotation.SocketMethod;
import com.windforce.core.Wsession;

@Component
@SocketClass
public class PlayerFacade {

	@SocketMethod
	public GetPlayerListResp getPlayerList(Wsession session, GetPlayerListReq req) {
		GetPlayerListResp resp = new GetPlayerListResp();
		// TODO 获取玩家角色信息
		return resp;
	}
}
