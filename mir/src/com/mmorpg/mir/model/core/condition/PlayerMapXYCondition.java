package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.utils.MathUtil;

public class PlayerMapXYCondition extends AbstractCoreCondition {

	private int x;
	private int y;
	private int halfX;
	private int halfY;

	@Override
	public boolean verify(Object object) {
		Player player = null;
		if(object instanceof Player){
			 player = (Player) object;
		}
		if(player == null){
			this.errorObject(object);
		}
		if (player.getMapId() == value && MathUtil.isInRange(x, y, player, halfY, halfY)) {
			return true;
		}
		throw new ManagedException(ManagedErrorCode.PLAYER_NOT_IN_POSITION);
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.x = resource.getX();
		this.y = resource.getY();
		this.halfX = resource.getHalfX();
		this.halfY = resource.getHalfY();
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHalfX() {
		return halfX;
	}

	public void setHalfX(int halfX) {
		this.halfX = halfX;
	}

	public int getHalfY() {
		return halfY;
	}

	public void setHalfY(int halfY) {
		this.halfY = halfY;
	}

}
