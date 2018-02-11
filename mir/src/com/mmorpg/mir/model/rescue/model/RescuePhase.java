package com.mmorpg.mir.model.rescue.model;

public enum RescuePhase {
	/** 未接取 */
	INACCEPT((byte) 0),
	/** 进行中 */
	INCOMPLETE((byte) 1),
	/** 未领奖 */
	INREWARD((byte) 2),
	/** 完成 */
	COMPLETE((byte) 3);

	private byte value;

	public static RescuePhase valueOf(byte code) {
		for (RescuePhase id : values()) {
			if (id.getValue() == code) {
				return id;
			}
		}
		throw new RuntimeException(" no match type of CountryId[" + code + "]");
	}

	private RescuePhase(byte code) {
		this.value = code;
	}

	public byte getValue() {
		return this.value;
	}

	public static RescuePhase typeOf(String code) {
		for (RescuePhase qp : RescuePhase.values()) {
			if (qp.equals(code)) {
				return qp;
			}
		}
		throw new RuntimeException(String.format("Error type QuestPhase code[%s]", code));
	}
}
