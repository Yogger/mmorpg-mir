package com.mmorpg.mir.model.world.facade;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.player.model.PlayerChatSimple;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;
import com.mmorpg.mir.model.player.packet.CM_PlayerSimple_Map;
import com.mmorpg.mir.model.player.packet.SM_PlayerSimple_Map;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.packet.CM_BackHome_End;
import com.mmorpg.mir.model.world.packet.CM_BackHome_Start;
import com.mmorpg.mir.model.world.packet.CM_CallBigBrother;
import com.mmorpg.mir.model.world.packet.CM_ChangeChannel;
import com.mmorpg.mir.model.world.packet.CM_EnterWorld;
import com.mmorpg.mir.model.world.packet.CM_GatherEnd;
import com.mmorpg.mir.model.world.packet.CM_GatherStart;
import com.mmorpg.mir.model.world.packet.CM_Get_MapChannel;
import com.mmorpg.mir.model.world.packet.CM_Move;
import com.mmorpg.mir.model.world.packet.CM_Pet_PickUp;
import com.mmorpg.mir.model.world.packet.CM_PickUp;
import com.mmorpg.mir.model.world.packet.CM_PlayerChatSimple;
import com.mmorpg.mir.model.world.packet.CM_PlayerSimple_All;
import com.mmorpg.mir.model.world.packet.CM_Query_Map_Players;
import com.mmorpg.mir.model.world.packet.CM_StatusNpcEnd;
import com.mmorpg.mir.model.world.packet.CM_StatusNpcStart;
import com.mmorpg.mir.model.world.packet.SM_BackHome_End;
import com.mmorpg.mir.model.world.packet.SM_BackHome_Start;
import com.mmorpg.mir.model.world.packet.SM_GatherEnd;
import com.mmorpg.mir.model.world.packet.SM_GatherStart;
import com.mmorpg.mir.model.world.packet.SM_Get_MapChannel;
import com.mmorpg.mir.model.world.packet.SM_Pet_PickUp;
import com.mmorpg.mir.model.world.packet.SM_PickUp;
import com.mmorpg.mir.model.world.packet.SM_PlayerChatSimple;
import com.mmorpg.mir.model.world.packet.SM_PlayerSimple_All;
import com.mmorpg.mir.model.world.packet.SM_Query_Map_Players;
import com.mmorpg.mir.model.world.packet.SM_StatusNpcEnd;
import com.mmorpg.mir.model.world.packet.SM_StatusNpcStart;
import com.mmorpg.mir.model.world.service.WorldService;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public class WorldFacade {

	private static Logger logger = Logger.getLogger(WorldFacade.class);

	@Autowired
	private WorldService worldService;

	@HandlerAnno
	public void enterWorld(TSession session, CM_EnterWorld req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			if (player != null) {
				worldService.enterWorld(player);
			}
		} catch (ManagedException e) {
			// 这里应该返回一个通用错误信息
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("玩家进入地图异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void callBigBrother(TSession session, CM_CallBigBrother req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			if (player != null) {
				worldService.callBigBrother(player);
			}
		} catch (ManagedException e) {
			// 这里应该返回一个通用错误信息
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("玩家进入地图异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void changeChannel(TSession session, CM_ChangeChannel req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			if (player != null) {
				worldService.changeChannel(player, req.getChannelId());
			}
		} catch (ManagedException e) {
			// 这里应该返回一个通用错误信息
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("玩家切换地图线路异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void getMapChannel(TSession session, CM_Get_MapChannel req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			if (player.getPosition() != null && player.isSpawned()) {
				WorldMap worldMap = World.getInstance().getWorldMap(player.getMapId());
				if (worldMap.isCopy()) {
					return;
				}
				PacketSendUtility.sendPacket(player,
						SM_Get_MapChannel.valueOf(worldMap.getMaxNotEmptyMapOrInitInstanceId()));
			}
		} catch (ManagedException e) {
			// 这里应该返回一个通用错误信息
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("玩家获取地图线路异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void move(TSession session, CM_Move req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			if (player != null) {
				worldService.move(player, req.getX(), req.getY(), req.getRoads());
			}
		} catch (ManagedException e) {
			// 这里应该返回一个通用错误信息
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("玩家移动异常", e);
			PacketSendUtility.sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.SYS_ERROR));
		}
	}

	@HandlerAnno
	public void pickUp(TSession session, CM_PickUp req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			if (player != null) {
				worldService.pickUp(player, req.getObjId());
			}
		} catch (ManagedException e) {
			SM_PickUp sm = new SM_PickUp();
			sm.setCode(e.getCode());
			sm.setObjId(req.getObjId());
			PacketSendUtility.sendPacket(player, sm);
		} catch (Exception e) {
			logger.error("玩家拾取物品异常", e);
			SM_PickUp sm = new SM_PickUp();
			sm.setCode(ManagedErrorCode.SYS_ERROR);
			sm.setObjId(req.getObjId());
			PacketSendUtility.sendPacket(player, sm);
		}
	}
	
	@HandlerAnno
	public void petPickUp(TSession session, CM_Pet_PickUp req) {
		Player player = SessionUtil.getPlayerBySession(session);
		SM_Pet_PickUp sm = new SM_Pet_PickUp();
		try {
			if (player != null) {
				worldService.petPickUp(player, req.getObjIds());
			}
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
			PacketSendUtility.sendPacket(player, sm);
		} catch (Exception e) {
			logger.error("玩家拾取物品异常", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}

	@HandlerAnno
	public SM_GatherStart gatherStart(TSession session, CM_GatherStart req) {
		SM_GatherStart sm = new SM_GatherStart();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			if (player != null) {
				sm = worldService.gatherStart(player, req.getObjId());
			}
			return sm;
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家开始采集异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_GatherEnd gatherEnd(TSession session, CM_GatherEnd req) {
		SM_GatherEnd sm = new SM_GatherEnd();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			if (player != null) {
				worldService.gatherEnd(player, req.getObjId());
			}
			return sm;
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家结束采集异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_BackHome_Start backHomeStart(TSession session, CM_BackHome_Start req) {
		SM_BackHome_Start sm = new SM_BackHome_Start();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			if (player != null) {
				sm = worldService.backHomeStart(player);
			}
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家开始回城错误", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_BackHome_End backHomeEnd(TSession session, CM_BackHome_End req) {
		SM_BackHome_End sm = new SM_BackHome_End();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			if (player != null) {
				worldService.backHomeEnd(player, req.isUseItem());
			}
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家结束采集异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_StatusNpcStart statusNpcStart(TSession session, CM_StatusNpcStart req) {
		SM_StatusNpcStart sm = new SM_StatusNpcStart();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			if (player != null) {
				sm = worldService.statusNpcStart(player, req.getObjId());
			}
			return sm;
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家触碰状态NPC异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public SM_StatusNpcEnd statusNpcEnd(TSession session, CM_StatusNpcEnd req) {
		SM_StatusNpcEnd sm = new SM_StatusNpcEnd();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			if (player != null) {
				sm = worldService.statusNpcEnd(player, req.getObjId());
			}
			return sm;
		} catch (ManagedException e) {
			sm.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家收获状态NPC异常", e);
			sm.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return sm;
	}

	@HandlerAnno
	public void getPlayerInMap(TSession session, CM_PlayerSimple_Map req) {
		Player player = SessionUtil.getPlayerBySession(session);
		if (!player.isSpawned()) {
			return;
		}
		Iterator<Player> players = World.getInstance().getWorldMap(player.getMapId())
				.getWorldMapInstanceById(player.getInstanceId()).playerIterator();
		ArrayList<PlayerSimpleInfo> infos = new ArrayList<PlayerSimpleInfo>();
		while (players.hasNext()) {
			Player p = players.next();
			// 过虑自己和组队的
			if (p.getObjectId() == player.getObjectId() || p.isInGroup() || p.getCountry() != player.getCountry()) {
				continue;
			}
			infos.add(p.createSimple());
		}
		SM_PlayerSimple_Map sm = new SM_PlayerSimple_Map();
		sm.setInfos(infos);
		PacketSendUtility.sendPacket(player, sm);
	}

	@HandlerAnno
	public void getPlayerInMap(TSession session, CM_PlayerSimple_All req) {
		Player player = SessionUtil.getPlayerBySession(session);
		if (!player.isSpawned()) {
			return;
		}
		ArrayList<PlayerSimpleInfo> infos = new ArrayList<PlayerSimpleInfo>();
		for (Player p : player.getCountry().getCivils().values()) {
			if ((!p.isInGang()) && ModuleOpenManager.getInstance().isOpenByKey(p, "opmk5") && (!p.equals(player))) {
				infos.add(p.createSimple());
				if (infos.size() >= 30) { // case too much result
					break;
				}
			}
		}
		PacketSendUtility.sendPacket(player, SM_PlayerSimple_All.valueOf(infos));
	}

	@HandlerAnno
	public void getPlayerInMap(TSession session, CM_PlayerChatSimple req) {
		Player player = SessionUtil.getPlayerBySession(session);
		if (!player.isSpawned()) {
			return;
		}
		Iterator<Player> players = World.getInstance().getWorldMap(player.getMapId())
				.getWorldMapInstanceById(player.getInstanceId()).playerIterator();
		ArrayList<PlayerChatSimple> infos = new ArrayList<PlayerChatSimple>();
		while (players.hasNext()) {
			Player p = players.next();
			// 过虑自己不是同国
			if (p.getObjectId() == player.getObjectId() || p.getCountry() != player.getCountry()) {
				continue;
			}
			infos.add(PlayerChatSimple.valueOf(p));
		}
		SM_PlayerChatSimple sm = new SM_PlayerChatSimple();
		sm.setInfos(infos);
		PacketSendUtility.sendPacket(player, sm);
	}

	@HandlerAnno
	public void getPlayersNumbers(TSession session, CM_Query_Map_Players req) {
		Player requester = SessionUtil.getPlayerBySession(session);
		WorldMap map = World.getInstance().getWorldMap(req.getMapId());
		if (map == null) {
			PacketSendUtility.sendErrorMessage(requester, ManagedErrorCode.ERROR_MSG);
			return;
		}
		Integer[] infos = new Integer[CountryId.values().length];
		for (CountryId id : CountryId.values()) {
			infos[id.getValue() - 1] = 0;
		}

		Iterator<WorldMapInstance> mapInstances = map.iterator();
		while (mapInstances.hasNext()) {
			WorldMapInstance instance = mapInstances.next();
			Iterator<Player> players = instance.playerIterator();
			while (players.hasNext()) {
				Player player = players.next();
				infos[player.getCountryValue() - 1]++;
			}
		}
		PacketSendUtility.sendPacket(requester, SM_Query_Map_Players.valueOf(infos));
	}
}
