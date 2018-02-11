package com.mmorpg.mir.model.gameobjects;

import java.util.Map;

import org.cliffc.high_scale_lib.NonBlockingHashMap;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.controllers.CreatureController;
import com.mmorpg.mir.model.controllers.MoveController;
import com.mmorpg.mir.model.controllers.ObserveController;
import com.mmorpg.mir.model.controllers.effect.EffectController;
import com.mmorpg.mir.model.controllers.stats.CreatureLifeStats;
import com.mmorpg.mir.model.gameobjects.stats.CreatureGameStats;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.object.cooldown.CoolDownContainer;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.task.tasks.PacketBroadcaster;
import com.mmorpg.mir.model.task.tasks.PacketBroadcaster.BroadcastMode;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.WorldPosition;
import com.mmorpg.mir.model.world.packet.SM_BackHome_Break;
import com.mmorpg.mir.model.world.resource.MapCountry;

public abstract class Creature extends VisibleObject {

	private MoveController moveController;

	private Skill castingSkill;
	private CoolDownContainer coolDownContainer;
	private CreatureLifeStats<? extends Creature> lifeStats;
	private CreatureGameStats<? extends Creature> gameStats;
	private EffectController effectController;
	private ObserveController observeController;
	private int level;
	private int atkRange;

	public Creature(long objId, CreatureController<? extends Creature> controller, WorldPosition position) {
		super(objId, controller, position);
		this.moveController = new MoveController(this);
		this.observeController = new ObserveController();
		this.coolDownContainer = new CoolDownContainer();
	}

	/** 触发技能公共CD */
	private Map<Integer, Long> lastProvokerEffectTimes = new NonBlockingHashMap<Integer, Long>();

	public boolean isProvokeCD(int skillId, long cd) {
		if (!lastProvokerEffectTimes.containsKey(skillId)) {
			return false;
		} else {
			long now = System.currentTimeMillis();
			if ((now - lastProvokerEffectTimes.get(skillId)) < cd) {
				return true;
			}
		}
		return false;
	}

	public void useProvokeSkill(int skillId) {
		lastProvokerEffectTimes.put(skillId, System.currentTimeMillis());
	}

	/**
	 * 移动检查敏感度，值越低敏感度越高，默认5
	 * 
	 * @return
	 */
	public int getMoveUpdateSensitivity() {
		return 5;
	}

	/**
	 * Return CreatureController of this Creature object.
	 * 
	 * @return CreatureController.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public CreatureController<? extends Creature> getController() {
		return (CreatureController<? extends Creature>) super.getController();
	}

	/**
	 * @return the moveController
	 */
	public MoveController getMoveController() {
		return moveController;
	}

	/**
	 * PacketBroadcasterMask
	 */
	private volatile int packetBroadcastMask;

	/**
	 * This is adding broadcast to player.
	 */
	public final void addPacketBroadcastMask(BroadcastMode mode) {
		if ((packetBroadcastMask & mode.mask()) == 0) {
			packetBroadcastMask |= mode.mask();
			PacketBroadcaster.getInstance().add(this);
		}
	}

	public final void removePacketBroadcastMask(BroadcastMode mode) {
		packetBroadcastMask &= ~mode.mask();
	}

	public boolean isCasting() {
		return castingSkill != null;
	}

	public Skill getCastingSkill() {
		return castingSkill;
	}

	public void setCasting(Skill castingSkill) {
		this.castingSkill = castingSkill;
	}

	public void updateCasting(Skill castingSkill) {
		this.castingSkill = castingSkill;
		if (castingSkill != null) {
			this.getEffectController().setAbnormal(EffectId.CASTING, true);
		} else {
			if (getEffectController().isAbnoramlSet(EffectId.CASTING)) {
				this.getEffectController().unsetAbnormal(EffectId.CASTING, true);
			}
		}
	}

	public Player getPlayerMaster() {
		if (this instanceof Summon) {
			return ((Summon) this).getMaster();
		}
		if (this instanceof Player) {
			return (Player) this;
		}
		return null;
	}
	public boolean isSkillDisabled(int skillId) {
		return coolDownContainer.isSkillDisabled(skillId);
	}

	public boolean isPublicDisabled(int group) {
		return coolDownContainer.isPublicDisabled(group);
	}

	public long getPublicCoolDown(int group) {
		return coolDownContainer.getPublicCoolDown(group);
	}

	public boolean isGatherDisable(long objId) {
		return coolDownContainer.isGatherDisable(objId);
	}

	public long getSkillCoolDown(int skillId) {
		return coolDownContainer.getSkillCoolDown(skillId);
	}

	public boolean isBackHomeDisable() {
		return coolDownContainer.isBackHomeDisable();
	}

	public boolean isTempleBrickDisable(int country) {
		return coolDownContainer.isTempleBrickDisable(country);
	}

	/**
	 * 
	 * @param skillId
	 * @param time
	 */
	public void setSkillCoolDown(int skillId, long time) {
		coolDownContainer.setSkillCoolDown(skillId, time);
	}

	public void setPublicCoolDown(int group, long time) {
		if (group == 0) {
			// 如果是0，不走公共CD
			return;
		}
		coolDownContainer.setPulibcCoolDown(group, time);
	}

	public void setGatherCoolDown(long objId, long time) {
		coolDownContainer.setGatherCoolDown(objId, time);
	}

	public void setBackHomeSing(long time) {
		coolDownContainer.setBackHomeSing(time);
	}

	public void setTempleBrick(long time, int country) {
		coolDownContainer.setTempleBrick(time, country);
	}

	/**
	 * @return the skillCoolDowns
	 */
	public Map<Integer, Long> getSkillCoolDowns() {
		return coolDownContainer.getSkillCoolDowns();
	}

	public Map<Integer, Long> getPublicCoolDowns() {
		return coolDownContainer.getPublicCoolDowns();
	}

	/**
	 * 
	 * @param skillId
	 */
	public void removeSkillCoolDown(int skillId) {
		coolDownContainer.removeSkillCoolDown(skillId);
	}

	public void removePublicCoolDown(int group) {
		coolDownContainer.removePublicCoolDown(group);
	}

	public void removeGatherCoolDown() {
		coolDownContainer.removeGatherCoolDown();
	}

	public void removeTempleBrickCoolDown() {
		if (getEffectController().isAbnoramlSet(EffectId.BRICK)) {
			getEffectController().unsetAbnormal(EffectId.BRICK, true);
		}
		coolDownContainer.removeTempleBrick();
	}

	public void removeBackHome() {
		if (getEffectController().isAbnoramlSet(EffectId.BACKHOME)) {
			PacketSendUtility.sendPacket(this, SM_BackHome_Break.valueOf());
			getEffectController().unsetAbnormal(EffectId.BACKHOME, true);
		}
		coolDownContainer.removeBackHome();
	}

	public boolean canPerformMove() {

		if (getPosition() != null && !getPosition().isSpawned()) {
			return false;
		}

		if (getEffectController().isAbnoramlSet(EffectId.STUN)) {
			if (this instanceof Player) {
				PacketSendUtility.sendErrorMessage((Player) this, ManagedErrorCode.PLAYER_STUN);
			}
			return false;
		}

		if (getEffectController().isAbnoramlSet(EffectId.CANNOT_MOVE)) {
			if (this instanceof Player) {
				PacketSendUtility.sendErrorMessage((Player) this, ManagedErrorCode.PLAYER_CANNOT_MOVE);
			}
			return false;
		}

		// TODO 这里就是用来判定玩家是否能够移动的逻辑，使用状态来判断
		if (getLifeStats().isAlreadyDead() || getGameStats().getCurrentStat(StatEnum.SPEED) == 0) {
			if (this instanceof Player) {
				PacketSendUtility.sendErrorMessage((Player) this, ManagedErrorCode.DEAD_ERROR);
			}
			return false;
		}

		return true;
	}

	public CreatureLifeStats<? extends Creature> getLifeStats() {
		return lifeStats;
	}

	public void setLifeStats(CreatureLifeStats<? extends Creature> lifeStats) {
		this.lifeStats = lifeStats;
	}

	public boolean isEnemy(Creature creature) {
		switch (creature.getObjectType()) {
		case MONSTER:
			return isMonsterEnemy((Monster) creature);
		case PLAYER:
			return isPlayerEnemy((Player) creature) && !creature.getEffectController().isAbnoramlSet(EffectId.GM_HIDE);
		case SUMMON:
			return isSummonEnemy((Summon) creature);
		case LORRY:
			return isLorryEnemy((Lorry) creature);
		case COUNTRY_NPC:
			return isCountryNpcEnemy((CountryNpc) creature);
		case COUNTRY_OBJECT:
			return isCountryObjectEnemy((CountryObject) creature);
		case BOSS:
			return isBossEnemy((Boss) creature);
		case STATUS_NPC:
			return isStatusNpc((StatusNpc) creature);
		case ROBOT:
			return isRobotEnemy((Robot) creature);
		case TOWN_PLAYER_NPC:
			return isTownPlayerNpcEnemy((TownPlayerNpc) creature);
		default:
			return false;
		}
	}
	
	protected boolean isRobotEnemy(Robot creature) {
		return false;
	}

	protected boolean isMonsterEnemy(Monster monster) {
		return false;
	}

	protected boolean isTownPlayerNpcEnemy(TownPlayerNpc townPlayerNpc) {
		return false;
	}

	protected boolean isPlayerEnemy(Player player) {
		return false;
	}

	protected boolean isSummonEnemy(Summon summon) {
		return isPlayerEnemy(summon.getMaster());
	}

	protected boolean isLorryEnemy(Lorry lorry) {
		return false;
	}

	protected boolean isCountryObjectEnemy(CountryObject countryObject) {
		return false;
	}

	protected boolean isBossEnemy(Boss boss) {
		return false;
	}

	protected boolean isStatusNpc(StatusNpc statusNpc) {
		return false;
	}

	protected boolean isCountryNpcEnemy(CountryNpc countryNpc) {
		return false;
	}

	protected boolean isSupervisorEnemy(Supervisor supervisor) {
		return false;
	}

	public boolean canAttack() {
		// TODO Auto-generated method stub
		return true;
	}

	public EffectController getEffectController() {
		return effectController;
	}

	public void setEffectController(EffectController effectController) {
		this.effectController = effectController;
	}

	public int getPacketBroadcastMask() {
		return packetBroadcastMask;
	}

	public ObserveController getObserveController() {
		return observeController;
	}

	public void setObserveController(ObserveController observeController) {
		this.observeController = observeController;
	}

	public CreatureGameStats<? extends Creature> getGameStats() {
		return gameStats;
	}

	public void setGameStats(CreatureGameStats<? extends Creature> gameStats) {
		this.gameStats = gameStats;
	}

	public CoolDownContainer getCoolDownContainer() {
		return coolDownContainer;
	}

	public void setCoolDownContainer(CoolDownContainer coolDownContainer) {
		this.coolDownContainer = coolDownContainer;
	}

	protected int getCountryValue() {
		return MapCountry.NEUTRAL.getValue();
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getAtkRange() {
		return atkRange;
	}

	public void setAtkRange(int atkRange) {
		this.atkRange = atkRange;
	}

}
