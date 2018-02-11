package com.mmorpg.mir.model.nickname.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.nickname.model.Nickname;
import com.mmorpg.mir.model.nickname.model.NicknamePool;
import com.windforce.common.utility.New;

public class SM_Nickname_Active {
	private ArrayList<Nickname> actives;
	private int currentActiveId;

	public static SM_Nickname_Active valueOf(NicknamePool pool, int id) {
		SM_Nickname_Active sm = new SM_Nickname_Active();
		ArrayList<Nickname> arr = New.arrayList();
		for (Nickname name : pool.getActiveds().values()) {
			if (name.isActived()) {
				arr.add(name);
			}
		}
		sm.actives = arr;
		sm.currentActiveId = id;
		return sm;
	}

	public ArrayList<Nickname> getActives() {
		return actives;
	}

	public void setActives(ArrayList<Nickname> actives) {
		this.actives = actives;
	}

	public Integer getCurrentActiveId() {
		return currentActiveId;
	}

	public void setCurrentActiveId(Integer currentActiveId) {
		this.currentActiveId = currentActiveId;
	}

}
