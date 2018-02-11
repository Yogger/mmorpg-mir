package com.mmorpg.mir.model.copy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.copy.packet.SM_Copy_EnemyDie;
import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.gameobjects.Boss;
import com.mmorpg.mir.model.gameobjects.CountryNpc;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.DropObject;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.resource.MapCountry;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.JsonUtils;

@Component
public class BossQuestCopyController extends AbstractCopyController {
	
	private ArrayList<Creature> copyAIs = new ArrayList<Creature>();
	
	public BossQuestCopyController() {
	}

	public BossQuestCopyController(Player owner, WorldMapInstance worldMapInstance, CopyResource resource) {
		super(owner, worldMapInstance, resource);
	}

	@Override
	public void startCopy() {
		final AtomicInteger die = new AtomicInteger();
		int country = getOwner().getCountryValue();
		int country1 = country - 1 < 1 ? 3 : country - 1;
		int country2 = country + 1 > 3 ? 1 : country + 1;
		// 设置敌我识别
		final Boss boss = (Boss) (getWorldMapInstance().findObjectBySpawnId("questcopy1Boss").get(0));
		boss.setCountry(MapCountry.valueOf(country1));
		boss.getController().setBroad(false);
		final CountryNpc summonPlayer1 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("questcopy1Robot1")
				.get(0));
		summonPlayer1.setCountry(MapCountry.valueOf(country2));
		summonPlayer1.getAggroList().addHate(boss, 10);
		summonPlayer1.getObserveController().addObserver(new ActionObserver(ObserverType.SEE) {

			@Override
			public void attack(Creature creature) {
				summonPlayer1.getAggroList().addHate(getOwner(), 100000);
			}

			@Override
			public void die(Creature creature) {
				// enemyDie(die);
			}

		});

		final CountryNpc summonPlayer2 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("questcopy1Robot2")
				.get(0));
		summonPlayer2.setCountry(MapCountry.valueOf(country2));
		summonPlayer2.getAggroList().addHate(boss, 10);
		summonPlayer2.getObserveController().addObserver(new ActionObserver(ObserverType.SEE) {

			@Override
			public void attack(Creature creature) {
				summonPlayer2.getAggroList().addHate(getOwner(), 100000);
			}

			@Override
			public void die(Creature creature) {
				// enemyDie(die);
			}

		});

		final CountryNpc summonPlayer3 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("questcopy1Robot3")
				.get(0));
		summonPlayer3.setCountry(MapCountry.valueOf(country2));

		// 设置玩家仇恨
		summonPlayer3.getAggroList().addHate(boss, 10000000);
		boss.getAggroList().addHate(summonPlayer3, 10000000);

		final CountryNpc summonPlayer4 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("questcopy1Robot4")
				.get(0));
		summonPlayer4.setCountry(MapCountry.valueOf(country2));
		final CountryNpc summonPlayer5 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("questcopy1Robot5")
				.get(0));
		summonPlayer5.setCountry(MapCountry.valueOf(country2));

		summonPlayer4.getAggroList().addHate(boss, 10);
		summonPlayer5.getAggroList().addHate(boss, 10);

		summonPlayer3.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {

			@Override
			public void die(Creature creature) {
				if (!boss.getLifeStats().isAlreadyDead() && !summonPlayer4.getLifeStats().isAlreadyDead()) {
					boss.getAggroList().addHate(summonPlayer4, 10000000);
				} else if (!boss.getLifeStats().isAlreadyDead() && !summonPlayer5.getLifeStats().isAlreadyDead()) {
					boss.getAggroList().addHate(summonPlayer5, 10000000);
				}
				// enemyDie(die);
			}

		});

		summonPlayer4.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				if (!boss.getLifeStats().isAlreadyDead() && !summonPlayer5.getLifeStats().isAlreadyDead()) {
					boss.getAggroList().addHate(summonPlayer5, 10000000);
					// enemyDie(die);
				}
			}
		});

		summonPlayer5.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				// enemyDie(die);
			}
		});

		boss.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				enemyDie(die);

				summonPlayer1.getController().delete();
				summonPlayer2.getController().delete();
				summonPlayer3.getController().delete();
				summonPlayer4.getController().delete();
				summonPlayer5.getController().delete();
			}

		});

		copyAIs.add(boss);
		copyAIs.add(summonPlayer1);
		copyAIs.add(summonPlayer2);
		copyAIs.add(summonPlayer3);
		copyAIs.add(summonPlayer4);
		copyAIs.add(summonPlayer5);
	}

	private void enemyDie(AtomicInteger die) {
		die.incrementAndGet();
		if (die.get() >= 1) {
			getOwner().getCopyHistory().addTodayCompleteCount(getOwner(),getCopyId(), 1, false, Integer.MAX_VALUE,true);
		}
		PacketSendUtility.sendPacket(getOwner(), new SM_Copy_EnemyDie());
	}

	@Override
	public String getCopyId() {
		return "Task_copy1";
	}

	@Override
	public void enterCopy(Player player, WorldMapInstance worldMapInstance, CopyResource resource) {
		BossQuestCopyController bossQuestCopyController = new BossQuestCopyController(player, worldMapInstance,
				resource);
		player.getCopyHistory().setCopyController(bossQuestCopyController);
		bossQuestCopyController.BOSSCOPY_QUIT_POINT = BOSSCOPY_QUIT_POINT;
		bossQuestCopyController.startCopy();
	}

	@Override
	public void leaveCopyBefore(Player player) {
		List<DropObject> dropObjects = getWorldMapInstance().findObjectByType(ObjectType.DROPOBJECT);
		if (!dropObjects.isEmpty()) {
			Reward reward = Reward.valueOf();
			for (DropObject dropObject : dropObjects) {
				reward.addRewardItem(dropObject.getRewardItem());
			}
			RewardManager.getInstance().grantReward(player, reward, ModuleInfo.valueOf(ModuleType.COPY, SubModuleType.COPY_QUEST));
		}
		setControlleaveCopy(true);
	}

	/** 出山大王副本坐标 */
	@Static("COPY:BOSSCOPY_QUIT_POINT")
	public ConfigValue<String> BOSSCOPY_QUIT_POINT;

	@Override
	public void leaveCopy(Player player) {
		for (Creature ai: copyAIs) {
			ai.getController().delete();
		}
		List<String> result = ChooserManager.getInstance().chooseValueByRequire(player,
				BOSSCOPY_QUIT_POINT.getValue());
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
