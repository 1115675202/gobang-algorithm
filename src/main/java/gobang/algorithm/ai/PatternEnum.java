package gobang.algorithm.ai;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static gobang.algorithm.ai.PieceType.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.toMap;

/**
 * 棋型及得分
 *
 * @author LiuBing
 * @date 2020/11/5
 */
enum PatternEnum {

    // 连五
    LINK_5(10000000, 5000000,
            unmodifiableSet(
                    new HashSet<int[]>(1) {{
                        add(new int[]{O, O, O, O, O});
                    }})
    ),

    // 活四
    LIVE_4(1000000, 500000,
            unmodifiableSet(
                    new HashSet<int[]>(1) {{
                        add(new int[]{E, O, O, O, O, E});
                    }})
    ),

    // 冲四
    RUSH_4(10000, 5000,
            unmodifiableSet(
                    new HashSet<int[]>(3) {{
                        add(new int[]{E, O, O, O, O, S});
                        add(new int[]{O, E, O, O, O, E});
                        add(new int[]{O, O, E, O, O, E});
                    }})
    ),

    // 活三
    LIVE_3(10000, 6000,
            unmodifiableSet(
                    new HashSet<int[]>(2) {{
                        add(new int[]{E, E, O, O, O, E, E});
                        add(new int[]{E, O, E, O, O, E});
                    }})
    ),

    // 眠三
    DIE_3(1200, 700,
            unmodifiableSet(
                    new HashSet<int[]>(3) {{
                        add(new int[]{E, E, O, O, O, S});
                        add(new int[]{E, O, E, O, O, S});
                        add(new int[]{E, O, O, E, O, S});
                        add(new int[]{E, O, E, E, O, O, E});
                        add(new int[]{E, O, E, O, E, O, E});
                        add(new int[]{S, E, O, O, O, E, S});
                    }})
    ),

    // 活二
    LIVE_2(600, 300,
            unmodifiableSet(
                    new HashSet<int[]>(3) {{
                        add(new int[]{E, E, O, O, E, E});
                        add(new int[]{E, O, E, O, E, E});
                        add(new int[]{E, O, E, E, O, E});
                    }})
    ),

    // 眠二
    DIE_2(200, 40,
            unmodifiableSet(
                    new HashSet<int[]>(3) {{
                        add(new int[]{E, E, E, O, O, S});
                        add(new int[]{E, E, O, E, O, S});
                        add(new int[]{E, O, E, E, O, S});
                        add(new int[]{E, O, E, E, E, O, E});
                    }})
    ),

    // 活一
    LIVE_1(30, 20,
            unmodifiableSet(
                    new HashSet<int[]>(3) {{
                        add(new int[]{E, E, E, E, O, E, E, E, E});
                    }})
    ),

    // 眠一
    DIE_1(6, 2,
            unmodifiableSet(
                    new HashSet<int[]>(3) {{
                        add(new int[]{O, E, E, E, E});
                        add(new int[]{E, O, E, E, E});
                        add(new int[]{E, E, O, E, E});
                        add(new int[]{E, E, E, O, E});
                        add(new int[]{E, E, E, E, O});
                    }})
    ),

    // 默认
    DEFAULT(1, 0, emptySet()),
    ;

    /**
     * 进攻得分
     */
    private final int attackScore;

    /**
     * 防守得分
     */
    private final int defenseScore;

    /**
     * k-棋型，v-next数组
     */
    private final Map<int[], int[]> patternTemplateMap;

    /**
     * 检测棋型
     *
     * @param subject 某个方向上连续棋位落子信息
     * @return 棋型
     */
    static PatternEnum check(int[] subject) {
        for (PatternEnum pattern : PatternEnum.values()) {
            if (pattern.hasPatternOf(subject)) {
                return pattern;
            }
        }
        return null;
    }

    PatternEnum(int attackScore, int defenseScore, Set<int[]> patternTemplates) {
        this.attackScore = attackScore;
        this.defenseScore = defenseScore;
        this.patternTemplateMap = patternTemplateMapInit(patternTemplates);
    }

    public int getAttackScore() {
        return attackScore;
    }

    public int getDefenseScore() {
        return defenseScore;
    }

    /**
     * @return true-subject中有该棋型
     */
    public boolean hasPatternOf(int[] subject) {
        return this.patternTemplateMap.isEmpty() || this.patternTemplateMap.entrySet().stream()
                .anyMatch(entry -> findPatternIn(subject, entry.getKey(), entry.getValue()));
    }

    /**
     * KMP 找完全匹配的棋型
     *
     * @param subject 被检测棋型
     * @param pattern 棋型模板
     * @param next    棋型模板next数组
     * @return true：有
     */
    private boolean findPatternIn(int[] subject, int[] pattern, int[] next) {
        if (subject.length < pattern.length) return false;

        // 正向查找
        int j = 0;
        for (int i = 0; i < subject.length && j < pattern.length; ) {
            if (j == -1 || subject[i] == pattern[j]) {
                i++;
                j++;
            } else j = next[j];
        }
        if (j == pattern.length) return true;

        // 反向查找
        j = 0;
        for (int i = subject.length - 1; i >= 0 && j < pattern.length; ) {
            if (j == -1 || subject[i] == pattern[j]) {
                i--;
                j++;
            } else j = next[j];
        }
        return j == pattern.length;
    }

    /**
     * patternTemplateMap初始化方法
     */
    private Map<int[], int[]> patternTemplateMapInit(Set<int[]> patternTemplates) {
        if (patternTemplates.isEmpty()) return emptyMap();
        return patternTemplates.stream()
                .collect(toMap(Function.identity(), this::buildNext));
    }

    /**
     * 根据棋型模板构建 next 数组，用于查找
     */
    private int[] buildNext(int[] patternTemplate) {
        int[] next = new int[patternTemplate.length];
        next[0] = -1;
        int j = -1;
        for (int i = 0; i < patternTemplate.length - 1; ) {
            if (j == -1 || patternTemplate[j] == patternTemplate[i])
                next[++i] = ++j;
            else j = next[j];
        }
        return next;
    }
}
