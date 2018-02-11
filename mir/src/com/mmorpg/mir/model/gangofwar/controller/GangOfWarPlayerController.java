package com.mmorpg.mir.model.gangofwar.controller;

import com.mmorpg.mir.model.controllers.PlayerController;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gangofwar.manager.GangOfWar;
import com.mmorpg.mir.model.gangofwar.model.PlayerGangWarInfo;

public class GangOfWarPlayerController extends PlayerController implements GangOfWarCamp {
	/** 阵营 */
	private Camps camps;

	private GangOfWar gangOfWar;

	private long lastReliveTime;

	public GangOfWarPlayerController(GangOfWar gangOfWar) {
		this.gangOfWar = gangOfWar;
	}

	@Override
	public boolean inActivity() {
		return true;
	}

	public boolean useGod() {
		if ((System.currentTimeMillis() - lastReliveTime) < 2000) {
			return true;
		}
		return false;
	}

	@Override
	public void onRelive() {
		super.onRelive();
		lastReliveTime = System.currentTimeMillis();
	}

	@Override
	public boolean isActivityEnemy(Creature other) {
		if (other instanceof Summon) {
			other = ((Summon) other).getMaster();
		}
		if (other.getController() instanceof GangOfWarCamp) {
			GangOfWarCamp gop = (GangOfWarCamp) other.getController();
			if (other instanceof Player) {
				return getCamps() != gop.getCamps() ? true : ((Player) other).isTraitor();
			} else {
				return getCamps() != gop.getCamps() ? true : false;
			}
		}
		return other instanceof Player ? ((Player) other).isTraitor() : false;
	}

	@Override
	public void onDie(Creature lastAttacker, int skillId) {
		super.onDie(lastAttacker, skillId);
		if (lastAttacker instanceof Summon) {
			lastAttacker = ((Summon) lastAttacker).getMaster();
		}
		if (lastAttacker instanceof Player) {
			Player attacker = (Player) lastAttacker;
			PlayerGangWarInfo warInfo = gangOfWar.getPlayerMap().get(attacker.getObjectId());
			PlayerGangWarInfo owernWarInfo = gangOfWar.getPlayerMap().get(getOwner().getObjectId());
			if (warInfo != null && owernWarInfo != null) {
				warInfo.increaseKillCount(getOwner().getObjectId());
				warInfo.increaseContinueKill();
				owernWarInfo.setContinueKill(0);
			}
		}
	}

	public Camps getCamps() {
		return camps;
	}

	public void setCamps(Camps camps) {
		this.camps = camps;
	}

	public long getLastReliveTime() {
		return lastReliveTime;
	}

}
