package com.mmorpg.mir.model.country.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.controllers.PlayerController;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.countryact.HiddenMissionType;
import com.mmorpg.mir.model.country.packet.SM_Country_War_Reward;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.CountryNpc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.StatusNpc;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.New;

@Component
public class HiddenMissionServiceImpl implements HiddenMissionService {

	@Autowired
	private PlayerManager playerManager;

	public void protectCountryFlag(Player killer, Player killed) {
		CountryNpc flag = killer.getCountry().getCountryFlag().getCountryNpc();
		boolean isKillerFlag = killer.getCountryValue() == flag.getCountryValue();
		boolean isNearFlag = MathUtil.isInRange(flag, killer, flag.getWarnrange(), flag.getWarnrange());

		if (isKillerFlag && isNearFlag) {
			rewardHiddenMission(HiddenMissionType.DEFEND_FLAG, getAssistPlayers(killer, killed));
		}
	}

	public void protectDiplomacy(Player killer, Player killed) {
		CountryNpc diplomacy = killer.getCountry().getDiplomacy().getCountryNpc();
		if (diplomacy == null) {
			return;
		}
		boolean isKillerFlag = killer.getCountryValue() == diplomacy.getCountryValue();
		boolean isNearFlag = MathUtil.isInRange(diplomacy, killer, diplomacy.getWarnrange(), diplomacy.getWarnrange());

		if (isKillerFlag && isNearFlag) {
			rewardHiddenMission(HiddenMissionType.DEFEND_DIPOMACY, getAssistPlayers(killer, killed));
		}
	}

	public void robBrickMan(Player killer, Player killed) {
		StatusNpc temple = killer.getCountry().getTemple().getStatusNpc();
		boolean isNearTemple = MathUtil.isInRange(temple, killer, temple.getWarnrange(), temple.getWarnrange());
		boolean isBrickMan = killed.getTempleHistory().getCurrentBrick() != null;

		if (isNearTemple || isBrickMan) {
			rewardHiddenMission(HiddenMissionType.ROB_BRICK_MAN, getAssistPlayers(killer, killed));
		}
	}

	public void robLorryByGroup(Player player) {
		rewardHiddenMission(HiddenMissionType.ROB_LORRY_GROUP, Arrays.asList(player));
	}

	public void robLorryByIndividual(Player player) {
		rewardHiddenMission(HiddenMissionType.ROB_LORRY_ALONE, Arrays.asList(player));
	}

	private void rewardHiddenMission(HiddenMissionType type, Collection<Player> awardPlayers) {
		String rewardId = ConfigValueManager.getInstance().HIDDEN_MISSION_REWARDID.getValue().get(type.name());
		for (Player p : awardPlayers) {
			if (p.getPlayerCountryHistory().takeMission(type)) {
				Map<String, Object> map = New.hashMap();
				map.put("LEVEL", p.getLevel());
				map.put("STANDARD_EXP", playerManager.getStandardExp(p));
				Reward reward = null;
				if (type == HiddenMissionType.DEFEND_DIPOMACY) {
					List<String> rewardIds = ChooserManager.getInstance().chooseValueByRequire(p, rewardId);
					reward = RewardManager.getInstance().creatReward(p, rewardIds, map);
				} else {
					reward = RewardManager.getInstance().creatReward(p, rewardId, map);
				}
				RewardManager.getInstance().grantReward(p, reward,
						ModuleInfo.valueOf(ModuleType.HIDDEN_MISSION, SubModuleType.COUNTRY_WAR_REWARD, type.name()));
				int leftCount = CountryManager.getInstance().getHiddenMissionLeftCount(p, type.getValue());
				PacketSendUtility.sendPacket(p,
						SM_Country_War_Reward.valueOf(p.getCountryValue(), type.getValue(), leftCount, reward));
			}
		}
	}

	private Collection<Player> getAssistPlayers(Player killer, Player killed) {
		long currentKey = System.currentTimeMillis() / DateUtils.MILLIS_PER_SECOND;
		long startKey = currentKey - PlayerController.STORAGE_SIZE;
		long tmpSt = startKey;
		// 拿玩家最近的伤害列表，合并助攻的玩家
		Map<Long, Set<Player>> damageHistory = killed.getController().getDamageHistory();
		Set<Player> assistants = new HashSet<Player>();
		while (tmpSt <= currentKey) {
			Set<Player> players = damageHistory.get(tmpSt);
			if (players != null) {
				for (Player attackers : players) {
					if (attackers.getCountryValue() == killer.getCountryValue()) {
						assistants.add(attackers);
					}
				}
			}
			tmpSt += 1;
		}
		assistants.add(killer);
		return assistants;
	}
}
