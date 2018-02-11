package com.mmorpg.mir.model.trigger.model.handle;

import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;

@Component
public class StopLeaveCopyTrigger extends AbstractTrigger {
	@Override
	public TriggerType getType() {
		return TriggerType.STOP_LEAVE_COPY;
	}

	@Override
	public void handle(Map<String, Object> contexts, final TriggerResource resource) {
		final Player player = (Player) contexts.get(TriggerContextKey.PLAYER);
		if (player.getCopyHistory().getCurrentCopyResourceId() == null) {
			return;
		}
		Future<?> future = player.getCopyHistory().getCurrentMapInstance().getCallbackFutures()
				.get(TriggerType.LEAVE_COPY);
		if (future != null && !future.isCancelled()) {
			future.cancel(true);
		}
	}
}
