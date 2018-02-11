package com.mmorpg.mir.model.dirtywords.model;

import com.mmorpg.mir.model.dirtywords.manager.NodeManager;

/**
 * 敏感词库类型
 * 
 * @author YUTAO
 * 
 */
public enum WordsType {
	
	ROLEWORDS() {
		@Override
		public Node findRootNode(NodeManager nodeManager) {
			return nodeManager.getRoleRoot();
		}

	},
	CHATWORDS() {
		@Override
		public Node findRootNode(NodeManager nodeManager) {
			return nodeManager.getChatRoot();
		}
	};

	public abstract Node findRootNode(NodeManager nodeManager);
}
