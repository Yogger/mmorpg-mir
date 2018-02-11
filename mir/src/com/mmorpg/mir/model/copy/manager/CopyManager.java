package com.mmorpg.mir.model.copy.manager;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.h2.util.New;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.copy.controller.AbstractCopyController;
import com.mmorpg.mir.model.copy.controller.LeavePickUpAllController;
import com.mmorpg.mir.model.copy.controller.PickUpDropObjectsController;
import com.mmorpg.mir.model.copy.event.LadderNewRecordEvent;
import com.mmorpg.mir.model.copy.model.CopyType;
import com.mmorpg.mir.model.copy.resource.CopyIndividualBossResource;
import com.mmorpg.mir.model.copy.resource.CopyLadderRewardResource;
import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.core.condition.BattleScoreCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.DateUtils;

@Component
public class CopyManager implements ICopyManager {

	@Static
	public Storage<String, CopyResource> copyResources;

	@Static
	private Storage<String, CopyLadderRewardResource> copyeLadderRewardResources;

	@Static("COPY:HORSEEQUIP_REWARD")
	public ConfigValue<Map<String, String>> HORSEEQUIP_REWARD;

	@Static("COPY:HORSEEQUIP_RESET_MIN_QUESTID")
	public ConfigValue<Map<String, String>> HORSEEQUIP_RESET_MIN_QUESTID;

	@Static
	private Storage<String, CopyIndividualBossResource> copyIndividualBossResource;

	@Static("COPY:LADDER_RESET_REWARD_RATE")
	public ConfigValue<Integer[]> LADDER_RESET_REWARD_RATE;

	@Static("COPY:HORSEEQUIP_BOSS_DROP")
	public ConfigValue<Map<String, String>> HORSEEQUIP_BOSS_DROP;

	@Static("COPY:WARBOOK_BOSS_DROP")
	public ConfigValue<Map<String, String>> WARBOOK_BOSS_DROP;

	public static Map<String, AbstractCopyController> copyControllers = New.hashMap();

	private static CopyManager instance;

	@PostConstruct
	public void init() {
		instance = this;

		for (CopyResource resource : copyResources.getAll()) {
			if (resource.getType() == CopyType.BOSS || resource.getType() == CopyType.VIP
					|| resource.getType() == CopyType.MINGJIANG) {
				copyControllers.put(resource.getId(), new LeavePickUpAllController());
			} else if (resource.isAutoPickup()) {
				copyControllers.put(resource.getId(), new PickUpDropObjectsController());
			}

		}
	}

	public static CopyManager getInstance() {
		return instance;
	}

	public Storage<String, CopyResource> getCopyResources() {
		return copyResources;
	}

	public void setCopyResources(Storage<String, CopyResource> copyResources) {
		this.copyResources = copyResources;
	}

	public Storage<String, CopyLadderRewardResource> getCopyeLadderRewardResources() {
		return copyeLadderRewardResources;
	}

	public void setCopyeLadderRewardResources(Storage<String, CopyLadderRewardResource> copyeLadderRewardResources) {
		this.copyeLadderRewardResources = copyeLadderRewardResources;
	}

	public CopyIndividualBossResource getCopyIndividualBossResource(String id) {
		return copyIndividualBossResource.get(id, true);
	}

	public void ladderCopyFix(Player player) {
		if (!player.getCopyHistory().isBeta21ladderFix()) {
			if (player.getCopyHistory().getLadderHisCompleteIndex() >= 200
					&& player.getGameStats().calcBattleScore() < 6000000L) {
				int maxHis = 0;
				for (CopyResource resource : copyResources.getAll()) {
					if (resource.getType() == CopyType.LADDER) {
						BattleScoreCondition cond = resource.getEnterConditions().findConditionType(
								BattleScoreCondition.class);
						if (cond == null) {
							return;
						}
						if (player.getGameStats().calcBattleScore() > ((double) cond.getLow() / 0.8)
								&& maxHis < resource.getIndex()) {
							maxHis = resource.getIndex();
						}
					}
				}
				if (maxHis > 0) {
					player.getCopyHistory().setLadderCompleteIndex(maxHis);
					player.getCopyHistory().setLadderHisCompleteIndex(maxHis);
					player.getRankInfo().setLayer(maxHis);
					player.getRankInfo().setCost(Integer.MAX_VALUE);
					EventBusManager.getInstance().syncSubmit(
							LadderNewRecordEvent.valueOf(player.getObjectId(), maxHis,
									(int) (DateUtils.MILLIS_PER_SECOND * 20)));
				}
			}

			player.getCopyHistory().setBeta21ladderFix(true);
		}
	}
}
