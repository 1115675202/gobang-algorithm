package gobang.algorithm.ai;

/**
 * 棋子类型
 *
 * @author LiuBing
 * @date 2020/11/5
 */
class PieceType {

	/**
	 * 对方棋子或超出边界
	 */
	static final int S = -1;

	/**
	 * 空位
	 */
	static final int E = 0;

	/**
	 * 己方棋子
	 */
	static final int O = 1;

	private PieceType() {
	}
}
