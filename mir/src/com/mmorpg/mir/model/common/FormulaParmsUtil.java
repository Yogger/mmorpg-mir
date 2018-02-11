package com.mmorpg.mir.model.common;

import java.util.HashMap;
import java.util.Map;

import com.mmorpg.mir.model.formula.Formula;

/**
 * 公式参数构建工具
 * 
 * @author Kuang Hao
 * @since v1.0 2013-3-28
 * 
 */
public class FormulaParmsUtil {
	private Map<String, Object> parms;
	private Formula formula;

	public static FormulaParmsUtil valueOf() {
		FormulaParmsUtil util = new FormulaParmsUtil();
		return util;
	}

	public static FormulaParmsUtil valueOf(Formula formula) {
		FormulaParmsUtil util = new FormulaParmsUtil();
		util.formula = formula;
		return util;
	}

	public FormulaParmsUtil addParm(String key, Object value) {
		if (parms == null) {
			parms = new HashMap<String, Object>();
		}
		this.parms.put(key, value);
		return this;
	}

	public Object getValue() {
		return this.getFormula().calculate(parms);
	}

	public Map<String, Object> getMap() {
		if (parms == null) {
			throw new RuntimeException("empty parms");
		}
		return this.parms;
	}

	public Formula getFormula() {
		return formula;
	}

	public void setFormula(Formula formula) {
		this.formula = formula;
	}
}
