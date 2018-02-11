package com.mmorpg.mir.model.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.world.resource.MapCountry;
import com.mmorpg.mir.model.world.service.MapInstanceService;

public final class WorldMap {

	private AtomicInteger nextInstanceId = new AtomicInteger(0);

	private Map<Integer, WorldMapInstance> instances = new ConcurrentHashMap<Integer, WorldMapInstance>();

	private final World world;

	private int mapId;

	private String name;

	private MapGrid[][] mapGrids;

	private int xNum;

	private int yNum;

	private boolean copy;

	private int maxNum;

	private CoreConditions deleteConditions;

	private int reliveId;

	private MapCountry country;

	private int maxChannel;

	private int initChannelNum;
	
	// only for resource check
	public WorldMap() {
		this.world = null;
	}

	public WorldMap(World world) {
		this.world = world;
	}

	public WorldMapInstance getOrCreateWorldMapInstance(Player player) {
		int channelId = World.INIT_INSTANCE;
		if (player != null) {
			if (player.getExpress().hadLorry()) {
				return instances.get(channelId);
			}
			if (player.isInGroup()) {
				Player groupLeader = player.getPlayerGroup().getGroupLeader();
				if (groupLeader != player) {
					if (groupLeader.getPosition() != null && groupLeader.getMapId() == mapId) {
						channelId = groupLeader.getInstanceId();
						WorldMapInstance leaderInstance = instances.get(channelId);
						if (leaderInstance != null && !leaderInstance.isFull()) {
							player.setDefaultMapChannel(channelId);
							return leaderInstance;
						}

					}
				}
			}
		}

		if (player != null && player.getDefaultMapChannel() != 0) {
			WorldMapInstance defaultInstance = instances.get(player.getDefaultMapChannel());
			if (defaultInstance != null) {
				if (defaultInstance.getPlayerCount() != 0) {
					return defaultInstance;
				}
			}
		}
		WorldMapInstance minPlayerNumInstance = null;
		int playerNum = Integer.MAX_VALUE;
		int allPlayerNum = 0;
		for (WorldMapInstance instance : instances.values()) {
			if (instance.getPlayerCount() < playerNum) {
				playerNum = instance.getPlayerCount();
				minPlayerNumInstance = instance;
			} else if (instance.getPlayerCount() == playerNum
					&& instance.getInstanceId() < minPlayerNumInstance.getInstanceId()) {
				playerNum = instance.getPlayerCount();
				minPlayerNumInstance = instance;
			}
			allPlayerNum += instance.getPlayerCount();
		}

		if (allPlayerNum >= maxNum * instances.size() && instances.size() < getMaxChannel()) {
			WorldMapInstance newInstance = MapInstanceService.createCommonMapCopy(mapId);
			if (player != null) {
				player.setDefaultMapChannel(newInstance.getInstanceId());
			}
			return newInstance;
		}
		return minPlayerNumInstance;

	}

	public WorldMapInstance getWorldMapInstanceById(int instanceId) {
		return getWorldMapInstance(instanceId);
	}

	private WorldMapInstance getWorldMapInstance(int instanceId) {
		return instances.get(instanceId);
	}

	public void removeWorldMapInstance(int instanceId) {
		instances.remove(instanceId);
	}

	public void addInstance(int instanceId, WorldMapInstance instance) {
		instances.put(instanceId, instance);
	}

	public World getWorld() {
		return world;
	}

	public int getNextInstanceId() {
		return nextInstanceId.incrementAndGet();
	}

	public WorldMapInstance createNewInstance() {
		return createNewInstance(getNextInstanceId());
	}

	public WorldMapInstance createNewInstance(int instance) {
		WorldMapInstance worldMapInstance = new WorldMapInstance(this, instance);
		addInstance(worldMapInstance.getInstanceId(), worldMapInstance);
		return worldMapInstance;
	}

	public Iterator<WorldMapInstance> iterator() {
		return instances.values().iterator();
	}

	public List<Integer> getAllIntanceIds() {
		List<Integer> ids = new ArrayList<Integer>();
		for (Integer id : instances.keySet()) {
			ids.add(id);
		}
		return ids;
	}

	public int getMaxNotEmptyMapOrInitInstanceId() {
		int max = 0;
		for (Integer id : instances.keySet()) {
			if (max < id
					&& (id.intValue() == World.INIT_INSTANCE || instances.get(id).getPlayerCount() != 0 || id <= getInitChannelNum())) {
				max = id;
			}
		}
		return max;
	}

	public MapGrid[][] getMapGrids() {
		return mapGrids;
	}

	public void setMapGrids(MapGrid[][] mapGrids) {
		this.mapGrids = mapGrids;
	}

	public int getxNum() {
		return xNum;
	}

	public void setxNum(int xNum) {
		this.xNum = xNum;
	}

	public int getyNum() {
		return yNum;
	}

	public void setyNum(int yNum) {
		this.yNum = yNum;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isBlock(int x, int y) {
		return mapGrids[y][x].isBlock();
	}

	public boolean isSafe(int x, int y) {
		return !isOut(x, y) && mapGrids[y][x].isSafe();
	}

	public boolean isOut(int x, int y) {
		return x < 0 || x >= xNum || y < 0 || y >= yNum;
	}

	public boolean isCopy() {
		return copy;
	}

	public void setCopy(boolean copy) {
		this.copy = copy;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public CoreConditions getDeleteConditions() {
		return deleteConditions;
	}

	public void setDeleteConditions(CoreConditions deleteConditions) {
		this.deleteConditions = deleteConditions;
	}

	public Map<Integer, WorldMapInstance> getInstances() {
		return instances;
	}

	public void setInstances(Map<Integer, WorldMapInstance> instances) {
		this.instances = instances;
	}

	public int getReliveId() {
		return reliveId;
	}

	public void setReliveId(int reliveId) {
		this.reliveId = reliveId;
	}

	public void setNextInstanceId(AtomicInteger nextInstanceId) {
		this.nextInstanceId = nextInstanceId;
	}

	public MapCountry getCountry() {
		return country;
	}

	public void setCountry(MapCountry country) {
		this.country = country;
	}

	public int[] createXY(int x, int y, int halfRange) {
		if (halfRange < 1) {
			return new int[] { x, y };
		}
		int[] result = new int[2];
		Random random = MathUtil.getRandom();
		result[0] = x + random.nextInt(halfRange * 2) - halfRange;
		result[1] = y + random.nextInt(halfRange * 2) - halfRange;
		if (isOut(result[0], result[1]) || isBlock(result[0], result[1])) {
			result[0] = x;
			result[1] = y;
		}
		return result;
	}

	public int getMaxChannel() {
		return maxChannel;
	}

	public void setMaxChannel(int maxChannel) {
		this.maxChannel = maxChannel;
	}

	public int getInitChannelNum() {
		return initChannelNum;
	}

	public void setInitChannelNum(int initChannelNum) {
		this.initChannelNum = initChannelNum;
	}

}
