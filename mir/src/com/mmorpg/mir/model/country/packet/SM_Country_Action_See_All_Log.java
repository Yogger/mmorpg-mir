package com.mmorpg.mir.model.country.packet;

import java.util.Map;

import com.mmorpg.mir.model.country.model.vo.CountryLoggerVO;

/**
 * 查看日志
 * 
 * @author 37wan
 * 
 */
public class SM_Country_Action_See_All_Log {

	private Map<Integer, CountryLoggerVO> map;

	public static SM_Country_Action_See_All_Log valueOf(Map<Integer, CountryLoggerVO> map) {
		SM_Country_Action_See_All_Log sm = new SM_Country_Action_See_All_Log();
		sm.setMap(map);
		return sm;
	}

	public Map<Integer, CountryLoggerVO> getMap() {
		return map;
	}

	public void setMap(Map<Integer, CountryLoggerVO> map) {
		this.map = map;
	}

}
