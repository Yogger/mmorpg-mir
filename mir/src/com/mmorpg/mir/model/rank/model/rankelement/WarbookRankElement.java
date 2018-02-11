package com.mmorpg.mir.model.rank.model.rankelement;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.rank.model.RankRow;
import com.mmorpg.mir.model.warbook.event.WarbookUpGradeEvent;
import com.windforce.common.event.event.IEvent;

public class WarbookRankElement extends RankRow {

	/** 角色 */
	private int role;
	/** 国家 */
	private int country;
	/** 阶数 */
	private int grade;
	/** 等级 */
	private int level;
	/** 附加信息 */
	private int extra;
	/** 转职等级 */
	private int promotionId;

	public static WarbookRankElement valueOf(Player player) {
		WarbookRankElement e = new WarbookRankElement();
		e.objId = player.getObjectId();
		e.name = player.getName();
		e.role = player.getRole();
		e.country = player.getCountryValue();
		e.grade = player.getWarBook().getGrade();
		e.level = player.getWarBook().getLevel();
		e.extra = player.getLevel();
		e.promotionId = player.getPromotion().getStage();
		return e;
	}

	@Override
	public int compareTo(RankRow o) {
		WarbookRankElement element = (WarbookRankElement) o;
		if (element.grade == this.grade) {
			if (element.level == this.level) {
				return element.extra - this.extra;
			} else {
				return element.level - this.level;
			}
		} else {
			return element.grade - this.grade;
		}
	}

	@Override
	public int compareEvent(IEvent event) {
		WarbookUpGradeEvent up = (WarbookUpGradeEvent) event;
		int ret = this.grade - up.getGrade();
		return ret == 0 ? this.level - up.getLevel() : ret;
	}

	@Override
	public void changeByEvent(IEvent event) {
		if (event instanceof WarbookUpGradeEvent) {
			WarbookUpGradeEvent up = (WarbookUpGradeEvent) event;
			this.grade = up.getGrade();
			this.level = up.getLevel();
		} else if (event instanceof PromotionEvent) {
			PromotionEvent e = (PromotionEvent) event;
			promotionId = e.getStage();
		}
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExtra() {
		return extra;
	}

	public void setExtra(int extra) {
		this.extra = extra;
	}

	public int getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}

}
