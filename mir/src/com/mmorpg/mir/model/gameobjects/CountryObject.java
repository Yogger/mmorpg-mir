package com.mmorpg.mir.model.gameobjects;

import com.mmorpg.mir.model.controllers.NpcController;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.world.WorldPosition;
import com.mmorpg.mir.model.world.resource.MapCountry;

public class CountryObject extends Npc {

	public CountryObject(long objId, NpcController controller, WorldPosition position, int country) {
		super(objId, controller, position);
		this.setCountry(MapCountry.valueOf(country));
	}

	@Override
	public int getCountryValue() {
		return getCountry().getValue();
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.COUNTRY_OBJECT;
	}

	@Override
	public String toString() {
		return String.format("COUNTRYNPC ID:[%d] NAME:[%s] X:[%d] Y:[%d]", objectId, getName(), getX(), getY());
	}
}
