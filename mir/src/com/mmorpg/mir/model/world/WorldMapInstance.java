package com.mmorpg.mir.model.world;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.h2.util.New;

import com.mmorpg.mir.model.controllers.MonsterController;
import com.mmorpg.mir.model.copy.model.CopyInfo;
import com.mmorpg.mir.model.gameobjects.DropObject;
import com.mmorpg.mir.model.gameobjects.Gatherable;
import com.mmorpg.mir.model.gameobjects.JourObject;
import com.mmorpg.mir.model.gameobjects.Lorry;
import com.mmorpg.mir.model.gameobjects.Monster;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.StaticObject;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class WorldMapInstance {

	public static final int regionX = 40;

	public static final int regionY = 20;

	private static final int maxWorldSize = 1000;

	private final WorldMap parent;

	private final Map<Integer, MapRegion> regions = new ConcurrentHashMap<Integer, MapRegion>();

	private final Map<Long, VisibleObject> worldMapObjects = new ConcurrentHashMap<Long, VisibleObject>();

	private final Map<Long, Player> worldMapPlayers = new ConcurrentHashMap<Long, Player>();

	private final Set<Long> registeredObjects = Collections.newSetFromMap(new ConcurrentHashMap<Long, Boolean>());

	private Future<?> emptyInstanceTask = null;

	private List<Future<?>> triggerTasks = new CopyOnWriteArrayList<Future<?>>();
	/** 可能需要在其它地方调用的任务 */
	private Map<String, Future<?>> callbackFutures = new HashMap<String, Future<?>>();

	private int instanceId;

	private AtomicInteger playerSize = new AtomicInteger(0);
	/** 副本信息 */
	private CopyInfo copyInfo = new CopyInfo();
	/** 创建时间 */
	private long createTime;
	/** 是否开启安全区 */
	private boolean openSafeArea = true;
	/** 阻挡区 */
	private Set<Integer> blocks = new HashSet<Integer>();
	/** 已经销毁 */
	private volatile boolean destory;

	public WorldMapInstance(WorldMap parent, int instanceId) {
		this.parent = parent;
		this.instanceId = instanceId;
		this.createTime = System.currentTimeMillis();
	}

	public void addBlocks(List<Block> blocks) {
		for (Block block : blocks) {
			this.blocks.add((block.getX() << 12) + block.getY());
		}
	}

	public void clearBlocks(List<Block> blocks) {
		for (Block block : blocks) {
			this.blocks.remove((block.getX() << 12) + block.getY());
		}
	}

	public boolean isBlock(int x, int y) {
		return blocks.contains((x << 12) + y);
	}

	public WorldMap getParent() {
		return parent;
	}

	MapRegion getRegion(VisibleObject object) {
		return getRegion(object.getX(), object.getY());
	}

	MapRegion getRegion(int x, int y) {
		Integer regionId = getRegionId(x, y);
		MapRegion region = regions.get(regionId);
		if (region == null) {
			synchronized (this) {
				region = regions.get(regionId);
				if (region == null) {
					region = createMapRegion(regionId);
				}
			}
		}
		return region;
	}

	private Integer getRegionId(int x, int y) {
		return x / regionX * maxWorldSize + y / regionY;
	}

	private MapRegion createMapRegion(Integer regionId) {
		MapRegion r = new MapRegion(regionId, this);
		regions.put(regionId, r);

		int rx = regionId / maxWorldSize;
		int ry = regionId % maxWorldSize;

		for (int x = rx - 1; x <= rx + 1; x++) {
			for (int y = ry - 1; y <= ry + 1; y++) {
				if (x == rx && y == ry)
					continue;
				int neighbourId = x * maxWorldSize + y;

				MapRegion neighbour = regions.get(neighbourId);
				if (neighbour != null) {
					r.addNeighbourRegion(neighbour);
					neighbour.addNeighbourRegion(r);
				}
			}
		}
		return r;
	}

	public World getWorld() {
		return getParent().getWorld();
	}

	public void addObject(VisibleObject object) {
		if (worldMapObjects.put(object.getObjectId(), object) != null)
			return;

		if (object instanceof Player) {
			worldMapPlayers.put(object.getObjectId(), (Player) object);
			playerSize.incrementAndGet();
		}

	}

	@SuppressWarnings("unchecked")
	public <T extends VisibleObject> List<T> findObjectByType(ObjectType type) {
		Iterator<VisibleObject> objectIterator = objectIterator();
		List<T> objects = New.arrayList();
		while (objectIterator.hasNext()) {
			VisibleObject object = objectIterator.next();
			if (object.getObjectType() == type) {
				objects.add((T) object);
			}
		}
		return objects;
	}

	public void removeObject(JourObject object) {
		worldMapObjects.remove(object.getObjectId());
		if (object instanceof Player) {
			if (worldMapPlayers.remove(object.getObjectId()) != null) {
				playerSize.decrementAndGet();
			}
		}

	}

	public VisibleObject findObject(long objId) {
		return worldMapObjects.get(objId);
	}

	public List<VisibleObject> findObjectBySpawnId(String spawnId) {
		List<VisibleObject> vos = New.arrayList();
		for (VisibleObject visibleObject : worldMapObjects.values()) {
			if (visibleObject.getSpawnKey() != null && visibleObject.getSpawnKey().equals(spawnId)) {
				vos.add(visibleObject);
			}
		}
		return vos;
	}

	public Player findPlayer(long objId) {
		return worldMapPlayers.get(objId);
	}

	public int getInstanceId() {
		return instanceId;
	}

	public boolean isInInstance(Long objId) {
		return worldMapPlayers.containsKey(objId);
	}

	public Iterator<VisibleObject> objectIterator() {
		return worldMapObjects.values().iterator();
	}

	public Iterator<Player> playerIterator() {
		return worldMapPlayers.values().iterator();
	}

	public void register(Long objectId) {
		registeredObjects.add(objectId);
	}

	public boolean isRegistered(Long objectId) {
		return registeredObjects.contains(objectId);
	}

	public Future<?> getEmptyInstanceTask() {
		return emptyInstanceTask;
	}

	public void setEmptyInstanceTask(Future<?> emptyInstanceTask) {
		this.emptyInstanceTask = emptyInstanceTask;
	}

	public void cancelEmptyTask() {
		if (emptyInstanceTask != null && (!emptyInstanceTask.isCancelled())) {
			emptyInstanceTask.cancel(false);
		}
	}

	public int getPlayerCount() {
		return playerSize.get();
	}

	public boolean isFull() {
		if (parent.getMaxNum() <= 0) {
			return false;
		}
		return playerSize.get() >= parent.getMaxNum();
	}

	@Override
	public String toString() {
		int objectSize = 0;
		int playerSize = 0;
		int monsterSize = 0;
		int dropSize = 0;
		int gatherSize = 0;
		int staticSize = 0;
		int npcSize = 0;
		int summonSize = 0;
		int lorrySize = 0;

		for (VisibleObject obj : worldMapObjects.values()) {
			if (obj instanceof Monster) {
				monsterSize++;
			} else if (obj instanceof Player) {
				playerSize++;
			} else if (obj instanceof DropObject) {
				dropSize++;
			} else if (obj instanceof Gatherable) {
				gatherSize++;
			} else if (obj instanceof Npc) {
				npcSize++;
			} else if (obj instanceof StaticObject) {
				staticSize++;
			} else if (obj instanceof Summon) {
				summonSize++;
			} else if (obj instanceof Lorry) {
				lorrySize++;
			}
			objectSize++;
		}

		return String
				.format("MapId : [%d] MapName : [%s] ObjectSize : [%d] MonsterSize : [%d] playerSize : [%d] dropSize : [%d] gatherSize : [%d] npcSize : [%d] staticSize : [%d] summonSize : [%d] lorrySize : [%d]",
						parent.getMapId(), parent.getName(), objectSize, monsterSize, playerSize, dropSize, gatherSize,
						npcSize, staticSize, summonSize, lorrySize);
	}

	public void monsterStopSpawn() {
		for (VisibleObject obj : worldMapObjects.values()) {
			if (obj instanceof Monster) {
				MonsterController mc = (MonsterController) obj.getController();
				mc.setRespwan(false);
			}
		}
	}

	public void stopTasks() {
		for (Future<?> future : getTriggerTasks()) {
			if (!future.isCancelled()) {
				future.cancel(true);
			}
		}

		for (Entry<String, Future<?>> entry : getCallbackFutures().entrySet()) {
			entry.getValue().cancel(true);
		}
	}

	public List<Future<?>> getTriggerTasks() {
		return triggerTasks;
	}

	public void setTriggerTasks(List<Future<?>> triggerTasks) {
		this.triggerTasks = triggerTasks;
	}

	public void addTriggerTask(Future<?> future) {
		triggerTasks.add(future);
	}

	public CopyInfo getCopyInfo() {
		return copyInfo;
	}

	public void setCopyInfo(CopyInfo copyInfo) {
		this.copyInfo = copyInfo;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public Map<String, Future<?>> getCallbackFutures() {
		return callbackFutures;
	}

	public void setCallbackFutures(Map<String, Future<?>> callbackFutures) {
		this.callbackFutures = callbackFutures;
	}

	public boolean isOpenSafeArea() {
		return openSafeArea;
	}

	public void setOpenSafeArea(boolean openSafeArea) {
		this.openSafeArea = openSafeArea;
	}

	public boolean isDestory() {
		return destory;
	}

	public void setDestory(boolean destory) {
		this.destory = destory;
	}

	public void sendPackAll(Object pack) {
		Iterator<Player> playerIter = playerIterator();
		while (playerIter.hasNext()) {
			PacketSendUtility.sendPacket(playerIter.next(), pack);
		}
	}

}
