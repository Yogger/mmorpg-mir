package com.mmorpg.mir.model.controllers;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Future;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.event.Event;
import com.mmorpg.mir.model.boss.config.BossConfig;
import com.mmorpg.mir.model.boss.manager.BossManager;
import com.mmorpg.mir.model.controllers.attack.AggroList;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Monster;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.event.MonsterKillEvent;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.model.DamageResult;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.skill.packet.SM_Attack;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;

public class NpcController extends CreatureController<Npc> {

	protected Future<?> damageBroadFuture;

	@Override
	public Npc getOwner() {
		return (Npc) super.getOwner();
	}

	@Override
	public void onAttack(Creature creature, int skillId, long damage, DamageResult damageResult) {
		if (getOwner().getLifeStats().isAlreadyDead())
			return;

		// 添加仇恨
		// getOwner().getAggroList().addDamage(creature, damage);
		// getOwner().addDamage(creature, damage);

		if (creature instanceof Summon) {
			Summon servant = (Summon) creature;
			getOwner().getAggroList().addDamage(servant.getMaster(), damage);
			getOwner().addDamage(servant.getMaster(), damage);
		} else {
			getOwner().getAggroList().addDamage(creature, damage);
			getOwner().addDamage(creature, damage);
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

		if (getOwner().getObjectResource().getAddBossCoins() > 0) {
			if (creature instanceof Summon) {
				getOwner().getOnAttackTime().put(((Summon) creature).getMaster().getObjectId(),
						System.currentTimeMillis());
			} else {
				getOwner().getOnAttackTime().put(creature.getObjectId(), System.currentTimeMillis());
			}
		}

		super.onAttack(creature, skillId, damage, damageResult);

	}

	synchronized protected void broadDamage() {

	}

	@Override
	public void see(VisibleObject object) {
		super.see(object);

		Npc owner = getOwner();

		if (object instanceof Player) {
			owner.getAi().handleEvent(Event.SEE_PLAYER);
		}
		getOwner().getObserveController().notifySeeObservers(object);
	}

	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange) {
		super.notSee(object, isOutOfRange);

		if (object instanceof Creature)
			getOwner().getAggroList().remove((Creature) object);
		if (object instanceof Player)
			getOwner().getAi().handleEvent(Event.NOT_SEE_PLAYER);

	}

	@Override
	public void attackTarget(Creature target) {
		Npc npc = getOwner();

		/**
		 * Check all prerequisites
		 */
		if (npc == null || npc.getLifeStats().isAlreadyDead() || !npc.isSpawned())
			return;

		if (!npc.canAttack())
			return;

		AI ai = npc.getAi();

		if (target == null || target.getLifeStats().isAlreadyDead()) {
			ai.handleEvent(Event.MOST_HATED_CHANGED);
			return;
		}

		/**
		 * notify attack observers
		 */
		super.attackTarget(target);

		// TODO 这里先默认写一个伤害

		int damage = 10;
		byte attackType = 0;

		PacketSendUtility.broadcastPacket(npc, SM_Attack.valueOf(getOwner(), target, attackType, damage));
		DamageResult damageResult = DamageResult.valueOf();
		target.getController().onAttack(npc, damage, damageResult);
	}

	protected void doReward(Creature lastAttacker, AggroList aggroList) {
		if (!(getOwner() instanceof Monster)) {
			return;
		}
		// 获取伤害第一的人
		Player mostDamagePlayer = getOwner().getDamageRank().get(1);
		if (mostDamagePlayer == null) {
			// 这个应该不可能发生
			switch (lastAttacker.getObjectType()) {
			case PLAYER:
				mostDamagePlayer = (Player) lastAttacker;
				break;
			case SUMMON:
				mostDamagePlayer = ((Summon) lastAttacker).getMaster();
				break;
			default:
				return;
			}
		}

		if (mostDamagePlayer == null) {
			return;
		}

		// 击杀记录
		if (getOwner().getObjectResource().isDropRecord()) {
			mostDamagePlayer.getDropHistory().add(getOwner().getObjectKey());
			ServerState.getInstance().addKill(getOwner().getObjectKey());
		}
		// 这里需要将东西掉落在地上
		Collection<Player> mostDamagePlayers = getOwner().getMostDamagePlayers();
		if (mostDamagePlayers == null) {
			return;
		}
		SpawnManager.getInstance().spawnDropObject(lastAttacker, mostDamagePlayer, getOwner(), mostDamagePlayers);
		int monsterLevel = getOwner().getLevel();

		// 击杀共享
		if (getOwner().getObjectResource().isShare()) {
			Set<Player> sended = new HashSet<Player>();
			for (Entry<Integer, Player> entry : getOwner().getDamageRank().entrySet()) {
				boolean knowPlayer = getOwner().getKnownList().knowns(entry.getValue());

				// 发奖，这里需要根据玩家伤害单独发奖,先处理任务共享的问题
				if (entry.getValue().isInGroup()) {
					entry.getValue()
							.getPlayerGroup()
							.submitHuntEvent(getOwner().getObjectKey(), getOwner().getSpawnKey(), sended,
									entry.getValue(), true, monsterLevel, mostDamagePlayer.getObjectId(), knowPlayer);
				} else {
					sended.add(entry.getValue());
					MonsterKillEvent event = MonsterKillEvent.valueOf(entry.getValue().getObjectId(), this.getOwner()
							.getObjectKey(), false, monsterLevel, getOwner().getSpawnKey(), mostDamagePlayer
							.getObjectId(), knowPlayer);
					if (entry.getValue().equals(mostDamagePlayer))
						event.setUseForMedal(true);
					EventBusManager.getInstance().submit(event);
				}

			}
		} else {
			boolean knowPlayer = getOwner().getKnownList().knowns(mostDamagePlayer);
			MonsterKillEvent event = MonsterKillEvent.valueOf(mostDamagePlayer.getObjectId(), this.getOwner()
					.getObjectKey(), true, monsterLevel, getOwner().getSpawnKey(), mostDamagePlayer.getObjectId(),
					knowPlayer);
			// 发奖，这里需要根据玩家伤害单独发奖,先处理任务共享的问题
			if (mostDamagePlayer.isInGroup()) {
				mostDamagePlayer.getPlayerGroup().submitHuntEvent(getOwner().getObjectKey(), getOwner().getSpawnKey(),
						new HashSet<Player>(), mostDamagePlayer, true, monsterLevel, mostDamagePlayer.getObjectId(),
						knowPlayer);
			} else {
				EventBusManager.getInstance().submit(event);
			}
		}

		if (getOwner().getObjectResource().getRewardId() != null) {
			// 发经验
			Map<Player, Long> playerDamages = aggroList.getPlayerDamage();
			if (getOwner().getObjectResource().isExpShare()) {
				for (Player player : playerDamages.keySet()) {
					Reward reward = RewardManager.getInstance().creatReward(mostDamagePlayer,
							getOwner().getObjectResource().getRewardId(), null);
					MathUtil.calcExp(player, reward, (Monster) getOwner(),
							getOwner().getAggroList().getDamagePercent(playerDamages.get(player)));
					RewardManager.getInstance().grantReward(
							mostDamagePlayer,
							reward,
							ModuleInfo.valueOf(ModuleType.MONSTERDROP, SubModuleType.KILL_NPC_REWARD, getOwner()
									.getObjectKey()));
				}
			} else {
				Player mostDamage = aggroList.getMostPlayerDamage();
				if (mostDamage != null) {
					Reward reward = RewardManager.getInstance().creatReward(mostDamagePlayer,
							getOwner().getObjectResource().getRewardId(), null);
					MathUtil.calcExp(mostDamage, reward, (Monster) getOwner(), 1.0);
					RewardManager.getInstance().grantReward(
							mostDamagePlayer,
							reward,
							ModuleInfo.valueOf(ModuleType.MONSTERDROP, SubModuleType.KILL_NPC_REWARD, getOwner()
									.getObjectKey()));
				}
			}
		}

		handleDropCoins(mostDamagePlayers);
	}

	private void handleDropCoins(Collection<Player> mostDamagePlayers) {
		int coins = getOwner().getObjectResource().getAddBossCoins();
		if (coins <= 0) {
			return;
		}
		Collection<Long> pids = getBossCoinsOwners(mostDamagePlayers);
		if (pids != null) {
			for (Long pid : pids) {
				Player p = PlayerManager.getInstance().getPlayer(pid);
				if (BossManager.getInstance().isBossCoinModuleOpen(p)) {
					Reward reward = Reward.valueOf().addCurrency(CurrencyType.BOSS_COINS, coins);
					RewardManager.getInstance().grantReward(p, reward,
							ModuleInfo.valueOf(ModuleType.BOSS_COINS, SubModuleType.BOSS_COINS_ATTEND_KILL_BOSS));
				}
			}
		}
	}

	private Collection<Long> getBossCoinsOwners(Collection<Player> mostDamagePlayers) {
		Set<Long> attenders = new HashSet<Long>();
		int killedCountry = 0;
		for (Player p : mostDamagePlayers) {
			if (!getOwner().getKnownList().knowns(p)) {
				continue;
			}
			attenders.add(p.getObjectId());
		}
		for (Player p : mostDamagePlayers) {
			killedCountry = p.getCountryValue();
		}
		for (Entry<Long, Long> entry : getOwner().getOnAttackTime().entrySet()) {
			if (System.currentTimeMillis() - entry.getValue() <= BossConfig.getInstance().COINS_LAST_ATTACK_PERIOD
					.getValue() * DateUtils.MILLIS_PER_SECOND) {
				Player bPlayer = PlayerManager.getInstance().getPlayer(entry.getKey());
				if (bPlayer.getCountryValue() == killedCountry) {
					attenders.add(bPlayer.getObjectId());
				}
			}
		}
		for (VisibleObject visObj : getOwner().getKnownList()) { // 没有伤害的
			// 围观者
			if (visObj instanceof Player) {
				Player knowPlayer = (Player) visObj;
				if (knowPlayer.getCountryValue() == killedCountry) {
					attenders.add(visObj.getObjectId());
				}
			}
		}
		return attenders;
	}

	@Override
	public void onDespawn() {
		Npc owner = getOwner();
		if (owner == null || !owner.isSpawned())
			return;

		owner.getAi().handleEvent(Event.DESPAWN);

		getOwner().getDamages().clear();
	}

	@Override
	public void delete() {
		Npc owner = getOwner();
		if (owner == null)
			return;
		owner.getAi().handleEvent(Event.DELETE);

		getOwner().getAggroList().clear();
		getOwner().getDamages().clear();
		getOwner().getMoveController().stop();
		getOwner().getEffectController().removeAllEffects();
		if (getOwner().isSpawned())
			World.getInstance().despawn(getOwner());

		super.delete();
	}

	@Override
	public void onSpawn(int mapId, int instanceId) {
		Npc owner = getOwner();
		if (owner == null)
			return;
		owner.getAi().handleEvent(Event.SPAWN);
		// 身上加一个光环
		if (owner.getObjectResource().getAuraSkillId() != 0) {
			if (!owner.getEffectController().containsSkill(owner.getObjectResource().getAuraSkillId())) {
				Skill skill = SkillEngine.getInstance().getSkill(getOwner(),
						owner.getObjectResource().getAuraSkillId(), getOwner().getObjectId(), -1, -1, getOwner(),
						Arrays.asList((Creature) owner));
				skill.useSkill();
			}
		}

		super.onSpawn(mapId, instanceId);
	}

	@Override
	public void onAddDamage() {
		super.onAddDamage();
		getOwner().getAi().handleEvent(Event.ATTACKED);
	}

	@Override
	public void onDie(Creature lastAttacker, int skillId) {
		super.onDie(lastAttacker, skillId);
	}

	@Override
	public void onAddHate() {
		super.onAddHate();
		getOwner().getAi().handleEvent(Event.ATTACKED);
	}

	public void onAtSpawnLocation() {

	}

	public void onFightOff() {
		getOwner().getAggroList().clear();
		getOwner().getDamages().clear();
	}

}
