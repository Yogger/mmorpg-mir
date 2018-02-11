package com.mmorpg.mir.model.controllers.observer;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.skill.model.Skill;

public class ActionObserver {
	public enum ObserverType {
		// 移动
		MOVE(1 << 0),
		// 攻击
		ATTACK(1 << 1),
		// 被攻击
		ATTACKED(1 << 2),
		// 使用技能
		SKILLUSE(1 << 3),
		// 离开副本
		LEAVE_COPY(1 << 4),
		// 死亡
		DIE(1 << 5),
		// 移动完路径
		ROUTEOVER(1 << 6),
		// 人品值变化
		RPTYPECHANGE(1 << 7),
		// 出生
		SPAWN(1 << 8),
		// 看见
		SEE(1 << 9),
		// 传送
		TRANSPORT(1 << 10);

		private final int value;

		private ObserverType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}

	private int observerType;

	private boolean disposable = false;

	public ActionObserver(ObserverType... observerTypes) {
		for (ObserverType type : observerTypes) {
			this.observerType |= type.getValue();
		}
	}

	/**
	 * @return the observerType
	 */
	public boolean isMatch(ObserverType type) {
		return (observerType & type.getValue()) > 0;
	}

	public void moved() {
	};

	public void attacked(Creature creature) {
	};

	public void attack(Creature creature) {
	};

	public void skilluse(Skill skill) {
	};

	public void leaveCopy() {
	};

	public void die(Creature creature) {
	};

	public void routeOver() {

	};

	public void rpChangeType() {
	}

	public void spawn(int mapId, int instanceId) {

	}

	public void transport() {

	}

	public void see(VisibleObject visibleObject) {

	}

	public boolean isDisposable() {
		return disposable;
	}

	public void setDisposable(boolean disposable) {
		this.disposable = disposable;
	}

}
