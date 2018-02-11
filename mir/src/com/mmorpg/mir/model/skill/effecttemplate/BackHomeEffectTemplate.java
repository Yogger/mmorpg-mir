package com.mmorpg.mir.model.skill.effecttemplate;

import java.util.List;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.world.World;
import com.windforce.common.utility.JsonUtils;

public class BackHomeEffectTemplate extends EffectTemplate {

	@Override
	public void applyEffect(Effect effect) {
		Player player = null;
		if (effect.getEffector() instanceof Player) {
			player = (Player) effect.getEffector();
		}
		if (player == null) {
			return;
		}
		if (player.isInCopy()) {
			throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
		}
		List<String> result = ChooserManager.getInstance().chooseValueByRequire(player.getCountryId().getValue(),
				PlayerManager.getInstance().BACKHOME_POINT.getValue());
		RelivePosition p = JsonUtils.string2Object(result.get(0), RelivePosition.class);
		if (player.getPosition().getMapId() == p.getMapId()) {
			World.getInstance().updatePosition(player, p.getX(), p.getY(), player.getHeading());
		} else {
			World.getInstance().setPosition(player, p.getMapId(), p.getX(), p.getY(), player.getHeading());
		}
		player.sendUpdatePosition();
	}

	@Override
	public void calculate(Effect effect) {
		effect.addSucessEffect(this);
	}

}
