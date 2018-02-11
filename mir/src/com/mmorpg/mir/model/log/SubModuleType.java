package com.mmorpg.mir.model.log;

public enum SubModuleType {
	PRIVATE_BOSS(1, "个人BOSS掉落"),

	ACTIVE_VALUE_REWARD(2, "活跃度领奖"),

	COPY_QUEST(3, "剧情副本掉落"),

	UPGRADE_MEDAL(4, "升级勋章"),

	KILL_ENEMY(5, "击杀敌国玩家"),

	LADDER_RESET(6, "爬塔副本重置"),

	LADDER_REWARD(7, "爬塔副本首通奖励"),

	LADDER_BATCH_ACT(8, "爬塔副本扫荡消耗"),

	LADDER_BATCH_REWARD(9, "爬塔副本扫荡奖励"),

	COPY_ENTER_ACT(10, "副本进入消耗"),

	COPY_ENCOURAGE_ACT(11, "副本鼓舞消耗"),

	COPY_BUY_COUNT(12, "购买副本次数"),

	PRIVATE_BOSS_COPY(13, "个人BOSS副本首通奖励"),

	COUNTRY_SALARY(14, "领取国民福利"),

	COUNTRY_OFFICIAL_SALARY(15, "领取官员俸禄"),

	COUNTRY_CONTRIBUTE(16, "捐献"),

	CONTRIBUTE_KILLITEM(17, "捐献杀人令"),

	COUNTRYSHOP_BUY(18, "国家商店购买"),

	COUNTRY_TANK_CREATE(19, "制造战车"),

	MOBILIZATION(20, "全民动员"),

	BOSS_FIRST(21, "BOSS首杀"),

	EXERCISE(22, "操练"),

	EXPRESS_ACT(23, "运镖押金"),

	CREATE_GANG(24, "创建帮派"),

	GANG_HELP_ACT(25, "家族救援"),

	GANG_ACTIVITY_EXP(26, "家族战经验BUFF"),

	ONE_OFF_GIFT(27, "领取一次性礼包"),

	HORSE_ILLUTION_ACT(28, "坐骑幻化消耗"),

	HORSE_ILLUTION_REWARD(29, "坐骑幻化奖励"),

	INVESTIGATE_REFRESH_ACT(32, "刺探一键刷橙"),

	DROP_ITEM(33, "丢弃物品"),

	BUYBACK(34, "回购物品"),

	ADD_PACKSIZE(35, "增加背包大小"),

	ENHANCE_EQUIP(36, "强化装备"),

	FORGE_EQUIP(37, "锻造装备"),

	ENHANCE_LELVE_TRANSFER(38, "强化转移"),

	COMBINING_ACT(39, "合成消耗"),

	COMBINING_REWARD(40, "合成奖励"),

	MILITARY_STAR_DAILY_ACT(41, "大将军王星级每日消耗"),

	KILL_NPC_REWARD(42, "打怪获得的奖励"),

	ONLINE_REWARD(43, "在线奖励"),

	DAILY_QUEST_ACT(44, "任务一键完成所有日常消耗"),

	QUEST_GOLD_COMPLETE(45, "一键完成任务"),

	QUEST_STAR_UPGRADE(46, "任务升星"),

	TRIGGER_REWARD(47, "触发的奖励"),

	SHOP_BUY(48, "商店购买"),

	FILL_SIGN(49, "补签"),

	SKILL_ACT(50, "技能消耗"),

	SKILL_LEARN(51, "学习被动技能"),

	BRICK_ACT(52, "砖块刷橙消耗"),

	BRICK_REWARD(53, "放砖的奖励"),

	FLY_SHOE_ACT(54, "飞鞋消耗"),

	CHAT_FLY_SHOE_ACT(55, "聊天动态消耗"),

	FIRST_PAY(56, "首充奖励"),

	SEVEN_LOGIN(57, "七天登录"),

	NICKNAME_COMBAT(58, "战力排行榜第一"),

	OPENACTIVE_EXP_ACT(59, "经验放送消耗"),

	OPENACTIVE_EXP(60, "经验放送"),

	OPENACTIVE_ORANGE_ACT(61, "领取橙色灵魂礼包消耗"),
	//
	OPENACTIVE_ORANGE(62, "橙色灵魂礼包"),

	OPENACTIVE_ENHANCE_ACT(63, "全民强化消耗"),
	//
	OPENACTIVE_ENHANCE(64, "全民强化"),
	//
	OPENACTIVE_HORSE(65, "坐骑竞技"),

	OPENACTIVE_LEVEL_ACT(66, "等级竞技消耗"),

	OPENACTIVE_CONSUME(67, "消费元宝活动奖励"),

	DAILYCHARGE_REWARD(68, "每日充值"),

	OPENACTIVE_CONSUME_ACT(69, "消费元宝活动消耗"),

	OPENACTIVE_LEVEL(70, "等级竞技"),

	OPENACTIVE_MILITARY(71, "全民军衔"),

	RECIEVED_BOSSGIFT(72, "BOSS红包"),

	CLAWBACK_ACT(73, "福利大厅找回的消耗"),

	EXCAHNGE_ACT(74, "交易的物品"),

	RESET_EQUIP_ELEMENT(75, "装备转五行"),

	REFRESH_EXPRESS_ACT(76, "重置镖车"),

	RELIVE_ACT(77, "买活消耗"),

	EXPRESS_REWARD(78, "运镖完成的奖励"),

	COPY_REWARD(79, "副本通关奖励"),

	QUEST_REWARD(80, "任务奖励"),

	OFFLINE_EXP_REWARD(81, "离线经验领取奖励"),

	OFFLINE_EXP_ACT(82, "离线经验领取消耗"),

	DAILY_QUEST_REWARD(83, "日常任务奖励"),

	CUT_COUNTRYFLAG(84, "砍国旗奖励"),

	CUT_DIPLOMACY(85, "砍大臣奖励"),

	YESTERDAY_HERO_REWARD(86, "昨日英雄排行榜领奖"),

	COUNTRY_WAR_REWARD(87, "国家战事任务奖励"),

	COMPLETE_INFO(88, "完善信息领奖"),

	MAIL_REWARD(89, "邮件领奖"),

	MOBILE_REWARD(90, "手机认证"),

	OPERATOR_VIP_REWARD(91, "平台VIP等级礼包"),

	RESCUE_REWARD(92, "营救奖励"),

	CLAW_BACK(93, "资源找回"),

	VIP_LEVEL_REWARD(94, "VIP等级奖励"),

	VIP_WEEK_REWARD(95, "VIP周奖励"),

	ROB_LORRY_FLY(96, "劫镖飞行的消耗"),

	SACRIFICE_REWARD(97, "祭祀的奖励"),

	SACRIFICE_ACT(98, "祭祀的消耗"),

	SACRIFICE_GOLD_ACT(99, "元宝祭祀的消耗"),

	SELL_ITEM(100, "卖东西"),

	CHAT_ACT(101, "聊天消耗"),

	SIGN_VIP_REWARD(102, "VIP签到额外奖励"),

	SIGN_REWARD(103, "签到累计天数奖励"),

	SKILL_LEVEL_UP(104, "技能升级"),

	SMELT_EQUIP_REWARD(105, "熔炼奖励"),

	SMELT_EQUIP_ACT(106, "熔炼消耗"),

	SUPER_FORGE_REWARD(107, "至尊锻造奖励"),

	SUPER_FORGE_ACT(108, "至尊锻造消耗"),

	TREASURE_UPGRADE(109, "升级宝物"),

	COUNTRY_SHOP_UPGRADE(110, "升级商店"),

	COUNTRY_DOOR_UPGRADE(111, "升级城门"),

	COUNTRY_FACTORY_UPGRADE(112, "升级兵工厂"),

	COUNTRY_FLAG_UPGRADE(113, "升级国旗"),

	HORSE_UPGRADE(114, "坐骑升阶"),

	MILITARY_KILL(115, "军衔杀人获得荣誉"),

	MILITARY_UPGRADE_STRATEGY(116, "军衔兵法升星"),

	CONSOLE_ADDMONEY(117, "控制台加钱"),

	CONSOLE_ADDREWARD(118, "控制台加奖励"),

	CONSOLE_ADDEXP(119, "控制台加经验"),
	//
	CONSOLE_ADDITEM(120, "控制台加道具"),

	CONSOLE_UPGRADE_ARTIFACT(121, "控制台升级神兵"),

	CONSOLE_UPGRADE_SOUL(122, "控制台升级英魂"),

	CONSOLE_UPGRADE_HORSE(123, "控制台升级坐骑"),

	COUNTRY_TANK_UPGRADE(124, "升级战车"),

	ARTIFACT_UPGRADE(125, "神兵升级"),

	SOUL_UPGRADE(126, "升级英魂"),

	USE_ITEM(127, "使用道具"),

	WARSHIP_EXP_REWARD(128, "膜拜经验区奖励"),

	WARSHIP_REWARD(129, "膜拜奖励"),

	WARSHIP_ACT(130, "膜拜皇帝消耗"),

	WARSHIP_REFRESH(131, "刷奖励"),

	WARSHIP_GOLD_REFRESH(132, "一键刷橙色"),

	WECHAT(133, "微信领奖"),

	LEVEL_RANK_FIRST(134, "等级排行榜第一"),

	PICKUP_DROP(135, "拾取掉落物品"),

	GATHER_OBJ(136, "采集物品"),

	BACK_HOME_ACT(137, "回城消耗"),

	RESERVEKING_TASK_REWARD(138, "储君任务奖励"),

	INVESTIGATE_REWARD(139, "刺探奖励"),

	INVEST_EXP_REWARD(140, "经验投资领奖"),

	INVEST_COPPER_REWARD(141, "铜钱投资领奖"),

	INVEST_RELIVEDAN_REWARD(142, "复活丹投资领奖"),

	INVEST_HONOR_REWARD(143, "荣誉投资领奖"),

	INVEST_GIFT_REWARD(144, "礼金投资领奖"),

	INVEST_EXP_BUY(145, "经验投资购买"),

	INVEST_COPPER_BUY(146, "铜钱投资购买"),

	INVEST_RELIVEDAN_BUY(147, "复活丹投资购买"),

	INVEST_HONOR_BUY(148, "荣誉投资购买"),

	INVEST_GIFT_BUY(149, "礼金投资购买"),

	ACHIEVEMENT_UPGRADE(150, "成就升级"),

	ACHIEVEMENT_REWARD(151, "成就领奖"),

	ARTIFACT_BUFF_BUY(152, "神兵buff购买"),

	ARTIFACT_BUFF_REWARD(153, "神兵buff奖励"),

	OPENACTIVE_ARTIFACT(154, "开服活动神兵进阶"),

	OPENACTIVE_COMPETE(155, "开服竞技活动"),

	OPERATOR_GM_REARD(156, "成为GM奖励"),

	MONSTERRIOT_REWARD(157, "怪物攻城排名奖励"),

	TREASURE_COMMON_SEEK_ACT_ONE(158, "第一档普通探宝消耗"),

	TREASURE_COMMON_SEEK_ACT_SECOND(159, "第二档普通探宝消耗"),

	TREASURE_COMMON_SEEK_ACT_THIRD(160, "第三档普通探宝消耗"),

	TREASURE_HIGH_SEEK_ACT_ONE(161, "第一档高级探宝消耗"),

	TREASURE_HIGH_SEEK_ACT_SECOND(162, "第二档高级探宝消耗"),

	TREASURE_HIGH_SEEK_ACT_THIRD(163, "第三档高级探宝消耗"),

	TREASURE_COMMON_SEEK_REWARD_ONE(164, "第一档普通探宝奖励"),

	TREASURE_COMMON_SEEK_REWARD_SECOND(165, "第二档普通探宝奖励"),

	TREASURE_COMMON_SEEK_REWARD_THIRD(166, "第三档普通探宝奖励"),

	TREASURE_HIGH_SEEK_REWARD_ONE(167, "第一档高级探宝奖励"),

	TREASURE_HIGH_SEEK_REWARD_SECOND(168, "第二档高级探宝奖励"),

	TREASURE_HIGH_SEEK_REWARD_THIRD(169, "第三档高级探宝奖励"),

	COLLECT_EQUIP_REWARD(170, "收集装备奖励"),

	PUBLIC_BETA_GROUP_PURCHASE_REWARD(172, "公测团购奖励"),

	OPENACTIVE_SOUL(173, "开服活动英魂奖励"),

	TECH_COPY_LAST_ATTACK(180, "墨家傀儡最后一击的奖励"),

	CREATE_EQUIPMENT_SOUL(181, "装备铸魂消耗"),

	CREATE_EQUIPMENT_SOUL_REPLACE(182, "装备铸魂替换的奖励"),

	COMBINING_EQUIPMENT_SOUL(183, "武魂合成的消耗"),

	COMBINING_EQUIPMENT_SOUL_REWARD(184, "武魂合成的奖励"),

	DECOMPOSE_ITEM_REWARD(185, "分解物品获得的奖励"),

	INSET_GEM(186, "装备镶嵌宝石消耗"),

	UNSET_GEM(187, "装备取出镶嵌宝石"),

	MINGJIANG_ACTIONS_RETURN(188, "名将副本失败返还进入的消耗"),

	BOSS_COPY_BATCH_ACTIONS(189, "个人BOSS副本扫荡的消耗"),

	BOSS_COPY_BATCH_REWARD(190, "个人BOSS副本扫荡的奖励"),

	CELEBRATE_RECHARGE_REWARD(191, "战国庆典重置领奖"),

	BASE_FEATS_AUTO_REWARD(192, "个人俸禄定时奖励"),

	FEATS_RECIEVED_REWARD(193, "领取城池奖励"),

	TOWN_BUY_COUNT(194, "购买城池行动次数的消耗"),

	TOWN_RESET_CHANLLENGE_CD(195, "重置城池挑战CD的消耗"),

	TOWN_ROB_FEATS(195, "挑战别人城池成功获得的奖励"),

	ATTACH_GOD_STAT_ACTIONS(196, "合成神装的消耗"),

	MERGE_CONSUME_REWARD(197, "合服消费活动竞技"),

	GAS_COPY_INMAP_REWARD(198, "西周王陵地图的奖励"),

	GAS_COPY_INMAP_ACTIONS(199, "进入西周王陵地图的消耗"),

	FLAG_QUEST_ATTEND(200, "国家战事参与奖"),

	FLAG_QUEST_DEFEND(201, "国家战事参与防守奖"),

	COUNTRY_COPY_ENCOURAGE_REWARD(202, "国家副本助威奖励"),

	COUNTRY_COPY_END_REWARD(203, "国家副本奖励"),

	RESERVEKING_BECOME_ACT(204, "成为储君的消耗"),

	RECHARGE_REBATE(205, "删档测试服返利"),

	OPENACTIVE_COLLECTITEM_REWARD(206, "集字活动奖励"),

	OPENACTIVE_COLLECTITEM_ACT(207, "集字活动消耗"),

	ENHANCE_POWER_ACTIVE(208, "强化装备战斗力活动"),

	OPERATOR_REWARD(209, "平台创建奖励"),

	GIFT360(210, "360游戏大厅奖励"),

	PUBLIC_TEST_GIFT_ACT(211, "公测献礼购买消耗"),

	PUBLIC_TEST_REWARD_GRANT(212, "公测献礼奖励发放"),

	COUNTRY_TECHNOLOGY_DONATE_ACT(213, "国家建设令捐献消耗"),

	COUNTRY_TECHNOLOGY_DONATE_REWARD(214, "国家建设令捐献奖励"),

	BLACKSHOP_BUY_ACT(216, "黑市购买消耗"),

	BLACKSHOP_BUY_REWARD(217, "黑市购买奖励"),

	BLACKSHOP_REFRESH_ACT(218, "黑市刷新消耗"),

	GROUPPURCHASE_TWO_REWARD(219, "超值团购2奖励"),

	GROUPPURCHASE_THREE_REWARD(220, "超值团购3奖励"),

	CELEBRATE_FIREWORK_ACT(221, "战国璀璨烟火消耗"),

	FASHION_WEAR_REWARD(222, "时装穿戴奖励"),

	CELEBRATE_FIREWORK_REWARD(223, "战国璀璨烟火奖励"),

	SKILL_RESET(224, "beta18技能重置"),

	LOGIN_GIFT(225, "登陆有礼"),

	CHEAP_GIFT_BAG(226, "特惠礼包"),

	REFER_REWARD(227, "渠道创建奖励"),

	RESCUE_ITEM_ACT(229, "营救令消耗"),

	INVESTIGATE_ITEM_ACT(230, "刺探令消耗"),

	CHANGE_SUIT_ACTIONS(231, "合成套装消耗"),

	COMMON_ACTIVE_LOGIN_GIFT_REWARD(232, "公共登陆有礼"),

	COMMON_ACTIVE_CHEAP_GIFT_BAG_ACT(233, "公共特惠礼包消耗"),

	COMMON_ACTIVE_CHEAP_GIFT_BAG_REWARD(234, "公共特惠礼包奖励"),

	COMMONACTIVITY_RECHARGE_REWARD(235, "通用充值活动奖励"),

	COMMONACTIVITY_CONSUME_REWARD(236, "通用消费活动奖励"),

	COMMONACTIVITY_CELEBRATE_FIREWORK_ACT(237, "通用烟花消耗"),

	COMMONACTIVITY_CELEBRATE_FIREWORK_REWARD(238, "通用烟花奖励"),

	COMMON_ACTIVE_IDENTIFY_TREASURE(239, "鉴宝"),

	WEEK_CRI_ACT(240, "暴击活动消耗"),

	WEEK_CRI_REWARD(241, "暴击活动奖励"),

	COMMONACTIVITY_FIRST_PAY_REWARD(242, "首充"),

	BOSS_COINS_BUY_STATS(243, "BOSS积分购买属性"),

	BOSS_COINS_UPGRADE_LEVEL(244, "BOSS积分升级"),

	BOSS_COINS_ONLINE_GAIN(245, "BOSS积分在线获取"),

	BOSS_COINS_ATTEND_KILL_BOSS(246, "参与击杀BOSS获得BOSS积分"),

	ACTIVITY_RECOLLECT_ACTIONS(247, "追回活动的消耗"),

	ACTIVITY_RECOLLECT_REWARD(248, "追回活动的奖励"),

	EQUIPMENT_SOUL_UPGRADE_ACTIONS(260, "装备灵魂等级升星消耗"),

	PET_ITEM_DEPRECATED(261, "宠物过期了"),

	PET_ITEM_UNEQUIP(262, "宠物脱下"),

	NEWER_PET_INSTEAD(263, "新手宠物续费"),

	MARCOSHOP_REFRESH_ACT(264, "开服巧取商店刷新消耗"),

	MARCOSHOP_BUY_ACT(265, "开服巧取商店购买消耗"),

	MARCOSHOP_BUY_REWARD(266, "开服巧取商店购买奖励"),

	COMMONACTIVITY_TREASUREACTIVE_REWARD(267, "通用活动探宝领奖"),

	COMMONACTIVITY_REDPACK_REWARD(268, "通用红包抢不停领奖"),

	COMMON_ACTIVE_COLLECT_WORD(269, "通用集字活动"),

	MINGJIANG_BOSS_COPY_ACTIONS(270, "名将自动扫荡消耗"),

	MINGJIANG_BOSS_COPY_REWARD(271, "名字自动扫荡奖励"),

	BEAUTY_LINGER_ACT(272, "美人缠绵消耗"),

	BEAUTY_ACTIVE_ACT(273, "美人激活消耗"),

	BROWSER_2345_REWARD(274, "2345浏览器"),

	WARBOOK_UPGRADE_ACT(275, "兵书升级消耗"),

	COMBING_EX_REWARD(277, "护腕奖励"),

	LUCKY_REWARD(278, "幸运抽奖"),

	WAR_BOOK_DOUBLE_ACTION(279, "兵书副本双倍奖励消耗"),

	WAR_BOOK_DOUBLE_REWARD(280, "兵书副本双倍奖励"),

	GOLD_TREASURY_ACITION(281, "黄金宝库消耗"),

	GOLD_TREASURY_REWARD(282, "黄金宝库"),

	WARBOOK_COPY_RESET(283, "兵书副本重置"),

	HORSEEQUIP_COPY_ACTIONS(284, "坐骑副本扫荡消耗"),

	ENHANCE_HORSE_EQUIP(285, "坐骑装备强化"),

	ENHANCE_HORSE_LELVE_TRANSFER(286, "坐骑装备强化转移"),

	COPY_BOSS_PICKUP(287, "副本boss掉落"),

	COMMONACTIVITY_CONSUME_GIFT_REWARD(288, "消费献礼"),

	MILITARY_BREAK_COIN(289, "军衔突破铜币"),

	MILITARY_BREAK_GOLD(290, "军衔突破元宝"),

	GOLD_TREASURY_GOLD_ACITION_1(291, "黄金宝库元宝消耗第一组"),

	GOLD_TREASURY_GOLD_ACITION_2(292, "黄金宝库元宝消耗第二组"),

	GOLD_TREASURY_GOLD_ACITION_3(293, "黄金宝库元宝消耗第三组"),

	GOLD_TREASURY_GOLD_ACITION_4(294, "黄金宝库元宝消耗第四组"),

	GOLD_TREASURY_ACITION_1(295, "黄金宝库锤子消耗第一组"),

	GOLD_TREASURY_ACITION_2(296, "黄金宝库锤子消耗第二组"),

	GOLD_TREASURY_ACITION_3(297, "黄金宝库锤子消耗第三组"),

	GOLD_TREASURY_REWARD_4(298, "黄金宝库锤子消耗第四组"),

	GOLD_TREASURY_REWARD_1(299, "黄金宝库奖励第一组"),

	GOLD_TREASURY_REWARD_2(300, "黄金宝库奖励第二组"),

	GOLD_TREASURY_REWARD_3(301, "黄金宝库奖励第三组"),

	GOLD_TREASURY_ACITION_4(302, "黄金宝库奖励第四组"),

	LIFEGRID_CONVERT_REWARD(303, "命格兑换奖励"),

	ASSASSINATION_ROLL(304, "荆轲刺秦随机奖励"),

	SUICIDE_COMMON_CHARGE_ACTION(305, "转生普通充元消耗"),

	SUICIDE_QUICK_CHARGE_ACTION(306, "转生一键充元消耗"),

	DROP_LIFEGRID_ITEM(307, "丢弃命格道具"),

	DEVOUR_LIFEGRID_ITEM(308, "吞噬命格"),

	DEVOUR_ALL_LIFEGRID_ITEM(309, "一键吞噬命格"),

	LIFEGRID_TAKE(310, "提取"),

	LIFEGRID_TAKE_ALL(311, "一键提取"),

	LIFEGRID_EQUIP(312, "命格装备"),

	LIFEGRID_UNEQUIP(313, "命格脱下"),

	LIFEGRID_CONVERT_ACTION(314, "命格碎片兑换"),

	LIFEGRID_EXCHANGE(315, "命格交换位置"),

	EQUIP_TURN_ACTIONS(316, "装备转生消耗"),
	
	POINT_GENERAL_REPLACE(360, "点将台替换消耗"),
	
	POINT_GENERAL_REWARD(361, "点将台领奖"),
	
	POINT_GENERAL_BUY(362, "点将台购买消耗"),
	
	STAMP_COPY_RESET(363, "印玺副本重置"),
	
	SEAL_UPGRADE_ACT(364, "印玺升级消耗"),

	WARBOOK_COPY_RESET_ACT(317, "兵书副本扫荡消耗"),

	WARBOOK_COPU_RESET_REWARD(318, "兵书副本扫荡奖励"),

	// 500-600跨服用
	BOSS_CENTER(500, "跨服BOSS战"),

	BOSS_CENTER_PICKUP(501, "跨服BOSS战地上拾取"),

	BOSS_CENTER_RANK(502, "跨服BOSS排名奖励");

	private final int id;

	private final String data;

	private SubModuleType(int id, String d) {
		this.id = id;
		this.data = d;
	}

	public int getId() {
		return id;
	}

	public String getData() {
		return data;
	}
}
