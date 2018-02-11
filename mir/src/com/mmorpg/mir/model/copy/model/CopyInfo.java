package com.mmorpg.mir.model.copy.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.copy.manager.CopyManager;
import com.mmorpg.mir.model.copy.resource.CopyResource;

public class CopyInfo {
	private String copyId;
	private int mapId;
	private int encourgeCount;
	private boolean reward;
	private boolean copyComplet;
	private int hpCount;
	private int damageCount;
	private transient CopyDestoryController copyDestoryController;
	private int skillId;
	private int killMonster;
	private long exp;
	private long createTime;
	/** 特殊副本 失败返还特殊消耗, true表示不需要返还 */
	private boolean failNotReturnSpecialAct;
	/** 城池战，对方的玩家ID */
	private long targetPlayerId;

	public boolean destory() {
		if (copyId == null) {
			return false;
		}
		if (copyDestoryController == null) {
			return true;
		}

		return copyDestoryController.destory();
	}

	@JsonIgnore
	public CopyResource getCopyResource() {
		if (copyId == null) {
			return null;
		}
		return CopyManager.getInstance().getCopyResources().get(copyId, true);
	}

	public void addKillMonster() {
		killMonster++;
	}

	public void addExp(long exp) {
		this.exp += exp;
	}

	public int addEncourgeCount(int count) {
		encourgeCount += count;
		return encourgeCount;
	}

	public int addHpEncourgeCount(int count) {
		hpCount += count;
		return hpCount;
	}

	public int addDamageEncourgeCount(int count) {
		damageCount += count;
		return damageCount;
	}

	public int getEncourgeCount() {
		return encourgeCount;
	}

	public void setEncourgeCount(int encourgeCount) {
		this.encourgeCount = encourgeCount;
	}

	public boolean isReward() {
		return reward;
	}

	public void setReward(boolean reward) {
		this.reward = reward;
	}

	public int getHpCount() {
		return hpCount;
	}

	public void setHpCount(int hpCount) {
		this.hpCount = hpCount;
	}

	public int getDamageCount() {
		return damageCount;
	}

	public void setDamageCount(int damageCount) {
		this.damageCount = damageCount;
	}

	public String getCopyId() {
		return copyId;
	}

	public void setCopyId(String copyId) {
		this.copyId = copyId;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public CopyDestoryController getCopyDestoryController() {
		return copyDestoryController;
	}

	public void setCopyDestoryController(CopyDestoryController copyDestoryController) {
		this.copyDestoryController = copyDestoryController;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public int getKillMonster() {
		return killMonster;
	}

	public void setKillMonster(int killMonster) {
		this.killMonster = killMonster;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public boolean isFailNotReturnSpecialAct() {
		return failNotReturnSpecialAct;
	}

	public void setFailNotReturnSpecialAct(boolean failNotReturnSpecialAct) {
		this.failNotReturnSpecialAct = failNotReturnSpecialAct;
	}

	public long getTargetPlayerId() {
		return targetPlayerId;
	}

	public void setTargetPlayerId(long targetPlayerId) {
		this.targetPlayerId = targetPlayerId;
	}

	public boolean isCopyComplet() {
		return copyComplet;
	}

	public void setCopyComplet(boolean copyComplet) {
		this.copyComplet = copyComplet;
	}

}
