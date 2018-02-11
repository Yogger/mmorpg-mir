package com.mmorpg.mir.model.kingofwar.packet;

import com.mmorpg.mir.model.kingofwar.packet.vo.BossHpVO;
import com.mmorpg.mir.model.kingofwar.packet.vo.KingCommandVO;
import com.mmorpg.mir.model.kingofwar.packet.vo.PlayerWarInfoVO;
import com.mmorpg.mir.model.kingofwar.packet.vo.ReliveStatusVO;

public class SM_KingOfWar_Info {
	private long endTime;
	private PlayerWarInfoVO playerWarInfoVO;
	private BossHpVO currentHp;
	private BossHpVO maxHp;
	private ReliveStatusVO reliveStatus;
	private KingCommandVO commands;

	public PlayerWarInfoVO getPlayerWarInfoVO() {
		return playerWarInfoVO;
	}

	public void setPlayerWarInfoVO(PlayerWarInfoVO playerWarInfoVO) {
		this.playerWarInfoVO = playerWarInfoVO;
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

	public ReliveStatusVO getReliveStatus() {
		return reliveStatus;
	}

	public void setReliveStatus(ReliveStatusVO reliveStatus) {
		this.reliveStatus = reliveStatus;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public KingCommandVO getCommands() {
		return commands;
	}

	public void setCommands(KingCommandVO commands) {
		this.commands = commands;
	}

}
