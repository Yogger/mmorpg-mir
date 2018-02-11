package com.mmorpg.mir.model.monsterriot.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.cliffc.high_scale_lib.NonBlockingHashSet;
import org.h2.util.New;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.monsterriot.config.MonsterriotConfig;
import com.mmorpg.mir.model.monsterriot.controller.RiotBossController;
import com.mmorpg.mir.model.monsterriot.controller.RiotMonsterController;
import com.mmorpg.mir.model.monsterriot.packet.SM_MonsterRiot_MapInfo;
import com.mmorpg.mir.model.monsterriot.packet.SM_MonsterRiot_Reward;
import com.mmorpg.mir.model.monsterriot.packet.SM_Riot_Round_End;
import com.mmorpg.mir.model.monsterriot.packet.vo.MonsterRiotRankVO;
import com.mmorpg.mir.model.monsterriot.resource.MonsterRiotResource;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;
import com.mmorpg.mir.model.world.WorldMapInstance;

/**
 * 怪物攻城的控制器
 * 
 * @author Kuang Hao
 * @since v1.0 2015-8-4
 * 
 */
public class MonsterRiot {
	/** 每次参加过活动的所有玩家ID */
	private NonBlockingHashSet<Long> attendTodayPlayerIds = new NonBlockingHashSet<Long>();
	/** 玩家的怪物击杀数量记录 */
	private NonBlockingHashMap<Player, Long> monsterKillDamge = new NonBlockingHashMap<Player, Long>();
	/** 活动的怪物集合 */
	private List<List<Creature>> monsters = new ArrayList<List<Creature>>();
	/** 是否在活动 **/
	private AtomicBoolean isInAct = new AtomicBoolean();
	/** 当前第几轮 */
	private AtomicInteger currentRound = new AtomicInteger();
	/** 当前轮数死了的怪的数量 */
	private AtomicInteger currentRoundDieCount = new AtomicInteger();
	/** 活动所属的国家 */
	private CountryId countryId;
	/** 排名奖励情况 */
	private Map<Long, MonsterRiotRankVO> rewardStatus = new HashMap<Long, MonsterRiotRankVO>();
	/** 奖励结算的波数 */
	private NonBlockingHashSet<Integer> doRewardRoundSet = new NonBlockingHashSet<Integer>();
	
	/** 奖励排名的缓存  <round, <pid, rank>> */
	private NonBlockingHashMap<Integer, Map<Long, Integer>>  rankCache = new NonBlockingHashMap<Integer, Map<Long, Integer>>();

	private Future<?> sendPackFuture;

	public static MonsterRiot valueOf(CountryId id) {
		MonsterRiot riot = new MonsterRiot();
		riot.countryId = id;
		for (int i = 0; i < MonsterriotConfig.getInstance().getMaxRound(id.getValue()); i++) {
			riot.monsters.add(i, new CopyOnWriteArrayList<Creature>());
		}
		return riot;
	}

	/**
	 * 击杀怪物
	 * 
	 * @param player
	 */
	public long monsterKill(Player player, long damage) {
		long damgeResult = 0L;
		if (MonsterriotConfig.getInstance().getActConditions().verify(player, false)) {
			if (!isInAct.get()) {
				return damgeResult;
			}
			if (!monsterKillDamge.containsKey(player)) {
				damgeResult = damage;
			} else {
				damgeResult = monsterKillDamge.get(player) + damage;
			}
			monsterKillDamge.put(player, damgeResult);
			attendTodayPlayerIds.add(player.getObjectId());
		}
		return damgeResult;
	}

	/**
	 * 活动开始
	 */
	public void start(int round) {
		if (isInAct.compareAndSet(false, true)) {
			doRewardRoundSet.clear(); // 这次活动的 第一轮 (round可以不为1)
		}
		clearActivityData();

		initActData(round);

		spawnMonsterRiot();

		startSendTask();

	}

	private void initActData(int round) {
		currentRound.set(round);
		currentRoundDieCount.set(0);
	}

	public int getKillRank(Player player) {
		int rank = 0;
		if (monsterKillDamge.containsKey(player)) {
			Map<Long, Integer> rankStatus = rankCache.get(getCurrentRound());
			if (rankStatus != null) {
				Integer rankLog = rankStatus.get(player.getObjectId());
				rank = (rankLog == null? 0 : rankLog);
			}
		}
		return rank;
	}

	public long getCurrentRoundDamage(Player player) {
		Long damage = monsterKillDamge.get(player);
		return damage == null ? 0 : damage;
	}

	synchronized public void currentRoundEnd() {
		final int round = getCurrentRound();
		if (!doRewardRoundSet.add(round)) {
			return;
		}
		deleteAllRoundMonster(round);
		
		doSendPack();

		Map<Long, Integer> rankMap = rankCache.get(round);
		boolean isLastRound = MonsterriotConfig.getInstance().isLastRound(countryId.getValue(), round);

		if (rankMap != null) {
			// do reward
			// send Rank
			for (Entry<Long, Integer> entry : rankMap.entrySet()) {
				Player player = PlayerManager.getInstance().getPlayer(entry.getKey());
				int rank = entry.getValue();
				String chooserGroup = MonsterriotConfig.getInstance().getMyRankRewardChooser(rank, round,
						countryId.getValue());
				List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player, chooserGroup);
				Map<String, Object> params = New.hashMap();
				params.put("STANDARD_EXP", PlayerManager.getInstance().getStandardExp(player));
				params.put("LEVEL", player.getLevel());
				Reward reward = RewardManager.getInstance().creatReward(player, rewardIds, params);
				RewardManager.getInstance().grantReward(player, reward,
						ModuleInfo.valueOf(ModuleType.MONSTERRIOT, SubModuleType.MONSTERRIOT_REWARD));
				long damage = monsterKillDamge.get(player);
				rewardStatus.put(player.getObjectId(),
						MonsterRiotRankVO.valueof(rank, damage, player, reward));

				if (rank == 1 && isLastRound) {
					I18nUtils utils = I18nUtils.valueOf("406002");
					utils.addParm("name", I18nPack.valueOf(player.getName()));
					ChatManager.getInstance().sendSystem(7100384, utils, null,
							CountryManager.getInstance().getCountries().get(countryId));
				}
			}
		}
		

		List<MonsterRiotRankVO> rankResult = new ArrayList<MonsterRiotRankVO>(rewardStatus.values());
		Collections.sort(rankResult);

		int maxSize = MonsterriotConfig.getInstance().RANK_MAX_SIZE.getValue();
		Map<Integer, MonsterRiotRankVO> ranks = new HashMap<Integer, MonsterRiotRankVO>();
		for (int i = 0; i < rankResult.size() && i < maxSize; i++) {
			MonsterRiotRankVO vo = rankResult.get(i);
			ranks.put(vo.getRank(), vo);
		}

		for (Player pOnline : CountryManager.getInstance().getCountryByValue(countryId.getValue()).getCivils().values()) {
			MonsterRiotRankVO vo = rewardStatus.get(pOnline.getObjectId());
			if (vo == null) {
				continue;
			}
			int rank = vo.getRank();
			Reward reward = vo.getReward();
			PacketSendUtility.sendPacket(pOnline, SM_MonsterRiot_Reward.valueOf(rank, round, reward, ranks));
		}

		ServerState.getInstance().logAttendMonsterRiot(monsterKillDamge.keySet());

		// clear
		clearActivityData();
	}

	/**
	 * 活动结束
	 */
	synchronized public void end() {
		if (!isInAct.compareAndSet(true, false)) {
			return;
		}
		currentRoundEnd();
		
		CountryManager.getInstance().getCountries().get(countryId).sendPackAll(new SM_Riot_Round_End());

		ServerState.getInstance().setMonsterRiotEndDate(countryId.getValue(), new Date());
		stopSendTask();
		
		openServerExtraReward();
		
		attendTodayPlayerIds.clear();
		rankCache.clear();
	}

	private void openServerExtraReward() {
		if (!MonsterriotConfig.getInstance().getOpenServerActConditions().verify(null)) {
			return;
		}
		for (Long playerId : attendTodayPlayerIds) {
			I18nUtils titel18n = I18nUtils.valueOf(MonsterriotConfig.getInstance().RIOT_MAIL_I18NTITLE.getValue());
			I18nUtils contextl18n = I18nUtils.valueOf(MonsterriotConfig.getInstance().RIOT_MAIL_I18NCONTENT.getValue());
			Reward reward = RewardManager.getInstance().creatReward(null, MonsterriotConfig.getInstance().RIOT_MAIL_REWARD.getValue(), null);
			Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
			MailManager.getInstance().sendMail(mail, playerId);
		}
	}

	private void clearActivityData() {
		monsterKillDamge.clear();
		for (int i = 0; i < monsters.size(); i++) {
			for (Creature creature : monsters.get(i)) {
				creature.getController().delete();
			}
			monsters.get(i).clear();
		}
		rewardStatus.clear();
	}

	private void calcDamageRankResult() {
		Map<Long, Integer> ranks = New.hashMap();
		if (!monsterKillDamge.isEmpty()) {
			List<Entry<Player, Long>> entrys = new ArrayList<Entry<Player, Long>>(monsterKillDamge.entrySet());
			Collections.sort(entrys, new Comparator<Entry<Player, Long>>() {
				@Override
				public int compare(Entry<Player, Long> o1, Entry<Player, Long> o2) {
					long calc = o2.getValue() - o1.getValue();
					if (calc > 0) {
						return 1;
					} else if (calc == 0) {
						return 0;
					} else {
						return -1;
					}
				}
			});
			int i = 1;
			for (Entry<Player, Long> entry : entrys) {
				ranks.put(entry.getKey().getObjectId(), i);
				i++;
			}
		}
		
		rankCache.put(getCurrentRound(), ranks);
	}

	private void deleteAllRoundMonster(int round) {
		for (int i = 0, j = round; i < j; i++) {
			if (i >= monsters.size()) {
				return;
			}
			List<Creature> creatures = monsters.get(i);
			for (Creature creature : creatures) {
				creature.getController().delete();
			}
		}
	}

	private void spawnMonsterRiot() {
		// clear previous monster
		deleteAllRoundMonster(getCurrentRound() - 1);
		MonsterRiotResource resource = getResource();
		if (resource == null) {
			return;
		}
		for (String[] spawnKeys : resource.getSpawnIdMaps().values()) {
			for (String spawnKey : spawnKeys) {
				SpawnGroupResource spawnRes = SpawnManager.getInstance().getSpawn(spawnKey);
				for (int i = 0; i < spawnRes.getNum(); i++) {
					// TODO: 建立在 已知地图龙牙关和王城 只有一条线的条件下
					VisibleObject visObj = SpawnManager.getInstance().creatObject(spawnKey, 1);
					if (visObj.getObjectType() == ObjectType.BOSS) {
						RiotBossController controller = new RiotBossController(countryId);
						if (!MonsterriotConfig.getInstance().isLastRound(countryId.getValue(), getCurrentRound())) {
							controller.setBroad(false);
						}
						visObj.setController(controller);
						controller.setOwner((Creature) visObj);
					} else if (visObj.getObjectType() == ObjectType.MONSTER){
						RiotMonsterController controller = new RiotMonsterController(countryId);
						visObj.setController(controller);
						controller.setOwner((Creature) visObj);
					}
					getCurrentRoundCreatures().add((Creature) visObj);
				}
				// notice
				Map<String, Integer> notice = resource.getI18nNotice();
				if (notice == null) {
					continue;
				}
				for (Entry<String, Integer> entry : notice.entrySet()) {
					I18nUtils utils = I18nUtils.valueOf(entry.getKey());
					ChatManager.getInstance().sendSystem(entry.getValue(), utils, null);
				}
			}
		}

		for (Creature creature : getCurrentRoundCreatures()) {
			SpawnManager.getInstance().bringIntoWorld(creature, 1);
		}

		doSendPack();
	}

	public MonsterRiotResource getResource() {
		for (MonsterRiotResource resource : MonsterriotConfig.getInstance().getCountryMonsters(countryId.getValue())) {
			if (resource.getRound() == currentRound.get()) {
				return resource;
			}
		}
		return null;
	}

	public List<Creature> getCurrentRoundCreatures() {
		if (getCurrentRound() == 0 || getCurrentRound() > monsters.size()) {
			return new ArrayList<Creature>();
		}
		return monsters.get(currentRound.get() - 1);
	}

	public int getCurrentRound() {
		return currentRound.get();
	}

	public boolean isInAct() {
		return isInAct.get();
	}

	public void startSendTask() {
		if (sendPackFuture == null || sendPackFuture.isCancelled()) {
			sendPackFuture = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					if (isInAct()) {
						doSendPack();
					}
				}
			}, 5000, 5000);
		}
	}

	public void stopSendTask() {
		if (sendPackFuture != null) {
			sendPackFuture.cancel(false);
		}
	}

	private void doSendPack() {
		calcDamageRankResult(); // 计算排名 并缓存
		for (Integer mapId : MonsterriotConfig.getInstance().ACTIVITY_MAP_IDS.getValue()) {
			WorldMap map = World.getInstance().getWorldMap(mapId);
			if (map.getCountry().getValue() != countryId.getValue()) {
				continue;
			}
			for (WorldMapInstance worldMapInstance : map.getInstances().values()) {
				Iterator<Player> players = worldMapInstance.playerIterator();
				
				while (players.hasNext()) {
					Player player = players.next();
					if (player.getCountryValue() != map.getCountry().getValue()) {
						continue;
					}
					if (MonsterriotConfig.getInstance().getActConditions().verify(player, false)) {
						SM_MonsterRiot_MapInfo sm = getMapStatusPack(player, map);
						sm.setPush((byte) 1);
						PacketSendUtility.sendPacket(player, sm);
					}
				}
			}
		}
	}

	public Map<String, Integer> getCurrentMapRiotStatus(int mapId) {
		List<Creature> creatures = getCurrentRoundCreatures();
		Map<String, Integer> ret = New.hashMap();

		if (getResource() == null) {
			return ret;
		}

		// 这里放0值进去 纯粹只是前端的逻辑 前端让加的
		for (Entry<Integer, String[]> entry : getResource().getSpawnIdMaps().entrySet()) {
			if (entry.getKey() == mapId) {
				for (String spawnKey : entry.getValue()) {
					ret.put(spawnKey, 0);
				}
			}
		}

		for (Creature creature : creatures) {
			boolean aliveInMap = !creature.getLifeStats().isAlreadyDead() && creature.isSpawned();
			if (aliveInMap && creature.getMapId() == mapId) {
				Integer count = ret.get(creature.getSpawnKey());
				ret.put(creature.getSpawnKey(), count == null ? 1 : count + 1);
			}
		}
		return ret;
	}

	public SM_MonsterRiot_MapInfo getMapStatusPack(Player player, WorldMap map) {
		int rank = getKillRank(player);
		long damage = getCurrentRoundDamage(player);
		int round = getCurrentRound();
		Map<String, Integer> ret = getCurrentMapRiotStatus(map.getMapId());
		return SM_MonsterRiot_MapInfo.valueOf(rank, damage, round, ret);
	}

	public boolean checkRoundEnd() {
		int count = currentRoundDieCount.incrementAndGet();
		return count >= getResource().getMonsterCount();
	}

}
