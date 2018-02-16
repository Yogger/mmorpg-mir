package com.mmorpg.mir.model.core.consumable;

import com.mmorpg.mir.model.core.consumable.model.CoreActionResource;
import com.mmorpg.mir.model.log.ModuleInfo;

public abstract class AbstractCoreAction {

	protected String code;

	protected int value;

	private String actionType;

	public abstract boolean verify(Object object);

	public abstract void act(Object object, ModuleInfo moduleInfo);

	public void init(CoreActionResource resource) {
		value = resource.getValue() == null? 0: resource.getValue();
		code = resource.getCode();
		init();
	}

	public void init() {

	}

	public void calculate(int num) {
		value *= num;
	}

	public AbstractCoreAction add(AbstractCoreAction action) {
		if (check(action)) {
			return doAdd(action);
		}
		return action;
	}

	public AbstractCoreAction doAdd(AbstractCoreAction action) {
		value += action.value;
		return null;
	}

	protected boolean check(AbstractCoreAction action) {
		return action.getClass().equals(getClass());
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

	public abstract AbstractCoreAction clone();

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

}
