package com.mmorpg.mir.model.copy.controller;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.copy.packet.SM_Copy_EnemyDie;
import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.gameobjects.CountryNpc;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.resource.MapCountry;
import com.windforce.common.resource.anno.Static;

@Component
public class SociatyCopyController extends AbstractCopyController {

	public static final String ID = "sociatycopy";

	public SociatyCopyController() {
	}

	public SociatyCopyController(Player owner, final WorldMapInstance worldMapInstance, CopyResource resource) {
		super(owner, worldMapInstance, resource);
	}

	@Override
	public void startCopy() {
		final AtomicInteger die = new AtomicInteger();
		final int country = getOwner().getCountryValue();
//		final int country1 = country - 1 < 1 ? 3 : country - 1;
		final int country1 = 0;
		int country2 = country + 1 > 3 ? 1 : country + 1;
		// 设置敌我识别
		final CountryNpc boss = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("sociatycopyboss").get(0));
		boss.setCountry(MapCountry.valueOf(country2));

		boss.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				final CountryNpc friendBoss = (CountryNpc) SpawnManager.getInstance().creatObject(
						"sociatycopyfriendboss", getWorldMapInstance().getInstanceId());
				friendBoss.setCountry(MapCountry.valueOf(country));
				SpawnManager.getInstance().bringIntoWorld(friendBoss, getWorldMapInstance().getInstanceId());

				final CountryNpc enemy4 = (CountryNpc) SpawnManager.getInstance().creatObject("sociatycopyenemy4",
						getWorldMapInstance().getInstanceId());
				enemy4.setCountry(MapCountry.valueOf(country1));
				SpawnManager.getInstance().bringIntoWorld(enemy4, getWorldMapInstance().getInstanceId());

				final CountryNpc enemy5 = (CountryNpc) SpawnManager.getInstance().creatObject("sociatycopyenemy5",
						getWorldMapInstance().getInstanceId());
				enemy5.setCountry(MapCountry.valueOf(country1));
				SpawnManager.getInstance().bringIntoWorld(enemy5, getWorldMapInstance().getInstanceId());

				final CountryNpc enemy6 = (CountryNpc) SpawnManager.getInstance().creatObject("sociatycopyenemy6",
						getWorldMapInstance().getInstanceId());
				enemy6.setCountry(MapCountry.valueOf(country1));
				SpawnManager.getInstance().bringIntoWorld(enemy6, getWorldMapInstance().getInstanceId());

				enemy4.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
					@Override
					public void die(Creature creature) {
						enemyDie(die);
					}
				});

				enemy5.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
					@Override
					public void die(Creature creature) {
						enemyDie(die);

						final CountryNpc enemy7 = (CountryNpc) SpawnManager.getInstance().creatObject(
								"sociatycopyenemy7", getWorldMapInstance().getInstanceId());
						enemy7.setCountry(MapCountry.valueOf(country1));
						SpawnManager.getInstance().bringIntoWorld(enemy7, getWorldMapInstance().getInstanceId());

						final CountryNpc enemy8 = (CountryNpc) SpawnManager.getInstance().creatObject(
								"sociatycopyenemy8", getWorldMapInstance().getInstanceId());
						enemy8.setCountry(MapCountry.valueOf(country1));
						SpawnManager.getInstance().bringIntoWorld(enemy8, getWorldMapInstance().getInstanceId());

						final CountryNpc enemy9 = (CountryNpc) SpawnManager.getInstance().creatObject(
								"sociatycopyenemy9", getWorldMapInstance().getInstanceId());
						enemy9.setCountry(MapCountry.valueOf(country1));
						SpawnManager.getInstance().bringIntoWorld(enemy9, getWorldMapInstance().getInstanceId());

						enemy7.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
							@Override
							public void die(Creature creature) {
								enemyDie(die);
							}
						});

						enemy8.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
							@Override
							public void die(Creature creature) {
								enemyDie(die);

								final CountryNpc enemy10 = (CountryNpc) SpawnManager.getInstance().creatObject(
										"sociatycopyenemy10", getWorldMapInstance().getInstanceId());
								enemy10.setCountry(MapCountry.valueOf(country1));
								SpawnManager.getInstance().bringIntoWorld(enemy10,
										getWorldMapInstance().getInstanceId());

								final CountryNpc enemy11 = (CountryNpc) SpawnManager.getInstance().creatObject(
										"sociatycopyenemy11", getWorldMapInstance().getInstanceId());
								enemy11.setCountry(MapCountry.valueOf(country1));
								SpawnManager.getInstance().bringIntoWorld(enemy11,
										getWorldMapInstance().getInstanceId());

								final CountryNpc enemy12 = (CountryNpc) SpawnManager.getInstance().creatObject(
										"sociatycopyenemy12", getWorldMapInstance().getInstanceId());
								enemy12.setCountry(MapCountry.valueOf(country1));
								SpawnManager.getInstance().bringIntoWorld(enemy12,
										getWorldMapInstance().getInstanceId());

								enemy10.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
									@Override
									public void die(Creature creature) {
										enemyDie(die);
									}
								});

								enemy11.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
									@Override
									public void die(Creature creature) {
										enemyDie(die);

									}
								});

								enemy12.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
									@Override
									public void die(Creature creature) {
										enemyDie(die);
									}
								});
							}
						});

						enemy9.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
							@Override
							public void die(Creature creature) {
								enemyDie(die);
							}
						});
					}
				});

				enemy6.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
					@Override
					public void die(Creature creature) {
						enemyDie(die);
					}
				});
			}

		});
		// 敌国
		final CountryNpc enemy1 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("sociatycopyenemy1").get(0));
		enemy1.setCountry(MapCountry.valueOf(country1));
		final CountryNpc enemy2 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("sociatycopyenemy2").get(0));
		enemy2.setCountry(MapCountry.valueOf(country1));
		final CountryNpc enemy3 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("sociatycopyenemy3").get(0));
		enemy3.setCountry(MapCountry.valueOf(country1));

		enemy1.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				enemyDie(die);
			}

			@Override
			public void see(VisibleObject visibleObject) {
				enemy1.getAggroList().addHate(boss, 100000);
			}

		});
		enemy2.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				enemyDie(die);
			}

			@Override
			public void see(VisibleObject visibleObject) {
				enemy2.getAggroList().addHate(boss, 100000);
			}

		});

		enemy3.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				enemyDie(die);
			}

			@Override
			public void see(VisibleObject visibleObject) {
				enemy3.getAggroList().addHate(boss, 100000);
			}

		});

	}

	@Override
	public String getCopyId() {
		return ID;
	}

	/** 临时国王技能Id */
	@Static("COPY:SOCIATY_TEMP_KING_SKILLID")
	public ConfigValue<Integer> TEMP_KING_SKILLID;

	private void enemyDie(AtomicInteger die) {
		die.incrementAndGet();
		if (die.get() >= 12) {
			getOwner().getCopyHistory().addTodayCompleteCount(getOwner(),getCopyId(), 1, false, Integer.MAX_VALUE,true);
			Skill skill = SkillEngine.getInstance().getSkill(null, TEMP_KING_SKILLID.getValue(),
					getOwner().getObjectId(), 0, 0, getOwner(), null);
			skill.noEffectorUseSkill();
		}
		PacketSendUtility.sendPacket(getOwner(), new SM_Copy_EnemyDie());
	}

	@Override
	public void enterCopy(Player player, WorldMapInstance worldMapInstance, CopyResource resource) {
		SociatyCopyController copyController = new SociatyCopyController(player, worldMapInstance, resource);
		player.getCopyHistory().setCopyController(copyController);
		copyController.TEMP_KING_SKILLID = TEMP_KING_SKILLID;
		copyController.startCopy();
	}

}
