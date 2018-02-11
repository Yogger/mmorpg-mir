package com.mmorpg.mir.model.horse.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.horse.manager.HorseManager;
import com.mmorpg.mir.model.horse.packet.SM_Not_Finish_Acitve;
import com.mmorpg.mir.model.horse.packet.SM_RideBroadcast;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.kingofwar.config.KingOfWarConfig;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;

/**
 * 坐骑外观
 * 
 * @author 37.com
 * 
 */
public class HorseAppearance {

	/** 外观id */
	private int currentAppearance;

	/** 失效时间 */
	private long overTime;

	/** 是否永久激活 */
	private boolean foreverActive;

	/** 完成激活 */
	private boolean finishActive;

	/** 构造函数 */
	public static HorseAppearance valueOf(int currentAppearance) {
		HorseAppearance appearance = new HorseAppearance();
		appearance.currentAppearance = currentAppearance;
		appearance.finishActive = false;
		return appearance;
	}

	// 业务方法
	@JsonIgnore
	public boolean refreshDeprecated(Player player) {
		if (this.finishActive && isDeprecated()) {
			deprecated(player);
			return true;
		}
		return false;
	}

	/**
	 * 过期
	 * 
	 * @param player
	 */
	@JsonIgnore
	public void deprecated(Player player) {
		if (isDeprecated()) {
			if (this.currentAppearance > 0) {
				this.currentAppearance = 0;
				player.getGameStats().replaceModifiers(Horse.GAME_STATE_ID, player.getHorse().getStat(), true);
				if (player.isRide()) {
					PacketSendUtility.broadcastPacket(player, SM_RideBroadcast.valueOf(player), true);
				}
			}
			this.finishActive = false;

			// 过期邮件
			I18nUtils titel18n = I18nUtils.valueOf(HorseManager.getInstance().HORSE_ILLUTION_DEPRECATE_MAIL_TITLE
					.getValue());
			I18nUtils contextl18n = I18nUtils.valueOf(HorseManager.getInstance().HORSE_ILLUTION_DEPRECATE_MAIL_CONTENT
					.getValue());
			Mail mail = Mail.valueOf(titel18n, contextl18n, null, null);
			MailManager.getInstance().sendMail(mail, player.getObjectId());
		}
	}

	/**
	 * 激活幻化
	 * 
	 * @param player
	 * @param foreverActive
	 * @param overTime
	 */
	@JsonIgnore
	public void active(Player player, boolean foreverActive, long overTime) {
		this.foreverActive = foreverActive;
		this.overTime = overTime;
		this.finishActive = true;
		this.useIllution(player);
	}

	/**
	 * 是否失效
	 * 
	 * @return
	 */
	@JsonIgnore
	public boolean isDeprecated() {
		if (true == foreverActive) {
			return false;
		}
		if (System.currentTimeMillis() - overTime <= 0L) {
			return false;
		}
		return true;
	}

	/**
	 * 取消幻化
	 */
	@JsonIgnore
	public void notUseIllution(Player player) {
		if (this.currentAppearance > 0) {
			this.currentAppearance = 0;
			player.getGameStats().replaceModifiers(Horse.GAME_STATE_ID, player.getHorse().getStat(), true);
			if (player.isRide()) {
				PacketSendUtility.broadcastPacket(player, SM_RideBroadcast.valueOf(player), true);
			}
		}
	}

	@JsonIgnore
	public void useIllution(Player player) {
		if (!isDeprecated() && currentAppearance == 0 && finishActive == true) {
			this.currentAppearance = KingOfWarConfig.getInstance().KOW_DRAGON_ID.getValue();
		}
		player.getGameStats().replaceModifiers(Horse.GAME_STATE_ID, player.getHorse().getStat(), true);
		if (player.isRide()) {
			PacketSendUtility.broadcastPacket(player, SM_RideBroadcast.valueOf(player), true);
		}
	}

	@JsonIgnore
	public void sendNotFinishActive(Player player) {
		if (true == this.finishActive) {
			return;
		}
		if (ModuleOpenManager.getInstance().isOpenByModuleKey(player, ModuleKey.HORSE)) {
			this.overTime = KingOfWarManager.getInstance().getNextKingOfWarTime();
			PacketSendUtility.sendPacket(player, SM_Not_Finish_Acitve.valueOf(this.overTime));
		}
	}

	// getter -setter
	public int getCurrentAppearance() {
		return currentAppearance;
	}

	public void setCurrentAppearance(int currentAppearance) {
		this.currentAppearance = currentAppearance;
	}

	public long getOvertime() {
		return overTime;
	}

	public void setOvertime(long overTime) {
		this.overTime = overTime;
	}

	public boolean isFinishActive() {
		return finishActive;
	}

	public boolean isForeverActive() {
		return foreverActive;
	}

	public void setForeverActive(boolean foreverActive) {
		this.foreverActive = foreverActive;
	}

	public void setFinishActive(boolean finishActive) {
		this.finishActive = finishActive;
	}

}
