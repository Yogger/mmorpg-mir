package com.mmorpg.mir.model.chat.manager;

import java.util.HashMap;

import com.mmorpg.mir.model.chat.model.handle.ChannelHandle;
import com.mmorpg.mir.model.chat.model.show.ShowHandle;
import com.mmorpg.mir.model.chat.packet.CM_Chat_Request;
import com.mmorpg.mir.model.chat.resource.ChannelResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.windforce.common.resource.Storage;

public interface IChatManager {
	boolean isInBlackList(Long senderId, Long reciverId);

	boolean isInBlackList(Player sender, Player reciver);

	void registerHandle(ChannelHandle handle);

	void registerShowHandle(ShowHandle handle);

	Storage<Integer, ChannelResource> getResources();

	void sendSystem(String content);

	void sendSystem(int channalId, String content, String i18n, HashMap<String, I18nPack> parms,
			HashMap<String, Object> shows, Object... args);

	void sendSystem(int channelId, I18nUtils i18nUtils, HashMap<String, Object> shows, Object... args);

	void send(Player player, CM_Chat_Request request);

	void sendEmotion(Player player, int faceId,boolean systemTrigger);
}
