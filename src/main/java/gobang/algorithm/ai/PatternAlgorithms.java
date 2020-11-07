package gobang.algorithm.ai;

import java.util.List;

/**
 * 棋型检测
 *
 * @author LiuBing
 * @date 2020/11/5
 */
class PatternAlgorithms {

	/**
	 * 检测棋型
	 *
	 * @param subject
	 * @return
	 */
	static PatternEnum check(int[] subject) {
		for (PatternEnum pattern : PatternEnum.values()) {
			if (isPatternOf(pattern, subject)) {
				return pattern;
			}
		}
		return null;
	}

	/**
	 * 判断是否指定的棋型
	 *
	 * @param pattern 棋型
	 * @param subject 被检测棋型
	 * @return true：是
	 */
	private static boolean isPatternOf(PatternEnum pattern, int[] subject) {
		List<int[]> patternTemplates = pattern.getPatternTemplates();

		if (patternTemplates.isEmpty()) {
			return true;
		}

		for (int[] patternTemplate : patternTemplates) {
			if (findPatternTemplateIn(subject, patternTemplate)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 找完全匹配的棋型
	 *
	 * @param subject         被检测棋型
	 * @param patternTemplate 棋型
	 * @return true：有
	 */
	private static boolean findPatternTemplateIn(int[] subject, int[] patternTemplate) {
		if (subject.length < patternTemplate.length) {
			return false;
		}

		for (int i = 0; i < subject.length; i++) {
			// 判断有没有越界
			int length = patternTemplate.length;
			if (i + length > subject.length) {
				return false;
			}

			boolean positiveEquals = true;
			boolean reverseEquals = true;
			for (int i1 = i, p = 0, r = length - 1; p < length; i1++, p++, r--) {
				// 与正向模板比较
				if (subject[i1] != patternTemplate[p]) {
					positiveEquals = false;
				}

				// 与反向模板比较
				if (subject[i1] != patternTemplate[r]) {
					reverseEquals = false;
				}

				if (!positiveEquals && !reverseEquals) {
					return false;
				}
			}

			if (positiveEquals || reverseEquals) {
				return true;
			}
		}

		return false;
	}

	private PatternAlgorithms() {
	}
}
