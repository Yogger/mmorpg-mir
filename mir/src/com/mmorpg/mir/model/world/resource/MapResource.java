package com.mmorpg.mir.model.world.resource;

import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.world.World;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class MapResource {
	@Id
	private int mapId;

	private String name;

	private String fileName;

	private int copy;

	private int maxNum;

	private String[] deleteConditionIds;

	private int cannotRelive;

	private int reliveBaseResourceId;

	private int country;

	private String[] safeArea;
	/** 禁止进入条件 */
	private String[] forbidEnter;
	/** 进入最大等级 */
	private int maxLevel;
	/** 进入最小等级 */
	private int minLevel;
	/** 本国进入最低军衔等级 */
	private int nativeMilitartLevel;
	/** 他国进入最低军衔等级 */
	private int enemyMilitartLevel;
	/** 最大分线数量 */
	private int maxChannel;
	/** 初始化开启线路数量,不填和填1都只开1条线 */
	private int initChannelNum;
	/** 玩家离开以后保存的时间 */
	private int duration;
	/** 不同国家回城的吟唱时间 */
	private int[] backInterval;
	/** 国君召集不扣免费复活次数的场景 */
	private boolean kingCallSpecialMap;

	/** 进入地图时需要移除的效果组 */
	private String[] removeEffectGroups;
	/** 这个地图捡东西的条件 */
	private String[] pickUpConditionIds;

	private boolean canRide;

	@Transient
	private CoreConditions forbidEnterCondition;

	@Transient
	private CoreConditions pickUpConditions;

	@JsonIgnore
	public CoreConditions getPickUpConditions() {
		if (pickUpConditions == null) {
			pickUpConditions = CoreConditionManager.getInstance().getCoreConditions(1, pickUpConditionIds);
		}
		return pickUpConditions;
	}

	@Transient
	private List<SafeAreaResource> safeAreas;

	public String[] getSafeArea() {
		return safeArea;
	}

	@JsonIgnore
	public boolean inLevel(Player player) {
		if (minLevel <= player.getLevel() && player.getLevel() <= maxLevel) {
			return true;
		}
		return false;
	}

	public void setSafeArea(String[] safeArea) {
		this.safeArea = safeArea;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	@Transient
	private CoreConditions coreConditions;

	public int getReliveBaseResourceId() {
		return reliveBaseResourceId;
	}

	public void setReliveBaseResourceId(int reliveBaseResourceId) {
		this.reliveBaseResourceId = reliveBaseResourceId;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCopy() {
		return copy;
	}

	public void setCopy(int copy) {
		this.copy = copy;
	}

	@JsonIgnore
	public boolean isCopy() {
		return copy > 0;
	}

	@JsonIgnore
	public boolean cannotRelive() {
		return cannotRelive > 0;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public String[] getDeleteConditionIds() {
		return deleteConditionIds;
	}

	public void setDeleteConditionIds(String[] deleteConditionIds) {
		this.deleteConditionIds = deleteConditionIds;
	}

	@JsonIgnore
	public CoreConditions getCoreConditions() {
		if (coreConditions == null) {
			if (ArrayUtils.isEmpty(deleteConditionIds)) {
				coreConditions = new CoreConditions();
			} else {
				coreConditions = CoreConditionManager.getInstance().getCoreConditions(1, deleteConditionIds);
			}
		}
		return coreConditions;
	}

	@JsonIgnore
	public void setCoreConditions(CoreConditions coreConditions) {
		this.coreConditions = coreConditions;
	}

	@JsonIgnore
	public List<SafeAreaResource> getSafeAreas() {
		if (safeAreas == null) {
			if (safeArea == null) {
				safeAreas = New.arrayList();
			} else {
				for (String key : safeArea) {
					safeAreas.add(World.getInstance().getSafeAreaResources().get(key, true));
				}
			}
		}
		return safeAreas;
	}

	public String[] getForbidEnter() {
		return forbidEnter;
	}

	public void setForbidEnter(String[] forbidEnter) {
		this.forbidEnter = forbidEnter;
	}

	@JsonIgnore
	public CoreConditions getForbidEnterCondition() {
		if (forbidEnterCondition == null) {
			if (forbidEnter != null) {
				forbidEnterCondition = CoreConditionManager.getInstance().getCoreConditions(1, forbidEnter);
			}
		}
		return forbidEnterCondition;
	}

	@JsonIgnore
	public void setForbidEnterCondition(CoreConditions forbidEnterCondition) {
		this.forbidEnterCondition = forbidEnterCondition;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}

	public int getNativeMilitartLevel() {
		return nativeMilitartLevel;
	}

	public void setNativeMilitartLevel(int nativeMilitartLevel) {
		this.nativeMilitartLevel = nativeMilitartLevel;
	}

	public int getEnemyMilitartLevel() {
		return enemyMilitartLevel;
	}

	public void setEnemyMilitartLevel(int enemyMilitartLevel) {
		this.enemyMilitartLevel = enemyMilitartLevel;
	}

	public int getMaxChannel() {
		return maxChannel;
	}

	public void setMaxChannel(int maxChannel) {
		this.maxChannel = maxChannel;
	}

	public int getInitChannelNum() {
		return initChannelNum;
	}

	public void setInitChannelNum(int initChannelNum) {
		this.initChannelNum = initChannelNum;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int[] getBackInterval() {
		return backInterval;
	}

	public void setBackInterval(int[] backInterval) {
		this.backInterval = backInterval;
	}

	public boolean isKingCallSpecialMap() {
		return kingCallSpecialMap;
	}

	public void setKingCallSpecialMap(boolean kingCallSpecialMap) {
		this.kingCallSpecialMap = kingCallSpecialMap;
	}

	public String[] getRemoveEffectGroups() {
		return removeEffectGroups;
	}

	public void setRemoveEffectGroups(String[] removeEffectGroups) {
		this.removeEffectGroups = removeEffectGroups;
	}

	public String[] getPickUpConditionIds() {
		return pickUpConditionIds;
	}

	public void setPickUpConditionIds(String[] pickUpConditionIds) {
		this.pickUpConditionIds = pickUpConditionIds;
	}

	public final boolean isCanRide() {
		return canRide;
	}

	public final void setCanRide(boolean canRide) {
		this.canRide = canRide;
	}

	public int getCannotRelive() {
		return cannotRelive;
	}

	public void setCannotRelive(int cannotRelive) {
		this.cannotRelive = cannotRelive;
	}

	public void setSafeAreas(List<SafeAreaResource> safeAreas) {
		this.safeAreas = safeAreas;
	}

}
