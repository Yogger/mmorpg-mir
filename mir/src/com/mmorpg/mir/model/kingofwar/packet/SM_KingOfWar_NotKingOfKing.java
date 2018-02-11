package com.mmorpg.mir.model.kingofwar.packet;

public class SM_KingOfWar_NotKingOfKing {
	private long kingOfKingId;

	public static SM_KingOfWar_NotKingOfKing valueOf(long playerId) {
		SM_KingOfWar_NotKingOfKing sm = new SM_KingOfWar_NotKingOfKing();
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
