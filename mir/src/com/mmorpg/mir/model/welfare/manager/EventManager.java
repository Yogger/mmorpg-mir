package com.mmorpg.mir.model.welfare.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.h2.util.New;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.boss.manager.BossManager;
import com.mmorpg.mir.model.boss.resource.BossResource;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.copy.model.CopyType;
import com.mmorpg.mir.model.country.event.CountryFlagQuestFinishEvent;
import com.mmorpg.mir.model.country.event.PlayerKillDiplomacyEvent;
import com.mmorpg.mir.model.countrycopy.event.CountryCopyFinishEvent;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.MonsterKillEvent;
import com.mmorpg.mir.model.moduleopen.event.ModuleOpenEvent;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.player.event.KillPlayerEvent;
import com.mmorpg.mir.model.player.event.LevelUpEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.event.LogoutEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.player.model.LoginType;
import com.mmorpg.mir.model.purse.event.CurrencyRewardEvent;
import com.mmorpg.mir.model.quest.event.QuestCompletEvent;
import com.mmorpg.mir.model.quest.manager.QuestManager;
import com.mmorpg.mir.model.quest.model.QuestType;
import com.mmorpg.mir.model.quest.resource.QuestResource;
import com.mmorpg.mir.model.rank.model.DayKey;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.vip.event.FirstPayEvent;
import com.mmorpg.mir.model.warship.event.WarshipEvent;
import com.mmorpg.mir.model.welfare.event.BossDieEvent;
import com.mmorpg.mir.model.welfare.event.CopyEvent;
import com.mmorpg.mir.model.welfare.event.CountrySacrificeEvent;
import com.mmorpg.mir.model.welfare.event.CurrencyActionEvent;
import com.mmorpg.mir.model.welfare.event.ExpressEvent;
import com.mmorpg.mir.model.welfare.event.InvestigateEvent;
import com.mmorpg.mir.model.welfare.event.RescueEvent;
import com.mmorpg.mir.model.welfare.event.TempleEvent;
import com.mmorpg.mir.model.welfare.model.ActiveEnum;
import com.mmorpg.mir.model.welfare.model.ClawbackEnum;
import com.mmorpg.mir.model.welfare.model.Welfare;
import com.mmorpg.mir.model.world.World;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.DateUtils;

@Component
public class EventManager implements IEventManager {

	private static EventManager instance;

	@Static
	private Storage<String, ObjectResource> objectStorage;

	@Static("PUBLIC:ACTIVE_TARGET_OBJECTIDS")
	private ConfigValue<String[]> ACTIVE_TARGET_OBJECTIDS;

	@PostConstruct
	public void init() {
		instance = this;
	}

	public static EventManager getInstance() {
		return instance;
	}

	private boolean containTargetObjectId(String objId) {
		for (String id : ACTIVE_TARGET_OBJECTIDS.getValue()) {
			if (id.equals(objId)) {
				return true;
			}
		}
		return false;
	}

	/** 下线 */
	public void loginoutEvent(LogoutEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		Date now = new Date();
		Date loginTime = player.getPlayerEnt().getStat().getLastLogin();
		if (!DateUtils.isToday(loginTime)) { // 跨天清零
			player.getWelfare().getOnlineReward().clear();
			player.getWelfare().getOnlineReward()
					.setToDayOnlineTimes(now.getTime() - DateUtils.getFirstTime(now).getTime());
		} else { // 当天的累计
			player.getWelfare().getOnlineReward().addOnlineTimes(now.getTime() - loginTime.getTime());
		}
	}

	/** 上线 */
	public void loginEvent(LoginEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		/** 增加登录天数 */
		player.getWelfare().getSevenDayReward().addDayIndex();
		ActiveManager.getInstance().sendActiveNotRewardMail(player);
		ActiveManager.getInstance().check(player);
		/** 微端登录 */
		if (event.getLoginType() == LoginType.MINICLIENT.getValue()) {
			// 活跃度
			// ActiveManager.getInstance().exec(player,
			// ActiveEnum.ACTIVE_WEI_LOGIN);
			// 经验加成属性
			player.getGameStats().addModifiers(Welfare.MINICLIENT_EXP,
					WelfareConfigValueManager.getInstance().MINICLIENT_STAT.getValue());
		}

		// if (player.getVip().isVip()) {
		// int status =
		// player.getWelfare().getActiveValue().getExeStatus(ActiveEnum.ACTIVE_VIP);
		// if (status == ActiveStatusEnum.STATUS_COMPLETED.getStatus()) {
		// return;
		// }
		// ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_VIP);
		// }
	}

	/** 升级 */
	public void levelUpEvent(LevelUpEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		ClawbackManager.getInstance().checkCondition(player);
	}

	/** 完成每日任务 */
	public void questCompletEvent(QuestCompletEvent event) {
		String questId = event.getQuestId();
		int count = event.getCount();
		QuestResource questResource = QuestManager.staticQuestResources.get(questId, true);
		if (questResource.getType() == QuestType.DAY) {
			long playerId = event.getOwner();
			Player player = PlayerManager.getInstance().getPlayer(playerId);
			// ActiveManager.getInstance().exec(player,
			// ActiveEnum.ACTIVE_DAILYTASK, count);
			// 追回
			ClawbackManager.getInstance().exec(player, ClawbackEnum.CLAWBACK_EVENT_DAILYTASK, count);
		} else if (questResource.getType() == QuestType.RANDOM) {
			// long playerId = event.getOwner();
			// Player player = PlayerManager.getInstance().getPlayer(playerId);
			// 追回
			// ClawbackManager.getInstance().exec(player,
			// ClawbackEnum.CLAWBACK_EVENT_RANDOM, count);
			// ActiveManager.getInstance().exec(player,
			// ActiveEnum.ACTIVE_QUEST_RANDOM, count);
		}
	}

	/** boss死亡 */
	public void bossDieEvent(BossDieEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		BossResource res = BossManager.getInstance().getBossResource(event.getBossId(), true);
		if (event.isKnowPlayer() && !res.isElite()) {
			ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_BOSS_COUNTRY);
		}
	}

	public void monsterKillEvent(MonsterKillEvent event) {
		if (!event.isKnowPlayer()) {
			return;
		}
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		ObjectResource objResource = objectStorage.get(event.getKey(), true);
		boolean containId = containTargetObjectId(objResource.getKey());
		if (containId
				&& World.getInstance()
						.getMapResource(SpawnManager.getInstance().getSpawn(event.getSpawnKey()).getMapId())
						.getCountry() == 0) {
			ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_MONSTER_OUT);
		}
	}

	/** 消耗礼金 / 元宝 */
	public void currencyActionEvent(CurrencyActionEvent event) {
		// Player player =
		// PlayerManager.getInstance().getPlayer(event.getOwner());
		// CurrencyType type = event.getType();
		// int value = event.getValue();
		// if (type == CurrencyType.GIFT) {
		// ActiveManager.getInstance().exec(player,
		// ActiveEnum.ACTIVE_CONSUMPTION_GIFT, value);
		// } else if (type == CurrencyType.GOLD) {
		// ActiveManager.getInstance().exec(player,
		// ActiveEnum.ACTIVE_CONSUMPTION_GOLD, value);
		// }
	}

	/** 副本 */
	public void copyEvent(CopyEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		// if (event.getCopyType() == CopyType.VIP) { // VIP副本找回功能删除
		// // ClawbackManager.getInstance().exec(player,
		// ClawbackEnum.CLAWBACK_EVENT_VIP_COPY);
		// } else
		if (event.getCopyType() == CopyType.LADDER) { // 爬塔通过
			// player.getWelfare().getWelfareHistory().intoLadder();
			// ClawbackManager.getInstance().exec(player,
			// ClawbackEnum.CLAWBACK_EVENT_LADDER);
		} else if (event.getCopyType() == CopyType.EXP) {
			// ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_COPY);
			ClawbackManager.getInstance().exec(player, ClawbackEnum.CLAWBACK_EVENT_COPY);
		}
	}

	/** 国家购买 */
	// public void countryBuyEvent(CountryBuyEvent event) {
	// Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
	// ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_COUNTRY_BUY);
	// }

	/** 装备强化 */
	// public void enhanceEquipmentEvent(EnhanceEquipmentEvent event) {
	// Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
	// ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_ENHANCE);
	// }

	/** 押镖 */
	public void expressEvent(ExpressEvent event) {
		if (event.getExprssId().equals("46")) {
			return;
		}
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_EXPRESS);
		// 追回
		ClawbackManager.getInstance().exec(player, ClawbackEnum.CLAWBACK_EVENT_EXPRESS);
	}

	/** 刺探 */
	public void investigateEvent(InvestigateEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_INVESTIGATE);
		// 追回
		ClawbackManager.getInstance().exec(player, ClawbackEnum.CLAWBACK_EVENT_INVESTIGATE);
	}

	/** 营救 */
	public void rescueEvent(RescueEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		// 活跃度
		ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_RESCUE);
		// 追回
		ClawbackManager.getInstance().exec(player, ClawbackEnum.CLAWBACK_EVENT_RESCUE);
	}

	/** 签到 */
	// public void signEvent(SignEvent event) {
	// Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
	// ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_SIGN);
	// }

	/** 祭剑 */
	// public void smeltEquipmentEvent(SmeltEquipmentEvent event) {
	// Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
	// ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_SMELT);
	// }

	/** 太庙 */
	public void templeEvent(TempleEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_TEMPLE);
		// 追回
		ClawbackManager.getInstance().exec(player, ClawbackEnum.CLAWBACK_EVENT_TEMPLE);
	}

	/** VIP */
	// public void vipEvent(VipEvent event) {
	// Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
	// int dif = event.getVipResource().getAddActive()
	// -
	// VipManager.getInstace().getVipResource(event.getBeforeUpgradeVip()).getAddActive();
	// ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_VIP, dif);
	// }

	/** 重置了爬塔副本 */
	public void restCopy(Player player) {
		Map<Long, Integer> restTimeStamps = player.getWelfare().getWelfareHistory().getLastRestTime();
		ArrayList<Long> removeIds = New.arrayList();
		Long today = DayKey.valueOf().getLunchTime();
		for (Entry<Long, Integer> entry : restTimeStamps.entrySet()) {
			long beforeYesterDay = today - 2 * DateUtils.MILLIS_PER_DAY;
			if (entry.getKey() <= beforeYesterDay) {
				removeIds.add(entry.getKey());
			}
		}
		for (Long key : removeIds) {
			restTimeStamps.remove(key);
		}
		restTimeStamps.put(today, 1);
	}

	public void moduleOpenEvent(ModuleOpenEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		ActiveManager.getInstance().moduleOpen(player);
		ClawbackManager.getInstance().checkCondition(player);
		OnlineManager.getInstance().startOnlineCount(player);
	}

	public void countrySacrificeEvent(CountrySacrificeEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		// ActiveManager.getInstance().exec(player,
		// ActiveEnum.ACTIVE_COUNTRY_SACRIFICE);
		ClawbackManager.getInstance().exec(player, ClawbackEnum.CLAWBACK_EVENT_COUNTRYSACRIFICE);
	}

	// public void upgradeCombatSpiritEvent(CombatSpiritUpEvent event) {
	// Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
	// ActiveManager.getInstance().exec(player,
	// ActiveEnum.ACTIVE_UPGRADE_COMBATSPIRIT);
	// }

	public void warshipKing(WarshipEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		// ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_WARSHIP);
		ClawbackManager.getInstance().exec(player, ClawbackEnum.CLAWBACK_EVENT_WARSHIP);
	}

	public void firstPayEvent(FirstPayEvent event) {
		// 首充奖励
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		if (player.getWelfare().isFinishFirstPay()) {
			return;
		}
		player.getWelfare().finishFirstPay(player);
	}

	// public void exerciseEvent(ExerciseStartEvent event) {
	// Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
	// // int count =
	// //
	// player.getPack().getTodayUseSituation(ExerciseManager.getInstance().PRACTICE_ITEMS.getValue());
	// ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_EXERCISE);
	// }

	// public void monsterHunt(MonsterKillEvent event) {
	// Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
	// if (Math.abs(player.getLevel() - event.getLevel()) <=
	// WelfareConfigValueManager.getInstance().ACTIVE_KILL_MONSTER_LEVELGAP
	// .getValue()) {
	// ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_MONSTER_HUNT);
	// }
	// }

	// public void giftChange(CurrencyRewardEvent event) {
	// Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
	// if (event.getType() == CurrencyType.GIFT) {
	// player.getWelfare().getGiftCollect().addAmount(event.getAmount());
	// }
	// }

	public void killDisplomacy(PlayerKillDiplomacyEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_KILL_DISPLOMACY);
		ClawbackManager.getInstance().exec(player, ClawbackEnum.CLAWBACK_EVENT_KILL_DISPLOMACY);
	}

	// public void killCountryFlag(PlayerKillCountryFlagEvent event) {
	// Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
	// ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_KILL_FLAG);
	// ClawbackManager.getInstance().exec(player,
	// ClawbackEnum.CLAWBACK_EVENT_KILL_FLAG);
	// }

	public void killPlayer(KillPlayerEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_KILL_PLAYER);
	}

	public void dayRecharge(CurrencyRewardEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_DAY_RECHARGE, (int) event.getAmount());
	}

	public void countryFlagQuestFinish(CountryFlagQuestFinishEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_COUNTRY_FLAG_QUEST);
		ClawbackManager.getInstance().exec(player, ClawbackEnum.CLAWBACK_EVENT_KILL_FLAG);
	}

	public void countryCopyFinish(CountryCopyFinishEvent event) {
		Player player = PlayerManager.getInstance().getPlayer(event.getOwner());
		ActiveManager.getInstance().exec(player, ActiveEnum.ACTIVE_COUNTRY_COPY);
	}
}
