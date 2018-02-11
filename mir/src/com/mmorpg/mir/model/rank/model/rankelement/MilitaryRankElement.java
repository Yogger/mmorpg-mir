package com.mmorpg.mir.model.rank.model.rankelement;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.military.event.MilitaryRankUpEvent;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.rank.model.RankRow;
import com.windforce.common.event.event.IEvent;

public class MilitaryRankElement extends RankRow {

	/** 角色 */
	private int role;
	/** 国家 */
	private int country;
	/** 军衔等级 */
	private int rank;
	private long extra;
	/** 转职等级 */
	private int promotionId;
	
	public static MilitaryRankElement valueOf(Player player) {
		MilitaryRankElement e = new MilitaryRankElement();
		e.objId = player.getObjectId();
		e.name = player.getName();
		e.role = player.getRole();
		e.country = player.getCountryValue();
		e.rank = player.getMilitary().getRank();
		e.extra = -player.getMilitary().getUpgradeTimeLog().get(e.rank) + player.getLevel();
		e.promotionId = player.getPromotion().getStage();
		return e;
	}

	@Override
    public void changeByEvent(IEvent event) {
		if (event instanceof MilitaryRankUpEvent) {
			MilitaryRankUpEvent up = (MilitaryRankUpEvent) event;
			rank = up.getRank();
		} else if (event instanceof PromotionEvent) {
			PromotionEvent e = (PromotionEvent) event;
			promotionId = e.getStage();
		}
	}
	
	@Override
    public int compareEvent(IEvent event) {
		MilitaryRankUpEvent e = (MilitaryRankUpEvent) event;
		return rank - e.getRank();
    }
	
	@Override
	public int compareTo(RankRow o) {
		MilitaryRankElement other = (MilitaryRankElement) o;
		int ret = rank - other.getRank();
		int dif = (int) (extra - other.getExtra());
		return ret == 0? dif: ret;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public long getExtra() {
		return extra;
	}

	public void setExtra(long extra) {
		this.extra = extra;
	}

	public int getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}

}
