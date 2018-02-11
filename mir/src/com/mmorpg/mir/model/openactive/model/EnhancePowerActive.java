package com.mmorpg.mir.model.openactive.model;

import java.util.HashSet;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.openactive.OpenActiveConfig;
import com.mmorpg.mir.model.openactive.resource.OpenActiveEnhanceResource;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.windforce.common.utility.New;

public class EnhancePowerActive {
	
	private int activePower;
	
	private HashSet<String> rewarded = New.hashSet();
	
	@JsonIgnore
	public void sendNotRewardMail(Player player) {
		for (OpenActiveEnhanceResource resource : OpenActiveConfig.getInstance().enhancePowerActiveStorage.getAll()) {
			if (resource.getMailConditions().verify(player) && !rewarded.contains(resource.getId())) {
				List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player, resource.getRewardChooserId());
				Reward reward = RewardManager.getInstance().creatReward(player, rewardIds, null);
				Mail mail = Mail.valueOf(I18nUtils.valueOf(resource.getMailTitle()),
						I18nUtils.valueOf(resource.getMailContent()), null, reward);
				MailManager.getInstance().sendMail(mail, player.getObjectId());
				rewarded.add(resource.getId());
			}
		}
	}

	public static EnhancePowerActive valueOf() {
		EnhancePowerActive result = new EnhancePowerActive();
		result.rewarded = New.hashSet();
		return result;
	}

	public HashSet<String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(HashSet<String> rewarded) {
		this.rewarded = rewarded;
	}

	public int getActivePower() {
		return activePower;
	}

	public void setActivePower(int activePower) {
		this.activePower = activePower;
	}

	@JsonIgnore
	public void updatePower(int activePower) {
		this.activePower = activePower;
	}
}
