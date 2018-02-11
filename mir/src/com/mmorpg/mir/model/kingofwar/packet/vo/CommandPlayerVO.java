package com.mmorpg.mir.model.kingofwar.packet.vo;

import com.mmorpg.mir.model.gameobjects.Player;

public class CommandPlayerVO {
	private String name;
	private String gangName;
	private int battle;

	public static CommandPlayerVO valueOf(Player player) {
		CommandPlayerVO vo = new CommandPlayerVO();
		vo.setName(player.getName());
		vo.setGangName(player.getGang() != null ? player.getGang().getName() : null);
		vo.setBattle(player.getGameStats().calcBattleScore());
		return vo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGangName() {
		return gangName;
	}

	public void setGangName(String gangName) {
		this.gangName = gangName;
	}

	public int getBattle() {
		return battle;
	}

	public void setBattle(int battle) {
		this.battle = battle;
	}

}
