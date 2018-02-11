package com.mmorpg.mir.model.country.model.vo;

import com.mmorpg.mir.model.country.model.TraitorPlayerFix;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.session.SessionManager;

public class TraitorVO implements Comparable<TraitorVO>{

	private String name;
	private long battlePower;
	
	public static TraitorVO valueOf(TraitorPlayerFix tp) {
		TraitorVO vo = new TraitorVO();
		vo.name = tp.getName();
		if (SessionManager.getInstance().isOnline(tp.getPlayerId())) {		
			Player player = PlayerManager.getInstance().getPlayer(tp.getPlayerId());
			vo.battlePower = player.getGameStats().calcBattleScore();
		} else {
			vo.battlePower = tp.getBattlePower();
		}
		return vo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getBattlePower() {
		return battlePower;
	}

	public void setBattlePower(long battlePower) {
		this.battlePower = battlePower;
	}

	@Override
	public int compareTo(TraitorVO o) {
		return (int) (o.getBattlePower() - battlePower);
	}

}
