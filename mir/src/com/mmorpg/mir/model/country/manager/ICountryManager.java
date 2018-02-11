package com.mmorpg.mir.model.country.manager;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

import com.mmorpg.mir.model.country.model.Country;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.country.packet.SM_Country_Open_Diplomacy;
import com.mmorpg.mir.model.country.packet.SM_Country_Open_Flag;
import com.mmorpg.mir.model.country.resource.CountryFactoryResource;
import com.mmorpg.mir.model.country.resource.CountryFlagResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.resource.Storage;
import com.windforce.common.scheduler.impl.SimpleScheduler;

public interface ICountryManager {
	boolean throwManagedException(String code);

	void initAll();

	void update(Country country);

	void appoint(Player player, Player target, String offical, int index);

	void forbidChat(Player player, Player target, int sign);

	void startExpress(final Player player, int sign);

	void startTemple(final Player player, int sign);

	void distributeTank(Player player, Player target, int tankId, int sign);

	void callbackTank(Player player, int tankId, int sign);

	void openCivilSalary(Player player, int sign);

	void checkSalaryStatus(Player player);

	void recevieCivilSalary(Player player, int sign);

	void recevieOfficialSalary(Player player, int sign);

	void openOfficialSalary(Player player, int sign);

	void appointGurad(Player player, Player target, int index);

	void deposeGurad(Player player, Player target);

	void depose(Player player, Player target);

	void setTraitor(Player player, Player target, int sign);

	void upgradeCountryShop(Player player);

	void upgradeDoor(Player player);

	void distributeTogetherToken(Player player, Player target, int sign);

	void callTogether(Player player, int sign);

	void callTogetherToken(Player player, int sign);

	void callTogetherGuard(Player player, int sign);

	void upgradeFlag(Player player);

	void upgradeFactory(Player player);

	void setNotice(Player player, String notice) throws UnsupportedEncodingException;

	void contribution(Player player, String type, int count, int sign);

	void contributionShop(Player player, String itemId, int count);

	void buy(Player player, String id, int count);

	void store(Player player, Set<Integer> indexs, boolean inPack);

	void countryMovePack(Player player, byte type, int fromIndex, int toIndex, int sign, boolean inPack);

	void take(Player player, Set<Integer> indexs, boolean inPack);

	void createTank(Player player, String id, int index, int sign);

	public void getFactory(Player player);

	public void getFlag(Player player);

	public void getOffers(Player player);

	public void getStorge(Player player);

	public void getShop(Player player);

	public void getOfficalList(Player player);

	public void getDoor(Player player);

	public void upgradeTank(Player player, int id, int sign);

	public Country getCountry(Player player);

	public void refreshCountryShop();

	public void refreshConctrol();

	public void refreshSalary();

	public void refreshForbidChat();

	public SM_Country_Open_Diplomacy openDiplomacyPanel(Player player);

	public SM_Country_Open_Flag openFlagPanel(Player player);

	public Storage<String, CountryFactoryResource> getCountryFactoryResources();

	public void registerPlayer(Player player);

	public void unRegisterPlayer(Player player);

	public Map<CountryId, Country> getCountries();

	public Storage<String, CountryFlagResource> getCountryFlagResources();

	public void setCountryFlagResources(Storage<String, CountryFlagResource> countryFlagResources);

	public SimpleScheduler getSimpleScheduler();

	public void setSimpleScheduler(SimpleScheduler simpleScheduler);

	public void seeAllLog(Player player);

	public void seeFeteLog(Player player, boolean all);

	public void sacrifice(Player player, int type, int sign);

	public void mobilization(Player player, String phrases, int sign);

	public void getHiddenMissionInfo(Player player, int type);

	public int getHiddenMissionLeftCount(Player player, int type);

	public void queryMobilization(Player player);

	public void getDiplomacyDamageRank(Player player, int country);

	public void getCountryFlagDamageRank(Player player, int country);

	public boolean canRecieveOfficialSalary(Player player);

	public boolean canRecieveCivilSalary(Player player);
}
