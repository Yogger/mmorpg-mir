package com.mmorpg.mir.model.invest.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.invest.InvestConfig;
import com.mmorpg.mir.model.invest.entity.InvestHistoryEntity;
import com.mmorpg.mir.model.invest.packet.SM_HistoryInfo;

public class InvestHistory {
	/** 全部记录 */
	private LinkedList<InvestInfo> allRecord;

	@Transient
	private InvestHistoryEntity investHistoryEnity;

	public static InvestHistory valueOf() {
		InvestHistory result = new InvestHistory();
		result.allRecord = new LinkedList<InvestInfo>();
		return result;
	}

	// 业务方法
	@JsonIgnore
	public void addRecord(InvestInfo info) {
		allRecord.addFirst(info);
		this.update();
	}

	@JsonIgnore
	public SM_HistoryInfo getHistoryRecords() {
		ArrayList<InvestInfo> pageRecord = new ArrayList<InvestInfo>();
		pageRecord.addAll(allRecord);
		int maxSize = InvestConfig.getInstance().HISTORY_RECORD_MAX_COUNT.getValue();
		int size = pageRecord.size();
		if (size <= maxSize) {
			return SM_HistoryInfo.valueOf(new ArrayList<InvestInfo>(pageRecord.subList(0, size)));
		} else {
			SM_HistoryInfo result = SM_HistoryInfo.valueOf(new ArrayList<InvestInfo>(pageRecord.subList(0, maxSize)));
			List<InvestInfo> list = pageRecord.subList(0, maxSize);
			if (size > maxSize * 2) {
				for (InvestInfo info : pageRecord) {
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

	public LinkedList<InvestInfo> getAllRecord() {
		return allRecord;
	}

	public void setAllRecord(LinkedList<InvestInfo> allRecord) {
		this.allRecord = allRecord;
	}

	@JsonIgnore
	public InvestHistoryEntity getInvestHistoryEnity() {
		return investHistoryEnity;
	}

	@JsonIgnore
	public void setInvestHistoryEnity(InvestHistoryEntity investHistoryEnity) {
		this.investHistoryEnity = investHistoryEnity;
	}

}
