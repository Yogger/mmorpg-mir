package com.mmorpg.mir.model.skill.effecttemplate;

import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.packet.SM_DashSkill;
import com.mmorpg.mir.model.skill.resource.EffectTemplateResource;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;
import com.mmorpg.mir.model.world.WorldMapInstance;

public class WarpEffectTemplate extends EffectTemplate {

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
		int x = effect.getX();
		int y = effect.getY();
		if (effect.getEffector().getPosition() == null || effect.getEffector().getPosition().getMapRegion() == null) {
			return;
		}
		boolean isInRange = MathUtil.isInRange(effect.getEffector().getX(), effect.getEffector().getY(), x, y,
				effect.getRange(), effect.getRange());
		if (!isInRange) {
			return;
		}

		WorldMapInstance mapInstance = effect.getEffector().getPosition().getMapRegion().getParent();
		int mapId = effect.getEffector().getMapId();
		// 验证动态城墙
		WorldMap worldMap = World.getInstance().getWorldMap(mapId);
		if (worldMap.isOut(x, y) || worldMap.isBlock(x, y) || mapInstance.isBlock(x, y)) {
			return;
		}

		World.getInstance().updatePosition(effect.getEffector(), x, y, effect.getEffector().getHeading());

		PacketSendUtility.broadcastPacketAndReceiver(effect.getEffector(), SM_DashSkill.valueOf(effect.getSkillId(),
				effect.getEffector().getX(), effect.getEffector().getY(), effect.getEffector().getObjectId()));
	}
}
