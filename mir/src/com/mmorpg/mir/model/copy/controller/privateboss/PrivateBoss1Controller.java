package com.mmorpg.mir.model.copy.controller.privateboss;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.world.WorldMapInstance;

@Component
public class PrivateBoss1Controller extends AbstractPrivateBossController {

	public PrivateBoss1Controller() {
	}
	
	public PrivateBoss1Controller(Player owner, WorldMapInstance worldMapInstance, CopyResource resource) {
		super(owner, worldMapInstance, resource);
	}
	
	@Override
	public String getCopyId() {
		return "individual_boss01";
	}

	@Override
	public void enterCopy(Player player, WorldMapInstance worldMapInstance, CopyResource resource) {
		PrivateBoss1Controller p1 = new PrivateBoss1Controller(player, worldMapInstance, resource);
		player.getCopyHistory().setCopyController(p1);
	}

}
