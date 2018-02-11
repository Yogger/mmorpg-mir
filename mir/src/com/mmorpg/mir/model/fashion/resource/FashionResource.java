package com.mmorpg.mir.model.fashion.resource;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class FashionResource {
	@Id
	private int id;

	/** 第一次获得的经验 */
	private int firstExp;

	/** 增加的经验值 */
	private int exp;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getFirstExp() {
		return firstExp;
	}

	public void setFirstExp(int firstExp) {
		this.firstExp = firstExp;
	}

}
