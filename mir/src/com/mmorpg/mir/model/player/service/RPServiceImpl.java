package com.mmorpg.mir.model.player.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.player.manager.RPManager;
import com.mmorpg.mir.model.player.resource.RPResource;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.resource.MapCountry;
import com.windforce.common.resource.anno.Static;

@Component
public class RPServiceImpl implements RPService {

	@Autowired
	private RPManager rpManager;

	@Static("RP:NEUTRALRATE")
	private ConfigValue<Integer> NEUTRALRATE;

	@Static("RP:LOCALCOUNTRY")
	private ConfigValue<Integer> LOCALCOUNTRY;

	@Static("RP:HOSTILE")
	private ConfigValue<Integer> HOSTILE;

	private static RPService instance;

	@PostConstruct
	public void init() {
		instance = this;
	}

	public static RPService getInstance() {
		return instance;
	}

	public void killAddRp(Player player, Player killed) {
		int ret = 0;
		RPResource selfResource = rpManager.getResource(player.getRp().getRpType());
		RPResource killedResource = rpManager.getResource(killed.getRp().getRpType());

		CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(killed.getLevel(),
				selfResource.getAcquireConditionId());
		Map<String, Player> context = new HashMap<String, Player>();
		context.put(TriggerContextKey.PLAYER, player);
		context.put(TriggerContextKey.OTHER_PLAYER, killed);
		if (conditions.verify(context)) {
			return;
		}

		MapCountry locationCountry = World.getInstance().getWorldMap(player.getMapId()).getCountry();
		double countryRate;

		if (MapCountry.NEUTRAL.getValue() == locationCountry.getValue()) {
			countryRate = NEUTRALRATE.getValue() * 100.0 / 10000;
		} else if (player.getCountryId().getValue() == locationCountry.getValue()) {
			countryRate = LOCALCOUNTRY.getValue() * 100.0 / 10000;
		} else {
			countryRate = HOSTILE.getValue() * 100.0 / 10000;
		}

		// 击杀获得的人品值 = 人品值奖励 * 人品值增益加成 * 地域的加成
		ret = (int) (Math.ceil(killedResource.getOfferRP()
				* (1 + player.getGameStats().getCurrentStat(StatEnum.RP_PLUS)) * countryRate));

		player.getRp().increaseRP(ret);
	}

}
