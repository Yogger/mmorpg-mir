package com.mmorpg.mir.common;

/**
 * 所有的通信协议
 * 
 * @author Kuang Hao
 * @since v1.0 2018年2月18日
 *
 */
public interface PacketId {

	/** 请求登陆验证 */
	short LOGIN_AUTN_REQ = 1;
	/** 登陆验证结果 */
	short LOGIN_AUTN_RESP = 2;
	/** 请求角色列表信息 */
	short GET_PLAYERLIST_REQ = 3;
	/** 返回角色列表信息 */
	short GET_PLAYERLIST_RESP = 4;
}
