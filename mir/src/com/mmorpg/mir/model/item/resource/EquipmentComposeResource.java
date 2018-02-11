package com.mmorpg.mir.model.item.resource;

import java.util.ArrayList;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.item.storage.EquipmentStorage.EquipmentType;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class EquipmentComposeResource {
	@Id
	private String id;

	private int roleType;

	private int level;

	private EquipmentType equipmentType;

	private int specialType;
	/** 消耗 */
	private Map<String, ArrayList<String>> actionMap;

	private Map<String, String> resultChooserGroupMap;

	private Map<String, String> soulLevelChooserGroupId;

	private Map<String, String> soulTypeChooserGroupId;

	private Map<String, String> elementChooserGroupMap;

	@JsonIgnore
	public boolean containsRecipe(String recipe) {
		return resultChooserGroupMap.containsKey(recipe);
	}

	public int getRoleType() {
		return roleType;
	}

	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public EquipmentType getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
	}

	public int getSpecialType() {
		return specialType;
	}

	public void setSpecialType(int specialType) {
		this.specialType = specialType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, ArrayList<String>> getActionMap() {
		return actionMap;
	}

	public void setActionMap(Map<String, ArrayList<String>> actionMap) {
		this.actionMap = actionMap;
	}

	public Map<String, String> getResultChooserGroupMap() {
		return resultChooserGroupMap;
	}

	public void setResultChooserGroupMap(Map<String, String> resultChooserGroupMap) {
		this.resultChooserGroupMap = resultChooserGroupMap;
	}

	public Map<String, String> getSoulLevelChooserGroupId() {
		return soulLevelChooserGroupId;
	}

	public void setSoulLevelChooserGroupId(Map<String, String> soulLevelChooserGroupId) {
		this.soulLevelChooserGroupId = soulLevelChooserGroupId;
	}

	public Map<String, String> getSoulTypeChooserGroupId() {
		return soulTypeChooserGroupId;
	}

	public void setSoulTypeChooserGroupId(Map<String, String> soulTypeChooserGroupId) {
		this.soulTypeChooserGroupId = soulTypeChooserGroupId;
	}

	public Map<String, String> getElementChooserGroupMap() {
		return elementChooserGroupMap;
	}

	public void setElementChooserGroupMap(Map<String, String> elementChooserGroupMap) {
		this.elementChooserGroupMap = elementChooserGroupMap;
	}

}
