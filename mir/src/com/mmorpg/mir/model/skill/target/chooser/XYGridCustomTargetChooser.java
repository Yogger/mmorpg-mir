package com.mmorpg.mir.model.skill.target.chooser;

import java.util.Iterator;
import java.util.List;

import org.h2.util.New;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.skill.resource.grid.Grid;
import com.mmorpg.mir.model.skill.target.TargetType;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;

@Component
public class XYGridCustomTargetChooser extends AbstractTargetChooser {

	@Override
	public TargetType getTargtType() {
		return TargetType.XYGRIDCUSTOM;
	}

	private boolean inGrid(int x, int y, List<Grid> grids) {
		Grid grid = Grid.valueOf(x, y);
		if (grids.contains(grid)) {
			return true;
		}
		Grid[] gridRound = new Grid[4];
		gridRound[0] = Grid.valueOf(x + 1, y);
		gridRound[1] = Grid.valueOf(x - 1, y);
		gridRound[2] = Grid.valueOf(x, y + 1);
		gridRound[3] = Grid.valueOf(x, y - 1);
		for (int i = 0; i < gridRound.length; i++) {
			if (!grids.contains(gridRound[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean endChooser(Skill skill) {
		List<Grid> grids = skill.getSkillTemplate().calc(skill.getX(), skill.getY(), skill.getDirection());
		Iterator<VisibleObject> vos = skill.getEffector().getKnownList().iterator();
		List<Creature> targets = New.arrayList();
		while (vos.hasNext()) {
			VisibleObject vo = vos.next();
			if (vo instanceof Creature) {
				Creature target = (Creature) vo;
				if (skill.getEffector().isEnemy(target) && !target.getLifeStats().isAlreadyDead()) {
					if (inGrid(target.getX(), target.getY(), grids)) {
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

	@Override
	public void useSkill(Player player, int skillId, long targetId, int x, int y, long[] targetList, byte direction) {
		if (x >= 0 && y >= 0) {
			player.getController().useSkill(skillId, targetId, x, y, null, null, direction);
		}
	}

	@Override
	public boolean startChooser(Skill skill) {
		if (!MathUtil.isInRange(skill.getX(), skill.getY(), skill.getEffector(), skill.getRange(), skill.getRange())) {
			if (skill.getEffector() instanceof Player) {
				PacketSendUtility.sendErrorMessage((Player) skill.getEffector(), ManagedErrorCode.OBJECT_TOO_LONG);
			}
			// 超过射程
			return false;
		}
		return true;
	}

}
