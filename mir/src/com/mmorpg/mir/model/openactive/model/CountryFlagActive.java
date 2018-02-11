package com.mmorpg.mir.model.openactive.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.openactive.OpenActiveConfig;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;

public class CountryFlagActive {
	/** 国旗争夺完成的次数 */
	private int count;

	public static CountryFlagActive valueOf() {
		CountryFlagActive result = new CountryFlagActive();
		result.count = 0;
		return result;
	}

	@JsonIgnore
	public void addCount(Player owner) {
		if (count == OpenActiveConfig.getInstance().COUNTRYFLAG_FINISH_COUNT.getValue()) {
			return;
		}
		count++;
		if (count == OpenActiveConfig.getInstance().COUNTRYFLAG_FINISH_COUNT.getValue()) {
			I18nUtils titel18n = I18nUtils.valueOf(OpenActiveConfig.getInstance().COUNTRYFLAG_REWARD_MAIL_TITLE_I18N
					.getValue());
			I18nUtils contextl18n = I18nUtils
					.valueOf(OpenActiveConfig.getInstance().COUNTRYFLAG_REWARD_MAIL_CONTENT_18N.getValue());
			Reward reward = RewardManager.getInstance().creatReward(owner,
					OpenActiveConfig.getInstance().COUNTRYFLAG_REWARDID.getValue(), null);
			Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
			MailManager.getInstance().sendMail(mail, owner.getObjectId());
		}
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
