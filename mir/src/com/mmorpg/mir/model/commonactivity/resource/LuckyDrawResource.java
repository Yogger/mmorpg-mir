package com.mmorpg.mir.model.commonactivity.resource;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class LuckyDrawResource {
	@Id
	private String id;
	
	/** 奖池 */
	private String chooserGroupId;
	/** 单次的抽奖需要充值的数量*/
	private int preLuckyDrawRecharge;
	/** 开启幸运抽奖的条件*/
	private String[] conditionIds;
	/** 广播道具等级等级*/
	private int quality;
	/** 电视广播I18N */
	private String tvI18nId;
	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

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

	public String getChartI18nId() {
		return chartI18nId;
	}

	public void setChartI18nId(String chartI18nId) {
		this.chartI18nId = chartI18nId;
	}

	public int getChartChannel() {
		return chartChannel;
	}

	public void setChartChannel(int chartChannel) {
		this.chartChannel = chartChannel;
	}

	/** 电视广播频道 */
	private int tvChannel;
	/** 聊天广播I18N */
	private String chartI18nId;
	/** 聊天广播频道 */
	private int chartChannel;
	
	private CoreConditions coreConditions;
	@JsonIgnore
	public CoreConditions getCoreConditions() {
		if (null == coreConditions) {
			coreConditions = CoreConditionManager.getInstance().getCoreConditions(1, conditionIds);
		}
		return coreConditions;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChooserGroupId() {
		return chooserGroupId;
	}

	public void setChooserGroupId(String chooserGroupId) {
		this.chooserGroupId = chooserGroupId;
	}

	public int getPreLuckyDrawRecharge() {
		return preLuckyDrawRecharge;
	}

	public void setPreLuckyDrawRecharge(int preLuckyDrawRecharge) {
		this.preLuckyDrawRecharge = preLuckyDrawRecharge;
	}

	public String[] getConditionIds() {
		return conditionIds;
	}

	public void setConditionIds(String[] conditionIds) {
		this.conditionIds = conditionIds;
	}
}
