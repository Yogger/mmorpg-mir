package com.mmorpg.mir.model.welfare.manager;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.resource.SignResource;
import com.mmorpg.mir.model.welfare.util.Util;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class SignManager implements ISignManager {

	private static SignManager instance;

	@Static
	private Storage<Integer, SignResource> signResource;

	@PostConstruct
	public void init() {
		instance = this;
	}

	public static SignManager getInstance() {
		return instance;
	}

	public SignResource getSignResource(int days, boolean throwException) {
		return signResource.get(days, throwException);
	}

	public Collection<SignResource> getAllSignResources() {
		return signResource.getAll();
	}

	/** TODO 满月签到广播 */
	@Deprecated
	public void broadcast(Player player) {
		if (player.getWelfare().getSign().getSize() >= Util.getInstance().getDaysOfMonthForYear(
				player.getWelfare().getSign().getLastTime())) {
			String key = WelfareConfigValueManager.getInstance().SIGN_FULL_BROADCAST.getValue();// getFullSignBroadcast();
			if (key != null && !key.isEmpty()) {
				ChatManager.getInstance().sendSystem(ChatManager.CHANNELID_SYSTEM, null, key, null, null, this);
			}
		}
	}

}
