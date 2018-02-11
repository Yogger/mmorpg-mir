package com.mmorpg.mir.model.ai.desires;

import java.util.Iterator;

public interface DesireIteratorHandler {
	public void next(Desire desire, Iterator<Desire> iterator);
}
