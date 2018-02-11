package com.mmorpg.mir.model.rank.model.rankelement;

import com.mmorpg.mir.model.combatspirit.event.CombatSpiritUpEvent;
import com.mmorpg.mir.model.combatspirit.manager.CombatSpiritManager;
import com.mmorpg.mir.model.combatspirit.model.CombatSpiritStorage.CombatSpiritType;
import com.mmorpg.mir.model.combatspirit.resource.CombatSpiritResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.rank.model.RankRow;
import com.windforce.common.event.event.IEvent;

public class ProtectureElement extends RankRow {

	/** 角色 */
	private int role;
	/** 国家 */
	private int country;
	/** 护符ID*/
	private String level;
	/** 额外需要比对的信息 */
	private long extra;
	/** 转职等级 */
	private int promotionId;

	public static ProtectureElement valueOf(Player player) {
		ProtectureElement e = new ProtectureElement();
		e.objId = player.getObjectId();
		e.name = player.getName();
		e.role = player.getRole();
		e.country = player.getCountryValue();
		e.level = player.getCombatSpiritStorage().getCombatSpiritCollection()
				.get(CombatSpiritType.PROTECTRUNE.getValue())
				.getCombatResourceId();
		e.extra = player.getLevel();
		e.promotionId = player.getPromotion().getStage();
		return e;

	}

	@Override
	public int compareTo(RankRow o) {
		MedalElement other = (MedalElement) o;
		CombatSpiritResource spirit = CombatSpiritManager.getInstance()
				.getCombatSpiritResource(level, true);
		CombatSpiritResource otherSpirit = CombatSpiritManager.getInstance()
				.getCombatSpiritResource(other.getLevel(), true);
		int ret = otherSpirit.getQuality() - spirit.getQuality();
		if (ret == 0) {
			ret = otherSpirit.getLevel() - spirit.getLevel();
			if (ret == 0) {
				if (extra - other.getExtra() < 0) {
					return -1;
				} else {
					return 1;
				}
			}
		}
		return ret;
	}

	@Override
	public int compareEvent(IEvent event) {
		CombatSpiritUpEvent e = (CombatSpiritUpEvent) event;
		CombatSpiritResource spirit = CombatSpiritManager.getInstance()
				.getCombatSpiritResource(level, true);
		CombatSpiritResource otherSpirit = CombatSpiritManager.getInstance()
				.getCombatSpiritResource(e.getResourceId(), true);
		int ret = spirit.getQuality() - otherSpirit.getQuality();
		if (ret == 0) {
			ret = spirit.getLevel() - otherSpirit.getLevel();
		}
		return ret;
	}

	@Override
	public void changeByEvent(IEvent event) {
		if (event instanceof CombatSpiritUpEvent){
			CombatSpiritUpEvent e = (CombatSpiritUpEvent) event;
			level = e.getResourceId();
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

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
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

}
