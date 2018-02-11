package com.mmorpg.mir.model.ai.state.handler;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.state.AIState;
import com.mmorpg.mir.model.ai.state.IStateHandler;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.packet.SM_Heading;

public class ThinkingStateHandler implements IStateHandler {
	@Override
	public AIState getState() {
		return AIState.THINKING;
	}

	/**
	 * State THINKING AI MonsterAi AI AggressiveAi
	 */
	@Override
	public void handleState(AIState state, AI ai) {
		ai.clearDesires();

		Npc owner = (Npc) ai.getOwner();
		if (owner.getAggroList().getMostHated() != null) {
			ai.setAiState(AIState.ATTACKING);
			return;
		}
		// 对于没有兵线的怪物，才可以回家和恢复
		if (!owner.hasRouteStep()) {
			// 如果怪物没有在出生点，那么就应该回家
			if (!owner.isAtSpawnLocation()) {
				ai.setAiState(AIState.MOVINGTOHOME);
				return;
			}
			if (owner.getHeading() != owner.getBornHeading()) {
				if (owner.getObjectResource().isFightOffHeading()) {
					owner.setHeading(owner.getBornHeading());
					PacketSendUtility.broadcastPacket(owner, SM_Heading.valueOf(owner));
				}
			}
			if (owner.isRestore() && (!owner.getLifeStats().isFullyRestoredHp())) {
				ai.setAiState(AIState.RESTING);
				return;
			} else {
				// 脱战
				owner.getController().onFightOff();
			}
		}
		ai.setAiState(AIState.ACTIVE);
	}
}
