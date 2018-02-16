package com.mmorpg.mir.model.gang.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gang.entity.GangEnt;
import com.mmorpg.mir.model.gang.event.PlayerGangChangeEvent;
import com.mmorpg.mir.model.gang.manager.GangManager;
import com.mmorpg.mir.model.gang.model.log.GangLog;
import com.mmorpg.mir.model.gang.packet.SM_ChangePosition_Gang;
import com.mmorpg.mir.model.gang.packet.SM_Self_Gang_Change;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;

public class Gang {
	private static Logger logger = LoggerFactory.getLogger(Gang.class);
	public static final int MAX_LOG_COUNT = 50;

	private long id;
	private int level;
	private String name;
	private ConcurrentHashMap<Long, Apply> applies;
	private GangEnt gangEnt;
	private ConcurrentHashMap<Long, Member> members;
	private long createTime;
	private String info;
	private long battle;
	private CountryId country;
	private boolean autoDeal = true;
	private List<GangLog> logs;
	private long memberImpeachId;
	private long startImpeachTime;
	private String server;
	private long lastModifyInfoTime;
	private int rank;

	@Transient
	private long joinChatTimeMills;

	public Member impeachMaster(Member impeacher, int interval) {
		Member master = getMaster();

		if (impeacher.getPosition() == GangPosition.Master)
			return null;

		boolean stillOnline = SessionManager.getInstance().isOnline(master.getPlayerId());
		if (!stillOnline && (System.currentTimeMillis() - master.getLastLoginTime()) > interval) {
			impeacher.setPosition(GangPosition.Master);
			master.setPosition(GangPosition.Member);
			return impeacher;
		}

		return null;
	}

	public void updateMemberBattlePoints(long playerId, long battlePoints, Player player) {
		members.get(playerId).updateBattlePoints(this, battlePoints, player);
	}

	synchronized public void addBattlePoints(long add) {
		battle += add;
	}

	synchronized public void decBattlePoints(long dec) {
		battle -= dec;
	}

	public void addGangLog(GangLog gangLog) {
		logs.add(gangLog);
		if (logs.size() > MAX_LOG_COUNT) {
			logs.remove(0);
		}
		send(gangLog, GangPosition.Member, SessionManager.getInstance());
	}

	@JsonIgnore
	public boolean isRight(Player player, GangPosition position) {
		Member member = load(player.getObjectId());
		if (member.getPosition().getValue() <= position.getValue()) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.GANG_NOT_RIGHT);
	}

	@JsonIgnore
	public void cancelApply(long playerId) {
		applies.remove(playerId);
	}

	@JsonIgnore
	public Member getMaster() {
		for (Member member : members.values()) {
			if (member.getPosition() == GangPosition.Master) {
				return member;
			}
		}
		logger.error(String.format("卧槽！帮会[%s][%s]首领丢失！", this.getId(), this.getName()));
		for (Member member : members.values()) {
			member.setPosition(GangPosition.Master);
			logger.error(String.format("修复玩家[%s]为新的首领", member.getName()));
			return member;
		}
		GangManager.getInstance().doRemoveGang(this);
		logger.error(String.format("没有成员直接删除帮会。"));
		throw new RuntimeException(String.format("获取帮主信息异常！"));
	}

	@JsonIgnore
	public Member getAssistant() {
		for (Member member : members.values()) {
			if (member.getPosition() == GangPosition.Assistant) {
				return member;
			}
		}
		return null;
	}

	@JsonIgnore
	public void send(Object message, GangPosition position, SessionManager sessionManager) {
		for (Member member : members.values()) {
			if (member.getPosition().getValue() <= position.getValue()) {
				if (sessionManager.isOnline(member.getPlayerId())) {
					PacketSendUtility.sendPacket(PlayerManager.getInstance().getPlayer(member.getPlayerId()), message);
				}
			}
		}
	}

	public void sendByFilter(Object message, Long senderId, GangPosition position, SessionManager sessionManager) {
		for (Member member : members.values()) {
			if (member.getPosition().getValue() <= position.getValue()) {
				if (sessionManager.isOnline(member.getPlayerId())) {
					if (ChatManager.getInstance().isInBlackList(senderId, member.getPlayerId()))
						continue;
					PacketSendUtility.sendPacket(PlayerManager.getInstance().getPlayer(member.getPlayerId()), message);
				}
			}
		}

	}

	@JsonIgnore
	public Member load(long playerId) {
		if (!members.containsKey(playerId)) {
			throw new ManagedException(ManagedErrorCode.GANG_NOT_JOIN);
		}
		return members.get(playerId);
	}

	public GangVO creatVO() {
		GangVO vo = new GangVO();
		vo.setId(id);
		vo.setName(name);
		vo.setVos(new ArrayList<MemberVO>());
		for (Member member : members.values()) {
			vo.getVos().add(member.createVO());
		}
		vo.setInfo(info);
		vo.setBattle(battle);
		vo.setCountry(country.name());
		vo.setLogs(new ArrayList<GangLog>());
		vo.getLogs().addAll(logs);
		vo.setServer(server);
		vo.setAutoDeal(autoDeal ? (byte) 1 : 0);
		vo.setLastModifyInfoTime(lastModifyInfoTime);
		vo.setRank(rank);
		vo.setJoinChatTimeMills(joinChatTimeMills);
		return vo;
	}

	public GangSimpleVO creatSimpleVO() {
		GangSimpleVO vo = new GangSimpleVO();
		vo.setId(id);
		vo.setName(name);
		vo.setSize(members.size());
		vo.setMasterName(this.getMaster().getName());
		vo.setBattlePoints(battle);
		vo.setAutoDeal(autoDeal ? (byte) 1 : 0);
		vo.setRank(rank);
		vo.setMasterId(this.getMaster().getPlayerId());
		vo.setMasterLevel(this.getMaster().getLevel());
		vo.setCreateTime(this.getCreateTime());
		vo.setOnline(SessionManager.getInstance().isOnline(this.getMaster().getPlayerId()) ? (byte) 1 : 0);
		vo.setCountry(getCountry().getValue());
		return vo;
	}

	public static Gang valueOf(long id, String name, CountryId country, Player player) {
		Gang gang = new Gang();
		gang.id = id;
		gang.level = 1;
		gang.name = name;
		gang.applies = new ConcurrentHashMap<Long, Apply>();
		gang.members = new ConcurrentHashMap<Long, Member>();
		gang.createTime = System.currentTimeMillis();
		gang.country = country;
		gang.logs = New.arrayList();
		gang.server = player.getPlayerEnt().getServer();
		return gang;
	}

	public Member setMemberPosition(long playerId, GangPosition position) {
		if (!members.containsKey(playerId)) {
			return null;
		}
		load(playerId).setPosition(position);
		SM_ChangePosition_Gang sm = new SM_ChangePosition_Gang();
		sm.setPosition(position.getValue());
		sm.setTargetId(playerId);
		PacketSendUtility.sendPacket(PlayerManager.getInstance().getPlayer(playerId), sm);
		return members.get(playerId);
	}

	public Member quit(Player player) {
		Member member = load(player.getObjectId());
		if (member.getPosition() == GangPosition.Master) {
			throw new ManagedException(ManagedErrorCode.GANG_MASTER_NOT_QUIT);
		}
		// notice
		I18nUtils utils = I18nUtils.valueOf("304007").addParm("user", I18nPack.valueOf(player.createSimple()));
		ChatManager.getInstance().sendSystem(2, utils, null, this);
		// do remove
		player.getPlayerGang().setGangAndUpdate(0);
		if (memberImpeachId == player.getObjectId().longValue()) {
			// 取消弹劾的人
			memberImpeachId = 0;
		}
		members.remove(player.getObjectId());
		refreshBattlePoints();
		PacketSendUtility.broadcastPacket(player, SM_Self_Gang_Change.valueOf(player.getObjectId(), null));
		EventBusManager.getInstance().submit(PlayerGangChangeEvent.valueOf(player.getObjectId(), 0L, null));
		return member;
	}

	public Member join(Player player, GangPosition gangPosition, boolean notice) {
		if (members.contains(player.getObjectId())) {
			return members.get(player.getObjectId());
		}
		if (player.getPlayerGang().getGangId() != 0) {
			throw new ManagedException(ManagedErrorCode.GANG_JOIN);
		}

		if (player.getCountryValue() != country.getValue()) {
			throw new ManagedException(ManagedErrorCode.COUNTRY_NOT_SAME);
		}

		Member member = Member.valueOf(player, gangPosition);
		player.getPlayerGang().setGangAndUpdate(this.getId());
		for (PlayerApply apply : player.getPlayerGang().getApplies()) {
			Gang applyGang = GangManager.getInstance().get(apply.getGangId());
			if (applyGang != null) {
				applyGang.getApplies().remove(player.getObjectId());
			}
		}
		player.getPlayerGang().getApplies().clear();
		player.getPlayerGang().getInvites().clear();
		members.put(player.getObjectId(), member);
		member.setLastLoginTime(player.getPlayerEnt().getStat().getLastLogoutTime().getTime());
		refreshBattlePoints();

		// notice
		if (notice) {
			I18nUtils utils = I18nUtils.valueOf("304006").addParm("user", I18nPack.valueOf(player.createSimple()));
			ChatManager.getInstance().sendSystem(2, utils, null, this);
		}

		PacketSendUtility.broadcastPacket(player, SM_Self_Gang_Change.valueOf(player.getObjectId(), this));
		EventBusManager.getInstance().submit(PlayerGangChangeEvent.valueOf(player.getObjectId(), this.id, this.name));
		return member;
	}

	public void refreshBattlePoints() {
		long battle = 0;
		for (Member member : members.values()) {
			battle += member.getBattlePoints();
		}
		this.battle = battle;
	}

	public Apply addApply(Player player) {
		Apply apply = Apply.valueOf(player);
		applies.put(player.getObjectId(), apply);
		return apply;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ConcurrentHashMap<Long, Apply> getApplies() {
		return applies;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public void setApplies(ConcurrentHashMap<Long, Apply> applies) {
		this.applies = applies;
	}

	@JsonIgnore
	public GangEnt getGangEnt() {
		return gangEnt;
	}

	public void setGangEnt(GangEnt gangEnt) {
		this.gangEnt = gangEnt;
	}

	public ConcurrentHashMap<Long, Member> getMembers() {
		return members;
	}

	public void setMembers(ConcurrentHashMap<Long, Member> members) {
		this.members = members;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public long getBattle() {
		return battle;
	}

	public void setBattle(long battle) {
		this.battle = battle;
	}

	public CountryId getCountry() {
		return country;
	}

	public void setCountry(CountryId country) {
		this.country = country;
	}

	public boolean isAutoDeal() {
		return autoDeal;
	}

	public void setAutoDeal(boolean autoDeal) {
		this.autoDeal = autoDeal;
	}

	public List<GangLog> getLogs() {
		return logs;
	}

	public void setLogs(List<GangLog> logs) {
		this.logs = logs;
	}

	public long getMemberImpeachId() {
		return memberImpeachId;
	}

	public void setMemberImpeachId(long memberImpeachId) {
		this.memberImpeachId = memberImpeachId;
	}

	public long getLastModifyInfoTime() {
		return lastModifyInfoTime;
	}

	public void setLastModifyInfoTime(long lastModifyInfoTime) {
		this.lastModifyInfoTime = lastModifyInfoTime;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public long getStartImpeachTime() {
		return startImpeachTime;
	}

	public void setStartImpeachTime(long startImpeachTime) {
		this.startImpeachTime = startImpeachTime;
	}

	@JsonIgnore
	public long getJoinChatTimeMills() {
		return joinChatTimeMills;
	}

	@JsonIgnore
	public void setJoinChatTimeMills(long joinChatTimeMills) {
		this.joinChatTimeMills = joinChatTimeMills;
	}

}
