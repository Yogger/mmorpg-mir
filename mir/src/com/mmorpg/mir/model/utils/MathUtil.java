package com.mmorpg.mir.model.utils;

import java.awt.Point;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.ArrayUtils;

import com.mmorpg.mir.model.commonactivity.CommonActivityConfig;
import com.mmorpg.mir.model.commonactivity.resource.CommonDoubleExpResource;
import com.mmorpg.mir.model.controllers.move.Road;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Monster;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.gameobjects.stats.CreatureGameStats;
import com.mmorpg.mir.model.gameobjects.stats.StatEnum;
import com.mmorpg.mir.model.mergeactive.MergeActiveConfig;
import com.mmorpg.mir.model.openactive.OpenActiveConfig;
import com.mmorpg.mir.model.openactive.model.ActivityEnum;
import com.mmorpg.mir.model.openactive.model.ActivityInfo;
import com.mmorpg.mir.model.rank.manager.WorldRankManager;
import com.mmorpg.mir.model.reward.model.Reward;
import com.mmorpg.mir.model.reward.model.RewardItem;
import com.mmorpg.mir.model.reward.model.RewardType;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.resource.DamageType;
import com.mmorpg.mir.model.world.DirectionEnum;
import com.mmorpg.mir.model.world.Grid;
import com.mmorpg.mir.model.world.Position;
import com.mmorpg.mir.model.world.RoadGrid;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMap;
import com.windforce.common.utility.RandomUtils;

public final class MathUtil {

	public static final float LINE = 25;
	public static final float DIAGONAL = (float) getDistance(0, 0, LINE, LINE);

	private static ThreadLocal<ByteBuffer> tb = new ThreadLocal<ByteBuffer>() {
		protected ByteBuffer initialValue() {
			return ByteBuffer.allocate(10240);
		};
	};

	private static ThreadLocal<Random> tr = new ThreadLocal<Random>() {
		protected Random initialValue() {
			return new Random(System.currentTimeMillis());
		};
	};

	public static Random getRandom() {
		return tr.get();
	}

	/**
	 * Returns distance between two 2D points
	 * 
	 * @param point1
	 *            first point
	 * @param point2
	 *            second point
	 * @return distance between points
	 */
	public static double getDistance(Point point1, Point point2) {
		return getDistance(point1.x, point1.y, point2.x, point2.y);
	}

	/**
	 * Returns distance between two sets of coords
	 * 
	 * @param x1
	 *            first x coord
	 * @param y1
	 *            first y coord
	 * @param x2
	 *            second x coord
	 * @param y2
	 *            second y coord
	 * @return distance between sets of coords
	 */
	public static double getDistance(int x1, int y1, float x2, float y2) {
		// using long to avoid possible overflows when multiplying
		float dx = x2 - x1;
		float dy = y2 - y1;

		// return Math.hypot(x2 - x1, y2 - y1); // Extremely slow
		// return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)); // 20 times faster
		// than hypot
		return Math.sqrt(dx * dx + dy * dy); // 10 times faster then previous
												// line
	}

	public static int getGridDistance(int x1, int y1, int x2, int y2) {
		return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	public static int getStreetDistance(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}

	/**
	 * Returns distance between 3D set of coords
	 * 
	 * @param x1
	 *            first x coord
	 * @param y1
	 *            first y coord
	 * @param z1
	 *            first z coord
	 * @param x2
	 *            second x coord
	 * @param y2
	 *            second y coord
	 * @param z2
	 *            second z coord
	 * @return distance between coords
	 */
	public static double getDistance(float x1, float y1, float z1, float x2, float y2, float z2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		float dz = z1 - z2;

		// We should avoid Math.pow or Math.hypot due to perfomance reasons
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	/**
	 * 
	 * @param object
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static double getDistance(VisibleObject object, int x, int y) {
		return getDistance(object.getX(), object.getY(), x, y);
	}

	/**
	 * Returns closest point on segment to point
	 * 
	 * @param ss
	 *            segment start point
	 * @param se
	 *            segment end point
	 * @param p
	 *            point to found closest point on segment
	 * @return closest point on segment to p
	 */
	public static Point getClosestPointOnSegment(Point ss, Point se, Point p) {
		return getClosestPointOnSegment(ss.x, ss.y, se.x, se.y, p.x, p.y);
	}

	/**
	 * Returns closest point on segment to point
	 * 
	 * @param sx1
	 *            segment x coord 1
	 * @param sy1
	 *            segment y coord 1
	 * @param sx2
	 *            segment x coord 2
	 * @param sy2
	 *            segment y coord 2
	 * @param px
	 *            point x coord
	 * @param py
	 *            point y coord
	 * @return closets point on segment to point
	 */
	public static Point getClosestPointOnSegment(int sx1, int sy1, int sx2, int sy2, int px, int py) {
		double xDelta = sx2 - sx1;
		double yDelta = sy2 - sy1;

		if ((xDelta == 0) && (yDelta == 0)) {
			throw new IllegalArgumentException("Segment start equals segment end");
		}

		double u = ((px - sx1) * xDelta + (py - sy1) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

		final Point closestPoint;
		if (u < 0) {
			closestPoint = new Point(sx1, sy1);
		} else if (u > 1) {
			closestPoint = new Point(sx2, sy2);
		} else {
			closestPoint = new Point((int) Math.round(sx1 + u * xDelta), (int) Math.round(sy1 + u * yDelta));
		}

		return closestPoint;
	}

	/**
	 * Returns distance to segment
	 * 
	 * @param ss
	 *            segment start point
	 * @param se
	 *            segment end point
	 * @param p
	 *            point to found closest point on segment
	 * @return distance to segment
	 */
	public static double getDistanceToSegment(Point ss, Point se, Point p) {
		return getDistanceToSegment(ss.x, ss.y, se.x, se.y, p.x, p.y);
	}

	/**
	 * Returns distance to segment
	 * 
	 * @param sx1
	 *            segment x coord 1
	 * @param sy1
	 *            segment y coord 1
	 * @param sx2
	 *            segment x coord 2
	 * @param sy2
	 *            segment y coord 2
	 * @param px
	 *            point x coord
	 * @param py
	 *            point y coord
	 * @return distance to segment
	 */
	public static double getDistanceToSegment(int sx1, int sy1, int sx2, int sy2, int px, int py) {
		Point closestPoint = getClosestPointOnSegment(sx1, sy1, sx2, sy2, px, py);
		return getDistance(closestPoint.x, closestPoint.y, px, py);
	}

	/**
	 * Checks whether two given instances of AionObject are within given range.
	 * 
	 * @param object1
	 * @param object2
	 * @param range
	 * @return true if objects are in range, false otherwise
	 */
	public static boolean isInRange(VisibleObject object1, VisibleObject object2, int halfRowRange, int halfColRange) {
		if (object1.getPosition() == null || object2.getPosition() == null) {
			return false;
		}
		if (object1.getMapId() != object2.getMapId() || object1.getInstanceId() != object2.getInstanceId())
			return false;
		return (Math.abs((object2.getX() - object1.getX())) <= halfRowRange && Math.abs((object2.getY() - object1
				.getY())) <= halfColRange);
	}

	/**
	 * 目标是否在矩形中
	 * 
	 * @param x
	 * @param y
	 * @param target
	 * @param halfRowRange
	 * @param halfColRange
	 * @return
	 */
	public static boolean isInRange(int x, int y, VisibleObject target, int halfRowRange, int halfColRange) {
		return (Math.abs((target.getX() - x)) <= halfRowRange && Math.abs((target.getY() - y)) <= halfColRange);
	}

	public static boolean isInRange(int fx, int fy, int tx, int ty, int halfRowRange, int halfColRange) {
		return (Math.abs((fx - tx)) <= halfRowRange && Math.abs((fy - ty)) <= halfColRange);
	}

	/**
	 * 
	 * @param obj1X
	 * @param obj1Y
	 * @param obj2X
	 * @param obj2Y
	 * @return float
	 */
	public final static float calculateAngleFrom(float obj1X, float obj1Y, float obj2X, float obj2Y) {
		float angleTarget = (float) Math.toDegrees(Math.atan2(obj2Y - obj1Y, obj2X - obj1X));
		if (angleTarget < 0)
			angleTarget = 360 + angleTarget;
		return angleTarget;
	}

	/**
	 * 
	 * @param obj1
	 * @param obj2
	 * @return float
	 */
	public static float calculateAngleFrom(VisibleObject obj1, VisibleObject obj2) {
		return calculateAngleFrom(obj1.getX(), obj1.getY(), obj2.getX(), obj2.getY());
	}

	/**
	 * 伤害计算
	 * 
	 * @param level
	 * @param damageType
	 * @param effect
	 * @param value
	 * @param percent
	 * @return
	 */
	public static long calculateDamage(int level, DamageType damageType, Effect effect, int value, int percent) {
		return calculateDamage(level, damageType, effect.getEffector(), effect.getEffected(), value, percent);
	}

	public static long calculateDamage(int level, DamageType damageType, Creature effector, Creature effected,
			int value, int percent) {
		StatEnum attackType = ((damageType == DamageType.MAGICAL) ? StatEnum.MAGICAL_ATTACK : StatEnum.PHYSICAL_ATTACK);
		// StatEnum resistType = ((damageType == DamageType.MAGICAL) ?
		// StatEnum.MAGICAL_RESIST : StatEnum.PHYSICAL_RESIST);
		CreatureGameStats<? extends Creature> effectorGameStats = effector.getGameStats();
		CreatureGameStats<? extends Creature> effectedGameStats = effected.getGameStats();
		// 攻-防御基础伤害
		double damage = effectedGameStats.getDamage(attackType, effectorGameStats.getCurrentStat(attackType),
				effectorGameStats.getCurrentStat(StatEnum.IGNORE_DEFENSE));

		long damageResist = 0;
		if (attackType == StatEnum.MAGICAL_ATTACK) {
			damageResist = effectedGameStats.getCurrentStat(StatEnum.MAGICAL_RESIST);
		} else {
			damageResist = effectedGameStats.getCurrentStat(StatEnum.PHYSICAL_RESIST);
		}
		// double percentDamage = damage* (percent * 1.0 / 10000.0)* (1+
		// ((effectorGameStats.getCurrentStat(StatEnum.DAMAGE_INCREASE) +
		// effectorGameStats.getCurrentStat(StatEnum.DAMAGE_INCREASE1)) * 1.0 /
		// 10000)- ((effectedGameStats.getCurrentStat(StatEnum.DAMAGE_REDUCE) +
		// effectedGameStats.getCurrentStat(StatEnum.DAMAGE_REDUCE1)) * 1.0 /
		// 10000) - (damageResist * 1.0 / 10000));

		// 百分比伤害
		double p1 = (percent * 1.0 / 10000.0);
		long e1 = effectorGameStats.getCurrentStat(StatEnum.DAMAGE_INCREASE)
				- effectedGameStats.getCurrentStat(StatEnum.DAMAGE_REDUCE);
		double p2 = 1 + (e1 * 1.0) / 10000;
		long e2 = effectorGameStats.getCurrentStat(StatEnum.DAMAGE_INCREASE1)
				- effectedGameStats.getCurrentStat(StatEnum.DAMAGE_REDUCE1);
		double p3 = 1 + (e2 * 1.0) / 10000;
		double p4 = damageResist * 1.0 / 10000;
		double percentDamage = damage * (p1 * (p2 - p4) * p3);

		// 真实伤害
		double result = value
				+ percentDamage
				+ (effectorGameStats.getCurrentStat(StatEnum.TRUE_DAMAGE) - effectedGameStats
						.getCurrentStat(StatEnum.TRUE_DEFENCE));

		if (effector instanceof Monster) { // 怪物等级压制
			if (((Monster) effector).getObjectResource().isLevelSuppress()) {
				if (effected instanceof Player) {
					Player beenAttacked = (Player) effected;
					int gap = ((Monster) effector).getLevel() - beenAttacked.getLevel();
					result += (effectorGameStats.getCurrentStat(attackType) * Math.min(Math.max(gap - 10, 0), 20) * 0.05);
				}
			}
		}
		// 魔法、物理抵抗 getDamage已经算过了
		// int resist = effectedGameStats.getCurrentStat(resistType);
		// if (resist != 0) {
		// result -= (result * (1 - ((10000 - resist) * 1.0 / 10000)));
		// }
		// 最低伤害1点
		if (result <= 0) {
			result = 1;
		}
		return (long) Math.ceil(result);
	}

	public static long calculateHeal(int level, DamageType damageType, Effect effect, int value, int percent) {
		CreatureGameStats<? extends Creature> effectedGameStats = effect.getEffected().getGameStats();
		double result = value + effectedGameStats.getCurrentStat(StatEnum.MAXHP) * ((percent * 1.0) / 10000);
		return (long) Math.ceil(result);
	}

	/**
	 * 
	 * @param effector
	 * @param effected
	 * @return
	 */
	public static com.mmorpg.mir.model.skill.model.DamageType calculateDamageType(Effect effect) {
		if (effect.getEffector() != null) {
			CreatureGameStats<? extends Creature> effectorGameStats = effect.getEffector().getGameStats();
			CreatureGameStats<? extends Creature> effectedGameStats = effect.getEffected().getGameStats();
			// 破击
			double rate = (effectorGameStats.getCurrentStat(StatEnum.IGNORE) - effectedGameStats
					.getCurrentStat(StatEnum.IGNORE_RESIST)) / 10000.0;
			if (RandomUtils.isHit(rate)) {
				return com.mmorpg.mir.model.skill.model.DamageType.IGNORE;
			}

			// 暴击
			rate = (effectorGameStats.getCurrentStat(StatEnum.CRITICAL) - effectedGameStats
					.getCurrentStat(StatEnum.CRITICAL_RESIST)) / 10000.0;
			if (RandomUtils.isHit(rate)) {
				return com.mmorpg.mir.model.skill.model.DamageType.CRITICAL;
			}
		}

		return com.mmorpg.mir.model.skill.model.DamageType.NORMAL;

	}

	/**
	 * 杀怪经验=MAX((1-1/30*min(max(人物等级-怪物等级 ,10)-10,60))*怪物经验,1)
	 * 
	 * 等级差0-10级，就为本身经验 等级差11-40级，衰减 等级差40+，经验为1
	 * 
	 * @param player
	 * @param reward
	 */
	public static void calcExp(Player player, Reward reward, Monster monster, double rate) {
		double statAddRate = player.getGameStats().getCurrentStat(StatEnum.EXP_PLUS) * 1.0 / 10000;
		int level = player.getLevel();
		// 世界等级的加成
		if (level >= WorldRankManager.getInstance().WORLD_LEVEL_VALID_BASE.getValue()) {
			int dif_w = WorldRankManager.getInstance().getWorldLevel() - level;
			if (dif_w > 0) {
				statAddRate += (dif_w * WorldRankManager.getInstance().WORLD_LEVEL_CONSTANT.getValue() / 100.0);
			}
		}
		ActivityInfo activity = ServerState.getInstance().getCelebrateActivityInfos()
				.get(ActivityEnum.DOUBLE_EXP.getValue());
		CoreConditions celebrateConds = OpenActiveConfig.getInstance().getCelebrateConds();
		if (activity != null && activity.isOpenning() && celebrateConds.verify(player)) {
			statAddRate += OpenActiveConfig.getInstance().CELEBRATE_DOUBLE_EXP_RATE.getValue();
		}

		if (MergeActiveConfig.getInstance().getOpenDoubleExpConditions().verify(player)) {
			statAddRate += MergeActiveConfig.getInstance().OPEN_DOUBLE_EXP_BASEVALUE.getValue();
		}

		// Map<String, ArrayList<String>> addMultipleConditions =
		// OpenActiveConfig.getInstance().ADD_MULTIPLE_CONDITIONS
		// .getValue();
		// Map<String, Integer> addMultipleNumber =
		// OpenActiveConfig.getInstance().ADD_MULTIPLE_NUMBER.getValue();
		// for (String name : addMultipleConditions.keySet()) {
		// if
		// (OpenActiveConfig.getInstance().getCoreConditionsByMultipleName(name).verify(player))
		// {
		// statAddRate += addMultipleNumber.get(name);
		// }
		// }

		for (CommonDoubleExpResource resource : CommonActivityConfig.getInstance().commonDBStorage.getAll()) {
			if (resource.getConditions().verify(player)) {
				statAddRate += resource.getNumber();
			}
		}

		double debuff = 1.0;
		/*
		 * if (player.getCountry().getCountryFlag().costExp() && level >=
		 * ConfigValueManager
		 * .getInstance().COUNTRY_FLAG_BUFF_LEVEL_MIN.getValue()) { debuff =
		 * ConfigValueManager
		 * .getInstance().FLAG_DEAD_DEBUFF_VALUE.getValue().doubleValue(); }
		 */
		List<RewardItem> rewards = reward.getItemsByType(RewardType.EXP);

		double factorA = 1.0;
		if (monster.isExpReduction()) {
			int dif = Math.abs(player.getLevel() - monster.getLevel());
			factorA = (1.0 - 1.0 / 30.0 * Math.min(Math.max(dif, 10.0) - 10.0, 60.0));
		}

		for (RewardItem rewardItem : rewards) {
			rewardItem.setAmount((int) (Math.ceil((Math.max(factorA * rewardItem.getAmount(), 1) * rate * debuff)
					* (1.0 + statAddRate))));
		}
	}

	public static long calDamageRate(Effect effect) {
		long damageIncrease = effect.getEffector().getGameStats().getCurrentStat(StatEnum.CRITICAL_DAMAGE_INCREASE);
		long damageReduct = effect.getEffected().getGameStats().getCurrentStat(StatEnum.CRITICAL_DAMAGE_REDUCE);
		return 10000 + Math.max(0, 5000 + damageIncrease - damageReduct);
	}

	/**
	 * 
	 * @param clientHeading
	 * @return float
	 */
	public final static float convertHeadingToDegree(byte clientHeading) {
		float degree = clientHeading * 3;
		return degree;
	}

	public static Road quickFindRoad(int mapId, int fx, int fy, int tx, int ty) {
		return quickFindRoad(mapId, fx, fy, tx, ty, 0);
	}

	public static Road quickFindRoad(int mapId, int fx, int fy, int tx, int ty, int distance) {
		WorldMap worldMap = World.getInstance().getWorldMap(mapId);

		if (checkPointOut(worldMap, tx, ty)) {
			return null;
		}

		if (fx == tx && fy == ty) {
			return null;
		}

		int dx = tx - fx;
		int dy = ty - fy;
		int ux = dx > 0 ? 1 : -1;
		int uy = dy > 0 ? 1 : -1;
		int x = fx;
		int y = fy;
		int eps = 0;
		dx = Math.abs(dx);
		dy = Math.abs(dy);

		int lx = -1;
		int ly = -1;
		int step = 0;

		byte[] result = new byte[dx + dy];

		if (dx > dy) {
			for (x = fx; x != tx; x += ux) {
				if (lx != -1 && ly != -1) {
					if (checkPointOut(worldMap, x, y)) {
						return null;
					}
					result[step] = (byte) DirectionEnum.directions[x - lx + 1][y - ly + 1].ordinal();
					step++;
				}
				lx = x;
				ly = y;

				eps += dy;
				if ((eps << 1) >= dx) {
					y += uy;
					eps -= dx;
				}
			}
		} else {
			for (y = fy; y != ty; y += uy) {
				if (lx != -1 && ly != -1) {
					if (checkPointOut(worldMap, x, y)) {
						return null;
					}
					result[step] = (byte) DirectionEnum.directions[x - lx + 1][y - ly + 1].ordinal();
					step++;
				}
				lx = x;
				ly = y;
				eps += dx;
				if ((eps << 1) >= dy) {
					x += ux;
					eps -= dy;
				}
			}
		}

		result[step] = (byte) DirectionEnum.directions[x - lx + 1][y - ly + 1].ordinal();

		int length = step + 1;
		distance = length > distance ? distance : length;

		byte[] roadStep = new byte[length - distance];
		System.arraycopy(result, 0, roadStep, 0, roadStep.length);

		return Road.valueOf(roadStep);
	}

	public static Road findRoad(int mapId, int fx, int fy, int tx, int ty) {
		return findRoadByStepAndDistance(mapId, fx, fy, tx, ty, 500, 0);
	}

	public static Road findRoadByStep(int mapId, int fx, int fy, int tx, int ty, int maxStep) {
		return findRoadByStepAndDistance(mapId, fx, fy, tx, ty, maxStep, 0);
	}

	public static Road findRoadByDistance(int mapId, int fx, int fy, int tx, int ty, int distance) {
		return findRoadByStepAndDistance(mapId, fx, fy, tx, ty, 500, distance);
	}

	public static Road findRoadByStepAndDistance(int mapId, int fx, int fy, int tx, int ty, int maxStep, int distance) {
		WorldMap worldMap = World.getInstance().getWorldMap(mapId);

		if (checkPointOut(worldMap, tx, ty)) {
			return null;
		}

		Position start = new Position();
		start.setX(fx);
		start.setY(fy);

		Position end = new Position();
		end.setX(tx);
		end.setY(ty);

		// TODO 这个代码需要重新构建一下
		if (start.equals(end)) {
			return null;
		}

		Grid startGrid = new Grid(start.getX(), start.getY());
		Grid endGrid = new Grid(end.getX(), end.getY());

		RoadGrid startRoadGrid = new RoadGrid();
		startRoadGrid.setGrid(startGrid);
		RoadGrid endRoadGrid = new RoadGrid();
		endRoadGrid.setGrid(endGrid);

		Set<RoadGrid> waitting = new TreeSet<RoadGrid>();

		HashMap<Integer, RoadGrid> counted = new HashMap<Integer, RoadGrid>();

		HashSet<Integer> passed = new HashSet<Integer>();

		waitting.add(startRoadGrid);
		passed.add(startGrid.getKey());

		int step = 0;

		while (waitting.size() > 0 && (step < maxStep)) {
			RoadGrid grid = waitting.iterator().next();
			waitting.remove(grid);

			if (grid.equals(endRoadGrid)) {
				endRoadGrid = grid;
				break;
			}

			step++;

			counted.put(grid.getGrid().getKey(), grid);

			List<Grid> rounds = getRoundGrid(worldMap, grid.getGrid());

			for (Grid round : rounds) {
				if (passed.contains(round.getKey())) {
					continue;
				}

				RoadGrid roundGrid = new RoadGrid();
				roundGrid.setGrid(round);
				roundGrid.setFather(grid.getGrid().getKey());
				passed.add(round.getKey());
				roundGrid.setWeight(getStreetDistance(round.getX(), round.getY(), endGrid.getX(), endGrid.getY()));

				waitting.add(roundGrid);
			}

		}

		if (endRoadGrid.getFather() != -1) {
			byte[] dirs = new byte[step];
			step--;
			RoadGrid grid = endRoadGrid;
			dirs[step] = grid.getGrid().getDir();
			while (grid.getFather() != -1) {
				if (grid.getFather() == startGrid.getKey()) {
					break;
				}
				step--;
				grid = counted.get(grid.getFather());
				dirs[step] = grid.getGrid().getDir();
			}

			byte[] newDirs = new byte[dirs.length - step];
			System.arraycopy(dirs, step, newDirs, 0, newDirs.length);
			dirs = newDirs;

			int length = dirs.length;
			distance = ((length > distance) ? distance : length);

			if (distance > 0) {
				byte[] result = new byte[length - distance];
				System.arraycopy(dirs, 0, result, 0, result.length);
				dirs = result;
			}

			return Road.valueOf(dirs);
		}

		return null;
	}

	private static List<Grid> getRoundGrid(WorldMap map, Grid grid) {
		List<Grid> grids = new ArrayList<Grid>(9);
		boolean out = false;
		if (checkPointOut(map, grid.getX(), grid.getY())) {
			out = true;
		}

		for (DirectionEnum dir : DirectionEnum.values()) {
			int x = grid.getX() + dir.getAddX();
			int y = grid.getY() + dir.getAddY();
			if (checkPointOut(map, x, y) && !out) {
				continue;
			}
			Grid temp = new Grid(x, y);
			temp.setDir((byte) dir.ordinal());
			grids.add(temp);
		}
		grids.add(grid);
		return grids;
	}

	public static byte[] unzipRoads(byte[] data) {
		byte[] result = null;
		if (!ArrayUtils.isEmpty(data)) {
			ByteBuffer buffer = tb.get();
			buffer.clear();
			for (byte r : data) {
				int step = (r >>> 3) & 31;
				byte d = (byte) (r & 7);
				for (int i = 0; i < step; i++) {
					buffer.put(d);
				}
			}
			buffer.flip();
			byte[] temp = buffer.array();
			result = new byte[buffer.limit()];
			System.arraycopy(temp, 0, result, 0, result.length);
		}
		return result;
	}

	public static byte[] zipRoads(byte[] data) {
		byte[] result = null;
		if (!ArrayUtils.isEmpty(data)) {
			ByteBuffer buffer = tb.get();
			buffer.clear();
			byte dir = -1;
			int step = 0;
			for (byte temp : data) {
				if (dir == -1) {
					dir = temp;
					step++;
				} else {
					if (dir == temp) {
						if (step == 31) {
							byte add = (byte) ((step << 3) | dir);
							buffer.put(add);
							step = 1;
						} else {
							step++;
						}
					} else {
						byte add = (byte) ((step << 3) | dir);
						buffer.put(add);
						dir = temp;
						step = 1;
					}
				}
			}
			byte add = (byte) ((step << 3) | dir);
			buffer.put(add);

			buffer.flip();
			byte[] temp = buffer.array();
			result = new byte[buffer.limit()];
			System.arraycopy(temp, 0, result, 0, result.length);
		}
		return result;
	}

	public static DirectionEnum face(int fx, int fy, int tx, int ty) {
		if (fx == tx && fy == ty)
			return null;

		int dx = tx - fx;
		int dy = ty - fy;

		if (dx == 0) {
			if (dy < 0) {
				return DirectionEnum.UP;
			} else {
				return DirectionEnum.DN;
			}
		} else if (dy == 0) {
			if (dx < 0) {
				return DirectionEnum.LE;
			} else {
				return DirectionEnum.RI;
			}
		} else {
			int adx = Math.abs(dx);
			int ady = Math.abs(dy);
			if (adx < ady) {
				if (dy < 0) {
					return DirectionEnum.UP;
				} else {
					return DirectionEnum.DN;
				}
			} else if (adx > ady) {
				if (dx < 0) {
					return DirectionEnum.LE;
				} else {
					return DirectionEnum.RI;
				}
			} else {
				int sx = dx / adx;
				int sy = dy / ady;
				return DirectionEnum.directions[sx + 1][sy + 1];
			}
		}
	}

	public static int[] findRandomPoint(int fx, int fy, int tx, int ty, int step) {
		DirectionEnum dir = face(tx, ty, fx, fy);
		DirectionEnum[] dirs = new DirectionEnum[3];
		dirs[0] = DirectionEnum.values()[(dir.ordinal() + 7) % 8];
		dirs[1] = DirectionEnum.values()[dir.ordinal()];
		dirs[2] = DirectionEnum.values()[(dir.ordinal() + 1) % 8];

		Random random = MathUtil.getRandom();

		for (int i = 0; i < step; i++) {
			DirectionEnum temp = dirs[random.nextInt(3)];
			tx += temp.getAddX();
			ty += temp.getAddY();
		}

		return new int[] { tx, ty };
	}

	public static Road SmoothFindRoadByStep(int mapId, int fx, int fy, int tx, int ty, int maxStep) {
		WorldMap worldMap = World.getInstance().getWorldMap(mapId);

		if (checkPointOut(worldMap, tx, ty)) {
			return null;
		}

		if (fx == tx && fy == ty) {
			return null;
		}

		int dx = Math.abs(tx - fx);
		int dy = Math.abs(ty - fy);

		int length = dx + dy;
		byte[] roads = new byte[length];
		int step = 0;
		for (int i = 0; i < length; i++) {
			DirectionEnum dir = face(fx, fy, tx, ty);
			fx += dir.getAddX();
			fy += dir.getAddY();
			if (checkPointOut(worldMap, fx, fy)) {
				return null;
			}
			roads[step++] = (byte) dir.ordinal();
			if ((fx == tx && fy == ty) || (step == maxStep)) {
				break;
			}
		}
		byte[] realRoads = new byte[step];
		System.arraycopy(roads, 0, realRoads, 0, step);
		return Road.valueOf(realRoads);
	}

	public static Road SmoothFindRoad(int mapId, int fx, int fy, int tx, int ty) {
		WorldMap worldMap = World.getInstance().getWorldMap(mapId);

		if (checkPointOut(worldMap, tx, ty)) {
			return null;
		}

		if (fx == tx && fy == ty) {
			return null;
		}

		int dx = Math.abs(tx - fx);
		int dy = Math.abs(ty - fy);
		if (dx == dy || dx == 0 || dy == 0) {
			int length = Math.max(dx, dy);
			byte[] roads = new byte[length];
			DirectionEnum dir = face(fx, fy, tx, ty);
			for (int i = 0; i < length; i++) {
				fx += dir.getAddX();
				fy += dir.getAddY();
				if (checkPointOut(worldMap, fx, fy)) {
					return null;
				}
				roads[i] = (byte) dir.ordinal();
			}
			return Road.valueOf(roads);
		} else {
			int length = dx + dy;
			byte[] roads = new byte[length];
			DirectionEnum dir = face(fx, fy, tx, ty);
			int step = 0;
			for (int i = 0; i < length; i++) {
				fx += dir.getAddX();
				fy += dir.getAddY();
				if (checkPointOut(worldMap, fx, fy)) {
					return null;
				}
				roads[step++] = (byte) dir.ordinal();
				dx = Math.abs(tx - fx);
				dy = Math.abs(ty - fy);
				if (dx == dy || dx == 0 || dy == 0) {
					break;
				}
			}
			length = Math.max(dx, dy);
			dir = face(fx, fy, tx, ty);
			for (int i = 0; i < length; i++) {
				fx += dir.getAddX();
				fy += dir.getAddY();
				if (checkPointOut(worldMap, fx, fy)) {
					return null;
				}
				roads[step++] = (byte) dir.ordinal();
			}
			byte[] realRoads = new byte[step];
			System.arraycopy(roads, 0, realRoads, 0, step);
			return Road.valueOf(realRoads);
		}
	}

	private static boolean checkPointOut(WorldMap worldMap, int x, int y) {
		// return false;
		return worldMap.isOut(x, y) || worldMap.isBlock(x, y);
	}

	public static void main(String[] args) {
		// int fx = 44;
		// int fy = 29;
		// Road road = SmoothFindRoadByStep(0, fx, fy, 36, 29, 13);
		// for (byte ddd : road.getRoads()) {
		// DirectionEnum dir = DirectionEnum.values()[ddd];
		// fx += dir.getAddX();
		// fy += dir.getAddY();
		// System.out.println("fx : " + fx + " fy : " + fy + " dir : " + dir);
		// }
		// / * 杀怪经验=MAX((1-1/30*min(max(人物等级-怪物等级 ,10)-10,60))*怪物经验,1)
		// Scanner scanner = new Scanner(System.in);
		// while (scanner.hasNext()) {
		// int dif = scanner.nextInt();
		// double factorA = (1 - 1 / 30.0 * Math.min(Math.max(dif, 10) - 10,
		// 60));
		// double monster = 10000.0;
		// System.out.println(Math.ceil(Math.max(factorA * monster, 1)));
		// }

		int length = 1000;
		Random random = new Random(System.currentTimeMillis());
		byte[] road = new byte[length];
		for (int i = 0; i < length; i++) {
			road[i] = (byte) random.nextInt(8);
		}

		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			byte[] temp1 = zipRoads(road);
			unzipRoads(temp1);
		}
		System.out.println("use : " + (System.currentTimeMillis() - start) / 100000.0);
		System.out.println((float) getDistance(0, 0, 25, 25));
		System.out.println((int) getDistance(0, 0, 25, 25));

	}
}
