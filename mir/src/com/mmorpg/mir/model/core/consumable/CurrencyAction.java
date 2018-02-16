package com.mmorpg.mir.model.core.consumable;

import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.purse.CurrencyUtils;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.task.tasks.PacketBroadcaster.BroadcastMode;
import com.mmorpg.mir.model.welfare.event.CurrencyActionEvent;
import com.windforce.common.event.core.EventBusManager;

public class CurrencyAction extends AbstractCoreAction {

	private transient CurrencyType type;

	@Override
	public void act(Object object, ModuleInfo moduleInfo) {
		Player player = (Player) object;
		CurrencyUtils.getInstance().costByLog(player, getType(), value, moduleInfo);
		player.addPacketBroadcastMask(BroadcastMode.UPDATE_PLAYER_PURSE);
		EventBusManager.getInstance().submit(CurrencyActionEvent.valueOf(player, getType(), value));
	}
	
	public void tradeAct(Object object, ModuleInfo moduleInfo) {
		Player player = (Player) object;
		CurrencyUtils.getInstance().costByLog(player, getType(), value, moduleInfo);
		player.addPacketBroadcastMask(BroadcastMode.UPDATE_PLAYER_PURSE);
	}

	@Override
	public void init() {
		setType(CurrencyType.valueOf(Integer.valueOf(code)));
		setActionType(CoreActionType.CURRENCY.name());
	}

	@Override
	public boolean verify(Object object) {
		Player player = (Player) object;
		return CoreConditionType.createCurrencyCondition(getType(), value).verify(player);
	}

	public CurrencyType getType() {
		return type;
	}

	public void setType(CurrencyType type) {
		this.type = type;
	}

	@Override
	protected boolean check(AbstractCoreAction action) {
		if (super.check(action)) {
			if (code.equals(action.getCode())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public AbstractCoreAction clone() {
		CurrencyAction currencyAction = new CurrencyAction();
		currencyAction.setCode(code);
		currencyAction.setType(type);
		currencyAction.setValue(value);
		currencyAction.setActionType(getActionType());
		return currencyAction;
	}

}
