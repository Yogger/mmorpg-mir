package com.mmorpg.mir.admin.packet;

public class SGM_Gang_Expel {
	/**
	 * 错误码可能 :成功0;咸阳争夺战进行中 -926; 家族战正在进行中 -927; 帮派不存在 -875,不能驱逐族长 -716;
	 */
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}