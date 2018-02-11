package com.mmorpg.mir.model.world;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.h2.util.New;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.mmorpg.mir.ClearAndMigrate;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.trigger.manager.TriggerManager;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.world.resource.MapCountry;
import com.mmorpg.mir.model.world.resource.MapResource;
import com.mmorpg.mir.model.world.resource.SafeAreaResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.JsonUtils;

@Component
public final class World implements ApplicationContextAware {

	public static final int INIT_INSTANCE = 1;

	private static final Logger logger = Logger.getLogger(World.class);

	@Static
	private Storage<Integer, MapResource> mapResources;

	@Static
	private Storage<String, SafeAreaResource> safeAreaResources;

	@Autowired
	private TriggerManager triggerManager;

	/**
	 * World maps supported by server.
	 */
	private final Map<Integer, WorldMap> worldMaps = new ConcurrentHashMap<Integer, WorldMap>();

	private static World instance;

	@PostConstruct
	public void init() throws Exception {
		if (ClearAndMigrate.clear) {
			return;
		}
		for (MapResource resource : mapResources.getAll()) {
			WorldMap map = createWorldMap(resource);
			worldMaps.put(resource.getMapId(), map);
		}

		instance = this;
	}

	private WorldMap createWorldMap(MapResource resource) throws Exception {
		WorldMap map = new WorldMap(this);
		map.setMapId(resource.getMapId());
		map.setName(resource.getName());
		map.setCopy(resource.isCopy());
		map.setMaxNum(resource.getMaxNum());
		map.setMaxChannel(resource.getMaxChannel());
		map.setInitChannelNum(resource.getInitChannelNum());
		map.setDeleteConditions(resource.getCoreConditions());
		map.setReliveId(resource.getReliveBaseResourceId());
		map.setCountry(MapCountry.valueOf(resource.getCountry()));

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(applicationContext.getResource("resource/map/" + resource.getFileName())
				.getFile());
		Element element = document.getDocumentElement();

		int xNum = Integer.parseInt(element.getAttribute("xNum"));
		int yNum = Integer.parseInt(element.getAttribute("yNum"));

		map.setxNum(xNum);
		map.setyNum(yNum);

		MapGrid[][] grids = new MapGrid[yNum][xNum];
		NodeList nodeList = ((Element) element.getElementsByTagName("Block").item(0)).getElementsByTagName("row");
		for (int i = 0, j = nodeList.getLength(); i < j; i++) {
			Element row = (Element) nodeList.item(i);
			String rowValue = row.getTextContent();
			String[] rowValues = rowValue.split(",");
			for (int m = 0, n = rowValues.length; m < n; m++) {
				grids[i][m] = new MapGrid();
				if (Integer.parseInt(rowValues[m]) != 0) {
					grids[i][m].openBlock();
				}
			}
		}

		nodeList = ((Element) element.getElementsByTagName("safe").item(0)).getElementsByTagName("row");
		for (int i = 0, j = nodeList.getLength(); i < j; i++) {
			Element row = (Element) nodeList.item(i);
			String rowValue = row.getTextContent();
			String[] rowValues = rowValue.split(",");
			for (int m = 0, n = rowValues.length; m < n; m++) {
				if (Integer.parseInt(rowValues[m]) != 0) {
					grids[i][m].openSafe();
				}
			}
		}

		map.setMapGrids(grids);

		logger.debug(String.format("map id [%s] name [%s] init finish", map.getMapId(), map.getName()));

		return map;
	}

	public static final World getInstance() {
		return instance;
	}

	public VisibleObject findObject(int mapId, int instanceId, long objId) {
		WorldMap worldMap = getWorldMap(mapId);
		if (worldMap != null) {
			WorldMapInstance instance = worldMap.getWorldMapInstanceById(instanceId);
			if (instance != null) {
				return instance.findObject(objId);
			}
		}
		return null;
	}

	/**
	 * 获取世界等级
	 * 
	 * @return
	 */
	public int getWorldLevel() {
		return WorldRankManager.getInstance().getWorldLevel();
	}

	/**
	 * Return World Map by id
	 * 
	 * @param id
	 *            - id of world map.
	 * @return World map.
	 */
	public WorldMap getWorldMap(int id) {
		return worldMaps.get(id);
	}

	/**
	 * Update position of VisibleObject [used when object is moving on one map
	 * instance]. Check if active map region changed and do all needed updates.
	 * 
	 * @param object
	 * @param newX
	 * @param newY
	 * @param newZ
	 * @param newHeading
	 */
	public void updatePosition(VisibleObject object, int newX, int newY, byte newHeading) {
		if (object instanceof Creature) {
			((Creature) object).getMoveController().stopMoving();
		}
		this.updatePosition(object, newX, newY, newHeading, true);
		if (object instanceof Player) {
			((Player) object).broadCastPosition();
		}
	}

	/**
	 * 
	 * @param object
	 * @param newX
	 * @param newY
	 * @param newZ
	 * @param newHeading
	 */
	public void updatePosition(VisibleObject object, int newX, int newY, byte newHeading, boolean updateKnownList) {
		// prevent updating object position in despawned state
		if (!object.isSpawned())
			return;

		object.getPosition().setXYH(newX, newY, newHeading);

		MapRegion oldRegion = object.getActiveRegion();
		if (oldRegion == null) {
			return;
		}

		MapRegion newRegion = oldRegion.getParent().getRegion(object);

		if (newRegion != oldRegion) {
			oldRegion.remove(object);
			newRegion.add(object);
			object.getPosition().setMapRegion(newRegion);
		}

		if (updateKnownList) {
			object.updateKnownlist();
		}

	}

	/**
	 * Set position of VisibleObject without spawning [object will be
	 * invisible]. If object is spawned it will be despawned first.
	 * 
	 * @param object
	 * @param mapId
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 * @throws NotSetPositionException
	 *             when object has not set position before.
	 */
	public void setPosition(VisibleObject object, int mapId, int x, int y, byte heading) {
		int instanceId = World.INIT_INSTANCE;
		if (object.getMapId() == mapId) {
			instanceId = object.getInstanceId();
		} else if (object instanceof Player) {
			instanceId = getWorldMap(mapId).getOrCreateWorldMapInstance((Player) object).getInstanceId();
		}

		this.setPosition(object, mapId, instanceId, x, y, heading);
	}

	/**
	 * 
	 * @param object
	 * @param mapId
	 * @param instance
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 */
	public void setPosition(VisibleObject object, int mapId, int instance, int x, int y, byte heading) {
		if (object.isSpawned())
			despawn(object);
		object.getPosition().setXYH(x, y, heading);
		object.getPosition().setMapId(mapId);
		object.getPosition().setMapRegion(getWorldMap(mapId).getWorldMapInstanceById(instance).getRegion(object));
	}

	/**
	 * Creates and return {@link WorldPosition} object, representing position
	 * with given parameters.
	 * 
	 * @param mapId
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 * @return WorldPosition
	 */
	public WorldPosition createPosition(Player player, int mapId, int x, int y, byte heading) {
		WorldPosition position = new WorldPosition();
		position.setXYH(x, y, heading);
		position.setMapId(mapId);
		position.setMapRegion(getWorldMap(mapId).getOrCreateWorldMapInstance(player).getRegion(x, y));
		return position;
	}

	public WorldPosition createPosition(int mapId, int instanceId, int x, int y, byte heading) {
		WorldPosition position = new WorldPosition();
		position.setXYH(x, y, heading);
		position.setMapId(mapId);
		position.setMapRegion(getWorldMap(mapId).getWorldMapInstanceById(instanceId).getRegion(x, y));
		return position;
	}

	public void spawn(VisibleObject object) {
		// 只有在角色没有处于出生状态下，才可以进行这个操作
		if (object.getPosition().isSpawned())
			return;

		object.getPosition().setIsSpawned(true);

		object.getActiveRegion().getParent().addObject(object);
		object.getActiveRegion().add(object);

		object.updateKnownlist();

		object.getController().onSpawn(object.getMapId(), object.getInstanceId());

	}

	public void despawn(VisibleObject object) {
		// 只有在角色处于出生状态下的时候，才可以进行重生调用
		if (object.getPosition() == null) {
			return;
		}
		if (!object.getPosition().isSpawned())
			return;

		if (object.getActiveRegion() != null) {
			if (object.getActiveRegion().getParent() != null)
				object.getActiveRegion().getParent().removeObject(object);
			object.getActiveRegion().remove(object);
		}
		object.clearKnownlist();

		// 触发器
		if (object.getSpawn() != null && object.getSpawn().getDespawnTriggers() != null) {
			for (String id : object.getSpawn().getDespawnTriggers()) {
				Map<String, Object> contexts = New.hashMap();
				contexts.put(TriggerContextKey.MAP_INSTANCE, object.getPosition().getMapRegion().getParent());
				triggerManager.trigger(contexts, id);
			}
		}

		object.getController().onDespawn();

		object.getPosition().setIsSpawned(false);
	}

	public Map<Integer, WorldMap> getWorldMaps() {
		return worldMaps;
	}

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public String[] getWorldMapInfo() {
		List<String> result = New.arrayList();
		for (WorldMap worldMap : worldMaps.values()) {
			Iterator<WorldMapInstance> ite = worldMap.iterator();
			while (ite.hasNext()) {
				WorldMapInstance instance = ite.next();
				result.add(instance.toString());
			}
		}
		return result.toArray(new String[result.size()]);
	}

	public String[] getWorldMapInfo(int mapId) {
		List<String> result = New.arrayList();
		WorldMap map = worldMaps.get(mapId);
		if (map != null) {
			Iterator<WorldMapInstance> ite = map.iterator();
			while (ite.hasNext()) {
				WorldMapInstance instance = ite.next();
				result.add(instance.toString());
			}
		}
		return result.toArray(new String[result.size()]);
	}

	public Storage<String, SafeAreaResource> getSafeAreaResources() {
		return safeAreaResources;
	}

	public void setSafeAreaResources(Storage<String, SafeAreaResource> safeAreaResources) {
		this.safeAreaResources = safeAreaResources;
	}

	public MapResource getMapResource(int mapId) {
		return mapResources.get(mapId, true);
	}

	/**
	 * 是否可以进入地图
	 * 
	 * @param player
	 * @param mapId
	 * @return
	 */
	public boolean canEnterMap(Player player, int mapId) {
		return canEnterMap(player, mapId, true);
	}

	public boolean canEnterMap(Player player, int mapId, boolean throwException) {
		MapResource mapResource = mapResources.get(mapId, true);
		if (player.getLifeStats().isAlreadyDead()) {
			if (throwException) {
				throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
			}
			return false;
		}

		if (player.getCountryValue() == mapResource.getCountry()) {
			if (mapResource.getNativeMilitartLevel() > player.getMilitary().getRank()) {
				if (throwException) {
					throw new ManagedException(ManagedErrorCode.MILITARY_NOT_ENOUGH);
				}
				return false;
			}
		} else {
			if (mapResource.getEnemyMilitartLevel() > player.getMilitary().getRank()) {
				if (throwException) {
					throw new ManagedException(ManagedErrorCode.MILITARY_NOT_ENOUGH);
				}
				return false;
			}
		}
		if (!mapResource.inLevel(player)) {
			if (throwException) {
				throw new ManagedException(ManagedErrorCode.LEVEL_NOT_ENOUGH);
			}
			return false;
		}
		if (mapResource.getCountry() != player.getCountryValue()) {
			if (mapResource.getForbidEnterCondition() != null) {
				if (mapResource.getForbidEnterCondition().verify(player, false)) {
					if (throwException) {
						throw new ManagedException(ManagedErrorCode.MAP_FORBIDTIME);
					}
					return false;
				}
			}
		}
		return true;
	}

	public static void main(String[] args) {
		System.out.println(JsonUtils.object2String(new Date()));
	}
}
