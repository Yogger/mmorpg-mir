package com.mmorpg.mir.model;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.horse.service.HorseService;
import com.mmorpg.mir.model.nickname.manager.NicknameManager;
import com.mmorpg.mir.model.operator.manager.OperatorManager;
import com.mmorpg.mir.model.respawn.ReliveService;
import com.mmorpg.mir.model.welfare.manager.GiftCollectManage;

@Component
public class SpringComponentStation {

	@Autowired
	private ReliveService reliveService;

	@Autowired
	private NicknameManager nicknameManager;

	@Autowired
	private OperatorManager operatorManager;

	@Autowired
	private GiftCollectManage giftCollectManager;

	@Autowired
	private HorseService horseService;
	
	private static SpringComponentStation self;

	@PostConstruct
	public void init() {
		self = this;
	}

	public static ReliveService getReliveService() {
		return self.reliveService;
	}

	public static NicknameManager getNicknameManager() {
		return self.nicknameManager;
	}

	public static OperatorManager getOperatorManager() {
		return self.operatorManager;
	}

	public static GiftCollectManage getGiftCollectManager() {
		return self.giftCollectManager;
	}

	public static HorseService getHorseService(){
		return self.horseService;
	}
	
}
