package com.mmorpg.mir.model.dirtywords.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.dirtywords.manager.DFAFilter;
import com.mmorpg.mir.model.dirtywords.model.WordsType;

/**
 * 敏感词管理器
 * 
 * @author YUTAO
 */
@Component
public class DirtyWordsManager implements IDirtyWordsManager {

	@Autowired
	private DFAFilter filter;

	private static DirtyWordsManager instance;

	@PostConstruct
	public void initManager() {
		filter.initDataModel();
		instance = this;
	}

	/**
	 * 过滤敏感字符
	 * 
	 * @param content
	 * @return
	 */
	public String filter(String content, WordsType wordsType) {
		return filter.filter(content, wordsType);
	}

	/**
	 * 检测是否包含敏感字符
	 * 
	 * @param content
	 * @return
	 */
	public boolean containsWords(String content, WordsType wordsType) {
		return filter.containsWords(content, wordsType);
	}

	public static DirtyWordsManager getInstance() {
		return instance;
	}
}
