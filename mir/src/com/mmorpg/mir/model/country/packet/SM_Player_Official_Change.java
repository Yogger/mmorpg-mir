package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.country.model.CountryOfficial;

public class SM_Player_Official_Change {
	private long targetId;
	private String officialValue;

	public static SM_Player_Official_Change valueOf(long objId, String officialValue) {
		SM_Player_Official_Change sm = new SM_Player_Official_Change();
		sm.targetId = objId;
		if (officialValue.equals(CountryOfficial.CITIZEN)) {
			sm.officialValue = null;
		} else {
			sm.officialValue = officialValue;
		}
		return sm;
	}

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	public String getOfficialValue() {
    	return officialValue;
    }

	public void setOfficialValue(String officialValue) {
    	this.officialValue = officialValue;
    }

}
