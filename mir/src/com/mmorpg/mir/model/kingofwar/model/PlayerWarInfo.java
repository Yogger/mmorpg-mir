package com.mmorpg.mir.model.kingofwar.model;

import java.util.Arrays;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.kingofwar.config.KingOfWarConfig;
import com.mmorpg.mir.model.kingofwar.packet.vo.PlayerRankInfoVO;
import com.mmorpg.mir.model.kingofwar.packet.vo.PlayerWarInfoVO;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.task.tasks.PacketBroadcaster.BroadcastMode;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class PlayerWarInfo implements Comparable<PlayerWarInfo> {
	private Player player;
	/** 积分 */
	private int points;
	/** 击杀次数统计 */
	private Map<Long, Integer> killCounts;
	/** 连杀 */
	private int continueKill;
	/** 排名 */
	private int rank;
	/** BOSS伤害 */
	private long bossDamage;
	/** 炸弹值 */
	private short bombValue;

	public static PlayerWarInfo valueOf(Player player) {
		PlayerWarInfo warHistory = new PlayerWarInfo();
		warHistory.player = player;
		return warHistory;
	}

	@JsonIgnore
	public boolean isMaxBombValue() {
		if (bombValue >= KingOfWarConfig.getInstance().BOMB_MAX_VALUE.getValue()) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public void clearBombValue() {
		bombValue = 0;
		player.getEffectController().removeEffect(KingOfWarConfig.getInstance().BOMB_BUFF_SKILL.getValue());
		sendUpdate();
	}

	@JsonIgnore
	public void addBombValueByKilled() {
		bombValue += KingOfWarConfig.getInstance().BOMB_VALUE.getValue();
		sendUpdate();
	}

	@JsonIgnore
	public int getAllKillCount() {
		int count = 0;
		if (killCounts != null) {
			for (Integer c : killCounts.values()) {
				count += c;
			}
		}
		return count;
	}

	synchronized public void increaseKillCount(long playerId) {
		if (killCounts == null) {
			killCounts = New.hashMap();
		}
		if (killCounts.containsKey(playerId)) {
			killCounts.put(playerId, killCounts.get(playerId) + 1);
		} else {
			killCounts.put(playerId, 1);
		}
		increaseContinueKill();
		sendUpdate();
	}

	synchronized public void increasePoints(int value) {
		points += value;
		sendUpdate();
	}

	synchronized public void increaseBombValue(int value) {
		bombValue += value;
		refreshBombBuff();
		sendUpdate();
	}

	public void refreshBombBuff() {
		if (isMaxBombValue()) {
			if (!player.getEffectController().containsSkill(KingOfWarConfig.getInstance().BOMB_BUFF_SKILL.getValue())) {
				Skill skill = SkillEngine.getInstance().getSkill(null,
						KingOfWarConfig.getInstance().BOMB_BUFF_SKILL.getValue(), player.getObjectId(), 0, 0, player,
						null);
				skill.noEffectorUseSkill();
			}
		}
	}

	public void increaseBossDamage(long value) {
		bossDamage += value;
		sendUpdate();
	}

	private void increaseContinueKill() {
		continueKill++;
		if ((continueKill % 10) == 0) {
			if (Arrays.asList(KingOfWarConfig.getInstance().CONTINUEKILL_TV_JUNIOR.getValue()).contains(continueKill)) {
				I18nUtils i18nUtils = I18nUtils.valueOf("10103");
				i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
				i18nUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
				i18nUtils.addParm("n", I18nPack.valueOf(continueKill));
				ChatManager.getInstance().sendSystem(11008, i18nUtils, null,
						Integer.valueOf(KingOfWarConfig.getInstance().MAPID.getValue()),
						player.getPosition().getInstanceId());
			} else if (Arrays.asList(KingOfWarConfig.getInstance().CONTINUEKILL_TV_SENIOR.getValue()).contains(
					continueKill)) {
				I18nUtils i18nUtils = I18nUtils.valueOf("10104");
				i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
				i18nUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
				i18nUtils.addParm("n", I18nPack.valueOf(continueKill));
				ChatManager.getInstance().sendSystem(11008, i18nUtils, null,
						Integer.valueOf(KingOfWarConfig.getInstance().MAPID.getValue()),
						player.getPosition().getInstanceId());
			}
		}
	}

	public PlayerWarInfoVO createVO() {
		PlayerWarInfoVO vo = new PlayerWarInfoVO();
		vo.setPoints(points);
		vo.setContinueKill(continueKill);
		vo.setTotalKill(getAllKillCount());
		vo.setRank(rank);
		vo.setBombValue(bombValue);
		return vo;
	}

	public PlayerRankInfoVO createRankInfo() {
		return PlayerRankInfoVO.valueOf(this);
	}

	public void sendUpdate() {
		player.addPacketBroadcastMask(BroadcastMode.SEND_KINGOFWAR_INFO);
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

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Map<Long, Integer> getKillCounts() {
		return killCounts;
	}

	public void setKillCounts(Map<Long, Integer> killCounts) {
		this.killCounts = killCounts;
	}

	public int getContinueKill() {
		return continueKill;
	}

	public void setContinueKill(int continueKill) {
		this.continueKill = continueKill;
	}

	@Override
	public int compareTo(PlayerWarInfo o) {
		return (int) (o.getPoints() - getPoints());
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public long getBossDamage() {
		return bossDamage;
	}

	public void setBossDamage(long bossDamage) {
		this.bossDamage = bossDamage;
	}

	public short getBombValue() {
		return bombValue;
	}

	public void setBombValue(short bombValue) {
		this.bombValue = bombValue;
	}

}
