package com.mmorpg.mir.model.value;

import org.springframework.util.StringUtils;

public class ValueUtils {

	public static String getHead(String formula) {
		return StringUtils.trimLeadingCharacter(formula.split("\\.")[0], '$');
	}

	public static String getBody(String formula, int index) {
		return StringUtils.trimLeadingCharacter(formula, '$').split("\\.")[index];
	}
}
