package com.mmorpg.mir.model.item.model.extrastat;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.model.EquipmentStat;
import com.mmorpg.mir.model.item.model.EquipmentStatType;

/**
 * 活动条件处理
 * 
 * @author Kuang Hao
 * @since v1.0 2013-7-17
 * 
 */
@Component
public abstract class EquipmentExtraStatHandle {
	/**
	 * 条件
	 * 
	 * @return
	 */
	public abstract EquipmentStatType getType();

	@Autowired
	protected ItemManager itemManager;

	@PostConstruct
	public void init() {
		itemManager.registerExtralStatHandle(this);
	}
	
	public abstract List<Stat> calcStat(Player player, Equipment equip, EquipmentStat equipmentStat);

}
