package com.mmorpg.mir.model.world;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Position of object in the world.
 * 
 * @author -Nemesiss-
 * 
 */
public class WorldPosition {

	/**
	 * Map id.
	 */
	private int mapId;
	/**
	 * Map Region.
	 */
	private MapRegion mapRegion;
	/**
	 * World position x
	 */
	private int x;
	/**
	 * World position y
	 */
	private int y;

	/**
	 * Value from 0 to 120 (120==0 actually)
	 */
	private byte heading;
	/**
	 * indicating if object is spawned or not.
	 */
	private AtomicBoolean isSpawned = new AtomicBoolean(false);

	/**
	 * Return World map id.
	 * 
	 * @return world map id
	 */
	public int getMapId() {
		return mapId;
	}

	/**
	 * @param mapId
	 *            the mapId to set
	 */
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	/**
	 * Return World position x
	 * 
	 * @return x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Return World position y
	 * 
	 * @return y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Return map region
	 * 
	 * @return Map region
	 */
	public MapRegion getMapRegion() {
		return isSpawned.get() ? mapRegion : null;
	}

	/**
	 * 
	 * @return
	 */
	public int getInstanceId() {
		return mapRegion.getParent().getInstanceId();
	}

	/**
	 * Return heading.
	 * 
	 * @return heading
	 */
	public byte getHeading() {
		return heading;
	}

	/**
	 * Returns the {@link World} instance in which this position is located. :D
	 * 
	 * @return World
	 */
	public World getWorld() {
		return mapRegion.getWorld();
	}

	/**
	 * Check if object is spawned.
	 * 
	 * @return true if object is spawned.
	 */
	public boolean isSpawned() {
		return isSpawned.get();
	}

	/**
	 * Set isSpawned to given value.
	 * 
	 * @param val
	 */
	void setIsSpawned(boolean val) {
		isSpawned.set(val);
	}

	boolean compareAndSet(boolean expect, boolean update) {
		return isSpawned.compareAndSet(expect, update);
	}

	/**
	 * Set map region
	 * 
	 * @param r
	 *            - map region
	 */
	void setMapRegion(MapRegion r) {
		mapRegion = r;
	}

	/**
	 * Set world position.
	 * 
	 * @param newX
	 * @param newY
	 * @param newHeading
	 *            Value from 0 to 120 (120==0 actually)
	 */
	void setXYH(int newX, int newY, byte newHeading) {
		x = newX;
		y = newY;
		setHeading(newHeading);
	}

	public void setXY(WorldPosition position) {
		x = position.getX();
		y = position.getY();
	}

	@Override
	public String toString() {
		return "WorldPosition [heading=" + getHeading() + ", isSpawned=" + isSpawned + ", mapRegion=" + mapRegion
				+ ", x=" + x + ", y=" + y + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof WorldPosition))
			return false;

		WorldPosition pos = (WorldPosition) o;
		return this.x == pos.x && this.y == pos.y && this.isSpawned == pos.isSpawned
				&& this.getHeading() == pos.getHeading() && this.mapRegion == pos.mapRegion;
	}

	@Override
	public WorldPosition clone() {
		WorldPosition pos = new WorldPosition();
		pos.setHeading(this.getHeading());
		pos.isSpawned = this.isSpawned;
		pos.mapRegion = this.mapRegion;
		pos.mapId = this.mapId;
		pos.x = this.x;
		pos.y = this.y;
		return pos;
	}

	public void setHeading(byte heading) {
		this.heading = heading;
	}

}
