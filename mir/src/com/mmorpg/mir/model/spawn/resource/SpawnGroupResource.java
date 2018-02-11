package com.mmorpg.mir.model.spawn.resource;

import java.util.Map;
import java.util.Random;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.ai.AIType;
import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Status;
import com.mmorpg.mir.model.object.route.QuestRouteStep;
import com.mmorpg.mir.model.object.route.RouteStep;
import com.mmorpg.mir.model.object.route.RouteType;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.world.Block;
import com.mmorpg.mir.model.world.DirectionEnum;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class SpawnGroupResource {
	@Id
	private String key;
	/** 地图id **/
	@Index(name = "mapId")
	private int mapId;
	/** x坐标 **/
	private int x;
	/** y坐标 **/
	private int y;
	/** 半径 **/
	private int range;
	/** 是否需要重生 **/
	private int respawn;
	/** 这个用来配置npc物种信息 **/
	private String objectKey;
	/** 尸体保留时间 **/
	private int decayInterval = 1;
	/** 重生时间 **/
	private int spawnInterval = 3;
	/** 巡逻范围 **/
	private int randomWalk;
	/** ai类型 **/
	private AIType aiType;
	/** ai时间间隔,这个属性只对monster有效 **/
	private int aiInterval = 1000;
	/** 这个组里面的怪物数量 **/
	private int num;
	/** 警戒范围 */
	private int warnrange;
	/** 折返距离 */
	private int homerange;
	/** 路线类型 **/
	private RouteType routeType;
	/** 路线点 **/
	private RouteStep[] routeSteps;
	/** 任务路线点 **/
	private QuestRouteStep[] questRouteSteps;
	/** 重生条件 */
	private String[] spawnConditions;
	/** 重生触发器 */
	private String[] spawnTriggers;
	/** 国家 */
	private int country;
	/** 不自动生成 */
	private boolean noAutoSpawn;
	/** 状态管理器 */
	private Status[] status;
	/** 状态触发器 */
	private Map<Integer, String> statusTriggers;
	/** 转换状态是否需要引导时间 */
	private int statusDuration;
	/** 死亡触发器 */
	private String[] dieTriggers;
	/** 消失触发器 */
	private String[] despawnTriggers;
	/** 朝向 */
	private String heading;
	/** 障碍点集合 */
	private Block[] blocks;
	/** 看见玩家时添加skill */
	private int seePlayerUseSkill;
	/** BossResource的ID */
	private String bossResourceId;
	/** 被攻击时发出通告的组号 */
	private int noticeGroup;
	/** 掉落拾取紫色广播的品质 颜色 */
	private int pickUpQuality;
	/** 掉落的广播 */
	private Map<String, Integer> pickUpI18nNotice;

	@Transient
	private CoreConditions conditions;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@JsonIgnore
	public boolean isRespawn() {
		return respawn > 0;
	}

	public void setRespawn(int respawn) {
		this.respawn = respawn;
	}

	public String getObjectKey() {
		return objectKey;
	}

	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}

	public int getDecayInterval() {
		return decayInterval;
	}

	public void setDecayInterval(int decayInterval) {
		this.decayInterval = decayInterval;
	}

	public int getSpawnInterval() {
		return spawnInterval;
	}

	public void setSpawnInterval(int spawnInterval) {
		this.spawnInterval = spawnInterval;
	}

	public int getRandomWalk() {
		return randomWalk;
	}

	public void setRandomWalk(int randomWalk) {
		this.randomWalk = randomWalk;
	}

	public AIType getAiType() {
		return aiType;
	}

	public void setAiType(AIType aiType) {
		this.aiType = aiType;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int[] createXY() {
		return createXY(x, y, range);
	}

	public int[] createXY(int x, int y) {
		return createXY(x, y, range);
	}

	public int[] createXY(int x, int y, int halfRange) {
		WorldMap map = getWorldMap();
		return map.createXY(x, y, halfRange);
	}

	private WorldMap getWorldMap() {
		return World.getInstance().getWorldMap(mapId);
	}

	public byte createHeading() {
		if (heading != null) {
			return (byte) DirectionEnum.indexOrdinal(Integer.valueOf(heading)).ordinal();
		}
		Random random = MathUtil.getRandom();
		return (byte) random.nextInt(DirectionEnum.values().length);
	}

	public boolean hasRandomWalk() {
		return randomWalk > 0;
	}

	@Override
	public String toString() {
		return String.format("KEY : [%s] MAPID : [%d] X : [%d] Y : [%d] RANGE : [%d] NUM : [%d]", key, mapId, x, y,
				range, num);
	}

	public int getWarnrange() {
		return warnrange;
	}

	public void setWarnrange(int warnrange) {
		this.warnrange = warnrange;
	}

	public RouteStep[] getRouteSteps() {
		return routeSteps;
	}

	public void setRouteSteps(RouteStep[] routeSteps) {
		this.routeSteps = routeSteps;
	}

	public boolean hasRouteStep() {
		return routeSteps != null && routeSteps.length > 0;
	}

	public RouteType getRouteType() {
		return routeType;
	}

	public void setRouteType(RouteType routeType) {
		this.routeType = routeType;
	}

	public int getRespawn() {
		return respawn;
	}

	public String[] getSpawnConditions() {
		return spawnConditions;
	}

	public void setSpawnConditions(String[] spawnConditions) {
		this.spawnConditions = spawnConditions;
	}

	@JsonIgnore
	public CoreConditions getConditions() {
		if (conditions == null) {
			if (spawnConditions == null) {
				conditions = new CoreConditions();
			} else {
				conditions = CoreConditionManager.getInstance().getCoreConditions(1, spawnConditions);
			}
		}
		return conditions;
	}

	public void setConditions(CoreConditions conditions) {
		this.conditions = conditions;
	}

	public String[] getSpawnTriggers() {
		return spawnTriggers;
	}

	public void setSpawnTriggers(String[] spawnTriggers) {
		this.spawnTriggers = spawnTriggers;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public boolean isNoAutoSpawn() {
		return noAutoSpawn;
	}

	public void setNoAutoSpawn(boolean noAutoSpawn) {
		this.noAutoSpawn = noAutoSpawn;
	}

	public Status[] getStatus() {
		return status;
	}

	public void setStatus(Status[] status) {
		this.status = status;
	}

	public Map<Integer, String> getStatusTriggers() {
		return statusTriggers;
	}

	public void setStatusTriggers(Map<Integer, String> statusTriggers) {
		this.statusTriggers = statusTriggers;
	}

	public int getStatusDuration() {
		return statusDuration;
	}

	public void setStatusDuration(int statusDuration) {
		this.statusDuration = statusDuration;
	}

	public String[] getDieTriggers() {
		return dieTriggers;
	}

	public void setDieTriggers(String[] dieTriggers) {
		this.dieTriggers = dieTriggers;
	}

	public String[] getDespawnTriggers() {
		return despawnTriggers;
	}

	public void setDespawnTriggers(String[] despawnTriggers) {
		this.despawnTriggers = despawnTriggers;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public Block[] getBlocks() {
		return blocks;
	}

	public void setBlocks(Block[] blocks) {
		this.blocks = blocks;
	}

	public int getSeePlayerUseSkill() {
		return seePlayerUseSkill;
	}

	public void setSeePlayerUseSkill(int seePlayerUseSkill) {
		this.seePlayerUseSkill = seePlayerUseSkill;
	}

	public int getHomerange() {
		return homerange;
	}

	public void setHomerange(int homerange) {
		this.homerange = homerange;
	}

	public String getBossResourceId() {
		return bossResourceId;
	}

	public void setBossResourceId(String bossResourceId) {
		this.bossResourceId = bossResourceId;
	}

	public int getNoticeGroup() {
		return noticeGroup;
	}

	public void setNoticeGroup(int noticeGroup) {
		this.noticeGroup = noticeGroup;
	}

	public int getAiInterval() {
		return aiInterval;
	}

	public QuestRouteStep[] getQuestRouteSteps() {
		return questRouteSteps;
	}

	public void setQuestRouteSteps(QuestRouteStep[] questRouteSteps) {
		this.questRouteSteps = questRouteSteps;
	}

	public void setAiInterval(int aiInterval) {
		if (aiInterval < 200) {
			aiInterval = 200;
		}
		this.aiInterval = aiInterval;
	}

	public int getPickUpQuality() {
		return pickUpQuality;
	}

	public void setPickUpQuality(int pickUpQuality) {
		this.pickUpQuality = pickUpQuality;
	}

	public Map<String, Integer> getPickUpI18nNotice() {
		return pickUpI18nNotice;
	}

	public void setPickUpI18nNotice(Map<String, Integer> pickUpI18nNotice) {
		this.pickUpI18nNotice = pickUpI18nNotice;
	}

}
