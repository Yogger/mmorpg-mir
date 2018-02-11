package com.mmorpg.mir.model.rescue.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.GatherEvent;
import com.mmorpg.mir.model.gameobjects.event.MonsterKillEvent;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.rescue.config.RescueConfig;
import com.mmorpg.mir.model.rescue.manager.RescueManager;
import com.mmorpg.mir.model.rescue.model.typehandle.RescueTypeHandle;
import com.mmorpg.mir.model.rescue.packet.SM_Rescue_Reward;
import com.mmorpg.mir.model.rescue.resource.RescueResource;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.welfare.event.RescueEvent;
import com.mmorpg.mir.model.welfare.model.ClawbackEnum;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;

public class Rescue {
	@Transient
	private transient Player owner;
	// 所有的营救任务
	private ArrayList<RescueItem> items;
	// 今日完成的营救任务次数
	private int todayCompleteCount;
	// 总共完成的营救任务次数
	private transient int totalCompleteCount;
	// 营救任务是否开始
	private boolean start;
	// 营救总共的奖励
	private transient Reward reward;

	private transient long lastRefreshTime;

	public static Rescue valueOf(Player owner) {
		Rescue rescue = new Rescue();
		rescue.owner = owner;
		rescue.items = new ArrayList<RescueItem>();
		rescue.lastRefreshTime = System.currentTimeMillis();
		return rescue;
	}

	@JsonIgnore
	public void refresh() {
		if (!DateUtils.isToday(new Date(lastRefreshTime))) {
			doRefresh();
		}
	}

	@JsonIgnore
	public void doRefresh() {
		todayCompleteCount = 0;
		items.clear();
		start = false;
		lastRefreshTime = System.currentTimeMillis();

	}

	@JsonIgnore
	private RescueItem getRescueItem(RescuePhase phase) {
		for (RescueItem item : items) {
			if (item.getPhase() == phase) {
				return item;
			}
		}
		return null;
	}

	@JsonIgnore
	public boolean isFirstStart() {
		RescueItem item = getRescueItem(RescuePhase.INCOMPLETE);
		if (item != null && item.getIndex() == 1) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public void monsterEvent(MonsterKillEvent monsterKillEvent) {
		if (!monsterKillEvent.isKnowPlayer()) {
			return;
		}
		RescueItem current = getRescueItem(RescuePhase.INCOMPLETE);
		if (current != null) {
			if (current.getResource().getType() == RescueType.MONSTER
					&& current.getResource().getMonsterId().equals(monsterKillEvent.getKey())) {
				addValue(current);
			}
		}
	}

	@JsonIgnore
	public void gatherEvent(GatherEvent gatherEvent) {
		RescueItem current = getRescueItem(RescuePhase.INCOMPLETE);
		if (current != null) {
			if (current.getResource().getType() == RescueType.GATHER
					&& current.getResource().getGatherId().equals(gatherEvent.getKey())) {
				addValue(current);
			}
		}
	}

	/**
	 * 玩家领奖
	 */
	@JsonIgnore
	public void chatReward() {
		if (!start) {
			return;
		}
		RescueItem current = getRescueItem(RescuePhase.INREWARD);
		if (current != null) {
			if (current.getPhase() == RescuePhase.INREWARD) {
				current.setPhase(RescuePhase.COMPLETE);
				if (reward == null) {
					reward = Reward.valueOf();
				}
				reward.addReward(rewardRescue(current.getResource().getChooserRewardId()));
				boolean isLast = isLast(current);
				Reward sendReward = reward.copy();
				accpetNext(current);
				PacketSendUtility.sendPacket(owner, this);
				if (isLast) {
					PacketSendUtility.sendPacket(owner, SM_Rescue_Reward.valueOf(sendReward));
				}
			}
		}
	}

	private boolean isLast(RescueItem complete) {
		boolean last = (items.indexOf(complete) == (items.size() - 1));
		return last || complete.getNextResource().getMilitaryLevel() > owner.getMilitary().getRank();
	}

	@JsonIgnore
	private void addValue(RescueItem current) {
		current.addValue();
		if (current.isComplete()) {
			doComplete(current);
		} else {
			PacketSendUtility.sendPacket(owner, current);
		}
	}

	@JsonIgnore
	private Reward rewardRescue(String rewardChooserId) {
		List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(owner, rewardChooserId);
		List<String> EXPRESCUE = ChooserManager.getInstance().chooseValueByRequire(owner,
				RescueConfig.getInstance().EXPRESCUE.getValue());
		List<String> CURRENCYRESCUE = ChooserManager.getInstance().chooseValueByRequire(owner,
				RescueConfig.getInstance().CURRENCYRESCUE.getValue());
		Map<String, Object> params = New.hashMap();
		params.put("EXPRESCUE", EXPRESCUE.get(0));
		params.put("CURRENCYRESCUE", CURRENCYRESCUE.get(0));
		params.put("LEVEL", owner.getLevel());
		params.put("WORLD_CLASS_BONUS", WorldRankManager.getInstance().getPlayerWorldLevel(owner));
		params.put("HONORINCREASE", owner.getGameStats().getCurrentStat(StatEnum.HONOR_PLUS) * 1.0 / 10000);
		return RewardManager.getInstance().grantReward(owner, rewardIds,
				ModuleInfo.valueOf(ModuleType.RESCUE, SubModuleType.RESCUE_REWARD), params);
	}

	/**
	 * 转换到下一个任务,如果任务链完成就重头开始
	 * 
	 * @param complete
	 */
	@JsonIgnore
	private void accpetNext(RescueItem complete) {
		boolean isLast = (items.indexOf(complete) == (items.size() - 1));
		if (isLast || complete.getNextResource().getMilitaryLevel() > owner.getMilitary().getRank()) {
			todayCompleteCount++;
			totalCompleteCount++;
			EventBusManager.getInstance().submit(RescueEvent.valueOf(owner.getObjectId()));
			items.clear();
			start = false;
			if (!isTodayAllCompleted()) {
				resetAllItem(owner.getCountryValue());
			}
			LogManager.rescue(owner.getObjectId(), owner.getPlayerEnt().getServer(), owner.getPlayerEnt()
					.getAccountName(), owner.getName(), System.currentTimeMillis(), todayCompleteCount,
					totalCompleteCount);
		} else {
			RescueItem ri = items.get(items.indexOf(complete) + 1);
			if (ri.getResource().getType() == RescueType.CHAT) {
				ri.setPhase(RescuePhase.INREWARD);
			} else {
				ri.setPhase(RescuePhase.INCOMPLETE);
			}
		}
	}

	@JsonIgnore
	private void doComplete(RescueItem complete) {
		if (complete.getResource().getRewardNpc() == null) {
			complete.setPhase(RescuePhase.COMPLETE);
			String chooserId = complete.getResource().getChooserRewardId();
			if (reward == null) {
				reward = Reward.valueOf();
			}
			reward.addReward(rewardRescue(chooserId));
			boolean isLast = isLast(complete);
			Reward sendReward = reward.copy();
			accpetNext(complete);
			PacketSendUtility.sendPacket(owner, this);
			if (isLast) {
				PacketSendUtility.sendPacket(owner, SM_Rescue_Reward.valueOf(sendReward));
			}
		} else {
			complete.setPhase(RescuePhase.INREWARD);
			PacketSendUtility.sendPacket(owner, complete);
		}
	}

	/**
	 * 使用营救令
	 * 
	 * @return
	 */
	public boolean useRescueItem() {
		if (!start) {
			throw new ManagedException(ManagedErrorCode.RESCUE_NOT_START);
		}
		boolean allComplete = true;
		for (RescueItem item : items) {
			if (item.getPhase() != RescuePhase.COMPLETE) {
				allComplete = false;
				break;
			}
		}
		if (allComplete) {
			throw new ManagedException(ManagedErrorCode.RESCUE_FINISH);
		}

		String[] itemActs = RescueConfig.getInstance().RESCUEITEM_ACTIONS.getValue();
		CoreActions actions = CoreActionManager.getInstance().getCoreActions(1, itemActs);
		if (!actions.verify(owner, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		actions.act(owner, ModuleInfo.valueOf(ModuleType.RESCUE, SubModuleType.RESCUE_ITEM_ACT));
		if (reward == null) {
			reward = Reward.valueOf();
		}
		for (RescueItem item : items) {
			if (item.getPhase() != RescuePhase.COMPLETE) {
				Reward rewardItem = rewardRescue(item.getResource().getChooserRewardId());
				reward.addReward(rewardItem);
				item.setPhase(RescuePhase.COMPLETE);
			}
		}
		PacketSendUtility.sendPacket(owner, SM_Rescue_Reward.valueOf(reward.copy()));

		todayCompleteCount++;
		totalCompleteCount++;
		EventBusManager.getInstance().submit(RescueEvent.valueOf(owner.getObjectId()));
		items.clear();
		start = false;
		if (!isTodayAllCompleted()) {
			resetAllItem(owner.getCountryValue());
		}
		LogManager.rescue(owner.getObjectId(), owner.getPlayerEnt().getServer(), owner.getPlayerEnt().getAccountName(),
				owner.getName(), System.currentTimeMillis(), todayCompleteCount, totalCompleteCount);

		PacketSendUtility.sendPacket(owner, this);
		return true;
	}

	/**
	 * 重置任务
	 * 
	 * @param countryId
	 */
	@JsonIgnore
	public boolean resetAllItem(int countryId) {
		boolean reset = false;
		if (!isTodayAllCompleted()) {
			ArrayList<RescueItem> rescueItems = new ArrayList<RescueItem>();
			for (RescueResource rr : RescueConfig.getInstance().selectRescues(countryId)) {
				if (rr.getMilitaryLevel() <= owner.getMilitary().getRank()) {
					RescueTypeHandle handle = RescueManager.getInstance().getRescueTypeHandle(rr.getType());
					rescueItems.add(handle.create(rr, owner));
					reset = true;
				}
			}
			this.reward = Reward.valueOf();
			this.items = rescueItems;
		}
		return reset;
	}

	@JsonIgnore
	private boolean isTodayAllCompleted() {
		// 加上福利大厅的找回次数限制,Vip次数
		if (todayCompleteCount >= 1 + owner.getVip().getResource().getExRescueCount()
				+ owner.getWelfare().getWelfareHistory().getClawbackNum(ClawbackEnum.CLAWBACK_EVENT_RESCUE)) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public void accpetLastRescueItem() {
		if (!isTodayAllCompleted()) {
			RescueItem last = items.get(items.size() - 1);
			if (last.getPhase() != RescuePhase.INACCEPT) {
				return;
			}
			String nextId = last.getResource().getNextId();
			if (nextId != null) {
				RescueResource rr = RescueConfig.getInstance().getResource(nextId);
				if (owner.getMilitary().getRank() >= rr.getMilitaryLevel()) {
					RescueItem newRescue = RescueItem.valueOf(rr);
					items.add(newRescue);
					PacketSendUtility.sendPacket(owner, newRescue);
					accpetLastRescueItem();
				}
			}
		}
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public ArrayList<RescueItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<RescueItem> items) {
		this.items = items;
	}

	public int getTodayCompleteCount() {
		return todayCompleteCount;
	}

	public void setTodayCompleteCount(int todayCompleteCount) {
		this.todayCompleteCount = todayCompleteCount;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	@JsonIgnore
	public void initStart() {
		setStart(true);
		reward = Reward.valueOf();
	}

	public int getTotalCompleteCount() {
		return totalCompleteCount;
	}

	public void setTotalCompleteCount(int totalCompleteCount) {
		this.totalCompleteCount = totalCompleteCount;
	}

	public Reward getReward() {
		return reward;
	}

	public void setReward(Reward reward) {
		this.reward = reward;
	}

}
