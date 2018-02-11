package com.mmorpg.mir.model.mergeactive.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.mergeactive.MergeActiveConfig;
import com.mmorpg.mir.model.mergeactive.resource.MergeCheapGiftBagResource;

public class CheapGiftBag {
	private Set<String> drawCheapGiftLog = new HashSet<String>();

	public Set<String> getDrawCheapGiftLog() {
		return drawCheapGiftLog;
	}

	public static CheapGiftBag valueOf() {
		CheapGiftBag bag = new CheapGiftBag();
		bag.drawCheapGiftLog = new HashSet<String>();
		return bag;
	}

	public void addDrawLog(String giftId) {
		drawCheapGiftLog.add(giftId);
	}

	@JsonIgnore
	public boolean hasBuyBefore(String giftId) {
		if (drawCheapGiftLog.contains(giftId)) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public boolean hasBuyLowLevel(MergeCheapGiftBagResource bagResource) {
		String lowLevelId = bagResource.getLowLevelId();
		if (lowLevelId == null || hasBuyBefore(lowLevelId)) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public Map<Integer, String> getFrontShowCheapGiftBag(Player player) {
		Map<Integer, String> frontMap = new HashMap<Integer, String>();
		for (MergeCheapGiftBagResource bagResource : MergeActiveConfig.getInstance().cheapGiftBagResource.getAll()) {
			if (hasBuyBefore(bagResource.getId()) || !hasBuyLowLevel(bagResource)
					|| !bagResource.getCoreConditions().verify(player)) {
				continue;
			}
			frontMap.put(bagResource.getGroupId(), bagResource.getId());
		}
		for (int i = 1; i <= MergeActiveConfig.getInstance().getMergeCheapGiftBagGroupNum(); i++) {
			if (!frontMap.containsKey(i) && recievedAll(i)) {
				frontMap.put(i, "");
			}
		}
		return frontMap;
	}

	@JsonIgnore
	public boolean recievedAll(int groupId) {
		for (MergeCheapGiftBagResource bagResource : MergeActiveConfig.getInstance().cheapGiftBagResource.getAll()) {
			if (bagResource.getGroupId() == groupId && !hasBuyBefore(bagResource.getId())) {
				return false;
			}
		}
		return true;
	}
}
