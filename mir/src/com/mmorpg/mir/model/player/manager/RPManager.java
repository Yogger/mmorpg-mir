package com.mmorpg.mir.model.player.manager;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.player.resource.RPResource;
import com.mmorpg.mir.model.player.resource.RPType;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class RPManager implements IRPManager{
	
	private static RPManager instance;

	@Static("RP:MAX_RP_LIMIT")
	private ConfigValue<Integer> MAX_RP_LIMIT;
	
	@Static("PUBLIC:KILLED_COUNT")
	private ConfigValue<Integer> KILLED_COUNT;
	
	@Static("PUBLIC:KILLED_PERIOD")
	private ConfigValue<Integer> KILLED_PERIOD;
	
	@Static("PUBLIC:COUNTRY_GRAY_SKILLID")
	private ConfigValue<Integer[]> COUNTRY_GRAY_SKILLID;
	
	@Static("PUBLIC:GRAY_DURATION")
	public ConfigValue<Integer> GRAY_DURATION;
	
	@Static
	private Storage<Integer, RPResource> rpResources;
	
	@PostConstruct
	public void init() {
		instance = this;
	}

	public static RPManager getInstance() {
		return instance;
	}
	
	public RPResource getResource(int resourceId) {
		return rpResources.get(resourceId, true);
	}
	
	public RPResource getResource(RPType type) {
		return rpResources.get(type.getValue(), true);
	}
	
	public int getRPMaxLimit() {
		return MAX_RP_LIMIT.getValue();
	}
	
	public int getDailyReduce(int rpId) {
		return getResource(rpId).getDailyReduce();
	}
	
	public int getNotBelow(int rpId) {
		return getResource(rpId).getNotBelow();
	}
	
	public int getKilledCount() {
		return KILLED_COUNT.getValue();
	}
	public int getKilledPeriod() {
		return KILLED_PERIOD.getValue();
	}
	
	public int getGraySkillId(int index) {
		return COUNTRY_GRAY_SKILLID.getValue()[index];
	}
	
	public int getGraySkillSize() {
		return COUNTRY_GRAY_SKILLID.getValue().length;
	}
	
	public RPType getRpType(int rpValue) {
		RPType type = null;
		for (RPType t: RPType.allRpType) {
			if (rpValue >= getNotBelow(t.getValue()))
				type = t;
		}
		
		return type;
	}
}
