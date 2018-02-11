package com.mmorpg.mir.model.chat.model.show.object;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.i18n.model.I18nPackItem;
import com.mmorpg.mir.model.i18n.model.I18nPackType;
import com.mmorpg.mir.model.rank.model.rankelement.HeroRankElement;

public class HeroMailShow implements I18nPackItem {

	private int rank;

	private String name;

	private int killCount;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getKillCount() {
		return killCount;
	}

	public void setKillCount(int killCount) {
		this.killCount = killCount;
	}

	@JsonIgnore
	@Override
	public byte getMessageType() {
		return I18nPackType.HERO_RANK.getValue();
	}
	
	@JsonIgnore
	public static HeroMailShow createShow(HeroRankElement element, int rank) {
		HeroMailShow show = new HeroMailShow();
		show.killCount = element.getCount();
		show.name = element.getName();
		show.rank = rank;
		return show;
	}
}
