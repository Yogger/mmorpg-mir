package com.mmorpg.mir.model.resourcecheck.handle;

import java.util.List;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chooser.model.sample.Chooser;
import com.mmorpg.mir.model.chooser.model.sample.ChooserGroup;
import com.mmorpg.mir.model.chooser.model.sample.Item;
import com.mmorpg.mir.model.chooser.model.sample.ItemGroup;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.mmorpg.mir.model.resourcecheck.WorldMapProvider;
import com.mmorpg.mir.model.transport.resource.PlayerTransportResource;
import com.mmorpg.mir.model.transport.resource.TransportResource;
import com.mmorpg.mir.model.world.Position;
import com.mmorpg.mir.model.world.WorldMap;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

/**
 * 传送点,飞行点验证
 * 
 * @author 37wan
 */
@Component
public class TransportResourceCheck extends ResourceCheckHandle {
	@Static
	private Storage<String, TransportResource> transportResources;
	
	@Static
	private Storage<String, PlayerTransportResource> playerTransportResources;
	
	@Static
	private Storage<String, ChooserGroup> chooserGroups;
	
	@Static
	private Storage<String, Chooser> choosers;

	@Autowired
	private WorldMapProvider world;

	@Override
	public Class<?> getResourceClass() {
		return TransportResource.class;
	}

	@Override
	public void check() {
		List<String> errorInfo = New.arrayList();
		for (TransportResource rs: transportResources.getAll()) {
			ChooserGroup group = chooserGroups.get(rs.getChooserGroupId(), true);
			List<String> values = New.arrayList();
			for (String chooserId: group.getValueChoosers()) {
				Chooser chooser = choosers.get(chooserId, true);
				for (ItemGroup itemGroup: chooser.getItemGroups()) {
					for (Item item: itemGroup.getItems()) {
						values.add(item.getValue());
					}
				}
			}
			for (String result: values) {
				if (!rs.getDestinationPos().containsKey(result)) {
					errorInfo.add(String.format(
							"TranportResource id[%s] chooserGroupId[%s] select[%s] doesn't exist!!！ ", rs.getId(),
							rs.getChooserGroupId(), result));
				} else {
					Position pos = rs.getDestinationPos().get(result);
					WorldMap worldMap = world.getWorldMap(rs.getTargetMapId());
					boolean block = worldMap.isBlock(pos.getX(), pos.getY());
					boolean out = worldMap.isOut(pos.getX(), pos.getY());
					if (block || out) {
						errorInfo.add(String.format(
								"TranportResource id[%s] mapId[%s] x[%s] y[%s] block[%s] out[%s]！ ", rs.getId(),
								rs.getTargetMapId(), pos.getX(), pos.getY(), block, out));
					}
				}
			}
		}
		for (PlayerTransportResource rs: playerTransportResources.getAll()) {
			WorldMap worldMap = world.getWorldMap(rs.getTargetMapId());
			if (worldMap == null) {
				errorInfo.add(String.format(
						"PlayerTransportResource id[%s] targetMapId[%s] 这个ID的地图不存在啊！ ", rs.getId(), rs.getTargetMapId()
						));
				continue;
			}
			if (worldMap.isOut(rs.getTargetX(), rs.getTargetY())) {
				errorInfo.add(String.format(
						"PlayerTransportResource id[%s] mapId[%s] x[%s] y[%s] block[%s] out[%s]！ ", rs.getId(),
						rs.getTargetMapId(), rs.getTargetX(), rs.getTargetY(), 
						false,
						true));
			} else if (worldMap.isBlock(rs.getTargetX(), rs.getTargetY())) {
				errorInfo.add(String.format(
						"PlayerTransportResource id[%s] mapId[%s] x[%s] y[%s] block[%s] out[%s]！ ", rs.getId(),
						rs.getTargetMapId(), rs.getTargetX(), rs.getTargetY(), 
						true,
						false));
			}
		}
		
		for (String s: errorInfo) {
			System.err.println(s);
		}
		
		if (!errorInfo.isEmpty()) {
			//throw new RuntimeException("传送阵,或者飞行点 飞到沟里去了！");
		}
	}
}
