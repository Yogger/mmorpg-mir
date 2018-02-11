package com.mmorpg.mir.model.gang.model.log;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gang.model.Member;

@Component
public class GangLogFactory {

	private static GangLogFactory instance;

	@PostConstruct
	public void init() {
		instance = this;
	}

	public static GangLogFactory getInstance() {
		return instance;
	}

	public GangLog joinLog(String name) {
		GangLog log = new GangLog();
		log.setType(GangLogType.JOIN.name());
		log.setTime(System.currentTimeMillis());
		log.getContexts().put("name", name);
		return log;
	}

	public GangLog createLog(String name) {
		GangLog log = new GangLog();
		log.setType(GangLogType.CREATE.name());
		log.setTime(System.currentTimeMillis());
		log.getContexts().put("name", name);
		return log;
	}

	public GangLog expelLog(Player player) {
		GangLog log = new GangLog();
		log.setType(GangLogType.EXPEL.name());
		log.setTime(System.currentTimeMillis());
		log.getContexts().put("name", player.getName());
		return log;
	}

	public GangLog quitLog(Player player) {
		GangLog log = new GangLog();
		log.setType(GangLogType.QUIT.name());
		log.setTime(System.currentTimeMillis());
		log.getContexts().put("name", player.getName());
		return log;
	}

	public GangLog transferLog(Player oldMaster, Member member) {
		GangLog log = new GangLog();
		log.setType(GangLogType.TRANSFER.name());
		log.setTime(System.currentTimeMillis());
		log.getContexts().put("oldMaster", oldMaster.getName());
		log.getContexts().put("newMaster", member.getName());
		return log;
	}

}
