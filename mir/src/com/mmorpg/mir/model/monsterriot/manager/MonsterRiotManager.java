package com.mmorpg.mir.model.monsterriot.manager;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.monsterriot.config.MonsterriotConfig;
import com.mmorpg.mir.model.monsterriot.model.MonsterRiot;
import com.mmorpg.mir.model.monsterriot.packet.SM_MonsterRiot_MapInfo;
import com.mmorpg.mir.model.monsterriot.packet.SM_MonsterRiot_Start;
import com.mmorpg.mir.model.monsterriot.resource.MonsterRiotResource;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;
import com.windforce.common.scheduler.ScheduledTask;
import com.windforce.common.scheduler.impl.SimpleScheduler;

@Component
public class MonsterRiotManager {
	private Map<CountryId, MonsterRiot> monsterRiots;
	
	@Autowired
	private MonsterriotConfig config;
	
	@Autowired
	private SimpleScheduler simpleScheduler;
	
	private static MonsterRiotManager INSTANCE;

	@PostConstruct
	void init() {
		monsterRiots = new HashMap<CountryId, MonsterRiot>();
		// 初始化每个国家的monsterRiot对象
		for (CountryId countryId: CountryId.values()) {
			monsterRiots.put(countryId, MonsterRiot.valueOf(countryId));
		}
		INSTANCE = this;
	}
	
	public static MonsterRiotManager getInstance() {
		return INSTANCE;
	}
	
	public void initAll() {
		// 定时刷怪
		for (final MonsterRiotResource resource: config.monsterRiotResources.getAll()) {
			simpleScheduler.schedule(new ScheduledTask() {
				
				@Override
				public void run() {
					monsterRiots.get(CountryId.valueOf(resource.getCountry())).start(resource.getRound());
				}
				
				@Override
				public String getName() {
					return "怪物攻城:" + resource.getId();
				}
			}, resource.getStartCron());
		}
		
		// 每一波超时结束
		for (final MonsterRiotResource resource: config.monsterRiotResources.getAll()) {
			simpleScheduler.schedule(new ScheduledTask() {
				
				@Override
				public void run() {
					monsterRiots.get(CountryId.valueOf(resource.getCountry())).currentRoundEnd();
				}
						
				@Override
				public String getName() {
					return "怪物攻城:" + resource.getId() + "超时结束";
				}
			}, resource.getDeleteCron());
		}
	
		simpleScheduler.schedule(new ScheduledTask() {
			
			@Override
			public void run() {
				SessionManager.getInstance().sendAllIdentified(new SM_MonsterRiot_Start());
				I18nUtils utils = I18nUtils.valueOf("406001");
				ChatManager.getInstance().sendSystem(7100184, utils, null);
				I18nUtils utils2 = I18nUtils.valueOf("306200");
				for (Country country: CountryManager.getInstance().getCountries().values()) {
					ChatManager.getInstance().sendSystem(6, utils2, null, country);
				}
			}
			
			@Override
			public String getName() {
				return "怪物攻城活动开始";
			}
		}, config.ACT_START_CRONTIME.getValue());
		
		simpleScheduler.schedule(new ScheduledTask() {
			
			@Override
			public void run() {
				I18nUtils utils = I18nUtils.valueOf("601006");
				ChatManager.getInstance().sendSystem(6100184, utils, null);
			}
			
			@Override
			public String getName() {
				return "提前5分钟的活动开始公告";
			}
		}, config.GAMESTART_BEFORE_NOTICE.getValue());
		
		// 超时结束
		simpleScheduler.schedule(new ScheduledTask() {
			
			@Override
			public void run() {
				for (MonsterRiot riot: monsterRiots.values()) {
					riot.end();
				}
			}
			
			@Override
			public String getName() {
				return "怪物攻城时间到了结束";
			}
		}, config.ACT_END_CRONTIME.getValue());
		
	}
	
	public void queryMapMonsterStatus(Player player, int mapId) {
		if (config.getPeriodConditions().verify(null, true)) {
			WorldMap map = World.getInstance().getWorldMap(mapId);
			MonsterRiot riot = monsterRiots.get(player.getCountryId());
			SM_MonsterRiot_MapInfo sm = riot.getMapStatusPack(player, map);
			PacketSendUtility.sendPacket(player, sm);
		}
	}
	
	public boolean isInAct(CountryId id) {
		return monsterRiots.get(id).isInAct();
	}

	public void triggerEnd(CountryId id) {
		MonsterRiot riot = monsterRiots.get(id); 
		boolean isRoundEnd = riot.checkRoundEnd();
		if (isRoundEnd) {
			if (config.isLastRound(id.getValue(), riot.getCurrentRound())) {
				riot.end();
			} else {
				riot.currentRoundEnd();
			}
		}
	}
	
	public void monsterKill(Player attacker, CountryId id, long damage) {
		MonsterRiot riot = monsterRiots.get(id);
		riot.monsterKill(attacker, damage);
	}
	
}
