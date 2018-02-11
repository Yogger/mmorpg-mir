package com.mmorpg.mir.model.trigger.model.handle;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.trigger.model.AbstractTrigger;
import com.mmorpg.mir.model.trigger.model.TriggerContextKey;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.mmorpg.mir.model.trigger.resource.TriggerResource;

@Component
public class MailTrigger extends AbstractTrigger {

	@Override
	public TriggerType getType() {
		return TriggerType.MAIL;
	}

	@Override
	public void handle(Map<String, Object> contexts, final TriggerResource resource) {
		final Player player = (Player) contexts.get(TriggerContextKey.PLAYER);
		String title = resource.getKeys().get(TriggerContextKey.MAIL_TITLE_ID);
		String content = resource.getKeys().get(TriggerContextKey.MAIL_CONTENT_ID);
		String chooserGroupId = resource.getKeys().get(TriggerContextKey.MAIL_REWARD_CHOOSERGROUP_ID);
		Reward reward = null;
		if (chooserGroupId != null) {
			List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(player, chooserGroupId);
			reward = RewardManager.getInstance().creatReward(player, rewardIds, null);
		}
		if (contexts.containsKey(TriggerContextKey.MAIL_REWARD)) {
			Reward mailReward = (Reward) contexts.get(TriggerContextKey.MAIL_REWARD);
			if (reward == null) {
				reward = mailReward;
			} else {
				reward.addReward(mailReward);
			}
		}
		I18nUtils titel18n = I18nUtils.valueOf(title);
		I18nUtils contextl18n = I18nUtils.valueOf(content);
		Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
		MailManager.getInstance().sendMail(mail, player.getObjectId());
	}
}
