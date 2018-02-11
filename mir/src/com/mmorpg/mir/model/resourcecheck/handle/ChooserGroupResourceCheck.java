package com.mmorpg.mir.model.resourcecheck.handle;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.model.sample.Chooser;
import com.mmorpg.mir.model.chooser.model.sample.ChooserGroup;
import com.mmorpg.mir.model.chooser.model.sample.Item;
import com.mmorpg.mir.model.chooser.model.sample.ItemGroup;
import com.mmorpg.mir.model.core.condition.CoreConditionType;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.country.model.CountryId;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.utility.New;

@Component
public class ChooserGroupResourceCheck extends ResourceCheckHandle {
	@Static
	private Storage<String, ChooserGroup> chooserGroupResources;

	@Static
	private Storage<String, Chooser> chooserResources;

	@Static
	private Storage<String, CoreConditionResource> conditionResources;

	@Override
	public Class<?> getResourceClass() {
		return ChooserGroup.class;
	}

	@Override
	public void check() {
		ArrayList<String> errInfos = New.arrayList();
		for (ChooserGroup rs : chooserGroupResources.getAll()) {
			if (rs.getValueChoosers() != null) {
				for (String chooserId : rs.getValueChoosers()) {
					Chooser chooser = chooserResources.get(chooserId, false);
					if (chooser == null) {
						String s = String.format("ChooserGroup id[%s] chooserId[%s]不存在！ ", rs.getChooserGroupId(),
								chooserId);
						errInfos.add(s);
						continue;
					}
					if (chooser.getItemGroups() == null) {
						String s = String.format("ChooserGroup id[%s] chooserId[%s] 选的组数为[%d]！ ",
								rs.getChooserGroupId(), chooserId, chooser.getItemGroups());
						errInfos.add(s);
						continue;
					}
					for (ItemGroup chooserItems : chooser.getItemGroups()) {
						if (chooserItems.getItems().size() < chooserItems.getResultCount()) {
							String s = String.format("ChooserGroup id[%s] chooserId[%s] 选的组数为[%d] 而要筛选的结果数为[%d]！ ",
									rs.getChooserGroupId(), chooserId, chooserItems.getItems().size(),
									chooserItems.getResultCount());
							errInfos.add(s);
						}
					}
					if (chooser.getConditionIds() != null) {
						for (String conditionId : chooser.getConditionIds()) {
							CoreConditionResource cr = conditionResources.get(conditionId, false);
							if (cr == null) {
								String s = String.format("Chooser id[%s] conditionId[%s]不存在！ ", chooserId, conditionId);
								errInfos.add(s);
							}
							if (cr.getType() == CoreConditionType.COUNTRY_QUEST_SELECT) {
								checkFormat(chooser);
							}
						}
					}
				}
			}
		}

		for (String s : errInfos) {
			System.err.println(s);
		}

	}
	
	private void checkFormat(Chooser chooser) {
		for (ItemGroup group: chooser.getItemGroups()) {
			for (Item item : group.getItems()) {
				String result =  item.getValue();
				result.trim();
				// 123,23-1
				if (result.contains("-")) { // 2v1模式
					CountryId.valueOf(Integer.valueOf(result.substring(0, 1)));
					CountryId.valueOf(Integer.valueOf(result.substring(1, 2)));
					CountryId.valueOf(Integer.valueOf(result.substring(3)));
				} else { // 1v1v1 模式
					CountryId.valueOf(Integer.valueOf(result.substring(0, 1)));
					CountryId.valueOf(Integer.valueOf(result.substring(1, 2)));
					CountryId.valueOf(Integer.valueOf(result.substring(2)));
				}
			}
		}
	}
}
