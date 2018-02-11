package com.mmorpg.mir.model.copy.controller.privateboss;

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

public abstract class AbstractPrivateBossController extends AbstractCopyController{
	
	public AbstractPrivateBossController() {}
	
	@Override
	public abstract String getCopyId();

	@Override
	public void startCopy() {
	}

	public AbstractPrivateBossController(Player owner, WorldMapInstance worldMapInstance, CopyResource resource) {
		super(owner, worldMapInstance, resource);
	}
	
	@Override
	public void leaveCopyBefore(Player player) {
		List<DropObject> dropObjects = getWorldMapInstance().findObjectByType(ObjectType.DROPOBJECT);
		if (!dropObjects.isEmpty()) {
			Reward reward = Reward.valueOf();
			for (DropObject dropObject : dropObjects) {
				reward.addRewardItem(dropObject.getRewardItem());
			}
			RewardManager.getInstance().grantReward(player, reward, ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.PRIVATE_BOSS));
		}
	}
}
