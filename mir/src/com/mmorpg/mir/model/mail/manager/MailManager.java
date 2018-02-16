package com.mmorpg.mir.model.mail.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.mail.entity.MailEnt;
import com.mmorpg.mir.model.mail.entity.MailGroupCollections;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.mail.model.MailBox;
import com.mmorpg.mir.model.mail.model.MailGroup;
import com.mmorpg.mir.model.mail.packet.SM_MailUpdate;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.service.EntityBuilder;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.utility.JsonUtils;

@Component
public class MailManager {
	private static MailManager self;

	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	/** 群邮件实体 */
	private MailGroupCollections mailGroupCollections;

	@Inject
	private EntityCacheService<Long, MailGroupCollections> mailGroupCollectionsDb;

	@Inject
	private EntityCacheService<Long, MailEnt> maiEntDb;

	@Autowired
	private SessionManager sessionManager;

	@Autowired
	private RewardManager rewardManager;

	@Autowired
	private PlayerManager playerManager;

	public static MailManager getInstance() {
		return self;
	}

	@PostConstruct
	public final void init() {
		mailGroupCollections = mailGroupCollectionsDb.loadOrCreate(1L, new EntityBuilder<Long, MailGroupCollections>() {
			@Override
			public MailGroupCollections newInstance(Long id) {
				return MailGroupCollections.valueOf();
			}
		});
		self = this;
	}

	public void clearMailGroup() {
		mailGroupCollections.getMailGroups().clear();
		mailGroupCollectionsDb.writeBack(mailGroupCollections.getId(), mailGroupCollections);
	}

	public MailEnt getMailEnt(Long playerId) {
		return maiEntDb.loadOrCreate(playerId, new EntityBuilder<Long, MailEnt>() {
			@Override
			public MailEnt newInstance(Long id) {
				return MailEnt.valueOf(id);
			}
		});
	}

	public void sendMail(Mail mail, Long playerId) {
		MailEnt mailEnt = getMailEnt(playerId);
		if (mailEnt.getMailBox().getPlayerId() == null) {
			Player player = playerManager.getPlayer(playerId);
			mailEnt.getMailBox().setPlayerId(player.getObjectId());
			mailEnt.getMailBox().setAccountName(player.getPlayerEnt().getAccountName());
			mailEnt.getMailBox().setName(player.getPlayerEnt().getName());
		}
		mailEnt.getMailBox().add(mail.cloneMail());
		notifyPlayerNewMail(mailEnt, playerId);
	}

	public void sendMailsByPlayerId(Mail mail, Collection<Long> playerIds) {
		for (Long playerId : playerIds) {
			sendMail(mail, playerId);
		}
	}

	public List<MailGroup> getMailGroups() {
		try {
			this.lock.readLock().lock();
			List<MailGroup> result = new ArrayList<MailGroup>();
			result.addAll(mailGroupCollections.getMailGroups());
			return result;
		} finally {
			this.lock.readLock().unlock();
		}
	}

	/**
	 * 领取邮件
	 * 
	 * @param player
	 */
	public void receiveGroupMail(Long playerId) {
		try {
			this.lock.writeLock().lock();

			List<MailGroup> mailGroups = mailGroupCollections.getMailGroups();

			if (mailGroups.isEmpty()) {
				return;
			}

			MailEnt mailEnt = getMailEnt(playerId);
			// 没有新的系统邮件，直接返回
			if (mailEnt.getMailBox().getGroupId() == mailGroupCollections.getMaxGroupId()) {
				return;
			}

			boolean add = false;

			// long maxGroupId = mailEnt.getMailBox().getGroupId();

			long now = System.currentTimeMillis();

			List<MailGroup> expiredList = New.arrayList();

			for (int i = mailGroups.size() - 1; i >= 0; i--) {
				MailGroup mailGroup = mailGroups.get(i);

				if (mailGroup.isExpired(now)) {
					expiredList.add(mailGroup);
					continue;
				}

				// 以前的邮件不用重复判断
				// if (mailGroup.getId() < maxGroupId) {
				// break;
				// }

				// 已经领取
				if (mailGroup.getReceiveds().contains(playerId)) {
					continue;
				}

				if (mailGroup.getReceivedCondtionResources() != null) {
					CoreConditions conditions = CoreConditionManager.getInstance()
							.getCoreConditions(mailGroup.getReceivedCondtionResources());
					Player player = PlayerManager.getInstance().getPlayer(playerId);
					if (player == null || (!conditions.verify(player, false))) {
						continue;
					}
				}

				// 发放邮件
				mailGroup.getReceiveds().add(playerId);
				Mail mail = mailGroup.createMail();
				mailEnt.getMailBox().add(mail);

				add = true;
			}

			if (!expiredList.isEmpty()) {
				mailGroups.removeAll(expiredList);
			}
			if (add) {
				mailGroupCollectionsDb.writeBack(mailGroupCollections.getId(), mailGroupCollections);
				notifyPlayerNewMail(mailEnt, playerId);
			}

		} finally {
			this.lock.writeLock().unlock();
		}
	}

	public void addMailGroup(MailGroup mailGroup) {
		try {
			this.lock.writeLock().lock();
			mailGroupCollections.addMailGroup(mailGroup);
			mailGroupCollectionsDb.writeBack(mailGroupCollections.getId(), mailGroupCollections);
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	private void notifyPlayerNewMail(MailEnt mailEnt, Long playerId) {
		// 这里也需要存储一下
		maiEntDb.writeBack(mailEnt.getId(), mailEnt);
		if (sessionManager.isOnline(playerId)) {
			// 通知玩家新邮件的变化
			PacketSendUtility.sendPacket(PlayerManager.getInstance().getPlayer(playerId),
					SM_MailUpdate.valueOf(mailEnt.getMailBox()));
		}
	}

	public void readMail(Player player, int index) {
		MailEnt mailEnt = getMailEnt(player.getObjectId());
		MailBox mailBox = mailEnt.getMailBox();
		Mail mail = mailBox.getMailByIndex(index);

		if (mail == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		mail.setLook(1);
		mailEnt.getMailBox().markMailByIndex(index);

		notifyPlayerNewMail(mailEnt, player.getObjectId());
		LogManager.mailLog(player.getPlayerEnt().getAccountName(), player.getObjectId(), player.getName(),
				System.currentTimeMillis(), mail.getTitle(), mail.getContext(),
				JsonUtils.object2String(mail.getI18nTile()), JsonUtils.object2String(mail.getI18nContext()),
				JsonUtils.object2String(mail.getReward()), 3);
	}

	public void rewardMail(Player player, int index) {
		MailEnt mailEnt = getMailEnt(player.getObjectId());
		MailBox mailBox = mailEnt.getMailBox();
		Mail mail = mailBox.getMailByIndex(index);
		if (mail == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		if (player.getPack().isFull()) {
			throw new ManagedException(ManagedErrorCode.PACK_FULL);
		}

		mail.setLook(1);
		Reward reward = mail.getReward();
		if (reward == null) {
			throw new ManagedException(ManagedErrorCode.MAIL_OPERATOR_TOO_FAST);
		}
		int needEmpty = rewardManager.calcRewardNeedPackSize(reward);
		if (needEmpty != 0 && player.getPack().getEmptySize() < needEmpty) {
			throw new ManagedException(ManagedErrorCode.PACK_NOT_ENOUGH);
		}
		reward = rewardManager.grantReward(player, reward,
				ModuleInfo.valueOf(ModuleType.MAIL, SubModuleType.MAIL_REWARD));
		mail.setReward(null);
		mailBox.markMailByIndex(index);

		notifyPlayerNewMail(mailEnt, player.getObjectId());
		LogManager.mailLog(player.getPlayerEnt().getAccountName(), player.getObjectId(), player.getName(),
				System.currentTimeMillis(), mail.getTitle(), mail.getContext(),
				JsonUtils.object2String(mail.getI18nTile()), JsonUtils.object2String(mail.getI18nContext()),
				JsonUtils.object2String(mail.getReward()), 4);
	}

	public void deleteMail(Player player, Set<Integer> indexs, boolean isDirectDelete) {
		MailEnt mailEnt = getMailEnt(player.getObjectId());
		MailBox mailBox = mailEnt.getMailBox();
		for (int index : indexs) {
			Mail mail = mailBox.getMailByIndex(index);
			if (mail == null) {
				throw new ManagedException(ManagedErrorCode.MAIL_OPERATOR_TOO_FAST);
			}
		}

		if (!isDirectDelete) {
			Reward reward = Reward.valueOf();
			for (int index : indexs) {
				Mail mail = mailBox.getMailByIndex(index);
				if (mail == null) {
					throw new ManagedException(ManagedErrorCode.MAIL_OPERATOR_TOO_FAST);
				}
				if (mail.getReward() != null) {
					reward.addReward(mail.getReward());
				}
			}

			// 验证背包是否满
			List<RewardItem> itemRewardItems = New.arrayList();
			for (RewardItem item : reward.getItems()) {
				if (item.getRewardType() == RewardType.ITEM) {
					itemRewardItems.add(item);
				}
			}
			int needEmpty = 0;
			for (RewardItem item : itemRewardItems) {
				needEmpty += ItemManager.getInstance().createItems(item.getCode(), item.getAmount()).length;
			}
			if (player.getPack().getEmptySize() < needEmpty && needEmpty != 0) {
				throw new ManagedException(ManagedErrorCode.PACK_FULL);
			}

			reward = rewardManager.grantReward(player, reward,
					ModuleInfo.valueOf(ModuleType.MAIL, SubModuleType.MAIL_REWARD));
		}
		for (int index : indexs) {
			mailBox.deleteMailByIndex(index);
		}

		notifyPlayerNewMail(mailEnt, player.getObjectId());
	}
}
