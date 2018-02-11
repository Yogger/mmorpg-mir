package com.mmorpg.mir.model.item.packet;

public class CM_Combining {

	private String combiningId;
	
	private int addition;
	
	private boolean useGold;
	
	private int quantity;

	public String getCombiningId() {
    	return combiningId;
    }

	public void setCombiningId(String combiningId) {
    	this.combiningId = combiningId;
    }

	public int getAddition() {
    	return addition;
    }

	public void setAddition(int addition) {
    	this.addition = addition;
    }

	public boolean isUseGold() {
    	return useGold;
    }

	public void setUseGold(boolean useGold) {
    	this.useGold = useGold;
    }

	public int getQuantity() {
    	return quantity;
    }

	public void setQuantity(int quantity) {
    	this.quantity = quantity;
    }

}
