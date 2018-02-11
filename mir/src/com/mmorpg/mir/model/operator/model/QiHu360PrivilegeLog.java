package com.mmorpg.mir.model.operator.model;

import java.util.Date;

public class QiHu360PrivilegeLog {
	private String id;
	private long time;

	public static QiHu360PrivilegeLog valueOf(String id){
		QiHu360PrivilegeLog qihu = new QiHu360PrivilegeLog();
		qihu.time = new Date().getTime();
		qihu.id = id;
		return qihu;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
