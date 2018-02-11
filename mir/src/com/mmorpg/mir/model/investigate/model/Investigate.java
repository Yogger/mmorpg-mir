package com.mmorpg.mir.model.investigate.model;

import java.util.Date;
import java.util.Map;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.investigate.packet.vo.InvestigateVO;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.New;

/**
 * 刺探
 * 
 * @author Kuang Hao
 * @since v1.0 2014-10-21
 * 
 */
public class Investigate {

	private Player player;
	/** 已经刺探次数 */
	private int count;
	/** 累计完成次数 */
	transient private int countAll;
	/** 最后一次刷新时间 */
	private long lastRefreshTime;
	/** 当前的刺探任务 */
	@Transient
	private String currentInvestigate;
	/** 当前的NPC */
	@Transient
	private String currentNpc;
	/** 最后一次换情报的时间 */
	private long lastChangeTime;
	/** 已获得情报次数，key:情报颜色 */
	private Map<Integer, Integer> historyColorCount = New.hashMap();

	@JsonIgnore
	public void refresh() {
		if (!DateUtils.isToday(new Date(lastRefreshTime))) {
			lastRefreshTime = System.currentTimeMillis();
			count = 0;
			currentNpc = null;
			currentInvestigate = null;
			lastChangeTime = 0;
		}
	}

	@JsonIgnore
	public boolean changeTimeCD(int cd) {
		if ((lastChangeTime + cd) > System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public void changeId(String currentInvestigate) {
		this.currentInvestigate = currentInvestigate;
		lastChangeTime = System.currentTimeMillis();
	}

	@JsonIgnore
	public InvestigateVO createVO() {
		return InvestigateVO.valueOf(this);
	}

	@JsonIgnore
	public void addCount(int add) {
		count += add;
		countAll += add;
	}

	@JsonIgnore
	public void addHistoryColorCount(int color){
		if (!this.historyColorCount.containsKey(color)) {
			historyColorCount.put(color, 1);
			return;
		}
		int num = historyColorCount.get(color);
		historyColorCount.put(color, ++num);
	}
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	@JsonIgnore
	public String getCurrentInvestigate() {
		return currentInvestigate;
	}

	@JsonIgnore
	public void setCurrentInvestigate(String currentInvestigate) {
		this.currentInvestigate = currentInvestigate;
	}

	@JsonIgnore
	public Player getPlayer() {
		return player;
	}

	@JsonIgnore
	public void setPlayer(Player player) {
		this.player = player;
	}

	@JsonIgnore
	public String getCurrentNpc() {
		return currentNpc;
	}

	@JsonIgnore
	public void setCurrentNpc(String currentNpc) {
		this.currentNpc = currentNpc;
	}

	public long getLastChangeTime() {
		return lastChangeTime;
	}

	public void setLastChangeTime(long lastChangeTime) {
		this.lastChangeTime = lastChangeTime;
	}

	public int getCountAll() {
		return countAll;
	}

	public void setCountAll(int countAll) {
		this.countAll = countAll;
	}

	public Map<Integer, Integer> getHistoryColorCount() {
		return historyColorCount;
	}

	public void setHistoryColorCount(Map<Integer, Integer> historyColorCount) {
		this.historyColorCount = historyColorCount;
	}

}
