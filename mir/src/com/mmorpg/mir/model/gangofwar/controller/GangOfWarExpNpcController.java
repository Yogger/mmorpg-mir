package com.mmorpg.mir.model.gangofwar.controller;

import com.mmorpg.mir.model.controllers.StatusNpcController;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.model.Skill;

/**
 * 经验NPC
 * 
 * @author Kuang Hao
 * @since v1.0 2014-11-6
 * 
 */
public class GangOfWarExpNpcController extends StatusNpcController {

	@Override
	public void see(VisibleObject object) {
		super.see(object);
		if (object instanceof Player) {
			Player player = (Player) object;
			// 添加BUFF
			// 释放经验加层BUFF
			Skill skill = SkillEngine.getInstance().getSkill(null, getOwner().getSpawn().getSeePlayerUseSkill(),
					player.getObjectId(), 0, 0, player, null);
			skill.noEffectorUseSkill();
		}
	}
}
