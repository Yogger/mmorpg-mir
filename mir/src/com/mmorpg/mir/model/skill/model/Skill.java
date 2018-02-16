package com.mmorpg.mir.model.skill.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mmorpg.mir.model.cooldown.PublicCooldown;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.core.consumable.CoreActions;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.packet.SM_UseSkill_End;
import com.mmorpg.mir.model.skill.packet.SM_UseSkill_Start;
import com.mmorpg.mir.model.skill.target.TargetManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;

public final class Skill {
	// 技能模版
	private SkillTemplate skillTemplate;
	// 被攻击者列表(这个集合包含了firstTarget 最终计算伤害的时候，是使用这个来计算)
	private List<Creature> effectedList;
	// 攻击者
	private Creature effector;

	private int skillId;

	private long targetId;
	// 如果是指向性技能，就是用target
	private Creature firstTarget;
	// 如果是非指向性技能，就是用xy
	private int x;
	private int y;
	// 这个是施法准备时间，用来同步前后端动作的
	private int duration;

	private int range;

	private int maxTarget;
	/** 方向 */
	private byte direction;

	private int level = 1;

	private boolean needSign;

	public static Skill valueOf(SkillTemplate skillTemplate, Creature effector, long targetId, int x, int y,
			Creature firstTarget, List<Creature> effectedList) {
		// TODO 请在这里添加属性
		Skill skill = new Skill();
		skill.skillTemplate = skillTemplate;
		skill.skillId = skillTemplate.getSkillId();
		skill.effector = effector;
		skill.firstTarget = firstTarget;
		skill.targetId = targetId;
		skill.x = x;
		skill.y = y;
		skill.range = skillTemplate.getRange();
		skill.maxTarget = skillTemplate.getMaxStarget();
		skill.effectedList = effectedList;
		skill.needSign = skillTemplate.isNeedSign();
		return skill;
	}

	/**
	 * Check if the skill can be used
	 * 
	 * @return True if the skill can be used
	 */
	public boolean canUseSkill() {

		if (!preCastCheck()) {
			return false;
		}

		if (effector.getLifeStats().isAlreadyDead())
			return false;

		return true;
	}

	/**
	 * Skill entry point
	 */
	public void useSkill() {
		// 正在吟唱中
		if (effector.isCasting() || effector.getEffectController().isAbnoramlSet(EffectId.BACKHOME)) {
			return;
		}
		// 如果技能不能使用
		if (!canUseSkill())
			return;
		// 装配目标前检查
		if (!TargetManager.startChooseTarget(this)) {
			return;
		}
		// start casting
		effector.setCasting(this);

		int skillDuration = skillTemplate.getDuration();
		this.duration = skillDuration;

		int cooldown = skillTemplate.getCooldown();
		long now = System.currentTimeMillis();
		long coolDownTime = 0;
		long publicCoolDownTime = 0;

		if (cooldown != 0) {
			coolDownTime = cooldown + now;
			effector.setSkillCoolDown(skillTemplate.getSkillId(), coolDownTime);
		}

		// 计算受到急速影响到公共CD
		long publicCd = PublicCooldown.getInstance().getPublicCooldown(skillTemplate.getPublicCoolDownGroup());
		publicCoolDownTime = (int) (publicCd * 1.0 / ((effector.getGameStats().getCurrentStat(StatEnum.HASTE) + 10000) * 1.0 / 10000))
				+ now;
		effector.setPublicCoolDown(skillTemplate.getPublicCoolDownGroup(), publicCoolDownTime);

		if (effector instanceof Player) {
			((Player) effector).setNextSkillTime(coolDownTime > publicCoolDownTime ? coolDownTime : publicCoolDownTime);
		}

		if (duration < 0)
			duration = 0;

		effector.getObserveController().notifySkilluseObservers(this);
		// 这里不能改
		effector.getMoveController().stopMoving();
		if (this.getSkillTemplate().isAction()) {
			// 通知前端开始施法
			if (this.getSkillTemplate().isActionSelf()) {
				PacketSendUtility
						.sendPacket(getEffector(), SM_UseSkill_Start.valueOf(getEffector(), skillId, targetId));
			} else {
				startCast();
			}
		}

		if (this.duration > 0) {
			if (!needSign) {
				// 对于不需要吟唱的技能，这里直接解除吟唱状态
				effector.setCasting(null);
			} else {
				effector.updateCasting(this);
			}
			schedule(this.duration);
		} else {
			endCast();
		}
	}

	/**
	 * Skill entry point
	 */
	public void useProvokeSkill() {
		if (effector.isSkillDisabled(this.getSkillTemplate().getSkillId())) {
			return;
		}

		// 如果技能不能使用
		if (!canUseSkill())
			return;
		// 装配目标前检查
		if (!TargetManager.startChooseTarget(this)) {
			return;
		}

		int cooldown = skillTemplate.getCooldown();
		long now = System.currentTimeMillis();
		long coolDownTime = 0;

		if (cooldown != 0) {
			coolDownTime = cooldown + now;
			effector.setSkillCoolDown(skillTemplate.getSkillId(), coolDownTime);
		}

		if (effector instanceof Player) {
			((Player) effector).setNextSkillTime(coolDownTime);
		}

		if (duration < 0)
			duration = 0;

		if (this.getSkillTemplate().isAction()) {
			// 通知前端开始施法
			if (this.getSkillTemplate().isActionSelf()) {
				PacketSendUtility
						.sendPacket(getEffector(), SM_UseSkill_Start.valueOf(getEffector(), skillId, targetId));
			} else {
				startCast();
			}
		}

		if (this.duration > 0) {
			schedule(this.duration);
		} else {
			endCast();
		}
		effector.useProvokeSkill(skillId);
	}

	private void startCast() {
		PacketSendUtility.broadcastPacketAndReceiver(getEffector(),
				SM_UseSkill_Start.valueOf(getEffector(), skillId, targetId));
	}

	private void endCast() {

		if (needSign && effector.getCastingSkill() != this) {
			return;
		}

		// stop casting must be before preUsageCheck()
		effector.updateCasting(null);

		// 装配目标
		if (!TargetManager.endChooseTarget(this)) {
			return;
		}

		CoreActions skillActions = skillTemplate.getActions();
		if (!skillActions.verify(this, false)) {
			return;
		}
		if (skillActions != null) {
			skillActions.act(this, ModuleInfo.valueOf(ModuleType.SKILL, SubModuleType.SKILL_ACT));
		}

		// 如果技能没有目标
		if (hasNoTarget() && targetId != -1)
			return;

		if (!preUsageCheck()) {
			return;
		}

		List<Effect> effects = new ArrayList<Effect>();
		if (skillTemplate.getEffects() != null) {
			for (Creature effected : effectedList) {
				Effect effect = new Effect(effector, effected, skillTemplate, level, 0);
				if (!effect.canAttackType(effected) && effected != effect.getEffector()) {
					continue;
				}
				effect.initialize();
				effect.setX(x);
				effect.setY(y);
				effects.add(effect);
			}
		}

		// 添加技能熟练度
		// if (effector instanceof Player) {
		// Player player = (Player) effector;
		// player.getSkillList().addSkillExp(skillTemplate.getSkillId());
		// }

		for (Effect effect : effects) {
			effect.applyEffect();
		}

		// 通知消息
		sendCastspellEnd(effects);
	}

	public List<Effect> noEffectorUseSkill() {
		return noEffectorUseSkill(0);
	}

	public List<Effect> noEffectorUseSkill(long duration) {
		List<Effect> effects = new ArrayList<Effect>();
		effectedList = Arrays.asList(firstTarget);
		if (skillTemplate.getEffects() != null) {
			for (Creature effected : effectedList) {
				Effect effect = new Effect(effector, effected, skillTemplate, level, duration);
				effect.initialize();
				effects.add(effect);
			}
		}

		PacketSendUtility.broadcastPacketAndReceiver(firstTarget,
				SM_UseSkill_End.valueOf(getEffector(), skillId, effects));

		for (Effect effect : effects) {
			effect.applyEffect();
		}
		return effects;
	}

	/**
	 * @param spellStatus
	 * @param effects
	 */
	private void sendCastspellEnd(List<Effect> effects) {
		SM_UseSkill_End end = SM_UseSkill_End.valueOf(getEffector(), skillId, effects);

		Creature creature = getEffector();
		// if (creature.isObjectType(ObjectType.PLAYER)
		// &&
		// !creature.getEffectController().isAbnoramlSet(EffectId.DAMAGESUCK)) {
		if (creature.isObjectType(ObjectType.PLAYER)) {
			if (effectedList == null || !effectedList.contains(creature)) {
				PacketSendUtility.sendPacket((Player) creature, end);
			}
		} else if (creature instanceof Summon) {
			PacketSendUtility.sendPacket(((Summon) creature).getMaster(), end);
		}

		if (skillTemplate.getEffects() != null) {
			for (Creature effected : effectedList) {
				if (!effected.getEffectController().isAbnoramlSet(EffectId.DAMAGESUCK)) {
					PacketSendUtility.sendPacket(effected, end);
				}
			}
		}

	}

	private void schedule(int delay) {
		ThreadPoolManager.getInstance().scheduleWithName(new Runnable() {
			public void run() {
				endCast();
			}
		}, delay, "skill : " + skillTemplate.getSkillId());
	}

	private boolean preCastCheck() {
		CoreConditions skillConditions = skillTemplate.getStartconditions();
		return checkConditions(skillConditions);
	}

	private boolean preUsageCheck() {
		CoreConditions skillConditions = skillTemplate.getUseconditions();
		return checkConditions(skillConditions);
	}

	private boolean checkConditions(CoreConditions conditions) {
		if (conditions != null) {
			return conditions.verify(this);
		}
		return true;
	}

	/**
	 * @return the effectedList
	 */
	public List<Creature> getEffectedList() {
		return effectedList;
	}

	/**
	 * @return the effector
	 */
	public Creature getEffector() {
		return effector;
	}

	/**
	 * @return the skillTemplate
	 */
	public SkillTemplate getSkillTemplate() {
		return skillTemplate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setSkillTemplate(SkillTemplate skillTemplate) {
		this.skillTemplate = skillTemplate;
	}

	public void setEffectedList(List<Creature> effectedList) {
		this.effectedList = effectedList;
	}

	public void setEffector(Creature effector) {
		this.effector = effector;
	}

	public boolean hasNoTarget() {
		return (effectedList == null || effectedList.isEmpty());
	}

	public Creature getFirstTarget() {
		return firstTarget;
	}

	public void setFirstTarget(Creature firstTarget) {
		this.firstTarget = firstTarget;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getMaxTarget() {
		return maxTarget;
	}

	public void setMaxTarget(int maxTarget) {
		this.maxTarget = maxTarget;
	}

	public byte getDirection() {
		return direction;
	}

	public void setDirection(byte direction) {
		this.direction = direction;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
