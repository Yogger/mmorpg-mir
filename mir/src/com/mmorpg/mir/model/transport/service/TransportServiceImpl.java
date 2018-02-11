package com.mmorpg.mir.model.transport.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gascopy.config.GasCopyMapConfig;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.transport.manager.PlayerChatTransportManager;
import com.mmorpg.mir.model.transport.model.PlayerChatTransport;
import com.mmorpg.mir.model.transport.packet.SM_PlayerTransport;
import com.mmorpg.mir.model.transport.packet.SM_Transport;
import com.mmorpg.mir.model.transport.resource.PlayerTransportResource;
import com.mmorpg.mir.model.transport.resource.TransportResource;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.world.Position;
import com.mmorpg.mir.model.world.World;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class TransportServiceImpl implements TransportService {

	@Static
	private Storage<Integer, TransportResource> traStorage;
	@Static
	private Storage<Integer, PlayerTransportResource> ptraStorage;
	@Autowired
	private CoreConditionManager conditionManager;
	@Autowired
	private CoreActionManager actionManager;
	@Autowired
	private PlayerChatTransportManager playerChatTransportManager;
	@Autowired
	private ChooserManager chooserManager;
	/** 聊天框中动态飞鞋的消耗 */
	@Static("TRANSPORT:CHATTRANSPORT")
	public ConfigValue<String[]> CHATTRANSPORT;

	public SM_Transport transport(int id, Player player) {
		TransportResource tr = traStorage.get(id, true);
		List<String> targetPos = chooserManager.chooseValueByRequire(player, tr.getChooserGroupId());
		Position pos = tr.getDestinationPos().get(targetPos.get(0));

		World.getInstance().canEnterMap(player, tr.getTargetMapId());
		if (player.getLifeStats().isAlreadyDead()) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}

		if (player.isInCopy() && player.getMapId() != tr.getTargetMapId()) {
			throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
		}

		if (!tr.getConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		player.getMoveController().stopMoving();
		if (player.getMapId() == tr.getBelongMapId()
				&& MathUtil.isInRange(tr.getBelongPosX(), tr.getBelongPosY(), player, tr.getRadius() * 2,
						tr.getRadius() * 2)) {
			if (player.getMapId() == tr.getTargetMapId()) {
				World.getInstance().updatePosition(player, pos.getX(), pos.getY(), player.getHeading());
			} else {
				World.getInstance().setPosition(player, tr.getTargetMapId(), pos.getX(), pos.getY(),
						player.getHeading());
			}
			player.sendUpdatePosition();
			player.getObserveController().notifyTransportObservers();
			SM_Transport sm = new SM_Transport();
			sm.setId(id);
			return sm;
		} else {
			throw new ManagedException(ManagedErrorCode.NOT_RIGHT_POSITION);
		}
	}

	public SM_PlayerTransport playerTransport(int id, Player player) {
		PlayerTransportResource tr = ptraStorage.get(id, true);
		World.getInstance().canEnterMap(player, tr.getTargetMapId());
		if (player.isInCopy() || GasCopyMapConfig.getInstance().isInGasCopyMap(player.getMapId())) {
			throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
		}
		if (!conditionManager.getCoreConditions(1, tr.getConditionId()).verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		if (!(tr.isFreeForVip() && player.getVip().getResource().isFreeFly())) { // /
			CoreActions actions = actionManager.getCoreActions(1, tr.getActionId());
			actions.verify(player, true);
			ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.TRANSPORT, SubModuleType.FLY_SHOE_ACT);
			actions.act(player, moduleInfo);
		}
		player.getMoveController().stopMoving();
		Position destination = tr.selectPosition();
		if (player.getMapId() == tr.getTargetMapId()) {
			if (tr.getInstance() != 0
					&& player.getInstanceId() != tr.getInstance()
					&& World.getInstance().getWorldMap(tr.getTargetMapId()).getWorldMapInstanceById(tr.getInstance()) != null) {
				World.getInstance().setPosition(player, tr.getTargetMapId(), tr.getInstance(), destination.getX(),
						destination.getY(), player.getHeading());
			} else {
				World.getInstance().updatePosition(player, destination.getX(), destination.getY(), player.getHeading());
			}
		} else {
			if (tr.getInstance() != 0
					&& World.getInstance().getWorldMap(tr.getTargetMapId()).getWorldMapInstanceById(tr.getInstance()) != null) {
				World.getInstance().setPosition(player, tr.getTargetMapId(), tr.getInstance(), destination.getX(),
						destination.getY(), player.getHeading());
			} else {
				World.getInstance().setPosition(player, tr.getTargetMapId(), destination.getX(), destination.getY(),
						player.getHeading());
			}
		}
		player.sendUpdatePosition();
		player.getObserveController().notifyTransportObservers();
		SM_PlayerTransport sm = new SM_PlayerTransport();
		sm.setId(id);
		return sm;
	}

	public void playerChatTransport(int id, Player player) {

		if (player.getLifeStats().isAlreadyDead()) {
			throw new ManagedException(ManagedErrorCode.DEAD_ERROR);
		}
		if (player.isInCopy() || GasCopyMapConfig.getInstance().isInGasCopyMap(player.getMapId())) {
			throw new ManagedException(ManagedErrorCode.PLAYER_IN_COPY);
		}
		PlayerChatTransport chatTransport = playerChatTransportManager.getChatTransport(id);
		if (chatTransport == null
				|| !World.getInstance().getWorldMap(chatTransport.getMapId()).getInstances()
						.containsKey(chatTransport.getInstanceId())) {
			throw new ManagedException(ManagedErrorCode.CHAT_TRANSPORT_DEPRECATED);
		}
		World.getInstance().canEnterMap(player, chatTransport.getMapId());
		if (chatTransport.getConditions() != null && !chatTransport.getConditions().verify(player, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		if (!(player.getVip().getResource().isFreeFly())) { // /
			CoreActions actions = actionManager.getCoreActions(1, CHATTRANSPORT.getValue());
			actions.verify(player, true);
			ModuleInfo moduleInfo = ModuleInfo.valueOf(ModuleType.TRANSPORT, SubModuleType.CHAT_FLY_SHOE_ACT);
			actions.act(player, moduleInfo);
		}

		player.getMoveController().stopMoving();
		if (player.getMapId() == chatTransport.getMapId() && player.getInstanceId() == chatTransport.getInstanceId()) {
			World.getInstance().updatePosition(player, chatTransport.getX(), chatTransport.getY(), player.getHeading());
		} else {
			World.getInstance().setPosition(player, chatTransport.getMapId(), chatTransport.getInstanceId(),
					chatTransport.getX(), chatTransport.getY(), player.getHeading());
		}
		player.sendUpdatePosition();
		player.getObserveController().notifyTransportObservers();
	}
}
