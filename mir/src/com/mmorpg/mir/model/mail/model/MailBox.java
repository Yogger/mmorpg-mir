package com.mmorpg.mir.model.mail.model;

import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.item.NullItem;
import com.windforce.common.utility.JsonUtils;

public class MailBox {

	private static final int DEFAULT_SIZE = 50;

	/** 当前邮件背包收到的最大的系统全局邮件id */
	private long groupId;
	/** 所有的邮件 */
	private Mail[] mails;
	@JsonIgnore
	private transient BitSet marks;

	private int size;

	private Long playerId;

	private String accountName;

	private String name;

	public MailBox() {
		this(DEFAULT_SIZE);
	}

	public MailBox(int size) {
		this.size = size;
		mails = new Mail[size];
		marks = new BitSet(size);
	}

	public static MailBox valueOf() {
		return new MailBox(DEFAULT_SIZE);
	}

	public void add(Mail mail) {
		Mail addMail = mail;
		int minIndex = -1;
		long startTime = Long.MAX_VALUE;
		// 然后在空的格子上添加
		if (addMail != null) {
			for (int i = 0, j = mails.length; i < j; i++) {
				if (mails[i] == null) {
					mails[i] = addMail;
					marks.set(i);
					addMail = null;
					break;
				} else {
					if (mails[i].getStartTime() < startTime) {
						startTime = mails[i].getStartTime();
						minIndex = i;
					}
				}
			}
		}

		if (addMail != null) {
			// 覆盖最后一封邮件
			if (mails[minIndex] != null) {
				LogManager.mailLog(getAccountName(), getPlayerId(), getName(), System.currentTimeMillis(),
						mails[minIndex].getTitle(), mails[minIndex].getContext(),
						JsonUtils.object2String(mails[minIndex].getI18nTile()),
						JsonUtils.object2String(mails[minIndex].getI18nContext()),
						JsonUtils.object2String(mails[minIndex].getReward()), 2);
			}
			mails[minIndex] = addMail;
			marks.set(minIndex);
		}

		if (mail != null) {
			if (mail.getGroupId() != 0) {
				if (mail.getGroupId() > groupId) {
					this.groupId = mail.getGroupId();
				}
			}
		}

		LogManager.mailLog(getAccountName(), getPlayerId(), getName(), System.currentTimeMillis(), mail.getTitle(),
				mail.getContext(), JsonUtils.object2String(mail.getI18nTile()),
				JsonUtils.object2String(mail.getI18nContext()), JsonUtils.object2String(mail.getReward()), 1);
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public Mail[] getMails() {
		return mails;
	}

	public void setMails(Mail[] mails) {
		this.mails = mails;
	}

	@JsonIgnore
	public BitSet getMarks() {
		return marks;
	}

	public void setMarks(BitSet marks) {
		this.marks = marks;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Integer, Object> collectUpdate() {
		Map<Integer, Object> result = new LinkedHashMap<Integer, Object>();
		for (int i = 0, j = marks.length(); i < j; i++) {
			if (marks.get(i)) {
				if (mails[i] == null) {
					result.put(i, NullItem.getInstance());
				} else {
					result.put(i, mails[i]);
				}
			}
			marks.set(i, false);
		}
		return result;
	}

	public Mail getMailByIndex(int index) {
		if (index >= 0 && index < mails.length) {
			return mails[index];
		}
		return null;
	}

	public void markMailByIndex(int index) {
		if (index >= 0 && index < mails.length) {
			marks.set(index);
		}
	}

	public void deleteMailByIndex(int index) {
		if (index >= 0 && index < mails.length) {
			Mail mail = mails[index];
			mails[index] = null;
			marks.set(index);
			LogManager.mailLog(getAccountName(), getPlayerId(), getName(), System.currentTimeMillis(), mail.getTitle(),
					mail.getContext(), JsonUtils.object2String(mail.getI18nTile()),
					JsonUtils.object2String(mail.getI18nContext()), JsonUtils.object2String(mail.getReward()), 2);
		}
	}

	public static void main(String[] args) {
		/*
		 * MailBox box = new MailBox(); for (int i = 0; i < 1000; i++) { Mail
		 * mail = new Mail(); mail.setId(i);
		 * mail.setStartTime(mail.getStartTime() + i); // box.add(mail); }
		 * System.out.println(box.getMails().length); for (Mail mail :
		 * box.getMails()) { if (mail != null) { System.out.println("mailId : "
		 * + mail.getId()); } }
		 * 
		 * System.out.println(box.getGroupId());
		 */
	}
}
