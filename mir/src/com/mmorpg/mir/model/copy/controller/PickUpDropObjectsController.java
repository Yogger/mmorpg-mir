package com.mmorpg.mir.model.copy.controller;

import java.util.List;

import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.gameobjects.DropObject;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.world.WorldMapInstance;

public class PickUpDropObjectsController extends AbstractCopyController {

	public PickUpDropObjectsController() {

	}

	public PickUpDropObjectsController(Player owner, WorldMapInstance worldMapInstance, CopyResource resource) {
		super(owner, worldMapInstance, resource);
	}

	@Override
	public String getCopyId() {
		return null;
	}

	@Override
	public void startCopy() {

	}

	@Override
	public void enterCopy(Player player, WorldMapInstance worldMapInstance, CopyResource resource) {
		PickUpDropObjectsController p = new PickUpDropObjectsController(player, worldMapInstance, resource);
		player.getCopyHistory().setCopyController(p);
	}

	@Override
	public void leaveCopyBefore(Player player) {
		List<DropObject> dropObjects = getWorldMapInstance().findObjectByType(ObjectType.DROPOBJECT);
		if (!dropObjects.isEmpty()) {
			Reward reward = Reward.valueOf();
			for (DropObject dropObject : dropObjects) {
				reward.addRewardItem(dropObject.getRewardItem());
			}
			RewardManager.getInstance().grantReward(player, reward,
					ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.COPY_BOSS_PICKUP, getCopyResource().getId()));
		}
	}
}
