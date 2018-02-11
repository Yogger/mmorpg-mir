package com.mmorpg.mir.model.gameobjects;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.windforce.common.utility.JsonUtils;

public class Status {
	private int f;
	private int t;
	private String[] cdn;
	@Transient
	private CoreConditions conditions;

	public static void main(String[] args) {
		Status s = new Status();
		s.f = 0;
		s.t = 1;
		s.cdn = new String[] { "FOREVERTRUE" };

		System.out.println(JsonUtils.object2String(new Status[] { s, s }));
	}

	public int getF() {
		return f;
	}

	public void setF(int f) {
		this.f = f;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public String[] getCdn() {
		return cdn;
	}

	public void setCdn(String[] cdn) {
		this.cdn = cdn;
	}

	@JsonIgnore
	public CoreConditions getConditions() {
		if (conditions == null) {
			if (cdn == null) {
				conditions = new CoreConditions();
			} else {
				conditions = CoreConditionManager.getInstance().getCoreConditions(1, cdn);
			}
		}
		return conditions;
	}

	public void setConditions(CoreConditions conditions) {
		this.conditions = conditions;
	}

}
