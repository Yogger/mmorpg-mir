package com.mmorpg.mir.model.restrictions;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.complexstate.ComplexStateType;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.group.model.PlayerGroup;
import com.mmorpg.mir.model.group.packet.SM_GROUP_COMMON;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class PlayerRestrictions {

	public static PlayerRestrictions getInstance() {
		return Instance.playerRestrictions;
	}

	static class Instance {
		public static PlayerRestrictions playerRestrictions = new PlayerRestrictions();
	}

	@Override
	public boolean isRestricted(Player player, Class<? extends IRestrictions> callingRestriction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canAttack(Player player, VisibleObject target) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canAffectBySkill(Player player, VisibleObject target) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canUseSkill(Player player, Skill skill) {

		if (player.getEffectController().isAbnoramlSet(EffectId.STUN)) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.PLAYER_STUN);
			return false;
		}

		if (player.getEffectController().isAbnoramlSet(EffectId.SILENCE)) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.PLAYER_SILENCE);
			return false;
		}

		if (player.isSkillDisabled(skill.getSkillTemplate().getSkillId())) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SKILL_CD);
			return false;
		}
		if (player.isPublicDisabled(skill.getSkillTemplate().getPublicCoolDownGroup())) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.SKILL_CD);
			return false;
		}
		return true;
	}

	@Override
	public boolean canChat(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canInviteToGroup(Player player, Player target) {
		final PlayerGroup group = player.getPlayerGroup();

		if (group != null && group.isFull()) {
			PacketSendUtility.sendPacket(player, new SM_GROUP_COMMON(ManagedErrorCode.FULL_GROUP));
			return false;
		} else if (target == null) {
			PacketSendUtility.sendPacket(player, new SM_GROUP_COMMON(ManagedErrorCode.INVITED_PLAYER_OFFLINE));
			return false;
		} else if (target.getObjectId() == player.getObjectId()) {
			PacketSendUtility.sendPacket(player, new SM_GROUP_COMMON(ManagedErrorCode.ERROR_MSG));
			return false;
		} else if (target.getLifeStats().isAlreadyDead()) {
			PacketSendUtility.sendPacket(player, new SM_GROUP_COMMON(ManagedErrorCode.SELECTED_TARGET_DEAD));
			return false;
		} else if (target.getComplexState().isState(ComplexStateType.GROUP)) {
			PacketSendUtility.sendPacket(player, new SM_GROUP_COMMON(ManagedErrorCode.TARGET_DISBAND_GROUP_REQUEST));
			return false;
		} else if (player.getLifeStats().isAlreadyDead()) {
			PacketSendUtility.sendPacket(player, new SM_GROUP_COMMON(ManagedErrorCode.CANNOT_INVITE_BECAUSE_YOU_DEAD));
			return false;
		} else if (target.getPlayerGroup() != null && target.getPlayerGroup().equals(group)) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.ALREADY_IN_SAME_GROUP);
			return false;
		} else if (target.getCountryValue() != player.getCountryValue()) {
			PacketSendUtility.sendErrorMessage(player, ManagedErrorCode.COUNTRY_NOT_SAME);
			return false;
		}
		// 在其他队伍
		// if (target.isInGroup()) {
		// PacketSendUtility.sendPacket(player, new
		// SM_GROUP_COMMON(ManagedErrorCode.PLAYER_IN_ANOTHER_GROUP));
		// return false;
		// }
		return true;
	}

	@Override
	public boolean canInviteToAlliance(Player player, Player target) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canChangeEquip(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canUseWarehouse(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canTrade(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canUseItem(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

}
