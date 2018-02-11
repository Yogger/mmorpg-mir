package com.mmorpg.mir.model.shop.manager;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.shop.resouce.ShopResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class ShopManager implements IShopManager {
	@Static
	private Storage<String, ShopResource> shopResource;

	private static ShopManager instance;

	@PostConstruct
	public void init() {
		instance = this;
	}

	public static ShopManager getInstance() {
		return instance;
	}

	public ShopResource getShopResource(String id) {
		return shopResource.get(id, true);
	}

}
