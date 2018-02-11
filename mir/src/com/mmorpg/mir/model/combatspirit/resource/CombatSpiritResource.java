package com.mmorpg.mir.model.combatspirit.resource;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.combatspirit.model.CombatSpiritStorage.CombatSpiritType;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CombatSpiritResource {
	/** 唯一标识 */
	@Id
	private String id;
	/** 类型,护符,宝物,勋章 */
	private CombatSpiritType type;
	/** 下一级的唯一标识 */
	private String nextId;
	/** 升到下一级 */
	private int upgradeNeed;
	private Stat[] currentStats;
	private Stat[] currentQualityStats;

	private String[] conds;

	private int quality;
	private int level;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public final CombatSpiritType getType() {
		return type;
	}

	public final void setType(CombatSpiritType type) {
		this.type = type;
	}

	public int getUpgradeNeed() {
		return upgradeNeed;
	}

	public void setUpgradeNeed(int upgradeNeed) {
		this.upgradeNeed = upgradeNeed;
	}

	public final Stat[] getCurrentStats() {
		return currentStats;
	}

	public final void setCurrentStats(Stat[] currentStats) {
		this.currentStats = currentStats;
	}

	public String getNextId() {
		return nextId;
	}

	public void setNextId(String nextId) {
		this.nextId = nextId;
	}

	public Stat[] getCurrentQualityStats() {
		return currentQualityStats;
	}

	public void setCurrentQualityStats(Stat[] currentQualityStats) {
		this.currentQualityStats = currentQualityStats;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String[] getConds() {
		return conds;
	}

	public void setConds(String[] conds) {
		this.conds = conds;
	}

	@JsonIgnore
	public CoreConditions getConditions() {
		return CoreConditionManager.getInstance().getCoreConditions(1, this.conds);
	}
}
