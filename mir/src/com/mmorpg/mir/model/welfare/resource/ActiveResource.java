package com.mmorpg.mir.model.welfare.resource;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class ActiveResource {

	@Id
	private int eventId; // 事件ID
	private int exeNum; // 执行次数
	private int activeValue; // 每次执行完成获得的活跃值
	private String openModuleId;// 开启的模块ID,空表示自动开启

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getExeNum() {
		return exeNum;
	}

	public void setExeNum(int exeNum) {
		this.exeNum = exeNum;
	}

	public int getActiveValue() {
		return activeValue;
	}

	public void setActiveValue(int activeValue) {
		this.activeValue = activeValue;
	}

	public String getOpenModuleId() {
		return openModuleId;
	}

	public void setOpenModuleId(String openModuleId) {
		this.openModuleId = openModuleId;
	}

}
