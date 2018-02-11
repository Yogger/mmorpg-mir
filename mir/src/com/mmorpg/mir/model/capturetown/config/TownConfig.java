package com.mmorpg.mir.model.capturetown.config;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.capturetown.model.TownType;
import com.mmorpg.mir.model.capturetown.resource.TownResource;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.FormulaParmsUtil;
import com.mmorpg.mir.model.core.action.CoreActionType;
import com.mmorpg.mir.model.core.action.CurrencyAction;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.formula.Formula;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.New;

@Component
public class TownConfig {
	
	@Static
	public Storage<String, TownResource> townResources;
	
	private static TownConfig townConfig;
	
	@PostConstruct
	void init() {
		townConfig = this;
	}

	public static TownConfig getInstance() {
		return townConfig;
	}

	public TownResource getTownResource(String key) {
		return townResources.get(key, true);
	}
	
	@Static("CAPTURETOWN:PLAYER_ENTER_DAILY_LIMIT")
	public ConfigValue<Integer> PLAYER_ENTER_DAILY_LIMIT;
	
	@Static("CAPTURETOWN:BUY_CHALLENGE_COUNT_ACTION")
	public Formula BUY_CHALLENGE_COUNT_ACTION;
	
	@Static("CAPTURETOWN:BUY_RESETCD_ACTION")
	public Formula BUY_RESETCD_ACTION;
	
	@Static("CAPTURETOWN:RESET_AND_REWARD_TIME")
	public ConfigValue<String> RESET_AND_REWARD_TIME;
	
	@Static("CAPTURETOWN:ENTER_ACC_CD_TIME")
	public ConfigValue<Integer> ENTER_ACC_CD_TIME;
	
	@Static("CAPTURETOWN:ACCUMULATE_MAX_CD_TIME")
	public ConfigValue<Integer> ACCUMULATE_MAX_CD_TIME;
	
	@Static("CAPTURETOWN:SELF_CAPTURE_LOG_MAX_SIZE")
	public ConfigValue<Integer> SELF_CAPTURE_LOG_MAX_SIZE;
	
	@Static("CAPTURETOWN:COUNTRY_CAPTURE_LOG_MAX_SIZE")
	public ConfigValue<Integer> COUNTRY_CAPTURE_LOG_MAX_SIZE;
	
	@Static("CAPTURETOWN:COUNTRY_CAPTURE_MAIL_TITLE")
	public ConfigValue<Map<String, String>> COUNTRY_CAPTURE_MAIL_TITLE;
	
	@Static("CAPTURETOWN:COUNTRY_CAPTURE_MAIL_CONTENT")
	public ConfigValue<Map<String, String>> COUNTRY_CAPTURE_MAIL_CONTENT;
	
	@Static("CAPTURETOWN:COUNTRY_CAPTURE_MAIL_REWARDID")
	public ConfigValue<Map<String, String>> COUNTRY_CAPTURE_MAIL_REWARDID;
	
	@Static("CAPTURETOWN:COUNTRY_CAPTURE_MODULE_OPEN_COND")
	public ConfigValue<String[]> COUNTRY_CAPTURE_MODULE_OPEN_COND;
	
	@Static("CAPTURETOWN:COUNTRY_CAPTURE_OFFLINE_LIMIT")
	public ConfigValue<Integer> COUNTRY_CAPTURE_OFFLINE_LIMIT;
	
	@Static("CAPTURETOWN:OFFLINE_REWARD_MAIL_TITLE")
	public ConfigValue<String> OFFLINE_REWARD_MAIL_TITLE;
	
	@Static("CAPTURETOWN:OFFLINE_REWARD_MAIL_CONTENT")
	public ConfigValue<String> OFFLINE_REWARD_MAIL_CONTENT;
	
	@Static("CAPTURETOWN:CHALLENGE_COND")
	public ConfigValue<String[]> CHALLENGE_COND;
	
	private CoreConditions townChallengeConditions;
	
	public CoreConditions getTownChallenegeConditions() {
		if (townChallengeConditions == null) {
			townChallengeConditions = CoreConditionManager.getInstance().getCoreConditions(1, CHALLENGE_COND.getValue());
		}
		return townChallengeConditions;
	}
	
	public String getResetAndRewardTime() {
		return RESET_AND_REWARD_TIME.getValue();
	}
	
	private CoreConditions townModuleOpenConditions;
	
	public CoreConditions getTownModuleOpenCond() {
		if (townModuleOpenConditions == null) {
			townModuleOpenConditions = CoreConditionManager.getInstance().getCoreConditions(1, COUNTRY_CAPTURE_MODULE_OPEN_COND.getValue());
		}
		return townModuleOpenConditions;
	}
	
	public CoreConditionResource[] getTownModuleOpenCoreConditionResource(int countryValue) {
		ArrayList<CoreConditionResource> arr = New.arrayList();
		for (String condId : COUNTRY_CAPTURE_MODULE_OPEN_COND.getValue()) {
			arr.add(CoreConditionManager.getInstance().getCoreConditionResource().get(condId, true));
		}
		arr.add(CoreConditionResource.createCondition(CoreConditionType.COUNTRY_COND, null, countryValue));
		CoreConditionResource[] rs = new CoreConditionResource[arr.size()];
		arr.toArray(rs);
		return rs;
	}
	
	public Reward getCountryRankReward(int rank) {
		String rewardId = COUNTRY_CAPTURE_MAIL_REWARDID.getValue().get(rank + "");
		Reward reward = RewardManager.getInstance().creatReward(null, rewardId, null);
		return reward;
	}
	
	public I18nUtils getCountryRankMailTitle(int rank) {
		return I18nUtils.valueOf(COUNTRY_CAPTURE_MAIL_TITLE.getValue().get(rank + ""));
	}
	
	public I18nUtils getCountryRankMailContent(int rank) {
		return I18nUtils.valueOf(COUNTRY_CAPTURE_MAIL_CONTENT.getValue().get(rank + ""));
	}
	
	public TownResource getSpecialTown() {
		return townResources.getIndex("TYPE", TownType.PVE).get(0);
	}
	
	public CurrencyAction getAddCountGoldActionValue(Player player) {
		int count = player.getPlayerCountryHistory().getCaptureTownInfo().getDailyBuyCount();
		int gold = (Integer) FormulaParmsUtil.valueOf(BUY_CHALLENGE_COUNT_ACTION).addParm("n", count + 1).getValue();
		return CoreActionType.createCurrencyCondition(CurrencyType.GOLD, gold);
	}
	
	public CurrencyAction getClearCDGoldActionValue(Player player) {
		int count = player.getPlayerCountryHistory().getCaptureTownInfo().getClearCDCount();
		int gold = (Integer) FormulaParmsUtil.valueOf(BUY_RESETCD_ACTION).addParm("n", count + 1).getValue();
		return CoreActionType.createCurrencyCondition(CurrencyType.GOLD, gold);
	}

	public boolean isInTownsCopyMap(int mapId) {
		for (TownResource resource : townResources.getAll()) {
			if (resource.getMapId() != 0 && resource.getMapId() == mapId) {
				return true;
			}
		}
		return false;
	}
}
