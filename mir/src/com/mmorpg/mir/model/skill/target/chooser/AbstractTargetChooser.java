package com.mmorpg.mir.model.skill.target.chooser;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.skill.target.TargetManager;
import com.mmorpg.mir.model.utils.MathUtil;

public abstract class AbstractTargetChooser implements ITargetChooser {

	@PostConstruct
	public void init() {
		TargetManager.registTargetChooser(this);
	}

	public boolean xyChooser(Skill skill) {
		Iterator<VisibleObject> vos = skill.getEffector().getKnownList().iterator();
		List<Creature> targets = New.arrayList();
		while (vos.hasNext()) {
			VisibleObject vo = vos.next();
			if (vo instanceof Creature) {
				Creature target = (Creature) vo;
				if (xyTargetSelect(skill, target)) {
					if (MathUtil.isInRange(skill.getX(), skill.getY(), target, skill.getSkillTemplate().getRangeX(),
							skill.getSkillTemplate().getRangeY())) {
						targets.add(target);
					}
				}
			}
			if (targets.size() >= skill.getMaxTarget()) {
				break;
			}
		}
		skill.setEffectedList(targets);
		return true;
	}

	protected boolean xyTargetSelect(Skill skill, Creature target) {
		throw new RuntimeException("没有合适的目标判断实现方法");
	}

	public void removeDie(List<Creature> dies) {
		List<Creature> dd = New.arrayList();
		for (Creature creature : dies) {
			if (creature.getLifeStats().isAlreadyDead()) {
				dd.add(creature);
			}
		}
		for (Creature d : dd) {
			dies.remove(d);
		}
	}

}
