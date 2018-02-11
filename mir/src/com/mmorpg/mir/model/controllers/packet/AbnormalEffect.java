package com.mmorpg.mir.model.controllers.packet;

import java.util.Map;

import org.h2.util.New;

import com.mmorpg.mir.model.skill.effect.Effect;

public class AbnormalEffect {
	private int skillId;
	private long elapsedTime;
	private int level;
	private Map<Byte, Long> context;
	/** 叠加层数 */
	private byte overlyCount;

	public static AbnormalEffect valueOf(int skillId, long elapsedTime, Effect effect) {
		AbnormalEffect abe = new AbnormalEffect();
		abe.skillId = skillId;
		abe.elapsedTime = elapsedTime;
		if (effect.getReserved3() != 0) {
			// 血包剩余容量值
			abe.addContext((byte) 3, effect.getReserved3());
		}
		abe.level = effect.getSkillLevel();
		abe.overlyCount = (byte) effect.getOverlyCount();

		return abe;
	}

	public void addContext(byte key, long value) {
		if (context == null) {
			context = New.hashMap();
		}
		context.put(key, value);
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public Map<Byte, Long> getContext() {
		return context;
	}

	public void setContext(Map<Byte, Long> context) {
		this.context = context;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public byte getOverlyCount() {
		return overlyCount;
	}

	public void setOverlyCount(byte overlyCount) {
		this.overlyCount = overlyCount;
	}

}
