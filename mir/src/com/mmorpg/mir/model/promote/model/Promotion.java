package com.mmorpg.mir.model.promote.model;

import javax.persistence.Transient;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.promote.manager.PromotionManager;
import com.mmorpg.mir.model.promote.resource.PromotionResource;

public class Promotion {

	private volatile int stage;

	@Transient
	private Player owner;

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	@JsonIgnore
	public int upgradeNextStageId(String questId) {
		if (questId != null && (!questId.isEmpty()) && stage != 0) {
			for (PromotionResource resource : PromotionManager.getInstance().promotionResources.getAll()) {
				if (resource.getQuestIds() == null) {
					continue;
				}
				for (String qId : resource.getQuestIds()) {
					if (qId.equals(questId)) {
						return resource.getId() + 1;
					}
				}
			}
		}
		return 0;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	@JsonIgnore
	public StatEffectId getStatEffectId() {
		return StatEffectId.valueOf("PROMOTION_STATS" + stage, StatEffectType.PROMOTION);
	}

	@JsonIgnore
	public StatEffectId getStatEffectId(int id) {
		return StatEffectId.valueOf("PROMOTION_STATS" + id, StatEffectType.PROMOTION);
	}

	@JsonIgnore
	public void resetPromotionStats(boolean calcInstance) {
		for (PromotionResource resource : PromotionManager.getInstance().promotionResources.getAll()) {
			if (stage > resource.getId() && !ArrayUtils.isEmpty(resource.getRoleStats(owner.getRole()))) {
				owner.getGameStats().addModifiers(PromotionManager.getInstance().getStatEffectId(resource.getId()),
						resource.getRoleStats(owner.getRole()), false);
			}
		}
		if (calcInstance) {
			owner.getGameStats().recomputeStats();
		}
	}

	public static Promotion valueOf() {
		Promotion p = new Promotion();
		return p;
	}

	@JsonIgnore
	public PromotionVO createVO() {
		PromotionVO vo = PromotionVO.valueOf(stage);
		return vo;
	}
}
