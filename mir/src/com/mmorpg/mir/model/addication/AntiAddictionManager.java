package com.mmorpg.mir.model.addication;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ServerConfigValue;

/**
 * 玩家沉迷容器,采用静态方法取玩家的沉迷收益
 * 
 * @author Kuang Hao
 * @since v1.0 2012-3-22
 * 
 */
@Component
public class AntiAddictionManager {

	// 是否开启防沉迷
	public static volatile boolean openAnti = false;
	@Autowired
	private ServerConfigValue serverConfigValue;

	private static AntiAddictionManager instance;

	@PostConstruct
	public void init() {
		// 是否默认开启防沉迷
		openAnti = serverConfigValue.getAntiAdd() == 1 ? true : false;
		instance = this;
	}

	public static AntiAddictionManager getInstance() {
		return instance;
	}

	public boolean isOpenAnti() {
		return openAnti;
	}

}
