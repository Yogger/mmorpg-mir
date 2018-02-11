package com.mmorpg.mir.model.skill.resource.grid;

import java.util.ArrayList;
import java.util.List;

import com.mmorpg.mir.model.world.DirectionEnum;

public class DNode {
	private DirectionEnum d;
	private List<DNode> ds = new ArrayList<DNode>();

	public DNode() {
	}

	public DNode(DirectionEnum d) {
		this.d = d;
	}

	public DirectionEnum getD() {
		return d;
	}

	public void setD(DirectionEnum d) {
		this.d = d;
	}

	public List<DNode> getDs() {
		return ds;
	}

	public void setDs(List<DNode> ds) {
		this.ds = ds;
	}

}
