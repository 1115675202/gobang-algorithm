package gobang.bean;

/**
 * 方向
 *
 * @author LiuBing
 * @date 2020/11/5
 */
public enum DirectionEnum {

	/**
	 * 水平
	 */
	HORIZONTAL,

	/**
	 * 垂直
	 */
	VERTICAL,

	/**
	 * 下斜向（左上角 — 右下角）
	 */
	TILT_DOWN,

	/**
	 * 上斜向（左下角 - 右上角）
	 */
	TILT_UP,
	;
}
