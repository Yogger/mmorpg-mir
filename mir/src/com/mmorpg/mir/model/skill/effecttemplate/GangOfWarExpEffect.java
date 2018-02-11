package com.mmorpg.mir.model.skill.effecttemplate;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.mmorpg.mir.model.common.FormulaParmsUtil;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gangofwar.config.GangOfWarConfig;
import com.mmorpg.mir.model.gangofwar.manager.GangOfWarManager;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.mmorpg.mir.model.utils.ThreadPoolManager;

public class GangOfWarExpEffect extends EffectTemplate {
	private static final Logger logger = Logger.getLogger(GangOfWarExpEffect.class);
	public static final String GANGOFWAR_EXP = "GANGOFWAR_EXP";
	protected int checktime;
	private int[] values;

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
		values = resource.getValues();
		this.checktime = resource.getChecktime();
	}

	@Override
	public void applyEffect(Effect effect) {
		effect.addToEffectedController();
	}

	@Override
	public void endEffect(Effect effect) {
	}

	@Override
	public void onPeriodicAction(Effect effect) {
		super.onPeriodicAction(effect);
		if (effect.getEffected() instanceof Player) {
			Player player = (Player) effect.getEffected();
			if (!player.getPosition().isSpawned()
					|| player.getMapId() != GangOfWarConfig.getInstance().MAPID.getValue()
					|| !GangOfWarManager.getInstance().getGangOfWars(player).isWarring()) {
				// 不在这个地图，或者消失时,战斗结束应该移除掉这个BUFF
				effect.endEffect();
				return;
			}
			Reward reward = Reward.valueOf();
			if (effect.getReserved1() >= Integer.MAX_VALUE) {
				logger.error(String.format("奖励超过21亿[{%s}]", effect.getReserved1()));
				reward.addExp(Integer.MAX_VALUE);
			} else {
				reward.addExp((int) effect.getReserved1());
			}
			RewardManager.getInstance().grantReward(player, reward,
					ModuleInfo.valueOf(ModuleType.GANGOFWAR, SubModuleType.GANG_ACTIVITY_EXP));
		}
	}

	@Override
	public void startEffect(final Effect effect) {
		Future<?> task = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new Runnable() {

			@Override
			public void run() {
				onPeriodicAction(effect);
			}
		}, checktime, checktime);
		effect.addEffectTask(task);
	}

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
		effect.setReserved1(values[0]);
		if (effect.getEffected() instanceof Player) {
			Player player = (Player) effect.getEffected();
			Integer value = (Integer) FormulaParmsUtil.valueOf(GangOfWarManager.getInstance().experience_area)
					.addParm("LEVEL", player.getLevel())
					.addParm("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player))
					.addParm("EFFECT", values[0]).getValue();
			effect.setReserved1(value.intValue());
		}
	}

}