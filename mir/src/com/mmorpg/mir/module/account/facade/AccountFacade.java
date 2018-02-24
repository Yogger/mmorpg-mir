package com.mmorpg.mir.module.account.facade;

import com.mmorpg.mir.common.session.SessionUtils;
import com.mmorpg.mir.module.account.packet.LoginAuthReq;
import com.mmorpg.mir.module.account.packet.LoginAuthResp;
import com.windforce.annotation.SocketClass;
import com.windforce.annotation.SocketMethod;
import com.windforce.core.Wsession;

@SocketClass
public class AccountFacade {

	/**
	 * 登陆验证
	 * 
	 * @param session
	 */
	@SocketMethod
	public LoginAuthResp loginAuth(Wsession session, LoginAuthReq req) {
		LoginAuthResp resp = new LoginAuthResp();
		// TODO 验证
		String expectMD5 = "sdsfdsfdf";
		if (!expectMD5.equals(req.getMd5check())) {
			resp.setResult(1);
		}

		resp.setResult(0);
		// 验证通过标记
		SessionUtils.setLoginAuth(session);
		// 设置线程分发方式
		session.setDispatcherHashCode(Math.abs(req.getAccount().hashCode()));
		return resp;
	}
}
