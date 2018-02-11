package com.mmorpg.mir.model.commonactivity.packet;

import java.util.ArrayList;

public class SM_Common_Consume_Push {

	private ArrayList<String> canRecieves;

	private String activityName;

	public static SM_Common_Consume_Push valueOf(String activityName, ArrayList<String> canRecieves) {
		SM_Common_Consume_Push sm = new SM_Common_Consume_Push();
		sm.activityName = activityName;
		sm.canRecieves = canRecieves;
		return sm;
	}

	public ArrayList<String> getCanRecieves() {
		return canRecieves;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setCanRecieves(ArrayList<String> canRecieves) {
		this.canRecieves = canRecieves;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

}
