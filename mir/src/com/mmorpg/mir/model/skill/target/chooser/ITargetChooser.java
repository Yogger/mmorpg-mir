package com.mmorpg.mir.model.skill.target.chooser;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.skill.target.TargetType;

public interface ITargetChooser {
	public TargetType getTargtType();

	public boolean endChooser(Skill skill);

	public void useSkill(Player player, int skillId, long targetId, int x, int y, long[] targetList, byte direction);

	public boolean startChooser(Skill skill);
}
