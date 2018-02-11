package com.mmorpg.mir.model.core.action.model;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;

import com.mmorpg.mir.model.core.action.CoreActionType;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class CoreActionResource {
	@Id
	private String key;

	private CoreActionType type;

	private String formula;

	private String code;

	private Integer value;

	/** 编译后的 验证表达式 */
	@JsonIgnore
	private transient volatile Serializable formulaExp;
	/** 表达式上下文 */
	@JsonIgnore
	private static final ParserContext parserContext = new ParserContext();

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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public CoreActionType getType() {
		return type;
	}

	public void setType(CoreActionType type) {
		this.type = type;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@JsonIgnore
	public int calculateValue(Map<String, Object> context) {
		if (value != null) {
			return value;
		}

		if (formulaExp == null) {
			synchronized (this) {
				if (formula == null) {
					return 0;
				}
				if (formulaExp == null) {
					formulaExp = MVEL.compileExpression(formula, parserContext);
				}
			}
		}
		return MVEL.executeExpression(formulaExp, context, Integer.class);
	}
	
	

}
