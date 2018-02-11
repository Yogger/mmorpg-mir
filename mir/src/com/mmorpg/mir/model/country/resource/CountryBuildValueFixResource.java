package com.mmorpg.mir.model.country.resource;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CountryBuildValueFixResource {

	@Id
	private int id;
	/** 标准值 */
	private int standValue;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStandValue() {
		return standValue;
	}

	public void setStandValue(int standValue) {
		this.standValue = standValue;
	}

}
