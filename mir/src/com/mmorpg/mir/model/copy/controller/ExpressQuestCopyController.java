package com.mmorpg.mir.model.copy.controller;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.express.manager.ExpressManager;
import com.mmorpg.mir.model.gameobjects.CountryNpc;
import com.mmorpg.mir.model.gameobjects.Lorry;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.object.followpolicy.ForeverFollowPolicy;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.resource.MapCountry;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.JsonUtils;

@Component
public class ExpressQuestCopyController extends AbstractCopyController {

	public static final String ID = "lorrytaskcopy1";

	public ExpressQuestCopyController() {
	}

	public ExpressQuestCopyController(Player owner, WorldMapInstance worldMapInstance, CopyResource resource) {
		super(owner, worldMapInstance, resource);
	}

	@Override
	public void startCopy() {

		ExpressManager.getInstance().expressCopy(getOwner(), "46");

		int country = getOwner().getCountryValue();
		int country1 = country - 1 < 1 ? 3 : country - 1;
		int country2 = country + 1 > 3 ? 1 : country + 1;

		final Lorry lorry = (Lorry) (getWorldMapInstance().findObjectBySpawnId("EXPRESS_COPY").get(0));

		// 设置敌我识别
		final CountryNpc enemyPlayer1 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("lorrycopyspawn01")
				.get(0));
		enemyPlayer1.setCountry(MapCountry.valueOf(country1));
		final CountryNpc enemyPlayer2 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("lorrycopyspawn02")
				.get(0));
		enemyPlayer2.setCountry(MapCountry.valueOf(country1));
		final CountryNpc enemyPlayer3 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("lorrycopyspawn03")
				.get(0));
		enemyPlayer3.setCountry(MapCountry.valueOf(country1));
		final CountryNpc enemyPlayer4 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("lorrycopyspawn04")
				.get(0));
		enemyPlayer4.setCountry(MapCountry.valueOf(country2));
		final CountryNpc enemyPlayer5 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("lorrycopyspawn05")
				.get(0));
		enemyPlayer5.setCountry(MapCountry.valueOf(country2));
		final CountryNpc enemyPlayer6 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("lorrycopyspawn06")
				.get(0));
		enemyPlayer6.setCountry(MapCountry.valueOf(country2));

		// 一直追杀
		enemyPlayer1.setFollowPolicy(new ForeverFollowPolicy(enemyPlayer1));
		enemyPlayer2.setFollowPolicy(new ForeverFollowPolicy(enemyPlayer2));
		enemyPlayer3.setFollowPolicy(new ForeverFollowPolicy(enemyPlayer3));
		enemyPlayer4.setFollowPolicy(new ForeverFollowPolicy(enemyPlayer4));
		enemyPlayer5.setFollowPolicy(new ForeverFollowPolicy(enemyPlayer5));
		enemyPlayer6.setFollowPolicy(new ForeverFollowPolicy(enemyPlayer6));

		ActionObserver observer = new ActionObserver(ObserverType.SEE) {
			@Override
			public void see(VisibleObject visibleObject) {
				if (visibleObject instanceof Lorry) {
					enemyPlayer1.getAggroList().addHate(lorry, 10000000);
				}
			}
		};
		ActionObserver observer2 = new ActionObserver(ObserverType.SEE) {
			@Override
			public void see(VisibleObject visibleObject) {
				if (visibleObject instanceof Lorry) {
					enemyPlayer4.getAggroList().addHate(lorry, 10000000);
				}
			}
		};
		enemyPlayer1.getObserveController().addObserver(observer);
		enemyPlayer4.getObserveController().addObserver(observer2);

		ActionObserver observer1 = new ActionObserver(ObserverType.SEE) {
			@Override
			public void see(VisibleObject visibleObject) {
				if (visibleObject instanceof Player) {
					enemyPlayer2.getAggroList().addHate((Player) visibleObject, 10);
					enemyPlayer3.getAggroList().addHate((Player) visibleObject, 10);
					enemyPlayer5.getAggroList().addHate((Player) visibleObject, 10);
					enemyPlayer6.getAggroList().addHate((Player) visibleObject, 10);
				}
			}
		};
		enemyPlayer2.getObserveController().addObserver(observer1);
		enemyPlayer3.getObserveController().addObserver(observer1);
		enemyPlayer5.getObserveController().addObserver(observer1);
		enemyPlayer6.getObserveController().addObserver(observer1);
	}

	@Override
	public String getCopyId() {
		return ID;
	}

	@Override
	public void enterCopy(Player player, WorldMapInstance worldMapInstance, CopyResource resource) {
		ExpressQuestCopyController expressQuestCopyController = new ExpressQuestCopyController(player,
				worldMapInstance, resource);
		player.getCopyHistory().setCopyController(expressQuestCopyController);
		expressQuestCopyController.EXPRESSCOPY_QUIT_POINT = EXPRESSCOPY_QUIT_POINT;
		expressQuestCopyController.startCopy();
	}

	/** 出镖车副本坐标 */
	@Static("COPY:EXPRESSCOPY_QUIT_POINT")
	public ConfigValue<String> EXPRESSCOPY_QUIT_POINT;

	@Override
	public void leaveCopyBefore(Player player) {
		setControlleaveCopy(true);
	}

	@Override
	public void leaveCopy(Player player) {
		List<String> result = ChooserManager.getInstance().chooseValueByRequire(player,
				EXPRESSCOPY_QUIT_POINT.getValue());
		RelivePosition p = JsonUtils.string2Object(result.get(0), RelivePosition.class);
		if (player.getPosition().getMapId() == p.getMapId()) {
			World.getInstance().updatePosition(player, p.getX(), p.getY(), player.getHeading());
		} else {
			World.getInstance().setPosition(player, p.getMapId(), p.getX(), p.getY(), player.getHeading());
		}
		player.sendUpdatePosition();
	}

}
