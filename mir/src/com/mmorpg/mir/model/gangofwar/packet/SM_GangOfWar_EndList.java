package com.mmorpg.mir.model.gangofwar.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.gangofwar.model.PlayerGangWarInfo;
import com.mmorpg.mir.model.gangofwar.packet.vo.GangOfWarRankItem;
import com.mmorpg.mir.model.gangofwar.packet.vo.PlayerGangWarInfoVO;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;
import com.mmorpg.mir.model.reward.model.Reward;

public class SM_GangOfWar_EndList {
	private long endTime;
	private PlayerGangWarInfoVO playerWarInfoVO;
	private ArrayList<GangOfWarRankItem> items;
	private PlayerSimpleInfo king;
	private Reward reward;

	public static SM_GangOfWar_EndList valueOf(long endTime, PlayerGangWarInfo player,
			ArrayList<GangOfWarRankItem> items, Reward reward, PlayerSimpleInfo king) {
		SM_GangOfWar_EndList sm = new SM_GangOfWar_EndList();
		sm.endTime = endTime;
		sm.playerWarInfoVO = player.createVO();
		sm.items = items;
		sm.reward = reward;
		sm.king = king;
		return sm;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public PlayerGangWarInfoVO getPlayerWarInfoVO() {
		return playerWarInfoVO;
	}

	public void setPlayerWarInfoVO(PlayerGangWarInfoVO playerWarInfoVO) {
		this.playerWarInfoVO = playerWarInfoVO;
	}

	public ArrayList<GangOfWarRankItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<GangOfWarRankItem> items) {
		this.items = items;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

	public PlayerSimpleInfo getKing() {
		return king;
	}

	public void setKing(PlayerSimpleInfo king) {
		this.king = king;
	}

}
