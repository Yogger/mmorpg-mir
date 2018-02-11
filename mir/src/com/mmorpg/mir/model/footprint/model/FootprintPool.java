package com.mmorpg.mir.model.footprint.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chat.model.show.object.FootprintShow;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.footprint.config.FootprintConfig;
import com.mmorpg.mir.model.footprint.packet.SM_Add_Footprint;
import com.mmorpg.mir.model.footprint.packet.SM_Change_Footprint;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.scheduler.ScheduledTask;
import com.windforce.common.utility.DateUtils;

public class FootprintPool {

	public static final StatEffectId FOOTPRINT_STAT = StatEffectId.valueOf("FOOTPRINT_STAT", StatEffectType.FOOTPRINT);

	private transient Player owner;

	private int openFootprint;
	private Map<Integer, Footprint> footprints;

	private transient Future<?> footprintTimeFuture;

	public static FootprintPool valueOf() {
		FootprintPool fp = new FootprintPool();
		fp.footprints = new HashMap<Integer, Footprint>();
		return fp;
	}

	@JsonIgnore
	public void open(int id) {
		if (!footprints.containsKey(id)) {
			throw new ManagedException(ManagedErrorCode.FOOTPRINT_NOT_FOUND);
		}
		if (footprints.get(id).isDeprecated()) {
			throw new ManagedException(ManagedErrorCode.FOOTPRINT_DEPRECATED);
		}
		if (id == getOpenFootprint()) {
			return;
		}
		setOpenFootprint(id);
		refreshStats();
		setFootprintFuture(true);
		PacketSendUtility.broadcastPacketAndReceiver(owner, SM_Change_Footprint.valueOf(owner, OPEN));
	}

	@JsonIgnore
	public void loginInit() {
		if (openFootprint != 0) {
			if (footprints.get(openFootprint).isDeprecated()) {
				setOpenFootprint(0);
			}
		}
		refreshStats(false);
		setFootprintFuture(false);
	}

	public static void main(String[] args) {
		System.out.println(1427254778349l - System.currentTimeMillis());
	}

	@JsonIgnore
	public void setFootprintFuture(boolean addTime) {
		if (openFootprint == 0) {
			stopFootprintFuture();
		} else {
			if (addTime) {
				stopFootprintFuture();
			}
			if (footprintTimeFuture == null || footprintTimeFuture.isCancelled()) {
				footprintTimeFuture = FootprintConfig.getInstance().getSimpleScheduler().schedule(new ScheduledTask() {
					@Override
					public void run() {
						if (footprints.get(openFootprint).isDeprecated()) {
							close(TIME);
						}
					}

					@Override
					public String getName() {
						return "足迹倒计时";
					}
				}, new Date(footprints.get(openFootprint).getEndTime()));
			}

		}
	}

	@JsonIgnore
	public void stopFootprintFuture() {
		if (footprintTimeFuture != null && !footprintTimeFuture.isCancelled()) {
			footprintTimeFuture.cancel(true);
		}
		footprintTimeFuture = null;
	}

	@JsonIgnore
	private void refreshStats(boolean recomputeStats) {
		Stat[] stats = null;
		for (Footprint footprint : footprints.values()) {
			for (Stat stat : footprint.getResource().getForeverStats()[footprint.getStar()]) {
				stats = (Stat[]) ArrayUtils.add(stats, stat);
			}
		}
		if (openFootprint != 0 && !footprints.get(openFootprint).isDeprecated()) {
			Footprint openFoot = footprints.get(openFootprint);
			for (Stat stat : openFoot.getResource().getOpenStats()) {
				stats = (Stat[]) ArrayUtils.add(stats, stat);
			}
		}
		if (!ArrayUtils.isEmpty(stats)) {
			owner.getGameStats().addModifiers(FOOTPRINT_STAT, stats, recomputeStats);
		}
	}

	@JsonIgnore
	private void refreshStats() {
		refreshStats(true);
	}

	/** 手动关闭 */
	public static final byte CLOSE = 1;
	/** 手动开启 */
	public static final byte OPEN = 2;
	/** 时间到 */
	public static final byte TIME = 3;

	@JsonIgnore
	public void close(byte type) {
		if (getOpenFootprint() == 0) {
			return;
		}
		setOpenFootprint(0);
		refreshStats();
		PacketSendUtility.broadcastPacketAndReceiver(owner, SM_Change_Footprint.valueOf(owner, type));
	}

	@JsonIgnore
	public void reward(Player player, int id, int time, String star) {
		if (footprints.containsKey(id)) {
			Footprint ft = footprints.get(id);
			if (star != null) {
				ft.increase(time * DateUtils.MILLIS_PER_MINUTE, Integer.valueOf(star));
			} else {
				ft.increase(time * DateUtils.MILLIS_PER_MINUTE);
			}
			if (id == openFootprint) {
				setFootprintFuture(true);
			}
			refreshStats();
		} else {
			long endTime = System.currentTimeMillis() + time * DateUtils.MILLIS_PER_MINUTE;
			Footprint ft = Footprint.valueOf(id, endTime);
			if (star != null) {
				int setStar = Integer.valueOf(star) - 1;
				ft.setStar(setStar);
			}
			footprints.put(id, ft);
			refreshStats();
			FootprintShow show = new FootprintShow();
			show.setId(id);
			show.setFootprint(ft);
			// 系统广播 60102
			I18nUtils utils = I18nUtils.valueOf("60102");
			utils.addParm("name", I18nPack.valueOf(player.getName()));
			utils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			utils.addParm("spoor", I18nPack.valueOf(show));
			ChatManager.getInstance().sendSystem(11001, utils, null);

			// 聊天广播307002
			I18nUtils utils2 = I18nUtils.valueOf("307002");
			utils2.addParm("name", I18nPack.valueOf(player.getName()));
			utils2.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			utils2.addParm("spoor", I18nPack.valueOf(show));
			ChatManager.getInstance().sendSystem(0, utils2, null);
		}
		PacketSendUtility.sendPacket(owner,
				SM_Add_Footprint.valueOf(id, footprints.get(id).getEndTime(), footprints.get(id).getStar()));
	}

	public Map<Integer, Footprint> getFootprints() {
		return footprints;
	}

	public void setFootprints(Map<Integer, Footprint> footprints) {
		this.footprints = footprints;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public int getOpenFootprint() {
		return openFootprint;
	}

	public void setOpenFootprint(int openFootprint) {
		this.openFootprint = openFootprint;
	}

}
