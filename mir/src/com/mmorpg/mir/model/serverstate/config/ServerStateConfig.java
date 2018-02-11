package com.mmorpg.mir.model.serverstate.config;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.windforce.common.resource.anno.Static;

@Component
public class ServerStateConfig {

	@Static("SERVER:LIMIT_MAP")
	public ConfigValue<Map<String, Integer>> SERVER_LIMIT_MAP;
	
	@Static("SERVER:LEVEL_REWARD_MAIL_TITLE")
	public ConfigValue<String[]> LEVEL_REWARD_MAIL_TITLE;
	
	@Static("SERVER:LEVEL_REWARD_MAIL_CONTENT")
	public ConfigValue<String[]> LEVEL_REWARD_MAIL_CONTENT;
	
	@Static("SERVER:LEVEL_REWARD_ID")
	public ConfigValue<String[]> LEVEL_REWARD_ID;
	
	@Static("SERVER:LEVEL_REWARD_BASE")
	public ConfigValue<Integer> LEVEL_REWARD_BASE;

	public static ServerStateConfig INSTANCE;
	
	@PostConstruct
	void init() {
		INSTANCE = this;
	}
	
	public static ServerStateConfig getInstance() {
		return INSTANCE;
	}
	
}
