package com.mmorpg.mir.model.promote.manager;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.promote.packet.SM_Promotion;
import com.mmorpg.mir.model.promote.resource.PromotionResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class PromotionManager implements IPromotionManager {

	@Static
	public Storage<Integer, PromotionResource> promotionResources;

	private static PromotionManager self;

	private ArrayList<StatEffectId> statEffectIds;

	@PostConstruct
	void init() {
		self = this;
		statEffectIds = new ArrayList<StatEffectId>();
		for (PromotionResource resource : promotionResources.getAll()) {
			statEffectIds.add(StatEffectId.valueOf("PROMOTION_STATS" + resource.getId(), StatEffectType.PROMOTION));
		}
	}

	public StatEffectId getStatEffectId(int id) {
		if (id >= statEffectIds.size()) {
			return null;
		}
		return statEffectIds.get(id);
	}

	public static PromotionManager getInstance() {
		return self;
	}

	public PromotionResource getResource(int id, boolean throwException) {
		return promotionResources.get(id, throwException);
	}

	public void selfPromote(Player player, String questId) {
		int nextStageId = player.getPromotion().upgradeNextStageId(questId);
		if (nextStageId == 0) {
			return;
		}
		player.getPromotion().setStage(nextStageId);
		player.getPromotion().resetPromotionStats(true);

		EventBusManager.getInstance().syncSubmit(PromotionEvent.valueOf(player));
		promoteNotice(player, player.getPromotion().getStage());
		PacketSendUtility.sendPacket(player, SM_Promotion.valueOf(player.getPromotion().getStage()));
	}

	public void openPromote(Player player) {
		if (player.getPromotion().getStage() == 0) {
			player.getPromotion().setStage(1);
		}
		player.getPromotion().resetPromotionStats(true);
		PacketSendUtility.sendPacket(player, SM_Promotion.valueOf(player.getPromotion().getStage()));
		EventBusManager.getInstance().submit(PromotionEvent.valueOf(player));
	}

	private void promoteNotice(Player player, int stage) {
		PromotionResource resource = promotionResources.get(stage, true);
		if (resource.getBroadcastId() != null && resource.getBroadcastId().length() != 0) {
			I18nUtils utils = I18nUtils.valueOf(resource.getBroadcastId());
			utils.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
			utils.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
			ChatManager.getInstance().sendSystem(resource.getRealmOfbroadcast(), utils, null);
		}

		if (resource.getTvId() != null && resource.getTvId().length() != 0) {
			I18nUtils utils = I18nUtils.valueOf(resource.getTvId());
			utils.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
			utils.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
			ChatManager.getInstance().sendSystem(resource.getRealmOfTV(), utils, null);
		}
	}
	
	public String getI18nJobName(int role, int stage) {
		PromotionResource resource = promotionResources.get(stage, true);
		return resource.getJob().get(role + "");
	}
}
