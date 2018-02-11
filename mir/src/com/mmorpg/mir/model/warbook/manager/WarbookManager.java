package com.mmorpg.mir.model.warbook.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.warbook.WarbookConfig;
import com.mmorpg.mir.model.warbook.model.Warbook;
import com.mmorpg.mir.model.warbook.packet.SM_Warbook_Upgrade;
import com.mmorpg.mir.model.warbook.resource.WarbookResource;

@Component
public class WarbookManager {

	@Autowired
	private ModuleOpenManager moduleOpenManager;

	@Autowired
	private CoreActionManager coreActionManager;

	@Autowired
	private ChooserManager chooserManager;

	@Autowired
	private WarbookConfig config;

	public void operateHide(Player player) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.WARBOOK)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		Warbook warbook = player.getWarBook();
		warbook.operateHide();
	}

	public SM_Warbook_Upgrade upgrade(Player player, boolean autoBuy) {
		if (!moduleOpenManager.isOpenByModuleKey(player, ModuleKey.WARBOOK)) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}

		Warbook book = player.getWarBook();

		WarbookResource resource = config.getWarbookResourceByGrade(book.getGrade());

		if (resource.isMaxGrade()) {
			throw new ManagedException(ManagedErrorCode.WARBOOK_MAX_GRADE);
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

		coreActions.act(player, ModuleInfo.valueOf(ModuleType.WARBOOK, SubModuleType.WARBOOK_UPGRADE_ACT));

		int countAdd = 1;
		if (canWeekCri) {
			String value = chooserManager.chooseValueByRequire(player, config.WARBOOK_CRI_CHOOSERGROUP_ID.getValue())
					.get(0);
			countAdd = Integer.parseInt(value);
		}
		SM_Warbook_Upgrade sm = book.upgrade(countAdd);
		return sm;
	}
}
