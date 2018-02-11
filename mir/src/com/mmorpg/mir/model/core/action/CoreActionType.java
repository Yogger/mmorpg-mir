package com.mmorpg.mir.model.core.action;

import com.mmorpg.mir.model.purse.model.CurrencyType;

public enum CoreActionType {

	HP {
		@SuppressWarnings("unchecked")
		@Override
		public HpAction create() {
			return new HpAction();
		}

	},
	SKILL_MP {
		@SuppressWarnings("unchecked")
		@Override
		public SkillMpAction create() {
			return new SkillMpAction();
		}

	},
	SKILL_DP {
		@SuppressWarnings("unchecked")
		@Override
		public SkillDpAction create() {
			return new SkillDpAction();
		}

	},

	ITEM {

		@SuppressWarnings("unchecked")
		@Override
		public ItemAction create() {
			return new ItemAction();
		}

	},
	COUNTRY_CURRENCY {
		@SuppressWarnings("unchecked")
		@Override
		public CountryCoppersAction create() {
			return new CountryCoppersAction();
		}

	},

	CURRENCY {
		@SuppressWarnings("unchecked")
		@Override
		public CurrencyAction create() {
			return new CurrencyAction();
		}
	},
	
	LIMITED_RESOURCE {
		@SuppressWarnings("unchecked")
		@Override
		public ServerLimitAction create() {
			return new ServerLimitAction();
		}
	};

	public abstract <T extends AbstractCoreAction> T create();

	public static CurrencyAction createCurrencyCondition(CurrencyType type, int value) {
		CurrencyAction currencyAction = new CurrencyAction();
		currencyAction.setType(type);
		currencyAction.value = value;
		return currencyAction;
	}

	public static ItemAction createItemCondition(String code, int value) {
		ItemAction itemAction = new ItemAction();
		itemAction.code = code;
		itemAction.value = value;
		return itemAction;
	}

	/**
	 * 由于不同的action需要的参数不同，所以不建议使用这个类型的action构建方法。添加子类的构造方法请自行实现独立的构造方法
	 * 
	 * @param type
	 * @param code
	 * @param value
	 * @return
	 */
	@Deprecated
	public static <T extends AbstractCoreAction> T createAction(CoreActionType type, String code, int value) {
		T action = type.create();
		action.code = code;
		action.value = value;
		action.init();
		return action;
	}
}
