package com.mmorpg.mir.model.mail.model;

import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.util.IdentifyManager;
import com.mmorpg.mir.model.util.IdentifyManager.IdentifyType;

public class Mail {

	private transient long id;
	/** 组邮件id */
	private transient long groupId;
	/** 标题 */
	private String title;
	/** 内容 */
	private String context;
	/** 发送人 */
	private String sender;
	/** 发送人NPC */
	private String npcId;
	/** 奖励附件 */
	private Reward reward;
	/** 邮件创建时间 */
	private long startTime;
	/** 是否查看 */
	private int look;
	/** i18n内容结构体 */
	private I18nUtils i18nContext;
	/** i18n标题结构体 */
	private I18nUtils i18nTile;

	public static Mail valueOf(String title, String sender, String context, Reward reward) {
		Mail mail = new Mail();
		mail.id = IdentifyManager.getInstance().getNextIdentify(IdentifyType.MAIL);
		mail.title = title;
		mail.context = context;
		mail.sender = sender;
		mail.reward = reward;
		mail.startTime = System.currentTimeMillis();
		return mail;
	}

	public static Mail valueOf(I18nUtils i18nTile, I18nUtils i18nContext, String sender, Reward reward) {
		Mail mail = new Mail();
		mail.id = IdentifyManager.getInstance().getNextIdentify(IdentifyType.MAIL);
		mail.i18nTile = i18nTile;
		mail.i18nContext = i18nContext;
		mail.reward = reward;
		mail.sender = sender;
		mail.startTime = System.currentTimeMillis();
		return mail;
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

	public int getLook() {
		return look;
	}

	public void setLook(int look) {
		this.look = look;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public Mail cloneMail() {
		Mail result = Mail.valueOf(title, sender, context, reward != null ? reward.copy() : null);
		result.startTime = startTime;
		if (i18nContext != null) {
			result.setI18nContext(I18nUtils.valueOf(i18nContext.getId(), i18nContext));
		}
		if (i18nTile != null) {
			result.setI18nTile(I18nUtils.valueOf(i18nTile.getId(), i18nTile));
		}
		return result;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
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

	public String getNpcId() {
		return npcId;
	}

	public void setNpcId(String npcId) {
		this.npcId = npcId;
	}

}
