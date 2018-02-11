package com.mmorpg.mir.model.skill.resource.grid;

import java.util.List;
import java.util.Map;

import org.h2.util.New;

import com.mmorpg.mir.model.world.DirectionEnum;

public class Node {
	private int x;
	private int y;
	private Map<DirectionEnum, Node> ns;
	private int value;

	public static Node valueOf(int x, int y, Map<DirectionEnum, Node> ns, int value) {
		Node node = new Node();
		node.x = x;
		node.y = y;
		node.ns = ns;
		node.value = value;
		return node;
	}

	public static void main(String[] args) {
		Node node1 = Node.valueOf(1, 1, null, 1);
		Node node2 = Node.valueOf(1, 1, null, 1);
		List<Node> nodes = New.arrayList();
		nodes.add(node1);
		System.out.println(nodes.contains(node2));

	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		}
		if (obj instanceof Node) {
			Node target = (Node) obj;
			if (this.x == target.x && this.y == target.y) {
				return true;
			}
		}
		return false;

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Map<DirectionEnum, Node> getNs() {
		return ns;
	}

	public void setNs(Map<DirectionEnum, Node> ns) {
		this.ns = ns;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("[%s:%s:%s]", new Object[] { x, y, value });
	}

}
