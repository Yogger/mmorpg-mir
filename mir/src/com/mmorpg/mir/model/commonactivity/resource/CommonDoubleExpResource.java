package com.mmorpg.mir.model.commonactivity.resource;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CommonDoubleExpResource {

	@Id
	private String id;
	/** 活动名字 */
	private String activityName;
	/** 条件数组 */
	private String[] conds;
	/** 倍数 */
	private int number;
	/** 广播条件 */
	private String[] noticeTimeConds;
	/** 广播的cron */
	private String noticeCron;
	/** 广播对应的广播 */
	private Map<String, Integer> beginNotice;

	@JsonIgnore
	public CoreConditions getConditions() {
		return CoreConditionManager.getInstance().getCoreConditions(1, this.conds);
	}

	@JsonIgnore
	public CoreConditions getNoticeConditions() {
		return CoreConditionManager.getInstance().getCoreConditions(1, this.noticeTimeConds);
	}

	public String getId() {
		return id;
	}

	public String[] getConds() {
		return conds;
	}

	public int getNumber() {
		return number;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setConds(String[] conds) {
		this.conds = conds;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Map<String, Integer> getBeginNotice() {
		return beginNotice;
	}

	public void setBeginNotice(Map<String, Integer> beginNotice) {
		this.beginNotice = beginNotice;
	}

	public String[] getNoticeTimeConds() {
		return noticeTimeConds;
	}

	public void setNoticeTimeConds(String[] noticeTimeConds) {
		this.noticeTimeConds = noticeTimeConds;
	}

	public String getNoticeCron() {
		return noticeCron;
	}

	public void setNoticeCron(String noticeCron) {
		this.noticeCron = noticeCron;
	}

}
