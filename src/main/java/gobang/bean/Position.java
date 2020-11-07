package gobang.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 二维坐标
 *
 * @author LiuBing
 * @date 2020/11/5
 */
public class Position implements Comparable<Position> {

	/**
	 * X 轴坐标
	 */
	private final int x;

	/**
	 * Y 轴坐标
	 */
	private final int y;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	private Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 建造方法
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public static Position valueOf(int x, int y) {
		Position position = PositionCache.findInCache(x, y);
		if (Objects.nonNull(position)) {
			return position;
		}
		return new Position(x, y);
	}

	/**
	 * 计算连个坐标点之间相差的坐标数（一个坐标可下一个棋子）
	 * 前提：两个坐标在一条直线
	 *
	 * @param position1
	 * @param position2
	 * @return
	 */
	public static int offsetOf(Position position1, Position position2) {
		if (position1.getX() == position2.getX()) {
			return Math.abs(position1.getY() - position2.getY());
		} else {
			return Math.abs(position1.getX() - position2.getX());
		}
	}

	/**
	 * true：两个坐标点在一条线上
	 */
	public static boolean oneLine(Position position1, Position position2) {
		if (position1.getX() == position2.getX() || position1.getY() == position2.getY()) {
			return true;
		}

		return Math.abs(position1.getX() - position2.getX()) == Math.abs(position1.getY() - position2.getY());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Position that = (Position) o;
		return x == that.x &&
				y == that.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public int compareTo(Position o) {
		return this.x == o.x ? this.y - o.y : this.x - o.x;
	}

	/**
	 * 坐标缓存类
	 */
	private static class PositionCache {

		/**
		 * true：范围内的坐标第一次构建时时缓存
		 * false：类加载时构建并缓存所有范围内的坐标
		 */
		static final boolean LAZY_CACHE = false;

		/**
		 * x轴坐标上下限，包含上下限两个值
		 */
		static final int HIGH_X = 14;
		static final int LOW_X = 0;

		/**
		 * y轴坐标上下限，包含上下限两个值
		 */
		static final int HIGH_Y = 14;
		static final int LOW_Y = 0;

		/**
		 * 缓存容器
		 */
		static final Map<String, Position> CACHE;

		private PositionCache() {
		}

		static {
			int lengthOfX = HIGH_X - LOW_X + 1;
			int lengthOfY = HIGH_Y - LOW_Y + 1;
			int initialCapacity = lengthOfX * lengthOfY * 4 / 3 + 1;
			CACHE = new HashMap<>(initialCapacity);

			if (!LAZY_CACHE) {
				for (int x = LOW_X; x <= HIGH_X; x++) {
					for (int y = LOW_Y; y <= HIGH_Y; y++) {
						CACHE.put(buildKey(x, y), new Position(x, y));
					}
				}
			}
		}

		static boolean inCacheScope(int x, int y) {
			return LOW_X <= x && x <= HIGH_X && LOW_Y <= y && y <= HIGH_Y;
		}

		static String buildKey(int x, int y) {
			return x + "_" + y;
		}

		static Position findInCache(int x, int y) {
			if (!inCacheScope(x, y)) {
				return null;
			}

			String key = buildKey(x, y);
			Position returnValue = CACHE.get(key);
			if (Objects.isNull(returnValue)) {
				synchronized (PositionCache.class) {
					returnValue = CACHE.get(key);
					if (Objects.isNull(returnValue)) {
						returnValue = new Position(x, y);
						CACHE.put(key, returnValue);
					}
				}
			}
			return returnValue;
		}
	}

	@Override
	public String toString() {
		return x + "_" + y;
	}
}
