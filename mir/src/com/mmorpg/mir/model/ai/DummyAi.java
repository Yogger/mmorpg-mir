package com.mmorpg.mir.model.ai;

import com.mmorpg.mir.model.ai.event.Event;

/**
 * 什么都不做的ai
 * 
 * @author liuzhou
 * 
 */
public class DummyAi extends AI {
	public DummyAi() {
	}

	@Override
	public void schedule() {
		// 什么也不做
	}

	@Override
	public void handleEvent(Event event) {
		//
	}

	@Override
	public void analyzeState() {
		//
	}

	@Override
	public void run() {
		//
	}
}
