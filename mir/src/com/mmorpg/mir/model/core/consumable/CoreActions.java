package com.mmorpg.mir.model.core.consumable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.purse.model.CurrencyType;

public class CoreActions {
	private ArrayList<AbstractCoreAction> actionList = new ArrayList<AbstractCoreAction>();

	/**
	 * 验证不通过不会抛出异常
	 * 
	 * @param object
	 * @return
	 */
	public boolean verify(Object object) {
		return this.verify(object, false);
	}

	/**
	 * 获取第一个道具消耗的key
	 * 
	 * @return
	 */
	public String getFirstItemKey() {
		for (AbstractCoreAction action : actionList) {
			if (action instanceof ItemAction) {
				return action.getCode();
			}
		}
		return null;
	}

	public Set<String> getAllItemKeys() {
		Set<String> items = new HashSet<String>();
		for (AbstractCoreAction action : actionList) {
			if (action instanceof ItemAction) {
				items.add(action.getCode());
			}
		}
		return items;
	}
	
	public long getCurrencyValue(CurrencyType currencyType) {
		long result = 0L;
		for (AbstractCoreAction action : actionList) {
			if (action instanceof CurrencyAction) {
				if (((CurrencyAction) action).getType() == currencyType) {
					result += action.getValue();
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @param object
	 * @param isThrowException
	 *            验证不通过抛出异常
	 * @return
	 */
	public boolean verify(Object object, boolean isThrowException) throws ManagedException {

		for (AbstractCoreAction action : actionList) {
			try {
				if (!action.verify(object)) {
					return false;
				}
			} catch (ManagedException e) {
				if (isThrowException) {
					throw e;
				}
				return false;
			}
		}

		return true;
	}

	public void act(Object object, ModuleInfo moduleInfo) {
		for (AbstractCoreAction action : actionList) {
			action.act(object, moduleInfo);
		}
	}

	public void addActions(CoreActions actions) {
		for (AbstractCoreAction ac : actions.actionList) {
			this.addAction(ac);
		}
	}

	public void addAction(AbstractCoreAction action) {
		AbstractCoreAction add = action;
		for (AbstractCoreAction temp : actionList) {
			add = temp.add(add);
			if (add == null) {
				break;
			}
		}
		if (add != null) {
			actionList.add(add.clone());
		}
	}

	public void addActions(AbstractCoreAction... actions) {
		for (AbstractCoreAction action : actions) {
			addAction(action);
		}
	}
	
	public boolean isEmpty() {
		return actionList.isEmpty();
	}
}
