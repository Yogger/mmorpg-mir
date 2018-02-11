package com.mmorpg.mir.model.country.resource;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CountryShopResource {
	@Id
	private String id;
	/** 奖励ID */
	private String chooserGroupId;
	/** 消耗 */
	private String[] actions;
	/** 购买条件 */
	private String[] buyCondtions;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChooserGroupId() {
		return chooserGroupId;
	}

	public void setChooserGroupId(String chooserGroupId) {
		this.chooserGroupId = chooserGroupId;
	}

	public String[] getActions() {
		return actions;
	}

	public void setActions(String[] actions) {
		this.actions = actions;
	}

	public String[] getBuyCondtions() {
		return buyCondtions;
	}

	public void setBuyCondtions(String[] buyCondtions) {
		this.buyCondtions = buyCondtions;
	}

}
