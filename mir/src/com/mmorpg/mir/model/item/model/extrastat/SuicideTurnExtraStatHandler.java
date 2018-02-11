package com.mmorpg.mir.model.item.model.extrastat;

import java.util.List;

import org.h2.util.New;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.model.EquipmentStat;
import com.mmorpg.mir.model.item.model.EquipmentStatType;
import com.mmorpg.mir.model.item.resource.EquipmentTurnResource;

@Component
public class SuicideTurnExtraStatHandler extends EquipmentExtraStatHandle {

	@Override
	public EquipmentStatType getType() {
		return EquipmentStatType.SUICIDE_TURN;
	}

	@Override
	public List<Stat> calcStat(Player player, Equipment equip, EquipmentStat equipmentStat) {
		List<Stat> stats = New.arrayList();
		for (String s : equipmentStat.getContext()) {
			if (s == null) {
				continue;
			}

			EquipmentTurnResource resource = itemManager.equipTurnResources.get(s, true);
			for (Stat st : resource.getStats()) {
				stats.add(st);
			}
		}
		return stats;
	}

}
