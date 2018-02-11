package com.mmorpg.mir.model.dirtywords.manager;

import com.mmorpg.mir.model.dirtywords.model.Node;

public interface INodeManager {
	void init();

	Node getChatRoot();

	Node getRoleRoot();
}
