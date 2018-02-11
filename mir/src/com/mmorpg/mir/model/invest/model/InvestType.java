package com.mmorpg.mir.model.invest.model;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.log.SubModuleType;

public enum InvestType {
	/** 经验投资 */
	EXP(0, ManagedErrorCode.EXP_INVEST_HAS_BUY, SubModuleType.INVEST_EXP_REWARD, SubModuleType.INVEST_EXP_BUY),
	/** 铜钱投资 */
//	COPPER(1, ManagedErrorCode.COPPER_INVEST_HAS_BUY, SubModuleType.INVEST_COPPER_REWARD,
//			SubModuleType.INVEST_COPPER_BUY),
	/** 复活丹投资 */
//	RELIVEDAN(2, ManagedErrorCode.RELIVEDAN_INVEST_HAS_BUY, SubModuleType.INVEST_RELIVEDAN_REWARD,
//			SubModuleType.INVEST_RELIVEDAN_BUY),
	/** 荣誉投资 */
	HONOR(3, ManagedErrorCode.HONOR_INVEST_HAS_BUY, SubModuleType.INVEST_HONOR_REWARD, SubModuleType.INVEST_HONOR_BUY),
	/** 礼金投资 */
	GIFT(4, ManagedErrorCode.GIFT_INVEST_HAS_BUY, SubModuleType.INVEST_GIFT_REWARD, SubModuleType.INVEST_GIFT_BUY);

	private int type;

	private int errorCode;

	private SubModuleType rewardSubmodule;

	private SubModuleType actSubmodule;

	private InvestType(int type, int errorCode, SubModuleType rewardSubmodule, SubModuleType actSubmodule) {
		this.type = type;
		this.errorCode = errorCode;
		this.rewardSubmodule = rewardSubmodule;
		this.actSubmodule = actSubmodule;
	}

	public static InvestType typeOf(int type) {
		for (InvestType t : InvestType.values()) {
			if (t.type == type) {
				return t;
			}
		}
		throw new IllegalArgumentException("非法InvestType类型 ：" + type);
	}

	public int getErrorCode() {
		return errorCode;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public SubModuleType getRewardSubmodule() {
		return rewardSubmodule;
	}

	public void setRewardSubmodule(SubModuleType rewardSubmodule) {
		this.rewardSubmodule = rewardSubmodule;
	}

	public SubModuleType getActSubmodule() {
		return actSubmodule;
	}

	public void setActSubmodule(SubModuleType actSubmodule) {
		this.actSubmodule = actSubmodule;
	}

}
