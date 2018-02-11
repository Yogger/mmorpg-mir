package com.mmorpg.mir.model.player.event;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.windforce.common.event.event.IEvent;

/**
 * 登录事件体
 * 
 * @author frank
 */
public class CreateEvent implements IEvent {

	/** 事件名 */
	public static String NAME = "common:create";

	/** 登录的用户标识 */
	private long id;

	private String op;

	public static IEvent valueOf(long id, String op) {
		CreateEvent body = new CreateEvent();
		body.id = id;
		body.op = op;
		return body;
	}

	// Getter and Setter ...

	public long getOwner() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOp() {
		return op;
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
