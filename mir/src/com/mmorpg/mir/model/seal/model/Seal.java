package com.mmorpg.mir.model.seal.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.seal.SealConfig;
import com.mmorpg.mir.model.seal.event.SealUpGradeEvent;
import com.mmorpg.mir.model.seal.packet.SM_Seal_Item_Change;
import com.mmorpg.mir.model.seal.packet.SM_Seal_Upgrade;
import com.mmorpg.mir.model.seal.resource.SealItemResource;
import com.mmorpg.mir.model.seal.resource.SealResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.scheduler.ScheduledTask;

public class Seal {
	public static final StatEffectId SEAL_GRADE_STATEID = StatEffectId.valueOf("seal_grade",
			StatEffectType.SEAL);

	public static final StatEffectId SEAL_ITEM_STATEID = StatEffectId
			.valueOf("seal_item", StatEffectType.SEAL);
	
	public static final StatEffectId SEAL_BLESS_STATEID = StatEffectId.valueOf("seal_bless", StatEffectType.SEAL);

	@Transient
	private transient Player owner;

	/** 阶数 */
	private int grade;
	/** 等级 */
	private int level;
	/** 升阶次数 */
	private volatile int upCount;
	/** 上一次点击升阶按钮的时间 */
	private volatile long clearTime; 
	/** 技能 */
	private Map<Integer, Integer> learnSkill;
	/** 各种丹 */
	private Map<String, Integer> itemCount;
	
	private transient Future<?> tempStatsFuture;

	public static Seal valueOf() {
		Seal result = new Seal();
		result.grade = 1;
		result.level = 0;
		result.upCount = 0;
		result.learnSkill = new HashMap<Integer, Integer>();
		result.itemCount = new HashMap<String, Integer>();
		return result;
	}

	@JsonIgnore
	public void refresh() {
		SealResource resource = SealConfig.getInstance().getResource(this.grade);
		if (resource.isCurrentCountReset() && this.clearTime != 0 && this.clearTime <= System.currentTimeMillis()) {
			clear();
		}
	}

	@JsonIgnore
	private void clear() {
		this.clearTime = 0;
		this.upCount = 0;
		stopTempStatsClearTask();
	}
	
	@JsonIgnore
	private void startTempStatsClearTask() {
		if (tempStatsFuture == null && clearTime > System.currentTimeMillis()) {
			tempStatsFuture = SealConfig.getInstance().touchTask(new ScheduledTask() {
				
				@Override
				public void run() {
					refresh();
					owner.getGameStats().endModifiers(SEAL_BLESS_STATEID);
					tempStatsFuture = null;
				}
				
				@Override
				public String getName() {
					return "印玺临时属性清空任务";
				}
			}, new Date(clearTime));
		}
	}
	
	private void stopTempStatsClearTask() {
		if (tempStatsFuture != null) {
			tempStatsFuture.cancel(false);
			tempStatsFuture = null;
			owner.getGameStats().endModifiers(SEAL_BLESS_STATEID);
		}
	}

	@JsonIgnore
	public SM_Seal_Upgrade upgrade(int count) {
		refresh();
		SealResource resource = SealConfig.getInstance().getResource(this.grade);

		this.upCount += count;
		if (resource.canUpLevel(this.level, this.upCount)) {
			if (isMaxLevel()) {
				this.level = 0;
				this.grade++;
				notice();
				EventBusManager.getInstance().submit(SealUpGradeEvent.valueOf(owner));
			} else {
				this.level++;
				EventBusManager.getInstance().submit(SealUpGradeEvent.valueOf(owner));
			}
			clear();
			owner.getGameStats().replaceModifiers(SEAL_GRADE_STATEID, getGradeStats(), true);

		} else {
			long now = System.currentTimeMillis();
			if (resource.isCurrentCountReset() && clearTime < now) {
				this.clearTime = now + SealConfig.getInstance().getClearBlessInterval();
				startTempStatsClearTask();
			}
		}
		owner.getGameStats().replaceModifiers(SEAL_BLESS_STATEID, getTempBlessStats(), true);
		return SM_Seal_Upgrade.valueOf(this, count);

	}

	@JsonIgnore
	private void notice() {
		if (this.grade >= SealConfig.getInstance().SEAL_NOTICE_GRADE.getValue()) {
			I18nUtils utils = I18nUtils.valueOf("60202");
			utils.addParm("name", I18nPack.valueOf(owner.getName()));
			utils.addParm("country", I18nPack.valueOf(owner.getCountry().getName()));
			utils.addParm("seal", I18nPack.valueOf(grade));
			ChatManager.getInstance().sendSystem(11001, utils, null);

			I18nUtils tvUtils = I18nUtils.valueOf("307102");
			tvUtils.addParm("name", I18nPack.valueOf(owner.getName()));
			tvUtils.addParm("country", I18nPack.valueOf(owner.getCountry().getName()));
			tvUtils.addParm("seal", I18nPack.valueOf(grade));
			ChatManager.getInstance().sendSystem(0, tvUtils, null);
		}
	}

	@JsonIgnore
	private Stat[] getGradeStats() {
		SealResource resource = SealConfig.getInstance().getResource(this.grade);
		return resource.getStats()[this.level];
	}
	
	@JsonIgnore
	public Stat[] getTempBlessStats() {
		SealResource resource = SealConfig.getInstance().getResource(this.grade);
		if (resource.isCurrentCountReset() && clearTime < System.currentTimeMillis()) {
			return null;
		}
		if (upCount > 0 && !resource.isMaxGrade()) {
			Stat[] currentStats = resource.getStats()[this.level];
			Stat[] nextLevelStats = null;
			if (isMaxLevel()) {
				nextLevelStats = SealConfig.getInstance().getResource(this.getGrade() + 1).getStats()[0];
			} else {
				nextLevelStats = resource.getStats()[this.level + 1];
			}
			Stat[] tempStats = new Stat[nextLevelStats.length];
			for (int i = 0; i < nextLevelStats.length; i++) {
				Stat temp = nextLevelStats[i].copyOf();
				for (int j = 0; j < currentStats.length; j++) {
					Stat target = currentStats[j];
					if (temp.getType() == target.getType()) {
						long difA = temp.getValueA() - target.getValueA();
						long difB = temp.getValueB() - target.getValueB();
						long difC = temp.getValueC() - target.getValueC();
						if (difA >= 0) {
							temp.setValueA(difA);
						}
						if (difB >= 0) {
							temp.setValueB(difB);
						}
						if (difC >= 0) {
							temp.setValueC(difC);
						}
					}
				}
				tempStats[i] = temp;
			}
			
			double factor = upCount * 1.0 / resource.getNeedCount()[this.level];
			for (Stat stat : tempStats) {
				stat.multipMerge(factor);
			}
			return tempStats;
		}
		return null;
	}
	
	@JsonIgnore
	public void addItemCount(String itemId) {
		if (!this.itemCount.containsKey(itemId)) {
			this.itemCount.put(itemId, 0);
		}
		this.itemCount.put(itemId, this.itemCount.get(itemId) + 1);
		owner.getGameStats().replaceModifiers(SEAL_ITEM_STATEID, getAllItemStats(), true);

		PacketSendUtility.sendPacket(owner, SM_Seal_Item_Change.valueOf(this));
	}

	@JsonIgnore
	public Stat[] getAllItemStats() {
		List<Stat> itemStats = new ArrayList<Stat>();
		for (Map.Entry<String, Integer> itemCountEntry : this.itemCount.entrySet()) {
			String itemId = itemCountEntry.getKey();
			int count = itemCountEntry.getValue();
			SealItemResource resource = SealConfig.getInstance().getSealItemResource(itemId);
			for (int i = 0; i < count; i++) {
				itemStats.addAll(Arrays.asList(resource.getStat()));
			}
		}
		return itemStats.toArray(new Stat[0]);
	}

	@JsonIgnore
	public void refreshStats(boolean recomputeStats) {
		if (ModuleOpenManager.getInstance().isOpenByKey(owner, "opmk105")) {
			owner.getGameStats().replaceModifiers(SEAL_GRADE_STATEID, getGradeStats(), recomputeStats);
			owner.getGameStats().replaceModifiers(SEAL_ITEM_STATEID, getAllItemStats(), recomputeStats);
			owner.getGameStats().replaceModifiers(SEAL_BLESS_STATEID, getTempBlessStats(), recomputeStats);
			if (SealConfig.getInstance().getResource(grade).isCurrentCountReset() && upCount > 0) {
				startTempStatsClearTask();
			}
		}
	}

	@JsonIgnore
	public boolean isSkillLearned(int skillId) {
		return this.learnSkill.containsValue(skillId);
	}

	@JsonIgnore
	public boolean isMaxLevel() {
		SealResource resource = SealConfig.getInstance().getResource(this.grade);
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

}
