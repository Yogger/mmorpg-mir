package com.mmorpg.mir.model.nickname.manager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.ResourceReload;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.nickname.model.Nickname;
import com.mmorpg.mir.model.nickname.model.NicknamePool;
import com.mmorpg.mir.model.nickname.packet.SM_Nickname_Active;
import com.mmorpg.mir.model.nickname.resource.NicknameResource;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.event.event.IEvent;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.JsonUtils;
import com.windforce.common.utility.New;

@Component
public class NicknameManager implements ResourceReload {
	private static final Logger logger = Logger.getLogger(NicknameManager.class);

	private Map<Class<?>, List<Integer>> activeEvents;

	private Map<Class<?>, List<Integer>> equipEvents;

	private static NicknameManager INSTANCE;

	@Autowired
	private EventBusManager eventBusManager;

	@Autowired
	private PlayerManager playerManager;

	@PostConstruct
	void init() {
		INSTANCE = this;
	}

	public static NicknameManager getInstance() {
		return INSTANCE;
	}

	@Static
	private Storage<Integer, NicknameResource> nicknameResources;

	@SuppressWarnings("unchecked")
	@Override
	public void reload() {
		try {
			Map<Class<?>, List<Integer>> activeEventSkillsTemp = New.hashMap();
			Map<Class<?>, List<Integer>> equipEventSkillsTemp = New.hashMap();

			Set<Class<?>> clazzes = new HashSet<Class<?>>();
			for (NicknameResource resource : nicknameResources.getAll()) {
				for (Class<?> clazz : resource.getAllActiveEvent()) {
					if (!activeEventSkillsTemp.containsKey(clazz)) {
						activeEventSkillsTemp.put(clazz, new ArrayList<Integer>());
					}
					activeEventSkillsTemp.get(clazz).add(resource.getId());
					if (!clazzes.contains(clazz)) {
						clazzes.add(clazz);
					}
				}

				for (Class<?> clazz : resource.getAllEquipEvent()) {
					if (!equipEventSkillsTemp.containsKey(clazz)) {
						equipEventSkillsTemp.put(clazz, new ArrayList<Integer>());
					}
					equipEventSkillsTemp.get(clazz).add(resource.getId());
					if (!clazzes.contains(clazz)) {
						clazzes.add(clazz);
					}
				}
			}

			activeEvents = activeEventSkillsTemp;
			equipEvents = equipEventSkillsTemp;

			Method method = NicknameManager.class.getMethod("doRefreshEvent", IEvent.class);
			for (Class<?> clazz : clazzes) {
				eventBusManager.registReceiver(this, method, (Class<? extends IEvent>) clazz);
			}

			logger.error("size[" + getSize(activeEvents) + "]activeEvents");
			logger.error("size[" + getSize(equipEvents) + "]equipEvents");
		} catch (Exception e) {
			logger.error("初始化称号事件接收器失败", e);
			throw new RuntimeException("初始化称号事件接收器失败");
		}
	}

	private int getSize(Map<Class<?>, List<Integer>> map) {
		int size = 0;
		for (Entry<Class<?>, List<Integer>> entry : map.entrySet()) {
			size += entry.getValue().size();
		}
		return size;
	}

	@Override
	public Class<?> getResourceClass() {
		return NicknameResource.class;
	}

	public Storage<Integer, NicknameResource> getNicknameResources() {
		return nicknameResources;
	}

	public void doRefreshEvent(IEvent event) {
		Player player = playerManager.getPlayer(event.getOwner());
		// 检查删除
		List<Integer> activeIds = activeEvents.get(event.getClass());
		if (activeIds != null) {
			for (Integer id : activeIds) {
				player.getNicknamePool().checkAndRemove(id);
			}
		}

		// 检查装备
		List<Integer> equipIds = equipEvents.get(event.getClass());
		if (equipIds != null) {
			for (Integer id : equipIds) {
				player.getNicknamePool().checkAndUnEquip(id);
			}
		}

		if (!ModuleOpenManager.getInstance().isOpenByModuleKey(player, ModuleKey.NICKNAME)) { // 没有开启
			return;
		}

		// 尝试激活
		player.getNicknamePool().tryActive(activeIds, nicknameResources);
	}

	public void unEquip(Player player, Integer id) {
		if (player.getNicknamePool().getEquipIds().contains(id)) {
			player.getNicknamePool().unEquip(id, true);
		}
	}

	public void equip(Player player, Integer equipId) {
		player.getNicknamePool().equip(equipId);
	}

	public void activeNickName(Player player, Integer id) {
		if (!player.getNicknamePool().getActiveds().containsKey(id)) {
			throw new ManagedException(ManagedErrorCode.NOT_FOUND_NICKNAME);
		}
		Nickname nickName = player.getNicknamePool().getActiveds().get(id);
		if (nickName.isDeprecat()) {
			player.getNicknamePool().checkAndRemove(nickName.getId());
			throw new ManagedException(ManagedErrorCode.NICKNAME_INVALID);
		}
		nickName.setActived(true);
		Map<String, Integer> notice = nickName.getResource().getI18nNotice();
		for (Entry<String, Integer> entry : notice.entrySet()) {
			I18nUtils utils = I18nUtils.valueOf(entry.getKey());
			utils.addParm("name", I18nPack.valueOf(player.getName()));
			utils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
			ChatManager.getInstance().sendSystem(entry.getValue(), utils, null);
		}
		player.getNicknamePool().refreshModifiers(true);
		PacketSendUtility.sendPacket(player, SM_Nickname_Active.valueOf(player.getNicknamePool(), id.intValue()));
	}
	
	public void refreshNickName(Player player, boolean login) {
		for (NicknameResource resource: nicknameResources.getAll()) {
			player.getNicknamePool().tryActive(Arrays.asList(resource.getId()), nicknameResources);
			boolean specialPass = (resource.getMergeTransferId() != null && ServerState.getInstance().hasMerged());
			if (specialPass) {
				continue;
			}
			if (resource.isActiveRemove()) {
				player.getNicknamePool().checkAndRemove(resource.getId());
			}
		}
		player.getNicknamePool().checkDeprecat(!login);
	}
	
	public void mergeServerTransferPlayer(PlayerEnt playerEnt, int targetCountryId) {
		if (playerEnt.getNickNameJson() != null) {
			NicknamePool nickNamePool = JsonUtils.string2Object(playerEnt.getNickNameJson(), NicknamePool.class);
			List<Integer> processIds = New.arrayList();
			for (Nickname nickName : nickNamePool.getActiveds().values()) {
				NicknameResource resource = nicknameResources.get(nickName.getId(), true);
				if (resource.getMergeTransferId() != null && resource.getMergeTransferId().length != 0) {
					processIds.add(resource.getId());
				}
			}
			for (Integer transferId : processIds) {
				Integer targetId = nicknameResources.get(transferId, true).getMergeTransferId()[targetCountryId - 1];
				nickNamePool.getActiveds().remove(transferId);
				nickNamePool.getEquipIds().remove(transferId);
				nickNamePool.getActiveds().put(targetId, nicknameResources.get(targetId, true).creatNickname());
			}
			if (!processIds.isEmpty()) {
				playerEnt.setNickNameJson(JsonUtils.object2String(nickNamePool));
			}
		}
	}
}
