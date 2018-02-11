package com.mmorpg.mir.model.chooser.model.sample;

/**
 * 只选择器的单项F
 * 
 * @author Kuang Hao
 * @since v1.0 2012-12-20
 * 
 */
public class Item {
	/** 值 */
	private String value;
	/** 选择的权值 */
	private int weight;

	public static Item valueOf(String value, int weight) {
		Item item = new Item();
		item.value = value;
		item.weight = weight;
		return item;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

}
