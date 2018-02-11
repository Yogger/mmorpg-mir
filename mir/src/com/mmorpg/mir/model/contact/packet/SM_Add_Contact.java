package com.mmorpg.mir.model.contact.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.contact.model.SocialNetData;

public class SM_Add_Contact {

	private int code;
	private ArrayList<SocialNetData> enemy;

	public static SM_Add_Contact valueOf(int code, ArrayList<SocialNetData> data) {
		SM_Add_Contact sm = new SM_Add_Contact();
		sm.code = code;
		sm.enemy = data;
		return sm;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public ArrayList<SocialNetData> getEnemy() {
		return enemy;
	}

	public void setEnemy(ArrayList<SocialNetData> enemy) {
		this.enemy = enemy;
	}
	
}
