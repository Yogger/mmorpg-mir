package com.mmorpg.mir.model.operator.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class SuperVip {
	private transient boolean init;
	private String name;
	private String serverId;
	private String contact;
	@Deprecated
	private int minRecharge;
	@Deprecated
	private int circleDay;
	@Deprecated
	private int level;
	private boolean open;
	private String picturePath;

	public SuperVipVO createVO(boolean superVip) {
		SuperVipVO vo = new SuperVipVO();
		vo.setName(name);
		if (superVip) {
			vo.setContact(contact);
		}
		vo.setSuperVip(superVip);
		vo.setPicturePath(picturePath);
		return vo;
	}

	@JsonIgnore
	public void build(String name, String serverId, String contact, int minRecharge, int level, boolean open,
			String picturePath, int circleDay) {
		this.init = true;
		this.name = name;
		this.serverId = serverId;
		this.contact = contact;
		this.minRecharge = minRecharge;
		this.level = level;
		this.open = open;
		this.picturePath = picturePath;
		this.circleDay = circleDay;
	}

	public static SuperVip valueOf() {
		SuperVip sv = new SuperVip();
		return sv;
	}

	@JsonIgnore
	public boolean isInit() {
		return init;
	}

	@JsonIgnore
	public void setInit(boolean init) {
		this.init = init;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	@Deprecated
	public int getMinRecharge() {
		return minRecharge;
	}

	public void setMinRecharge(int minRecharge) {
		this.minRecharge = minRecharge;
	}

	@Deprecated
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public String getPicturePath() {
		return picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	@Deprecated
	public int getCircleDay() {
		return circleDay;
	}

	public void setCircleDay(int circleDay) {
		this.circleDay = circleDay;
	}

}
