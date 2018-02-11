package com.mmorpg.mir.model.controllers;

import java.util.Map;

import org.h2.util.New;

import com.mmorpg.mir.model.controllers.packet.SM_Die;
import com.mmorpg.mir.model.controllers.packet.SM_Relive;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.skill.model.DamageResult;
import com.mmorpg.mir.model.trigger.manager.TriggerManager;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.packet.SM_Move;

public abstract class CreatureController<T extends Creature> extends VisibleObjectController<Creature> {
	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange) {
		super.notSee(object, isOutOfRange);
		if (object == getOwner().getTarget()) {
			getOwner().setTarget(null);
		}
	}

	public void onDie(Creature lastAttacker, int skillId) {
		this.getOwner().setCasting(null);
		this.getOwner().getEffectController().dieRemoveAllEffects();
		this.getOwner().getMoveController().stop();
		this.getOwner().getObserveController().notifyDieObservers(lastAttacker);
		broadcastPacketAndReceiver(lastAttacker, skillId);
		// 触发器
		dieTrigger(lastAttacker);
	}

	protected void broadcastPacketAndReceiver(Creature lastAttacker, int skillId) {
		PacketSendUtility.broadcastPacketAndReceiver(getOwner(),
				SM_Die.valueOf(getOwner(), skillId, lastAttacker.getObjectId()));
	}

	protected void dieTrigger(Creature lastAttacker) {
		if (getOwner().getSpawn() != null && getOwner().getSpawn().getDieTriggers() != null) {
			for (String id : getOwner().getSpawn().getDieTriggers()) {
				Map<String, Object> contexts = New.hashMap();
				if (lastAttacker instanceof Player) {
					contexts.put(TriggerContextKey.PLAYER, lastAttacker);
					TriggerManager.getInstance().trigger(contexts, id);
				}
			}
		}
	}

	/**
	 * 是否在活动中
	 * 
	 * @return
	 */
	public boolean inActivity() {
		return false;
	}

	/**
	 * 在活动中,对玩家的敌我识别
	 * 
	 * @param other
	 * @return
	 */
	public boolean isActivityEnemy(Creature other) {
		return false;
	}

	@Override
	public void onRelive() {
		PacketSendUtility.broadcastPacketAndReceiver(getOwner(), SM_Relive.valueOf(getOwner(), getOwner()
				.getLifeStats().getCurrentHp(), getOwner().getLifeStats().getCurrentMp()));
	}

	public final void onAttack(Creature creature, long damage, DamageResult damageResult) {
		this.onAttack(creature, 0, damage, damageResult);
	}

	public void onAttack(Creature creature, int skillId, long damage, DamageResult damageResult) {
		getOwner().getObserveController().notifyAttackedObservers(creature);
	}

	protected void doReward() {

	}

	public void onDialogRequest(Player player) {

	}

	public void attackTarget(Creature target) {
		getOwner().getObserveController().notifyAttackObservers(target);
	}

	public void onAddDamage() {

	}

	public void onAddHate() {

	}

	public void stopMoving() {
		Creature owner = getOwner();
		World.getInstance().updatePosition(owner, owner.getX(), owner.getY(), owner.getHeading());
		// 通知观察着们自己停止了
		PacketSendUtility.broadcastPacket(owner, SM_Move.valueOf(owner, owner.getX(), owner.getY(), null, (byte) 0));
	}

	@Override
	public void delete() {
		getOwner().getMoveController().stop();
		super.delete();
	}

	public void onStartMove() {
		getOwner().getObserveController().notifyMoveObservers();
	}

	public void onMove() {

	}

	@Override
	public void onSpawn(int mapId, int instanceId) {
		super.onSpawn(mapId, instanceId);
		getOwner().getObserveController().notifySpawnObservers(mapId, instanceId); // 告诉观察者，自己切换地图了
	}

	public void onStopMove() {

	}
}
