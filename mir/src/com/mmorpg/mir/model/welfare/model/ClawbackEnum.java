package com.mmorpg.mir.model.welfare.model;

import com.mmorpg.mir.model.commonactivity.model.RecollectType;

public enum ClawbackEnum {

	/** !血战边疆 */
	CLAWBACK_EVENT_COPY(1) {
		@Override
		public RecollectType getRecollectType() {
			return RecollectType.EXP_COPY;
		}
	},

	/** ! 名将试炼 - 爬塔副本 */
	/*CLAWBACK_EVENT_LADDER(2),*/

	/** ! vip副本 */
	// CLAWBACK_EVENT_VIP_COPY(3),

	/** !日常任务 */
	CLAWBACK_EVENT_DAILYTASK(4) {
		@Override
		public RecollectType getRecollectType() {
			return RecollectType.DAILY_MISSION;
		}
	},
	/** !太庙搬砖 */
	CLAWBACK_EVENT_TEMPLE(5) {
		@Override
		public RecollectType getRecollectType() {
			return RecollectType.BRICK;
		}
	},

	/** !运镖 */
	CLAWBACK_EVENT_EXPRESS(6) {
		@Override
		public RecollectType getRecollectType() {
			return RecollectType.EXPRESS_LORRY;
		}
	},

	/** !营救 */
	CLAWBACK_EVENT_RESCUE(7)  {
		@Override
		public RecollectType getRecollectType() {
			return RecollectType.RESCUE;
		}
	},

	/** !刺探 */
	CLAWBACK_EVENT_INVESTIGATE(8) {
		@Override
		public RecollectType getRecollectType() {
			return RecollectType.INVESTIGATE;
		}
	},

	/** !膜拜皇帝 */
	CLAWBACK_EVENT_WARSHIP(9) {
		@Override
		public RecollectType getRecollectType() {
			return RecollectType.WARSHIP;
		}
	},

	/** !国家祭祀 */
	CLAWBACK_EVENT_COUNTRYSACRIFICE(10) {
		@Override
		public RecollectType getRecollectType() {
			return RecollectType.COUNTRY_SACRIFICE;
		}
	},
	
	/** !游历任务次数找回 */
	// CLAWBACK_EVENT_RANDOM(11),

	/** !砍大臣 */
	CLAWBACK_EVENT_KILL_DISPLOMACY(12) {
		@Override
		public RecollectType getRecollectType() {
			return RecollectType.DIPLOMACY;
		}
	},

	/** !砍国旗 */
	CLAWBACK_EVENT_KILL_FLAG(13)  {
		@Override
		public RecollectType getRecollectType() {
			return RecollectType.COUNTRY_FLAG;
		}
	},

	;
	private final int eventId;

	private ClawbackEnum(int eventId) {
		this.eventId = eventId;
	}

	public int getEventId() {
		return eventId;
	}
	
	public RecollectType getRecollectType() {
		return null;
	}

	public static ClawbackEnum valueOf(int eventId) {
		for (ClawbackEnum claw : values()) {
			if (claw.eventId == eventId) {
				return claw;
			}
		}
		throw new RuntimeException("can not find enmu by id = [" + eventId + "]");
	}

}
