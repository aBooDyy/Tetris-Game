package tetris.gamecomponents.pieces;

import tetris.gamecomponents.Piece;
import tetris.gamecomponents.Point;
import tetris.utilities.Properties;

public class OPiece extends Piece {

    public OPiece() {
        super(Properties.getColorScheme().getYellow(), new Point(1, 1),
                new Point(0, 0),
                new Point(1, 0),
                new Point(0, 1)
        );
    }

    @Override
    public int getID() {
        return 3;
    }

}
