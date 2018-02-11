package com.mmorpg.mir.model.welfare.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.formula.Formula;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.welfare.resource.GiftResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class WelfareConfigValueManager implements IWelfareConfigValueManager {

	private WelfareConfigValueManager() {
	}

	private static WelfareConfigValueManager instance;

	@PostConstruct
	public void init() {
		instance = this;
	}

	public static WelfareConfigValueManager getInstance() {
		return instance;
	}

	public static void setInstance(WelfareConfigValueManager instance) {
		WelfareConfigValueManager.instance = instance;
	}

	/** 活跃度 可领奖次数列表 */
	@Static("WELFARE:WELFARE_VITALITY_AWARD_NUM")
	public ConfigValue<Integer[]> WELFARE_VITALITY_AWARD_NUM;

	/** 营救环任务的最后一个任务 */
	@Static("WELFARE:RESCUE_QUESTS")
	public ConfigValue<Map<String, ArrayList<String>>> RESCUE_QUESTS;

	/** 领取离线经验- 铜币消耗公式 */
	@Static("WELFARE:OFFLINE_CONSUME_EXP_COUNT_1")
	public Formula OFFLINE_CONSUME_EXP_COUNT_1;

	/** 领取离线经验-元宝消耗公式 */
	@Static("WELFARE:OFFLINE_CONSUME_EXP_COUNT_2")
	public Formula OFFLINE_CONSUME_EXP_COUNT_2;

	/** 签到满整月广播内容 */
	@Static("WELFARE:SIGN_FULL_BROADCAST")
	public ConfigValue<String> SIGN_FULL_BROADCAST;

	/** 最长累计离线时间 - 单位:小时 */
	@Static("WELFARE:OFFLINE_MAX_TIME_OUT")
	public ConfigValue<Integer> OFFLINE_MAX_TIME_OUT;

	@Static("WELFARE:MINICLIENT_STAT")
	public ConfigValue<Stat[]> MINICLIENT_STAT;

	@Static("WELFARE:ONE_SHOT_REWARD_ID")
	public ConfigValue<String[]> ONE_SHOT_REWARD_ID;

	/** 七天登录奖励id */
	@Static("WELFARE:SEVEN_DAY_ONLINE_REWARD_CHOOSERIDS")
	public ConfigValue<String[]> SEVEN_DAY_ONLINE_REWARD_CHOOSERIDS;

	@Static("WELFARE:ACTIVE_KILL_MONSTER_LEVELGAP")
	public ConfigValue<Integer> ACTIVE_KILL_MONSTER_LEVELGAP;

	/** 活跃值奖励邮件标题 */
	@Static("WELFARE:ACTIVE_REWARD_MAIL_TITLE_IL18N")
	public ConfigValue<String> ACTIVE_REWARD_MAIL_TITLE_IL18N;

	/** 活跃值奖励邮件内容 */
	@Static("WELFARE:ACTIVE_REWARD_MAIL_CONTENT_IL18N")
	public ConfigValue<String> ACTIVE_REWARD_MAIL_CONTENT_IL18N;

	@Static("WELFARE:FIRSTPAY_ARTIFACT_BUFF_REWARD_ID")
	public ConfigValue<String> ARTIFACT_BUFF_REWARD_ID;
	
	@Static("WELFARE:SIGN_COMPENSATE_MAIL_TITLE")
	public ConfigValue<String> SIGN_COMPENSATE_MAIL_TITLE;
	
	@Static("WELFARE:SIGN_COMPENSATE_MAIL_CONTENT")
	public ConfigValue<String> SIGN_COMPENSATE_MAIL_CONTENT;
	
	@Static
	private Storage<Integer, GiftResource> oneOffGiftResources;

	public GiftResource getOneOffGiftResource(int id) {
		return oneOffGiftResources.get(id, true);
	}

	public Collection<GiftResource> getAllOneOffGiftResources() {
		return oneOffGiftResources.getAll();
	}

	public ArrayList<Boolean> oneOffGiftStatus(Player player) {
		ArrayList<Boolean> status = player.getWelfare().getOneOffGift();
		for (GiftResource resource : getAllOneOffGiftResources()) {
			if (resource.getId() < status.size() && resource.getRecievedConditions().verify(player, false)) {
				status.set(resource.getId(), true);
			}
		}
		return status;
	}

}
