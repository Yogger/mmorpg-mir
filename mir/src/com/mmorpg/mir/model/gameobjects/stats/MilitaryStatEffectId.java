package com.mmorpg.mir.model.gameobjects.stats;


public class MilitaryStatEffectId extends StatEffectId {
	
	private int section;
	
	public static MilitaryStatEffectId valueOf(String objId, int section) {
		MilitaryStatEffectId effect = new MilitaryStatEffectId();
		effect.setId(objId);
		effect.setType(StatEffectType.MILITARY);
		effect.setSection(section);
		return effect;
	}
	
	public int getSection() {
		return section;
	}
	public void setSection(int section) {
		this.section = section;
	}
}
