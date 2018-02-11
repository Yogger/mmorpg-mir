package com.mmorpg.mir.model.reward.model.sample;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.apache.log4j.Logger;
import org.h2.util.New;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.slf4j.helpers.MessageFormatter;

import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardType;

/**
 * 奖励内容配置表
 * 
 * @author Kuang Hao
 * @since v1.0 2012-12-20
 * 
 */
public class RewardItemSample {
	private static final Logger logger = Logger.getLogger(RewardItemSample.class);
	/** 奖励类型 */
	private RewardType type;
	/** code */
	private String code;
	// /** 固定值,为了简化配置，如果固定值不为0会默认读取固定值的数值 */
	private int value;
	/** 公式计算字符 */
	private String formula;
	/** 编译后的 验证表达式 */
	private transient volatile Serializable formulaExp;
	/** 表达式上下文 */
	private static final ParserContext parserContext = new ParserContext();
	/** 上下文参数 */
	private Map<String, String> parms;

	static {
		/** 导入 {@link Math} 中的全部静态方法 */
		parserContext.addImport(Math.class);
		for (Method method : Math.class.getMethods()) {
			int mod = method.getModifiers();
			if (Modifier.isStatic(mod) && Modifier.isPublic(mod)) {
				String name = method.getName();
				parserContext.addImport(name, method);
			}
		}
	}

	public static void main(String[] args) {
		RewardItemSample sample = new RewardItemSample();
		// {STANDARD_EXP=1.4, STANDARD_COINS=0.4, WORLD_CLASS_BONUS=140,
		// LEVEL=120}
		sample.formula = "ceil((10+pow((LEVEL-1),STANDARD_EXP))*1260*(1+0.14*(max(WORLD_CLASS_BONUS-LEVEL,0))))";
		sample.formulaExp = MVEL.compileExpression(sample.formula, parserContext);
		System.out.println(sample.formulaExp);
		Map<String, Object> param = New.hashMap();
		param.put("STANDARD_EXP", 1.4);
		param.put("STANDARD_COINS", 0.4);
		param.put("LEVEL", 120);
		param.put("WORLD_CLASS_BONUS", 140);
		System.out.println(MVEL.executeExpression(sample.formulaExp, param, Integer.class));
		System.out.println(Math.ceil((Math.pow(119.0, 1.4) + 10) * 1260.0 * (1 + 0.14 * 20.0)));
	}

	/**
	 * 生成奖励单项
	 * 
	 * @param formulaCtx
	 * @return
	 */
	public RewardItem createRewardItem(Object formulaCtx) {
		try {
			int result = 0;
			RewardItem item = null;
			if (getValue() != 0) {
				result = getValue();
			} else if (formula != null) {
				if (formulaExp == null) {
					synchronized (this) {
						if (formulaExp == null) {
							formulaExp = MVEL.compileExpression(formula, parserContext);
						}
					}
				}
				result = MVEL.executeExpression(formulaExp, formulaCtx, Integer.class);
			}
			item = RewardItem.valueOf(type, code, result, parms);
			return item;
		} catch (RuntimeException e) {
			String message = MessageFormatter.format("奖励[{}]的表达式[{}]有误", new Object[] { formula, e }).getMessage();
			logger.error(message);
			return null;
		}
	}

	public RewardType getType() {
		return type;
	}

	public void setType(RewardType type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Map<String, String> getParms() {
		return parms;
	}

	public void setParms(Map<String, String> parms) {
		this.parms = parms;
	}

}
