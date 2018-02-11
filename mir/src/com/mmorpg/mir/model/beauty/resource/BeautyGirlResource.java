package com.mmorpg.mir.model.beauty.resource;

import java.util.Map;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.action.model.CoreActionResource;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.object.resource.SkillSelectorItemSample;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class BeautyGirlResource {
	public static final String SPAWN_INDEX = "SPAWN_INDEX";

	@Id
	private String id;
	/** spawkey */
	@Index(name = SPAWN_INDEX, unique = true)
	private String spawnKey;
	/** 激活条件 */
	private String[] activeConds;
	/** 激活条件（且为true,或为false） */
	private boolean activeVerify;
	/** 激活消耗 */
	private String[] activeActs;
	/** 一次缠绵需要的材料份数 */
	private int[] lingerActCount;
	/** 缠绵需要的一份材料 */
	private CoreActionResource[] lingerMaterialAct;
	/** 缠绵自动购买的ShopResource#id */
	private String autoBuyShopId;
	/** 每次缠绵增加的缠绵值 */
	private int[] sense;
	/** 升级到下一级须要的缠绵值 */
	private int[] needSense;
	/** 出战时给主人加的buff */
	private int[] passiveSkills;
	/** 给主人的属性 */
	private Stat[][] masterStats;
	/** 美人自身属性 */
	private Stat[][] beautyStats;
	/** 技能 */
	private SkillSelectorItemSample[] skillSelectorItemSamples;
	/** 初始技能id */
	private int[] initSkill;
	/** 技能格子数 */
	private int maxSkillCount;
	/** 激活广播 */
	private Map<String, Integer> activeNotice;
	@Transient
	private transient CoreConditions activeConditions;

	@Transient
	private transient CoreActions activeActions;

	@JsonIgnore
	public CoreConditions getActiveConditions() {
		if (null == activeConditions) {
			activeConditions = CoreConditionManager.getInstance().getCoreConditions(1, this.activeConds);
		}
		return activeConditions;
	}

	@JsonIgnore
	public CoreActions getActiveActions() {
		if (null == activeActions) {
			activeActions = CoreActionManager.getInstance().getCoreActions(1, this.activeActs);
		}
		return activeActions;
	}

	@JsonIgnore
	public boolean containSkillId(int skillId) {
		for (SkillSelectorItemSample s : this.skillSelectorItemSamples) {
			if (s.getSkillId() == skillId) {
				return true;
			}
		}
		return false;
	}

	@JsonIgnore
	public boolean containPassiveSkill(int passiveSkillId) {
		if (this.passiveSkills != null) {
			for (int skillId : this.passiveSkills) {
				if (skillId == passiveSkillId) {
					return true;
				}
			}
		}
		return false;
	}

	@JsonIgnore
	public int getNeedSenseByLevel(int level) {
		return this.needSense[level];
	}

	@JsonIgnore
	public int getSenseByLevel(int level) {
		return this.sense[level];
	}

	@JsonIgnore
	public int getLingerActCountByLevel(int level) {
		return this.lingerActCount[level];
	}

	public String getId() {
		return id;
	}

	public String getSpawnKey() {
		return spawnKey;
	}

	public String[] getActiveConds() {
		return activeConds;
	}

	public boolean isActiveVerify() {
		return activeVerify;
	}

	public String[] getActiveActs() {
		return activeActs;
	}

	public int[] getLingerActCount() {
		return lingerActCount;
	}

	public CoreActionResource[] getLingerMaterialAct() {
		return lingerMaterialAct;
	}

	public String getAutoBuyShopId() {
		return autoBuyShopId;
	}

	public int[] getSense() {
		return sense;
	}

	public int[] getNeedSense() {
		return needSense;
	}

	public Stat[][] getMasterStats() {
		return masterStats;
	}

	public Stat[][] getBeautyStats() {
		return beautyStats;
	}

	public SkillSelectorItemSample[] getSkillSelectorItemSamples() {
		return skillSelectorItemSamples;
	}

	public int[] getInitSkill() {
		return initSkill;
	}

	public int getMaxSkillCount() {
		return maxSkillCount;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSpawnKey(String spawnKey) {
		this.spawnKey = spawnKey;
	}

	public void setActiveConds(String[] activeConds) {
		this.activeConds = activeConds;
	}

	public void setActiveVerify(boolean activeVerify) {
		this.activeVerify = activeVerify;
	}

	public void setActiveActs(String[] activeActs) {
		this.activeActs = activeActs;
	}

	public void setLingerActCount(int[] lingerActCount) {
		this.lingerActCount = lingerActCount;
	}

	public void setLingerMaterialAct(CoreActionResource[] lingerMaterialAct) {
		this.lingerMaterialAct = lingerMaterialAct;
	}

	public void setAutoBuyShopId(String autoBuyShopId) {
		this.autoBuyShopId = autoBuyShopId;
	}

	public void setSense(int[] sense) {
		this.sense = sense;
	}

	public void setNeedSense(int[] needSense) {
		this.needSense = needSense;
	}

	public void setMasterStats(Stat[][] masterStats) {
		this.masterStats = masterStats;
	}

	public void setBeautyStats(Stat[][] beautyStats) {
		this.beautyStats = beautyStats;
	}

	public void setSkillSelectorItemSamples(SkillSelectorItemSample[] skillSelectorItemSamples) {
		this.skillSelectorItemSamples = skillSelectorItemSamples;
	}

	public void setInitSkill(int[] initSkill) {
		this.initSkill = initSkill;
	}

	public void setMaxSkillCount(int maxSkillCount) {
		this.maxSkillCount = maxSkillCount;
	}

	public int[] getPassiveSkills() {
		return passiveSkills;
	}

	public void setPassiveSkills(int[] passiveSkills) {
		this.passiveSkills = passiveSkills;
	}

	public Map<String, Integer> getActiveNotice() {
		return activeNotice;
	}

	public void setActiveNotice(Map<String, Integer> activeNotice) {
		this.activeNotice = activeNotice;
	}

	
}
