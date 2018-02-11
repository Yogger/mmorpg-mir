package com.mmorpg.mir.model.item.config;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.FormulaParmsUtil;
import com.mmorpg.mir.model.core.action.CoreActionType;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.formula.Formula;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.model.EquipmentGroup;
import com.mmorpg.mir.model.item.model.EquipmentStat;
import com.mmorpg.mir.model.item.model.EquipmentStatType;
import com.mmorpg.mir.model.item.model.EquipmentStorageType;
import com.mmorpg.mir.model.item.resource.EquipmentEnhanceResource;
import com.mmorpg.mir.model.item.resource.EquipmentSoulResource;
import com.mmorpg.mir.model.item.resource.PetResource;
import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;
import com.mmorpg.mir.model.reward.model.Reward;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.scheduler.ScheduledTask;
import com.windforce.common.scheduler.impl.SimpleScheduler;

@Component
public class ItemConfig {

	@Static("BAG:TIDY_CD")
	public ConfigValue<Integer> BAGTIDYCD;

	@Static("DEPOT:TIDY_CD")
	public ConfigValue<Integer> DEPOTTIDYCD;

	@Static("TREASURE:TREASURETIDYCD")
	public ConfigValue<Integer> TREASURETIDYCD;

	@Static("EQUIPMENT:ELEMENT_RESET_GOLDACTIONIDS")
	public ConfigValue<String[]> ELEMENT_RESET_GOLDACTIONIDS;

	@Static("EQUIPMENT:ELEMENT_RESET_ACTIONIDS")
	public ConfigValue<String[]> ELEMENT_RESET_ACTIONIDS;

	@Static("EQUIPMENT:ELEMENT_RESET_COPPERACTIONIDS")
	public ConfigValue<String[]> ELEMENT_RESET_ACTIONIDS_COPPER;

	@Static("EQUIPMENT:ELEMENT_CHOOSERID")
	public ConfigValue<String> ELEMENT_CHOOSERID;

	@Static("EQUIPMENT:DELETE_EQUIPMENT_PERFECT_STAT")
	public ConfigValue<Boolean> DELETE_EQUIPMENT_PERFECT_STAT;

	@Static("EQUIP:SMELT_EXPADD_LEVEL")
	public ConfigValue<Map<String, Double>> SMELT_EXPADD_LEVEL;

	@Static("EQUIP:SMELT_EXPADD_QUALITY")
	public ConfigValue<Map<String, Double>> SMELT_EXPADD_QUALITY;

	@Static("EQUIP:SMELT_EXPADD_SOULE_LEVEL")
	public ConfigValue<Map<String, Double>> SMELT_EXPADD_SOULE_LEVEL;

	@Static("EQUIP:SMELT_EXPADD_TYPE")
	public ConfigValue<Map<String, Double>> SMELT_EXPADD_TYPE;

	@Static("EQUIP:SMELT_EXPADD_SOULE_TYPE")
	public ConfigValue<Map<String, Double>> SMELT_EXPADD_SOULE_TYPE;

	@Static("EQUIPMENT:SMELT_EXPADD")
	public Formula SMELT_EXPADD;

	/** 坐骑装备的熔炼经验的公式 */
	@Static("EQUIPMENT:SMELT_HORSE_EXPADD")
	public Formula SMELT_HORSE_EXPADD;

	@Static("EQUIP:SOUL_MAP")
	public ConfigValue<Integer> SOUL_MIN;

	@Static("EQUIP:STONE_FRAGMENT_OF_GOD_ITEMKEY")
	public ConfigValue<String> STONE_FRAGMENT_OF_GOD_ITEMKEY;

	@Static("EQUIP:STONE_OF_GOD_TO_FRAGMENT_NUM")
	public ConfigValue<Integer> STONE_OF_GOD_TO_FRAGMENT_NUM;

	@Static("FORMULA:SOUL_EQUIPMENT_SMELT_STONE_FRAGMENT")
	public Formula SMELT_STONE_FRAGMENT;

	@Static("EQUIP:SMELT_STONE_MAIL_TITLE")
	public ConfigValue<String> SMELT_STONE_MAIL_TITLE;

	@Static("EQUIP:SMELT_STONE_MAIL_CONTENT")
	public ConfigValue<String> SMELT_STONE_MAIL_CONTENT;

	@Static("EQUIP:SMELT_STONE_LEVEL_FACTOR")
	public ConfigValue<Map<String, Double>> SMELT_STONE_LEVEL_FACTOR;

	@Static("EQUIP:SMELT_STONE_QUALITY_FACTOR")
	public ConfigValue<Map<String, Double>> SMELT_STONE_QUALITY_FACTOR;

	@Static("EQUIP:SMELT_STONE_TYPE_FACTOR")
	public ConfigValue<Map<String, Double>> SMELT_STONE_TYPE_FACTOR;

	@Static("EQUIP:BECOME_SUIT_ATTACK_ACTIONIDS")
	public ConfigValue<String> BECOME_SUIT_ATTACK_ITEMKEY;

	@Static("EQUIP:BECOME_SUIT_DEFEND_ACTIONIDS")
	public ConfigValue<String> BECOME_SUIT_DEFEND_ITEMKEY;

	@Static("EQUIP:BECOME_SUIT_TYPE_FACTOR")
	public ConfigValue<Map<String, Double>> BECOME_SUIT_TYPE_FACTOR;

	@Static("EQUIP:BECOME_SUIT_LEVEL_FACTOR")
	public ConfigValue<Map<String, Double>> BECOME_SUIT_LEVEL_FACTOR;

	@Static("EQUIP:BECOME_SUIT_ATTACK")
	public Formula BECOME_SUIT_ATTACK;

	@Static("EQUIP:BECOME_SUIT_DEFEND")
	public Formula BECOME_SUIT_DEFEND;

	@Static("EQUIP:STONE_OF_SOUL_ITEM_KEY")
	public ConfigValue<String> STONE_OF_SOUL_ITEM_KEY;

	@Static("EQUIP:SOUL_UPGRADE_ACT_TYPE_FACTOR")
	public ConfigValue<Map<String, Double>> SOUL_UPGRADE_ACT_TYPE_FACTOR;

	@Static("EQUIP:SOUL_UPGRADE_ACT_SOULTYPE_FACTOR")
	public ConfigValue<Map<String, Double>> SOUL_UPGRADE_ACT_SOULTYPE_FACTOR;

	@Static("EQUIP:SOUL_UPGRADE_STONE_NUM")
	public Formula SOUL_UPGRADE_STONE_NUM;

	@Static("EQUIP:SOUL_MAX_LEVEL")
	public ConfigValue<Integer> SOUL_MAX_LEVEL;

	@Static("EQUIP:SOUL_UPGRADE_MIN_LEVEL")
	public ConfigValue<Integer> SOUL_UPGRADE_MIN_LEVEL;

	@Static("EQUIP:SOUL_SMELT_STONE_MAIL_TITLE")
	public ConfigValue<String> SOUL_SMELT_STONE_MAIL_TITLE;

	@Static("EQUIP:SOUL_SMELT_STONE_MAIL_CONTENT")
	public ConfigValue<String> SOUL_SMELT_STONE_MAIL_CONTENT;

	@Static("PET:NEWER_PET_ITEM_KEY")
	public ConfigValue<String> NEWER_PET_ITEM_KEY;

	@Static("PET:SHOP_PET_ITEM_KEY")
	public ConfigValue<String[]> SHOP_PET_ITEM_KEY;

	@Static("PET:NEWER_BUY_PET_SHOP_ID")
	public ConfigValue<String> NEWER_BUY_PET_SHOP_ID;

	@Static
	public Storage<String, PetResource> petResources;

	private static ItemConfig INSTANCE;

	@Autowired
	private SimpleScheduler simpleScheduler;

	@PostConstruct
	void init() {
		INSTANCE = this;
	}

	public static ItemConfig getInstance() {
		return INSTANCE;
	}

	// ceil(EQUIP:LEVEL*EQUIP:QUALITY*(1+EQUIP:SOULE_LEVEL)*EQUIP:TYPE*EQUIP:SOULE_TYPE)
	public Integer calcSmeltPlayerEquipmentExp(Equipment equip) {
		double soulLevel = 0;
		double soulTypeResult = 0.0;
		EquipmentStat soulIds = equip.getExtraStats().get(EquipmentStatType.SOUL_STAT.getValue());
		if (soulIds != null && !soulIds.getContext().isEmpty()) {
			EquipmentSoulResource resource = ItemManager.getInstance().getEquipmentSoulReousrce(
					soulIds.getContext().get(0));
			soulLevel = SMELT_EXPADD_SOULE_LEVEL.getValue().get(resource.getSoulStatLevel() + "");
			soulTypeResult = SMELT_EXPADD_SOULE_TYPE.getValue().get(resource.getStatType() + "");
		} else {
			soulTypeResult = SMELT_EXPADD_SOULE_TYPE.getValue().get("1");
		}
		return (Integer) FormulaParmsUtil.valueOf(SMELT_EXPADD)
				.addParm("LEVEL", SMELT_EXPADD_LEVEL.getValue().get(equip.getResource().getLevel() + ""))
				.addParm("QUALITY", SMELT_EXPADD_QUALITY.getValue().get(equip.getResource().getQuality() + ""))
				.addParm("SOULE_LEVEL", soulLevel)
				.addParm("TYPE", SMELT_EXPADD_TYPE.getValue().get(equip.getResource().getEquipmentType().name()))
				.addParm("SOULE_TYPE", soulTypeResult).getValue();
	}

	public Integer calcSmeltEquipmentExp(Equipment equip) {
		int result = 0;
		if (equip.getResource().getEquipStorageByType() == EquipmentStorageType.PLAYER) {
			result = calcSmeltPlayerEquipmentExp(equip);
		} else if (equip.getResource().getEquipStorageByType() == EquipmentStorageType.HORSE) {
			result = calcEquipExpInHorseEquipStorage(equip);
		}
		return result;
	}

	public Integer calcEquipExpInHorseEquipStorage(Equipment equip) {
		return (Integer) FormulaParmsUtil.valueOf(SMELT_HORSE_EXPADD).addParm("LEVEL", equip.getResource().getLevel())
				.addParm("QUALITY", equip.getResource().getQuality() + "").getValue();
	}

	public Integer calcEquipmentStoneFragment(Player player, Equipment equip) {
		return (Integer) FormulaParmsUtil.valueOf(SMELT_STONE_FRAGMENT)
				.addParm("LEVEL", SMELT_STONE_LEVEL_FACTOR.getValue().get(equip.getResource().getLevel() + ""))
				.addParm("QUALITY", SMELT_STONE_QUALITY_FACTOR.getValue().get(equip.getResource().getQuality() + ""))
				.addParm("TYPE", SMELT_STONE_TYPE_FACTOR.getValue().get(equip.getEquipmentType().name())).getValue();
	}

	public Reward smeltNormalSuitEquipmentBack(Player player, Equipment e) {
		Reward reward = Reward.valueOf();
		if (!e.hasSpecifiedTypeStat(EquipmentStatType.SUIT_STAT)) {
			return reward;
		}
		if (isAttackSuitEquipment(e)) {
			reward.addItem(BECOME_SUIT_ATTACK_ITEMKEY.getValue(),
					getEquipmentSuitFactor(player, e, EquipmentGroup.ATTACK_GROUP));
		} else if (isDefendSuitEquipment(e)) {
			reward.addItem(BECOME_SUIT_DEFEND_ITEMKEY.getValue(),
					getEquipmentSuitFactor(player, e, EquipmentGroup.DEFEND_GROUP));
		}
		return reward;
	}

	public int getEquipmentSuitFactor(Player player, Equipment equip, EquipmentGroup group) {
		if (group == EquipmentGroup.ATTACK_GROUP) {
			return (Integer) FormulaParmsUtil.valueOf(BECOME_SUIT_ATTACK)
					.addParm("TYPE", BECOME_SUIT_TYPE_FACTOR.getValue().get(equip.getEquipmentType().name()))
					.addParm("LEVEL", BECOME_SUIT_LEVEL_FACTOR.getValue().get(equip.getResource().getLevel() + ""))
					.getValue();
		} else if (group == EquipmentGroup.DEFEND_GROUP) {
			return (Integer) FormulaParmsUtil.valueOf(BECOME_SUIT_DEFEND)
					.addParm("TYPE", BECOME_SUIT_TYPE_FACTOR.getValue().get(equip.getEquipmentType().name()))
					.addParm("LEVEL", BECOME_SUIT_LEVEL_FACTOR.getValue().get(equip.getResource().getLevel() + ""))
					.getValue();
		}
		return 0;
	}

	public boolean isAttackSuitEquipment(Equipment e) {
		for (Integer equipOrdinal : ItemManager.getInstance().getAttackEquipGroup()) {
			if (equipOrdinal.equals(e.getEquipmentType().getIndex()))
				return true;
		}
		return false;
	}

	public boolean isDefendSuitEquipment(Equipment e) {
		for (Integer equipOrdinal : ItemManager.getInstance().getDefenseEquipGroup()) {
			if (equipOrdinal.equals(e.getEquipmentType().getIndex()))
				return true;
		}
		return false;
	}

	public CoreActions getSuitEquipmentActions(Player player, Equipment e) {
		if (isAttackSuitEquipment(e)) {
			CoreActions actions = new CoreActions();
			actions.addActions(CoreActionType.createItemCondition(BECOME_SUIT_ATTACK_ITEMKEY.getValue(),
					getEquipmentSuitFactor(player, e, EquipmentGroup.ATTACK_GROUP)));
			return actions;
		}
		if (isDefendSuitEquipment(e)) {
			CoreActions actions = new CoreActions();
			actions.addActions(CoreActionType.createItemCondition(BECOME_SUIT_DEFEND_ITEMKEY.getValue(),
					getEquipmentSuitFactor(player, e, EquipmentGroup.DEFEND_GROUP)));
			return actions;
		}
		return null;
	}

	public int getUpgradeSoulLevelItemActNum(Player player, EquipmentType type, EquipmentSoulResource resource) {
		return (Integer) FormulaParmsUtil.valueOf(SOUL_UPGRADE_STONE_NUM)
				.addParm("TYPE", SOUL_UPGRADE_ACT_TYPE_FACTOR.getValue().get(type.name()))
				.addParm("SOUL_TYPE", SOUL_UPGRADE_ACT_SOULTYPE_FACTOR.getValue().get(resource.getStatType() + ""))
				.getValue();
	}

	public boolean checkCanUpgradeSoul(String soulKey) {
		EquipmentSoulResource resource = ItemManager.getInstance().getEquipmentSoulReousrce(soulKey);
		return resource != null && resource.getSoulStatLevel() >= SOUL_UPGRADE_MIN_LEVEL.getValue()
				&& resource.getSoulStatLevel() < SOUL_MAX_LEVEL.getValue();
	}

	public Future<?> schedule(ScheduledTask task, long endTime) {
		return simpleScheduler.schedule(task, new Date(endTime));
	}

	public int getEquipmentMaxEnhanceLevel() {
		for (EquipmentEnhanceResource resource : ItemManager.getInstance().equipEnhanceResource.getAll()) {
			if (resource.getNextLevel() == 0) {
				return resource.getLevel();
			}
		}

		return 0;
	}
}
