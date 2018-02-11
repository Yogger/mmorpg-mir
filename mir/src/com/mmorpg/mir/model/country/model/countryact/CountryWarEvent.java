package com.mmorpg.mir.model.country.model.countryact;

import java.util.ArrayList;

import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.model.Country;
import com.windforce.common.utility.New;

public enum CountryWarEvent {
	/** 敌国发布国家搬砖 **/
	ENEMY_COUNTRY_TEMPLE(1) {
		@Override
		public ArrayList<Integer> isLightUp(Country country) {
			ArrayList<Integer> result = New.arrayList();
			if (country.getCountryQuest().getTempleEndTime() > System.currentTimeMillis()) {
				result.add(country.getId().getValue());
			}
			return result;
		}
	},
	/** 敌国发布国家运镖 **/
	ENEMY_COUNTRY_EXPRSS(2) {
		@Override
		public ArrayList<Integer> isLightUp(Country country) {
			ArrayList<Integer> result = New.arrayList();
			if (country.getCountryQuest().getExpressEndTime() > System.currentTimeMillis()) {
				result.add(country.getId().getValue());
			}
			return result;
		}
	},
	/** 敌国大臣刷新 **/
	ENEMY_DIPLOMACY_RELIVE(3) {
		@Override
		public ArrayList<Integer> isLightUp(Country country) {
			return New.arrayList();
		}
	},
	/** 敌国国旗刷新 **/
	ENEMY_COUNTRYFLAG_RELIVE(4) {
		@Override
		public ArrayList<Integer> isLightUp(Country country) {
			return New.arrayList();
		}
	},
	/** 我国国旗被打 **/
	COUNTRYFLAG_UNDER_ATTACK(5) {
		@Override
		public ArrayList<Integer> isLightUp(Country country) {
			ArrayList<Integer> result = New.arrayList();
			/*if (!country.getCountryFlag().isNotAttacked()) {
				result.add(country.getId().getValue());
			}*/
			return result;
		}
	},
	/** 我国大臣被打 **/
	DIPLOMACY_UNDER_ATTACK(6) {
		@Override
		public ArrayList<Integer> isLightUp(Country country) {
			ArrayList<Integer> result = New.arrayList();
			if (!country.getDiplomacy().isNotAttacked()) {
				result.add(country.getId().getValue());
			}
			return result;
		}
	},
	/** 我国攻击敌国的国旗 **/
	ATTACK_COUNTRYFLAG(7) {

		@Override
		public ArrayList<Integer> isLightUp(Country country) {
			ArrayList<Integer> result = New.arrayList();
			/*for (Country c: CountryManager.getInstance().getCountries().values()) {
				boolean notSelf = c.getId() != country.getId();
				boolean underAttack = !c.getCountryFlag().isNotAttacked();
				if (notSelf && underAttack && 
						c.getCountryFlag().getLastAttackCountry() == country.getId().getValue()) {
					result.add(c.getId().getValue());
				}
			}*/
			return result;
		}
		
	},
	/** 我国攻击敌国的大臣 **/
	ATTACK_DIPLOMACY(8) {

		@Override
		public ArrayList<Integer> isLightUp(Country country) {
			ArrayList<Integer> result = New.arrayList();
			for (Country c: CountryManager.getInstance().getCountries().values()) {
				boolean notSelf = c.getId() != country.getId();
				boolean underAttack = !c.getDiplomacy().isNotAttacked();
				if (notSelf && underAttack && 
						c.getDiplomacy().getLastAttackCountry() == country.getId().getValue()) {
					result.add(c.getId().getValue());
				}
			}
			return result;
		}
		
	};
	
	private final int value;
	
	private CountryWarEvent(int v) {
		this.value = v;
	}

	public int getValue() {
		return value;
	}
	
	public abstract ArrayList<Integer> isLightUp(Country country);
}
