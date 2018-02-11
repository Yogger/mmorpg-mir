package com.mmorpg.mir.model.copy.controller.privateboss;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.copy.resource.CopyResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.world.WorldMapInstance;

@Component
public class PrivateBoss2Controller extends AbstractPrivateBossController {
	
	public PrivateBoss2Controller() {
	}

	public PrivateBoss2Controller(Player owner, WorldMapInstance worldMapInstance, CopyResource resource) {
		super(owner, worldMapInstance, resource);
	}
	
	@Override
	public String getCopyId() {
		return "individual_boss02";
	}

	@Override
	public void enterCopy(Player player, WorldMapInstance worldMapInstance, CopyResource resource) {
		PrivateBoss2Controller p = new PrivateBoss2Controller(player, worldMapInstance, resource);
		player.getCopyHistory().setCopyController(p);
	}

}
