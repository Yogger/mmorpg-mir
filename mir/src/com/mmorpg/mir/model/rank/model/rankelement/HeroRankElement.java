package com.mmorpg.mir.model.rank.model.rankelement;


import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gang.event.PlayerGangChangeEvent;
import com.mmorpg.mir.model.gang.model.Gang;
import com.mmorpg.mir.model.player.event.PlayerDieEvent;
import com.mmorpg.mir.model.rank.model.DayKey;
import com.mmorpg.mir.model.rank.model.RankRow;
import com.windforce.common.event.event.IEvent;

public class HeroRankElement extends RankRow{

	/** 家族名字 */
	private String group;
	/** 击杀了多少人 */
	private int count;
	/** 是否领取了奖励 */
	private boolean reward;
	/** 国家 */
	private int countryValue;

	public static HeroRankElement valueOf(Player player) {
		HeroRankElement e = new HeroRankElement();
		e.objId = player.getObjectId();
		e.name = player.getName();
		Gang gang = player.getGang();
		if (gang != null) {
			e.group = gang.getName();
		}
		Long key = DayKey.valueOf().getLunchTime();
		e.count = player.getRankInfo().getSlaughterHistory().get(key).size();
		e.countryValue = player.getCountryValue();
		return e;
	}
	
	@Override
    public void changeByEvent(IEvent event) {
		if (event instanceof PlayerDieEvent) {
			PlayerDieEvent up = (PlayerDieEvent) event;
			count = up.getExtra();
		} else if (event instanceof PlayerGangChangeEvent) {
			PlayerGangChangeEvent change = (PlayerGangChangeEvent) event;
			group = change.getGangName();
		}
	}

	@Override
	public int compareEvent(IEvent event) {
		PlayerDieEvent e = (PlayerDieEvent) event;
		return count - e.getExtra();
	}

	@Override
	public int compareTo(RankRow o) {
		HeroRankElement other = (HeroRankElement) o;
		return count - other.count;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isReward() {
		return reward;
	}

	public void setReward(boolean reward) {
		this.reward = reward;
	}

	public int getCountryValue() {
		return countryValue;
	}

	public void setCountryValue(int countryValue) {
		this.countryValue = countryValue;
	}
	
}
