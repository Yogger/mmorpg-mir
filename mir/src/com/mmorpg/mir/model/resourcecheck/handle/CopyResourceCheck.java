package com.mmorpg.mir.model.resourcecheck.handle;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class CopyResourceCheck extends ResourceCheckHandle {
	@Static
	private Storage<String, CopyResource> copyResources;

	@Static
	private Storage<String, TriggerResource> triggerResources;

	@Static
	private Storage<String, CoreConditionResource> conditionResources;

	@Override
	public Class<?> getResourceClass() {
		return CopyResource.class;
	}

	@Override
	public void check() {
		for (CopyResource tr : copyResources.getAll()) {
			if (tr.getEnterCondtions() != null) {
				for (String conditionId : tr.getEnterCondtions()) {
					CoreConditionResource cr = conditionResources.get(conditionId, false);
					if (cr == null) {
						throw new RuntimeException(String.format("CopyResource id[%s] 进入条件getEnterCondtions[%s]不存在！ ",
								tr.getId(), conditionId));
					}
				}
			}

			if (tr.getBuyConditionIds() != null) {
				for (String conditionId : tr.getBuyConditionIds()) {
					CoreConditionResource cr = conditionResources.get(conditionId, false);
					if (cr == null) {
						throw new RuntimeException(String.format("CopyResource id[%s] 购买条件getBuyConditionIds[%s]不存在！ ",
								tr.getId(), conditionId));
					}
				}
			}

			if (tr.getEncourageConditionIds() != null) {
				for (String conditionId : tr.getEnterCondtions()) {
					CoreConditionResource cr = conditionResources.get(conditionId, false);
					if (cr == null) {
						throw new RuntimeException(String.format(
								"CopyResource id[%s] 鼓舞条件getEncourageConditionIds[%s]不存在！ ", tr.getId(), conditionId));
					}
				}
			}

			if (tr.getRewardConditionIds() != null) {
				for (String conditionId : tr.getRewardConditionIds()) {
					CoreConditionResource cr = conditionResources.get(conditionId, false);
					if (cr == null) {
						throw new RuntimeException(String.format(
								"CopyResource id[%s] 通关领奖条件getRewardConditionIds[%s]不存在！ ", tr.getId(), conditionId));
					}
				}
			}

			if (tr.getTriggers() != null) {
				for (String trigger : tr.getTriggers()) {
					TriggerResource triggerResource = triggerResources.get(trigger, false);
					if (triggerResource == null) {
						throw new RuntimeException(String.format("CopyResource id[%s] 触发器getTriggers[%s]不存在！ ",
								tr.getId(), trigger));
					}
				}
			}

		}
	}

}
