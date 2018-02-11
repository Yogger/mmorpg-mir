package com.mmorpg.mir.model.rank.model.rankelement;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.rank.model.RankRow;
import com.mmorpg.mir.model.welfare.event.CurrencyActionEvent;
import com.windforce.common.event.event.IEvent;

public class ChristmasConsumeElement extends RankRow {
	/** 消费元宝 */
	private int score;
	/** 角色 */
	private int role;
	/** 国家 */
	private int country;
	/** 转职等级 */
	private int promotionId;

	public static ChristmasConsumeElement valueOf(Player player) {
		ChristmasConsumeElement e = new ChristmasConsumeElement();
		e.objId = player.getObjectId();
		e.name = player.getName();
		e.score = player.getCommonActivityPool().getConsumeActives().get("ChristmasConsume").getConsumeGold();
		e.country = player.getCountryValue();
		e.promotionId = player.getPromotion().getStage();
		e.role = player.getRole();
		return e;
	}

	@Override
	public void changeByEvent(IEvent event) {
		if (event instanceof CurrencyActionEvent) {
			CurrencyActionEvent up = (CurrencyActionEvent) event;
			score = up.getActivityValue();
		} else if (event instanceof PromotionEvent) {
			PromotionEvent e = (PromotionEvent) event;
			promotionId = e.getStage();
		}
	}

	@Override
	public int compareEvent(IEvent event) {
		CurrencyActionEvent e = (CurrencyActionEvent) event;
		int ret = score - e.getActivityValue();
		return ret;
	}

	@Override
	public int compareTo(RankRow o) {
		ChristmasConsumeElement element = (ChristmasConsumeElement) o;
		return element.score - score;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
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

	public int getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}

}
