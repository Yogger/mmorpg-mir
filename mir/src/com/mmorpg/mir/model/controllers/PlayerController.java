package com.mmorpg.mir.model.controllers;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.cliffc.high_scale_lib.NonBlockingHashSet;

import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.controllers.packet.SM_PlayerDie;
import com.mmorpg.mir.model.exchange.manager.ExchangeManager;
import com.mmorpg.mir.model.gameobjects.BigBrother;
import com.mmorpg.mir.model.gameobjects.Boss;
import com.mmorpg.mir.model.gameobjects.CountryNpc;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.DropObject;
import com.mmorpg.mir.model.gameobjects.Gatherable;
import com.mmorpg.mir.model.gameobjects.Lorry;
import com.mmorpg.mir.model.gameobjects.Monster;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Robot;
import com.mmorpg.mir.model.gameobjects.Sculpture;
import com.mmorpg.mir.model.gameobjects.StatusNpc;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.TownPlayerNpc;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.gangofwar.config.GangOfWarConfig;
import com.mmorpg.mir.model.kingofwar.config.KingOfWarConfig;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.player.event.KillPlayerEvent;
import com.mmorpg.mir.model.player.event.PlayerDieEvent;
import com.mmorpg.mir.model.player.packet.SM_BigBrotherInfo;
import com.mmorpg.mir.model.player.packet.SM_BossInfo;
import com.mmorpg.mir.model.player.packet.SM_CountryNpcInfo;
import com.mmorpg.mir.model.player.packet.SM_DropObjectInfo;
import com.mmorpg.mir.model.player.packet.SM_GatherableInfo;
import com.mmorpg.mir.model.player.packet.SM_LorryInfo;
import com.mmorpg.mir.model.player.packet.SM_MonsterInfo;
import com.mmorpg.mir.model.player.packet.SM_PlayerInfo;
import com.mmorpg.mir.model.player.packet.SM_SculptureInfo;
import com.mmorpg.mir.model.player.packet.SM_StatusNpcInfo;
import com.mmorpg.mir.model.player.packet.SM_SummonInfo;
import com.mmorpg.mir.model.player.packet.SM_TownPlayerNpc;
import com.mmorpg.mir.model.relive.manager.PlayerReliveManager;
import com.mmorpg.mir.model.restrictions.PlayerRestrictions;
import com.mmorpg.mir.model.restrictions.RestrictionsManager;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.model.DamageResult;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.skill.packet.SM_Attack;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.packet.SM_Move;
import com.mmorpg.mir.model.world.packet.SM_Remove;
import com.mmorpg.mir.model.world.resource.MapResource;
import com.windforce.common.event.core.EventBusManager;

public class PlayerController extends CreatureController<Player> {

	private Logger logger = Logger.getLogger(PlayerController.class);

	public static final long STORAGE_SIZE = 5L;

	private NonBlockingHashMap<Long, Set<Player>> damageHistory = new NonBlockingHashMap<Long, Set<Player>>();

	private long pioneerTime;

	public Map<Long, Set<Player>> getDamageHistory() {
		return damageHistory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void see(VisibleObject object) {
		super.see(object);
		// 发送一个消息，通知给自己，看到了一个东东（这里需要根据类型来传输不同的对象，减少流量）
		if (object instanceof Player) {
			PacketSendUtility.sendPacket(getOwner(), SM_PlayerInfo.valueOf((Player) object));
		} else if (object.getObjectType() == ObjectType.COUNTRY_NPC) {
			PacketSendUtility.sendPacket(getOwner(), SM_CountryNpcInfo.valueOf((CountryNpc) object));
		} else if (object.getObjectType() == ObjectType.MONSTER) {
			PacketSendUtility.sendPacket(getOwner(), SM_MonsterInfo.valueOf((Monster) object));
		} else if (object instanceof DropObject) {
			PacketSendUtility.sendPacket(getOwner(), SM_DropObjectInfo.valueOf((DropObject) object));
		} else if (object.getObjectType() == ObjectType.GATHERABLE) {
			PacketSendUtility.sendPacket(getOwner(), SM_GatherableInfo.valueOf((Gatherable) object));
		} else if (object.getObjectType() == ObjectType.SUMMON) {
			PacketSendUtility.sendPacket(getOwner(), SM_SummonInfo.valueOf((Summon) object));
		} else if (object.getObjectType() == ObjectType.LORRY) {
			PacketSendUtility.sendPacket(getOwner(), SM_LorryInfo.valueOf((Lorry) object));
		} else if (object.getObjectType() == ObjectType.STATUS_NPC) {
			PacketSendUtility.sendPacket(getOwner(), SM_StatusNpcInfo.valueOf((StatusNpc) object));
		} else if (object.getObjectType() == ObjectType.BOSS) {
			PacketSendUtility.sendPacket(getOwner(), SM_BossInfo.valueOf((Boss) object));
		} else if (object.getObjectType() == ObjectType.ROBOT) {
			PacketSendUtility.sendPacket(getOwner(), SM_CountryNpcInfo.valueOf((Robot) object));
		} else if (object.getObjectType() == ObjectType.SCULPTURE) {
			PacketSendUtility.sendPacket(getOwner(), SM_SculptureInfo.valueOf((Sculpture) object));
		} else if (object.getObjectType() == ObjectType.BIGBROTHER) {
			PacketSendUtility.sendPacket(getOwner(), SM_BigBrotherInfo.valueOf((BigBrother) object));
		} else if (object.getObjectType() == ObjectType.TOWN_PLAYER_NPC) {
			PacketSendUtility.sendPacket(getOwner(), SM_TownPlayerNpc.valueOf((TownPlayerNpc) object));
		} else if (object.getObjectType() == ObjectType.SERVANT) {
			PacketSendUtility.sendPacket(getOwner(), SM_SummonInfo.valueOf((Summon) object));
		}

		// 当玩家新看到一个对象的时候，如果这个对象正在移动，那么就应该把这个对象的移动信息也发送给前端
		if (object instanceof Creature) {
			Creature creature = (Creature) object;
			byte[] leftRoads = creature.getMoveController().getLeftRoads();
			if (leftRoads != null) {
				PacketSendUtility.sendPacket(getOwner(),
						SM_Move.valueOf(creature, creature.getX(), creature.getY(), leftRoads, (byte) 1));
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange) {
		super.notSee(object, isOutOfRange);
		PacketSendUtility.sendPacket(getOwner(), SM_Remove.valueOf(object.getObjectId()));
	}

	@Override
	public void onRelive() {
		super.onRelive();
		for (VisibleObject object : getOwner().getKnownList()) {
			if (object instanceof Npc) {
				((Npc) object).getAi().handleEvent(Event.SEE_PLAYER);
			}
		}
	}

	/**
	 * 玩家使用技能
	 * 
	 * @param skillId
	 *            技能id
	 * @param x
	 *            非指向性技能的x坐标
	 * @param y
	 *            非指向性技能的y坐标
	 * @param target
	 *            指向性技能用来通知的第一个目标
	 * @param creatures
	 *            指向性技能的目标集合(不包含target)
	 */
	public void useSkill(int skillId, long targetId, int x, int y, Creature target, List<Creature> creatureList) {
		this.useSkill(skillId, targetId, x, y, target, creatureList, (byte) 0);

		if (target != null) {
			for (Summon summon : getOwner().getSummons().values()) {
				if (summon.isEnemy(target)) {
					summon.getAggroList().addHate(target, 1);
				}
			}
		} else {
			if (creatureList != null) {
				for (Creature creature : creatureList) {
					for (Summon summon : getOwner().getSummons().values()) {
						if (summon.isEnemy(creature)) {
							summon.getAggroList().addHate(creature, 1);
						}
					}
				}
			}
		}
	}

	public void useSkill(int skillId, long targetId, int x, int y, Creature target, List<Creature> creatureList,
			byte direction) {
		Player player = getOwner();
		Skill skill = SkillEngine.getInstance()
				.getSkillForPlayer(player, skillId, targetId, x, y, target, creatureList);
		if (skill != null) {
			skill.setDirection(direction);
			// 这里进行权限的验证(各种状态的初判断可以在这里进行)
			if (PlayerRestrictions.getInstance().canUseSkill(player, skill)) {
				skill.useSkill();
			}
		} else {
			logger.warn(String.format("玩家 id:[%s] name:[%s] 使用了一个非法的技能 skillId:[%s]", player.getObjectId(),
					player.getName(), skillId));
		}
	}

	@Override
	public void attackTarget(Creature target) {
		Player player = getOwner();

		/**
		 * Check all prerequisites
		 */
		if (target == null || !player.canAttack())
			return;

		if (!RestrictionsManager.canAttack(player, target))
			return;

		super.attackTarget(target);

		int damage = 10;

		byte attackType = 0; // TODO investigate attack types
		PacketSendUtility.broadcastPacket(player, SM_Attack.valueOf(player, target, attackType, damage), true);
		DamageResult damageResult = DamageResult.valueOf();
		target.getController().onAttack(player, damage, damageResult);
	}

	@Override
	public void onAttack(Creature creature, int skillId, long damage, DamageResult damageResult) {
		long currentTime = System.currentTimeMillis();

		if (getOwner().getLifeStats().isAlreadyDead())
			return;
		/*
		 * if (damage > getOwner().getLifeStats().getCurrentHp()) { damage =
		 * getOwner().getLifeStats().getCurrentHp() + 1; }
		 */

		doOnAttack(creature, damage);

		if (creature instanceof Player) {
			getOwner().setLastPlayerAttackedTime(currentTime);
			long key = currentTime / DateUtils.MILLIS_PER_SECOND;
			if (damageHistory.get(key) == null) {
				Set<Player> secondSet = new NonBlockingHashSet<Player>();
				damageHistory.put(key, secondSet);
			}
			damageHistory.get(key).add((Player) creature);
			if (pioneerTime == 0L) {
				pioneerTime = key;
			} else if (key - pioneerTime > STORAGE_SIZE) {
				long deadLine = key - STORAGE_SIZE;
				while (pioneerTime < deadLine) {
					damageHistory.remove(pioneerTime);
					pioneerTime += 1;
				}
			}
		}

		long barrier = getOwner().getGameStats().getCurrentStat(StatEnum.BARRIER_PERCENT);
		if (barrier != 0) {
			// 防护值抵消
			long barrierDamage = Math.round(damage * 1.0 * Math.min(barrier, 10000) / 10000);
			long currentBarrier = getOwner().getLifeStats().getCurrentBarrier();
			if (currentBarrier < barrierDamage) {
				getOwner().getLifeStats().reduceBarrier(currentBarrier);
				barrierDamage = currentBarrier;
			} else {
				getOwner().getLifeStats().reduceBarrier(barrierDamage);
			}
			getOwner().getLifeStats().reduceHp(damage - barrierDamage, creature, skillId);
			damageResult.setDamage(damage - barrierDamage);
		} else {
			getOwner().getLifeStats().reduceHp(damage, creature, skillId);
		}
		super.onAttack(creature, skillId, damage, damageResult);
	}

	private void doOnAttack(Creature creature, long damage) {
		for (Entry<String, Summon> summon : getOwner().getSummons().entrySet()) {
			summon.getValue().getAggroList().addHate(creature, damage);
		}
	}

	@Override
	public void onMove() {
		super.onMove();
	}

	@Override
	public void onStopMove() {
		super.onStopMove();
	}

	@Override
	public void onStartMove() {
		super.onStartMove();
		getOwner().updateCasting(null);
	}

	@Override
	public Player getOwner() {
		return (Player) super.getOwner();
	}

	@Override
	public void onDie(Creature lastAttacker, int skillId) {
		Player player = this.getOwner();

		super.onDie(lastAttacker, skillId);

		// 玩家死亡了以后，应该是先通知周围的玩家，这个玩家死亡了
		if (lastAttacker instanceof Player) {
			if (player.getMapId() != KingOfWarConfig.getInstance().MAPID.getValue()
					&& player.getMapId() != GangOfWarConfig.getInstance().MAPID.getValue()) {
				PlayerDieEvent event = PlayerDieEvent.valueOf(getOwner().getObjectId(), lastAttacker.getObjectId(),
						player.getCountryValue() != ((Player) lastAttacker).getCountryValue());
				EventBusManager.getInstance().syncSubmit(event);
				KillPlayerEvent killEvent = KillPlayerEvent.valueOf(lastAttacker.getObjectId(), getOwner()
						.getObjectId(), player.getCountryValue() != ((Player) lastAttacker).getCountryValue());
				EventBusManager.getInstance().submit(killEvent);
			}
		}

		player.getLifeStats().setLastAttackerName(lastAttacker.getName());
		player.getLifeStats().setLastAttackerId(lastAttacker.getObjectId());
		player.getLifeStats().setLastDeadTime(System.currentTimeMillis());
		doOnDie();
		// 然后再通知自己，自己死亡了
		logger.debug(String.format("player [%s] is dead !", player.getPlayerEnt().getAccountName()));
	}

	@Override
	public void broadcastPacketAndReceiver(Creature lastAttacker, int skillId) {
		int countryValue = 0;
		if (lastAttacker instanceof Player) {
			int other = ((Player) lastAttacker).getCountryValue();
			countryValue = (other != getOwner().getCountryValue() ? other : 0);
		}
		int reliveId = World.getInstance().getMapResource(getOwner().getMapId()).getReliveBaseResourceId();
		boolean buyLife = PlayerReliveManager.getInstance().getReliveResource(reliveId).isAllowBuyLife();
		PacketSendUtility.broadcastPacketAndReceiver(
				getOwner(),
				SM_PlayerDie.valueOf(getOwner(), skillId, lastAttacker.getObjectId(), lastAttacker.getName(),
						System.currentTimeMillis(), getOwner().getLifeStats().getBuyCounts(), countryValue, buyLife));
	}

	private void doOnDie() {

		getOwner().getBeautyGirlPool().doRest();
		// 取消交易
		if (getOwner().isTrading()) {
			ExchangeManager.getInstance().cancelExchange(getOwner(), ManagedErrorCode.DEAD_ERROR);
		}
	}

	@Override
	public void onSpawn(int mapId, int instanceId) {
		super.onSpawn(mapId, instanceId);
		getOwner().getMoveController().schedule();
		getOwner().getLifeStats().restoreDp();
		getOwner().getLifeStats().restoreBarrier();
		MapResource mapResource = World.getInstance().getMapResource(mapId);
		if (mapResource.getRemoveEffectGroups() != null) {
			for (String skillGroup : mapResource.getRemoveEffectGroups()) {
				getOwner().getEffectController().removeEffect(skillGroup);
			}
		}

	}

	public void onDespawn() {
		getOwner().clearFlagAllianceState();
	}

	public boolean canUserSkill(Integer skillId) {
		return false;
	}
}