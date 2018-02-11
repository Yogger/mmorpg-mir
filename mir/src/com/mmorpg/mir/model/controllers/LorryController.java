package com.mmorpg.mir.model.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.mmorpg.mir.log.LogManager;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chat.model.handle.ChannelPrivate;
import com.mmorpg.mir.model.express.manager.ExpressManager;
import com.mmorpg.mir.model.express.packet.SM_Express_Rob;
import com.mmorpg.mir.model.express.packet.SM_Lorry_AttackTime;
import com.mmorpg.mir.model.express.packet.SM_Lorry_MapInfo;
import com.mmorpg.mir.model.express.packet.SM_My_Rob_Count;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Lorry;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.mail.manager.MailManager;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.model.DamageResult;
import com.mmorpg.mir.model.skill.model.EffectVO;
import com.mmorpg.mir.model.skill.packet.SM_UseSkill_End;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.World;
import com.windforce.common.utility.DateUtils;
import com.windforce.common.utility.New;

public class LorryController extends NpcController {

	@Override
	public Lorry getOwner() {
		return (Lorry) super.getOwner();
	}

	@Override
	public void onDie(Creature lastAttacker, int skillId) {
		// 掉落归属？
		final Lorry lorry = getOwner();
		Player lorryOwner = lorry.getOwner();
		Player lastAttack = null;

		if (lastAttacker instanceof Player) {
			lastAttack = (Player) lastAttacker;
		} else if (lastAttacker instanceof Summon) {
			lastAttack = ((Summon) lastAttacker).getMaster();
		}
		// 剧情副本怪物击杀
		if (lastAttack == null) {
			return;
		}
		lorry.setRob(true);
		lorry.getEffectController().setAbnormal(EffectId.GOD.getEffectId());// /
																			// 前端还是能继续攻击，rob都是true，还攻击..
		lorryOwner.getExpress().setBeenRob(true);
		lorry.setLastOnAttackTime(0L);

		Reward robReward = ExpressManager.getInstance().createRobExpressReward(lastAttack, lorry);

		Collection<Player> mostDamagePlayers = lorry.getMostDamagePlayers();
		SpawnManager.getInstance().spawnDropObject(lastAttack, lastAttack, lorry, mostDamagePlayers);
		SM_Express_Rob sm = SM_Express_Rob.valueOf(lorry.getObjectId());
		PacketSendUtility.broadcastPacket(lorry, sm);
		PacketSendUtility.sendPacket(lorryOwner, sm);
		for (Player p : mostDamagePlayers) {
			int currentRobCount = p.getExpress().addRobCount();
			if (currentRobCount > ExpressManager.getInstance().getRobRewardCountLimit()) {
				PacketSendUtility.sendPacket(p, SM_My_Rob_Count.valueOf(p.getExpress().getRobCount().get(), null));
			} else {
				// 这里改成邮件发送
				boolean newTech = ExpressManager.getInstance().getRobTechCondition().verify(p);
				I18nUtils titel18n = I18nUtils
						.valueOf(newTech ? ExpressManager.getInstance().ROB_LORRY_REWARD_MAIL_TITLE_TECH.getValue()
								: ExpressManager.getInstance().ROB_LORRY_REWARD_MAIL_TITLE.getValue());
				I18nUtils contextl18n = I18nUtils
						.valueOf(newTech ? ExpressManager.getInstance().ROB_LORRY_REWARD_MAIL_CONTENT_TECH.getValue()
								: ExpressManager.getInstance().ROB_LORRY_REWARD_MAIL_CONTENT.getValue());
				contextl18n.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(lorryOwner.getName()));
				contextl18n.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(lorryOwner.getCountry().getName()));
				contextl18n.addParm("time", I18nPack.valueOf(DateUtils.date2String(new Date(), "HH:mm")));
				contextl18n.addParm("map",
						I18nPack.valueOf(World.getInstance().getMapResource(lorry.getMapId()).getName()));
				contextl18n
						.addParm(I18NparamKey.LORRY_COLOR,
								I18nPack.valueOf(ExpressManager.getInstance().LORRY_COLOR_NAME.getValue()[lorry
										.getColor() - 1]));
				Mail mail = Mail.valueOf(titel18n, contextl18n, null, robReward);
				MailManager.getInstance().sendMail(mail, p.getObjectId());

				PacketSendUtility.sendPacket(p, SM_My_Rob_Count.valueOf(p.getExpress().getRobCount().get(), robReward));
				LogManager.robExpress(p.getObjectId(), p.getPlayerEnt().getServer(), p.getPlayerEnt().getAccountName(),
						p.getName(), System.currentTimeMillis(), lorry.getExpressId(), lorry.getOwner().getObjectId(),
						currentRobCount);
			}
			// EventBusManager.getInstance().submit(ExpressBeenRobEvent.valueOf(p.getObjectId(),
			// lorry.getCountryValue()));
		}
		lorry.getLifeStats().setCurrentHp(1);
		ExpressManager.getInstance().unRegisterLorry(lorry);

		// 公告
		I18nUtils utils = I18nUtils
				.valueOf("30102")
				.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(lastAttack.getName()))
				.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(lastAttack.getCountry().getName()))
				.addParm(I18NparamKey.PLAYERNAME_SECOND, I18nPack.valueOf(lorryOwner.getName()))
				.addParm(I18NparamKey.COUNTRY_SECOND, I18nPack.valueOf(lorryOwner.getCountry().getName()))
				.addParm(
						I18NparamKey.LORRY_COLOR,
						I18nPack.valueOf(ExpressManager.getInstance().LORRY_COLOR_NAME.getValue()[lorry.getColor() - 1]));
		ChatManager.getInstance().sendSystem(11001, utils, null);

		// utils = I18nUtils
		// .valueOf("303007")
		// .addParm(I18NparamKey.PLAYERNAME,
		// I18nPack.valueOf(lastAttack.getName()))
		// .addParm(I18NparamKey.COUNTRY,
		// I18nPack.valueOf(lastAttack.getCountry().getName()))
		// .addParm(I18NparamKey.PLAYERNAME_SECOND,
		// I18nPack.valueOf(lorryOwner.getName()))
		// .addParm(I18NparamKey.COUNTRY_SECOND,
		// I18nPack.valueOf(lorryOwner.getCountry().getName()))
		// .addParm(
		// I18NparamKey.LORRY_COLOR,
		// I18nPack.valueOf(ExpressManager.getInstance().LORRY_COLOR_NAME.getValue()[lorry.getColor()
		// - 1]));
		// ChatManager.getInstance().sendSystem(0, utils, null);
	}

	@Override
	public void see(VisibleObject object) {
		super.see(object);
	}

	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange) {
		super.notSee(object, isOutOfRange);
		if (object == getOwner().getOwner()) {
			getOwner().faraway(false);
		}
	}

	@Override
	public void onAttack(Creature creature, int skillId, long damage, DamageResult damageResult) {
		if (getOwner().isRob() || getOwner().isGod()) {
			return;
		}
		damage += techAttackAddition(creature, skillId);
		super.onAttack(creature, skillId, damage, damageResult);
		long now = System.currentTimeMillis();
		boolean duration = getOwner().getLastOnAttackTime() + ExpressManager.getInstance().getOutOfOnAttackTime() >= now;
		if (getOwner().getLastOnAttackTime() == 0L || !duration) { // 第一次被攻击
			PacketSendUtility.sendPacket(getOwner().getOwner(), SM_Lorry_AttackTime.valueOf(now));
			ChatManager.getInstance().sendSystem(81007, I18nUtils.valueOf("801001"), null,
					ChannelPrivate.SYSTEM_SENDER, getOwner().getOwner().getObjectId());
		}
		getOwner().setLastOnAttackTime(now);
	}

	private long techAttackAddition(Creature creature, int skillId) {
		long extraDamage = 0L;
		if (creature instanceof Player) {
			Player attacker = (Player) creature;
			if (ExpressManager.getInstance().getRobTechCondition().verify(attacker)) {
				extraDamage = (long) (getOwner().getLifeStats().getMaxHp() * ExpressManager.getInstance()
						.getExtraDamage());
				SM_UseSkill_End sm = new SM_UseSkill_End();
				sm.setEffector(creature.getObjectId());
				sm.setSkillId((short) skillId);
				ArrayList<EffectVO> list = New.arrayList();
				EffectVO effEffectVO = new EffectVO();
				effEffectVO.setTargetId(getOwner().getObjectId());
				effEffectVO.setHpDamage(extraDamage);
				list.add(effEffectVO);
				sm.setEffectList(list);
				PacketSendUtility.sendPacket(getOwner().getOwner(), sm);
				PacketSendUtility.sendPacket(creature, sm);
			}
		}
		return extraDamage;
	}

	@Override
	public void onDespawn() {
		if (getOwner().getOwner() != null) {
			getOwner().getOwner().updateKnownlist();
		}
	}

	@Override
	public void delete() {
		super.delete();
	}

	@Override
	public void onSpawn(int mapId, int instanceId) {
		PacketSendUtility.sendPacket(getOwner().getOwner(), SM_Lorry_MapInfo.valueOf(getOwner()));
		super.onSpawn(mapId, instanceId);
	}
}
