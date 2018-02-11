package com.mmorpg.mir.model.world;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.cliffc.high_scale_lib.NonBlockingHashMapLong;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;

public final class MapRegion {
	/**
	 * Region id of this map region [NOT WORLD ID!]
	 */
	private final Integer regionId;
	/**
	 * WorldMapInstance witch is parent of this map region.
	 */
	private final WorldMapInstance parent;
	/**
	 * Surrounding regions + self.
	 */
	private final List<MapRegion> neighbours = new CopyOnWriteArrayList<MapRegion>();
	/**
	 * Objects on this map region.
	 */
	private final NonBlockingHashMapLong<VisibleObject> objects = new NonBlockingHashMapLong<VisibleObject>();

	private int playerCount = 0;

	private boolean regionActive = false;

	/**
	 * Constructor.
	 * 
	 * @param id
	 * @param parent
	 */
	MapRegion(Integer id, WorldMapInstance parent) {
		this.regionId = id;
		this.parent = parent;
		this.neighbours.add(this);
	}

	/**
	 * Return an instance of {@link World}, which keeps map, to which belongs
	 * this region
	 */
	public World getWorld() {
		return getParent().getWorld();
	}

	/**
	 * Returns region id of this map region. [NOT WORLD ID!]
	 * 
	 * @return region id.
	 */
	public Integer getRegionId() {
		return regionId;
	}

	/**
	 * Returns WorldMapInstance witch is parent of this instance
	 * 
	 * @return parent
	 */
	public WorldMapInstance getParent() {
		return parent;
	}

	/**
	 * Returns iterator over AionObjects on this region
	 * 
	 * @return objects iterator
	 */
	public Map<Long, VisibleObject> getObjects() {
		return objects;
	}

	/**
	 * @return the neighbours
	 */
	public List<MapRegion> getNeighbours() {
		return neighbours;
	}

	/**
	 * Add neighbour region to this region neighbours list.
	 * 
	 * @param neighbour
	 */
	void addNeighbourRegion(MapRegion neighbour) {
		neighbours.add(neighbour);
	}

	/**
	 * Add AionObject to this region objects list.
	 * 
	 * @param object
	 */
	void add(VisibleObject object) {
		if (objects.put(object.getObjectId(), object) == null) {
			if (object instanceof Player) {
				playerCount++;
				regionActive = playerCount > 0;
			}
		}
	}

	/**
	 * Remove AionObject from region objects list.
	 * 
	 * @param object
	 */
	void remove(VisibleObject object) {
		if (objects.remove(object.getObjectId()) != null)
			if (object instanceof Player) {
				playerCount--;
				regionActive = playerCount > 0;
			}
	}

	public boolean isMapRegionActive() {
		for (MapRegion r : neighbours) {
			if (r.regionActive)
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("REGIONID : [%d] PLAYERCOUNT : [%d]", regionId, playerCount);
	}
}
