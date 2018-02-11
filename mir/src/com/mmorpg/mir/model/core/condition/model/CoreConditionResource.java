package com.mmorpg.mir.model.core.condition.model;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.mvel2.ParserContext;

import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.core.condition.Operator;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;
import com.windforce.common.utility.JsonUtils;

@Resource
public class CoreConditionResource {
	@Id
	private String key;

	private String typeString;

	private transient CoreConditionType type;

	private String formula;

	private String code;

	private int value;

	private String startDate;

	private String endDate;

	private String taskCtxKey;

	private String taskCtxValue;

	private int low;

	private int high;

	private int x;

	private int y;

	private int halfX;

	private int halfY;

	private int svrErrorCode;

	private String operator;

	private String itemId;

	public static void main(String[] args) {
		CoreConditionResource resource = new CoreConditionResource();
		resource.type = CoreConditionType.QUEST_KEYVALUE;
		resource.code = "MONSTER_HUNT";
		resource.value = 10;
		System.out.println(JsonUtils.object2String(resource));
		String json = "{\"formula\":null,\"startDate\":null,\"endDate\":null}";
		System.out.println(JsonUtils.string2Object(json, CoreConditionResource.class));

	}

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

	/**
	 * 构建货币conditon,如果要构建条件请直接调用CoreConditionType
	 * 
	 * @see CoreConditionType
	 * @param currencyType
	 * @param value
	 * @return
	 */
	@JsonIgnore
	@Deprecated
	public static CoreConditionResource createCurrenyCondition(CurrencyType currencyType, int value) {
		return createCondition(CoreConditionType.CURRENCY, currencyType.getValue() + "", value);
	}

	/**
	 * 构建道具condition，如果要构建条件请直接调用CoreConditionType
	 * 
	 * @see CoreConditionType
	 * @param key
	 * @param value
	 * @return
	 */
	@JsonIgnore
	@Deprecated
	public static CoreConditionResource createItemCondition(String key, int value) {
		return createCondition(CoreConditionType.ITEM, key, value);
	}

	@JsonIgnore
	public Operator getOperatorEnum() {
		return Operator.typeOf(this.operator);
	}

	public static CoreConditionResource createCondition(CoreConditionType type, String code, int value) {
		CoreConditionResource resource = new CoreConditionResource();
		resource.type = type;
		resource.code = code;
		resource.value = value;
		return resource;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public CoreConditionType getType() {
		return type;
	}

	public void setType(CoreConditionType type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
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

	// @JsonIgnore
	// public int calculateValue(Map<String, Object> context) {
	// if (value != 0 || formula == null) {
	// return value;
	// }
	//
	// if (formulaExp == null) {
	// synchronized (this) {
	// if (formula == null) {
	// return 0;
	// }
	// if (formulaExp == null) {
	// formulaExp = MVEL.compileExpression(formula, parserContext);
	// }
	// }
	// }
	// return MVEL.executeExpression(formulaExp, context, Integer.class);
	// }

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTaskCtxKey() {
		return taskCtxKey;
	}

	public void setTaskCtxKey(String taskCtxKey) {
		this.taskCtxKey = taskCtxKey;
	}

	public String getTaskCtxValue() {
		return taskCtxValue;
	}

	public void setTaskCtxValue(String taskCtxValue) {
		this.taskCtxValue = taskCtxValue;
	}

	public int getLow() {
		return low;
	}

	public void setLow(int low) {
		this.low = low;
	}

	public int getHigh() {
		return high;
	}

	public void setHigh(int high) {
		this.high = high;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHalfX() {
		return halfX;
	}

	public void setHalfX(int halfX) {
		this.halfX = halfX;
	}

	public int getHalfY() {
		return halfY;
	}

	public void setHalfY(int halfY) {
		this.halfY = halfY;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getTypeString() {
		return typeString;
	}

	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}

	public int getSvrErrorCode() {
		return svrErrorCode;
	}

	public void setSvrErrorCode(int svrErrorCode) {
		this.svrErrorCode = svrErrorCode;
	}

}
