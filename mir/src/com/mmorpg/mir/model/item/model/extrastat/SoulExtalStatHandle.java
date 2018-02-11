package com.mmorpg.mir.model.item.model.extrastat;

import java.util.List;

import org.h2.util.New;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.model.EquipmentStat;
import com.mmorpg.mir.model.item.model.EquipmentStatType;
import com.mmorpg.mir.model.item.resource.EquipmentSoulResource;
import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;

/**
 * 活动条件处理
 * 
 * @author Kuang Hao
 * @since v1.0 2013-7-17
 * 
 */
@Component
public class SoulExtalStatHandle extends EquipmentExtraStatHandle {

	@Override
	public EquipmentStatType getType() {
		return EquipmentStatType.SOUL_STAT;
	}

	@Override
	public List<Stat> calcStat(Player player, Equipment equip, EquipmentStat equipmentStat) {
		List<Stat> stats = New.arrayList();
		for (String s : equipmentStat.getContext()) {
			EquipmentSoulResource resource = ItemManager.getInstance()
					.getEquipmentSoulReousrce(s);
			Equipment activating = equip;
			EquipmentType factorType = ItemManager.getInstance()
					.getActivateMap().get(equip.getEquipmentType());
			Equipment factor = player.getEquipmentStorage()
					.getEquip(factorType);

			if (factor == null || (!factor.hasSpecifiedTypeStat(EquipmentStatType.SOUL_STAT))) {
				equip.setSoulType(0);
				continue;
			}
			// boolean sameElement = activating.hasElement() && activating.getElement() == factor.getElement();
			// boolean sameLevel = activating.getResource().getLevel() == factor.getResource().getLevel();
			boolean sameRole = factor.getResource().getRoletype() == activating.getResource().getRoletype();

			if (sameRole) {
				for (Stat stat : resource.getStats())
					stats.add(stat);
				equip.setSoulType(resource.getStatType());
			} else {
				equip.setSoulType(0);
			}
		}
		return stats;
	}

}
