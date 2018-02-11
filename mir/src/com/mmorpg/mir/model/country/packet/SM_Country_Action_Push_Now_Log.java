package com.mmorpg.mir.model.country.packet;

import com.mmorpg.mir.model.country.model.log.CountryLogValue;
import com.mmorpg.mir.model.country.model.vo.CountryLogValueVO;

/**
 * 国家当前操作日志推送
 * 
 * @author 37wan
 * 
 */
public class SM_Country_Action_Push_Now_Log {

	private CountryLogValueVO logValue;

	public static SM_Country_Action_Push_Now_Log valueOf(CountryLogValue logValue) {
		SM_Country_Action_Push_Now_Log sm = new SM_Country_Action_Push_Now_Log();
		sm.setLogValue(CountryLogValueVO.valueOf(logValue));
		return sm;
	}

	public CountryLogValueVO getLogValue() {
		return logValue;
	}

	public void setLogValue(CountryLogValueVO logValue) {
		this.logValue = logValue;
	}

}
