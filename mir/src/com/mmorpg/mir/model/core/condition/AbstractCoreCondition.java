package com.mmorpg.mir.model.core.condition;

import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;

public abstract class AbstractCoreCondition {

	protected String code;
	protected int value;
	private boolean throwException = true;

	public abstract boolean verify(Object object);

	protected AbstractCoreCondition add(AbstractCoreCondition condition) {
		if (check(condition)) {
			return doAdd(condition);
		}
		return condition;
	}

	protected AbstractCoreCondition doAdd(AbstractCoreCondition condition) {
		this.value += condition.value;
		return null;
	}

	protected void errorObject(Object object) {
		throw new RuntimeException(String.format("不正确的条件判断类型 class[%s]", object.getClass()));
	}

	/**
	 * 带有resource参数的init方法。方便以后子类扩展新的字段
	 * 
	 * @param resource
	 */
	protected void init(CoreConditionResource resource) {
		this.value = resource.getValue();
		this.code = resource.getCode();
		this.init();
	}

	protected void init() {

	}

	protected void calculate(int num) {
		this.value *= num;
	}

	protected boolean check(AbstractCoreCondition condition) {
		if (!condition.getClass().equals(getClass())) {
			return false;
		}
		if (code == null && condition.getCode() != null) {
			return false;
		}

		if (code != null && !code.equals(condition.getCode())) {
			return false;
		}
		return true;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isThrowException() {
		return throwException;
	}

	public void setThrowException(boolean throwException) {
		this.throwException = throwException;
	}

}
