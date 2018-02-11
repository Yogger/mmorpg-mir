package com.mmorpg.mir.model.monsterriot.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.core.condition.BetweenCronTimeCondition;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.monsterriot.resource.MonsterRiotResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class MonsterriotConfig {
	
	@Static("MONSTERWAR:GAMESTART")
	public ConfigValue<String> ACT_START_CRONTIME;
	
	@Static("MONSTERWAR:GAMEEND")
	public ConfigValue<String> ACT_END_CRONTIME;
	
	@Static("MONSTERWAR:GAMESTART_BEFORE_NOTICE")
	public ConfigValue<String> GAMESTART_BEFORE_NOTICE;
	
	@Static("MONSTERRIOT:RANK_MAX_SIZE")
	public ConfigValue<Integer> RANK_MAX_SIZE;
	
	@Static("MONSTERWAR:ACTIVITY_MAP_IDS")
	public ConfigValue<Integer[]> ACTIVITY_MAP_IDS;
	
	@Static("MONSTERWAR:REWARD_RANK_INTERVAL")
	public ConfigValue<Integer[]> REWARD_RANK_INTERVAL;
	
	@Static("MONSTERWAR:ATTEND_LEVEL_LIMIT")
	public ConfigValue<String[]> ATTEND_LEVEL_LIMIT; 
	
	@Static("OPENSERVER:RIOT_MAIL_I18NTITLE")
	public ConfigValue<String> RIOT_MAIL_I18NTITLE;
	
	@Static("OPENSERVER:RIOT_MAIL_I18NCONTENT")
	public ConfigValue<String> RIOT_MAIL_I18NCONTENT;
	
	@Static("OPENSERVER:RIOT_MAIL_REWARD")
	public ConfigValue<String> RIOT_MAIL_REWARD;
	
	@Static("OPENSERVER:RIOT_MAIL_CONDITIONID")
	public ConfigValue<String[]> RIOT_MAIL_CONDITIONID;
	
	@Static
	public Storage<String, MonsterRiotResource> monsterRiotResources;
	
	private CoreConditions betweenTimeCond;
	
	private CoreConditions openServerActTimeCond;
	
	public CoreConditions getPeriodConditions() {
		if (betweenTimeCond == null) {
			CoreConditions conds = new CoreConditions(); 
			BetweenCronTimeCondition cond = CoreConditionType.createBetweenCronTimeCondition(ACT_START_CRONTIME.getValue(), ACT_END_CRONTIME.getValue());
			conds.addConditions(cond);
			betweenTimeCond = conds;
		}
		return betweenTimeCond;
	}

	public static MonsterriotConfig INSTANCE;
	
	@PostConstruct
	void init() {
		INSTANCE = this;
	}
	
	public static MonsterriotConfig getInstance() {
		return INSTANCE;
	}
	
	public MonsterRiotResource getResource(String key) {
		return monsterRiotResources.get(key, true);
	}
	
	public int getMaxRound(int countryValue) {
		int maxRound = 0;
		for (MonsterRiotResource resource: getCountryMonsters(countryValue)) {
			if (resource.getRound() > maxRound) {
				maxRound = resource.getRound();
			}
		}
		return maxRound;
	}
	
	public boolean isLastRound(int countryValue, int currentRound) {
		return currentRound >= getMaxRound(countryValue);
	}
	
	public List<MonsterRiotResource> getCountryMonsters(int countryValue) {
		return monsterRiotResources.getIndex(MonsterRiotResource.COUNTRY_INDEX, countryValue);
	}
	
	public String getMyRankRewardChooser(int rank, int round, int countryValue) {
		int index = 0;
		for (int i = 0; i < REWARD_RANK_INTERVAL.getValue().length; i++) {
			if (rank <= REWARD_RANK_INTERVAL.getValue()[i]) {
				index = i;
				break;
			}
		}
		for (MonsterRiotResource resource : getCountryMonsters(countryValue)) {
			if (resource.getRound() == round) {
				return resource.getRankChooserGroup()[index];
			}
		}
		return null;
	}
	
	private CoreConditions actConditions;
	
	public CoreConditions getActConditions() {
		if (actConditions == null) {
			actConditions = CoreConditionManager.getInstance().getCoreConditions(1, ATTEND_LEVEL_LIMIT.getValue());
		}
		return actConditions;
	}

	public CoreConditions getOpenServerActConditions() {
		if (openServerActTimeCond == null) {
			openServerActTimeCond = CoreConditionManager.getInstance().getCoreConditions(1, RIOT_MAIL_CONDITIONID.getValue());
		}
		return openServerActTimeCond;
	}
}
