package com.mmorpg.mir.model.kingofwar.packet;

public class SM_KingOfWar_KingOfKing {
	private long kingOfKingId;

	public static SM_KingOfWar_KingOfKing valueOf(long playerId) {
		SM_KingOfWar_KingOfKing sm = new SM_KingOfWar_KingOfKing();
		sm.kingOfKingId = playerId;
		return sm;
	}

	public long getKingOfKingId() {
		return kingOfKingId;
	}

	public void setKingOfKingId(long kingOfKingId) {
		this.kingOfKingId = kingOfKingId;
	}

}
