package com.mmorpg.mir.model.skill;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.consumable.CoreActionManager;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.skill.effecttemplate.EffectTemplateManager;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.skill.model.SkillTemplate;
import com.mmorpg.mir.model.skill.resource.ShowHpDamageType;
import com.mmorpg.mir.model.skill.resource.SkillResource;
import com.mmorpg.mir.model.skill.target.TargetType;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

/**
 * 这个类用来创造skill对象
 * 
 * @author san
 * 
 */
@Component
public class SkillEngine {

	@Static
	private Storage<Integer, SkillResource> skillResourceStorage;

	private ConcurrentMap<Integer, SkillTemplate> skillTemplateMap = new ConcurrentHashMap<Integer, SkillTemplate>();
	@Autowired
	private EffectTemplateManager effectTemplateManager;
	@Autowired
	private CoreConditionManager coreConditionManager;
	@Autowired
	private CoreActionManager coreActionManager;

	private static SkillEngine self;

	public static SkillEngine getInstance() {
		return self;
	}

	@PostConstruct
	protected void init() {
		// 这里应该包括了技能模版的初始化工作
		// 考虑到效率，可以使用激进加载的方式，把技能模版都生成好
		for (SkillResource sr : skillResourceStorage.getAll()) {
			loadOrCreateSkillTemplate(sr.getSkillId());
		}
		self = this;
	}

	public Skill getSkillForPlayer(Player player, int skillId, long targetId, int x, int y, Creature target,
			List<Creature> creatureList) {
		Skill skill = getSkill(player, skillId, targetId, x, y, target, creatureList);
		// SkillService已经验证技能是否存在
		if (player.getSkillList().getSkills().get(skillId) != null) {
			skill.setLevel(player.getSkillList().getSkills().get(skillId).getLevel());
		}
		return skill;
	}

	/**
	 * 怪物的技能获取走这里的逻辑，并不需要对怪物的技能做验证
	 * 
	 * @param effector
	 *            施法者
	 * @param skillId
	 *            技能id
	 * @param x
	 *            x坐标
	 * @param y
	 *            y坐标
	 * @param target
	 *            目标
	 * @param creatureList
	 *            其它目标
	 * @return
	 */
	public Skill getSkill(Creature effector, int skillId, long targetId, int x, int y, Creature target,
			List<Creature> creatureList) {
		Skill skill = null;
		SkillTemplate template = loadOrCreateSkillTemplate(skillId);
		skill = Skill.valueOf(template, effector, targetId, x, y, target, creatureList);
		return skill;
	}

	private SkillTemplate createSkillTemplate(int skillId) {
		SkillResource sr = skillResourceStorage.get(skillId, true);
		SkillTemplate template = new SkillTemplate();
		// 这里开始装配技能模版
		template.setSkillId(skillId);
		template.setTargetType(sr.getTargetType());
		template.setCooldown(sr.getCooldown());
		template.setDuration(sr.getDuration());
		template.setRangeX(sr.getRangeX());
		template.setRangeY(sr.getRangeY());
		template.setRange(sr.getRange());
		template.setMaxStarget(sr.getMaxTarget());
		template.setPriority(sr.getPriority());
		template.setPublicCoolDownGroup(sr.getPublicCoolDownGroup());
		template.setGroup(sr.getGroup());
		template.setDirectionGrids(sr.createDirectionGrid());
		template.setStartconditions(coreConditionManager.getCoreConditions(1, sr.getStartconditions()));
		template.setUseconditions(coreConditionManager.getCoreConditions(1, sr.getUseconditions()));
		template.setActions(coreActionManager.getCoreActions(1, sr.getActions()));
		template.setDamageType(sr.getDamageType());
		template.setReplace(sr.isReplace());
		template.setShowHpDamageType(sr.getShowHpDamageType());
		template.setNeedSign(sr.isNeedSign());
		template.setUseCannotLearnInCopyMap(sr.getUseCannotLearnInCopyMap());
		template.setCombatShow(sr.isCombatShow());
		template.setAction(sr.isAction());
		template.setActionSelf(sr.isActionSelf());
		for (int eId : sr.getEffects()) {
			template.getEffects().addEffect(effectTemplateManager.createEffectTemplate(eId));
		}
		template.setUnlineDuration(sr.isUnlineDuration());
		template.setDeadRemove(sr.isDeadRemove());
		template.setBroadcast(sr.isBroadcast());
		template.setNotLearn(sr.isNotlearn());
		if (!ArrayUtils.isEmpty(sr.getAttackObject())) {
			template.setAttackObject(Arrays.asList(sr.getAttackObject()));
		}
		return template;
	}

	public SkillTemplate loadOrCreateSkillTemplate(int skillId) {
		SkillTemplate template = skillTemplateMap.get(skillId);
		if (template == null) {
			SkillTemplate newTemplate = createSkillTemplate(skillId);
			template = skillTemplateMap.putIfAbsent(skillId, newTemplate);
			if (template == null) {
				template = newTemplate;
			}
		}
		return template;
	}

	public TargetType getSkillTargetType(int skillId) {
		return loadOrCreateSkillTemplate(skillId).getTargetType();
	}

	public int getSkillAtkRange(int skillId) {
		return loadOrCreateSkillTemplate(skillId).getRange();
	}

	public ShowHpDamageType getSkillShowHpDamageType(int skillId) {
		return loadOrCreateSkillTemplate(skillId).getShowHpDamageType();
	}

	public void reload() {
		skillTemplateMap.clear();
	}
}
