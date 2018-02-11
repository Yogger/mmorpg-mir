package com.mmorpg.mir.model.quest.model;

public enum QuestPhase {
	/** 未完成 */
	INCOMPLETE((byte) 0),
	/** 完成 */
	COMPLETE((byte) 1),
	/** 失败 */
	FAIL((byte) 2);

	private byte value;

	public static QuestPhase valueOf(byte code) {
		for (QuestPhase id : values()) {
			if (id.getValue() == code) {
				return id;
			}
		}
		throw new RuntimeException(" no match type of CountryId[" + code + "]");
	}

	private QuestPhase(byte code) {
		this.value = code;
	}

	public byte getValue() {
		return this.value;
	}

	public static QuestPhase typeOf(String code) {
		for (QuestPhase qp : QuestPhase.values()) {
			if (qp.equals(code)) {
				return qp;
			}
		}
		throw new RuntimeException(String.format("Error type QuestPhase code[%s]", code));
	}
}
