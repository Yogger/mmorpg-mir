package com.mmorpg.mir.model.countrycopy.packet;

import com.mmorpg.mir.model.countrycopy.model.CountryCopy;
import com.mmorpg.mir.model.countrycopy.packet.vo.FirstEncourageVO;

public class SM_Encourage_Change {
	private FirstEncourageVO firstVO;
	
	private byte encourageCount;
	
	public static SM_Encourage_Change valueOf(CountryCopy copy) {
		SM_Encourage_Change sm = new SM_Encourage_Change();
		int size = copy.getEncourageList().size();
		sm.encourageCount = (byte) size;
		if (size == 1) {
			sm.firstVO = FirstEncourageVO.valueOf(copy.getEncourageList().get(size));
		}
		return sm;
	}

	public FirstEncourageVO getFirstVO() {
		return firstVO;
	}

	public void setFirstVO(FirstEncourageVO firstVO) {
		this.firstVO = firstVO;
	}

	public byte getEncourageCount() {
		return encourageCount;
	}

	public void setEncourageCount(byte encourageCount) {
		this.encourageCount = encourageCount;
	}
	
}
