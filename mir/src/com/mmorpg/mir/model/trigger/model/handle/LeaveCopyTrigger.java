package com.mmorpg.mir.model.trigger.model.handle;

import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.copy.service.CopyService;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;
import com.mmorpg.mir.model.utils.ThreadPoolManager;

@Component
public class LeaveCopyTrigger extends AbstractTrigger {

	@Override
	public TriggerType getType() {
		return TriggerType.LEAVE_COPY;
	}

	@Autowired
	private CopyService copyService;

	@Override
	public void handle(Map<String, Object> contexts, final TriggerResource resource) {
		final Player player = (Player) contexts.get(TriggerContextKey.PLAYER);
		if (player.getCopyHistory().getCurrentCopyResourceId() == null) {
			return;
		}

		// 有延迟执行的
		if (resource.getKeys().containsKey(TriggerContextKey.DELAY)) {
			Future<?> future = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					copyService.leaveCopy(player);
				}
			}, Integer.valueOf(resource.getKeys().get(TriggerContextKey.DELAY)));
			player.getCopyHistory().getCurrentMapInstance().getTriggerTasks().add(future);
			player.getCopyHistory().getCurrentMapInstance().getCallbackFutures().put(getType().name(), future);
			return;
		}

		// 立即执行
		copyService.leaveCopy(player);
	}
}
