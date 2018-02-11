package com.mmorpg.mir.model.rank.packet;

public class CM_Get_Rank {

	private int rankType;
	
	private int page;

	public int getRankType() {
    	return rankType;
    }

	public void setRankType(int rankType) {
    	this.rankType = rankType;
    }

	public int getPage() {
    	return page;
    }

	public void setPage(int page) {
    	this.page = page;
    }

}
