package tetris.gamecomponents.gifts;

import tetris.gamecomponents.Board;
import tetris.utilities.Properties;

import java.util.Random;

public class ScorePoint extends Gift {

    public ScorePoint(Board board) {
        super(board, Properties.getColorScheme().getYellow());
    }

    @Override
    public void giveReward() {
        Random random = new Random();
        board.increaseScore(random.nextInt(10) + 1);
    }
}
