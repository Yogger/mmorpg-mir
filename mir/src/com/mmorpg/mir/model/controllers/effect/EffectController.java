package com.mmorpg.mir.model.controllers.effect;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.h2.util.New;

import com.mmorpg.mir.model.controllers.packet.SM_ABNORMAL_EFFECT;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.task.tasks.PacketBroadcaster.BroadcastMode;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.utils.BitSetConvert;

public class EffectController {
	private Creature owner;

	protected NonBlockingHashMap<String, Effect> provokeEffectMap = new NonBlockingHashMap<String, Effect>();
	protected NonBlockingHashMap<String, Effect> abnormalEffectMap = new NonBlockingHashMap<String, Effect>();

	protected BitSet abnormals = new BitSet();

	private Object abnormalsLock = new Object();

	public EffectController(Creature owner) {
		this.owner = owner;
	}

	/**
	 * @return the owner
	 */
	public Creature getOwner() {
		return owner;
	}

	/**
	 * 
	 * @param effect
	 */
	public void addEffect(Effect effect) {
		Map<String, Effect> mapToUpdate = getMapForEffect(effect);
		effect.addDuration(effect.getEffectsDuration());
		Effect existingEffect = mapToUpdate.get(effect.getGroup());
		if (existingEffect != null) {
			if (existingEffect.getPriority() > effect.getPriority() || !existingEffect.isReplace())
				return;

			existingEffect.replace(effect);
			existingEffect.endEffect();
		}

		mapToUpdate.put(effect.getGroup(), effect);
		effect.startEffect(false);

		if (!effect.isPassive()) {
			if (effect.isBroadcast()) {
				broadCastEffects();
			} else {
				broadCastSelf();
			}
		}
	}

	public void removeAllEffects() {
		for (Effect effect : abnormalEffectMap.values()) {
			effect.endEffect();
		}
		abnormalEffectMap.clear();
		for (Effect effect : provokeEffectMap.values()) {
			effect.endEffect();
		}
		provokeEffectMap.clear();
	}

	/**
	 * 
	 * @param effect
	 * @return
	 */
	private Map<String, Effect> getMapForEffect(Effect effect) {
		if (effect.isProvoked())
			return provokeEffectMap;

		return abnormalEffectMap;
	}

	/**
	 * 
	 * @param stack
	 * @return abnormalEffectMap
	 */
	public Effect getAnormalEffect(String stack) {
		return abnormalEffectMap.get(stack);
	}

	public boolean contains(String stack) {
		return getAnormalEffect(stack) == null ? false : true;
	}

	public byte[] bitSet2ByteArray() {
		synchronized (abnormalsLock) {
			return BitSetConvert.bitSet2ByteArray(abnormals);
		}
	}

	public boolean containsSkill(int skillId) {
		for (Effect effect : abnormalEffectMap.values()) {
			if (effect.getSkillId() == skillId) {
				return true;
			}
		}
		return false;
	}

	public void broadCastEffects() {
		owner.addPacketBroadcastMask(BroadcastMode.BROAD_CAST_EFFECTS);
	}

	/**
	 * Broadcasts current effects to all visible objects
	 */
	public void broadCastEffectsImp() {
		synchronized (abnormalsLock) {
			List<Effect> effects = getAbnormalEffects();
			PacketSendUtility.broadcastPacketAndReceiver(getOwner(), new SM_ABNORMAL_EFFECT(getOwner().getObjectId(),
					BitSetConvert.bitSet2ByteArray(abnormals), effects));
		}
	}

	public void broadCastSelf() {
		if (getOwner() instanceof Player) {
			synchronized (abnormalsLock) {
				List<Effect> effects = getAbnormalEffects();
				PacketSendUtility.sendPacket((Player) getOwner(), new SM_ABNORMAL_EFFECT(getOwner().getObjectId(),
						BitSetConvert.bitSet2ByteArray(abnormals), effects));
			}
		}
	}

	/**
	 * 
	 * @param effect
	 */
	public void clearEffect(Effect effect) {
		abnormalEffectMap.remove(effect.getGroup());
		broadCastEffects();
	}

	/**
	 * Removes the effect by skillid.
	 * 
	 * @param skillid
	 */
	public void removeEffect(int skillid) {
		for (Effect effect : abnormalEffectMap.values()) {
			if (effect.getSkillId() == skillid) {
				effect.endEffect();
				abnormalEffectMap.remove(effect.getGroup());
			}
		}
	}

	/**
	 * Removes the effect by group.
	 * 
	 * @param skillid
	 */
	public void removeEffect(String group) {
		for (Effect effect : abnormalEffectMap.values()) {
			if (group.equals(effect.getGroup())) {
				effect.endEffect();
				abnormalEffectMap.remove(effect.getGroup());
			}
		}
	}

	/**
	 * Removes the effect by skillid.
	 * 
	 * @param skillid
	 */
	public void removeProvokedEffect(int skillid) {
		for (Effect effect : provokeEffectMap.values()) {
			if (effect.getSkillId() == skillid) {
				effect.endEffect();
				provokeEffectMap.remove(effect.getGroup());
			}
		}
	}

	/**
	 * Removes all effects from controllers and ends them appropriately Passive
	 * effect will not be removed
	 */
	public void dieRemoveAllEffects() {
		List<String> removes = New.arrayList();
		for (Entry<String, Effect> effect : abnormalEffectMap.entrySet()) {
			if (effect.getValue().isDeadRemove()) {
				effect.getValue().endEffect();
				removes.add(effect.getKey());
			}
		}
		for (String remove : removes) {
			abnormalEffectMap.remove(remove);
		}
	}

	/**
	 * @return copy of anbornals list
	 */
	public List<Effect> getAbnormalEffects() {
		List<Effect> effects = new ArrayList<Effect>();
		Iterator<Effect> iterator = iterator();
		while (iterator.hasNext()) {
			Effect effect = iterator.next();
			if (effect != null)
				effects.add(effect);
		}
		return effects;
	}

	/**
	 * ABNORMAL EFFECTS
	 */
	public void setAbnormal(int mask) {
		setAbnormal(mask, false);
	}

	public void setAbnormal(EffectId effectId, boolean broadcast) {
		setAbnormal(effectId.getEffectId(), broadcast);
	}

	public void setAbnormal(int mask, boolean broadcast) {
		synchronized (abnormalsLock) {
			abnormals.set(mask);
			if (broadcast) {
				broadCastEffects();
			}
		}
	}

	public void unsetAbnormal(int mask) {
		unsetAbnormal(mask, false);
	}

	public void unsetAbnormal(EffectId effectId) {
		unsetAbnormal(effectId.getEffectId(), false);
	}

	public void unsetAbnormal(EffectId effectId, boolean broadcast) {
		unsetAbnormal(effectId.getEffectId(), broadcast);
	}

	public void unsetAbnormal(int mask, boolean broadcast) {
		synchronized (abnormalsLock) {
			abnormals.set(mask, false);
			if (broadcast) {
				broadCastEffects();
			}
		}
	}

	public BitSet getAbnormals() {
		return abnormals;
	}

	/**
	 * 
	 * @return
	 */
	public Iterator<Effect> iterator() {
		return abnormalEffectMap.values().iterator();
	}

	public boolean isAbnoramlSet(EffectId silence) {
		synchronized (abnormalsLock) {
			return abnormals.get(silence.getEffectId());
		}
	}
}
