package com.mmorpg.mir.model.rescue.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.rescue.resource.RescueResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class RescueConfig {

	@Static("RESCUE:EXPRESCUE")
	public ConfigValue<String> EXPRESCUE;

	@Static("RESCUE:CURRENCYRESCUE")
	public ConfigValue<String> CURRENCYRESCUE;

	@Static("RESCUE:RESCUEITEM_ACTIONS")
	public ConfigValue<String[]> RESCUEITEM_ACTIONS;

	@Static
	private Storage<String, RescueResource> rescueResources;

	public static RescueConfig INSTANCE;

	@PostConstruct
	void init() {
		INSTANCE = this;
	}

	public static RescueConfig getInstance() {
		return INSTANCE;
	}

	public RescueResource getResource(String id) {
		return rescueResources.get(id, true);
	}

	public List<RescueResource> selectRescues(int countryId) {
		RescueResource head = null;

		for (RescueResource rr : rescueResources.getAll()) {
			if (rr.getCountry() == countryId) {
				if (rr.getIndex() == 1) {
					// 开头
					head = rr;
					break;
				}
			}
		}

		RescueResource current = head;
		List<RescueResource> result = new ArrayList<RescueResource>();
		if (current == null) {
			return result;
		}
		result.add(current);
		while (current.getNextId() != null) {
			RescueResource next = rescueResources.get(current.getNextId(), true);
			result.add(next);
			current = next;
		}

		return result;
	}
}
