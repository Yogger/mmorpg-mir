package com.mmorpg.mir.model.country.packet;

public class SM_Official_Mobilization {

	private String sponsor;
	private String official;
	private String phrases;

	public static SM_Official_Mobilization valueOf(String s, String p, String o) {
		SM_Official_Mobilization sm = new SM_Official_Mobilization();
		sm.phrases = p;
		sm.sponsor = s;
		sm.official = o;
		return sm;
	}
	
	public String getSponsor() {
		return sponsor;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	public String getPhrases() {
		return phrases;
	}

	public void setPhrases(String phrases) {
		this.phrases = phrases;
	}

	public String getOfficial() {
    	return official;
    }

	public void setOfficial(String official) {
    	this.official = official;
    }

}
