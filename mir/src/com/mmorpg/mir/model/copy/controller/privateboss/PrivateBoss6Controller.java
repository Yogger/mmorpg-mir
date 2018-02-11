package com.mmorpg.mir.model.copy.controller.privateboss;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.world.WorldMapInstance;

@Component
public class PrivateBoss6Controller extends AbstractPrivateBossController {

	public PrivateBoss6Controller() {
	}
	
	public PrivateBoss6Controller(Player owner, WorldMapInstance worldMapInstance, CopyResource resource) {
		super(owner, worldMapInstance, resource);
	}
	
	@Override
	public String getCopyId() {
		return "individual_boss06";
	}

	@Override
	public void enterCopy(Player player, WorldMapInstance worldMapInstance, CopyResource resource) {
		PrivateBoss6Controller p = new PrivateBoss6Controller(player, worldMapInstance, resource);
		player.getCopyHistory().setCopyController(p);
	}

}
