package gobang.algorithm;

import gobang.bean.ColorEnum;
import gobang.bean.DirectionEnum;
import gobang.bean.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static gobang.bean.ColorEnum.EMPTY;

/**
 * 棋盘通用算法
 *
 * @author LiuBing
 * @date 2020/11/5
 */
public class CommonAlgorithms {

	/**
	 * 找出棋盘中同色棋子坐标
	 */
	public static List<Position> checkChessPositionFrom(int[][] chessBoardInfo, ColorEnum pieceColor) {
		List<Position> returnValue = new ArrayList<>(10);
		for (int x = 0; x < chessBoardInfo.length; x++) {
			int[] yArray = chessBoardInfo[x];
			for (int y = 0; y < yArray.length; y++) {
				if (chessBoardInfo[x][y] == pieceColor.ordinal()) {
					returnValue.add(Position.valueOf(x, y));
				}
			}
		}
		return returnValue;
	}

	/**
	 * 找出棋盘中检所有空坐标
	 */
	public static List<Position> checkEmptyChessPositionFrom(int[][] chessBoardInfo) {
		List<Position> returnValue = new ArrayList<>(10);
		for (int x = 0; x < chessBoardInfo.length; x++) {
			int[] yArray = chessBoardInfo[x];
			for (int y = 0; y < yArray.length; y++) {
				if (chessBoardInfo[x][y] == EMPTY.ordinal()) {
					returnValue.add(Position.valueOf(x, y));
				}
			}
		}
		return returnValue;
	}

	/**
	 * 从原点出发，计算偏移 n 个棋位后的坐标
	 *
	 * @param direction      方向
	 * @param toInitialPoint true：往左上/下角、往左/上；false：往右上/下角、往右/下
	 * @param originPosition 原点
	 * @param offsetNum      偏移棋位数
	 * @return nonNull：坐标；null：超出棋盘
	 */
	public static Position afterOffSet(
			DirectionEnum direction, boolean toInitialPoint, Position originPosition, int offsetNum, int[][] chessBoardInfo) {
		int xAfterOffSet = xAfterOffset(direction, toInitialPoint, originPosition.getX(), offsetNum);
		int yAfterOffSet = yAfterOffset(direction, toInitialPoint, originPosition.getY(), offsetNum);
		return isLegalPosition(xAfterOffSet, yAfterOffSet, chessBoardInfo) ?
				Position.valueOf(xAfterOffSet, yAfterOffSet) : null;
	}

	/**
	 * true：同色棋子
	 */
	public static boolean isSameColorChess(
			int[][] chessBoardInfo, ColorEnum pieceColor, Position position) {
		return Objects.nonNull(position)
				&& isLegalPosition(position.getX(), position.getY(), chessBoardInfo)
				&& chessBoardInfo[position.getX()][position.getY()] == pieceColor.ordinal();
	}

	/**
	 * true：该坐标点无棋子
	 */
	public static boolean isLegalEmptyPosition(int[][] chessBoardInfo, Position position) {
		return Objects.nonNull(position)
				&& isLegalPosition(position.getX(), position.getY(), chessBoardInfo)
				&& chessBoardInfo[position.getX()][position.getY()] == 0;
	}

	public static boolean isLegalPosition(int x, int y, int[][] chessBoardInfo) {
		int highX = chessBoardInfo.length - 1;
		int highY = chessBoardInfo[0].length - 1;
		return 0 <= x && x <= highX && 0 <= y && y <= highY;
	}

	public static int calculateTotalPositionNum(int[][] chessBoardInfo) {
		return chessBoardInfo.length * chessBoardInfo[0].length;
	}

	/**
	 * 获取往一个方向偏移棋位后的 X 轴坐标值
	 *
	 * @param direction      方向
	 * @param toInitialPoint true：往左上/下角、往左/上；false：往右上/下角、往右/下
	 * @param init           初始值
	 * @param offsetNum      偏移棋位
	 * @return X 轴坐标值
	 */
	private static int xAfterOffset(DirectionEnum direction, boolean toInitialPoint, int init, int offsetNum) {
		switch (direction) {
			case HORIZONTAL:
			case TILT_DOWN:
			case TILT_UP:
				return toInitialPoint ? init - offsetNum : init + offsetNum;
			case VERTICAL:
				return init;
			default:
				return -1;
		}
	}

	/**
	 * 获取往一个方向偏移棋位后的 Y 轴坐标值
	 *
	 * @param direction      方向
	 * @param toInitialPoint true：往左/竖直方向往下；false：往右/竖直方向往上
	 * @param init           初始值
	 * @param offsetNum      偏移棋位
	 * @return Y 轴坐标值
	 */
	private static int yAfterOffset(DirectionEnum direction, boolean toInitialPoint, int init, int offsetNum) {
		switch (direction) {
			case HORIZONTAL:
				return init;
			case VERTICAL:
			case TILT_DOWN:
				return toInitialPoint ? init - offsetNum : init + offsetNum;
			case TILT_UP:
				return toInitialPoint ? init + offsetNum : init - offsetNum;
			default:
				return -1;
		}
	}

	/**
	 * 判断坐标棋子类型
	 *
	 * @param position       棋位坐标
	 * @param chessBoardInfo 棋盘信息
	 * @return -
	 */
	public static int chessPieceTypeIn(Position position, int[][] chessBoardInfo) {
		return chessBoardInfo[position.getX()][position.getY()];
	}

	private CommonAlgorithms() {
	}
}
