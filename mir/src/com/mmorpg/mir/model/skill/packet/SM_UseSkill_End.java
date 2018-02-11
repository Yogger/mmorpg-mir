package com.mmorpg.mir.model.skill.packet;

import java.util.ArrayList;
import java.util.List;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.model.EffectVO;

/**
 * 通知玩家使用技能
 * 
 * @author liuzhou
 * 
 */
public class SM_UseSkill_End {
	/** 技能使用者 */
	private long effector;
	private short skillId;
	private ArrayList<EffectVO> effectList;

	public long getEffector() {
		return effector;
	}

	public void setEffector(long effector) {
		this.effector = effector;
	}

	public short getSkillId() {
		return skillId;
	}

	public void setSkillId(short skillId) {
		this.skillId = skillId;
	}

	public ArrayList<EffectVO> getEffectList() {
		return effectList;
	}

	public void setEffectList(ArrayList<EffectVO> effectList) {
		this.effectList = effectList;
	}

	public static SM_UseSkill_End valueOf(Creature creature, int skillId, List<Effect> effectList) {
		SM_UseSkill_End result = new SM_UseSkill_End();
		if (creature != null) {
			result.effector = creature.getObjectId();
		}
		result.skillId = (short) skillId;
		ArrayList<EffectVO> effectVoList = new ArrayList<EffectVO>(effectList.size());
		for (Effect effect : effectList) {
			effectVoList.add(EffectVO.valueOf(effect));
		}
		result.effectList = effectVoList;
		return result;
	}
}
