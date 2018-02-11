package com.mmorpg.mir.model.commonactivity.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.commonactivity.CommonActivityConfig;
import com.mmorpg.mir.model.serverstate.ServerState;

public class CommonIdentifyTreasureServer {
	private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String activeName;

	private long startTime;

	private long endTime;

	private int version = 1;

	private LinkedList<IdentifyTreasureLog> bigTreasureLogs;

	private LinkedList<IdentifyTreasureLog> nomalTreasureLogs;

	@Transient
	private final ReentrantReadWriteLock bigLock = new ReentrantReadWriteLock();

	@Transient
	private final ReentrantReadWriteLock nomalLock = new ReentrantReadWriteLock();

	public static CommonIdentifyTreasureServer valueOf(String activeName) {
		CommonIdentifyTreasureServer server = new CommonIdentifyTreasureServer();
		server.bigTreasureLogs = new LinkedList<IdentifyTreasureLog>();
		server.nomalTreasureLogs = new LinkedList<IdentifyTreasureLog>();
		server.activeName = activeName;
		return server;
	}

	/**
	 * 添加日志排名
	 * 
	 * @param log
	 */
	@JsonIgnore
	public void rankIdentifyTreasure(IdentifyTreasureLog log) {
		if (log.isBigTreasure()) {
			try {
				bigLock.writeLock().lock();
				bigTreasureLogs.add(log);
				if (bigTreasureLogs.size() > CommonActivityConfig.getInstance().IDENTIFY_TREASURE_RANK_NUM.getValue()) {
					bigTreasureLogs.removeFirst();
				}
			} finally {
				bigLock.writeLock().unlock();
			}
		} else {
			try {
				nomalLock.writeLock().lock();
				nomalTreasureLogs.add(log);
				if (nomalTreasureLogs.size() > CommonActivityConfig.getInstance().IDENTIFY_TREASURE_RANK_NUM.getValue()) {
					nomalTreasureLogs.removeFirst();
				}
			} finally {
				nomalLock.writeLock().unlock();
			}
		}
	}

	/**
	 * 查询排名
	 * 
	 * @return
	 */
	@JsonIgnore
	public LinkedList<IdentifyTreasureLog> queryIdentifyTreasureRank() {
		LinkedList<IdentifyTreasureLog> big = new LinkedList<IdentifyTreasureLog>();
		LinkedList<IdentifyTreasureLog> nomal = new LinkedList<IdentifyTreasureLog>();
		LinkedList<IdentifyTreasureLog> result = new LinkedList<IdentifyTreasureLog>();
		try {
			bigLock.readLock().lock();
			big.addAll(bigTreasureLogs);
		} finally {
			bigLock.readLock().unlock();
		}
		try {
			nomalLock.readLock().lock();
			nomal.addAll(nomalTreasureLogs);
		} finally {
			nomalLock.readLock().unlock();
		}
		if (big.size() > CommonActivityConfig.getInstance().IDENTIFY_TREASURE_RANK_LITTLE_GIT_TREASURE.getValue()) {
			if (big.size() + nomal.size() > CommonActivityConfig.getInstance().IDENTIFY_TREASURE_RANK_NUM.getValue()) {
				IdentifyTreasureLog bRemove = big.getFirst();
				int index = nomal.size()
						- (CommonActivityConfig.getInstance().IDENTIFY_TREASURE_RANK_NUM.getValue() - big.size()) - 1;
				IdentifyTreasureLog nRemove = nomal.get(index);
				if (bRemove.getTime() > nRemove.getTime()) {
					result.addAll(big);
					result.addAll(nomal.subList(index + 1, nomal.size()));
				} else {
					result.addAll(big.subList(1, big.size()));
					result.addAll(nomal.subList(index, nomal.size()));
				}
			} else {
				result.addAll(big);
				result.addAll(nomal);
			}
		} else {
			if (big.size() + nomal.size() > CommonActivityConfig.getInstance().IDENTIFY_TREASURE_RANK_NUM.getValue()) {
				int index = nomal.size()
						- (CommonActivityConfig.getInstance().IDENTIFY_TREASURE_RANK_NUM.getValue() - big.size());
				result.addAll(big);
				result.addAll(nomal.subList(index, nomal.size()));
			} else {
				result.addAll(big);
				result.addAll(nomal);
			}
		}
		Collections.sort(result, new Comparator<IdentifyTreasureLog>() {
			@Override
			public int compare(IdentifyTreasureLog o1, IdentifyTreasureLog o2) {
				return (int) (o1.getTime() - o2.getTime());
			}
		});
		return result;
	}

	public void clearRank() {
		bigTreasureLogs.clear();
		nomalTreasureLogs.clear();
	}

	@JsonIgnore
	public boolean isOpeningIdentifyTreasure() {
		long nowTime = new Date().getTime();
		if (nowTime < startTime || nowTime >= endTime) {
			return false;
		}
		return true;
	}

	@JsonIgnore
	public void refresh(String activeName, long startTime, long endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
		version++;
		clearRank();
	}

	synchronized public long getStartTime() {
		Map<String, ArrayList<String>> openTimes = ServerState.getInstance().IDENTIFY_TREASURE_OPEN_TIME.getValue();
		ArrayList<String> times = openTimes.get(activeName);
		if (times != null) {
			try {
				startTime = format.parse(times.get(0)).getTime();
			} catch (ParseException e) {
				throw new RuntimeException(String.format("时间转换格式出错[%s]", times.get(0)));
			}
		}
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	synchronized public long getEndTime() {
		Map<String, ArrayList<String>> openTimes = ServerState.getInstance().IDENTIFY_TREASURE_OPEN_TIME.getValue();
		ArrayList<String> times = openTimes.get(activeName);
		if (times != null) {
			try {
				endTime = format.parse(times.get(1)).getTime();
			} catch (ParseException e) {
				throw new RuntimeException(String.format("时间转换格式出错[%s]", times.get(1)));
			}
		}
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public LinkedList<IdentifyTreasureLog> getBigTreasureLogs() {
		return bigTreasureLogs;
	}

	public void setBigTreasureLogs(LinkedList<IdentifyTreasureLog> bigTreasureLogs) {
		this.bigTreasureLogs = bigTreasureLogs;
	}

	public LinkedList<IdentifyTreasureLog> getNomalTreasureLogs() {
		return nomalTreasureLogs;
	}

	public void setNomalTreasureLogs(LinkedList<IdentifyTreasureLog> nomalTreasureLogs) {
		this.nomalTreasureLogs = nomalTreasureLogs;
	}

	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}
}
