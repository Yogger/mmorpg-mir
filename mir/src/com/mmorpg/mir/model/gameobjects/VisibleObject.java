/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mmorpg.mir.model.gameobjects;

import com.mmorpg.mir.model.controllers.VisibleObjectController;
import com.mmorpg.mir.model.knownlist.KnownList;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.world.MapRegion;
import com.mmorpg.mir.model.world.WorldPosition;

public abstract class VisibleObject extends JourObject {

	/** 观察者列表 */
	private KnownList knownlist;
	/** 位置 */
	private WorldPosition position;
	/** 　控制器 */
	private VisibleObjectController<? extends VisibleObject> controller;

	/**
	 * Visible object's target
	 */
	private VisibleObject target;

	private String spawnKey;

	private String objectKey;

	private short templateId;

	private int bornX;
	private int bornY;
	private byte bornHeading;

	/**
	 * Constructor.
	 * 
	 * @param objId
	 * @param objectTemplate
	 */
	public VisibleObject(long objId, VisibleObjectController<? extends VisibleObject> controller, WorldPosition position) {
		this.objectId = objId;
		this.setController(controller);
		this.position = position;
		this.knownlist = new KnownList(this);
	}

	/**
	 * Returns current WorldRegion AionObject is in.
	 * 
	 * @return mapRegion
	 */
	public MapRegion getActiveRegion() {
		return position.getMapRegion();
	}

	public int getInstanceId() {
		return position.getInstanceId();
	}

	/**
	 * Return World map id.
	 * 
	 * @return world map id
	 */
	public int getMapId() {
		return position.getMapId();
	}

	/**
	 * Return World position x
	 * 
	 * @return x
	 */
	public int getX() {
		return position.getX();
	}

	/**
	 * Return World position y
	 * 
	 * @return y
	 */
	public int getY() {
		return position.getY();
	}

	/**
	 * Heading of the object. Values from <0,120)
	 * 
	 * @return heading
	 */
	public byte getHeading() {
		return position.getHeading();
	}

	public void setHeading(byte heading) {
		position.setHeading(heading);
	}

	/**
	 * Return object position
	 * 
	 * @return position.
	 */
	public WorldPosition getPosition() {
		return position;
	}

	/**
	 * Check if object is spawned.
	 * 
	 * @return true if object is spawned.
	 */
	public boolean isSpawned() {
		return position.isSpawned();
	}

	public void clearKnownlist() {
		getKnownList().clear();
	}

	public void updateKnownlist() {
		getKnownList().doUpdate();
	}

	/**
	 * Set KnownList to this VisibleObject
	 * 
	 * @param knownlist
	 */
	public void setKnownlist(KnownList knownlist) {
		this.knownlist = knownlist;
	}

	/**
	 * Returns KnownList of this VisibleObject.
	 * 
	 * @return knownList.
	 */
	public KnownList getKnownList() {
		return knownlist;
	}

	/**
	 * Return VisibleObjectController of this VisibleObject
	 * 
	 * @return VisibleObjectController.
	 */
	public VisibleObjectController<? extends VisibleObject> getController() {
		return controller;
	}

	/**
	 * 
	 * @return VisibleObject
	 */
	public VisibleObject getTarget() {
		return target;
	}

	/**
	 * 
	 * @param creature
	 */
	public void setTarget(VisibleObject creature) {
		target = creature;
	}

	public String getSpawnKey() {
		return spawnKey;
	}

	public void setSpawnKey(String spawnKey) {
		this.spawnKey = spawnKey;
	}

	public String getObjectKey() {
		return objectKey;
	}

	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}

	public SpawnGroupResource getSpawn() {
		SpawnGroupResource result = null;
		if (spawnKey != null) {
			result = SpawnManager.getInstance().getSpawn(spawnKey);
		}
		return result;
	}

	/**
	 * 
	 * @param objectId
	 * @return target is object with id equal to objectId
	 */
	public boolean isTargeting(int objectId) {
		return target != null && target.getObjectId().longValue() == objectId;
	}

	public int getBornX() {
		return bornX;
	}

	public void setBornX(int bornX) {
		this.bornX = bornX;
	}

	public int getBornY() {
		return bornY;
	}

	public void setBornY(int bornY) {
		this.bornY = bornY;
	}

	/**
	 * 这是一个特殊的方法，如果这个方法想要有效必须保证一下语义 有两个对象 obj1 obje2 obj1.cansee(obj2) ==
	 * obj2.cansee(obj1) 否则，将会发生一些不可预知的显示错误
	 * 
	 * @param visibleObject
	 * @return
	 */
	public boolean canSee(VisibleObject visibleObject) {
		switch (visibleObject.getObjectType()) {
		case BOSS:
			return canSeeBoss(visibleObject);
		case COUNTRY_NPC:
			return canSeeCountryNpc(visibleObject);
		case COUNTRY_OBJECT:
			return canSeeCountryObject(visibleObject);
		case DROPOBJECT:
			return canSeeDropObject(visibleObject);
		case GATHERABLE:
			return canSeeGatherable(visibleObject);
		case SCULPTURE:
			return canSeeSculpture(visibleObject);
		case LORRY:
			return canSeeLorry(visibleObject);
		case MONSTER:
			return canSeeMonster(visibleObject);
		case NPC:
			return canSeeNPC(visibleObject);
		case PLAYER:
			return canSeePlayer(visibleObject);
		case ROBOT:
			return canSeeRobot(visibleObject);
		case STATICOBJECT:
			return canSeeStaticObject(visibleObject);
		case STATUS_NPC:
			return canSeeStatusNpc(visibleObject);
		case SUMMON:
			return canSeeSummon(visibleObject);
		case BIGBROTHER:
			return canSeeBigBrother(visibleObject);
		case SUPERVISOR:
			return canSeeSupervisor(visibleObject);
		case TOWN_PLAYER_NPC:
			return canSeeTownPlayerNpc(visibleObject);
		case SERVANT:
			return canSeeServant(visibleObject);
		default:
			return false;
		}
	}

	protected boolean canSeeServant(VisibleObject visibleObject) {
		return false;
	}

	protected boolean canSeeBigBrother(VisibleObject visibleObject) {
		return false;
	}

	protected boolean canSeeSummon(VisibleObject visibleObject) {
		return false;
	}

	protected boolean canSeeSculpture(VisibleObject visibleObject) {
		return false;
	}

	protected boolean canSeeStatusNpc(VisibleObject visibleObject) {
		return false;
	}

	protected boolean canSeeStaticObject(VisibleObject visibleObject) {
		return false;
	}

	protected boolean canSeeRobot(VisibleObject visibleObject) {
		return false;
	}

	protected boolean canSeePlayer(VisibleObject visibleObject) {
		return true;
	}

	protected boolean canSeeNPC(VisibleObject visibleObject) {
		return false;
	}

	protected boolean canSeeMonster(VisibleObject visibleObject) {
		return false;
	}

	protected boolean canSeeLorry(VisibleObject visibleObject) {
		return false;
	}

	protected boolean canSeeGatherable(VisibleObject visibleObject) {
		return false;
	}

	protected boolean canSeeDropObject(VisibleObject visibleObject) {
		return false;
	}

	protected boolean canSeeCountryObject(VisibleObject visibleObject) {
		return false;
	}

	protected boolean canSeeCountryNpc(VisibleObject visibleObject) {
		return false;
	}

	protected boolean canSeeSupervisor(VisibleObject visibleObject) {
		return false;
	}

	protected boolean canSeeBoss(VisibleObject visibleObject) {
		return false;
	}

	protected boolean canSeeTownPlayerNpc(VisibleObject visibleObject) {
		return false;
	}

	public abstract ObjectType getObjectType();

	public boolean isObjectType(ObjectType objectType) {
		return getObjectType().equals(objectType);
	}

	public byte getBornHeading() {
		return bornHeading;
	}

	public void setBornHeading(byte bornHeading) {
		this.bornHeading = bornHeading;
	}

	public void setController(VisibleObjectController<? extends VisibleObject> controller) {
		this.controller = controller;
	}

	public void setPosition(WorldPosition position) {
		this.position = position;
	}

	public short getTemplateId() {
		return templateId;
	}

	public void setTemplateId(short templateId) {
		this.templateId = templateId;
	}

	public String toString() {
		return String.format("[%s] [%s] [%d] [%d] objectKey[%s] spawnKey[%s]", getObjectType(), getName(), getX(),
				getY(), getObjectKey(), getSpawnKey());
	}
}