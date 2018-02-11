package com.mmorpg.mir.model.country.packet;

/**
 * 处理前端请求发送集结消息
 * 
 * @author 37wan
 * 
 */
public class SM_Country_Request_Send_Message {

	private int code;// 错误码
	private long coolTime;// 下次可以发送的时间

	public long getCoolTime() {
		return coolTime;
	}

	public void setCoolTime(long coolTime) {
		this.coolTime = coolTime;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
