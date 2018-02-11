package com.mmorpg.mir.model.commonactivity.model;

import java.util.HashMap;
import java.util.Map;

public class CommonIdentifyTreasureTotalServers {
	private Map<String, CommonIdentifyTreasureServer> treasureServers = new HashMap<String, CommonIdentifyTreasureServer>();
	
	public static CommonIdentifyTreasureTotalServers valueOf(){
		CommonIdentifyTreasureTotalServers servers = new CommonIdentifyTreasureTotalServers();
		servers.treasureServers = new HashMap<String, CommonIdentifyTreasureServer>();
		return servers;
	}

	public Map<String, CommonIdentifyTreasureServer> getTreasureServers() {
		return treasureServers;
	}

	public void setTreasureServers(Map<String, CommonIdentifyTreasureServer> treasureServers) {
		this.treasureServers = treasureServers;
	}
	
	public CommonIdentifyTreasureServer getTreasureServerByActiveName(String activeName){
		return treasureServers.get(activeName);
	}
	
	public void addTreasureServer(String activeName, CommonIdentifyTreasureServer treasureServer){
		treasureServers.put(activeName, treasureServer);
	}
}
