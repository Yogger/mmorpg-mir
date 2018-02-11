package com.mmorpg.mir.model.object;

/**
 * 这个用来定义生物类别
 * 
 * @author zhou.liu
 * 
 */
public enum ObjectType {
	/** ROBOT **/
	ROBOT,
	/** NPC **/
	NPC,
	/** 怪物 **/
	MONSTER,
	/** 召唤物 **/
	SUMMON,
	/** 特殊召唤物 大B哥 **/
	BIGBROTHER,
	/** 镖车 **/
	LORRY,
	/** 玩家 **/
	PLAYER,
	/** 采集物 **/
	GATHERABLE,
	/** 掉落物品 **/
	DROPOBJECT,
	/** 静态物品 **/
	STATICOBJECT,
	/** 雕塑 */
	SCULPTURE,
	/** 国家物件 */
	COUNTRY_OBJECT,
	/** 国家NPC */
	COUNTRY_NPC,
	/** 状态转换物件 */
	STATUS_NPC,
	/** BOSS */
	BOSS,
	/** 监管者 */
	SUPERVISOR,
	/** 城池玩家NPC */
	TOWN_PLAYER_NPC,
	/** 美人 */
	SERVANT;

}
