package com.mmorpg.mir.model.group.manager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.RequestHandlerType;
import com.mmorpg.mir.model.gameobjects.RequestResponseHandler;
import com.mmorpg.mir.model.group.model.GroupApply;
import com.mmorpg.mir.model.group.model.PlayerGroup;
import com.mmorpg.mir.model.group.packet.SM_APPLY_LIST_PLAYER;
import com.mmorpg.mir.model.group.packet.SM_DISBAND_GROUP;
import com.mmorpg.mir.model.group.packet.SM_GROUP_COMMON;
import com.mmorpg.mir.model.group.packet.SM_GROUP_INFO;
import com.mmorpg.mir.model.group.packet.SM_GROUP_LIST;
import com.mmorpg.mir.model.group.packet.SM_GROUP_MEMBER_INFO;
import com.mmorpg.mir.model.group.packet.SM_Group_Member_Position;
import com.mmorpg.mir.model.group.packet.SM_LEADER_CHANGE;
import com.mmorpg.mir.model.group.packet.SM_PARTY_HE_BECOME_OFFLINE;
import com.mmorpg.mir.model.group.packet.SM_PARTY_HE_BECOME_ONLINE;
import com.mmorpg.mir.model.group.packet.SM_REJECT_GROUP_INVITE;
import com.mmorpg.mir.model.group.packet.SM_REQUEST_GROUP_INVITE;
import com.mmorpg.mir.model.group.packet.SM_STR_REQUEST_GROUP_INVITE;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.promote.manager.PromotionManager;
import com.mmorpg.mir.model.restrictions.PlayerRestrictions;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.util.IdentifyManager;
import com.mmorpg.mir.model.util.IdentifyManager.IdentifyType;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.packet.SM_Move;
import com.windforce.common.resource.anno.Static;

/**
 * player group
 * 
 * @author Kuang Hao
 * @since v1.0 2014-9-3
 * 
 */
@Component
public class GroupManager {
	/**
	 * Caching group members
	 */
	private final Map<Long, PlayerGroup> groupMembers = new ConcurrentHashMap<Long, PlayerGroup>();

	/**
	 * Caching player apply history
	 */
	private final Map<Long, Set<Long>> playerApplyHistory = new ConcurrentHashMap<Long, Set<Long>>();
	/**
	 * Caching remove group member schedule
	 */
	private Map<Long, Future<?>> playerGroup = new ConcurrentHashMap<Long, Future<?>>();

	@Autowired
	private SessionManager sessionManager;

	@Static("GROUP:GROUP_MAX_LIMIT")
	private ConfigValue<Integer> groupMaxLimit;

	@Static("GROUP:LOGOUT_AUTO_QUITGROUP_TIME")
	private ConfigValue<Integer> LOGOUT_AUTO_QUITGROUP_TIME;

	private static GroupManager instance = new GroupManager();

	@PostConstruct
	void init() {
		instance = this;
	}

	public static GroupManager getInstance() {
		return instance;
	}

	public int getGroupMaxLimit() {
		return groupMaxLimit.getValue();
	}

	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public void getPlayerGroupList(Player player) {
		try {
			lock.readLock().lock();
			if (isGroupMember(player.getObjectId())) {
				return;
			}
			Set<Long> ids = New.hashSet();
			SM_GROUP_LIST sm = new SM_GROUP_LIST();
			for (PlayerGroup pg : this.groupMembers.values()) {
				if ((pg.getGroupLeader().getCountryValue() == player.getCountryValue())
						&& pg.getGroupLeader().getMapId() == player.getMapId() && ids.add(pg.getObjectId())) {
					sm.getVos().add(pg.createSimpleVO());
				}
			}
			PacketSendUtility.sendPacket(player, sm);
		} finally {
			lock.readLock().unlock();
		}
	}

	public void apply(Player player, Player leader) {
		try {
			lock.writeLock().lock();
			if (isGroupMember(player.getObjectId())) {
				PacketSendUtility.sendPacket(player, new SM_GROUP_COMMON(ManagedErrorCode.PLAYER_IN_ANOTHER_GROUP));
				return;
			}
			PlayerGroup playerGroup = getGroup(leader.getObjectId());
			if (playerGroup == null) {
				PacketSendUtility.sendPacket(player, new SM_GROUP_COMMON(ManagedErrorCode.PLAYER_NOT_IN_GROUP));
				return;
			}
			playerGroup.addApply(player);
			Set<Long> applySet = playerApplyHistory.get(player.getObjectId());
			if (applySet == null) {
				applySet = new HashSet<Long>();
				playerApplyHistory.put(player.getObjectId(), applySet);
			}
			applySet.add(leader.getObjectId());
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * This method will add a member to the group member cache
	 * 
	 * @param player
	 */
	private void addGroupMemberToCache(Player player) {
		if (!groupMembers.containsKey(player.getObjectId())) {
			groupMembers.put(player.getObjectId(), player.getPlayerGroup());
			// addMoveObserver(player);
		}
		Set<Long> applySet = playerApplyHistory.get(player.getObjectId());
		if (applySet != null) {
			for (Long leaderId : applySet) {
				PlayerGroup group = groupMembers.get(leaderId);
				if (group != null) {
					group.getApplies().remove(player.getObjectId());
				}
			}
		}
	}

	private void addObserver(final Player player) {
		player.getObserveController().addObserver(new ActionObserver(ObserverType.MOVE) {
			@Override
			public void moved() {
				if (player.getPlayerGroup() == null)
					return;
				byte[] leftRoads = player.getMoveController().getLeftRoads();
				for (Long pid : player.getPlayerGroup().getGroupMembers().keySet()) {
					if (player.getObjectId().equals(pid) || (!sessionManager.isOnline(pid)))
						continue;
					Player otherPlayer = PlayerManager.getInstance().getPlayer(pid);
					boolean notSamePlace = (otherPlayer.getMapId() != player.getMapId())
							|| (otherPlayer.getInstanceId() != player.getInstanceId());
					if (otherPlayer == null || notSamePlace)
						continue;
					PacketSendUtility.sendPacket(otherPlayer,
							SM_Move.valueOf(player, player.getX(), player.getY(), leftRoads, (byte) 1));
				}
			}

			@Override
			public void spawn(int mapId, int instanceId) {
				if (player.getPlayerGroup() == null)
					return;
				for (Long pid : player.getPlayerGroup().getGroupMembers().keySet()) {
					if (player.getObjectId().equals(pid) || (!sessionManager.isOnline(pid)))
						continue;
					Player otherPlayer = PlayerManager.getInstance().getPlayer(pid);
					if (otherPlayer != null) {
						PacketSendUtility.sendPacket(otherPlayer, SM_Group_Member_Position.valueOf(player));
					}
				}
			}
		});
	}

	private void removeGroupMemberFromCache(long playerObjId) {
		if (groupMembers.containsKey(playerObjId))
			groupMembers.remove(playerObjId);
	}

	/**
	 * @param playerObjId
	 * @return returns true if player is in the cache
	 */
	public boolean isGroupMember(long playerObjId) {
		return groupMembers.containsKey(playerObjId);
	}

	/**
	 * Returns the player's group
	 * 
	 * @param playerObjId
	 * @return PlayerGroup
	 */
	private PlayerGroup getGroup(long playerObjId) {
		return groupMembers.get(playerObjId);
	}

	private PlayerGroup load(Player player) {
		PlayerGroup playerGroup = this.getGroup(player.getObjectId());
		if (playerGroup == null) {
			PacketSendUtility.sendPacket(player, new SM_GROUP_COMMON(ManagedErrorCode.PLAYER_IN_ANOTHER_GROUP));
			return null;
		}
		return playerGroup;
	}

	public void getAppyList(Player player) {
		try {
			lock.readLock().lock();
			PlayerGroup playerGroup = load(player);
			if (playerGroup == null) {
				return;
			}
			PacketSendUtility.sendPacket(player, SM_APPLY_LIST_PLAYER.valueOf(playerGroup));
		} finally {
			lock.readLock().unlock();
		}
	}

	private void disbandGroup(PlayerGroup group) {
		for (Player member : group.getMembers()) {
			member.setPlayerGroup(null);
			removeGroupMemberFromCache(member.getObjectId());
			cancelScheduleRemove(member.getObjectId());
			PacketSendUtility.sendPacket(member, new SM_DISBAND_GROUP());
		}
		group.setGroupLeader(null);
		group.disband();
	}

	/**
	 * @param player
	 */
	public void onLogin(Player activePlayer) {
		try {
			lock.writeLock().lock();
			addObserver(activePlayer);
			final PlayerGroup group = activePlayer.getPlayerGroup();
			if (!isGroupMember(activePlayer.getObjectId()))
				return;
			cancelScheduleRemove(activePlayer.getObjectId());
			PacketSendUtility.sendPacket(activePlayer, SM_GROUP_INFO.valueOf(group));
			group.send(PlayerGroup.SYSTEM_SENDER_ID, SM_PARTY_HE_BECOME_ONLINE.valueOf(activePlayer),
					activePlayer.getObjectId());
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * @param playerGroupCache
	 *            the playerGroupCache to set
	 */
	private void addPlayerGroupCache(long playerObjId, Future<?> future) {
		if (!playerGroup.containsKey(playerObjId))
			playerGroup.put(playerObjId, future);
	}

	/**
	 * This method will remove a schedule to remove a player from a group
	 * 
	 * @param playerObjId
	 */
	public void cancelScheduleRemove(long playerObjId) {
		if (playerGroup.containsKey(playerObjId)) {
			playerGroup.get(playerObjId).cancel(false);
			playerGroup.remove(playerObjId);
		}
	}

	/**
	 * @param player
	 */
	public void removePlayerFromGroup(Player player, boolean force) {
		try {
			lock.writeLock().lock();
			if (player.isInGroup()) {
				final PlayerGroup group = player.getPlayerGroup();
				boolean isLeader = group.getGroupLeader().equals(player);
				boolean noOneOnlineExceptSelf = true; // 就自己一个人在线
				if (isLeader) {
					for (Player member : group.getMembers()) {
						if (member.getObjectId().equals(player.getObjectId())) {
							continue;
						}
						if (sessionManager.isOnline(member.getObjectId())) {
							noOneOnlineExceptSelf = false;
						}
					}
				}

				if (group.size() == 1 || (isLeader && noOneOnlineExceptSelf)) {
					I18nUtils utils = I18nUtils.valueOf("305005")
							.addParm("turn", I18nPack.valueOf("" + player.getSuicide().getTurn()))
							.addParm("user", I18nPack.valueOf(player.createSimple()))
							.addParm(I18NparamKey.LEVEL,
									I18nPack.valueOf(player.getSuicide().getPlayerLevelAfterTurn()))
							.addParm(I18NparamKey.JOB, I18nPack.valueOf(PromotionManager.getInstance()
									.getI18nJobName(player.getRole(), player.getPromotion().getStage())));
					ChatManager.getInstance().sendSystem(PlayerGroup.GROUP_CHANNEL_ID, utils, null, group);
				} else {
					if (force) { // 被T了
						// notice
						I18nUtils utils = I18nUtils.valueOf("305003")
								.addParm("turn", I18nPack.valueOf("" + player.getSuicide().getTurn()))
								.addParm("user", I18nPack.valueOf(player.createSimple()))
								.addParm(I18NparamKey.LEVEL,
										I18nPack.valueOf(player.getSuicide().getPlayerLevelAfterTurn()))
								.addParm(I18NparamKey.JOB, I18nPack.valueOf(PromotionManager.getInstance()
										.getI18nJobName(player.getRole(), player.getPromotion().getStage())));
						ChatManager.getInstance().sendSystem(PlayerGroup.GROUP_CHANNEL_ID, utils, null, group);
					} else { // 自己要退的
						// notice
						I18nUtils utils = I18nUtils.valueOf("305004")
								.addParm("turn", I18nPack.valueOf("" + player.getSuicide().getTurn()))
								.addParm("user", I18nPack.valueOf(player.createSimple()))
								.addParm(I18NparamKey.LEVEL,
										I18nPack.valueOf(player.getSuicide().getPlayerLevelAfterTurn()))
								.addParm(I18NparamKey.JOB, I18nPack.valueOf(PromotionManager.getInstance()
										.getI18nJobName(player.getRole(), player.getPromotion().getStage())));
						ChatManager.getInstance().sendSystem(PlayerGroup.GROUP_CHANNEL_ID, utils, null, group);
					}
				}

				if ((isLeader && group.size() <= 1) || (isLeader && noOneOnlineExceptSelf)) {
					disbandGroup(group);
				} else {
					group.removePlayerFromGroup(player);
				}
				removeGroupMemberFromCache(player.getObjectId());
				cancelScheduleRemove(player.getObjectId());
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void exple(Player leader, Player target) {
		try {
			lock.writeLock().lock();
			if (leader.getPlayerGroup().getGroupLeader() != leader) {
				PacketSendUtility.sendPacket(leader,
						new SM_GROUP_COMMON(ManagedErrorCode.ONLY_GROUP_LEADER_CAN_INVITE));
				return;
			}
			if (leader.getPlayerGroup() != target.getPlayerGroup()) {
				PacketSendUtility.sendPacket(leader, new SM_GROUP_COMMON(ManagedErrorCode.PLAYER_NOT_IN_GROUP));
				return;
			}
			removePlayerFromGroup(target, true);

		} finally {
			lock.writeLock().unlock();
		}
	}

	public static void main(String[] args) {
		ReentrantReadWriteLock lock1 = new ReentrantReadWriteLock();
		lock1.writeLock().lock();
		lock1.writeLock().lock();
		lock1.writeLock().unlock();
		lock1.writeLock().unlock();
	}

	public void changeLeader(Player leader, Player target) {
		try {
			lock.writeLock().lock();
			if (leader.getPlayerGroup().getGroupLeader() != leader) {
				PacketSendUtility.sendPacket(leader,
						new SM_GROUP_COMMON(ManagedErrorCode.ONLY_GROUP_LEADER_CAN_INVITE));
				return;
			}
			if (!leader.getPlayerGroup().equals(target.getPlayerGroup())) {
				PacketSendUtility.sendPacket(leader, new SM_GROUP_COMMON(ManagedErrorCode.PLAYER_NOT_IN_GROUP));
				return;
			}
			leader.getPlayerGroup().setGroupLeader(target);
			target.getPlayerGroup().send(PlayerGroup.SYSTEM_SENDER_ID, SM_LEADER_CHANGE.valueOf(target.getObjectId()));
			target.getPlayerGroup().refreshSimpleVO();
			// notice
			I18nUtils contextl18n = I18nUtils.valueOf("305001");
			contextl18n.addParm("user", I18nPack.valueOf(target.createSimple()))
					.addParm("turn", I18nPack.valueOf("" + target.getSuicide().getTurn()))
					.addParm(I18NparamKey.LEVEL, I18nPack.valueOf(target.getSuicide().getPlayerLevelAfterTurn()))
					.addParm(I18NparamKey.JOB, I18nPack.valueOf(PromotionManager.getInstance()
							.getI18nJobName(target.getRole(), target.getPromotion().getStage())));
			ChatManager.getInstance().sendSystem(PlayerGroup.GROUP_CHANNEL_ID, contextl18n, null,
					leader.getPlayerGroup());
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * This method will create a schedule to remove a player from a group
	 * 
	 * @param player
	 */
	public void scheduleRemove(final Player player) {
		ScheduledFuture<?> future = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				removePlayerFromGroup(player, false);
				playerGroup.remove(player.getObjectId());
			}
		}, LOGOUT_AUTO_QUITGROUP_TIME.getValue());
		addPlayerGroupCache(player.getObjectId(), future);
		// player.getPlayerGroup().getMembers().remove(player.getObjectId());

		for (Player groupMember : player.getPlayerGroup().getMembers()) {
			if (groupMember.getObjectId().longValue() == player.getObjectId().longValue()) {
				continue;
			}
			PacketSendUtility.sendPacket(groupMember, SM_PARTY_HE_BECOME_OFFLINE.valueOf(player));
		}

		try {
			lock.writeLock().lock();
			PlayerGroup group = player.getPlayerGroup();
			if (group.getGroupLeader().equals(player)) {
				group.getApplies().clear();
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void dealApply(Player leader, long invitedId, boolean ok) {
		try {
			lock.writeLock().lock();
			if (!leader.isInGroup()) {
				PacketSendUtility.sendPacket(leader, new SM_GROUP_COMMON(ManagedErrorCode.PLAYER_NOT_IN_GROUP));
				return;
			}
			GroupApply apply = leader.getPlayerGroup().getApply(invitedId);
			if (apply == null) {
				PacketSendUtility.sendPacket(leader, new SM_GROUP_COMMON(ManagedErrorCode.PLAYER_NOT_APPLY));
				return;
			}
			Player invited = PlayerManager.getInstance().getPlayer(invitedId);
			if (ok) {
				if (PlayerRestrictions.getInstance().canInviteToGroup(leader, invited)) {
					invitePlayer(leader, invited, false);
				}
			} else {
				PacketSendUtility.sendPacket(invited, SM_REJECT_GROUP_INVITE.valueOf(leader.getName(), 1));
			}
			leader.getPlayerGroup().removeApply(invitedId);
		} finally {
			lock.writeLock().unlock();
		}
	}

	private void invitePlayer(Player inviter, Player invited, boolean isAsk) {
		PlayerGroup group = inviter.getPlayerGroup();
		if (group != null && group.isFull()) {
			if (isAsk) {
				PacketSendUtility.sendErrorMessage(inviter, ManagedErrorCode.FULL_GROUP);
			} else {
				PacketSendUtility.sendErrorMessage(invited, ManagedErrorCode.FULL_GROUP);
			}
			return;
		}

		if (inviter.getCountryValue() != invited.getCountryValue()) {
			PacketSendUtility.sendErrorMessage(invited, ManagedErrorCode.COUNTRY_NOT_SAME);
			return;
		}

		PacketSendUtility.sendPacket(inviter, SM_REQUEST_GROUP_INVITE.valueOf(invited.getName()));
		PlayerGroup invitedGroup = invited.getPlayerGroup();
		if (invitedGroup != null) {
			removePlayerFromGroup(invited, false);
		}
		if (group != null) {
			inviter.getPlayerGroup().addPlayerToGroup(invited);
			addGroupMemberToCache(invited);
		} else {
			group = new PlayerGroup(IdentifyManager.getInstance().getNextIdentify(IdentifyType.GROUP), inviter);
			inviter.getPlayerGroup().addPlayerToGroup(invited);
			addGroupMemberToCache(inviter);
			addGroupMemberToCache(invited);
		}
		group.removeApply(invited.getObjectId());
		group.send(PlayerGroup.SYSTEM_SENDER_ID,
				SM_GROUP_MEMBER_INFO.valueOf(group, invited, sessionManager.isOnline(invited.getObjectId())),
				invited.getObjectId());
	}

	public void createPlayerGroup(Player inviter) {
		try {
			lock.writeLock().lock();
			if (isGroupMember(inviter.getObjectId())) {
				PacketSendUtility.sendPacket(inviter, new SM_GROUP_COMMON(ManagedErrorCode.PLAYER_IN_ANOTHER_GROUP));
				return;
			}
			new PlayerGroup(IdentifyManager.getInstance().getNextIdentify(IdentifyType.GROUP), inviter);
			addGroupMemberToCache(inviter);
		} finally {
			lock.writeLock().unlock();
		}

	}

	/**
	 * This method will handle everything to a player that is invited for a group
	 * 
	 * @param inviter
	 * @param invited
	 */
	public void invitePlayerToGroup(final Player inviter, final Player invited) {
		// 组队验证
		if (PlayerRestrictions.getInstance().canInviteToGroup(inviter, invited)) {
			RequestResponseHandler responseHandler = new RequestResponseHandler(inviter) {
				@Override
				public void acceptRequest(Creature requester, Player responder) {
					try {
						lock.writeLock().lock();
						invitePlayer(inviter, invited, true);
					} finally {
						lock.writeLock().unlock();
					}
				}

				@Override
				public void denyRequest(Creature requester, Player responder) {
					PacketSendUtility.sendPacket(inviter, SM_REJECT_GROUP_INVITE.valueOf(responder.getName(), 0));
				}

				@Override
				public boolean deprecated() {
					return invited.isInGroup();
				}
			};

			boolean result = invited.getRequester().putRequest(RequestHandlerType.GROUP_INVITE, responseHandler);
			if (result) {
				PacketSendUtility.sendPacket(invited, SM_STR_REQUEST_GROUP_INVITE.valueOf(inviter));
			}
		}
	}
}
