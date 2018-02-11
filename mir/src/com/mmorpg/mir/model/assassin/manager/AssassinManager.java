package com.mmorpg.mir.model.assassin.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.assassin.config.AssassinConfig;
import com.mmorpg.mir.model.assassin.model.AssassinEmperor;
import com.mmorpg.mir.model.assassin.packet.SM_Assassin_Dice;
import com.mmorpg.mir.model.assassin.packet.SM_Assassin_Ranks;
import com.mmorpg.mir.model.assassin.packet.SM_Assassination_Status;
import com.mmorpg.mir.model.assassin.packet.SM_Enter_Assassin;
import com.mmorpg.mir.model.assassin.packet.SM_Leave_Assassin;
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
import com.mmorpg.mir.model.object.route.RouteStep;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.windforce.common.scheduler.ScheduledTask;
import com.windforce.common.scheduler.impl.SimpleScheduler;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;

@Component
public class AssassinManager {

	@Autowired
	private World world;

	@Autowired
	private AssassinConfig config;

	@Autowired
	private SimpleScheduler simpleScheduler;

	private AssassinEmperor assassinEmperor;

	private static AssassinManager INSTANCE;

	public static AssassinManager getInstance() {
		return INSTANCE;
	}
	
	public void initAll() {
		WorldMapInstance instance = world.getWorldMap(config.MAPID.getValue()).createNewInstance(1);
		assassinEmperor = new AssassinEmperor(instance);
		Date now = new Date();
		for (int i = 0; i < config.OPEN_TIME.getValue().length; i++) {
			Date start = DateUtils.getNextTime(config.OPEN_TIME.getValue()[i], DateUtils.getFirstTime(now));
			Date end = DateUtils.getNextTime(config.END_TIME.getValue()[i], DateUtils.getFirstTime(now));
			if (now.getTime() >= start.getTime() && now.getTime() < end.getTime()) {
				assassinEmperor.start();
				break;
			}
		}
		for (String cronTime : config.OPEN_TIME.getValue()) {
			simpleScheduler.schedule(new ScheduledTask() {

				@Override
				public void run() {
					assassinEmperor.start();
				}

				@Override
				public String getName() {
					return "荆轲刺秦开始";
				}
			}, cronTime);
		}
		simpleScheduler.schedule(new ScheduledTask() {

			@Override
			public void run() {
				I18nUtils utils = I18nUtils.valueOf("601012");
				ChatManager.getInstance().sendSystem(61001, utils, null);
			}

			@Override
			public String getName() {
				return "荆轲前5分钟公告";
			}
		}, config.NOTICE_TIME.getValue());
	}

	@PostConstruct
	void init() {
		INSTANCE = this;
	}

	public void enterAssassinAct(Player player) {
		if (!config.getEnterCondition().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (player.isInCopy() || GasCopyMapConfig.getInstance().isInGasCopyMap(player.getMapId())) {
			throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
		}
		if (!assassinEmperor.isWarring()) {
			return;
		}
		int mapId = config.MAPID.getValue();
		player.getCopyHistory().setRouteStep(RouteStep.valueOf(player.getMapId(), player.getX(), player.getY()));
		Integer[] bornPosition = config.BORN_POSITION.getValue();
		World.getInstance().setPosition(player, mapId, 1, bornPosition[0], bornPosition[1], player.getHeading());
		player.sendUpdatePosition();
		PacketSendUtility.sendPacket(player, SM_Enter_Assassin.valueOf(assassinEmperor.getNextSkillTime()));
		PacketSendUtility.sendPacket(player, SM_Assassination_Status.valueOf(assassinEmperor.getActivityNpc()));
	}

	public void leaveAssassinAct(Player player) {
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
		PacketSendUtility.sendPacket(player, new SM_Leave_Assassin());
	}

	public void getCountryTechCopyRanks(Player player) {
		if (assassinEmperor.isWarring()) {
			return;
		}

		ArrayList<TechCopyRankVO> ranks = new ArrayList<TechCopyRankVO>();
		for (TechCopyRankVO vo : assassinEmperor.getRewardRanks().values()) {
			if (vo.getRank() > 100) {
				continue;
			}
			ranks.add(vo);
		}
		Collections.sort(ranks);
		PacketSendUtility.sendPacket(player, SM_Assassin_Ranks.valueOf(ranks));
	}

	public void throwDice(Player player, int hpPercent) {
		PacketSendUtility.sendPacket(player,
				SM_Assassin_Dice.valueOf(assassinEmperor.throwDicePoints(hpPercent, player.getObjectId()), hpPercent));
	}

	public boolean isInAssassin() {
		return assassinEmperor.isWarring();
	}

	public void die(Creature creature) {
		assassinEmperor.die(creature);
	}
	
}
