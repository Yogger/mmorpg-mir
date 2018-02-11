package com.mmorpg.mir.model.rank.packet;

import java.util.AbstractList;

import com.mmorpg.mir.model.rank.model.RankRow;

public class SM_Get_Rank {
	
	private int total;
	private int type;
	private AbstractList<RankRow> ranks;
	private int myRank;

	public static SM_Get_Rank valueOf(int total, int type, AbstractList<RankRow> results, int myRank) {
		SM_Get_Rank rank = new SM_Get_Rank();
		rank.total = total;
		rank.type = type;
		rank.ranks = results;
		rank.myRank = myRank;
		return rank;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public AbstractList<RankRow> getRanks() {
    	return ranks;
    }

	public void setRanks(AbstractList<RankRow> ranks) {
    	this.ranks = ranks;
    }

	public int getTotal() {
    	return total;
    }

	public void setTotal(int total) {
    	this.total = total;
    }

	public int getMyRank() {
    	return myRank;
    }

	public void setMyRank(int myRank) {
    	this.myRank = myRank;
    }

}
