package com.mmorpg.mir.model.quest.resource;

import java.util.Map;

import org.h2.util.New;

import com.mmorpg.mir.model.quest.model.KeyType;
import com.windforce.common.utility.JsonUtils;

public class QuestKeyResource {
	/** 条件类型 */
	private KeyType type;
	/** 条件类型的限定参数 */
	private Map<String, Object> parms;
	/** 参数 */
	private String value;
	/** key名称 */
	private String keyname;

	public static void main(String[] args) {
		QuestKeyResource resource = new QuestKeyResource();
		resource.setType(KeyType.MONSTER_HUNT);
		resource.parms = New.hashMap();
		resource.parms.put("MONSTERID", "TEST:1");
		resource.value = "10";
		System.out.println(JsonUtils.object2String(resource));
	}

	public KeyType getType() {
		return type;
	}

	public void setType(KeyType type) {
		this.type = type;
	}

	public Map<String, Object> getParms() {
		return parms;
	}

	public void setParms(Map<String, Object> parms) {
		this.parms = parms;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKeyname() {
		return keyname;
	}

	public void setKeyname(String keyname) {
		this.keyname = keyname;
	}

}
