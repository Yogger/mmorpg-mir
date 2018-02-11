package com.mmorpg.mir.model.object.route;

public class RouteRoad {
	protected final RouteStep[] routeSteps;
	protected final int length;
	protected int step = 0;

	public RouteRoad(RouteStep[] routeSteps) {
		this.routeSteps = routeSteps;
		this.length = this.routeSteps.length;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public boolean isOver() {
		return step >= routeSteps.length;
	}

	public RouteStep getNextStep() {
		return routeSteps[step];
	}

	public void overStep() {
		step++;
	}

	public void reset() {
		step = 0;
	}
}
