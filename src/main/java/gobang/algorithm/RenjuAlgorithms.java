package gobang.algorithm;

import gobang.bean.ColorEnum;
import gobang.bean.DirectionEnum;
import gobang.bean.Position;

import java.util.*;
import java.util.stream.Collectors;

import static gobang.algorithm.CommonAlgorithms.afterOffSet;
import static gobang.algorithm.CommonAlgorithms.isSameColorChess;

/**
 * 连珠算法
 *
 * @author LiuBing
 * @date 2020/11/5
 */
public class RenjuAlgorithms {

	/**
	 * 赢棋连珠数
	 */
	public static final int WIN_RENJU_NUM = 5;

	/**
	 * 从原点出发，从横、纵、两斜向找出能赢棋的所有连珠
	 *
	 * @param boardInfo      棋盘[x][y] 每个点的值决定是否有棋及棋子颜色
	 * @param piecesColor    棋子颜色
	 * @param originPosition 原点坐标
	 * @return <所有方向上的连珠<一条线上的连珠列表>>
	 */
	public static List<List<Position>> checkWinRenju(
			int[][] boardInfo, ColorEnum piecesColor, Position originPosition) {
		Map<DirectionEnum, List<Position>> renjuMap =
				calculateAllRenjuStartAt(originPosition, boardInfo, piecesColor, WIN_RENJU_NUM);

		return renjuMap.isEmpty() ? Collections.emptyList() :
				renjuMap
						.values()
						.stream()
						.collect(Collectors.toList());
	}

	/**
	 * 从原点出发，从横、纵、两斜向找出所有连珠
	 *
	 * @param originPosition 原点坐标
	 * @param boardInfo      棋盘[x][y] 每个点的值决定是否有棋及棋子颜色
	 * @param piecesColor    棋子颜色
	 * @param minRenjuNum    最小连珠数，一条线上的连珠数大于等于最小连珠数才会返回
	 * @return
	 */
	private static Map<DirectionEnum, List<Position>> calculateAllRenjuStartAt(
			Position originPosition, int[][] boardInfo, ColorEnum piecesColor, int minRenjuNum) {
		Map<DirectionEnum, List<Position>> renjuMap = new EnumMap<>(DirectionEnum.class);
		for (DirectionEnum direction : DirectionEnum.values()) {
			List<Position> renjuList = checkRenjuInThe(
					direction, boardInfo, piecesColor, originPosition, minRenjuNum);
			if (!renjuList.isEmpty()) {
				renjuMap.put(direction, renjuList);
			}
		}
		return renjuMap.isEmpty() ? Collections.emptyMap() : renjuMap;
	}

	/**
	 * 从原点(x, y)出发，从不同方向往两边找连珠坐标
	 *
	 * @param direction      方向
	 * @param boardInfo      棋盘及棋子信息
	 * @param piecesColor    玩家棋色
	 * @param originPosition 原点坐标
	 * @param minRenjuNum    最小连珠数
	 * @return null：方向无连珠；nonNull：连珠坐标列表
	 */
	private static List<Position> checkRenjuInThe(
			DirectionEnum direction, int[][] boardInfo, ColorEnum piecesColor,
			Position originPosition, int minRenjuNum) {

		List<Position> returnValue = new ArrayList<>(10);
		returnValue.add(originPosition);

		// 同时往两边找
		boolean toInitialPoint = true;
		boolean awayInitialPoint = true;
		for (int offsetNum = 1; toInitialPoint || awayInitialPoint; offsetNum++) {
			if (toInitialPoint) {
				Position positionToLeft = afterOffSet(
						direction, true, originPosition, offsetNum, boardInfo);
				if (Objects.nonNull(positionToLeft)
						&& isSameColorChess(boardInfo, piecesColor, positionToLeft)) {
					returnValue.add(positionToLeft);
				} else {
					toInitialPoint = false;
				}
			}

			if (awayInitialPoint) {
				Position positionToRight = afterOffSet(
						direction, false, originPosition, offsetNum, boardInfo);
				if (Objects.nonNull(positionToRight)
						&& isSameColorChess(boardInfo, piecesColor, positionToRight)) {
					returnValue.add(positionToRight);
				} else {
					awayInitialPoint = false;
				}
			}
		}

		if (returnValue.size() >= minRenjuNum) {
			returnValue.sort(Position::compareTo);
			return returnValue;
		} else {
			return Collections.emptyList();
		}
	}

	private RenjuAlgorithms() {
	}
}
