package com.mmorpg.mir.model.rescue.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.rescue.config.RescueConfig;
import com.mmorpg.mir.model.rescue.resource.RescueResource;

public class RescueItem implements Comparable<RescueItem> {
	// 配置表里面的ID
	private String id;
	private transient RescuePhase phase;
	// 当前采集或者杀怪数量
	private int value;
	// 当前任务状态
	private byte rescuePhase;
	// 任务所在位置1-5，1则为第一个任务
	private int index;

	public static RescueItem valueOf(RescueResource rescueResource) {
		RescueItem ri = new RescueItem();
		ri.id = rescueResource.getId();
		if (rescueResource.getIndex() != 1) {
			ri.phase = RescuePhase.INACCEPT;
		} else {
			if (rescueResource.getType() == RescueType.CHAT) {
				ri.phase = RescuePhase.INREWARD;
			} else {
				ri.phase = RescuePhase.INCOMPLETE;
			}
		}
		ri.rescuePhase = ri.phase.getValue();
		ri.value = 0;
		ri.index = rescueResource.getIndex();
		return ri;
	}

	@JsonIgnore
	public RescueResource getResource() {
		return RescueConfig.getInstance().getResource(id);
	}

	@JsonIgnore
	public RescueResource getNextResource() {
		return RescueConfig.getInstance().getResource(RescueConfig.getInstance().getResource(id).getNextId());
	}

	@JsonIgnore
	public boolean isComplete() {
		if (value >= getResource().getComplete()) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	public void addValue() {
		value++;
	}

	public RescuePhase getPhase() {
		return phase;
	}

	public void setPhase(RescuePhase phase) {
		this.phase = phase;
		setRescuePhase(this.phase.getValue());
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte getRescuePhase() {
		return rescuePhase;
	}

	public void setRescuePhase(byte rescuePhase) {
		this.rescuePhase = rescuePhase;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public int compareTo(RescueItem o) {
		return getIndex() - o.getIndex();
	}

}
