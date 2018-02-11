package com.mmorpg.mir.model.agateinvest.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.agateinvest.model.InvestAgateInfo;

public class SM_Agate_HistoryInfo {
	private ArrayList<InvestAgateInfo> records;

	private int code;

	public static SM_Agate_HistoryInfo valueOf(ArrayList<InvestAgateInfo> records) {
		SM_Agate_HistoryInfo result = new SM_Agate_HistoryInfo();
		result.records = records;
		return result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public ArrayList<InvestAgateInfo> getRecords() {
		return records;
	}

	public void setRecords(ArrayList<InvestAgateInfo> records) {
		this.records = records;
	}

}
