package com.mmorpg.mir.model.object.resource;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.dynamicstats.StatsResource;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;
import com.windforce.common.utility.JsonUtils;

/**
 * 这个配置表是生物的全量配置表，包含了所有类别的生物所需要的属性集合，但是会由生物管理器根据不同的类别来创建 原理类似与物品管理器
 * 
 * @author zhou.liu
 * 
 */
@Resource
public class ObjectResource {
	@Id
	private String key;
	@Index(name = "templateId", unique = true)
	private short templateId;

	private String name;

	private ObjectType objectType;

	private String rewardId;
	/** 采集物品的冷却时间间隔 **/
	private int interval;

	private Stat[] stats;

	private int[] skills;

	private int level;

	private String dropKey;

	private boolean share;
	/** 是否可见 */
	private boolean hiding;
	/** 血量恢复百分比(0-10000) */
	private int restoreHp;
	/** 是否是动态属性 true,false */
	private boolean dynamicStats;
	/** 等级类型 0-世界等级，1-玩家等级 */
	private int levelType;
	/** 属性公式计算 */
	private Map<String, String> statsFormula;
	/** 经验是否共享 */
	private boolean expShare;
	/** 记录掉落. true-要记录,false-不记录 */
	private boolean dropRecord;
	/** 最小世界等级 */
	private int minWorldLevel;
	/** 最大世界等级 */
	private int maxWorldLevel;
	/** 是否等级压制 */
	private boolean levelSuppress;
	/** 等级掉落限制 */
	private int levelGap;
	/** 技能赛选器 */
	private SkillSelectorItemSample[] skillSelectorItemSamples;
	/** 是否将玩家打下马 */
	private boolean hitUnRide;
	/** 怪物身上的光环 */
	private int auraSkillId;
	/** 脱战以后恢复朝向 */
	private boolean fightOffHeading;
	/** 是否经验衰减 */
	private boolean expReduction;
	/** 记录玩家击杀掉落的道具 */
	private String[] dropItemKeys;
	/** 掉落是否是无主掉落 */
	private boolean dropWithNoOwner;
	/** 被击杀增加的瘴气值 */
	private int addGasValue;
	/** 被击杀增加BOSS积分值 */
	private int addBossCoins;

	/** 动态属性中间表 */
	@Transient
	private Map<Integer, List<Stat>> dynamicStatsResults;

	@JsonIgnore
	public List<Stat> getStats(StatsResource statsResource) {
		if (dynamicStatsResults == null) {
			dynamicStatsResults = New.hashMap();
		}
		if (!dynamicStatsResults.containsKey(statsResource.getStatslevel())) {
			List<Stat> stats = New.arrayList();
			for (Entry<String, String> entry : statsFormula.entrySet()) {
				Map<String, Object> context = new HashMap<String, Object>();
				context.put("res", statsResource);
				Serializable formulaExp = MVEL.compileExpression(entry.getValue(), parserContext);
				long valueA = MVEL.executeExpression(formulaExp, context, Long.class);
				Stat stat = new Stat();
				stat.setType(StatEnum.valueOf(entry.getKey()));
				stat.setValueA(valueA);
				stats.add(stat);
			}
			dynamicStatsResults.put(statsResource.getStatslevel(), stats);
		}
		List<Stat> newStats = New.arrayList();
		for (Stat stat : dynamicStatsResults.get(statsResource.getStatslevel())) {
			newStats.add(stat.getNewProperty());
		}
		return newStats;
	}

	/** 表达式上下文 */
	private static final ParserContext parserContext = new ParserContext();

	static {
		/** 导入 {@link Math} 中的全部静态方法 */
		parserContext.addImport(Math.class);
		for (Method method : Math.class.getMethods()) {
			int mod = method.getModifiers();
			if (Modifier.isStatic(mod) && Modifier.isPublic(mod)) {
				String name = method.getName();
				parserContext.addImport(name, method);
			}
		}
	}

	public static void main(String[] args) {
		ObjectResource or = new ObjectResource();
		Map<String, String> statsFormula = New.hashMap();
		statsFormula.put(StatEnum.PHYSICAL_ATTACK.name(),
				"res.getField('aLessMagicalDefense')*1.2/0.5/3/3 + res.getField('aAttack')");
		statsFormula.put(StatEnum.PHYSICAL_DEFENSE.name(),
				"res.getField('aLessMagicalDefense')*1.2/0.5/3/3 + res.getField('aAttack')");
		or.setStatsFormula(statsFormula);
		System.out.println(JsonUtils.object2String(or.getStatsFormula()));
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	public String getRewardId() {
		return rewardId;
	}

	public void setRewardId(String rewardId) {
		this.rewardId = rewardId;
	}

	public Stat[] getStats() {
		return stats;
	}

	public void setStats(Stat[] stats) {
		this.stats = stats;
	}

	public int[] getSkills() {
		return skills;
	}

	public void setSkills(int[] skills) {
		this.skills = skills;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDropKey() {
		return dropKey;
	}

	public void setDropKey(String dropKey) {
		this.dropKey = dropKey;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isShare() {
		return share;
	}

	public void setShare(boolean share) {
		this.share = share;
	}

	public boolean isHiding() {
		return hiding;
	}

	public void setHiding(boolean hiding) {
		this.hiding = hiding;
	}

	public int getRestoreHp() {
		return restoreHp;
	}

	public void setRestoreHp(int restoreHp) {
		this.restoreHp = restoreHp;
	}

	public short getTemplateId() {
		return templateId;
	}

	public void setTemplateId(short templateId) {
		this.templateId = templateId;
	}

	public boolean isDynamicStats() {
		return dynamicStats;
	}

	public void setDynamicStats(boolean dynamicStats) {
		this.dynamicStats = dynamicStats;
	}

	public int getLevelType() {
		return levelType;
	}

	public void setLevelType(int levelType) {
		this.levelType = levelType;
	}

	public Map<String, String> getStatsFormula() {
		return statsFormula;
	}

	public void setStatsFormula(Map<String, String> statsFormula) {
		this.statsFormula = statsFormula;
	}

	public boolean isExpShare() {
		return expShare;
	}

	public void setExpShare(boolean expShare) {
		this.expShare = expShare;
	}

	public boolean isDropRecord() {
		return dropRecord;
	}

	public void setDropRecord(boolean dropRecord) {
		this.dropRecord = dropRecord;
	}

	public int getMinWorldLevel() {
		return minWorldLevel;
	}

	public void setMinWorldLevel(int minWorldLevel) {
		this.minWorldLevel = minWorldLevel;
	}

	public boolean isLevelSuppress() {
		return levelSuppress;
	}

	public void setLevelSuppress(boolean levelSuppress) {
		this.levelSuppress = levelSuppress;
	}

	public SkillSelectorItemSample[] getSkillSelectorItemSamples() {
		return skillSelectorItemSamples;
	}

	public void setSkillSelectorItemSamples(SkillSelectorItemSample[] skillSelectorItemSamples) {
		this.skillSelectorItemSamples = skillSelectorItemSamples;
	}

	public int getLevelGap() {
		return levelGap;
	}

	public void setLevelGap(int levelGap) {
		this.levelGap = levelGap;
	}

	public boolean isHitUnRide() {
		return hitUnRide;
	}

	public void setHitUnRide(boolean hitUnRide) {
		this.hitUnRide = hitUnRide;
	}

	public int getAuraSkillId() {
		return auraSkillId;
	}

	public void setAuraSkillId(int auraSkillId) {
		this.auraSkillId = auraSkillId;
	}

	public boolean isFightOffHeading() {
		return fightOffHeading;
	}

	public void setFightOffHeading(boolean fightOffHeading) {
		this.fightOffHeading = fightOffHeading;
	}

	public boolean isExpReduction() {
		return expReduction;
	}

	public void setExpReduction(boolean expReduction) {
		this.expReduction = expReduction;
	}

	public String[] getDropItemKeys() {
		return dropItemKeys;
	}

	public void setDropItemKeys(String[] dropItemKeys) {
		this.dropItemKeys = dropItemKeys;
	}

	public int getMaxWorldLevel() {
		return maxWorldLevel;
	}

	public void setMaxWorldLevel(int maxWorldLevel) {
		this.maxWorldLevel = maxWorldLevel;
	}

	public boolean isDropWithNoOwner() {
		return dropWithNoOwner;
	}

	public void setDropWithNoOwner(boolean dropWithNoOwner) {
		this.dropWithNoOwner = dropWithNoOwner;
	}

	public int getAddGasValue() {
		return addGasValue;
	}

	public void setAddGasValue(int addGasValue) {
		this.addGasValue = addGasValue;
	}

	public int getAddBossCoins() {
		return addBossCoins;
	}

	public void setAddBossCoins(int addBossCoins) {
		this.addBossCoins = addBossCoins;
	}

	public Map<Integer, List<Stat>> getDynamicStatsResults() {
		return dynamicStatsResults;
	}

	public void setDynamicStatsResults(Map<Integer, List<Stat>> dynamicStatsResults) {
		this.dynamicStatsResults = dynamicStatsResults;
	}


}
