package com.mmorpg.mir.model.rank.model.rankelement;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.rank.model.RankRow;
import com.mmorpg.mir.model.welfare.event.CurrencyActionEvent;
import com.windforce.common.event.event.IEvent;

/**
 * 1. 添加排行榜类型, 并在RankType注册
 * 2. 添加排行榜最大记录size
 * 3. message中注入当前类
 */
public class RomaticConsumeRankElement extends RankRow {

	/** 消费元宝 */
	private int score;
	/** 角色 */
	private int role;
	/** 国家 */
	private int country;
	/** 转职等级 */
	private int promotionId;

	public static RomaticConsumeRankElement valueOf(Player player) {
		RomaticConsumeRankElement e = new RomaticConsumeRankElement();
		e.objId = player.getObjectId();
		e.name = player.getName();
		e.country = player.getCountryValue();
		e.score = player.getCommonActivityPool().getConsumeActives().get("Romatic_ConsumeAct").getConsumeGold();
		e.promotionId = player.getPromotion().getStage();
		e.role = player.getRole();
		return e;
	}

	@Override
	public int compareTo(RankRow o) {
		RomaticConsumeRankElement element = (RomaticConsumeRankElement) o;
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