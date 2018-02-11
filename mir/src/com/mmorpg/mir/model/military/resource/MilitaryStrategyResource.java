package com.mmorpg.mir.model.military.resource;

import java.util.List;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class MilitaryStrategyResource {

	public static final String SECTION_INDEX = "Military_Section";
	@Id
	private int id;

	private String[] conditionIds;

	@Index(name = SECTION_INDEX, comparatorClz = MilitaryStrategyResourceComparator.class)
	private Integer section;

	private int nextId;

	private String[] actions;

	private Stat[] archerStats;

	private Stat[] archerLayer;

	private Stat[] sorcererStats;

	private Stat[] sorcererLayer;

	private Stat[] strategistStats;

	private Stat[] strategistLayer;

	private Stat[] warriorStats;

	private Stat[] warriorLayer;

	private int percent;

	/** 兵法升级最少次数 */
	private int min;

	/** 兵法升级最大次数 */
	private int max;

	/** 是否需要突破 */
	private boolean needBreak;
	/** 突破消耗 */
	private String[] breakActions;
	/** 突破元宝消耗 */
	private String[] breakGoldActions;
	/** 突破概率 */
	private int breakPercent;
	/** 突破成功电视广播I18N */
	private String tvI18nId;
	/** 突破成功电视广播频道 */
	private int tvChannel;

	public String getTvI18nId() {
		return tvI18nId;
	}

	public void setTvI18nId(String tvI18nId) {
		this.tvI18nId = tvI18nId;
	}

	public int getTvChannel() {
		return tvChannel;
	}

	public void setTvChannel(int tvChannel) {
		this.tvChannel = tvChannel;
	}

	public String[] getBreakActions() {
		return breakActions;
	}

	public void setBreakActions(String[] breakActions) {
		this.breakActions = breakActions;
	}

	public int getBreakPercent() {
		return breakPercent;
	}

	public void setBreakPercent(int breakPercent) {
		this.breakPercent = breakPercent;
	}

	@JsonIgnore
	public List<Stat> getRoleStat(int role) {
		int indexStat = 0, indexLayer = 0;
		List<Stat> stats = New.arrayList();
		switch (role) {
		case 1:
			while (warriorStats != null && indexStat < warriorStats.length) {
				stats.add(warriorStats[indexStat++]);
			}
			while (warriorLayer != null && indexLayer < warriorLayer.length) {
				stats.add(warriorLayer[indexLayer++]);
			}
			return stats;
		case 2:
			while (archerStats != null && indexStat < archerStats.length) {
				stats.add(archerStats[indexStat++]);
			}
			while (archerLayer != null && indexLayer < archerLayer.length) {
				stats.add(archerLayer[indexLayer++]);
			}
			return stats;
		case 3:
			while (strategistStats != null && indexStat < strategistStats.length) {
				stats.add(strategistStats[indexStat++]);
			}
			while (strategistLayer != null && indexLayer < strategistLayer.length) {
				stats.add(strategistLayer[indexLayer++]);
			}
			return stats;
		case 4:
			while (sorcererStats != null && indexStat < sorcererStats.length) {
				stats.add(sorcererStats[indexStat++]);
			}
			while (sorcererLayer != null && indexLayer < sorcererLayer.length) {
				stats.add(sorcererLayer[indexLayer++]);
			}
			return stats;
		default:
			break;
		}
		return null;
	}

	public Integer getSection() {
		return section;
	}

	public void setSection(Integer section) {
		this.section = section;
	}

	public int getNextId() {
		return nextId;
	}

	public void setNextId(int nextId) {
		this.nextId = nextId;
	}

	public Stat[] getArcherStats() {
		return archerStats;
	}

	public void setArcherStats(Stat[] archerStats) {
		this.archerStats = archerStats;
	}

	public Stat[] getSorcererStats() {
		return sorcererStats;
	}

	public void setSorcererStats(Stat[] sorcererStats) {
		this.sorcererStats = sorcererStats;
	}

	public Stat[] getStrategistStats() {
		return strategistStats;
	}

	public void setStrategistStats(Stat[] strategistStats) {
		this.strategistStats = strategistStats;
	}

	public Stat[] getWarriorStats() {
		return warriorStats;
	}

	public void setWarriorStats(Stat[] warriorStats) {
		this.warriorStats = warriorStats;
	}

	public Stat[] getArcherLayer() {
		return archerLayer;
	}

	public void setArcherLayer(Stat[] archerLayer) {
		this.archerLayer = archerLayer;
	}

	public Stat[] getSorcererLayer() {
		return sorcererLayer;
	}

	public void setSorcererLayer(Stat[] sorcererLayer) {
		this.sorcererLayer = sorcererLayer;
	}

	public Stat[] getStrategistLayer() {
		return strategistLayer;
	}

	public void setStrategistLayer(Stat[] strategistLayer) {
		this.strategistLayer = strategistLayer;
	}

	public Stat[] getWarriorLayer() {
		return warriorLayer;
	}

	public void setWarriorLayer(Stat[] warriorLayer) {
		this.warriorLayer = warriorLayer;
	}

	@Transient
	private CoreActions action;

	@Transient
	private CoreConditions conditions;

	@Transient
	private CoreActions breakAction;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String[] getConditionIds() {
		return conditionIds;
	}

	public void setConditionIds(String[] conditionIds) {
		this.conditionIds = conditionIds;
	}

	public String[] getActions() {
		return actions;
	}

	public void setActions(String[] actions) {
		this.actions = actions;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	@JsonIgnore
	public CoreConditions getConditions() {
		if (conditions == null) {
			if (conditionIds == null) {
				conditions = new CoreConditions();
			} else {
				conditions = CoreConditionManager.getInstance().getCoreConditions(1, conditionIds);
			}
		}
		return conditions;
	}

	@JsonIgnore
	public CoreActions getAction() {
		if (action == null) {
			if (actions == null) {
				action = new CoreActions();
			} else {
				action = CoreActionManager.getInstance().getCoreActions(1, actions);
			}
		}
		return action;
	}

	@JsonIgnore
	public CoreActions getBreakAction() {
		if (breakAction == null) {
			if (breakActions == null) {
				breakAction = new CoreActions();
			} else {
				breakAction = CoreActionManager.getInstance().getCoreActions(1, breakActions);
			}
		}
		return breakAction;
	}

	private CoreActions breakGoldAction;

	@JsonIgnore
	public CoreActions getBreakGoldAction() {
		if (breakGoldAction == null) {
			if (breakGoldActions == null) {
				breakGoldAction = new CoreActions();
			} else {
				breakGoldAction = CoreActionManager.getInstance().getCoreActions(1, breakGoldActions);
			}
		}
		return breakGoldAction;
	}

	public boolean isNeedBreak() {
		return needBreak;
	}

	public void setNeedBreak(boolean needBreak) {
		this.needBreak = needBreak;
	}

	public String[] getBreakGoldActions() {
		return breakGoldActions;
	}

	public void setBreakGoldActions(String[] breakGoldActions) {
		this.breakGoldActions = breakGoldActions;
	}
}