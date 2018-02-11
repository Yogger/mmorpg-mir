package com.mmorpg.mir.model.controllers;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.world.World;

public abstract class VisibleObjectController<T extends VisibleObject> {

	/**
	 * Object that is controlled by this controller.
	 */
	private T owner;

	/**
	 * Set owner (controller object).
	 * 
	 * @param owner
	 */
	public void setOwner(T owner) {
		this.owner = owner;
	}

	/**
	 * Get owner (controller object).
	 */
	public T getOwner() {
		return owner;
	}

	/**
	 * Called when controlled object is seeing other VisibleObject.
	 * 
	 * @param object
	 */
	public void see(VisibleObject object) {

	}

	/**
	 * Called when controlled object no longer see some other VisibleObject.
	 * 
	 * @param object
	 */
	public void notSee(VisibleObject object, boolean isOutOfRange) {

	}

	/**
	 * Removes controlled object from the world.
	 */
	public void delete() {
		if (getOwner().isSpawned())
			World.getInstance().despawn(getOwner());
	}

	/**
	 * Called when object is re-spawned
	 */
	public void onRelive() {

	}

	public void onDie(Creature lastAttacker, int skillId) {
	}

	public void onDespawn() {

	}

	public void onSpawn(int mapId, int instanceId) {

	}
}
