package com.mmorpg.mir.model.blackshop.model;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.blackshop.packet.SM_BlackShop_Activity_Change;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.DateUtils;

public class BlackShopServer {
	// 版本号
	private int version;

	private boolean merge;

	private String goodGroupId;

	private long beginTime;

	private long endTime;

	public static BlackShopServer valueOf() {
		BlackShopServer result = new BlackShopServer();
		result.merge = false;
		result.beginTime = 0L;
		result.endTime = 0L;
		result.version = 0;
		return result;
	}

	@JsonIgnore
	public void doMerge(String goodGroupId, Date mergeTime, int beginAddDays, int endAddDays) {
		if (this.merge) {
			this.merge = false;
			this.version++;
			this.goodGroupId = goodGroupId;
			Date firstTime = DateUtils.getFirstTime(mergeTime);
			this.beginTime = DateUtils.addDays(firstTime, beginAddDays).getTime();
			this.endTime = DateUtils.addDays(firstTime, endAddDays + 1).getTime();
		}
	}

	@JsonIgnore
	public void fix(String goodGroupId, long beginTime, long endTime) {
		this.version++;
		this.goodGroupId = goodGroupId;
		this.beginTime = beginTime;
		this.endTime = endTime;
		for (long pid : SessionManager.getInstance().getOnlineIdentities()) {
			Player player = PlayerManager.getInstance().getPlayer(pid);
			PacketSendUtility.sendPacket(player, SM_BlackShop_Activity_Change.valueOf());
		}
	}

	@JsonIgnore
	public void close() {
		this.beginTime = 0L;
		this.endTime = 0L;
		for (long pid : SessionManager.getInstance().getOnlineIdentities()) {
			Player player = PlayerManager.getInstance().getPlayer(pid);
			PacketSendUtility.sendPacket(player, SM_BlackShop_Activity_Change.valueOf());
		}
	}

	@JsonIgnore
	public boolean isActivityOpen() {
		long now = System.currentTimeMillis();
		return beginTime <= now && now <= endTime;
	}

	public String getGoodGroupId() {
		return goodGroupId;
	}

	public void setGoodGroupId(String goodGroupId) {
		this.goodGroupId = goodGroupId;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public boolean isMerge() {
		return merge;
	}

	public void setMerge(boolean merge) {
		this.merge = merge;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public static void main(String[] args) {
		Date now = new Date();
		Date n = DateUtils.addDays(DateUtils.getFirstTime(now), 1);
		System.out.println(DateUtils.date2String(n, "yyyy-MM-dd HH:mm:ss"));
	}
}
