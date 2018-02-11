package com.mmorpg.mir.model.beauty.model;

import java.util.HashMap;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.beauty.BeautyGirlConfig;
import com.mmorpg.mir.model.beauty.resource.BeautyGirlResource;
import com.mmorpg.mir.model.gameobjects.Player;

public class BeautyGirl {
	private String id;
	/** 等级 */
	private int level;
	/** 好感度 */
	private int sense;
	/** 已学习的技能 */
	private HashMap<Integer, Integer> proactiveSkills;

	private HashMap<Integer, Integer> passiveSkills;

	public static BeautyGirl valueOf(BeautyGirlResource resource) {
		BeautyGirl girl = new BeautyGirl();
		girl.id = resource.getId();
		girl.level = 0;
		girl.sense = 0;
		girl.proactiveSkills = new HashMap<Integer, Integer>();
		int i = 0;
		for (int skill : resource.getInitSkill()) {
			girl.proactiveSkills.put(i, skill);
			i++;
		}
		girl.passiveSkills = new HashMap<Integer, Integer>();
		return girl;
	}

	@JsonIgnore
	public boolean addSense(Player owner, int sense) {
		this.sense += sense;
		if (isEnoughSense()) {
			this.sense = 0;
			this.level++;
			LogManager.beautyUp(owner.getPlayerEnt().getServer(), owner.getPlayerEnt().getAccountName(),
					owner.getName(), owner.getObjectId(), System.currentTimeMillis(), this.id, this.level, this.sense);
			return true;
		}
		return false;
	}

	@JsonIgnore
	public boolean isMaxLevel() {
		BeautyGirlResource resource = BeautyGirlConfig.getInstance().beautyGirlStorage.get(id, true);
		return resource.getNeedSense()[this.level] == 0;
	}

	@JsonIgnore
	public boolean isEnoughSense() {
		BeautyGirlResource resource = BeautyGirlConfig.getInstance().beautyGirlStorage.get(this.id, true);
		return this.sense >= resource.getNeedSense()[this.level];
	}

	/***
	 * 升到下一级需要缠绵的次数
	 * 
	 * @return
	 */
	@JsonIgnore
	public int getNeedLingerCount() {
		BeautyGirlResource resource = BeautyGirlConfig.getInstance().beautyGirlStorage.get(this.id, true);
		int needSense = resource.getNeedSense()[this.level];
		int preAddSense = resource.getSense()[this.level];
		// 需要缠绵的次数
		int needTime = (needSense - this.sense) / preAddSense;
		return needTime;
	}

	@JsonIgnore
	public int getNeedLingerItemCount() {
		BeautyGirlResource resource = BeautyGirlConfig.getInstance().beautyGirlStorage.get(this.id, true);
		return getNeedLingerCount() * resource.getLingerActCount()[this.level];
	}

	/** getter -setter */
	public String getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public int getSense() {
		return sense;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setSense(int sense) {
		this.sense = sense;
	}

	public HashMap<Integer, Integer> getProactiveSkills() {
		return proactiveSkills;
	}

	public void setProactiveSkills(HashMap<Integer, Integer> proactiveSkills) {
		this.proactiveSkills = proactiveSkills;
	}

	public HashMap<Integer, Integer> getPassiveSkills() {
		return passiveSkills;
	}

	public void setPassiveSkills(HashMap<Integer, Integer> passiveSkills) {
		this.passiveSkills = passiveSkills;
	}

}
