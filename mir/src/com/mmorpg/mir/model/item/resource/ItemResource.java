package com.mmorpg.mir.model.item.resource;

import java.util.List;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.item.ItemType;
import com.mmorpg.mir.model.item.model.EquipmentStorageType;
import com.mmorpg.mir.model.item.model.GemType;
import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;
import com.windforce.common.utility.JsonUtils;
import com.windforce.common.utility.New;

@Resource
public class ItemResource {

	public final static int PURPLE = 3;
	public final static int ORANGE = 6;
	public final static int PERFECT_ORANGE = 8;

	// 物品唯一ID
	@Id
	private String key;
	@Index(name = "templateId", unique = true)
	private short templateId;
	// 物品名称
	private String name;
	// 物品类型
	@Index(name = "itemType")
	private ItemType itemType;
	// 物品最大堆叠数
	private int overLimit;

	private EquipmentType equipmentType;
	// 装备附加的属性信息
	private Stat[] stats;
	// 装备每一级强化等级加的属性
	private Stat[] enhanceStats;
	// 装备限制
	private String[] equipCodintions;
	// 道具的使用条件
	private String[] conditions;
	// 道具的使用代价
	private String[] actions;
	// 道具的使用结果
	private String reward;
	// 冷却组
	private int group;
	// 冷却时间
	private int coolDown;
	// 角色限制
	private int roletype;
	// 等级
	private int level;
	// 品质
	private int quality;
	// 物防装,法防装
	private int specialType;
	// 是否能够回购
	private int buyBack;
	// 卖出价格
	private int sellPrice;
	// 是否装备绑定
	private int equipBind;
	// 使用物品的触发器
	private String[] triggerIds;
	// 模型等级（地图模型显示用）
	private byte modelLevel;
	// 奖励
	private String chooserReward;
	// 记录掉落
	private boolean dropRecord;
	// 道具不足提示的错误码
	private short errorCode = ManagedErrorCode.ITEM_COUNT;
	// 道具收集记录
	private boolean logCollect;
	// 排序用的字段
	private long sortIndex;
	// 是否返回获得的奖励 前端要求加的
	private int isSendReward;
	// 是否返回获得的奖励 前端要求加的
	private boolean cannotForged;
	// 运营用的名字字段
	private String operatorName;
	// 宝石类型
	private GemType gemType;
	// 不能丢弃
	private boolean cannotDrop;
	// 不能卖
	private int can_sell;

	private int monsterClearDayInterval;

	private int enhanceHigh;
	/** 装备栏类型 */
	private int equipStorageType;

	/** 命格等级 */
	private int lifeGridLevel;

	/** 命格升到下一级所需经验 */
	private int needExp;

	/** 基础经验 */
	private int baseExp;

	/** 命格类型 */
	private int lifeGridType;

	public int getSpecialType() {
		return specialType;
	}

	public void setSpecialType(int specialType) {
		this.specialType = specialType;
	}

	public static void main(String[] args) {
		// String value =
		// "[[{\"moduleKey\":2,\"type\":\"PHYSICAL_ATTACK\",\"valueA\":11}]]";
		Stat[][] stats = new Stat[2][1];
		stats[0][0] = new Stat(StatEnum.CRITICAL, 3, 3, 3);
		stats[1][0] = new Stat(StatEnum.CRITICAL, 3, 3, 3);
		System.out.println(JsonUtils.object2String(stats));
	}

	public String[] getTriggerIds() {
		if (triggerIds == null) {
			triggerIds = new String[0];
		}
		return triggerIds;
	}

	public void setTriggerIds(String[] triggerIds) {
		this.triggerIds = triggerIds;
	}

	public int getOverLimit() {
		return overLimit;
	}

	public void setOverLimit(int overLimit) {
		this.overLimit = overLimit;
	}

	public EquipmentType getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
	}

	public ItemType getItemType() {
		return itemType;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

	public Stat[] getStats() {
		return stats;
	}

	public void setStats(Stat[] stats) {
		this.stats = stats;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public int getCoolDown() {
		return coolDown;
	}

	public void setCoolDown(int coolDown) {
		this.coolDown = coolDown;
	}

	public String[] getEquipCodintions() {
		return equipCodintions;
	}

	public void setEquipCodintions(String[] equipCodintions) {
		this.equipCodintions = equipCodintions;
	}

	/** 下面这三个属性，并不是配置表填写的属性，是为了提高效率，生成的对象 **/
	@Transient
	private CoreConditions itemConditions;
	@Transient
	private CoreActions itemActions;
	@Transient
	private CoreConditions equipCoreConditons;

	@JsonIgnore
	public CoreConditions getItemConditions(int num) {
		if (itemConditions == null)
			itemConditions = CoreConditionManager.getInstance().getCoreConditions(1, conditions);

		if (num == 1)
			return itemConditions;

		return CoreConditionManager.getInstance().getCoreConditions(num, conditions);
	}

	@JsonIgnore
	public CoreActions getItemActions(int num) {
		if (itemActions == null)
			itemActions = CoreActionManager.getInstance().getCoreActions(1, actions);

		if (num == 1)
			return itemActions;

		return CoreActionManager.getInstance().getCoreActions(num, actions);
	}

	@JsonIgnore
	public CoreConditions getEquipCoreConditions() {
		if (equipCoreConditons == null) {
			List<CoreConditionResource> conditionIds = New.arrayList();
			for (String conditionId : equipCodintions) {
				CoreConditionResource condRes = CoreConditionManager.getInstance().getCoreConditionResource()
						.get(conditionId, true);
				if (condRes.getType() == CoreConditionType.LEVEL && quality >= ORANGE) {
					continue;
				}
				conditionIds.add(condRes);
			}
			CoreConditionResource[] resources = new CoreConditionResource[conditionIds.size()];
			conditionIds.toArray(resources);
			equipCoreConditons = CoreConditionManager.getInstance().getCoreConditions(1, resources);
		}

		return equipCoreConditons;
	}

	@JsonIgnore
	public boolean isLifeGridMaxLevel() {
		return this.needExp == 0;
	}

	@JsonIgnore
	public EquipmentStorageType getEquipStorageByType() {
		return EquipmentStorageType.typeOf(this.equipStorageType);
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getBuyBack() {
		return buyBack;
	}

	public void setBuyBack(int buyBack) {
		this.buyBack = buyBack;
	}

	public boolean canBuyBack() {
		return buyBack > 0;
	}

	public int getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}

	public int getEquipBind() {
		return equipBind;
	}

	public void setEquipBind(int equipBind) {
		this.equipBind = equipBind;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getRoletype() {
		return roletype;
	}

	public void setRoletype(int roletype) {
		this.roletype = roletype;
	}

	public byte getModelLevel() {
		return modelLevel;
	}

	public void setModelLevel(byte modelLevel) {
		this.modelLevel = modelLevel;
	}

	public short getTemplateId() {
		return templateId;
	}

	public void setTemplateId(short templateId) {
		this.templateId = templateId;
	}

	public String getChooserReward() {
		return chooserReward;
	}

	public void setChooserReward(String chooserReward) {
		this.chooserReward = chooserReward;
	}

	public boolean isDropRecord() {
		return dropRecord;
	}

	public void setDropRecord(boolean dropRecord) {
		this.dropRecord = dropRecord;
	}

	public Stat[] getEnhanceStats() {
		return enhanceStats;
	}

	public void setEnhanceStats(Stat[] enhanceStats) {
		this.enhanceStats = enhanceStats;
	}

	public short getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(short errorCode) {
		this.errorCode = errorCode;
	}

	public boolean isLogCollect() {
		return logCollect;
	}

	public void setLogCollect(boolean logCollect) {
		this.logCollect = logCollect;
	}

	public long getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(long sortIndex) {
		this.sortIndex = sortIndex;
	}

	public int getIsSendReward() {
		return isSendReward;
	}

	public void setIsSendReward(int isSendReward) {
		this.isSendReward = isSendReward;
	}

	public boolean isCannotForged() {
		return cannotForged;
	}

	public void setCannotForged(boolean cannotForged) {
		this.cannotForged = cannotForged;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public GemType getGemType() {
		return gemType;
	}

	public void setGemType(GemType gemType) {
		this.gemType = gemType;
	}

	public boolean isCannotDrop() {
		return cannotDrop;
	}

	public void setCannotDrop(boolean cannotDrop) {
		this.cannotDrop = cannotDrop;
	}

	public int getCan_sell() {
		return can_sell;
	}

	public void setCan_sell(int can_sell) {
		this.can_sell = can_sell;
	}

	@JsonIgnore
	public boolean canSell() {
		return can_sell > 0;
	}

	public int getMonsterClearDayInterval() {
		return monsterClearDayInterval;
	}

	public void setMonsterClearDayInterval(int monsterClearDayInterval) {
		this.monsterClearDayInterval = monsterClearDayInterval;
	}

	public int getEnhanceHigh() {
		return enhanceHigh;
	}

	public void setEnhanceHigh(int enhanceHigh) {
		this.enhanceHigh = enhanceHigh;
	}

	public int getEquipStorageType() {
		return equipStorageType;
	}

	public void setEquipStorageType(int equipStorageType) {
		this.equipStorageType = equipStorageType;
	}

	public int getNeedExp() {
		return needExp;
	}

	public void setNeedExp(int needExp) {
		this.needExp = needExp;
	}

	public int getLifeGridType() {
		return lifeGridType;
	}

	public void setLifeGridType(int lifeGridType) {
		this.lifeGridType = lifeGridType;
	}

	public int getBaseExp() {
		return baseExp;
	}

	public void setBaseExp(int baseExp) {
		this.baseExp = baseExp;
	}

	public int getLifeGridLevel() {
		return lifeGridLevel;
	}

	public void setLifeGridLevel(int lifeGridLevel) {
		this.lifeGridLevel = lifeGridLevel;
	}

	public String[] getConditions() {
		return conditions;
	}

	public void setConditions(String[] conditions) {
		this.conditions = conditions;
	}

	public String[] getActions() {
		return actions;
	}

	public void setActions(String[] actions) {
		this.actions = actions;
	}
	
	

}
