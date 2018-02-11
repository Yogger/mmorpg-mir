package com.mmorpg.mir.model.common.exception;

/**
 * 所有的异常类型，请写在这里
 * 
 * @author zhou.liu
 * 
 */
public interface ManagedErrorCode {

	public static final int SYS_ERROR = Short.MIN_VALUE;
	/** 非法请求，所有不和情理的请求，都返回这个错误吗 **/
	public static final int ERROR_MSG = -999;

	/** 没有足够的元宝 */
	public static final int NOT_ENOUGH_GOLD = -1;
	/** 没有足够的铜币 */
	public static final int NOT_ENOUGH_COPPER = -2;
	/** 没有足够的礼券 */
	public static final int NOT_ENOUGH_GIFT = -3;
	/** 该账户已经存在角色 */
	public static final int HAVE_ROLE = -4;
	/** 角色名字重复 */
	public static final int NAME_REPEAT = -5;
	/** 包含非法字符 */
	public static final int LAWLESS_CHAR = -6;
	/** 验证失败 */
	public static final int VALIDATE_FAILURE = -7;
	/** 没有角色 */
	public static final int NO_ROLE = -8;
	/** 没有足够的内币 */
	public static final int NOT_ENOUGH_INNER = -9;
	/** 订单重复 */
	public static final int RECHARGE_ORDER_REPEAT = -10;
	/** 登录sign过期 */
	public static final int SIGN_DEPRECATED = -11;
	/** 禁止选择国家 */
	public static final int FORBID_COUNTRY = -12;
	/** 没有找到玩家 */
	public static final int NOT_FOUND_PLAYER = -13;
	/** 重连失败 */
	public static final int RELOGIN_FAIL = -21;
	/** 体力满 */
	public static final int PHYSICAL_FULL = -22;
	/** 耐力满 */
	public static final int ENDURANCE_FULL = -23;
	/** 没有足够的购买次数 */
	public static final int NOT_ENOUGH_BUYCOUNT = -24;
	/** 气血满 */
	public static final int HP_FULL = -25;
	/** 名字长度非法 */
	public static final int OUT_OF_MAX_LENGTH = -26;
	/** vip等级不够 */
	public static final int NOT_ENOUGH_VIP = -27;
	/** 禁止登陆 */
	public static final int BAN_STATUS = -28;

	/** 道具数量不足 */
	public static final int ITEM_COUNT = -29;

	/** 非法参数 */
	public static final int INVALIDPAR = -30;

	/** 已经加入了阵营 */
	public static final int JOINCAMP = -31;

	/** 没有开启 */
	public static final int NOT_OPEN_RETRIEVE = -32;

	/** 没有知道最高等级玩家信息 */
	public static final int NOT_FOUND_MAXLEVEL = -33;

	/** 不需要使用找回经验 */
	public static final int NOT_USE_RETRIEVE_EXP = -34;

	/** 找回经验还在冷却中 */
	public static final int RETRIEVE_EXP_IS_COOL = -35;

	/** 非装备道具 **/
	public static final int NOT_EQUIPMENT = -36;

	/** 背包已满 **/
	public static final int PACK_FULL = -37;
	/** 仓库没有足够的空位 **/
	public static final int WARE_NOT_ENOUGH = -38;
	/** 背包空间不足 **/
	public static final int PACK_NOT_ENOUGH = -39;
	/** 道具不可使用 **/
	public static final int NOT_USEABLE_ITEM = -40;
	/** 血量不足 **/
	public static final int NOT_ENOUGH_HP = -41;
	/** 道具不足 **/
	public static final int NOT_ENOUGH_ITEM = -42;
	/** 道具冷却之中 **/
	public static final int ITEM_IN_COOLDOWN = -43;
	/** 角色已经死亡 **/
	public static final int DEAD_ERROR = -44;
	/** 没有在指定位置 */
	public static final int NOT_RIGHT_POSITION = -45;
	/** 掉落物品不属于你 */
	public static final int DROP_NOT_OWNERSHIP = -46;
	/** 掉落物品不存在 */
	public static final int DROP_NOT_FOUND = -47;
	/** 等级不足 */
	public static final int LEVEL_NOT_ENOUGH = -48;
	/** 时间不在中间 */
	public static final int TIME_NOT_BETWEEN = -49;
	/** 没有足够的蓝 */
	public static final int NOT_ENOUGH_MP = -50;
	/** 已经包含任务 */
	public static final int HAVE_QUEST = -51;
	/** 任务未完成 */
	public static final int QUEST_INCOMPLETE = -52;
	/** 任务未知找到 */
	public static final int QUEST_NOT_FOUND = -53;
	/** 坐骑最大等级 */
	public static final int HORE_MAX_LEVEL = -54;
	/** 没有足够的坐骑阶段 */
	public static final int NOT_ENOUGH_HORSEGRADE = -55;
	/** 族长不能退出 */
	public static final int GANG_MASTER_NOT_QUIT = -56;
	/** 族名字重复 */
	public static final int GANG_NAME_REPEAT = -57;
	/** 超过个人最大申请个数 */
	public static final int GANG_APPLY_MAXSIZE = -58;
	/** 没有加入任何帮会 */
	public static final int GANG_NOT_JOIN = -59;
	/** 帮会没有权限 */
	public static final int GANG_NOT_RIGHT = -60;
	/** 没有找到申请 */
	public static final int GANG_NOT_APPLY = -61;
	/** 帮会超过最大人数 */
	public static final int GANG_MAX_SIZE = -62;
	/** 已经加入帮会 */
	public static final int GANG_JOIN = -63;
	/** 已经邀请 */
	public static final int GANG_INVITED = -64;
	/** 未邀请 */
	public static final int GANG_NOT_INVITED = -65;
	/** 队伍满 */
	public static final int FULL_GROUP = -66;
	/** 你不是队长 */
	public static final int ONLY_GROUP_LEADER_CAN_INVITE = -67;
	/** 邀请的玩家已经下线 */
	public static final int INVITED_PLAYER_OFFLINE = -68;
	/** 选择的目标已经死亡 */
	public static final int SELECTED_TARGET_DEAD = -69;
	/** 你已经死亡 */
	public static final int CANNOT_INVITE_BECAUSE_YOU_DEAD = -70;
	/** 玩家已经在其他的队伍 */
	public static final int PLAYER_IN_ANOTHER_GROUP = -71;
	/** 玩家不再队伍中 */
	public static final int PLAYER_NOT_IN_GROUP = -72;
	/** 玩家没有申请加入 */
	public static final int PLAYER_NOT_APPLY = -73;
	/** 技能不再奖励中 */
	public static final int SKILL_NOT_INREWARD = -74;
	/** 技能等级不够 */
	public static final int SKILL_NOT_LEVEL = -75;
	/** 没有找到该技能 */
	public static final int SKILL_NOT_FOUND = -76;
	/** 真气不足 */
	public static final int NOT_ENOUGH_QI = -77;
	/** 没有找到道具 */
	public static final int NOT_FOUND_ITEM = -78;
	/** 玩家不在线了 */
	public static final int PLAYER_INLINE = -79;
	/** 玩家不再指定位置 */
	public static final int PLAYER_NOT_IN_POSITION = -80;
	/** 背包格子数不足清空再领取 */
	public static final int PACK_GRID_NOT_ENOUGH = -81;
	/** 名字中间包含空格 */
	public static final int NAME_CONTAINS_BLACK = -99;
	/** 对方正在交易中 */
	public static final int TARGET_TRADING_ERROR = -100;
	/** 交易之中 */
	public static final int IN_TRADING_ERROR = -101;
	/** 交易已经锁定 */
	public static final int IN_TRADING_LOCK_ERROR = -102;
	/** 道具超过交易上线 */
	public static final int IN_TRADING_IS_FULL = -103;
	/** 道具已经存在 */
	public static final int IN_TRADING_EXIST_ERROR = -104;
	/** 交易还未锁定 */
	public static final int IN_TRADING_NOT_LOCK_ERROR = -105;
	/** 不能跟自己交易 */
	public static final int IN_TRADING_SELF_ERROR = -106;
	/** 交易已经确定 */
	public static final int IN_TRADING_CONFRIMD_ERROR = -107;
	/** 没有足够的DP */
	public static final int NOT_ENOUGH_DP = -108;
	/** 没有找到技能 */
	public static final int NOT_FOUND_SKILLID = -109;
	/** 没有足够的贡献 */
	public static final int NOT_ENOUGH_CONTRIBUTION = -110;
	/** 没有足够的副本次数 */
	public static final int NOT_ENOUGH_COPY = -111;
	/** 副本进入CD */
	public static final int COPY_ENTER_CD = -112;
	/** 玩家被沉默 */
	public static final int PLAYER_SILENCE = -113;
	/** 技能CD */
	public static final int SKILL_CD = -114;
	/** 玩家被眩晕 */
	public static final int PLAYER_STUN = -115;
	/** 玩家在副本中 */
	public static final int PLAYER_IN_COPY = -116;
	/** 没有足够的购买次数 */
	public static final int COPY_BUY_COUNT = -117;
	/** 国家权限不足 */
	public static final int COUNTRY_AUTHORITY_ERROR = -118;
	/** 不是国家官员 */
	public static final int COUNTRY_NOT_OFFICAL = -119;
	/** 商店道具数量不足 */
	public static final int COUNTRY_SHOP_ENOUGH = -120;
	/** 仓库没有足够的空位 **/
	public static final int STORGE_NOT_ENOUGH = -121;
	/** 城门满级 */
	public static final int DOOR_MAX_LEVEL = -122;
	/** 工厂满级 */
	public static final int FACTORY_MAX_LEVEL = -123;
	/** 国家资源不足 */
	public static final int NOT_ENOUGH_COUNTRY_MOENY = -124;
	/** 卫队数量满 */
	public static final int COUNTRY_GURAD_MAX = -125;
	/** 玩家已经是卫队成员 */
	public static final int PLAYER_GUARDED = -126;
	/** 国旗满级 */
	public static final int FLAG_MAX_LEVEL = -127;
	/** 兵工厂满 */
	public static final int FACTORY_FULL = -128;
	/** 没有发现战车 */
	public static final int NOT_FOUND_TANK = -129;
	/** 军工厂等级不足 */
	public static final int COUNTRY_FACTORY_LEVEL = -130;
	/** 副本没有完成 */
	public static final int COPY_NOT_COMPLETE = -131;
	/** 国家官员 */
	public static final int COUNTRY_OFFICAL = -132;
	/** 爬塔副本通光奖励已经领取 */
	public static final int COPY_LADDER_REWARDED = -133;
	/** 爬塔副本没有完成 */
	public static final int COPY_LADDER_NOT_COMPLETED = -134;
	/** 权限使次数不足 */
	public static final int COUNTRY_TODAY_NOT_AUTHORITY = -135;
	/** 没有足够重置次数 */
	public static final int NOT_ENOUGH_COPY_RESET = -136;
	/** 召集国民CD */
	public static final int CALLTOGETHER_CD = -137;
	/** 召集国民次数 */
	public static final int CALLTOGETHER_COUNT = -138;
	/** 国家禁言 */
	public static final int COUNTRY_FORBIDCHAT = -139;
	/** 已经禁言 */
	public static final int COUNTRY_FORBIDCHATED = -140;
	/** 已经标记为内奸 */
	public static final int COUNTRY_TRAITORED = -141;
	/** 已经发放 */
	public static final int COUNTRY_SALARYED = -142;
	/** 已经领取 */
	public static final int COUNTRY_RECEVIED = -143;
	/** 已经拥有战车 */
	public static final int HAD_TANK = -144;
	/** 没有战车 */
	public static final int HAD_NOT_TANK = -145;
	/** 已经拥有虎符 */
	public static final int HAD_TOKEN = -146;
	/** 没有薪水记录 */
	public static final int NO_SALARY_RECORD = -147;
	/** 没有虎符 */
	public static final int NO_TOGETHER_TOKEN = -148;
	/** 不能移动 */
	public static final int CANNOT_MOVE = -149;
	/** 没有福利 */
	public static final int COUNTRY_NO_SALARYED = -150;
	/** 已经在运镖中 */
	public static final int HAD_EXPRESS = -151;
	/** 没有发现镖车 */
	public static final int NOT_FOUND_LORRY = -152;
	/** 没有发现运镖奖励 */
	public static final int NOT_FOUND_EXPRESS_REWARD = -153;
	/** 状态NPC状态切换的CD未到 */
	public static final int STATUS_NPC_NO_DURATION = -154;
	/** 状态NPC状态已经变化 */
	public static final int STATUS_NPC_CHANGE = -155;
	/** 状态NPC状态错误 */
	public static final int STATUS_NPC_ERROR = -156;
	/** 包含了无敌镖车 */
	public static final int HAS_GOD_LORRY = -157;
	/** 板砖CD中 */
	public static final int CHANGE_BRICK_CD = -158;
	/** 调查中 */
	public static final int INVESTIGEING = -159;
	/** 刺探最大次数 */
	public static final int INVESTIGE_MAX_COUNT = -160;
	/** 筛选出过多的NPC */
	public static final int INVESTIGE_SELECT_NPC_MORE = -161;
	/** 没有发现刺探NPC */
	public static final int INVESTIGE_NOT_NPC = -162;
	/** 超过最大板砖任务接取次数 */
	public static final int COUNTRY_TEMPLE_MAX_QUEST = -163;
	/** 换情报CD */
	public static final int INVESTIGE_CHANGE_CD = -164;

	/** 该玩家禁止邀请加入帮会 */
	public static final int GANG_FORBIT_INVITE = -165;

	/** 福利已被前任官员领取，无法再领取 */
	public static final int OFFICIAL_SALARY = -166;

	/** 请到任务指定的国家搬砖 */
	public static final int NOT_RIGHT_TEMPLECOUNTRY = -167;

	/** 已经学习了该技能 */
	public static final int LEARNED_SKILL = -170;

	/** 对象不存在 **/
	public static final int OBJECT_NOT_FOUND = -200;
	/** 距离太远 **/
	public static final int OBJECT_TOO_LONG = -201;
	/** 时间未到 **/
	public static final int GATHER_TIME_ERROR = -202;
	/** 物品绑定 */
	public static final int ITEM_BIND = -203;
	/** 无法交易的对象 */
	public static final int CAN_NOT_TRADING_ITEM = -204;
	/** 玩家不再地图上 */
	public static final int PLAYER_NOT_IN_MAP = -210;

	/** 玩家在PK中 */
	public static final int PLAYER_IN_PK = -250;

	/** 玩家定身中 */
	public static final int PLAYER_CANNOT_MOVE = -251;

	/** DP已满 **/
	public static final int DP_FULL_ERROR = -499;
	/** HP已满 **/
	public static final int HP_FULL_ERROR = -500;

	/** MP已满 **/
	public static final int MP_FULL_ERROR = -501;
	/** 咸阳战复活点状态 */
	public static final int KINGOFWAR_STATUSNPC = -550;
	/** 国王已经领奖 */
	public static final int KINGOFKING_REWARDED = -551;
	/** 不是皇帝 */
	public static final int KINGOFKING_NOT = -552;
	/** 皇城战指挥CD */
	public static final int KINGOFWAR_COMMAND_CD = -553;
	/** 没有找到地图频道 */
	public static final int MAP_CHANNELID_NOTFOUND = -570;

	/** 勋章成长值不够 */
	public static final int MEDAL_GROWVALUE_NOT_ENOUGH = -698;
	/** 护符成长值不够 */
	public static final int PROTECTURE_GROWVALUE_NOT_ENOUGH = -699;
	/** 升星失败 */
	public static final int STAR_UPGRADE_FAIL = -700;
	/** 战魂装备已经满级了 */
	public static final int MILITARY_EQUIPMENT_MAX_LEVEL = -701;
	/** 今天已经上香了 */
	public static final int TODAY_ALREADY_FETE = -702;
	/** 没有在线奖励可领 */
	public static final int NO_ONLINE_REWARD_AVALIABLE = -703;
	/** 宝物升级成长值不够 */
	public static final int TREASURE_GROWVALUE_NOT_ENOUGH = -704;
	/** 目标已经不在运镖中 */
	public static final int TARGET_LORRY_NOT_EXIST = -705;
	/** 全民动员话太多了 */
	public static final int PHRASE_TOO_LARGE = -706;
	/** 已经在同一个队伍中 */
	public static final int ALREADY_IN_SAME_GROUP = -707;
	/** 对方已经死亡 */
	public static final int OPPONENT_IS_ALEADY_DEAD = -708;
	/** 交易已经过期 */
	public static final int EXCHANGE_OVER_TIME = -709;
	/** 强化自动购买材料失败 */
	public static final int ENHANCE_AUTO_BUY_FAIL = -710;
	/** 玩家在敌国的地盘上 */
	public static final int PLAYER_IN_ENEMY_COUNTRY = -711;
	/** 目标地图是敌国的地盘 */
	public static final int TARGET_MAP_IS_ENEMY_PLACE = -712;
	/** 玩家在敌国的王城 */
	public static final int PLAYER_AT_ENEMY_HOME = -713;
	/** 目标地图是敌国的王城 */
	public static final int TARGET_MAP_IS_ENEMY_HOME = -714;
	/** 国家官员全名动员次数今日已经用完 */
	public static final int COUNTRY_MOBILIZATION_COUNT_LIMIT = -715;
	/** 不能驱逐族长 */
	public static final int GANG_EXPEL_MASTER_ERROR = -716;
	/** 交易自己的背包满 */
	public static final int TRADING_SELF_PACK_FULL = -717;
	/** 交易对方的背包满 */
	public static final int TRADING_OTHER_PACK_FULL = -718;
	/** 帮主还在线 , 不能弹劾 */
	public static final int GANG_MASTER_STILL_ONLINE = -719;
	/** 前任官员把今天的禁言次数 */
	public static final int COUNTRY_FORBID_COUNT_LIMIT = -720;
	/** 副本已经打完了 */
	public static final int COPY_ALREADY_COMPLETE = -721;
	/** 沒有發放國民福利 */
	public static final int COUNTRY_NOT_SALARYED = -722;
	/** 双倍熔炼已经用完 */
	public static final int DOUBLE_SMELT_NOT_AVALIABLE = -726;
	/** 狙击的镖车在敌国,不能前往 */
	public static final int EXPRESS_ROB_DESTINATION_ENEMY = -727;
	/** 个人BOSS副本首通奖励已经领取 */
	public static final int BOSS_COPY_FIRST_REWADED = -728;
	/** 任务完成时错误的请求 */
	public static final int QUEST_COMPLETE_ERROR_REQUEST = -729;
	/** 还没有打死过这只BOSS */
	public static final int HAVENT_BEAT_BOSS = -730;
	/** 护符每日上限 */
	public static final int PROTECUTRE_DAILY_LIMIT = -731;
	/** 短时间击杀同一目标过多，10分钟内无法增加杀人数 */
	public static final int KILLED_SAME_PLAYER_IN_SHORTTIME = -732;
	/** 膜拜皇帝的奖励, 已经是橙色了 */
	public static final int WARSHIP_ALREADY_ORANGE = -733;
	/** 今天膜拜次数用完了 */
	public static final int TODAY_WARSHIP_NOT_AVAILIBLE = -734;
	/** 不在皇城站中 */
	public static final int KINGOFWAR_NOT_FIGHTING = -735;
	/** 这位申请人被拒绝了 */
	public static final int GANG_APPLIER_REJECT = -736;
	/** 国民福利领取过期 */
	public static final int CIVIL_RECIEVE_TIME_INVALID = -737;
	/** 官员福利领取过期 */
	public static final int OFFICIAL_RECIEVE_TIME_INVALID = -738;
	/** 皇城站不能召集卫队 */
	public static final int KINGOFWAR_CANNOT_CALLGUARD = -739;
	/** 国旗已活 */
	public static final int COUNTRYFLAG_IS_ALIVE = -740;
	/** 大臣已活 */
	public static final int DIPLOMACY_IS_ALIVE = -741;
	/** 升级VIP,增加每日使用次数 */
	public static final int ITEM_VIP_USE_LIMIT = -742;
	/** 交易条件不满足 */
	public static final int TRADE_CONDITION_NOT_SATISFY = -743;
	/** 交易条件不满足 */
	public static final int OPPNENT_TRADE_CONDITION_NOT_SATISFY = -744;

	/** 没有到指定开服的日期 */
	public static final int GAME_NOT_BEGIN = -745;

	/** 超过了指定的开服的时间 */
	public static final int GAME_OVER_START = -746;

	/** 称号没有激活 */
	public static final int NICKNAME_NOT_ACTIVE = -747;

	/** 军衔奖励已经领取 */
	public static final int MILITARY_ACTIVE_REWARED = -748;

	/** 超出了服务器的限量的数 */
	public static final int BEYOND_SERVER_LIMIT = -749;

	/** 消费排行榜奖励已经领取 */
	public static final int CONSUME_ACTIVE_REWARDED = -750;

	/** 等级竞技奖励已经领取 */
	public static final int LEVEL_ACTIVE_REWARDED = -751;

	/** 消费活动消费元宝数量没有达到要求 */
	public static final int CONSUME_ACTIVE_NOT_ENOUGH = -752;

	/** 军衔活动，活动期间军衔没有达到要求 */
	public static final int MILITARY_ACTIVE_NOT_ENOUGH = -753;

	/** 等级竞技活动，活动期间等级没有达到要求 */
	public static final int LEVEL_ACTIVE_NOT_ENOUGH = -754;

	/** 称号已经装备了 */
	public static final int NICKNAME_ALREADY_EQUIP = -755;

	/** 当前VIP等级，不能佩戴更多的称号 */
	public static final int VIP_LEVEL_CANNOT_EQUIP_MORE = -756;

	/** VIP等级不够，还不能至尊锻造 */
	public static final int VIP_LEVEL_CANNOT_SUPERFORGE = -757;

	/** 回购点击过快 */
	public static final int OPERATE_TOO_FAST = -758;

	/** 称号已经过期,不能激活 */
	public static final int NICKNAME_INVALID = -759;

	/** 不能对官职比自己高的人禁言 */
	public static final int OFFICIAL_CANNOT_FORBIT_HIGHER_OFFICIAL = -760;

	/** 不能标记自己为内奸 */
	public static final int CANNOT_SET_SELF_TRAITOR = -761;

	/** 不能标记比自己官职高的人为内奸 */
	public static final int OFFCIAL_CANNOT_SETTRAITOR_HIGER_OFFICIAL = -762;

	/** 对方还不是内奸 */
	public static final int TARGET_NOT_TRAITOR = -763;

	/** 国家内奸数量超过上限了 */
	public static final int COUNTRY_TRAITOR_TOO_MUCH = -764;

	/** 开服竞技的奖励已经领取了 */
	public static final int COMPETE_REWARD_ALREADY_RECIEVED = -765;

	/** 邮件操作 过快 */
	public static final int MAIL_OPERATOR_TOO_FAST = -766;

	/** 每日获得经验值到达上限 */
	public static final int DAILY_EXP_LIMIT = -767;

	/** 收集灵魂装备奖励已经领取了 */
	public static final int COLLECT_REWARD_ALREADY_RECIEVED = -768;

	/** 申请进入家族在退出家族的CD中 */
	public static final int APPLY_GANG_IN_QUIT_CD = -769;

	/** 邀请进家族的人在退出家族的CD中 */
	public static final int INVITE_GANG_IN_QUIT_CD = -770;

	/** 家族还剩一个人的时候不能退出家族，只能解散 */
	public static final int GANG_MASTER_CANNOT_QUIT = -771;

	/** 商店限购的东西 购买的过多 */
	public static final int SHOP_BUY_LIMIT = -772;
	/** 武器镶嵌宝石数量达到上限 */
	public static final int INSET_GEM_FULL = -773;
	/** 武器脱掉镶嵌宝石已经不在了 */
	public static final int UNSET_GEM_IS_NOT_EXIST = -774;
	/** 这个副本今天已经扫荡过了 */
	public static final int COPY_TODAY_ALREADY_BATCH = -775;
	/** 功勋分不够 */
	public static final int FEATS_NOT_ENOUGH = -776;
	/** 充的钱还不够 */
	public static final int RECHARGE_NOT_ENOUGH = -777;
	/** 充值献礼活动已经领取奖励了 */
	public static final int RECHARGE_ALREADY_RECIEVED_REWARD = -778;
	/** 城池战的行动力不够 */
	public static final int CAPTURE_MOTION_NOT_ENOUGH = -779;
	/** 领取城池奖励失败,你的城池被别人抢了 */
	public static final int TOWN_BEEN_ROBBED = -780;
	/** 申请的入驻空的城池已经被别人抢先一步了 */
	public static final int TOWN_APPLIED_BEEN_CAPTURE = -781;
	/** 挑战城池CD */
	public static final int TOWN_CHALLENGE_CD = -782;
	/** 已经变成空城了,不需要挑战 */
	public static final int TOWN_ALREADY_EMPTY = -783;
	/** 地图不允许骑坐骑 */
	public static final int MAP_FORBID_RIDE_HORSE = -784;
	/** 坐骑的技能槽已经满了 */
	public static final int HORSE_LEARNED_SKILL = -785;
	/** 城池站当前没有CD中，不能重置CD */
	public static final int CAPTURE_NOT_IN_CD = -786;
	/** 装备不满足合成神装的条件 */
	public static final int EQUIPMENT_CANNOT_ATTACH_GODSTAT = -787;
	/** 合服消费活动奖励已经获得 */
	public static final int MERGE_CONSUME_ALREADY_RECIEVED = -788;
	/** BOSS之家地图不能召集 */
	public static final int BOSS_HOME_CANNOT_CALLTOGETHER = -789;
	/** 装备不满足合成套装的条件 */
	public static final int EQUIPMENT_CANNOT_CHANGE_SUIT = -790;
	/** 不满足进入西周王陵的地图的条件 */
	public static final int GAS_COPY_CANNOT_ENTER = -791;
	/** 进入西周王陵的次数不足 */
	public static final int GAS_COPY_COUNT_NOT_ENOUGH = -792;
	/** 不能使用回城卷轴 */
	public static final int CANNOT_USE_BACKITEM = -793;
	/** 输入的数目是错误的 */
	public static final int INPUT_NUM_ILLEGAL = -794;
	/** 装备不满足灵魂升星的条件 */
	public static final int EQUIPMENT_CANNOT_UPGRADE_SOUL = -795;
	/** 充值活动已经领过了 （点击过快，第二次的提示） */
	public static final int RECHARGE_ALREADY_RECIEVED = -796;
	/** 身上没有使用宠物 */
	public static final int NOT_EQUIP_PET = -797;
	/** 道具不能丢弃 */
	public static final int ITEM_CANNOT_DROP = -798;
	/** 宠物已经过期了 */
	public static final int PET_IS_DEPRECATED = -799;
	/** 已经是族长了 */
	public static final int GANG_ALREADY_MASTER = -800;
	/** 弹劾失败 */
	public static final int GANG_CANNOT_IMPEACH = -801;
	/** 物品使用已经达到上限 */
	public static final int ITEM_USE_LIMIT = -802;
	/** 使用物品加RP值 RP值已经高于X值 */
	public static final int ITEM_ADD_RP_LIMIT = -803;
	/** 还没死 不能买活 */
	public static final int BUY_LIFE_NOT_DEAD = -804;
	/** 自动复活的时间 还在CD */
	public static final int AUTO_LIFE_CD = -805;
	/** 对方处于虚弱的状态 */
	public static final int PLAYER_IN_WEAK = -806;
	/** 玩家之间的等级差距太大 */
	public static final int LEVEL_GAP_OVERFLOW = -807;
	/** 该玩家是内奸 */
	public static final int PLAYER_IS_TRAITOR = -808;
	/** 不在活动时间 */
	public static final int NOT_IN_ACTIVITY_PERIOD = -809;
	/** 军衔等级条件不对 */
	public static final int MILITARY_NOT_ENOUGH = -810;
	/** 军衔升级失败 */
	public static final int MILITARY_UPGRADE_FAIL = -811;
	/** 军衔兵法升阶失败 */
	public static final int MILITARY_STRATEGY_UPGRADE_FAIL = -812;
	/** 军衔兵法升阶概率 失败 */
	public static final int MILITARY_STRATEGY_UPGRADE_FAIL_UNLUCKY = -813;
	/** 强化的装备不存在 */
	public static final int IMPROVE_EQUIP_NOT_EXIST = -814;
	/** 强化的装备已经满级 */
	public static final int IMPROVE_EQUIP_MAX_LEVEL = -815;
	/** 继承出来的装备不存在 */
	public static final int EXTENDS_EQUIP_NOT_EXIST = -816;
	/** 没有五行的装备不能重置五行 */
	public static final int EQUIP_ELEMENT_NONE = -817;
	/** 打开英魂操作界面异常 */
	public static final int OPEN_YH_WINDOW_ERROR = -818;
	/** 运镖已经刷到最好的了 */
	public static final int EXPRESS_ALREADY_BEST_QUALITY = -820;
	/** 当前没有在运的镖车 */
	public static final int EXPRESS_NOT_EXIST = -821;
	/** 英魂等级已满 */
	public static final int SOUL_LEVEL_FULL = -822;
	/** 坐骑进阶丹不足 */
	public static final int SOUL_CAN_NOT_OPEN = -823;
	/** 神兵进阶丹不足 */
	public static final int ARTIFACT_CAN_NOT_OPEN = -824;
	/** 英魂系统为开启 */
	public static final int SOUL_NOT_OPEN = -826;
	/** 神兵系统为开启 */
	public static final int ARTIFACT_NOT_OPEN = -827;
	/** 英魂满级了 */
	public static final int SOUL_MAX_LEVEL = -828;
	/** 神兵蛮级了 */
	public static final int ARTIFACT_MAX_LEVEL = -829;
	/** 继承的主副装备无法匹配 */
	public static final int EQUIPMENT_EXTENDS_NOT_MATCH = -830;
	/** 今天运镖次数已经用完了 */
	public static final int LORRY_COUNT_TODAY_OVER = -831;
	/** 国家相同 */
	public static final int COUNTRY_SAME = -834;
	/** 国家砍大臣/国旗发集结冷却时间未过 */
	public static final int COUNTRY_DIP_COOL_TIME_NOT_PASS = -835;
	/** 合成的灵魂属性不存在 */
	public static final int TARGET_SOUL_STAT_NOT_EXIST = -836;
	/** 国家不同 */
	public static final int COUNTRY_NOT_SAME = -837;
	/** 包含敏感词汇 */
	public static final int WORDS_SENSITIVE = -838;
	/** 非本国玩家 */
	public static final int NOT_LOCAL_RESIDENT = -839;
	/** 镖车走沟里了 */
	public static final int LORRY_IS_BUSY_NOW = -840;
	/** 好友不能拉黑的 */
	public static final int ADD_FRIEND_TO_BLACKLIST = -841;
	/** 召集人数的限制 */
	public static final int CALL_TOGETHER_LIMIT = -842;
	/** 特定技能ID的BUFF不存在 */
	public static final int SPECIFIC_SKILL_BUFF_INEXISTENCE = -843;
	/** 特定技能ID的BUFF不存在 */
	public static final int CLIENT_SETTINGS_TOO_LONG = -844;
	/** 目标禁止被攻击 */
	public static final int TARGET_DISBAND_ADD_FRIEND = -845;
	/** 模块没有开启 */
	public static final int MODULE_NOT_OPEN = -846;
	/** 对方禁止被邀请入队 */
	public static final int TARGET_DISBAND_GROUP_REQUEST = -847;
	/** 对方禁止被请求交易 */
	public static final int TARGET_DISBAND_TRADE_REQUEST = -848;
	/** 玩家名字包含敏感字符 */
	public static final int PLAYER_NAME_ILLEAGAL = -849;
	/** 不在排行榜里面 */
	public static final int PLAYER_NOT_IN_RANK = -850;
	/** 无效的页数请求,没有那么多数据 */
	public static final int INVALID_REQUEST = -851;
	/** 熔炼已经满级了 */
	public static final int SMELT_MAX_LEVEL = -852;
	/** 神兵等级不足 */
	public static final int ARTIFACT_GRADE_NOT_ENOUGH = -853;
	/** 英魂等级不足 */
	public static final int SOUL_GRADE_NOT_ENOUGH = -854;
	/** 军衔兵法等级不足 */
	public static final int STRATEGY_LEVEL_NOT_ENOUGH = -855;
	/** 帮主没走多久,不能弹劾 */
	public static final int MASTER_STILL_ACTIVE = -856;
	/** 有人已经在 */
	public static final int SOMEONE_ALREADY_IMPEACH = -857;
	/** 帮派不存在 */
	public static final int APPLY_GANG_NOT_EXIST = -858;
	/** 使用道具不在指定的场景 */
	public static final int USE_ITEM_NOT_IN_SCENE = -859;
	/** 该任务不是客服端接取类型 */
	public static final int QUEST_NOT_CLIENTACCPET = -860;
	/** 该国家任务已经发布 */
	public static final int COUNTRY_QUEST_STARTED = -861;
	/** VIP等级条件不满足 */
	public static final int VIP_CONDITION_NOT_SATISFY = -862;
	/** 礼物已经领取了 */
	public static final int GIFT_REWARDED = -863;
	/** 商店已经最大等级 */
	public static final int COUNTRYSHOP_MAX_LEVEL = -864;
	/** 商店等级条件不满足 */
	public static final int COUNTRYSHOP_LEVEL_NOT_SATISFY = -865;
	/** 军衔兵法 升星条件不满足 */
	public static final int UPGRADE_STRATEGY_CONDITION_NOT_ENOUGH = -866;
	/** 红包过期了,跨天清空的 */
	public static final int RED_GIFT_OVERDUE = -867;
	/** GM权限不足 */
	public static final int OPERATOR_GMPRIVILEGE = -868;
	/** GM */
	public static final int OPERATOR_GM = -869;

	/** 地图处于封边时间 */
	public static final int MAP_FORBIDTIME = -870;
	/** 地图处于封边时间 */
	public static final int MAP_FORBIDTIME1 = -871;

	/** 超过铜币鼓舞次数 */
	public static final int COPY_COPPER_ENCOURGE_COUNT = -872;
	/** 转职状态不满足 */
	public static final int PROMOTION_STAGE_NOT_SATIESFIED = -873;
	/** 强化转移的两个装备 部位不一致 */
	public static final int ENHANCE_TRANSFER_NOT_SAME_TYPE = -874;
	/** 帮派不存在 */
	public static final int GANG_NOT_EXIST = -875;
	/** 超过鼓舞次数 */
	public static final int COPY_ENCOURAGE_COUNT_MAX = -876;
	/** 管理后台禁言 */
	public static final int OPERATOR_FORBID_CHAT = -877;
	/** 充值数额不足 */
	public static final int RECHARGE_LESS = -878;
	/** 名将的这个武魂已经收集满了 */
	public static final int EQUIP_SOUL_ALREADY_ENOUGH = -879;
	/** 战斗力条件不满足 */
	public static final int BATTLE_SCORE_NOT_SATISFY = -880;
	/** BOSS积分不足 */
	public static final int BOSS_COINS_NOT_ENOUGH = -881;
	/** BOSS积分这一档已经买了 */
	public static final int BOSS_COINS_ALREADY_PAYED = -882;
	/** 购买BOSS积分属性条件不满足 */
	public static final int BOSS_COINS_CANNOT_BUY = -883;
	/** BOSS积分等级已经满级 */
	public static final int BOSS_COINS_MAX_LEVEL = -884;
	/** 没有可以强化的装备 */
	public static final int DONNOT_HAVE_ENHANCEABLE_EQUIP = -885;
	/** 没有可以找回的项目 */
	public static final int NO_CLAW_ITEMS = -886;
	/** 追回不在活动时间内 */
	public static final int CLAW_NOT_PERIOD_TIME = -887;

	/** 说说的长度限制 */
	public static final int MOOD_PHRASE_LEN_LIMIT = -900;
	/** 好友数量最大上限 */
	public static final int FRIEND_NUMS_LIMIT = -901;
	/** 不是同一个国家的人 */
	public static final int FRIEND_NOT_SAME_COUNTRY = -902;
	/** 目标在另一个关系栏里面 */
	public static final int TARGET_IN_OTHER_RELATIONLIST = -903;
	/** 联系人里面目标不存在 */
	public static final int CONTACT_TARGET_NOT_EXIST = -904;
	/** 联系人已经存在 */
	public static final int CONTACT_ALREADY_EXIST = -905;
	/** 客户端传过来的字符串非法 */
	public static final int CLIENT_SEND_STRING_ILLEAGAL = -906;
	/** 此人不是你们国家的人 */
	public static final int COUNTRY_NOT_HAVE_PEOPLE = -907;
	/** 此人已经有官职了 */
	public static final int COUNTRY_OFFICAL_HAVED = -908;
	/** 家族名字过长 */
	public static final int COUNTRY_NAME_LENGTH_2_LANG = -909;
	/** 家族名不能为空 */
	public static final int COUNTRY_NAME_IS_NULL = -910;
	/** 平民不能领取官员福利 */
	public static final int COUNTRY_GET_OFFICIAL_ERROR = -911;
	/** 禁言次数不足 */
	public static final int COUNTRY_FORBID_NUM_NOT_ENGOUTH = -912;
	/** 虎符只能分配给两个人 */
	public static final int COUNTRY_CALLTOGETHER_TOKEN_OUT = -913;
	/** 大臣处于保护期 */
	public static final int COUNTRY_DIPLOMACY_IS_SAFE = -914;
	/** 前后端签到日期不一致 */
	public static final int WELFARE_SIGN_CURRENT_DAY_NOT_SAME = -916;
	/** 重复签到 */
	public static final int WELFARE_SIGN_REPEAT = -917;
	/** 签到领奖天数未达到 */
	public static final int WELFARE_SIGN_NOT_PASS = -918;
	/** 签到vip奖励已经领取 */
	public static final int WELFARE_SIGN_REWARDED_VIP = -919;
	/** 签到普通奖励已经领取 */
	public static final int WELFARE_SIGN_REWARDED_DEF = -920;
	/** 签到非vip玩家领取vip奖励 */
	public static final int WELFARE_SIGN_NOT_VIP = -921;
	/** 签到非vip玩家补签 */
	public static final int WELFARE_SIGN__NOT_VIP_FILLSIGN = -922;
	/** 补签次数不足 */
	public static final int WELFARE_SIGN_FILL_SIGN_NUM_NOT_ENGOUTH = -923;
	/** 创建时间太晚 */
	public static final int WELFARE_SIGN_CREATE_TIME_TOO_SMALL = -924;
	/** 已经领取过奖励了 */
	public static final int WELFARE_OFFLINE_REWARDED = -925;
	/** 咸阳争夺战进行中 */
	public static final int KINGOFWAR_FIGHTING = -926;
	/** 家族战正在进行中 */
	public static final int GANGOFWAR_FIGHTING = -927;
	/** 活跃值不够 */
	public static final int WELFARE_ACTIVE_VALUE_NOT_ENGOUTH = -928;
	/** 活跃值已经领过奖励了 */
	public static final int WELFARE_ACTIVE_REWARDED = -929;
	/** 累计在线已经领过奖励了 */
	public static final int WELFARE_ONLINE_REWARDED = -930;
	/** 累计在线时间未达到 */
	public static final int WELFARE_ONLINE_NOT_ENOUGH = -931;
	/** 离线时间=0 */
	public static final int WELFARE_OFFLINE_ZERO = -932;
	/** 不能禁言自己 */
	public static final int COUNTRY_CANTNOT_FORBID_SELF = -933;
	/** 昨天没有可追回收益 */
	public static final int WELFARE_CLAWBACK_BACK_IS_NULL = -934;
	/** 家族战回城CD中 */
	public static final int GANGOFWAR_BACKHOME_CD = -935;
	/** 已经领取周奖励 */
	public static final int VIP_WEEKREWARD_RECEIVED = -936;
	/** VIP等级不能扫荡爬塔副本 */
	public static final int VIP_LEVEL_CANNOT_BATCHLADDER = -937;
	/** VIP等级奖励已经领取 */
	public static final int VIP_LEVEL_REWARDED = -938;
	/** 传送点已经过期 */
	public static final int CHAT_TRANSPORT_DEPRECATED = -940;
	/** 次数追回不可以使用元宝 */
	public static final int WELFARE_CLAWBACK_CAN_NOT_USE_GOLD = -950;
	/** 至尊追回不可以追回次数 */
	public static final int WELFARE_CLAWBACK_CAN_NOT_CLAWBACK_NUM = -951;
	/** 被任命官员对象已经是卫队 */
	public static final int COUNTRY_APPOINT_IS_GUARD = -952;
	/** 被任命卫队对象已经是官员 */
	public static final int COUNTRY_APPOINT_IS_OFFICIAL = -953;
	/** 今天杀人荣誉已达最大值 */
	public static final int TODAY_HONOR_IS_MAX = -954;

	/** 指定天对应在线奖励已经领取（7天登录） */
	public static final int TARGET_DAY_ONLINE_REWARD_HAS_DRAW = -955;

	/** 没有充值 */
	public static final int FIRSTPAY_NOT_PAY = -956;

	/** 首充奖励已经领取 */
	public static final int FIRSTPAY_REWARD_HAS_DRAW = -957;

	/** 装备不存在的足迹 */
	public static final int FOOTPRINT_NOT_FOUND = -970;

	/** 装备足迹过期 */
	public static final int FOOTPRINT_DEPRECATED = -971;

	/** 搬砖读条时间未到 */
	public static final int TEMPLE_BRICK_CD = -972;
	/** 手机认证礼包已经领取 */
	public static final int MOBILEPHONE_REWARDED = -973;
	/** 手机认证失败 */
	public static final int MOBILEPHONE_UNCONFIRMATION = -974;
	/** 平台VIP等级礼包已经领取 */
	public static final int OPERATOR_REWARDED = -975;
	/** 平台VIP等级不足 */
	public static final int OPERATOR_LEVEL = -976;
	/** 微信礼包已经领取 */
	public static final int WECHAT_REWARDED = -977;
	/** 游历任务CD中 */
	public static final int RANDOM_QUEST_CD = -980;
	/** 该类型的任务超过每天接取的最大次数 */
	public static final int TYPE_OF_QUEST_OUTOF_COMPLETETODAY = -981;
	/** 当前已经接取了该类型的任务 */
	public static final int CURRENT_NOACCEPT_TYPE_QUEST = -982;
	/** 完善信息奖励已经领取 */
	public static final int SUBINFORMATION_REWARDED = -983;
	/** 完善信息失败 */
	public static final int SUBINFORMATION_UNCONFIRMATION = -984;
	/** 坐骑幻化没有使用 */
	public static final int HORSE_ILLUTION_IS_NOT_USE = -985;
	/** 坐骑幻化已永久激活 */
	public static final int HORSE_ILLUTION_HAS_FOREVER_ACTIVE = -986;
	/** 坐骑幻化正在使用 */
	public static final int HORSE_ILLUTION_IS_USE = -987;
	/** 坐骑幻化没有激活 */
	public static final int HORSE_ILLUTION_IS_NOT_ACTIVE = -988;

	/** 防沉迷中，不健康游戏 */
	public static final int ADDICATION_UNHEALTHY_TIME = -990;

	/** 没有发现该称号 */
	public static final int NOT_FOUND_NICKNAME = -1000;

	/** 已购买铜钱投资 */
	public static final int COPPER_INVEST_HAS_BUY = -1010;
	/** 已购买礼金投资 */
	public static final int GIFT_INVEST_HAS_BUY = -1011;
	/** 已购买经验投资 */
	public static final int EXP_INVEST_HAS_BUY = -1012;
	/** 已购买荣誉投资 */
	public static final int HONOR_INVEST_HAS_BUY = -1013;
	/** 已购买复活丹投资 */
	public static final int RELIVEDAN_INVEST_HAS_BUY = -1014;
	/** 该类型投资没有购买 */
	public static final int INVEST_TYPE_NOT_BUY = -1015;
	/** 领取奖励失败 */
	public static final int INVEST_REWARD_DRAW_FAIL = -1016;

	/** 该档次经验放送已经领取 */
	public static final int EXPACTIVE_REWARD_HAS_DRAW = -1020;
	/** 橙色灵魂礼包已经领取 */
	public static final int EQUIPACTIVE_REWARD_HAS_DRAW = -1021;
	/** 全民强化奖励已经领取 */
	public static final int EQUIPENHANCEACTIVE_REWARD_HAS_DRAW = -1022;
	/** 每日充值没有奖励可领取 */
	public static final int EVERYDAY_RECHARGE_WITHOUT_REWARD_CANDRAW = -1023;

	/** 在储君黑名单内 */
	public static final int IN_RESERVEKING_BLACKLIST = -1030;
	/** 储君召集令在cd中 */
	public static final int RESERVEKING_CALLTOGETHER_CD = -1031;
	/** 不是储君 */
	public static final int NOT_RESERVEKING = -1032;
	/** 已存在储君 */
	public static final int RESERVEKING_EXIST = -1033;

	/** 成就任务没有完成 */
	public static final int ACHIEVEMENT_NOT_FINISH = -1035;
	/** 成就任务已经领奖 */
	public static final int ACHIEVEMENT_IS_REWARDED = -1036;
	/** 成就点不足 */
	public static final int ACHIEVEMENT_POINT_NOT_ENOUGH = -1037;
	/** 已达到成就最高等级 */
	public static final int ACHIEVEMENT_MAX_LEVEL = -1038;
	/** 改成就没有完成 */
	public static final int ACHIEVEMENT_NOT_COMPLETE = -1039;

	/** 探宝仓库空间不足 */
	public static final int TREASURE_STORAGE_PACK_SIZE_NOT_ENOUGH = -1040;

	/** 国家副本正在进行中 */
	public static final int COUNTRYCOPY_WARRING = -1050;

	/** 国家副本已经报名 */
	public static final int COUNTRYCOPY_ENROLLED = -1051;
	/** 已经报名的不能助威 */
	public static final int ENROLL_COUNTRYCOPY_ENCOURAGED = -1052;

	/** 国家副本已经助威最大次数 */
	public static final int COUNTRYCOPY_ENCOURAGED_MAX = -1053;
	/** 进入国家副本CD */
	public static final int COUNTRYCOPY_ENTER_CD = -1054;
	/** 国家副本进入次数不足 */
	public static final int COUNTRYCOPY_ENTER_MAXCOUNT = -1055;
	/** 今日助威次数已经满 */
	public static final int ENCOURAGE_TOO_MUCH = -1056;
	/** 助威申请CD中 */
	public static final int APPLY_ENCOURAGE_CD = -1057;
	/** 还没有报名 */
	public static final int HAVNT_ENROLL_COUNTRYCOPY = -1058;
	/** 副本不在进行中 */
	public static final int COUNTRYCOPY_NOT_WARRING = -1059;
	/** 同一次副本 已经助威的不能再助威 */
	public static final int COUNTRYCOPY_ENCOURAGED = -1060;
	/** 副本不存在，也没有人报名 */
	public static final int COUNTRYCOPY_NOT_EXSIT = -1061;
	/** 强化属性战力奖励已经领取了 */
	public static final int ENHANCE_POWER_ACTIVE_REWARD_HAS_DRAW = -1062;
	/** boss首杀奖励已经领取 */
	public static final int BOSS_FHREWARD_HAS_RECEIVE = -1063;

	/** 不在活动时间内 */
	public static final int NOT_IN_ACTIVITY_TIME = -1064;

	/** 360礼包一天只能领取一次 */
	public static final int GIFT360_TIME = -1070;
	/** 360礼包只能领取7次 */
	public static final int GIFT360_COUNT = -1071;
	/** 公测活动已经购买 */
	public static final int PUBLIC_TEST_GIFT_HAS_BUY = -1072;
	/** 公测活动购买条件不足 */
	public static final int PUBLIC_TEST_GIFT_HAS_NOT_BUY_CONDITION = -1073;

	/** 军旗数量不足 */
	public static final int COUNTRY_TECHNOLOGY_NOT_ENOUGH_FLAG_COUNT = -1077;

	/** 奇虎360加速球礼包领取， 已经领取 */
	public static final int QIHU_SPEED_BALL_REWARD = -1078;
	/** 黑市积分不足 */
	public static final int BLACKSHOP_POINT_NOT_ENOUGH = -1079;
	/** 奇虎360特权礼包已经领取 */
	public static final int QIHU_PRIVILEGE_HAS_DRAW = -1080;
	/** 奇虎360特权礼包领取条件不足 */
	public static final int QIHU_PRIVILEGE_NO_CONDITION = -1081;

	/** 开启时间或结时间件不能为空 */
	public static final int QIHU_PRIVILEGE_TIME_NULL = -1082;
	/** 开启时间不能大于结束事件 */
	public static final int QIHU_PRIVILEGE_START_TIME_THAN_END = -1083;
	/** 聊天字符太多错误 */
	public static final int CHAT_CHAR_TOO_MUCH_ERROR = -1084;
	/** 活动正在进行中 */
	public static final int ACTIVITY_OPENNING = -1085;

	/** 时装达最大等级 */
	public static final int FASHION_MAX_LEVEL = -1090;
	/** 当前没有穿戴的时装 */
	public static final int FASHION_NOT_WEAR = -1091;
	/** 没有时装 */
	public static final int FASHION_COUNT_EQUAL_ZERO = -1092;
	/** 和服活动登陆有礼已经领取 */
	public static final int MERGE_ACTIVE_LOGIN_GIFT_HAS_DRAW = -1093;
	/** 和服活动特惠礼包已经购买 */
	public static final int MERGE_ACTIVE_CHEAP_GIFT_HAS_DRAW = -1094;
	/** 和服活动特惠礼包购买条件不足 */
	public static final int MERGE_ACTIVE_CHEAP_GIFT_NO_CONDITION = -1095;

	/** 黑市活动已开启 */
	public static final int BLACKSHOP_ACTIVITY_HAS_OPEN = -1098;
	/** 黑市活动未开启 */
	public static final int BLACKSHOP_ACTIVITY_NOT_OPEN = -1099;

	/** 没有接取营救任务 */
	public static final int RESCUE_NOT_START = -1100;
	/** 当前没有刺探任务 */
	public static final int INVESTIGATE_NOT_START = -1101;
	/** 当前情报颜色已是道具颜色 */
	public static final int INVESTIGATE_CURRENT_COLOR_GREATER = -1102;
	/** 营救任务已完成 */
	public static final int RESCUE_FINISH = -1103;
	/** 登陆有礼已经领取 */
	public static final int COMMON_LOGIN_GIFT_HAS_DRAW = -1104;
	/** 特惠礼包已经购买 */
	public static final int COMMON_CHEAP_GIFT_BAG_HAS_BUY = -1105;
	/** 特惠礼包没有购买上一级别 */
	public static final int COMMON_CHEAP_GIFT_BAG_NOT_BUY_LOW_LEVEL = -1106;
	/** 特惠礼包购买条件不足 */
	public static final int COMMON_CHEAP_GIFT_NO_CONDITION = -1107;
	/** 通用消费活动奖励已领取 */
	public static final int CONSUME_ACTIVITY_REWARDED = -1110;
	/** 鉴宝活动正在开启 */
	public static final int COMMON_IDENTITY_TREASURE_IS_OPENING = -1111;
	/** 鉴宝活动没有正在开启 */
	public static final int COMMON_IDENTITY_TREASURE_NOT_OPENING = -1112;
	/** 鉴宝条件不足 */
	public static final int COMMON_IDENTITY_TREASURE_NOT_CONDITION = -1113;
	/** 鉴宝活动不存在 */
	public static final int COMMON_IDENTITY_TREASURE_NOT_EXIST = -1114;
	/** 首充领取条件不足 */
	public static final int COMMON_FIRST_PAY_NOT_CONDITION = -1115;
	/** 首充奖励已经领取 */
	public static final int COMMON_FIRST_PAY_HAS_DRAW = -1116;

	/** 通用全民探宝奖励已领取 */
	public static final int COMMON_TREASURE_ACTIVE_REWARDED = -1117;

	/** 通用红包已领取 */
	public static final int COMMON_REDPACK_ACTIVE_REWARDED = -1118;

	/** 集字活动领取条件不足 */
	public static final int COMMON_COLLECT_WORLD_NOT_CONDITION = -1119;

	/** 集字活动消耗不足 */
	public static final int COMMON_COLLECT_WORLD_NO_CORE = -1120;
	/** 副本进入的条件不足 */
	public static final int COPY_ENTER_NO_CONDITION = -1121;

	/** 没有正在出战美人 */
	public static final int BEAUTY_WITHOUT_FIGHTING_GIRL = -1122;
	/** 当前有正在出战的美人 */
	public static final int BEAUTY_HAVE_FIGHTING_GIRL = -1123;
	/** 美人已是最大等级 */
	public static final int BEAUTY_MAX_LEVEL = -1124;
	/** 没有足够的缠绵值 */
	public static final int BEAUTY_NOT_ENOUGH_SENSE = -1125;
	/** 在出战cd中 */
	public static final int BEAUTY_IN_CD = -1126;
	/** 该技能已学习 */
	public static final int BEAUTY_SKILL_LEARNED = -1127;
	/** 已经达到最大可获得技能数 */
	public static final int BEAUTY_MAX_SKILL_COUNT = -1128;
	/** 技能学习条件不满足 */
	public static final int BEAUTY_SKILL_LEARN_CONDITION_NOT_VERYFY = -1129;
	/** 激活条件不满足 */
	public static final int BEAUTY_ACTIVE_CONDITION_NOT_VERYFY = -1130;
	/** 对应美人没有激活 */
	public static final int BEAUTY_TARGET_NOT_ACTIVE = -1131;
	/** 美人已经激活 */
	public static final int BEAUTY_GIRL_ACTIVE = -1132;
	/** 美人等级不足 */
	public static final int BEAUTY_GIRL_LEVEL_NOT_ENOUGH = -1133;

	/** 美人道具数量限制 */
	public static final int BEAUTY_GIRL_ITEM_COUNT_LIMIT = -1134;
	/** 总缠绵等级不在范围内 */
	public static final int BEAUTY_GIRL_SUMLEVEL_NOT_VERIFY = -1135;

	/** 兵书已达最大阶数 */
	public static final int WARBOOK_MAX_GRADE = -1136;
	/** 兵书该技能已经学习了 */
	public static final int WARBOOK_SKILL_LEARNED = -1137;
	/** 道具使用已达上限 */
	public static final int WARBOOK_ITEM_COUNT_LIMIT = -1139;
	/** 2345浏览器奖励已经领取 */
	public static final int BROWSER_2345_REWARDED = -1140;
	/** 护腕合成部位不一样 */
	public static final int ITEM_COMBING_TARGET_TYPE_NOT_EQUAL = -1141;
	/** 兵书阶数不大于 */
	public static final int WARBOOK_GRADE_NOT_GREATER = -1143;
	/** 主装备的阶数已达上限 */
	public static final int COMBING_ITEM_MAX_GRADE = -1144;

	/** 大将军王已达最大星 */
	public static final int MILITARY_MAX_STAR = -1145;

	/** 副本重置次数超过限制 */
	public static final int COPY_RESET_TIMES_LIMIT = -1146;

	/** 材料装备为空 */
	public static final int COMBING_PACK_ITEM_NOT_EXIST = -1147;
	/** 没有穿戴材料装备对应部位的主装备 */
	public static final int COMBING_WITHOUT_TARGET_EQUIPMENT_TYPE_MAINITEM = -1148;
	/** 不在指定的材料装备名单里 */
	public static final int COMBING_NOT_IN_TARGET_MATERIAL_ITEM = -1149;
	/** 主装备物法与最终目标装备物法对不上 */
	public static final int COMBING_MAINITEM_SPECIFY_TYPE_NOT_VERIFY = -1150;
	/** 黄金宝库砸奖条件不足 */
	public static final int GOLD_TREASURY_REWARD_NO_CONDITION = -1151;
	/** 黄金宝库砸奖消耗不足 */
	public static final int GOLD_TREASURY_REWARD_NO_ACTION = 1152;
	/** 黄金宝重置超过次数限制 */
	public static final int GOLD_TREASURY_RESET_NO_TIMES = 1153;
	/** 坐骑装备副本扫荡最低评分 */
	public static final int COPY_HORSEEQUIP_RESET_MIN_QUEST = -1154;
	/** 坐骑装备副本扫荡所需材料不足 */
	public static final int COPY_HORSEEQUIP_RESET_ACTION_NOT_VERIFY = -1155;
	/** 黄金宝库已经砸奖 */
	public static final int GOLD_TREASURY_REWARDED = -1156;

	/** 不是命格道具 */
	public static final int NOT_LIFEGRID_ITEM = -1170;
	/** 命格背包已满 */
	public static final int LIFE_GRID_PACK_NOT_ENOUGH_SIZE = -1171;
	/** 装备了同类型的命格 */
	public static final int LIFE_GRID_EQUIP_SAME_TYPE_ITEM = -1172;
	/** 命格装备栏已满 */
	public static final int LIFE_GRID_EQUIPPACK_NOT_ENOUGH_SIZE = -1173;
	/** 当前命格被锁定 */
	public static final int LIFE_GRID_ITEM_LOCK = -1174;
	/** 命格已达到最大等级 */
	public static final int LIFE_GRID_ITEM_MAX_LEVEL = -1175;
	/** 命格之源不能吞噬别人 */
	public static final int LIFE_GRID_SPECIAL_ITEM_CAN_NOT_BE_DEVOURED = -1176;
	/** 没有符合要求的吞噬命格 */
	public static final int LIFE_GRID_NOT_FOUND_DEVOUR_ITEM = -1177;
	/** 没有找到符合要求的被吞噬命格 */
	public static final int LIFE_GRID_NOT_FOUND_DEVOURED_ITEMS = -1178;
	/** 命格碎片不足 */
	public static final int LIFE_GRID_POINT_NOT_ENOUGH = -1179;
	/** 移动的目标未知有东西 */
	public static final int LIFE_GRID_TARGET_POSITION_HAS_ITEM = -1180;
	/** 命格之源不能被装备 */
	public static final int LIFE_GRID_SPECIAL_ITEM_CAN_NOT_EQUIP = -1181;
	/** 命格仓库没有足够空间 */
	public static final int LIFE_GRID_HOUSE_NOT_ENOUGH_SIZE = -1182;

	/** 所有元素都满了 */
	public static final int SUICIDE_ALL_ELEMENT_FULL = -1185;
	/** 转生已满 */
	public static final int SUICIDE_TRANS_FULL = -1186;
	/** 转生条件未满足 */
	public static final int SUICIDE_TURN_NOT_VERIFY = -1187;
	/** 转生次数不足 */
	public static final int SUICIDE_TURN_NOT_ENOUGH = -1188;
	/** 转生的玩家等级不足 */
	public static final int SUICIDE_LEVEL_N0T_ENOUGH = -1189;

	/** 装备不能转生 */
	public static final int EQUIPMENT_CANNOT_TURN = -1195;
	/** 不是灵魂装备 */
	public static final int NOT_SOUL_EQUIPMENT = -1196;

	/** 坐骑成长丹数量不足 */
	public static final int HORSE_ITEM_COUNT_LIMIT = -1197;
	/** 神兵成长丹数量不足 */
	public static final int ARTIFACT_ITEM_COUNT_LIMIT = -1198;
	/** 英魂成长丹数量不足 */
	public static final int SOUL_ITEM_COUNT_LIMIT = -1199;

	/**
	 * 1200 ~ 1500,留给跨服战系统
	 * */

	/** 跨服BOSS战状态不对 */
	public static final int BOSS_CENTER_STATUS = -1200;
	/** 跨服BOSS没有报名 */
	public static final int BOSS_CENTER_SIGNUP = -1201;

	/** 消费献礼领取条件不足 */
	public static final int CONSUME_GIFT_NO_CONDTION = -1502;
	/** 消费献礼已经领取 */
	public static final int CONSUME_GIFT_HAS_REWARD = -1503;
	/** 军衔突破失败 */
	public static final int MILITARY_BREAK_ERROR = -1504;
	/** 平台VIP等级礼包已经领取 */
	public static final int OPERATOR_TODAY_REWARDED = -1505;
	
	/** 点将台次数已经用完 */
	public static final int POINT_GENERAL_COUNT_LIMIT = -1622;
	/** 点将台已经购买过永久次数了 */
	public static final int POINT_GENERAL_COUNT_BUYED = -1623;
	
	/** 印玺已达最大阶数 */
	public static final int SEAL_MAX_GRADE = -1624;
	/** 印玺该技能已经学习了 */
	public static final int SEAL_SKILL_LEARNED = -1625;
	/** 印玺强化道具使用已达上限 */
	public static final int SEAL_ITEM_COUNT_LIMIT = -1626;
	/** 印玺阶数不大于 */
	public static final int SEAL_GRADE_NOT_GREATER = -1627;
	
	/** Ip被禁 */
	public static final int IP_BLOCK = -25536;

}