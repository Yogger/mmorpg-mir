package com.mmorpg.mir.model.openactive.model;

import java.util.HashSet;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.openactive.OpenActiveConfig;
import com.mmorpg.mir.model.openactive.resource.PublicTestGiftResource;
import com.windforce.common.utility.New;

/**
 * 已经购买的礼物 
 */
public class GiftActive {
	private HashSet<String> gifted = New.hashSet();

	public HashSet<String> getGifted() {
		return gifted;
	}

	public void setGifted(HashSet<String> gifted) {
		this.gifted = gifted;
	}
	
	public static GiftActive valueOf(){
		GiftActive ga = new GiftActive();
		ga.gifted = New.hashSet();
		return ga;
	}
	
	/**
	 * 获取前段显示的礼物， map的Integer表示要显示的组， String表示要显示组的级别
	 * 1. 如果返回的map的value为null， 则表示最高级别已经买过了。
	 * @return
	 */
	@JsonIgnore
	public Map<Integer, String> getFrontShowGift(Player player){
		Map<Integer, String> map = New.hashMap();
		for (PublicTestGiftResource resource : OpenActiveConfig.getInstance().getPublicTestResources()) {
			if(resource.getVersion() != OpenActiveConfig.getInstance().DOUBLE_11_VERSION.getValue()){
				continue;
			}
			if (hasBuyBefore(resource) || !hasBuyLowLevel(resource) || !resource.getCoreConditions().verify(player)) {
				continue;
			}
			map.put(resource.getGroupId(), resource.getId());
		}
		
		for (int i = 1; i <= OpenActiveConfig.getInstance().getPublicTestGiftGroupNum(); i++) {
			if (!map.containsKey(i) && recievedAll(i)) {
				map.put(i, "");
			}
		}
		return map;
	}
	
	@JsonIgnore
	public boolean recievedAll(int groupId) {
		for (PublicTestGiftResource resource : OpenActiveConfig.getInstance().getPublicTestResources()) {
			if (resource.getVersion() != OpenActiveConfig.getInstance().DOUBLE_11_VERSION.getValue()) {
				continue;
			}
			if (resource.getGroupId() == groupId && !hasBuyBefore(resource)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否购买了上一级别
	 * 1. 当低级别为null， 或者是已经购买了低级别返回true
	 * @param giftResource
	 * @return
	 */
	@JsonIgnore
	public boolean hasBuyLowLevel(PublicTestGiftResource giftResource){
		String lowLevelId = giftResource.getLowLevelId();
		if(lowLevelId == null || gifted.contains(lowLevelId)){
			return true;
		}
		return false;
	}
	
	/**
	 * 是否购买过
	 * @param giftResource
	 * @return
	 */
	@JsonIgnore
	public boolean hasBuyBefore(PublicTestGiftResource giftResource){
		if(gifted.contains(giftResource.getId())){
			return true;
		}
		return false;
	}
}
