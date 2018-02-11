package com.mmorpg.mir.model.mergeactive.resource;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class MergeConsumeCompeteResource {
	@Id
	private String id;
	/** 奖励ID */
	private String chooserGroupId;
	/** 竞技对应的排行榜ID */
	private Integer rankType;
	/** 记录数据的时间条件 */
	private String[] logDataCond;
	/** 进入排行榜的条件 */
	private String[] enterRankCond;
	/** 领取这个奖励的条件 */
	private String[] recieveCond;
	/** 没有领取这个奖励的补偿时间 */
	private String[] mailCompensateCond;
	/** 奖励补偿的邮件I18N标题 */
	private String mailI18nTitle;
	/** 奖励补偿的邮件I18N内容 */
	private String mailI18nContent;
	
	@Transient
	private CoreConditions logDataConditions;
	
	@Transient
	private CoreConditions enterRankConditions;
	
	@Transient
	private CoreConditions recieveConditions;
	
	@Transient
	private CoreConditions mailCompensateConditions;
	
	@JsonIgnore
	public CoreConditions getLogDataConditions() {
		if (logDataConditions == null) {
			logDataConditions = CoreConditionManager.getInstance().getCoreConditions(1, logDataCond);
		}
		return logDataConditions;
	}
	
	@JsonIgnore
	public CoreConditions getEnterRankConditions() {
		if (enterRankConditions == null) {
			enterRankConditions = CoreConditionManager.getInstance().getCoreConditions(1, enterRankCond);
		}
		return enterRankConditions;
	}

	@JsonIgnore
	public CoreConditions getRecieveConditions() {
		if (recieveConditions == null) {
			recieveConditions = CoreConditionManager.getInstance().getCoreConditions(1, recieveCond);
		}
		return recieveConditions;
	}
	
	@JsonIgnore
	public CoreConditions getMailCompensateConditions() {
		if (mailCompensateConditions == null) {
			mailCompensateConditions = CoreConditionManager.getInstance().getCoreConditions(1, mailCompensateCond);
		}
		return mailCompensateConditions;
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

	public Integer getRankType() {
		return rankType;
	}

	public void setRankType(Integer rankType) {
		this.rankType = rankType;
	}

	public String[] getEnterRankCond() {
		return enterRankCond;
	}

	public void setEnterRankCond(String[] enterRankCond) {
		this.enterRankCond = enterRankCond;
	}

	public String[] getRecieveCond() {
		return recieveCond;
	}

	public void setRecieveCond(String[] recieveCond) {
		this.recieveCond = recieveCond;
	}

	public String[] getMailCompensateCond() {
		return mailCompensateCond;
	}

	public void setMailCompensateCond(String[] mailCompensateCond) {
		this.mailCompensateCond = mailCompensateCond;
	}

	public String getMailI18nTitle() {
		return mailI18nTitle;
	}

	public void setMailI18nTitle(String mailI18nTitle) {
		this.mailI18nTitle = mailI18nTitle;
	}

	public String getMailI18nContent() {
		return mailI18nContent;
	}

	public void setMailI18nContent(String mailI18nContent) {
		this.mailI18nContent = mailI18nContent;
	}

	public String[] getLogDataCond() {
		return logDataCond;
	}

	public void setLogDataCond(String[] logDataCond) {
		this.logDataCond = logDataCond;
	}
	
}
