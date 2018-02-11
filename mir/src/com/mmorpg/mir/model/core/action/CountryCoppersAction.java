package com.mmorpg.mir.model.core.action;

import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.country.model.CoppersType;
import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.packet.SM_Country_Coppers;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class CountryCoppersAction extends AbstractCoreAction {

	private CoppersType type;

	@Override
	public void act(Object object, ModuleInfo moduleInfo) {
		Country country = null;
		if (object instanceof Country) {
			country = (Country) object;
		}
		if (object instanceof Player) {
			country = ((Player) object).getCountry();
		}
		country.getCoppers().cost(type, value);
		if (object instanceof Player) {
			PacketSendUtility.sendPacket((Player) object,
					SM_Country_Coppers.valueOf(((Player) object).getCountry().getCoppers(), 0));
		}
	}

	@Override
	public void init() {
		setType(CoppersType.valueOf(Integer.valueOf(code)));
		
	}

	@Override
	public boolean verify(Object object) {
		Country country = null;
		if (object instanceof Country) {
			country = (Country) object;
		}
		if (object instanceof Player) {
			country = ((Player) object).getCountry();
		}
		return CoreConditionType.createCountryCurrencyCondition(getType(), value).verify(country);
	}

	public CoppersType getType() {
		return type;
	}

	public void setType(CoppersType type) {
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
		CountryCoppersAction countryCoppersAction = new CountryCoppersAction();
		countryCoppersAction.setCode(code);
		countryCoppersAction.setType(type);
		countryCoppersAction.setValue(value);
		return countryCoppersAction;
	}

}
