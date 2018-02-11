package com.mmorpg.mir.model.group.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.ArrayUtils;
import org.h2.util.New;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.gameobjects.JourObject;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.MonsterKillEvent;
import com.mmorpg.mir.model.group.manager.GroupManager;
import com.mmorpg.mir.model.group.packet.SM_GROUP_INFO;
import com.mmorpg.mir.model.group.packet.SM_GROUP_LEAVE;
import com.mmorpg.mir.model.group.packet.SM_LEADER_CHANGE;
import com.mmorpg.mir.model.group.packet.SM_NEW_APPLY;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.promote.manager.PromotionManager;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;

public class PlayerGroup extends JourObject {
	public static final int GROUP_CHANNEL_ID = 3;
	public static final long SYSTEM_SENDER_ID = -1;
	private Player groupLeader;
	private Map<Long, Player> groupMembers = new ConcurrentHashMap<Long, Player>();
	private Map<Long, GroupApply> applies = new ConcurrentHashMap<Long, GroupApply>();
	private GroupSimpleVO groupSimplVO;
	private Map<Long, Long> enterTimes = New.hashMap();

	public GroupSimpleVO createSimpleVO() {
		if (groupSimplVO != null) {
			return groupSimplVO;
		}
		this.refreshSimpleVO();
		return groupSimplVO;
	}

	public void refreshSimpleVO() {
		GroupSimpleVO vo = new GroupSimpleVO();
		vo.setLeaderName(groupLeader.getName());
		vo.setLeaderLevel(groupLeader.getLevel());
		vo.setLeaderId(groupLeader.getObjectId());
		int sumLevel = 0;
		int maxLevel = 0;
		int i = 0;
		for (Player player : groupMembers.values()) {
			sumLevel += player.getLevel();
			if (maxLevel < player.getLevel()) {
				maxLevel = player.getLevel();
			}
			i++;
		}
		vo.setMaxLevel(maxLevel);
		vo.setAvgLevel((int) Math.ceil(sumLevel * 1.0 / i));
		vo.setSize(i);
		groupSimplVO = vo;
	}

	public Map<Long, Player> getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(Map<Long, Player> groupMembers) {
		this.groupMembers = groupMembers;
	}

	public void addApply(Player player) {
		if (applies.containsKey(player.getObjectId())) {
			return;
		}
		if (applies.size() >= GroupManager.getInstance().getGroupMaxLimit()) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.FULL_GROUP);
			return;
		}

		GroupApply apply = GroupApply.valueOf(player);
		this.applies.put(player.getObjectId(), apply);
		PacketSendUtility.sendPacket(groupLeader, SM_NEW_APPLY.valueOf(apply.createVO()));
	}

	public void submitHuntEvent(String key, String spawnKey, Set<Player> sended, Player finder, boolean forMedal,
			int level, long mostDamagePlayer, boolean knowPlayer) {
		for (Player member : groupMembers.values()) {
			boolean isSended = sended.contains(member);
			if (SessionManager.getInstance().isOnline(member.getObjectId())) {
				if (!isSended) {
					if (finder == member || finder.getKnownList().knowns(member)) {
						MonsterKillEvent killEvent = MonsterKillEvent.valueOf(member.getObjectId(), key, forMedal,
								level, spawnKey, mostDamagePlayer, knowPlayer);
						EventBusManager.getInstance().submit(killEvent);
						sended.add(member);
					}
				}
			} else if (!isSended) {
				if (finder == member || finder.getKnownList().knowns(member)) {
					MonsterKillEvent killEvent = MonsterKillEvent.valueOf(member.getObjectId(), key, forMedal, level,
							spawnKey, mostDamagePlayer, false);
					EventBusManager.getInstance().submit(killEvent);
					sended.add(member);
				}
			}
		}
	}

	public ArrayList<Player> getFinderKnowMember(Player finder) {
		ArrayList<Player> members = New.arrayList();
		for (Player member : groupMembers.values()) {
			if (SessionManager.getInstance().isOnline(member.getObjectId())) {
				if (!members.contains(member)) {
					if (finder == member || finder.getKnownList().knowns(member)) {
						members.add(member);
					}
				}
			}
		}
		return members;
	}

	public void removeApply(long playerId) {
		this.applies.remove(playerId);
	}

	public GroupApply getApply(long playerId) {
		return applies.get(playerId);
	}

	/**
	 * Instantiates new player group with unique groupId
	 * 
	 * @param groupId
	 */
	public PlayerGroup(long groupId, Player groupleader) {
		this.setObjectId(groupId);
		this.groupMembers.put(groupleader.getObjectId(), groupleader);
		this.setGroupLeader(groupleader);
		groupleader.setPlayerGroup(this);
		enterTimes.put(groupleader.getObjectId(), System.currentTimeMillis() - 1L); // 减一是因为队长和第二队员入队时间有可能一致..
																					// 前端位置不好放..
		// 通知客服端
		PacketSendUtility.sendPacket(groupLeader, SM_GROUP_INFO.valueOf(this));
	}

	/**
	 * @return the groupId
	 */
	public long getGroupId() {
		return this.getObjectId();
	}

	/**
	 * @return the groupLeader
	 */
	public Player getGroupLeader() {
		return groupLeader;
	}

	/**
	 * Used to set group leader
	 * 
	 * @param groupLeader
	 *            the groupLeader to set
	 */
	public void setGroupLeader(Player groupLeader) {
		this.groupLeader = groupLeader;
	}

	/**
	 * Adds player to group
	 * 
	 * @param newComer
	 */
	public void addPlayerToGroup(Player newComer) {
		groupMembers.put(newComer.getObjectId(), newComer);
		enterTimes.put(newComer.getObjectId(), System.currentTimeMillis());
		newComer.setPlayerGroup(this);
		I18nUtils contextl18n = I18nUtils.valueOf("305002");
		contextl18n
				.addParm("user", I18nPack.valueOf(newComer.createSimple()))
				.addParm("turn", I18nPack.valueOf("" + newComer.getSuicide().getTurn()))
				.addParm(I18NparamKey.LEVEL, I18nPack.valueOf("" + newComer.getSuicide().getPlayerLevelAfterTurn()))
				.addParm(
						I18NparamKey.JOB,
						I18nPack.valueOf(PromotionManager.getInstance().getI18nJobName(newComer.getRole(),
								newComer.getPromotion().getStage())));
		ChatManager.getInstance().sendSystem(GROUP_CHANNEL_ID, contextl18n, null, this);
		PacketSendUtility.sendPacket(newComer, SM_GROUP_INFO.valueOf(this));
		refreshSimpleVO();
	}

	public void send(long senderId, Object packet, long... excludes) {
		List<Long> es = Arrays.asList(ArrayUtils.toObject(excludes));
		for (Player player : groupMembers.values()) {
			if (!es.contains(player.getObjectId())) {
				if (senderId != SYSTEM_SENDER_ID
						&& ChatManager.getInstance().isInBlackList(senderId, player.getObjectId()))
					continue;
				if (SessionManager.getInstance().isOnline(player.getObjectId())) {
					PacketSendUtility.sendPacket(player, packet);
				}
			}
		}
	}

	/**
	 * Removes player from group
	 * 
	 * @param player
	 */
	public void removePlayerFromGroup(Player player) {
		// 通知小队成员有人离开了
		send(SYSTEM_SENDER_ID, SM_GROUP_LEAVE.valueOf(player.getObjectId()));

		groupMembers.remove(player.getObjectId());
		enterTimes.remove(player.getObjectId());
		if (player == this.getGroupLeader() && !groupMembers.isEmpty()) {
			Player selectLeader = pickUpNextLeader(player);
			if (selectLeader == null) {
				player.setPlayerGroup(null);
				return;
			}
			setGroupLeader(selectLeader);
			send(SYSTEM_SENDER_ID, SM_LEADER_CHANGE.valueOf(this.getGroupLeader().getObjectId()));
			I18nUtils contextl18n = I18nUtils.valueOf("305001");
			contextl18n
					.addParm("turn", I18nPack.valueOf("" + groupLeader.getSuicide().getTurn()))
					.addParm("user", I18nPack.valueOf(groupLeader.createSimple()))
					.addParm(I18NparamKey.LEVEL, I18nPack.valueOf(groupLeader.getSuicide().getPlayerLevelAfterTurn()))
					.addParm(
							I18NparamKey.JOB,
							I18nPack.valueOf(PromotionManager.getInstance().getI18nJobName(groupLeader.getRole(),
									groupLeader.getPromotion().getStage())));
			ChatManager.getInstance().sendSystem(GROUP_CHANNEL_ID, contextl18n, null, this);
		}
		player.setPlayerGroup(null);
		refreshSimpleVO();
	}

	public Player pickUpNextLeader(Player oldLeader) {
		Long earlierEnter = Long.MAX_VALUE;
		Long pid = 0L;
		for (Entry<Long, Long> entry : enterTimes.entrySet()) {
			if (entry.getKey().equals(oldLeader.getObjectId())) {
				continue;
			}
			if (entry.getValue() < earlierEnter && SessionManager.getInstance().isOnline(entry.getKey())) {
				earlierEnter = entry.getValue();
				pid = entry.getKey();
			}
		}

		if (earlierEnter != Long.MAX_VALUE) {
			return groupMembers.get(pid);
		}
		return null;
	}

	public void disband() {
		this.groupMembers.clear();
		this.applies.clear();
		this.enterTimes.clear();
	}

	// public void onGroupMemberLogIn(Player player) {
	// groupMembers.remove(player.getObjectId());
	// groupMembers.put(player.getObjectId(), player);
	// }

	/**
	 * Checks whether group is full
	 * 
	 * @return true or false
	 */
	public boolean isFull() {
		return groupMembers.size() >= 4;
	}

	public Collection<Player> getMembers() {
		return groupMembers.values();
	}

	public Collection<Long> getMemberObjIds() {
		return groupMembers.keySet();
	}

	/**
	 * @return count of group members
	 */
	public int size() {
		return groupMembers.size();
	}

	@Override
	public String getName() {
		return "Player Group";
	}

	public Map<Long, GroupApply> getApplies() {
		return applies;
	}

	public void setApplies(Map<Long, GroupApply> applies) {
		this.applies = applies;
	}

	public Map<Long, Long> getEnterTimes() {
		return enterTimes;
	}
}
