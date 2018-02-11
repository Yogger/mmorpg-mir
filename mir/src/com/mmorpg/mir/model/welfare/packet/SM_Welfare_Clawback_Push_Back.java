package com.mmorpg.mir.model.welfare.packet;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.welfare.packet.vo.PushNumListVO;

public class SM_Welfare_Clawback_Push_Back {
	
	private PushNumListVO pushNumListVO;

	public static SM_Welfare_Clawback_Push_Back valueOf(Player player) {
		SM_Welfare_Clawback_Push_Back sm = new SM_Welfare_Clawback_Push_Back();
		sm.setPushNumListVO(PushNumListVO.valueOf(player));
		return sm;
	}

	public PushNumListVO getPushNumListVO() {
		return pushNumListVO;
	}

	public void setPushNumListVO(PushNumListVO pushNumListVO) {
		this.pushNumListVO = pushNumListVO;
	}

}
