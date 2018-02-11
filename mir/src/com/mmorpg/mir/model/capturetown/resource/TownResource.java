package com.mmorpg.mir.model.capturetown.resource;

import com.mmorpg.mir.model.capturetown.model.TownType;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class TownResource {

	/** 城池ID **/
	@Id
	private String id;
	
	private String cityName;
	/** 城池类型 **/
	@Index(name="TYPE")
	private TownType type;
	/** 城池排名 **/
	private int rank;
	/** 个人俸禄每分钟产出功勋值 **/
	private int baseFeatsPerMin;
	/** 个人俸禄计算的频率(分钟) **/
	private int baseFeatsCalcRate;
	/** 战争储备每分钟产出功勋值 **/
	private int featsPerMin;
	/** 战争储备最多积累的最长时间(分钟) **/
	private int featsProduceMaxLimit;
	/** 战争储备计算的频率(分钟) **/
	private int featsCalcRate;
	/** 守城玩家机器人(对应三个职业）的SpawnKey **/
	private String[] defendNpcSpawnKey;
	/** 城池挑战的地图ID **/
	private int mapId;
	/** 进入城池的初始坐标 */
	private int x;
	/** 进入城池的初始坐标 */
	private int y;
	/** 进入城池的初始朝向 */
	private byte heading;
	/** 守城玩家机器人的属性系数 **/
	private int statsFactor;
	/** 挑战限制的时间(分钟) **/
	private int challengeMaxMin;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TownType getType() {
		return type;
	}

	public void setType(TownType type) {
		this.type = type;
	}

	public int getFeatsProduceMaxLimit() {
		return featsProduceMaxLimit;
	}

	public void setFeatsProduceMaxLimit(int featsProduceMaxLimit) {
		this.featsProduceMaxLimit = featsProduceMaxLimit;
	}

	public String[] getDefendNpcSpawnKey() {
		return defendNpcSpawnKey;
	}

	public void setDefendNpcSpawnKey(String[] defendNpcSpawnKey) {
		this.defendNpcSpawnKey = defendNpcSpawnKey;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getBaseFeatsPerMin() {
		return baseFeatsPerMin;
	}

	public void setBaseFeatsPerMin(int baseFeatsPerMin) {
		this.baseFeatsPerMin = baseFeatsPerMin;
	}

	public int getFeatsPerMin() {
		return featsPerMin;
	}

	public void setFeatsPerMin(int featsPerMin) {
		this.featsPerMin = featsPerMin;
	}

	public int getStatsFactor() {
		return statsFactor;
	}

	public void setStatsFactor(int statsFactor) {
		this.statsFactor = statsFactor;
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

	public byte getHeading() {
		return heading;
	}

	public void setHeading(byte heading) {
		this.heading = heading;
	}

	public int getBaseFeatsCalcRate() {
		return baseFeatsCalcRate;
	}

	public void setBaseFeatsCalcRate(int baseFeatsCalcRate) {
		this.baseFeatsCalcRate = baseFeatsCalcRate;
	}

	public int getFeatsCalcRate() {
		return featsCalcRate;
	}

	public void setFeatsCalcRate(int featsCalcRate) {
		this.featsCalcRate = featsCalcRate;
	}

	public int getChallengeMaxMin() {
		return challengeMaxMin;
	}

	public void setChallengeMaxMin(int challengeMaxMin) {
		this.challengeMaxMin = challengeMaxMin;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	

}
