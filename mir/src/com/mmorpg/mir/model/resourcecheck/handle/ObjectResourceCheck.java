package com.mmorpg.mir.model.resourcecheck.handle;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.model.sample.Chooser;
import com.mmorpg.mir.model.chooser.model.sample.ChooserGroup;
import com.mmorpg.mir.model.chooser.model.sample.Item;
import com.mmorpg.mir.model.chooser.model.sample.ItemGroup;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.core.condition.drop.HuntMonsterCountCondition;
import com.mmorpg.mir.model.core.condition.drop.ItemDropCountCondition;
import com.mmorpg.mir.model.core.condition.drop.PlayerHuntMonsterCountCondition;
import com.mmorpg.mir.model.core.condition.drop.PlayerItemDropCountCondition;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.reward.model.sample.RewardItemSample;
import com.mmorpg.mir.model.reward.model.sample.RewardSample;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.New;

@Component
public class ObjectResourceCheck extends ResourceCheckHandle {

	@Static
	private Storage<String, ObjectResource> objectResources;

	@Static
	private Storage<String, ChooserGroup> chooserGroup;

	@Static
	private Storage<String, Chooser> chooserResources;

	@Static
	private Storage<String, RewardSample> rewardSamples;
	
	@Static
	private Storage<String, ItemResource> itemResources;
	
	private ArrayList<String> errInfos = New.arrayList();

	@Override
	public Class<?> getResourceClass() {
		return ObjectResource.class;
	}

	@Override
	public void check() {
		for (ObjectResource rs : objectResources.getAll()) {
			if (rs.getDropKey() != null) {
				ChooserGroup or = chooserGroup.get(rs.getDropKey(), false);
				if (or == null) {
					throw new RuntimeException(String.format("ObjectResource id[%s] getDropKey[%s]不存在！ ", rs.getKey(),
							rs.getDropKey()));
				}
				testObjectKeyDropItemIsItemDrop(rs);
				
				for (String chooserId : or.getValueChoosers()) {
					Chooser chooser = chooserResources.get(chooserId, false);
					if (chooser == null) {
						throw new RuntimeException(String.format("ChooserGroup id[%s] chooserId[%s]不存在！ ",
								or.getChooserGroupId(), chooserId));
					}
					for (ItemGroup ig : chooser.getItemGroups()) {
						for (Item it : ig.getItems()) {
							RewardSample rewardSample = rewardSamples.get(it.getValue(), false);
							if (rewardSample == null) {
								throw new RuntimeException(String.format(
										"ObjectResource id[%s],dropKey[%s] chooser[%s] 包含奖励不存在。rewardId[%s]",
										rs.getKey(), rs.getDropKey(), chooserId, it.getValue()));
							}
						}
					}
					CoreConditions conditions = CoreConditionManager.getCoreConditionResources(1, getCoreConditionResources(chooser.getConditionIds()));
					for (AbstractCoreCondition cond: conditions.getConditionList()) {
						if (cond.getClass().equals(PlayerHuntMonsterCountCondition.class)) {
							checkPlayerHuntMoster(rs, chooser, (PlayerHuntMonsterCountCondition) cond);
						} else if (cond.getClass().equals(PlayerItemDropCountCondition.class)) {
							checkPlayerItemDrop(rs, chooser, (PlayerItemDropCountCondition) cond);
						} else if (cond.getClass().equals(HuntMonsterCountCondition.class)) {
							checkHuntMonsterCountCondition(rs, chooser, (HuntMonsterCountCondition) cond);
						} else if (cond.getClass().equals(ItemDropCountCondition.class)) {
							checkItemDropCountCondition(rs, chooser, (ItemDropCountCondition) cond);
						}
					}
				}

			}

			if (rs.isDynamicStats()) {
				if (rs.getStatsFormula() == null) {
					throw new RuntimeException(String.format("ObjectResource id[%s] getStatsFormula[%s]不存在！ ",
							rs.getKey(), rs.getStatsFormula()));
				}
//				if (rs.isLevelSuppress()) {
//					throw new RuntimeException(String.format("ObjectResource id[%s] 动态属性竟然还有等级压制一说!",
//							rs.getKey()));
//				}
			}

		}
		
		
		if (!errInfos.isEmpty()) {
			for (String info: errInfos) {
				System.err.println(info);
			}
		}
	}
	
	private void testObjectKeyDropItemIsItemDrop(ObjectResource rs) {
		if (rs.getDropItemKeys() != null) {
			for (String playerLogItemKey: rs.getDropItemKeys()) {
				ItemResource resource = itemResources.get(playerLogItemKey, true);
				if (!resource.isDropRecord()) {
					String s = String.format("ObjectResource id[%s] 的dropItemKeys 里面的道具 id[%s] 本身dropRecord为false", rs.getKey(), resource.getKey());
					errInfos.add(s);
				}
			}
		}
	}
	
	private void checkPlayerHuntMoster(ObjectResource rs, Chooser chooser, PlayerHuntMonsterCountCondition condition) {
		if (chooser.getItemGroups().length > 1) {
			// 可能不是个问题
			String s = String.format("[可能是误会] ObjectResource id[%s] 的chooser id[%s] 里面的道具不止一个 [%d]", rs.getKey(), chooser.getId(), chooser.getItemGroups().length);
			errInfos.add(s);
		}
		if (!condition.getCode().equals(rs.getKey())) {
			String s = String.format("ObjectResource id[%s] 的chooser id[%s] 里面的PLAYER_MONSTER_HUNT_COUNT 配的CODE是[%s]", rs.getKey(), chooser.getId(), condition.getCode());
			errInfos.add(s);
		}
		boolean found = false;
		for (String itemKey: rs.getDropItemKeys()) {
			if (itemKey.equals(condition.getItemId())) {
				found = true;
				break;
			}
		}
		if (!found) {
			String s = String.format("ObjectResource id[%s] 的chooser id[%s] 里面的PLAYER_MONSTER_HUNT_COUNT 配的itemId是[%s] 没有被ObjectResource的dropItemKeys包含!", rs.getKey(), chooser.getId(), condition.getItemId());
			errInfos.add(s);
		}
		ItemResource itemResource = itemResources.get(condition.getItemId(), true);
		if (!itemResource.isDropRecord()) {
			String s = String.format("ObjectResource id[%s] 的chooser id[%s] 里面的PLAYER_MONSTER_HUNT_COUNT 配的itemId是[%s] ItemResource的 dropRecord是false", rs.getKey(), chooser.getId(), condition.getItemId());
			errInfos.add(s);
		}
		String sampleId = chooser.calcResult().get(0);
		RewardSample sample = rewardSamples.get(sampleId, true);
		if (sample.getRewardItems().length > 1) {
			String s = String.format("[可能是误会] ObjectResource id[%s] 的chooser id[%s] 里面的value 配的RewardSample [%s] 奖励内容不只一个", rs.getKey(), chooser.getId(), sampleId);
			errInfos.add(s);
		}
		if (sample.getId().equals("NULLreward")) {
			return;
		}
		RewardItemSample itemSample = sample.getRewardItems()[0];
		if (itemSample.getType() == RewardType.ITEM) {
			ItemResource rewardResource = itemResources.get(itemSample.getCode(), true);
			if (!rewardResource.getKey().equals(condition.getItemId())) {
				String s = String.format("ObjectResource id[%s] 的chooser id[%s] 里面的value 配的[%s] 奖励道具[%s] 但是条件PLAYER_MONSTER_HUNT_COUNT的itemId配的是[%s]", rs.getKey(), chooser.getId(), sample.getId(), rewardResource.getKey(), condition.getItemId());
				errInfos.add(s);
			}
		}
	}
	
	private void checkPlayerItemDrop(ObjectResource rs, Chooser chooser, PlayerItemDropCountCondition condition) {
		if (chooser.getItemGroups().length > 1) {
			// 可能不是个问题
			String s = String.format("[可能是误会] ObjectResource id[%s] 的chooser id[%s] 里面的道具不止一个 [%d]", rs.getKey(), chooser.getId(), chooser.getItemGroups().length);
			errInfos.add(s);
		}
		ItemResource itemResource = itemResources.get(condition.getCode(), true);
		if (!itemResource.isDropRecord()) {
			String s = String.format("ObjectResource id[%s] 的chooser id[%s] 的PLAYER_ITEM_DROP_COUNT条件的code[%s]道具 dropRecord为false", rs.getKey(), chooser.getId(), condition.getCode());
			errInfos.add(s);
		}
		String sampleId = chooser.calcResult().get(0);
		RewardSample sample = rewardSamples.get(sampleId, true);
		if (sample.getRewardItems().length > 1) {
			String s = String.format("[可能是误会] ObjectResource id[%s] 的chooser id[%s] 里面的value 配的RewardSample [%s] 奖励内容不只一个", rs.getKey(), chooser.getId(), sampleId);
			errInfos.add(s);
		}
		if (sample.getId().equals("NULLreward")) {
			return;
		}
		RewardItemSample itemSample = sample.getRewardItems()[0];
		if (itemSample.getType() == RewardType.ITEM) {
			ItemResource rewardResource = itemResources.get(itemSample.getCode(), true);
			if (!rewardResource.getKey().equals(condition.getCode())) {
				String s = String.format("ObjectResource id[%s] 的chooser id[%s] 里面的value 配的[%s] 奖励道具[%s] 但是条件PLAYER_ITEM_DROP_COUNT的code配的是[%s]", rs.getKey(), chooser.getId(), sample.getId(), rewardResource.getKey(), condition.getCode());
				errInfos.add(s);
			}
		}
	}
	
	private void checkHuntMonsterCountCondition(ObjectResource rs, Chooser chooser, HuntMonsterCountCondition condition) {
		if (chooser.getItemGroups().length > 1) {
			// 可能不是个问题
			String s = String.format("[可能是误会] ObjectResource id[%s] 的chooser id[%s] 里面的道具不止一个 [%d]", rs.getKey(), chooser.getId(), chooser.getItemGroups().length);
			errInfos.add(s);
		}
		if (!rs.getKey().equals(condition.getCode())) {
			String s = String.format("ObjectResource id[%s] 的chooser id[%s] 里面的MONSTER_HUNT_COUNT 条件配的code 是[%s]!", rs.getKey(), chooser.getId(), condition.getCode());
			errInfos.add(s);
		}
	}
	
	private void checkItemDropCountCondition(ObjectResource rs, Chooser chooser, ItemDropCountCondition condition) {
		if (chooser.getItemGroups().length > 1) {
			// 可能不是个问题
			String s = String.format("[可能是误会] ObjectResource id[%s] 的chooser id[%s] 里面的道具不止一个 [%d]", rs.getKey(), chooser.getId(), chooser.getItemGroups().length);
			errInfos.add(s);
		}
		ItemResource itemResource = itemResources.get(condition.getCode(), true);
		if (!itemResource.isDropRecord()) {
			String s = String.format("ObjectResource id[%s] 的chooser id[%s] 的ITEM_DROP_COUNT条件的code[%s]道具 dropRecord为false", rs.getKey(), chooser.getId(), condition.getCode());
			errInfos.add(s);
		}
		String sampleId = chooser.calcResult().get(0);
		RewardSample sample = rewardSamples.get(sampleId, true);
		if (sample.getRewardItems().length > 1) {
			String s = String.format("[可能是误会] ObjectResource id[%s] 的chooser id[%s] 里面的value 配的RewardSample [%s] 奖励内容不只一个", rs.getKey(), chooser.getId(), sampleId);
			errInfos.add(s);
		}
		if (sample.getId().equals("NULLreward")) {
			return;
		}
		RewardItemSample itemSample = sample.getRewardItems()[0];
		if (itemSample.getType() == RewardType.ITEM) {
			ItemResource rewardResource = itemResources.get(itemSample.getCode(), true);
			if (!rewardResource.getKey().equals(condition.getCode())) {
				String s = String.format("ObjectResource id[%s] 的chooser id[%s] 里面的value 配的[%s] 奖励道具[%s] 但是条件ITEM_DROP_COUNT的code配的是[%s]", rs.getKey(), chooser.getId(), sample.getId(), rewardResource.getKey(), condition.getCode());
				errInfos.add(s);
			}
		}
		
	}
	
}
