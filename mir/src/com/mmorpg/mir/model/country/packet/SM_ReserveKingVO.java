package com.mmorpg.mir.model.country.packet;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.mmorpg.mir.model.country.model.ReserveKing;
import com.mmorpg.mir.model.country.model.ReserveTask;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;
import com.mmorpg.mir.model.session.SessionManager;

public class SM_ReserveKingVO {
	/** 当前储君id */
	private long currentReserveKing;
	/** 自己国家储君的下一次可以使用国民召集时间 */
	private long nextCallTime;
	/** 最后一次离线的时间 */
	private long lastUnlineTime;
	/** 是否已过期 */
	private boolean deprected;
	/** 当前是否在线 */
	private boolean online;
	/** 各个国家的储君名字 */
	private Map<Integer, PlayerSimpleInfo> reserveKingInfos;
	/** 任务完成次数 */
	private Map<Integer, Integer> taskFinishCount;
	/** 错误码返回 */
	private int code;

	public static SM_ReserveKingVO valueOf(long nextCallTime, ReserveKing reserveKing,
			HashMap<Integer, PlayerSimpleInfo> reserveKingInfos) {
		SM_ReserveKingVO result = new SM_ReserveKingVO();
		result.currentReserveKing = reserveKing.getPlayerId();
		result.nextCallTime = nextCallTime;
		result.lastUnlineTime = reserveKing.getLastUnlineTime();
		result.deprected = reserveKing.isDeprected();
		result.reserveKingInfos = reserveKingInfos;
		result.online = SessionManager.getInstance().isOnline(reserveKing.getPlayerId());
		result.taskFinishCount = new HashMap<Integer, Integer>();
		for (Entry<Integer, ReserveTask> entry : reserveKing.getTasks().entrySet()) {
			result.taskFinishCount.put(entry.getKey(), entry.getValue().getFinishCount());
		}
		return result;
	}

	public long getNextCallTime() {
		return nextCallTime;
	}

	public void setNextCallTime(long nextCallTime) {
		this.nextCallTime = nextCallTime;
	}

	public long getLastUnlineTime() {
		return lastUnlineTime;
	}

	public void setLastUnlineTime(long lastUnlineTime) {
		this.lastUnlineTime = lastUnlineTime;
	}

	public Map<Integer, PlayerSimpleInfo> getReserveKingInfos() {
		return reserveKingInfos;
	}

	public void setReserveKingInfos(Map<Integer, PlayerSimpleInfo> reserveKingInfos) {
		this.reserveKingInfos = reserveKingInfos;
	}

	public boolean isDeprected() {
		return deprected;
	}

	public void setDeprected(boolean deprected) {
		this.deprected = deprected;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public long getCurrentReserveKing() {
		return currentReserveKing;
	}

	public void setCurrentReserveKing(long currentReserveKing) {
		this.currentReserveKing = currentReserveKing;
	}

	public Map<Integer, Integer> getTaskFinishCount() {
		return taskFinishCount;
	}

	public void setTaskFinishCount(Map<Integer, Integer> taskFinishCount) {
		this.taskFinishCount = taskFinishCount;
	}

}
