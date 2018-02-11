package com.mmorpg.mir.model.ai.event;

public enum Event {
	/**
	 * This event is received on each enemy attack
	 */
	ATTACKED,
	/**
	 * Target is too far or long time passed since last attak
	 */
	TIRED_ATTACKING_TARGET,
	/**
	 * During attack most hated creature changed from current target
	 */
	MOST_HATED_CHANGED,
	/**
	 * In active state there is nothing to do
	 */
	NOTHING_TODO,
	/**
	 * Npc is far from spawn point
	 */
	FAR_FROM_HOME,
	/**
	 * Npc returned to spawn point
	 */
	BACK_HOME,
	/**
	 * Npc Route is over
	 */
	ROUTE_OVER,
	/**
	 * Npc restored health fully (after returning to home)
	 */
	RESTORED_HEALTH,
	/**
	 * Npc sees another player
	 */
	SEE_PLAYER,
	/**
	 * Player removed from knownlist
	 */
	NOT_SEE_PLAYER,
	/**
	 * Any creature is in the visible radius
	 */
	SEE_CREATURE,
	/**
	 * Creature removed from knownlist
	 */
	NOT_SEE_CREATURE,
	/**
	 * Talk request
	 */
	TALK,
	/**
	 * Npc is respawned
	 */
	RESPAWNED,
	/**
	 * Creature died
	 */
	DIED,
	/**
	 * DayTime changed
	 */
	DAYTIME_CHANGE,
	/**
	 * Despawn service was called
	 */
	DESPAWN,
	/**
	 * bring into world was called
	 */
	SPAWN,
	/**
	 * delete from world
	 */
	DELETE,
}
