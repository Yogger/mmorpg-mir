package com.mmorpg.mir.model.skill.effecttemplate;

import java.util.List;

import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.windforce.common.utility.JsonUtils;

public class SlowEffect extends EffectTemplate {
	protected int value;
	protected Stat[][] stats;

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		this.stats = resource.getStats();
	}

	@Override
	public void applyEffect(Effect effect) {
		if (effect.getEffected().getLifeStats().isAlreadyDead()) {
			return;
		}
		effect.addToEffectedController();
	}

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
	}

	@Override
	public void endEffect(Effect effect) {
		Creature effected = effect.getEffected();
		effected.getEffectController().unsetAbnormal(EffectId.SLOW.getEffectId(), true);
		effected.getGameStats().endModifiers(
				StatEffectId.valueOf(this.getEffectTemplateId(), StatEffectType.SKILL_EFFECT));
	}

	@Override
	public void startEffect(final Effect effect) {
		final Creature effected = effect.getEffected();
		if (effected.isObjectType(ObjectType.BOSS) || effected.getEffectController().isAbnoramlSet(EffectId.PABODY)) {
			return;
		}
		effected.getEffectController().setAbnormal(EffectId.SLOW.getEffectId(), true);
		List<Stat> newStat = New.arrayList();
		for (Stat stat : stats[effect.getSkillLevel() - 1]) {
			newStat.add(stat.getNewProperty());
		}
		effected.getGameStats().addModifiers(
				StatEffectId.valueOf(this.getEffectTemplateId(), StatEffectType.SKILL_EFFECT), newStat);
	}

	public static void main(String[] args) {
		Stat stat = new Stat(StatEnum.SPEED, 0, 0, -5000);
		System.out.println(JsonUtils.object2String(new Stat[] { stat }));
	}
}