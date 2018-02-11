package com.mmorpg.mir.model.suicide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.suicide.resource.SuicideElementResource;
import com.mmorpg.mir.model.suicide.resource.SuicideStatResource;
import com.mmorpg.mir.model.suicide.resource.SuicideTurnStatResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class SuicideConfig {

	private static SuicideConfig INSTANCE;

	@PostConstruct
	void init() {
		INSTANCE = this;
	}

	public static SuicideConfig getInstance() {
		return INSTANCE;
	}

	@Static
	public Storage<String, SuicideElementResource> elementStorage;

	@Static
	public Storage<Integer, SuicideStatResource> elementStatStorage;

	@Static
	public Storage<Integer, SuicideTurnStatResource> turnStatStorage;

	@Static("SUICIDE:SUICIDE_COMMON_RECHAGE_ACTS")
	private ConfigValue<String[]> SUICIDE_COMMON_CHAGE_ACTS;

	public CoreActions getCommonChargeActions() {
		return CoreActionManager.getInstance().getCoreActions(1, this.SUICIDE_COMMON_CHAGE_ACTS.getValue());
	}

	@Static("SUICIDE:SUICIDE_TRANS_CONDS")
	public ConfigValue<Map<String, ArrayList<String>>> SUICIDE_TRANS_CONDS;

	public CoreConditions getTransConds(int turn) {
		if (!this.SUICIDE_TRANS_CONDS.getValue().containsKey(String.valueOf(turn))) {
			return null;
		}
		ArrayList<String> conds = this.SUICIDE_TRANS_CONDS.getValue().get(String.valueOf(turn));
		return CoreConditionManager.getInstance().getCoreConditions(1, conds.toArray(new String[conds.size()]));
	}

	public Stat[] getTurnStats(int turn) {
		List<Stat> stats = new ArrayList<Stat>();
		for (SuicideTurnStatResource resource : turnStatStorage.getAll()) {
			if (resource.getId() <= turn) {
				stats.addAll(Arrays.asList(resource.getStats()));
			}
		}
		return stats.toArray(new Stat[stats.size()]);

	}
}
