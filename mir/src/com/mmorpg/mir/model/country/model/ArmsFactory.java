package com.mmorpg.mir.model.country.model;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.vo.ArmsFactoryVO;
import com.mmorpg.mir.model.country.packet.SM_Country_Tank;
import com.mmorpg.mir.model.country.packet.SM_Country_UpgradeFactory;
import com.mmorpg.mir.model.country.resource.CountryFactoryResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.JsonUtils;

/**
 * 军工厂
 * 
 * @author Kuang Hao
 * @since v1.0 2014-9-26
 * 
 */
public class ArmsFactory {
	private Technology technology;
	public static final String INIT_FACTORYID = "1";
	private String factoryId;
	private Map<Integer, Tank> tanks = new HashMap<Integer, Tank>();

	public static ArmsFactory valueOf() {
		ArmsFactory af = new ArmsFactory();
		af.factoryId = INIT_FACTORYID;
		af.setTanks(new HashMap<Integer, Tank>());
		return af;
	}

	public ArmsFactoryVO createVO() {
		return ArmsFactoryVO.valueOf(this);
	}

	@JsonIgnore
	public Tank getPlayerTank(long playerId) {
//		for (Tank tank : tanks.values()) {
//			if (tank.getOwner() == playerId) {
//				return tank;
//			}
//		}
		return null;
	}

	@JsonIgnore
	public Tank distributeTank(int id, long playerId) {
		if (hasTank(playerId)) {
			throw new ManagedException(ManagedErrorCode.HAD_TANK);
		}
		Tank tank = tanks.get(id);
		if (tank == null) {
			throw new ManagedException(ManagedErrorCode.NOT_FOUND_TANK);
		}
		tank.take(playerId);
		return tank;
	}

	@JsonIgnore
	public long callbackTank(int id) {
		Tank tank = tanks.get(id);
		if (tank == null) {
			throw new ManagedException(ManagedErrorCode.NOT_FOUND_TANK);
		}
		if (!tank.hasOwner()) {
			throw new ManagedException(ManagedErrorCode.HAD_NOT_TANK);
		}
		long ownerId = tank.getOwner();
		tank.callback();
		return ownerId;
	}

	@JsonIgnore
	public boolean hasTank(long playerId) {
		return getPlayerTank(playerId) == null ? false : true;
	}

	@JsonIgnore
	public boolean isFull() {
		CountryFactoryResource resource = CountryManager.getInstance().getCountryFactoryResources()
				.get(factoryId, true);
		if (resource.getGarageSize() <= getTanks().size()) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public CountryFactoryResource getReousrce() {
		return CountryManager.getInstance().getCountryFactoryResources().get(factoryId, true);
	}

	@JsonIgnore
	public Tank getTank(int id) {
		return getTanks().get(id);
	}

	@JsonIgnore
	public int selectId() {
		if (getTanks().isEmpty()) {
			return 1;
		}
		for (int i = 1; i <= getTanks().size() + 1; i++) {
			if (!getTanks().containsKey(i)) {
				return i;
			}
		}
		throw new RuntimeException(String.format("不应该到达这里tanks[%s]", JsonUtils.object2String(getTanks())));
	}

	@JsonIgnore
	public void createTank(String resourceId, Player player, int index) {
		int id = selectId();
		Tank tank = Tank.valueOf(id, resourceId, index);
		getTanks().put(id, tank);
		// 通知前端
		PacketSendUtility.sendPacket(player, SM_Country_Tank.valueOf(tank));
	}

	@JsonIgnore
	public void upgradeTank(int id, String resourceId, Player player) {
		getTanks().get(id).setResourceId(resourceId);
		// 通知前端
		PacketSendUtility.sendPacket(player, SM_Country_Tank.valueOf(getTanks().get(id)));
	}

	@JsonIgnore
	public void upgradeFactory(String nextFactoryId) {
		this.factoryId = nextFactoryId;
		technology.getCountry().sendPacket(SM_Country_UpgradeFactory.valueOf(factoryId));
	}

	public String getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}

	@JsonIgnore
	public Technology getTechnology() {
		return technology;
	}

	public void setTechnology(Technology technology) {
		this.technology = technology;
	}

	public Map<Integer, Tank> getTanks() {
		return tanks;
	}

	public void setTanks(Map<Integer, Tank> tanks) {
		this.tanks = tanks;
	}

}
