package com.mmorpg.mir.model.controllers;

import com.mmorpg.mir.model.SpringComponentStation;

public class GatherableController extends StaticObjectController {

	public void onDie() {
		scheduleRelive();
		delete();
	}

	private void scheduleRelive() {
		SpringComponentStation.getReliveService().scheduleDecayAndReliveTask(getOwner());
	}
}
