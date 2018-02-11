package com.mmorpg.mir.model.dirtywords.service;

import com.mmorpg.mir.model.dirtywords.model.WordsType;

public interface IDirtyWordsManager {
	String filter(String content, WordsType wordsType);

	boolean containsWords(String content, WordsType wordsType);
}
