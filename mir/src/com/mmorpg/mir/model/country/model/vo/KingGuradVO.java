package com.mmorpg.mir.model.country.model.vo;

import com.mmorpg.mir.model.country.model.CountryOfficial;
import com.mmorpg.mir.model.country.model.KingGurad;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.model.PlayerSimpleInfo;

public class KingGuradVO {

	public static final String GUARD_OFFICIAL = CountryOfficial.GUARD.name();
	private String offical;

	private PlayerSimpleInfo info;

	private int index;

	public static KingGuradVO valueOf(KingGurad kingGurad, Player player) {
		KingGuradVO vo = new KingGuradVO();
		vo.offical = GUARD_OFFICIAL;
		vo.info = player.createSimple();
		vo.index = kingGurad.getIndex();
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

}
