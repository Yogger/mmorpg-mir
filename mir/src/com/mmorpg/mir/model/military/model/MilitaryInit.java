package com.mmorpg.mir.model.military.model;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class MilitaryInit extends ModuleHandle{

	@Override
	public ModuleKey getModule() {
		return ModuleKey.MILITARY;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		if (ent.getMilitaryJson() == null) {
			Military m = Military.valueOf(0);
			ent.setMilitaryJson(JsonUtils.object2String(m));
		}
		
		Player player = ent.getPlayer();
		
		if (player.getMilitary() == null) {
			Military m = JsonUtils.string2Object(ent.getMilitaryJson(), Military.class);
			if(m.getBreaks() == null){
				m.setBreaks(new ArrayList<Integer>());
			}
			player.setMilitary(m);
			m.setOwner(player);
		}
		
	}

	public static void main(String[] args) {
		Military m = Military.valueOf(0);
		m.getKillCount().put(123L, 12);
		m.getKillLastTime().put(123L, 12312L);
		//m.getKillMap().put(12313L, new Pair<Long, Double>(123L, 1.0));
		String a = JsonUtils.object2String(m);
		System.out.println(a);
		Military m1 = JsonUtils.string2Object(a, Military.class);
		m1.setStarId("1");
		System.out.println(JsonUtils.object2String(m1));
	}

	@Override
	public void serialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (player.getMilitary() != null) {
			ent.setMilitaryJson(JsonUtils.object2String(player.getMilitary()));
		}
	}

}
