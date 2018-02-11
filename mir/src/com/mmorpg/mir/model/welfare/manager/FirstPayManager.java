package com.mmorpg.mir.model.welfare.manager;


import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.windforce.common.resource.anno.Static;

@Component
public class FirstPayManager implements IFirstPayManager{

	private static FirstPayManager instance;

	@Static("PUBLIC:FIRST_PAY_REWARD")
	public ConfigValue<String> FIRST_PAY_REWARD;

	@SuppressWarnings("unused")
	@PostConstruct
	private void init() {
		instance = this;
	}
	
	public String getVipFirstPayReward() {
		return FIRST_PAY_REWARD.getValue();
	}

	public static FirstPayManager getInstance() {
		return instance;
	}
}
