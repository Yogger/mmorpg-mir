package com.mmorpg.mir.model.country.model;

/**
 * 储君任务类型
 * 
 * @author 37.com
 * 
 */
public enum ReserveTaskEnum {
	/** 国家副本 */
	COUNTRY_BOSS(1),
	/** 砍国旗 */
	KILL_FLAG(2),
	;

	private int code;

	public static ReserveTaskEnum typeOf(int code) {
		for (ReserveTaskEnum taskType : ReserveTaskEnum.values()) {
			if (taskType.getCode() == code) {
				return taskType;
			}
		}
		throw new IllegalArgumentException("非法储君任务类型【" + code + "】");
	}

	private ReserveTaskEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
