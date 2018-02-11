package com.mmorpg.mir.model.military.packet;

public class SM_Break_MilitaryStratege {
	private int code;
	
	private int session;
	
	private int id;
	
	public static SM_Break_MilitaryStratege valueOf(int session, int code, int id){
		SM_Break_MilitaryStratege sm = new SM_Break_MilitaryStratege();
		sm.code = code;
		sm.session = session;
		sm.id = id;
		return sm;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getSession() {
		return session;
	}

	public void setSession(int session) {
		this.session = session;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
