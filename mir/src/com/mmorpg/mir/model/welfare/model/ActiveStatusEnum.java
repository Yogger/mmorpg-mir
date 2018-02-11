package com.mmorpg.mir.model.welfare.model;

public enum ActiveStatusEnum {

	/** 未开启 */
	STATUS_NOT_OPEN(0),

	/** 未完成 */
	STATUS_NOT_COMPLETED(1),

	/** 完成 */
	STATUS_COMPLETED(2);

	private final int status;

	private ActiveStatusEnum(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

}
