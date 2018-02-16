package com.mmorpg.mir.model.skill.model;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.consumable.CoreActionManager;
import com.mmorpg.mir.model.core.consumable.CoreActions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.effecttemplate.EffectTemplateManager;
import com.mmorpg.mir.model.skill.manager.SkillManager;
import com.mmorpg.mir.model.skill.packet.SM_LearnNewSkill;
import com.mmorpg.mir.model.skill.packet.SM_SkillUpdate;
import com.mmorpg.mir.model.skill.packet.SM_Skill_Common;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.mmorpg.mir.model.skill.resource.SkillResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class SkillList {

	private Player owner;
	private NonBlockingHashMap<Integer, SkillEntry> skills = new NonBlockingHashMap<Integer, SkillEntry>();
	// beta18技能重置
	private boolean notReset;

	public NonBlockingHashMap<Integer, SkillEntry> getSkills() {
		return skills;
	}

	@JsonIgnore
	public SkillListVO createVO() {
		SkillListVO vo = new SkillListVO();
		for (SkillEntry se : skills.values()) {
			vo.getSeVos().add(SkillEntryVO.valueOf(se));
		}
		return vo;
	}

	@JsonIgnore
	public void usePassiveSkill() {
		for (SkillEntry se : skills.values()) {
			if (!se.isEnd() && se.getResource().isPassive()) {
				for (int effectId : se.getResource().getEffects()) {
					EffectTemplateResource resource = EffectTemplateManager.getInstance().getEffectTemplateResource()
							.get(effectId, true);
					if (resource.getStats() != null) {
						owner.getGameStats().addModifiers(
								StatEffectId.valueOf(resource.getEffectTemplateId(), StatEffectType.SKILL_EFFECT),
								resource.getStats()[se.getLevel() - 1], false);
					}
				}
			}

			// if (!se.isEnd() && se.getResource().isProvoke()) {
			// Skill skill = SkillEngine.getInstance().getSkill(null,
			// se.getId(), owner.getObjectId(), 0, 0, owner,
			// null);
			// skill.noEffectorUseSkill();
			// }

		}
	}

	@JsonIgnore
	public void skillLevelUp(int skillId) {
		if (!skills.containsKey(skillId)) {
			PacketSendUtility.sendPacket(owner, SM_Skill_Common.valueOf(ManagedErrorCode.SKILL_NOT_FOUND));
			return;
		}
		SkillEntry se = skills.get(skillId);
		if (se.isEnd()) {
			return;
		}

		if (!CoreConditionManager.getInstance()
				.getCoreConditions(1, se.getResource().getLevelConditions()[se.getLevel() - 1]).verify(owner, true)) {
			return;
		}
		CoreActions actions = new CoreActions();
		if (!se.isExpFull()) {
			actions.addActions(CoreActionManager.getInstance().getCoreActions(1,
					se.getResource().getActs()[se.getLevel() - 1]));
		}
		actions.verify(owner, true);
		if (se.getResource().isPassive()) {
			owner.getGameStats().endModifiers(
					StatEffectId.valueOf(se.getResource().getEffects()[0], StatEffectType.SKILL_EFFECT), false);
		}
		if (se.isMaxLevel()) {
			// 进阶
			actions.act(owner, ModuleInfo.valueOf(ModuleType.SKILL, SubModuleType.SKILL_LEVEL_UP));
			se.setEnd(true);
			this.learnSkill(se.getResource().isPassive(), se.getResource().getNextSkillId());
			PacketSendUtility.sendPacket(owner, SM_SkillUpdate.valueOf(se));
		} else {
			actions.act(owner, ModuleInfo.valueOf(ModuleType.SKILL, SubModuleType.SKILL_LEVEL_UP));
			// 升级
			se.levelUp();
			if (se.getResource().isPassive()) {
				for (int effectId : se.getResource().getEffects()) {
					EffectTemplateResource resource = EffectTemplateManager.getInstance().getEffectTemplateResource()
							.get(effectId, true);
					if (resource.getStats() != null) {
						owner.getGameStats().addModifiers(
								StatEffectId.valueOf(resource.getEffectTemplateId(), StatEffectType.SKILL_EFFECT),
								resource.getStats()[se.getLevel() - 1]);
					}
				}
			}
			PacketSendUtility.sendPacket(owner, SM_SkillUpdate.valueOf(se));
		}

		LogManager.skillLevelup(owner.getPlayerEnt().getAccountName(), owner.getObjectId(), owner.getName(),
				System.currentTimeMillis(), se.getId(), se.getLevel());
	}

	@JsonIgnore
	public void addSkillExp(int skillId) {
		if (!skills.containsKey(skillId)) {
			return;
		}
		if (skills.get(skillId).isExpFull()) {
			return;
		}
		skills.get(skillId).addExp(1);
	}

	@JsonIgnore
	public void learnSkill(boolean passive, int... ids) {
		for (Integer id : ids) {
			doLearnSkill(passive, id);
		}
	}

	@JsonIgnore
	private boolean doLearnSkill(boolean passive, int id) {
		if (skills.containsKey(id)) {
			return false;
		}
		SkillResource skillResource = SkillManager.getInstance().getResource(id);
		if (skillResource.isPassive()) {
			if (!passive) {
				return false;
			}
		}

		CoreConditionManager conditionManager = CoreConditionManager.getInstance();
		if (conditionManager.getCoreConditions(1, skillResource.getLearnConditions()).verify(owner)) {
			skills.put(id, SkillEntry.valueOf(skillResource));
			// 通知客服端
			PacketSendUtility.sendPacket(owner, SM_LearnNewSkill.valueOf(id));
			if (skillResource.isPassive()) {
				if (!passive) {
					return false;
				}
				for (int effectId : skillResource.getEffects()) {
					EffectTemplateResource resource = EffectTemplateManager.getInstance().getEffectTemplateResource()
							.get(effectId, true);
					if (resource.getStats() != null && resource.getStats().length >= 1) {
						owner.getGameStats().addModifiers(
								StatEffectId.valueOf(resource.getEffectTemplateId(), StatEffectType.SKILL_EFFECT),
								resource.getStats()[0]);
					}
				}
			}
		}
		return true;
	}

	@JsonIgnore
	public void learnSkill(Integer... ids) {
		for (Integer id : ids) {
			doLearnSkill(false, id);
		}
	}

	@JsonIgnore
	public void addRewardSkill(SkillResource skillResource) {
		if (skills.containsKey(skillResource.getSkillId())) {
			return;
		}
		skills.put(skillResource.getSkillId(), SkillEntry.valueOf(skillResource));
		// 通知客服端
		PacketSendUtility.sendPacket(owner, SM_LearnNewSkill.valueOf(skillResource.getSkillId()));
	}

	@JsonIgnore
	public boolean isContainSkill(Integer skillId) {
		// 已经学习
		if (skills.containsKey(skillId) || getOwner().getController().canUserSkill(skillId)) {
			return true;
		}

		// 在Exp_copy0中可以用天赋技能
		SkillTemplate skillTemple = SkillEngine.getInstance().loadOrCreateSkillTemplate(skillId);
		String useCannotLearnInCopyMap = skillTemple.getUseCannotLearnInCopyMap();
		if (useCannotLearnInCopyMap != null
				&& useCannotLearnInCopyMap.equals(getOwner().getCopyHistory().getCurrentCopyResourceId())) {
			return true;

		}

		// 是否可以不学习就使用
		if (skillTemple.isNotLearn()) {
			return true;
		}
		// 不能使用
		return false;
	}

	public void setSkills(NonBlockingHashMap<Integer, SkillEntry> skills) {
		this.skills = skills;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public boolean isNotReset() {
		return notReset;
	}

	public void setNotReset(boolean notReset) {
		this.notReset = notReset;
	}

}
