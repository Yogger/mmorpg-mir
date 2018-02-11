package com.mmorpg.mir.model.rescue.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.rescue.config.RescueConfig;
import com.mmorpg.mir.model.rescue.model.RescueType;
import com.mmorpg.mir.model.rescue.model.typehandle.ChatRescueTypeHandle;
import com.mmorpg.mir.model.rescue.model.typehandle.GatherRescueTypeHandle;
import com.mmorpg.mir.model.rescue.model.typehandle.MonsterRescueTypeHandle;
import com.mmorpg.mir.model.rescue.model.typehandle.RescueTypeHandle;
import com.mmorpg.mir.model.reward.manager.RewardManager;

@Component
public class RescueManager implements IRescueManager {

	private Map<RescueType, RescueTypeHandle> handles = new HashMap<RescueType, RescueTypeHandle>();

	@Autowired
	private RewardManager rewardManager;

	private static RescueManager INSTANCE;

	@PostConstruct
	public void init() {
		INSTANCE = this;
		handles.put(RescueType.CHAT, new ChatRescueTypeHandle());
		handles.put(RescueType.GATHER, new GatherRescueTypeHandle());
		handles.put(RescueType.MONSTER, new MonsterRescueTypeHandle());
	}

	public RescueTypeHandle getRescueTypeHandle(RescueType type) {
		return handles.get(type);
	}

	public void rewardRescue(Player player, Quest quest, String rewardChooserId) {
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(quest, rewardChooserId);
		List<String> EXPRESCUE = ChooserManager.getInstance().chooseValueByRequire(player,
				RescueConfig.getInstance().EXPRESCUE.getValue());
		List<String> CURRENCYRESCUE = ChooserManager.getInstance().chooseValueByRequire(player,
				RescueConfig.getInstance().CURRENCYRESCUE.getValue());
		Map<String, Object> params = New.hashMap();
		params.put("EXPRESCUE", EXPRESCUE.get(0));
		params.put("CURRENCYRESCUE", CURRENCYRESCUE.get(0));
		params.put("LEVEL", player.getLevel());
		params.put("HONORINCREASE", player.getGameStats().getCurrentStat(StatEnum.HONOR_PLUS) * 1.0 / 10000);
		rewardManager.grantReward(player, rewardIds,
				ModuleInfo.valueOf(ModuleType.RESCUE, SubModuleType.RESCUE_REWARD), params);
	}

	public static RescueManager getInstance() {
		return INSTANCE;
	}

}
