package com.mmorpg.mir.model.session;

import java.util.Collection;
import java.util.Map;

import com.xiaosan.socket.core.TSession;

public interface ISessionManager {
	void init();

	void serverCreate(TSession session);

	void serverClose(TSession session);

	void attach(TSession session);

	void send(Object request, Long... ids);

	void send(Object request, TSession... sessions);

	void sendAllIdentified(Object request);

	void sendAllAnonymous(Object request);

	boolean isOnline(Long... ids);

	void kick(Long... ids);

	void kickByIp(String ip);

	void kickAll();

	void kickAll(TSession session);

	Collection<Long> getOnlineIdentities();

	TSession getSession(Long id);

	public int count(boolean includeAnonymous);

	public int getOnlineSize();

	public Map<Long, TSession> getIdentities();

	public void setIdentities(Map<Long, TSession> identities);

	public Map<Integer, TSession> getAnonymous();

	public void setAnonymous(Map<Integer, TSession> anonymous);
}
