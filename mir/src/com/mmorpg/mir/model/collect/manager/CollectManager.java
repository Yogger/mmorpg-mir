package com.mmorpg.mir.model.collect.manager;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.collect.packet.SM_CollectNew_Equip;
import com.mmorpg.mir.model.collect.packet.SM_Reward_Collect;
import com.mmorpg.mir.model.collect.packet.SM_Reward_FamedGeneral;
import com.mmorpg.mir.model.collect.resource.CollectGeneralResource;
import com.mmorpg.mir.model.collect.resource.CollectGeneralSkillCombiResource;
import com.mmorpg.mir.model.collect.resource.CollectSuitRewardResource;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.item.AbstractItem;
import com.mmorpg.mir.model.item.Equipment;
import com.mmorpg.mir.model.item.model.EquipmentStatType;
import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.object.ObjectManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class CollectManager {

	@Static("COLLECT:COLLECT_EQUIP_MAX_LEVEL")
	public ConfigValue<Integer> COLLECT_EQUIP_MAX_LEVEL;
	
	@Static("COLLECT:COLLECT_EQUIP_MIN_LEVEL")
	public ConfigValue<Integer> COLLECT_EQUIP_MIN_LEVEL;
	
	// @Static
	// public Storage<Integer, CollectSuitResource> collectSuitResource;
	
	@Static
	public Storage<String, CollectSuitRewardResource> collectSuitRewardResource;
	
	@Static
	public Storage<String, CollectGeneralResource> collectGeneralResources;
	
	@Static
	public Storage<String, CollectGeneralSkillCombiResource> collectGeneralSkillCombiResources;
	
	public static CollectManager INSTANCE;
	
	@PostConstruct
	void init() {
		INSTANCE = this;
	}
	
	public static CollectManager getInstance() {
		return INSTANCE;
	}

	public void rewardCollect(Player player, String collectRewardId) {
		CollectSuitRewardResource resource = collectSuitRewardResource.get(collectRewardId, true);
		
		if (player.getCollect().getCollectRewarded().contains(collectRewardId)) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COLLECT_REWARD_ALREADY_RECIEVED);
			return;
		}
		
		if (player.getCollect().getCollectEquipLog().size() < resource.getNeedNum()) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COLLECT_REWARD_ALREADY_RECIEVED);
			return;
		}
		
		RewardManager.getInstance().grantReward(player, resource.getRewardId(), ModuleInfo.valueOf(ModuleType.COLLECT, SubModuleType.COLLECT_EQUIP_REWARD));
		player.getCollect().getCollectRewarded().add(collectRewardId);
		PacketSendUtility.sendPacket(player, SM_Reward_Collect.valueOf(player));
	}
	
	public void doCollectEquipment(Player player, List<AbstractItem> equips) {
		if (equips == null) {
			return;
		}
		for (AbstractItem item : equips) {
			if (item instanceof Equipment) {
				collectSouledEquipment(player, (Equipment) item);
			}
		}
	}
	
	private void collectSouledEquipment(Player player, Equipment eq) {
		if (eq.getResource().getLevel() < COLLECT_EQUIP_MIN_LEVEL.getValue() || 
				eq.getResource().getLevel() > COLLECT_EQUIP_MAX_LEVEL.getValue()
				|| eq.getEquipmentType() == EquipmentType.RIGHTCUFF
				|| eq.getEquipmentType() == EquipmentType.LEFTCUFF) {
			return;
		}
		boolean equipType = !player.getCollect().getCollectEquipLog().containsKey(eq.getEquipmentType().getIndex());
		boolean sameRole = eq.getResource().getRoletype() == player.getRole();
		boolean hasSoul = ((Equipment) eq).hasSpecifiedTypeStat(EquipmentStatType.SOUL_STAT);
		boolean havntCollect = !player.getCollect().getCollectEquipLog().containsKey(eq.getEquipmentType().getIndex());
		if (equipType && sameRole && hasSoul && havntCollect) {
			player.getCollect().getCollectEquipLog().put(eq.getEquipmentType().getIndex(), eq);
			PacketSendUtility.sendPacket(player, SM_CollectNew_Equip.valueOf(player));
		}
	}
	
	public SM_Reward_FamedGeneral activeCollectGeneral(Player player, String id) {
		if (!ModuleOpenManager.getInstance().isOpenByKey(player, "opmk80")) {
			throw new ManagedException(ManagedErrorCode.MODULE_NOT_OPEN);
		}
		
		CollectGeneralResource res = collectGeneralResources.get(id, true);
		int num = player.getCollect().getFamedGeneral().getFamedGeneralCollectNum(res.getObjectKeyOwner());
		if (num < res.getNum()) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (!player.getCollect().getFamedGeneral().getActiveCollectGeneralIds().add(id)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		player.getGameStats().addModifiers(res.getStatsEffectId(), res.getStats(), true);
				
		if (res.getSkillId() != null && res.getSkillId().length >= 1) {
			player.getCollect().getFamedGeneral().refreshLearnSkill(player, player);
			
			I18nUtils utils = I18nUtils.valueOf("20304");
			utils.addParm("name", I18nPack.valueOf(player.getName()));
			utils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			utils.addParm("npc", I18nPack.valueOf(ObjectManager.getInstance().getObjectResource(res.getObjectKeyOwner()).getName()));
			ChatManager.getInstance().sendSystem(11001, utils, null);
			I18nUtils chatUtils = I18nUtils.valueOf("309204");
			chatUtils.addParm("name", I18nPack.valueOf(player.getName()));
			chatUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			chatUtils.addParm("npc", I18nPack.valueOf(ObjectManager.getInstance().getObjectResource(res.getObjectKeyOwner()).getName()));
			ChatManager.getInstance().sendSystem(0, chatUtils, null);
		}
		
		return SM_Reward_FamedGeneral.valueOf(id);
	}
	
}
