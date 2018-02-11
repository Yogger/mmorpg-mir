package com.mmorpg.mir.model.country.model;

import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.country.manager.CountryManager;
import com.mmorpg.mir.model.country.resource.CountryAuthorityResource;
import com.mmorpg.mir.model.gameobjects.Player;

public enum CountryOfficial {
	/** 国王 */
	KING(0) {
		@Override
		public CoreConditions getAuthority(CountryAuthorityResource resource) {
			return resource.getKingConditions();
		}
	},
	/** 大司马 */
	PREMIER(1) {
		@Override
		public CoreConditions getAuthority(CountryAuthorityResource resource) {
			return resource.getPermierConditions();
		}
	},
	/** 御史大夫 */
	MINISTER(2) {
		@Override
		public CoreConditions getAuthority(CountryAuthorityResource resource) {
			return resource.getMinisterConditions();
		}
	},
	/** 大将军 */
	GENERALISSIMO(3) {
		@Override
		public CoreConditions getAuthority(CountryAuthorityResource resource) {
			return resource.getGeneralissimoConditions();
		}
	},
	/** 车骑将军 */
	GENERAL(4) {
		@Override
		public CoreConditions getAuthority(CountryAuthorityResource resource) {
			return resource.getGeneralConditions();
		}
	},
	/** 平民 */
	CITIZEN(5) {
		@Override
		public CoreConditions getAuthority(CountryAuthorityResource resource) {
			return resource.getCitizenConditions();
		}
	},
	/** 卫队 */
	GUARD(6) {
		@Override
		public CoreConditions getAuthority(CountryAuthorityResource resource) {
			return resource.getGuardCondition();
		}
	};

	private int value;

	public CountryAuthorityResource getResource(String id) {
		return CountryManager.getAuthorityResource(id);
	}

	public abstract CoreConditions getAuthority(CountryAuthorityResource resource);

	public boolean authority(String id, Player player) {
		return getAuthority(getResource(id)).verify(player, true);
	}

	public static CountryOfficial typeOf(String name) {
		for (CountryOfficial type : values()) {
			if (type.name().equals(name)) {
				return type;
			}
		}
		throw new IllegalArgumentException("无效的官职类型[" + name + "]");
	}

	private CountryOfficial(int code) {
		this.value = code;
	}

	public int getValue() {
		return this.value;
	}
}
