package com.mmorpg.mir.model.skill.facade;

import java.util.List;

import org.apache.log4j.Logger;
import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.controllers.effect.PlayerEffectController;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.military.event.MilitaryRankUpEvent;
import com.mmorpg.mir.model.player.event.LevelUpEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.quest.event.QuestCompletEvent;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.skill.effecttemplate.ExerciseEffect;
import com.mmorpg.mir.model.skill.manager.SkillManager;
import com.mmorpg.mir.model.skill.model.SkillEntry;
import com.mmorpg.mir.model.skill.packet.CM_Attack;
import com.mmorpg.mir.model.skill.packet.CM_Buff_Accumulate;
import com.mmorpg.mir.model.skill.packet.CM_Learn_PassiveSkill;
import com.mmorpg.mir.model.skill.packet.CM_Skill_LevelUp;
import com.mmorpg.mir.model.skill.packet.CM_UseSkill;
import com.mmorpg.mir.model.skill.packet.SM_Buff_Accumulate;
import com.mmorpg.mir.model.skill.packet.SM_Skill_Common;
import com.mmorpg.mir.model.skill.packet.SM_UseSkill;
import com.mmorpg.mir.model.skill.resource.SkillResource;
import com.mmorpg.mir.model.skill.service.SkillService;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.SessionUtil;
import com.windforce.common.event.anno.ReceiverAnno;
import com.windforce.common.resource.anno.Static;
import com.xiaosan.dispatcher.anno.HandlerAnno;
import com.xiaosan.socket.core.TSession;

@Component
public final class SkillFacade {

	private static Logger logger = Logger.getLogger(SkillFacade.class);

	@Autowired
	private SkillService skillService;

	@Autowired
	private SkillManager skillManager;

	@Autowired
	private PlayerManager playerManager;

	/**
	 * 技能重置返还邮件title
	 */
	@Static("SKILL:SKILL_RESET_MAIL_TITLE")
	private ConfigValue<String> SKILL_RESET_MAIL_TITLE;

	/**
	 * 技能重置返还邮件context
	 */
	@Static("SKILL:SKILL_RESET_MAIL_CONTEXT")
	private ConfigValue<String> SKILL_RESET_MAIL_CONTEXT;

	@HandlerAnno
	public SM_UseSkill useSkill(TSession session, CM_UseSkill req) {
		SM_UseSkill res = new SM_UseSkill();
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			if (player != null) {
				skillService.useSkill(player, req.getSkillId(), req.getTargetId(), req.getX(), req.getY(),
						req.getTargetList(), req.getDirction());
				res.setSkillId(req.getSkillId());
				res.setNextTime(player.getNextSkillTime());
			}
		} catch (ManagedException e) {
			res.setCode(e.getCode());
		} catch (Exception e) {
			logger.error("玩家使用技能异常", e);
			res.setCode(ManagedErrorCode.SYS_ERROR);
		}
		return res;
	}

	@HandlerAnno
	public void attack(TSession session, CM_Attack req) {
		try {
			Player player = SessionUtil.getPlayerBySession(session);
			if (player != null)
				skillService.attack(player, req.getTargetId());
		} catch (ManagedException e) {
			// 这里应该返回一个通用错误信息
		} catch (Exception e) {
			logger.error("玩家攻击异常", e);
		}
	}

	@HandlerAnno
	public void skillLevelUp(TSession session, CM_Skill_LevelUp req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			if (player != null)
				skillService.skillLevelUp(player, req.getSkillId());
		} catch (ManagedException e) {
			// 这里应该返回一个通用错误信息
			PacketSendUtility.sendPacket(player, SM_Skill_Common.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("玩家技能升级异常", e);
		}
	}

	@HandlerAnno
	public void learnPassiveSkill(TSession session, CM_Learn_PassiveSkill req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			skillService.learnPassiveSkill(player, req.getSkillId());
		} catch (ManagedException e) {
			// 这里应该返回一个通用错误信息
			PacketSendUtility.sendPacket(player, SM_Skill_Common.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("玩家技能升级异常", e);
		}
	}

	@HandlerAnno
	public void getLastBuffAccumulate(TSession session, CM_Buff_Accumulate req) {
		Player player = SessionUtil.getPlayerBySession(session);
		try {
			SM_Buff_Accumulate sm = new SM_Buff_Accumulate();
			sm.setResult(((PlayerEffectController) player.getEffectController())
					.getAndDelete(ExerciseEffect.EXERCISE_EFFECT));
			PacketSendUtility.sendPacket(player, sm);
		} catch (ManagedException e) {
			// 这里应该返回一个通用错误信息
			PacketSendUtility.sendPacket(player, SM_Skill_Common.valueOf(e.getCode()));
		} catch (Exception e) {
			logger.error("玩家技能升级异常", e);
			PacketSendUtility.sendErrorMessage(player);
		}
	}

	@ReceiverAnno
	public void levelUpEvent(LevelUpEvent levelUpEvent) {
		Player player = playerManager.getPlayer(levelUpEvent.getOwner());
		List<Integer> skillIds = skillManager.getAcceptEventSkills().get(levelUpEvent.getClass());
		if (skillIds != null) {
			player.getSkillList().learnSkill(skillIds.toArray(new Integer[skillIds.size()]));
		}
	}

	@ReceiverAnno
	public void militaryRankUpEvent(MilitaryRankUpEvent militaryRankUpEvent) {
		Player player = playerManager.getPlayer(militaryRankUpEvent.getOwner());
		List<Integer> skillIds = skillManager.getAcceptEventSkills().get(militaryRankUpEvent.getClass());
		if (skillIds != null) {
			player.getSkillList().learnSkill(skillIds.toArray(new Integer[skillIds.size()]));
		}
	}

	@ReceiverAnno
	public void levelUpEvent(QuestCompletEvent questComplete) {
		Player player = playerManager.getPlayer(questComplete.getOwner());
		List<Integer> skillIds = skillManager.getAcceptEventSkills().get(questComplete.getClass());
		if (skillIds != null) {
			player.getSkillList().learnSkill(skillIds.toArray(new Integer[skillIds.size()]));
		}
	}

	@ReceiverAnno
	public void promotedStage(PromotionEvent questComplete) {
		Player player = playerManager.getPlayer(questComplete.getOwner());
		List<Integer> skillIds = skillManager.getAcceptEventSkills().get(questComplete.getClass());
		if (skillIds != null) {
			player.getSkillList().learnSkill(skillIds.toArray(new Integer[skillIds.size()]));
		}
	}

	@ReceiverAnno
	public void loginEvent(LoginEvent loginEvent) {
		Player player = playerManager.getPlayer(loginEvent.getOwner());

		if (!player.getSkillList().isNotReset()) {
			int sumQi = 0;
			for (SkillResource skillResource : skillManager.getSkillResources().getAll()) {
				if (skillResource.isResetRemove()) {
					SkillEntry skillEntry = player.getSkillList().getSkills().get(skillResource.getSkillId());
					if (skillEntry != null) {
						sumQi += skillEntry.getResource().getResetQi()[skillEntry.getLevel() - 1];
						player.getSkillList().getSkills().remove(skillEntry.getId());
					}
				}
			}
			if (sumQi != 0) {
				Reward reward = Reward.valueOf();
				reward.addCurrency(CurrencyType.QI, sumQi);
				I18nUtils titel18n = I18nUtils.valueOf(SKILL_RESET_MAIL_TITLE.getValue());
				I18nUtils contextl18n = I18nUtils.valueOf(SKILL_RESET_MAIL_CONTEXT.getValue());
				Mail mail = Mail.valueOf(titel18n, contextl18n, null, reward);
				MailManager.getInstance().sendMail(mail, player.getObjectId());
			}

			player.getPlayerEnt().setAutoBattle(null);
			player.getPlayerEnt().setKeyboards(null);

			player.getSkillList().setNotReset(true);
		}
		List<Integer> skillIds = New.arrayList();
		for (SkillResource skillResource : skillManager.getSkillResources().getAll()) {
			skillIds.add(skillResource.getSkillId());
		}
		if (skillIds != null) {
			player.getSkillList().learnSkill(skillIds.toArray(new Integer[skillIds.size()]));
		}
	}
}
