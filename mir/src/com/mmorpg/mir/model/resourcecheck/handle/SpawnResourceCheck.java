package com.mmorpg.mir.model.resourcecheck.handle;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.object.ObjectType;
import com.mmorpg.mir.model.object.resource.ObjectResource;
import com.mmorpg.mir.model.object.route.RouteStep;
import com.mmorpg.mir.model.object.route.RouteType;
import com.mmorpg.mir.model.resourcecheck.ResourceCheckHandle;
import com.mmorpg.mir.model.resourcecheck.WorldMapProvider;
import com.mmorpg.mir.model.spawn.resource.SpawnGroupResource;
import com.mmorpg.mir.model.world.WorldMap;
import com.mmorpg.mir.model.world.resource.MapResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class SpawnResourceCheck extends ResourceCheckHandle {
	@Static
	private Storage<String, SpawnGroupResource> spawnGroupResources;

	@Static
	private Storage<String, ObjectResource> objectResources;

	@Static
	private Storage<Integer, MapResource> mapResources;

	@Autowired
	private WorldMapProvider world;

	@Override
	public Class<?> getResourceClass() {
		return SpawnGroupResource.class;
	}

	@Override
	public void check() {
		List<String> errorInfo = new ArrayList<String>();
		for (SpawnGroupResource rs : spawnGroupResources.getAll()) {
			ObjectResource or = objectResources.get(rs.getObjectKey(), false);
			if (or == null) {
				throw new RuntimeException(String.format("SpawnGroupResource id[%s] objectKey[%s]不存在！ ", rs.getKey(),
						rs.getObjectKey()));
			}
			MapResource mr = mapResources.get(rs.getMapId(), false);
			if (mr == null) {
				throw new RuntimeException(String.format("SpawnGroupResource id[%s] mapId[%s]不存在！ ", rs.getKey(),
						rs.getObjectKey()));
			}
			WorldMap worldMap = world.getWorldMap(mr.getMapId());
			boolean block = worldMap.isBlock(rs.getX(), rs.getY());
			boolean out = worldMap.isOut(rs.getX(), rs.getY());
			if (block || out) {
				// 雕像可以放在阻挡区里面
				if (or.getObjectType() != ObjectType.SCULPTURE) {
					errorInfo.add(String.format("SpawnGroupResource id[%s] mapId[%s] x[%s] y[%s] block[%s] out[%s]！ ",
							rs.getKey(), rs.getMapId(), rs.getX(), rs.getY(), block, out));
				}
			}

			if (rs.getRouteSteps() != null) {
				for (int i = 0; i < rs.getRouteSteps().length; i++) {
					if (i + 1 <= rs.getRouteSteps().length - 1) {
						RouteStep r1 = rs.getRouteSteps()[i], r2 = rs.getRouteSteps()[i + 1];
						if (r1.getMapId() == r2.getMapId() && r1.getX() == r2.getX() && r1.getY() == r2.getY()) {
							errorInfo.add(String.format(
									"SpawnGroupResource id[%s] routeStep{mapId[%s] x[%s] y[%s]} 重复了,不走不是路的路！ ",
									rs.getKey(), r1.getMapId(), r2.getX(), r2.getY()));
						}
					}
				}
				for (RouteStep routeStep : rs.getRouteSteps()) {
					WorldMap wp = world.getWorldMap(routeStep.getMapId());
					if (wp == null) {
						errorInfo.add(String.format(
								"SpawnGroupResource id[%s] routeStep{mapId[%s] x[%s] y[%s]} 地图没有找到！ ", rs.getKey(),
								routeStep.getMapId(), routeStep.getX(), routeStep.getY()));
					}
					boolean b = wp.isBlock(routeStep.getX(), routeStep.getY());
					boolean o = wp.isOut(routeStep.getX(), routeStep.getY());
					if (block || out) {
						errorInfo.add(String.format(
								"SpawnGroupResource id[%s] routeStep{[%s] x[%s] y[%s]} block[%s] out[%s]！ ",
								rs.getKey(), routeStep.getMapId(), routeStep.getX(), routeStep.getY(), b, o));
					}
				}
			}
			if (rs.getRouteType() == RouteType.ONEWAY_LOOP_ROUTE) {
				if (rs.getRouteSteps() != null && rs.getRouteSteps().length <= 1) {
					errorInfo.add(String.format(
							"SpawnGroupResource id[%s] routeType ONEWAY_LOOP_ROUTE but routeStep illeagal",
							rs.getKey()));
				}
			}
		}
		for (String s : errorInfo) {
			System.err.println(s);
		}
		if (!errorInfo.isEmpty()) {
			throw new RuntimeException();
		}
	}
}
