package com.mmorpg.mir.model.chat.model.show.object;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.i18n.model.I18nPackItem;
import com.mmorpg.mir.model.i18n.model.I18nPackType;
import com.mmorpg.mir.model.item.AbstractItem;

public class ItemShow implements I18nPackItem {
	private String key;
	private String owner;
	private ArrayList<AbstractItem> items = New.arrayList();

	public String getKey() {
		return key;
	}

	@JsonIgnore
	public void setItem(AbstractItem abstractItem) {
		if (items == null) {
			items = New.arrayList();
		}
		items.add(abstractItem);
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public ArrayList<AbstractItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<AbstractItem> items) {
		this.items = items;
	}

	@Override
	public byte getMessageType() {
		return I18nPackType.PLAYERITEM.getValue();
	}

}
