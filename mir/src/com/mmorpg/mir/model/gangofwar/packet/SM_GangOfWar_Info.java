package com.mmorpg.mir.model.gangofwar.packet;

import com.mmorpg.mir.model.gangofwar.packet.vo.BossHpVO;
import com.mmorpg.mir.model.gangofwar.packet.vo.DefendGang;
import com.mmorpg.mir.model.gangofwar.packet.vo.PlayerGangWarInfoVO;

public class SM_GangOfWar_Info {
	private long endTime;
	private PlayerGangWarInfoVO playerGangWarInfoVO;
	private BossHpVO currentHp;
	private BossHpVO maxHp;
	private DefendGang defendGang;

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public PlayerGangWarInfoVO getPlayerGangWarInfoVO() {
		return playerGangWarInfoVO;
	}

	public void setPlayerGangWarInfoVO(PlayerGangWarInfoVO playerGangWarInfoVO) {
		this.playerGangWarInfoVO = playerGangWarInfoVO;
	}

	public BossHpVO getCurrentHp() {
		return currentHp;
	}

	public void setCurrentHp(BossHpVO currentHp) {
		this.currentHp = currentHp;
	}

	public BossHpVO getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(BossHpVO maxHp) {
		this.maxHp = maxHp;
	}

	public DefendGang getDefendGang() {
		return defendGang;
	}

	public void setDefendGang(DefendGang defendGang) {
		this.defendGang = defendGang;
	}

}
