package com.mmorpg.mir.model.chooser.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.model.sample.Chooser;
import com.mmorpg.mir.model.chooser.model.sample.ChooserGroup;
import com.mmorpg.mir.model.chooser.model.sample.Item;
import com.mmorpg.mir.model.chooser.model.sample.ItemGroup;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.JsonUtils;
import com.windforce.common.utility.New;

@Component
public class ChooserManager implements IChooserManager {
	@Autowired
	private CoreConditionManager conditionManager;

	@Static
	private Storage<String, ChooserGroup> chooserGroups;
	@Static
	private Storage<String, Chooser> choosers;

	private static ChooserManager self;

	public static ChooserManager getInstance() {
		return self;
	}

	@PostConstruct
	protected void init() {
		self = this;
	}

	public List<String> chooser(String id) {
		Chooser chooser = choosers.get(id, true);
		return chooser.calcResult();
	}

	/**
	 * 按照优先级选值
	 * 
	 * @param chooserId
	 * @param limitCtx
	 * @return
	 */
	@Deprecated
	public List<String> chooseValue(Object object, String chooserId, Map<String, Object> parms) {
		String[] groups = chooserGroups.get(chooserId, true).getValueChoosers();
		for (String csId : groups) {
			Chooser chooser = choosers.get(csId, true);
			// 如果只有一个直接返回，不进行判断
			if (groups.length == 1 || conditionManager.isPass(object, chooser.getConditionIds(), parms, false)) {
				return chooser.calcResult();
			}
		}
		throw new RuntimeException("[" + chooserId + "][" + JsonUtils.object2String(parms) + "]未选出任何结果！");
	}

	/**
	 * 按照需求选值
	 * 
	 * @param chooserId
	 * @param limitCtx
	 * @return
	 */
	public List<String> chooseValueByRequire(Object object, String chooserId, Map<String, Object> parms) {
		String[] groups = chooserGroups.get(chooserId, true).getValueChoosers();
		List<String> values = new ArrayList<String>();
		for (String csId : groups) {
			Chooser chooser = choosers.get(csId, true);
			// 如果只有一个直接返回，不进行判断
			if (groups.length == 1 || conditionManager.isPass(object, chooser.getConditionIds(), parms, false)) {
				values.addAll(chooser.calcResult());
			}
		}
		if (values.size() == 0) {
			throw new RuntimeException("[" + chooserId + "][" + JsonUtils.object2String(parms) + "]未选出任何结果！");
		}
		return values;
	}

	public List<String> chooseValueByRequire(Object object, String chooserId) {
		return this.chooseValueByRequire(object, chooserId, new HashMap<String, Object>());
	}

	/**
	 * 获取所有chooserGroup里面包含的判断条件
	 * 
	 * @param chooserGroupId
	 * @return
	 */
	public Set<CoreConditionType> selectChooserAllConditionTypes(String chooserGroupId) {
		Set<CoreConditionType> types = new HashSet<CoreConditionType>();
		ChooserGroup chooserGroup = chooserGroups.get(chooserGroupId, true);
		for (String chooserId : chooserGroup.getValueChoosers()) {
			Chooser chooser = choosers.get(chooserId, true);
			for (String coreConditionId : chooser.getConditionIds()) {
				CoreConditionResource conditionResource = CoreConditionManager.getInstance().getCoreConditionResource()
						.get(coreConditionId, true);
				types.add(conditionResource.getType());
			}
		}
		return types;
	}

	public Map<CoreConditionType, CoreConditionResource> selectChooserAllConditions(String chooserGroupId) {
		Map<CoreConditionType, CoreConditionResource> types = New.hashMap();
		ChooserGroup chooserGroup = chooserGroups.get(chooserGroupId, true);
		for (String chooserId : chooserGroup.getValueChoosers()) {
			Chooser chooser = choosers.get(chooserId, true);
			for (String coreConditionId : chooser.getConditionIds()) {
				CoreConditionResource conditionResource = CoreConditionManager.getInstance().getCoreConditionResource()
						.get(coreConditionId, true);
				types.put(conditionResource.getType(), conditionResource);
			}
		}
		return types;
	}

	public List<String> chooserFormExcept(Object object, String chooserId, List<String> excepts,
			Map<String, Object> parms) {
		String[] groups = chooserGroups.get(chooserId, true).getValueChoosers();
		List<String> values = new ArrayList<String>();
		for (String csId : groups) {
			Chooser chooser = choosers.get(csId, true);
			// 如果只有一个直接返回，不进行判断
			if (groups.length == 1 || conditionManager.isPass(object, chooser.getConditionIds(), parms, false)) {
				values.addAll(exceptChooser(chooser, excepts).calcResult());
			}
		}
		if (values.size() == 0) {
			throw new RuntimeException("[" + chooserId + "][" + JsonUtils.object2String(parms) + "]未选出任何结果！");
		}
		return values;
	}

	private Chooser exceptChooser(Chooser chooser, List<String> excepts) {
		Chooser exceptChooser = JsonUtils.string2Object(JsonUtils.object2String(chooser), Chooser.class);
		for (ItemGroup group : exceptChooser.getItemGroups()) {
			List<Item> removeItems = new ArrayList<Item>();
			for (Item item : group.getItems()) {
				if (excepts.contains(item.getValue())) {
					removeItems.add(item);
				}
			}
			group.getItems().removeAll(removeItems);
		}
		return exceptChooser;
	}

	public String chooserOneResult(String id) {
		return chooser(id).get(0);
	}
	
}
