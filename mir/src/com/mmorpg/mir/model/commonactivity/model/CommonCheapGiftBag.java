package com.mmorpg.mir.model.commonactivity.model;

import java.util.HashMap;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.commonactivity.resource.CommonCheapGiftBagResource;
import com.windforce.common.resource.Storage;

public class CommonCheapGiftBag {
	private HashMap<Integer, String> rewarded;
	
	public static CommonCheapGiftBag valueOf(){
		CommonCheapGiftBag bag = new CommonCheapGiftBag();
		bag.setRewarded(new HashMap<Integer, String>());
		return bag;
	}
	
	public HashMap<Integer, String> getRewarded() {
		return rewarded;
	}

	public void setRewarded(HashMap<Integer, String> rewarded) {
		this.rewarded = rewarded;
	}

	@JsonIgnore
	public boolean hasBuyBefore(CommonCheapGiftBagResource resource, Storage<String, CommonCheapGiftBagResource> cheapGiftBagStorage){
		if(!rewarded.keySet().contains(resource.getGroupId())){
			return false;
		}
		
		if(cheapGiftBagStorage.get(rewarded.get(resource.getGroupId()), true).getLevel() >= resource.getLevel()){
			return true;
		}
		return false;
	}
	
	@JsonIgnore
	public boolean hasBuyLowLevel(CommonCheapGiftBagResource resource, Storage<String, CommonCheapGiftBagResource> cheapGiftBagStorage){
		if(resource.getLowLevelId() == null){
			return true;
		}
		
		if(!rewarded.containsKey(resource.getGroupId())){
			return true;
		}
		
		if(1 == resource.getLevel() - cheapGiftBagStorage.get(rewarded.get(resource.getGroupId()), true).getLevel()){
			return true;
		}
		
		return false;
	}
	
	@JsonIgnore
	public void addLog(CommonCheapGiftBagResource resource){
		rewarded.put(resource.getGroupId(), resource.getId());
	}
}
