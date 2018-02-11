package com.mmorpg.mir.model.copy.controller;

import java.util.List;

import com.mmorpg.mir.model.copy.controller.AbstractCopyController;
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

public class LeavePickUpAllController extends AbstractCopyController {

	public LeavePickUpAllController() {
	}

	public LeavePickUpAllController(Player owner, WorldMapInstance worldMapInstance, CopyResource resource) {
		super(owner, worldMapInstance, resource);
	}

	/* 
	 * 
	 * 这里在程序初始化的时候 程序手写注入 
	 */
	@Override
	public String getCopyId() {
		return null;
	}

	@Override
	public void startCopy() {
	}

	@Override
	public void enterCopy(Player player, WorldMapInstance worldMapInstance, CopyResource resource) {
		LeavePickUpAllController p = new LeavePickUpAllController(player, worldMapInstance, resource);
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
					ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.PRIVATE_BOSS));
		}
	}
}
