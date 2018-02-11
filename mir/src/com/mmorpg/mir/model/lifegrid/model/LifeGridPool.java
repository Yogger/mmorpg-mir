package com.mmorpg.mir.model.lifegrid.model;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.item.LifeGridItem;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_OperateLock;
import com.mmorpg.mir.model.lifegrid.packet.SM_LifeGrid_Point_Change;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class LifeGridPool {
	@Transient
	private transient Player owner;

	private LifeGridStorage equipStorage;
	private LifeGridStorage packStorage;
	private LifeGridStorage houseStorage;
	/** 碎片积分 */
	private int point;

	public LifeGridPool() {
		this.point = 0;
	}

	public static LifeGridPool valueOf(int equipSize, int packSize, int houseSize) {
		LifeGridPool pool = new LifeGridPool();
		pool.equipStorage = LifeGridStorage.valueOf(equipSize);
		pool.packStorage = LifeGridStorage.valueOf(packSize);
		pool.houseStorage = LifeGridStorage.valueOf(houseSize);
		return pool;
	}

	@JsonIgnore
	public void refreshEquipStats(boolean recomputed) {
		if (!ModuleOpenManager.getInstance().isOpenByModuleKey(owner, ModuleKey.LIFEGRID)) {
			return;
		}

		for (LifeGridItem item : equipStorage.getItems()) {
			if (item != null) {
				owner.getGameStats().replaceModifiers(
						StatEffectId.valueOf(item.getObjectId(), StatEffectType.EQUIPMENT),
						item.getResource().getStats(), false);
			}
		}
		if (recomputed) {
			owner.getGameStats().recomputeStats();
		}
	}

	@JsonIgnore
	public SM_LifeGrid_OperateLock operateLock(LifeStorageType storageType, int index) {
		LifeGridItem item = null;
		LifeGridStorage storage = getStorageByType(storageType);
		item = storage.getItemByIndex(index);
		if (item == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (item.isLock()) {
			item.closeLock();
		} else {
			item.openLock();
		}
		return SM_LifeGrid_OperateLock.valueOf(storageType.getType(), index, item);
	}

	@JsonIgnore
	public void addPoint(ModuleInfo moduleInfo, int v) {
		this.point += v;
		LogManager.addlifegridPoint(owner.getPlayerEnt().getServer(), owner.getPlayerEnt().getAccountName(),
				owner.getName(), owner.getObjectId(), moduleInfo, 1, System.currentTimeMillis(), v, this.point);
		PacketSendUtility.sendPacket(owner, SM_LifeGrid_Point_Change.valueOf(this.point));
	}

	@JsonIgnore
	public int decreasePoint(ModuleInfo moduleInfo, int v) {
		this.point -= v;
		LogManager.addlifegridPoint(owner.getPlayerEnt().getServer(), owner.getPlayerEnt().getAccountName(),
				owner.getName(), owner.getObjectId(), moduleInfo, -1, System.currentTimeMillis(), v, this.point);
		return this.point;
	}

	@JsonIgnore
	public LifeGridStorage getStorageByType(LifeStorageType storageType) {
		LifeGridStorage storage = null;
		if (storageType == LifeStorageType.EQUIP) {
			storage = this.equipStorage;
		} else if (storageType == LifeStorageType.PACK) {
			storage = this.packStorage;
		} else if (storageType == LifeStorageType.HOUSE) {
			storage = this.houseStorage;
		}
		return storage;
	}

	@JsonIgnore
	public long getTotalItemCountByKey(String key) {
		return this.equipStorage.getItemSizeByKey(key) + this.packStorage.getItemSizeByKey(key)
				+ this.houseStorage.getItemSizeByKey(key);
	}

	public LifeGridStorage getPackStorage() {
		return packStorage;
	}

	public void setPackStorage(LifeGridStorage packStorage) {
		this.packStorage = packStorage;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public LifeGridStorage getEquipStorage() {
		return equipStorage;
	}

	public void setEquipStorage(LifeGridStorage equipStorage) {
		this.equipStorage = equipStorage;
	}

	public LifeGridStorage getHouseStorage() {
		return houseStorage;
	}

	public void setHouseStorage(LifeGridStorage houseStorage) {
		this.houseStorage = houseStorage;
	}

}
