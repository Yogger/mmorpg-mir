package com.mmorpg.mir.model.seal.manager;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.consumable.CoreActionManager;
import com.mmorpg.mir.model.core.consumable.CoreActions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.seal.SealConfig;
import com.mmorpg.mir.model.seal.model.Seal;
import com.mmorpg.mir.model.seal.packet.SM_Seal_Upgrade;
import com.mmorpg.mir.model.seal.resource.SealResource;

@Component
public class SealManager {

	private static SealManager INSTANCE;
	
	public static SealManager getInstance() {
		return INSTANCE;
	}
	
	@Autowired
	private ModuleOpenManager moduleOpenManager;
	
	@Autowired
	private SealConfig config;
	
	@Autowired
	private CoreActionManager coreActionManager;
	
	@Autowired
	private ChooserManager chooserManager;
	
	@PostConstruct
	void init() {
		INSTANCE = this;
	}

	public SM_Seal_Upgrade upgrade(Player player, boolean autoBuy) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.SEAL)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		Seal seal = player.getSeal();

		SealResource resource = config.getResource(seal.getGrade());

		if (resource.isMaxGrade()) {
			throw new ManagedException(ManagedErrorCode.SEAL_MAX_GRADE);
		}

		CoreActions coreActions = new CoreActions();

		boolean canWeekCri = false;
		int itemCountFirst = 0;
		if (resource.getFirstMaterialActions() != null && resource.getFirstMaterialActions().length != 0) {
			String itemIdFirstCost = coreActionManager.getCoreActions(1, resource.getFirstMaterialActions())
					.getFirstItemKey();
			itemCountFirst = (int) player.getPack().getItemSizeByKey(itemIdFirstCost);
		}

		if (resource.getActCount() <= itemCountFirst) {
			canWeekCri = true;
			coreActions.addActions(coreActionManager.getCoreActions(resource.getActCount(),
					resource.getFirstMaterialActions()));
		} else {
			int needItemCount = resource.getActCount() - itemCountFirst;
			if (itemCountFirst > 0) {
				coreActions.addActions(coreActionManager.getCoreActions(itemCountFirst,
						resource.getFirstMaterialActions()));
			}
			int itemCountSecond = 0;
			if (resource.getSecondmaterialActions() != null && resource.getSecondmaterialActions().length != 0) {
				String itemIdSecondCost = coreActionManager.getCoreActions(1, resource.getSecondmaterialActions())
						.getFirstItemKey();
				itemCountSecond = (int) player.getPack().getItemSizeByKey(itemIdSecondCost);
			}

			if (needItemCount <= itemCountSecond) {
				coreActions.addActions(coreActionManager.getCoreActions(needItemCount,
						resource.getSecondmaterialActions()));
			} else {
				if (!autoBuy) {
					throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_ITEM);
				}
				if (itemCountSecond > 0) {
					coreActions.addActions(coreActionManager.getCoreActions(itemCountSecond,
							resource.getSecondmaterialActions()));
				}

				int needGoldCount = needItemCount - itemCountSecond;
				coreActions.addActions(coreActionManager.getCoreActions(needGoldCount, resource.getGoldActions()));
			}
		}

		if (!coreActions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		coreActions.act(player, ModuleInfo.valueOf(ModuleType.SEAL, SubModuleType.SEAL_UPGRADE_ACT));

		int countAdd = 1;
		if (canWeekCri) {
			String value = chooserManager.chooseValueByRequire(player, config.SEAL_CRI_CHOOSERGROUP_ID.getValue())
					.get(0);
			countAdd = Integer.parseInt(value);
		}
		return seal.upgrade(countAdd);
	}
	
}
