package com.mmorpg.mir.model.rank.model.rankelement;

import com.mmorpg.mir.model.copy.event.LadderNewRecordEvent;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.rank.model.RankRow;
import com.windforce.common.event.event.IEvent;

public class LadderRankElement extends RankRow {

	/** 国家 */
	private int country;
	/** 爬塔副本的最高层数 */
	private int ladderLayers;
	/** 爬塔副本最高层数所花的时间 */
	private int totalTime;

	public static LadderRankElement valueOf(Player player) {
		LadderRankElement e = new LadderRankElement();
		e.objId = player.getObjectId();
		e.name = player.getName();
		e.country = player.getCountryValue();
		e.ladderLayers = player.getRankInfo().getLayer();
		e.totalTime = player.getRankInfo().getCost();
		return e;
	}

	@Override
    public void changeByEvent(IEvent event) {
		LadderNewRecordEvent up = (LadderNewRecordEvent) event;
		ladderLayers = up.getLadderLayer();
		totalTime = up.getConsumeTime();
	}
	
	@Override
	public int compareEvent(IEvent event) {
		LadderNewRecordEvent e = (LadderNewRecordEvent) event;
		int ret = ladderLayers - e.getLadderLayer();
		if (ret == 0) {
			int dif = e.getConsumeTime() - totalTime;
			return dif;
		}
	    return ret;
    }

	@Override
	public int compareTo(RankRow o) {
		LadderRankElement other = (LadderRankElement) o;
		int compare1 = other.getLadderLayers() - ladderLayers;
		if (compare1 == 0) {
			return other.getTotalTime() - totalTime;
		}
		return compare1;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public int getLadderLayers() {
		return ladderLayers;
	}

	public void setLadderLayers(int ladderLayers) {
		this.ladderLayers = ladderLayers;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

}
