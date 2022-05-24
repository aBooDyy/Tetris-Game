package tetris.gamecomponents.pieces;

import tetris.gamecomponents.Piece;
import tetris.gamecomponents.Rotatable;
import tetris.gamecomponents.Point;
import tetris.utilities.Properties;

public class TPiece extends Piece implements Rotatable {

    public TPiece() {
        super(Properties.getColorScheme().getMagenta(), new Point(1, 1),
                new Point(1, 0),
                new Point(0, 1),
                new Point(2, 1)
        );
    }

    @Override
    public int getID() {
        return 6;
    }

    @Override
    public void rotate(boolean isClockwise) {
        for (Point point : this.getPoints()) {
            if (point.equals(this.getOrigin())) {
                continue;
            }

            point.rotate(this.getOrigin(), isClockwise);
        }
    }

}
