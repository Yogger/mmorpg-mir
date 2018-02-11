package com.mmorpg.mir.model.country.model;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.Transient;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.country.event.BecomeReseveKingEvent;
import com.mmorpg.mir.model.country.event.ReserveKingAbdicateEvent;
import com.mmorpg.mir.model.country.event.ReserveKingRetireEvent;
import com.mmorpg.mir.model.country.packet.SM_ReserveKing_Become;
import com.mmorpg.mir.model.country.packet.SM_ReserveKing_Finish_Task;
import com.mmorpg.mir.model.country.packet.SM_ReserveKing_Task_Reward;
import com.mmorpg.mir.model.country.packet.SM_ReserveKing_UseCallTogether;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.DateUtils;

/**
 * 储君
 * 
 * @author Kuang Hao
 * @since v1.0 2015-6-26
 * 
 */
public class ReserveKing {
	/** 储君id */
	private volatile long playerId;
	/** 上次使用国民召集时间 */
	private long nextCallTime;
	/** 最后一次离线的时间 */
	private long lastUnlineTime;
	/** 各种类型任务信息 */
	private Map<Integer, ReserveTask> tasks;
	/** 是否已过期 */
	private volatile boolean deprected;
	/** 黑名单 */
	private Set<Long> blackList;
	@Transient
	private Map<Long, Integer> calltogeterCount = new ConcurrentHashMap<Long, Integer>();

	private AtomicInteger callCount = new AtomicInteger();

	// 构造函数
	public static ReserveKing valueOf() {
		ReserveKing result = new ReserveKing();
		result.playerId = 0L;
		result.nextCallTime = 0L;
		result.lastUnlineTime = 0L;
		result.tasks = new NonBlockingHashMap<Integer, ReserveTask>();
		result.init();
		result.deprected = false;
		result.blackList = New.hashSet();
		return result;
	}

	@JsonIgnore
	private void init() {
		for (ReserveTaskEnum taskType : ReserveTaskEnum.values()) {
			ReserveTask reserveTask = ReserveTask.valueOf(taskType);
			tasks.put(taskType.getCode(), reserveTask);
		}
	}

	/**
	 * 检查离线时间
	 * 
	 * @return
	 */
	@JsonIgnore
	public boolean isUnlineTimeOverLimit() {
		Date lastOutTime = new Date(this.lastUnlineTime);
		Date now = new Date();
		Date firstTime = DateUtils.getFirstTime(now);

		Date nextBeginTime = DateUtils.getNextTime(ConfigValueManager.getInstance().RESERVEKING_BEGIN_CRON.getValue(),
				firstTime);
		Date nextEndTime = DateUtils.getNextTime(ConfigValueManager.getInstance().RESERVEKING_END_CRON.getValue(),
				firstTime);
		long cdLimit = DateUtils.MILLIS_PER_SECOND
				* ConfigValueManager.getInstance().RESERVEKING_OFFLINE_TIME.getValue();
		if (now.before(nextBeginTime)) {
			return false;
		} else if (now.after(nextBeginTime) && now.before(nextEndTime)) {
			Date calBeginTime = lastOutTime.before(nextBeginTime) ? nextBeginTime : lastOutTime;
			return now.getTime() - calBeginTime.getTime() > cdLimit;
		} else if (now.after(nextEndTime)) {
			if (lastOutTime.after(nextEndTime)) {
				return false;
			} else if (lastOutTime.before(nextBeginTime)) {
				return nextEndTime.getTime() - nextEndTime.getTime() > cdLimit;
			} else if (lastOutTime.after(nextBeginTime) && lastOutTime.before(nextEndTime)) {
				return nextEndTime.getTime() - lastOutTime.getTime() > cdLimit;
			}
		}
		return false;
	}

	/**
	 * 储君系统失效
	 */
	@JsonIgnore
	synchronized public void deprect() {
		if (!this.deprected && isExistReserveKing()) {
			this.deprected = true;
			for (Map.Entry<Integer, ReserveTask> entry : tasks.entrySet()) {
				Player reserveKing = PlayerManager.getInstance().getPlayer(playerId);
				entry.getValue().rewardMail(reserveKing);
			}

			I18nUtils i18nUtils = I18nUtils.valueOf("10515");
			i18nUtils.addParm("name", I18nPack.valueOf(getReserveKingPlayer().getName()));
			i18nUtils.addParm("country", I18nPack.valueOf(getReserveKingPlayer().getCountry().getName()));
			ChatManager.getInstance().sendSystem(11001, i18nUtils, null);

			I18nUtils i18nUtils2 = I18nUtils.valueOf("307007");
			i18nUtils2.addParm("name", I18nPack.valueOf(getReserveKingPlayer().getName()));
			ChatManager.getInstance().sendSystem(6, i18nUtils2, null, getReserveKingPlayer().getCountry());
			
			EventBusManager.getInstance().submit(ReserveKingRetireEvent.valueOf(this.playerId));
		}
	}

	@JsonIgnore
	public void addCallCount(long playerId) {
		if (calltogeterCount.containsKey(playerId)) {
			calltogeterCount.put(playerId, calltogeterCount.get(playerId) + 1);
		} else {
			calltogeterCount.put(playerId, 1);
		}
	}

	@JsonIgnore
	public int getCallCount(long playerId) {
		if (calltogeterCount.containsKey(playerId)) {
			return calltogeterCount.get(playerId);
		} else {
			return 0;
		}
	}

	@JsonIgnore
	public void useCallTogether(long cd) {
		this.nextCallTime = System.currentTimeMillis() + cd * DateUtils.MILLIS_PER_SECOND;
		Player owner = PlayerManager.getInstance().getPlayer(this.playerId);
		PacketSendUtility.sendPacket(owner, SM_ReserveKing_UseCallTogether.valueOf(this.nextCallTime));
	}

	/**
	 * 成为储君
	 */
	@JsonIgnore
	public void becomeReserveKing(Player player) {
		// 成为储君
		this.playerId = player.getObjectId();
		this.lastUnlineTime = player.getPlayerStat().getLastLogoutTime().getTime();
		EventBusManager.getInstance().submit(BecomeReseveKingEvent.valueOf(this.playerId));
		PacketSendUtility.sendPacket(player, SM_ReserveKing_Become.valueOf(this));
		// 发个广播 10514
		I18nUtils i18nUtils = I18nUtils.valueOf("10514");
		i18nUtils.addParm("name", I18nPack.valueOf(player.getName()));
		i18nUtils.addParm("country", I18nPack.valueOf(player.getCountry().getName()));
		ChatManager.getInstance().sendSystem(11001, i18nUtils, null);

		I18nUtils i18nUtils2 = I18nUtils.valueOf("307006");
		i18nUtils2.addParm("name", I18nPack.valueOf(player.getName()));
		ChatManager.getInstance().sendSystem(6, i18nUtils2, null, player.getCountry());
	}

	/**
	 * 被动退位
	 */
	@JsonIgnore
	synchronized public void passiveAdbicate() {
		Player reserveKingPlayer = getReserveKingPlayer();
		I18nUtils i18nUtils = I18nUtils.valueOf("10515");
		i18nUtils.addParm("name", I18nPack.valueOf(reserveKingPlayer.getName()));
		i18nUtils.addParm("country", I18nPack.valueOf(reserveKingPlayer.getCountry().getName()));
		ChatManager.getInstance().sendSystem(11001, i18nUtils, null);

		I18nUtils i18nUtils2 = I18nUtils.valueOf("307007");
		i18nUtils2.addParm("name", I18nPack.valueOf(reserveKingPlayer.getName()));
		ChatManager.getInstance().sendSystem(6, i18nUtils2, null, reserveKingPlayer.getCountry());

		// 分个邮件
		I18nUtils titel18n = I18nUtils.valueOf(ConfigValueManager.getInstance().RESERVEKING_STEPDOWN_MAIL_TITLE_IL18N
				.getValue());
		I18nUtils contextl18n = I18nUtils
				.valueOf(ConfigValueManager.getInstance().RESERVEKING_STEPDOWN_MAIL_CONTENT_IL18N.getValue());
		contextl18n.addParm("country", I18nPack.valueOf(reserveKingPlayer.getCountry().getName()));
		contextl18n.addParm("new_reverveking_name",
				I18nPack.valueOf(ConfigValueManager.getInstance().RESERVEKING_DEFAULT_NEWRESERVEKING_NAME.getValue()));
		Mail mail = Mail.valueOf(titel18n, contextl18n, null, null);
		MailManager.getInstance().sendMail(mail, reserveKingPlayer.getObjectId());

		EventBusManager.getInstance().submit(ReserveKingAbdicateEvent.valueOf(this.playerId));
		this.blackList.add(this.playerId);
		this.playerId = 0L;
		this.lastUnlineTime = 0L;
	}

	/**
	 * 主动动退位
	 */
	@JsonIgnore
	synchronized public void initiativeAdbicate() {
		Player reserveKingPlayer = getReserveKingPlayer();
		I18nUtils i18nUtils = I18nUtils.valueOf("10516");
		i18nUtils.addParm("name", I18nPack.valueOf(reserveKingPlayer.getName()));
		i18nUtils.addParm("country", I18nPack.valueOf(reserveKingPlayer.getCountry().getName()));
		ChatManager.getInstance().sendSystem(11001, i18nUtils, null);

		I18nUtils i18nUtils2 = I18nUtils.valueOf("307008");
		i18nUtils2.addParm("name", I18nPack.valueOf(reserveKingPlayer.getName()));
		ChatManager.getInstance().sendSystem(6, i18nUtils2, null, reserveKingPlayer.getCountry());

		EventBusManager.getInstance().submit(ReserveKingAbdicateEvent.valueOf(this.playerId));
		this.playerId = 0L;
		this.lastUnlineTime = 0L;
	}

	/**
	 * 领取任务奖励
	 * 
	 * @param taskType
	 */
	@JsonIgnore
	synchronized public void rewardTask(ReserveTaskEnum taskType) {
		Player reserveKing = getReserveKingPlayer();
		ReserveTask task = this.tasks.get(taskType.getCode());
		int finishCount = task.getFinishCount();
		if (finishCount >= ConfigValueManager.getInstance().getTaskRequestCount(taskType)) {
			task.reward(reserveKing);
			PacketSendUtility.sendPacket(reserveKing,
					SM_ReserveKing_Task_Reward.valueOf(taskType.getCode(), task.getFinishCount()));
		} else {
			PacketSendUtility.sendErrorMessage(reserveKing);
		}
	}

	@JsonIgnore
	public boolean isReserveKing(long targetPlayerId) {
		return playerId == targetPlayerId;
	}
	
	@JsonIgnore
	public boolean isRealReserveKing(long targetPlayerId) {
		return isReserveKing(targetPlayerId) && !deprected;
	}

	/**
	 * 添加任务完成次数
	 * 
	 * @param taskType
	 *            任务类型
	 * @return
	 */
	@JsonIgnore
	public void addTaskCount(ReserveTaskEnum taskType) {
		ReserveTask task = tasks.get(taskType.getCode());
		task.addFinishCount();
		PacketSendUtility.sendPacket(getReserveKingPlayer(),
				SM_ReserveKing_Finish_Task.valueOf(taskType.getCode(), task.getFinishCount()));
	}

	/**
	 * 当前是否有储君
	 * 
	 * @return
	 */
	@JsonIgnore
	public boolean isExistReserveKing() {
		return this.playerId != 0L;
	}

	@JsonIgnore
	public Player getReserveKingPlayer() {
		Player player = PlayerManager.getInstance().getPlayer(this.playerId);
		return player;
	}

	// getter- setter
	public long getPlayerId() {
		return playerId;
	}

	public long getNextCallTime() {
		return nextCallTime;
	}

	public void setNextCallTime(long nextCallTime) {
		this.nextCallTime = nextCallTime;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getLastUnlineTime() {
		return lastUnlineTime;
	}

	public void setLastUnlineTime(long lastUnlineTime) {
		this.lastUnlineTime = lastUnlineTime;
	}

	public Map<Integer, ReserveTask> getTasks() {
		return tasks;
	}

	public void setTasks(Map<Integer, ReserveTask> tasks) {
		this.tasks = tasks;
	}

	public boolean isDeprected() {
		return deprected;
	}

	public void setDeprected(boolean deprected) {
		this.deprected = deprected;
	}

	public Set<Long> getBlackList() {
		return blackList;
	}

	public void setBlackList(Set<Long> blackList) {
		this.blackList = blackList;
	}

	@JsonIgnore
	public Map<Long, Integer> getCalltogeterCount() {
		return calltogeterCount;
	}

	@JsonIgnore
	public void setCalltogeterCount(Map<Long, Integer> calltogeterCount) {
		this.calltogeterCount = calltogeterCount;
	}

	public AtomicInteger getCallCount() {
		return callCount;
	}

	public void setCallCount(AtomicInteger callCount) {
		this.callCount = callCount;
	}
}
