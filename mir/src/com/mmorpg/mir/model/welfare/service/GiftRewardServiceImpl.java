package com.mmorpg.mir.model.welfare.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.boss.manager.BossManager;
import com.mmorpg.mir.model.boss.model.BossGiftInfo;
import com.mmorpg.mir.model.boss.model.BossHistory;
import com.mmorpg.mir.model.boss.resource.BossResource;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.welfare.event.GiftrecievedEvent;
import com.mmorpg.mir.model.welfare.manager.WelfareConfigValueManager;
import com.mmorpg.mir.model.welfare.model.BossGift;
import com.mmorpg.mir.model.welfare.packet.SM_One_Off_Gift;
import com.mmorpg.mir.model.welfare.packet.SM_Query_Gift;
import com.mmorpg.mir.model.welfare.packet.SM_Recieve_Gift;
import com.mmorpg.mir.model.welfare.resource.GiftResource;
import com.windforce.common.event.core.EventBusManager;

@Component
public class GiftRewardServiceImpl implements GiftRewardService {

	@Autowired
	private WelfareConfigValueManager welfareManager;

	public void getOneOffGift(Player player, int giftType) {
		GiftResource resource = welfareManager.getOneOffGiftResource(giftType);
		if (!resource.getConditions().verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}

		player.getWelfare().rewardOneOffGiftIsReward(giftType);

		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player,
				resource.getRewardChooserGroup());
		Reward reward = RewardManager.getInstance().creatReward(player, rewardIds);
		if (resource.getMailTitle() != null) {
			I18nUtils titel18n = I18nUtils.valueOf(resource.getMailTitle());
			I18nUtils contextl18n = I18nUtils.valueOf(resource.getMailText());
			Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
			MailManager.getInstance().sendMail(mail, player.getObjectId());
		} else {
			RewardManager.getInstance().grantReward(player, reward,
					ModuleInfo.valueOf(ModuleType.ONE_OFF, SubModuleType.ONE_OFF_GIFT));
		}

		SM_One_Off_Gift sm = new SM_One_Off_Gift();
		sm.setCode(giftType);
		PacketSendUtility.sendPacket(player, sm);
		EventBusManager.getInstance().submit(GiftrecievedEvent.valueOf(player.getObjectId()));
	}

	public void queryRedGift(Player player, long deadTime, String spawnKey) {
		SpawnGroupResource resource = SpawnManager.getInstance().getSpawn(spawnKey);
		BossResource bossResource = BossManager.getInstance().getBossResource(resource.getBossResourceId(), true);
		BossHistory history = BossManager.getInstance().loadOrCreateBossEntity(bossResource);
		BossGiftInfo info = history.getGiftInfoMap().get(deadTime);
		if (info == null) {
			throw new ManagedException(ManagedErrorCode.RED_GIFT_OVERDUE);
		}
		Iterator<BossGift> iterator = player.getWelfare().getBossGiftSet().iterator();
		while (iterator.hasNext()) {
			BossGift gift = iterator.next();
			if (gift.getDeadTime() == deadTime && gift.getSpawnKey().equals(spawnKey)) {
				PacketSendUtility.sendPacket(player, SM_Query_Gift.valueOf(history.getLastAttacker(), info, spawnKey,
						gift.getReward().getTotalAmountByType(RewardType.CURRENCY)));
				return;
			}
		}
	}

	public void recieveRedGift(Player player, long deadTime, String spawnKey) {
		BossGift gift = BossGift.valueOf(player, deadTime, spawnKey);
		if (player.getWelfare().getBossGiftSet().contains(gift)) {
			Iterator<BossGift> iterator = player.getWelfare().getBossGiftSet().iterator();
			while (iterator.hasNext()) {
				BossGift g = iterator.next();
				if (g.getDeadTime() == deadTime && g.getSpawnKey().equals(spawnKey)) {
					RewardManager.getInstance().grantReward(player, g.getReward(),
							ModuleInfo.valueOf(ModuleType.BOSS_GIFT, SubModuleType.RECIEVED_BOSSGIFT));
					PacketSendUtility.sendPacket(player,
							SM_Recieve_Gift.valueOf(g.getReward().getTotalAmountByType(RewardType.CURRENCY)));
					player.getWelfare().getBossGiftSet().remove(g);
					break;
				}
			}
		} else {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.RED_GIFT_OVERDUE);
		}
	}

}
