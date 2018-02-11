package com.mmorpg.mir.admin.bean;

import java.util.Collection;
import java.util.Map;

public class ServerStat {
	// 服务器开服时间
	private long openDate;
	// 是否开启了防火墙
	private boolean blockAll;
	// 是否开启了防沉迷
	private boolean antiAddiction;
	// 在线数
	private int onLine;
	// 最大的连接数
	private int maxConnectionSize;
	// ip白名单
	private Collection<String> allowIp;
	// ip黑名单
	private Collection<String> blockIp;
	// 管理后台ip信息
	private Collection<String> managedIp;
	// 总内存
	private long totalMemory;
	// 空闲内存
	private long freeMemory;
	// 使用内存
	private long useMemory;
	// 服务器开启时间
	private long startTime;
	// 网络连接数量
	private int netConnectionSize;
	// 网络连接创建次数
	private long netCreateTimes;
	// 网络连接关闭次数
	private long netCloseTimes;
	// 网络接收包个数
	private long netReceivePackets;
	// 网络接收包总长
	private long netReceiveLengths;
	// 网络发送包个数
	private long netSendPackets;
	// 网络发送包总长
	private long netSendLengths;
	// 网络接受包的详细信息
	private Map<String, StatNetReceiveBean> netReceiveMap;
	// 网络发送包的详细信息
	private Map<String, StatNetSendBean> netSendMap;
	// 任务包数量
	private long taskTotalPackets;
	// 任务包处理总时间
	private long taskTotalTimes;
	// 任务包详细信息
	private Map<String, StatTaskBean> taskMap;
	// 事件任务总数
	private long eventTotalPackets;
	// 事件任务消耗的总时间
	private long eventTotalTimes;
	// 事件任务详细信息
	private Map<String, StatEventBean> eventMap;

	public boolean isBlockAll() {
		return blockAll;
	}

	public void setBlockAll(boolean blockAll) {
		this.blockAll = blockAll;
	}

	public int getOnLine() {
		return onLine;
	}

	public void setOnLine(int onLine) {
		this.onLine = onLine;
	}

	public Collection<String> getAllowIp() {
		return allowIp;
	}

	public void setAllowIp(Collection<String> allowIp) {
		this.allowIp = allowIp;
	}

	public Collection<String> getManagedIp() {
		return managedIp;
	}

	public void setManagedIp(Collection<String> managedIp) {
		this.managedIp = managedIp;
	}

	public Collection<String> getBlockIp() {
		return blockIp;
	}

	public void setBlockIp(Collection<String> blockIp) {
		this.blockIp = blockIp;
	}

	public long getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}

	public long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public long getUseMemory() {
		return useMemory;
	}

	public void setUseMemory(long useMemory) {
		this.useMemory = useMemory;
	}

	public long getOpenDate() {
		return openDate;
	}

	public void setOpenDate(long openDate) {
		this.openDate = openDate;
	}

	public int getMaxConnectionSize() {
		return maxConnectionSize;
	}

	public void setMaxConnectionSize(int maxConnectionSize) {
		this.maxConnectionSize = maxConnectionSize;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getNetConnectionSize() {
		return netConnectionSize;
	}

	public void setNetConnectionSize(int netConnectionSize) {
		this.netConnectionSize = netConnectionSize;
	}

	public long getNetCreateTimes() {
		return netCreateTimes;
	}

	public void setNetCreateTimes(long netCreateTimes) {
		this.netCreateTimes = netCreateTimes;
	}

	public long getNetCloseTimes() {
		return netCloseTimes;
	}

	public void setNetCloseTimes(long netCloseTimes) {
		this.netCloseTimes = netCloseTimes;
	}

	public long getNetReceivePackets() {
		return netReceivePackets;
	}

	public void setNetReceivePackets(long netReceivePackets) {
		this.netReceivePackets = netReceivePackets;
	}

	public long getNetReceiveLengths() {
		return netReceiveLengths;
	}

	public void setNetReceiveLengths(long netReceiveLengths) {
		this.netReceiveLengths = netReceiveLengths;
	}

	public long getNetSendPackets() {
		return netSendPackets;
	}

	public void setNetSendPackets(long netSendPackets) {
		this.netSendPackets = netSendPackets;
	}

	public long getNetSendLengths() {
		return netSendLengths;
	}

	public void setNetSendLengths(long netSendLengths) {
		this.netSendLengths = netSendLengths;
	}

	public Map<String, StatNetReceiveBean> getNetReceiveMap() {
		return netReceiveMap;
	}

	public void setNetReceiveMap(Map<String, StatNetReceiveBean> netReceiveMap) {
		this.netReceiveMap = netReceiveMap;
	}

	public Map<String, StatNetSendBean> getNetSendMap() {
		return netSendMap;
	}

	public void setNetSendMap(Map<String, StatNetSendBean> netSendMap) {
		this.netSendMap = netSendMap;
	}

	public long getTaskTotalPackets() {
		return taskTotalPackets;
	}

	public void setTaskTotalPackets(long taskTotalPackets) {
		this.taskTotalPackets = taskTotalPackets;
	}

	public long getTaskTotalTimes() {
		return taskTotalTimes;
	}

	public void setTaskTotalTimes(long taskTotalTimes) {
		this.taskTotalTimes = taskTotalTimes;
	}

	public Map<String, StatTaskBean> getTaskMap() {
		return taskMap;
	}

	public void setTaskMap(Map<String, StatTaskBean> taskMap) {
		this.taskMap = taskMap;
	}

	public long getEventTotalPackets() {
		return eventTotalPackets;
	}

	public void setEventTotalPackets(long eventTotalPackets) {
		this.eventTotalPackets = eventTotalPackets;
	}

	public long getEventTotalTimes() {
		return eventTotalTimes;
	}

	public void setEventTotalTimes(long eventTotalTimes) {
		this.eventTotalTimes = eventTotalTimes;
	}

	public Map<String, StatEventBean> getEventMap() {
		return eventMap;
	}

	public void setEventMap(Map<String, StatEventBean> eventMap) {
		this.eventMap = eventMap;
	}

	public boolean isAntiAddiction() {
		return antiAddiction;
	}

	public void setAntiAddiction(boolean antiAddiction) {
		this.antiAddiction = antiAddiction;
	}
}
