package com.mmorpg.mir.model.copy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.controllers.CountryNpcController;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.copy.packet.SM_Copy_EnemyDie;
import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.gameobjects.CountryNpc;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.skill.model.DamageResult;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.resource.MapCountry;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.JsonUtils;

@Component
public class CountryQuestCopyController extends AbstractCopyController {

	public static final String ID = "Task_copy2";

	private ArrayList<Creature> copyAIs = new ArrayList<Creature>();

	public CountryQuestCopyController() {
	}

	public CountryQuestCopyController(Player owner, final WorldMapInstance worldMapInstance, CopyResource resource) {
		super(owner, worldMapInstance, resource);
	}

	private static class BossQuestDachenController extends CountryNpcController {

		@Override
		public void onAttack(Creature creature, int skillId, long damage, DamageResult damageResult) {
			synchronized (this) {
				if (getOwner().getLifeStats().getCurrentHp() == 1) {
					return;
				}
				if (getOwner().getLifeStats().getCurrentHp() <= damage) {
					super.onAttack(creature, skillId, getOwner().getLifeStats().getCurrentHp() - 1, damageResult);
				} else {
					super.onAttack(creature, skillId, damage, damageResult);
				}
			}
		}

	}

	@Override
	public void startCopy() {
		int country = getOwner().getCountryValue();
		final int country1 = country - 1 < 1 ? 3 : country - 1;
		// 设置敌我识别
		CountryNpc dachen = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("protectcopydachen").get(0));
		BossQuestDachenController bqdc = new BossQuestDachenController();
		bqdc.setOwner(dachen);
		dachen.setController(bqdc);
		dachen.setCountry(MapCountry.valueOf(country));
		dachen.getLifeStats().setCurrentHpPercent(80);
		copyAIs.add(dachen);

		CountryNpc friend1 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("protectcopyFriend1").get(0));
		friend1.setCountry(MapCountry.valueOf(country));
		CountryNpc friend2 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("protectcopyFriend2").get(0));
		friend2.setCountry(MapCountry.valueOf(country));
		CountryNpc friend3 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("protectcopyFriend3").get(0));
		friend3.setCountry(MapCountry.valueOf(country));
		final CountryNpc friend4 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("protectcopyFriend4").get(0));
		friend4.setCountry(MapCountry.valueOf(country));

		copyAIs.add(friend1);
		copyAIs.add(friend2);
		copyAIs.add(friend3);
		copyAIs.add(friend4);

		// 敌国
		CountryNpc enemy1 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("protectcopyenemy1").get(0));
		enemy1.setCountry(MapCountry.valueOf(country1));
		CountryNpc enemy2 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("protectcopyenemy2").get(0));
		enemy2.setCountry(MapCountry.valueOf(country1));
		CountryNpc enemy3 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("protectcopyenemy3").get(0));
		enemy3.setCountry(MapCountry.valueOf(country1));
		final CountryNpc enemy4 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("protectcopyenemy4").get(0));
		enemy4.setCountry(MapCountry.valueOf(country1));
		CountryNpc enemy5 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("protectcopyenemy5").get(0));
		enemy5.setCountry(MapCountry.valueOf(country1));
		CountryNpc enemy6 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("protectcopyenemy6").get(0));
		enemy6.setCountry(MapCountry.valueOf(country1));
		copyAIs.add(enemy1);
		copyAIs.add(enemy2);
		copyAIs.add(enemy3);
		copyAIs.add(enemy4);
		copyAIs.add(enemy5);
		copyAIs.add(enemy6);

		friend1.getAggroList().addHate(enemy1, 1000000);
		enemy1.getAggroList().addHate(friend1, 1000000);

		friend2.getAggroList().addHate(enemy2, 1000000);
		enemy2.getAggroList().addHate(friend2, 1000000);

		friend3.getAggroList().addHate(enemy3, 1000000);
		enemy3.getAggroList().addHate(friend3, 1000000);

		friend4.getObserveController().addObserver(new ActionObserver(ObserverType.SEE) {
			@Override
			public void see(VisibleObject visibleObject) {
				if (visibleObject == enemy4) {
					friend4.getAggroList().addHate(enemy4, 10000000);
				}
			}

		});

		enemy4.getAggroList().addHate(dachen, 10000000);
		enemy5.getAggroList().addHate(dachen, 10000000);
		enemy6.getAggroList().addHate(dachen, 10000000);

		final AtomicInteger die = new AtomicInteger();

		enemy1.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				enemyDie(die);
			}
		});
		enemy2.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				enemyDie(die);
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
				final CountryNpc enemy7 = (CountryNpc) SpawnManager.getInstance().creatObject("protectcopyenemy7",
						getWorldMapInstance().getInstanceId());
				enemy7.setCountry(MapCountry.valueOf(country1));
				SpawnManager.getInstance().bringIntoWorld(enemy7, getWorldMapInstance().getInstanceId());

				final CountryNpc enemy8 = (CountryNpc) SpawnManager.getInstance().creatObject("protectcopyenemy8",
						getWorldMapInstance().getInstanceId());
				enemy8.setCountry(MapCountry.valueOf(country1));
				SpawnManager.getInstance().bringIntoWorld(enemy8, getWorldMapInstance().getInstanceId());

				final CountryNpc enemy9 = (CountryNpc) SpawnManager.getInstance().creatObject("protectcopyenemy9",
						getWorldMapInstance().getInstanceId());
				enemy9.setCountry(MapCountry.valueOf(country1));
				SpawnManager.getInstance().bringIntoWorld(enemy9, getWorldMapInstance().getInstanceId());

				enemy7.getAggroList().addHate(friend4, 10);
				enemy8.getAggroList().addHate(friend4, 10);
				enemy9.getAggroList().addHate(friend4, 10);

				enemy7.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
					@Override
					public void die(Creature creature) {
						enemyDie(die);
					}

					@Override
					public void see(VisibleObject visibleObject) {
						if (visibleObject == friend4) {
							enemy7.getAggroList().addHate(friend4, 1000000);
						}
					}

				});
				enemy8.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
					@Override
					public void die(Creature creature) {
						enemyDie(die);
					}

					@Override
					public void see(VisibleObject visibleObject) {
						if (visibleObject == friend4) {
							enemy8.getAggroList().addHate(friend4, 1000000);
						}
					}
				});
				enemy9.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
					@Override
					public void die(Creature creature) {
						enemyDie(die);
					}

					@Override
					public void see(VisibleObject visibleObject) {
						if (visibleObject == friend4) {
							enemy9.getAggroList().addHate(friend4, 1000000);
						}
					}
				});
				copyAIs.add(enemy7);
				copyAIs.add(enemy8);
				copyAIs.add(enemy9);
				enemyDie(die);
			}
		});
		enemy5.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				enemyDie(die);
			}
		});
		enemy6.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				enemyDie(die);
			}
		});

	}

	@Override
	public String getCopyId() {
		return ID;
	}

	private void enemyDie(AtomicInteger die) {
		die.incrementAndGet();
		if (die.get() >= 9) {
			getOwner().getCopyHistory().addTodayCompleteCount(getOwner(), getCopyId(), 1, false, Integer.MAX_VALUE,true);
		}
		PacketSendUtility.sendPacket(getOwner(), new SM_Copy_EnemyDie());
	}

	@Override
	public void enterCopy(Player player, WorldMapInstance worldMapInstance, CopyResource resource) {
		CountryQuestCopyController copyController = new CountryQuestCopyController(player, worldMapInstance, resource);
		player.getCopyHistory().setCopyController(copyController);
		copyController.COUNTRYCOPY_QUIT_POINT = COUNTRYCOPY_QUIT_POINT;
		copyController.startCopy();
	}

	/** 出保护大臣副本坐标 */
	@Static("COPY:COUNTRYCOPY_QUIT_POINT")
	public ConfigValue<String> COUNTRYCOPY_QUIT_POINT;

	@Override
	public void leaveCopyBefore(Player player) {
		setControlleaveCopy(true);
	}

	@Override
	public void leaveCopy(Player player) {
		for (Creature ai : copyAIs) {
			ai.getController().delete();
		}
		List<String> result = ChooserManager.getInstance().chooseValueByRequire(player,
				COUNTRYCOPY_QUIT_POINT.getValue());
		RelivePosition p = JsonUtils.string2Object(result.get(0), RelivePosition.class);
		if (player.getPosition().getMapId() == p.getMapId()) {
			World.getInstance().updatePosition(player, p.getX(), p.getY(), player.getHeading());
		} else {
			World.getInstance().setPosition(player, p.getMapId(), p.getX(), p.getY(), player.getHeading());
		}
		player.sendUpdatePosition();
		copyAIs.clear();
	}

}
