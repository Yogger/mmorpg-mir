package com.mmorpg.mir.model.controllers;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.skill.model.Skill;

public class ObserveController {

	protected Queue<ActionObserver> observers = new ConcurrentLinkedQueue<ActionObserver>();

	/**
	 * 
	 * @param observer
	 */
	public void attach(ActionObserver observer) {
		observer.setDisposable(true);
		observers.add(observer);
	}

	public void clear() {
		observers.clear();
	}

	/**
	 * notify that creature moved
	 */
	protected void notifyMoveObservers() {
		Iterator<ActionObserver> ite = observers.iterator();
		while (ite.hasNext()) {
			ActionObserver observer = ite.next();
			if (observer.isDisposable()) {
				if (observer.isMatch(ObserverType.MOVE)) {
					observer.moved();
					ite.remove();
				}
			} else {
				observer.moved();
			}
		}
	}

	/**
	 * notify that creature moved
	 */
	protected void notifySeeObservers(VisibleObject visibleObject) {
		Iterator<ActionObserver> ite = observers.iterator();
		while (ite.hasNext()) {
			ActionObserver observer = ite.next();
			if (observer.isDisposable()) {
				if (observer.isMatch(ObserverType.SEE)) {
					observer.see(visibleObject);
					ite.remove();
				}
			} else {
				observer.see(visibleObject);
			}
		}
	}

	public void notifyTransportObservers() {
		Iterator<ActionObserver> ite = observers.iterator();
		while (ite.hasNext()) {
			ActionObserver observer = ite.next();
			if (observer.isDisposable()) {
				if (observer.isMatch(ObserverType.TRANSPORT)) {
					observer.transport();
					ite.remove();
				}
			} else {
				observer.transport();
			}
		}
	}

	public void notifyLeaveCopyObservers(String copyResourceId) {
		Iterator<ActionObserver> ite = observers.iterator();
		while (ite.hasNext()) {
			ActionObserver observer = ite.next();
			if (observer.isDisposable()) {
				if (observer.isMatch(ObserverType.LEAVE_COPY)) {
					observer.leaveCopy();
					ite.remove();
				}
			} else {
				observer.leaveCopy();
			}
		}
	}

	/**
	 * notify that creature attacking
	 */
	public void notifyAttackObservers(Creature creature) {
		Iterator<ActionObserver> ite = observers.iterator();
		while (ite.hasNext()) {
			ActionObserver observer = ite.next();
			if (observer.isDisposable()) {
				if (observer.isMatch(ObserverType.ATTACK)) {
					observer.attack(creature);
					ite.remove();
				}
			} else {
				observer.attack(creature);
			}
		}
	}

	/**
	 * notify that creature attacked
	 */
	protected void notifyAttackedObservers(Creature creature) {
		Iterator<ActionObserver> ite = observers.iterator();
		while (ite.hasNext()) {
			ActionObserver observer = ite.next();
			if (observer.isDisposable()) {
				if (observer.isMatch(ObserverType.ATTACKED)) {
					observer.attacked(creature);
					ite.remove();
				}
			} else {
				observer.attacked(creature);
			}
		}
	}

	/**
	 * notify that creature used a skill
	 */
	public void notifySkilluseObservers(Skill skill) {
		Iterator<ActionObserver> ite = observers.iterator();
		while (ite.hasNext()) {
			ActionObserver observer = ite.next();
			if (observer.isDisposable()) {
				if (observer.isMatch(ObserverType.SKILLUSE)) {
					observer.skilluse(skill);
					ite.remove();
				}
			} else {
				observer.skilluse(skill);
			}
		}
	}

	public void notifyDieObservers(Creature creature) {
		Iterator<ActionObserver> ite = observers.iterator();
		while (ite.hasNext()) {
			ActionObserver observer = ite.next();
			if (observer.isDisposable()) {
				if (observer.isMatch(ObserverType.DIE)) {
					observer.die(creature);
					ite.remove();
				}
			} else {
				observer.die(creature);
			}
		}
	}

	public void notifyRouteOverObservers() {
		Iterator<ActionObserver> ite = observers.iterator();
		while (ite.hasNext()) {
			ActionObserver observer = ite.next();
			if (observer.isDisposable()) {
				if (observer.isMatch(ObserverType.ROUTEOVER)) {
					observer.routeOver();
					ite.remove();
				}
			} else {
				observer.routeOver();
			}
		}
	}

	public void notifyRPTypeChangeObservers() {
		Iterator<ActionObserver> ite = observers.iterator();
		while (ite.hasNext()) {
			ActionObserver observer = ite.next();
			if (observer.isDisposable()) {
				if (observer.isMatch(ObserverType.RPTYPECHANGE)) {
					observer.rpChangeType();
					ite.remove();
				}
			} else {
				observer.rpChangeType();
			}
		}
	}

	public void notifySpawnObservers(int mapId, int instanceId) {
		Iterator<ActionObserver> ite = observers.iterator();
		while (ite.hasNext()) {
			ActionObserver observer = ite.next();
			if (observer.isDisposable()) {
				if (observer.isMatch(ObserverType.SPAWN)) {
					observer.spawn(mapId, instanceId);
					ite.remove();
				}
			} else {
				observer.spawn(mapId, instanceId);
			}
		}
	}

	public void addObserver(ActionObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(ActionObserver observer) {
		observers.remove(observer);
	}

}
