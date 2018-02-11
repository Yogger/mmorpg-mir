package com.mmorpg.mir.model.temple.model;

import java.util.Date;
import java.util.Map;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.temple.packet.SM_Temple_BrickFail;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.welfare.event.TempleEvent;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.New;

public class TempleHistory {
	public final static String BRICK_BUFF_GROUP = "BRICK";

	private Player player;
	/** 已经板砖次数 */
	private int count;
	/** 总共板砖的次数 */
	transient private int countAll;
	/** 各种颜色总共搬砖次数，key:颜色 */
	private Map<Integer, Integer> historyColorCount = New.hashMap();
	/** 最后一次刷新时间 */
	private long lastRefreshTime;
	/** 当前砖块 */
	@Transient
	private Brick currentBrick;
	/** 是否带有搬砖任务 */
	@Transient
	private int questCountry;
	/** 是否通知了失败 */
	private boolean beenNotifyFail = true;

	@JsonIgnore
	public void refresh() {
		if (!DateUtils.isToday(new Date(lastRefreshTime))) {
			lastRefreshTime = System.currentTimeMillis();
			count = 0;
			questCountry = 0;
			beenNotifyFail = true;
		}
	}

	@JsonIgnore
	public void addCount(int add) {
		count += add;
		countAll += add;
		int max = ConfigValueManager.getInstance().COUNTRY_TEMPLE_QUEST_MAX_BRICK.getValue() + player.getVip().getResource().getExBrickCount();
		if (count > max) {
			count = max;
		}
	}

	@JsonIgnore
	public TempleVO creatVO() {
		TempleVO vo = TempleVO.valueOf(this);
		return vo;
	}

	@JsonIgnore
	public boolean hasBrick() {
		if (currentBrick != null) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public void timeFail(int deadline) {
		if (currentBrick == null) {
			return;
		}
		if ((currentBrick.getStartTime() + deadline) <= System.currentTimeMillis()) {
			currentBrick.getFromTemple().getTakedBricks().remove(getPlayer().getObjectId());
			currentBrick.getFromTemple().addBrick(1,
					ConfigValueManager.getInstance().COUNTRY_TEMPLE_MAX_BRICK.getValue());
			boolean rightBrick = player.getTempleHistory().getCurrentBrick().getFromTemple().getCountry().getId()
					.getValue() == player.getTempleHistory().getQuestCountry() ? true : false;
			if (deadline != 0 && rightBrick) {
				questCountry = 0;
				addCount(1);
				EventBusManager.getInstance().submit(TempleEvent.valueOf(player, true));
				LogManager.brick(player.getObjectId(), player.getPlayerEnt().getServer(), player.getPlayerEnt()
						.getAccountName(), player.getName(), System.currentTimeMillis(), currentBrick.getFromTemple().getCountry()
						.getId().getValue(), player.getCountryValue(), player.getTempleHistory().getCount(), player
						.getTempleHistory().getCountAll(), 1);
			}
			playerTempleStatusClear();
		}
	}

	@JsonIgnore
	public void playerTempleStatusClear() {
		if (SessionManager.getInstance().isOnline(getPlayer().getObjectId())) {
			PacketSendUtility.sendPacket(getPlayer(), new SM_Temple_BrickFail());
			beenNotifyFail = true;
		} else {
			beenNotifyFail = false;
			return;
		}
		if (getPlayer().getEffectController().contains(BRICK_BUFF_GROUP)) {
			getPlayer().getEffectController().clearEffect(
					getPlayer().getEffectController().getAnormalEffect(BRICK_BUFF_GROUP));
		}
		currentBrick = null;
	}

	@JsonIgnore
	public void success() {
		if (getPlayer().getEffectController().contains(BRICK_BUFF_GROUP)) {
			getPlayer().getEffectController().clearEffect(
					getPlayer().getEffectController().getAnormalEffect(BRICK_BUFF_GROUP));
		}
		currentBrick = null;
	}

	@JsonIgnore
	public void addHistoryColorCount(int color) {
		if (!this.historyColorCount.containsKey(color)) {
			this.historyColorCount.put(color, 1);
			return;
		}
		int num = this.historyColorCount.get(color);
		this.historyColorCount.put(color, ++num);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	@JsonIgnore
	public Brick getCurrentBrick() {
		return currentBrick;
	}

	public void setCurrentBrick(Brick currentBrick) {
		this.currentBrick = currentBrick;
	}

	@JsonIgnore
	public Player getPlayer() {
		return player;
	}

	@JsonIgnore
	public void setPlayer(Player player) {
		this.player = player;
	}

	@JsonIgnore
	public int getQuestCountry() {
		return questCountry;
	}

	@JsonIgnore
	public void setQuestCountry(int questCountry) {
		this.questCountry = questCountry;
	}

	public int getCountAll() {
		return countAll;
	}

	public void setCountAll(int countAll) {
		this.countAll = countAll;
	}

	public boolean isBeenNotifyFail() {
		return beenNotifyFail;
	}

	public void setBeenNotifyFail(boolean beenNotifyFail) {
		this.beenNotifyFail = beenNotifyFail;
	}

	public Map<Integer, Integer> getHistoryColorCount() {
		return historyColorCount;
	}

	public void setHistoryColorCount(Map<Integer, Integer> historyColorCount) {
		this.historyColorCount = historyColorCount;
	}

}
