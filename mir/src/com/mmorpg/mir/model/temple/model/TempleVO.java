package com.mmorpg.mir.model.temple.model;

public class TempleVO {
	private String id;
	private int count;
	private long startTime;
	/** 最后一次换砖的时间 */
	private long lastChangeTime;
	private int fromCountry;
	/** 是否带有搬砖任务 */
	private int questCountry;
	
	private boolean beenNotifyFail;

	public static TempleVO valueOf(TempleHistory history) {
		TempleVO vo = new TempleVO();
		vo.count = history.getCount();
		if (history.getCurrentBrick() != null) {
			vo.id = history.getCurrentBrick().getId();
			vo.startTime = history.getCurrentBrick().getStartTime();
			vo.lastChangeTime = history.getCurrentBrick().getLastChangeTime();
			vo.fromCountry = history.getCurrentBrick().getFromTemple().getCountry().getId().getValue();
		}
		vo.setQuestCountry(history.getQuestCountry());
		vo.setBeenNotifyFail(history.isBeenNotifyFail());
		return vo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getLastChangeTime() {
		return lastChangeTime;
	}

	public void setLastChangeTime(long lastChangeTime) {
		this.lastChangeTime = lastChangeTime;
	}

	public int getFromCountry() {
		return fromCountry;
	}

	public void setFromCountry(int fromCountry) {
		this.fromCountry = fromCountry;
	}

	public int getQuestCountry() {
		return questCountry;
	}

	public void setQuestCountry(int questCountry) {
		this.questCountry = questCountry;
	}

	public boolean isBeenNotifyFail() {
		return beenNotifyFail;
	}

	public void setBeenNotifyFail(boolean beenNotifyFail) {
		this.beenNotifyFail = beenNotifyFail;
	}

}
