package com.mmorpg.mir.model.dirtywords.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.dirtywords.model.Node;
import com.mmorpg.mir.model.dirtywords.model.WordsType;
import com.mmorpg.mir.model.dirtywords.model.Node.SearchResult;

@Component
public class DFAFilter {

	// TEST
	@Autowired
	private NodeManager nodeManager;

	public String filter(String input, WordsType wordsType) {
		int index = 0;
		char[] chars = input.toCharArray();
		Node node = wordsType.findRootNode(nodeManager);
		List<SearchResult> words = new ArrayList<SearchResult>();

		while (index < chars.length) {
			index = searchWord(chars, index, words, node);
		}

		input = replace(words, input);
		words = null;
		return input;
	}

	public boolean containsWords(String content, WordsType wordsType) {
		int index = 0;
		char[] chars = content.toCharArray();
		List<SearchResult> words = new ArrayList<SearchResult>();
		Node node = wordsType.findRootNode(nodeManager);

		while (index < chars.length) {
			index = searchWord(chars, index, words, node);
		}

		return words.size() > 0;
	}

	public void initDataModel() {
		nodeManager.init();
	}

	private String replace(List<SearchResult> words, String input) {
		char chars[] = input.toCharArray();
		for (SearchResult searchResult : words) {
			char replChars[] = searchResult.getWord().toCharArray();
			for (int index = searchResult.getStartIndex(); index < searchResult.getStartIndex() + replChars.length; index++) {
				chars[index] = '*';

			}
		}
		return new String(chars);
	}

	private int searchWord(char characters[], int count, List<SearchResult> words, Node node) {
		SearchResult tempResult = null;
		int startIndex = count;
		for (int index = count; index < characters.length; index++) {
			char c = characters[index];
			node = node.findNode(c);
			if (node == null) {
				if (tempResult != null) {
					words.add(tempResult);
					return index;
				}
				return count + 1;
			} else {
				if (node.getDirtyWord() != null && node.getDirtyWord().length() > 0)
					tempResult = node.new SearchResult(startIndex, node.getDirtyWord());
				if (index == characters.length - 1 && tempResult != null) {
					words.add(tempResult);
					return index + 1;
				}
			}
		}
		return count + 1;
	}
}