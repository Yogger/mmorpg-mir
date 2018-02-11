package com.mmorpg.mir.model.trigger.model;

public interface TriggerContextKey {
	/** 地图实例 */
	public static final String MAP_INSTANCE = "MAP_INSTANCE";
	/** 玩家 */
	public static final String PLAYER = "PLAYER";
	/** 任务 */
	public static final String QUEST = "QUEST";
	/** 怪物组ID */
	public static final String SPAWNGROUPID = "spawnGroupId";
	/** 技能ID */
	public static final String SKILLID = "skillId";
	/** 延迟 */
	public static final String DELAY = "delay";
	/** CRON表达式 */
	public static final String CRON = "cron";
	/** 任务接取Id */
	public static final String QUESTIDS = "questId";
	/** 地图id */
	public static final String MAP_ID = "mapId";
	/** x坐标 */
	public static final String X = "x";
	/** y坐标 */
	public static final String Y = "y";
	/** 奖励id */
	public static final String REWARDID = "rewardId";
	/** 奖励 */
	public static final String REWARD = "reward";
	/** NPC */
	public static final String NPC = "npc";
	/** 副本ID */
	public static final String COPYID = "copyId";
	/** 数量 */
	public static final String NUM = "num";
	/** 另一个玩家 */
	public static final String OTHER_PLAYER = "OTHER_PLAYER";
	/** 另一个玩家 */
	public static final String RELATED_MILITARY = "related_military";
	/** 消失时间 */
	public static final String DESPAWN = "despawn";
	/** 是否做地图通报 */
	public static final String BROADCAST_MAP = "BROADCAST_MAP";
	/** 地图NPC */
	public static final String STATUS_NPC = "STATUS_NPC";
	/** 状态NPC的值 */
	public static final String STATUS_NPC_VALUE = "status";
	/** 状态NPC的id */
	public static final String STATUS_NPC_ID = "statusNpcId";
	/** 郵件的標題I18n */
	public static final String MAIL_TITLE_ID = "mailTitleI18n";
	/** 郵件的內容I18n */
	public static final String MAIL_CONTENT_ID = "mailContentI18n";
	/** 郵件獎勵chooserGroup */
	public static final String MAIL_REWARD_CHOOSERGROUP_ID = "mailRewardChooserGroup";
	/** 公告id */
	public static final String I18N_RESOURCE_ID = "i18NId";
	/** 频道id */
	public static final String CHANNEL_ID = "channelId";
	/** 物品id */
	public static final String ITEM_ID = "itemId";
	/** 表情id */
	public static final String FACE_ID = "faceId";
	/** 是否显示道具 */
	public static final String SHOW_ITEM = "showItem";
	/** 是否带有附件 */
	public static final String MAIL_REWARD = "MAIL_REWARD";
	/** 命格仓库类型 */
	public static final String LIFE_STORAGE_TYPE = "LIFE_STORAGE_TYPE";
}