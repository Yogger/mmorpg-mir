package com.mmorpg.mir.model.commonactivity.model;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.commonactivity.CommonActivityConfig;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;

public class CommonRecollectActive {
	private Map<Integer, RecollectEventLog> eventLogData = new HashMap<Integer, RecollectEventLog>();
	
	public static CommonRecollectActive valueOf(Player player) {
		CommonRecollectActive c = new CommonRecollectActive();
		for (RecollectType type : RecollectType.values()) {
			c.eventLogData.put(type.getValue(), RecollectEventLog.valueOf(player, type));
		}
		return c;
	}
	
	public Map<Integer, RecollectEventLog> getEventLogData() {
		return eventLogData;
	}

	public void setEventLogData(Map<Integer, RecollectEventLog> eventLogData) {
		this.eventLogData = eventLogData;
	}

	@JsonIgnore
	public void logDoneStatus(Player player, Long dayKey, RecollectType type, int count) {
		RecollectEventLog log = eventLogData.get(type.getValue());
		if (log == null) {
			log = RecollectEventLog.valueOf(player, type);
			eventLogData.put(type.getValue(), log);
		}
		log.addFinishedCount(dayKey, count);
	}
	
	@JsonIgnore
	public void logClawbackStatus(Player player, Long dayKey, RecollectType type, int count) {
		RecollectEventLog log = eventLogData.get(type.getValue());
		if (log == null) {
			log = RecollectEventLog.valueOf(player, type);
			eventLogData.put(type.getValue(), log);
		}
		log.addClawbackCount(count, dayKey);
	}
	
	@JsonIgnore
	public ClawbackStatus getClawStatus(Player player, RecollectType type) {
		RecollectEventLog log = eventLogData.get(type.getValue());
		String opmkId = CommonActivityConfig.getInstance().getCurrentCommonRecollectResource(type).getOpmkId();
		if (log == null) {
			return ClawbackStatus.CANNOT_CLAW;
		}
		if (!ModuleOpenManager.getInstance().isOpenByKey(player, opmkId)) {
			return ClawbackStatus.MODULE_NOT_OPEN;
		}
		if (log.hasRecollected()) {
			return ClawbackStatus.CLAWBACKED;
		}
		return ClawbackStatus.CAN_CLAW;
	}
	
	@JsonIgnore
	public int getCanClawbackCount(Player player, RecollectType type) {
		RecollectEventLog log = eventLogData.get(type.getValue());
		if (log == null) {
			return 0;
		}
		return log.getCanRecolllectCount(player);
	}
	
	@JsonIgnore
	public int getAllCanClawBackCount(Player player) {
		int all = 0;
		for (RecollectEventLog log : eventLogData.values()) {
			all += log.getCanRecolllectCount(player);
		}
		return all;
	}
	
	@JsonIgnore
	public void recollectAll(Player player, RecollectType type) {
		RecollectEventLog log = eventLogData.get(type.getValue());
		log.recollectAll();
	}
	
}

