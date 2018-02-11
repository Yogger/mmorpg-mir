package com.mmorpg.mir.model.military.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.core.action.CoreActionType;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.action.CurrencyAction;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.military.resource.MilitaryLevelResource;
import com.mmorpg.mir.model.military.resource.MilitaryStarResource;
import com.mmorpg.mir.model.military.resource.MilitaryStrategyResource;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.model.Skill;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.DateUtils;

@Component
public class MilitaryManager implements IMilitaryManager {

	@Static
	private Storage<Integer, MilitaryLevelResource> resources;

	@Static
	private Storage<Integer, MilitaryStrategyResource> strategyResources;

	@Static
	private Storage<String, MilitaryStarResource> starResources;

	@Static("MILITARY:INITIAL_STAR_ID")
	public ConfigValue<String> INITIAL_STAR_ID;

	@Static("MILITARY:INITIAL_STAR_RANK")
	public ConfigValue<Integer> INITIAL_STAR_RANK;

	@Static("MILITARY:INITIAL_RANK_HONOR")
	public ConfigValue<Integer> INITIAL_RANK_HONOR;

	@Static("MILITARY:MILITARY_STRAGY_MODULEOPEN_ID")
	public ConfigValue<Map<String, String>> MILITARY_STRAGY_MODULEOPEN_ID;

	private static MilitaryManager self;

	@PostConstruct
	public void init() {
		self = this;
	}

	public static MilitaryManager getInstance() {
		return self;
	}

	public String getMilitaryStragyModuleOpenId(int section) {
		return MILITARY_STRAGY_MODULEOPEN_ID.getValue().get(section + "");
	}

	public MilitaryLevelResource getResource(int r) {
		return resources.get(r, true);
	}

	public MilitaryLevelResource getResource(int r, boolean isThrowException) {
		return resources.get(r, isThrowException);
	}

	public MilitaryStrategyResource getStrategyResource(int r) {
		return strategyResources.get(r, true);
	}

	public int getInitalId(int section) {
		List<MilitaryStrategyResource> result = strategyResources.getIndex(MilitaryStrategyResource.SECTION_INDEX,
				section);
		return result.size() > 0 ? result.get(0).getId() : 0;
	}

	@Deprecated
	public String getCurrentStar(Player player, String oldStarId) {
		String currentStarId = null;
		MilitaryStarResource res = starResources.get(oldStarId, true);
		while (res.getPreviousId() != null) {
			res = starResources.get(res.getPreviousId(), true);
			if (!res.getUpConditions().verify(player, false)) {
				currentStarId = res.getPreviousId();
			} else {
				currentStarId = res.getNextId();
				break;
			}
		}
		return currentStarId == null ? oldStarId : currentStarId;
	}

	public String getCurrentStar(Player player) {
		String currentStarId = INITIAL_STAR_ID.getValue();
		String endStarId = player.getMilitary().getStarId();
		while (!currentStarId.equals(endStarId) && endStarId != null) {
			MilitaryStarResource res = starResources.get(currentStarId, true);
			if (res.getNextId() != null && res.getUpConditions().verify(player, false)) {
				currentStarId = res.getNextId();
			} else {
				break;
			}
		}
		return currentStarId;
	}

	public MilitaryStarResource getMilitaryStarResource(String key, boolean throwException) {
		return starResources.get(key, throwException);
	}

	public boolean checkNeedCost(Player player) {
		return player.getMilitary().getRank() == MilitaryManager.getInstance().INITIAL_STAR_RANK.getValue();
	}

	/**
	 * @param player
	 */
	public String doCostDailyHonor(Player player) {
		if (player.getMilitary().getLastStarActionTime() == 0L) {
			player.getMilitary().setLastStarActionTime(System.currentTimeMillis());
		}

		int dayDif = DateUtils.calcIntervalDays(new Date(player.getMilitary().getLastStarActionTime()), new Date());
		player.getMilitary().setLastStarActionTime(System.currentTimeMillis());

		MilitaryStarResource res = starResources.get(player.getMilitary().getStarId(), true);
		boolean hasActions = res.getKeepStarDailyAct() != null && res.getKeepStarDailyAct().length != 0;

		if (!hasActions || dayDif == 0) {
			return player.getMilitary().getStarId();
		}

		CoreActions actions = res.getDailyActions(dayDif);
		long honor = actions.getCurrencyValue(CurrencyType.HONOR);
		long nowHonor = player.getPurse().getValue(CurrencyType.HONOR);
		boolean touchLimit = nowHonor - honor < MilitaryManager.getInstance().INITIAL_RANK_HONOR.getValue();
		if (touchLimit) {
			// 当身上的荣誉不够扣的情况下 (理论上很多天没有上线，这时候只扣到最后一级军衔的荣誉值)
			int dif = (int) (nowHonor - MilitaryManager.getInstance().INITIAL_RANK_HONOR.getValue());
			CurrencyAction leastActions = CoreActionType.createCurrencyCondition(CurrencyType.HONOR, dif < 0 ? 0 : dif);
			if (leastActions.verify(player)) {
				leastActions.act(player,
						ModuleInfo.valueOf(ModuleType.MILITARY_STAR, SubModuleType.MILITARY_STAR_DAILY_ACT));
			}
			return MilitaryManager.getInstance().INITIAL_STAR_ID.getValue();
		} else {
			actions.act(player, ModuleInfo.valueOf(ModuleType.MILITARY_STAR, SubModuleType.MILITARY_STAR_DAILY_ACT));
			return getCurrentStar(player);
		}
	}

	public void refreshPlayerMilitaryStarBuff(Player player) {
		MilitaryStarResource starResource = getMilitaryStarResource(player.getMilitary().getStarId(), true);
		player.getEffectController().removeEffect(MilitaryStarResource.MILIARY_STAR_BUFF_GROUP);
		Integer skillId = starResource.getStarSkillId();
		if (skillId != null && skillId != 0) {
			Skill skill = SkillEngine.getInstance().getSkill(null, skillId.intValue(), player.getObjectId(), 0, 0,
					player, null);
			skill.noEffectorUseSkill();
		}
	}
}
