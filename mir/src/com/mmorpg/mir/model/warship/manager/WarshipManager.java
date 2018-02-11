package com.mmorpg.mir.model.warship.manager;

import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.FormulaParmsUtil;
import com.mmorpg.mir.model.core.condition.BetweenCronTimeCondition;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.formula.Formula;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Sculpture;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.warship.packet.SM_Warhip_End;
import com.mmorpg.mir.model.warship.packet.SM_Warship_Add_Exp;
import com.mmorpg.mir.model.warship.resource.WarshipResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.scheduler.ScheduledTask;
import com.windforce.common.scheduler.impl.SimpleScheduler;
import com.windforce.common.utility.DateUtils;

@Component
public class WarshipManager implements IWarshipManager{

	private static WarshipManager INSTANCE;
	
	/** 加经验的时间间隔 毫秒 */
	@Static("WARSHIP:EXP_CHECKTIME")
	private ConfigValue<Integer> EXP_CHECKTIME;

	/** 加经验，离雕像的距离 *//*
	@Static("WARSHIP:EXP_SCOPE")
	private ConfigValue<Integer> EXP_SCOPE;*/

	/** 经验加层的条件 时间开始 */
	@Static("WARSHIP:EXP_UP_START")
	private ConfigValue<String> EXP_UP_START;
	
	/** 经验加层的时间开始前5分钟的通报 */
	@Static("WARSHIP:EXP_UP_START_NOTICE")
	private ConfigValue<String> EXP_UP_START_NOTICE;
	
	/** 经验加层的 时间结束 */
	@Static("WARSHIP:EXP_UP_END")
	private ConfigValue<String> EXP_UP_END;
	
//	@Static("WARSHIP:KINGSLAND_TRANSPORT_ID")
//	private ConfigValue<Integer> KINGSLAND_TRANSPORT_ID;
	
	@Static("WARSHIP:SCULPTURE_ADD_EXP_CONDS")
	private ConfigValue<String[]> SCULPTURE_ADD_EXP_CONDS;
	
	private CoreConditions addExpConditions;
	
	@Static("WARSHIP:WARSHIP_ADD_EXP")
	private Formula WARSHIP_ADD_EXP;
	
	@Static
	private Storage<Integer, WarshipResource> warshipResources;
	/** 记录 玩家 活动期间获得的经验 */
	private NonBlockingHashMap<Long, Long> logExp = new NonBlockingHashMap<Long, Long>();
	/** 记录 玩家 活动期间获得的阅历 */
	private NonBlockingHashMap<Long, Long> logQi = new NonBlockingHashMap<Long, Long>();

	@Autowired
	private SimpleScheduler simpleScheduler;
	
	@PostConstruct
	void init() {
		INSTANCE = this;
	}
	
	public void spawnAll() {
		simpleScheduler.schedule(new ScheduledTask() {

			@Override
			public void run() {
				I18nUtils i18nUtils1 = I18nUtils.valueOf("405004");
				ChatManager.getInstance().sendSystem(6100160, i18nUtils1, null);
			}

			@Override
			public String getName() {
				return "膜拜皇帝开始前的公告";
			}
		}, EXP_UP_START_NOTICE.getValue());
		
		simpleScheduler.schedule(new ScheduledTask() {
			
			@Override
			public void run() {
				logExp.clear();
				logQi.clear();
//				I18nUtils utils1 = I18nUtils.valueOf("301023");
//				utils1.addParm("transportId", I18nPack.valueOf(KINGSLAND_TRANSPORT_ID.getValue()));
//				ChatManager.getInstance().sendSystem(0, utils1, null);
				I18nUtils utils2 = I18nUtils.valueOf("40114");
				ChatManager.getInstance().sendSystem(1100160, utils2, null);
				I18nUtils utils3 = I18nUtils.valueOf("405006");
				ChatManager.getInstance().sendSystem(6100160, utils3, null);
			}
			
			@Override
			public String getName() {
				return "膜拜皇帝开始";
			}
		}, EXP_UP_START.getValue());
		
		simpleScheduler.schedule(new ScheduledTask() {
			
			@Override
			public void run() {
				I18nUtils i18nUtils1 = I18nUtils.valueOf("405005");
				ChatManager.getInstance().sendSystem(6100160, i18nUtils1, null);
//				I18nUtils utils1 = I18nUtils.valueOf("301026");
//				ChatManager.getInstance().sendSystem(0, utils1, null);
				I18nUtils utils2 = I18nUtils.valueOf("40116");
				ChatManager.getInstance().sendSystem(1100160, utils2, null);
				
				for (Entry<Long, Long> entry: logExp.entrySet()) {
					if (SessionManager.getInstance().isOnline(entry.getKey())) {
						SM_Warhip_End sm = new SM_Warhip_End();
						sm.setTotalExp(entry.getValue());
						Long qi = logQi.get(entry.getKey());
						if (qi != null) {
							sm.setTotalQi(qi);
						}
						PacketSendUtility.sendPacket(PlayerManager.getInstance().getPlayer(entry.getKey()), sm);
					}
				}
				for (Entry<Long, Long> entry: logQi.entrySet()) {
					if ((!logExp.containsKey(entry.getKey())) && SessionManager.getInstance().isOnline(entry.getKey())) {
						SM_Warhip_End sm = new SM_Warhip_End();
						sm.setTotalQi(entry.getValue());
						Long exp = logExp.get(entry.getKey());
						if (exp != null) {
							sm.setTotalExp(exp);
						}
						PacketSendUtility.sendPacket(PlayerManager.getInstance().getPlayer(entry.getKey()), sm);
					}
				}
			}
			
			@Override
			public String getName() {
				return "膜拜皇帝结束";
			}
		}, EXP_UP_END.getValue());
		long now = System.currentTimeMillis();
		long delay = DateUtils.getNextTime(EXP_UP_START.getValue(), new Date(now)).getTime() - now;
		delay %= EXP_CHECKTIME.getValue();
		ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (getExpUpCondition().verify(new Object(), false)) {
					addExp();
				}
			}
		}, delay, EXP_CHECKTIME.getValue());
	}

	private void addExp() {
		List<Sculpture> kingOfKings = KingOfWarManager.getInstance().getCountrySculpture(0);
		Sculpture sculpture = kingOfKings.get(0);
		if (sculpture == null) {
			throw new RuntimeException(String.format("皇帝雕像不存在"));
		}
		for (VisibleObject visObject: sculpture.getKnownList()) {
			if (visObject instanceof Player) {
				Player player = (Player) visObject;
				if (checkAddExp(player)) {
					Integer addExp = (Integer) FormulaParmsUtil.valueOf(WARSHIP_ADD_EXP).
											addParm("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player)).
											addParm("LEVEL", player.getLevel())
											.getValue();
					Reward reward = Reward.valueOf().addExp(addExp);
					RewardManager.getInstance().grantReward(player, reward, ModuleInfo.valueOf(ModuleType.WARSHIP, SubModuleType.WARSHIP_EXP_REWARD));
					PacketSendUtility.sendPacket(player, SM_Warship_Add_Exp.valueOf(addExp));
					logGain(player, reward);
				}
			}
		}
	}

	public static WarshipManager getInstance() {
		return INSTANCE;
	}
	
	public WarshipResource getWarshipResource(int key) {
		return warshipResources.get(key, true);
	}
	
	public void logGain(Player player, Reward reward) {
		List<RewardItem> rewardItems = reward.getItems();
		for (RewardItem rewardItem: rewardItems) {
			if (rewardItem.getRewardType() == RewardType.EXP) {
				long exp = rewardItem.getAmount();
				if (logExp.containsKey(player.getObjectId())) {
					exp += logExp.get(player.getObjectId());
				}
				logExp.put(player.getObjectId(), exp);
			} else if (rewardItem.getRewardType() == RewardType.CURRENCY && rewardItem.getCode().equals(String.valueOf(CurrencyType.QI.getValue()))) {
				long qi = rewardItem.getAmount();
				if (logQi.containsKey(player.getObjectId())) {
					qi += logQi.get(player.getObjectId());
				}
				logQi.put(player.getObjectId(), qi);
			}
		}
	}
	
	private CoreConditions conditions;

	public boolean isExpUpIn() {
		return getExpUpCondition().verify(null);
	}

	private CoreConditions getExpUpCondition() {
		if (conditions != null) {
			return conditions;
		}
		BetweenCronTimeCondition bctc = new BetweenCronTimeCondition();
		bctc.setStartDate(EXP_UP_START.getValue());
		bctc.setEndDate(EXP_UP_END.getValue());
		CoreConditions cronTimeConditions = new CoreConditions();
		cronTimeConditions.addCondition(bctc);
		conditions = cronTimeConditions;
		return conditions;
	}
	
	private boolean checkAddExp(Player player) {
		if (addExpConditions == null) {
			addExpConditions = CoreConditionManager.getInstance().getCoreConditions(1, SCULPTURE_ADD_EXP_CONDS.getValue());
		}
		return addExpConditions.verify(player, false);
	}
}
