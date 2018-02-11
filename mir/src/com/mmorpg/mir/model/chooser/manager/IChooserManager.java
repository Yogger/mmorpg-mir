package com.mmorpg.mir.model.chooser.manager;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;

public interface IChooserManager {
	List<String> chooser(String id);

	List<String> chooseValue(Object object, String chooserId, Map<String, Object> parms);

	List<String> chooseValueByRequire(Object object, String chooserId, Map<String, Object> parms);

	Set<CoreConditionType> selectChooserAllConditionTypes(String chooserGroupId);

	Map<CoreConditionType, CoreConditionResource> selectChooserAllConditions(String chooserGroupId);
}
