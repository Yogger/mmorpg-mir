package com.mmorpg.mir.model.capturetown.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.capturetown.config.TownConfig;
import com.mmorpg.mir.model.capturetown.manager.TownManager;
import com.mmorpg.mir.model.capturetown.packet.SM_Get_Specified_Town_Info;
import com.mmorpg.mir.model.capturetown.packet.SM_Town_Copy_Finish;
import com.mmorpg.mir.model.capturetown.resource.TownResource;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;

public class TownSchema {
	private HashMap<String, Town> townMap;

	private TownLog townLogger;

	@Transient
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	@JsonIgnore
	public void initANewTown(String townKey) {
		if (townMap == null) {
			townMap = new HashMap<String, Town>();
		}
		townMap.put(townKey, Town.valueOf(townKey));
	}

	@JsonIgnore
	public long getTownAccFeats(String key) {
		return townMap.get(key).getAccFeats();
	}

	@JsonIgnore
	public Map<Integer, Integer> getCountryCaptureTownNums() {
		try {
			lock.readLock().lock();
			Map<Integer, Integer> map = New.hashMap();
			for (Town town : townMap.values()) {
				if (town.getCaptureCountryValue() == 0) {
					continue;
				}
				if (!map.containsKey(town.getCaptureCountryValue())) {
					map.put(town.getCaptureCountryValue(), 1);
				} else {
					map.put(town.getCaptureCountryValue(), map.get(town.getCaptureCountryValue()) + 1);
				}
			}
			for (CountryId id : CountryId.values()) {
				if (!map.containsKey(id.getValue())) {
					map.put(id.getValue(), 0);
				}
			}
			return map;
		} finally {
			lock.readLock().unlock();
		}
	}

	@JsonIgnore
	public Map<Integer, Integer> getCountryCaptureRank() {
		Map<Integer, Integer> map = getCountryCaptureTownNums();
		List<Map.Entry<Integer, Integer>> entries = new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<Integer, Integer>>() {

			@Override
			public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
				return o2.getValue() - o1.getValue();
			}

		});
		Map<Integer, Integer> result = New.hashMap();
		int rank = 1;
		for (int i = 0; i < entries.size(); i++) {
			Integer selfMax = entries.get(i).getValue();
			if (i > 0) {
				if (!entries.get(i-1).getValue().equals(selfMax)) {
					rank++;
				}
			}
			result.put(entries.get(i).getKey(), rank);
		}
		return result;
	}
	
	public HashMap<String, Town> getTownMap() {
		return townMap;
	}

	public void setTownMap(HashMap<String, Town> townMap) {
		this.townMap = townMap;
	}

	@JsonIgnore
	public Map<String, Integer> getTownCountryInfo() {
		Map<String, Integer> map = New.hashMap();
		for (Town town : townMap.values()) {
			map.put(town.getId(), town.getCaptureCountryValue());
		}
		return map;
	}

	@JsonIgnore
	public int recievedTownReward(Player player) {
		try {
			lock.writeLock().lock();
			String key = player.getPlayerCountryHistory().getCaptureTownInfo().getCatpureTownKey();
			int accFeats = townMap.get(key).clearAccFeats();
			if (accFeats <= 0) {
				return 0;
			}
			Reward reward = Reward.valueOf().addCurrency(CurrencyType.FEATS, accFeats);
			RewardManager.getInstance().grantReward(player, reward,
					ModuleInfo.valueOf(ModuleType.CAPTURE_TOWN, SubModuleType.FEATS_RECIEVED_REWARD));
			player.getPlayerCountryHistory().getCaptureTownInfo().addDailyCount();
			TownManager.getInstance().updateCaptureTownEnt();
			return accFeats;
		} finally {
			lock.writeLock().unlock();
		}
	}

	@JsonIgnore
	public SM_Get_Specified_Town_Info getSpecifiedTownInfo(String key) {
		try {
			lock.readLock().lock();
			Town town = townMap.get(key);
			if (town == null) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}
			return SM_Get_Specified_Town_Info.valueOf(key, town.getCaptureName(), town.getCapturePlayerId() == null ? 0
					: town.getCapturePlayerId().get(), town.getCaptureCountryValue(), town.getAccFeats());
		} finally {
			lock.readLock().unlock();
		}

	}

	@JsonIgnore
	public boolean applyCaptureTown(Player player, String key) {
		try {
			lock.writeLock().lock();

			Town town = townMap.get(key);
			if (TownConfig.getInstance().getTownResource(key).getType() == TownType.PVE) {
				String originalKey = player.getPlayerCountryHistory().getCaptureTownInfo().getCatpureTownKey();
				Town originalTown = townMap.get(originalKey);
				originalTown.stopAccumulateFeats();
				originalTown.clearAccFeats();
				originalTown.clearTownOwner();
				player.getPlayerCountryHistory().getCaptureTownInfo().setCatpureTownKey(key);
				TownManager.getInstance().updateCaptureTownEnt();
				return true;
			} else if (town.catpureThisTown(player, 0L)) {
				String originalKey = player.getPlayerCountryHistory().getCaptureTownInfo().getCatpureTownKey();
				int originalAccFeats = 0;
				if (TownConfig.getInstance().getTownResource(originalKey).getType() != TownType.PVE) {
					Town originalTown = townMap.get(originalKey);
					originalAccFeats = originalTown.clearAccFeats();
					originalTown.stopAccumulateFeats();
					originalTown.clearTownOwner();
				}
				town.becomeTownOwner(player, originalAccFeats);
				town.startAccumulateFeats();
				player.getPlayerCountryHistory().getCaptureTownInfo().setCatpureTownKey(key);

				TownManager.getInstance().updateCaptureTownEnt();
				return true;
			}
			return false;
		} finally {
			lock.writeLock().unlock();
		}

	}

	public SM_Town_Copy_Finish captureTargetTown(Player player, String key, Player targetPlayer) {
		try {
			lock.writeLock().lock();
			Town town = townMap.get(key);
			if (town == null) {
				return SM_Town_Copy_Finish.valueOf(ResultType.FAIL_TIME_OUT.getValue(), targetPlayer, 0, key);
			}
			String originalKey = player.getPlayerCountryHistory().getCaptureTownInfo().getCatpureTownKey();
			String robbedKey = key;
			if (!town.catpureThisTown(player, targetPlayer.getObjectId())) {
				robbedKey = targetPlayer.getPlayerCountryHistory().getCaptureTownInfo().getCatpureTownKey();
			}
			TownResource originalResource = TownConfig.getInstance().getTownResource(originalKey);
			TownResource targetResource = TownConfig.getInstance().getTownResource(robbedKey);
			if (targetResource.getType() == TownType.PVE) {
				return SM_Town_Copy_Finish.valueOf(ResultType.WIN_NOT_GAIN_TOWN.getValue(), targetPlayer, 0, robbedKey);
			}
			Town originalTown = townMap.get(originalKey);
			Town targetTown = townMap.get(robbedKey);
			
			int gainFeats = robTargetTownAccFeats(player, targetTown);
			if (originalResource.getRank() <= targetResource.getRank()) {
				targetTown.setCapturePlayerId(new AtomicLong(targetPlayer.getObjectId()));
				addSelfLog(player, CaptureInfoType.ATTACK_SUCC_NOT_ACQ_TOWN, targetPlayer, gainFeats, originalKey, robbedKey);
				addSelfLog(targetPlayer, CaptureInfoType.BEENATTACKED_SUCC_NOT_LOST_TOWN, player, gainFeats, robbedKey,
						originalKey);
				TownManager.getInstance().updateCaptureTownEnt();
				return SM_Town_Copy_Finish.valueOf(ResultType.WIN_NOT_GAIN_TOWN.getValue(), targetPlayer, gainFeats,
						robbedKey);
			} else {
				int originalAccFeats = 0;
				if (originalResource.getType() == TownType.PVP) {
					originalAccFeats = originalTown.clearAccFeats();
					originalTown.stopAccumulateFeats();
					originalTown.becomeTownOwner(targetPlayer, 0);
					originalTown.startAccumulateFeats();
				}
				targetTown.becomeTownOwner(player, originalAccFeats);
				targetTown.startAccumulateFeats();
				
				player.getPlayerCountryHistory().getCaptureTownInfo().setCatpureTownKey(robbedKey);
				targetPlayer.getPlayerCountryHistory().getCaptureTownInfo().setCatpureTownKey(originalKey);
				addSelfLog(player, CaptureInfoType.ATTACK_SUCC_ACQ_TOWN, targetPlayer, gainFeats, originalKey, robbedKey);
				CaptureInfoType logType = (originalResource.getType() == TownType.PVP ? CaptureInfoType.BEENATTACKED_SUCC_ACQ_TOWN : 
																CaptureInfoType.BEENATTACKED_SUCC_LOST_TOWN);
				addSelfLog(targetPlayer, logType, player, gainFeats, robbedKey, originalKey);
				addCountryLog(player, targetPlayer, robbedKey);
				PlayerManager.getInstance().updateIfOffline(targetPlayer);
				doNotice(targetResource, player, targetPlayer);
				TownManager.getInstance().updateCaptureTownEnt();
				return SM_Town_Copy_Finish.valueOf(ResultType.WIN_GAIN_TOWN.getValue(), targetPlayer, gainFeats, robbedKey);
			}
			
		} finally {
			lock.writeLock().unlock();
		}

	}
	
	private int robTargetTownAccFeats(Player player, Town town) {
		int gainFeats = town.clearAccFeats();
		if (gainFeats > 0) {
			Reward reward = Reward.valueOf().addCurrency(CurrencyType.FEATS, gainFeats);
			RewardManager.getInstance().grantReward(player, reward,
					ModuleInfo.valueOf(ModuleType.CAPTURE_TOWN, SubModuleType.TOWN_ROB_FEATS));
		}
		return gainFeats;
	}

	private void doNotice(TownResource targetResource, Player player, Player targetPlayer) {
		if (targetResource.getRank() <= 5) {
			I18nUtils utils = I18nUtils.valueOf("40120");
			utils.addParm("name", I18nPack.valueOf(player.getName()));
			utils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			utils.addParm("targetname", I18nPack.valueOf(targetPlayer.getName()));
			utils.addParm("targetCountry", I18nPack.valueOf(targetPlayer.getCountry().getName()));
			utils.addParm("rank", I18nPack.valueOf(targetResource.getRank()));
			utils.addParm("city", I18nPack.valueOf(targetResource.getCityName()));
			ChatManager.getInstance().sendSystem(11001, utils, null);
		}
	}

	@JsonIgnore
	public long getTownOwnerPlayerId(String key) {
		AtomicLong id = townMap.get(key).getCapturePlayerId();
		return id == null ? 0L : id.longValue();
	}

	@JsonIgnore
	public int getTownOwnerRole(String key) {
		return townMap.get(key).getCaptureRole();
	}

	public TownLog getTownLogger() {
		return townLogger;
	}

	public void setTownLogger(TownLog townLogger) {
		this.townLogger = townLogger;
	}

	public ArrayList<SelfCaptureInfo> getMyFightInfo(Player player) {
		LinkedList<SelfCaptureInfo> arr = townLogger.getMySelfLogs(player);
		if (arr == null) {
			return new ArrayList<SelfCaptureInfo>();
		}
		return new ArrayList<SelfCaptureInfo>(arr);
	}

	public ArrayList<CountryCaptureInfo> getCountryCaptureInfo(Player player) {
		return new ArrayList<CountryCaptureInfo>(townLogger.getCountryCaptureLogs());
	}

	public void addSelfLog(Player attacker, CaptureInfoType type, Player target, long feats, String from, String to) {
		townLogger.addSelfCaptureInfo(attacker, type, target, feats, from, to);
	}

	public void addCountryLog(Player attacker, Player target, String key) {
		townLogger.addCountryCaptureLogs(attacker, target, key);
	}

	public void initAllTown() {
		for (Town town : townMap.values()) {
			if (town.getCapturePlayerId() != null && town.getCapturePlayerId().get() != 0L) {
				town.startAccumulateFeats();
			}
		}
	}
}
