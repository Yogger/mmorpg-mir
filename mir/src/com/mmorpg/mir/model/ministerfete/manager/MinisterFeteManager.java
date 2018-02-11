package com.mmorpg.mir.model.ministerfete.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.countrycopy.model.vo.TechCopyRankVO;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gascopy.config.GasCopyMapConfig;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.ministerfete.config.MinisterFeteConfig;
import com.mmorpg.mir.model.ministerfete.model.MinisterFete;
import com.mmorpg.mir.model.ministerfete.packet.SM_Enter_Minister;
import com.mmorpg.mir.model.ministerfete.packet.SM_Leave_Minister;
import com.mmorpg.mir.model.ministerfete.packet.SM_Minister_Dice;
import com.mmorpg.mir.model.ministerfete.packet.SM_Minister_Ranks;
import com.mmorpg.mir.model.ministerfete.packet.SM_Minister_Status;
import com.mmorpg.mir.model.object.route.RouteStep;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.windforce.common.scheduler.ScheduledTask;
import com.windforce.common.scheduler.impl.SimpleScheduler;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;

@Component
public class MinisterFeteManager {

	@Autowired
	private World world;

	@Autowired
	private MinisterFeteConfig config;

	@Autowired
	private SimpleScheduler simpleScheduler;

	private MinisterFete ministerFete;

	private static MinisterFeteManager INSTANCE;

	public static MinisterFeteManager getInstance() {
		return INSTANCE;
	}
	
	public void initAll() {
		WorldMapInstance instance = world.getWorldMap(config.MAPID.getValue()).createNewInstance(1);
		ministerFete = new MinisterFete(instance);
		Date now = new Date();
		for (int i = 0; i < config.OPEN_TIME.getValue().length; i++) {
			Date start = DateUtils.getNextTime(config.OPEN_TIME.getValue()[i], DateUtils.getFirstTime(now));
			Date end = DateUtils.getNextTime(config.END_TIME.getValue()[i], DateUtils.getFirstTime(now));
			if (now.getTime() >= start.getTime() && now.getTime() < end.getTime()) {
				ministerFete.start();
				break;
			}
		}
		for (String cronTime : config.OPEN_TIME.getValue()) {
			simpleScheduler.schedule(new ScheduledTask() {

				@Override
				public void run() {
					ministerFete.start();
				}

				@Override
				public String getName() {
					return "相国祭天开始";
				}
			}, cronTime);
		}
		for (String cronTime : config.END_TIME.getValue()) {
			simpleScheduler.schedule(new ScheduledTask() {

				@Override
				public void run() {
					ministerFete.end(null);
				}

				@Override
				public String getName() {
					return "相国祭天结束";
				}
			}, cronTime);
		}
		simpleScheduler.schedule(new ScheduledTask() {

			@Override
			public void run() {
				I18nUtils utils = I18nUtils.valueOf("601013");
				ChatManager.getInstance().sendSystem(61001, utils, null);
			}

			@Override
			public String getName() {
				return "相国祭天前5分钟公告";
			}
		}, config.NOTICE_TIME.getValue());
	}

	@PostConstruct
	void init() {
		INSTANCE = this;
	}

	public void enterMinisterAct(Player player) {
		if (!config.getEnterCondition().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (player.isInCopy() || GasCopyMapConfig.getInstance().isInGasCopyMap(player.getMapId())) {
			throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
		}
		if (!ministerFete.isWarring()) {
			return;
		}
		int mapId = config.MAPID.getValue();
		player.getCopyHistory().setRouteStep(RouteStep.valueOf(player.getMapId(), player.getX(), player.getY()));
		Integer[] bornPosition = config.BORN_POSITION.getValue();
		World.getInstance().setPosition(player, mapId, 1, bornPosition[0], bornPosition[1], player.getHeading());
		player.sendUpdatePosition();
		PacketSendUtility.sendPacket(player, SM_Enter_Minister.valueOf(ministerFete.getNextSkillTime()));
		PacketSendUtility.sendPacket(player, SM_Minister_Status.valueOf(ministerFete.getActivityNpc()));
	}

	public void leaveMinisterAct(Player player) {
		if (player.getMapId() != config.MAPID.getValue()) {
			return;
		}
		player.getMoveController().stopMoving(); // 停下来
		RouteStep position = player.getCopyHistory().getRouteStep();
		if (position != null) {
			World.getInstance().setPosition(player, position.getMapId(), position.getX(), position.getY(),
					player.getHeading());
		} else {
			// 防止出错让他回新手村
			List<String> result = ChooserManager.getInstance().chooseValueByRequire(player.getCountryValue(),
					ConfigValueManager.getInstance().BIRTH_POINT.getValue());
			RelivePosition p = JsonUtils.string2Object(result.get(0), RelivePosition.class);
			World.getInstance().setPosition(player, p.getMapId(), p.getX(), p.getY(), (byte) 0);
		}
		player.sendUpdatePosition();
		PacketSendUtility.sendPacket(player, new SM_Leave_Minister());
	}

	public void getCountryTechCopyRanks(Player player) {
		if (ministerFete.isWarring()) {
			return;
		}

		ArrayList<TechCopyRankVO> ranks = new ArrayList<TechCopyRankVO>();
		for (TechCopyRankVO vo : ministerFete.getRewardRanks().values()) {
			if (vo.getRank() > 100) {
				continue;
			}
			ranks.add(vo);
		}
		Collections.sort(ranks);
		PacketSendUtility.sendPacket(player, SM_Minister_Ranks.valueOf(ranks));
	}

	public void throwDice(Player player, int hpPercent) {
		PacketSendUtility.sendPacket(player,
				SM_Minister_Dice.valueOf(ministerFete.throwDicePoints(hpPercent, player.getObjectId()), hpPercent));
	}

	public boolean isInMinister() {
		return ministerFete.isWarring();
	}

	public void die(Creature creature) {
		ministerFete.die(creature);
	}
	
}
