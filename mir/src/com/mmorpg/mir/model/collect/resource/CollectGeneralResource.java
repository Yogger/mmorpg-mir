package com.mmorpg.mir.model.collect.resource;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CollectGeneralResource {

	@Id
	private String id;
	/** 收集的对应NPC的ObjectKey */
	private String objectKeyOwner;
	/** 收集的对应NPC的数量 */
	private int num;
	/** 收集到对应数量激活后获得的属性 <这里有就获得> */
	private Stat[] stats;
	/** 收集到对应数量激活后获得的被动技能的ID <这里有就获得> */
	private int[] skillId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObjectKeyOwner() {
		return objectKeyOwner;
	}

	public void setObjectKeyOwner(String objectKeyOwner) {
		this.objectKeyOwner = objectKeyOwner;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Stat[] getStats() {
		return stats;
	}

	public void setStats(Stat[] stats) {
		this.stats = stats;
	}

	public int[] getSkillId() {
		return skillId;
	}

	public void setSkillId(int[] skillId) {
		this.skillId = skillId;
	}
	

	@JsonIgnore
	public StatEffectId getStatsEffectId() {
		return StatEffectId.valueOf("FAMED_GENERAL_STATID" + id, StatEffectType.FAMED_GENERAL_STATID);
	}
}
