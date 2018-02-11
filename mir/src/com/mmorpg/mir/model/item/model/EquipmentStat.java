package com.mmorpg.mir.model.item.model;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

public class EquipmentStat {
	public static final String STAT_NOT_EXIST = "None";
	public static final String GEM_NOT_EXIST = "";
	/** 类型 @see EquimentStatType */
	private int type;
	/** 包含属性 */
	private ArrayList<String> context;

	public static EquipmentStat valueOf(int type, String...resourceIds) {
		EquipmentStat stat = new EquipmentStat();
		stat.type = type;
		stat.context = New.arrayList();
		for (String s: resourceIds)
			stat.context.add(s);
		return stat;
	}
	
	@JsonIgnore
	public EquipmentStat copy() {
		EquipmentStat stat = new EquipmentStat();
		stat.type = this.type;
		stat.context = New.arrayList();
		if (this.context != null) {
			for (String s: this.context) {
				stat.context.add(s);
			}
		}
		return stat;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ArrayList<String> getContext() {
		return context;
	}

	public void setContext(ArrayList<String> context) {
		this.context = context;
	}

}
