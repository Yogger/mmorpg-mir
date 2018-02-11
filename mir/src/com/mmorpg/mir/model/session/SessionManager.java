package com.mmorpg.mir.model.session;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.LogoutEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.socket.filter.session.SessionEventCause;
import com.xiaosan.dispatcher.anno.CloseHandlerAnno;
import com.xiaosan.dispatcher.anno.CreateHandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class SessionManager implements ISessionManager {

	public static final String IDENTITY = "identity";

	public static final int MAXREADTIMEOUT = 300000;
	public static final int MAXWRITETIMEOUT = 300000;
	public static final int MAXSESSIONTIMEOUT = 300000;

	/** 已经鉴别用户身份的会话，Key:用户身份标识，Value:{@link IoSession} */
	private Map<Long, TSession> identities = new ConcurrentHashMap<Long, TSession>();
	/** 匿名会话，Key:{@link IoSession#getId()}，Value:{@link IoSession} */
	private Map<Integer, TSession> anonymous = new ConcurrentHashMap<Integer, TSession>();

	private final static byte[] policyResponse = "<?xml version=\"1.0\"?><cross-domain-policy><site-control permitted-cross-domain-policies=\"all\"/><allow-access-from domain=\"*\" to-ports=\"*\"/></cross-domain-policy>\0"
			.getBytes();

	public static void main(String[] args) {
		System.out.println(policyResponse);
	}

	private static SessionManager self;

	public static SessionManager getInstance() {
		return self;
	}

	@PostConstruct
	public void init() {
		Thread thread = new Thread(new ClearTask());
		thread.setDaemon(true);
		thread.start();
		self = this;
	}

	@CreateHandlerAnno
	public void serverCreate(TSession session) {
		anonymous.put(session.getId(), session);
		try {
			session.getSocketChannel().write(ByteBuffer.wrap(policyResponse));
		} catch (IOException e) {
		}
	}

	@CloseHandlerAnno
	public void serverClose(TSession session) {
		removeSession(session);
	}

	private void removeSession(TSession session) {
		anonymous.remove(session.getId());
		Object identify = session.getAttribute(IDENTITY);
		if (identify != null) {
			if (session == identities.get(identify)) {
				identities.remove(identify);
				if (!session.hasAttribute("noEvent"))
					PlayerManager.getInstance().logout(
							LogoutEvent.valueOf((Long) identify, session));
			}
		}
	}

	public void attach(TSession session) {
		Long identify = (Long) session.getAttribute(IDENTITY);
		anonymous.remove(session.getId());
		identities.put(identify, session);
	}

	/**
	 * 向指定目标发送请求
	 * 
	 * @param request
	 * @param ids
	 */
	public void send(Object request, Long... ids) {
		for (Long id : ids) {
			TSession session = identities.get(id);
			if (session != null) {
				session.sendPacket(request);
			}
		}
	}

	/**
	 * 向指定目标发送请求
	 * 
	 * @param request
	 * @param sessions
	 */
	public void send(Object request, TSession... sessions) {
		for (TSession session : sessions) {
			session.sendPacket(request);
		}
	}

	/**
	 * 向所有会话发送请求
	 * 
	 * @param request
	 */
	void sendAll(Object request) {
		for (Entry<Long, TSession> entry : identities.entrySet()) {
			entry.getValue().sendPacket(request);
		}
		for (Entry<Integer, TSession> entry : anonymous.entrySet()) {
			entry.getValue().sendPacket(request);
		}
	}

	/**
	 * 向所有已经验证身份的会话发送请求
	 * 
	 * @param request
	 */
	public void sendAllIdentified(Object request) {
		for (Entry<Long, TSession> entry : identities.entrySet()) {
			entry.getValue().sendPacket(request);
		}
	}

	/**
	 * 向所有匿名会话发送请求
	 * 
	 * @param request
	 */
	public void sendAllAnonymous(Object request) {
		for (Entry<Integer, TSession> entry : anonymous.entrySet()) {
			entry.getValue().sendPacket(request);
		}
	}

	/**
	 * 检查指定用户是否在线
	 * 
	 * @param ids
	 *            用户标识
	 * @return
	 */
	public boolean isOnline(Long... ids) {
		for (Long id : ids) {
			if (!identities.containsKey(id)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 踢指定的用户下线
	 * 
	 * @param cause
	 *            原因标识{@link SessionEventCause}
	 * @param ids
	 *            被踢下线的用户标识
	 */
	public void kick(Long... ids) {
		for (Long id : ids) {
			TSession session = identities.get(id);
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 踢指定的用户下线,并且发送一个包
	 * 
	 * @param cause
	 *            原因标识{@link SessionEventCause}
	 * @param ids
	 *            被踢下线的用户标识
	 */
	public void kickAndSendClosePacket(Object sendPacket, Long... ids) {
		for (Long id : ids) {
			TSession session = identities.get(id);
			if (session != null) {
				session.close(sendPacket);
			}
		}
	}

	public void kickByIp(String ip) {
		for (Entry<Long, TSession> entry : identities.entrySet()) {
			TSession t = entry.getValue();
			if (t.getInetIp().equals(ip))
				t.close();
		}
		for (Entry<Integer, TSession> entry : anonymous.entrySet()) {
			TSession t = entry.getValue();
			if (t.getInetIp().equals(ip))
				t.close();
		}
	}

	/**
	 * 将全部的在线用户踢下线
	 * 
	 * @param cause
	 *            原因标识{@link SessionEventCause}
	 */
	public void kickAll() {
		for (Entry<Long, TSession> entry : identities.entrySet()) {
			entry.getValue().close();
		}
		for (Entry<Integer, TSession> entry : anonymous.entrySet()) {
			entry.getValue().close();
		}
	}

	public void kickAll(TSession session) {
		for (Entry<Long, TSession> entry : identities.entrySet()) {
			entry.getValue().close();
		}
		for (Entry<Integer, TSession> entry : anonymous.entrySet()) {
			if (entry.getKey() == session.getId()) {
				continue;
			}
			entry.getValue().close();
		}
	}

	public void kickAllAndSendPacket(Object sendPacket, TSession session) {
		for (Entry<Long, TSession> entry : identities.entrySet()) {
			entry.getValue().close(sendPacket);
		}
		for (Entry<Integer, TSession> entry : anonymous.entrySet()) {
			if (entry.getKey() == session.getId()) {
				continue;
			}
			entry.getValue().close();
		}
	}

	/**
	 * 获取全部的在线用户标识
	 * 
	 * @return
	 */
	public Collection<Long> getOnlineIdentities() {
		return Collections.unmodifiableCollection(identities.keySet());
	}

	/**
	 * 获取指定用户标识的会话对象
	 * 
	 * @param id
	 *            用户标识
	 * @return 会返回null，被延时关闭的会话也会返回
	 */
	public TSession getSession(Long id) {
		return identities.get(id);
	}

	/**
	 * 统计当前的会话数量
	 * 
	 * @param includeAnonymous
	 *            是否包含匿名会话
	 * @return
	 */
	public int count(boolean includeAnonymous) {
		int size = identities.size();
		if (includeAnonymous) {
			size += anonymous.size();
		}
		return size;
	}

	public int getOnlineSize() {
		return identities.size();
	}

	public Map<Long, TSession> getIdentities() {
		return identities;
	}

	public void setIdentities(Map<Long, TSession> identities) {
		this.identities = identities;
	}

	public Map<Integer, TSession> getAnonymous() {
		return anonymous;
	}

	public void setAnonymous(Map<Integer, TSession> anonymous) {
		this.anonymous = anonymous;
	}

	private class ClearTask implements Runnable {

		@Override
		public void run() {
			for (;;) {
				try {
					Thread.sleep(60000);
					long now = System.currentTimeMillis();
					for (TSession session : identities.values()) {
						if (session.isPendingClose()) {
							removeSession(session);
						} else {
							if ((now - session.getLastReadTime()) > MAXREADTIMEOUT
									|| (now - session.getLastWriteTime() > MAXWRITETIMEOUT)) {
								session.close();
							}
							Player player = SessionUtil
									.getPlayerBySession(session);
							if (player != null) {
								if (now - player.getLastHeartBeatTime() > MAXSESSIONTIMEOUT) {
									session.close();
								}
							}
						}
					}
					for (TSession session : anonymous.values()) {
						if (session.isPendingClose()) {
							removeSession(session);
						} else {
							if ((now - session.getLastReadTime()) > MAXREADTIMEOUT
									|| (now - session.getLastWriteTime() > MAXWRITETIMEOUT)) {
								session.close();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
