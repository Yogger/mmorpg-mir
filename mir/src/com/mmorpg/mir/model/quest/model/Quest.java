package com.mmorpg.mir.model.quest.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.quest.manager.QuestManager;
import com.mmorpg.mir.model.quest.packet.QuestKeyVO;
import com.mmorpg.mir.model.quest.packet.QuestVO;
import com.mmorpg.mir.model.quest.resource.QuestResource;
import com.windforce.common.utility.JsonUtils;

public class Quest {

	public static final byte FULLSTAR = 4;

	private String id;
	/** 归属 */
	private Player owner;
	/** 所有条件参数 */
	private List<QuestKey> keys;
	/** 任务状态 */
	private QuestPhase phase;
	/** 创建时间 */
	private long createTime;
	/** 任务星级 */
	private byte star;
	/** 任务怪 */
	@Transient
	private List<Npc> questMonsters;

	public static void main(String[] args) {
		String[] ss = JsonUtils.string2Array("[1,2,2,1]", String.class);
		System.out.println(JsonUtils.object2String(ss));
	}

	public QuestVO createVO() {
		QuestVO vo = new QuestVO();
		if (keys != null && !keys.isEmpty()) {
			vo.setVos(new ArrayList<QuestKeyVO>());
			for (QuestKey qk : keys) {
				vo.getVos().add(qk.createVO());
			}
		}
		vo.setTemplateId(QuestManager.getInstance().convertTemplateId(id));
		vo.setPhase(phase.getValue());
		vo.setCreateTime(createTime);
		vo.setStar((byte) (star + 1));
		return vo;
	}

	public void fullStar() {
		this.star = FULLSTAR;
	}

	public static Quest valueOf(Player player, String id, long now) {
		Quest task = new Quest();
		task.keys = New.arrayList();
		task.createTime = now;
		task.id = id;
		task.phase = QuestPhase.INCOMPLETE;
		task.owner = player;
		return task;
	}

	public QuestKey findKey(String keyname) {
		for (QuestKey questKey : keys) {
			if (questKey.getKeyname().equals(keyname)) {
				return questKey;
			}
		}
		return null;
	}

	@JsonIgnore
	public QuestResource getResource() {
		return QuestManager.staticQuestResources.get(id, true);
	}

	public boolean canAccept() {
		return getResource().getAcceptConditions().verify(this);
	}

	public boolean canCompletion() {
		return getResource().getCompleteConditions().verify(this);
	}

	public boolean canGiveUp() {
		return getResource().getGiveUpConditions().verify(this);
	}

	public boolean canFail() {
		return getResource().getFailConditions().verify(this);
	}

	public boolean canRemove() {
		return getResource().getRemoveConditions().verify(this);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public List<QuestKey> getKeys() {
		return keys;
	}

	public void setKeys(List<QuestKey> keys) {
		this.keys = keys;
	}

	public QuestPhase getPhase() {
		return phase;
	}

	public void setPhase(QuestPhase phase) {
		this.phase = phase;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public byte getStar() {
		return star;
	}

	public void setStar(byte star) {
		this.star = star;
	}

	@JsonIgnore
	public List<Npc> getQuestMonsters() {
		return questMonsters;
	}

	public void setQuestMonsters(List<Npc> questMonsters) {
		this.questMonsters = questMonsters;
	}

}
