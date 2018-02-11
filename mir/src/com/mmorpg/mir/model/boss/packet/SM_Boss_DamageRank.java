package com.mmorpg.mir.model.boss.packet;

import java.util.Map;

import com.mmorpg.mir.model.boss.vo.BossDamageVO;

public class SM_Boss_DamageRank {
	private Map<Integer, BossDamageVO> nameDamages;
	private long bossMaxHp;

	public static SM_Boss_DamageRank valueOf(Map<Integer, BossDamageVO> nameDamages, long bossMaxHp) {
		SM_Boss_DamageRank sm = new SM_Boss_DamageRank();
		sm.setNameDamages(nameDamages);
		sm.bossMaxHp = bossMaxHp;
		return sm;
	}

	public long getBossMaxHp() {
		return bossMaxHp;
	}

	public void setBossMaxHp(long bossMaxHp) {
		this.bossMaxHp = bossMaxHp;
	}

	public Map<Integer, BossDamageVO> getNameDamages() {
		return nameDamages;
	}

	public void setNameDamages(Map<Integer, BossDamageVO> nameDamages) {
		this.nameDamages = nameDamages;
	}

}
