package com.mmorpg.mir.model.reward.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.h2.util.New;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardProvider;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.reward.model.sample.RewardSample;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.JsonUtils;

/**
 * 奖励管理器
 * 
 * @author Kuang Hao
 * @since v1.0 2012-12-20
 * 
 */
@Component
public class RewardManager implements IRewardManager {
	private static final Logger logger = Logger.getLogger(RewardManager.class);
	@Static
	private Storage<String, RewardSample> rewardSamples;
	/** 奖励发放处理器 */
	private Map<RewardType, RewardProvider> providers = new HashMap<RewardType, RewardProvider>();

	private static RewardManager self;
	public static Storage<String, RewardSample> staticRewardSamples;

	@Static("PUBLIC:MAIL_MAX_ITEMS")
	public ConfigValue<Integer> MAIL_MAX_ITEMS;

	public static RewardManager getInstance() {
		return self;
	}

	@PostConstruct
	public void init() {
		self = this;
		staticRewardSamples = this.rewardSamples;
	}

	/**
	 * 根据奖励id和上下文发放奖励
	 * 
	 * @param playerId
	 * @param rewardSampleId
	 * @param module
	 * @param customs
	 * @return
	 */
	public Reward grantReward(Player player, List<String> rewardIds, ModuleInfo module, Map<String, Object> custom) {
		// 使用上下文参数
		Reward reward = Reward.valueOf();
		for (String reward1 : rewardIds) {
			try {
				reward.addReward(rewardSamples.get(reward1, true).createReward(custom));
			} catch (Exception e) {
				e.printStackTrace();
				String message = MessageFormatter.format("module[{}] rewardSample id[{}]解析上下文错误！ context[{}]",
						new String[] { module + "", reward1, JsonUtils.object2String(custom) }).getMessage();
				logger.error(message, e);
			}
		}
		return this.grantReward(player, reward, module);
	}

	public Reward creatReward(Player player, String rewardId, Map<String, Object> custom) {
		Reward reward = Reward.valueOf();
		try {
			reward.addReward(rewardSamples.get(rewardId, true).createReward(custom));
		} catch (Exception e) {
			logger.error(
					String.format("rewardSample id[{}]解析上下文错误！ context[{}]", rewardId, JsonUtils.object2String(custom)),
					e);
		}
		return reward;
	}

	public Reward creatReward(Player player, List<String> rewardIds, Map<String, Object> custom) {
		Reward reward = Reward.valueOf();
		for (String rewardId : rewardIds) {
			reward.addReward(this.creatReward(player, rewardId, custom));
		}
		return reward;
	}

	public Reward creatReward(Player player, List<String> rewardIds) {
		return creatReward(player, rewardIds, new HashMap<String, Object>());
	}

	/**
	 * 怪物掉落,奖励不合并,散落一地的那种 策划 丁涛说的，一个奖励中物品配的虽然是多个，但是也要分成一个一个的掉落
	 * 
	 * @param player
	 * @param rewardIds
	 * @param custom
	 * @return
	 */
	public Reward createRewardButNotMerge(Player player, List<String> rewardIds, Map<String, Object> custom) {
		Reward reward = Reward.valueOf();
		for (String rewardId : rewardIds) {
			for (RewardItem drop : this.creatReward(player, rewardId, custom).getItems()) {
				int amount = drop.getAmount();
				if (drop.getType().equals(RewardType.ITEM.name()) && amount > 1) {
					RewardItem item = RewardItem.valueOf(drop.getRewardType(), drop.getCode(), 1, drop.getParms());
					while (amount-- > 0) {
						reward.addRewardItem(item);
					}
				} else {
					reward.addRewardItem(drop);
				}
			}
		}
		return reward;
	}

	/**
	 * 根据奖励id和上下文发放奖励
	 * 
	 * @param playerId
	 * @param rewardSampleId
	 * @param module
	 * @param customs
	 * @return
	 */
	public Reward grantReward(Player player, List<String> rewardIds, ModuleInfo module) {
		// 构造上下文参数
		return this.grantReward(player, rewardIds, module, new HashMap<String, Object>());
	}

	/**
	 * 根据奖励id和上下文发放奖励
	 * 
	 * @param playerId
	 * @param rewardSampleId
	 * @param module
	 * @param customs
	 * @return
	 */
	public Reward grantReward(Player player, String rewardId, ModuleInfo module) {
		// 构造上下文参数
		return this.grantReward(player, rewardId, module, new HashMap<String, Object>());
	}

	/**
	 * 根据奖励id和上下文发放奖励
	 * 
	 * @param playerId
	 * @param rewardSampleId
	 * @param module
	 * @param customs
	 * @return
	 */
	public Reward grantReward(Player player, String rewardId, ModuleInfo module, Map<String, Object> custom) {
		List<String> rewardIds = new ArrayList<String>();
		rewardIds.add(rewardId);
		return this.grantReward(player, rewardIds, module, custom);
	}

	@Static("PUBLIC:MODULE_NAME")
	private ConfigValue<Map<String, String>> MODULE_NAME;

	public int calcRewardNeedPackSize(Reward reward) {
		int needEmpty = 0;
		for (RewardItem item : reward.getItems()) {
			if (item.getRewardType() == RewardType.ITEM) {
				needEmpty += ItemManager.getInstance().getItemLength(item.getCode(), item.getAmount());
			}
		}
		return needEmpty;
	}

	/**
	 * 该玩家的背包是否能装下所有奖励
	 * 
	 * @param player
	 * @param reward
	 * @return
	 */
	public boolean playerPackCanholdAll(Player player, Reward reward) {
		List<RewardItem> itemRewardItems = New.arrayList();
		for (RewardItem item : reward.getItems()) {
			if (item.getRewardType() == RewardType.ITEM) {
				itemRewardItems.add(item);
			}
		}

		int needEmpty = 0;
		for (RewardItem item : itemRewardItems) {
			needEmpty += ItemManager.getInstance().getItemLength(item.getCode(), item.getAmount());
		}

		return player.getPack().getEmptySize() >= needEmpty;
	}

	public Reward grantReward(Player player, Reward reward, ModuleInfo module) {
		List<RewardItem> itemRewardItems = New.arrayList();
		List<RewardItem> notItemRewardItems = New.arrayList();
		for (RewardItem item : reward.getItems()) {
			if (item.getRewardType() == RewardType.ITEM) {
				itemRewardItems.add(item);
			} else {
				notItemRewardItems.add(item);
			}
		}

		for (RewardItem item : notItemRewardItems) {
			this.providers.get(item.getRewardType()).withdraw(player, item, module);
		}

		int needEmpty = 0;
		for (RewardItem item : itemRewardItems) {
			needEmpty += ItemManager.getInstance().getItemLength(item.getCode(), item.getAmount());
		}

		boolean isToPack = reward.isToPack();
		if (player.getPack().getEmptySize() < needEmpty && needEmpty != 0 && isToPack) {
			// 如果背包装不下就发邮件
			Reward itemReward = Reward.valueOf();
			for (RewardItem item : itemRewardItems) {
				itemReward.addRewardItem(item);
			}
			for (Reward spilited : itemReward.spilteItemReward(MAIL_MAX_ITEMS.getValue())) {
				I18nUtils i18nTile = I18nUtils.valueOf("PACK_full_mail_title");
				I18nUtils i18nContext = I18nUtils.valueOf("PACK_full_mail_content");
				String modulName = null;
				if (MODULE_NAME != null) {
					modulName = MODULE_NAME.getValue().get(module.getModule() + "");
				}
				i18nContext.addParm("moduleName", I18nPack.valueOf(modulName != null ? modulName : module.getModule()));
				Mail mail = Mail.valueOf(i18nTile, i18nContext, null, spilited);
				MailManager.getInstance().sendMail(mail, player.getObjectId());
			}
		} else {
			for (RewardItem item : itemRewardItems) {
				this.providers.get(item.getRewardType()).withdraw(player, item, module);
			}
		}

		return reward;
	}

	public Reward grantReward(Player player, Reward[] rewards, ModuleInfo module) {
		Reward reward = Reward.valueOf();
		for (Reward r : rewards) {
			reward.addReward(r);
		}
		return grantReward(player, reward, module);
	}

	public void registerProvider(RewardProvider rewardProvider) {
		this.providers.put(rewardProvider.getType(), rewardProvider);
	}

	public Map<String, Object> getRewardParams(Player player) {
		Map<String, Object> params = New.hashMap();
		params.put("LEVEL", player.getLevel());
		params.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player));
		params.put("STANDARD_COINS", PlayerManager.getInstance().getStandardCoins(player));
		params.put("WORLD_CLASS_BONUS", WorldRankManager.getInstance().getPlayerWorldLevel(player));
		params.put("COUNTRY_EVALUATION", player.getCountry().getCountryLevel());
		params.put("WORLD_MILITARY_RANK", WorldRankManager.getInstance().getWorldMilitaryRank());
		params.put("PERSONAL_MILITARY_RANK", player.getMilitary().getRank());
		return params;
	}

	public Reward createRewardByChooserGroupId(Player player, String chooserGroupId) {
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player, chooserGroupId);
		return creatReward(player, rewardIds);
	}
	
}
