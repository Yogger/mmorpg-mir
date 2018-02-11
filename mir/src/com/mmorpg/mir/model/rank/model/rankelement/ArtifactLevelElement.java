package com.mmorpg.mir.model.rank.model.rankelement;

import com.mmorpg.mir.model.artifact.event.ArtifactUpEvent;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.rank.model.RankRow;
import com.windforce.common.event.event.IEvent;

public class ArtifactLevelElement extends RankRow {

	/** 角色 */
	private int role;
	/** 国家 */
	private int country;
	/** 神兵阶数 */
	private int level;
	/** 额外需要比对的信息 */
	private long extra;
	/** 转职等级 */
	private int promotionId;
	/** 神兵等级 */
	private int rank;

	public static ArtifactLevelElement valueOf(Player player) {
		ArtifactLevelElement e = new ArtifactLevelElement();
		e.objId = player.getObjectId();
		e.name = player.getName();
		e.role = player.getRole();
		e.country = player.getCountryValue();
		e.level = player.getArtifact().getLevel();
		e.extra = player.getLevel();
		e.promotionId = player.getPromotion().getStage();
		e.rank = player.getArtifact().getRank();
		return e;
	}
	
	@Override
    public void changeByEvent(IEvent event) {
		if (event instanceof ArtifactUpEvent) {
			ArtifactUpEvent up = (ArtifactUpEvent) event;
			level = up.getGrade();
			rank = up.getLevel();
		} else if (event instanceof PromotionEvent) {
			PromotionEvent e = (PromotionEvent) event;
			promotionId = e.getStage();
		}
	}

	@Override
	public int compareEvent(IEvent event) {
		ArtifactUpEvent up = (ArtifactUpEvent) event;
		int dif = level - up.getGrade();
		return dif == 0? rank - up.getLevel() : dif;
	}

	@Override
	public int compareTo(RankRow o) {
		ArtifactLevelElement other = (ArtifactLevelElement) o;
		int ret = other.level - level;
		return ret == 0? other.getLevel() - rank : ret;
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

}
