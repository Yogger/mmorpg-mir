package com.mmorpg.mir.model.welfare.model;

public enum ActiveEnum {

	/** 微端登录 */
	// ACTIVE_WEI_LOGIN(1),

	/** ! 今日签到，补签不算完成该事件 */
	// ACTIVE_SIGN(2),

	/** 成为VIP后，每日登陆自动完成 */
	// ACTIVE_VIP(3),

	/** ! 击杀国家BOSS：参与击杀一次国家BOSS */
	ACTIVE_BOSS_COUNTRY(4),

	/** ! 击杀鳄鱼或豹子*/
	ACTIVE_MONSTER_OUT(5),

	/** ! 每日押镖 */
	ACTIVE_EXPRESS(6),

	/** ! 每日刺探 */
	ACTIVE_INVESTIGATE(7),

	/** ! 每日营救 */
	ACTIVE_RESCUE(8),

	/** ! 每日太庙 */
	ACTIVE_TEMPLE(9),

	/** ! 血战边疆 */
	// ACTIVE_COPY(10),

	/** ! 日常任务：完成每日日常任务 */
	// ACTIVE_DAILYTASK(11),

	/** ! 强化装备：完成每日强化装备次数（不计算强化成功，只计算点击强化次数） */
	// ACTIVE_ENHANCE(12),

	/** ! 国家兑换：国家商店兑换购买物品 */
	// ACTIVE_COUNTRY_BUY(13),

	/** 消费礼金：消耗礼金购买物品 */
	// ACTIVE_CONSUMPTION_GIFT(14),

	/** 消费元宝：消费元宝购买物品 */
	// ACTIVE_CONSUMPTION_GOLD(15),

	/** ! 祭剑：完成每日祭剑次数 */
	// ACTIVE_SMELT(16),

	/** ! 国家祭祀 */
	// ACTIVE_COUNTRY_SACRIFICE(17),

	/** ! 升级战魂 */
	// ACTIVE_UPGRADE_COMBATSPIRIT(18),

	/** ! 膜拜皇帝 */
	// ACTIVE_WARSHIP(19),

	/** ! 操练 */
	// ACTIVE_EXERCISE(20),

	/** ! 野怪 */
	// ACTIVE_MONSTER_HUNT(21),

	/** ! 游历任务 */
	// ACTIVE_QUEST_RANDOM(22),

	/** ! 击杀敌国玩家 */
	ACTIVE_KILL_PLAYER(23),

	/** ! 击杀大臣 */
	ACTIVE_KILL_DISPLOMACY(24),

	/** !击杀国旗 */
	// ACTIVE_KILL_FLAG(25),

	/** ！当天充值 */
	ACTIVE_DAY_RECHARGE(26),

	/** 国旗任务 */
	ACTIVE_COUNTRY_FLAG_QUEST(27),

	/** 国家副本 */
	ACTIVE_COUNTRY_COPY(28), ;

	private final int eventId;

	private ActiveEnum(int eventId) {
		this.eventId = eventId;
	}

	public int getEventId() {
		return eventId;
	}

	public static ActiveEnum valueOf(int eventId) {
		for (ActiveEnum e : values()) {
			if (e.eventId == eventId) {
				return e;
			}
		}
		throw new RuntimeException("找不到事件[" + eventId + "]");
	}

}
