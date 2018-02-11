package com.mmorpg.mir.model.kingofwar.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.mmorpg.mir.model.gameobjects.Sculpture;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.kingofwar.manager.KingOfWarManager;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.world.World;
import com.windforce.common.utility.New;

/**
 * 雕像
 */
public class Sculptures {

	private Map<Integer, List<Sculpture>> kingScalpture = New.hashMap();

	@JsonIgnore
	public void refreshKingScalepture(int countryValue) {
		for (Sculpture sculpture : kingScalpture.get(countryValue)) {
			if (countryValue == 0) { //皇帝
				sculpture.carve(KingOfWarManager.getInstance().getKingOfKing());
			} else {
				sculpture.carve(CountryManager.getInstance().getCountries().get(CountryId.valueOf(countryValue)).getKing());
			}
		}
	}
	
	@JsonIgnore
	public void initKingSculpture() {
		Map<String, ArrayList<String>> map = ConfigValueManager.getInstance().SCULPTURE_SPAWN_ID.getValue();
		for (Entry<String, ArrayList<String>> entry: map.entrySet()) {
			Integer key = Integer.valueOf(entry.getKey());
			ArrayList<Sculpture> sculptures = New.arrayList();
			for (String spawnKey : entry.getValue()) {
				SpawnGroupResource npcSpawn = SpawnManager.getInstance().getSpawn(spawnKey);
				List<VisibleObject> npcs = World.getInstance().getWorldMap(npcSpawn.getMapId()).getInstances().get(1)
						.findObjectBySpawnId(npcSpawn.getKey());
				if (npcs.isEmpty() || npcs.size() > 1) {
					throw new RuntimeException(String.format("country[%s]皇帝雕像size[%s]", npcs.size()));
				}
				sculptures.add((Sculpture) npcs.get(0));
				kingScalpture.put(key, sculptures);
			}
			refreshKingScalepture(key);
		}
	}

	public Map<Integer, List<Sculpture>> getKingScalpture() {
		return kingScalpture;
	}

	public void setKingScalpture(Map<Integer, List<Sculpture>> kingScalpture) {
		this.kingScalpture = kingScalpture;
	}

	public List<Sculpture> getSculpturesByCountry(int countryValue) {
		return kingScalpture.get(countryValue);
	}
}
