package com.mmorpg.mir.model.gangofwar.controller;

import com.mmorpg.mir.model.controllers.StatusNpcController;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gangofwar.manager.GangOfWar;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.model.Skill;

public class ReliveGodStatusNpcController extends StatusNpcController {

	public static int GOD_SKILL_ID = 20046;

	private Phase phase;
	private GangOfWar gangOfWar;

	synchronized public void playerChangeStatus(Player player) {
	}

	@Override
	public void see(VisibleObject object) {
		if (object instanceof Player) {
			Player player = (Player) object;
			if (phase == Phase.DEFEND && player.getGang() == getGangOfWar().getDefendGang()) {
				if (player.getController() instanceof GangOfWarPlayerController) {
					if (((GangOfWarPlayerController) player.getController()).useGod()) {
						Skill skill = SkillEngine.getInstance().getSkill(null, GOD_SKILL_ID, player.getObjectId(), 0,
								0, player, null);
						skill.noEffectorUseSkill();
					}
				}
			}
			if (phase != Phase.DEFEND && player.getGang() != getGangOfWar().getDefendGang()) {
				if (player.getController() instanceof GangOfWarPlayerController) {
					if (((GangOfWarPlayerController) player.getController()).useGod()) {
						Skill skill = SkillEngine.getInstance().getSkill(null, GOD_SKILL_ID, player.getObjectId(), 0,
								0, player, null);
						skill.noEffectorUseSkill();
					}
				}
			}
		}
	}

	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange) {
	}

	public Phase getPhase() {
		return phase;
	}

	public void setPhase(Phase phase) {
		this.phase = phase;
	}

	public GangOfWar getGangOfWar() {
		return gangOfWar;
	}

	public void setGangOfWar(GangOfWar gangOfWar) {
		this.gangOfWar = gangOfWar;
	}

}
