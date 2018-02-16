package com.mmorpg.mir.model.dirtywords.manager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.dirtywords.model.Node;

/**
 * 敏感词数据模型管理器
 * 
 * @author YUTAO
 */
@Component
public class NodeManager {

	@Autowired
	private DataManager dataManager;

	private Node chatRoot = new Node();

	private Node roleRoot = new Node();

	/** 聊天敏感词库默认名称 */
	private final String CHATPATH = "resource/words_chat.jour";

	/** 创建角色敏感词库默认名称 */
	private final String ROLEPATH = "resource/words_role.jour";

	public void init() {
		insertNode(roleRoot, dataManager.loadDirtyWords(ROLEPATH));
		insertNode(chatRoot, dataManager.loadDirtyWords(CHATPATH));
	}

	public void reloadChat() {
		Node newRoot = new Node();
		insertNode(newRoot, dataManager.loadDirtyWords(CHATPATH));
		chatRoot = newRoot;
	}

	public void reloadRole() {
		Node newRoot = new Node();
		insertNode(newRoot, dataManager.loadDirtyWords(ROLEPATH));
		roleRoot = newRoot;
	}

	private void insertNode(Node root, Collection<String> roleWords) {
		for (String word : roleWords) {
			Node rootTemp = root;
			char chars[] = word.toCharArray();
			for (int index = 0; index < chars.length; index++) {
				rootTemp = rootTemp.insertNode(chars[index]);
				if (index == chars.length - 1)
					rootTemp.setDirtyWord(word);
			}
		}
	}

	public Node getChatRoot() {
		return chatRoot;
	}

	public Node getRoleRoot() {
		return roleRoot;
	}
}
