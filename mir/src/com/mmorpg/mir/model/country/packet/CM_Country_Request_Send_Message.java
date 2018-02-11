package com.mmorpg.mir.model.country.packet;


/**
 * 前段请求发送集结消息
 * @author 37wan
 *
 */
public class CM_Country_Request_Send_Message {

	private int countryId;//目标国家ID

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	
	
	
}
