package com.mmorpg.mir.model.horse.manager;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.formula.Formula;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.horse.resource.HorseAttachResource;
import com.mmorpg.mir.model.horse.resource.HorseGrowItemResource;
import com.mmorpg.mir.model.horse.resource.HorseResource;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.DateUtils;

@Component
public class HorseManager implements IHorseManager {
	// private static final Log log = LogFactory.getLog(HorseManager.class);

	@Static
	public Storage<Integer, HorseResource> horseResourceStorage;
	@Static
	private Storage<Integer, HorseAttachResource> horseAttachResource;
	@Static
	public Storage<String, HorseGrowItemResource> horseGrowItemStorage;
	@Static
	private Storage<String, Formula> formulaResource;

	@Static("HORSE:CLEAR_BLESSVALUE_INTERVAL_HOUR")
	private ConfigValue<Integer> clearNowBlessTimeInterval;

	@Static("HORSE:HORSE_NAME")
	public ConfigValue<Map<String, String>> HORSE_NAME;

	@Static("HORSE:START_BROADCAST")
	public ConfigValue<Integer> START_BROADCAST;

	/** 坐骑永久幻化消耗 */
	@Static("HORSE:HORSE_FOREVER_ILLUTION_ACT")
	public ConfigValue<String[]> HORSE_FOREVER_ILLUTION_ACT;

	/** 坐骑幻化激活条件 */
	@Static("HORSE:HORSE_ILLUTION_ACTIVE_CONDTIONS")
	public ConfigValue<String[]> HORSE_ILLUTION_ACTIVE_CONDTIONS;

	@Static("HORSE:HORSE_ILLUTION_DEPRECATE_MALL_TITLE_IL18N")
	public ConfigValue<String> HORSE_ILLUTION_DEPRECATE_MAIL_TITLE;

	@Static("HORSE:HORSE_ILLUTION_DEPRECATE_MALL_CONTENT_IL18N")
	public ConfigValue<String> HORSE_ILLUTION_DEPRECATE_MAIL_CONTENT;

	/** 坐骑幻化激活消耗公式id formula中 */
	@Static("HORSE:HORSE_ILLUTION_ACTIVE")
	public Formula HORSE_ILLUTION_ACTIVE_FORMULA;

	@Static("HORSE:BLESSVALUE_CONVER_COUNT")
	public ConfigValue<Integer> BLESSVALUE_CONVER_COUNT;

	/** 暴击活动倍数 */
	@Static("HORSE:CRI_ACTIVITY_CHOSERGROUPID")
	public ConfigValue<String> CRI_ACTIVITY_CHOSERGROUPID;

	@Static("HORSE:CRI_ACTIVITY_CONDS")
	private ConfigValue<String[]> CRI_ACTIVITY_CONDS;

	private CoreConditions criActivityConds;

	public CoreConditions getCriActivityConds() {
		if (null == criActivityConds) {
			criActivityConds = CoreConditionManager.getInstance().getCoreConditions(1, CRI_ACTIVITY_CONDS.getValue());
		}
		return criActivityConds;
	}

	/** 暴击广播最低倍数要求 */
	@Static("HORSE:BROADCAST_LEAST_RATE")
	public ConfigValue<Integer> BROADCAST_LEAST_RATE;

	private static HorseManager instance;

	@PostConstruct
	public void init() {
		instance = this;
	}

	public Integer getClearNowBlessTimeInterval() {
		return clearNowBlessTimeInterval.getValue();
	}

	public static HorseManager getInstance() {
		return instance;
	}

	public HorseAttachResource getHorseAttachResource(int id) {
		return horseAttachResource.get(id, true);
	}

	public HorseResource getHorseResource(int id) {
		return horseResourceStorage.get(id, true);
	}

	public long getIntervalTime() {
		return getClearNowBlessTimeInterval() * DateUtils.MILLIS_PER_HOUR;
	}

	public boolean isOpen(Player player) {
		return ModuleOpenManager.getInstance().isOpenByModuleKey(player, ModuleKey.HORSE);
	}

	public Formula getFormula(String id) {
		return formulaResource.get(id, true);
	}

	public String[] getHorseIllutionActiveConditions() {
		return this.HORSE_ILLUTION_ACTIVE_CONDTIONS.getValue();
	}

	// 周暴击活动
	@Static("HORSE:WEEK_CRI_CHOOSER_ID")
	public ConfigValue<String> WEEK_CRI_CHOOSER_ID;

}
