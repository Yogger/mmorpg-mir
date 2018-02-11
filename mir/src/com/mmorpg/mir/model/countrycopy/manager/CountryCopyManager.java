package com.mmorpg.mir.model.countrycopy.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.core.condition.BetweenCronTimeCondition;
import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.countrycopy.config.CountryCopyConfig;
import com.mmorpg.mir.model.countrycopy.model.CountryCopy;
import com.mmorpg.mir.model.countrycopy.model.TechnologyCopy;
import com.mmorpg.mir.model.countrycopy.model.vo.TechCopyRankVO;
import com.mmorpg.mir.model.countrycopy.packet.SM_CountryCopy_Status;
import com.mmorpg.mir.model.countrycopy.packet.SM_Get_Encourage_Info;
import com.mmorpg.mir.model.countrycopy.packet.SM_Get_Encourage_List;
import com.mmorpg.mir.model.countrycopy.packet.SM_TechCopy_Monster_Spawn;
import com.mmorpg.mir.model.countrycopy.packet.SM_Tech_Copy_Ranks;
import com.mmorpg.mir.model.countrycopy.packet.vo.CountryCopyVO;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.object.route.RouteStep;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.service.MapInstanceService;
import com.windforce.common.scheduler.ScheduledTask;
import com.windforce.common.scheduler.impl.SimpleScheduler;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;

@Component
public class CountryCopyManager {
	private Map<CountryId, CountryCopy> countryCopys = New.hashMap();
	
	private Map<CountryId, TechnologyCopy> technologyCopys = New.hashMap();

	@Autowired
	private CountryCopyConfig countryOfWarConfig;

	@Autowired
	private SpawnManager spawnManager;
	
	@Autowired
	private SimpleScheduler simpleScheduler;

	private static CountryCopyManager instance;

	@PostConstruct
	public void init() {
		instance = this;
	}

	/**
	 * TODO 在debug和start类启动的时候调用
	 */
	public void initAll() {
		for (CountryId countryId : CountryId.values()) {
			// 构建地图
			CountryCopy countryCopy = new CountryCopy(countryId, spawnManager);
			WorldMapInstance worldMapInstance = MapInstanceService.createCountryCopyMap(
					countryOfWarConfig.MAPID.getValue(), countryId.getValue(), countryCopy);
			countryCopy.setWorldMapInstance(worldMapInstance);
			countryCopys.put(countryId, countryCopy);
			
			// 构建墨家傀儡的地图
			TechnologyCopy technologyCopy = new TechnologyCopy(countryId);
			WorldMap worldMap = World.getInstance().getWorldMap(countryOfWarConfig.TECH_COPY_MAP_ID.getValue());
			WorldMapInstance technologyCopyInstance = worldMap.createNewInstance(countryId.getValue());
			technologyCopy.setWorldMapInstance(technologyCopyInstance);
			technologyCopys.put(countryId, technologyCopy);
		}
		// 墨家傀儡的副本
		startCopyTask();
	}
	
	private void startCopyTask() {
		simpleScheduler.schedule(new ScheduledTask() {
			
			@Override
			public void run() {
				for (Country country : CountryManager.getInstance().getCountries().values()) {
					if (CountryCopyConfig.getInstance().getTechCopyStartCond().verify(country)) {
						I18nUtils utils = I18nUtils.valueOf("601008");
						ChatManager.getInstance().sendSystem(61003, utils, null, country);
					}
				}
			}
			
			@Override
			public String getName() {
				return "墨家傀儡前5分钟公告";
			}
		}, countryOfWarConfig.TECH_START_NOTICE_CRON_TIME.getValue());
		
		simpleScheduler.schedule(new ScheduledTask() {
			
			@Override
			public void run() {
				for (TechnologyCopy techCopy : technologyCopys.values()) {
					techCopy.start();
				}
			}
			
			@Override
			public String getName() {
				return "墨家傀儡定点开始";
			}
		}, countryOfWarConfig.TECH_START_CRON_TIME.getValue());
		
		simpleScheduler.schedule(new ScheduledTask() {
			
			@Override
			public void run() {
				/*if (!countryOfWarConfig.getTechCopyStartCond().verify(null)) {
					return;
				}*/
				for (TechnologyCopy techCopy : technologyCopys.values()) {
					techCopy.end(null);
				}
			}
			
			@Override
			public String getName() {
				return "墨家傀儡定点结束";
			}
		}, countryOfWarConfig.TECH_END_CRON_TIME.getValue());
	}

	public static CountryCopyManager getInstance() {
		return instance;
	}

	public void enrolle(Player player, int sign) {
		CountryCopy copy = countryCopys.get(player.getCountryId());
		copy.enroll(player);
		PacketSendUtility.sendPacket(player, SM_Get_Encourage_Info.valueOf(sign, copy, player));
	}

	/**
	 * 取消报名
	 * 
	 * @param player
	 */
	public void unEnrolle(Player player, int sign) {
		CountryCopy copy = countryCopys.get(player.getCountryId());
		copy.unEnrolle(player, true);
		PacketSendUtility.sendPacket(player, SM_Get_Encourage_Info.valueOf(sign, copy, player));
	}

	public void unEnrolleBySystem(Player player) {
		CountryCopy copy = countryCopys.get(player.getCountryId());
		copy.unEnrolle(player, false);
	}

	public void encourage(Player player) {
		countryCopys.get(player.getCountryId()).encourage(player);
	}

	public boolean isWarring(Player player) {
		return countryCopys.get(player.getCountryId()).isWarring();
	}
	
	public boolean isTechCopyWarring(Player player) {
		return technologyCopys.get(player.getCountryId()).isWarring();
	}

	public void enter(Player player) {
		if (player.isInCopy() || !CountryCopyConfig.getInstance().getEnterCountryCopyConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
		}
		countryCopys.get(player.getCountryId()).enter(player);
	}

	public void quit(Player player) {
		countryCopys.get(player.getCountryId()).quit(player);
	}

	public void getEncourageNameList(Player player) {
		PacketSendUtility.sendPacket(player, SM_Get_Encourage_List.valueOf(countryCopys.get(player.getCountryId())));
	}

	public void applyEncourage(Player player, int sign) {
		CountryCopy copy = countryCopys.get(player.getCountryId());
		/*if (copy.isWarring()) {
			throw new ManagedException(ManagedErrorCode.COUNTRYCOPY_WARRING);
		}*/
		
		if (System.currentTimeMillis() - player.getCountryCopyInfo().getLastApplyEncourageTime() <= CountryCopyConfig
				.getInstance().APPLY_ENCOURAGE_CD.getValue() * DateUtils.MILLIS_PER_SECOND) {
			throw new ManagedException(ManagedErrorCode.APPLY_ENCOURAGE_CD);
		}
		copy.sendEncourageChat(player, true);
		player.getCountryCopyInfo().setLastApplyEncourageTime(System.currentTimeMillis());
		PacketSendUtility.sendPacket(player, SM_Get_Encourage_Info.valueOf(sign, copy, player));
	}

	public void getEncourageInfo(Player player, int sign) {
		CountryCopy copy = countryCopys.get(player.getCountryId());
		PacketSendUtility.sendPacket(player, SM_Get_Encourage_Info.valueOf(sign, copy, player));
	}

	public CountryCopyVO createPlayerCountryCopyVO(Player player) {
		CountryCopyVO vo = new CountryCopyVO();
		vo.setLeftCount(player.getCountryCopyInfo().getLeftCount());
		CountryCopy copy = countryCopys.get(player.getCountryId());
		vo.setCopyStartTime(copy.getStartTime());
		vo.setLastEnterTime(player.getCountryCopyInfo().getLastEnterTime());
		return vo;
	}

	public void getCountryCopyStatus(Player player) {
		if (player.getMapId() != CountryCopyConfig.getInstance().MAPID.getValue()) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		CountryCopy copy = countryCopys.get(player.getCountryId());
		if (!copy.isWarring()) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COUNTRYCOPY_NOT_WARRING);
			return;
		}
		SM_CountryCopy_Status sm = copy.createBossPack();
		if (sm == null) {
			return;
		}
		PacketSendUtility.sendPacket(player, sm);
	}

	public void enterCountryTechCopy(Player player) {
		if (!countryOfWarConfig.getTechCopyEnterCond().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		
		if (player.isInCopy()) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.PLAYER_IN_COPY);
			return;
		}
		
		TechnologyCopy copy = technologyCopys.get(player.getCountryId());
		if (!copy.isWarring()) {
			return;
		}
		
		player.getMoveController().stopMoving();
		
		int mapId = CountryCopyConfig.getInstance().TECH_COPY_MAP_ID.getValue();
		player.getCopyHistory().setRouteStep(RouteStep.valueOf(player.getMapId(), player.getX(), player.getY()));
		Integer[] bornPosition = null;
		bornPosition = CountryCopyConfig.getInstance().TECH_BORN_POSITION.getValue();
		World.getInstance().setPosition(player, mapId, player.getCountryValue(), bornPosition[0], bornPosition[1],
				player.getHeading());
		player.sendUpdatePosition();
		
		PacketSendUtility.sendPacket(player, SM_TechCopy_Monster_Spawn.valueOf(copy.getNextMonsterSpawnTime(), copy.getNextSpawnKey()));
	}

	public void leaveCountryTechCopy(Player player) {
		if (player.getMapId() != CountryCopyConfig.getInstance().TECH_COPY_MAP_ID.getValue()) {
			return;
		}
		removeTechCopySpeicialBuff(player);
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
	}

	public void getCountryTechCopyRanks(Player player) {
		if (!countryOfWarConfig.getTechCopyStartCond().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		
		TechnologyCopy copy = technologyCopys.get(player.getCountryId());
		if (copy.isWarring()) {
			return;
		}
		
		ArrayList<TechCopyRankVO> ranks = new ArrayList<TechCopyRankVO>();
		for (TechCopyRankVO vo : copy.getRewardRanks().values()) {
			if (vo.getRank() > 100) {
				continue;
			}
			ranks.add(vo);
		}
		Collections.sort(ranks);
		PacketSendUtility.sendPacket(player, SM_Tech_Copy_Ranks.valueOf(ranks));
	}

	public void startCountryTechCopy(CountryId countryId) {
		TechnologyCopy copy = technologyCopys.get(countryId);
		if (copy.isWarring() || DateUtils.isToday(new Date(copy.getEndTime()))) {
			return;
		}
		BetweenCronTimeCondition condition = CoreConditionType.createBetweenCronTimeCondition(countryOfWarConfig.TECH_START_CRON_TIME.getValue(), 
				countryOfWarConfig.TECH_END_CRON_TIME.getValue());
		if (!condition.verify(null)) {
			return;
		}
		copy.start();
	}
	
	public void removeTechCopySpeicialBuff(Player player) {
		for (Integer skillId : CountryCopyConfig.getInstance().KILL_MONSTER_GAIN_SKILL.getValue().values()) {
			player.getEffectController().removeEffect(skillId);
		}
	}
}
