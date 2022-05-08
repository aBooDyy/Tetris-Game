package tetris.gamecomponents.gifts;

import tetris.gamecomponents.Board;
import tetris.utilities.Point;
import tetris.utilities.Properties;

public class MagicPiece extends Gift {

    public MagicPiece(Board board) {
        //TODO COLOR
        super(board, Properties.getColorScheme().getDarkBlue());
    }

    @Override
    public void giveReward() {
        for (int i = 0; i < Properties.getWidth(); i++) {
            Point point = new Point(i, Properties.getHeight()-1, Properties.getColorScheme().getDarkBlue());

            if (board.containsPoint(point)) {
                continue;
            }

            board.fillPoint(point);
            return;
        }
    }
}
