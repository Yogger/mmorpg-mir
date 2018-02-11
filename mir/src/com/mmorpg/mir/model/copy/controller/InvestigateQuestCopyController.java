package com.mmorpg.mir.model.copy.controller;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.copy.packet.SM_Copy_EnemyDie;
import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.gameobjects.CountryNpc;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.resource.MapCountry;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.JsonUtils;

@Component
public class InvestigateQuestCopyController extends AbstractCopyController {

	public static final String ID = "spytaskcopy";

	public InvestigateQuestCopyController() {
	}

	public InvestigateQuestCopyController(Player owner, WorldMapInstance worldMapInstance, CopyResource resource) {
		super(owner, worldMapInstance, resource);
	}

	@Override
	public void startCopy() {
		final AtomicInteger die = new AtomicInteger();
		final int country = getOwner().getCountryValue();
		final int country1 = country - 1 < 1 ? 3 : country - 1;

		Future<?> enemy1Future = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				final CountryNpc spycopyenemy1 = (CountryNpc) SpawnManager.getInstance().creatObject("spycopyenemy1",
						getWorldMapInstance().getInstanceId());
				spycopyenemy1.setCountry(MapCountry.valueOf(country1));
				SpawnManager.getInstance().bringIntoWorld(spycopyenemy1, getWorldMapInstance().getInstanceId());
				spycopyenemy1.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
					@Override
					public void die(Creature creature) {
						enemyDie(die);
					}
				});
			}
		}, 5000);
		getWorldMapInstance().addTriggerTask(enemy1Future);

		Future<?> friend1Future = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				final CountryNpc spycopyfriend1 = (CountryNpc) SpawnManager.getInstance().creatObject("spycopyfriend1",
						getWorldMapInstance().getInstanceId());
				spycopyfriend1.setCountry(MapCountry.valueOf(country));
				SpawnManager.getInstance().bringIntoWorld(spycopyfriend1, getWorldMapInstance().getInstanceId());
				spycopyfriend1.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
					@Override
					public void die(Creature creature) {
						// enemyDie(die);
					}
				});
			}
		}, 10000);
		getWorldMapInstance().addTriggerTask(friend1Future);

		final CountryNpc spycopyenemy2 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("spycopyenemy2")
				.get(0));
		spycopyenemy2.setCountry(MapCountry.valueOf(country1));
		spycopyenemy2.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {

			@Override
			public void die(Creature creature) {
				enemyDie(die);
				final CountryNpc spycopyenemy4 = (CountryNpc) SpawnManager.getInstance().creatObject("spycopyenemy4",
						getWorldMapInstance().getInstanceId());
				spycopyenemy4.setCountry(MapCountry.valueOf(country1));
				SpawnManager.getInstance().bringIntoWorld(spycopyenemy4, getWorldMapInstance().getInstanceId());
				spycopyenemy4.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
					@Override
					public void die(Creature creature) {
						enemyDie(die);
					}
				});
			}

		});

		final CountryNpc spycopyenemy3 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("spycopyenemy3")
				.get(0));
		spycopyenemy3.setCountry(MapCountry.valueOf(country1));
		spycopyenemy3.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				enemyDie(die);
			}
		});

	}

	private void enemyDie(AtomicInteger die) {
		die.incrementAndGet();
		if (die.get() >= 4) {
			getOwner().getCopyHistory().addTodayCompleteCount(getOwner(),getCopyId(), 1, false, Integer.MAX_VALUE,true);
		}
		PacketSendUtility.sendPacket(getOwner(), new SM_Copy_EnemyDie());
	}

	@Override
	public String getCopyId() {
		return ID;
	}

	@Override
	public void enterCopy(Player player, WorldMapInstance worldMapInstance, CopyResource resource) {
		InvestigateQuestCopyController copyController = new InvestigateQuestCopyController(player, worldMapInstance,
				resource);
		player.getCopyHistory().setCopyController(copyController);
		copyController.INVESTIGATECOPY_QUIT_POINT = INVESTIGATECOPY_QUIT_POINT;
		copyController.startCopy();
	}

	/** 出刺探副本坐标 */
	@Static("COPY:INVESTIGATECOPY_QUIT_POINT")
	public ConfigValue<String> INVESTIGATECOPY_QUIT_POINT;

	@Override
	public void leaveCopyBefore(Player player) {
		setControlleaveCopy(true);
	}

	@Override
	public void leaveCopy(Player player) {
		List<String> result = ChooserManager.getInstance().chooseValueByRequire(player,
				INVESTIGATECOPY_QUIT_POINT.getValue());
		RelivePosition p = JsonUtils.string2Object(result.get(0), RelivePosition.class);
		if (player.getPosition().getMapId() == p.getMapId()) {
			World.getInstance().updatePosition(player, p.getX(), p.getY(), player.getHeading());
		} else {
			World.getInstance().setPosition(player, p.getMapId(), p.getX(), p.getY(), player.getHeading());
		}
		player.sendUpdatePosition();
	}

}
