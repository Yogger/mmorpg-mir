package com.mmorpg.mir.model.skill.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.skill.model.SkillCondition;
import com.mmorpg.mir.model.skill.model.SkillType;
import com.mmorpg.mir.model.skill.resource.grid.Grid;
import com.mmorpg.mir.model.skill.target.TargetType;
import com.mmorpg.mir.model.world.DirectionEnum;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

/**
 * 技能资源配置表
 * 
 * @author liuzhou
 * 
 */
@Resource
public class SkillResource {
	@Id
	private int skillId;
	private String[] startconditions;
	private String[] useconditions;
	private int[] effects;
	private String[] actions;
	private String[] learnActions;
	private int duration;
	private int cooldown;
	private TargetType targetType;
	private int rangeX;
	private int rangeY;
	private int range;
	private int maxTarget;
	private int publicCoolDownGroup;
	private String group;
	private int priority;
	private Grid[] grids;
	private DamageType damageType;
	private ShowHpDamageType showHpDamageType = ShowHpDamageType.NONE;
	/** 是否替换 */
	private boolean replace;
	/** 学习条件 */
	private String[] learnConditions;
	/** 升级条件 */
	private String[][] levelConditions;
	/** 下一阶段技能ID */
	private int[] nextSkillId;
	/** 每一级升级所需的熟练度 */
	private int[] exps;
	/** 升级消耗 */
	private String[][] acts;
	

	/** 技能类型 */
	private int skillType;
	/** 升级的最大等级 */
	private int maxLevel;
	/** 下线时Buff时间是否继续走动 */
	private boolean unlineDuration;
	/** 死亡移除 */
	private boolean deadRemove;
	/** 是否广播给周围的玩家 */
	private boolean broadcast;
	/** 不能攻击的对象 */
	private ObjectType[] attackObject;
	/** 不用学习就能释放 */
	private boolean notlearn;
	/** 需要吟唱 */
	private boolean needSign;
	/** 是否增加战斗力 */
	private boolean combatShow;
	/** 可以在某个副本中随便使用的技能 */
	private String useCannotLearnInCopyMap;
	/** 是否播放攻击动作 */
	private boolean action = true;
	/** 是否只对自己播放攻击动作 */
	private boolean actionSelf;
	/** beta18技能重置 */
	private boolean resetRemove;
	/** 　返还阅历 */
	private int[] resetQi;

	@Transient
	private CoreConditions acceptConditions;
	@Transient
	private List<Class<?>> acceptEventClass;

	@JsonIgnore
	public Map<DirectionEnum, List<List<DirectionEnum>>> createDirectionGrid() {
		if (TargetType.XYGRIDCUSTOM != targetType) {
			return null;
		}
		Grid root = grids[0];
		List<List<DirectionEnum>> directions = New.arrayList();
		for (Grid grid : grids) {
			if (grid.equals(root)) {
				continue;
			}
			List<DirectionEnum> ds = New.arrayList();
			int dx = root.getX() - grid.getX();
			int dy = root.getY() - grid.getY();
			if (dx > 0) {
				for (int i = 0; i < Math.abs(dx); i++) {
					ds.add(DirectionEnum.LE);
				}
			} else {
				for (int i = 0; i < Math.abs(dx); i++) {
					ds.add(DirectionEnum.RI);
				}
			}
			if (dy > 0) {
				for (int i = 0; i < Math.abs(dy); i++) {
					ds.add(DirectionEnum.UP);
				}
			} else {
				for (int i = 0; i < Math.abs(dy); i++) {
					ds.add(DirectionEnum.DN);
				}
			}
			directions.add(ds);
		}

		Map<DirectionEnum, List<List<DirectionEnum>>> directionGrids = New.hashMap();
		directionGrids.put(DirectionEnum.UP, directions);

		for (DirectionEnum dde : DirectionEnum.values()) {
			if (dde == DirectionEnum.UP) {
				continue;
			}
			List<List<DirectionEnum>> dds = New.arrayList();
			for (List<DirectionEnum> ds : directions) {
				List<DirectionEnum> newDe = new ArrayList<DirectionEnum>();
				for (DirectionEnum de : ds) {
					int ordinal = 0;
					if ((de.ordinal() + dde.ordinal()) > DirectionEnum.values().length - 1) {
						ordinal = (de.ordinal() + dde.ordinal()) - DirectionEnum.values().length;
					} else {
						ordinal = (de.ordinal() + dde.ordinal());
					}
					newDe.add(DirectionEnum.indexOrdinal(ordinal));
				}
				dds.add(newDe);
			}
			directionGrids.put(dde, dds);
		}
		return directionGrids;
	}

	/**
	 * 获取所有接受条件响应的事件
	 * 
	 * @return
	 */
	@JsonIgnore
	public List<Class<?>> getAllAcceptEvent() {
		if (acceptEventClass == null) {
			List<Class<?>> eventClass = new ArrayList<Class<?>>();
			for (AbstractCoreCondition condition : getAcceptConditions().getConditionList()) {
				if (condition instanceof SkillCondition) {
					SkillCondition qc = (SkillCondition) condition;
					for (Class<?> clazz : qc.getSkillEvent()) {
						if (!eventClass.contains(clazz)) {
							eventClass.add(clazz);
						}
					}
				}
			}
			acceptEventClass = eventClass;
		}

		return acceptEventClass;
	}

	@JsonIgnore
	public CoreConditions getAcceptConditions() {
		if (acceptConditions == null) {
			acceptConditions = CoreConditionManager.getInstance().getCoreConditions(1, learnConditions);
		}
		return acceptConditions;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public String[] getStartconditions() {
		return startconditions;
	}

	public void setStartconditions(String[] startconditions) {
		this.startconditions = startconditions;
	}

	public String[] getUseconditions() {
		return useconditions;
	}

	public void setUseconditions(String[] useconditions) {
		this.useconditions = useconditions;
	}

	public int[] getEffects() {
		if (effects == null) {
			effects = new int[0];
		}
		return effects;
	}

	public void setEffects(int[] effects) {
		this.effects = effects;
	}

	public String[] getActions() {
		return actions;
	}

	public void setActions(String[] actions) {
		this.actions = actions;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public TargetType getTargetType() {
		return targetType;
	}

	public void setTargetType(TargetType targetType) {
		this.targetType = targetType;
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

	public int getMaxTarget() {
		return maxTarget;
	}

	public void setMaxTarget(int maxTarget) {
		this.maxTarget = maxTarget;
	}

	public int getPublicCoolDownGroup() {
		return publicCoolDownGroup;
	}

	public void setPublicCoolDownGroup(int publicCoolDownGroup) {
		this.publicCoolDownGroup = publicCoolDownGroup;
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

	public Grid[] getGrids() {
		return grids;
	}

	public void setGrids(Grid[] grids) {
		this.grids = grids;
	}

	public DamageType getDamageType() {
		return damageType;
	}

	public void setDamageType(DamageType damageType) {
		this.damageType = damageType;
	}

	public boolean isReplace() {
		return replace;
	}

	public void setReplace(boolean replace) {
		this.replace = replace;
	}

	public String[] getLearnConditions() {
		return learnConditions;
	}

	public void setLearnConditions(String[] learnConditions) {
		this.learnConditions = learnConditions;
	}

	public int[] getNextSkillId() {
		return nextSkillId;
	}

	public void setNextSkillId(int[] nextSkillId) {
		this.nextSkillId = nextSkillId;
	}

	public int[] getExps() {
		return exps;
	}

	public void setExps(int[] exps) {
		this.exps = exps;
	}

	public String[][] getActs() {
		return acts;
	}

	public int getSkillType() {
		return skillType;
	}

	public void setSkillType(int skillType) {
		this.skillType = skillType;
	}

	@JsonIgnore
	public boolean isPassive() {
		return skillType == SkillType.PASSIVE.getValue();
	}

	@JsonIgnore
	public boolean isCast() {
		return skillType == SkillType.CAST.getValue();
	}

	// @JsonIgnore
	// public boolean isProvoke() {
	// return skillType == SkillType.PROVOKE.getValue();
	// }

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public String[][] getLevelConditions() {
		return levelConditions;
	}

	public void setLevelConditions(String[][] levelConditions) {
		this.levelConditions = levelConditions;
	}

	public ShowHpDamageType getShowHpDamageType() {
		return showHpDamageType;
	}

	public void setShowHpDamageType(ShowHpDamageType showHpDamageType) {
		this.showHpDamageType = showHpDamageType;
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

	public ObjectType[] getAttackObject() {
		return attackObject;
	}

	public void setAttackObject(ObjectType[] attackObject) {
		this.attackObject = attackObject;
	}

	public boolean isBroadcast() {
		return broadcast;
	}

	public void setBroadcast(boolean broadcast) {
		this.broadcast = broadcast;
	}

	public String[] getLearnActions() {
		return learnActions;
	}

	public void setLearnActions(String[] learnActions) {
		this.learnActions = learnActions;
	}

	public boolean isNotlearn() {
		return notlearn;
	}

	public void setNotlearn(boolean notlearn) {
		this.notlearn = notlearn;
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

	public boolean isResetRemove() {
		return resetRemove;
	}

	public void setResetRemove(boolean resetRemove) {
		this.resetRemove = resetRemove;
	}

	public int[] getResetQi() {
		return resetQi;
	}

	public void setResetQi(int[] resetQi) {
		this.resetQi = resetQi;
	}
	public void setActs(String[][] acts) {
		this.acts = acts;
	}
}
