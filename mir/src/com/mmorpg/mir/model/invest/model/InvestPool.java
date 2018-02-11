package com.mmorpg.mir.model.invest.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.invest.packet.SM_Invest_Buy;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.New;

public class InvestPool {
	/** 各类投资信息 */
	private HashMap<Integer, Invest> invests;
	/** 上次增加各类型购买后累计登录天数 */
	private long lastAddTime;

	// 构造函数
	public static InvestPool valueOf() {
		InvestPool result = new InvestPool();
		result.invests = New.hashMap();
		result.lastAddTime = System.currentTimeMillis();
		return result;
	}

	// 业务方法
	@JsonIgnore
	public void buy(Player player, InvestType type) {
		Invest invest = Invest.valueOf(type);
		invests.put(type.getType(), invest);
		PacketSendUtility.sendPacket(player, SM_Invest_Buy.valueOf(type.getType(), invest));
	}

	@JsonIgnore
	public void addAccLoginDays() {
		if (!DateUtils.isToday(new Date(lastAddTime))) {
			for (Map.Entry<Integer, Invest> entry : invests.entrySet()) {
				entry.getValue().addAccLoginDays();
			}
			this.lastAddTime = System.currentTimeMillis();
		}
	}

	// getter - setter
	public HashMap<Integer, Invest> getInvests() {
		return invests;
	}

	public void setInvests(HashMap<Integer, Invest> invests) {
		this.invests = invests;
	}

	public long getLastAddTime() {
		return lastAddTime;
	}

	public void setLastAddTime(long lastAddTime) {
		this.lastAddTime = lastAddTime;
	}

}
