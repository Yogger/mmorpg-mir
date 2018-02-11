package com.mmorpg.mir.model.rank.manager;

import java.util.Map;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.event.BattleScoreRefreshEvent;
import com.mmorpg.mir.model.promote.event.PromotionEvent;
import com.mmorpg.mir.model.rank.model.RankRow;
import com.mmorpg.mir.model.rank.model.RankType;
import com.mmorpg.mir.model.rank.model.rank.CountryRank;
import com.mmorpg.mir.model.rank.model.rank.WorldRank;
import com.windforce.common.event.event.IEvent;

public interface IWorldRankManager {
	public int getWorldLevel();

	public void updateRank(WorldRank rank);

	public void updateCountryRank(CountryRank rank);

	public void submitCountryRankRow(int countryValue, BattleScoreRefreshEvent event);

	public void submitRankRow(Player sender, RankType type, IEvent event);

	public void getRankByType(Player player, RankType type, int page);

	public Map<Integer, RankRow> getHeroRank(Player player, boolean todayHero);

	public RankType getHeroType(int countryValue, boolean todayHero);

	public void rewardHero(Player player);

	public void changeMyRankRowInfo(RankType type, IEvent event);

	public void searchCountryPowerPlayer(Player player);
	
	public int getWeakestCountry();
	
	public boolean yesterdayHeroReward(Player player);
	
	public void refreshByPromotionEvent(PromotionEvent event);
}
