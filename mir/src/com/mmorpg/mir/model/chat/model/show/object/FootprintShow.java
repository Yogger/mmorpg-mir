package com.mmorpg.mir.model.chat.model.show.object;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.footprint.model.Footprint;
import com.mmorpg.mir.model.i18n.model.I18nPackItem;
import com.mmorpg.mir.model.i18n.model.I18nPackType;

public class FootprintShow implements I18nPackItem {
	
	private Integer id;
	private ArrayList<Footprint> footprints;

	@Override
	public byte getMessageType() {
		return I18nPackType.FOOTPRINT.getValue();
	}
	
	@JsonIgnore
	public void setFootprint(Footprint footprint) {
		if (footprints == null) {
			footprints = New.arrayList();
		}
		footprints.add(footprint);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ArrayList<Footprint> getFootprints() {
		return footprints;
	}

	public void setFootprints(ArrayList<Footprint> footprints) {
		this.footprints = footprints;
	}

}
