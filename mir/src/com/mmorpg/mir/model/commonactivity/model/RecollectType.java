package com.mmorpg.mir.model.commonactivity.model;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.vip.manager.VipManager;

public enum RecollectType {
	/** 血战边疆 **/
	EXP_COPY(1) {
		@Override
		public int getVipExtraCount(Player player, long time) {
			return 0;
		}
	},
	/** 日常任务 **/
	DAILY_MISSION(2) {
		@Override
		public int getVipExtraCount(Player player, long time) {
			return 0;
		}
	},
	/** 膜拜皇帝 **/
	WARSHIP(3) {
		@Override
		public int getVipExtraCount(Player player, long time) {
			return 0;
		}
	},
	/** 国家祭祀 **/
	COUNTRY_SACRIFICE(4) {
		@Override
		public int getVipExtraCount(Player player, long time) {
			return 0;
		}
	},
	/** 城池争夺 **/
	TOWN_CAPTURE(5) {
		@Override
		public int getVipExtraCount(Player player, long time) {
			return 0;
		}
	},
	/** 运镖 **/
	EXPRESS_LORRY(6) {
		@Override
		public int getVipExtraCount(Player player, long time) {
			int vipLevel = player.getVip().getVipLevelAtThatDay(time);
			return VipManager.getInstace().getVipResource(vipLevel).getExExpressCount();
		}
	},
	/** 营救 **/
	RESCUE(7) {
		@Override
		public int getVipExtraCount(Player player, long time) {
			int vipLevel = player.getVip().getVipLevelAtThatDay(time);
			return VipManager.getInstace().getVipResource(vipLevel).getExRescueCount();
		}
	},
	/** 大臣 **/
	DIPLOMACY(8) {
		@Override
		public int getVipExtraCount(Player player, long time) {
			return 0;
		}
	},
	/** 国旗 **/
	COUNTRY_FLAG(9) {
		@Override
		public int getVipExtraCount(Player player, long time) {
			return 0;
		}
	},
	/** 刺探 **/
	INVESTIGATE(10) {
		@Override
		public int getVipExtraCount(Player player, long time) {
			int vipLevel = player.getVip().getVipLevelAtThatDay(time);
			return VipManager.getInstace().getVipResource(vipLevel).getExInvestigateCount();
		}
	},
	/** 搬砖 **/
	BRICK(11) {
		@Override
		public int getVipExtraCount(Player player, long time) {
			int vipLevel = player.getVip().getVipLevelAtThatDay(time);
			return VipManager.getInstace().getVipResource(vipLevel).getExBrickCount();
		}
	};
	
	public abstract int getVipExtraCount(Player player, long time);
	
	private final int value;
	
	public static RecollectType valueOf(int v) {
		for (RecollectType type : RecollectType.values()) {
			if (type.getValue() == v) {
				return type;
			}
		}
		return null;
	}
	
	private RecollectType(int v) {
		this.value = v;
	}
	
	public final int getValue() {
		return value;
	}
	
}
