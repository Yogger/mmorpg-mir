package com.mmorpg.mir.model.trigger.model.handle;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chat.model.ChannelType;
import com.mmorpg.mir.model.chat.model.show.object.ItemShow;
import com.mmorpg.mir.model.chat.resource.ChannelResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.lifegrid.model.LifeStorageType;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;

/**
 * 获取指定物品发的公告
 * 
 * @author 37.com
 * 
 */
@Component
public class UseItemNoticeTrigger extends AbstractTrigger {

	@Override
	public TriggerType getType() {
		return TriggerType.NOTICE;
	}

	@Override
	public void handle(Map<String, Object> contexts, TriggerResource resource) {
		final Reward reward = (Reward) contexts.get(TriggerContextKey.REWARD);
		final Player player = (Player) contexts.get(TriggerContextKey.PLAYER);

		LifeStorageType lifeGridType = null;

		if (resource.getKeys().containsKey(TriggerContextKey.LIFE_STORAGE_TYPE)) {
			lifeGridType = LifeStorageType.typeOf(Integer.parseInt(resource.getKeys().get(
					TriggerContextKey.LIFE_STORAGE_TYPE)));
		}
		for (RewardItem rewardItem : reward.getItems()) {
			if (resource.getKeys().get(TriggerContextKey.ITEM_ID).equals(rewardItem.getCode())) {
				String strI18NId = resource.getKeys().get(TriggerContextKey.I18N_RESOURCE_ID);
				String strChannelId = resource.getKeys().get(TriggerContextKey.CHANNEL_ID);

				if (StringUtils.isBlank(strI18NId)) {
					throw new IllegalArgumentException("TriggerResource-id[" + resource.getId() + "]缺少公告id");
				}
				if (StringUtils.isBlank(strChannelId)) {
					throw new IllegalArgumentException("TriggerResource-id[" + resource.getId() + "]缺少频道id");
				}

				I18nUtils utils = I18nUtils.valueOf(strI18NId)
						.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()))
						.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
				if (resource.getKeys().containsKey(TriggerContextKey.SHOW_ITEM)) {
					ItemShow show = new ItemShow();
					show.setOwner(player.getName());
					show.setKey(rewardItem.getCode());
					if (lifeGridType != null) {
						show.setItem(player.getLifeGridPool().getStorageByType(lifeGridType)
								.getItemByKey(rewardItem.getCode()));
					} else {
						show.setItem(player.getPack().getItemByKey(rewardItem.getCode()));
					}
					utils.addParm(I18NparamKey.ITEM, I18nPack.valueOf(show));
				} else {
					utils.addParm(I18NparamKey.ITEM,
							I18nPack.valueOf(ItemManager.getInstance().getResource(rewardItem.getCode()).getName()));
				}
				sendSystem(player, Integer.parseInt(strChannelId), utils);
			}
		}
	}

	private void sendSystem(Player player, int channelId, I18nUtils utils) {
		ChannelResource channelResource = ChatManager.getInstance().getResources().get(channelId, true);
		if (channelResource.getScope() == ChannelType.ALL) {
			ChatManager.getInstance().sendSystem(channelId, utils, null);
		}

		if (channelResource.getScope() == ChannelType.COUNTRY) {
			ChatManager.getInstance().sendSystem(channelId, utils, null, player.getCountry());
		}

		if (channelResource.getScope() == ChannelType.GANG) {
			ChatManager.getInstance().sendSystem(channelId, utils, null, player.getGang());
		}

	}
}
