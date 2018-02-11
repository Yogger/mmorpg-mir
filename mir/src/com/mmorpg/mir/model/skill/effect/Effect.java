package com.mmorpg.mir.model.skill.effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.packet.AbnormalEffect;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.skill.effecttemplate.EffectTemplate;
import com.mmorpg.mir.model.skill.model.EffectDB;
import com.mmorpg.mir.model.skill.model.HealEffect;
import com.mmorpg.mir.model.skill.model.SkillTemplate;
import com.mmorpg.mir.model.skill.model.SpecialEffect;
import com.mmorpg.mir.model.skill.resource.DamageType;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.ThreadPoolManager;

public class Effect {
	private SkillTemplate skillTemplate;

	private Creature effected;
	private Creature effector;

	private AtomicBoolean isStopped = new AtomicBoolean(false);

	private byte damageType;

	private long duration;
	private long endTime;

	private Collection<EffectTemplate> sucessEffects = new LinkedList<EffectTemplate>();
	/** 这个任务用来保证在最大的事件间隔以后，这个效果的所有事件都会被关闭 */
	private Future<?> endTask = null;
	/** 当我们有不用的效果事件的时候，就添加到这个事件列表里面 */
	private List<Future<?>> effectTasks;

	/** Used for damage/heal values */
	private long reserved1;
	/** Used for shield total hit damage; */
	private long reserved2;
	/** Used for shield hit damage */
	private long reserved3;
	/** value contain */
	private Map<String, Integer> values = new HashMap<String, Integer>();

	private boolean addedToController;

	private int skillLevel;

	private long bloodSuckValue;

	private long reboundValue;

	private long healDp;

	private long healHp;

	private int x;
	private int y;
	/** 触发类技能最近一次触发时间 */
	private long lastProvokerEffectTime;

	private ActionObserver actionObserver;
	/** 叠加层数 */
	private int overlyCount = 1;

	public EffectDB creatToDB() {
		EffectDB db = new EffectDB();
		db.setSkillId(getSkillTemplate().getSkillId());
		db.setSaveTime(System.currentTimeMillis());
		db.setSkillLevel(skillLevel);
		db.setEndTime(endTime);
		db.setReserved3(getReserved3());
		return db;
	}

	public boolean isDeadRemove() {
		return getSkillTemplate().isDeadRemove();
	}

	public Effect(Creature effector, Creature effected, SkillTemplate skillTemplate, int skillLevel, long duration) {
		this(effector, effected, skillTemplate);
		this.setDuration(duration);
		this.skillLevel = skillLevel;
	}

	public Effect(Creature effector, Creature effected, SkillTemplate skillTemplate) {
		this.effector = effector;
		this.effected = effected;
		this.setSkillTemplate(skillTemplate);
	}

	public int getRange() {
		return getSkillTemplate().getRange();
	}

	/**
	 * @return the skillId
	 */
	public int getSkillId() {
		return getSkillTemplate().getSkillId();
	}

	@JsonIgnore
	public List<ObjectType> getAttackObject() {
		return getSkillTemplate().getAttackObject();
	}

	@JsonIgnore
	public boolean canAttackType(Creature creature) {
		if (getAttackObject() == null || getAttackObject().isEmpty()
				|| getAttackObject().contains(creature.getObjectType())) {
			return true;
		}
		return false;
	}

	/**
	 * @return the effected
	 */
	public Creature getEffected() {
		return effected;
	}

	public void addEndTime(int time) {
		this.setEndTime(this.getEndTime() + time);
	}

	public void replace(Effect newEffect) {
		for (EffectTemplate temple : sucessEffects) {
			temple.replace(this, newEffect);
		}
	}

	/**
	 * @return the effector
	 */
	public Creature getEffector() {
		return effector;
	}

	public void initialize() {
		if (getSkillTemplate().getEffects() == null)
			return;

		this.damageType = MathUtil.calculateDamageType(this).getValue();
		for (EffectTemplate template : getEffectTemplates()) {
			template.calculate(this);
		}

	}

	/**
	 * Apply all effect templates
	 */
	public void applyEffect() {
		if (getSkillTemplate().getEffects() == null || sucessEffects.isEmpty())
			return;

		for (EffectTemplate template : sucessEffects) {
			template.applyEffect(this);
		}

	}

	public void startEffect(boolean restored) {
		if (sucessEffects.isEmpty())
			return;

		for (EffectTemplate template : sucessEffects) {
			template.startEffect(this);
		}

		// 这个duration的计算应该放到replace之前
		// if (!restored)
		// addDuration(getEffectsDuration());
		if (getDuration() == 0)
			return;

		setEndTime(System.currentTimeMillis() + getDuration());

		endTask = ThreadPoolManager.getInstance().scheduleEffect((new Runnable() {
			@Override
			public void run() {
				endEffect();
			}
		}), getDuration());

	}

	/**
	 * Try to add this effect to effected controller
	 */
	synchronized public void addToEffectedController() {
		if (!addedToController) {
			effected.getEffectController().addEffect(this);
			addedToController = true;
		}
	}

	public void addAllEffectToSucess() {
		for (EffectTemplate template : getEffectTemplates()) {
			sucessEffects.add(template);
		}
	}

	public void addEffectTask(Future<?> effectTask) {
		if (effectTasks == null) {
			effectTasks = new ArrayList<Future<?>>();
		}
		effectTasks.add(effectTask);
	}

	public Future<?> getEndTask() {
		return endTask;
	}

	public void setEndTask(Future<?> endTask) {
		this.endTask = endTask;
	}

	synchronized public void doEndEffect() {
		for (EffectTemplate template : sucessEffects) {
			template.endEffect(this);
		}
		stopTasks();
		effected.getEffectController().clearEffect(this);
	}

	synchronized public void endEffect() {
		if (getIsStopped().compareAndSet(false, true)) {
			doEndEffect();
		}
	}

	public void addSucessEffect(EffectTemplate effect) {
		sucessEffects.add(effect);
	}

	/**
	 * @return
	 */
	public Collection<EffectTemplate> getSuccessEffect() {
		return sucessEffects;
	}

	public List<EffectTemplate> getEffectTemplates() {
		return getSkillTemplate().getEffects().getEffects();
	}

	public byte getDamageType() {
		return damageType;
	}

	public void setDamageType(byte damageType) {
		this.damageType = damageType;
	}

	public String getGroup() {
		return getSkillTemplate().getGroup();
	}

	public int getPriority() {
		return getSkillTemplate().getPriority();
	}

	public boolean isReplace() {
		return getSkillTemplate().isReplace();
	}

	public long getElapsedTime() {
		long elapsedTime = getEndTime() - System.currentTimeMillis();
		return elapsedTime > 0 ? elapsedTime : 0;
	}

	public AbnormalEffect createAbnormalEffect() {
		return AbnormalEffect.valueOf(this.getSkillId(), getElapsedTime(), this);
	}

	public int getEffectsDuration() {
		int duration = 0;
		for (EffectTemplate template : sucessEffects) {
			int effectDuration = template.getDuration();
			duration = (duration > effectDuration ? duration : effectDuration);
		}
		return duration;
	}

	public void stopTasks() {
		if (endTask != null) {
			if (!endTask.isCancelled()) {
				endTask.cancel(true);
				endTask = null;
			}
		}
		if (effectTasks != null) {
			for (Future<?> task : effectTasks) {
				if (!task.isCancelled()) {
					task.cancel(true);
				}
			}
			effectTasks = null;
		}
	}

	public long getReserved1() {
		return reserved1;
	}

	public DamageType getSkillDamageType() {
		return this.getSkillTemplate().getDamageType();
	}

	public void setReserved1(long reserved1) {
		this.reserved1 = reserved1;
	}

	public void reduceReserved3(long reduce) {
		this.reserved3 -= reduce;
	}

	public long getReserved2() {
		return reserved2;
	}

	public void setReserved2(long reserved2) {
		this.reserved2 = reserved2;
	}

	public long getReserved3() {
		return reserved3;
	}

	public void setReserved3(long reserved3) {
		this.reserved3 = reserved3;
	}

	public boolean isPassive() {
		return getSkillTemplate().isPassive();
	}

	public boolean isProvoked() {
		return getSkillTemplate().isProvoked();
	}

	public boolean isBroadcast() {
		return getSkillTemplate().isBroadcast();
	}

	public int getSkillLevel() {
		return skillLevel;
	}

	public int maxSkillTarget() {
		return getSkillTemplate().getMaxStarget();
	}

	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
	}

	public Map<String, Integer> getValues() {
		return values;
	}

	public void setValues(Map<String, Integer> values) {
		this.values = values;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getBloodSuckValue() {
		return bloodSuckValue;
	}

	public void setBloodSuckValue(long bloodSuckValue) {
		this.bloodSuckValue = bloodSuckValue;
	}

	public long getReboundValue() {
		return reboundValue;
	}

	public void setReboundValue(long reboundValue) {
		this.reboundValue = reboundValue;
	}

	public void calculateSepecialEffect(long damage) {
		long bloodSucker = effector.getGameStats().getCurrentStat(StatEnum.LIFE_STEAL);
		long rebound = effected.getGameStats().getCurrentStat(StatEnum.RETURN_DAMAGE);
		if (bloodSucker > 0) {
			bloodSuckValue = (long) Math.ceil(bloodSucker * (damage * 1.0 / 10000.0));
		}
		if (rebound > 0) {
			reboundValue = (long) Math.ceil(rebound * (damage * 1.0 / 10000.0));
		}
	}

	public SpecialEffect getSpecialEffect() {
		if (bloodSuckValue == 0 && reboundValue == 0) {
			return null;
		}
		return SpecialEffect.valueOf(bloodSuckValue, reboundValue);
	}

	public HealEffect getHealEffect() {
		if (healDp == 0 && healHp == 0) {
			return null;
		}
		return HealEffect.valueOf(healDp, healHp);
	}

	public void addDuration(long elapsedTime) {
		this.duration += elapsedTime;
		this.endTime += elapsedTime;
	}

	public SkillTemplate getSkillTemplate() {
		return skillTemplate;
	}

	public void setSkillTemplate(SkillTemplate skillTemplate) {
		this.skillTemplate = skillTemplate;
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

	public ActionObserver getActionObserver() {
		return actionObserver;
	}

	public void setActionObserver(ActionObserver actionObserver) {
		this.actionObserver = actionObserver;
	}

	public AtomicBoolean getIsStopped() {
		return isStopped;
	}

	public int getOverlyCount() {
		return overlyCount;
	}

	public void setOverlyCount(int overlyCount) {
		this.overlyCount = overlyCount;
	}

	public long getLastProvokerEffectTime() {
		return lastProvokerEffectTime;
	}

	public void setLastProvokerEffectTime(long lastProvokerEffectTime) {
		this.lastProvokerEffectTime = lastProvokerEffectTime;
	}

	public long getHealDp() {
		return healDp;
	}

	public void setHealDp(long healDp) {
		this.healDp = healDp;
	}

	public long getHealHp() {
		return healHp;
	}

	public void setHealHp(long healHp) {
		this.healHp = healHp;
	}

}