package com.mmorpg.mir.admin.bean;

import java.util.ArrayList;
import java.util.List;

public class IpGroupBean {
	private String ip;
	private List<String> accounts = new ArrayList<String>();
	private List<String> names = new ArrayList<String>();

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public List<String> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<String> accounts) {
		this.accounts = accounts;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

}
