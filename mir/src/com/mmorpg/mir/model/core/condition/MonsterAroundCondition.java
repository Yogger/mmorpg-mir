package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Monster;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.utils.MathUtil;

/**
 * 目标周围的怪物数量
 * 
 * @author Kuang Hao
 * @since v1.0 2014-8-21
 * 
 */
public class MonsterAroundCondition extends AbstractCoreCondition {

	private Operator op;

	private int halfX;

	private int halfY;

	@Override
	public boolean verify(Object object) {

		if (!(object instanceof Monster)) {
			return false;
		}
		int count = 0;
		Monster monster = ((Monster) object);
		if (!monster.isSpawned()) {
			count = 0;
		}

		Creature mostHated = monster.getAggroList().getMostHated();
		if (mostHated == null) {
			return false;
		}
		for (VisibleObject vo : mostHated.getKnownList()) {
			if (vo instanceof Monster) {
				if (MathUtil.isInRange(vo, monster, halfX, halfY)) {
					if (mostHated.isEnemy((Monster) vo)) {
						count++;
					}
				}
			}
		}

		if (op == Operator.GREATER_EQUAL) {
			return count >= value;
		} else if (op == Operator.GREATER) {
			return count > value;
		} else if (op == Operator.EQUAL) {
			return count == value;
		} else if (op == Operator.LESS_EQUAL) {
			return count <= value;
		} else if (op == Operator.LESS) {
			return count < value;
		}
		throw new ManagedException(ManagedErrorCode.COPY_ALREADY_COMPLETE);
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		if (resource.getOperator() == null || resource.getOperator().isEmpty()) {
			this.op = Operator.GREATER_EQUAL;
		} else {
			this.op = resource.getOperatorEnum();
		}
		this.halfX = resource.getHalfX();
		this.halfY = resource.getHalfY();
	}

}
