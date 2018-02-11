package com.mmorpg.mir.model.skill.effecttemplate;

import com.mmorpg.mir.model.common.FormulaParmsUtil;
import com.mmorpg.mir.model.controllers.effect.PlayerEffectController;
import com.mmorpg.mir.model.exercise.ExerciseManager;
import com.mmorpg.mir.model.exercise.event.ExerciseStartEvent;
import com.mmorpg.mir.model.formula.Formula;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.packet.SM_Exercise_Status;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;
import com.windforce.common.event.core.EventBusManager;

public class ExerciseEffect extends EffectTemplate {
	
	public static final String EXERCISE_EFFECT = "PRACTICE";

	private int maxOverlyTime;

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		maxOverlyTime = resource.getMaxOverlyTime();
	}

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
		if (effect.getEffected() instanceof Player) {
			Player player = (Player) effect.getEffected();
			PacketSendUtility.sendPacket(player, SM_Exercise_Status.valueOf(ExerciseManager.getInstance().getExerciseAvailable(player)));
			EventBusManager.getInstance().submit(ExerciseStartEvent.valueOf(player.getObjectId()));
		}
	}

	@Override
	public void replace(Effect oldEffect, Effect newEffect) {
		if (newEffect.getSkillId() != oldEffect.getSkillId()) {
			return;
		}
		long overlyDuration = oldEffect.getElapsedTime() + newEffect.getDuration();
		if (overlyDuration > maxOverlyTime) {
			newEffect.setDuration(maxOverlyTime);
			newEffect.setEndTime((System.currentTimeMillis()) + newEffect.getDuration());
		} else {
			newEffect.addDuration(oldEffect.getElapsedTime());
		}
	}

	@Override
	public void onPeriodicAction(Effect effect) {
		super.onPeriodicAction(effect);
		if (effect.getEffected() instanceof Player) {
			Player player = (Player) effect.getEffected();
			String stardardExp = PlayerManager.getInstance().getStandardExp(player);
			final int level = player.getLevel();
			for (EffectTemplate tId : effect.getEffectTemplates()) {
				Formula formula = ExerciseManager.getInstance().getExerciseFormula(tId.getEffectTemplateId());
				FormulaParmsUtil util = FormulaParmsUtil.valueOf(formula).addParm("LEVEL", level)
						.addParm("STANDARD_EXP", stardardExp);
				Integer value = 0;
				if (ExerciseManager.getInstance().isExpUpIn()) {
					value = ExerciseManager.getInstance().EXERCISE_ACTIVITY_ADDITION.getValue();
				}
				util.addParm("EXERCISE_ACTIVITY", value);
				Integer addExp = (Integer) util.getValue();
				double expAddRate = 0.0;
				if (level >= 90) {
					int dif = World.getInstance().getWorldLevel() - level;
					if (dif > 0) {
						expAddRate += (dif * WorldRankManager.getInstance().WORLD_LEVEL_CONSTANT.getValue() / 100.0);
					}
				}
				addExp = (int) ((addExp * (1 + expAddRate) + 0.5) * ((10000 + player.getVip().getResource()
						.getExerciseExExp()) * 1.0 / 10000));
				Reward reward = Reward.valueOf().addExp(addExp);
				RewardManager.getInstance().grantReward(player, reward, ModuleInfo.valueOf(ModuleType.EXERCISE, SubModuleType.EXERCISE));
				((PlayerEffectController)player.getEffectController()).accumulateBuff(EXERCISE_EFFECT, addExp);
			}
		}
	}

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
	}

}