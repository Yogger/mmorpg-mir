package com.mmorpg.mir.model.dirtywords.manager;

import java.util.Collection;

public interface IDataManager {
	Collection<String> loadDirtyWords(String path);

	String parse(String input);
}
