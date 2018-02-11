package com.mmorpg.mir.model.trigger.model.handle;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;

@Component
public class EmotionTrigger extends AbstractTrigger {

	@Override
	public TriggerType getType() {
		return TriggerType.EMOTION;
	}

	@Override
	public void handle(Map<String, Object> contexts, TriggerResource resource) {
		final Player player = (Player) contexts.get(TriggerContextKey.PLAYER);
		int faceId = Integer.parseInt(resource.getKeys().get(TriggerContextKey.FACE_ID));
		ChatManager.getInstance().sendEmotion(player, faceId,true);
	}
}
