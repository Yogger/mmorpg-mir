package com.mmorpg.mir.model.item.resource;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class TreasureItemFixResource {

	@Id
	private String id;

	private String formulaId;

	private String sourceItem;

	private String targetItem;

	private String mailTitleIl18n;

	private String mailContentIl18n;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFormulaId() {
		return formulaId;
	}

	public void setFormulaId(String formulaId) {
		this.formulaId = formulaId;
	}

	public String getSourceItem() {
		return sourceItem;
	}

	public void setSourceItem(String sourceItem) {
		this.sourceItem = sourceItem;
	}

	public String getTargetItem() {
		return targetItem;
	}

	public void setTargetItem(String targetItem) {
		this.targetItem = targetItem;
	}

	public String getMailTitleIl18n() {
		return mailTitleIl18n;
	}

	public void setMailTitleIl18n(String mailTitleIl18n) {
		this.mailTitleIl18n = mailTitleIl18n;
	}

	public String getMailContentIl18n() {
		return mailContentIl18n;
	}

	public void setMailContentIl18n(String mailContentIl18n) {
		this.mailContentIl18n = mailContentIl18n;
	}

}
