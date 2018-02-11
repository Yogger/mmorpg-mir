package com.mmorpg.mir.model.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.slf4j.helpers.MessageFormatter;

import com.mmorpg.mir.Debug;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.packet.SM_PlayerGameStats;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.system.packet.SM_System_Message;
import com.mmorpg.mir.model.system.packet.SM_System_Sign;
import com.windforce.common.utility.JsonUtils;

public class PacketSendUtility {
	private static final Logger logger = Logger.getLogger(PacketSendUtility.class);

	public static void sendPacket(Creature creature, Object packet) {
		if (creature.isObjectType(ObjectType.PLAYER)) {
			sendPacket((Player) creature, packet);
		}
	}

	public static void sendPacket(Player player, Object packet) {
		if (player.getSession() != null) {
			if (Debug.debug) {
				try {
					// 过虑
					Class<?>[] clazzs = new Class[] { SM_PlayerGameStats.class };
					List<Class<?>> clazzList = Arrays.asList(clazzs);
					if (packet.getClass().getName().contains("transfer")) {
						String message = MessageFormatter.format(
								"class[{}] session[{}] 推送信息给[{}],信息内容[{}]]",
								new Object[] { packet.getClass(), player.getSession().getId(), player.getName(),
										isEmptyClass(packet) ? "{}" : JsonUtils.object2String(packet) }).getMessage();
						logger.error(message);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			player.getSession().sendPacket(packet);
		}
	}

	private static boolean isEmptyClass(Object object) {
		for (Method m : object.getClass().getMethods()) {
			if ((m.getName().startsWith("get") || m.getName().startsWith("is")) && !m.getName().startsWith("getClass")
					&& !m.getName().startsWith("getInstance")) {
				return false;
			}
		}
		return true;
	}

	public static void broadcastPacket(Player player, Object packet, boolean toSelf) {
		if (toSelf)
			sendPacket(player, packet);

		broadcastPacket(player, packet);
	}

	public static void broadcastPacket(VisibleObject visibleObject, Object packet) {
		for (VisibleObject obj : visibleObject.getKnownList()) {
			if (obj instanceof Player)
				sendPacket(((Player) obj), packet);
		}
	}

	public static void broadcastPacketAndReceiver(VisibleObject visibleObject, Object packet) {
		if (visibleObject instanceof Player)
			sendPacket((Player) visibleObject, packet);
		broadcastPacket(visibleObject, packet);
	}

	public static void broadcastMessageAndReceiver(Player sender, Object packet) {
		sendPacket(sender, packet);
		for (VisibleObject obj : sender.getKnownList()) {
			if (obj instanceof Player) {
				if (ChatManager.getInstance().isInBlackList(sender.getObjectId(), obj.getObjectId()))
					continue;
				sendPacket(((Player) obj), packet);
			}
		}
	}

	public static void sendErrorMessage(Player player) {
		sendPacket(player, SM_System_Message.valueOf(ManagedErrorCode.ERROR_MSG));
	}

	public static void sendSignMessage(Player player, int sign) {
		sendPacket(player, SM_System_Sign.valueOf(sign));
	}

	public static void sendErrorMessage(Player player, int code) {
		sendPacket(player, SM_System_Message.valueOf(code));
	}

}
