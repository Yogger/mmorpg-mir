package com.mmorpg.mir.model.skill.effecttemplate;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.mmorpg.mir.model.spawn.SpawnManager;

public class SummonEffect extends EffectTemplate {

	private String key;
	private String summonType;

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
	}

	@Override
	public void applyEffect(Effect effect) {
		Creature effector = effect.getEffector();
		if (effector instanceof Player) {
			Player master = (Player) effector;
			// Summon summon = master.getSummon();
			// if (summon != null) {
			// summon.getAi().handleEvent(Event.DELETE);
			// }

			// TODO 这里的物种key是测试使用的
			// SpawnGroupResource resource =
			// SpawnManager.getInstance().createSpawnGroupResource(master, key);
			// SpawnGroupResource resource =
			// SpawnManager.getInstance().createSpawnGroupResource(master,
			// testkey);
			// resource.setAiType(AIType.SUMMON);
			// resource.setWarnrange(10);
			// summon = (Summon)
			// SpawnManager.getInstance().spawnObject(resource,
			// master.getInstanceId(), master);
			Summon summon = (Summon) SpawnManager.getInstance().creatObject(key,
					effector.getPosition().getInstanceId(), master);
			master.changeSummon(summonType, summon);
			SpawnManager.getInstance().bringIntoWorld(summon, effector.getPosition().getInstanceId());
		}
	}

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		this.key = resource.getSummonKey();
		this.summonType = resource.getSummonType();
	}

	public String getSummonType() {
		return summonType;
	}

	public void setSummonType(String summonType) {
		this.summonType = summonType;
	}

}
