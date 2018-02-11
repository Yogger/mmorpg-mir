package com.mmorpg.mir.model.player.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class RPResource {

	@Id
	private int rpId;

	/** 人品值最低要求 */
	private int notBelow;
	/** 人品值对应的人品状态 */
	private RPType type;
	/** 每天人品减少的值 */
	private int dailyReduce;
	/** 被击杀的时候提供的人品值 */
	private int offerRP;
	/** 击杀获得的RP值的条件们 */
	private String[] acquireConditionId;
	/** 提供加经验的属性 */
	private Stat[] stats;

	@Transient
	private CoreConditions acquireConditions;

	public int getRpId() {
		return rpId;
	}

	public void setRpId(int rpId) {
		this.rpId = rpId;
	}

	public RPType getType() {
		return type;
	}

	public void setType(RPType type) {
		this.type = type;
	}

	public int getNotBelow() {
		return notBelow;
	}

	public void setNotBelow(int notBelow) {
		this.notBelow = notBelow;
	}

	public int getDailyReduce() {
		return dailyReduce;
	}

	public void setDailyReduce(int dailyReduce) {
		this.dailyReduce = dailyReduce;
	}

	public int getOfferRP() {
		return offerRP;
	}

	public void setOfferRP(int offerRP) {
		this.offerRP = offerRP;
	}

	@JsonIgnore
	public CoreConditions getCoreConditions() {
		if (acquireConditions == null) {
			if (acquireConditionId == null) {
				acquireConditions = new CoreConditions();
			} else {
				acquireConditions = CoreConditionManager.getInstance().getCoreConditions(1, acquireConditionId);
			}
		}
		return acquireConditions;
	}

	public CoreConditions getAcquireConditions() {
		return acquireConditions;
	}

	@JsonIgnore
	public void setAcquireConditions(CoreConditions acquireConditions) {
		this.acquireConditions = acquireConditions;
	}

	public String[] getAcquireConditionId() {
		return acquireConditionId;
	}

	public void setAcquireConditionId(String[] acquireConditionId) {
		this.acquireConditionId = acquireConditionId;
	}

	public Stat[] getStats() {
		return stats;
	}

	public void setStats(Stat[] stats) {
		this.stats = stats;
	}
}
