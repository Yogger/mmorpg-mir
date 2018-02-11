package com.mmorpg.mir.model.kingofwar.packet.vo;

public class KingCommandVO {

	private String command;

	public static KingCommandVO valueOf(String command) {
		KingCommandVO sm = new KingCommandVO();
		sm.command = command;
		return sm;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

}
