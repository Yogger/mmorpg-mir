package com.mmorpg.mir.model.system.packet;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;

public class SM_System_Message {

	public static final SM_System_Message BLOCK_MESSAGE = SM_System_Message.valueOf(ManagedErrorCode.IP_BLOCK);

	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public static SM_System_Message valueOf(int code) {
		return MessageCache.getMessage(code);
	}
}
