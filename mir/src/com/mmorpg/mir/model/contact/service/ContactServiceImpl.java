package com.mmorpg.mir.model.contact.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.clientdb.packet.SM_CLIENT_SETTINGS;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.contact.entity.ContactEnt;
import com.mmorpg.mir.model.contact.manager.ContactManager;
import com.mmorpg.mir.model.contact.model.ContactRelationData;
import com.mmorpg.mir.model.contact.model.EnemyVO;
import com.mmorpg.mir.model.contact.model.SocialNetData;
import com.mmorpg.mir.model.contact.packet.SM_Add_Contact;
import com.mmorpg.mir.model.contact.packet.SM_DELETE_CONTACT;
import com.mmorpg.mir.model.contact.packet.SM_FANS_MESSAGE;
import com.mmorpg.mir.model.contact.packet.SM_GET_CONTACTINFO;
import com.mmorpg.mir.model.contact.packet.SM_Player_Social_Data;
import com.mmorpg.mir.model.contact.packet.SM_Player_Social_Login;
import com.mmorpg.mir.model.contact.packet.SM_Query_Friends;
import com.mmorpg.mir.model.contact.packet.SM_Update_Enemy;
import com.mmorpg.mir.model.dirtywords.model.WordsType;
import com.mmorpg.mir.model.dirtywords.service.DirtyWordsManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.utils.CharCheckUtil;
import com.windforce.common.resource.anno.Static;

@Component
public class ContactServiceImpl implements ContactService {

	public static final int SELF_SOCIALNET_DATA = 1;
	public static final int ATTENTION_SOCIALNET_DATA = 2;
	public static final int ENEMY_SOCIAL_DATA = 3;
	public static final int BLACKLIST_SOCIAL_DATA = 4;

	@Static("CONTACT:MOOD_PHRASE_LEN_LIMIT")
	private ConfigValue<Integer> MOOD_PHRASE_LEN_LIMIT;
	@Static("CONTACT:QUERY_MAX_LENGTH")
	private ConfigValue<Integer> QUERY_MAX_LENGTH;
	@Static("CONTACT:DELETE_ENEMIES_DURATION")
	private ConfigValue<Integer> DELETE_ENEMIES_DURATION;

	@Autowired
	private ContactManager contactManager;

	public void deliverMoodPhrase(Player player, String phrase, int sign) throws UnsupportedEncodingException {
		checkStringParam(phrase, MOOD_PHRASE_LEN_LIMIT.getValue());
		String filerPhrase = CharCheckUtil.filterOffUtf8Mb4(phrase);
		contactManager.getMySocialNetData(player.getObjectId()).setMoodPhrase(filerPhrase);
		contactManager.updateContact(player.getObjectId());
		// 通知关注我的人
		notifyMyFans(player);
		PacketSendUtility.sendSignMessage(player, sign);
	}

	public void setMapPublic(Player player, boolean offline, boolean head, boolean publicMapInfo, int sign) {
		ContactEnt contactEnt = contactManager.getContactEnt(player.getObjectId());
		SocialNetData data = contactEnt.getMySocialData();
		contactEnt.setDisplayOnline(offline);
		contactEnt.setShowHead(head);
		boolean notify = contactEnt.isOpenMapInfo();
		contactEnt.setOpenMapInfo(publicMapInfo);
		if (publicMapInfo) {
			data.setMapId(player.getMapId());
			data.setMapInstanceId(player.getInstanceId());
			if (!notify) { // 地图信息由不公开改成公开
				notifyMyFans(player);
			}
		} else {
			data.setMapId(0);
		}
		contactManager.updateContact(contactEnt);
		PacketSendUtility.sendSignMessage(player, sign);
	}

	public void addAttentionFriend(Player player, HashSet<Long> friendId) {
		if (friendId.contains(player.getObjectId())) {
			throw new ManagedException(ManagedErrorCode.SYS_ERROR);
		}
		ContactEnt pContactEnt = contactManager.getContactEnt(player.getObjectId());
		ContactRelationData relationData = pContactEnt.getMyRelationData();
		if (relationData.getAttentionGuys().size() >= ContactManager.getInstance().getAttentionSizeLimit()) {
			throw new ManagedException(ManagedErrorCode.FRIEND_NUMS_LIMIT);
		}
		List<ContactEnt> ents = New.arrayList();
		for (Long fid : friendId) {
			ContactEnt fContactEnt = contactManager.getContactEnt(fid);
			checkClientSendTarget(fContactEnt);
			if (relationData.getBlackList().containsKey(fid) || relationData.getEnemies().containsKey(fid)) {
				throw new ManagedException(ManagedErrorCode.TARGET_IN_OTHER_RELATIONLIST);
			}
			if (pContactEnt.getMySocialData().getCountryValue() != fContactEnt.getMySocialData().getCountryValue()) {
				throw new ManagedException(ManagedErrorCode.FRIEND_NOT_SAME_COUNTRY);
			}
			if (relationData.getAttentionGuys().containsKey(fid)) {
				throw new ManagedException(ManagedErrorCode.CONTACT_ALREADY_EXIST);
			}
			if (fContactEnt.isDisbandAddFriends()) {
				throw new ManagedException(ManagedErrorCode.TARGET_DISBAND_ADD_FRIEND);
			}
			ents.add(fContactEnt);
		}
		ArrayList<SocialNetData> contactInfos = New.arrayList();
		for (ContactEnt fContact : ents) {
			relationData.getAttentionGuys().put(fContact.getId(), 0);
			fContact.getMyRelationData().getFans().add(player.getObjectId());
			contactManager.updateContact(fContact);
			contactInfos.add(fContact.getMySocialData());
			if (SessionManager.getInstance().isOnline(fContact.getId())) {
				Player target = PlayerManager.getInstance().getPlayer(fContact.getId());
				PacketSendUtility.sendPacket(target,
						SM_FANS_MESSAGE.valueOf(fContact.getMyRelationData().getFans().size()));
			}
		}
		contactManager.updateContact(pContactEnt);
		PacketSendUtility.sendPacket(player, SM_Add_Contact.valueOf(ATTENTION_SOCIALNET_DATA, contactInfos));
	}

	public void cancelAttentionFriend(Player player, long targetId) {
		ContactRelationData data = contactManager.getMyContactRelationData(player.getObjectId());
		ContactEnt target = contactManager.getContactEnt(targetId);
		Integer remove = data.getAttentionGuys().remove(targetId);
		checkClientSendTarget(target);
		checkClientSendTarget(remove);
		target.getMyRelationData().getFans().remove(player.getObjectId());
		if (SessionManager.getInstance().isOnline(targetId)) {
			Player targetPlayer = PlayerManager.getInstance().getPlayer(targetId);
			PacketSendUtility.sendPacket(targetPlayer,
					SM_FANS_MESSAGE.valueOf(target.getMyRelationData().getFans().size()));
		}
		contactManager.updateContact(player.getObjectId());
		PacketSendUtility.sendPacket(player, SM_DELETE_CONTACT.valueOf(targetId, ATTENTION_SOCIALNET_DATA));
	}

	public void addEnemy(long playerId, long attackerId) {
		Player player = PlayerManager.getInstance().getPlayer(playerId);
		ContactRelationData data = contactManager.getMyContactRelationData(playerId);
		ContactEnt enemyEnt = contactManager.getContactEnt(attackerId);
		// 在黑名单里面 不处理
		if (data.getBlackList().containsKey(attackerId)) {
			return;
		}
		boolean isNewEnemy = data.addOrUpdateEnemy(player, attackerId);
		enemyEnt.getMyRelationData().slaughtCount(playerId);
		if (isNewEnemy) {
			ArrayList<SocialNetData> contactInfos = New.arrayList();
			contactInfos.add(enemyEnt.getMySocialData());
			PacketSendUtility.sendPacket(player, SM_Add_Contact.valueOf(ENEMY_SOCIAL_DATA, contactInfos));
		} else {
			PacketSendUtility.sendPacket(
					player,
					SM_Update_Enemy.valueOf(attackerId, data.getSlaughtered().get(attackerId),
							data.getEnemies().get(attackerId)));
		}

		contactManager.updateContact(playerId);
		contactManager.updateContact(enemyEnt);
	}

	public void deleteEnemy(Player player, long enemyId) {
		ContactRelationData data = contactManager.getMyContactRelationData(player.getObjectId());
		ContactEnt enemyEnt = contactManager.getContactEnt(enemyId);
		checkClientSendTarget(enemyEnt);
		if (!data.getLastRecentlyEnemies().contains(enemyId)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		data.deleteEnemy(player, enemyId);
		enemyEnt.getMyRelationData().getHater().remove(player.getObjectId());
		contactManager.updateContact(player.getObjectId());
		contactManager.updateContact(enemyEnt);

	}

	public void updateMySocialInfo(Player player, boolean onlineStatus) {
		ContactEnt contactEnt = contactManager.getContactEnt(player.getObjectId());
		if (contactEnt == null) {
			contactEnt = contactManager.loadOrCreate(player.getObjectId());
			contactEnt.getMySocialData().initData(player);
		}
		SocialNetData data = contactEnt.getMySocialData();
		if (contactEnt.isOpenMapInfo() && player.getPosition() != null) {
			data.setMapId(player.getMapId());
			data.setMapInstanceId(player.getInstanceId());
		}
		data.setOnlineStatus(onlineStatus);
		data.setLevel(player.getLevel());
		data.setPromotionId(player.getPromotion().getStage());
		data.setCountryValue(player.getCountryValue());

		if (onlineStatus) {
			PacketSendUtility.sendPacket(player, SM_Player_Social_Login.valueOf(contactEnt, data));
		} else {
			// remove deprecated
			removeInvalidEnemy(contactEnt, player);
		}
		contactManager.updateContact(contactEnt);
	}

	private void removeInvalidEnemy(ContactEnt contactEnt, Player player) {
		ContactRelationData relationData = contactEnt.getMyRelationData();
		ArrayList<Long> removeIds = New.arrayList();
		long now = System.currentTimeMillis();
		for (Entry<Long, Long> entry : relationData.getEnemiesLogTime().entrySet()) {
			if (entry.getValue() + DELETE_ENEMIES_DURATION.getValue() < now) {
				removeIds.add(entry.getKey());
			}
		}
		for (Long removeId : removeIds) {
			if (!relationData.getLastRecentlyEnemies().contains(removeId)) {
				relationData.getEnemiesLogTime().remove(removeId);
				relationData.getEnemies().remove(removeId);
				ContactEnt enemyEnt = contactManager.getContactEnt(removeId);
				enemyEnt.getMyRelationData().getSlaughtered().remove(player.getObjectId());
				contactManager.updateContact(enemyEnt);
			}
		}
	}

	public boolean updateMyMapInfo(Player player) {
		ContactEnt ent = contactManager.getContactEnt(player.getObjectId());
		if (ent.isOpenMapInfo()) {
			ent.getMySocialData().setMapId(player.getMapId());
			ent.getMySocialData().setMapInstanceId(player.getInstanceId());
			return true;
		}
		return false;
	}

	public void notifyMyFans(Player player) {
		ContactEnt contactEnt = contactManager.getContactEnt(player.getObjectId());
		for (Long id : contactEnt.getMyRelationData().getFans()) {
			if (SessionManager.getInstance().isOnline(id)) {
				Player target = PlayerManager.getInstance().getPlayer(id);
				PacketSendUtility.sendPacket(target,
						SM_Player_Social_Data.valueOf(ATTENTION_SOCIALNET_DATA, contactEnt.getMySocialData()));
			}
		}
		for (Long id : contactEnt.getMyRelationData().getSlaughtered().keySet()) {
			if (SessionManager.getInstance().isOnline(id)) {
				Player target = PlayerManager.getInstance().getPlayer(id);
				PacketSendUtility.sendPacket(target,
						SM_Player_Social_Data.valueOf(ENEMY_SOCIAL_DATA, contactEnt.getMySocialData()));
			}
		}
		for (Long id : contactEnt.getMyRelationData().getShield()) {
			if (SessionManager.getInstance().isOnline(id)) {
				Player target = PlayerManager.getInstance().getPlayer(id);
				PacketSendUtility.sendPacket(target,
						SM_Player_Social_Data.valueOf(BLACKLIST_SOCIAL_DATA, contactEnt.getMySocialData()));
			}
		}
	}

	public void addTargetToBlackList(Player player, long targetId) {
		if (targetId == player.getObjectId().longValue()) {
			throw new ManagedException(ManagedErrorCode.CONTACT_TARGET_NOT_EXIST);
		}
		ContactEnt contactEnt = contactManager.getContactEnt(player.getObjectId());
		ContactEnt targetEnt = contactManager.getContactEnt(targetId);
		ContactRelationData data = contactEnt.getMyRelationData();
		checkClientSendTarget(targetEnt);
		if (data.getBlackList().size() >= ContactManager.getInstance().getBlacklistSizeLimit()) {
			throw new ManagedException(ManagedErrorCode.FRIEND_NUMS_LIMIT);
		}
		if (data.getBlackList().containsKey(targetId)) {
			throw new ManagedException(ManagedErrorCode.CONTACT_ALREADY_EXIST);
		}
		if (data.getAttentionGuys().containsKey(targetId)) {
			throw new ManagedException(ManagedErrorCode.ADD_FRIEND_TO_BLACKLIST);
		}
		if (data.getEnemies().containsKey(targetId)) {
			data.deleteEnemy(player, targetId);
			targetEnt.getMyRelationData().getHater().remove(player.getObjectId());
		}

		data.getBlackList().put(targetId, 1);
		targetEnt.getMyRelationData().getShield().add(player.getObjectId());
		contactManager.updateContact(contactEnt);
		contactManager.updateContact(targetEnt);
		ArrayList<SocialNetData> contactInfos = New.arrayList();
		contactInfos.add(targetEnt.getMySocialData());
		PacketSendUtility.sendPacket(player, SM_Add_Contact.valueOf(BLACKLIST_SOCIAL_DATA, contactInfos));
	}

	public void removeBlackList(Player player, long targetId) {
		ContactEnt contactEnt = contactManager.getContactEnt(player.getObjectId());
		ContactEnt targetEnt = contactManager.getContactEnt(targetId);
		checkClientSendTarget(targetEnt);
		contactEnt.getMyRelationData().getBlackList().remove(targetId);
		targetEnt.getMyRelationData().getShield().remove(player.getObjectId());
		contactManager.updateContact(contactEnt);
		contactManager.updateContact(targetEnt);
		PacketSendUtility.sendPacket(player, SM_DELETE_CONTACT.valueOf(targetId, BLACKLIST_SOCIAL_DATA));
	}

	public void queryFriends(Player player, String partOfName) {
		checkStringParam(partOfName, QUERY_MAX_LENGTH.getValue());
		ArrayList<PlayerSimpleInfo> list = new ArrayList<PlayerSimpleInfo>();
		for (Map.Entry<String, Player> entry : player.getCountry().getNameCivils().entrySet()) {
			if (entry.getKey().contains(partOfName))
				list.add(entry.getValue().createSimple());
			if (list.size() >= QUERY_MAX_LENGTH.getValue())
				break;
		}
		int i = 0;
		boolean found = false;
		for (; i < list.size(); i++) {
			if (list.get(i).getPlayerId() == player.getObjectId()) {
				found = true;
				break;
			}
		}
		if (found)
			list.remove(i);
		PacketSendUtility.sendPacket(player, SM_Query_Friends.valueOf(list));
	}

	public void getAllContactInformation(Player player) {
		ContactRelationData data = contactManager.getMyContactRelationData(player.getObjectId());
		Map<Long, SocialNetData> friends = New.hashMap();
		Map<Long, SocialNetData> enemies = New.hashMap();
		Map<Long, EnemyVO> enmityMap = New.hashMap();
		Map<Long, SocialNetData> blackList = New.hashMap();

		for (Long pid : data.getAttentionGuys().keySet()) {
			friends.put(pid, contactManager.getContactEnt(pid).getMySocialData());
		}

		for (Long pid : data.getLastRecentlyEnemies()) {
			ContactEnt enemyEnt = contactManager.getContactEnt(pid);
			enemies.put(pid, enemyEnt.getMySocialData());
			enmityMap.put(pid, EnemyVO.valueOf(pid, data.getSlaughtered().get(pid), data.getEnemies().get(pid)));
		}

		for (Long pid : data.getBlackList().keySet()) {
			blackList.put(pid, contactManager.getContactEnt(pid).getMySocialData());
		}

		PacketSendUtility.sendPacket(player, SM_GET_CONTACTINFO.valueOf(friends, enemies, enmityMap, blackList));
	}

	private void checkStringParam(String clientParam, int maxLen) {
		if (clientParam == null || clientParam.isEmpty() || clientParam.length() > maxLen)
			throw new ManagedException(ManagedErrorCode.CLIENT_SEND_STRING_ILLEAGAL);
		if (DirtyWordsManager.getInstance().containsWords(clientParam, WordsType.ROLEWORDS)) {
			throw new ManagedException(ManagedErrorCode.WORDS_SENSITIVE);
		}
	}

	private void checkClientSendTarget(Object targetEnt) {
		if (targetEnt == null) {
			throw new ManagedException(ManagedErrorCode.CONTACT_TARGET_NOT_EXIST);
		}
	}

	public void setDisbandAddFriends(Player player, boolean disbandAddFriend) {
		ContactEnt contactEnt = contactManager.getContactEnt(player.getObjectId());
		contactEnt.setDisbandAddFriends(disbandAddFriend);
		contactManager.updateContact(contactEnt);
		PacketSendUtility.sendPacket(player, SM_CLIENT_SETTINGS.valueOf());
	}
}
