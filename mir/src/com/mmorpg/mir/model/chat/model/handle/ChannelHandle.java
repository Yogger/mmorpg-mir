package com.mmorpg.mir.model.chat.model.handle;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chat.model.ChannelType;
import com.mmorpg.mir.model.chat.model.Message;
import com.mmorpg.mir.model.chat.resource.ChannelResource;

public abstract class ChannelHandle {
	public abstract void send(ChannelResource resource, Message message, Object... args);

	public abstract ChannelType getType();

	@Autowired
	private ChatManager chatManager;

	@PostConstruct
	public void init() {
		chatManager.registerHandle(this);
	}

}
