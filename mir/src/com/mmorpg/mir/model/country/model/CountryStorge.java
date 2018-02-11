package com.mmorpg.mir.model.country.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.country.model.log.CountryLogEnum;
import com.mmorpg.mir.model.country.model.log.CountryLogValue;
import com.mmorpg.mir.model.country.model.log.CountryLogger;
import com.mmorpg.mir.model.country.packet.SM_Country_Action_Push_Now_Log;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.AbstractItem;
import com.mmorpg.mir.model.item.storage.ItemStorage;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.DateUtils;

public class CountryStorge {
	/** 国家仓库 */
	private ItemStorage storge;

	// TODO 日志 3天
	private Map<Integer, CountryLogger> logMap = New.hashMap();

	public static CountryStorge valueOf() {
		CountryStorge countryStorge = new CountryStorge();
		countryStorge.setStorge(ItemStorage.valueOf(
				ConfigValueManager.getInstance().COUNTRY_STORE_INIT_SIZE.getValue(),
				ConfigValueManager.getInstance().COUNTRY_STORE_MAX_SIZE.getValue()));
		return countryStorge;
	}

	/**
	 * 记录日志
	 * 
	 * @param cLog
	 * @see {@link CountryLogEnum}
	 * @param player
	 * @param indexs
	 *            格子
	 * @param sendPacket
	 */
	@JsonIgnore
	public void recordLog(CountryLogEnum cLog, Player player, Set<Integer> indexs, boolean sendPacket) {
		CountryLogger logger = getLogger(cLog);
		if (logger == null) {
			logger = CountryLogger.valueOf();
			logMap.put(cLog.getAction(), logger);
		}
		ArrayList<String> items = New.arrayList();
		AbstractItem item = null;
		for (Iterator<Integer> iterator = indexs.iterator(); iterator.hasNext();) {
			if (cLog == CountryLogEnum.TAKE) {
				item = player.getCountry().getCountryStorge().getStorge().getItemByIndex(iterator.next());
			} else if (cLog == CountryLogEnum.PUT) {
				item = player.getPack().getItemByIndex(iterator.next());
			}
			if (item != null)
				items.add(item.getKey());
		}
		logger.record(player.getName(), items, System.currentTimeMillis(), item.getSize());
		if (sendPacket) {
			PacketSendUtility.sendPacket(player, SM_Country_Action_Push_Now_Log.valueOf(logger.getLastLog()));
		}
		release();
	}

	private void release() {
		String[] conditions = ConfigValueManager.getInstance().STORAGE_LOG_MAX_COUNT.getValue();
		int maxKeepSize = Integer.valueOf(conditions[0]);
		int maxKeepDays = Integer.valueOf(conditions[1]);
		int keyPut = CountryLogEnum.PUT.getAction();
		int keyTake = CountryLogEnum.TAKE.getAction();
		int sizePut = logMap.containsKey(keyPut) ? logMap.get(keyPut).getSize() : 0;
		int sizeTake = logMap.containsKey(keyTake) ? logMap.get(keyTake).getSize() : 0;
		// 删除多余的
		if ((sizePut + sizeTake) > maxKeepSize) {
			int count = (sizePut + sizeTake) - maxKeepSize;
			for (int i = count; --i >= 0;) {
				CountryLogger putLogger = logMap.get(keyPut);
				CountryLogger takeLogger = logMap.get(keyTake);
				if (putLogger != null && takeLogger != null) {
					if (!putLogger.isEmpty() && !takeLogger.isEmpty()) {
						CountryLogValue putLogValue = putLogger.getFirstLog();
						CountryLogValue takeLogValue = takeLogger.getFirstLog();
						if (putLogValue.getLastTime() < takeLogValue.getLastTime()) {
							putLogger.remove(0);
						} else {
							takeLogger.remove(0);
						}
					} else if (putLogger.isEmpty() && !takeLogger.isEmpty()) {
						takeLogger.remove(0);
					} else if (!putLogger.isEmpty() && takeLogger.isEmpty()) {
						putLogger.remove(0);
					}
				} else if (putLogger != null && takeLogger == null) {
					putLogger.remove(0);
				} else if (putLogger == null && takeLogger != null) {
					takeLogger.remove(0);
				}
			}
		}
		// 删除过期的
		for (Map.Entry<Integer, CountryLogger> entry : logMap.entrySet()) {
			CountryLogger logger = entry.getValue();
			for (int i = logger.getLogValues().size(); --i >= 0;) {
				CountryLogValue logValue = logger.getLogValues().get(i);
				Date startDate = new Date(logValue.getLastTime());
				Date endDate = new Date();
				if (DateUtils.calcIntervalDays(startDate, endDate) > maxKeepDays) {
					logger.remove(i);
				}
			}
		}
	}

	/**
	 * 获取日志
	 * 
	 * @param cLog
	 * @see {@link CountryLogEnum}
	 * @return
	 */
	@JsonIgnore
	public CountryLogger getLogger(CountryLogEnum cLog) {
		return logMap.get(cLog.getAction());
	}

	@JsonIgnore
	public void setSize(int size) {
		this.storge.setSize(size);
	}

	public ItemStorage getStorge() {
		return storge;
	}

	public void setStorge(ItemStorage storge) {
		this.storge = storge;
	}

	public Map<Integer, CountryLogger> getLogMap() {
		return logMap;
	}

	public void setLogMap(Map<Integer, CountryLogger> logMap) {
		this.logMap = logMap;
	}

	public void checkIndexs(Set<Integer> indexs) {
		for (Integer index : indexs) {
			if (storge.getItemByIndex(index) == null) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}
		}
	}

}
