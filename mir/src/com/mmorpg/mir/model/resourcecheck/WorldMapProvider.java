package com.mmorpg.mir.model.resourcecheck;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.h2.util.New;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.mmorpg.mir.ClearAndMigrate;
import com.mmorpg.mir.model.world.MapGrid;
import com.mmorpg.mir.model.world.WorldMap;
import com.mmorpg.mir.model.world.WorldMapInstance;
import com.mmorpg.mir.model.world.resource.MapCountry;
import com.mmorpg.mir.model.world.resource.MapResource;
import com.mmorpg.mir.model.world.resource.SafeAreaResource;
import com.windforce.common.resource.Storage;
import com.windforce.common.resource.anno.Static;

@Component
public class WorldMapProvider implements ApplicationContextAware {

	public static final int INIT_INSTANCE = 1;

	@Static
	private Storage<Integer, MapResource> mapResources;

	@Static
	private Storage<String, SafeAreaResource> safeAreaResources;

	/**
	 * World maps supported by server.
	 */
	private final Map<Integer, WorldMap> worldMaps = new ConcurrentHashMap<Integer, WorldMap>();

	private static WorldMapProvider instance;

	@PostConstruct
	public void init() throws Exception {
		if (ClearAndMigrate.clear) {
			return;
		}
		for (MapResource resource : mapResources.getAll()) {
			WorldMap map = createWorldMap(resource);
			worldMaps.put(resource.getMapId(), map);
		}

		instance = this;
	}

	private WorldMap createWorldMap(MapResource resource) throws Exception {
		WorldMap map = new WorldMap();
		map.setMapId(resource.getMapId());
		map.setName(resource.getName());
		map.setCopy(resource.isCopy());
		map.setMaxNum(resource.getMaxNum());
		map.setMaxChannel(resource.getMaxChannel());
		map.setInitChannelNum(resource.getInitChannelNum());
		map.setDeleteConditions(resource.getCoreConditions());
		map.setReliveId(resource.getReliveBaseResourceId());
		map.setCountry(MapCountry.valueOf(resource.getCountry()));

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(applicationContext.getResource("resource/map/" + resource.getFileName())
				.getFile());
		Element element = document.getDocumentElement();

		int xNum = Integer.parseInt(element.getAttribute("xNum"));
		int yNum = Integer.parseInt(element.getAttribute("yNum"));

		map.setxNum(xNum);
		map.setyNum(yNum);

		MapGrid[][] grids = new MapGrid[yNum][xNum];
		NodeList nodeList = ((Element) element.getElementsByTagName("Block").item(0)).getElementsByTagName("row");
		for (int i = 0, j = nodeList.getLength(); i < j; i++) {
			Element row = (Element) nodeList.item(i);
			String rowValue = row.getTextContent();
			String[] rowValues = rowValue.split(",");
			for (int m = 0, n = rowValues.length; m < n; m++) {
				grids[i][m] = new MapGrid();
				if (Integer.parseInt(rowValues[m]) != 0) {
					grids[i][m].openBlock();
				}
			}
		}

		nodeList = ((Element) element.getElementsByTagName("safe").item(0)).getElementsByTagName("row");
		for (int i = 0, j = nodeList.getLength(); i < j; i++) {
			Element row = (Element) nodeList.item(i);
			String rowValue = row.getTextContent();
			String[] rowValues = rowValue.split(",");
			for (int m = 0, n = rowValues.length; m < n; m++) {
				if (Integer.parseInt(rowValues[m]) != 0) {
					grids[i][m].openSafe();
				}
			}
		}

		map.setMapGrids(grids);
		return map;
	}

	public static final WorldMapProvider getInstance() {
		return instance;
	}

	/**
	 * Return World Map by id
	 * 
	 * @param id
	 *            - id of world map.
	 * @return World map.
	 */
	public WorldMap getWorldMap(int id) {
		return worldMaps.get(id);
	}

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public String[] getWorldMapInfo() {
		List<String> result = New.arrayList();
		for (WorldMap worldMap : worldMaps.values()) {
			Iterator<WorldMapInstance> ite = worldMap.iterator();
			while (ite.hasNext()) {
				WorldMapInstance instance = ite.next();
				result.add(instance.toString());
			}
		}
		return result.toArray(new String[result.size()]);
	}

	public String[] getWorldMapInfo(int mapId) {
		List<String> result = New.arrayList();
		WorldMap map = worldMaps.get(mapId);
		if (map != null) {
			Iterator<WorldMapInstance> ite = map.iterator();
			while (ite.hasNext()) {
				WorldMapInstance instance = ite.next();
				result.add(instance.toString());
			}
		}
		return result.toArray(new String[result.size()]);
	}

	public Storage<String, SafeAreaResource> getSafeAreaResources() {
		return safeAreaResources;
	}

	public void setSafeAreaResources(Storage<String, SafeAreaResource> safeAreaResources) {
		this.safeAreaResources = safeAreaResources;
	}

	public MapResource getMapResource(int mapId) {
		return mapResources.get(mapId, true);
	}

}
