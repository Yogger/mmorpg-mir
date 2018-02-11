package com.mmorpg.mir.model.skill.model;

import java.util.List;
import java.util.Map;

import org.h2.util.New;

import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.skill.effecttemplate.Effects;
import com.mmorpg.mir.model.skill.resource.DamageType;
import com.mmorpg.mir.model.skill.resource.ShowHpDamageType;
import com.mmorpg.mir.model.skill.resource.grid.Grid;
import com.mmorpg.mir.model.skill.target.TargetType;
import com.mmorpg.mir.model.world.DirectionEnum;

public final class SkillTemplate {

	private int skillId;

	private String group;
	private int priority;
	private CoreConditions startconditions = new CoreConditions();
	private CoreConditions useconditions = new CoreConditions();
	private Effects effects = new Effects();
	private CoreActions actions = new CoreActions();
	private int duration;
	private int cooldown;
	private ActivationAttribute activationAttribute;
	private TargetType targetType;
	private int rangeX;
	private int rangeY;
	private int range;
	/** 最大攻击人数 */
	private int maxStarget;
	/** 公共CD组 */
	private int publicCoolDownGroup;
	/** grid 自定义格子筛选器 */
	private Map<DirectionEnum, List<List<DirectionEnum>>> directionGrids;
	private DamageType damageType;
	private ShowHpDamageType showHpDamageType;
	private boolean replace;
	private boolean unlineDuration;
	private boolean deadRemove;
	private List<ObjectType> attackObject;
	private boolean broadcast;
	private boolean notLearn;
	private boolean needSign;
	private boolean combatShow;
	private String useCannotLearnInCopyMap;
	/** 是否播放动作 */
	private boolean action;

	/** 只对自己播放动作 */
	private boolean actionSelf;

	public List<Grid> calc(int x, int y, int direction) {
		List<Grid> grids = New.arrayList();
		DirectionEnum de = DirectionEnum.indexOrdinal(direction);
		for (List<DirectionEnum> del : directionGrids.get(de)) {
			int dx = x;
			int dy = y;
			for (DirectionEnum d : del) {
				dx += d.getAddY();
				dy += d.getAddX();
			}
			grids.add(Grid.valueOf(dx, dy));
		}
		return grids;
	}

	public CoreConditions getStartconditions() {
		return startconditions;
	}

	public void setStartconditions(CoreConditions startconditions) {
		this.startconditions = startconditions;
	}

	public CoreConditions getUseconditions() {
		return useconditions;
	}

	public void setUseconditions(CoreConditions useconditions) {
		this.useconditions = useconditions;
	}

	public Effects getEffects() {
		return effects;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setEffects(Effects effects) {
		this.effects = effects;
	}

	public CoreActions getActions() {
		return actions;
	}

	public void setActions(CoreActions actions) {
		this.actions = actions;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public TargetType getTargetType() {
		return targetType;
	}

	public void setTargetType(TargetType targetType) {
		this.targetType = targetType;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isPassive() {
		return activationAttribute == ActivationAttribute.PASSIVE;
	}

	public boolean isProvoked() {
		return activationAttribute == ActivationAttribute.PROVOKED;
	}

	public boolean isActive() {
		return activationAttribute == ActivationAttribute.ACTIVE;
	}

	public int getRangeX() {
		return rangeX;
	}

	public void setRangeX(int rangeX) {
		this.rangeX = rangeX;
	}

	public int getRangeY() {
		return rangeY;
	}

	public void setRangeY(int rangeY) {
		this.rangeY = rangeY;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getMaxStarget() {
		return maxStarget;
	}

	public void setMaxStarget(int maxStarget) {
		this.maxStarget = maxStarget;
	}

	public int getPublicCoolDownGroup() {
		return publicCoolDownGroup;
	}

	public void setPublicCoolDownGroup(int publicCoolDownGroup) {
		this.publicCoolDownGroup = publicCoolDownGroup;
	}

	public Map<DirectionEnum, List<List<DirectionEnum>>> getDirectionGrids() {
		return directionGrids;
	}

	public void setDirectionGrids(Map<DirectionEnum, List<List<DirectionEnum>>> directionGrids) {
		this.directionGrids = directionGrids;
	}

	public DamageType getDamageType() {
		return damageType;
	}

	public void setDamageType(DamageType damageType) {
		this.damageType = damageType;
	}

	public ShowHpDamageType getShowHpDamageType() {
		return showHpDamageType;
	}

	public void setShowHpDamageType(ShowHpDamageType showHpDamageType) {
		this.showHpDamageType = showHpDamageType;
	}

	public boolean isReplace() {
		return replace;
	}

	public void setReplace(boolean replace) {
		this.replace = replace;
	}

	public boolean isUnlineDuration() {
		return unlineDuration;
	}

	public void setUnlineDuration(boolean unlineDuration) {
		this.unlineDuration = unlineDuration;
	}

	public boolean isDeadRemove() {
		return deadRemove;
	}

	public void setDeadRemove(boolean deadRemove) {
		this.deadRemove = deadRemove;
	}

	public List<ObjectType> getAttackObject() {
		return attackObject;
	}

	public void setAttackObject(List<ObjectType> attackObject) {
		this.attackObject = attackObject;
	}

	public boolean isBroadcast() {
		return broadcast;
	}

	public void setBroadcast(boolean broadcast) {
		this.broadcast = broadcast;
	}

	public boolean isNotLearn() {
		return notLearn;
	}

	public void setNotLearn(boolean notLearn) {
		this.notLearn = notLearn;
	}

	public boolean isNeedSign() {
		return needSign;
	}

	public void setNeedSign(boolean needSign) {
		this.needSign = needSign;
	}

	public boolean isCombatShow() {
		return combatShow;
	}

	public void setCombatShow(boolean combatShow) {
		this.combatShow = combatShow;
	}

	public String getUseCannotLearnInCopyMap() {
		return useCannotLearnInCopyMap;
	}

	public void setUseCannotLearnInCopyMap(String useCannotLearnInCopyMap) {
		this.useCannotLearnInCopyMap = useCannotLearnInCopyMap;
	}

	public boolean isAction() {
		return action;
	}

	public void setAction(boolean action) {
		this.action = action;
	}

	public boolean isActionSelf() {
		return actionSelf;
	}

	public void setActionSelf(boolean actionSelf) {
		this.actionSelf = actionSelf;
	}

}
