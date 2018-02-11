package com.mmorpg.mir.model.rank.model.rankelement;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.BattleScoreRefreshEvent;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.rank.model.RankRow;
import com.windforce.common.event.event.IEvent;

public class BattleScoreElement extends RankRow {

	/** 角色 */
	private int role;
	/** 国家 */
	private int country;
	/** 战斗力 */
	private int score;
	private int extra;
	/** 转职等级 */
	private int promotionId;

	public static BattleScoreElement valueOf(Player player) {
		BattleScoreElement e = new BattleScoreElement();
		e.objId = player.getObjectId();
		e.name = player.getName();
		e.country = player.getCountryValue();
		e.role = player.getRole();
		e.score = player.getGameStats().calcBattleScore();
		e.extra = player.getLevel();
		e.promotionId = player.getPromotion().getStage();
		return e;
	}

	@Override
    public void changeByEvent(IEvent event) {
		if (event instanceof BattleScoreRefreshEvent) {
			BattleScoreRefreshEvent up = (BattleScoreRefreshEvent) event;
			score = up.getBattleScore();
		} else if (event instanceof PromotionEvent) {
			PromotionEvent e = (PromotionEvent) event;
			promotionId = e.getStage();
		}
	}
	
	@Override
	public int compareEvent(IEvent event) {
		BattleScoreRefreshEvent e = (BattleScoreRefreshEvent) event;
		int ret = score - e.getBattleScore();
		if (ret == 0) {
			return extra - e.getLevel();
		}
		return ret;
	}

	@Override
	public int compareTo(RankRow o) {
		BattleScoreElement other = (BattleScoreElement) o;
		int ret = score - other.score;
		if (ret == 0) {
			int dif = (int) (extra - other.extra);
			return dif;
		}
		return ret;
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
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
