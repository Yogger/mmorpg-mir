package com.mmorpg.mir.model.ai;

public enum AIType {
	ROBOT {
		@SuppressWarnings("unchecked")
		@Override
		public RobotAi create() {
			return new RobotAi();
		}
	},
	AGGRESSIVE {
		@SuppressWarnings("unchecked")
		@Override
		public AggressiveAi create() {
			return new AggressiveAi();
		}
	},
	PASSIVE {

		@SuppressWarnings("unchecked")
		@Override
		public MonsterAi create() {
			return new MonsterAi();
		}
	},

	ROUTE {
		@SuppressWarnings("unchecked")
		@Override
		public RouteAi create() {
			return new RouteAi();
		}

	},

	ROUTEAGGRESSIVE {
		@SuppressWarnings("unchecked")
		@Override
		public RouteAggressiveAi create() {
			return new RouteAggressiveAi();
		}
	},

	NONE {
		@SuppressWarnings("unchecked")
		@Override
		public DummyAi create() {
			return new DummyAi();
		}
	},

	SUMMON {
		@SuppressWarnings("unchecked")
		@Override
		public SummonAi create() {
			return new SummonAi();
		}
	},
	BIGBROTHER {
		@SuppressWarnings("unchecked")
		@Override
		public BigBrotherAi create() {
			return new BigBrotherAi();
		}
	};

	public abstract <T extends AI> T create();
}
