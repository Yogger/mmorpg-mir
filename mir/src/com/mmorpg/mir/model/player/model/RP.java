package com.mmorpg.mir.model.player.model;

import java.util.Date;
import java.util.LinkedList;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.player.manager.RPManager;
import com.mmorpg.mir.model.player.packet.SM_RP_Update;
import com.mmorpg.mir.model.player.resource.RPType;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.effecttemplate.GrayEffect;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.task.tasks.PacketBroadcaster.BroadcastMode;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;

/**
 * 人品
 * 
 * @author Kuang Hao
 * @since v1.0 2014-9-16
 * 
 */
public class RP {

	public static final StatEffectId GAME_STAT_ID = StatEffectId.valueOf("RP", StatEffectType.RP_EFFECT, true);

	@JsonIgnore
	private Player owner;

	private int rp;

	private long lastReduceTime;

	private LinkedList<Long> recentDeadTime;
	
	private int grayLevel;
	
	private int killEnemyCount;
	
	private int killKingCount;

	private RPType rpType;

	public static void main(String[] args) {
		RP test = RP.valueOf();
		test.getRecentDeadTime().add(14123L);
		String a = JsonUtils.object2String(test);
		System.out.println(a);
		RP rp = JsonUtils.string2Object(a, RP.class);
		System.out.println(rp.getRecentDeadTime().size() + " " + rp.getRecentDeadTime().getFirst());
	}

	public void addObserve() {

		ActionObserver observer = new ActionObserver(ObserverType.RPTYPECHANGE) {
			@Override
			public void rpChangeType() {
				owner.getGameStats().endModifiers(GAME_STAT_ID, false);
				owner.getGameStats().addModifiers(GAME_STAT_ID, RPManager.getInstance().getResource(rpType).getStats());
			}
		};

		owner.getObserveController().addObserver(observer);
	}

	public void initRpStat() {
		owner.getGameStats().addModifiers(GAME_STAT_ID, RPManager.getInstance().getResource(rpType).getStats(), false);
	}

	@JsonIgnore
	public void reduceRP(int reduce) {
		rp -= reduce;
		if (rp == 0) {
			rp = 0;
		}
		update(rp);
		sendRpPacketUpdate();
	}

	@JsonIgnore
	private void update(int rpNew) {
		RPType mayBeNew = RPManager.getInstance().getRpType(rpNew);
		if (mayBeNew.getValue() != rpType.getValue()) {
			rpType = mayBeNew;
			owner.getObserveController().notifyRPTypeChangeObservers();
		}
	}

	@JsonIgnore
	public void increaseRP(int increase) {
		rp += increase;
		int MAX_RP = RPManager.getInstance().getRPMaxLimit();
		if (rp > MAX_RP) {
			rp = MAX_RP;
		}
		update(rp);
		sendRpPacketUpdate();
	}

	@JsonIgnore
	public void sendRpPacketUpdate() {
		owner.addPacketBroadcastMask(BroadcastMode.UPDATE_PLAYER_RP_STAT);
	}

	public void sendRpPacketUpdateImpl() {
		PacketSendUtility.sendPacket(owner, SM_RP_Update.valueOf(rp, rpType.getValue()));
	}

	public static RP valueOf() {
		RP rp = new RP();
		rp.rp = 0;
		rp.rpType = RPType.CIVILIANS;
		rp.recentDeadTime = new LinkedList<Long>();
		rp.lastReduceTime = System.currentTimeMillis();
		return rp;
	}

	public void refresh() {
		if (!DateUtils.isToday(new Date(lastReduceTime))) {
			int interval = DateUtils.calcIntervalDays(new Date(lastReduceTime), new Date());
			int DAY_REDUCE = RPManager.getInstance().getDailyReduce(rpType.getValue());
			rp -= (interval * DAY_REDUCE);
			if (rp < 0) {
				rp = 0;
			}
			update(rp);
			lastReduceTime = System.currentTimeMillis();
		}
	}

	public int getRp() {
		return rp;
	}

	public void setRp(int rp) {
		this.rp = rp;
	}

	public long getLastReduceTime() {
		return lastReduceTime;
	}

	public LinkedList<Long> getRecentDeadTime() {
		return recentDeadTime;
	}

	public void setRecentDeadTime(LinkedList<Long> recentDeadTime) {
		this.recentDeadTime = recentDeadTime;
	}

	public void setLastReduceTime(long lastReduceTime) {
		this.lastReduceTime = lastReduceTime;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public RPType getRpType() {
		return rpType;
	}

	public void setRpType(RPType rpType) {
		this.rpType = rpType;
	}

	@JsonIgnore
	public void doGray() {
		long now = System.currentTimeMillis();
		recentDeadTime.addLast(now);
		if (recentDeadTime.size() > RPManager.getInstance().getKilledCount()) {
			recentDeadTime.removeFirst();
			if (now - recentDeadTime.getFirst() <= RPManager.getInstance().getKilledPeriod()) {
				if (owner.getEffectController().contains(GrayEffect.GRAY)) {
					if (grayLevel < RPManager.getInstance().getGraySkillSize() - 1) {
						long duration = owner.getEffectController().getAnormalEffect(GrayEffect.GRAY).getEndTime() - System.currentTimeMillis();
						grayLevel++;
						Skill skill = SkillEngine.getInstance().getSkill(null, RPManager.getInstance().getGraySkillId(grayLevel),
								owner.getObjectId(), 0, 0, owner, null);
						skill.noEffectorUseSkill(duration);
					}
				} else {
					grayLevel = 0;
					Skill skill = SkillEngine.getInstance().getSkill(null, RPManager.getInstance().getGraySkillId(grayLevel),
							owner.getObjectId(), 0, 0, owner, null);
					skill.noEffectorUseSkill(RPManager.getInstance().GRAY_DURATION.getValue());
				}
			} else {
				grayLevel = 0;
			}
		}
	}
	
	@JsonIgnore
	public void weakenGrayBuff(boolean isKillPlayer) {
		if (isKillPlayer) {
			grayLevel -= 2;
		} else {
			grayLevel--;
		}
		if (grayLevel < 0) {
			grayLevel = 0;
		}
	}
	
	@JsonIgnore
	public void clearRpBuff() {
		grayLevel = 0;
		owner.getEffectController().removeEffect(GrayEffect.GRAY);
	}

	public int getGrayLevel() {
		return grayLevel;
	}

	public void setGrayLevel(int grayLevel) {
		this.grayLevel = grayLevel;
	}

	public int getKillEnemyCount() {
		return killEnemyCount;
	}

	public void setKillEnemyCount(int killEnemyCount) {
		this.killEnemyCount = killEnemyCount;
	}

	public int getKillKingCount() {
		return killKingCount;
	}

	public void setKillKingCount(int killKingCount) {
		this.killKingCount = killKingCount;
	}

	@JsonIgnore
	public void addKillKingCount() {
		this.killKingCount++;
	}
	
	@JsonIgnore
	public void addKillEnemyCount() {
		this.killEnemyCount++;
	}
	
}
