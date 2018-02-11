package com.mmorpg.mir.model.blackshop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.blackshop.BlackShopConfig;
import com.mmorpg.mir.model.blackshop.packet.SM_BlackShop_Buy;
import com.mmorpg.mir.model.blackshop.packet.SM_BlackShop_Refresh;
import com.mmorpg.mir.model.blackshop.resource.BlackShopResource;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chat.model.show.object.ItemShow;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.item.core.ItemManager;
import com.mmorpg.mir.model.item.resource.ItemResource;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.reward.manager.RewardManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.SelectRandom;

public class BlackShop {
	private int version;

	@Transient
	private transient Player owner;

	// 活动期间的限购商品
	private Map<String, Integer> restrictGoods;

	// 当前显示商品
	private List<BlackShopGood> goods;

	private long lastRefreshTime;

	public static BlackShop valueOf(BlackShopServer blackShopServer) {
		BlackShop result = new BlackShop();
		result.version = 0;
		result.goods = new ArrayList<BlackShopGood>();
		result.restrictGoods = new HashMap<String, Integer>();
		return result;
	}

	@JsonIgnore
	public boolean checkCanRefresh() {
		long now = System.currentTimeMillis();
		long nextRefreshTime = lastRefreshTime + BlackShopConfig.getInstance().SHOP_REFRESH_INTERVAL_TIME.getValue()
				* DateUtils.MILLIS_PER_MINUTE;
		return nextRefreshTime < now;
	}

	@JsonIgnore
	private List<String> choose() {
		String goodGroupId = ServerState.getInstance().getBlackShopServer().getGoodGroupId();
		int gridCount = BlackShopConfig.getInstance().SHOP_GRID_COUNT.getValue();
		List<BlackShopResource> storage = BlackShopConfig.getInstance().blackShopStorage.getIndex(
				BlackShopResource.GROUP_INDEX, goodGroupId);
		SelectRandom<String> selector = new SelectRandom<String>();
		for (BlackShopResource resource : storage) {
			boolean contained = restrictGoods.containsKey(resource.getId());
			if (resource.isRestrictGood() && contained) {
				int count = restrictGoods.get(resource.getId());
				if (count >= resource.getRestrictCount()) {
					continue;
				}
			}
			selector.addElement(resource.getId(), resource.getWeight());
		}
		return selector.run(gridCount);
	}

	@JsonIgnore
	synchronized public void refresh(boolean systemRefresh, boolean sended) {
		BlackShopServer server = ServerState.getInstance().getBlackShopServer();
		if (this.version != server.getVersion()) {
			this.version = server.getVersion();
			this.lastRefreshTime = server.getBeginTime();
			restrictGoods.clear();
		}
		this.goods.clear();
		List<String> goodIds = choose();
		Collections.shuffle(goodIds);
		for (String idNew : goodIds) {
			BlackShopGood good = BlackShopGood.valueOf(idNew);
			this.goods.add(good);
			BlackShopResource resource = BlackShopConfig.getInstance().blackShopStorage.get(idNew, true);
			if (resource.isRestrictGood() && !restrictGoods.containsKey(idNew)) {
				restrictGoods.put(idNew, 0);
			}
		}
		long now = System.currentTimeMillis();
		if (systemRefresh) {
			long intervalTime = BlackShopConfig.getInstance().SHOP_REFRESH_INTERVAL_TIME.getValue()
					* DateUtils.MILLIS_PER_MINUTE;
			int addCount = (int) ((now - this.lastRefreshTime) / intervalTime);
			this.lastRefreshTime += addCount * intervalTime;
		} else {
			this.lastRefreshTime = now;
		}

		if (sended) {
			PacketSendUtility.sendPacket(owner, SM_BlackShop_Refresh.valueOf(systemRefresh, this));
		}
	}

	@JsonIgnore
	synchronized public void buy(int gridIndex) {
		BlackShopGood good = goods.get(gridIndex);
		BlackShopResource resource = BlackShopConfig.getInstance().blackShopStorage.get(good.getId(), true);
		if (good.getCount() >= 1) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		int gridBuyCountMax = resource.getRestrictCount();
		if (resource.isRestrictGood() && restrictGoods.get(resource.getId()) >= gridBuyCountMax) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		CoreActions buyActions = resource.getBuyActions();
		if (!buyActions.verify(owner, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		buyActions.act(owner, ModuleInfo.valueOf(ModuleType.BLACKSHOP, SubModuleType.BLACKSHOP_BUY_ACT, resource.getId()));

		Reward reward = RewardManager.getInstance().grantReward(owner, resource.getRewardId(),
				ModuleInfo.valueOf(ModuleType.BLACKSHOP, SubModuleType.BLACKSHOP_BUY_REWARD, resource.getId()));
		good.addCount();
		if (resource.isRestrictGood()) {
			restrictGoods.put(resource.getId(), restrictGoods.get(resource.getId()) + 1);
		}
		PacketSendUtility.sendPacket(owner, SM_BlackShop_Buy.valueOf(gridIndex, good));
		broadCast(reward);
	}

	@JsonIgnore
	private void broadCast(Reward reward) {
		List<RewardItem> rewardItems = reward.getItemsByType(RewardType.ITEM);
		for (RewardItem rewardItem : rewardItems) {
			ItemResource itemResource = ItemManager.getInstance().getResource(rewardItem.getCode());
			if (itemResource.getQuality() >= BlackShopConfig.getInstance().SHOP_NOTICE_ITEM_QUALITY.getValue()) {
				ItemShow show = new ItemShow();
				show.setOwner(owner.getName());
				show.setKey(rewardItem.getCode());
				show.setItem(owner.getPack().getItemByKey(rewardItem.getCode()));

				I18nUtils utils = I18nUtils.valueOf("20302");
				utils.addParm("name", I18nPack.valueOf(owner.getName()));
				utils.addParm("country", I18nPack.valueOf(owner.getCountry().getName()));
				utils.addParm(I18NparamKey.ITEM, I18nPack.valueOf(show));
				ChatManager.getInstance().sendSystem(11001, utils, null);

				I18nUtils tvUtils = I18nUtils.valueOf("309202");
				tvUtils.addParm("name", I18nPack.valueOf(owner.getName()));
				tvUtils.addParm("country", I18nPack.valueOf(owner.getCountry().getName()));
				tvUtils.addParm(I18NparamKey.ITEM, I18nPack.valueOf(show));
				ChatManager.getInstance().sendSystem(0, tvUtils, null);
			}
		}
	}

	// getter-setter
	public List<BlackShopGood> getGoods() {
		return goods;
	}

	public void setGoods(List<BlackShopGood> goods) {
		this.goods = goods;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	public Map<String, Integer> getRestrictGoods() {
		return restrictGoods;
	}

	public void setRestrictGoods(Map<String, Integer> restrictGoods) {
		this.restrictGoods = restrictGoods;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
