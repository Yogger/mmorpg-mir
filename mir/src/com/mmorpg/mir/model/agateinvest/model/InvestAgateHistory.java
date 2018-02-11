package com.mmorpg.mir.model.agateinvest.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.agateinvest.InvestAgateConfig;
import com.mmorpg.mir.model.agateinvest.entity.InvestAgateHistoryEntity;
import com.mmorpg.mir.model.agateinvest.packet.SM_Agate_HistoryInfo;

public class InvestAgateHistory {
	/** 全部记录 */
	private LinkedList<InvestAgateInfo> allRecord;

	@Transient
	private InvestAgateHistoryEntity investHistoryEnity;

	public static InvestAgateHistory valueOf() {
		InvestAgateHistory result = new InvestAgateHistory();
		result.allRecord = new LinkedList<InvestAgateInfo>();
		return result;
	}

	// 业务方法
	@JsonIgnore
	public void addRecord(InvestAgateInfo info) {
		allRecord.addFirst(info);
		this.update();
	}

	@JsonIgnore
	public SM_Agate_HistoryInfo getHistoryRecords() {
		ArrayList<InvestAgateInfo> pageRecord = new ArrayList<InvestAgateInfo>();
		pageRecord.addAll(allRecord);
		int maxSize = InvestAgateConfig.getInstance().HISTORY_RECORD_MAX_COUNT.getValue();
		int size = pageRecord.size();
		if (size <= maxSize) {
			return SM_Agate_HistoryInfo.valueOf(new ArrayList<InvestAgateInfo>(pageRecord.subList(0, size)));
		} else {
			SM_Agate_HistoryInfo result = SM_Agate_HistoryInfo.valueOf(new ArrayList<InvestAgateInfo>(pageRecord.subList(0, maxSize)));
			List<InvestAgateInfo> list = pageRecord.subList(0, maxSize);
			if (size > maxSize * 2) {
				for (InvestAgateInfo info : pageRecord) {
					if (!list.contains(info)) {
						allRecord.remove(info);
					}
				}
				this.update();
			}
			return result;
		}
	}

	@JsonIgnore
	public void update() {
		investHistoryEnity.update();
	}

	public LinkedList<InvestAgateInfo> getAllRecord() {
		return allRecord;
	}

	public void setAllRecord(LinkedList<InvestAgateInfo> allRecord) {
		this.allRecord = allRecord;
	}

	@JsonIgnore
	public InvestAgateHistoryEntity getInvestHistoryEnity() {
		return investHistoryEnity;
	}

	@JsonIgnore
	public void setInvestHistoryEnity(InvestAgateHistoryEntity investHistoryEnity) {
		this.investHistoryEnity = investHistoryEnity;
	}

}
