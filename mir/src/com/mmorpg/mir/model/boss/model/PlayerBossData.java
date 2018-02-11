package com.mmorpg.mir.model.boss.model;

import java.util.HashSet;
import java.util.List;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.boss.config.BossConfig;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;

public class PlayerBossData {

	public static final StatEffectId BOSS_COINS_LEVEL = StatEffectId.valueOf("BOSS_COINS_LEVEL",
			StatEffectType.BOSS_COINS);
	public static final StatEffectId BOSS_COINS_PAYED = StatEffectId.valueOf("BOSS_COINS_PAYED",
			StatEffectType.BOSS_COINS);

	@Transient
	private transient Player owner;

	private String bossCoinsLevel;

	private HashSet<String> payedBossCoinsIds;

	@JsonIgnore
	public void init(Player player) {
		if (getPayedBossCoinsIds() == null) {
			setPayedBossCoinsIds(new HashSet<String>());
		}
		setOwner(player);
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public String getBossCoinsLevel() {
		return bossCoinsLevel;
	}

	public void setBossCoinsLevel(String bossCoinsLevel) {
		this.bossCoinsLevel = bossCoinsLevel;
	}

	public HashSet<String> getPayedBossCoinsIds() {
		return payedBossCoinsIds;
	}

	public void setPayedBossCoinsIds(HashSet<String> payedBossCoinsIds) {
		this.payedBossCoinsIds = payedBossCoinsIds;
	}

	@JsonIgnore
	public void updatesStats(boolean recompute) {
		owner.getGameStats().replaceModifiers(BOSS_COINS_PAYED, getPayedStats(), recompute);
		owner.getGameStats().replaceModifiers(BOSS_COINS_LEVEL, getLevelStats(), recompute);
		owner.getLifeStats().restoreBarrier();
	}

	@JsonIgnore
	public Stat[] getLevelStats() {
		if (bossCoinsLevel == null || bossCoinsLevel.isEmpty()) {
			return null;
		}
		return BossConfig.getInstance().getBossStoreCoinResource(bossCoinsLevel).getLevelStats();
	}

	@JsonIgnore
	public List<Stat> getPayedStats() {
		if (payedBossCoinsIds.isEmpty()) {
			return null;
		}
		List<Stat> result = New.arrayList();
		for (String id : payedBossCoinsIds) {
			Stat[] stats = BossConfig.getInstance().getBossStoreCoinResource(id).getPayedStats();
			if (stats != null && stats.length != 0) {
				for (Stat stat : stats) {
					result.add(stat);
				}
			}
		}
		return result;
	}

	@JsonIgnore
	public void openModule() {
		if (getBossCoinsLevel() == null) {
			setBossCoinsLevel(BossConfig.getInstance().COINS_INIT_LEVEL_ID.getValue());
			updatesStats(true);
		}
	}
}
