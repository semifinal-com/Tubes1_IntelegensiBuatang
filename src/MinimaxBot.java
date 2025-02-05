import javafx.scene.control.Button;

import java.time.Duration;
import java.time.Instant;

public class MinimaxBot extends Bot{
    private int alpha = -64;
    private int beta = 64;
    private static final int ROW = 8;
    private static final int COL = 8;
    private final int maxDepthCheck = 5;

    @Override
    public int[] move(int roundsLeft, boolean isBotFirst, int playerOScore, int playerXScore, Button[][] buttons, boolean isMaximizingX) {
        int maxDepth = roundsLeft * 2 - 1;

        int curDepth = 0;
        int[] maxValues;
        if (isMaximizingX) {
            maxValues = min(playerOScore, playerXScore, curDepth, maxDepth, buttons, isBotFirst, isMaximizingX);
        } else {
            maxValues = max(playerOScore, playerXScore, curDepth, maxDepth, buttons, isBotFirst, isMaximizingX);
        }

        this.alpha = -64;
        this.beta = 64;
        return new int[]{maxValues[1], maxValues[2]};
    }

    public int[] max(int playerOScore, int playerXScore, int curDepth, int maxDepth, Button[][] buttons, boolean isBotFirst, boolean isMaxingX) {
        int[] maxValues = new int[3];
        int maxVal = -64;
        int maxRow = -1;
        int maxCol = -1;

        int curPlayerOScore = playerOScore;
        int curPlayerXScore = playerXScore;

        if (curDepth == maxDepth || curDepth == maxDepthCheck) {
            maxValues[0] = curPlayerOScore - curPlayerXScore;
            maxValues[1] = maxRow;
            maxValues[2] = maxCol;
            return maxValues;
        }

        Button[][] curButtons = new Button[ROW][COL];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                curButtons[i][j] = new Button(buttons[i][j].getText()); // Create a new Button with the same properties
            }
        }

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (curButtons[i][j].getText().equals("")) {
                    curButtons[i][j].setText("O");

                    int difference = updateGameBoard(!isMaxingX, i, j, curButtons);
                    curPlayerOScore += (difference + 1);
                    curPlayerXScore -= difference;

                    int[] minValues = min(curPlayerOScore, curPlayerXScore, curDepth + 1, maxDepth, curButtons, isBotFirst, isMaxingX);

                    curPlayerOScore = playerOScore;
                    curPlayerXScore = playerXScore;
                    curButtons[i][j].setText("");

                    if (minValues[0] > maxVal) {
                        maxVal = minValues[0];
                        maxRow = i;
                        maxCol = j;
                    }
                    if (maxVal >= beta) {
                        maxValues[0] = maxVal;
                        maxValues[1] = maxRow;
                        maxValues[2] = maxCol;
                        return maxValues;
                    }
                    if (maxVal > alpha) {
                        alpha = maxVal;
                    }
                }
            }
        }
        maxValues[0] = maxVal;
        maxValues[1] = maxRow;
        maxValues[2] = maxCol;
        return maxValues;
    }

    public int[] min(int playerOScore, int playerXScore, int curDepth, int maxDepth, Button[][] buttons, boolean isBotFirst, boolean isMaxingX) {
        int[] minValues = new int[3];
        int minVal = 64;
        int minRow = -1;
        int minCol = -1;

        int curPlayerOScore = playerOScore;
        int curPlayerXScore = playerXScore;

        if (curDepth == maxDepth || curDepth == maxDepthCheck) {
            minValues[0] = curPlayerOScore - curPlayerXScore;
            minValues[1] = minRow;
            minValues[2] = minCol;
            return minValues;
        }

        Button[][] curButtons = new Button[ROW][COL];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                curButtons[i][j] = new Button(buttons[i][j].getText()); // Create a new Button with the same properties
            }
        }

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (curButtons[i][j].getText().equals("")) {
                    curButtons[i][j].setText("X");

                    int difference = updateGameBoard(isMaxingX, i, j, curButtons);
                    curPlayerOScore -= difference;
                    curPlayerXScore += (difference + 1);

                    int[] maxValues = max(curPlayerOScore, curPlayerXScore, curDepth + 1, maxDepth, curButtons, isBotFirst, isMaxingX);

                    // Cleaning for the next iteration
                    curPlayerOScore = playerOScore;
                    curPlayerXScore = playerXScore;
                    curButtons[i][j].setText("");

                    // Pruning
                    if (maxValues[0] < minVal) {
                        minVal = maxValues[0];
                        minRow = i;
                        minCol = j;
                    }
                    if (minVal <= alpha) {
                        minValues[0] = minVal;
                        minValues[1] = minRow;
                        minValues[2] = minCol;
                        return minValues;
                    }
                    if (minVal < beta) {
                        beta = minVal;
                    }
                }
            }
        }
        minValues[0] = minVal;
        minValues[1] = minRow;
        minValues[2] = minCol;
        return minValues;
    }


}