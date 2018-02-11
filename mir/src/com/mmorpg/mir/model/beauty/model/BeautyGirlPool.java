package com.mmorpg.mir.model.beauty.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.commons.lang.time.DateUtils;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.beauty.BeautyGirlConfig;
import com.mmorpg.mir.model.beauty.packet.SM_Beauty_Active;
import com.mmorpg.mir.model.beauty.packet.SM_Beauty_Fight;
import com.mmorpg.mir.model.beauty.packet.SM_Beauty_Item_Change;
import com.mmorpg.mir.model.beauty.packet.SM_Beauty_Learn_Skill;
import com.mmorpg.mir.model.beauty.packet.SM_Beauty_Linger;
import com.mmorpg.mir.model.beauty.packet.SM_Beauty_UnFight;
import com.mmorpg.mir.model.beauty.resource.BeautyGirlItemResource;
import com.mmorpg.mir.model.beauty.resource.BeautyGirlResource;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Servant;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class BeautyGirlPool {

	public static final String BEAUTY_SUMMONTYPE = "beauty_girl";

	public static final StatEffectId MASTER_STATEID = StatEffectId.valueOf("master_stat", StatEffectType.BEAUTY_GIRL);

	public static final StatEffectId MASTER_ITEM_STATEID = StatEffectId.valueOf("master_item_stat",
			StatEffectType.BEAUTY_GIRL);

	@Transient
	private transient Player owner;
	/** 上次出战时间 */
	private long lastFightTime;
	/** 已激活的美人 ,key为BeautyResource的id */
	private NonBlockingHashMap<String, BeautyGirl> beautyGirls;
	// 当前正在出战的美人 只有
	private String fightingGirlId;
	/** 各种道具 ,给主人和美人的都记录在这个集合里 */
	private Map<String, Integer> itemCounts;

	public static BeautyGirlPool valueOf() {
		BeautyGirlPool pool = new BeautyGirlPool();
		pool.beautyGirls = new NonBlockingHashMap<String, BeautyGirl>();
		pool.lastFightTime = System.currentTimeMillis();
		pool.itemCounts = new HashMap<String, Integer>();
		return pool;
	}

	@JsonIgnore
	public int getSumLevel() {
		int sumLevel = 0;
		for (BeautyGirl girl : this.beautyGirls.values()) {
			sumLevel += girl.getLevel();
		}
		return sumLevel;
	}

	@JsonIgnore
	public void refreshForeverStats(boolean recomputeStats) {
		if (ModuleOpenManager.getInstance().isOpenByModuleKey(owner, ModuleKey.BEAUTY)) {
			owner.getGameStats().addModifiers(MASTER_STATEID, getForeverMasterStats(), recomputeStats);
		}
	}

	@JsonIgnore
	public void refreshItemStats(boolean recomputeStats) {
		if (ModuleOpenManager.getInstance().isOpenByModuleKey(owner, ModuleKey.BEAUTY)) {
			List<Stat> toMasterItemStats = new ArrayList<Stat>();
			for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
				BeautyGirlItemResource resource = BeautyGirlConfig.getInstance().beautyGirlItemStorage.get(
						entry.getKey(), true);
				for (int i = 0; i < entry.getValue(); i++) {
					toMasterItemStats.addAll(Arrays.asList(resource.getStat()));
				}
			}
			owner.getGameStats().replaceModifiers(MASTER_ITEM_STATEID, toMasterItemStats, recomputeStats);
		}
	}

	@JsonIgnore
	public void addItemCount(String itemId) {
		if (!this.itemCounts.containsKey(itemId)) {
			this.itemCounts.put(itemId, 0);
		}

		this.itemCounts.put(itemId, this.itemCounts.get(itemId) + 1);
		refreshItemStats(true);
		PacketSendUtility.sendPacket(owner, SM_Beauty_Item_Change.valueOf(this.itemCounts));
	}

	@JsonIgnore
	public void learnSkill(String girlId, int skillId, boolean proactive) {
		if (!isActive(girlId)) {
			throw new ManagedException(ManagedErrorCode.BEAUTY_TARGET_NOT_ACTIVE);
		}
		BeautyGirl girl = beautyGirls.get(girlId);
		if (girl.getProactiveSkills().containsValue(skillId) || girl.getPassiveSkills().containsValue(skillId)) {
			throw new ManagedException(ManagedErrorCode.BEAUTY_SKILL_LEARNED);
		}

		BeautyGirlResource resource = BeautyGirlConfig.getInstance().beautyGirlStorage.get(girl.getId(), true);
		if (proactive && girl.getProactiveSkills().size() >= resource.getMaxSkillCount()) {
			throw new ManagedException(ManagedErrorCode.BEAUTY_MAX_SKILL_COUNT);
		}

		if (!resource.containSkillId(skillId) && !resource.containPassiveSkill(skillId)) {
			throw new ManagedException(ManagedErrorCode.SYS_ERROR);
		}
		int index = 0;
		if (proactive) {
			// 主动技能
			index = girl.getProactiveSkills().size();
			girl.getProactiveSkills().put(index, skillId);
			if (isTargetGirlOnFight(girlId)) {
				doFight(girlId, false);
			}
		} else {
			index = girl.getPassiveSkills().size();
			girl.getPassiveSkills().put(index, skillId);
			if (isTargetGirlOnFight(girlId)) {
				Servant nowFightGirl = getFightGirl();
				Skill skill = SkillEngine.getInstance().getSkill(nowFightGirl, skillId, owner.getObjectId(), 0, 0,
						owner, null);
				skill.noEffectorUseSkill();
			}
		}
		PacketSendUtility.sendPacket(owner, SM_Beauty_Learn_Skill.valueOf(girl.getId(), proactive, index, skillId));
	}

	@JsonIgnore
	public String getFightGirlId() {
		Summon fightGirl = owner.getSummons().get(BEAUTY_SUMMONTYPE);
		if (fightGirl != null) {
			BeautyGirlResource resource = BeautyGirlConfig.getInstance().beautyGirlStorage.getUnique(
					BeautyGirlResource.SPAWN_INDEX, fightGirl.getSpawnKey());
			return resource.getId();
		}
		return null;
	}

	@JsonIgnore
	public List<Stat> getForeverMasterStats() {
		List<Stat> allStats = new ArrayList<Stat>();
		for (BeautyGirl girl : beautyGirls.values()) {
			BeautyGirlResource resource = BeautyGirlConfig.getInstance().beautyGirlStorage.get(girl.getId(), true);
			Stat[][] stats = resource.getMasterStats();
			allStats.addAll(Arrays.asList(stats[girl.getLevel()]));
		}
		return allStats;
	}

	@JsonIgnore
	public boolean isInCD() {
		return System.currentTimeMillis() - this.lastFightTime < BeautyGirlConfig.getInstance().FIGHT_CD_TIME
				.getValue() * DateUtils.MILLIS_PER_SECOND;
	}

	@JsonIgnore
	public void active(BeautyGirl girl) {
		beautyGirls.put(girl.getId(), girl);
		// 重新计算永久给主人加的属性
		refreshForeverStats(true);
		PacketSendUtility.sendPacket(owner, SM_Beauty_Active.valueOf(girl));

		BeautyGirlResource resource = BeautyGirlConfig.getInstance().beautyGirlStorage.get(girl.getId(), true);
		if (resource.getActiveNotice() == null || resource.getActiveNotice().isEmpty()) {
			return;
		}

		for (Map.Entry<String, Integer> entry : resource.getActiveNotice().entrySet()) {
			I18nUtils utils = I18nUtils.valueOf(entry.getKey());
			utils.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(owner.getName()));
			utils.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(owner.getCountry().getName()));
			ChatManager.getInstance().sendSystem(entry.getValue(), utils, null);
		}
	}

	@JsonIgnore
	public boolean isTargetGirlOnFight(String girlId) {
		if (!owner.getSummons().containsKey(BEAUTY_SUMMONTYPE)) {
			return false;
		}
		String spawnKey = owner.getSummons().get(BEAUTY_SUMMONTYPE).getSpawnKey();
		BeautyGirlResource resource = BeautyGirlConfig.getInstance().beautyGirlStorage.getUnique(
				BeautyGirlResource.SPAWN_INDEX, spawnKey);

		return girlId.equals(resource.getId());
	}

	@JsonIgnore
	public void loginOrRelieve() {
		if (fightingGirlId == null) {
			return;
		}

		if (isHavingFightGirl()) {
			return;
		}

		doFight(getFightingGirlId(), false);
	}

	@JsonIgnore
	public void unFight() {
		if (!isHavingFightGirl()) {
			throw new ManagedException(ManagedErrorCode.BEAUTY_WITHOUT_FIGHTING_GIRL);
		}
		doRest();
		this.fightingGirlId = null;
		PacketSendUtility.sendPacket(owner, new SM_Beauty_UnFight());
	}

	@JsonIgnore
	public void fight(String fightGirlId) {
		if (!isActive(fightGirlId)) {
			throw new ManagedException(ManagedErrorCode.BEAUTY_TARGET_NOT_ACTIVE);
		}
		if (isInCD()) {
			throw new ManagedException(ManagedErrorCode.BEAUTY_IN_CD);
		}

		doFight(fightGirlId, true);
		PacketSendUtility.sendPacket(owner, SM_Beauty_Fight.valueOf(getFightGirlId(), getLastFightTime()));
	}

	@JsonIgnore
	public void doFight(String fightGirlId, boolean updateLastFightTime) {
		doRest();
		BeautyGirlResource resource = BeautyGirlConfig.getInstance().beautyGirlStorage.get(fightGirlId, true);

		Servant nowGirl = (Servant) SpawnManager.getInstance().creatObject(resource.getSpawnKey(), 1, owner);
		owner.changeSummon(BeautyGirlPool.BEAUTY_SUMMONTYPE, nowGirl);

		BeautyGirl fightGirl = beautyGirls.get(fightGirlId);

		for (int skillId : fightGirl.getPassiveSkills().values()) {
			Skill skill = SkillEngine.getInstance().getSkill(nowGirl, skillId, owner.getObjectId(), 0, 0, owner, null);
			skill.noEffectorUseSkill();

		}
		SpawnManager.getInstance().bringIntoWorld(nowGirl, owner.getInstanceId());
		setFightingGirlId(fightGirlId);
		if (updateLastFightTime) {
			this.lastFightTime = System.currentTimeMillis();
		}
	}

	@JsonIgnore
	public void doRest() {
		Servant fightGirl = getFightGirl();
		if (fightGirl == null) {
			return;
		}
		String fightGirlId = getFightGirlId();

		BeautyGirl girl = beautyGirls.get(fightGirlId);
		for (int skillId : girl.getPassiveSkills().values()) {
			fightGirl.getMaster().getEffectController().removeEffect(skillId);
		}
		owner.clearSummon(BeautyGirlPool.BEAUTY_SUMMONTYPE);
	}

	@JsonIgnore
	public void addSense(String girlId, int sense) {
		BeautyGirl girl = beautyGirls.get(girlId);
		boolean upLevel = girl.addSense(owner, sense);
		if (upLevel) {
			// 永久增加给主人的属性
			refreshForeverStats(true);
			if (isTargetGirlOnFight(girlId)) {
				doFight(girlId, false);
			}
		}
		PacketSendUtility.sendPacket(owner, SM_Beauty_Linger.valueOf(girl));
	}

	@JsonIgnore
	public boolean isActive(String girlId) {
		return beautyGirls.containsKey(girlId);
	}

	@JsonIgnore
	public boolean isHavingFightGirl() {
		synchronized (owner.getSummons()) {
			return owner.getSummons().containsKey(BEAUTY_SUMMONTYPE);
		}
	}

	// getter- setter
	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public Servant getFightGirl() {
		return (Servant) owner.getSummons().get(BEAUTY_SUMMONTYPE);
	}

	public long getLastFightTime() {
		return lastFightTime;
	}

	public NonBlockingHashMap<String, BeautyGirl> getBeautyGirls() {
		return beautyGirls;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public void setLastFightTime(long lastFightTime) {
		this.lastFightTime = lastFightTime;
	}

	public void setBeautyGirls(NonBlockingHashMap<String, BeautyGirl> beautyGirls) {
		this.beautyGirls = beautyGirls;
	}

	public String getFightingGirlId() {
		return fightingGirlId;
	}

	public void setFightingGirlId(String fightingGirlId) {
		this.fightingGirlId = fightingGirlId;
	}

	public Map<String, Integer> getItemCounts() {
		return itemCounts;
	}

	public void setItemCounts(Map<String, Integer> itemCounts) {
		this.itemCounts = itemCounts;
	}

}
