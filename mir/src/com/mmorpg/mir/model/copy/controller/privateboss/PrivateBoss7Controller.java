package com.mmorpg.mir.model.copy.controller.privateboss;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.world.WorldMapInstance;

@Component
public class PrivateBoss7Controller extends AbstractPrivateBossController {

	public PrivateBoss7Controller() {
	}
	
	public PrivateBoss7Controller(Player owner, WorldMapInstance worldMapInstance, CopyResource resource) {
		super(owner, worldMapInstance, resource);
	}
	
	@Override
	public String getCopyId() {
		return "individual_boss07";
	}

	@Override
	public void enterCopy(Player player, WorldMapInstance worldMapInstance, CopyResource resource) {
		PrivateBoss7Controller p = new PrivateBoss7Controller(player, worldMapInstance, resource);
		player.getCopyHistory().setCopyController(p);
	}

}
