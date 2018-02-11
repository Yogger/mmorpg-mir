package com.mmorpg.mir.model.gameobjects;

public enum RequestHandlerType {
	/** 组队邀请 */
	GROUP_INVITE(1),
	/** 交易邀请 */
	EXCHANGE_INVITE(2),
	/** 国王召集 */
	COUNTRY_CALLTOGETHER(3),
	/** 卫队召集 */
	COUNTRY_GUARDTOGETHER(4),
	/** 全体动员 */
	OFFICIAL_MOBILIZATION(5),
	/** 家族救援令 */
	ASK_GANG_FOR_HELP(6),
	/**储君召集*/
	RESERVEKING_CALLTOGETHER(7);

	private int value;

	public static RequestHandlerType valueOf(int statusCode) {
		for (RequestHandlerType status : values()) {
			if (status.value == statusCode) {
				return status;
			}
		}
		throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
	}

	private RequestHandlerType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
