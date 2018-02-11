package com.mmorpg.mir.model.kingofwar.packet;

import com.mmorpg.mir.model.kingofwar.packet.vo.PlayerWarInfoVO;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;
import com.mmorpg.mir.model.reward.model.Reward;

public class SM_KingOfWar_EndList {
	private long endTime;
	private PlayerWarInfoVO playerWarInfoVO;
	private PlayerSimpleInfo pointsFirst;
	private PlayerSimpleInfo killFirst;
	private PlayerSimpleInfo bossDamageFirst;
	private PlayerSimpleInfo kingOfKing;
	private Reward reward;

	public static SM_KingOfWar_EndList valueOf(long endTime, PlayerWarInfoVO playerWarInfoVO,
			PlayerSimpleInfo pointsFirst, PlayerSimpleInfo killFirst, PlayerSimpleInfo bossDamageFirst,
			PlayerSimpleInfo kongOfKing, Reward reward) {
		SM_KingOfWar_EndList sm = new SM_KingOfWar_EndList();
		sm.endTime = endTime;
		sm.playerWarInfoVO = playerWarInfoVO;
		sm.pointsFirst = pointsFirst;
		sm.killFirst = killFirst;
		sm.bossDamageFirst = bossDamageFirst;
		sm.reward = reward;
		sm.kingOfKing = kongOfKing;
		return sm;
	}

	public PlayerWarInfoVO getPlayerWarInfoVO() {
		return playerWarInfoVO;
	}

	public void setPlayerWarInfoVO(PlayerWarInfoVO playerWarInfoVO) {
		this.playerWarInfoVO = playerWarInfoVO;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public PlayerSimpleInfo getPointsFirst() {
		return pointsFirst;
	}

	public void setPointsFirst(PlayerSimpleInfo pointsFirst) {
		this.pointsFirst = pointsFirst;
	}

	public PlayerSimpleInfo getKillFirst() {
		return killFirst;
	}

	public void setKillFirst(PlayerSimpleInfo killFirst) {
		this.killFirst = killFirst;
	}

	public PlayerSimpleInfo getBossDamageFirst() {
		return bossDamageFirst;
	}

	public void setBossDamageFirst(PlayerSimpleInfo bossDamageFirst) {
		this.bossDamageFirst = bossDamageFirst;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	public PlayerSimpleInfo getKingOfKing() {
		return kingOfKing;
	}

	public void setKingOfKing(PlayerSimpleInfo kingOfKing) {
		this.kingOfKing = kingOfKing;
	}

}
