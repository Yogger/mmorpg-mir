package com.mmorpg.mir.model.shop.resouce;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class ShopResource {
	@Id
	private String id;
	/** 商店id */
	private int shopId;
	/** 需要材料 */
	private String[] actions;
	/** 购买条件 */
	private String[] conditions;
	/** 值选择器 */
	private String chooserGrounpId;
	/** VIP购买的数量 */
	private int buyCount[];
	/** VIP总共购买的数量 */
	private int totalCount[];
	/** 对应等级今日购买的数量的chooserGroupId */
	private String levelBuyCountChooserGroup;
	/** 对应等级进入购买的数量的chooserGroupId */
	private String levelTotalCountChooserGroup;
	/** 后台运营显示的Id */
	private String i18name;
	/** 商店刷新的类型 */
	private int type;

	public String getId() {
		return id;
	}

	@JsonIgnore
	public boolean isEnoughCount(int count, int vipLevel) {
		if (this.buyCount[vipLevel] == -1) {
			return true;
		}
		return buyCount[vipLevel] >= count;
	}

	@JsonIgnore
	public boolean isEnoughTotalCount(int count, int vipLevel) {
		if (this.totalCount[vipLevel] == -1) {
			return true;
		}
		return totalCount[vipLevel] >= count;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChooserGrounpId() {
		return chooserGrounpId;
	}

	public void setChooserGrounpId(String chooserGrounpId) {
		this.chooserGrounpId = chooserGrounpId;
	}

	public int[] getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(int[] buyCount) {
		this.buyCount = buyCount;
	}

	public int[] getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int[] totalCount) {
		this.totalCount = totalCount;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public String[] getConditions() {
		return conditions;
	}

	public void setConditions(String[] conditions) {
		this.conditions = conditions;
	}

	public String[] getActions() {
		return actions;
	}

	public void setActions(String[] actions) {
		this.actions = actions;
	}

	public String getI18name() {
		return i18name;
	}

	public void setI18name(String i18name) {
		this.i18name = i18name;
	}

	public String getLevelBuyCountChooserGroup() {
		return levelBuyCountChooserGroup;
	}

	public void setLevelBuyCountChooserGroup(String levelBuyCountChooserGroup) {
		this.levelBuyCountChooserGroup = levelBuyCountChooserGroup;
	}

	public String getLevelTotalCountChooserGroup() {
		return levelTotalCountChooserGroup;
	}

	public void setLevelTotalCountChooserGroup(String levelTotalCountChooserGroup) {
		this.levelTotalCountChooserGroup = levelTotalCountChooserGroup;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@JsonIgnore
	public boolean isSpecialType() { 
		return type > 0;
	}
}
