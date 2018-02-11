package com.mmorpg.mir.model.chat.model.show.object;

import com.mmorpg.mir.model.i18n.model.I18nPackItem;
import com.mmorpg.mir.model.i18n.model.I18nPackType;

public class PositionShow implements I18nPackItem {
	private int mapId;
	private int x;
	private int y;
	private int instanceId;
	private int flyId;

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

	@Override
	public byte getMessageType() {
		return I18nPackType.POSITION.getValue();
	}

	public int getFlyId() {
		return flyId;
	}

	public void setFlyId(int flyId) {
		this.flyId = flyId;
	}

}
