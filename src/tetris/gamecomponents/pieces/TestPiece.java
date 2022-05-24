package tetris.gamecomponents.pieces;

import tetris.gamecomponents.Piece;
import tetris.gamecomponents.Point;
import tetris.utilities.Properties;

public class TestPiece extends Piece {


    public TestPiece() {
        super(Properties.getColorScheme().getDarkBlue(), new Point(0,0));
    }

    @Override
    public int getID() {
        return 999;
    }
}
