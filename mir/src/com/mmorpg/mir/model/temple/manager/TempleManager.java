package com.mmorpg.mir.model.temple.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.ClearAndMigrate;
import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.country.model.Temple;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.player.packet.SM_CountryPlayerInfo;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.effecttemplate.EffectTemplate;
import com.mmorpg.mir.model.skill.effecttemplate.HeroRankEffect;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.temple.model.Brick;
import com.mmorpg.mir.model.temple.model.TempleHistory;
import com.mmorpg.mir.model.temple.packet.SM_Query_Temple_Status;
import com.mmorpg.mir.model.temple.packet.SM_Temple_AccpetQuest;
import com.mmorpg.mir.model.temple.packet.SM_Temple_BrickSuccess;
import com.mmorpg.mir.model.temple.packet.SM_Temple_Change_Brick;
import com.mmorpg.mir.model.temple.packet.SM_Temple_Put_Brick;
import com.mmorpg.mir.model.temple.packet.SM_Temple_StartTake_Brick;
import com.mmorpg.mir.model.temple.packet.SM_Temple_Take_Brick;
import com.mmorpg.mir.model.temple.resouce.BrickResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.welfare.event.TempleEvent;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.New;
import com.windforce.common.utility.SelectRandom;

@Component
public class TempleManager implements ITempleManager {

	@Autowired
	private CoreConditionManager conditionManager;

	@Static
	private Storage<String, BrickResource> brickResources;

	@Autowired
	private RewardManager rewardManager;

	@Static("COUNTRY:ACCEPT_CONDITIONIDS")
	private ConfigValue<String[]> ACCEPT_CONDITIONIDS;

	@Static("COUNTRY:COLOR_FACTOR")
	private ConfigValue<Integer[]> COLOR_FACTOR;

	@Static("COUNTRY:HERO_RANK_CHOOSER_1")
	public ConfigValue<String> HERO_RANK_CHOOSER_1;

	@Static("COUNTRY:HERO_RANK_CHOOSER_2")
	public ConfigValue<String> HERO_RANK_CHOOSER_2;

	@Static("COUNTRY:HERO_RANK_BUFF_1")
	public ConfigValue<Integer> HERO_RANK_BUFF_1;

	@Static("COUNTRY:HERO_RANK_BUFF_2")
	public ConfigValue<Integer> HERO_RANK_BUFF_2;

	@Static("COUNTRY:CHANGE_BRICK_GOLD_ACT")
	public ConfigValue<String[]> CHANGE_BRICK_GOLD_ACT;

	/** 搬砖引导的时间，毫秒 */
	@Static("COUNTRY:BRICK_CAST_TIME")
	public ConfigValue<Integer> BRICK_CAST_TIME;

	@Static("COUNTRY:COUNTRY_TEMPLE_SPY_COLOR")
	public ConfigValue<Map<String, String>> COUNTRY_TEMPLE_SPY_COLOR;

	private Map<Integer, Integer> currentBrickMapStatus;

	private static TempleManager instance;

	@PostConstruct
	public void init() {
		instance = this;
		if (ClearAndMigrate.clear) {
			return;
		}

		currentBrickMapStatus = new ConcurrentHashMap<Integer, Integer>();

		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Runnable() {

			@Override
			public void run() {
				for (Integer mapId : ConfigValueManager.getInstance().COUNTRY_ACT_DISPLAY_MAPID.getValue()) {
					int count = 0;
					for (WorldMapInstance instance : World.getInstance().getWorldMap(mapId).getInstances().values()) {
						Iterator<Player> iterator = instance.playerIterator();
						while (iterator.hasNext()) {
							Player next = iterator.next();
							if (next.getTempleHistory().getCurrentBrick() != null) {
								count++;
							}
						}
					}
					currentBrickMapStatus.put(mapId, count);
				}
			}
		}, 5000, 5000);
	}

	private String selectBrick(Player player) {
		// 昨日英雄排行榜BUFF
		String value = null;
		Effect heroEffect = player.getEffectController().getAnormalEffect(HeroRankEffect.HERO_BUFF);
		if (heroEffect != null) {
			for (EffectTemplate template : heroEffect.getEffectTemplates()) {
				if (template.getEffectTemplateId() == HERO_RANK_BUFF_1.getValue().intValue()) {
					value = ChooserManager.getInstance().chooser(HERO_RANK_CHOOSER_1.getValue()).get(0);
					break;
				}
				if (template.getEffectTemplateId() == HERO_RANK_BUFF_2.getValue().intValue()) {
					value = ChooserManager.getInstance().chooser(HERO_RANK_CHOOSER_2.getValue()).get(0);
					break;
				}
			}
		} else {
			SelectRandom<String> selector = new SelectRandom<String>();
			for (BrickResource resource : getBrickResources().getAll()) {
				selector.addElement(resource.getId(), resource.getWeight());
			}
			value = selector.run();
		}
		return value;
	}

	public void acceptQuest(Player player) {
		player.getTempleHistory().refresh();
		if (player.getTempleHistory().getQuestCountry() != 0) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (player.getTempleHistory().getCurrentBrick() != null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (player.getTempleHistory().getCount() >= ConfigValueManager.getInstance().COUNTRY_TEMPLE_QUEST_MAX_BRICK
				.getValue() + player.getVip().getResource().getExBrickCount()) {
			throw new ManagedException(ManagedErrorCode.COUNTRY_TEMPLE_MAX_QUEST);
		}
		CoreConditions conditions = CoreConditionManager.getInstance().getCoreConditions(1,
				ACCEPT_CONDITIONIDS.getValue());
		if (!conditions.verify(player, true)) {
			PacketSendUtility.sendErrorMessage(player);
			return;
		}
		player.getTempleHistory().setQuestCountry(selectCountry(player.getCountryValue()));
		PacketSendUtility.sendPacket(player, SM_Temple_AccpetQuest.valueOf(player.getTempleHistory()));
	}

	public int selectCountry(int playerCountry) {
		int country = (Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) % 2 + 1;
		int result = (playerCountry + country) > 3 ? playerCountry + country - 3 : playerCountry + country;
		return result;
	}

	public static void main(String[] args) {
		for (int j = 0; j < 10; j++) {
			for (int i = 1; i <= 3; i++) {
				int country = (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + j) % 2 + 1;
				int result = (i + country) > 3 ? i + country - 3 : i + country;
				System.out.println(result);
				if (result == i) {
					throw new RuntimeException();
				}
			}
		}

	}

	public void changeBrick(Player player, boolean useGold) {
		if (player.getLifeStats().isAlreadyDead() || (!player.isSpawned())) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}
		player.getTempleHistory().refresh();
		Brick brick = player.getTempleHistory().getCurrentBrick();
		if (brick == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		if (player.getCoolDownContainer().isTempleBrickDisable(brick.getFromTemple().getCountry().getId().getValue())) {
			return;
		}
		BrickResource brickResource = getBrickResources().get(brick.getId(), true);

		if (!brickResource.isCanChange()) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		if (useGold) { // 一键
			if (!player.getVip().getResource().isBrickOrange()) {
				throw new ManagedException(ManagedErrorCode.VIP_CONDITION_NOT_SATISFY);
			}
			CoreActions actions = CoreActionManager.getInstance().getCoreActions(1, CHANGE_BRICK_GOLD_ACT.getValue());
			actions.verify(player, true);
			actions.act(player, ModuleInfo.valueOf(ModuleType.TEMPLE, SubModuleType.BRICK_ACT));
			brick.changeId(brickResource.getBestId());
			PacketSendUtility.sendPacket(player, SM_Temple_Change_Brick.valueOf(brick, useGold));
			publicNotice(player, brick);
			PacketSendUtility.broadcastPacket(player, SM_CountryPlayerInfo.valueOf(player));
		} else {
			if (brick.changeTimeCD(ConfigValueManager.getInstance().COUNTRY_TEMPLE_CHANGE_BRICK_CD.getValue())) {
				throw new ManagedException(ManagedErrorCode.CHANGE_BRICK_CD);
			}
			String[] conditionIds = ConfigValueManager.getInstance().COUNTRY_TEMPLE_CHANGE_BRICK_CONDITIONS.getValue()
					.get(brick.getFromTemple().getCountry().getId().getValue());
			CoreConditions conditions = conditionManager.getCoreConditions(1, conditionIds);
			if (!conditions.verify(player, true)) {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}
			brick.changeId(selectBrick(player));
			if (player.getEffectController().contains(TempleHistory.BRICK_BUFF_GROUP)) {
				player.getEffectController().clearEffect(
						player.getEffectController().getAnormalEffect(TempleHistory.BRICK_BUFF_GROUP));
			}
			PacketSendUtility.broadcastPacket(player, SM_CountryPlayerInfo.valueOf(player));
			PacketSendUtility.sendPacket(player, SM_Temple_Change_Brick.valueOf(brick, useGold));
			publicNotice(player, brick);
		}

		player.getEffectController().unsetAbnormal(EffectId.BRICK, true);
		player.removeTempleBrickCoolDown();

	}

	public SM_Temple_StartTake_Brick startBrick(final Player player, int country) {
		if (player.getLifeStats().isAlreadyDead() || (!player.isSpawned())) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}
		if (player.getCountryValue() == country) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		ArrayList<String> conditionIds = ConfigValueManager.getInstance().COUNTRY_TEMPLE_BRICK_CONDITIONS.getValue()
				.get(String.valueOf(country));
		String[] condIds = new String[conditionIds.size()];
		conditionIds.toArray(condIds);
		CoreConditions conditions = conditionManager.getCoreConditions(1, condIds);
		if (!conditions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		long endTime = System.currentTimeMillis() + BRICK_CAST_TIME.getValue();
		SM_Temple_StartTake_Brick sm = SM_Temple_StartTake_Brick.valueOf(country, endTime);
		player.setTempleBrick(endTime, country);
		player.unRide();
		player.getObserveController().attach(
				new ActionObserver(ObserverType.MOVE, ObserverType.ATTACK, ObserverType.SKILLUSE, ObserverType.DIE) {

					@Override
					public void moved() {
						breakGather();
					}

					@Override
					public void attack(Creature creature) {
						breakGather();
					}

					@Override
					public void skilluse(Skill skill) {
						breakGather();
					}

					@Override
					public void die(Creature creature) {
						breakGather();
					}

					private void breakGather() {
						if (player.getEffectController().isAbnoramlSet(EffectId.BRICK)) {
							player.getEffectController().unsetAbnormal(EffectId.BRICK, true);
							player.removeTempleBrickCoolDown();
						}
					}

				});

		player.getEffectController().setAbnormal(EffectId.BRICK, true);
		return sm;
	}

	public void endTakeBrick(final Player player, int country) {
		player.getTempleHistory().refresh();
		if (player.getCountryValue() == country) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		if (player.getCoolDownContainer().isTempleBrickDisable(country)) {
			throw new ManagedException(ManagedErrorCode.TEMPLE_BRICK_CD);
		}
		if (player.getTempleHistory().getCurrentBrick() != null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		ArrayList<String> conditionIds = ConfigValueManager.getInstance().COUNTRY_TEMPLE_BRICK_CONDITIONS.getValue()
				.get(String.valueOf(country));
		String[] condIds = new String[conditionIds.size()];
		conditionIds.toArray(condIds);
		CoreConditions conditions = conditionManager.getCoreConditions(1, condIds);
		if (!conditions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		Temple targetTemple = CountryManager.getInstance().getCountries().get(CountryId.valueOf(country)).getTemple();

		Brick brick = Brick.valueOf(player.getObjectId(), targetTemple, selectBrick(player));
		Future<?> future = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				player.getTempleHistory().timeFail(
						ConfigValueManager.getInstance().COUNTRY_TEMPLE_BRICK_DEADTIME.getValue());
			}
		}, ConfigValueManager.getInstance().COUNTRY_TEMPLE_BRICK_DEADTIME.getValue());
		brick.setFuture(future);

		brick.getFromTemple().reduceBrick();
		brick.getFromTemple().getTakedBricks().add(player.getObjectId());
		player.getTempleHistory().setCurrentBrick(brick);
		player.getTempleHistory().setLastRefreshTime(System.currentTimeMillis());
		PacketSendUtility.broadcastPacket(player, SM_CountryPlayerInfo.valueOf(player));
		PacketSendUtility.sendPacket(player, SM_Temple_Take_Brick.valueOf(brick));
		publicNotice(player, brick);
		player.getEffectController().unsetAbnormal(EffectId.BRICK, true);
		player.removeTempleBrickCoolDown();
	}

	public void putBrick(final Player player) {
		player.getTempleHistory().refresh();
		if (player.getTempleHistory().getCurrentBrick() == null) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		Brick brick = player.getTempleHistory().getCurrentBrick();
		ArrayList<String> conditionIds = ConfigValueManager.getInstance().COUNTRY_TEMPLE_BRICK_CONDITIONS.getValue()
				.get(String.valueOf(player.getCountryValue()));
		String[] condIds = new String[conditionIds.size()];
		conditionIds.toArray(condIds);
		CoreConditions conditions = conditionManager.getCoreConditions(1, condIds);
		if (!conditions.verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		brick.getFuture().cancel(false);

		boolean rightBrick = player.getTempleHistory().getCurrentBrick().getFromTemple().getCountry().getId()
				.getValue() == player.getTempleHistory().getQuestCountry() ? true : false;

		player.getTempleHistory().getCurrentBrick().getFromTemple().getTakedBricks().remove(player.getObjectId());
		player.getCountry().getTemple().addBrick(ConfigValueManager.getInstance().COUNTRY_TEMPLE_MAX_BRICK.getValue());
		player.getTempleHistory().addHistoryColorCount(Integer.parseInt(brick.getId())); // 记录放砖块的颜色
		player.getTempleHistory().success();
		if (rightBrick) {
			Reward reward = createExpressReward(player, brick.getResource().getChooserGroupId(),
					Integer.valueOf(brick.getResource().getId()));
			rewardManager
					.grantReward(player, reward, ModuleInfo.valueOf(ModuleType.TEMPLE, SubModuleType.BRICK_REWARD));
			player.getTempleHistory().setQuestCountry(0);
			PacketSendUtility.sendPacket(player, SM_Temple_Put_Brick.valueOf(reward));
			PacketSendUtility.sendPacket(player, new SM_Temple_BrickSuccess());
			EventBusManager.getInstance().submit(TempleEvent.valueOf(player, false));
			player.getTempleHistory().addCount(1);
			LogManager.brick(player.getObjectId(), player.getPlayerEnt().getServer(), player.getPlayerEnt()
					.getAccountName(), player.getName(), System.currentTimeMillis(), brick.getFromTemple().getCountry()
					.getId().getValue(), player.getCountryValue(), player.getTempleHistory().getCount(), player
					.getTempleHistory().getCountAll(), 0);
		} else {
			PacketSendUtility.sendPacket(player, SM_Temple_Put_Brick.valueOf(null));
		}
		if (Integer.valueOf(brick.getId()) >= 4) {
			// 紫色通报
			I18nUtils i18nUtils = I18nUtils.valueOf("10409");
			i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
			i18nUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			i18nUtils.addParm("targetCountry", I18nPack.valueOf(brick.getFromTemple().getCountry().getName()));
			i18nUtils.addParm("color", I18nPack.valueOf(COUNTRY_TEMPLE_SPY_COLOR.getValue().get(brick.getId())));
			i18nUtils.addParm("brickcolor", I18nPack.valueOf(brick.getResource().getColorI18n()));
			i18nUtils.addParm("templenumber", I18nPack.valueOf(player.getCountry().getTemple().getBrick()));
			ChatManager.getInstance().sendSystem(11001, i18nUtils, null);
		}
	}

	@Static("EXPRESS:COE_CHOOSER")
	private ConfigValue<String> COE_CHOOSER;

	private Reward createExpressReward(Player player, String chooserGroupId, int color) {
		List<String> core = ChooserManager.getInstance().chooseValueByRequire(player, COE_CHOOSER.getValue());
		Map<String, Object> param = New.hashMap();
		param.put("LEVEL", player.getLevel());
		param.put("EXPTEMPLECOE", core.get(0));
		param.put("COLOR", COLOR_FACTOR.getValue()[color - 1]);
		param.put("WORLD_CLASS_BONUS", WorldRankManager.getInstance().getPlayerWorldLevel(player));
		param.put("HONORINCREASE", player.getGameStats().getCurrentStat(StatEnum.HONOR_PLUS) * 1.0 / 10000);
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player, chooserGroupId);
		Reward reward = RewardManager.getInstance().creatReward(player, rewardIds, param);
		return reward;
	}

	public static TempleManager getInstance() {
		return instance;
	}

	public Storage<String, BrickResource> getBrickResources() {
		return brickResources;
	}

	public void queryTempleStatus(Player player) {
		PacketSendUtility.sendPacket(player, SM_Query_Temple_Status.valueOf(currentBrickMapStatus));
	}

	private void publicNotice(Player player, Brick brick) {
		if (Integer.valueOf(brick.getId()) >= 4) {
			I18nUtils i18nUtils = I18nUtils.valueOf("10411");
			i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
			i18nUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			i18nUtils.addParm("targetCountry", I18nPack.valueOf(brick.getFromTemple().getCountry().getName()));
			i18nUtils.addParm("color", I18nPack.valueOf(COUNTRY_TEMPLE_SPY_COLOR.getValue().get(brick.getId())));
			i18nUtils.addParm("brickcolor", I18nPack.valueOf(brick.getResource().getColorI18n()));
			ChatManager.getInstance().sendSystem(11001, i18nUtils, null);
		}
	}
}
