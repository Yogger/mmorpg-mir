package com.mmorpg.mir.model.warbook.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.warbook.WarbookConfig;
import com.mmorpg.mir.model.warbook.event.WarbookUpGradeEvent;
import com.mmorpg.mir.model.warbook.packet.SM_Warbook_Broadcast;
import com.mmorpg.mir.model.warbook.packet.SM_Warbook_Item_Change;
import com.mmorpg.mir.model.warbook.packet.SM_Warbook_Learn_Skill;
import com.mmorpg.mir.model.warbook.packet.SM_Warbook_OperateHide;
import com.mmorpg.mir.model.warbook.packet.SM_Warbook_Upgrade;
import com.mmorpg.mir.model.warbook.resource.WarbookItemResource;
import com.mmorpg.mir.model.warbook.resource.WarbookResource;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.RandomUtils;

public class Warbook {
	public static final StatEffectId WARBOOK_GRADE_STATEID = StatEffectId.valueOf("warbook_grade",
			StatEffectType.WARBOOK);

	public static final StatEffectId WARBOOK_ITEM_STATEID = StatEffectId
			.valueOf("warbook_item", StatEffectType.WARBOOK);

	@Transient
	private transient Player owner;

	private boolean hided;

	/** 阶数 */
	private int grade;
	/** 等级 */
	private int level;
	/** 升阶次数 */
	private int upCount;
	/** 上一次点击升阶按钮的时间 */
	private long clearTime;
	/** 技能 */
	private Map<Integer, Integer> learnSkill;
	/** 各种丹 */
	private Map<String, Integer> itemCount;

	public static Warbook valueOf() {
		Warbook result = new Warbook();
		result.grade = 1;
		result.level = 0;
		result.upCount = 0;
		result.hided = false;
		result.learnSkill = new HashMap<Integer, Integer>();
		result.itemCount = new HashMap<String, Integer>();
		return result;
	}

	@JsonIgnore
	public void operateHide() {
		this.hided = !isHided();
		PacketSendUtility.sendPacket(owner, SM_Warbook_OperateHide.valueOf(this.hided));
		broadCastKnownList();
	}

	@JsonIgnore
	public void refresh() {
		WarbookResource resource = WarbookConfig.getInstance().getWarbookResourceByGrade(this.grade);
		if (resource.isCurrentCountReset() && this.clearTime != 0 && this.clearTime < System.currentTimeMillis()) {
			clear();
		}
	}

	@JsonIgnore
	private void clear() {
		this.clearTime = 0;
		this.upCount = 0;
	}

	@JsonIgnore
	public SM_Warbook_Upgrade upgrade(int count) {
		refresh();
		WarbookResource resource = WarbookConfig.getInstance().getWarbookResourceByGrade(this.grade);

		this.upCount += count;
		if (resource.canUpLevel(this.level, this.upCount)) {
			if (isMaxLevel()) {
				this.level = 0;
				this.grade++;
				broadCastKnownList();
				notice();
				EventBusManager.getInstance().submit(WarbookUpGradeEvent.valueOf(owner));
			} else {
				this.level++;
				EventBusManager.getInstance().submit(WarbookUpGradeEvent.valueOf(owner));
			}
			LogManager.warbookUp(owner.getPlayerEnt().getServer(), owner.getPlayerEnt().getAccountName(),
					owner.getName(), owner.getObjectId(), this.grade, this.level, this.upCount,
					System.currentTimeMillis());
			clear();
			owner.getGameStats().replaceModifiers(WARBOOK_GRADE_STATEID, getGradeStats(), true);

		} else {
			long now = System.currentTimeMillis();
			if (resource.isCurrentCountReset() && clearTime < now) {
				this.clearTime = now + WarbookConfig.getInstance().getClearBlessInterval();
			}
		}
		return SM_Warbook_Upgrade.valueOf(this, count);

	}

	@JsonIgnore
	public void broadCastKnownList() {
		PacketSendUtility.broadcastPacket(owner,
				SM_Warbook_Broadcast.valueOf(owner.getObjectId(), this.grade, this.hided), true);
	}

	@JsonIgnore
	private void notice() {
		if (this.grade >= WarbookConfig.getInstance().WARBOOK_NOTICE_GRADE.getValue()) {
			I18nUtils utils = I18nUtils.valueOf("60201");
			utils.addParm("name", I18nPack.valueOf(owner.getName()));
			utils.addParm("country", I18nPack.valueOf(owner.getCountry().getName()));
			utils.addParm("warbook", I18nPack.valueOf(this.grade + ""));
			ChatManager.getInstance().sendSystem(11001, utils, null);

			I18nUtils tvUtils = I18nUtils.valueOf("307101");
			tvUtils.addParm("name", I18nPack.valueOf(owner.getName()));
			tvUtils.addParm("country", I18nPack.valueOf(owner.getCountry().getName()));
			tvUtils.addParm("warbook", I18nPack.valueOf(this.grade + ""));
			ChatManager.getInstance().sendSystem(0, tvUtils, null);
		}
	}

	@JsonIgnore
	private Stat[] getGradeStats() {
		WarbookResource resource = WarbookConfig.getInstance().getWarbookResourceByGrade(this.grade);
		return resource.getStats()[this.level];
	}

	@JsonIgnore
	public void learnSkill(Integer skillId) {
		if (this.learnSkill.containsValue(skillId)) {
			throw new ManagedException(ManagedErrorCode.WARBOOK_SKILL_LEARNED);
		}
		WarbookResource resource = WarbookConfig.getInstance().getWarbookResourceByGrade(this.grade);
		int index = RandomUtils.nextInt(resource.getSkillBoxCount());
		Integer beforeSkillId = this.learnSkill.put(index, skillId);
		if (beforeSkillId != null) {
			owner.getEffectController().removeEffect(beforeSkillId);
		}
		Skill skill = SkillEngine.getInstance().getSkill(null, skillId, owner.getObjectId(), 0, 0, owner, null);
		skill.noEffectorUseSkill();

		PacketSendUtility.sendPacket(owner, SM_Warbook_Learn_Skill.valueOf(index, skillId));
	}

	@JsonIgnore
	public void addItemCount(String itemId) {
		if (!this.itemCount.containsKey(itemId)) {
			this.itemCount.put(itemId, 0);
		}
		this.itemCount.put(itemId, this.itemCount.get(itemId) + 1);
		owner.getGameStats().replaceModifiers(WARBOOK_ITEM_STATEID, getAllItemStats(), true);

		PacketSendUtility.sendPacket(owner, SM_Warbook_Item_Change.valueOf(this));
	}

	@JsonIgnore
	public Stat[] getAllItemStats() {
		List<Stat> itemStats = new ArrayList<Stat>();
		for (Map.Entry<String, Integer> itemCountEntry : this.itemCount.entrySet()) {
			String itemId = itemCountEntry.getKey();
			int count = itemCountEntry.getValue();
			WarbookItemResource resource = WarbookConfig.getInstance().getWarbookItemResourceById(itemId);
			for (int i = 0; i < count; i++) {
				itemStats.addAll(Arrays.asList(resource.getStat()));
			}
		}
		return itemStats.toArray(new Stat[0]);
	}

	@JsonIgnore
	public void refreshStats(boolean recomputeStats) {
		owner.getGameStats().replaceModifiers(WARBOOK_GRADE_STATEID, getGradeStats(), recomputeStats);
		owner.getGameStats().replaceModifiers(WARBOOK_ITEM_STATEID, getAllItemStats(), recomputeStats);
	}

	@JsonIgnore
	public boolean isSkillLearned(int skillId) {
		return this.learnSkill.containsValue(skillId);
	}

	@JsonIgnore
	public boolean isMaxLevel() {
		WarbookResource resource = WarbookConfig.getInstance().getWarbookResourceByGrade(this.grade);
		int[] needCount = resource.getNeedCount();
		return this.level == needCount.length - 1;
	}

	// getter - setter
	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	public int getGrade() {
		return grade;
	}

	public int getUpCount() {
		return upCount;
	}

	public Map<Integer, Integer> getLearnSkill() {
		return learnSkill;
	}

	public Map<String, Integer> getItemCount() {
		return itemCount;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public void setUpCount(int upCount) {
		this.upCount = upCount;
	}

	public void setLearnSkill(Map<Integer, Integer> learnSkill) {
		this.learnSkill = learnSkill;
	}

	public void setItemCount(Map<String, Integer> itemCount) {
		this.itemCount = itemCount;
	}

	public long getClearTime() {
		return clearTime;
	}

	public void setClearTime(long clearTime) {
		this.clearTime = clearTime;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isHided() {
		return hided;
	}

	public void setHided(boolean hided) {
		this.hided = hided;
	}

}
