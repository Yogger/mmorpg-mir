package com.mmorpg.mir.model.session;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xiaosan.dispatcher.filter.AbstractOrderFilter;
import com.xiaosan.socket.core.TSession;

public class ManagementFilter extends AbstractOrderFilter {

	/** 管理后台标识属性 */
	public static final String MANAGEMENT = "management";

	/** 管理后台标识属性 */
	private static final String ATT_MANAGEMENT = "ATT_MANAGEMENT";

	@Override
	public boolean doOpen(TSession session) {
		String ip = session.getInetIp();

		if (allowIps.containsKey(ip)) {
			session.setAttribute(ATT_MANAGEMENT, MANAGEMENT);
		}

		return true;
	}

	@Override
	public boolean doConnected(TSession session) {
		return true;
	}

	private Map<String, String> allowIps = new ConcurrentHashMap<String, String>();

	/**
	 * 设置管理后台许可IP与对应的管理后台名称
	 * 
	 * @param allowIps
	 *            key:许可IP的正则 value:名称
	 */
	public void setAllowIps(LinkedHashMap<String, String> config) {
		allowIps.putAll(config);
	}

	/**
	 * 设置管理后台许可IP与对应的管理后台名称
	 * 
	 * @param config
	 *            内容条目间用","分隔，IP和管理后台名称之间用"="分隔。范例格式:[IP]=[NAME],...
	 */
	public void setAllowIpConfig(String config) {
		String[] ips = config.split(",");
		LinkedHashMap<String, String> result = new LinkedHashMap<String, String>(
				ips.length);
		for (String ip : ips) {
			String[] s = ip.split("=", 2);
			result.put(s[0], s[1]);
		}
		setAllowIps(result);
	}

	/**
	 * 添加许可IP
	 * 
	 * @param ip
	 *            许可的IP
	 * @param name
	 *            许可名
	 */
	public void addAllowIp(String ip, String name) {
		allowIps.put(ip, ip);
	}

	public Collection<String> getList() {
		return allowIps.keySet();
	}

	@Override
	public int getOrder() {
		return 1;
	}
}
