package com.mmorpg.mir.model.commonactivity.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.chat.model.show.object.ItemShow;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.commonactivity.CommonActivityConfig;
import com.mmorpg.mir.model.commonactivity.packet.SM_CommonMarcoShop_Buy;
import com.mmorpg.mir.model.commonactivity.packet.SM_CommonMarcoShop_Refresh;
import com.mmorpg.mir.model.commonactivity.resource.CommonMarcoShopGoodResource;
import com.mmorpg.mir.model.commonactivity.resource.CommonMarcoShopResource;
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
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.SelectRandom;

public class CommonMarcoShop {
	private int version;

	/** 活动名字 */
	private String activityName;

	// 活动期间的限购商品
	private Map<String, Integer> restrictGoods;

	// 当前显示商品
	private List<CommonMarcoShopGood> goods;

	private long lastRefreshTime;

	public static CommonMarcoShop valueOf(String activityName) {
		CommonMarcoShop result = new CommonMarcoShop();
		result.activityName = activityName;
		result.version = 0;
		result.goods = new ArrayList<CommonMarcoShopGood>();
		result.restrictGoods = new HashMap<String, Integer>();
		return result;
	}

	@JsonIgnore
	public boolean checkCanRefresh() {
		long now = System.currentTimeMillis();
		CommonMarcoShopResource resource = getMarcoShopResource();
		long nextRefreshTime = lastRefreshTime + resource.getIntervalRefreshTime() * DateUtils.MILLIS_PER_MINUTE;
		return nextRefreshTime < now;
	}

	@JsonIgnore
	private List<String> choose() {
		CommonMarcoShopResource resource = getMarcoShopResource();
		int gridCount = resource.getGridCount();
		List<CommonMarcoShopGoodResource> storage = CommonActivityConfig.getInstance().commonShopGoodStorage.getIndex(
				CommonMarcoShopGoodResource.ACTIVITY_NAME, this.activityName);
		SelectRandom<String> selector = new SelectRandom<String>();
		for (CommonMarcoShopGoodResource goodResource : storage) {
			boolean contained = restrictGoods.containsKey(goodResource.getId());
			if (goodResource.isRestrictGood() && contained) {
				int count = restrictGoods.get(goodResource.getId());
				if (count >= goodResource.getRestrictCount()) {
					continue;
				}
			}
			selector.addElement(goodResource.getId(), goodResource.getWeight());
		}
		return selector.run(gridCount);
	}

	@JsonIgnore
	synchronized public void refresh(Player owner, boolean systemRefresh, boolean sended) {
		this.goods.clear();
		List<String> goodIds = choose();
		Collections.shuffle(goodIds);
		for (String idNew : goodIds) {
			CommonMarcoShopGood good = CommonMarcoShopGood.valueOf(idNew);
			this.goods.add(good);
			CommonMarcoShopGoodResource resource = CommonActivityConfig.getInstance().commonShopGoodStorage.get(idNew,
					true);
			if (resource.isRestrictGood() && !restrictGoods.containsKey(idNew)) {
				restrictGoods.put(idNew, 0);
			}
		}
		long now = System.currentTimeMillis();
		if (systemRefresh) {
			long intervalTime = getMarcoShopResource().getIntervalRefreshTime() * DateUtils.MILLIS_PER_MINUTE;
			int addCount = (int) ((now - this.lastRefreshTime) / intervalTime);
			this.lastRefreshTime += addCount * intervalTime;
		} else {
			this.lastRefreshTime = now;
		}

		if (sended) {
			PacketSendUtility.sendPacket(owner, SM_CommonMarcoShop_Refresh.valueOf(systemRefresh, this));
		}
	}

	@JsonIgnore
	synchronized public void buy(Player owner, int gridIndex) {
		CommonMarcoShopGood good = goods.get(gridIndex);
		CommonMarcoShopGoodResource resource = getMarcoShopGoodResource(good.getId());
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
		buyActions.act(owner,
				ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE, SubModuleType.MARCOSHOP_BUY_ACT, resource.getId()));

		Reward reward = RewardManager.getInstance().grantReward(owner, resource.getRewardId(),
				ModuleInfo.valueOf(ModuleType.COMMON_ACTIVE, SubModuleType.MARCOSHOP_BUY_REWARD, resource.getId()));
		good.addCount();
		if (resource.isRestrictGood()) {
			restrictGoods.put(resource.getId(), restrictGoods.get(resource.getId()) + 1);
		}
		PacketSendUtility.sendPacket(owner, SM_CommonMarcoShop_Buy.valueOf(gridIndex, this.activityName, good));
		broadCast(owner, reward);
	}

	@JsonIgnore
	private void broadCast(Player owner, Reward reward) {
		CommonMarcoShopResource resource = getMarcoShopResource();
		List<RewardItem> rewardItems = reward.getItemsByType(RewardType.ITEM);
		for (RewardItem rewardItem : rewardItems) {
			ItemResource itemResource = ItemManager.getInstance().getResource(rewardItem.getCode());
			if (itemResource.getQuality() >= resource.getNoticeItemQuility()) {
				ItemShow show = new ItemShow();
				show.setOwner(owner.getName());
				show.setKey(rewardItem.getCode());
				show.setItem(owner.getPack().getItemByKey(rewardItem.getCode()));

				I18nUtils utils = I18nUtils.valueOf(resource.getChatIl18n());
				utils.addParm("name", I18nPack.valueOf(owner.getName()));
				utils.addParm("country", I18nPack.valueOf(owner.getCountry().getName()));
				utils.addParm(I18NparamKey.ITEM, I18nPack.valueOf(show));
				ChatManager.getInstance().sendSystem(resource.getChatChannel(), utils, null);

				I18nUtils tvUtils = I18nUtils.valueOf(resource.getTvIl18n());
				tvUtils.addParm("name", I18nPack.valueOf(owner.getName()));
				tvUtils.addParm("country", I18nPack.valueOf(owner.getCountry().getName()));
				tvUtils.addParm(I18NparamKey.ITEM, I18nPack.valueOf(show));
				ChatManager.getInstance().sendSystem(resource.getTvChannel(), tvUtils, null);
			}
		}
	}

	@JsonIgnore
	private CommonMarcoShopResource getMarcoShopResource() {
		return CommonActivityConfig.getInstance().commonShopStorage.getUnique(CommonMarcoShopResource.ACTIVITY_NAME,
				this.activityName);
	}

	@JsonIgnore
	private CommonMarcoShopGoodResource getMarcoShopGoodResource(String id) {
		return CommonActivityConfig.getInstance().commonShopGoodStorage.get(id, true);
	}

	// getter-setter
	public int getVersion() {
		return version;
	}

	public Map<String, Integer> getRestrictGoods() {
		return restrictGoods;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setRestrictGoods(Map<String, Integer> restrictGoods) {
		this.restrictGoods = restrictGoods;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public void setGoods(List<CommonMarcoShopGood> goods) {
		this.goods = goods;
	}

	public List<CommonMarcoShopGood> getGoods() {
		return goods;
	}

}
