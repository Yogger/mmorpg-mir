package com.mmorpg.mir.model.skill.target;

public enum TargetType {
	/** 自己1人 */
	SELF,
	/** 友军1人 */
	FRIEND,
	/** 敌人1人 */
	ENEMY,
	/** 以某个人为中心的敌军 */
	ENEMYRANGE,
	/** 以某个点为中心的敌军 */
	ENEMYXYRANGE,
	/** 以某个人为中心的友军 */
	FRIENDRANGE,
	/** 以某个点为中心的友军 */
	FRIENDXYRANGE,
	/** 以某个人为中心的全体 */
	RANGE,
	/** 以自己为中心的全体小队成员 */
	SELF_RANGE_GROUP,
	/** 以自己为中心的敌人 */
	SELF_RANGE_ENEMY,
	/** 以某个点位中心的全体 */
	XYRANGE,
	/** 客服端选择 */
	CLIENTRANGE,
	/** 以地标方格自定义 */
	XYGRIDCUSTOM,
	/** 以敌人为中心方格自定义 */
	ENEMYGRIDCUSTOM
}
