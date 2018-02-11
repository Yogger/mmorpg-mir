package com.mmorpg.mir.model.welfare.packet.vo;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.manager.ClawbackManager;
import com.mmorpg.mir.model.welfare.model.ClawbackType;
import com.mmorpg.mir.model.welfare.resource.ClawbackResource;

/**
 * 追回事件vo数据
 * 
 * @author 37wan
 * 
 */
public class ClawbackVO {

	private int eventId;// 事件id
	private int type;// 事件类型
	private int exeNum;// 执行次数
	private int runs;// 执行环数

	/**
	 * 0.未开启 1.已经追回; 2.可追回; 3.不可追回(只有爬塔存在);
	 */
	private int status;// 状态

	@Override
	public String toString() {
		return "eventId = " + eventId + " , type = " + type + " , exeNum = " + exeNum + " , runs = " + runs
				+ " ,status = " + status;
	}

	public static ClawbackVO valueOf(Player player, ClawbackResource resource) {
		ClawbackVO vo = new ClawbackVO();
		vo.setEventId(resource.getEventId());
		vo.setType(resource.getType());
		// 未开启
		if (!ClawbackManager.getInstance().isOpen(player, resource.getEventId())) {
			vo.setStatus(0);
			return vo;
		} else // 已经追回
		if (ClawbackManager.getInstance().isClawbacked(player, resource.getEventId())) {
			vo.setStatus(1);
			return vo;
		} else {
			Boolean can = ClawbackManager.getInstance().pass(player, resource);
			vo.setStatus(can ? 2 : 3);
		}
		int exeNum = player.getWelfare().getOldlawback().getExeNum(resource.getEventId());
		if (resource.getType() == ClawbackType.CLAWBACK_TYPE_NUM.getType()) {
			int maxNum = ClawbackManager.getInstance().getCorrectExeNums(resource, player.getVip().getYesterdayLevel());
			exeNum = maxNum - exeNum;
		}
		vo.setExeNum(exeNum); // 直接返回可以找回的次数，因为这里加了VIP的找回

		int runs = player.getWelfare().getOldlawback().getRunNum(resource.getEventId());
		/*if (resource.getEventId() == ClawbackEnum.CLAWBACK_EVENT_LADDER.getEventId()) {
			Long key = DayKey.valueOf().getLunchTime() - DateUtils.MILLIS_PER_DAY;
			boolean isRest = player.getWelfare().getWelfareHistory().getLastRestTime().containsKey(key);
			if (!isRest) {
				runs = player.getCopyHistory().getLadderHisCompleteIndex();
			} else {
				runs = 0;
			}
		}*/
		vo.setRuns(runs);
		return vo;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getExeNum() {
		return exeNum;
	}

	public void setExeNum(int exeNum) {
		this.exeNum = exeNum;
	}

	public int getRuns() {
		return runs;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
