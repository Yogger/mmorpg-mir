package com.mmorpg.mir.model.welfare.model;

import java.util.ArrayList;
import java.util.Map;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.event.ActiveValueRewardEvent;
import com.mmorpg.mir.model.welfare.manager.ActiveManager;
import com.windforce.common.event.core.EventBusManager;

/**
 * 活跃值
 * 
 * @author 37wan
 * 
 */
public class ActiveValue {

	// 执行进度
	// eventId:exeNum
	private Map<Integer, Integer> exeMap = New.hashMap();

	// 状态
	// eventId:status
	private Map<Integer, Integer> exeStatusMap = New.hashMap();

	// 领取了活跃值的记录
	private ArrayList<Integer> rewardedList = New.arrayList();

	// 活跃值
	// player activeValue
	private int activeValue;

	// 重置的时间
	private long resetTime;

	@Transient
	private Player owner;

	public static ActiveValue valueOf(Player owner) {
		ActiveValue result = new ActiveValue();
		result.owner = owner;
		result.activeValue= ActiveManager.getInstance().ACTIVE_VALUE_INIT_VALUE.getValue();
		result.resetTime = System.currentTimeMillis();
		return result;
	}

	/**
	 * 完成活跃值的领奖
	 * 
	 * @return
	 */
	@JsonIgnore
	public boolean isFinishDrawAllReward() {
		return rewardedList.size() == ActiveManager.getInstance().activeRewardStorage.getAll().size();
	}

	@JsonIgnore
	public int getExeNum(ActiveEnum activeEnum) {
		return exeMap.get(activeEnum.getEventId()) == null ? 0 : exeMap.get(activeEnum.getEventId());
	}

	@JsonIgnore
	public int getExeStatus(ActiveEnum activeEnum) {
		return exeStatusMap.get(activeEnum.getEventId()) == null ? ActiveStatusEnum.STATUS_NOT_OPEN.getStatus()
				: exeStatusMap.get(activeEnum.getEventId());
	}

	@JsonIgnore
	public void reward(int activeValue) {
		rewardedList.add(new Integer(activeValue));
		EventBusManager.getInstance().submit(ActiveValueRewardEvent.valueOf(owner.getObjectId()));
	}

	@JsonIgnore
	public boolean isRewarded(int activeValue) {
		return rewardedList.contains(new Integer(activeValue));
	}

	@JsonIgnore
	public void addValue(int value) {
		activeValue += value;
		LogManager.addActiveValue(owner.getPlayerEnt().getServer(), owner.getPlayerEnt().getAccountName(),
				owner.getName(), owner.getObjectId(), System.currentTimeMillis(), value, activeValue);
	}

	@JsonIgnore
	public void setLoad(int eventId, int num) {
		exeMap.put(eventId, num);
	}

	/**
	 * 更新状态
	 * 
	 * @param activeEnum
	 * @param status
	 */
	@JsonIgnore
	public void updateStatus(ActiveEnum activeEnum, ActiveStatusEnum status) {
		exeStatusMap.put(activeEnum.getEventId(), status.getStatus());
	}

	@JsonIgnore
	public boolean isNull() {
		return exeStatusMap.isEmpty() && exeMap.isEmpty() && rewardedList.isEmpty() && activeValue == 0;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Map<Integer, Integer> getExeMap() {
		return exeMap;
	}

	public void setExeMap(Map<Integer, Integer> exeMap) {
		this.exeMap = exeMap;
	}

	public Map<Integer, Integer> getExeStatusMap() {
		return exeStatusMap;
	}

	public void setExeStatusMap(Map<Integer, Integer> exeStatusMap) {
		this.exeStatusMap = exeStatusMap;
	}

	public int getActiveValue() {
		return activeValue;
	}

	public void setActiveValue(int activeValue) {
		this.activeValue = activeValue;
	}

	public long getResetTime() {
		return resetTime;
	}

	public void setResetTime(long resetTime) {
		this.resetTime = resetTime;
	}

	public ArrayList<Integer> getRewardedList() {
		return rewardedList;
	}

	public void setRewardedList(ArrayList<Integer> rewardedList) {
		this.rewardedList = rewardedList;
	}

}
