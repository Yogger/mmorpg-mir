package com.mmorpg.mir.model.operator.model;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class OperatorInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.OPERATOR;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getOperatorJson() == null) {
			ent.setOperatorJson(JsonUtils.object2String(OperatorPool.valueOf()));
		}

		Player player = ent.getPlayer();

		if (player.getOperatorPool() == null) {
			OperatorPool operator = JsonUtils.string2Object(ent.getOperatorJson(), OperatorPool.class);
			if (operator.getQiHuPrivilege() == null) {
				operator.setQiHuPrivilege(QiHu360PrivilegePlayer.valueOf());
			}
			if (operator.getQiHuSpeedPrivilege() == null) {
				operator.setQiHuSpeedPrivilege(QiHu360SpeedPrivilegePlayer.valueOf());
			}
			if (operator.getOperatorVip().getLevelRewarded() == null) {
				operator.getOperatorVip().setLevelRewarded(new HashMap<Integer, Boolean>());
			}
			if(operator.getOperatorVip().getNickNameRewarded() == null){
				operator.getOperatorVip().setNickNameRewarded(new HashMap<Integer, Boolean>());
			}
			operator.getMobilePhone().setOwner(player);
			operator.getOperatorVip().setOwner(player);
			operator.getSubInformation().setOwner(player);
			operator.getGmPrivilege().setOwner(player);
			player.setOperatorPool(operator);
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getOperatorPool() != null) {
			ent.setOperatorJson(JsonUtils.object2String(player.getOperatorPool()));
		}
	}
}
