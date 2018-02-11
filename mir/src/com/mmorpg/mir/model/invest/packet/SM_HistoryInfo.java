package com.mmorpg.mir.model.invest.packet;

import java.util.ArrayList;

import com.mmorpg.mir.model.invest.model.InvestInfo;

public class SM_HistoryInfo {
	private ArrayList<InvestInfo> records;

	private int code;

	public static SM_HistoryInfo valueOf(ArrayList<InvestInfo> records) {
		SM_HistoryInfo result = new SM_HistoryInfo();
		result.records = records;
		return result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public ArrayList<InvestInfo> getRecords() {
		return records;
	}

	public void setRecords(ArrayList<InvestInfo> records) {
		this.records = records;
	}

}
