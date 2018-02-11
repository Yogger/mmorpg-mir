package com.mmorpg.mir.model.welfare.manager;


import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.FormulaParmsUtil;
import com.mmorpg.mir.model.formula.Formula;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.packet.SM_GiftCollect_Open;
import com.windforce.common.resource.anno.Static;

@Component
public class GiftCollectManage implements IGiftCollectManager {

	private static GiftCollectManage INSTANCE;

	@Static("GIFT:GIFT_LEFT_SALARY")
	public Formula LEFT_FORMULA;

	@Static("GIFT:GIFT_ACCUMULA_SALARY")
	public Formula ACCUMULA_FORMULA;

	@PostConstruct
	void init() {
		INSTANCE = this;
	}

	public static GiftCollectManage getInstance() {
		return INSTANCE;
	}

	public SM_GiftCollect_Open getGiftCollect_Open(Player player){
		player.getWelfare().getGiftCollect().refreshDeprect();
		return SM_GiftCollect_Open.valueOf(getLeftAmount(player), getAccumulaCount(player));
	}


	public  Long getLeftAmount(Player player){
		long todayAmount = player.getWelfare().getGiftCollect().getAmount();
		Long leftAmout = (Long) FormulaParmsUtil.valueOf(LEFT_FORMULA).addParm("todayAmount", todayAmount).getValue();
		return leftAmout;
	}
	
	public Long getAccumulaCount(Player player){
		Long totalAmount = player.getWelfare().getGiftCollect().getTotalAmount();
		Long accumulaCount = (Long) FormulaParmsUtil.valueOf(ACCUMULA_FORMULA).addParm("totalAmount", totalAmount)
				.getValue();
		return accumulaCount;
	}
}
