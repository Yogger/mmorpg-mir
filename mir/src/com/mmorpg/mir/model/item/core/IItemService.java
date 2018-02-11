package com.mmorpg.mir.model.item.core;

import java.util.HashSet;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.PetItem;
import com.mmorpg.mir.model.item.model.EquipmentStorageType;
import com.mmorpg.mir.model.item.packet.CM_EquipReElement;
import com.mmorpg.mir.model.item.packet.CM_ExtendsEquip;
import com.mmorpg.mir.model.item.packet.CM_SmeltEquipment;
import com.mmorpg.mir.model.item.packet.CM_Super_Forge_Equip;
import com.mmorpg.mir.model.item.packet.SM_AddPackSize;
import com.mmorpg.mir.model.item.packet.SM_BuyBack;
import com.mmorpg.mir.model.item.packet.SM_Combining_CreateSoul;
import com.mmorpg.mir.model.item.packet.SM_Decompose_Item;
import com.mmorpg.mir.model.item.packet.SM_Drop;
import com.mmorpg.mir.model.item.packet.SM_Equip;
import com.mmorpg.mir.model.item.packet.SM_Equip_Create_Soul;
import com.mmorpg.mir.model.item.packet.SM_Equip_Inset_Gem;
import com.mmorpg.mir.model.item.packet.SM_Equip_Unset_Gem;
import com.mmorpg.mir.model.item.packet.SM_ExchagePack;
import com.mmorpg.mir.model.item.packet.SM_Merge;
import com.mmorpg.mir.model.item.packet.SM_MovePack;
import com.mmorpg.mir.model.item.packet.SM_Sell;
import com.mmorpg.mir.model.item.packet.SM_Split;
import com.mmorpg.mir.model.item.packet.SM_Store;
import com.mmorpg.mir.model.item.packet.SM_Take;
import com.mmorpg.mir.model.item.packet.SM_UnEquip;
import com.mmorpg.mir.model.item.packet.SM_UseItem;

public interface IItemService {

	public SM_Equip equip(Player player, int index);

	public SM_UnEquip unEquip(Player player, int index, EquipmentStorageType equipStorageType);

	public SM_ExchagePack exchangePack(Player player, byte type, int fromIndex, int toIndex);

	public SM_Store store(Player player, HashSet<Integer> indexs);

	public SM_Take take(Player player, HashSet<Integer> indexs);

	public SM_Drop drop(Player player, int[] indexs);

	public SM_UseItem useItem(Player player, int index, int num);

	public SM_Merge merge(Player player, byte type);

	public SM_MovePack movePack(Player player, byte type, int fromIndex, int toIndex);

	public SM_AddPackSize addPackSize(Player player, byte type, int index);

	public SM_Split split(Player player, byte type, int index, int size);

	public SM_Sell sell(Player player, HashSet<Integer> indexs);

	public SM_BuyBack buyBack(Player player, int index);

	public void enhanceEquipment(Player player, int equipIndex, boolean gold);

	public void enhanceEquipmentEx(Player player, int equipIndex, boolean gold, EquipmentStorageType equipStorageType);

	public void forgeEquipment(Player player, String id, String recipeKey);

	public void extendsEquipment(Player player, CM_ExtendsEquip req);

	public void extendsEquipEx(Player player, long mainEquipIndex, long viceEquipIndex, boolean selectElement,
			boolean selectSoul, boolean selectRare, EquipmentStorageType storageType);

	public void resetEquipElement(Player player, CM_EquipReElement req);

	public void smeltEquipment(Player player, CM_SmeltEquipment req);

	public void smeltEquipmentEx(Player player, HashSet<Integer> indexs, int isTreasureWare, EquipmentStorageType type);

	public void combiningItem(Player player, String combiningId, int addition, boolean useGold, int quantity);

	public void superForge(Player player, CM_Super_Forge_Equip req);

	public void seekTreasure(Player player, String id);

	public void queryAllTreasureHistory(Player player);

	public void takeFromTreasureStorage(Player player, int index);

	public void takeAllFromTreasureStorage(Player player);

	public SM_Equip_Create_Soul equipCreateSoul(Player player, long obj, String itemKey);

	public SM_Combining_CreateSoul combiningCreateSoul(Player player, String createSoulKey, int count);

	public SM_Decompose_Item spliteCreateSoul(Player player, HashSet<Long> indexes);

	public SM_Equip_Inset_Gem insetEquipGem(Player player, long equipmentId, long gemId);

	public SM_Equip_Unset_Gem unsetEquipGem(Player player, long equipmentId, int index);

	public void attachGodExtraStat(Player player, int index, boolean addRate);

	public void attachTurnExtra(Player player, int index, boolean addRate);

	public void changeEquipmentSuit(Player player, int index);

	public void transferEnhanceLevel(Player player);

	public void transferSoulLevel(Player player);

	public void upgradeEquipmentSoulLevel(Player player, int index);

	public void transferTreasureItems(Player player);

	public PetItem callPet(Player player, int index);

	public void callBackPet(Player player);

	public void deleteNewerPet(Player player, boolean broadCast);

	public void combiningItemEx(Player player, String combingId, int packIndex);
}
