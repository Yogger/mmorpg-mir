package com.mmorpg.mir.model.skill.service;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.skill.effecttemplate.EffectTemplateManager;
import com.mmorpg.mir.model.skill.manager.SkillManager;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.mmorpg.mir.model.skill.resource.SkillResource;
import com.mmorpg.mir.model.skill.target.TargetManager;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;

@Component
public final class SkillServiceImpl implements SkillService {

	public void useSkill(Player player, int skillId, long targetId, int x, int y, long[] targetList, byte direction) {
		if (!player.getLifeStats().isAlreadyDead() && player.isSpawned()) {
			if (!player.getSkillList().isContainSkill(skillId)) {
				PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.NOT_FOUND_SKILLID));
				return;
			}
			TargetManager.useSkill(player, skillId, targetId, x, y, targetList, direction);
		}
	}

	public void attack(Player player, long targetId) {
		if (!player.getLifeStats().isAlreadyDead() && player.isSpawned()) {
			VisibleObject object = World.getInstance().findObject(player.getMapId(), player.getInstanceId(), targetId);
			if (object != null) {
				Creature target = (Creature) object;
				player.getController().attackTarget(target);
			}
		}
	}

	public void skillLevelUp(Player player, int skillId) {
		player.getSkillList().skillLevelUp(skillId);
	}

	public void learnPassiveSkill(Player player, int skillId) {
		if (player.getSkillList().isContainSkill(skillId)) {
			throw new ManagedException(ManagedErrorCode.LEARNED_SKILL);
		}
		SkillResource skillResource = SkillManager.getInstance().getResource(skillId);
		if (!CoreConditionManager.getInstance().getCoreConditions(1, skillResource.getLearnConditions())
				.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		CoreActions actions = CoreActionManager.getInstance().getCoreActions(1, skillResource.getLearnActions());
		if (!actions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		actions.act(player, ModuleInfo.valueOf(ModuleType.SKILL, SubModuleType.SKILL_LEARN));
		player.getSkillList().addRewardSkill(skillResource);

		for (int effectId : skillResource.getEffects()) {
			EffectTemplateResource resource = EffectTemplateManager.getInstance().getEffectTemplateResource()
					.get(effectId, true);
			if (resource.getStats() != null && resource.getStats().length >= 1) {
				player.getGameStats().addModifiers(
						StatEffectId.valueOf(resource.getEffectTemplateId(), StatEffectType.SKILL_EFFECT),
						resource.getStats()[0]);
			}
		}
	}
}
