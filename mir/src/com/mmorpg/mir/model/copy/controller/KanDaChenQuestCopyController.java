package com.mmorpg.mir.model.copy.controller;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.copy.packet.SM_Copy_EnemyDie;
import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.gameobjects.CountryNpc;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.resource.MapCountry;

@Component
public class KanDaChenQuestCopyController extends AbstractCopyController {

	public static final String ID = "Task_copy3";

	public KanDaChenQuestCopyController() {
	}

	public KanDaChenQuestCopyController(Player owner, WorldMapInstance worldMapInstance, CopyResource resource) {
		super(owner, worldMapInstance, resource);
	}

	@Override
	public void startCopy() {
		final AtomicInteger die = new AtomicInteger();
		final int country = getOwner().getCountryValue();
		final int country1 = country - 1 < 1 ? 3 : country - 1;

		final CountryNpc dachen = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("procopydachen").get(0));
		dachen.setCountry(MapCountry.valueOf(country));

		Future<?> enemyFuture = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				final CountryNpc procopyFriend1 = (CountryNpc) SpawnManager.getInstance().creatObject("procopyFriend1",
						getWorldMapInstance().getInstanceId());
				procopyFriend1.setCountry(MapCountry.valueOf(country));
				SpawnManager.getInstance().bringIntoWorld(procopyFriend1, getWorldMapInstance().getInstanceId());

				final CountryNpc procopyFriend2 = (CountryNpc) SpawnManager.getInstance().creatObject("procopyFriend2",
						getWorldMapInstance().getInstanceId());
				procopyFriend2.setCountry(MapCountry.valueOf(country));
				SpawnManager.getInstance().bringIntoWorld(procopyFriend2, getWorldMapInstance().getInstanceId());

				final CountryNpc procopyFriend3 = (CountryNpc) SpawnManager.getInstance().creatObject("procopyFriend3",
						getWorldMapInstance().getInstanceId());
				procopyFriend3.setCountry(MapCountry.valueOf(country));
				SpawnManager.getInstance().bringIntoWorld(procopyFriend3, getWorldMapInstance().getInstanceId());
			}
		}, 9000);
		getWorldMapInstance().addTriggerTask(enemyFuture);

		final CountryNpc procopyenemy1 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("procopyenemy1")
				.get(0));
		procopyenemy1.setCountry(MapCountry.valueOf(country1));
		procopyenemy1.getObserveController().addObserver(new ActionObserver(ObserverType.SEE) {
			@Override
			public void die(Creature creature) {
				enemyDie(die);
			}

			@Override
			public void see(VisibleObject visibleObject) {
				procopyenemy1.getAggroList().addHate(dachen, 100000);
			}
		});

		final CountryNpc procopyenemy2 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("procopyenemy2")
				.get(0));
		procopyenemy2.setCountry(MapCountry.valueOf(country1));
		procopyenemy2.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				enemyDie(die);
			}

			@Override
			public void see(VisibleObject visibleObject) {
				procopyenemy2.getAggroList().addHate(dachen, 100000);
			}
		});

		final CountryNpc procopyenemy3 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("procopyenemy3")
				.get(0));
		procopyenemy3.setCountry(MapCountry.valueOf(country1));
		procopyenemy3.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				enemyDie(die);
			}

			@Override
			public void see(VisibleObject visibleObject) {
				procopyenemy3.getAggroList().addHate(dachen, 100000);
			}
		});

	}

	private void enemyDie(AtomicInteger die) {
		die.incrementAndGet();
		if (die.get() >= 3) {
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
		KanDaChenQuestCopyController copyController = new KanDaChenQuestCopyController(player, worldMapInstance,
				resource);
		player.getCopyHistory().setCopyController(copyController);
		copyController.startCopy();
	}

}
