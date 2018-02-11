package com.mmorpg.mir.model.commonactivity.model;

public class CommonIdentifyTreasure {
	private int version;
	
	private int luckValue;
	
	public static CommonIdentifyTreasure valueOf(int version){
		CommonIdentifyTreasure treasure = new CommonIdentifyTreasure();
		treasure.version = version;
		return treasure;
	}
	
	public void reset(CommonIdentifyTreasureServer treasureServer){
		version = treasureServer.getVersion();
		luckValue = 0;
	}

	public void addLuckValue(int perLuckValue){
		this.luckValue += perLuckValue;
	}
	
	public void clearLuckValue(){
		this.luckValue = 0;
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getLuckValue() {
		return luckValue;
	}

	public void setLuckValue(int luckValue) {
		this.luckValue = luckValue;
	}
}
