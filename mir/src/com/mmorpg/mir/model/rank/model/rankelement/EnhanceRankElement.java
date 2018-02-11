package com.mmorpg.mir.model.rank.model.rankelement;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.event.EquipEquipmentEvent;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.rank.model.RankRow;
import com.mmorpg.mir.model.welfare.event.EnhanceEquipmentEvent;
import com.windforce.common.event.event.IEvent;

public class EnhanceRankElement extends RankRow{
	/** 全身强化等级 */
	private int score;
	/** 角色 */
	private int role;
	/** 国家 */
	private int country;
	/** 转职等级 */
	private int promotionId;
	
	public static EnhanceRankElement valueOf(Player player) {
		EnhanceRankElement e = new EnhanceRankElement();
		e.objId = player.getObjectId();
		e.name = player.getName();
		e.score = player.getOpenActive().getEnhanceActive().getMaxCount();
		e.country = player.getCountryValue();
		e.promotionId = player.getPromotion().getStage();
		e.role = player.getRole();
		return e;
	}

	@Override
    public void changeByEvent(IEvent event) {
		if (event instanceof EnhanceEquipmentEvent) {
			EnhanceEquipmentEvent up = (EnhanceEquipmentEvent) event;
			score = up.getEnhanceAllCount();
		} else if (event instanceof EquipEquipmentEvent) {
			EquipEquipmentEvent up = (EquipEquipmentEvent) event;
			score = up.getEnhanceAllCount();
		} else if (event instanceof PromotionEvent) {
			PromotionEvent e = (PromotionEvent) event;
			promotionId = e.getStage();
		}
	}
	
	@Override
	public int compareEvent(IEvent event) {
		int other = 0;
		if (event instanceof EquipEquipmentEvent) {
			EquipEquipmentEvent e = (EquipEquipmentEvent) event;
			other = e.getEnhanceAllCount();
		} else if (event instanceof EnhanceEquipmentEvent) {
			EnhanceEquipmentEvent e = (EnhanceEquipmentEvent) event;
			other = e.getEnhanceAllCount();
		}
		int ret = score - other;
		return ret;
	}

	@Override
	public int compareTo(RankRow o) {
		EnhanceRankElement element = (EnhanceRankElement) o;
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
