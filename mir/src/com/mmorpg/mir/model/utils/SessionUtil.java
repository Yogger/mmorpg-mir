package com.mmorpg.mir.model.utils;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.session.SessionManager;
import com.xiaosan.socket.core.TSession;

public class SessionUtil {

	public static Player getPlayerBySession(TSession session) {
		Object identify = session.getAttribute(SessionManager.IDENTITY);
		if (identify != null) {
			return PlayerManager.getInstance().getPlayer((Long) identify);
		}
		return null;
	}

}
