package com.mmorpg.mir.model.chat.model.show;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chat.model.ShowType;
import com.mmorpg.mir.model.gameobjects.Player;

public abstract class ShowHandle {
	public abstract ShowType getType();

	@Autowired
	private ChatManager chatManager;

	@PostConstruct
	public void init() {
		chatManager.registerShowHandle(this);
	}

	public abstract Object createShowObject(Player player, HashMap<String, String> requestShow);
}
