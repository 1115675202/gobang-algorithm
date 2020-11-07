package gobang.algorithm.ai;

import gobang.bean.ColorEnum;

/**
 * 游戏信息
 *
 * @author LiuBing
 * @date 2020/11/5
 */
class GamingInfo {

	final int[][] boardInfo;

	final ColorEnum ownColor;

	final ColorEnum opponentColor;

	public GamingInfo(int[][] boardInfo, ColorEnum ownColor, ColorEnum opponentColor) {
		this.boardInfo = boardInfo;
		this.ownColor = ownColor;
		this.opponentColor = opponentColor;
	}
}
