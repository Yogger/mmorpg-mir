package com.mmorpg.mir.model.boss.model;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.boss.manager.BossManager;
import com.mmorpg.mir.model.boss.resource.BossResource;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.gameobjects.Boss;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.StatusNpc;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.trigger.packet.SM_Red_Gift;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.welfare.event.BossDieEvent;
import com.mmorpg.mir.model.welfare.model.BossGift;
import com.mmorpg.mir.model.world.World;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;

public class BossScheme {
	private static Logger logger = Logger.getLogger(BossScheme.class);
	/** BOSS信息 */
	private BossHistory bossHistory;
	/** 当前BOSS */
	private volatile Boss boss;
	/** 当前boss墓碑 */
	private volatile StatusNpc bombstone;

	private BossResource bossResource;
	/** boss下次刷新的时间 */
	private long nextBossRefreshTime;
	/** Boss 刷新器 */
	private Future<?> refreshBossFuture;
	/** Boss 刷新通知 */
	private Future<?> refreshNotifyFuture;
	
	/**
	 * boss重生
	 */
	public void spawnBoss() {
		if (boss != null && boss.isSpawned()) {
			return;
		}

		// 重生
		List<String> spawnIds = ChooserManager.getInstance().chooseValueByRequire(new Object(),
				bossResource.getSpawnChooserGroupId());
		if (spawnIds.isEmpty() || spawnIds.size() > 1) {
			String mesasge = String.format("bossResource[%s] spawnIds size[%s]", bossResource.getId(), spawnIds.size());
			logger.error(mesasge);
			throw new RuntimeException(mesasge);
		}
		// 刷新
		boss = (Boss) SpawnManager.getInstance().spawnObject(spawnIds.get(0), 1, bossHistory);
		boss.setBossScheme(this);
		// 让墓碑消失
		if (bombstone != null) {
			bombstone.getController().delete();
		}

		boss.getObserveController().addObserver(new ActionObserver(ObserverType.DIE) {
			@Override
			public void die(Creature creature) {
				bossDie(creature);
			}
		});
	}

	public BossView creartView() {
		BossView bv = new BossView();
		bv.setId(this.getBossResource().getId());
		if (boss == null || !boss.isSpawned() || boss.getLifeStats().isAlreadyDead()) {
			bv.setLastRefreshTime(nextBossRefreshTime);
			bv.setStatus(BossStatus.DIE.getValue());
		} else {
			bv.setStatus(BossStatus.SURVIVAL.getValue());
			bv.setSpawnId(boss.getSpawnKey());
			bv.setLevel(boss.getLevel());
		}
		bv.setLastByKillPlayers(getBossHistory().getLastByKillPlayers());
		return bv;
	}

	public static BossScheme valueOf(BossResource bossResource, BossHistory bossHistory) {
		BossScheme bossScheme = new BossScheme();
		bossScheme.bossHistory = bossHistory;
		bossScheme.bossResource = bossResource;
		return bossScheme;
	}

	/**
	 * BOSS被击杀
	 */
	public void bossDie(Creature creature) {
		Map<Integer, Player> ranks = boss.getDamageRank();
		if (bossResource.getRewardChooserGroupId() != null && !ranks.isEmpty()) {
			for (Entry<Integer, Player> rank : ranks.entrySet()) {
				// 构建奖励,邮件
				List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(rank.getKey(),
						bossResource.getRewardChooserGroupId());
				if (rewardIds.isEmpty()) {
					continue;
				}
				Reward reward = RewardManager.getInstance().creatReward(rank.getValue(), rewardIds, null);
				String title = ChooserManager.getInstance()
						.chooseValueByRequire(rank.getKey(), bossResource.getMailTitleChooserGroupId()).get(0);
				String context = ChooserManager.getInstance()
						.chooseValueByRequire(rank.getKey(), bossResource.getMailContextChooserGroupId()).get(0);
				I18nUtils titel18n = I18nUtils.valueOf(title);
				I18nUtils contextl18n = I18nUtils.valueOf(context);
				contextl18n.addParm("BOSS", I18nPack.valueOf(boss.getObjectResource().getName()));
				titel18n.addParm("BOSS", I18nPack.valueOf(boss.getObjectResource().getName()));
				Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
				MailManager.getInstance().sendMail(mail, rank.getValue().getObjectId());
			}
		}
		cancelRefreshBossFuture();
		long delayTime = bossResource.getRefreshTime() * DateUtils.MILLIS_PER_SECOND;
		refreshBossFuture = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				bossHistory.refresh();
				spawnBoss();
			}
		}, delayTime);
		if (getBossResource().getTombstoneSpawnId() != null) {
			bombstone = (StatusNpc) SpawnManager.getInstance().spawnObject(getBossResource().getTombstoneSpawnId(), 1);
			bombstone.setStatus((int) ((System.currentTimeMillis() + delayTime) % 1000000000));
		}
		nextBossRefreshTime = (System.currentTimeMillis() + delayTime);

		bossDieNotice(creature); // BOSS死亡的公告
		spawnNotice(creature, delayTime - BossManager.getInstance().BOSS_NOTITY_BEFORE_SPAWN.getValue()); // BOSS刷新的公告
		
		// 提交boss被杀事件
		for (Entry<Integer, Player> entry : ranks.entrySet()) {
			boolean knowPlayer = getBoss().getKnownList().knowns(entry.getValue());
			BossDieEvent event = BossDieEvent.valueOf(entry.getValue().getObjectId(), getBossResource().getId(),
					boss.getSpawnKey(), knowPlayer, entry.getKey());
			EventBusManager.getInstance().submit(event);
		}

		handOutGift(creature);
		
		Player player = null;
		if (creature instanceof Player) {
			player = (Player) creature;
		} else if (creature instanceof Summon) {
			Summon summon = (Summon) creature;
			player = summon.getMaster();
		}
		LogManager.addBossDie(player.getPlayerEnt().getServer(), player.getPlayerEnt().getAccountName(),
				player.getName(), System.currentTimeMillis(), player.getObjectId(), bossResource.getId(), delayTime,
				nextBossRefreshTime);
	}

	private void handOutGift(Creature creature) {
		if (bossResource.getGiftChooserGroup() == null || bossResource.getGiftChooserGroup().length() == 0) {
			return;
		}
		Player player = null;
		if (creature instanceof Player) {
			player = (Player) creature;
		} else if (creature instanceof Summon) {
			Summon summon = (Summon) creature;
			player = summon.getMaster();
		}
		long now = System.currentTimeMillis();
		BossGiftInfo giftInfo = BossGiftInfo.valueOf(getBoss().getSpawnKey(), player.createSimple());
		for (Player p : player.getCountry().getCivils().values()) {
			List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(p,
					bossResource.getGiftChooserGroup());
			Reward reward = RewardManager.getInstance().creatReward(player, rewardIds, null);
			BossGift gift = BossGift.valueOf(p, now, getBoss().getSpawnKey(), reward);
			giftInfo.getRankElements().add(gift);
			p.getWelfare().getBossGiftSet().add(gift);
			PacketSendUtility.sendPacket(p, SM_Red_Gift.valueOf(now, getBoss().getSpawnKey()));
		}
		bossHistory.getGiftInfoMap().put(now, giftInfo);
	}

	/**
	 * BOSS死亡公告
	 * 
	 * @param creature
	 */
	public void bossDieNotice(Creature creature) {
		if (bossResource.getKillNotice() == null || bossResource.getKillNotice().isEmpty()) {
			return;
		}
		
		if (bossResource.getKillNotice() != null) {
			Player player = null;
			if (creature instanceof Player) {
				player = (Player) creature;
			} else if (creature instanceof Summon) {
				player = ((Summon) creature).getMaster();
			}
			final Player finalPlayer = player;
			for (Entry<String, Integer> entry : bossResource.getKillNotice().entrySet()) {
				I18nUtils utils = I18nUtils.valueOf(entry.getKey());
				utils.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(finalPlayer.getName()));
				utils.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(finalPlayer.getCountry().getName()));
				utils.addParm(I18NparamKey.BOSS, I18nPack.valueOf(boss.getName()));
				Country country = finalPlayer.getCountry();
				if (boss.getCountry().getValue() != 0) {
					country = CountryManager.getInstance().getCountries().get(CountryId.valueOf(boss.getCountry().getValue()));
				}
				ChatManager.getInstance().sendSystem(entry.getValue(), utils, null, country);
			}
		}
	}

	/**
	 * BOSS刷新的公告
	 * 
	 * @param creature
	 * @param delay
	 */
	public void spawnNotice(Creature creature, long delay) {
		if (bossResource.getSpawnNotice() == null || bossResource.getSpawnNotice().isEmpty()) {
			return;
		}
		Player player = null;
		if (creature instanceof Player) {
			player = (Player) creature;
		} else if (creature instanceof Summon) {
			player = ((Summon) creature).getMaster();
		}
		final Player finalPlayer = player;
		refreshNotifyFuture = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (boss.isSpawned()) {
					return;
				}
				if (bossResource.getSpawnNotice() != null) {
					for (Entry<String, Integer> entry : bossResource.getSpawnNotice().entrySet()) {
						I18nUtils utils = I18nUtils.valueOf(entry.getKey());
						utils.addParm(I18NparamKey.BOSS, I18nPack.valueOf(boss.getName()));
						utils.addParm(I18NparamKey.MAPNAME,
								I18nPack.valueOf(World.getInstance().getMapResource(boss.getMapId()).getName()));
						utils.addParm("mapId", I18nPack.valueOf(boss.getSpawn().getMapId() + ""));
						utils.addParm("x", I18nPack.valueOf(boss.getSpawn().getX() + ""));
						utils.addParm("y", I18nPack.valueOf(boss.getSpawn().getY() + ""));
						ChatManager.getInstance().sendSystem(entry.getValue(), utils, null, finalPlayer.getCountry());
					}
				}
			}
		}, delay);
	}

	public void cancelRefreshBossFuture() {
		if (refreshBossFuture != null && !refreshBossFuture.isCancelled()) {
			refreshBossFuture.cancel(false);
		}
	}

	public void clearBombstone() {
		// 让墓碑消失
		if (bombstone != null) {
			bombstone.getController().delete();
		}
	}

	public void cancelRefreshNotifyFuture() {
		if (refreshNotifyFuture != null && !refreshNotifyFuture.isCancelled()) {
			refreshNotifyFuture.cancel(false);
		}
	}

	public BossHistory getBossHistory() {
		return bossHistory;
	}

	public void setBossHistory(BossHistory bossHistory) {
		this.bossHistory = bossHistory;
	}

	public Boss getBoss() {
		return boss;
	}

	public void setBoss(Boss boss) {
		this.boss = boss;
	}

	public BossResource getBossResource() {
		return bossResource;
	}

	public void setBossResource(BossResource bossResource) {
		this.bossResource = bossResource;
	}

	@JsonIgnore
	public long getNextBossRefreshTime() {
		return nextBossRefreshTime;
	}

	@JsonIgnore
	public void setNextBossRefreshTime(long nextBossRefreshTime) {
		this.nextBossRefreshTime = nextBossRefreshTime;
	}

}