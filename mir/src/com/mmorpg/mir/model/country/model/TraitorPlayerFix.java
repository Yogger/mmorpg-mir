package com.mmorpg.mir.model.country.model;

import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.effecttemplate.TraitorEffect;
import com.mmorpg.mir.model.skill.model.Skill;
import com.windforce.common.utility.DateUtils;

public class TraitorPlayerFix {

	private long playerId;
	private String name;
	private long battlePower;
	private long remainTime;
	private long lastReduceTime;

	public boolean reduceTimeAndIsRemove(Player player) {
		long now = System.currentTimeMillis();
		this.setRemainTime(this.getRemainTime() - (now - lastReduceTime));
		if (getRemainTime() <= 0) {
			player.getEffectController().removeEffect(TraitorEffect.TRAITOR);
			return true;
		} else {
			lastReduceTime = now;
		}

		return false;
	}

	public void login(Player player) {
		long now = System.currentTimeMillis();
		lastReduceTime = now;
		Skill skill = SkillEngine.getInstance().getSkill(null,
				ConfigValueManager.getInstance().COUNTRY_TRAITOR_SKILLID.getValue(), player.getObjectId(), 0, 0,
				player, null);
		skill.noEffectorUseSkill();
	}

	public static final TraitorPlayerFix valueOf(Player player) {
		TraitorPlayerFix p = new TraitorPlayerFix();
		p.playerId = player.getObjectId();
		if (player.getGameStats().statSize() == 0) {
			PlayerManager.getInstance().resetPlayerGameStats(player);
			player.getGameStats().addModifiers(CountryFlag.COUNTRY_FLAG,
					player.getCountry().getCountryFlag().getResource().getPlayerStats());
		}
		p.battlePower = player.getGameStats().calcBattleScore();
		p.name = player.getName();
		p.lastReduceTime = System.currentTimeMillis();
		p.setRemainTime(ConfigValueManager.getInstance().TRAITOR_DURATION_TIME_SECOND.getValue()
				* DateUtils.MILLIS_PER_SECOND);
		return p;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getBattlePower() {
		return battlePower;
	}

	public void setBattlePower(long battlePower) {
		this.battlePower = battlePower;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(long remainTime) {
		this.remainTime = remainTime;
	}

}
