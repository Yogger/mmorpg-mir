package com.mmorpg.mir.model.kingofwar.controller;

import java.util.List;
import java.util.concurrent.Future;

import org.h2.util.New;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.controllers.StatusNpcController;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.gameobjects.CountryNpc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.StatusNpc;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.kingofwar.config.KingOfWarConfig;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.kingofwar.packet.vo.ReliveStatusVO;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.ThreadPoolManager;

/**
 * 复活点状态NPC
 * 
 * @author Kuang Hao
 * @since v1.0 2014-11-6
 * 
 */
public class KingOfWarStatusNpcController extends StatusNpcController {

	private List<CountryNpc> countryNpcs = New.arrayList();

	/** 发放积分任务 */
	private Future<?> pointsFuture;

	public static final Object lock = new Object();

	public void setStatus(int country) {
		stopPointsFuture();
		StatusNpc npc = (StatusNpc) getOwner();
		StatusNpc old = null;
		if (country != 0) {
			for (StatusNpc statusNpc : KingOfWarManager.getInstance().getStatusNpcs().values()) {
				if (statusNpc != getOwner() && statusNpc.getStatus() == country) {
					old = statusNpc;
					break;
				}
			}
		}
		if (old != null) {
			((KingOfWarStatusNpcController) old.getController()).setStatus(0);
		} else {
			// 新开复活点,需要添加复活点BUFF
			if (country != 0) {
				for (Player player : KingOfWarManager.getInstance().getPlayerOnWar(country)) {
					KingOfWarManager.getInstance().addReliveBuff(player);
				}
			}
		}

		if (npc.getStatus() != 0 && country != 0) {
			// 移除被占领玩家身上的BUFF
			for (Player player : KingOfWarManager.getInstance().getPlayerOnWar(npc.getStatus())) {
				KingOfWarManager.getInstance().removeReliveBuff(player);
			}
		}
		npc.setStatus(country);

		// 消除老的卫兵
		for (CountryNpc countryNpc : countryNpcs) {
			countryNpc.getController().delete();
		}

		if (country != 0) {
			startPointsFuture();
			String[] countryNpcSpawns = getCountryNpc();
			for (String spawnKey : countryNpcSpawns) {
				// 生成新的卫兵
				CountryNpc countryNpc = (CountryNpc) SpawnManager.getInstance().creatObject(spawnKey, 1,
						new Integer(country));
				countryNpcs.add(countryNpc);
				KingOfWarManager.getInstance().getVisibleObjects().add(countryNpc);
				SpawnManager.getInstance().bringIntoWorld(countryNpc, 1);
			}
			getOwner().getDurations().clear();

			// 通报
			String countryName = CountryManager.getInstance().getCountries().get(CountryId.valueOf(country)).getName();
			I18nUtils i18nUtils1 = I18nUtils.valueOf("202003");
			i18nUtils1.addParm("country", I18nPack.valueOf(countryName));
			i18nUtils1.addParm("relive", I18nPack.valueOf(getOwner().getObjectResource().getName()));
			ChatManager.getInstance().sendSystem(41004, i18nUtils1, null,
					Integer.valueOf(KingOfWarConfig.getInstance().MAPID.getValue()));
			
			// 通报
//			I18nUtils i18nUtils = I18nUtils.valueOf("402004");
//			i18nUtils.addParm("country", I18nPack.valueOf(countryName));
//			i18nUtils.addParm("relive", I18nPack.valueOf(getOwner().getObjectResource().getName()));
//			ChatManager.getInstance().sendSystem(71001, i18nUtils, null);

//			I18nUtils i18nUtils1 = I18nUtils.valueOf("10109");
//			i18nUtils1.addParm("country", I18nPack.valueOf(countryName));
//			i18nUtils1.addParm("relive", I18nPack.valueOf(getOwner().getObjectResource().getName()));
//			ChatManager.getInstance().sendSystem(11001, i18nUtils1, null);
		}

	}

	public void stopPointsFuture() {
		if (pointsFuture != null && !pointsFuture.isCancelled()) {
			pointsFuture.cancel(true);
		}
	}

	public void startPointsFuture() {
		pointsFuture = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(
				new Runnable() {
					@Override
					public void run() {
						for (Player player : KingOfWarManager.getInstance().getPlayerOnWar(getOwner().getStatus())) {
							KingOfWarManager.getInstance().getPlayerWarInfos().get(player.getObjectId())
									.increasePoints(KingOfWarConfig.getInstance().RELIVE_POINTS.getValue());
						}
					}
				}, KingOfWarConfig.getInstance().RELIVE_POINTS_PERIOD.getValue(),
				KingOfWarConfig.getInstance().RELIVE_POINTS_PERIOD.getValue());
	}

	@Override
	public void playerChangeStatus(Player player) {
		synchronized (lock) {
			if (getOwner().getSpawn().getStatusDuration() != 0) {
				if (!getOwner().getDurations().containsKey(player.getObjectId())
						|| getOwner().getDurations().get(player.getObjectId()) > System.currentTimeMillis()) {
					throw new ManagedException(ManagedErrorCode.STATUS_NPC_NO_DURATION);
				}
			}
			if (getOwner().getStatus() == player.getCountryValue()) {
				return;
			}
			setStatus(player.getCountryValue());
			// 通知
			KingOfWarManager.getInstance().sendPackOnWar(
					ReliveStatusVO.valueOf(KingOfWarManager.getInstance().getStatusNpcs()));
		}
	}

	@Override
	public void onDespawn() {
		stopPointsFuture();
	}

	/**
	 * 获取对应的守卫
	 * 
	 * @return
	 */
	private String[] getCountryNpc() {
		if (getOwner().getSpawnKey().equals(KingOfWarConfig.getInstance().CENTER_STATUSNPC_SPAW.getValue())) {
			return KingOfWarConfig.getInstance().CENTER_COUNTRYNPC_SPAW.getValue();
		}
		if (getOwner().getSpawnKey().equals(KingOfWarConfig.getInstance().LEFT_STATUSNPC_SPAW.getValue())) {
			return KingOfWarConfig.getInstance().LEFT_COUNTRYNPC_SPAW.getValue();
		}
		if (getOwner().getSpawnKey().equals(KingOfWarConfig.getInstance().RIGHT_STATUSNPC_SPAW.getValue())) {
			return KingOfWarConfig.getInstance().RIGHT_COUNTRYNPC_SPAW.getValue();
		}

		throw new RuntimeException(String.format("statuNpc[%s]没有找到对应的守卫", getOwner().getSpawnKey()));
	}
}
