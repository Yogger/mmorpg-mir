package com.mmorpg.mir.model.copy.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.boss.manager.BossManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.copy.model.CopyType;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.object.ObjectManager;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.trigger.manager.TriggerManager;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CopyResource {
	@Id
	private String id;
	/** 进入条件 */
	private String[] enterCondtions;
	/** 进入消耗 */
	private String[] enterActions;
	/** 初始坐标 */
	private int x;
	/** 坐标 */
	private int y;
	/** 关联的地图ID */
	private int mapId;
	/** 朝向 */
	private byte heading;
	/** 购买条件 */
	private String[] buyConditionIds;
	/** 购买进入次数消耗 */
	private String[] buyCountAct;
	/** 副本类型 */
	private CopyType type;
	/** 触发器 */
	private String[] triggers;
	/** 手动退出 */
	private boolean manualQuit;
	/** 是否是客服端触发 */
	private boolean clientTrigger;
	/** 鼓舞条件 */
	private String[] encourageConditionIds;
	/** 元宝鼓舞 */
	private String[] encourageGoldActs;
	/** 铜钱鼓舞 */
	private String[] encourageCopperActs;

	private int[] hpEncourageSkills;

	private int[] damageEncourageSkills;
	/** 领奖条件 */
	private String[] rewardConditionIds;
	/** 奖励groupId */
	private String rewardGroupId;
	/** 索引 */
	private int index;
	/** 扫地消耗 */
	private String[] batchActionIds;
	/** 进入时是否不增加记录次数 */
	private boolean noEnterAddCount;
	/** 奖励的日志类型 */
	private ModuleType rewardLogType = ModuleType.COPY;
	/** 副本消除时间,秒 */
	private int copyDestoryTime;
	/** VIP对应等级每日进入次数 */
	private Map<String, Integer> vipDailyEnterCount;
	/** VIP对应等级每日购买次数 */
	private Map<String, Integer> vipDailyBuyCount;
	/** VIP对应消耗chooserGroupId */
	private Map<String, String[]> enterSpecialAction;
	/** VIP对应消耗所对应的奖励id */
	private Map<String, String> specialRewardIds;
	/** VIP重置双倍奖励消耗 */
	private Map<String, String[]> doubleRewardAction;
	/** 最大重置次数 */
	private int maxResetTimes;
	/** 是否自动拾取 */
	private boolean autoPickup;
	/** 双倍扫荡消耗 */
	private String[] sweepAction;

	/** 扫荡的奖励chooserGroupId */
	@Transient
	private String wipeOutRewardChooser;

	@Transient
	private CoreConditions rewardConditions;
	@Transient
	private CoreConditions encourageConditions;
	@Transient
	private CoreConditions buyCountConditions;
	@Transient
	private CoreConditions enterConditions;
	@Transient
	private CoreActions batchActions;
	@Transient
	private CoreActions copyActions;

	public String[] getEncourageGoldActs() {
		return encourageGoldActs;
	}

	public void setEncourageGoldActs(String[] encourageGoldActs) {
		this.encourageGoldActs = encourageGoldActs;
	}

	public String[] getEncourageCopperActs() {
		return encourageCopperActs;
	}

	public void setEncourageCopperActs(String[] encourageCopperActs) {
		this.encourageCopperActs = encourageCopperActs;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getEnterCondtions() {
		return enterCondtions;
	}

	public void setEnterCondtions(String[] enterCondtions) {
		this.enterCondtions = enterCondtions;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public byte getHeading() {
		return heading;
	}

	public void setHeading(byte heading) {
		this.heading = heading;
	}

	public String[] getBuyCountAct() {
		return buyCountAct;
	}

	public void setBuyCountAct(String[] buyCountAct) {
		this.buyCountAct = buyCountAct;
	}

	public String[] getTriggers() {
		return triggers;
	}

	public void setTriggers(String[] triggers) {
		this.triggers = triggers;
	}

	public boolean isManualQuit() {
		return manualQuit;
	}

	public void setManualQuit(boolean manualQuit) {
		this.manualQuit = manualQuit;
	}

	public CopyType getType() {
		return type;
	}

	public void setType(CopyType type) {
		this.type = type;
	}

	public boolean isClientTrigger() {
		return clientTrigger;
	}

	public void setClientTrigger(boolean clientTrigger) {
		this.clientTrigger = clientTrigger;
	}

	public int[] getHpEncourageSkills() {
		return hpEncourageSkills;
	}

	public void setHpEncourageSkills(int[] hpEncourageSkills) {
		this.hpEncourageSkills = hpEncourageSkills;
	}

	public int[] getDamageEncourageSkills() {
		return damageEncourageSkills;
	}

	public void setDamageEncourageSkills(int[] damageEncourageSkills) {
		this.damageEncourageSkills = damageEncourageSkills;
	}

	public String[] getRewardConditionIds() {
		return rewardConditionIds;
	}

	public void setRewardConditionIds(String[] rewardConditionIds) {
		this.rewardConditionIds = rewardConditionIds;
	}

	public String getRewardGroupId() {
		return rewardGroupId;
	}

	public void setRewardGroupId(String rewardGroupId) {
		this.rewardGroupId = rewardGroupId;
	}
	
	@JsonIgnore
	public CoreConditions getRewardConditions() {
		if (rewardConditions == null) {
			if (rewardConditionIds == null) {
				rewardConditions = new CoreConditions();
			} else {
				rewardConditions = CoreConditionManager.getInstance().getCoreConditions(1, rewardConditionIds);
			}
		}
		return rewardConditions;
	}

	@JsonIgnore
	public void setRewardConditions(CoreConditions rewardConditions) {
		this.rewardConditions = rewardConditions;
	}

	public String[] getEncourageConditionIds() {
		return encourageConditionIds;
	}

	public void setEncourageConditionIds(String[] encourageConditionIds) {
		this.encourageConditionIds = encourageConditionIds;
	}

	@JsonIgnore
	public CoreConditions getEncourageConditions() {
		if (encourageConditions == null) {
			if (ArrayUtils.isEmpty(encourageConditionIds)) {
				encourageConditions = new CoreConditions();
			} else {
				encourageConditions = CoreConditionManager.getInstance().getCoreConditions(1, encourageConditionIds);
			}
		}
		return encourageConditions;
	}

	@JsonIgnore
	public void setEncourageConditions(CoreConditions encourageConditions) {
		this.encourageConditions = encourageConditions;
	}

	public String[] getBuyConditionIds() {
		return buyConditionIds;
	}

	public void setBuyConditionIds(String[] buyConditionIds) {
		this.buyConditionIds = buyConditionIds;
	}

	@JsonIgnore
	public CoreConditions getBuyCountConditions() {
		if (buyCountConditions == null) {
			if (ArrayUtils.isEmpty(buyConditionIds)) {
				buyCountConditions = new CoreConditions();
			} else {
				buyCountConditions = CoreConditionManager.getInstance().getCoreConditions(1, buyConditionIds);
			}
		}
		return buyCountConditions;
	}

	@JsonIgnore
	public void setBuyCountConditions(CoreConditions buyCountConditions) {
		this.buyCountConditions = buyCountConditions;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String[] getBatchActionIds() {
		return batchActionIds;
	}

	public void setBatchActionIds(String[] batchActionIds) {
		this.batchActionIds = batchActionIds;
	}

	@JsonIgnore
	public CoreActions getBatchActions() {
		if (batchActions == null) {
			if (ArrayUtils.isEmpty(batchActionIds)) {
				batchActions = new CoreActions();
			} else {
				batchActions = CoreActionManager.getInstance().getCoreActions(1, batchActionIds);
			}
		}
		return batchActions;
	}

	@JsonIgnore
	public void setBatchActions(CoreActions batchActions) {
		this.batchActions = batchActions;
	}

	@JsonIgnore
	public CoreConditions getEnterConditions() {
		if (enterConditions == null) {
			if (ArrayUtils.isEmpty(enterCondtions)) {
				enterConditions = new CoreConditions();
			} else {
				enterConditions = CoreConditionManager.getInstance().getCoreConditions(1, enterCondtions);
			}
		}
		return enterConditions;
	}

	@JsonIgnore
	public void setEnterConditions(CoreConditions enterConditions) {
		this.enterConditions = enterConditions;
	}

	public String[] getEnterActions() {
		return enterActions;
	}

	public void setEnterActions(String[] enterActions) {
		this.enterActions = enterActions;
	}

	@JsonIgnore
	public CoreActions getCopyActions() {
		if (copyActions == null) {
			if (ArrayUtils.isEmpty(enterActions)) {
				copyActions = new CoreActions();
			} else {
				copyActions = CoreActionManager.getInstance().getCoreActions(1, enterActions);
			}
		}
		return copyActions;
	}

	@JsonIgnore
	public void setCopyActions(CoreActions copyActions) {
		this.copyActions = copyActions;
	}

	public boolean isNoEnterAddCount() {
		return noEnterAddCount;
	}

	public void setNoEnterAddCount(boolean noEnterAddCount) {
		this.noEnterAddCount = noEnterAddCount;
	}

	public ModuleType getRewardLogType() {
		return rewardLogType;
	}

	public void setRewardLogType(ModuleType rewardLogType) {
		this.rewardLogType = rewardLogType;
	}

	public int getCopyDestoryTime() {
		return copyDestoryTime;
	}

	public void setCopyDestoryTime(int copyDestoryTime) {
		this.copyDestoryTime = copyDestoryTime;
	}

	public Map<String, Integer> getVipDailyEnterCount() {
		return vipDailyEnterCount;
	}

	public void setVipDailyEnterCount(Map<String, Integer> vipDailyEnterCount) {
		this.vipDailyEnterCount = vipDailyEnterCount;
	}

	public Map<String, String[]> getEnterSpecialAction() {
		return enterSpecialAction;
	}

	public void setEnterSpecialAction(Map<String, String[]> enterSpecialAction) {
		this.enterSpecialAction = enterSpecialAction;
	}

	@JsonIgnore
	public boolean vipExtraEnterCondVerify(Player player) {
		if (vipDailyEnterCount == null || vipDailyEnterCount.isEmpty()) {
			return true;
		}
		int count = vipDailyEnterCount.get(player.getVip().getLevel() + "");
		int typeCount = 0;
		typeCount += player.getCopyHistory().getDailyTypeCopyFinishedCount(type);
		return typeCount < count;
	}

	@JsonIgnore
	public CoreActions getEnterSpecialCoreActions(Player player) {
		if (enterSpecialAction == null || enterSpecialAction.isEmpty()) {
			return null;
		}
		int enterCount = 1;
		enterCount += player.getCopyHistory().getDailyTypeCopyFinishedCount(type);
		String[] actionIds = enterSpecialAction.get(enterCount + "");
		return CoreActionManager.getInstance().getCoreActions(1, actionIds);
	}

	@JsonIgnore
	public CoreActions getDoubleRewardCoreActions(Player player) {
		if (doubleRewardAction == null || doubleRewardAction.isEmpty()) {
			return null;
		}
		int enterCount = 1;
		enterCount += player.getCopyHistory().getDailyTypeCopyFinishedCount(type);
		String[] actionIds = doubleRewardAction.get(enterCount + "");
		return CoreActionManager.getInstance().getCoreActions(1, actionIds);
	}

	@JsonIgnore
	public String getEnterSpecialReturnReward(Player player) {
		if (specialRewardIds == null || specialRewardIds.isEmpty()) {
			return null;
		}
		int enterCount = 1;
		enterCount += player.getCopyHistory().getDailyTypeCopyFinishedCount(type);
		return specialRewardIds.get(enterCount + "");
	}

	public Map<String, String> getSpecialRewardIds() {
		return specialRewardIds;
	}

	public void setSpecialRewardIds(Map<String, String> specialRewardIds) {
		this.specialRewardIds = specialRewardIds;
	}

	@JsonIgnore
	public String getWipeOutRewardChooser() {
		if (wipeOutRewardChooser == null && triggers != null && triggers.length != 0) {
			for (String triggerId : triggers) {
				TriggerResource triggerRes = TriggerManager.getInstance().getTiggerResource().get(triggerId, true);
				if (triggerRes.getType() == TriggerType.MONSTER) {
					String spawnKey = triggerRes.getKeys().get(TriggerContextKey.SPAWNGROUPID);
					String objKey = SpawnManager.getInstance().getSpawn(spawnKey).getObjectKey();
					wipeOutRewardChooser = ObjectManager.getInstance().getObjectResource(objKey).getDropKey();
				}
			}
		}
		return wipeOutRewardChooser;
	}

	@JsonIgnore
	public Reward getWipeOutReward(Player player) {
		List<String> drops = ChooserManager.getInstance().chooseValueByRequire(player, getWipeOutRewardChooser());
		Reward drop = RewardManager.getInstance().creatReward(player, drops);
		return drop;
	}

	@JsonIgnore
	public Reward getBossCoinsReward(Player player) {
		if (!BossManager.getInstance().isBossCoinModuleOpen(player)) {
			return Reward.valueOf();
		}
		if (triggers != null) {
			int coins = 0;
			for (String triggerId : triggers) {
				TriggerResource triggerRes = TriggerManager.getInstance().getTiggerResource().get(triggerId, true);
				if (triggerRes.getType() == TriggerType.MONSTER) {
					String spawnKey = triggerRes.getKeys().get(TriggerContextKey.SPAWNGROUPID);
					String objKey = SpawnManager.getInstance().getSpawn(spawnKey).getObjectKey();
					coins += ObjectManager.getInstance().getObjectResource(objKey).getAddBossCoins();
				}
			}
			if (coins > 0) {
				return Reward.valueOf().addCurrency(CurrencyType.BOSS_COINS, coins);
			}
		}
		return Reward.valueOf();
	}

	@JsonIgnore
	public List<String> getMingJiangRewardChoosers() {
		List<String> choosers = new ArrayList<String>();
		if (triggers != null && triggers.length != 0) {
			for (String triggerId : triggers) {
				TriggerResource triggerRes = TriggerManager.getInstance().getTiggerResource().get(triggerId, true);
				if (triggerRes.getType() == TriggerType.MONSTER) {
					String spawnKey = triggerRes.getKeys().get(TriggerContextKey.SPAWNGROUPID);
					String objKey = SpawnManager.getInstance().getSpawn(spawnKey).getObjectKey();
					String chooser = ObjectManager.getInstance().getObjectResource(objKey).getDropKey();
					if (chooser != null) {
						choosers.add(chooser);
					}
				}
			}
		}
		return choosers;
	}

	@JsonIgnore
	public Reward getMingJiangReward(Player player) {
		List<String> dropss = new ArrayList<String>();
		for (String chooser : getMingJiangRewardChoosers()) {
			List<String> drops = ChooserManager.getInstance().chooseValueByRequire(player, chooser);
			dropss.addAll(drops);
		}
		Reward drop = RewardManager.getInstance().creatReward(player, dropss);
		return drop;
	}

	public Map<String, String[]> getDoubleRewardAction() {
		return doubleRewardAction;
	}

	public void setDoubleRewardAction(Map<String, String[]> doubleRewardAction) {
		this.doubleRewardAction = doubleRewardAction;
	}

	public int getMaxResetTimes() {
		return maxResetTimes;
	}

	public void setMaxResetTimes(int maxResetTimes) {
		this.maxResetTimes = maxResetTimes;
	}

	public boolean isAutoPickup() {
		return autoPickup;
	}

	public void setAutoPickup(boolean autoPickup) {
		this.autoPickup = autoPickup;
	}

	public String[] getSweepAction() {
		return sweepAction;
	}

	public void setSweepAction(String[] sweepAction) {
		this.sweepAction = sweepAction;
	}

	@JsonIgnore
	public CoreActions getSweepActions() {
		return CoreActionManager.getInstance().getCoreActions(1, this.sweepAction);
	}
	

	public Map<String, Integer> getVipDailyBuyCount() {
		return vipDailyBuyCount;
	}

	public void setVipDailyBuyCount(Map<String, Integer> vipDailyBuyCount) {
		this.vipDailyBuyCount = vipDailyBuyCount;
	}

	public void setWipeOutRewardChooser(String wipeOutRewardChooser) {
		this.wipeOutRewardChooser = wipeOutRewardChooser;
	}
	

}
