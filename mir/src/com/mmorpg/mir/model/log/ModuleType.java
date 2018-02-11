package com.mmorpg.mir.model.log;

public enum ModuleType {
	//
	USEITEM(1, "使用道具"),
	//
	DROPITEM(2, "丢弃道具"),
	//
	SELLITEM(3, "卖出道具"),
	//
	MONSTERDROP(4, "怪物掉落"),
	//
	BUYPACK(5, "购买格子"),
	//
	PICKUP(6, "拾取"),
	//
	SKILL(7, "技能消耗"),
	//
	QUEST(8, "任务发放"),
	//
	HORSE(9, "坐骑"),
	//
	TRANSPORT(10, "传送"),
	//
	GATHER(11, "采集"),
	//
	SHOP(12, "商店"),
	//
	EXCHANGE(13, "交易"),
	//
	BUYBACK(14, "回购"),
	//
	MAIL(15, "邮件"),
	//
	CHAT(16, "聊天"),
	//
	COPY(17, "副本"),
	//
	TRIGGER(18, "触发器"),
	//
	GANG(19, "家族"),
	//
	BUY_LIFE(20, "买活"),
	//
	COUNTRY(21, "国家"),
	//
	MILITARY(22, "军衔"),
	//
	EXPRESS(23, "运镖"),
	//
	TEMPLE(25, "太庙"),
	//
	INVESTIGATE(26, "刺探"),
	//
	SOUL(27, "英魂"),
	//
	ARTIFACT(28, "神兵"),
	//
	RESCUE(29, "营救"),
	//
	SIGN(30, "签到"),
	//
	OFFLINE(31, "离线"),
	//
	KINGOFKING(32, "皇帝"),
	//
	GANGOFWAR(33, "家族战"),
	//
	ACTIVEVALUE(34, "活跃值"),
	//
	ONLINEREWARD(35, "在线奖励"),
	//
	CLAWBACK(36, "收益追回"),
	//
	EXERCISE(37, "操练"),
	//
	HERO_RANK(38, "排行榜"),
	//
	COMBINING_ITEM(39, "合成"),
	//
	VIP(40, "VIP"),
	//
	ONE_OFF(41, "一次性奖励"),
	//
	MILITARY_STAR(42, "大将军王的星"),
	//
	COMBAT_SPIRIT(43, "战魂装备消耗"),
	//
	FETE(44, "国家祭祀"),
	//
	HIDDEN_MISSION(45, "隐藏任务"),
	//
	BOSS_GIFT(46, "BOSS红包"),
	//
	EXTENDS(47, "装备强化转移"),
	//
	ENHANCE(48, "强化"),
	//
	FORGE(49, "锻造"),
	//
	SMELT(50, "熔炼"),
	//
	RESET(51, "五行重置"),
	//
	PROMOTION(52, "转职"),
	//
	BOSS_FB(53, "BOSS首杀"),
	//
	SEVENDAYREWARD(54, "7天在线奖励"),
	//
	WARSHIP(55, "膜拜皇帝"),
	//
	VIP_FIRST_PAY(56, "首充"),
	//
	KILL_PEOPLE(57, "击杀玩家"),
	//
	BACKHOME(58, "回城"),
	//
	HORSE_ILLUTION(59, "坐骑幻化"),
	//
	OPERATOR_MOBILE(60, "手机验证"),
	//
	INVEST(61, "投资理财"),
	//
	OPENACTIVE(62, "开服活动"),
	//
	NICKNAME(63, "排行榜称号"),
	//
	OPERATOR_INFORMATION(64, "完善资料"),
	//
	OPERATOR_OPVIP(65, "平台VIP"),
	//
	OPERATOR_WECHAT(66, "微信礼包"),
	//
	EVERYDAY_RECHARGE(70, "每日充值"),
	//
	RESERVEKING(74, "储君"),
	//
	ACHIEVEMENT(75, "成就"),
	//
	OPERATOR_GM(76, "GM"),
	//
	MONSTERRIOT(77, "怪物攻城活动"),
	//
	TREASURE(78, "探宝"),
	//
	OPERATOR_REWARD(79, "平台创建奖励"),
	//
	GIFT360(80, "360游戏大厅礼包"),
	//
	PUBLIC_BETA(81, "公测"),
	//
	BLACKSHOP(82, "黑市"),
	//
	TECH_COPY(85, "墨家傀儡副本"),
	//
	DECOMPOSE_ITEM(87, "分解道具"),
	//
	CREATE_EQUIP_SOUL(88, "装备铸魂"),
	//
	FAMED_GENDERAL_COLLECT(89, "名将武魂收集"),
	//
	COLLECT(90, "收集道具"),
	//
	FLAG_QUEST(91, "国家战事国旗任务"),
	//
	EQUIP_INSET_GEM(92, "装备镶嵌宝石"),
	//
	COPY_RETURN_ACTIONS(93, "名将返还消耗"),
	//
	CELEBRATE(94, "战国庆典"),
	//
	FASHION(95, "时装"),
	//
	CAPTURE_TOWN(96, "城池战"),
	//
	SKILL_RESET(97, "BETA18技能重置"),
	//
	REBATE(98, "删档测试服返利"),
	//
	RECHARGE(99, "充值"),
	//
	CONSOLE(100, "控制台 "),
	//
	MERGE_ACTIVE(101, "和服活动"),
	//
	ATTACH_GOD(102, "合成神装"),
	//
	CHANGE_SUIT(103, "合成套装"),
	//
	GAS_COPY(104, "西周王陵"),
	//
	COMMON_ACTIVE(105, "公共活动"),
	//
	EQUIPMENT_SOUL_UPGRADE(106, "灵魂装备升星"),
	//
	WEEK_CRI(107, "暴击活动"),
	//
	PET_ITEM(108, "宠物"),
	//
	BOSS_COINS(109, "BOSS积分"),
	//
	INVESTAGATE(110, "聚宝盆"),

	ACTIVITY_RECOLLECT(111, "追回活动"),

	BEAUTY(112, "美人"),

	WARBOOK(113, "兵书"),

	COMBINING_EX(114, "护腕合成"),

	LIFEGRID(115, "命格"),

	SUICIDE(116, "转生"),

	EQUIP_TURN(117, "装备转生"),
	// PS：加新的要告诉策划。
	ASSASSINATION(131, "荆轲刺秦"),
	
	POINT_GENERAL(160, "点将台"),
	
	SEAL(161, "印玺"),

	// 200-300留给跨服战
	BOSS_CENTER(200, "跨服BOSS战"), ;

	private final int id;
	private final String name;

	private ModuleType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
