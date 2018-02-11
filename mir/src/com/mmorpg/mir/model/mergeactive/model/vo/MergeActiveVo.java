package com.mmorpg.mir.model.mergeactive.model.vo;

import java.util.ArrayList;
import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.mergeactive.manager.MergeActiveManager;
import com.mmorpg.mir.model.mergeactive.model.ConsumeCompete;
import com.mmorpg.mir.model.serverstate.ServerState;

public class MergeActiveVo {
	private String[] drawLog;

	private Map<Integer, String> frontShowCheapGiftBag;
	
	private ArrayList<String> canRecieves;
	
	private ConsumeCompete consumeCompete;
	
	private long mergeDate;

	private long nextExpressOpenTime;

	private long nextTempleOpenTime;

	public static MergeActiveVo valueOf(Player player) {
		MergeActiveVo vo = new MergeActiveVo();
		vo.setDrawLog(player.getMergeActive().getLoginGift().getDrawLog().keySet().toArray(new String[0]));
		vo.setFrontShowCheapGiftBag(player.getMergeActive().getCheapGiftBag().getFrontShowCheapGiftBag(player));
		vo.setNextExpressOpenTime(MergeActiveManager.getInstance().calNextOpenTime(
				MergeActiveManager.getInstance().getExpressOpenDays()));
		vo.setNextTempleOpenTime(MergeActiveManager.getInstance().calNextOpenTime(
				MergeActiveManager.getInstance().getTempleOpenDays()));
		vo.setCanRecieves(MergeActiveManager.getInstance().getConsumeCanRecievesReward(player));
		vo.setConsumeCompete(player.getMergeActive().getConsumeCompete());
		vo.setMergeDate(ServerState.getInstance().getMergeTime() == null ? 0L : ServerState.getInstance().getMergeTime().getTime());
		return vo;
	}

	public Map<Integer, String> getFrontShowCheapGiftBag() {
		return frontShowCheapGiftBag;
	}

	public void setFrontShowCheapGiftBag(Map<Integer, String> frontShowCheapGiftBag) {
		this.frontShowCheapGiftBag = frontShowCheapGiftBag;
	}

	public ArrayList<String> getCanRecieves() {
		return canRecieves;
	}

	public void setCanRecieves(ArrayList<String> canRecieves) {
		this.canRecieves = canRecieves;
	}

	public final ConsumeCompete getConsumeCompete() {
		return consumeCompete;
	}

	public final void setConsumeCompete(ConsumeCompete consumeCompete) {
		this.consumeCompete = consumeCompete;
	}

	public final long getMergeDate() {
		return mergeDate;
	}

	public final void setMergeDate(long mergeDate) {
		this.mergeDate = mergeDate;
	}


	public long getNextExpressOpenTime() {
		return nextExpressOpenTime;
	}

	public void setNextExpressOpenTime(long nextExpressOpenTime) {
		this.nextExpressOpenTime = nextExpressOpenTime;
	}

	public long getNextTempleOpenTime() {
		return nextTempleOpenTime;
	}

	public void setNextTempleOpenTime(long nextTempleOpenTime) {
		this.nextTempleOpenTime = nextTempleOpenTime;
	}

	public String[] getDrawLog() {
		return drawLog;
	}

	public void setDrawLog(String[] drawLog) {
		this.drawLog = drawLog;
	}
}
