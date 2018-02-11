package com.mmorpg.mir.model.skill.effecttemplate;

import com.mmorpg.mir.model.ai.AIType;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.gameobjects.BigBrother;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;

/**
 * 召唤大B哥的技能
 * 
 * @author Kuang Hao
 * @since v1.0 2015-8-3
 * 
 */
public class BigBrotherEffect extends EffectTemplate {

	private String key;

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
	}

	@Override
	public void applyEffect(Effect effect) {
		Creature effector = effect.getEffector();
		if (effector instanceof Player) {
			final Player master = (Player) effector;
			BigBrother bigBrother = master.getBigBrother();
			if (bigBrother != null) {
				bigBrother.getController().delete();
			}

			SpawnGroupResource resource = SpawnManager.getInstance().createSpawnGroupResource(master, key);
			resource.setAiType(AIType.BIGBROTHER);
			resource.setWarnrange(10);
			resource.setHomerange(20);
			final BigBrother newBigBrother = (BigBrother) SpawnManager.getInstance().spawnObject(resource,
					master.getInstanceId(), master);
			master.setBigBrother(newBigBrother);
			final int playerMapId = master.getMapId();

			master.getObserveController().attach(new ActionObserver(ObserverType.SPAWN) {

				@Override
				public void spawn(int mapId, int instanceId) {
					if (mapId != playerMapId) {
						newBigBrother.getController().delete();
					}
				}

			});
		}
	}

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		this.key = resource.getSummonKey();
	}

}
