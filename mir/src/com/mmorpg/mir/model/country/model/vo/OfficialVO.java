package com.mmorpg.mir.model.country.model.vo;

import java.util.HashMap;

import com.mmorpg.mir.model.country.model.Official;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;

public class OfficialVO {
	private String offical;

	private PlayerSimpleInfo info;

	private int index;

	private HashMap<String, Integer> useAuthorityHistory;
	private HashMap<Integer, String> contexts;

	public static OfficialVO valueOf(Player player, Official offical) {
		OfficialVO vo = new OfficialVO();
		vo.setOffical(offical.getOfficial().name());
		vo.setInfo(player.createSimple());
		vo.index = offical.getIndex();
		vo.useAuthorityHistory = (HashMap<String, Integer>) offical.getUseAuthorityHistory();
		vo.contexts = (HashMap<Integer, String>) offical.getContexts();
		return vo;
	}

	public String getOffical() {
		return offical;
	}

	public void setOffical(String offical) {
		this.offical = offical;
	}

	public PlayerSimpleInfo getInfo() {
		return info;
	}

	public void setInfo(PlayerSimpleInfo info) {
		this.info = info;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public HashMap<String, Integer> getUseAuthorityHistory() {
		return useAuthorityHistory;
	}

	public void setUseAuthorityHistory(HashMap<String, Integer> useAuthorityHistory) {
		this.useAuthorityHistory = useAuthorityHistory;
	}

	public HashMap<Integer, String> getContexts() {
		return contexts;
	}

	public void setContexts(HashMap<Integer, String> contexts) {
		this.contexts = contexts;
	}

}
