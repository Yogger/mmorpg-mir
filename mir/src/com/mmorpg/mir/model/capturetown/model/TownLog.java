package com.mmorpg.mir.model.capturetown.model;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import com.mmorpg.mir.model.capturetown.config.TownConfig;
import com.mmorpg.mir.model.capturetown.manager.TownManager;
import com.mmorpg.mir.model.gameobjects.Player;

public class TownLog {

	private ConcurrentHashMap<Long, LinkedList<SelfCaptureInfo>>  playerCaptureLogs = new ConcurrentHashMap<Long, LinkedList<SelfCaptureInfo>>();
	
	private LinkedList<CountryCaptureInfo> countryCaptureLogs = new LinkedList<CountryCaptureInfo>();
	
	public synchronized void addSelfCaptureInfo(Player player, CaptureInfoType type, Player enemy, long feats, String from, String to) {
		SelfCaptureInfo info = SelfCaptureInfo.valueOf(type.getValue(), feats, enemy, from, to);
		LinkedList<SelfCaptureInfo> infos = playerCaptureLogs.get(player.getObjectId());
		if (infos == null) {
			infos = new LinkedList<SelfCaptureInfo>();
			playerCaptureLogs.put(player.getObjectId(), infos);
		}
		if (infos.size() >= TownConfig.getInstance().SELF_CAPTURE_LOG_MAX_SIZE.getValue()) {
			infos.removeFirst();
		}
		infos.addLast(info);
		TownManager.getInstance().updateCaptureTownEnt();
	}
	
	public synchronized void addCountryCaptureLogs(Player player, Player enemy, String key) {
		CountryCaptureInfo info = CountryCaptureInfo.valueOf(player, enemy, key);
		if (countryCaptureLogs.size() >= TownConfig.getInstance().COUNTRY_CAPTURE_LOG_MAX_SIZE.getValue()) {
			countryCaptureLogs.removeFirst();
		}
		countryCaptureLogs.addLast(info);
		TownManager.getInstance().updateCaptureTownEnt();
	}

	public ConcurrentHashMap<Long, LinkedList<SelfCaptureInfo>> getPlayerCaptureLogs() {
		return playerCaptureLogs;
	}

	public void setPlayerCaptureLogs(ConcurrentHashMap<Long, LinkedList<SelfCaptureInfo>> playerCaptureLogs) {
		this.playerCaptureLogs = playerCaptureLogs;
	}

	public synchronized LinkedList<CountryCaptureInfo> getCountryCaptureLogs() {
		return countryCaptureLogs;
	}

	public void setCountryCaptureLogs(LinkedList<CountryCaptureInfo> countryCaptureLogs) {
		this.countryCaptureLogs = countryCaptureLogs;
	}
	
	public synchronized LinkedList<SelfCaptureInfo> getMySelfLogs(Player player) {
		return playerCaptureLogs.get(player.getObjectId());
	}
	
	public static void main(String[] args) {
	}
	
}
