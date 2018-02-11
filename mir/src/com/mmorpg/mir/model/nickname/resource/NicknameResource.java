package com.mmorpg.mir.model.nickname.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.nickname.model.Nickname;
import com.mmorpg.mir.model.nickname.model.NicknameCondition;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class NicknameResource {
	@Id
	private Integer id;
	/** 合服之后称号转成的ID **/
	private Integer[] mergeTransferId;
	/** 激活条件 */
	private String[] activeConditionIds;
	/** 佩戴条件 */
	private String[] equipConditionIds;
	/** 不满足激活条件时是否移除 */
	private boolean activeRemove;
	/** 不满足佩戴条件时是否卸下 */
	private boolean equipRemove;

	/** 激活属性 */
	private Stat[] activeStats;
	/** 佩戴属性 */
	private Stat[] equipStats;
	
	private Map<String, Integer> i18nNotice;
	/** 获得的时候自动穿戴 */
	private boolean autoEquip;

	@Transient
	private CoreConditions activeConditions;
	@Transient
	private CoreConditions equipConditions;

	@Transient
	private List<Class<?>> activeEventClass;
	@Transient
	private List<Class<?>> equipEventClass;

	@JsonIgnore
	public List<Class<?>> getAllActiveEvent() {
		if (activeEventClass == null) {
			List<Class<?>> eventClass = new ArrayList<Class<?>>();
			for (AbstractCoreCondition condition : getActiveConditions().getConditionList()) {
				if (condition instanceof NicknameCondition) {
					NicknameCondition qc = (NicknameCondition) condition;
					for (Class<?> clazz : qc.getNicknameEvent()) {
						if (!eventClass.contains(clazz)) {
							eventClass.add(clazz);
						}
					}
				}
			}
			activeEventClass = eventClass;
		}

		return activeEventClass;
	}

	@JsonIgnore
	public List<Class<?>> getAllEquipEvent() {
		if (equipEventClass == null) {
			List<Class<?>> eventClass = new ArrayList<Class<?>>();
			for (AbstractCoreCondition condition : getEquipConditions().getConditionList()) {
				if (condition instanceof NicknameCondition) {
					NicknameCondition qc = (NicknameCondition) condition;
					for (Class<?> clazz : qc.getNicknameEvent()) {
						if (!eventClass.contains(clazz)) {
							eventClass.add(clazz);
						}
					}
				}
			}
			equipEventClass = eventClass;
		}

		return equipEventClass;
	}

	@JsonIgnore
	public Nickname creatNickname() {
		return creatNickname(0);
	}

	@JsonIgnore
	public Nickname creatNickname(long deprecatedTime) {
		Nickname nickname = new Nickname();
		nickname.setId(id);
		nickname.setDeprecatedTime(deprecatedTime);
		return nickname;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String[] getActiveConditionIds() {
		return activeConditionIds;
	}

	public void setActiveConditionIds(String[] activeConditionIds) {
		this.activeConditionIds = activeConditionIds;
	}

	public String[] getEquipConditionIds() {
		return equipConditionIds;
	}

	public void setEquipConditionIds(String[] equipConditionIds) {
		this.equipConditionIds = equipConditionIds;
	}

	public boolean isActiveRemove() {
		return activeRemove;
	}

	public void setActiveRemove(boolean activeRemove) {
		this.activeRemove = activeRemove;
	}

	public boolean isEquipRemove() {
		return equipRemove;
	}

	public void setEquipRemove(boolean equipRemove) {
		this.equipRemove = equipRemove;
	}

	@JsonIgnore
	public CoreConditions getActiveConditions() {
		if (activeConditions == null) {
			activeConditions = CoreConditionManager.getInstance().getCoreConditions(1, activeConditionIds);
		}
		return activeConditions;
	}

	@JsonIgnore
	public void setActiveConditions(CoreConditions activeConditions) {
		this.activeConditions = activeConditions;
	}

	@JsonIgnore
	public CoreConditions getEquipConditions() {
		if (equipConditions == null) {
			equipConditions = CoreConditionManager.getInstance().getCoreConditions(1, equipConditionIds);
		}
		return equipConditions;
	}

	@JsonIgnore
	public void setEquipConditions(CoreConditions equipConditions) {
		this.equipConditions = equipConditions;
	}

	public Stat[] getActiveStats() {
		return activeStats;
	}

	public void setActiveStats(Stat[] activeStats) {
		this.activeStats = activeStats;
	}

	public Stat[] getEquipStats() {
		return equipStats;
	}

	public void setEquipStats(Stat[] equipStats) {
		this.equipStats = equipStats;
	}

	public Map<String, Integer> getI18nNotice() {
		return i18nNotice;
	}

	public void setI18nNotice(Map<String, Integer> i18nNotice) {
		this.i18nNotice = i18nNotice;
	}

	public boolean isAutoEquip() {
		return autoEquip;
	}

	public void setAutoEquip(boolean autoEquip) {
		this.autoEquip = autoEquip;
	}

	public Integer[] getMergeTransferId() {
		return mergeTransferId;
	}

	public void setMergeTransferId(Integer[] mergeTransferId) {
		this.mergeTransferId = mergeTransferId;
	}

}
