package com.mmorpg.mir.model.rank.model.rankelement;

import com.mmorpg.mir.model.rank.model.RankRow;
import com.windforce.common.event.event.IEvent;

public class CountryRankElement extends RankRow{

	/** 国家实力 */
	private long power;
	private String king;
	
	public static CountryRankElement valueOf(long countryValue, long power) {
		CountryRankElement e = new CountryRankElement();
		e.power = power;
		e.name = countryValue + "";
		e.objId = countryValue;
		return e;
	}
	
	@Override
    public void changeByEvent(IEvent event) {
	}
	
	@Override
    public int compareTo(RankRow o) { /// 差个几百亿的战斗力....
		CountryRankElement e = (CountryRankElement) o;
		long dif = power - e.getPower();
		if (dif > 0) {
			return -1;
		} else if (dif == 0) {
			return 0;
		} else {
			return 1;
		}
    }

	@Override
    public int compareEvent(IEvent event) {
	    return 0;
    }

	public long getPower() {
    	return power;
    }

	public void setPower(long power) {
    	this.power = power;
    }

	public String getKing() {
    	return king;
    }

	public void setKing(String king) {
    	this.king = king;
    }

}
