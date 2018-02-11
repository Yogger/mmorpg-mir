package com.mmorpg.mir.model.vip.resource;

import java.lang.reflect.Field;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class VipResource {
	private static Logger logger = Logger.getLogger(VipResource.class);

	@Id
	private int level;
	/** 需要成长值 */
	private int growthValue;
	/** 永久属性加层 */
	private Stat[] stats;
	/** 是否可用大表情 */
	private boolean bigEmoji;
	/** 补签元宝 */
	private int goldAct;
	/** 补签天数 */
	private int fillDays;
	/** vip领取离线奖励的系数 */
	private double unlineExpReward;
	/** vip升级奖励 */
	private String vipRewardChoosergroup;
	/** 周礼包奖励 */
	private String weekRewardChoosergroup;
	/** 是否有额外的签到奖励 */
	private boolean extraSignedReward;
	/** 杀怪额外经验加层 */
	private int killMonsterExExp;
	/** 操练额外经验加层 */
	private int exerciseExExp;
	/** 每天增加的活跃度 */
	private int addActive;
	/** 背包开启加速 */
	private int bagOpenSpeedUp;
	/** 仓库开启加速 */
	private int wareHouseOpenSpeedUp;
	/** 主线任务加层 */
	private int trunkQuestExReward;
	/** 是否免费使用小飞鞋 */
	private boolean freeFly;
	/** 血战边疆额外挑战次数 */
	private int expCopyExCount;
	/** 重置名将试炼副本次数 */
	private int resetLadderCopyCount;
	/** 称号个数 */
	private int titleCount;
	/** 至尊锻造 */
	private boolean richForgeEquipment;
	/** 自动反击 */
	private boolean autoStrikeBack;
	/** 至尊强化 */
	private boolean extremeEnhance;
	/** 双倍离线经验 */
	private boolean doubleOfflineExp;
	/** 五倍离线经验 */
	private boolean ultraOfflineExp;
	/** 是否可以扫荡爬塔副本 */
	private boolean canBatchLadderCopy;
	/** 是否可以元宝祭祀 */
	private boolean canSacrifice;
	/** 熔炼的额外经验加成 */
	private int smeltExpExtra;
	/** 每天额外搬砖次数 */
	private int exBrickCount;
	/** 每天额外刺探次数 */
	private int exInvestigateCount;
	/** 每天额外营救次数 */
	private int exRescueCount;
	/** 每天额外运镖次数 */
	private int exExpressCount;
	/** 搬砖是否可以一键刷橙 */
	private boolean brickOrange;
	/** 刺探是否可以一键刷橙 */
	private boolean investigateOrange;
	/** 是否可以购买橙色镖车 */
	private boolean isGodExpress;
	/** 升到当前等级触发的事件 */
	private String[] eventTriggers;
	/** 是否可以补签 */
	private boolean canFillSign;
	/** 道具每日额外使用次数 */
	private Map<String, Integer> itemDailyExtraCount;
	/** 可以佩戴称号的数量 */
	private int equipNicknameNums;
	/** 升级时附带在邮件里面的附件奖励 */
	private String mailRewardChooserGroupId;
	/** 西周王陵每日进入上限 */
	private int gasCopyDailyEnterLimit;
	/** 购买兵书副本次数*/
	private int warbookCopyExCount;

	@Transient
	private Map<String, Object> fieldMap;

	private void createFieldMap() {
		try {
			Map<String, Object> temp = New.hashMap();
			for (Field field : VipResource.class.getDeclaredFields()) {
				if (!field.isAnnotationPresent(Transient.class)) {
					temp.put(field.getName(), field.get(this));
				}
			}
			fieldMap = temp;
		} catch (Exception e) {
			logger.error("反射生成VipResource异常", e);
			throw new RuntimeException("反射生成VipResource异常");
		}

	}

	@JsonIgnore
	public Object getField(String field) {
		if (fieldMap == null) {
			createFieldMap();
		}
		return fieldMap.get(field);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getFillDays() {
		return fillDays;
	}

	public void setFillDays(int fillDays) {
		this.fillDays = fillDays;
	}

	public int getGrowthValue() {
		return growthValue;
	}

	public void setGrowthValue(int growthValue) {
		this.growthValue = growthValue;
	}

	public Stat[] getStats() {
		return stats;
	}

	public void setStats(Stat[] stats) {
		this.stats = stats;
	}

	public boolean isBigEmoji() {
		return bigEmoji;
	}

	public void setBigEmoji(boolean bigEmoji) {
		this.bigEmoji = bigEmoji;
	}

	public double getUnlineExpReward() {
		return unlineExpReward;
	}

	public void setUnlineExpReward(double unlineExpReward) {
		this.unlineExpReward = unlineExpReward;
	}

	public boolean isExtraSignedReward() {
		return extraSignedReward;
	}

	public void setExtraSignedReward(boolean extraSignedReward) {
		this.extraSignedReward = extraSignedReward;
	}

	public int getKillMonsterExExp() {
		return killMonsterExExp;
	}

	public void setKillMonsterExExp(int killMonsterExExp) {
		this.killMonsterExExp = killMonsterExExp;
	}

	public int getExerciseExExp() {
		return exerciseExExp;
	}

	public void setExerciseExExp(int exerciseExExp) {
		this.exerciseExExp = exerciseExExp;
	}

	public int getAddActive() {
		return addActive;
	}

	public void setAddActive(int addActive) {
		this.addActive = addActive;
	}

	public int getBagOpenSpeedUp() {
		return bagOpenSpeedUp;
	}

	public void setBagOpenSpeedUp(int bagOpenSpeedUp) {
		this.bagOpenSpeedUp = bagOpenSpeedUp;
	}

	public int getTrunkQuestExReward() {
		return trunkQuestExReward;
	}

	public void setTrunkQuestExReward(int trunkQuestExReward) {
		this.trunkQuestExReward = trunkQuestExReward;
	}

	public boolean isFreeFly() {
		return freeFly;
	}

	public void setFreeFly(boolean freeFly) {
		this.freeFly = freeFly;
	}

	public int getExpCopyExCount() {
		return expCopyExCount;
	}

	public void setExpCopyExCount(int expCopyExCount) {
		this.expCopyExCount = expCopyExCount;
	}

	public int getResetLadderCopyCount() {
		return resetLadderCopyCount;
	}

	public void setResetLadderCopyCount(int resetLadderCopyCount) {
		this.resetLadderCopyCount = resetLadderCopyCount;
	}

	public Map<String, Object> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<String, Object> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public int getTitleCount() {
		return titleCount;
	}

	public void setTitleCount(int titleCount) {
		this.titleCount = titleCount;
	}

	public boolean isRichForgeEquipment() {
		return richForgeEquipment;
	}

	public void setRichForgeEquipment(boolean richForgeEquipment) {
		this.richForgeEquipment = richForgeEquipment;
	}

	public boolean isAutoStrikeBack() {
		return autoStrikeBack;
	}

	public void setAutoStrikeBack(boolean autoStrikeBack) {
		this.autoStrikeBack = autoStrikeBack;
	}

	public int getWareHouseOpenSpeedUp() {
		return wareHouseOpenSpeedUp;
	}

	public void setWareHouseOpenSpeedUp(int wareHouseOpenSpeedUp) {
		this.wareHouseOpenSpeedUp = wareHouseOpenSpeedUp;
	}

	public boolean isExtremeEnhance() {
		return extremeEnhance;
	}

	public void setExtremeEnhance(boolean extremeEnhance) {
		this.extremeEnhance = extremeEnhance;
	}

	public boolean isDoubleOfflineExp() {
		return doubleOfflineExp;
	}

	public void setDoubleOfflineExp(boolean doubleOfflineExp) {
		this.doubleOfflineExp = doubleOfflineExp;
	}

	public boolean isUltraOfflineExp() {
		return ultraOfflineExp;
	}

	public void setUltraOfflineExp(boolean ultraOfflineExp) {
		this.ultraOfflineExp = ultraOfflineExp;
	}

	public boolean isCanBatchLadderCopy() {
		return canBatchLadderCopy;
	}

	public void setCanBatchLadderCopy(boolean canBatchLadderCopy) {
		this.canBatchLadderCopy = canBatchLadderCopy;
	}

	public int getGoldAct() {
		return goldAct;
	}

	public void setGoldAct(int goldAct) {
		this.goldAct = goldAct;
	}

	public boolean isCanSacrifice() {
		return canSacrifice;
	}

	public void setCanSacrifice(boolean canSacrifice) {
		this.canSacrifice = canSacrifice;
	}

	public int getExBrickCount() {
		return exBrickCount;
	}

	public void setExBrickCount(int exBrickCount) {
		this.exBrickCount = exBrickCount;
	}

	public int getExInvestigateCount() {
		return exInvestigateCount;
	}

	public void setExInvestigateCount(int exInvestigateCount) {
		this.exInvestigateCount = exInvestigateCount;
	}

	public int getExRescueCount() {
		return exRescueCount;
	}

	public void setExRescueCount(int exRescueCount) {
		this.exRescueCount = exRescueCount;
	}

	public int getExExpressCount() {
		return exExpressCount;
	}

	public void setExExpressCount(int exExpressCount) {
		this.exExpressCount = exExpressCount;
	}

	public final boolean isBrickOrange() {
		return brickOrange;
	}

	public final void setBrickOrange(boolean brickOrange) {
		this.brickOrange = brickOrange;
	}

	public final boolean isInvestigateOrange() {
		return investigateOrange;
	}

	public final void setInvestigateOrange(boolean investigateOrange) {
		this.investigateOrange = investigateOrange;
	}

	public final boolean isGodExpress() {
		return isGodExpress;
	}

	public final void setGodExpress(boolean isGodExpress) {
		this.isGodExpress = isGodExpress;
	}

	public String[] getEventTriggers() {
		return eventTriggers;
	}

	public void setEventTriggers(String[] eventTriggers) {
		this.eventTriggers = eventTriggers;
	}

	public String getVipRewardChoosergroup() {
		return vipRewardChoosergroup;
	}

	public void setVipRewardChoosergroup(String vipRewardChoosergroup) {
		this.vipRewardChoosergroup = vipRewardChoosergroup;
	}

	public String getWeekRewardChoosergroup() {
		return weekRewardChoosergroup;
	}

	public void setWeekRewardChoosergroup(String weekRewardChoosergroup) {
		this.weekRewardChoosergroup = weekRewardChoosergroup;
	}

	public boolean isCanFillSign() {
		return canFillSign;
	}

	public void setCanFillSign(boolean canFillSign) {
		this.canFillSign = canFillSign;
	}

	public Map<String, Integer> getItemDailyExtraCount() {
		return itemDailyExtraCount;
	}

	public void setItemDailyExtraCount(Map<String, Integer> itemDailyExtraCount) {
		this.itemDailyExtraCount = itemDailyExtraCount;
	}

	public int getEquipNicknameNums() {
		return equipNicknameNums;
	}

	public void setEquipNicknameNums(int equipNicknameNums) {
		this.equipNicknameNums = equipNicknameNums;
	}

	public String getMailRewardChooserGroupId() {
		return mailRewardChooserGroupId;
	}

	public void setMailRewardChooserGroupId(String mailRewardChooserGroupId) {
		this.mailRewardChooserGroupId = mailRewardChooserGroupId;
	}

	public int getSmeltExpExtra() {
		return smeltExpExtra;
	}

	public void setSmeltExpExtra(int smeltExpExtra) {
		this.smeltExpExtra = smeltExpExtra;
	}

	public int getGasCopyDailyEnterLimit() {
		return gasCopyDailyEnterLimit;
	}

	public void setGasCopyDailyEnterLimit(int gasCopyDailyEnterLimit) {
		this.gasCopyDailyEnterLimit = gasCopyDailyEnterLimit;
	}

	public int getWarbookCopyExCount() {
		return warbookCopyExCount;
	}

	public void setWarbookCopyExCount(int warbookCopyExCount) {
		this.warbookCopyExCount = warbookCopyExCount;
	}

}
