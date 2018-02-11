package com.mmorpg.mir.model.openactive.model;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.OpenActiveConfig;
import com.mmorpg.mir.model.openactive.resource.OpenAcitveMilitaryResource;
import com.mmorpg.mir.model.openactive.resource.OpenActiveConsumeResource;
import com.mmorpg.mir.model.openactive.resource.OpenActiveEquipEnhanceResource;
import com.mmorpg.mir.model.openactive.resource.OpenActiveEquipResource;
import com.mmorpg.mir.model.openactive.resource.OpenActiveExpResource;
import com.mmorpg.mir.model.openactive.resource.OpenActiveHorseUpgradeResource;
import com.mmorpg.mir.model.openactive.resource.OpenActiveLevelResource;

public enum OpenActiveType {

	EXP(1) {

		@Override
		public int getCanRewardCount(Player player) {
			int count = 0;
			for (OpenActiveExpResource resource : OpenActiveConfig.getInstance().expStorage.getAll()) {
				if (!player.getOpenActive().getExpActive().getRewarded().contains(resource.getId())
						&& resource.getCoreConditions().verify(player, false)) {
					count++;
				}
			}
			return count;
		}

	},

	EQUIP(2) {

		@Override
		public int getCanRewardCount(Player player) {
			int count = 0;
			for (OpenActiveEquipResource resource : OpenActiveConfig.getInstance().equipStorage.getAll()) {
				if (!player.getOpenActive().getEquipActive().getRewarded().contains(resource.getId())
						&& resource.getCoreConditions().verify(player, false)) {
					count++;
				}
			}
			return count;
		}

	},

	EQUIP_ENHANCE(3) {

		@Override
		public int getCanRewardCount(Player player) {
			int count = 0;
			for (OpenActiveEquipEnhanceResource resource : OpenActiveConfig.getInstance().equipEnhanceStorage.getAll()) {
				if (!player.getOpenActive().getEnhanceActive().getRewarded().contains(resource.getId())
						&& resource.getCoreConditions().verify(player, false)) {
					count++;
				}
			}
			return count;
		}

	},

	/** 消费活动 **/
	CONSUME(4) {
		@Override
		public int getCanRewardCount(Player player) {
			int count = 0;
			if (!player.getOpenActive().getConsumeActive().getRewarded().isEmpty()) {
				return 1;
			}
			for (OpenActiveConsumeResource resource : OpenActiveConfig.getInstance().consumeActiveResource.getAll()) {
				if (resource.getCoreConditions().verify(player, false)) {
					return 1;
				}
			}
			return count;
		}
	},
	/** 等级竞技 **/
	LEVEL(5) {
		@Override
		public int getCanRewardCount(Player player) {
			int count = 0;
			for (OpenActiveLevelResource resource : OpenActiveConfig.getInstance().levelActiveResource.getAll()) {
				if (!player.getOpenActive().getLevelActive().getRewarded().contains(resource.getId())
						&& resource.getCoreConditions().verify(player, false)) {
					count++;
				}
			}
			return count;
		}
	},
	/** 全民军衔 **/
	MILITARY(6) {
		@Override
		public int getCanRewardCount(Player player) {
			int count = 0;
			for (OpenAcitveMilitaryResource resource : OpenActiveConfig.getInstance().militaryActiveResource.getAll()) {
				if (!player.getOpenActive().getMilitaryActive().getRewarded().contains(resource.getId())
						 && resource.getCoreConditions().verify(player, false)) {
					count++;
				}
			}
			return count;
		}
	},
	/** 坐骑竞技 */
	HORSE(7) {

		@Override
		public int getCanRewardCount(Player player) {
			int count = 0;
			for (OpenActiveHorseUpgradeResource resource : OpenActiveConfig.getInstance().horseUpgradeStorage.getAll()) {
				if (!player.getOpenActive().getHorseUpgradeActive().getRewarded().contains(resource.getId())
						&& resource.getCoreConditions().verify(player, false)) {
					count++;
				}
			}
			return count;
		}

	};

	private final int value;

	private OpenActiveType(int v) {
		this.value = v;
	}

	public int getValue() {
		return value;
	}

	public abstract int getCanRewardCount(Player player);
}
