package com.mmorpg.mir.model.kingofwar.packet;

import java.util.ArrayList;

import org.h2.util.New;

import com.mmorpg.mir.model.kingofwar.packet.vo.CommandPlayerVO;

public class SM_KingOfWar_CommandPlayerList {
	private ArrayList<CommandPlayerVO> commandPlayerVos;

	public static SM_KingOfWar_CommandPlayerList valueOf(ArrayList<CommandPlayerVO> commandPlayerVos) {
		SM_KingOfWar_CommandPlayerList sm = new SM_KingOfWar_CommandPlayerList();
		sm.commandPlayerVos = commandPlayerVos;
		return sm;
	}

	public void add(CommandPlayerVO vo) {
		if (commandPlayerVos == null) {
			commandPlayerVos = New.arrayList();
		}
		commandPlayerVos.add(vo);
	}

	public ArrayList<CommandPlayerVO> getCommandPlayerVos() {
		return commandPlayerVos;
	}

	public void setCommandPlayerVos(ArrayList<CommandPlayerVO> commandPlayerVos) {
		this.commandPlayerVos = commandPlayerVos;
	}

}
