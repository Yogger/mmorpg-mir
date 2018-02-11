package com.mmorpg.mir.model.rank.model.rankelement;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.horse.event.HorseGradeUpEvent;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.rank.model.RankRow;
import com.windforce.common.event.event.IEvent;

public class HorseGradeElement extends RankRow {

	/** 角色 */
	private int role;
	/** 国家 */
	private int country;
	/** 坐骑阶数 */
	private int grade;
	private long extra;
	/** 转职等级 */
	private int promotionId;
	/** 坐骑等级 */
	private int level;
	
	public static HorseGradeElement valueOf(Player player) {
		HorseGradeElement e = new HorseGradeElement();
		e.objId = player.getObjectId();
		e.name = player.getName();
		e.country = player.getCountryValue();
		e.role = player.getRole();
		e.grade = player.getHorse().getGrade();
		e.extra = player.getLevel();
		e.promotionId = player.getPromotion().getStage();
		e.level = player.getHorse().getLevel();
		return e;
	}
	
	@Override
    public void changeByEvent(IEvent event) {
		if (event instanceof HorseGradeUpEvent) {
			HorseGradeUpEvent up = (HorseGradeUpEvent) event;
			grade = up.getGrade();
			level = up.getLevel();
		} else if (event instanceof PromotionEvent) {
			PromotionEvent e = (PromotionEvent) event;
			promotionId = e.getStage();
		}
	}
	
	@Override
    public int compareEvent(IEvent event) {
		HorseGradeUpEvent e = (HorseGradeUpEvent) event;
		int ret = grade - e.getGrade();
		return ret == 0? level - e.getLevel() : ret;
    }

	/* 
	 * 排序用的
	 */
	@Override
	public int compareTo(RankRow o) {
		HorseGradeElement other = (HorseGradeElement) o;
		int ret = other.getGrade() - grade;
		return ret == 0? other.getLevel() - level : ret;
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
