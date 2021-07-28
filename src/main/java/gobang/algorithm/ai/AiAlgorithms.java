package gobang.algorithm.ai;

import gobang.bean.ColorEnum;
import gobang.bean.DirectionEnum;
import gobang.bean.Position;

import java.util.*;

import static gobang.algorithm.CommonAlgorithms.afterOffSet;
import static gobang.algorithm.CommonAlgorithms.checkEmptyChessPositionFrom;
import static gobang.algorithm.ai.PieceType.*;

/**
 * 逻辑AI
 * 通过计算每个空棋位能成的棋型来打分，最后选择分数最高的位置下棋
 *
 * @author LiuBing
 * @date 2020/10/10
 */
public class AiAlgorithms {

    private static final ThreadLocal<GamingInfo> GAMING_INFO = new ThreadLocal<>();

    private static final boolean PRINT_CALCULATION = true;

    /**
     * 计算下棋坐标
     *
     * @param boardInfo     棋盘及棋子信息
     * @param ownColor      本方棋色
     * @param opponentColor 对方棋色
     * @return null：棋盘下满
     */
    public static Position checkChessPosition(
            int[][] boardInfo, ColorEnum ownColor, ColorEnum opponentColor) {
        long a = System.currentTimeMillis();
        GAMING_INFO.set(new GamingInfo(Objects.requireNonNull(boardInfo), ownColor, opponentColor));
        Position bestPosition;
        try {
            bestPosition = findBestPosition();
        } finally {
            GAMING_INFO.remove();
        }
        System.out.println(System.currentTimeMillis() - a);
        return bestPosition;
    }

    /**
     * 找出对自己最有利的棋位坐标
     *
     * @return null：棋盘已满，nonNull：坐标
     */
    private static Position findBestPosition() {
        List<Position> emptyPositionList = checkEmptyChessPositionFrom(GAMING_INFO.get().boardInfo);

        if (emptyPositionList.isEmpty()) {
            return null;
        }

        Map<Position, Integer> positionScoreMap = calculateAllPositionScore(emptyPositionList);
        List<Map.Entry<Position, Integer>> entryList = new ArrayList<>(positionScoreMap.entrySet());
        Collections.shuffle(entryList);
        Optional<Map.Entry<Position, Integer>> maxEntryOptional = entryList
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue));
        if (!maxEntryOptional.isPresent()) {
            throw new NullPointerException("Max score position not be found.");
        }
        Position decision = maxEntryOptional.get().getKey();
        printCalculationResult(positionScoreMap, decision);
        return decision;
    }

    /**
     * 计算所有棋位得分
     *
     * @param emptyPositionList 空棋位列表
     * @return k-坐标，v-分数
     */
    private static Map<Position, Integer> calculateAllPositionScore(List<Position> emptyPositionList) {
        if (emptyPositionList.isEmpty()) {
            return Collections.emptyMap();
        }

        int initialCapacity = emptyPositionList.size() * 4 / 3 + 1;
        Map<Position, Integer> positionScoreMap = new HashMap<>(initialCapacity);
        emptyPositionList.forEach(position -> {
            List<PatternEnum>[] patternGroups = calculateAllPattern(position);
            positionScoreMap.put(position, socreFunction(patternGroups));
        });

        return positionScoreMap;
    }

    /**
     * 根据棋型计算棋位得分
     */
    private static int socreFunction(List<PatternEnum>[] patternGroups) {
        int score = patternGroups[0].stream().mapToInt(PatternEnum::getAttackScore).sum();
        return score + patternGroups[1].stream().mapToInt(PatternEnum::getDefenseScore).sum();
    }

    /**
     * 计算本家和对手下某个棋位能成的棋型
     *
     * @param position 棋位坐标
     * @return Object[0]：本家棋型列表，Object[1]：对手棋型列表
     */
    private static List<PatternEnum>[] calculateAllPattern(Position position) {
        List<PatternEnum>[] patternGroups =
                new ArrayList[]{new ArrayList<>(4), new ArrayList<>(4)};
        ColorEnum ownColor = GAMING_INFO.get().ownColor;
        ColorEnum opponentColor = GAMING_INFO.get().opponentColor;
        for (DirectionEnum direction : DirectionEnum.values()) {
            PatternEnum ownPattern = calculateBy(direction, position, ownColor, opponentColor);
            patternGroups[0].add(ownPattern);
            PatternEnum opponentPattern = calculateBy(direction, position, opponentColor, ownColor);
            patternGroups[1].add(opponentPattern);
        }
        return patternGroups;
    }

    /**
     * 计算某个方向上的棋型
     *
     * @param direction        方向
     * @param originOfPosition 原点，假设原点下了自己的棋
     * @param ownColor         本方棋色
     * @return nonNull
     */
    private static PatternEnum calculateBy(
            DirectionEnum direction, Position originOfPosition, ColorEnum ownColor, ColorEnum opponentColor) {
        int[] subjectArray = findSubjectArray(direction, originOfPosition, ownColor, opponentColor);
        return PatternEnum.check(subjectArray);
    }

    /**
     * 从原点出发，找某个方向上找相连的棋位信息，转换成能检测棋型的数组
     *
     * @param direction        方向
     * @param originOfPosition 原点，假设原点下了自己的棋
     * @param ownColor         本方棋色
     * @param opponentColor    对方棋色
     * @return 能检测棋型的数组 nonNull
     */
    private static int[] findSubjectArray(
            DirectionEnum direction, Position originOfPosition, ColorEnum ownColor, ColorEnum opponentColor) {
        int[][] boardInfo = GAMING_INFO.get().boardInfo;
        LinkedList<Integer> subjectList = new LinkedList<>();
        subjectList.add(O);

        int maxOffsetNum = 4;
        boolean toInitialPoint = true;
        boolean awayInitialPoint = true;
        for (int offsetNum = 1; (toInitialPoint || awayInitialPoint) && offsetNum <= maxOffsetNum; offsetNum++) {
            if (toInitialPoint) {
                Position positionToLeft = afterOffSet(
                        direction, true, originOfPosition, offsetNum, boardInfo);
                int chessInfoType = convertByColor(
                        positionToLeft, boardInfo, ownColor, opponentColor);
                if (chessInfoType == S) {
                    toInitialPoint = false;
                }
                subjectList.addFirst(chessInfoType);
            }

            if (awayInitialPoint) {
                Position positionToRight = afterOffSet(
                        direction, false, originOfPosition, offsetNum, boardInfo);
                int chessInfoType = convertByColor(positionToRight, boardInfo, ownColor, opponentColor);
                if (chessInfoType == S) {
                    awayInitialPoint = false;
                }
                subjectList.addLast(chessInfoType);
            }
        }
        return subjectList.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * 转换坐标棋子信息
     *
     * @param position      棋位坐标
     * @param boardInfo     棋盘信息
     * @param ownColor      本方棋色
     * @param opponentColor 对方棋色
     * @return -
     */
    private static int convertByColor(
            Position position, int[][] boardInfo, ColorEnum ownColor, ColorEnum opponentColor) {
        if (Objects.isNull(position)) {
            return S;
        }

        int colorOnPosition = boardInfo[position.getX()][position.getY()];
        if (colorOnPosition == ownColor.ordinal()) {
            return O;
        } else if (colorOnPosition == opponentColor.ordinal()) {
            return S;
        } else {
            return E;
        }
    }

    private static void printCalculationResult(Map<Position, Integer> positionScoreMap, Position decision) {
        if (!PRINT_CALCULATION) {
            return;
        }
        System.out.println("[开始]");
        positionScoreMap
                .entrySet()
                .stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .forEach(entry ->
                        System.out.println(
                                String.format("[%s, %s]", entry.getKey().getX(),
                                        entry.getKey().getY()) + "：" + entry.getValue()));
        System.out.println("最后选择：" + String.format("[%s, %s]", decision.getX(), decision.getY()));
        System.out.println("[结束]");
    }

    private AiAlgorithms() {
    }
}
