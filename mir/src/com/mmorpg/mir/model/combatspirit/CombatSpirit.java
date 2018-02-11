package com.mmorpg.mir.model.combatspirit;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.combatspirit.manager.CombatSpiritManager;
import com.mmorpg.mir.model.combatspirit.model.CombatSpiritStorage.CombatSpiritType;
import com.mmorpg.mir.model.combatspirit.packet.SM_CombatSpirit_Up;
import com.mmorpg.mir.model.combatspirit.resource.CombatSpiritResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class CombatSpirit {
	private String combatResourceId;
	private int growUpValue;
	private int type;
	
	private int historyValue;
	private int todayValue;
	
	public static CombatSpirit valueOf(CombatSpiritType type) {
		CombatSpirit c = new CombatSpirit();
		c.type = type.getValue();
		c.combatResourceId = CombatSpiritManager.getInstance().COMBAT_SPIRIT_ID_INIT.getValue().get(type.name());
		return c;
	}

	public final String getCombatResourceId() {
		return combatResourceId;
	}

	public final void setCombatResourceId(String combatResourceId) {
		this.combatResourceId = combatResourceId;
	}

	public final int getGrowUpValue() {
		return growUpValue;
	}

	public final void setGrowUpValue(int growUpValue) {
		this.growUpValue = growUpValue;
	}
	
	@JsonIgnore
	public void addGrowUpValue(int inc, boolean special) {
		this.growUpValue += inc;
		if (special) {
			todayValue += inc;
			historyValue += inc;
		}
	}
	
	@JsonIgnore
	public void refresh() {
		todayValue = 0;
	}

	public final int getType() {
		return type;
	}

	public final void setType(int type) {
		this.type = type;
	}

	public int getHistoryValue() {
		return historyValue;
	}

	public void setHistoryValue(int historyValue) {
		this.historyValue = historyValue;
	}

	public int getTodayValue() {
		return todayValue;
	}

	public void setTodayValue(int todayValue) {
		this.todayValue = todayValue;
	}

	@JsonIgnore
	public void notifyUpgrade(Player owner, int addValue) {
		CombatSpiritResource resource = CombatSpiritManager.getInstance().getCombatSpiritResource(getCombatResourceId(), true);
		boolean crossLevel = (resource.getUpgradeNeed() == 0 || 
				(getGrowUpValue() < resource.getUpgradeNeed() && (getGrowUpValue() + addValue) >= resource.getUpgradeNeed()));
		if (resource.getNextId() != null && crossLevel) {
			PacketSendUtility.sendPacket(owner, SM_CombatSpirit_Up.valueOf(getType()));
		}
	}
	
}
