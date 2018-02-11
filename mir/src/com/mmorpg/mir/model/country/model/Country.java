package com.mmorpg.mir.model.country.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.persistence.Transient;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.country.entity.CountryEnt;
import com.mmorpg.mir.model.country.event.WeakCountryEvent;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.countryact.CountryFlagQuestType;
import com.mmorpg.mir.model.country.model.countryact.CountryWarEvent;
import com.mmorpg.mir.model.country.model.log.FeteLog;
import com.mmorpg.mir.model.country.model.vo.CountryFlagQuestVO;
import com.mmorpg.mir.model.country.packet.SM_Country_Level;
import com.mmorpg.mir.model.country.packet.SM_Get_Country_Unitiy_Buff_Floor;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effecttemplate.TraitorEffect;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;

/**
 * 国家
 * 
 * @author Kuang Hao
 * @since v1.0 2014-9-15
 * 
 */
public class Country {
	/** 国家频道，根据情况需要是否提取成CONFIGVALUE */
	public static final int channalId = 6;
	public static final StatEffectId UNITY_BUFF_ID = StatEffectId
			.valueOf("Unity-buff", StatEffectType.UNITY_BUFF, true);
	/** 国家 */
	private CountryId id;
	/** 朝廷 */
	private Court court;
	/** 国旗 */
	private CountryFlag countryFlag;
	/** 科技 */
	@Deprecated
	private Technology technology;

	/** TODO 这个版本暂时不打开 */
	private NewTechnology newTechnology;

	/** 外交 */
	private Diplomacy diplomacy;
	/** 国库 */
	private Coppers coppers;
	/** 国家商店 */
	private CountryShop countryShop;
	/** 仓库 */
	private CountryStorge countryStorge;
	/** 禁言 */
	private ConcurrentHashMap<Long, ForbidChat> forbidChats;
	/** 太庙 */
	private Temple temple;
	/** 上一次家族战胜利的帮会 */
	private long lastWinGangId;
	/** 国家任务 */
	private CountryQuest countryQuest;
	/** 上香日志 */
	private FeteLog feteLog;
	/** 强弱国的档次 */
	private volatile int countryLevel;
	/** 标记为内奸的玩家 */
	@Deprecated
	private ConcurrentHashMap<Long, TraitorPlayer> traitorMap;

	private ConcurrentHashMap<Long, TraitorPlayerFix> traitorMapFixs;

	/** 储君 */
	private ReserveKing reserveKing;

	@Transient
	private CountryEnt ent;

	/** 国力 */
	private int gdp;

	/** 家族战开启次数 */
	private int gangWarCount;

	/** 众志成城buff层数 */
	private volatile int buffFloor;

	private ReentrantLock lock = new ReentrantLock();

	@Transient
	private ConcurrentHashMap<Integer, Long> guardNoticeCD = new ConcurrentHashMap<Integer, Long>();
	@Transient
	private ConcurrentHashMap<Long, Player> civils = new ConcurrentHashMap<Long, Player>();
	@Transient
	private ConcurrentHashMap<String, Player> nameCivils = new ConcurrentHashMap<String, Player>();

	public void init() {
		court = Court.valueOf(this);
		countryFlag = CountryFlag.valueOf();
		countryShop = CountryShop.valueOf();
		countryStorge = CountryStorge.valueOf();
		technology = Technology.valueOf();
		coppers = Coppers.valueOf();
		forbidChats = new ConcurrentHashMap<Long, ForbidChat>();
		diplomacy = new Diplomacy();
		countryQuest = new CountryQuest();
		temple = new Temple();
		feteLog = FeteLog.valueOf();
		traitorMap = new ConcurrentHashMap<Long, TraitorPlayer>();
		traitorMapFixs = new ConcurrentHashMap<Long, TraitorPlayerFix>();
		reserveKing = ReserveKing.valueOf();
		newTechnology = NewTechnology.valueOf(ConfigValueManager.getInstance().COUNTRY_TECHNOLOGY_FLAG_INIT_COUNT
				.getValue());
		buffFloor = 0;
	}

	@JsonIgnore
	public void addBuffFloor(int count) {
		getLock().lock();
		try {
			buffFloor += count;
			Stat[][] stats = ConfigValueManager.getInstance().getCountryUnityBuff();
			if (buffFloor > stats.length) {
				buffFloor = stats.length;
			}
			sendPackAll(SM_Get_Country_Unitiy_Buff_Floor.valueOf(this.buffFloor));
		} finally {
			getLock().unlock();
		}
	}

	@JsonIgnore
	public void clearBuffFloor() {
		getLock().lock();
		try {
			buffFloor = 0;
			sendPackAll(SM_Get_Country_Unitiy_Buff_Floor.valueOf(this.buffFloor));
		} finally {
			getLock().unlock();
		}
	}

	@JsonIgnore
	public Player getCivil(String name) {
		return nameCivils.get(name);
	}

	@JsonIgnore
	public void registerCivil(Player player) {
		civils.putIfAbsent(player.getObjectId(), player);
		nameCivils.putIfAbsent(player.getRealName(), player);
	}

	@JsonIgnore
	public Map<String, Player> getNameCivils() {
		return nameCivils;
	}

	@JsonIgnore
	public void unRegisterCivil(Player player) {
		civils.remove(player.getObjectId());
		nameCivils.remove(player.getRealName());
	}

	@JsonIgnore
	public boolean isOffical(Player target) {
		return court.getOfficials().containsKey(target.getObjectId());
	}

	@JsonIgnore
	public boolean isOffical(Long pid) {
		return court.getOfficials().containsKey(pid) || court.isGurad(pid);
	}

	@JsonIgnore
	public Official getPlayerOffical(long playerId) {
		return court.getOfficials().get(playerId);
	}

	@JsonIgnore
	public Player getKing() {
		Official kingOfficial = court.getKing();
		if (kingOfficial == null) {
			return null;
		}
		return PlayerManager.getInstance().getPlayer(kingOfficial.getPlayerId());
	}

	@JsonIgnore
	public String getName() {
		return ConfigValueManager.getInstance().COUNTRY_NAMES.getValue().get(id.getValue() + "");
	}

	/**
	 * 获取玩家的官职，包含平民和禁军
	 * 
	 * @return
	 */
	@JsonIgnore
	public int getPlayerAllOffical(long playerId) {
		if (court.getOfficials().get(playerId) != null) {
			return court.getOfficials().get(playerId).getOfficial().getValue();
		}
		if (court.getKingGuards().contains(KingGurad.valueOf(playerId, 0))) {
			return CountryOfficial.GUARD.getValue();
		}
		return CountryOfficial.CITIZEN.getValue();
	}

	@JsonIgnore
	public List<Official> getOfficalType(CountryOfficial type) {
		List<Official> officals = New.arrayList();
		for (Official offical : court.getOfficials().values()) {
			if (offical.getOfficial() == type) {
				officals.add(offical);
			}
		}
		return officals;
	}

	@JsonIgnore
	public void forbidChat(long playerId, long endTime) {
		if (forbidChats.containsKey(playerId)) {
			return;
		}
		forbidChats.put(playerId, ForbidChat.valueOf(playerId, System.currentTimeMillis() + endTime));
	}

	@JsonIgnore
	public boolean isForbidchat(long playerId) {
		if (forbidChats.containsKey(playerId)) {
			if (forbidChats.get(playerId).end()) {
				forbidChats.remove(playerId);
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	public boolean authority(Player player, String id) {
		if (!player.getCountry().getCourt().authority(player, id)) {
			throw new ManagedException(ManagedErrorCode.COUNTRY_AUTHORITY_ERROR);
		}
		return true;
	}

	public void sendPacket(Object object, CountryOfficial... countryOfficials) {
		if (ArrayUtils.isEmpty(countryOfficials)) {
			for (long id : court.getOfficials().keySet()) {
				if (SessionManager.getInstance().isOnline(id)) {
					Player player = PlayerManager.getInstance().getPlayer(id);
					PacketSendUtility.sendPacket(player, object);
				}
			}
		} else {
			List<CountryOfficial> coList = Arrays.asList(countryOfficials);
			for (Entry<Long, Official> entry : court.getOfficials().entrySet()) {
				if (SessionManager.getInstance().isOnline(entry.getKey())
						&& coList.contains(entry.getValue().getOfficial())) {
					Player player = PlayerManager.getInstance().getPlayer(entry.getKey());
					PacketSendUtility.sendPacket(player, object);
				}
			}
		}
	}

	public void sendPacket(Object object, List<Player> players) {
		for (Player player : players) {
			PacketSendUtility.sendPacket(player, object);
		}
	}

	public void sendPackAll(Object object) {
		for (Player player : civils.values()) {
			PacketSendUtility.sendPacket(player, object);
		}
	}

	public void sendPacketAll(String authorityId, Object object) {
		for (Player player : civils.values()) {
			if (player.getCountry().getCourt().authority(player, authorityId)) {
				PacketSendUtility.sendPacket(player, object);
			}
		}
	}

	public void sendPacketAllExceptCollections(String authorityId, Object object, Collection<Long> objectIds) {
		for (Player player : civils.values()) {
			if (!objectIds.contains(player.getObjectId())
					&& player.getCountry().getCourt().authority(player, authorityId)) {
				PacketSendUtility.sendPacket(player, object);
			}
		}
	}

	public void sendPackAll(Object object, CoreConditions filterCond) {
		for (Player player : civils.values()) {
			if (filterCond.verify(player, false)) {
				PacketSendUtility.sendPacket(player, object);
			}
		}
	}

	public void sendPackAllExceptCollections(Object object, CoreConditions filterCond, Collection<Long> objectIds) {
		for (Player player : civils.values()) {
			if (filterCond.verify(player, false) && (!objectIds.contains(player.getObjectId()))) {
				PacketSendUtility.sendPacket(player, object);
			}
		}
	}

	public void sendPackAllByFilter(Long sender, Object object) {
		for (Player player : civils.values()) {
			if (ChatManager.getInstance().isInBlackList(sender, player.getObjectId()))
				continue;
			PacketSendUtility.sendPacket(player, object);
		}
	}
	
	public void sendOfficialPack(Object packet) {
		for (Long pid : court.getOfficials().keySet()) {
			if (SessionManager.getInstance().isOnline(pid)) {
				Player official = PlayerManager.getInstance().getPlayer(pid);
				PacketSendUtility.sendPacket(official, packet);
			}
		}
		
		for (KingGurad guard : court.getKingGuards()) {
			if (SessionManager.getInstance().isOnline(guard.getPlayerId())) {
				Player official = PlayerManager.getInstance().getPlayer(guard.getPlayerId());
				PacketSendUtility.sendPacket(official, packet);
			}
		}
	}
	
	@JsonIgnore
	public Collection<Player> getAllCountryPlayer(Player caller) {
		return civils.values();
	}

	@JsonIgnore
	public List<Player> getAllGuardPlayer(Player caller) {
		List<Player> players = new ArrayList<Player>();
		for (KingGurad kg : court.getKingGuards()) {
			Player target = civils.get(kg.getPlayerId());
			if (target != null) {
				players.add(target);
			}
		}
		return players;
	}

	@JsonIgnore
	public boolean isWeakCountry() {
		int weakCountry = WorldRankManager.getInstance().getWeakestCountry();
		if (weakCountry != 0 && weakCountry == id.getValue()) {
			return true;
		}
		return false;
	}

	public CountryId getId() {
		return id;
	}

	public void setId(CountryId id) {
		this.id = id;
	}

	public int getGdp() {
		return gdp;
	}

	public void setGdp(int gdp) {
		this.gdp = gdp;
	}

	public CountryFlag getCountryFlag() {
		return countryFlag;
	}

	public void setCountryFlag(CountryFlag countryFlag) {
		this.countryFlag = countryFlag;
	}

	public Technology getTechnology() {
		return technology;
	}

	public void setTechnology(Technology technology) {
		this.technology = technology;
	}

	public Diplomacy getDiplomacy() {
		return diplomacy;
	}

	public void setDiplomacy(Diplomacy diplomacy) {
		this.diplomacy = diplomacy;
	}

	public Coppers getCoppers() {
		return coppers;
	}

	public void setCoppers(Coppers coppers) {
		this.coppers = coppers;
	}

	public Court getCourt() {
		return court;
	}

	public void setCourt(Court court) {
		this.court = court;
	}

	public void appoint(Player target, CountryOfficial official, int index) {
		court.appoint(target, official, index);
	}

	@JsonIgnore
	public CountryEnt getEnt() {
		return ent;
	}

	public void setEnt(CountryEnt ent) {
		this.ent = ent;
	}

	@JsonIgnore
	public void upate() {
		CountryManager.getInstance().update(this);
	}

	public CountryShop getCountryShop() {
		return countryShop;
	}

	public void setCountryShop(CountryShop countryShop) {
		this.countryShop = countryShop;
	}

	public CountryStorge getCountryStorge() {
		return countryStorge;
	}

	public void setCountryStorge(CountryStorge countryStorge) {
		this.countryStorge = countryStorge;
	}

	public ConcurrentHashMap<Long, ForbidChat> getForbidChats() {
		return forbidChats;
	}

	public void setForbidChats(ConcurrentHashMap<Long, ForbidChat> forbidChats) {
		this.forbidChats = forbidChats;
	}

	public Temple getTemple() {
		return temple;
	}

	public void setTemple(Temple temple) {
		this.temple = temple;
	}

	public long getLastWinGangId() {
		return lastWinGangId;
	}

	public void setLastWinGangId(long lastWinGangId) {
		this.lastWinGangId = lastWinGangId;
	}

	@JsonIgnore
	public ConcurrentHashMap<Long, Player> getCivils() {
		return civils;
	}

	@JsonIgnore
	public ConcurrentHashMap<Integer, Long> getGuardNoticeCDMap() {
		return guardNoticeCD;
	}

	public CountryQuest getCountryQuest() {
		return countryQuest;
	}

	public void setCountryQuest(CountryQuest countryQuest) {
		this.countryQuest = countryQuest;
	}

	public FeteLog getFeteLog() {
		return feteLog;
	}

	public void setFeteLog(FeteLog feteLog) {
		this.feteLog = feteLog;
	}

	@JsonIgnore
	public Map<Integer, ArrayList<Integer>> getCountryWarEventStatus(Player player) {
		Map<Integer, ArrayList<Integer>> status = new HashMap<Integer, ArrayList<Integer>>();
		for (CountryWarEvent warEvent : CountryWarEvent.values()) {
			if (ConfigValueManager.getInstance().getCountryWarPushCond(warEvent.getValue()).verify(player, false)) {
				status.put(warEvent.getValue(), warEvent.isLightUp(this));
			} else {
				status.put(warEvent.getValue(), new ArrayList<Integer>());
			}
		}
		return status;
	}

	@JsonIgnore
	public ReentrantLock getLock() {
		return lock;
	}

	public void lockLock() {
		lock.lock();
	}

	public void unlockLock() {
		lock.unlock();
	}

	public int getCountryLevel() {
		return countryLevel;
	}

	public void setCountryLevel(int countryLevel) {
		this.countryLevel = countryLevel;
	}

	public ConcurrentHashMap<Long, TraitorPlayer> getTraitorMap() {
		return traitorMap;
	}

	public void setTraitorMap(ConcurrentHashMap<Long, TraitorPlayer> traitorMap) {
		this.traitorMap = traitorMap;
	}

	public int getBuffFloor() {
		return buffFloor;
	}

	public void setBuffFloor(int buffFloor) {
		this.buffFloor = buffFloor;
	}

	@JsonIgnore
	public void cleanDeprecateTraitor() {
		ArrayList<Long> removeIds = New.arrayList();
		for (TraitorPlayer traitor : traitorMap.values()) {
			if (SessionManager.getInstance().isOnline(traitor.getPlayerId())) {
				Player tPlayer = PlayerManager.getInstance().getPlayer(traitor.getPlayerId());
				Effect ef = tPlayer.getEffectController().getAnormalEffect(TraitorEffect.TRAITOR);
				if (ef == null) {
					removeIds.add(traitor.getPlayerId());
				} else {
					traitor.setEndTime(ef.getEndTime());
				}
			}
		}
		for (Long removeId : removeIds) {
			traitorMap.remove(removeId);
		}
		removeIds.clear();
	}

	@JsonIgnore
	public void addTraitor(Player traitor) {
		traitorMapFixs.put(traitor.getObjectId(), TraitorPlayerFix.valueOf(traitor));
	}

	public ReserveKing getReserveKing() {
		return reserveKing;
	}

	public void setReserveKing(ReserveKing reserveKing) {
		this.reserveKing = reserveKing;
	}

	@JsonIgnore
	public void calcAndSetCountryLevel(long selfPower, long mostCountryPower) {
		int oldCountryLevel = countryLevel;
		Double[] factors = ConfigValueManager.getInstance().COUNTRY_LEVEL_FACTOR.getValue();
		for (int i = 0; i < factors.length; i++) {
			if ((selfPower * 1.0 / mostCountryPower) >= factors[i]) {
				countryLevel = i;
				break;
			}
		}
		if (countryLevel != 0) {
			for (Long playerId : civils.keySet()) {
				EventBusManager.getInstance().submit(WeakCountryEvent.valueOf(playerId));
			}
		}
		if (oldCountryLevel != countryLevel) {
			sendPackAll(SM_Country_Level.valueOf(countryLevel));
		}
	}

	public int getGangWarCount() {
		return gangWarCount;
	}

	public void setGangWarCount(int gangWarCount) {
		this.gangWarCount = gangWarCount;
	}

	@JsonIgnore
	public void addGangWarCount() {
		this.gangWarCount++;
	}

	@JsonIgnore
	public void initCountryFlagQuest(CountryFlagQuestType type, int target, int alliance, List<Integer> attackCountries) {
		countryFlag.spawnFlagAndStartQuest(type, target, alliance, attackCountries);
	}

	@JsonIgnore
	public boolean isInFlagActivity() {
		return countryFlag.isFlagQuesting();
	}

	@JsonIgnore
	public CountryFlagQuestVO createFlagQuestVO(Player player) {
		CountryFlagQuestVO vo = new CountryFlagQuestVO();
		if (countryFlag.isQuesting()) {
			vo.setStartTime(countryFlag.questStartTime());
			vo.setType(countryFlag.getFlagQuestType().getValue());
			vo.setAlliance(countryFlag.getAlliance());
			vo.setInAct(true);
			vo.setTarget(countryFlag.getTarget());
			vo.setAttackCountries(countryFlag.getAttackCountries());
		} else {
			vo.setInAct(false);
		}
		vo.setNextStartTime(countryFlag.getNextReliveTime());
		vo.setAttendFlag(player.getPlayerCountryHistory().isAttendFlag());
		return vo;
	}

	public NewTechnology getNewTechnology() {
		return newTechnology;
	}

	public void setNewTechnology(NewTechnology newTechnology) {
		this.newTechnology = newTechnology;
	}

	public ConcurrentHashMap<Long, TraitorPlayerFix> getTraitorMapFixs() {
		return traitorMapFixs;
	}

	public void setTraitorMapFixs(ConcurrentHashMap<Long, TraitorPlayerFix> traitorMapFixs) {
		this.traitorMapFixs = traitorMapFixs;
	}

}
