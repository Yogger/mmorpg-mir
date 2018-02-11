package com.mmorpg.mir.model.welfare.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.welfare.manager.SignManager;
import com.mmorpg.mir.model.welfare.manager.WelfareConfigValueManager;
import com.mmorpg.mir.model.welfare.resource.SignResource;
import com.mmorpg.mir.model.welfare.util.Util;
import com.windforce.common.utility.DateUtils;

/**
 * 签到
 * 
 * @author 37wan
 * 
 */
public class Sign {

	private ArrayList<Long> signTimeList = New.arrayList();// 记录玩家签到的时间
	private ArrayList<Integer> rewardedList = New.arrayList();// 记录领过奖的普通累计天数;
	private int currentFillSignNum;// 已经补签的次数
	private int totalSignNum; // 当前总共签到多少次 ---满30次清零
	private int totalSignCount;// 总共签到次数--不清0

	@Transient
	private Player owner;

	public static Sign valueOf(Player owner) {
		Sign result = new Sign();
		result.owner = owner;
		return result;
	}

	public ArrayList<Long> getSignTimeList() {
		return signTimeList;
	}

	public void setSignTimeList(ArrayList<Long> signTimeList) {
		this.signTimeList = signTimeList;
	}

	public ArrayList<Integer> getRewardedList() {
		return rewardedList;
	}

	public void setRewardedList(ArrayList<Integer> rewardedList) {
		this.rewardedList = rewardedList;
	}

	public int getCurrentFillSignNum() {
		return currentFillSignNum;
	}

	public void setCurrentFillSignNum(int currentFillSignNum) {
		this.currentFillSignNum = currentFillSignNum;
	}

	/** 是否能补签 */
	@JsonIgnore
	public boolean canFillSign(Player player) {
		return currentFillSignNum < getMaxFillSignNum(player);
	}

	/** 获取当前vip等级能补签的最多次数 */
	@JsonIgnore
	private int getMaxFillSignNum(Player player) {
		return player.getVip().getResource().getFillDays();
	}

	@JsonIgnore
	public void addCurrentFillSignNum() {
		currentFillSignNum++;
	}

	@JsonIgnore
	public void rewarded(int days) {
		rewardedList.add(new Integer(days));
	}

	@JsonIgnore
	public boolean isRewarded(int days) {
		return rewardedList.contains(new Integer(days));
	}

	@JsonIgnore
	public boolean addSign(long time) {
		if (!canSign(time)) {
			return false;
		}
		totalSignNum++;
		this.totalSignCount++;
		LogManager.addSign(owner.getPlayerEnt().getServer(), owner.getPlayerEnt().getAccountName(), owner.getName(),
				owner.getObjectId(), totalSignNum, currentFillSignNum, time, System.currentTimeMillis());
		return signTimeList.add(new Long(time));
	}

	/** 检查是否能签到 */
	@JsonIgnore
	public boolean canSign(long time) {
		for (Long t : signTimeList) {
			if (Util.getInstance().isSameDay(time, t)) {
				return false;
			}
		}
		return true;
	}

	/** 清除签到记录 */
	@JsonIgnore
	public void clearAll() {
		compensateSignReward();
		// signTimeList.clear();
		clearWhenPassMonth();
		totalSignNum = 0;
		rewardedList.clear();
		currentFillSignNum = 0;
	}

	private void compensateSignReward() {
		List<String> rewardIds = New.arrayList();
		for (SignResource res : SignManager.getInstance().getAllSignResources()) {
			boolean hasSpecialReward = res.getDefaultRewadChooserGroup() != null
					&& !res.getDefaultRewadChooserGroup().isEmpty();
			if (hasSpecialReward && !rewardedList.contains(res.getDays())) {
				List<String> rewards = ChooserManager.getInstance().chooseValueByRequire(owner,
						res.getDefaultRewadChooserGroup());
				rewardIds.addAll(rewards);
			}
		}
		if (!rewardIds.isEmpty()) {
			I18nUtils title = I18nUtils.valueOf(WelfareConfigValueManager.getInstance().SIGN_COMPENSATE_MAIL_TITLE
					.getValue());
			I18nUtils content = I18nUtils.valueOf(WelfareConfigValueManager.getInstance().SIGN_COMPENSATE_MAIL_CONTENT
					.getValue());
			Reward reward = RewardManager.getInstance().creatReward(owner, rewardIds, null);
			// 这里假设发的都是 物品
			for (Reward spilited : reward.spilteItemReward(RewardManager.getInstance().MAIL_MAX_ITEMS.getValue())) {
				Mail mail = Mail.valueOf(title, content, null, spilited);
				MailManager.getInstance().sendMail(mail, owner.getObjectId());
			}
		}
	}

	@JsonIgnore
	public void clearWhenPassMonth() {
		ArrayList<Long> newSignTimeList = New.arrayList();
		long now = System.currentTimeMillis();
		for (int i = 0; i < signTimeList.size(); i++) {
			if (Util.getInstance().isSameMonth(now, signTimeList.get(i))) {
				newSignTimeList.add(signTimeList.get(i));
			}
		}
		signTimeList.clear();
		signTimeList = newSignTimeList;
	}

	/** 总共签到了多少天 */
	@JsonIgnore
	public int getSize() {
		return signTimeList.size();
	}

	/** 最早签到的时间 */
	@JsonIgnore
	public long getFirstTime() {
		if (signTimeList.isEmpty()) {
			return 0;
		}
		return signTimeList.get(0);
	}

	/** 最后签到的时间 */
	@JsonIgnore
	public long getLastTime() {
		if (signTimeList.isEmpty()) {
			return 0;
		}
		return signTimeList.get(getSize() - 1);
	}

	public int getTotalSignNum() {
		return totalSignNum;
	}

	public void setTotalSignNum(int totalSignNum) {
		this.totalSignNum = totalSignNum;
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
	public void see(Player player) {
		System.out.println("签到:");
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (long signTime : signTimeList) {
			String date = DateUtils.date2String(new Date(signTime), "yyyy-MM-dd HH:mm:ss");
			builder.append(date);
			if (i < signTimeList.size() - 1) {
				builder.append(",");
			}
			i++;
		}
		builder.append("]");
		System.out.println(builder.toString());

		builder.setLength(0);
		System.out.println("领取奖励:");
		builder.append("[");
		int j = 0;
		for (int days : rewardedList) {
			builder.append(days);
			if (j < rewardedList.size() - 1) {
				builder.append(",");
			}
			j++;
		}
		builder.append("]");
		System.out.println(builder.toString());

		builder.setLength(0);
		System.out.println("vip最多能补签天数:" + getMaxFillSignNum(player) + " , 已经补签天数:" + currentFillSignNum);
	}

	public int getTotalSignCount() {
		return totalSignCount;
	}

	public void setTotalSignCount(int totalSignCount) {
		this.totalSignCount = totalSignCount;
	}

}
