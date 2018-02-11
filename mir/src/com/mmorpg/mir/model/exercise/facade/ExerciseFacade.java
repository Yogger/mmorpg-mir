package com.mmorpg.mir.model.exercise.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.exercise.ExerciseManager;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.windforce.common.event.anno.ReceiverAnno;

@Component
public class ExerciseFacade {

	@Autowired
	private PlayerManager playerManager;

	@Autowired
	private ExerciseManager exerciseManager;

	@ReceiverAnno
	public void clearExecrise(LoginEvent loginEvent) {
		Player player = playerManager.getPlayer(loginEvent.getOwner());
		exerciseManager.clearExecrise(player);
	}
}
