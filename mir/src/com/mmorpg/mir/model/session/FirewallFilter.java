package com.mmorpg.mir.model.session;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.slf4j.helpers.MessageFormatter;

import com.mmorpg.mir.ClearAndMigrate;
import com.mmorpg.mir.model.player.packet.SM_IP_Forbiden;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.xiaosan.dispatcher.filter.AbstractOrderFilter;
import com.xiaosan.socket.core.TSession;

/**
 * 防火墙过滤器
 * 
 * @author frank
 */
public class FirewallFilter extends AbstractOrderFilter {

	private static final Logger logger = Logger.getLogger(FirewallFilter.class);
	private static final String ATT_ALLOW = "ATT_ALLOW";
	private static final String ATT_RECORD = "ATT_RECORD";

	/** 分隔符 */
	public static final String SPLIT = ",";

	/** 白名单IP集合 */
	private ConcurrentHashMap<String, String> allows = new ConcurrentHashMap<String, String>();
	/** 黑名单IP集合 */
	private ConcurrentHashMap<String, String> blocks = new ConcurrentHashMap<String, String>();

	private int blockTimes = Integer.MAX_VALUE;
	/** 最大客户端连接数 */
	private int maxClients = 5000;
	/** 当前客户端连接数 */
	private AtomicInteger currentClients = new AtomicInteger();

	/** 阻止全部连接状态(不包括白名单) */
	private boolean blockAll = false;

	/** 服务器是否真实启动 */
	private static volatile boolean open = false;

	public static volatile boolean FIREWALL_BLOCK = false;

	public Collection<String> getBlockList() {
		return blocks.keySet();
	}

	public Collection<String> getAllowList() {
		return allows.keySet();
	}

	public void block(String ip, boolean tick) {
		if (tick)
			tickByIp(ip);
		if (blocks.containsKey(ip))
			return;
		blocks.put(ip, ip);
	}

	private void tickByIp(String ip) {
		SessionManager.getInstance().kickByIp(ip);
	}

	public void block(String ip) {
		block(ip, true);
	}

	public void unblock(String ip) {
		blocks.remove(ip);
	}

	public String blockByIoSession(TSession session) {
		String ip = session.getInetIp();
		block(ip, false);
		session.close();
		return ip;
	}

	public void blockAll() {
		blockAll = true;
	}

	public void unblockAll() {
		blockAll = false;
	}

	public boolean isBlockAll() {
		return blockAll;
	}

	public void allow(String ip) {
		if (allows.containsKey(ip)) {
			return;
		}
		allows.put(ip, ip);
	}

	public void disallow(String ip) {
		allows.remove(ip);
	}

	public int getCurrentConnections() {
		return currentClients.get();
	}

	@Override
	public boolean doOpen(TSession session) {
		return true;
	}

	public boolean doConnected(TSession session) {

		// 增加当前的在线客户端数量
		int clients = currentClients.getAndIncrement();

		// 检查是否白名单
		String ip = session.getInetIp();

		if (!open) {
			if (!ClearAndMigrate.clear) {
				String message = MessageFormatter.format("服务器处于关服或者启动状态中，阻止用户[{}]登录服务器", ip).getMessage();
				logger.error(message);
			}
			session.close(SM_System_Message.BLOCK_MESSAGE);
			return false;
		}

		if (allows.containsKey(ip)) {
			if (logger.isDebugEnabled()) {
				String message = MessageFormatter.format("白名单用户[{}]登录服务器", ip).getMessage();
				logger.debug(message);
			}
			session.setAttribute(ATT_ALLOW, true);
			return true; // 白名单不进行后续判断,直接跳过
		}

		// 是否禁止全部连接
		if (blockAll) {
			String message = MessageFormatter.format("由于阻止全部连接状态，阻止用户[{}]登录服务器", ip).getMessage();
			logger.error(message);
			session.close(SM_System_Message.BLOCK_MESSAGE);
			return false;
		}

		// 最大连接数判断
		if (clients >= maxClients) {
			if (logger.isDebugEnabled()) {
				String message = MessageFormatter.format("到达最大连接数[{}/{}]，非白名单连接将会被拒绝", clients, maxClients)
						.getMessage();
				logger.warn(message);
			}
			// 到达最大连接数，拒绝连接
			session.close();
			return false;
		}

		// 检查是否黑名单
		if (blocks.containsKey(ip)) {
			// 是黑名单内的IP，拒绝会话打开
			if (logger.isDebugEnabled()) {
				String message = MessageFormatter.format("黑名单用户[{}]登录服务器被拒绝", ip).getMessage();
				logger.debug(message);
			}
			session.close(new SM_IP_Forbiden());
			return false;
		}

		return true;
	}

	public boolean doClosed(TSession session) {
		// 减少当前的在线客户端数量
		currentClients.decrementAndGet();
		return true;
	}

	public boolean doHandle(TSession session, int opIndex, Object packet, int length) {
		if (isAllow(session)) {
			return true;
		}

		if (check(session, length)) {
			blockByIoSession(session);
			return false;
		}
		return true;
	}

	/**
	 * 检查是否需要阻止该会话
	 * 
	 */
	private boolean check(TSession session, int length) {
		// 如果没开这个，直接就跳过
		if (!FIREWALL_BLOCK) {
			return false;
		}

		// 获取记录
		FirewallRecord record = getRecord(session);

		// 检查本次访问
		int bytes = length;
		if (!record.check(bytes)) {
			return false;
		}
		if (logger.isDebugEnabled()) {
			String ip = session.getInetIp();
			String message = MessageFormatter.format("会话[{}]发生违规，违规状态[总违规次数:{} 长度:{} 次数:{}]",
					new Object[] { ip, record.getViolateTime(), record.getBytesInSecond(), record.getTimesInSecond() })
					.getMessage();
			logger.debug(message);
		}

		// 检查是否超出许可
		if (record.isBlock()) {
			return true;
		}
		return false;
	}

	/**
	 * 检查是否是白名单
	 * 
	 * @param session
	 * @return
	 */
	private boolean isAllow(TSession session) {
		Object result = session.getAttribute(ATT_ALLOW);
		if (result != null) {
			return (Boolean) result;
		}
		return false;
	}

	private FirewallRecord getRecord(TSession session) {
		FirewallRecord record = (FirewallRecord) session.getAttribute(ATT_RECORD);
		if (record == null) {
			record = new FirewallRecord();
			FirewallRecord pre = (FirewallRecord) session.setAttributeIfAbsent(ATT_RECORD, record);
			if (pre != null) {
				record = pre;
			}
		}
		return record;
	}

	// Getter and Setter ...

	/**
	 * 设置阻止全部连接状态
	 * 
	 * @param state
	 *            状态
	 */
	public void setBlockAllState(boolean state) {
		blockAll = state;
	}

	/**
	 * 设置白名单IP集合
	 * 
	 * @param allows
	 *            白名单集合
	 */
	public void setAllows(String allows) {
		if (allows.isEmpty()) {
			return;
		}
		String[] ips = allows.split(SPLIT);
		for (String ip : ips) {
			this.allows.put(ip, ip);
		}
	}

	/**
	 * 设置永久黑名单
	 * 
	 * @param blocks
	 *            黑名单集合
	 */
	public void setBlocks(String blocks) {
		if (blocks.isEmpty()) {
			return;
		}
		String[] ips = blocks.split(SPLIT);
		for (String ip : ips) {
			this.blocks.put(ip, ip);
		}
	}

	/**
	 * 设置最大连接数
	 * 
	 * @param maxClients
	 */
	public void setMaxClients(int maxClients) {
		this.maxClients = maxClients;
	}

	/**
	 * 设置最大违规次数
	 * 
	 * @param times
	 */
	public void setMaxViolateTimes(int times) {
		FirewallRecord.setMaxViolateTimes(times);
	}

	/**
	 * 设置每秒收到的字节数限制
	 * 
	 * @param size
	 */
	public void setBytesInSecondLimit(int size) {
		FirewallRecord.setBytesInSecondLimit(size);
	}

	/**
	 * 设置每秒收到的数据包次数限制
	 * 
	 * @param size
	 */
	public void setTimesInSecondLimit(int size) {
		FirewallRecord.setTimesInSecondLimit(size);
	}

	public int getBlockTimes() {
		return blockTimes;
	}

	public void setBlockTimes(int blockTimes) {
		this.blockTimes = blockTimes;
	}

	public int getMaxClients() {
		return maxClients;
	}

	public void setAllowIpConfig(String config) {
		String[] ips = config.split(",");
		for (String ip : ips) {
			String[] s = ip.split("=", 2);
			String tIp = s[0];
			this.allows.put(tIp, ip);
		}
	}

	public static void setOpen(boolean open) {
		FirewallFilter.open = open;
	}

	@Override
	public int getOrder() {
		return 0;
	}
}
