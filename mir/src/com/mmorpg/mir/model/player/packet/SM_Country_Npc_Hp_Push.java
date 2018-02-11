package com.mmorpg.mir.model.player.packet;

/** 国家特殊NPC(国旗和大臣)被砍的血量推送给前端显示 */
public class SM_Country_Npc_Hp_Push {

	private byte type;// 0国旗 1大臣
	private int countryId; // 国家ID
	private String hp; // npc血量百分比

	
	/**
	 * 
	 * @param type 0国旗 1大臣
	 * @param countryId  国家ID
	 * @param hp             血量%
	 * @return
	 */
	public static SM_Country_Npc_Hp_Push valueOf(int type, int countryId, String hp) {
		SM_Country_Npc_Hp_Push sm = new SM_Country_Npc_Hp_Push();
		sm.setType((byte) type);
		sm.setCountryId(countryId);
		sm.setHp(hp);
		return sm;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getHp() {
		return hp;
	}

	public void setHp(String hp) {
		this.hp = hp;
	}

}
