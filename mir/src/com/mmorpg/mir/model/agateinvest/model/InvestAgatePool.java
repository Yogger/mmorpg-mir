package com.mmorpg.mir.model.agateinvest.model;

import java.util.HashMap;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.agateinvest.packet.SM_Agate_Invest_Buy;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.New;

public class InvestAgatePool {
	/** 各类投资信息 */
	private HashMap<Integer, InvestAgate> invests;
	/** 上次增加各类型购买后累计登录天数 */
	private long lastAddTime;

	// 构造函数
	public static InvestAgatePool valueOf() {
		InvestAgatePool result = new InvestAgatePool();
		result.invests = New.hashMap();
		result.lastAddTime = System.currentTimeMillis();
		return result;
	}

	// 业务方法
	@JsonIgnore
	public void buy(Player player, InvestAgateType type) {
		InvestAgate invest = InvestAgate.valueOf(type);
		invests.put(type.getType(), invest);
		PacketSendUtility.sendPacket(player, SM_Agate_Invest_Buy.valueOf(type.getType(), invest));
	}

	// getter - setter
	public HashMap<Integer, InvestAgate> getInvests() {
		return invests;
	}

	public void setInvests(HashMap<Integer, InvestAgate> invests) {
		this.invests = invests;
	}

	public long getLastAddTime() {
		return lastAddTime;
	}

	public void setLastAddTime(long lastAddTime) {
		this.lastAddTime = lastAddTime;
	}

}
