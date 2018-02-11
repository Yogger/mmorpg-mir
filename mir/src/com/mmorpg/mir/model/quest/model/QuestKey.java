package com.mmorpg.mir.model.quest.model;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.quest.packet.QuestKeyVO;

public class QuestKey {
	/** 条件类型 */
	private KeyType type;
	/** key名称 */
	private String keyname;
	/** 条件类型的限定参数 */
	private Map<String, Object> parms = new HashMap<String, Object>();
	/** 参数 */
	private int value;
	/** 当前的上下文 */
	private Map<String, Object> ctx = new HashMap<String, Object>();

	public QuestKeyVO createVO() {
		QuestKeyVO vo = new QuestKeyVO();
		vo.setKeyname(keyname);
		vo.setValue(value);
		return vo;
	}

	public KeyType getType() {
		return type;
	}

	@JsonIgnore
	public void addValue() {
		addValue(1);
	}

	@JsonIgnore
	synchronized public void addValue(int add) {
		value += add;
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

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Map<String, Object> getCtx() {
		return ctx;
	}

	public void setCtx(Map<String, Object> ctx) {
		this.ctx = ctx;
	}

	public String getKeyname() {
		return keyname;
	}

	public void setKeyname(String keyname) {
		this.keyname = keyname;
	}

}
