package com.mmorpg.mir.model.copy.controller;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.copy.packet.SM_Copy_EnemyDie;
import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.gameobjects.CountryNpc;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.resource.MapCountry;

@Component
public class CountryFlagQuestCopyController extends AbstractCopyController {

	public static final String ID = "countryflagcopy";

	private ArrayList<Creature> copyAIs = new ArrayList<Creature>();

	public CountryFlagQuestCopyController() {
	}

	public CountryFlagQuestCopyController(Player owner, final WorldMapInstance worldMapInstance, CopyResource resource) {
		super(owner, worldMapInstance, resource);
	}

	@Override
	public void startCopy() {
		int country = getOwner().getCountryValue();
		final int country1 = country - 1 < 1 ? 3 : country - 1;
		// 设置敌我识别
		CountryNpc flag = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("flagcopyflag_qi").get(0));
		flag.setCountry(MapCountry.valueOf(country1));
		String objectKey = null;
		switch (country1) {
		case 1:
			objectKey = "flagcopyflag_qi";
			break;
		case 2:
			objectKey = "flagcopyflag_chu";
			break;
		case 3:
			objectKey = "flagcopyflag_zhao";
			break;
		default:
			break;
		}
		flag.setObjectKey(objectKey);
		copyAIs.add(flag);

		CountryNpc friend1 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("flagcopyfriend1").get(0));
		friend1.setCountry(MapCountry.valueOf(country));
		CountryNpc friend2 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("flagcopyfriend2").get(0));
		friend2.setCountry(MapCountry.valueOf(country));
		CountryNpc friend3 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("flagcopyfriend3").get(0));
		friend3.setCountry(MapCountry.valueOf(country));
		CountryNpc friend4 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("flagcopyfriend4").get(0));
		friend4.setCountry(MapCountry.valueOf(country));
		CountryNpc friend5 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("flagcopyfriend5").get(0));
		friend5.setCountry(MapCountry.valueOf(country));
		CountryNpc friend6 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("flagcopyfriend6").get(0));
		friend6.setCountry(MapCountry.valueOf(country));
		CountryNpc friend7 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("flagcopyfriend7").get(0));
		friend7.setCountry(MapCountry.valueOf(country));
		CountryNpc friend8 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("flagcopyfriend8").get(0));
		friend8.setCountry(MapCountry.valueOf(country));
		CountryNpc friend9 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("flagcopyfriend9").get(0));
		friend9.setCountry(MapCountry.valueOf(country));
		CountryNpc friend10 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("flagcopyfriend10").get(0));
		friend10.setCountry(MapCountry.valueOf(country));
		CountryNpc friend11 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("flagcopyfriend11").get(0));
		friend11.setCountry(MapCountry.valueOf(country));

		copyAIs.add(friend1);
		copyAIs.add(friend2);
		copyAIs.add(friend3);
		copyAIs.add(friend4);
		copyAIs.add(friend5);
		copyAIs.add(friend6);
		copyAIs.add(friend7);
		copyAIs.add(friend8);
		copyAIs.add(friend9);
		copyAIs.add(friend10);
		copyAIs.add(friend11);

		// 敌国
		CountryNpc enemy1 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("flagcopyenemy1").get(0));
		enemy1.setCountry(MapCountry.valueOf(country1));
		CountryNpc enemy2 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("flagcopyenemy2").get(0));
		enemy2.setCountry(MapCountry.valueOf(country1));
		CountryNpc enemy3 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("flagcopyenemy3").get(0));
		enemy3.setCountry(MapCountry.valueOf(country1));
		CountryNpc enemy4 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("flagcopyenemy4").get(0));
		enemy4.setCountry(MapCountry.valueOf(country1));
		CountryNpc enemy5 = (CountryNpc) (getWorldMapInstance().findObjectBySpawnId("flagcopyenemy5").get(0));
		enemy5.setCountry(MapCountry.valueOf(country1));
		copyAIs.add(enemy1);
		copyAIs.add(enemy2);
		copyAIs.add(enemy3);
		copyAIs.add(enemy4);
		copyAIs.add(enemy5);

		final AtomicInteger die = new AtomicInteger();

		flag.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
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
		if (die.get() >= 1) {
			getOwner().getCopyHistory().addTodayCompleteCount(getOwner(), getCopyId(), 1, false, Integer.MAX_VALUE,true);
		}
		PacketSendUtility.sendPacket(getOwner(), new SM_Copy_EnemyDie());
	}

	@Override
	public void enterCopy(Player player, WorldMapInstance worldMapInstance, CopyResource resource) {
		CountryFlagQuestCopyController copyController = new CountryFlagQuestCopyController(player, worldMapInstance,
				resource);
		player.getCopyHistory().setCopyController(copyController);
		copyController.startCopy();
	}

	@Override
	public void leaveCopyBefore(Player player) {
		setControlleaveCopy(true);
	}

	@Override
	public void leaveCopy(Player player) {
		for (Creature ai : copyAIs) {
			ai.getController().delete();
		}
		// List<String> result =
		// ChooserManager.getInstance().chooseValueByRequire(player,
		// COUNTRYCOPY_QUIT_POINT.getValue());
		// RelivePosition p = JsonUtils.string2Object(result.get(0),
		// RelivePosition.class);
		// if (player.getPosition().getMapId() == p.getMapId()) {
		// World.getInstance().updatePosition(player, p.getX(), p.getY(),
		// player.getHeading());
		// } else {
		// World.getInstance().setPosition(player, p.getMapId(), p.getX(),
		// p.getY(), player.getHeading());
		// }
		// player.sendUpdatePosition();
		copyAIs.clear();
	}

}
