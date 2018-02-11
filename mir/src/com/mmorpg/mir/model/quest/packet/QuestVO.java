package com.mmorpg.mir.model.quest.packet;

import java.util.ArrayList;

public class QuestVO {

	private short templateId;
	private ArrayList<QuestKeyVO> vos;
	private byte phase;
	private long createTime;
	/** 任务星级 */
	private byte star;

	public ArrayList<QuestKeyVO> getVos() {
		return vos;
	}

	public void setVos(ArrayList<QuestKeyVO> vos) {
		this.vos = vos;
	}

	public byte getStar() {
		return star;
	}

	public void setStar(byte star) {
		this.star = star;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public short getTemplateId() {
		return templateId;
	}

	public void setTemplateId(short templateId) {
		this.templateId = templateId;
	}

	public byte getPhase() {
		return phase;
	}

	public void setPhase(byte phase) {
		this.phase = phase;
	}

}
