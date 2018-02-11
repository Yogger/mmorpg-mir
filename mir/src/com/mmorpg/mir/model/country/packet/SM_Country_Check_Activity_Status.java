package com.mmorpg.mir.model.country.packet;

import java.util.ArrayList;
import java.util.Date;

import com.mmorpg.mir.model.country.model.vo.ActivityVO;
import com.mmorpg.mir.model.country.resource.ConfigValueManager;
import com.windforce.common.utility.DateUtils;

/**
 * 处理前端查看砍大臣,砍国旗的状态
 * 
 * @author 37wan
 * 
 */
public class SM_Country_Check_Activity_Status {

	private ArrayList<ActivityVO> times;

	public static SM_Country_Check_Activity_Status valueOf() {
		SM_Country_Check_Activity_Status resp = new SM_Country_Check_Activity_Status();
		ArrayList<ActivityVO> times = new ArrayList<ActivityVO>(2);
		long oneDayTimeMillis = DateUtils.MILLIS_PER_DAY;
		long flagStart = DateUtils.getNextTime(ConfigValueManager.getInstance().COUNTRY_SAFE_START_CRON_FLAG.getValue(),
				new Date(System.currentTimeMillis())).getTime();
		flagStart -= oneDayTimeMillis;
		long flagEnd = DateUtils.getNextTime(ConfigValueManager.getInstance().COUNTRY_SAFE_END_CRON_FLAG.getValue(),
				new Date(System.currentTimeMillis())).getTime();
		flagEnd -= oneDayTimeMillis;
		// 国旗
		ActivityVO falgVO = ActivityVO.valueOf(flagStart, flagEnd);
		//System.out.println("国旗 : " + falgVO);
		times.add(falgVO);
		// 大臣
		long startDip = DateUtils.getNextTime(ConfigValueManager.getInstance().COUNTRY_SAFE_START_CRON.getValue(),
				new Date(System.currentTimeMillis())).getTime();
		startDip -= oneDayTimeMillis;
		long endDip = DateUtils.getNextTime(ConfigValueManager.getInstance().COUNTRY_SAFE_END_CRON.getValue(),
				new Date(System.currentTimeMillis())).getTime();
		endDip -= oneDayTimeMillis;
		ActivityVO falgVO2 = ActivityVO.valueOf(startDip, endDip);
		//System.out.println("大臣 : " + falgVO2);
		times.add(falgVO2);
		//
		resp.setTimes(times);
		return resp;
	}

	public ArrayList<ActivityVO> getTimes() {
		return times;
	}

	public void setTimes(ArrayList<ActivityVO> times) {
		this.times = times;
	}

}
