package com.mmorpg.mir.model.skill.target;

import java.util.HashMap;
import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.skill.target.chooser.ITargetChooser;

public final class TargetManager {

	private static final Map<TargetType, ITargetChooser> targetChooserMap = new HashMap<TargetType, ITargetChooser>();

	public static void registTargetChooser(ITargetChooser chooser) {
		targetChooserMap.put(chooser.getTargtType(), chooser);
	}

	public static boolean endChooseTarget(Skill skill) {
		// 根据skill类型获取到target对象
		ITargetChooser targetChooser = targetChooserMap.get(skill.getSkillTemplate().getTargetType());
		// 根据tagetchooser装备skill的攻击目标
		return targetChooser.endChooser(skill);
	}

	public static boolean startChooseTarget(Skill skill) {
		// 根据skill类型获取到target对象
		ITargetChooser targetChooser = targetChooserMap.get(skill.getSkillTemplate().getTargetType());
		// 根据tagetchooser装备skill的攻击目标
		return targetChooser.startChooser(skill);
	}

	public static void useSkill(Player player, int skillId, long targetId, int x, int y, long[] targetList,
			byte direction) {
		// 根据skill类型获取到target对象
		ITargetChooser targetChooser = targetChooserMap.get(SkillEngine.getInstance().getSkillTargetType(skillId));
		targetChooser.useSkill(player, skillId, targetId, x, y, targetList, direction);
	}

}
