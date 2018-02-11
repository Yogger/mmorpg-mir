package com.mmorpg.mir.model.nickname.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.nickname.model.Nickname;
import com.windforce.common.utility.New;

public class SM_Nickname_Waited_Active {
	private ArrayList<Nickname> waitedActive;

	public static SM_Nickname_Waited_Active valueOf(Nickname... nicknames) {
		SM_Nickname_Waited_Active sm = new SM_Nickname_Waited_Active();
		ArrayList<Nickname> actives = New.arrayList();
		for (Nickname name : nicknames) {
			actives.add(name);
		}
		sm.waitedActive = actives;
		return sm;
	}

	public ArrayList<Nickname> getWaitedActive() {
		return waitedActive;
	}

	public void setWaitedActive(ArrayList<Nickname> waitedActive) {
		this.waitedActive = waitedActive;
	}

}
