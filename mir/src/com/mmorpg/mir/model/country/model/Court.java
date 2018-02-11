package com.mmorpg.mir.model.country.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.country.event.ReceiveCivilSalaryEvent;
import com.mmorpg.mir.model.country.model.vo.CourtVO;
import com.mmorpg.mir.model.country.model.vo.KingGuradVO;
import com.mmorpg.mir.model.country.packet.SM_Country_Appoint;
import com.mmorpg.mir.model.country.packet.SM_Country_Depose;
import com.mmorpg.mir.model.country.packet.SM_Country_QuestStatus;
import com.mmorpg.mir.model.country.packet.SM_Country_Reset;
import com.mmorpg.mir.model.country.packet.SM_Player_Official_Change;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.New;

/**
 * 官吏
 * 
 * @author Kuang Hao
 * @since v1.0 2014-9-15
 * 
 */
public class Court {

	private static Logger logger = Logger.getLogger(Court.class);
	private Country country;
	/** 官员 */
	private Map<Long, Official> officials;
	/** 国王登基的时间 */
	private long becomeKingTime;
	/** 最后重置的时间 */
	private long lastResetTime;
	/** 统治力 */
	private int control;
	/** 国王卫队 */
	private HashSet<KingGurad> kingGuards;
	/** 国家权限使用次数 */
	private Map<String, Integer> useAuthorityHistory;
	/** 全民动员的记录 */
	private Map<Long, Integer> mobilizationHistory;
	/** 当日的禁言记录 */
	private Map<Long, Integer> forbidChatHistory;

	private long lastReduceTime;
	/** 官员俸禄 */
	private boolean officialSalary;
	/** 国民福利 */
	private boolean civilSalary;
	/** 最后发放福利时间 */
	private long lastOpenOfficialSalary;
	private long lastOpenCivilSalary;
	/** 领取人 */
	private Set<Long> officialReceived = new HashSet<Long>();
	private Set<Long> civilReceived = new HashSet<Long>();
	private Set<Long> officialSalaryRecord = new HashSet<Long>();

	private AtomicInteger callCount = new AtomicInteger();
	private AtomicInteger callTokenCount = new AtomicInteger();
	private AtomicInteger mobilizationCount = new AtomicInteger();

	private String notice;

	@JsonIgnore
	public void fix() {
		List<Long> removeIds = new ArrayList<Long>();
		for (long playerId : officials.keySet()) {
			Player player = PlayerManager.getInstance().getPlayer(playerId);
			if (player == null) {
				removeIds.add(playerId);
			}
		}
		for (long removeId : removeIds) {
			officials.remove(removeId);
			logger.error(String.format("国家[%s]信息删除不存在的玩家[%s]", country.getName(), removeId));
		}

		List<KingGurad> removeGurads = new ArrayList<KingGurad>();
		for (KingGurad kingGuard : kingGuards) {
			Player player = PlayerManager.getInstance().getPlayer(kingGuard.getPlayerId());
			if (player == null) {
				removeGurads.add(kingGuard);
			}
		}
		for (KingGurad kingGurad : removeGurads) {
			kingGuards.remove(kingGurad);
			logger.error(String.format("国家[%s]信息删除不存在的玩家[%s]", country.getName(), kingGurad.getPlayerId()));
		}
	}

	public static Court valueOf(Country country) {
		Court officals = new Court();
		officals.country = country;
		officals.officials = new HashMap<Long, Official>();
		officals.kingGuards = new HashSet<KingGurad>();
		officals.setLastReduceTime(System.currentTimeMillis());
		officals.useAuthorityHistory = new HashMap<String, Integer>();
		officals.mobilizationHistory = new HashMap<Long, Integer>();
		officals.forbidChatHistory = new HashMap<Long, Integer>();
		officals.reset(false);
		return officals;
	}

	public CourtVO creatVO() {
		return CourtVO.valueOf(this);
	}

	@JsonIgnore
	private List<CountryOfficalInfo> initCountryOfficalInfos() {
		List<CountryOfficalInfo> infos = new ArrayList<CountryOfficalInfo>();
		infos.add(CountryOfficalInfo.valueOf(CountryOfficial.PREMIER, 0));
		infos.add(CountryOfficalInfo.valueOf(CountryOfficial.GENERALISSIMO, 0));
		infos.add(CountryOfficalInfo.valueOf(CountryOfficial.MINISTER, 1));
		infos.add(CountryOfficalInfo.valueOf(CountryOfficial.MINISTER, 0));
		infos.add(CountryOfficalInfo.valueOf(CountryOfficial.GENERAL, 0));
		infos.add(CountryOfficalInfo.valueOf(CountryOfficial.GENERAL, 1));
		return infos;
	}

	@JsonIgnore
	private List<CountryOfficalInfo> initGuardOfficalInfos() {
		List<CountryOfficalInfo> infos = new ArrayList<CountryOfficalInfo>();
		infos.add(CountryOfficalInfo.valueOf(CountryOfficial.GUARD, 0));
		infos.add(CountryOfficalInfo.valueOf(CountryOfficial.GUARD, 1));
		infos.add(CountryOfficalInfo.valueOf(CountryOfficial.GUARD, 2));
		infos.add(CountryOfficalInfo.valueOf(CountryOfficial.GUARD, 3));
		infos.add(CountryOfficalInfo.valueOf(CountryOfficial.GUARD, 4));
		return infos;
	}

	@JsonIgnore
	public void autoAppoint(Country country, List<Player> players) {
		country.lockLock();
		try {
			int i = 0;
			List<CountryOfficalInfo> infos = initCountryOfficalInfos();

			Player kingPlayer = country.getKing();
			// 任命官员
			for (CountryOfficalInfo info : infos) {
				if (i >= players.size()) {
					return;
				}
				Player targetPlayer = players.get(i);
				i++;
				autoAppointOffical(kingPlayer, targetPlayer, info);
			}

			List<CountryOfficalInfo> guardInfos = initGuardOfficalInfos();
			// 任命禁卫军
			for (CountryOfficalInfo info : guardInfos) {
				if (i >= players.size()) {
					return;
				}
				Player targetPlayer = players.get(i);
				i++;
				autoAppointGurad(kingPlayer, targetPlayer, info);
			}
		} finally {
			country.unlockLock();
		}
	}

	@JsonIgnore
	private void autoAppointOffical(Player kingPlayer, Player targetPlayer, CountryOfficalInfo info) {
		if (targetPlayer.getCountry().isOffical(targetPlayer)) {
			return;
		}
		String authorityId = (AuthorityID.APPOINT_BASE + info.getOfficalEnum().getValue()) + "";
		country.authority(kingPlayer, authorityId);
		country.appoint(targetPlayer, info.getOfficalEnum(), info.getIndex());
		kingPlayer.addUseAuthorityHistory(authorityId);

		I18nUtils utils = I18nUtils.valueOf("10502");
		utils.addParm("name", I18nPack.valueOf(targetPlayer.getName()));
		utils.addParm(
				"officialname",
				I18nPack.valueOf(ConfigValueManager.getInstance().OFFICIAL_NAME.getValue().get(
						info.getOfficalEnum().name())));
		ChatManager.getInstance().sendSystem(11003, utils, null, targetPlayer.getCountry());

		I18nUtils chatUtils = I18nUtils.valueOf("302004", utils);
		ChatManager.getInstance().sendSystem(6, chatUtils, null, targetPlayer.getCountry());

		// inform target and those who appear in his screen
		if (SessionManager.getInstance().isOnline(targetPlayer.getObjectId())) {
			PacketSendUtility.broadcastPacket(targetPlayer,
					SM_Player_Official_Change.valueOf(targetPlayer.getObjectId(), info.getOfficalEnum().name()));
		}
		I18nUtils titel18n = I18nUtils.valueOf(ConfigValueManager.getInstance().AUTO_APPOINT_OFFICAL_MAIL_TITLEID
				.getValue());
		I18nUtils contextl18n = I18nUtils.valueOf(ConfigValueManager.getInstance().AUTO_APPOINT_OFFICAL_MAIL_CONTENTID
				.getValue());
		contextl18n.addParm("country", I18nPack.valueOf(targetPlayer.getCountry().getName()));
		contextl18n.addParm(
				"officer",
				I18nPack.valueOf(ConfigValueManager.getInstance().OFFICIAL_NAME.getValue().get(
						info.getOfficalEnum().name())));
		contextl18n.addParm("kingname", I18nPack.valueOf(country.getKing().getName()));
		Mail mail = Mail.valueOf(titel18n, contextl18n, null, null);
		MailManager.getInstance().sendMail(mail, targetPlayer.getObjectId());
	}

	@JsonIgnore
	private void autoAppointGurad(Player kingPlayer, Player targetPlayer, CountryOfficalInfo info) {
		if (targetPlayer.getCountry().getCourt().isGurad(targetPlayer.getObjectId())) {
			return;
		}
		if (targetPlayer.getCountry().getCourt().isOfficial(targetPlayer.getObjectId())) {
			return;
		}
		targetPlayer.getCountry().authority(kingPlayer, AuthorityID.APPOINT_GUARD);
		CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1,
				ConfigValueManager.getInstance().COUNTRY_APPOINT_GUARD.getValue());
		if (!conditions.verify(targetPlayer, false)) {
			return;
		}
		targetPlayer.getCountry().getCourt().setGurads(targetPlayer.getObjectId(), info.getIndex());
		targetPlayer.getCountry().getCourt().appoint(targetPlayer, CountryOfficial.GUARD, info.getIndex());
		PacketSendUtility.sendPacket(kingPlayer,
				SM_Country_Appoint.valueOf(kingPlayer, KingGuradVO.GUARD_OFFICIAL, info.getIndex()));
		kingPlayer.addUseAuthorityHistory(AuthorityID.APPOINT_GUARD);

		CountryOfficial countryOfficial = CountryOfficial.GUARD;
		I18nUtils utils = I18nUtils.valueOf("10502");
		utils.addParm("name", I18nPack.valueOf(targetPlayer.getName()));
		utils.addParm("officialname",
				I18nPack.valueOf(ConfigValueManager.getInstance().OFFICIAL_NAME.getValue().get(countryOfficial.name())));
		ChatManager.getInstance().sendSystem(11003, utils, null, targetPlayer.getCountry());

		I18nUtils chatUtils = I18nUtils.valueOf("302004", utils);
		ChatManager.getInstance().sendSystem(6, chatUtils, null, targetPlayer.getCountry());
		// inform target and those who appear in his screen
		if (SessionManager.getInstance().isOnline(targetPlayer.getObjectId())) {
			PacketSendUtility.broadcastPacket(targetPlayer,
					SM_Player_Official_Change.valueOf(targetPlayer.getObjectId(), countryOfficial.name()));
		}

		I18nUtils titel18n = I18nUtils.valueOf(ConfigValueManager.getInstance().AUTO_APPOINT_OFFICAL_MAIL_TITLEID
				.getValue());
		I18nUtils contextl18n = I18nUtils.valueOf(ConfigValueManager.getInstance().AUTO_APPOINT_OFFICAL_MAIL_CONTENTID
				.getValue());
		contextl18n.addParm("country", I18nPack.valueOf(targetPlayer.getCountry().getName()));
		contextl18n
				.addParm(
						"officer",
						I18nPack.valueOf(ConfigValueManager.getInstance().OFFICIAL_NAME.getValue().get(
								countryOfficial.name())));
		contextl18n.addParm("kingname", I18nPack.valueOf(country.getKing().getName()));
		Mail mail = Mail.valueOf(titel18n, contextl18n, null, null);
		MailManager.getInstance().sendMail(mail, targetPlayer.getObjectId());
	}

	@JsonIgnore
	public void openCivilSalary() {
		civilSalary = true;
		lastOpenCivilSalary = System.currentTimeMillis();
	}

	@JsonIgnore
	public void openOfficialSalary() {
		officialSalary = true;
		lastOpenOfficialSalary = System.currentTimeMillis();
		for (long playerId : officials.keySet()) {
			officialSalaryRecord.add(playerId);
		}
		/** 把卫队也添加到领取官员福利列表 */
		for (Iterator<KingGurad> iterator = kingGuards.iterator(); iterator.hasNext();) {
			officialSalaryRecord.add(iterator.next().getPlayerId());
		}
	}

	@JsonIgnore
	public void resetSalary(String civilResetCron, String officialResetCron) {
		Date now = new Date();
		if (civilSalary) {
			// 国民福利
			long nextCivilTime1 = DateUtils.getNextTime(civilResetCron, new Date(lastOpenCivilSalary)).getTime();
			long nextCivilTime2 = DateUtils.getNextTime(civilResetCron, now).getTime();
			if (nextCivilTime1 != nextCivilTime2) {
				civilSalary = false;
				civilReceived.clear();
			}
		}

		if (officialSalary) {
			// 官员福利
			long nextOfficialTime1 = DateUtils.getNextTime(civilResetCron, new Date(lastOpenOfficialSalary)).getTime();
			long nextOfficialTime2 = DateUtils.getNextTime(civilResetCron, now).getTime();
			if (nextOfficialTime1 != nextOfficialTime2) {
				officialSalary = false;
				officialReceived.clear();
				officialSalaryRecord.clear();
			}
		}
	}

	@JsonIgnore
	public void receiveOfficialSalary(long playerId) {
		officialReceived.add(playerId);
	}

	@JsonIgnore
	public void receiveCivilSalary(long playerId) {
		civilReceived.add(playerId);
		EventBusManager.getInstance().submit(ReceiveCivilSalaryEvent.valueOf(playerId));
	}

	@JsonIgnore
	private void addUseAuthorityHistory(String id, int count) {
		if (useAuthorityHistory.containsKey(id)) {
			useAuthorityHistory.put(id, useAuthorityHistory.get(id) + count);
		} else {
			useAuthorityHistory.put(id, count);
		}
	}

	public boolean authority(Player player, String id) {
		Official offical = officials.get(player.getObjectId());
		// 官员
		if (offical != null) {
			return offical.authority(player, id);
		}
		// 卫队
		if (player.getCountry().getCourt().isGurad(player.getObjectId())) {
			return CountryOfficial.GUARD.authority(id, player);
		}
		// 平民
		return CountryOfficial.CITIZEN.authority(id, player);
	}

	public void addUseAuthorityHistory(Player player, String id) {
		Official offical = officials.get(player.getObjectId());
		if (offical != null) {
			offical.addUseAuthorityHistory(id, 1);
			addUseAuthorityHistory(id, 1);
		}
	}

	@JsonIgnore
	public Official getKing() {
		for (Official offical : officials.values()) {
			if (offical.getOfficial() == CountryOfficial.KING) {
				return offical;
			}
		}
		return null;
	}

	public void reset(boolean sendPack) {
		// inform former official
		for (Long former : officials.keySet()) {
			if (SessionManager.getInstance().isOnline(former)) {
				Player target = PlayerManager.getInstance().getPlayer(former);
				PacketSendUtility.broadcastPacket(target,
						SM_Player_Official_Change.valueOf(former, CountryOfficial.CITIZEN.name()));
			}
		}
		if (sendPack) {
			country.sendPacket(SM_Country_Reset.valueOf(country.getId()));
		}

		officials.clear();
		control = ConfigValueManager.getInstance().COUNTRY_CONTROL_INIT.getValue();
		kingGuards.clear();
		lastResetTime = System.currentTimeMillis();
		mobilizationHistory.clear();
		forbidChatHistory.clear();
		if (sendPack) {
			country.sendOfficialPack(SM_Country_QuestStatus.valueOf(country.getCountryQuest(), country.getCourt(),
					!country.getTraitorMapFixs().isEmpty()));
		}
	}

	@JsonIgnore
	public void resetControl() {
		control = ConfigValueManager.getInstance().COUNTRY_CONTROL_INIT.getValue();
	}

	@JsonIgnore
	public void setGurads(long playerId, int index) {
		KingGurad kingGurad = KingGurad.valueOf(playerId, index);
		kingGuards.add(kingGurad);
	}

	@JsonIgnore
	public int deposeGurads(long playerId) {
		KingGurad kingGurad = null;
		for (KingGurad kg : kingGuards) {
			if (kg.getPlayerId() == playerId) {
				kingGurad = kg;
			}
		}
		kingGuards.remove(kingGurad);
		return kingGurad.getIndex();
	}

	@JsonIgnore
	public long getIndexGuardsPlayerId(int index) {
		if (containsIndexGuard(index)) {
			for (KingGurad kg : kingGuards) {
				if (kg.getIndex() == index) {
					return kg.getPlayerId();
				}
			}
		}
		return 0L;
	}

	@JsonIgnore
	public boolean containsIndexGuard(int index) {
		for (KingGurad kg : kingGuards) {
			if (kg.getIndex() == index) {
				return true;
			}
		}
		return false;
	}

	@JsonIgnore
	public boolean isGurad(long playerId) {
		return kingGuards.contains(KingGurad.valueOf(playerId, 0));
	}

	@JsonIgnore
	public boolean isOfficial(long playerId) {
		return officials.containsKey(playerId);
	}

	public void refresh() {
		if (!DateUtils.isToday(new Date(getLastReduceTime()))) {
			int interval = DateUtils.calcIntervalDays(new Date(getLastReduceTime()), new Date());
			control -= (interval * ConfigValueManager.getInstance().COUNTRY_CONTROL_DAY_REDUCE.getValue());
			setLastReduceTime(System.currentTimeMillis());
			if (control < 0) {
				control = 0;
				onReduceControl();
				return;
			}

			// 刷新官员的操作记录
			for (Official offical : officials.values()) {
				offical.clearUseAuthorityHistory();
			}
			useAuthorityHistory.clear();
			mobilizationHistory.clear();
			forbidChatHistory.clear();

			// 刷新国民的召唤记录
			if (getKing() != null) {
				getKing().getCalltogeterhCount().clear();
			}
		}
	}

	public void onReduceControl() {
		// 统治力下降为0，国王自动下台
		if (control <= 0) {
			Official kingOfficial = getKing();
			if (kingOfficial != null) {

				// Player king =
				// PlayerManager.getInstance().getPlayer(getKing().getPlayerId());
				// I18nUtils i18nUtils = I18nUtils.valueOf("405003");
				// i18nUtils.addParm("name", I18nPack.valueOf(king.getName()));
				// i18nUtils.addParm("country",
				// I18nPack.valueOf(king.getCountry().getName()));
				// ChatManager.getInstance().sendSystem(71001, i18nUtils, null);

				reset(true);
				KingOfWarManager.getInstance().refreshSculptures(country.getId().getValue());
			}
		}
	}

	public void reduceControl(int value) {
		control -= value;
		if (control < 0) {
			control = 0;
		}
		onReduceControl();
	}

	public void onIncreaseControl() {

	}

	public void increaseControl(int value) {
		control += value;
		if (control > ConfigValueManager.getInstance().COUNTRY_CONTROL_MAX.getValue()) {
			control = ConfigValueManager.getInstance().COUNTRY_CONTROL_MAX.getValue();
		}
		onIncreaseControl();
	}

	public Map<Long, Official> getOfficials() {
		return officials;
	}

	@JsonIgnore
	public CountryOfficial getPlayerOfficial(Player player) {
		if (officials.containsKey(player.getObjectId())) {
			return officials.get(player.getObjectId()).getOfficial();
		}
		if (isGurad(player.getObjectId())) {
			return CountryOfficial.GUARD;
		}
		return null;
	}

	public void setOfficials(Map<Long, Official> officials) {
		this.officials = officials;
	}

	@JsonIgnore
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public int getControl() {
		return control;
	}

	public void setControl(int control) {
		this.control = control;
	}

	public void appoint(Player target, CountryOfficial official, int index) {
		Official offical = Official.valueOf(target, official, index);
		officials.put(target.getObjectId(), offical);
		country.sendPacket(SM_Country_Appoint.valueOf(target, official.name(), index));
		if (official == CountryOfficial.KING) {
			PacketSendUtility.broadcastPacket(target,
					SM_Player_Official_Change.valueOf(target.getObjectId(), CountryOfficial.KING.name()));
		} else if (getKing().calltogetherCd()) {
			for (String officialName : ConfigValueManager.getInstance().COUNTRY_CALL_PEOPLE.getValue()) {
				if (officialName.equals(official.name())) {
					offical.putContext(OfficalCtxKey.CALLTOGETHER_CD_END.getValue(),
							getKing().getContexts().get(OfficalCtxKey.CALLTOGETHER_CD_END.getValue()));
					break;
				}
			}
		}
		PacketSendUtility
				.sendPacket(target, SM_Country_QuestStatus.valueOf(country.getCountryQuest(), this, !country
						.getTraitorMapFixs().isEmpty()));
	}

	@JsonIgnore
	public void deposeNotice(Player formerPlayer, CountryOfficial official) {
		I18nUtils utils = I18nUtils.valueOf("10501");
		utils.addParm("officialname",
				I18nPack.valueOf(ConfigValueManager.getInstance().OFFICIAL_NAME.getValue().get(official.name())));
		utils.addParm("name", I18nPack.valueOf(formerPlayer.getName()));
		ChatManager.getInstance().sendSystem(11003, utils, null, getCountry());

		I18nUtils chatUtils = I18nUtils.valueOf("302003", utils);
		ChatManager.getInstance().sendSystem(6, chatUtils, null, getCountry());

		if (SessionManager.getInstance().isOnline(formerPlayer.getObjectId())) {
			PacketSendUtility.broadcastPacket(formerPlayer,
					SM_Player_Official_Change.valueOf(formerPlayer.getObjectId(), CountryOfficial.CITIZEN.name()));
		}
	}

	public void depose(Player target) {
		Official offical = officials.get(target.getObjectId());
		officials.remove(target.getObjectId());
		SM_Country_Depose sm = SM_Country_Depose.valueOf(target, offical.getOfficial().name(), offical.getIndex());
		country.sendPacket(sm);
		PacketSendUtility.sendPacket(target, sm);
	}

	public HashSet<KingGurad> getKingGuards() {
		return kingGuards;
	}

	public void setKingGuards(HashSet<KingGurad> kingGuards) {
		this.kingGuards = kingGuards;
	}

	public long getLastReduceTime() {
		return lastReduceTime;
	}

	public void setLastReduceTime(long lastReduceTime) {
		this.lastReduceTime = lastReduceTime;
	}

	public Map<String, Integer> getUseAuthorityHistory() {
		return useAuthorityHistory;
	}

	public void setUseAuthorityHistory(Map<String, Integer> useAuthorityHistory) {
		this.useAuthorityHistory = useAuthorityHistory;
	}

	@JsonIgnore
	public int getAuthorityExtraCount(String authorityId) {
		if (country.isWeakCountry()) {
			Integer count = ConfigValueManager.getInstance().COUNTRY_AUTHORITY_EXTRA_COUNT.getValue().get(authorityId);
			if (count != null) {
				return count;
			}
		}
		return 0;
	}

	public boolean isOfficialSalary() {
		return officialSalary;
	}

	public void setOfficialSalary(boolean officialSalary) {
		this.officialSalary = officialSalary;
	}

	public boolean isCivilSalary() {
		return civilSalary;
	}

	public void setCivilSalary(boolean civilSalary) {
		this.civilSalary = civilSalary;
	}

	public long getLastOpenOfficialSalary() {
		return lastOpenOfficialSalary;
	}

	public void setLastOpenOfficialSalary(long lastOpenOfficialSalary) {
		this.lastOpenOfficialSalary = lastOpenOfficialSalary;
	}

	public long getLastOpenCivilSalary() {
		return lastOpenCivilSalary;
	}

	public void setLastOpenCivilSalary(long lastOpenCivilSalary) {
		this.lastOpenCivilSalary = lastOpenCivilSalary;
	}

	public Set<Long> getOfficialReceived() {
		return officialReceived;
	}

	public void setOfficialReceived(Set<Long> officialReceived) {
		this.officialReceived = officialReceived;
	}

	public Set<Long> getCivilReceived() {
		return civilReceived;
	}

	public void setCivilReceived(Set<Long> civilReceived) {
		this.civilReceived = civilReceived;
	}

	public Set<Long> getOfficialSalaryRecord() {
		return officialSalaryRecord;
	}

	public void setOfficialSalaryRecord(Set<Long> officialSalaryRecord) {
		this.officialSalaryRecord = officialSalaryRecord;
	}

	public long getBecomeKingTime() {
		return becomeKingTime;
	}

	public void setBecomeKingTime(long becomeKingTime) {
		this.becomeKingTime = becomeKingTime;
	}

	public AtomicInteger getCallCount() {
		return callCount;
	}

	public void setCallCount(AtomicInteger callCount) {
		this.callCount = callCount;
	}

	public AtomicInteger getCallTokenCount() {
		return callTokenCount;
	}

	public void setCallTokenCount(AtomicInteger callTokenCount) {
		this.callTokenCount = callTokenCount;
	}

	public long getLastResetTime() {
		return lastResetTime;
	}

	public void setLastResetTime(long lastResetTime) {
		this.lastResetTime = lastResetTime;
	}

	public AtomicInteger getMobilizationCount() {
		return mobilizationCount;
	}

	public void setMobilizationCount(AtomicInteger mobilizationCount) {
		this.mobilizationCount = mobilizationCount;
	}

	public Map<Long, Integer> getMobilizationHistory() {
		return mobilizationHistory;
	}

	public void setMobilizationHistory(Map<Long, Integer> mobilizationHistory) {
		this.mobilizationHistory = mobilizationHistory;
	}

	// 一个官职可能有多个玩家
	@JsonIgnore
	public void addMobilizationCount(long playerId) {
		Integer count = mobilizationHistory.get(playerId);
		if (count == null) {
			mobilizationHistory.put(playerId, 1);
		} else {
			mobilizationHistory.put(playerId, count + 1);
		}
	}

	@JsonIgnore
	public int getMobilizationCount(long playerId) {
		Integer count = mobilizationHistory.get(playerId);
		return count == null ? 0 : count.intValue();
	}

	@JsonIgnore
	public void addForbidChatCount(long playerId) {
		Integer count = forbidChatHistory.get(playerId);
		if (count == null) {
			forbidChatHistory.put(playerId, 1);
		} else {
			forbidChatHistory.put(playerId, count + 1);
		}
	}

	@JsonIgnore
	public int getForbidChatCount(long playerId) {
		Integer count = forbidChatHistory.get(playerId);
		return count == null ? 0 : count.intValue();
	}

	public Map<Long, Integer> getForbidChatHistory() {
		return forbidChatHistory;
	}

	public void setForbidChatHistory(Map<Long, Integer> forbidChatHistory) {
		this.forbidChatHistory = forbidChatHistory;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	@JsonIgnore
	public List<Official> getOfficialByOfficialName(String[] officialNames) {
		List<Official> ret = New.arrayList();
		for (Official official : officials.values()) {
			for (String name : officialNames) {
				if (name.equals(official.getOfficial().name())) {
					ret.add(official);
					break;
				}
			}
		}
		return ret;
	}

}
