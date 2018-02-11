package com.mmorpg.mir.model.object.route;

public enum RouteType {

	COMMON_ROUTE() {
		@Override
		public RouteRoad createRoad(RouteStep[] steps) {
			return new RouteRoad(steps);
		}
	},

	ONEWAY_LOOP_ROUTE() {

		@Override
		public RouteRoad createRoad(RouteStep[] steps) {
			return new OneWayLoopRouteRoad(steps);
		}

	};

	// REVERSE_LOOP_ROUTE() {
	//
	// @Override
	// public RouteRoad createRoad(RouteStep[] steps) {
	// return new ReverseLoopRouteRoad(steps);
	// }
	//
	// };

	public abstract RouteRoad createRoad(RouteStep[] steps);
}
