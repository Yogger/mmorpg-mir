package com.mmorpg.mir.model.gangofwar.controller;

import com.mmorpg.mir.model.controllers.BossController;
import com.mmorpg.mir.model.gameobjects.Boss;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.skill.effect.EffectId;

public class GangOfWarGuardBossController extends BossController implements GangOfWarCamp {
	/** 阵营 */
	private Camps camps = Camps.DEFEND;
	/** 门 */
	private Boss doorBoss;

	public GangOfWarGuardBossController(Boss doorBoss) {
		this.doorBoss = doorBoss;
	}

	@Override
	public void onDie(Creature lastAttacker, int skillId) {
		super.onDie(lastAttacker, skillId);
		doorBoss.getEffectController().unsetAbnormal(EffectId.GOD.getEffectId(), true);
	}

	@Override
	public boolean isActivityEnemy(Creature other) {
		if (other instanceof Summon) {
			other = ((Summon) other).getMaster();
		}
		if (other.getController() instanceof GangOfWarCamp) {
			GangOfWarCamp gop = (GangOfWarCamp) other.getController();
			return getCamps() != gop.getCamps() ? true : false;
		}
		return false;
	}

	@Override
	public boolean inActivity() {
		return true;
	}

	@Override
	protected void broadDamage() {

	}

	public Camps getCamps() {
		return camps;
	}

	public void setCamps(Camps camps) {
		this.camps = camps;
	}
}
