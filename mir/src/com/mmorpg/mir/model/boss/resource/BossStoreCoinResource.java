package com.mmorpg.mir.model.boss.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.consumable.CoreActionManager;
import com.mmorpg.mir.model.core.consumable.CoreActions;
import com.mmorpg.mir.model.core.consumable.model.CoreActionResource;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class BossStoreCoinResource {

	@Id
	private String id;
	
	/** 下一级的唯一标识 */
	private String nextId;
	/** 升级消耗 */
	private CoreActionResource[] levelUpactions;
	
	private CoreActionResource[] buyActions;
	
	private int quality;
	
	private int level;

	private Stat[] levelStats;
	
	private Stat[] payedStats;
	
	@Transient
	private CoreActions buyCoreActions;
	
	@Transient
	private CoreActions upgradeCoreActions;
	
	@JsonIgnore
	public CoreActions getBuyCoreActions() { 
		if (buyCoreActions == null) {
			buyCoreActions = CoreActionManager.getInstance().getCoreActions(1, buyActions);
		}
		return buyCoreActions;
	}
	
	@JsonIgnore
	public CoreActions getUpgradeCoreActions() {
		if (upgradeCoreActions == null) {
			upgradeCoreActions = CoreActionManager.getInstance().getCoreActions(1, levelUpactions);
		}
		return upgradeCoreActions;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNextId() {
		return nextId;
	}

	public void setNextId(String nextId) {
		this.nextId = nextId;
	}

	public Stat[] getLevelStats() {
		return levelStats;
	}

	public void setLevelStats(Stat[] levelStats) {
		this.levelStats = levelStats;
	}

	public Stat[] getPayedStats() {
		return payedStats;
	}

	public void setPayedStats(Stat[] payedStats) {
		this.payedStats = payedStats;
	}

	public CoreActionResource[] getLevelUpactions() {
		return levelUpactions;
	}

	public void setLevelUpactions(CoreActionResource[] levelUpactions) {
		this.levelUpactions = levelUpactions;
	}

	public CoreActionResource[] getBuyActions() {
		return buyActions;
	}

	public void setBuyActions(CoreActionResource[] buyActions) {
		this.buyActions = buyActions;
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

}
