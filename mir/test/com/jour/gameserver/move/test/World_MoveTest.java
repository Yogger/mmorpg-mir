package com.jour.gameserver.move.test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.jour.gameserver.controllers.PlayerController;
import com.jour.gameserver.model.gameobjects.Player;
import com.jour.gameserver.utils.ThreadPoolManager;
import com.jour.gameserver.world.DirectionEnum;
import com.jour.gameserver.world.World;
import com.jour.gameserver.world.WorldPosition;

public class World_MoveTest {

	private static final Logger logger = Logger.getLogger(World_MoveTest.class);

	private static final Random random = new Random(System.currentTimeMillis());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 这里先模拟一个world创建流程
		World world = new World();
		world.init();
		int threadSize = 10;
		int playerSize = 200;
		AtomicInteger index = new AtomicInteger(0);

		for (int i = 0; i < threadSize; i++) {
			List<Player> playerList = new LinkedList<Player>();
			Map<Long, WorldPosition> playerDirMap = new HashMap<Long, WorldPosition>();
			for (int j = 0; j < playerSize; j++) {
				int x = random.nextInt(500);
				int y = random.nextInt(500);
				byte heading = (byte) random.nextInt(DirectionEnum.values().length);
				WorldPosition position = world.createPosition(1, x, y, heading);
				Player player = new Player(index.incrementAndGet(), new PlayerController(), position);
				player.getMoveController().setSpeed(500);
				player.getMoveController().schedule();
				World.getInstance().spawn(player);
				playerList.add(player);
				playerDirMap.put(player.getObjectId(), position.clone());
			}
			MoveTask task = new MoveTask(playerList, playerDirMap);
			ThreadPoolManager.getInstance().scheduleAtFixedRate(task, 0, 2000);
		}
	}

	public static class MoveTask implements Runnable {

		private final List<Player> playerList;

		private final Map<Long, WorldPosition> playerDirMap;

		public MoveTask(List<Player> playerList, Map<Long, WorldPosition> playerDirMap) {
			this.playerList = playerList;
			this.playerDirMap = playerDirMap;
		}

		@Override
		public void run() {
			for (Player player : playerList) {
				Queue<Byte> roads = createRoads(player);
				player.getMoveController().setNewRoads(roads);
			}
		}

		private final Queue<Byte> createRoads(Player player) {
			Queue<Byte> result = new LinkedList<Byte>();
			byte heading = player.getHeading();
			int x = player.getX();
			int y = player.getY();
			if (x < 0 || x > 500 || y < 0 || y > 500) {
				WorldPosition position = playerDirMap.get(player.getObjectId());
				heading = (byte) faceToPoint(x, y, position.getX(), position.getY());
			}

			int length = random.nextInt(15) + 1;
			for (int i = 0; i < length; i++) {
				result.add(heading);
			}
			return result;
		}

		private int faceToPoint(int x, int y, int tx, int ty) {
			if (x == tx) {
				if (y < ty) {
					// 向下走
					return DirectionEnum.DN.ordinal();
				} else {
					// 向上走
					return DirectionEnum.UP.ordinal();
				}
			} else if (y == tx) {
				if (x < tx) {
					// 向右走
					return DirectionEnum.RI.ordinal();
				} else {
					// 向左走
					return DirectionEnum.LE.ordinal();
				}
			} else {
				if (x < tx && y < ty) {
					// 右下
					return DirectionEnum.RD.ordinal();
				} else if (x < tx && y > ty) {
					// 右上
					return DirectionEnum.RU.ordinal();
				} else if (x > tx && y > ty) {
					// 左上
					return DirectionEnum.LU.ordinal();
				} else {
					// 左下
					return DirectionEnum.LD.ordinal();
				}
			}
		}

	}

}
