package com.mmorpg.mir.model.object.creater;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ai.SkillSelector;
import com.mmorpg.mir.model.controllers.MonsterController;
import com.mmorpg.mir.model.controllers.effect.EffectController;
import com.mmorpg.mir.model.controllers.stats.MonsterLifeStats;
import com.mmorpg.mir.model.gameobjects.Monster;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.TownPlayerNpc;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.stats.MonsterGameStats;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.followpolicy.RouteFollowPolicy;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.player.packet.SM_PlayerInfo;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.util.IdentifyManager.IdentifyType;

@Component
public final class TownPlayerNpcCreater extends AbstractObjectCreater {

	@Override
	public VisibleObject create(SpawnGroupResource spawnResource, ObjectResource resource, int instanceIndex,
			Object... args) {
		Player player = (Player) args[0];
		double statsRate = ((Integer) args[1]) * 1.0 / 10000;
		TownPlayerNpc townPlayerNpc = new TownPlayerNpc(identifyManager.getNextIdentify(IdentifyType.MONSTER),
				new MonsterController(), world.createPosition(spawnResource.getMapId(), instanceIndex,
						spawnResource.getX(), spawnResource.getY(), spawnResource.createHeading()));
		townPlayerNpc.setBornX(spawnResource.getX());
		townPlayerNpc.setBornY(spawnResource.getY());
		townPlayerNpc.setAi(spawnResource.getAiType().create());
		townPlayerNpc.getAi().setInterval(spawnResource.getAiInterval());

		// 设置怪物的一些属性
		townPlayerNpc.setGameStats(new MonsterGameStats(townPlayerNpc));
		townPlayerNpc.getAi().setOwner(townPlayerNpc);
		townPlayerNpc.setSpawnKey(spawnResource.getKey());
		townPlayerNpc.setObjectKey(resource.getKey());
		townPlayerNpc.setTemplateId(resource.getTemplateId());

		townPlayerNpc.setSkillSelector(SkillSelector.valueOf(townPlayerNpc, resource.getSkillSelectorItemSamples()));
		townPlayerNpc.setWarnrange(spawnResource.getWarnrange());
		townPlayerNpc.setHomeRange(spawnResource.getHomerange());
		townPlayerNpc.setBornHeading(townPlayerNpc.getHeading());
		townPlayerNpc.setExpReduction(resource.isExpReduction());

		if (spawnResource.hasRouteStep() && spawnResource.getRouteType() != null) {
			townPlayerNpc.setRouteRoad(spawnResource.getRouteType().createRoad((spawnResource.getRouteSteps())));
			townPlayerNpc.setFollowPolicy(new RouteFollowPolicy(townPlayerNpc));
		}

		// 设置属性
		List<Stat> playerStats = player.getGameStats().getAllStat(statsRate);
		townPlayerNpc.getGameStats().addModifiers(StatEffectId.valueOf("level_base", StatEffectType.LEVEL_BASE),
				playerStats, false);
		townPlayerNpc.getGameStats().recomputeStats();

		townPlayerNpc.setLifeStats(new MonsterLifeStats(townPlayerNpc, townPlayerNpc.getGameStats().getCurrentStat(
				StatEnum.MAXHP), townPlayerNpc.getGameStats().getCurrentStat(StatEnum.MAXMP), false));

		townPlayerNpc.setLevel(player.getLevel());
		townPlayerNpc.setEffectController(new EffectController(townPlayerNpc));
		townPlayerNpc.setPlayerInfo(SM_PlayerInfo.valueOf(player));

		return townPlayerNpc;
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.TOWN_PLAYER_NPC;
	}

	@Override
	public void relive(ObjectResource resource, VisibleObject object, Object... args) {
		Monster townPlayerNpc = (Monster) object;
		// 设置属性
		setStats(townPlayerNpc, resource);

		townPlayerNpc.getLifeStats().setCurrentHpPercent(100);

		// 这里需要重置一下路线
		if (townPlayerNpc.hasRouteStep())
			townPlayerNpc.getRouteRoad().reset();

	}

}
