package com.mmorpg.mir.model.copy.controller.privateboss;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.world.WorldMapInstance;

@Component
public class PrivateBoss4Controller extends AbstractPrivateBossController {

	public PrivateBoss4Controller() {
	}
	
	public PrivateBoss4Controller(Player owner, WorldMapInstance worldMapInstance, CopyResource resource) {
		super(owner, worldMapInstance, resource);
	}
	
	@Override
	public String getCopyId() {
		return "individual_boss04";
	}

	@Override
	public void enterCopy(Player player, WorldMapInstance worldMapInstance, CopyResource resource) {
		PrivateBoss4Controller p = new PrivateBoss4Controller(player, worldMapInstance, resource);
		player.getCopyHistory().setCopyController(p);
	}

}
