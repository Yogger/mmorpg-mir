package com.mmorpg.mir.model.chat.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.chat.model.ChannelType;
import com.mmorpg.mir.model.chat.model.Message;
import com.mmorpg.mir.model.chat.model.Sender;
import com.mmorpg.mir.model.chat.model.ShowType;
import com.mmorpg.mir.model.chat.model.handle.ChannelHandle;
import com.mmorpg.mir.model.chat.model.show.ShowHandle;
import com.mmorpg.mir.model.chat.packet.CM_Chat_Request;
import com.mmorpg.mir.model.chat.packet.SM_Big_Face;
import com.mmorpg.mir.model.chat.resource.ChannelResource;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.contact.manager.ContactManager;
import com.mmorpg.mir.model.contact.model.ContactRelationData;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.dirtywords.model.WordsType;
import com.mmorpg.mir.model.dirtywords.service.DirtyWordsManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.operator.manager.OperatorManager;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.player.model.PlayerChatSimple;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.packet.SM_PlayerChatSimple;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.JsonUtils;

@Component
public class ChatManager implements IChatManager {

	public static int CHANNELID_SYSTEM = 0;

	private static Logger logger = Logger.getLogger(ChatManager.class);
	private Map<ChannelType, ChannelHandle> handles = New.hashMap();
	private Map<ShowType, ShowHandle> showHandles = New.hashMap();

	@Static
	private Storage<Integer, ChannelResource> resources;

	@Autowired
	private PlayerManager playerManager;

	@Autowired
	private CoreConditionManager conditionManager;
	@Autowired
	private CoreActionManager actionManager;
	@Autowired
	private ContactManager contactManager;
	@Autowired
	private OperatorManager operatorManager;

	private static ChatManager instance;

	@Static("CHAT:CHAT_BYTE_LIMIT_NUMBER")
	private ConfigValue<Integer> CHAT_BYTE_LIMIT_NUMBER;

	@PostConstruct
	public void init() {
		setInstance(this);
	}

	public boolean isInBlackList(Long senderId, Long reciverId) {
		ContactRelationData data = contactManager.getMyContactRelationData(senderId);
		return data.getShield().contains(reciverId);
	}

	public boolean isInBlackList(Player sender, Player reciver) {
		return isInBlackList(sender.getObjectId(), reciver.getObjectId());
	}

	public void registerHandle(ChannelHandle handle) {
		if (handles.containsKey(handle.getType())) {
			logger.error("ChannelHandle type[" + handle.getType() + "]重复。");
			throw new IllegalArgumentException("ChannelHandle type[" + handle.getType() + "]重复。");
		}
		handles.put(handle.getType(), handle);
	}

	public void registerShowHandle(ShowHandle handle) {
		if (showHandles.containsKey(handle.getType())) {
			logger.error("ShowHandle type[" + handle.getType() + "]重复。");
			throw new IllegalArgumentException("ShowHandle type[" + handle.getType() + "]重复。");
		}
		showHandles.put(handle.getType(), handle);
	}

	public Storage<Integer, ChannelResource> getResources() {
		return resources;
	}

	private Message createMessage(Player player, CM_Chat_Request request) {
		Message message = new Message();
		message.setChannel(request.getChannelId());
		message.setContent(request.getContent());
		message.setI18n(request.getI18n());
		Sender sender = new Sender();
		sender.setId(player.getObjectId());
		sender.setName(player.getName());
		sender.setVipLevel(player.getVip().getLevel());
		if (player.getCountry().getCourt().getPlayerOfficial(player) != null) {
			sender.setOfficial(player.getCountry().getCourt().getPlayerOfficial(player).name());
		}
		if (KingOfWarManager.getInstance().isKingOfKing(player.getObjectId())) {
			sender.setKingOfking((byte) 1);
		}
		if (player.getEffectController().isAbnoramlSet(EffectId.GM_NICKNAME)) {
			sender.setGm((byte) 1);
		}
		sender.setServer(player.getPlayerEnt().getServer());
		sender.setCountry((byte) player.getCountryValue());
		message.setSender(sender);
		if (request.getObjects() != null) {
			message.setI18nParms(new ConcurrentHashMap<String, I18nPack>());
			for (Entry<String, HashMap<Integer, HashMap<String, String>>> entry : request.getObjects().entrySet()) {
				for (Entry<Integer, HashMap<String, String>> entryRequestShow : entry.getValue().entrySet()) {
					Object showObject = showHandles.get(ShowType.valueOf(Integer.valueOf(entryRequestShow.getKey())))
							.createShowObject(player, entryRequestShow.getValue());
					message.getI18nParms().put(entry.getKey(), I18nPack.valueOf(showObject));
				}
			}
		}
		message.setReciver(request.getReciverId());
		return message;
	}

	/**
	 * 系统广播
	 * 
	 * @param content
	 */
	public void sendSystem(String content) {
		sendSystem(CHANNELID_SYSTEM, content, null, null, null);
	}

	/**
	 * 发送系统消息
	 * 
	 * @param content
	 *            内容
	 * @param i18n
	 *            i18n id可不填
	 * @param parms
	 *            可不填
	 * @param shows
	 *            展示物品
	 */
	public void sendSystem(int channalId, String content, String i18n, HashMap<String, I18nPack> parms,
			HashMap<String, Object> shows, Object... args) {
		Message message = new Message();
		message.setChannel(channalId);
		message.setContent(content);
		message.setI18n(i18n);
		if (parms != null) {
			ConcurrentHashMap<String, I18nPack> nbmap = new ConcurrentHashMap<String, I18nPack>();
			for (Entry<String, I18nPack> entry : parms.entrySet()) {
				if (entry.getKey() == null || entry.getValue() == null) {
					throw new RuntimeException(String.format("广播信息有空参数key[%s],value[%s]", entry.getKey(),
							entry.getValue()));
				}
				nbmap.put(entry.getKey(), entry.getValue());
			}
			message.setI18nParms(nbmap);
		}
		ChannelResource resource = resources.get(channalId, true);
		handles.get(resource.getScope()).send(resource, message, args);
	}

	public void sendSystem(int channelId, I18nUtils i18nUtils, HashMap<String, Object> shows, Object... args) {
		this.sendSystem(channelId, null, i18nUtils.getId(), (HashMap<String, I18nPack>) i18nUtils.getParms(), shows,
				args);
	}

	public void send(Player player, CM_Chat_Request request) {
		if (player == null) {
			return;
		}
		if (operatorManager.isForbidChat(player.getObjectId())) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.OPERATOR_FORBID_CHAT);
			return;
		}
		if (player.getChatCoolTime().isCd(request.getChannelId())) {
			return;
		}
		if (request.getChannelId() == 0) {
			return;
		}
		ChannelResource resource = resources.get(request.getChannelId(), true);
		if (resource.getConditions() != null) {
			if (!conditionManager.getCoreConditions(1, resource.getConditions()).verify(player, true)) {
				return;
			}
		}

		if (request.getContent().getBytes().length > CHAT_BYTE_LIMIT_NUMBER.getValue()) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.CHAT_CHAR_TOO_MUCH_ERROR);
			return;
		}
		if (resource.getActs() != null) {
			CoreActions actions = actionManager.getCoreActions(1, resource.getActs());
			if (!actions.verify(player, true)) {
				return;
			}
			actions.act(player, ModuleInfo.valueOf(ModuleType.CHAT, SubModuleType.CHAT_ACT));
		}

		Player target = null;
		if (request.getReciverId() != 0) {
			target = playerManager.getPlayer(request.getReciverId());
		}
		if (resource.getScope() == ChannelType.PRIVATE) {
			if (target.getCountryValue() != player.getCountryValue()) {
				// 私聊要限制国际
				if (!target.getOperatorPool().getGmPrivilege().isGm()
						&& !player.getOperatorPool().getGmPrivilege().isGm()) {
					return;
				}
			}
		}

		Date openServerDate = ServerState.getInstance().getOpenServerDate();
		long openServerTime = openServerDate == null ? 0l : openServerDate.getTime();
		LogManager.addChat(player.getPlayerEnt().getServer(), player.getPlayerEnt().getAccountName(), player.getName(),
				player.getObjectId(), player.getLevel(), player.getPurse().getValue(CurrencyType.GOLD),
				target != null ? target.getPlayerEnt().getAccountName() : null, target != null ? target.getName()
						: null, target != null ? target.getLevel() : 0, System.currentTimeMillis(), player.getSession()
						.getInetIp(), request.getChannelId(), player.getOperatorPool().getGmPrivilege().isGm() ? 1 : 0,
				StringUtils.trimAllWhitespace(request.getContent()), player.getVip().getLevel(), openServerTime);

		// 敏感词过滤
		Message message = createMessage(player, request);
		String input = message.getContent();
		message.setContent(DirtyWordsManager.getInstance().filter(input, WordsType.CHATWORDS));

		handles.get(resource.getScope()).send(resource, message);
		player.getChatCoolTime().putCd(request.getChannelId(), resource.getCd());

		if ("#drop.player".equals(message.getContent())) {
			Message dropMessage = new Message();
			dropMessage.setContent(JsonUtils.object2String(player.getDropHistory()));
			dropMessage.setChannel(request.getChannelId());
			PacketSendUtility.sendPacket(player, dropMessage);
		}
		if ("#drop.server".equals(message.getContent())) {
			Message dropMessage = new Message();
			dropMessage.setContent(JsonUtils.object2String(ServerState.getInstance().getMonsterKilledHis()));
			dropMessage.setChannel(request.getChannelId());
			PacketSendUtility.sendPacket(player, dropMessage);
		}

	}

	public static ChatManager getInstance() {
		return instance;
	}

	public static void setInstance(ChatManager instance) {
		ChatManager.instance = instance;
	}

	public void sendEmotion(Player player, int faceId, boolean systemTrigger) {
		if (systemTrigger) {
			PacketSendUtility.broadcastPacketAndReceiver(player, SM_Big_Face.valueOf(player.getObjectId(), faceId));
			return;
		}
		if (!player.getVip().getResource().isBigEmoji()) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.VIP_CONDITION_NOT_SATISFY);
			return;
		}
		PacketSendUtility.broadcastPacketAndReceiver(player, SM_Big_Face.valueOf(player.getObjectId(), faceId));
	}

	public SM_PlayerChatSimple queryCountryOnlineLocal(Player player, String partOfName) {
		if (partOfName == null || partOfName.isEmpty()) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		ArrayList<PlayerChatSimple> list = new ArrayList<PlayerChatSimple>();
		if (player.getOperatorPool().getGmPrivilege().isGm()) {
			for (Country country : CountryManager.getInstance().getCountries().values()) {
				for (Map.Entry<String, Player> entry : country.getNameCivils().entrySet()) {
					if (entry.getKey().contains(partOfName))
						list.add(PlayerChatSimple.valueOf(entry.getValue()));
					if (list.size() >= 30)
						break;
				}
			}
		} else {
			for (Map.Entry<String, Player> entry : player.getCountry().getNameCivils().entrySet()) {
				if (entry.getKey().contains(partOfName))
					list.add(PlayerChatSimple.valueOf(entry.getValue()));
				if (list.size() >= 30)
					break;
			}
		}
		int index = 0;
		boolean found = false;
		for (; index < list.size(); index++) {
			if (list.get(index).getPlayerId() == player.getObjectId()) {
				found = true;
				break;
			}
		}
		if (found)
			list.remove(index);
		SM_PlayerChatSimple sm = new SM_PlayerChatSimple();
		sm.setInfos(list);
		return sm;
	}
}
