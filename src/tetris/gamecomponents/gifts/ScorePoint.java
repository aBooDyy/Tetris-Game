package tetris.gamecomponents.gifts;

import tetris.gamecomponents.Board;
import tetris.utilities.Properties;

import java.util.Random;

public class ScorePoint extends Gift {

    private final int randomScore;

    public ScorePoint(Board board) {
        super(board, Properties.getColorScheme().getYellow());
        Random random = new Random();
        randomScore = random.nextInt(10) + 1;
    }

    @Override
    public void giveReward() {
        board.increaseScore(randomScore);
    }

    public int getRandomScore() {
        return randomScore;
    }
}
