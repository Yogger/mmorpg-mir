package com.mmorpg.mir.model.gangofwar.model;

import java.util.Arrays;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gangofwar.config.GangOfWarConfig;
import com.mmorpg.mir.model.gangofwar.packet.vo.GangOfWarRankItem;
import com.mmorpg.mir.model.gangofwar.packet.vo.PlayerGangWarInfoVO;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.task.tasks.PacketBroadcaster.BroadcastMode;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class PlayerGangWarInfo implements Comparable<PlayerGangWarInfo> {
	private Player player;
	/** 总共击杀 */
	private int totalKill;
	@Transient
	private transient long lastAddTotalKill;
	/** 连杀 */
	private int continueKill;
	/** 最大连杀 */
	private int maxContinueKill;
	/** 排名 */
	private int rank;
	/** 最后一次回城时间 */
	private long lastBackHomeTime;
	private String gangName;
	private String gangServer;

	public static PlayerGangWarInfo valueOf(Player player) {
		PlayerGangWarInfo warHistory = new PlayerGangWarInfo();
		warHistory.player = player;
		warHistory.gangName = player.getGang().getName();
		warHistory.gangServer = player.getGang().getServer();

		return warHistory;
	}

	synchronized public void increaseKillCount(long playerId) {
		totalKill++;
		lastAddTotalKill = System.currentTimeMillis();
		sendUpdate();
	}

	synchronized public void increaseContinueKill() {
		continueKill++;
		if (continueKill > maxContinueKill) {
			maxContinueKill++;
		}
		if (Arrays.asList(GangOfWarConfig.getInstance().CONTINUEKILL_TV.getValue()).contains(continueKill)) {
			I18nUtils i18nUtils = I18nUtils.valueOf("10202");
			i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
			i18nUtils.addParm("family", I18nPack.valueOf(player.getGang().getName()));
			i18nUtils.addParm("n", I18nPack.valueOf(continueKill + ""));
			ChatManager.getInstance().sendSystem(11008, i18nUtils, null,
					Integer.valueOf(GangOfWarConfig.getInstance().MAPID.getValue()), player.getCountryValue());
		}
		sendUpdate();
	}

	public PlayerGangWarInfoVO createVO() {
		PlayerGangWarInfoVO vo = new PlayerGangWarInfoVO();
		vo.setContinueKill(continueKill);
		vo.setTotalKill(totalKill);
		vo.setMaxContinueKill(maxContinueKill);
		vo.setRank(rank);
		vo.setLastBackHomeTime(lastBackHomeTime);
		return vo;
	}

	public GangOfWarRankItem createRankInfo() {
		return GangOfWarRankItem.valueOf(rank, player, this);
	}

	public void sendUpdate() {
		player.addPacketBroadcastMask(BroadcastMode.SEND_GANGOFWAR_INFO);
	}

	public void sendUpdateImp() {
		PacketSendUtility.sendPacket(player, createVO());
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getContinueKill() {
		return continueKill;
	}

	public void setContinueKill(int continueKill) {
		this.continueKill = continueKill;
	}

	@Override
	public int compareTo(PlayerGangWarInfo o) {
		if (o.getTotalKill() == getTotalKill()) {
			return (int) (getLastAddTotalKill() - o.getLastAddTotalKill());
		}
		return (int) (o.getTotalKill() - getTotalKill());
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getMaxContinueKill() {
		return maxContinueKill;
	}

	public void setMaxContinueKill(int maxContinueKill) {
		this.maxContinueKill = maxContinueKill;
	}

	public int getTotalKill() {
		return totalKill;
	}

	public void setTotalKill(int totalKill) {
		this.totalKill = totalKill;
	}

	public long getLastBackHomeTime() {
		return lastBackHomeTime;
	}

	public void setLastBackHomeTime(long lastBackHomeTime) {
		this.lastBackHomeTime = lastBackHomeTime;
	}

	@JsonIgnore
	public long getLastAddTotalKill() {
		return lastAddTotalKill;
	}

	@JsonIgnore
	public void setLastAddTotalKill(long lastAddTotalKill) {
		this.lastAddTotalKill = lastAddTotalKill;
	}

	public String getGangName() {
		return gangName;
	}

	public void setGangName(String gangName) {
		this.gangName = gangName;
	}

	public String getGangServer() {
		return gangServer;
	}

	public void setGangServer(String gangServer) {
		this.gangServer = gangServer;
	}

}
