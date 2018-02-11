package com.mmorpg.mir.model.fashion.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.fashion.FashionConfig;
import com.mmorpg.mir.model.fashion.model.FashionPool;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;

@Component
public class FashionManager {

	@Autowired
	private FashionConfig config;

	public void wearFashion(Player player, int fashionId) {
		if (!ModuleOpenManager.getInstance().isOpenByModuleKey(player, ModuleKey.FASHION)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		FashionPool fashionPool = player.getFashionPool();
		fashionPool.wear(fashionId);
	}

	public void unWearFashion(Player player) {
		if (!ModuleOpenManager.getInstance().isOpenByModuleKey(player, ModuleKey.FASHION)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		FashionPool pool = player.getFashionPool();
		if (!pool.isWearFashion()) {
			throw new ManagedException(ManagedErrorCode.FASHION_NOT_WEAR);
		}
		pool.unWear();
	}

	public void upgrade(Player player) {
		if (!ModuleOpenManager.getInstance().isOpenByModuleKey(player, ModuleKey.FASHION)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		FashionPool pool = player.getFashionPool();
		if (pool.isMaxLevel()) {
			throw new ManagedException(ManagedErrorCode.FASHION_MAX_LEVEL);
		}

		if (!config.getUpgradeConds().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		pool.upgrade();
	}

	public void operateHide(Player player) {
		if (!ModuleOpenManager.getInstance().isOpenByModuleKey(player, ModuleKey.FASHION)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		FashionPool pool = player.getFashionPool();
		pool.operateHide();
	}

}
