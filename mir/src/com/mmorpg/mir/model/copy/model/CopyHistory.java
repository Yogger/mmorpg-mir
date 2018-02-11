package com.mmorpg.mir.model.copy.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.copy.controller.AbstractCopyController;
import com.mmorpg.mir.model.copy.event.CopyCompleteEvent;
import com.mmorpg.mir.model.copy.event.LadderNewRecordEvent;
import com.mmorpg.mir.model.copy.event.LeaveCopyEvent;
import com.mmorpg.mir.model.copy.manager.CopyManager;
import com.mmorpg.mir.model.copy.packet.SM_Copy_BuyCount;
import com.mmorpg.mir.model.copy.packet.SM_Copy_CompleteCount;
import com.mmorpg.mir.model.copy.packet.SM_Copy_EnterCount;
import com.mmorpg.mir.model.copy.packet.SM_Copy_First_Finish;
import com.mmorpg.mir.model.copy.packet.SM_Copy_LadderComplete;
import com.mmorpg.mir.model.copy.packet.SM_Copy_ResetLadder;
import com.mmorpg.mir.model.copy.packet.SM_Ladder_ResetCount_Change;
import com.mmorpg.mir.model.copy.packet.SM_Lader_Target_Lay_Reset_Count_Change;
import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.object.route.RouteStep;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.resource.MapResource;
import com.mmorpg.mir.model.world.service.MapInstanceService;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.JsonUtils;

public class CopyHistory {
	private transient Player owner;
	private Map<String, Integer> enterHistory = New.hashMap();
	private Map<String, Integer> todayEnterHistory = New.hashMap();

	private Map<String, Integer> completeHistory = New.hashMap();
	private Map<String, Integer> todayCompleteHistory = New.hashMap();

	private Map<String, Long> lastEnterTime = New.hashMap();

	private Map<String, Integer> buyCounts = New.hashMap();

	private long lastRefreshTime;
	/** 最后一次出现的位置 */
	private transient RouteStep routeStep;

	private transient String currentCopyResourceId;

	private List<String> ladderRewarded = New.arrayList();

	private int ladderCompleteIndex;

	private int ladderResetCount;

	private int ladderHisCompleteIndex;

	private List<String> bossRewarded = New.arrayList();

	private Map<String, Long> copyFirstDoneTime = New.hashMap();

	private transient boolean failNotReturnSpecialAct;

	private Map<Integer, Integer> ladderCurrenctResetCount = New.hashMap();

	private boolean beta21ladderFix;

	private Map<String, Integer> horseEquipMaxQuestHis = new HashMap<String, Integer>();

	private transient Integer curHorseEquipQuest;
	/** 副本控制器 */
	@Transient
	private AbstractCopyController copyController;

	public void refresh() {
		if (!DateUtils.isToday(new Date(lastRefreshTime))) {
			todayEnterHistory.clear();
			buyCounts.clear();
			todayCompleteHistory.clear();
			lastRefreshTime = System.currentTimeMillis();
			ladderResetCount = 0;
		}
	}

	@JsonIgnore
	public void removeLadderCurrenctResetCount(int index) {
		if (ladderCurrenctResetCount.containsKey(index)) {
			ladderCurrenctResetCount.remove(index);
		}
		PacketSendUtility.sendPacket(owner,
				SM_Lader_Target_Lay_Reset_Count_Change.valueOf(index, getLadderCurrentResetCount(index)));
	}

	@JsonIgnore
	public void removeLadderCurrenctResetCount(List<String> ladderIds) {
		for (String ladderId : ladderIds) {
			CopyResource resource = CopyManager.getInstance().getCopyResources().get(ladderId, true);
			if (ladderCurrenctResetCount.containsKey(resource.getIndex())) {
				ladderCurrenctResetCount.remove(resource.getIndex());
			}
		}
		PacketSendUtility.sendPacket(owner, SM_Ladder_ResetCount_Change.valueOf(owner));
	}

	@JsonIgnore
	public int getLadderCurrentResetCount(int index) {
		if (ladderCurrenctResetCount.containsKey(index)) {
			return ladderCurrenctResetCount.get(index);
		}
		return 0;
	}

	@JsonIgnore
	public void addLadderResetCount() {
		ladderResetCount++;
	}

	@JsonIgnore
	public List<String> getLadderBatchIds() {
		List<String> ids = New.arrayList();
		for (CopyResource resource : CopyManager.getInstance().getCopyResources().getAll()) {
			if (resource.getType() == CopyType.LADDER) {
				if (ladderCompleteIndex < resource.getIndex() && resource.getIndex() <= ladderHisCompleteIndex) {
					ids.add(resource.getId());
				}
			}
		}
		return ids;
	}

	@JsonIgnore
	public void addTodayEnterCount(String id, int count, boolean sendPack) {
		CopyResource resource = CopyManager.getInstance().getCopyResources().get(id, true);
		if (resource.getType() == CopyType.LADDER) {
			return;
		}

		lastEnterTime.put(id, System.currentTimeMillis());
		if (!todayEnterHistory.containsKey(id)) {
			todayEnterHistory.put(id, count);
		} else {
			todayEnterHistory.put(id, todayEnterHistory.get(id) + count);
		}
		addEnterCount(id, count, sendPack);
	}

	@JsonIgnore
	public void addLastEnterTime(String id) {
		CopyResource resource = CopyManager.getInstance().getCopyResources().get(id, true);
		if (resource.getType() == CopyType.LADDER) {
			return;
		}
		lastEnterTime.put(id, System.currentTimeMillis());
	}

	@JsonIgnore
	public int getTotalTypeCopyBuyCount(CopyType type) {
		int sum = 0;
		for (Entry<String, Integer> entry : buyCounts.entrySet()) {
			CopyResource resource = CopyManager.getInstance().getCopyResources().get(entry.getKey(), true);
			if (resource.getType() == type) {
				sum += entry.getValue();
			}
		}
		return sum;
	}

	@JsonIgnore
	public int getTotalTypeCopyCount(CopyType type) {
		int sum = 0;
		for (Entry<String, Integer> entry : todayEnterHistory.entrySet()) {
			CopyResource resource = CopyManager.getInstance().getCopyResources().get(entry.getKey(), true);
			if (resource.getType() == type) {
				sum += entry.getValue();
			}
		}
		return sum;
	}

	@JsonIgnore
	public int getDailyTypeCopyFinishedCount(CopyType type) {
		int sum = 0;
		for (Entry<String, Integer> entry : todayCompleteHistory.entrySet()) {
			CopyResource resource = CopyManager.getInstance().getCopyResources().get(entry.getKey(), true);
			if (resource.getType() == type) {
				sum += entry.getValue();
			}
		}
		return sum;
	}

	@JsonIgnore
	public void resetLadderCopy(Player player, int resetCount) {
		int end = ladderCompleteIndex;
		int start = 0;
		if (ladderCompleteIndex < resetCount) {
			ladderCompleteIndex = 0;
		} else {
			ladderCompleteIndex -= resetCount;
		}
		start = ladderCompleteIndex;
		resetLadderCopyCount(start, end);
	}

	@JsonIgnore
	private void resetLadderCopyCount(int begin, int end) {
		for (int i = begin; i <= end; i++) {
			this.ladderCurrenctResetCount.put(i, ladderResetCount);
		}
		PacketSendUtility.sendPacket(owner, SM_Ladder_ResetCount_Change.valueOf(owner));
	}

	@JsonIgnore
	public void clearCopyCompleteType(CopyType type, Player player) {
		if (type == CopyType.LADDER) {
			ladderCompleteIndex = 0;
			return;
		}
		List<String> removes = New.arrayList();
		for (Entry<String, Integer> entry : completeHistory.entrySet()) {
			CopyResource resource = CopyManager.getInstance().getCopyResources().get(entry.getKey(), true);
			if (resource.getType() == type) {
				removes.add(entry.getKey());
			}
		}
		SM_Copy_ResetLadder sm = new SM_Copy_ResetLadder();
		for (String id : removes) {
			completeHistory.remove(id);
			sm.getRemoves().add(id);
		}
		PacketSendUtility.sendPacket(player, sm);
	}

	@JsonIgnore
	public int getBuyCount(String id) {
		if (buyCounts.containsKey(id)) {
			return buyCounts.get(id);
		}
		return 0;
	}

	@JsonIgnore
	public void addEnterCount(String id, int count, boolean sendPack) {
		if (!enterHistory.containsKey(id)) {
			enterHistory.put(id, count);
		} else {
			enterHistory.put(id, enterHistory.get(id) + count);
		}
		if (sendPack) {
			// 通知前端
			PacketSendUtility.sendPacket(owner, SM_Copy_EnterCount.valueOf(id, enterHistory.get(id)));
		}
	}

	@JsonIgnore
	public void addTodayCompleteCount(Player player, String id, int count, boolean sendPack, int time, boolean check) {
		CopyResource resource = CopyManager.getInstance().getCopyResources().get(id, true);
		long now = System.currentTimeMillis();
		if (check) {
			switch (resource.getType()) {
			case LADDER:
				addLadderTodayCompleteCount(player, resource, time);
				return;
			case BOSS:
				addLadderTodayCompleteCount(id, now);
				break;
			case WARBOOK:
				addLadderTodayCompleteCount(player);
				break;
			}
		}
		if (!todayCompleteHistory.containsKey(id)) {
			todayCompleteHistory.put(id, count);
		} else {
			todayCompleteHistory.put(id, todayCompleteHistory.get(id) + count);
		}
		addCompleteCount(id, count, sendPack);
	}

	private void addLadderTodayCompleteCount(Player player) {
		player.getCopyHistory().getCurrentMapInstance().getCopyInfo().setCopyComplet(true);
	}

	private void addLadderTodayCompleteCount(String id, long now) {
		if (!copyFirstDoneTime.containsKey(id)) {
			copyFirstDoneTime.put(id, now);
			PacketSendUtility.sendPacket(owner, SM_Copy_First_Finish.valueOf(id, now));
		}
	}

	private void addLadderTodayCompleteCount(Player player, CopyResource resource, int time) {
		if (resource.getIndex() > ladderCompleteIndex) {
			ladderCompleteIndex = resource.getIndex();
			PacketSendUtility.sendPacket(owner, SM_Copy_LadderComplete.valueOf(this));
		}
		if (resource.getIndex() >= ladderHisCompleteIndex) {
			// 首通25层倍数的公告
			if (resource.getIndex() > ladderHisCompleteIndex && resource.getIndex() % 25 == 0) {
				// 电视广播
				// 90001、90002、90003、90004、90005、90006、90007、90008、90009、90010
				int mutiple = resource.getIndex() / 25;
				String i18Id = Integer.toString(90000 + mutiple);
				I18nUtils utils = I18nUtils.valueOf(i18Id).addParm(I18NparamKey.PLAYERNAME,
						I18nPack.valueOf(player.getName()));
				utils.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
				ChatManager.getInstance().sendSystem(11001, utils, null);

				// 聊天广播
				// 311001、311002、311003、311004、311005、311006、311007、311008、311009、311010
				i18Id = Integer.toString(311000 + mutiple);
				utils = I18nUtils.valueOf(i18Id).addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(player.getName()));
				utils.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(player.getCountry().getName()));
				ChatManager.getInstance().sendSystem(0, utils, null);
			}

			ladderHisCompleteIndex = resource.getIndex();
			EventBusManager.getInstance().syncSubmit(
					LadderNewRecordEvent.valueOf(owner.getObjectId(), resource.getIndex(), time));
		}
	}

	@JsonIgnore
	private void addCompleteCount(String id, int count, boolean sendPack) {
		if (!completeHistory.containsKey(id)) {
			completeHistory.put(id, count);
			CopyResource copyResource = CopyManager.getInstance().getCopyResources().get(id, true);
			if (copyResource.getType() == CopyType.MINGJIANG && copyResource.getRewardGroupId() != null) {
				List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(owner,
						copyResource.getRewardGroupId());
				RewardManager.getInstance().grantReward(owner, rewardIds,
						ModuleInfo.valueOf(copyResource.getRewardLogType(), SubModuleType.COPY_REWARD));
			}
		} else {
			completeHistory.put(id, completeHistory.get(id) + count);
		}
		EventBusManager.getInstance().submit(CopyCompleteEvent.valueOf(owner.getObjectId(), id));
		// 通知前端
		if (sendPack)
			PacketSendUtility.sendPacket(owner, SM_Copy_CompleteCount.valueOf(id, completeHistory.get(id)));
	}

	@JsonIgnore
	public void addBuyCount(String id, int count) {
		if (!buyCounts.containsKey(id)) {
			buyCounts.put(id, count);
		} else {
			buyCounts.put(id, buyCounts.get(id) + count);
		}
		// 通知前端
		PacketSendUtility.sendPacket(owner, SM_Copy_BuyCount.valueOf(id, buyCounts.get(id)));
	}

	public CopyHistoryVO createVO() {
		return CopyHistoryVO.valueOf(this);
	}

	/**
	 * 离开副本
	 */
	@JsonIgnore
	public void leaveCopy() {
		if (getCurrentCopyResourceId() == null) {
			return;
		}
		owner.getObserveController().notifyLeaveCopyObservers(getCurrentCopyResourceId());
		if (owner.getPosition() != null && owner.getPosition().getMapRegion() != null) {
			WorldMapInstance worldMapInstance = owner.getPosition().getMapRegion().getParent();
			if (worldMapInstance != null) {
				worldMapInstance.stopTasks();
			}
		}

		if (copyController == null || !copyController.isControlleaveCopy()) {
			MapResource mapResource = World.getInstance().getMapResource(getRouteStep().getMapId());
			if (mapResource.getCountry() != owner.getCountryValue()) {
				if (mapResource.getForbidEnterCondition() != null) {
					if (mapResource.getForbidEnterCondition().verify(owner, false)) {
						List<String> result = ChooserManager.getInstance().chooseValueByRequire(
								owner.getCountryValue(), PlayerManager.getInstance().BACKHOME_POINT.getValue());
						RelivePosition p = JsonUtils.string2Object(result.get(0), RelivePosition.class);
						World.getInstance().setPosition(owner, p.getMapId(), p.getX(), p.getY(), (byte) 0);
						PacketSendUtility.sendErrorMessage(owner, ManagedErrorCode.MAP_FORBIDTIME);
					} else {
						World.getInstance().setPosition(owner, getRouteStep().getMapId(), getRouteStep().getX(),
								getRouteStep().getY(), owner.getHeading());
					}
				}
			} else {
				World.getInstance().setPosition(owner, getRouteStep().getMapId(), getRouteStep().getX(),
						getRouteStep().getY(), owner.getHeading());
			}
			owner.sendUpdatePosition();
		}

		String oldCurrentCopyResourceId = currentCopyResourceId;
		currentCopyResourceId = null;
		EventBusManager.getInstance().syncSubmit(LeaveCopyEvent.valueOf(owner.getObjectId(), oldCurrentCopyResourceId));
	}

	@JsonIgnore
	public void enterCopy(WorldMapInstance instance, Player player, CopyResource resource) {
		if (!player.isInCopy()) {
			player.getMoveController().stopMoving();
			player.getCopyHistory().setRouteStep(RouteStep.valueOf(player.getMapId(), player.getX(), player.getY()));
		}
		World.getInstance().setPosition(player, instance.getParent().getMapId(), instance.getInstanceId(),
				resource.getX(), resource.getY(), resource.getHeading());
		player.sendUpdatePosition();
		instance.register(player.getObjectId());
		player.getCopyHistory().setCurrentCopyResourceId(resource.getId());
		if (resource.getType() != CopyType.LADDER) {
			if (!resource.isNoEnterAddCount()) {
				player.getCopyHistory().addTodayEnterCount(resource.getId(), 1, true);
			} else {
				// 第一次进入增加时间
				player.getCopyHistory().addLastEnterTime(resource.getId());
			}
		}
	}

	@JsonIgnore
	public WorldMapInstance getCurrentMapInstance() {
		if (currentCopyResourceId == null) {
			return null;
		}
		CopyResource resource = CopyManager.getInstance().getCopyResources().get(currentCopyResourceId, true);
		WorldMapInstance instance = MapInstanceService.getRegisteredInstanceByCopyId(resource.getMapId(),
				owner.getObjectId(), currentCopyResourceId);
		if (instance == null) {
			currentCopyResourceId = null;
		}
		return instance;
	}

	@JsonIgnore
	public WorldMapInstance getCurrentNotRewardMapInstance() {
		if (currentCopyResourceId == null) {
			return null;
		}
		CopyResource resource = CopyManager.getInstance().getCopyResources().get(currentCopyResourceId, true);

		WorldMapInstance instance = MapInstanceService.getRegisteredNotRewardInstanceByCopyId(resource.getMapId(),
				owner.getObjectId(), currentCopyResourceId);
		if (instance == null) {
			currentCopyResourceId = null;
		}
		return instance;
	}

	@JsonIgnore
	public CopyResource getCurrentCopyResource() {
		if (currentCopyResourceId == null) {
			return null;
		}
		return CopyManager.getInstance().getCopyResources().get(currentCopyResourceId, true);
	}

	public Map<String, Integer> getEnterHistory() {
		return enterHistory;
	}

	public void setEnterHistory(Map<String, Integer> enterHistory) {
		this.enterHistory = enterHistory;
	}

	public Map<String, Integer> getTodayEnterHistory() {
		return todayEnterHistory;
	}

	public void setTodayEnterHistory(Map<String, Integer> todayEnterHistory) {
		this.todayEnterHistory = todayEnterHistory;
	}

	public Map<String, Long> getLastEnterTime() {
		return lastEnterTime;
	}

	public void setLastEnterTime(Map<String, Long> lastEnterTime) {
		this.lastEnterTime = lastEnterTime;
	}

	public Map<String, Integer> getBuyCounts() {
		return buyCounts;
	}

	public void setBuyCounts(Map<String, Integer> buyCounts) {
		this.buyCounts = buyCounts;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	@JsonIgnore
	public String getCurrentCopyResourceId() {
		return currentCopyResourceId;
	}

	public void setCurrentCopyResourceId(String currentCopyResourceId) {
		this.currentCopyResourceId = currentCopyResourceId;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	public Map<String, Integer> getCompleteHistory() {
		return completeHistory;
	}

	public void setCompleteHistory(Map<String, Integer> completeHistory) {
		this.completeHistory = completeHistory;
	}

	public Map<String, Integer> getTodayCompleteHistory() {
		return todayCompleteHistory;
	}

	public void setTodayCompleteHistory(Map<String, Integer> todayCompleteHistory) {
		this.todayCompleteHistory = todayCompleteHistory;
	}

	public List<String> getLadderRewarded() {
		return ladderRewarded;
	}

	public void setLadderRewarded(List<String> ladderRewarded) {
		this.ladderRewarded = ladderRewarded;
	}

	public int getLadderResetCount() {
		return ladderResetCount;
	}

	public void setLadderResetCount(int ladderResetCount) {
		this.ladderResetCount = ladderResetCount;
	}

	public int getLadderCompleteIndex() {
		return ladderCompleteIndex;
	}

	public void setLadderCompleteIndex(int ladderCompleteIndex) {
		this.ladderCompleteIndex = ladderCompleteIndex;
	}

	public int getLadderHisCompleteIndex() {
		return ladderHisCompleteIndex;
	}

	public void setLadderHisCompleteIndex(int ladderHisCompleteIndex) {
		this.ladderHisCompleteIndex = ladderHisCompleteIndex;
	}

	public RouteStep getRouteStep() {
		return routeStep;
	}

	public void setRouteStep(RouteStep routeStep) {
		this.routeStep = routeStep;
	}

	@JsonIgnore
	public AbstractCopyController getCopyController() {
		return copyController;
	}

	public void setCopyController(AbstractCopyController copyController) {
		this.copyController = copyController;
	}

	public List<String> getBossRewarded() {
		return bossRewarded;
	}

	public void setBossRewarded(List<String> bossRewarded) {
		this.bossRewarded = bossRewarded;
	}

	public Map<String, Long> getCopyFirstDoneTime() {
		return copyFirstDoneTime;
	}

	public void setCopyFirstDoneTime(Map<String, Long> copyFirstDoneTime) {
		this.copyFirstDoneTime = copyFirstDoneTime;
	}

	public boolean isFailNotReturnSpecialAct() {
		return failNotReturnSpecialAct;
	}

	public void setFailNotReturnSpecialAct(boolean failNotReturnSpecialAct) {
		this.failNotReturnSpecialAct = failNotReturnSpecialAct;
	}

	public Map<Integer, Integer> getLadderCurrenctResetCount() {
		return ladderCurrenctResetCount;
	}

	public void setLadderCurrenctResetCount(Map<Integer, Integer> ladderCurrenctResetCount) {
		this.ladderCurrenctResetCount = ladderCurrenctResetCount;
	}

	public boolean isBeta21ladderFix() {
		return beta21ladderFix;
	}

	public void setBeta21ladderFix(boolean beta21ladderFix) {
		this.beta21ladderFix = beta21ladderFix;
	}

	public Integer getCurHorseEquipQuest() {
		return curHorseEquipQuest;
	}

	public void setCurHorseEquipQuest(Integer curHorseEquipQuest) {
		this.curHorseEquipQuest = curHorseEquipQuest;
	}

	public Map<String, Integer> getHorseEquipMaxQuestHis() {
		return horseEquipMaxQuestHis;
	}

	public void setHorseEquipMaxQuestHis(Map<String, Integer> horseEquipMaxQuestHis) {
		this.horseEquipMaxQuestHis = horseEquipMaxQuestHis;
	}

}
