package com.mmorpg.mir.model.nickname.model.vo;

import java.util.ArrayList;

import com.mmorpg.mir.model.nickname.model.Nickname;

public class NickNamePoolVO {

	private ArrayList<Integer> equips;
	private ArrayList<Nickname> actives;

	public ArrayList<Integer> getEquips() {
		return equips;
	}

	public void setEquips(ArrayList<Integer> equips) {
		this.equips = equips;
	}

	public ArrayList<Nickname> getActives() {
		return actives;
	}

	public void setActives(ArrayList<Nickname> actives) {
		this.actives = actives;
	}

}
