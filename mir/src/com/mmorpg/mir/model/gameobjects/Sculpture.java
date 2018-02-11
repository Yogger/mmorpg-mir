package com.mmorpg.mir.model.gameobjects;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.controllers.StaticObjectController;
import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.player.packet.SM_SculptureInfo;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.world.WorldPosition;

/**
 * 人物雕塑
 * 
 * @author Kuang Hao
 * @since v1.0 2015-3-25
 * 
 */
public class Sculpture extends StaticObject {

	private long playerId;
	private String playerName;
	private int role;
	private int country;

	public Sculpture(long objectId, StaticObjectController controller, WorldPosition position) {
		super(objectId, controller, position);
	}

	@JsonIgnore
	public void carve(Player player) {
		if (player == null) {
			destroy();
		} else {
			playerName = player.getName();
			role = player.getRole();
			country = player.getCountryValue();
			playerId = player.getObjectId();
			PacketSendUtility.broadcastPacket(this, SM_SculptureInfo.valueOf(this));
		}
	}
	
	@JsonIgnore
	public void destroy() {
		playerName = "";
		role = 0;
		country = 0;
		playerId = 0L;
		PacketSendUtility.broadcastPacket(this, SM_SculptureInfo.valueOf(this));	
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.SCULPTURE;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

}