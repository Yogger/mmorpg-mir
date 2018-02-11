package com.mmorpg.mir.model.promote.manager;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.promote.resource.PromotionResource;

public interface IPromotionManager {
	public StatEffectId getStatEffectId(int id);

	public PromotionResource getResource(int id, boolean throwException);

	public void selfPromote(Player player, String questId);

	public void openPromote(Player player);
	
	public String getI18nJobName(int role, int stage);
}
