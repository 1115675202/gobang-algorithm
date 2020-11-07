package gobang.algorithm.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static gobang.algorithm.ai.PieceType.*;

/**
 * 棋型及得分
 *
 * @author LiuBing
 * @date 2020/11/5
 */
enum PatternEnum {

	// 连五
	LINK_5(10000000, 5000000,
			Collections.unmodifiableList(
					new ArrayList<int[]>(1) {{
						add(new int[]{O, O, O, O, O});
					}})
	),

	// 活四
	LIVE_4(1000000, 500000,
			Collections.unmodifiableList(
					new ArrayList<int[]>(1) {{
						add(new int[]{_, O, O, O, O, _});
					}})
	),

	// 冲四
	RUSH_4(10000, 5000,
			Collections.unmodifiableList(
					new ArrayList<int[]>(3) {{
						add(new int[]{_, O, O, O, O, S});
						add(new int[]{O, _, O, O, O, _});
						add(new int[]{O, O, _, O, O, _});
					}})
	),

	// 活三
	LIVE_3(10000, 6000,
			Collections.unmodifiableList(
					new ArrayList<int[]>(2) {{
						add(new int[]{_, _, O, O, O, _, _});
						add(new int[]{_, O, _, O, O, _});
					}})
	),

	// 眠三
	DIE_3(1200, 700,
			Collections.unmodifiableList(
					new ArrayList<int[]>(3) {{
						add(new int[]{_, _, O, O, O, S});
						add(new int[]{_, O, _, O, O, S});
						add(new int[]{_, O, O, _, O, S});
						add(new int[]{_, O, _, _, O, O, _});
						add(new int[]{_, O, _, O, _, O, _});
						add(new int[]{S, _, O, O, O, _, S});
					}})
	),

	// 活二
	LIVE_2(600, 300,
			Collections.unmodifiableList(
					new ArrayList<int[]>(3) {{
						add(new int[]{_, _, O, O, _, _});
						add(new int[]{_, O, _, O, _, _});
						add(new int[]{_, O, _, _, O, _});
					}})
	),

	// 眠二
	DIE_2(200, 40,
			Collections.unmodifiableList(
					new ArrayList<int[]>(3) {{
						add(new int[]{_, _, _, O, O, S});
						add(new int[]{_, _, O, _, O, S});
						add(new int[]{_, O, _, _, O, S});
						add(new int[]{_, O, _, _, _, O, _});
					}})
	),

	// 活一
	LIVE_1(30, 20,
			Collections.unmodifiableList(
					new ArrayList<int[]>(3) {{
						add(new int[]{_, _, _, _, O, _, _, _, _});
					}})
	),

	// 眠一
	DIE_1(6, 2,
			Collections.unmodifiableList(
					new ArrayList<int[]>(3) {{
						add(new int[]{O, _, _, _, _});
						add(new int[]{_, O, _, _, _});
						add(new int[]{_, _, O, _, _});
						add(new int[]{_, _, _, O, _});
						add(new int[]{_, _, _, _, O});
					}})
	),

	// 默认
	DEFAULT(1, 0, Collections.emptyList()),
	;

	private final int attackScore;

	private final int defenseScore;

	private final List<int[]> patternTemplates;

	PatternEnum(int attackScore, int defenseScore, List<int[]> patternTemplates) {
		this.attackScore = attackScore;
		this.defenseScore = defenseScore;
		this.patternTemplates = patternTemplates;
	}

	public int getAttackScore() {
		return attackScore;
	}

	public int getDefenseScore() {
		return defenseScore;
	}

	public List<int[]> getPatternTemplates() {
		return patternTemplates;
	}
}
