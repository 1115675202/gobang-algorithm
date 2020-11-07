package gobang;

import gobang.algorithm.RenjuAlgorithms;
import gobang.algorithm.ai.AiAlgorithms;
import gobang.bean.ColorEnum;
import gobang.bean.Position;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author LiuBing
 * @date 2020/11/5
 */
public class TestStarter {

	/**
	 * 每一步间隔时间（毫秒）
	 */
	private static final long EACH_STEP_INTERVAL_TIME_MILLIS = 5;

	/**
	 * 每一局间隔时间（毫秒）
	 */
	private static final long EACH_TURN_INTERVAL_TIME_MILLIS = 5000;

	/**
	 * 局数
	 */
	private static final int GAME_TURN = 10;

	/**
	 * 比分
	 */
	private static int blackWinTimes;
	private static int whiteWinTimes;
	private static int deuceTimes;

	/**
	 * 本局起手棋色
	 */
	private static ColorEnum startColor = ColorEnum.BLACK;

	/**
	 * 当前手棋色
	 */
	private static ColorEnum currentColor;

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < GAME_TURN; i++) {
			gaming();
			// 换起手方
			startColor = ColorEnum.BLACK.equals(currentColor) ? ColorEnum.WHITE : ColorEnum.BLACK;
			Thread.sleep(EACH_TURN_INTERVAL_TIME_MILLIS);
		}
	}

	private static void gaming() throws InterruptedException {
		int[][] boardInfo = new int[15][15];
		currentColor = startColor;
		while (true) {
			// 本方和对方棋色
			ColorEnum ownColor = currentColor;
			ColorEnum opponentColor = ColorEnum.BLACK.equals(ownColor) ? ColorEnum.WHITE : ColorEnum.BLACK;
			// 计算出下棋点
			Position position = AiAlgorithms.checkChessPosition(boardInfo, ownColor, opponentColor);
			if (Objects.isNull(position)) {
				deuceTimes++;
				printScore(true);
				return;
			}
			// 下棋到棋盘
			boardInfo[position.getX()][position.getY()] = ownColor.ordinal();
			// 检查是否连珠胜利
			List<List<Position>> renjuGroup = RenjuAlgorithms.checkWinRenju(boardInfo, ownColor, position);

			System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

			if (renjuGroup.isEmpty()) {
				// 游戏中打印棋局
				printGamingBoard(boardInfo, position);
			} else {
				// 本局结束打印棋局
				printFinishBoard(boardInfo, position, renjuGroup);
			}

			if (!renjuGroup.isEmpty()) {
				if (ColorEnum.BLACK.equals(currentColor)) {
					blackWinTimes++;
				} else {
					whiteWinTimes++;
				}
				printScore(false);
				return;
			}
			// 换手
			currentColor = opponentColor;

			Thread.sleep(EACH_STEP_INTERVAL_TIME_MILLIS);
		}
	}

	private static void printScore(boolean currentTurnDeuce) {
		String startColorStr = ColorEnum.BLACK.equals(startColor) ? "黑方" : "白方";
		String turnResult;
		if (currentTurnDeuce) {
			turnResult = "平局";
		} else if (ColorEnum.BLACK.equals(currentColor)) {
			turnResult = "黑方胜";
		} else {
			turnResult = "白方胜";
		}
		System.out.println(String.format(
				"\t\t\t\t\t\t\t\t\t\t起手：%s —— 本局结果：%s —— %s VS %s（黑方 VS 白方）—— 平局：%s",
				startColorStr, turnResult, blackWinTimes, whiteWinTimes, deuceTimes));
	}

	private static void printGamingBoard(int[][] boardInfo, Position lastPut) {
		String lineHeadStr = "\t\t\t\t\t\t\t\t\t\t\t";
		StringBuilder firstLineStr =
				new StringBuilder()
						.append(lineHeadStr)
						.append("\t");
		for (int i = 0; i < boardInfo.length; i++) {
			firstLineStr.append(i < 10 ? String.format(" %s ", i) : String.format("%s ", i));
		}
		System.out.println(firstLineStr);
		for (int y = 0; y < boardInfo[0].length; y++) {
			StringBuilder lineStr = new StringBuilder(lineHeadStr);
			if (y < 10) {
				lineStr.append(" ");
			}
			lineStr.append(y).append("  ");
			for (int x = 0; x < boardInfo.length; x++) {
				boolean isLastPut = lastPut.getX() == x && lastPut.getY() == y;
				lineStr.append(covertPieces(boardInfo[x][y], isLastPut)).append(" ");
			}
			System.out.println(lineStr);
		}
	}

	private static void printFinishBoard(int[][] boardInfo, Position lastPut, List<List<Position>> renjuGroup) {
		List<String> renjuList = renjuGroup
				.stream()
				.flatMap(List::stream)
				.map(Position::toString)
				.collect(Collectors.toList());

		String lineHeadStr = "\t\t\t\t\t";
		String intervalStr = "  |   ";
		String firstLineStr = lineHeadStr + "\t";
		StringBuilder xAxisNumberStr = new StringBuilder();
		for (int i = 0; i < boardInfo.length; i++) {
			xAxisNumberStr.append(i < 10 ? String.format(" %s ", i) : String.format("%s ", i));
		}
		firstLineStr += xAxisNumberStr + intervalStr + "\t   " + xAxisNumberStr;
		System.out.println(firstLineStr);
		for (int y = 0; y < boardInfo[0].length; y++) {
			StringBuilder lineStr = new StringBuilder(lineHeadStr);
			if (y < 10) {
				lineStr.append(" ");
			}
			lineStr.append(y).append("  ");
			for (int x = 0; x < boardInfo.length; x++) {
				boolean isLastPut = lastPut.getX() == x && lastPut.getY() == y;
				lineStr.append(covertPieces(boardInfo[x][y], isLastPut)).append(" ");
			}

			lineStr.append(intervalStr);

			if (y < 10) {
				lineStr.append(" ");
			}
			lineStr.append(y).append("  ");
			for (int x = 0; x < boardInfo.length; x++) {
				boolean isLastPut = lastPut.getX() == x && lastPut.getY() == y;
				if (isLastPut) {
					lineStr.append(covertPieces(boardInfo[x][y], true));
				} else if (renjuList.contains(x + "_" + y)) {
					lineStr.append(covertPieces(boardInfo[x][y], false));
				} else {
					lineStr.append(covertPieces(0, false));
				}
				lineStr.append(" ");
			}
			System.out.println(lineStr);
		}
	}

	private static String covertPieces(int color, boolean isLastPut) {
		switch (color) {
			case 0:
				return "—-";
			case 1:
				return isLastPut ? "⚫" : "⬤";
			case 2:
				return isLastPut ? "◯" : "OO";
			default:
				return "";
		}
	}
}
