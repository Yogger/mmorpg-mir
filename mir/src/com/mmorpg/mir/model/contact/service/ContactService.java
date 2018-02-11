package com.mmorpg.mir.model.contact.service;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;

import com.mmorpg.mir.model.gameobjects.Player;

public interface ContactService {

	public void deliverMoodPhrase(Player player, String phrase, int sign) throws UnsupportedEncodingException;

	public void setMapPublic(Player player, boolean offline, boolean head, boolean publicMapInfo, int sign);

	public void addAttentionFriend(Player player, HashSet<Long> friendId);

	public void cancelAttentionFriend(Player player, long targetId);

	public void addEnemy(long playerId, long attackerId);

	public void deleteEnemy(Player player, long enemyId);

	public void updateMySocialInfo(Player player, boolean onlineStatus);

	public boolean updateMyMapInfo(Player player);

	public void notifyMyFans(Player player);

	public void addTargetToBlackList(Player player, long targetId);

	public void removeBlackList(Player player, long targetId);

	public void queryFriends(Player player, String partOfName);

	public void getAllContactInformation(Player player);

	public void setDisbandAddFriends(Player player, boolean disbandAddFriend);
}
