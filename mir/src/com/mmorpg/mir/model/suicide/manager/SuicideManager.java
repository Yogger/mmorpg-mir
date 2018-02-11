package com.mmorpg.mir.model.suicide.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.suicide.SuicideConfig;

@Component
public class SuicideManager {

	@Autowired
	private ModuleOpenManager moduleOpenManager;

	@Autowired
	private SuicideConfig config;

	// 普通充元
	public void commonCharge(Player player) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.SUICIDE)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		CoreConditions conds = config.getTransConds(player.getSuicide().getTurn() + 1);
		if (conds == null) {
			throw new ManagedException(ManagedErrorCode.SUICIDE_TRANS_FULL);
		}

		if (player.getSuicide().isAllElementFull()) {
			throw new ManagedException(ManagedErrorCode.SUICIDE_ALL_ELEMENT_FULL);
		}

		CoreActions actions = config.getCommonChargeActions();
		if (!actions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		actions.act(player, ModuleInfo.valueOf(ModuleType.SUICIDE, SubModuleType.SUICIDE_COMMON_CHARGE_ACTION));

		player.getSuicide().commonCharge();

	}

	// 一键充元
	public void quickCharge(Player player) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.SUICIDE)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		CoreConditions conds = config.getTransConds(player.getSuicide().getTurn() + 1);
		if (conds == null) {
			throw new ManagedException(ManagedErrorCode.SUICIDE_TRANS_FULL);
		}

		if (player.getSuicide().isAllElementFull()) {
			throw new ManagedException(ManagedErrorCode.SUICIDE_ALL_ELEMENT_FULL);
		}

		player.getSuicide().quickCharge();
	}

	// 转生
	public void tranfer(Player player) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.SUICIDE)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		if (player.getLifeStats().isAlreadyDead()) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}

		CoreConditions conds = config.getTransConds(player.getSuicide().getTurn() + 1);
		if (conds == null) {
			throw new ManagedException(ManagedErrorCode.SUICIDE_TRANS_FULL);
		}
		if (!conds.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.SUICIDE_TURN_NOT_VERIFY);
		}

		player.getSuicide().turn();
	}
}
