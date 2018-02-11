package com.mmorpg.mir.model.skill.target.chooser;

import java.util.Collections;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.skill.target.TargetType;

@Component
public class SelfTargetChooser extends AbstractTargetChooser {

	@Override
	public TargetType getTargtType() {
		return TargetType.SELF;
	}

	@Override
	public boolean endChooser(Skill skill) {
		skill.setFirstTarget(skill.getEffector());
		skill.setEffectedList(Collections.singletonList(skill.getEffector()));
		return true;
	}

	@Override
	public void useSkill(Player player, int skillId, long targetId, int x, int y, long[] targetList, byte direction) {
		player.getController().useSkill(skillId, targetId, x, y, player, null);
	}

	@Override
	public boolean startChooser(Skill skill) {
		return true;
	}
}
