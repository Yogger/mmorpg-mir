package com.mmorpg.mir.model.mail.model;

import java.util.concurrent.CopyOnWriteArrayList;

import javax.persistence.Transient;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.util.IdentifyManager;
import com.mmorpg.mir.model.util.IdentifyManager.IdentifyType;
import com.windforce.common.utility.DateUtils;

public class MailGroup {

	private static final long DEFAULT_EXPIRED_TIME = DateUtils.MILLIS_PER_DAY;

	private long id;
	/** 开始时间 */
	private long startTime;
	/** 结束时间 */
	private long endTime;
	/** 奖励附件 */
	private Reward reward;
	/** 标题 */
	private String title;
	/** 发送人 */
	private String sender;
	/** 内容 */
	private String context;
	/** 已经领取奖励的人 */
	private CopyOnWriteArrayList<Long> receiveds = new CopyOnWriteArrayList<Long>();
	/** 领取条件 */
	private CoreConditionResource[] receivedCondtionResources;
	/** i18n内容结构体 */
	private I18nUtils i18nContext;
	/** i18n标题结构体 */
	private I18nUtils i18nTile;

	@Transient
	private CoreConditions conditions;

	/**
	 * 生成邮件
	 * 
	 * @return
	 */
	@JsonIgnore
	public Mail createMail() {
		Reward copy = null;
		if (reward != null) {
			copy = reward.copy();
		}
		Mail mail = Mail.valueOf(i18nTile, i18nContext, sender, copy);
		mail.setGroupId(id);
		mail.setStartTime(startTime);
		mail.setTitle(title);
		mail.setContext(context);
		return mail;
	}

	public static MailGroup valueOf(String title, String sender, String context, Reward reward) {
		MailGroup mailGroup = new MailGroup();
		mailGroup.title = title;
		mailGroup.sender = sender;
		mailGroup.context = context;
		mailGroup.reward = reward;
		mailGroup.startTime = System.currentTimeMillis();
		mailGroup.endTime = mailGroup.startTime + DEFAULT_EXPIRED_TIME;
		mailGroup.id = IdentifyManager.getInstance().getNextIdentify(IdentifyType.MAILGROUP);
		return mailGroup;
	}

	public static MailGroup valueOf(String title, String sender, String context, Reward reward,
			CoreConditionResource[] receiveCondition, long expiredTime) {
		MailGroup mailGroup = new MailGroup();
		mailGroup.title = title;
		mailGroup.sender = sender;
		mailGroup.context = context;
		mailGroup.reward = reward;
		mailGroup.receivedCondtionResources = receiveCondition;
		mailGroup.startTime = System.currentTimeMillis();
		if (expiredTime > 0)
			mailGroup.endTime = expiredTime;
		else
			mailGroup.endTime = mailGroup.startTime + DEFAULT_EXPIRED_TIME;
		mailGroup.id = IdentifyManager.getInstance().getNextIdentify(IdentifyType.MAILGROUP);
		return mailGroup;
	}

	public static MailGroup valueOf(I18nUtils i18nTile, I18nUtils i18nContext, String sender,
			CoreConditionResource[] receiveCondition, Reward reward) {
		MailGroup mailGroup = new MailGroup();
		mailGroup.id = IdentifyManager.getInstance().getNextIdentify(IdentifyType.MAILGROUP);
		mailGroup.i18nTile = i18nTile;
		mailGroup.i18nContext = i18nContext;
		mailGroup.reward = reward;
		mailGroup.sender = sender;
		mailGroup.startTime = System.currentTimeMillis();
		mailGroup.endTime = mailGroup.startTime + DEFAULT_EXPIRED_TIME;
		mailGroup.receivedCondtionResources = receiveCondition;
		return mailGroup;
	}

	public static MailGroup valueOf(I18nUtils i18nTile, I18nUtils i18nContext, String sender,
			CoreConditionResource[] receiveCondition, Reward reward, long expiredTime) {
		MailGroup mailGroup = new MailGroup();
		mailGroup.id = IdentifyManager.getInstance().getNextIdentify(IdentifyType.MAILGROUP);
		mailGroup.i18nTile = i18nTile;
		mailGroup.i18nContext = i18nContext;
		mailGroup.reward = reward;
		mailGroup.sender = sender;
		mailGroup.startTime = System.currentTimeMillis();
		if (expiredTime > 0)
			mailGroup.endTime = expiredTime;
		else
			mailGroup.endTime = mailGroup.startTime + DEFAULT_EXPIRED_TIME;
		mailGroup.receivedCondtionResources = receiveCondition;
		return mailGroup;
	}

	@JsonIgnore
	public void addReceiveConditionResource(CoreConditionResource resource) {
		receivedCondtionResources = (CoreConditionResource[]) ArrayUtils.add(receivedCondtionResources, resource);
	}

	/**
	 * 验证是否过期
	 * 
	 * @param now
	 * @return
	 */
	@JsonIgnore
	public boolean isExpired(long now) {
		if (startTime <= now && now <= endTime) {
			return false;
		}
		return true;
	}

	public CopyOnWriteArrayList<Long> getReceiveds() {
		return receiveds;
	}

	public void setReceiveds(CopyOnWriteArrayList<Long> receiveds) {
		this.receiveds = receiveds;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public I18nUtils getI18nContext() {
		return i18nContext;
	}

	public void setI18nContext(I18nUtils i18nContext) {
		this.i18nContext = i18nContext;
	}

	public I18nUtils getI18nTile() {
		return i18nTile;
	}

	public void setI18nTile(I18nUtils i18nTile) {
		this.i18nTile = i18nTile;
	}

	public CoreConditionResource[] getReceivedCondtionResources() {
		return receivedCondtionResources;
	}

	public void setReceivedCondtionResources(CoreConditionResource[] receivedCondtionResources) {
		this.receivedCondtionResources = receivedCondtionResources;
	}

	@JsonIgnore
	public CoreConditions getConditions() {
		if (conditions == null) {
			if (receivedCondtionResources == null || receivedCondtionResources.length == 0) {
				conditions = new CoreConditions();
			} else {
				conditions = CoreConditionManager.getInstance().getCoreConditions(1, receivedCondtionResources);
			}
		}
		return conditions;
	}
}
