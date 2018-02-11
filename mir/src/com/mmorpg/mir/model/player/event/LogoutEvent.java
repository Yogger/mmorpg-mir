package com.mmorpg.mir.model.player.event;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.windforce.common.event.event.IEvent;
import com.xiaosan.socket.core.TSession;

/**
 * 登出事件体
 * 
 * @author frank
 */
public class LogoutEvent implements IEvent {

	/** 登出的用户标识 */
	private long id;
	/** 会话 */
	private TSession session;

	public static LogoutEvent valueOf(long id, TSession session) {
		LogoutEvent body = new LogoutEvent();
		body.id = id;
		body.session = session;
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

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
