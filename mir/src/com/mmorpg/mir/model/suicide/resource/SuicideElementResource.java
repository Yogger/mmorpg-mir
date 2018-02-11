package com.mmorpg.mir.model.suicide.resource;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class SuicideElementResource {

	public static final String TURN_TYPE_INDEX = "TURN_TYPE_INDEX";

	@Id
	private String id;
	// 一转还是二转
	private int turn;
	// 元素类型
	private int type;
	// 所需次数
	private int count;

	@Index(name = TURN_TYPE_INDEX, unique = true)
	@JsonIgnore
	public String getTurnTypeIndex() {
		return this.turn + "_" + this.type;
	}

	public static String toTurnTypeIndex(int turn, int type) {
		return turn + "_" + type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
