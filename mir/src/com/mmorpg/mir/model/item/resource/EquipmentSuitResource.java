package com.mmorpg.mir.model.item.resource;

import java.util.Map;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.item.model.EquipmentGroup;
import com.mmorpg.mir.model.item.model.EquipmentStatType;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class EquipmentSuitResource {

	public static final String STAR = "STAR";
	public static final String SOULTYPE = "SOULTYPE";
	public static final String SUITLEVEL = "SUITLEVEL";
	public static final String TYPE = "TYPE";

	@Id
	private String id;
	/** 装备栏 */
	private int equipStorageType;
	/** 套装条件 */
	private String[] suitConditionIds;
	/** 套装属性 */
	private Stat[] stats;
	/** 套装的类型 */
	@Index(name = TYPE)
	private EquipmentStatType suitType;
	/** 满足这一条属性的需要的装备数量 */
	private int needNum;
	/** 全身强化星级 */
	@Index(name = STAR)
	private int star;
	/** 套装的部位 */
	private Integer[] child;
	/** 套装组 */
	private EquipmentGroup group;
	/** 灵魂属性的类型 */
	@Index(name = SOULTYPE)
	private int soulType;
	@Index(name = SUITLEVEL)
	private int level;
	/** 广播I18N */
	private Map<String, Integer> i18nNotice;

	@Transient
	private CoreConditions activeCondition;

	@JsonIgnore
	public CoreConditions getActiveCoreCondition() {
		if (activeCondition == null) {
			activeCondition = CoreConditionManager.getInstance().getCoreConditions(1, suitConditionIds);
		}
		return activeCondition;
	}

	public static String toStorageStarIndex(int star, int equipStorageType) {
		return star + "_" + equipStorageType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getSuitConditionIds() {
		return suitConditionIds;
	}

	public void setSuitConditionIds(String[] suitConditionIds) {
		this.suitConditionIds = suitConditionIds;
	}

	public Stat[] getStats() {
		return stats;
	}

	public void setStats(Stat[] stats) {
		this.stats = stats;
	}

	public EquipmentStatType getSuitType() {
		return suitType;
	}

	public void setSuitType(EquipmentStatType suitType) {
		this.suitType = suitType;
	}

	public int getNeedNum() {
		return needNum;
	}

	public void setNeedNum(int needNum) {
		this.needNum = needNum;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public Integer[] getChild() {
		return child;
	}

	public void setChild(Integer[] child) {
		this.child = child;
	}

	public int getSoulType() {
		return soulType;
	}

	public void setSoulType(int soulType) {
		this.soulType = soulType;
	}

	public Map<String, Integer> getI18nNotice() {
		return i18nNotice;
	}

	public void setI18nNotice(Map<String, Integer> i18nNotice) {
		this.i18nNotice = i18nNotice;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public EquipmentGroup getGroup() {
		return group;
	}

	public void setGroup(EquipmentGroup group) {
		this.group = group;
	}

	public int getEquipStorageType() {
		return equipStorageType;
	}

	public void setEquipStorageType(int equipStorageType) {
		this.equipStorageType = equipStorageType;
	}

}