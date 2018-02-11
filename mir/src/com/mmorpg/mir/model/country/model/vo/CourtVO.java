package com.mmorpg.mir.model.country.model.vo;

import java.util.ArrayList;
import java.util.HashSet;

import com.mmorpg.mir.model.country.model.CountryOfficial;
import com.mmorpg.mir.model.country.model.Court;
import com.mmorpg.mir.model.country.model.KingGurad;
import com.mmorpg.mir.model.country.model.Official;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.player.manager.PlayerManager;

public class CourtVO {
	private ArrayList<OfficialVO> vos;
	private int control;
	private HashSet<KingGuradVO> kingGuards;
	private String gangName;
	private String notice;

	public static CourtVO valueOf(Court court) {
		CourtVO vo = new CourtVO();
		vo.vos = new ArrayList<OfficialVO>();
		vo.control = court.getControl();
		for (Official offical : court.getOfficials().values()) {
			vo.getVos().add(offical.creatVO());
			if (offical.getOfficial() == CountryOfficial.KING) {
				Player player = PlayerManager.getInstance().getPlayer(offical.getPlayerId());
				if (player.getGang() != null) {
					vo.gangName = player.getGang().getName();
				}
			}
		}
		if (!court.getKingGuards().isEmpty()) {
			vo.kingGuards = new HashSet<KingGuradVO>();
			for (KingGurad kg : court.getKingGuards()) {
				vo.kingGuards.add(KingGuradVO.valueOf(kg, PlayerManager.getInstance().getPlayer(kg.getPlayerId())));
			}
		}
		vo.setNotice(court.getNotice());
		return vo;
	}

	public ArrayList<OfficialVO> getVos() {
		return vos;
	}

	public void setVos(ArrayList<OfficialVO> vos) {
		this.vos = vos;
	}

	public int getControl() {
		return control;
	}

	public void setControl(int control) {
		this.control = control;
	}

	public HashSet<KingGuradVO> getKingGuards() {
		return kingGuards;
	}

	public void setKingGuards(HashSet<KingGuradVO> kingGuards) {
		this.kingGuards = kingGuards;
	}

	public String getGangName() {
		return gangName;
	}

	public void setGangName(String gangName) {
		this.gangName = gangName;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

}
