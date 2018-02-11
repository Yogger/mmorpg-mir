package com.mmorpg.mir.model.welfare.manager;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.moduleopen.event.ModuleOpenEvent;
import com.mmorpg.mir.model.player.event.LevelUpEvent;
import com.mmorpg.mir.model.player.event.LoginEvent;
import com.mmorpg.mir.model.player.event.LogoutEvent;
import com.mmorpg.mir.model.vip.event.FirstPayEvent;
import com.mmorpg.mir.model.warship.event.WarshipEvent;
import com.mmorpg.mir.model.welfare.event.BossDieEvent;
import com.mmorpg.mir.model.welfare.event.CopyEvent;
import com.mmorpg.mir.model.welfare.event.ExpressEvent;
import com.mmorpg.mir.model.welfare.event.InvestigateEvent;
import com.mmorpg.mir.model.welfare.event.RescueEvent;
import com.mmorpg.mir.model.welfare.event.TempleEvent;

public interface IEventManager {
	void loginoutEvent(LogoutEvent event);

	void loginEvent(LoginEvent event);

	void levelUpEvent(LevelUpEvent event);

//	void questCompletEvent(QuestCompletEvent event);

	void bossDieEvent(BossDieEvent event);

//	void currencyActionEvent(CurrencyActionEvent event);

	void copyEvent(CopyEvent event);

//	void countryBuyEvent(CountryBuyEvent event);

//	void enhanceEquipmentEvent(EnhanceEquipmentEvent event);

	void expressEvent(ExpressEvent event);

	void investigateEvent(InvestigateEvent event);

	void rescueEvent(RescueEvent event);

//	void signEvent(SignEvent event);

//	void smeltEquipmentEvent(SmeltEquipmentEvent event);

	void templeEvent(TempleEvent event);

//	void vipEvent(VipEvent event);

	void restCopy(Player player);

	void moduleOpenEvent(ModuleOpenEvent event);

//	void countrySacrificeEvent(CountrySacrificeEvent event);

//	void upgradeCombatSpiritEvent(CombatSpiritUpEvent event);

	void warshipKing(WarshipEvent event);

	void firstPayEvent(FirstPayEvent event);

//	void exerciseEvent(ExerciseStartEvent event);
}
