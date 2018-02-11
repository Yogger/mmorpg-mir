package com.mmorpg.mir.model.world.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.boss.manager.BossManager;
import com.mmorpg.mir.model.boss.resource.BossResource;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chat.model.handle.ChannelPrivate;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.controllers.move.Road;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.DropObject;
import com.mmorpg.mir.model.gameobjects.Gatherable;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.StatusNpc;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.event.GatherEvent;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.object.ObjectManager;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.DirectionEnum;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.packet.SM_BackHome_Start;
import com.mmorpg.mir.model.world.packet.SM_ChangeChannel;
import com.mmorpg.mir.model.world.packet.SM_GatherStart;
import com.mmorpg.mir.model.world.packet.SM_PickUp;
import com.mmorpg.mir.model.world.packet.SM_StatusNpcEnd;
import com.mmorpg.mir.model.world.packet.SM_StatusNpcStart;
import com.mmorpg.mir.model.world.packet.SM_StatusNpc_Break;
import com.mmorpg.mir.model.world.resource.MapResource;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.JsonUtils;

@Component
public class WorldServiceImpl implements WorldService {

	@Static("PUBLIC:ENEMIES_NOTICE1")
	private ConfigValue<Map<String, ArrayList<Integer>>> ENEMIES_NOTICE1;

	@Static("PUBLIC:ENEMIES_NOTICE2")
	private ConfigValue<Map<String, ArrayList<Integer>>> ENEMIES_NOTICE2;

	@Static("PUBLIC:BACKHOME_ACTIONS")
	private ConfigValue<String[]> BACKHOME_ACTIONS;

	private CoreActions backHomeActions;

	public CoreActions getBackHomeActions() {
		if (backHomeActions == null) {
			backHomeActions = CoreActionManager.getInstance().getCoreActions(1, BACKHOME_ACTIONS.getValue());
		}
		return backHomeActions;
	}

	@Autowired
	private RewardManager rewardManager;

	public void enterWorld(Player player) {
		World.getInstance().spawn(player);

		ArrayList<Integer> notice1 = ENEMIES_NOTICE1.getValue().get(player.getCountryId().name());
		ArrayList<Integer> notice2 = ENEMIES_NOTICE2.getValue().get(player.getCountryId().name());
		int playerMapId = player.getMapId();
		if (notice1.contains(playerMapId)) {
			I18nUtils utils = I18nUtils.valueOf("801006");
			ChatManager.getInstance()
					.sendSystem(81007, utils, null, ChannelPrivate.SYSTEM_SENDER, player.getObjectId());
		}
		if (notice2.contains(playerMapId)) {
			I18nUtils utils = I18nUtils.valueOf("801007");
			ChatManager.getInstance()
					.sendSystem(81007, utils, null, ChannelPrivate.SYSTEM_SENDER, player.getObjectId());
		}
	}

	public void changeChannel(Player player, int channelId) {
		if (!player.getLifeStats().isAlreadyDead()) {
			if (player.getInstanceId() != channelId) {
				if (player.isInCopy()) {
					throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
				}
				if (World.getInstance().getWorldMap(player.getMapId()).getWorldMapInstanceById(channelId) != null) {
					World.getInstance().despawn(player);
					World.getInstance().setPosition(player, player.getMapId(), channelId, player.getX(), player.getY(),
							player.getHeading());
					player.sendUpdatePosition();
					PacketSendUtility.sendPacket(player, SM_ChangeChannel.valueOf(channelId));
				} else {
					throw new ManagedException(ManagedErrorCode.MAP_CHANNELID_NOTFOUND);
				}
			}
		}
	}

	public void move(Player player, int x, int y, byte[] roads) {
		if (player.isSpawned() && player.canPerformMove()) {
			byte[] realRoads = MathUtil.unzipRoads(roads);
			if (check(player.getPosition().getMapRegion().getParent(), player.getPosition().getMapId(), x, y, realRoads)) {
				player.updateCasting(null);
				player.getMoveController().setNewRoads(x, y, Road.valueOf(realRoads));
				player.getController().onStartMove();
			} else {
				player.sendUpdatePosition();
			}
		}
	}

	private boolean check(WorldMapInstance mapInstance, int mapId, int x, int y, byte[] roads) {
		WorldMap worldMap = World.getInstance().getWorldMap(mapId);
		int dx = x;
		int dy = y;
		if (worldMap.isOut(x, y) || worldMap.isBlock(x, y) || mapInstance.isBlock(x, y)) {
			return false;
		}
		if (!ArrayUtils.isEmpty(roads)) {
			for (byte r : roads) {
				DirectionEnum direction = DirectionEnum.values()[r];
				dx += direction.getAddX();
				dy += direction.getAddY();
				if (worldMap.isOut(dx, dy) || worldMap.isBlock(dx, dy) || mapInstance.isBlock(dx, dy)) {
					return false;
				}
			}
		}
		return true;
	}

	public void pickUp(Player player, long objId) {
		DropObject dObj = findObject(player, objId, 10, DropObject.class);

		if (dObj.getRewardItem().getRewardType() == RewardType.ITEM) {
			if (player.getPack().isFull()) {
				throw new ManagedException(ManagedErrorCode.PACK_FULL);
			}
		}

		if (!dObj.inOwnership(player.getObjectId())) {
			throw new ManagedException(ManagedErrorCode.DROP_NOT_OWNERSHIP);
		}

		if (dObj.pickUp()) {
			Reward reward = Reward.valueOf().addRewardItem(dObj.getRewardItem());
			rewardManager.grantReward(player, reward, ModuleInfo.valueOf(ModuleType.PICKUP, SubModuleType.PICKUP_DROP));
			SM_PickUp sm = new SM_PickUp();
			sm.setObjId(dObj.getObjectId());
			PacketSendUtility.sendPacket(player, sm);
			World.getInstance().despawn(dObj);
			if (dObj.getRewardItem().getRewardType() == RewardType.ITEM) {
				ItemResource itemResource = ItemManager.getInstance().getResource(dObj.getRewardItem().getCode());
				if (itemResource.getQuality() >= 3) {
					BossResource resource = BossManager.getInstance().getBossResource(dObj.getFromNpcSpawnKey(), false);
					if (resource != null && !resource.isElite()) {
						SpawnGroupResource bossSpawnRes = SpawnManager.getInstance()
								.getSpawn(dObj.getFromNpcSpawnKey());
						ObjectResource boss = ObjectManager.getInstance()
								.getObjectResource(bossSpawnRes.getObjectKey());
						I18nUtils i18nUtils = I18nUtils.valueOf("40113");
						i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
						i18nUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
						i18nUtils.addParm("BOSS", I18nPack.valueOf(boss.getName()));
						i18nUtils.addParm("item", I18nPack.valueOf(itemResource.getName()));
						i18nUtils.addParm(
								"color",
								I18nPack.valueOf(ItemManager.getInstance().ITEM_QUALITY_COLOR.getValue().get(
										itemResource.getQuality() + "")));
						ChatManager.getInstance().sendSystem(11001, i18nUtils, null);

						i18nUtils = I18nUtils.valueOf("306013");
						i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
						i18nUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
						i18nUtils.addParm("BOSS", I18nPack.valueOf(boss.getName()));
						i18nUtils.addParm("item", I18nPack.valueOf(itemResource.getName()));
						i18nUtils.addParm(
								"color",
								I18nPack.valueOf(ItemManager.getInstance().ITEM_QUALITY_COLOR.getValue().get(
										itemResource.getQuality() + "")));
						ChatManager.getInstance().sendSystem(0, i18nUtils, null);

					}
				}

			}
			dObj.cancelAllTask();
		} else {
			throw new ManagedException(ManagedErrorCode.OBJECT_NOT_FOUND);
		}

	}

	public SM_GatherStart gatherStart(final Player player, final long objId) {
		if (player.getLifeStats().isAlreadyDead() || (!player.isSpawned())) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}
		player.getMoveController().stopMoving();
		Gatherable gatherable = findObject(player, objId, 5, Gatherable.class);
		long endTime = System.currentTimeMillis() + gatherable.getInterval();
		player.setGatherCoolDown(objId, endTime);
		SM_GatherStart start = new SM_GatherStart();
		start.setEndTime(endTime);
		player.unRide();

		player.getObserveController().attach(
				new ActionObserver(ObserverType.MOVE, ObserverType.ATTACK, ObserverType.SKILLUSE, ObserverType.DIE,
						ObserverType.SPAWN, ObserverType.TRANSPORT) {

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

					@Override
					public void spawn(int mapId, int instanceId) {
						breakGather();
					}

					@Override
					public void transport() {
						breakGather();
					}

					private void breakGather() {
						if (player.getEffectController().isAbnoramlSet(EffectId.GATHER)) {
							PacketSendUtility.sendPacket(player, SM_StatusNpc_Break.valueOf(objId));
							player.getEffectController().unsetAbnormal(EffectId.GATHER, true);
							player.removeGatherCoolDown();
						}
					}

				});

		player.getEffectController().setAbnormal(EffectId.GATHER, true);
		return start;
	}

	public void gatherEnd(Player player, long objId) {
		if (player.isGatherDisable(objId))
			throw new ManagedException(ManagedErrorCode.DROP_NOT_OWNERSHIP);

		player.getEffectController().unsetAbnormal(EffectId.GATHER, true);
		player.removeGatherCoolDown();
		Gatherable gatherable = findObject(player, objId, 5, Gatherable.class);

		if (gatherable.getRewardId() != null) {
			rewardManager.grantReward(player, gatherable.getRewardId(),
					ModuleInfo.valueOf(ModuleType.GATHER, SubModuleType.GATHER_OBJ, gatherable.getObjectKey()));
		}
		EventBusManager.getInstance().submit(GatherEvent.valueOf(player.getObjectId(), gatherable.getObjectKey()));

	}

	public SM_BackHome_Start backHomeStart(final Player player) {
		if (player.getLifeStats().isAlreadyDead() || (!player.isSpawned())) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}
		player.getMoveController().stopMoving();
		SM_BackHome_Start start = new SM_BackHome_Start();
		if (player.getEffectController().isAbnoramlSet(EffectId.STUN)) {
			start.setCode(ManagedErrorCode.PLAYER_STUN);
			return start;
		}

		if (player.getEffectController().isAbnoramlSet(EffectId.SILENCE)) {
			start.setCode(ManagedErrorCode.PLAYER_SILENCE);
			return start;
		}

		MapResource resource = World.getInstance().getMapResource(player.getMapId());
		long interval = resource.getBackInterval()[player.getCountryValue() - 1];
		long endTime = System.currentTimeMillis() + interval;
		player.setBackHomeSing(endTime);
		start.setEndTime(endTime);
		player.unRide();

		player.getObserveController().attach(
				new ActionObserver(ObserverType.MOVE, ObserverType.DIE, ObserverType.SPAWN, ObserverType.TRANSPORT) {

					@Override
					public void moved() {
						breakBackhome();
					}

					@Override
					public void die(Creature creature) {
						breakBackhome();
					}

					@Override
					public void spawn(int mapId, int instanceId) {
						breakBackhome();
					}

					@Override
					public void transport() {
						breakBackhome();
					}

					private void breakBackhome() {
						player.removeBackHome();
					}

				});

		player.getEffectController().setAbnormal(EffectId.BACKHOME, true);
		player.breakGather();
		player.removeTempleBrickCoolDown();
		return start;
	}

	public void backHomeEnd(final Player player, boolean useItem) {
		if (player.isBackHomeDisable())
			throw new ManagedException(ManagedErrorCode.ITEM_IN_COOLDOWN);

		if (useItem) {
			CoreActions actions = getBackHomeActions();
			if (!actions.verify(player, true)) {
				PacketSendUtility.sendErrorMessage(player);
				return;
			}
			actions.act(player, ModuleInfo.valueOf(ModuleType.BACKHOME, SubModuleType.BACK_HOME_ACT));
		}

		List<String> result = ChooserManager.getInstance().chooseValueByRequire(player.getCountryId().getValue(),
				PlayerManager.getInstance().BACKHOME_POINT.getValue());
		RelivePosition p = JsonUtils.string2Object(result.get(0), RelivePosition.class);
		if (player.getPosition().getMapId() == p.getMapId()) {
			World.getInstance().updatePosition(player, p.getX(), p.getY(), player.getHeading());
		} else {
			World.getInstance().setPosition(player, p.getMapId(), p.getX(), p.getY(), player.getHeading());
		}
		player.sendUpdatePosition();

		player.getEffectController().unsetAbnormal(EffectId.BACKHOME, true);
		player.removeBackHome();
	}

	public SM_StatusNpcStart statusNpcStart(final Player player, final long objId) {
		if (player.getLifeStats().isAlreadyDead() || (!player.isSpawned())) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}
		player.getMoveController().stopMoving();
		final StatusNpc statusNpc = findObject(player, objId, 5, StatusNpc.class);
		long endTime = System.currentTimeMillis() + statusNpc.getSpawn().getStatusDuration();
		statusNpc.addDuration(player.getObjectId(), endTime);
		player.getObserveController().attach(
				new ActionObserver(ObserverType.MOVE, ObserverType.ATTACKED, ObserverType.ATTACK,
						ObserverType.SKILLUSE, ObserverType.DIE, ObserverType.SPAWN, ObserverType.TRANSPORT) {

					@Override
					public void moved() {
						breakStausNpc();
					}

					@Override
					public void attacked(Creature creature) {
						breakStausNpc();
					}

					@Override
					public void attack(Creature creature) {
						breakStausNpc();
					}

					@Override
					public void skilluse(Skill skill) {
						breakStausNpc();
					}

					@Override
					public void die(Creature creature) {
						breakStausNpc();
					}

					@Override
					public void spawn(int mapId, int instanceId) {
						breakStausNpc();
					}

					@Override
					public void transport() {
						breakStausNpc();
					}

					private void breakStausNpc() {
						if (statusNpc.removeDuration(player.getObjectId())) {
							PacketSendUtility.sendPacket(player, SM_StatusNpc_Break.valueOf(objId));
							player.getEffectController().unsetAbnormal(EffectId.GATHER, true);
						}
					}

				});
		// 如果必要的话，这里可以注册两个观察者，做最严格的检查
		SM_StatusNpcStart start = new SM_StatusNpcStart();
		start.setEndTime(endTime);
		player.getEffectController().setAbnormal(EffectId.GATHER, true);
		return start;
	}

	public SM_StatusNpcEnd statusNpcEnd(Player player, long objId) {
		player.getEffectController().unsetAbnormal(EffectId.GATHER, true);
		StatusNpc statusNpc = findObject(player, objId, 5, StatusNpc.class);
		statusNpc.getController().playerChangeStatus(player);
		SM_StatusNpcEnd end = new SM_StatusNpcEnd();
		return end;
	}

	@SuppressWarnings("unchecked")
	private <T extends VisibleObject> T findObject(Player player, long objId, int halfRange, Class<T> clz) {
		if (player.getLifeStats().isAlreadyDead() || (!player.isSpawned())) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}
		VisibleObject obj = World.getInstance().findObject(player.getMapId(), player.getInstanceId(), objId);
		if (obj != null) {
			if (!obj.isSpawned()) {
				throw new ManagedException(ManagedErrorCode.OBJECT_NOT_FOUND);
			}
			if (clz.isAssignableFrom(obj.getClass())) {
				if (!MathUtil.isInRange(player, obj, halfRange, halfRange)) {
					throw new ManagedException(ManagedErrorCode.OBJECT_TOO_LONG);
				}
			} else {
				throw new ManagedException(ManagedErrorCode.ERROR_MSG);
			}
		} else {
			throw new ManagedException(ManagedErrorCode.OBJECT_NOT_FOUND);
		}
		return (T) obj;
	}

}
