package com.mmorpg.mir.model.object.creater;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ai.SkillSelector;
import com.mmorpg.mir.model.beauty.BeautyGirlConfig;
import com.mmorpg.mir.model.beauty.model.BeautyGirl;
import com.mmorpg.mir.model.beauty.resource.BeautyGirlResource;
import com.mmorpg.mir.model.controllers.SummonController;
import com.mmorpg.mir.model.controllers.effect.EffectController;
import com.mmorpg.mir.model.controllers.stats.SummonLifeStats;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Servant;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.gameobjects.stats.SummonGameStats;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.followpolicy.MasterFollowPolicy;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.object.resource.SkillSelectorItemSample;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.util.IdentifyManager.IdentifyType;
import com.mmorpg.mir.model.world.Position;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;
import com.mmorpg.mir.model.world.WorldPosition;

@Component
public class ServantCreater extends AbstractObjectCreater {

	private static Position[] AROUND_POSITION = new Position[] { new Position(-2, 2), new Position(-2, 1),
			new Position(-2, 0), new Position(-2, -1), new Position(-2, -2), new Position(-1, 2), new Position(-1, 1),
			new Position(-1, 0), new Position(-1, -1), new Position(-1, -2), new Position(0, 2), new Position(0, 1),
			new Position(0, -1), new Position(0, -2), new Position(1, 2), new Position(1, 1), new Position(1, 0),
			new Position(1, -1), new Position(1, -2), new Position(2, 2), new Position(2, 1), new Position(2, 0),
			new Position(2, -1), new Position(2, -2) };

	@Override
	public VisibleObject create(SpawnGroupResource spawnResource, ObjectResource resource, int instanceIndex,
			Object... args) {
		// int[] fxy = spawnResource.createXY();
		Player master = (Player) args[0];

		Position servantPosition = getCanUsePosition(this.world, master);
		WorldPosition position = world.createPosition(master.getMapId(), master.getInstanceId(),
				servantPosition.getX(), servantPosition.getY(), spawnResource.createHeading());
		Servant summon = new Servant(identifyManager.getNextIdentify(IdentifyType.MONSTER), new SummonController(),
				position, master);
		summon.setBornX(servantPosition.getX());
		summon.setBornY(servantPosition.getY());
		summon.setAi(spawnResource.getAiType().create());
		summon.setGameStats(new SummonGameStats(summon));
		summon.getAi().setOwner(summon);
		summon.getAi().setInterval(200);
		summon.setSpawnKey(spawnResource.getKey());
		summon.setObjectKey(resource.getKey());
		summon.setTemplateId(resource.getTemplateId());
		summon.setNpcSkillIds(resource.getSkills());
		setSkillSelector(master, summon);
		summon.setWarnrange(spawnResource.getWarnrange());
		summon.setHomeRange(spawnResource.getHomerange());

		if (spawnResource.hasRouteStep() && spawnResource.getRouteType() != null) {
			summon.setRouteRoad(spawnResource.getRouteType().createRoad((spawnResource.getRouteSteps())));
			summon.setFollowPolicy(new MasterFollowPolicy(summon));
		}

		// 设置属性
		setStats(summon, master);

		summon.setLifeStats(new SummonLifeStats(summon, summon.getGameStats().getCurrentStat(StatEnum.MAXHP), 0, false));

		summon.setEffectController(new EffectController(summon));

		return summon;
	}

	private void setSkillSelector(Player master, Summon summon) {
		BeautyGirlResource resource = BeautyGirlConfig.getInstance().beautyGirlStorage.getUnique(
				BeautyGirlResource.SPAWN_INDEX, summon.getSpawnKey());
		BeautyGirl girl = master.getBeautyGirlPool().getBeautyGirls().get(resource.getId());
		List<SkillSelectorItemSample> samples = new ArrayList<SkillSelectorItemSample>();

		for (Integer skillId : girl.getProactiveSkills().values()) {
			for (SkillSelectorItemSample s : resource.getSkillSelectorItemSamples()) {
				if (s.getSkillId() == skillId) {
					samples.add(s);
				}
			}
		}

		summon.setSkillSelector(SkillSelector.valueOf(summon, samples.toArray(new SkillSelectorItemSample[0])));
	}

	public static Position getCanUsePosition(World world, Player master) {
		int masterX = master.getX();
		int masterY = master.getY();
		WorldMap map = world.getWorldMap(master.getMapId());
		for (Position p : AROUND_POSITION) {
			int newX = masterX + p.getX();
			int newY = masterY + p.getY();
			if (map.isOut(newX, newY) || map.isBlock(newX, newY)) {
				continue;
			}
			return new Position(newX, newY);
		}
		return new Position(masterX, masterY);
	}

	private void setStats(Creature creature, Player master) {
		creature.getGameStats().clear();
		BeautyGirlResource resource = BeautyGirlConfig.getInstance().beautyGirlStorage.getUnique(
				BeautyGirlResource.SPAWN_INDEX, creature.getSpawnKey());
		BeautyGirl girl = master.getBeautyGirlPool().getBeautyGirls().get(resource.getId());
		List<Stat> stats = Arrays.asList(resource.getBeautyStats()[girl.getLevel()]);
		if (!stats.isEmpty()) {
			creature.getGameStats().addModifiers(StatEffectId.valueOf("level_base", StatEffectType.LEVEL_BASE), stats);
			creature.setLevel(girl.getLevel());
		} else {
			throw new RuntimeException(String.format("美人[%s] 等级[%d]属性为空", girl.getId(), girl.getLevel()));
		}

	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.SERVANT;
	}

}
