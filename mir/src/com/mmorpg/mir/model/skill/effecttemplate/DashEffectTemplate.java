package com.mmorpg.mir.model.skill.effecttemplate;

import org.apache.commons.lang.ArrayUtils;

import com.mmorpg.mir.model.controllers.move.Road;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.packet.SM_DashSkill;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.DirectionEnum;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;
import com.mmorpg.mir.model.world.WorldMapInstance;

public class DashEffectTemplate extends EffectTemplate {

	@Override
	public void init(EffectTemplateResource resource) {
		super.init(resource);
	}

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
	}

	@Override
	public void applyEffect(Effect effect) {
		int mapId = effect.getEffector().getMapId();
		int x = effect.getEffector().getX();
		int y = effect.getEffector().getY();
		if (effect.getEffector().getPosition() == null || effect.getEffector().getPosition().getMapRegion() == null) {
			return;
		}
		WorldMapInstance mapInstance = effect.getEffector().getPosition().getMapRegion().getParent();
		// 判断坐标点是否合法
		Road road = MathUtil.SmoothFindRoad(mapId, x, y, effect.getEffected().getX(), effect.getEffected().getY());

		if (road == null) {
			road = MathUtil.findRoad(effect.getEffector().getMapId(), effect.getEffector().getX(), effect.getEffector()
					.getY(), effect.getEffected().getX(), effect.getEffected().getY());
		}

		if (road != null) {
			// 验证动态城墙
			WorldMap worldMap = World.getInstance().getWorldMap(mapId);
			int dx = x;
			int dy = y;
			if (worldMap.isOut(x, y) || worldMap.isBlock(x, y) || mapInstance.isBlock(x, y)) {
				return;
			}
			if (!ArrayUtils.isEmpty(road.getLeftRoads())) {
				for (byte r : road.getLeftRoads()) {
					DirectionEnum direction = DirectionEnum.values()[r];
					dx += direction.getAddX();
					dy += direction.getAddY();
					if (worldMap.isOut(dx, dy) || worldMap.isBlock(dx, dy) || mapInstance.isBlock(dx, dy)) {
						return;
					}
				}
			}
		}

		if (road != null && road.getLeftRoads().length < (effect.getRange() * 2)) {
			int step = road.getRoads().length;
			int i = step;
			for (byte r : road.getRoads()) {
				if (i == 1) {
					break;
				}
				DirectionEnum de = DirectionEnum.indexOrdinal((int) r);
				x += de.getAddX();
				y += de.getAddY();
				i--;
			}

			World.getInstance().updatePosition(effect.getEffector(), x, y, road.getRoads()[step - 1]);

			PacketSendUtility.broadcastPacketAndReceiver(effect.getEffector(), SM_DashSkill.valueOf(
					effect.getSkillId(), effect.getEffector().getX(), effect.getEffector().getY(), effect.getEffector()
							.getObjectId()));
		}
	}
}
