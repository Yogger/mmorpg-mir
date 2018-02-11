package com.mmorpg.mir.model.gameobjects;

import com.mmorpg.mir.model.controllers.CountryNpcController;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.world.WorldPosition;
import com.mmorpg.mir.model.world.resource.MapCountry;

public class CountryNpc extends Monster {

	private volatile boolean god = false;

	public CountryNpc(long objId, CountryNpcController controller, WorldPosition position, int country) {
		super(objId, controller, position);
		setCountry(MapCountry.valueOf(country));
	}

	@Override
	public boolean isPlayerEnemy(Player player) {
		if (getCountry().getValue() == player.getCountryId().getValue()) {
			return false;
		}
		// TODO:特殊活动地图 可能与国家无关
		return super.isPlayerEnemy(player);
	}

	@Override
	protected boolean isMonsterEnemy(Monster monster) {
		return false;
	}

	public boolean isAtSpawnLocation() {
		if (getX() == getBornX() && getY() == getBornY()) {
			// if (getHeading() != getBornHeading()) {
			// this.set
			// }
			return true;
		}
		return false;
	}

	@Override
	protected boolean isCountryNpcEnemy(CountryNpc countryNpc) {
		return getCountry() != countryNpc.getCountry();
	}

	@Override
	protected boolean isLorryEnemy(Lorry lorry) {
		return getCountry() != lorry.getCountry();
	}

	@Override
	protected boolean isBossEnemy(Boss boss) {
		return getCountry() != boss.getCountry();
	}

	@Override
	protected boolean canSeeLorry(VisibleObject visibleObject) {
		return true;
	}

	@Override
	protected boolean canSeeCountryNpc(VisibleObject visibleObject) {
		return true;
	}

	@Override
	protected boolean canSeeBoss(VisibleObject visibleObject) {
		return true;
	}

	@Override
	protected boolean canSeeSummon(VisibleObject visibleObject) {
		return true;
	}

	@Override
	protected boolean canSeeBigBrother(VisibleObject visibleObject) {
		return true;
	}

	@Override
	protected boolean canSeeRobot(VisibleObject visibleObject) {
		return true;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.COUNTRY_NPC;
	}

	@Override
	public int getCountryValue() {
		return getCountry().getValue();
	}

	public boolean isGod() {
		return god;
	}

	public void setGod(boolean god) {
		this.god = god;
	}
}
