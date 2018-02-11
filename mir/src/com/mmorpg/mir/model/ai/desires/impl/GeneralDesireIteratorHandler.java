package com.mmorpg.mir.model.ai.desires.impl;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.mmorpg.mir.model.ai.AI;
import com.mmorpg.mir.model.ai.desires.Desire;
import com.mmorpg.mir.model.ai.desires.DesireIteratorHandler;

public class GeneralDesireIteratorHandler implements DesireIteratorHandler {

	private static final Logger logger = Logger.getLogger(GeneralDesireIteratorHandler.class);

	private AI ai;

	/**
	 * @param ai
	 */
	public GeneralDesireIteratorHandler(AI ai) {
		super();
		this.ai = ai;
	}

	@Override
	public void next(Desire desire, Iterator<Desire> iterator) {
		boolean isOk = false;
		try {
			isOk = desire.handleDesire(ai);
		} catch (Exception e) {
			logger.error(ai.getOwner().toString(), e);
		}
		if (!isOk) {
			desire.onClear();
			iterator.remove();
		}
	}
}
