package com.mmorpg.mir.model.country.model.vo;

import java.util.ArrayList;

import org.h2.util.New;

import com.mmorpg.mir.model.country.model.log.CountryLogValue;
import com.mmorpg.mir.model.country.model.log.CountryLogger;

public class CountryLoggerVO {

	private ArrayList<CountryLogValueVO> logValues;

	public static CountryLoggerVO valueOf(CountryLogger logger) {
		CountryLoggerVO vo = new CountryLoggerVO();
		ArrayList<CountryLogValueVO> logValues = New.arrayList();
		for (CountryLogValue value : logger.getLogValues()) {
			logValues.add(CountryLogValueVO.valueOf(value));
		}
		vo.setLogValues(logValues);
		return vo;
	}

	public ArrayList<CountryLogValueVO> getLogValues() {
		return logValues;
	}

	public void setLogValues(ArrayList<CountryLogValueVO> logValues) {
		this.logValues = logValues;
	}

}
