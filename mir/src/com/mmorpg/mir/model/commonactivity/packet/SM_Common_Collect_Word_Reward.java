package com.mmorpg.mir.model.commonactivity.packet;

public class SM_Common_Collect_Word_Reward{
	private int code;
	
	private String id;
	
	private String activeName;
	
	public static SM_Common_Collect_Word_Reward valueOf(CM_Common_Collect_Word_Reward cm){
		SM_Common_Collect_Word_Reward sm  = new SM_Common_Collect_Word_Reward();
		sm.activeName = cm.getActiveName();
		sm.setId(cm.getId());
		return sm;
	}

	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
}
