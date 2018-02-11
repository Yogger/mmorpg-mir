package com.mmorpg.mir.model.moduleopen.packet.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;

public class ModuleOpenVO {
	
	private Map<String, Boolean> openeds;
	
	private ArrayList<String> loginOpeneds;
	
	public static ModuleOpenVO valueOf(Player player, ArrayList<String> loginOpeneds) {
		ModuleOpenVO vo = new ModuleOpenVO();
		vo.loginOpeneds = loginOpeneds;
		vo.openeds = new HashMap<String, Boolean>(player.getModuleOpen().getOpeneds());
		return vo;
	}

	public Map<String, Boolean> getOpeneds() {
		return openeds;
	}

	public void setOpeneds(Map<String, Boolean> openeds) {
		this.openeds = openeds;
	}

	public ArrayList<String> getLoginOpeneds() {
		return loginOpeneds;
	}

	public void setLoginOpeneds(ArrayList<String> loginOpeneds) {
		this.loginOpeneds = loginOpeneds;
	}
	
}
