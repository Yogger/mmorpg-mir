package com.mmorpg.mir.model.rank.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.ModuleHandle;
import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.entity.PlayerEnt;
import com.windforce.common.utility.JsonUtils;

@Component
public class RankInfoInit extends ModuleHandle {

	@Override
	public ModuleKey getModule() {
		return ModuleKey.RANK_INFO;
	}

	@Override
	public void deserialize(PlayerEnt ent) {
		Player player = ent.getPlayer();
		if (ent.getRankInfoJson() == null) {
			ent.setRankInfoJson(JsonUtils.object2String(RankInfo.valueOf(player)));
		}
		
		if (player.getRankInfo() == null) {
			player.setRankInfo(JsonUtils.string2Object(ent.getRankInfoJson(), RankInfo.class));
		}
	}

	@Override
	public void serialize(PlayerEnt ent) {
		if (ent.getPlayer().getRankInfo() != null)  {
			ent.setRankInfoJson(JsonUtils.object2String(ent.getPlayer().getRankInfo()));
		}
	}

	public static void main(String[] args) {
	    RankInfo i = new RankInfo();
	    i.setSlaughterHistory(new HashMap<Long, Set<Long>>());
	    i.getSlaughterHistory().put(123L, new HashSet<Long>());
	    i.getSlaughterHistory().get(123L).add(12345L);
	    i.getSlaughterHistory().get(123L).add(12345L);
	    i.getSlaughterHistory().get(123L).add(12355L);
	    String s = JsonUtils.object2String(i);
	    System.out.println(s);
	    RankInfo info = JsonUtils.string2Object(s, RankInfo.class);
	    info.refresh();
	    System.out.println(info);
    }
}
