package com.mmorpg.mir.model.knownlist;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.cliffc.high_scale_lib.NonBlockingHashMapLong;

import com.mmorpg.mir.model.controllers.DropObjectController;
import com.mmorpg.mir.model.gameobjects.DropObject;
import com.mmorpg.mir.model.gameobjects.JourObject;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.StaticObject;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.world.MapRegion;

public class KnownList implements Iterable<VisibleObject> {

	private static final Logger logger = Logger.getLogger(KnownList.class);

	protected final VisibleObject owner;

	protected final NonBlockingHashMapLong<VisibleObject> knownObjects = new NonBlockingHashMapLong<VisibleObject>();

	private long lastUpdate;

	private static final int VisRowDis = 40;
	private static final int VisColDis = 20;

	public KnownList(VisibleObject owner) {
		this.owner = owner;
	}

	public void doUpdate() {
		doUpdate(true);
	}

	public void doUpdate(boolean force) {
		if (!force) {
			if ((System.currentTimeMillis() - lastUpdate) < 1000)
				return;
		}

		forgetObjects();
		findVisibleObjects();

		lastUpdate = System.currentTimeMillis();
	}

	public void clear() {
		Iterator<VisibleObject> knownIt = iterator();
		while (knownIt.hasNext()) {
			VisibleObject obj = knownIt.next();
			knownIt.remove();
			owner.getController().notSee(obj, false);
			obj.getKnownList().del(owner, false);
		}
	}

	public boolean knowns(JourObject object) {
		return knownObjects.containsKey(object.getObjectId());
	}

	public boolean knownObjectType(ObjectType objectType) {
		Iterator<VisibleObject> knownIt = iterator();
		while (knownIt.hasNext()) {
			VisibleObject obj = knownIt.next();
			if (obj.getObjectType() == objectType) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterator<VisibleObject> iterator() {
		return knownObjects.values().iterator();
	}

	public int getVisibleSize() {
		return knownObjects.size();
	}

	/**
	 * Add VisibleObject to this KnownList.
	 * 
	 * @param object
	 */
	protected void add(VisibleObject object) {
		/**
		 * object is not known.
		 */
		if (knownObjects.put(object.getObjectId(), object) == null)
			owner.getController().see(object);
	}

	/**
	 * Delete VisibleObject from this KnownList.
	 * 
	 * @param object
	 */
	private void del(VisibleObject object, boolean isOutOfRange) {
		/**
		 * object was known.
		 */
		if (knownObjects.remove(object.getObjectId()) != null)
			owner.getController().notSee(object, isOutOfRange);
	}

	/**
	 * forget out of distance objects.
	 */
	private void forgetObjects() {
		Iterator<VisibleObject> knownIt = iterator();

		while (knownIt.hasNext()) {
			VisibleObject obj = knownIt.next();

			if (!checkObjectInRange(owner, obj)) {
				knownIt.remove();
				owner.getController().notSee(obj, true);
				obj.getKnownList().del(owner, true);
			}
		}
	}

	/**
	 * Find objects that are in visibility range.
	 */
	protected void findVisibleObjects() {
		if (owner == null || !owner.isSpawned())
			return;

		List<MapRegion> list = owner.getActiveRegion().getNeighbours();
		for (MapRegion r : list) {
			Map<Long, VisibleObject> objects = r.getObjects();
			for (Entry<Long, VisibleObject> e : objects.entrySet()) {
				VisibleObject newObject = e.getValue();
				if (newObject == owner || newObject == null)
					continue;

				if (!check(owner, newObject))
					continue;

				if (!checkObjectInRange(owner, newObject))
					continue;

				add(newObject);
				newObject.getKnownList().add(owner);
			}
		}
	}

	// FIX 这段代码比较特别
	private boolean check(VisibleObject owner, VisibleObject newObject) {
		boolean see1 = owner.canSee(newObject);
		boolean see2 = newObject.canSee(owner);
		if (see1 != see2) {
			logger.error(String.format("obj1 [%s] obj2 [%s] can see method is not equal error", owner, newObject));
		}
		return see1 && see2;
	}

	public boolean isActive() {
		for (VisibleObject visibleObject : this) {
			if (visibleObject instanceof Player)
				return true;
		}
		return false;
	}

	protected boolean checkObjectInRange(VisibleObject owner, VisibleObject newObject) {
		return MathUtil.isInRange(owner, newObject, VisRowDis, VisColDis);
	}

	public static void main(String[] args) {
		DropObject dobj = new DropObject(0, new DropObjectController(), null);
		System.out.println(dobj instanceof StaticObject);
	}
}
