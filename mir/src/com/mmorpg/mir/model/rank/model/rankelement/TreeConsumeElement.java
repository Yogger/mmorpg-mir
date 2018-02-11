package com.mmorpg.mir.model.rank.model.rankelement;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.rank.model.RankRow;
import com.mmorpg.mir.model.welfare.event.CurrencyActionEvent;
import com.windforce.common.event.event.IEvent;

public class TreeConsumeElement extends RankRow {

	/** 消费元宝 */
	private int score;
	/** 角色 */
	private int role;
	/** 国家 */
	private int country;
	/** 转职等级 */
	private int promotionId;

	public static TreeConsumeElement valueOf(Player player) {
		TreeConsumeElement e = new TreeConsumeElement();
		e.objId = player.getObjectId();
		e.name = player.getName();
		//TODO 还没改活动名字
		e.score = player.getCommonActivityPool().getConsumeActives().get("Arborday_Consume").getConsumeGold();
		e.country = player.getCountryValue();
		e.promotionId = player.getPromotion().getStage();
		e.role = player.getRole();
		return e;
	}

	@Override
	public int compareTo(RankRow o) {
		TreeConsumeElement element = (TreeConsumeElement) o;
		return element.score - score;
	}

	@Override
	public int compareEvent(IEvent event) {
		CurrencyActionEvent e = (CurrencyActionEvent) event;
		int ret = score - e.getActivityValue();
		return ret;
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

	public int getScore() {
		return score;
	}

	public int getRole() {
		return role;
	}

	public int getCountry() {
		return country;
	}

	public int getPromotionId() {
		return promotionId;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}

}
