package com.mmorpg.mir.model.skill.effect;

public enum EffectId {
	//
	BUFF(0),
	/** 中毒 */
	POISON(1),
	/** 流血 */
	BLEED(2),
	/** 无敌 */
	GOD(3),
	/** 引导搬砖 */
	BRICK(4),
	/** 操练 */
	EXERCISE(6),
	/** 回城 */
	BACKHOME(8),
	/** 沉默 */
	SILENCE(9),
	/** 无法选取 */
	INSELECT(10),
	/** 免死 */
	UNDEAD(11),
	/** 伤害吸收 */
	DAMAGESUCK(12),
	/** 眩晕 */
	STUN(13),
	/** 霸体 */
	PABODY(15),
	/** 采集 */
	GATHER(16),
	/** 无敌斩 */
	INVINCIBLECUT(17),
	/** 吟唱 */
	CASTING(18),
	/** 减速 */
	SLOW(19),
	/** 护盾 */
	SHIELD(20),
	/** 蓝包 */
	MPRESTORE(21),
	/** 无法移动 */
	CANNOT_MOVE(22),
	/** 血包恢复 */
	HPRESTORE(23),
	/** 回血 */
	HOT(24),
	/** 新手保护 */
	FRESHMAN(25),
	/** GM称号显示 */
	GM_NICKNAME(26),
	/** GM隐藏 */
	GM_HIDE(27),
	/** 同盟国是齐国 **/
	ALLIANCE_QI(28),
	/** 同盟国是楚国 **/
	ALLIANCE_CHU(29),
	/** 同盟国是赵国 **/
	ALLIANCE_ZHAO(30);
	
	
	private int effectId;

	private EffectId(int effectId) {
		this.effectId = effectId;
	}

	public int getEffectId() {
		return effectId;
	}
}