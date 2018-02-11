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

@Component
public class CreateSoulExtraStatHandle extends EquipmentExtraStatHandle {

	@Override
	public EquipmentStatType getType() {
		return EquipmentStatType.CREATE_SOUL_STAT;
	}

	@Override
	public List<Stat> calcStat(Player player, Equipment equip, EquipmentStat equipmentStat) {
		List<Stat> stats = New.arrayList();
		for (String s: equipmentStat.getContext()) {
			for (Stat stat: ItemManager.getInstance().getCreateSoulResource(s).getStats())
				stats.add(stat);
		}
		return stats;
	}

}
