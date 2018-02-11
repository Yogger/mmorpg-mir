package com.mmorpg.mir.model.express.manager;

import java.util.List;

import com.mmorpg.mir.model.express.resource.ExpressResource;
import com.mmorpg.mir.model.gameobjects.Lorry;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.reward.model.Reward;
import com.windforce.common.resource.Storage;

public interface IExpressManager {
	void registerLorry(Lorry lorry);

	void unRegisterLorry(Lorry lorry);

	Double getRobRewardPercent();

	void resetSelect(Player player, boolean gold, boolean autoBuy);

	List<String> selectLorry(Player player, boolean gold, boolean initial);

	void express(final Player player, final String id, int sign);

	void expressCopy(final Player player, final String id);

	void reward(Player player, int sign);

	void getRecentOnAttackTime(Player player);

	void flyToLorry(Player player, int sign);

	Storage<String, ExpressResource> getExpressResources();

	ExpressResource getExpressResource(String key);

	int getOutOfOnAttackTime();

	int getRobRewardCountLimit();

	Reward createExpressReward(Lorry lorry, String chooserGroupId);

	void checkLorryInVisualRange(Player player, boolean canSeeLorry);

	void askForHelp(Player player);

	void getExpressNavigator(Player player);

	void robSpecifiedLorry(Player player, long targetId, int sign);
}
