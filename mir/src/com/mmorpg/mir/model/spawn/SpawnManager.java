package com.mmorpg.mir.model.spawn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.controllers.DropObjectController;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.drop.model.MonsterKilledHistory;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.DropObject;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.gascopy.config.GasCopyMapConfig;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.object.ObjectManager;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.dynamicstats.StatsResource;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.object.route.QuestRouteStep;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.util.IdentifyManager;
import com.mmorpg.mir.model.util.IdentifyManager.IdentifyType;
import com.mmorpg.mir.model.world.Position;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.WorldPosition;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.DateUtils;

/**
 * 出生引擎，这个引擎用来管理所有的非玩家生物的出生
 * 
 * @author zhou.liu
 * 
 */
@Component
public class SpawnManager implements ISpawnManager {
	private static final Logger log = Logger.getLogger(SpawnManager.class);

	private Map<ObjectType, Integer> objectMap = new HashMap<ObjectType, Integer>();

	@Static("PUBLIC:DISAPPEAR_TIME")
	private ConfigValue<Integer> DROP_DESPAWN_TIME;
	@Static("PUBLIC:DISAPPEAR_TIME_ITEM")
	private ConfigValue<Integer[]> DROP_DESPAWN_TIME_ITEM;
	@Static("PUBLIC:MONSTER_DROP_INTERVAL")
	private ConfigValue<Integer> MONSTER_DROP_INTERVAL;

	@Static("PUBLIC:BIG_BROTHER_ROUTE_1")
	private ConfigValue<QuestRouteStep[]> BIG_BROTHER_ROUTE_1;
	@Static("PUBLIC:BIG_BROTHER_ROUTE_2")
	private ConfigValue<QuestRouteStep[]> BIG_BROTHER_ROUTE_2;
	@Static("PUBLIC:BIG_BROTHER_ROUTE_3")
	private ConfigValue<QuestRouteStep[]> BIG_BROTHER_ROUTE_3;
	
	@Static("PUBLIC:SPECIAL_COUNTRYFLAG_GUARD_NPCS")
	public ConfigValue<String[]> SPECIAL_COUNTRYFLAG_GUARD_NPCS;

	private final ArrayList<Position> GENERATOR_DROP_VECTOR = new ArrayList<Position>();

	@Static
	protected Storage<Integer, StatsResource> statsResources;
	
	private static final int DROP_ITEM_MAX_SIZE = 128;

	@Autowired
	private World world;
	@Autowired
	private ObjectManager objectManager;

	@Static
	private Storage<String, SpawnGroupResource> spawnGroupResourceStorage;

	@Autowired
	private IdentifyManager identifyManager;

	private static SpawnManager self;

	public static SpawnManager getInstance() {
		return self;
	}

	@PostConstruct
	protected void init() {
		// TODO 这里用来初始化配置表

		// 创建所有的npc类型对象
		self = this;

		int dropInterval = MONSTER_DROP_INTERVAL.getValue().intValue();
		Position[] MONAD_VECTOR = { new Position(0, dropInterval), new Position(dropInterval, 0),
				new Position(0, -dropInterval), new Position(-dropInterval, 0) };
		int monad_vector_indexer = 0;
		LinkedList<Position> genteratedPositions = new LinkedList<Position>();
		Position previous = new Position(); // base point
		genteratedPositions.add(previous);

		while (genteratedPositions.size() < DROP_ITEM_MAX_SIZE) { // 掉落的最大数量
			int index = monad_vector_indexer % MONAD_VECTOR.length;
			Position newPosition = previous.translateNew(MONAD_VECTOR[index]);
			if (!genteratedPositions.contains(newPosition)) {
				genteratedPositions.add(newPosition);
				monad_vector_indexer++;
				previous = newPosition;
			} else {
				monad_vector_indexer--;
			}
		}
		GENERATOR_DROP_VECTOR.addAll(genteratedPositions);
		// System.out.println(GENERATOR_DROP_VECTOR);
	}

	public VisibleObject spawnObject(String spawnKey, int instanceIndex, Object... args) {
		SpawnGroupResource resource = getSpawn(spawnKey);
		WorldMap map = world.getWorldMap(resource.getMapId());
		if (map.isOut(resource.getX(), resource.getY()) || map.isBlock(resource.getX(), resource.getY())) {
			log.error(String.format("mapId : [%s] mapName : [%s] spawn resource [%s] is out or in block",
					map.getMapId(), map.getName(), resource));
		}
		return spawnObject(resource, instanceIndex, args);
	}

	public VisibleObject creatObject(String spawnKey, int instanceIndex, Object... args) {
		SpawnGroupResource spawn = getSpawn(spawnKey);
		VisibleObject object = objectManager.createObject(spawn, instanceIndex, args);

		if (object != null) {
			object.setSpawnKey(spawn.getKey());
		}

		return object;
	}

	public VisibleObject spawnObject(SpawnGroupResource spawn, int instanceIndex, Object... args) {
		VisibleObject object = objectManager.createObject(spawn, instanceIndex, args);

		if (object != null) {
			object.setSpawnKey(spawn.getKey());
			bringIntoWorld(object, instanceIndex);

			ObjectType ot = object.getObjectType();
			if (!objectMap.containsKey(ot)) {
				objectMap.put(ot, 0);
			}

			objectMap.put(ot, objectMap.get(ot) + 1);

		}

		return object;
	}

	public void bringIntoWorld(VisibleObject visibleObject, int instanceIndex) {
		World world = World.getInstance();
		world.setPosition(visibleObject, visibleObject.getMapId(), instanceIndex, visibleObject.getX(),
				visibleObject.getY(), visibleObject.getHeading());
		world.spawn(visibleObject);
	}

	/**
	 * Spawn all NPC's from templates
	 */
	public void spawnAll() {

		for (WorldMap map : world.getWorldMaps().values()) {
			if (!map.isCopy()) {
				for (int i = 0; i < Math.max(map.getInitChannelNum(), 1); i++) {
					WorldMapInstance worldMapInstance = map.createNewInstance();
					spawnInstance(map.getMapId(), worldMapInstance.getInstanceId());
				}
			}
		}
		
		for (String spawnKey : SPECIAL_COUNTRYFLAG_GUARD_NPCS.getValue()) {
			Creature guard = (Creature) spawnObject(spawnGroupResourceStorage.get(spawnKey, true), 1);
			guard.getEffectController().setAbnormal(EffectId.GOD.getEffectId(), true);
		}

		for (Entry<ObjectType, Integer> entry : objectMap.entrySet()) {
			log.warn("Loaded " + entry.getValue() + " " + entry.getKey() + "  spawns");
		}

	}

	/**
	 * 
	 * @param mapId
	 * @param instanceIndex
	 */
	public void spawnInstance(int mapId, int instanceIndex) {

		int instanceSpawnCounter = 0;
		// 这里根据地图id获取对应的生物资源列表
		for (SpawnGroupResource resource : spawnGroupResourceStorage.getIndex("mapId", mapId)) {
			if (!resource.isNoAutoSpawn()) {
				instanceSpawnCounter += spawn(resource, instanceIndex);
			}
		}
		// 这里根据地图id来初始化

		log.warn("Spawned " + mapId + " [" + instanceIndex + "] : " + instanceSpawnCounter);
	}

	public int spawn(SpawnGroupResource resource, int instanceIndex) {
		return spawn(resource, instanceIndex, 0);
	}

	public int spawn(SpawnGroupResource resource, int instanceIndex, int num) {
		WorldMap map = world.getWorldMap(resource.getMapId());
		ObjectResource or = ObjectManager.getInstance().getObjectResource(resource.getObjectKey());
		if (or.getObjectType() != ObjectType.SCULPTURE
				&& (map.isOut(resource.getX(), resource.getY()) || map.isBlock(resource.getX(), resource.getY()))) {
			log.error(String.format("mapId : [%s] mapName : [%s] spawn resource [%s] is out or in block",
					map.getMapId(), map.getName(), resource));
			// throw new RuntimeException(String.format(
			// "mapId : [%s] mapName : [%s] spawn resource [%s] is out or in block",
			// map.getMapId(),
			// map.getName(), resource));
		}
		int instanceSpawnCounter = 0;
		int number = num == 0 ? resource.getNum() : num;

		for (int i = 0, j = number; i < j; i++) {
			spawnObject(resource, instanceIndex);
			instanceSpawnCounter++;
		}
		return instanceSpawnCounter;
	}

	public SpawnGroupResource getSpawn(String key) {
		return spawnGroupResourceStorage.get(key, true);
	}

	public void spawnDropObject(Creature lastAttacker, Player mostDamagePlayer, Npc monster, Collection<Player> owners) {
		if (monster.getObjectResource().getDropKey() == null) {
			return;
		}
		// 判断掉落与否的条件
		Player higestPlayer = null; // 等级最高的
		for (Player p : owners) {
			if (higestPlayer == null || p.getLevel() > higestPlayer.getLevel()) {
				higestPlayer = p;
			}
		}

		if (higestPlayer == null) {
			higestPlayer = mostDamagePlayer;
		}

		int interval = monster.getObjectResource().getLevelGap();
		if (higestPlayer.getLevel() - monster.getLevel() > interval) {
			return;
		}
		Reward reward = null;

		// 设置怪物掉落信息
		synchronized (mostDamagePlayer.getDropHistory()) {
			if (ServerState.getInstance().getMonsterKilledHis().containsKey(monster.getObjectKey())) {
				MonsterKilledHistory monsterKilledHistory = ServerState.getInstance().getMonsterKilledHis()
						.get(monster.getObjectKey());
				mostDamagePlayer.getDropHistory().setMonsterKilledHistory(monsterKilledHistory);
			}
			List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(mostDamagePlayer,
					monster.getObjectResource().getDropKey());
			reward = RewardManager.getInstance().createRewardButNotMerge(mostDamagePlayer, rewardIds, null);
			mostDamagePlayer.getDropHistory().setMonsterKilledHistory(null);
		}

		Map<CoreConditionType, CoreConditionResource> dropConditionTypes = ChooserManager.getInstance()
				.selectChooserAllConditions(monster.getObjectResource().getDropKey());

		// 记录掉落
		if (ServerState.getInstance().getMonsterKilledHis().containsKey(monster.getObjectKey())) {
			for (RewardItem item : reward.getItems()) {
				if (item.getRewardType() == RewardType.ITEM) {
					ItemResource itemResource = ItemManager.getInstance().getResource(item.getCode());
					if (itemResource.isDropRecord()) {
						MonsterKilledHistory monsterKilledHistory = ServerState.getInstance().getMonsterKilledHis()
								.get(monster.getObjectKey());
						// 添加掉落数量
						monsterKilledHistory.putItem(item.getCode(), item.getAmount());
						// 添加个人每日掉落数量
						mostDamagePlayer.getDropHistory().addPlayerDropHis(item.getCode(), item.getAmount());
						// mostDamagePlayer.getDropHistory().add
						if (dropConditionTypes.containsKey(CoreConditionType.PLAYER_MONSTER_HUNT_COUNT)) {
							// 清理玩家对应道具杀怪次数
							mostDamagePlayer.getDropHistory()
									.resetSpecifiedItem(item.getCode(), monster.getObjectKey());
						}

						if (dropConditionTypes.containsKey(CoreConditionType.MONSTER_HUNT_COUNT)) {
							if (CoreConditionManager.getInstance()
									.getCoreConditions(1, dropConditionTypes.get(CoreConditionType.MONSTER_HUNT_COUNT))
									.verify(mostDamagePlayer, false)) {
								// 清理怪物击杀数量
								monsterKilledHistory.clearKilledCount();
							}
						}
					}
				}
			}
		}

		monster.onDrop(reward, lastAttacker, mostDamagePlayer);
		WorldMap worldMap = World.getInstance().getWorldMap(monster.getMapId());

		int dropPositionIndex = 0;
		Collections.shuffle(reward.getItems());
		for (RewardItem rewardItem : reward.getItems()) {
			int despawnTime = 0;
			if (rewardItem.getRewardType() == RewardType.ITEM) {
				int quality = ItemManager.getInstance().getResource(rewardItem.getCode()).getQuality();
				if (quality == 0) {
					quality = 1;
				}
				despawnTime = (int) (DROP_DESPAWN_TIME_ITEM.getValue()[quality - 1] * DateUtils.MILLIS_PER_SECOND);
			} else {
				despawnTime = (int) (DROP_DESPAWN_TIME.getValue() * DateUtils.MILLIS_PER_SECOND);
			}

			// 掉落的位置
			Position dropPosition = null;
			int monsterX = monster.getX();
			int monsterY = monster.getY();
			while (dropPositionIndex < GENERATOR_DROP_VECTOR.size()) {
				dropPosition = GENERATOR_DROP_VECTOR.get(dropPositionIndex++);
				if (!worldMap.isOut(dropPosition.getX() + monsterX, dropPosition.getY() + monsterY)
						&& !worldMap.isBlock(dropPosition.getX() + monsterX, dropPosition.getY() + monsterY)) {
					break;
				}
			}
			if (dropPosition == null) { // true的话, 肯定是配置问题
				return;
			}
			WorldPosition position = world.createPosition(monster.getMapId(), monster.getPosition().getInstanceId(),
					monsterX + dropPosition.getX(), monsterY + dropPosition.getY(), (byte) 0);
			final DropObject dropObject = DropObject.valueOf(identifyManager.getNextIdentify(IdentifyType.DROPOBJECT),
					new DropObjectController(), position, rewardItem, despawnTime, monster.getSpawnKey());
			if (!monster.getObjectResource().isDropWithNoOwner()) {
				if (monster.getObjectType() != ObjectType.BOSS &&
						GasCopyMapConfig.getInstance().isInGasCopyMap(monster.getMapId())) {
					dropObject.addOwnership(mostDamagePlayer.getObjectId());
				} else {
					for (Player player : owners) {
						dropObject.addOwnership(player.getObjectId());
					}
				}
			}
			bringIntoWorld(dropObject, monster.getInstanceId());
			dropObject.startClearTask();
		}
	}

	public SpawnGroupResource createSpawnGroupResource(VisibleObject object, String key) {
		SpawnGroupResource resource = new SpawnGroupResource();
		resource.setMapId(object.getMapId());
		resource.setX(object.getX());
		resource.setY(object.getY());
		resource.setRange(5);
		resource.setObjectKey(key);
		return resource;
	}

	public QuestRouteStep[] getQuestRouteStep(CountryId countryId) {
		switch (countryId) {
		case C1:
			return BIG_BROTHER_ROUTE_1.getValue();
		case C2:
			return BIG_BROTHER_ROUTE_2.getValue();
		case C3:
			return BIG_BROTHER_ROUTE_3.getValue();
		}
		return null;
	}

	public void reConstructNpcDynamicStats(Creature creature, ObjectResource resource, int level) {
		int worldLevel = resource.getMinWorldLevel() + level;
		if (resource.getMaxWorldLevel() != 0) {
			worldLevel = Math.min(resource.getMaxWorldLevel(), worldLevel);
		}
		creature.getGameStats().replaceModifiers(StatEffectId.valueOf("level_base", StatEffectType.LEVEL_BASE),
				resource.getStats(statsResources.get(worldLevel, true)), true);
		creature.setLevel(worldLevel);
	}

}
