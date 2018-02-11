package com.mmorpg.mir.model.skill.resource.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.h2.util.New;

import com.mmorpg.mir.model.world.DirectionEnum;

public class NodeDemo {

	public static final int LENGTH = 20;
	public static byte[][] marks;

	private static List<Node> nodes;

	public static Node get(int x, int y) {
		for (Node node : nodes) {
			if (node.getX() == x && node.getY() == y) {
				return node;
			}
		}
		return null;
	}

	public static void spinD(DNode dn, Node node) {
		Map<DirectionEnum, DNode> md = New.hashMap();
		for (Entry<DirectionEnum, Node> entry : node.getNs().entrySet()) {
			if (entry.getValue().getValue() == 1 && !have.contains(entry.getValue())) {
				DNode ndn = new DNode(entry.getKey());
				dn.getDs().add(ndn);
				md.put(entry.getKey(), ndn);
				have.add(entry.getValue());
			}
		}
		if (!md.isEmpty()) {
			for (Entry<DirectionEnum, Node> entry : node.getNs().entrySet()) {
				if (entry.getValue().getValue() == 1 && md.containsKey(entry.getKey())) {
					spinD(md.get(entry.getKey()), entry.getValue());
				}
			}
		}
	}

	static {
		marks = new byte[LENGTH][LENGTH];
		// -------------------- 0--1--2--3--4--5--6--7--8--9-10-11-12-13-14-15-16-17-18-19
		marks[0] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		marks[1] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		marks[2] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		marks[3] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		marks[4] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		marks[5] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		marks[6] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		marks[7] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		marks[8] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		marks[9] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		marks[10] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		marks[11] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		marks[12] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		marks[13] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		marks[14] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		marks[15] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		marks[16] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		marks[17] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		marks[18] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		marks[19] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		nodes = New.arrayList();

		for (int i = 0; i < marks.length; i++) {
			for (int j = 0; j < marks[i].length; j++) {
				nodes.add(Node.valueOf(j, i, new HashMap<DirectionEnum, Node>(), marks[i][j]));

			}
		}

		for (Node node : nodes) {
			for (DirectionEnum de : DirectionEnum.values()) {
				int x = node.getX() + de.getAddX();
				int y = node.getY() + de.getAddY();
				if (out(y, x)) {
					continue;
				}
				node.getNs().put(de, get(x, y));
			}
		}
		// int nodeCount = 9 + LENGTH;
		// System.out.println(nodes.get(nodeCount));
		// System.out.println();
		// for (Node node : nodes.get(nodeCount).getNs().values()) {
		// System.out.println(node.toString());
		// }

	}

	public static boolean out(int x, int y) {
		if (x < 0 || y < 0 || x >= LENGTH || y >= LENGTH) {
			return true;
		}
		return false;
	}

	public static List<Node> have = New.arrayList();

	public static DNode spin(int x, int y) {
		Node root = get(x, y);
		if (root.getValue() != 1) {
			throw new RuntimeException("根节点不为1");
		}
		have.add(root);

		DNode dnRoot = new DNode();
		spinD(dnRoot, root);
		return dnRoot;

	}

	// public static Map<Node, List<DirectionEnum>> directions;

	public static Map<Node, List<DirectionEnum>> createDirections(int x, int y) {
		Map<Node, List<DirectionEnum>> directions = New.hashMap();
		Node root = get(x, y);
		for (Node node : nodes) {
			if (node.getValue() == 1 && !node.equals(root)) {
				have.add(node);
			}
		}
		directions = New.hashMap();
		for (Node node : have) {
			directions.put(node, new ArrayList<DirectionEnum>());
			int dx = root.getX() - node.getX();
			int dy = root.getY() - node.getY();
			if (dx > 0) {
				for (int i = 0; i < Math.abs(dx); i++) {
					directions.get(node).add(DirectionEnum.LE);
				}
			} else {
				for (int i = 0; i < Math.abs(dx); i++) {
					directions.get(node).add(DirectionEnum.RI);
				}
			}
			if (dy > 0) {
				for (int i = 0; i < Math.abs(dy); i++) {
					directions.get(node).add(DirectionEnum.UP);
				}
			} else {
				for (int i = 0; i < Math.abs(dy); i++) {
					directions.get(node).add(DirectionEnum.DN);
				}
			}
		}

		return directions;
	}

	public static List<List<DirectionEnum>> spin(Map<Node, List<DirectionEnum>> directions, int direction) {
		List<List<DirectionEnum>> result = New.arrayList();
		for (List<DirectionEnum> des : directions.values()) {
			List<DirectionEnum> newDe = new ArrayList<DirectionEnum>();
			for (DirectionEnum de : des) {
				int ordinal = 0;
				if ((de.ordinal() + direction) > DirectionEnum.values().length - 1) {
					ordinal = (de.ordinal() + direction) - DirectionEnum.values().length;
				} else {
					ordinal = (de.ordinal() + direction);
				}
				newDe.add(DirectionEnum.indexOrdinal(ordinal));
			}
			result.add(newDe);
		}
		return result;
	}

	public static void main(String[] args) {
		int x = 9;
		int y = 11;
		show();
		// DNode dn = spin(4, 6);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		Map<Node, List<DirectionEnum>> deMap = createDirections(x, y);
		// System.out.println(JsonUtils.object2String(spin(deMap, 1)));
		for (int i = 0; i < 8; i++) {
			System.out.println(">>>>>>>>>>>>>>>>direct:" + i);
			initNodes();
			List<List<DirectionEnum>> result = spin(deMap, i);
			setNodes(result, y, x);
			show();
			System.out.println();
		}
	}

	public static void initNodes() {
		for (int i = 0; i < LENGTH; i++) {
			for (int j = 0; j < LENGTH; j++) {
				marks[i][j] = 0;
			}
		}
	}

	public static void setNodes(List<List<DirectionEnum>> des, int x, int y) {
		marks[x][y] = 1;
		for (List<DirectionEnum> del : des) {
			int dx = x;
			int dy = y;
			for (DirectionEnum de : del) {
				dx += de.getAddY();
				dy += de.getAddX();
			}
			if (!out(dx, dy)) {
				marks[dx][dy] = 1;
			}
		}
	}

	public static void show() {
		// int old = 0;
		// for (Node node : nodes) {
		// if (node.getY() != old) {
		// System.out.println();
		// old = node.getY();
		// }
		// if (node.getValue() == 0) {
		// System.out.print("-");
		// } else {
		// System.out.print("*");
		// }
		// }

		for (int i = 0; i < LENGTH; i++) {
			for (int j = 0; j < LENGTH; j++) {
				if (marks[i][j] == 0) {
					System.out.print("  -");
				} else {
					System.out.print("  *");
				}
			}
			System.out.println();
		}
	}
}
