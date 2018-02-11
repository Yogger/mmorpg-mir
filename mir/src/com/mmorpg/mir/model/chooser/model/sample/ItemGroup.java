package com.mmorpg.mir.model.chooser.model.sample;

import java.util.List;

import com.windforce.common.utility.SelectRandom;

/**
 * 
 * 结果组
 * 
 * @author Kuang Hao
 * @since v1.0 2012-12-20
 * 
 */
public class ItemGroup {
	/** 单项集合 */
	private List<Item> items;
	/** 筛选结果数量 */
	private int resultCount;

	/**
	 * 赛选
	 * 
	 * @return
	 */
	public List<String> selectResult() {
		SelectRandom<String> selector = new SelectRandom<String>();
		for (Item item : items) {
			selector.addElement(item.getValue(), item.getWeight());
		}
		return selector.run(resultCount);
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public int getResultCount() {
		return resultCount;
	}

	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}

}
