package com.mmorpg.mir.model.express.model;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.express.manager.ExpressManager;
import com.mmorpg.mir.model.express.resource.ExpressResource.LorryColor;
import com.mmorpg.mir.model.gameobjects.Lorry;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.reward.model.Reward;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;

public class Express {
	/** 已经运镖次数 */
	private int lorryCount;
	/** 总共运镖的次数 */
	transient volatile private int lorryCompleteHistoryCount;
	/** 各种颜色镖车的历史运镖次数 key 颜色 value 次数 */
	private Map<Integer, Integer> lorryColorCompleteCount;
	private long lastRefreshTime;
	/** 劫镖次数 */
	private AtomicInteger robCount = new AtomicInteger();
	/** 赛选出来的镖车 */
	private String selectLorrys;
	/** 未领取的奖励 */
	private Reward reward;
	/** 是否被抢 */
	private boolean beenRob;
	/** 是否通知了运镖失败 */
	private boolean beenNotifyFail = true;
	/** 镖车的游标 */
	@Transient
	private int currentSelectIndex = 1;
	@Transient
	private Player Owner;

	@Transient
	private volatile Lorry currentLorry;

	public static Express valueOf() {
		Express express = new Express();
		express.lastRefreshTime = System.currentTimeMillis();
		express.beenNotifyFail = true;
		express.lorryColorCompleteCount = New.hashMap();
		return express;
	}

	public ExpressVO createVO() { // send2client
		ExpressVO vo = ExpressVO.valueOf(this, createLorryVO());
		if (beenNotifyFail == false && currentLorry == null) {
			beenNotifyFail = true; // 标识发了
		}
		return vo;
	}

	@JsonIgnore
	public boolean hasGodLorry() {
		if (selectLorrys == null) {
			return false;
		}
		return ExpressManager.getInstance().getExpressResources().get(selectLorrys, true).isGod();
	}

	@JsonIgnore
	public boolean hadLorry() {
		return currentLorry != null ? true : false;
	}

	@JsonIgnore
	public void clearLorry() {
		this.currentLorry = null;
	}

	@JsonIgnore
	public void refresh() {
		if (!DateUtils.isToday(new Date(lastRefreshTime))) {
			doRefresh();
		}
	}

	@JsonIgnore
	public void doRefresh() {
		lastRefreshTime = System.currentTimeMillis();
		lorryCount = 0;
		robCount.set(0);
		selectLorrys = null;
		beenNotifyFail = true;
		restCurrentSelectIndex();
	}

	@JsonIgnore
	public void addLorryCount() {
		lorryCount++;
	}

	public int getLorryCount() {
		return lorryCount;
	}

	public void setLorryCount(int lorryCount) {
		this.lorryCount = lorryCount;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	@JsonIgnore
	public Lorry getCurrentLorry() {
		return currentLorry;
	}

	public void setCurrentLorry(Lorry currentLorry) {
		this.currentLorry = currentLorry;
	}

	public AtomicInteger getRobCount() {
		return robCount;
	}

	public void setRobCount(AtomicInteger robCount) {
		this.robCount = robCount;
	}

	@JsonIgnore
	public Player getOwner() {
		return Owner;
	}

	public void setOwner(Player owner) {
		Owner = owner;
	}

	public String getSelectLorrys() {
		return selectLorrys;
	}

	public void setSelectLorrys(String selectLorrys) {
		this.selectLorrys = selectLorrys;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	public boolean isBeenRob() {
		return beenRob;
	}

	public void setBeenRob(boolean beenRob) {
		this.beenRob = beenRob;
	}

	public LorryVO createLorryVO() {
		if (currentLorry != null) {
			return LorryVO.valueOf(currentLorry);
		} else {
			return null;
		}
	}

	public int addRobCount() {
		return robCount.incrementAndGet();
	}

	public boolean isBeenNotifyFail() {
		return beenNotifyFail;
	}

	public void setBeenNotifyFail(boolean beenNotifyFail) {
		this.beenNotifyFail = beenNotifyFail;
	}

	@JsonIgnore
	public int getNextSelectIndex() {
		if (currentSelectIndex >= LorryColor.ORANGE.getValue()) {
			return currentSelectIndex;
		}
		return currentSelectIndex++;
	}

	@JsonIgnore
	public void restCurrentSelectIndex() {
		currentSelectIndex = LorryColor.WHITE.getValue();
	}

	public int getLorryCompleteHistoryCount() {
		return lorryCompleteHistoryCount;
	}

	public void setLorryCompleteHistoryCount(int lorryCompleteHistoryCount) {
		this.lorryCompleteHistoryCount = lorryCompleteHistoryCount;
	}

	@JsonIgnore
	public void addLorryCompleteHistoryCount() {
		this.lorryCompleteHistoryCount++;
	}

	@JsonIgnore
	public void addLorryColorCompleteCount(int color) {
		if (!this.lorryColorCompleteCount.containsKey(color)) {
			lorryColorCompleteCount.put(color, 1);
			return;
		}
		int count = lorryColorCompleteCount.get(color);
		lorryColorCompleteCount.put(color, ++count);
	}

	public Map<Integer, Integer> getLorryColorCompleteCount() {
		return lorryColorCompleteCount;
	}

	public void setLorryColorCompleteCount(Map<Integer, Integer> lorryColorCompleteCount) {
		this.lorryColorCompleteCount = lorryColorCompleteCount;
	}

	public static void main(String[] args) {
		Express ss = Express.valueOf();
		ss.addRobCount();
		String s = JsonUtils.object2String(ss);
		System.out.println(s);
		Express e = JsonUtils.string2Object(s, Express.class);
		System.out.println(e.robCount);
	}
}
