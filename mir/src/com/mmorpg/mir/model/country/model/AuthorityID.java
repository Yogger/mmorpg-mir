package com.mmorpg.mir.model.country.model;

public interface AuthorityID {
	/** 任命 */
	public static final int APPOINT_BASE = 10000;
	/** 罢免 */
	public static final int DEPOSE_BASE = 10010;
	/** 任命卫队 */
	public static final String APPOINT_GUARD = "10005";
	/** 罢免卫队 */
	public static final String DESPOSE_GUARD = "10015";

	/** 10021-国家仓库放入权限 */
	public static final String STORE = "10021";
	/** 10022-国家仓库取出权限 */
	public static final String TAKE = "10022";

	/** 10031-升级城门 */
	public static final String UPGRADE_DOOR = "10031";
	/** 10084-升级商店 */
	public static final String UPGRADE_COUNTRYSHOP = "10084";
	/** 升级军工房 */
	public static final String UPGRADE_FACATORY = "10033";
	/** 升级国旗 */
	public static final String UPGRADE_FLAG = "10032";
	/** 设置公告 */
	public static final String SET_NOTICE = "10041";
	/** 制造战车 */
	public static final String CREATE_TANK = "10051";
	/** 升级战车 */
	public static final String UPGRADE_TANK = "10052";
	/** 召集国民 */
	public static final String CALLTOGETHER = "10061";
	/** 召集卫队 */
	public static final String CALLTOGETHER_GUARD = "10062";
	/** 禁言 */
	public static final String FORBIDCHAT = "10076";
	/** 标记内奸 */
	public static final String SET_TRAITOR = "10082";
	/** 国家福利 */
	public static final String OPEN_CIVILSALARY = "10071";
	/** 官员福利 */
	public static final String OPEN_OFFICIALSALARY = "10072";
	/** 分配战车 */
	public static final String DISTRIBUTE_TANK = "10079";
	/** 召回战车 */
	public static final String CALLBACK_TANK = "10080";
	/** 分发虎符 */
	public static final String DISTRIBUTE_TOGETHER_TOKEN = "10081";
	/** 官员全体动员 */
	public static final String OFFICIAL_MOBILIZATION = "10083";

	/** 解除内奸标记 */
	public static final String RELIEVE_TRAITOR = "10085";

	/** 发布国运 */
	public static final String START_COUNTRY_EXPESS = "10073";
	/** 发布国砖 */
	public static final String START_COUNTRY_TEMPLE = "10075";
	/** 放置军旗 */
	public static final String PLACE_ARMY_FLAG = "10086";

}
