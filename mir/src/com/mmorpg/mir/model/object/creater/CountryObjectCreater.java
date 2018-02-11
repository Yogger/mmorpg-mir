package com.mmorpg.mir.model.object.creater;
//package com.jour.gameserver.model.object.creater;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.springframework.stereotype.Component;
//
//import com.jour.gameserver.model.controllers.NpcController;
//import com.jour.gameserver.model.controllers.effect.EffectController;
//import com.jour.gameserver.model.controllers.stats.CountryObjectLifeStats;
//import com.jour.gameserver.model.country.manager.CountryManager;
//import com.jour.gameserver.model.country.model.Country;
//import com.jour.gameserver.model.country.model.CountryFlag;
//import com.jour.gameserver.model.country.model.CountryId;
//import com.jour.gameserver.model.country.model.Technology;
//import com.jour.gameserver.model.gameobjects.CountryObject;
//import com.jour.gameserver.model.gameobjects.Monster;
//import com.jour.gameserver.model.gameobjects.VisibleObject;
//import com.jour.gameserver.model.gameobjects.stats.CountryObjectGameStats;
//import com.jour.gameserver.model.gameobjects.stats.Stat;
//import com.jour.gameserver.model.gameobjects.stats.StatEffectId;
//import com.jour.gameserver.model.gameobjects.stats.StatEffectType;
//import com.jour.gameserver.model.gameobjects.stats.StatEnum;
//import com.jour.gameserver.model.object.ObjectType;
//import com.jour.gameserver.model.object.resource.ObjectResource;
//import com.jour.gameserver.model.spawn.resource.SpawnGroupResource;
//import com.jour.gameserver.model.util.IdentifyManager.IdentifyType;
//
//@Component
//public class CountryObjectCreater extends AbstractObjectCreater {
//
//	@Override
//	public VisibleObject create(SpawnGroupResource spawnResource, ObjectResource resource, int instanceIndex,
//			Object... args) {
//		CountryObject countryObject = new CountryObject(identifyManager.getNextIdentify(IdentifyType.COUNTRY_OBJECT),
//				new NpcController(), world.createPosition(spawnResource.getMapId(), instanceIndex,
//						spawnResource.getX(), spawnResource.getY(), spawnResource.createHeading()),
//				spawnResource.getCountry());
//		countryObject.setBornX(spawnResource.getX());
//		countryObject.setBornY(spawnResource.getY());
//		countryObject.setAi(spawnResource.getAiType().create());
//		countryObject.setGameStats(new CountryObjectGameStats(countryObject));
//		countryObject.getAi().setOwner(countryObject);
//		countryObject.setSpawnKey(spawnResource.getKey());
//		countryObject.setObjectKey(resource.getKey());
//
//		Country country = CountryManager.getInstance().getCountries()
//				.get(CountryId.valueOf(spawnResource.getCountry()));
//		Stat[] resourceStats = null;
//		if (spawnResource.getKey().equals(CountryFlag.FLAG_OBJECTID)) {
//			// 国旗
//			resourceStats = country.getCountryFlag().getResource().getStats();
//		} else if (spawnResource.getKey().equals(Technology.DOOR_OBJECTID)) {
//			// 城门
//			resourceStats = country.getCountryFlag().getResource().getStats();
//		}
//		if (resourceStats == null) {
//			throw new RuntimeException("[" + spawnResource.getKey() + "]国家物件没有发现属性");
//		}
//		List<Stat> stats = Arrays.asList(resourceStats);
//		if (!stats.isEmpty()) {
//			countryObject.getGameStats().addModifiers(StatEffectId.valueOf("level_base", StatEffectType.LEVEL_BASE),
//					stats);
//		}
//
//		countryObject.setLifeStats(new CountryObjectLifeStats(countryObject, countryObject.getGameStats()
//				.getCurrentStat(StatEnum.MAXHP), countryObject.getGameStats().getCurrentStat(StatEnum.MAXMP), false));
//
//		countryObject.setEffectController(new EffectController(countryObject));
//
//		return countryObject;
//	}
//
//	@Override
//	public ObjectType getObjectType() {
//		return ObjectType.COUNTRY_OBJECT;
//	}
//
//	@Override
//	public void relive(ObjectResource resource, VisibleObject object, Object... args) {
//		Monster countryObject = (Monster) object;
//
//		countryObject.getLifeStats().setCurrentHpPercent(100);
//
//	}
//
//}
