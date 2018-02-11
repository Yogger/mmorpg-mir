package com.mmorpg.mir.model.gangofwar.manager;

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
import com.mmorpg.mir.model.controllers.PlayerController;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.core.condition.BetweenCronTimeCondition;
import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.formula.Formula;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gangofwar.config.GangOfWarConfig;
import com.mmorpg.mir.model.gangofwar.controller.GangOfWarPlayerController;
import com.mmorpg.mir.model.gangofwar.controller.ReliveGodStatusNpcController;
import com.mmorpg.mir.model.gangofwar.model.PlayerGangWarInfo;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.object.route.RouteStep;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;

@Component
public class GangOfWarManager implements IGangOfWarManager {
	private Map<CountryId, GangOfWar> gangOfwars = New.hashMap();

	@Autowired
	private GangOfWarConfig gangOfWarConfig;

	@Static("experience_area")
	public Formula experience_area;

	@Autowired
	private SpawnManager spawnManager;

	private static GangOfWarManager instance;

	@PostConstruct
	public void init() {
		instance = this;
		for (CountryId countryId : CountryId.values()) {
			getGangOfwars().put(countryId, new GangOfWar(countryId, gangOfWarConfig, spawnManager));
		}
	}

	public void leaveWar(Player player) {
		if (player.getMapId() != GangOfWarConfig.getInstance().MAPID.getValue()) {
			return;
		}
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

	public boolean canOpenWar(Country country, Date now) {
		// 验证是否满足开启的条件
		Date openTime = ServerState.getInstance().getOpenServerDate();
		// 未开服
		if (openTime == null) {
			return false;
		}
		// 开服第一天
		if (DateUtils.isSameDay(now, openTime)) {
			return false;
		}

		if (DateUtils.isSameDay(now, (new Date(openTime.getTime() + DateUtils.MILLIS_PER_DAY)))) {
			// 开服第二天
			return true;
		} else {
			CoreConditions coreConditions = new CoreConditions();
			BetweenCronTimeCondition bctc = CoreConditionType.createBetweenCronTimeCondition("0 0 0 * * FRI",
					"0 0 0 * * SAT");
			bctc.setTime(now.getTime());
			coreConditions.addConditions(bctc);
			if (coreConditions.verify(null, false)) {
				// 周5
				Date lastMergeTime = ServerState.getInstance().getServerEnt().getLastMergeTime();
				if (lastMergeTime != null
						&& DateUtils.calcIntervalDays(lastMergeTime, now) < gangOfWarConfig.MERGE_DATE_NOT_OPEN
								.getValue()) {
					return false;
				}
				if (DateUtils.calcIntervalDays(openTime, now) != 2) {
					return true;
				}
			} else {
				if (country.getKing() == null) {
					Date lastMergeTime = ServerState.getInstance().getServerEnt().getLastMergeTime();
					if (lastMergeTime != null
							&& DateUtils.calcIntervalDays(lastMergeTime, now) != gangOfWarConfig.MERGE_DATE_NOT_OPEN
									.getValue()) {
						// if (DateUtils.calcIntervalDays(new
						// Date(country.getCourt().getBecomeKingTime()), now) >
						// 0) {
						// return true;
						// }
						return false;
					}
					return true;
				}
			}
		}
		return false;
	}

	public void openWar(boolean onlySendSystem) {
		for (Country country : CountryManager.getInstance().getCountries().values()) {
			if (canOpenWar(country, new Date())) {
				if (onlySendSystem) {
					I18nUtils i18nUtils = I18nUtils.valueOf("601005");
					ChatManager.getInstance().sendSystem(61002, i18nUtils, null, country);
				} else {
					start(country.getId().getValue(), true);
				}
			}
		}
	}

	/**
	 * 构建玩家切换地图的观察者
	 * 
	 * @param player
	 * @return
	 */
	public ActionObserver gangOfWarSpawnObsever(final Player player) {
		ActionObserver observer = new ActionObserver(ObserverType.SPAWN) {
			@Override
			public void spawn(int mapId, int instanceId) {
				// 切换地图处理
				if (mapId == GangOfWarConfig.getInstance().MAPID.getValue()) {
					GangOfWar gangOfWar = GangOfWarManager.getInstance().getGangOfwars().get(player.getCountryId());
					if (player.getGang() == null) {
						// 乱飞进来的，直接弹出去
						GangOfWarManager.getInstance().leaveWar(player);
						PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.GANG_NOT_JOIN);
					} else if (gangOfWar.isWarring()) {
						// 进入地图
						gangOfWar.playerJoin(player);
					}

				} else {
					// 离开地图
					if (player.getController() instanceof GangOfWarPlayerController) {
						player.getEffectController().removeEffect(ReliveGodStatusNpcController.GOD_SKILL_ID);
						player.getEffectController().unsetAbnormal(EffectId.GOD.getEffectId(), true);
						player.setController(new PlayerController());
						player.getController().setOwner(player);
					}
				}
			}
		};
		player.getObserveController().addObserver(observer);
		return observer;
	}

	public void backhome(Player player) {
		if (!(player.getController() instanceof GangOfWarPlayerController)) {
			return;
		}
		GangOfWar gow = gangOfwars.get(player.getCountryId());
		if (!gow.isWarring()) {
			return;
		}
		PlayerGangWarInfo pgi = gow.getPlayerMap().get(player.getObjectId());
		if (pgi == null) {
			return;
		}
		if ((System.currentTimeMillis() - pgi.getLastBackHomeTime()) < GangOfWarConfig.getInstance().BACKHOME_CD
				.getValue()) {
			throw new ManagedException(ManagedErrorCode.GANGOFWAR_BACKHOME_CD);
		}
		pgi.setLastBackHomeTime(System.currentTimeMillis());
		gow.setRightPosition(player);
		pgi.sendUpdate();
	}

	public void enterWar(Player player) {
		gangOfwars.get(player.getCountryId()).enterWar(player);
	}

	public void start(int countryId, boolean mergeFirst) {
		gangOfwars.get(CountryId.valueOf(countryId)).start(mergeFirst);
	}

	public static GangOfWarManager getInstance() {
		return instance;
	}

	public Map<CountryId, GangOfWar> getGangOfwars() {
		return gangOfwars;
	}

	public GangOfWar getGangOfWars(Player player) {
		return getGangOfwars().get(player.getCountryId());
	}

}
