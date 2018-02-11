package com.mmorpg.mir.model.dirtywords.model;

import java.util.HashMap;
import java.util.Map;

public class Node {

	private char character;
	private String dirtyWord;
	private Map<Character, Node> child = new HashMap<Character, Node>();

	public Node(char character) {
		this.character = character;
	}

	public Node() {
	}

	public Node insertNode(Character character) {
		Node result = child.get(character);
		if (result == null) {
			result = new Node(character);
			child.put(character, result);
		}
		return result;
	}

	public Node findNode(Character character) {
		return child.get(character);
	}

	public char getCharacter() {
		return character;
	}

	public void setCharacter(char character) {
		this.character = character;
	}

	public String getDirtyWord() {
		return dirtyWord;
	}

	public void setDirtyWord(String dirtyWord) {
		this.dirtyWord = dirtyWord;
	}

	public class SearchResult {
		private int startIndex;
		private String word;

		public SearchResult(int startIndex, String word) {
			this.startIndex = startIndex;
			this.word = word;
		}

		public int getStartIndex() {
			return startIndex;
		}

		public void setStartIndex(int startIndex) {
			this.startIndex = startIndex;
		}

		public String getWord() {
			return word;
		}

		public void setWord(String word) {
			this.word = word;
		}
	}
}
