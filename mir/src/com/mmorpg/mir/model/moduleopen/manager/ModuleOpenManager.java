package com.mmorpg.mir.model.moduleopen.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.h2.util.New;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.common.ResourceReload;
import com.mmorpg.mir.model.core.condition.AbstractCoreCondition;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.core.condition.quest.CompleteQuestCondition;
import com.mmorpg.mir.model.core.condition.quest.CompleteQuestORCondition;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.moduleopen.event.ModuleOpenEvent;
import com.mmorpg.mir.model.moduleopen.packet.SM_Module_Open;
import com.mmorpg.mir.model.moduleopen.packet.vo.ModuleOpenVO;
import com.mmorpg.mir.model.moduleopen.resource.ModuleOpenResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;

@Component
public class ModuleOpenManager implements ResourceReload,IModuleOpenManager {

	@Static
	private Storage<String, ModuleOpenResource> moduleOpenResources;

	/** <模块ID,模块总ID> */
	private Map<ModuleKey, String> moduleOpenMapping;
	/** <任务Id,模块总ID> */
	private Map<String, ArrayList<String>> questModuleIndex;

	private static ModuleOpenManager INSTANCE;

	@PostConstruct
	void init() {
		INSTANCE = this;
		reload();
	}

	public static ModuleOpenManager getInstance() {
		return INSTANCE;
	}

	/**
	 * moduleKey 是否开启
	 * 
	 * @param player
	 * @param moduleKey
	 *            模块
	 * @return true 表示开启, false表示未开启
	 */
	public boolean isOpenByModuleKey(Player player, ModuleKey moduleKey) {
		String value = moduleOpenMapping.get(moduleKey);
		return value == null ? true : isOpenByKey(player, value);
	}

	public boolean isOpenByKey(Player player, String key) {
		Boolean open = player.getModuleOpen().getOpeneds().get(key);
		return open == null ? false : open.booleanValue();
	}
	
	public boolean isOpenByKeyAtThatDay(Player player, String key, long time) {
		Long t = player.getModuleOpen().getOpenDate().get(key);
		return t == null? false : (time >= t || DateUtils.isSameDay(new Date(time), new Date(t)));
	}

	public void refreshAll(Player player) {
		ArrayList<String> opens = New.arrayList();
		for (ModuleOpenResource resource : moduleOpenResources.getAll()) {
			if (!isOpenByKey(player, resource.getId())) {
				boolean check = resource.isConditionVerify() ? resource.getConditions().verify(player) : resource.getConditions().verifyOr(player);
				if (!check) {
					continue;
				}
				player.getModuleOpen().openModule(resource.getId());
				EventBusManager.getInstance().syncSubmit(
							ModuleOpenEvent.valueOf(player.getObjectId(), resource.getId()));
				if (resource.isImmediatelyPush()) {
					opens.add(resource.getId());
				}
			}
		}
		if (!opens.isEmpty()) {
			PacketSendUtility.sendPacket(player, SM_Module_Open.valueOf(opens));
		}
	}
	
	public ModuleOpenVO loginRefreshVO(Player player) {
		ArrayList<String> opens = New.arrayList();
		for (ModuleOpenResource resource : moduleOpenResources.getAll()) {
			if (!isOpenByKey(player, resource.getId())) {
				boolean check = resource.isConditionVerify() ? resource.getConditions().verify(player) : resource.getConditions().verifyOr(player);
				if (!check) {
					continue;
				}
				player.getModuleOpen().openModule(resource.getId());
				EventBusManager.getInstance().syncSubmit(
							ModuleOpenEvent.valueOf(player.getObjectId(), resource.getId()));
				if (resource.isLoginOpenNotice()) {
					opens.add(resource.getId());
				}
			}
		}
		return ModuleOpenVO.valueOf(player, opens);
	}

	public void completeQuestOpenModule(Player player, String questId) {
		ArrayList<String> ids = questModuleIndex.get(questId);
		if (ids != null && (!ids.isEmpty())) {
			ArrayList<String> opens = New.arrayList();
			for (String id: ids) {
				if (isOpenByKey(player, id)) {
					continue;
				}
				ModuleOpenResource resource = moduleOpenResources.get(id, false);
				boolean check = false;
				if (resource != null) { 
					if (resource.isConditionVerify()) {
						check = resource.getConditions().verify(player, false);
					} else {
						check = resource.getConditions().verifyOr(player);
					}
				}
				if (check) {
					player.getModuleOpen().openModule(id);
					EventBusManager.getInstance().syncSubmit(
							ModuleOpenEvent.valueOf(player.getObjectId(), resource.getId()));
					if (resource.isImmediatelyPush()) {
						opens.add(resource.getId());
					}
				}
			}
			if (!opens.isEmpty())
				PacketSendUtility.sendPacket(player, SM_Module_Open.valueOf(opens));
		}
	}
	
	@Override
	public void reload() {
		Map<String, ArrayList<String>> questModuleIndexTemp = New.hashMap();
		Map<ModuleKey, String> moduleOpenMappingTemp = New.hashMap();
		for (ModuleOpenResource resource : moduleOpenResources.getAll()) {
			CoreConditions conditions = resource.getConditions();
			for (AbstractCoreCondition condition : conditions.getConditionList()) {
				if (condition instanceof CompleteQuestCondition) {
					if (!questModuleIndexTemp.containsKey(condition.getCode())) {
						questModuleIndexTemp.put(condition.getCode(), new ArrayList<String>());
					}
					questModuleIndexTemp.get(condition.getCode()).add(resource.getId());
				}
				if (condition instanceof CompleteQuestORCondition) {
					String[] questIds = JsonUtils.string2Array(condition.getCode(), String.class);
					for (String id : questIds) {
						if (!questModuleIndexTemp.containsKey(id)) {
							questModuleIndexTemp.put(id, new ArrayList<String>());
						}
						questModuleIndexTemp.get(id).add(resource.getId());
					}
				}
			}
			if (resource.getModuleKey() != 0) {
				moduleOpenMappingTemp.put(ModuleKey.valueOf(resource.getModuleKey()), resource.getId());
			}
		}
		questModuleIndex = questModuleIndexTemp;
		moduleOpenMapping = moduleOpenMappingTemp;
	}

	public ModuleOpenResource getResource(String id) {
		return moduleOpenResources.get(id, false);
	}

	@Override
	public Class<?> getResourceClass() {
		return ModuleOpenResource.class;
	}
}
