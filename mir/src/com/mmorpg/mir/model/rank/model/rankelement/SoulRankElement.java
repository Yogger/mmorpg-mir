package com.mmorpg.mir.model.rank.model.rankelement;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.rank.model.RankRow;
import com.mmorpg.mir.model.soul.event.SoulUpgradeEvent;
import com.windforce.common.event.event.IEvent;

public class SoulRankElement extends RankRow {

	/** 角色 */
	private int role;
	/** 国家 */
	private int country;
	/** 英魂ID */
	private int grade;
	private long extra;
	/** 转职等级 */
	private int promotionId;
	/** 英魂等级 */
	private int level;

	public static SoulRankElement valueOf(Player player) {
		SoulRankElement e = new SoulRankElement();
		e.objId = player.getObjectId();
		e.name = player.getName();
		e.role = player.getRole();
		e.country = player.getCountryValue();
		e.grade = player.getSoul().getLevel();
		e.extra = player.getLevel();
		e.promotionId = player.getPromotion().getStage();
		e.level = player.getSoul().getRank();
		return e;
	}

	@Override
	public void changeByEvent(IEvent event) {
		if (event instanceof SoulUpgradeEvent){
			SoulUpgradeEvent up = (SoulUpgradeEvent) event;
			grade = up.getGrade();
			level = up.getLevel();
		} else if (event instanceof PromotionEvent) {
			PromotionEvent e = (PromotionEvent) event;
			promotionId = e.getStage();
		}
	}

	@Override
	public int compareEvent(IEvent event) {
		SoulUpgradeEvent e = (SoulUpgradeEvent) event;
		return grade - e.getGrade() == 0? level - e.getLevel() : grade - e.getGrade();
	}

	@Override
	public int compareTo(RankRow o) {
		SoulRankElement other = (SoulRankElement) o;
		int ret = other.getGrade() - grade;
		return ret == 0 ? other.getLevel() - level : ret;
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

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
