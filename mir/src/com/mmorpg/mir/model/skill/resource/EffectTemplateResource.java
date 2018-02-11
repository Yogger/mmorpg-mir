package com.mmorpg.mir.model.skill.resource;

import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.skill.effecttemplate.EffectTemplateType;
import com.mmorpg.mir.model.skill.model.DamageType;
import com.mmorpg.mir.model.skill.model.ProvokeTarget;
import com.mmorpg.mir.model.skill.model.ProvokeType;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class EffectTemplateResource {
	@Id
	private int effectTemplateId;
	/** 效果持续时间 */
	private int duration;

	private EffectTemplateType effectTemplateType;

	private int[] percents;

	private int[] values;
	/** 周期性时间 */
	private int checktime;
	/** 属性 */
	private Stat[][] stats;
	/** 是否可以暴击破击 */
	private DamageType[] damageTypes;
	/** 召唤物的key **/
	private String summonKey;
	/** 召唤物种类(同种类的被玩家召唤会相互替换) */
	private String summonType;
	/** 最大叠加时间 */
	private int maxOverlyTime;
	/** 最大叠加层数 */
	private int maxOverlyCount = 1;
	/** 蓝血包容量 */
	private int hpMpStore;
	/** 释放技能Id */
	private int useSkillId;
	/** 触发概率（0-10000） */
	private int provokeProb;
	/** 触发目标(ME-自己, OPPONENT-目标) */
	private ProvokeTarget provokeTarget;
	private ProvokeTarget[] provokeTargets;
	/**
	 * 触发类型（移动-MOVE,攻击-ATTACK,被攻击-ATTACKED,-使用技能-SKILLUSE,离开副本-LEAVE_COPY,死亡-DIE
	 * ,人品增加-RPTYPECHANGE,出现在地图上-SPAWN,看见生物-SEE
	 */
	private ProvokeType provokeType;
	/** 触发技能 */
	private int[] provokeSkills;
	/** 触发技能内置CD,单位毫秒 */
	private long[] provokeCD;
	/** 优先级触发概率 */
	private int[] priorityProbs;
	/** 被单位类型攻击时触发 */
	private ObjectType[] attackedByType;
	/** 多倍暴击 ([2,5]) */
	private int[] multiCriticals;
	/** 施法者自身攻击(PHYSICAL_ATTACK或者MAGICAL_ATTACK属性)百分比(0-10000)的真实伤害 */
	private int attackTrueDamagePercent;
	/** 触发技能,施法者需要满足的条件 */
	private String[] provokeEffectorConditionIds;

	public int getEffectTemplateId() {
		return effectTemplateId;
	}

	public void setEffectTemplateId(int effectTemplateId) {
		this.effectTemplateId = effectTemplateId;
	}

	public EffectTemplateType getEffectTemplateType() {
		return effectTemplateType;
	}

	public void setEffectTemplateType(EffectTemplateType effectTemplateType) {
		this.effectTemplateType = effectTemplateType;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getChecktime() {
		return checktime;
	}

	public void setChecktime(int checktime) {
		this.checktime = checktime;
	}

	public Stat[][] getStats() {
		return stats;
	}

	public void setStats(Stat[][] stats) {
		this.stats = stats;
	}

	public int[] getValues() {
		return values;
	}

	public void setValues(int[] values) {
		this.values = values;
	}

	public int[] getPercents() {
		return percents;
	}

	public void setPercents(int[] percents) {
		this.percents = percents;
	}

	public DamageType[] getDamageTypes() {
		return damageTypes;
	}

	public void setDamageTypes(DamageType[] damageTypes) {
		this.damageTypes = damageTypes;
	}

	public String getSummonKey() {
		return summonKey;
	}

	public void setSummonKey(String summonKey) {
		this.summonKey = summonKey;
	}

	public int getMaxOverlyTime() {
		return maxOverlyTime;
	}

	public void setMaxOverlyTime(int maxOverlyTime) {
		this.maxOverlyTime = maxOverlyTime;
	}

	public int getHpMpStore() {
		return hpMpStore;
	}

	public void setHpMpStore(int hpMpStore) {
		this.hpMpStore = hpMpStore;
	}

	public int getUseSkillId() {
		return useSkillId;
	}

	public void setUseSkillId(int useSkillId) {
		this.useSkillId = useSkillId;
	}

	public int getProvokeProb() {
		return provokeProb;
	}

	public void setProvokeProb(int provokeProb) {
		this.provokeProb = provokeProb;
	}

	public ProvokeTarget getProvokeTarget() {
		return provokeTarget;
	}

	public void setProvokeTarget(ProvokeTarget provokeTarget) {
		this.provokeTarget = provokeTarget;
	}

	public ProvokeType getProvokeType() {
		return provokeType;
	}

	public void setProvokeType(ProvokeType provokeType) {
		this.provokeType = provokeType;
	}

	public int[] getProvokeSkills() {
		return provokeSkills;
	}

	public void setProvokeSkills(int[] provokeSkills) {
		this.provokeSkills = provokeSkills;
	}

	public int getMaxOverlyCount() {
		return maxOverlyCount;
	}

	public void setMaxOverlyCount(int maxOverlyCount) {
		this.maxOverlyCount = maxOverlyCount;
	}

	public long[] getProvokeCD() {
		return provokeCD;
	}

	public void setProvokeCD(long[] provokeCD) {
		this.provokeCD = provokeCD;
	}

	public int[] getPriorityProbs() {
		return priorityProbs;
	}

	public void setPriorityProbs(int[] priorityProbs) {
		this.priorityProbs = priorityProbs;
	}

	public ProvokeTarget[] getProvokeTargets() {
		return provokeTargets;
	}

	public void setProvokeTargets(ProvokeTarget[] provokeTargets) {
		this.provokeTargets = provokeTargets;
	}

	public ObjectType[] getAttackedByType() {
		return attackedByType;
	}

	public void setAttackedByType(ObjectType[] attackedByType) {
		this.attackedByType = attackedByType;
	}

	public String getSummonType() {
		return summonType;
	}

	public void setSummonType(String summonType) {
		this.summonType = summonType;
	}

	public int[] getMultiCriticals() {
		return multiCriticals;
	}

	public void setMultiCriticals(int[] multiCriticals) {
		this.multiCriticals = multiCriticals;
	}

	public int getAttackTrueDamagePercent() {
		return attackTrueDamagePercent;
	}

	public void setAttackTrueDamagePercent(int attackTrueDamagePercent) {
		this.attackTrueDamagePercent = attackTrueDamagePercent;
	}

	public String[] getProvokeEffectorConditionIds() {
		return provokeEffectorConditionIds;
	}

	public void setProvokeEffectorConditionIds(String[] provokeEffectorConditionIds) {
		this.provokeEffectorConditionIds = provokeEffectorConditionIds;
	}

}
