package com.mmorpg.mir.model.copy.controller;

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
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.resource.MapCountry;

@Component
public class TempleQuestCopyController extends AbstractCopyController {

	public TempleQuestCopyController() {
	}

	public TempleQuestCopyController(Player owner, WorldMapInstance worldMapInstance, CopyResource resource) {
		super(owner, worldMapInstance, resource);
	}

	@Override
	public void startCopy() {
		final AtomicInteger die = new AtomicInteger();
		int country = getOwner().getCountryValue();
		final int country1 = country - 1 < 1 ? 3 : country - 1;
		// int country2 = country + 1 > 3 ? 1 : country + 1;

		final CountryNpc friend1 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("templecopynpc1").get(0));
		friend1.setCountry(MapCountry.valueOf(country));

		final CountryNpc friend2 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("templecopynpc2").get(0));
		friend2.setCountry(MapCountry.valueOf(country));

		final CountryNpc enemy1 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("templecopyenemy1").get(0));
		enemy1.setCountry(MapCountry.valueOf(country1));

		final CountryNpc enemy2 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("templecopyenemy2").get(0));
		enemy2.setCountry(MapCountry.valueOf(country1));

		CountryNpc enemy3 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("templecopyenemy3").get(0));
		enemy3.setCountry(MapCountry.valueOf(country1));

		CountryNpc enemy4 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("templecopyenemy4").get(0));
		enemy4.setCountry(MapCountry.valueOf(country1));

		friend1.getObserveController().addObserver(new ActionObserver(ObserverType.SEE) {

			@Override
			public void see(VisibleObject visibleObject) {
				if (visibleObject == enemy1) {
					friend1.getAggroList().addHate(enemy1, 10000000);
				}
			}

		});

		friend2.getObserveController().addObserver(new ActionObserver(ObserverType.SEE) {

			@Override
			public void see(VisibleObject visibleObject) {
				if (visibleObject == enemy2) {
					friend2.getAggroList().addHate(enemy2, 10000000);
				}
			}

		});

		enemy1.getObserveController().addObserver(new ActionObserver(ObserverType.DIE, ObserverType.SEE) {
			@Override
			public void die(Creature creature) {
				enemyDie(die);
			}

			@Override
			public void see(VisibleObject visibleObject) {
				if (visibleObject == friend1) {
					enemy1.getAggroList().addHate(friend1, 10000000);
				}
			}

		});

		enemy2.getObserveController().addObserver(new ActionObserver(ObserverType.DIE, ObserverType.SEE) {
			@Override
			public void die(Creature creature) {
				enemyDie(die);

				final CountryNpc enemy5 = (CountryNpc) SpawnManager.getInstance().creatObject("templecopyenemy5",
						getWorldMapInstance().getInstanceId());
				enemy5.setCountry(MapCountry.valueOf(country1));

				enemy5.getObserveController().addObserver(new ActionObserver(ObserverType.SEE, ObserverType.DIE) {

					@Override
					public void see(VisibleObject visibleObject) {
						if (visibleObject instanceof Player) {
							enemy5.getAggroList().addHate((Player) visibleObject, 10000000);
						}
					}

					@Override
					public void die(Creature creature) {
						enemyDie(die);
					}

				});

				SpawnManager.getInstance().bringIntoWorld(enemy5, getWorldMapInstance().getInstanceId());
			}

			@Override
			public void see(VisibleObject visibleObject) {
				if (visibleObject == friend2) {
					enemy2.getAggroList().addHate(friend2, 10000000);
				}
			}

		});
		enemy3.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				enemyDie(die);
			}

		});
		enemy4.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				enemyDie(die);
			}

		});

	}

	private void enemyDie(AtomicInteger die) {
		die.incrementAndGet();
		if (die.get() >= 5) {
			getOwner().getCopyHistory().addTodayCompleteCount(getOwner(), getCopyId(), 1, false, Integer.MAX_VALUE,
					true);
		}
		PacketSendUtility.sendPacket(getOwner(), new SM_Copy_EnemyDie());
	}

	@Override
	public String getCopyId() {
		return "templetaskcopy";
	}

	@Override
	public void enterCopy(Player player, WorldMapInstance worldMapInstance, CopyResource resource) {
		TempleQuestCopyController templeQuestCopyController = new TempleQuestCopyController(player, worldMapInstance,
				resource);
		player.getCopyHistory().setCopyController(templeQuestCopyController);
		templeQuestCopyController.startCopy();
	}

}
