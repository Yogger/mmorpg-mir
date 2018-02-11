package com.mmorpg.mir.model.player.event;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.windforce.common.event.event.IEvent;
import com.xiaosan.socket.core.TSession;

/**
 * 登录事件体
 * 
 * @author frank
 */
public class LoginEvent implements IEvent {

	/** 事件名 */
	public static String NAME = "common:login";

	/** 登录的用户标识 */
	private long id;

	private TSession session;
	/** 登陆渠道 */
	private int loginType;

	public static IEvent valueOf(long id, TSession session, int loginType) {
		LoginEvent body = new LoginEvent();
		body.id = id;
		body.session = session;
		body.loginType = loginType;
		return body;
	}

	// Getter and Setter ...

	public long getOwner() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public TSession getSession() {
		return session;
	}

	public void setSession(TSession session) {
		this.session = session;
	}

	public int getLoginType() {
		return loginType;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
